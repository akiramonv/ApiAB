package com.example.providerapiemulator.service;

import com.example.providerapiemulator.dto.AccountDto;
import com.example.providerapiemulator.dto.AccountRequest;
import com.example.providerapiemulator.dto.BankFilialDto;
import com.example.providerapiemulator.dto.CategoryServiceDto;
import com.example.providerapiemulator.dto.CategoryServiceRequest;
import com.example.providerapiemulator.dto.CommissionDto;
import com.example.providerapiemulator.dto.CommissionLevelDto;
import com.example.providerapiemulator.dto.CommissionRequest;
import com.example.providerapiemulator.dto.OrganisationDto;
import com.example.providerapiemulator.dto.OrganisationRequest;
import com.example.providerapiemulator.dto.ProviderDto;
import com.example.providerapiemulator.dto.ProviderRequest;
import com.example.providerapiemulator.dto.ProviderServiceDto;
import com.example.providerapiemulator.dto.ProviderServiceRequest;
import com.example.providerapiemulator.dto.RoleDto;
import com.example.providerapiemulator.dto.RoleRequest;
import com.example.providerapiemulator.dto.SpecializationDto;
import com.example.providerapiemulator.dto.SpecializationRequest;
import com.example.providerapiemulator.dto.UserDto;
import com.example.providerapiemulator.dto.UserRequest;
import com.example.providerapiemulator.entity.AccountTypeEntity;
import com.example.providerapiemulator.entity.BankFilialEntity;
import com.example.providerapiemulator.entity.CategoryServiceEntity;
import com.example.providerapiemulator.entity.Commission;
import com.example.providerapiemulator.entity.OrganisationEntity;
import com.example.providerapiemulator.entity.ProviderEntity;
import com.example.providerapiemulator.entity.ProviderServiceEntity;
import com.example.providerapiemulator.entity.RoleEntity;
import com.example.providerapiemulator.entity.ServiceSpecializationEntity;
import com.example.providerapiemulator.entity.SpecializationEntity;
import com.example.providerapiemulator.entity.UserEntity;
import com.example.providerapiemulator.entity.UserRoleEntity;
import com.example.providerapiemulator.entity.UserSpecializationEntity;
import com.example.providerapiemulator.exception.NotFoundException;
import com.example.providerapiemulator.model.CommissionLevelName;
import com.example.providerapiemulator.repository.AccountTypeRepository;
import com.example.providerapiemulator.repository.BankFilialRepository;
import com.example.providerapiemulator.repository.CategoryServiceRepository;
import com.example.providerapiemulator.repository.CommissionRepository;
import com.example.providerapiemulator.repository.OrganisationRepository;
import com.example.providerapiemulator.repository.ProviderRepository;
import com.example.providerapiemulator.repository.ProviderServiceRepository;
import com.example.providerapiemulator.repository.RoleRepository;
import com.example.providerapiemulator.repository.ServiceSpecializationRepository;
import com.example.providerapiemulator.repository.SpecializationRepository;
import com.example.providerapiemulator.repository.UserRepository;
import com.example.providerapiemulator.repository.UserRoleRepository;
import com.example.providerapiemulator.repository.UserSpecializationRepository;
import com.example.providerapiemulator.support.Amounts;
import com.example.providerapiemulator.support.Ids;
import com.example.providerapiemulator.support.QrCodes;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Работа со справочниками БД billing: сервисы, категории, провайдеры, счета,
 * пользователи, роли, специализации, комиссии и организации.
 *
 * Особенности схемы billing, которые тут учтены:
 * - все первичные ключи varchar (у старых записей числа, у новых - UUID строкой);
 * - суммы услуг лежат строками в сомах, разделитель может быть "." или ",";
 * - у сервисов, категорий, специализаций и пользователей есть флаг deleted,
 *   поэтому DELETE помечает запись удалённой, а не ломает внешние ключи.
 */
@Service
@Transactional
public class ProviderCatalogService {

    private final ProviderServiceRepository serviceRepository;
    private final CategoryServiceRepository categoryRepository;
    private final ProviderRepository providerRepository;
    private final SpecializationRepository specializationRepository;
    private final UserRepository userRepository;
    private final UserSpecializationRepository userSpecializationRepository;
    private final ServiceSpecializationRepository serviceSpecializationRepository;
    private final AccountTypeRepository accountRepository;
    private final CommissionRepository commissionRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final OrganisationRepository organisationRepository;
    private final BankFilialRepository bankFilialRepository;
    private final LookupFactory lookupFactory;

    public ProviderCatalogService(ProviderServiceRepository serviceRepository,
                                  CategoryServiceRepository categoryRepository,
                                  ProviderRepository providerRepository,
                                  SpecializationRepository specializationRepository,
                                  UserRepository userRepository,
                                  UserSpecializationRepository userSpecializationRepository,
                                  ServiceSpecializationRepository serviceSpecializationRepository,
                                  AccountTypeRepository accountRepository,
                                  CommissionRepository commissionRepository,
                                  RoleRepository roleRepository,
                                  UserRoleRepository userRoleRepository,
                                  OrganisationRepository organisationRepository,
                                  BankFilialRepository bankFilialRepository,
                                  LookupFactory lookupFactory) {
        this.serviceRepository = serviceRepository;
        this.categoryRepository = categoryRepository;
        this.providerRepository = providerRepository;
        this.specializationRepository = specializationRepository;
        this.userRepository = userRepository;
        this.userSpecializationRepository = userSpecializationRepository;
        this.serviceSpecializationRepository = serviceSpecializationRepository;
        this.accountRepository = accountRepository;
        this.commissionRepository = commissionRepository;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
        this.organisationRepository = organisationRepository;
        this.bankFilialRepository = bankFilialRepository;
        this.lookupFactory = lookupFactory;
    }

    // ------------------------------------------------------------------ услуги

    @Transactional(readOnly = true)
    public List<ProviderServiceDto> getAllServices(boolean includeDeleted) {
        return toServiceDtos(filterDeleted(serviceRepository.findAll(), ProviderServiceEntity::getDeleted, includeDeleted));
    }

    @Transactional(readOnly = true)
    public ProviderServiceDto getService(String id) {
        return toServiceDtos(List.of(requireService(id))).get(0);
    }

    @Transactional(readOnly = true)
    public List<ProviderServiceDto> getServicesByCategory(String categoryId) {
        return toServiceDtos(filterDeleted(serviceRepository.findByCategoryId(categoryId),
                ProviderServiceEntity::getDeleted, false));
    }

    @Transactional(readOnly = true)
    public List<ProviderServiceDto> getServicesByProvider(String providerId) {
        return toServiceDtos(filterDeleted(serviceRepository.findByProviderId(providerId),
                ProviderServiceEntity::getDeleted, false));
    }

    @Transactional(readOnly = true)
    public List<ProviderServiceDto> getServicesByAccount(String accountId) {
        return toServiceDtos(filterDeleted(serviceRepository.findByAccountTypeId(accountId),
                ProviderServiceEntity::getDeleted, false));
    }

    /** Услуги, доступные пользователю по его специализациям. */
    @Transactional(readOnly = true)
    public List<ProviderServiceDto> getServicesByUserSpecialization(String userId) {
        requireUser(userId);
        Set<String> specializationIds = userSpecializationRepository.findByUserId(userId).stream()
                .map(UserSpecializationEntity::getSpecializationId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        if (specializationIds.isEmpty()) {
            return List.of();
        }
        Set<String> serviceIds = serviceSpecializationRepository.findBySpecializationIdIn(specializationIds).stream()
                .map(ServiceSpecializationEntity::getServiceId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        if (serviceIds.isEmpty()) {
            return List.of();
        }
        return toServiceDtos(filterDeleted(serviceRepository.findAllById(serviceIds),
                ProviderServiceEntity::getDeleted, false));
    }

    public ProviderServiceDto createService(ProviderServiceRequest dto) {
        ProviderServiceEntity entity = new ProviderServiceEntity();
        entity.setId(Ids.newId());
        entity.setDeleted(false);
        applyService(entity, dto);
        ProviderServiceEntity saved = serviceRepository.save(entity);
        syncServiceSpecializations(saved.getId(), dto.specializationIds());
        return getService(saved.getId());
    }

    public ProviderServiceDto updateService(String id, ProviderServiceRequest dto) {
        ProviderServiceEntity entity = requireService(id);
        applyService(entity, dto);
        serviceRepository.save(entity);
        syncServiceSpecializations(id, dto.specializationIds());
        return getService(id);
    }

    /** Мягкое удаление: на provider_services ссылается payment_services. */
    public void deleteService(String id) {
        ProviderServiceEntity entity = requireService(id);
        entity.setDeleted(true);
        serviceRepository.save(entity);
    }

    @Transactional(readOnly = true)
    public String getServiceQrCode(String id) {
        ProviderServiceEntity entity = requireService(id);
        String stored = QrCodes.stripDataUri(entity.getQrCodeBase64());
        if (stored != null && !stored.isBlank()) {
            return stored;
        }
        String link = Ids.trimToNull(entity.getQrLink());
        if (link == null) {
            throw new NotFoundException("QR link not found for service: " + id);
        }
        return QrCodes.generateBase64(link);
    }

    private void applyService(ProviderServiceEntity entity, ProviderServiceRequest dto) {
        requireProvider(dto.providerId());
        if (!Ids.isBlank(dto.categoryId()) && !categoryRepository.existsById(dto.categoryId())) {
            throw new NotFoundException("Category not found: " + dto.categoryId());
        }
        if (!Ids.isBlank(dto.accountTypeId()) && !accountRepository.existsById(dto.accountTypeId())) {
            throw new NotFoundException("Account not found: " + dto.accountTypeId());
        }
        if (!Ids.isBlank(dto.commissionId()) && !commissionRepository.existsById(dto.commissionId())) {
            throw new NotFoundException("Commission not found: " + dto.commissionId());
        }

        entity.setName(dto.name());
        entity.setProviderId(dto.providerId());
        entity.setCode(dto.code());
        entity.setCheckSource(Ids.trimToNull(dto.checkSource()));
        entity.setCategoryId(Ids.trimToNull(dto.categoryId()));
        entity.setAccountTypeId(Ids.trimToNull(dto.accountTypeId()));
        entity.setCommissionId(Ids.trimToNull(dto.commissionId()));

        BigDecimal price = Amounts.zeroIfNull(dto.price());
        entity.setSumm(Amounts.formatSom(price));
        entity.setServicePrice(Amounts.formatSom(price));
        entity.setTotalSumm(Amounts.formatSom(price));

        entity.setIncrease(dto.increase() != null ? dto.increase() : Boolean.FALSE);
        entity.setIncreaseText(dto.increaseText());
        entity.setServiceType(dto.serviceType());
        entity.setServiceAccount(dto.serviceAccount());
        entity.setServiceIdPl(dto.serviceIdPl());
        entity.setQrLink(dto.qrLink());
        if (dto.deleted() != null) {
            entity.setDeleted(dto.deleted());
        }
    }

    private List<ProviderServiceDto> toServiceDtos(List<ProviderServiceEntity> rows) {
        if (rows.isEmpty()) {
            return List.of();
        }
        Lookups lookups = lookupFactory.load();
        Map<String, List<String>> specializations = specializationNamesByService(ids(rows), lookups);
        return rows.stream()
                .map(row -> toDto(row, lookups, specializations.getOrDefault(row.getId(), List.of())))
                .toList();
    }

    private ProviderServiceDto toDto(ProviderServiceEntity entity, Lookups lookups, List<String> specializations) {
        return new ProviderServiceDto(
                entity.getId(),
                entity.getName(),
                entity.getCode(),
                Amounts.parseSom(entity.getSumm()),
                Amounts.parseSom(entity.getServicePrice()),
                Amounts.parseSom(entity.getTotalSumm()),
                entity.getCheckSource(),
                entity.getCategoryId(),
                lookups.categoryName(entity.getCategoryId()),
                entity.getProviderId(),
                lookups.providerName(entity.getProviderId()),
                entity.getAccountTypeId(),
                lookups.accountName(entity.getAccountTypeId()),
                entity.getCommissionId(),
                lookups.commissionDetails(entity.getCommissionId()),
                entity.getIncrease(),
                entity.getIncreaseText(),
                entity.getServiceType(),
                entity.getServiceAccount(),
                entity.getDeleted(),
                entity.getServiceIdPl(),
                entity.getQrLink(),
                entity.getQrCodeBase64() != null && !entity.getQrCodeBase64().isBlank(),
                specializations
        );
    }

    private Map<String, List<String>> specializationNamesByService(Collection<String> serviceIds, Lookups lookups) {
        Map<String, List<String>> result = new HashMap<>();
        for (ServiceSpecializationEntity link : serviceSpecializationRepository.findByServiceIdIn(serviceIds)) {
            String name = lookups.specializationName(link.getSpecializationId());
            if (name != null) {
                result.computeIfAbsent(link.getServiceId(), key -> new ArrayList<>()).add(name);
            }
        }
        return result;
    }

    private void syncServiceSpecializations(String serviceId, List<String> specializationIds) {
        if (specializationIds == null) {
            return;
        }
        serviceSpecializationRepository.deleteAll(serviceSpecializationRepository.findByServiceId(serviceId));
        serviceSpecializationRepository.flush();
        for (String specializationId : distinct(specializationIds)) {
            if (!specializationRepository.existsById(specializationId)) {
                throw new NotFoundException("Specialization not found: " + specializationId);
            }
            ServiceSpecializationEntity link = new ServiceSpecializationEntity();
            link.setId(Ids.newId());
            link.setServiceId(serviceId);
            link.setSpecializationId(specializationId);
            serviceSpecializationRepository.save(link);
        }
    }

    // -------------------------------------------------------------- категории

    @Transactional(readOnly = true)
    public List<CategoryServiceDto> getAllCategories(boolean includeDeleted) {
        return toCategoryDtos(filterDeleted(categoryRepository.findAll(), CategoryServiceEntity::getDeleted, includeDeleted));
    }

    @Transactional(readOnly = true)
    public CategoryServiceDto getCategory(String id) {
        return toCategoryDtos(List.of(requireCategory(id))).get(0);
    }

    @Transactional(readOnly = true)
    public List<CategoryServiceDto> getCategoriesByProvider(String providerId) {
        return toCategoryDtos(filterDeleted(categoryRepository.findByProviderId(providerId),
                CategoryServiceEntity::getDeleted, false));
    }

    @Transactional(readOnly = true)
    public List<CategoryServiceDto> getCategoriesByParent(String parentId) {
        return toCategoryDtos(filterDeleted(categoryRepository.findByParentCategory(parentId),
                CategoryServiceEntity::getDeleted, false));
    }

    public CategoryServiceDto createCategory(CategoryServiceRequest dto) {
        CategoryServiceEntity entity = new CategoryServiceEntity();
        entity.setId(Ids.newId());
        entity.setDeleted(false);
        applyCategory(entity, dto);
        return getCategory(categoryRepository.save(entity).getId());
    }

    public CategoryServiceDto updateCategory(String id, CategoryServiceRequest dto) {
        CategoryServiceEntity entity = requireCategory(id);
        applyCategory(entity, dto);
        categoryRepository.save(entity);
        return getCategory(id);
    }

    public void deleteCategory(String id) {
        CategoryServiceEntity entity = requireCategory(id);
        entity.setDeleted(true);
        categoryRepository.save(entity);
    }

    private void applyCategory(CategoryServiceEntity entity, CategoryServiceRequest dto) {
        requireProvider(dto.providerId());
        if (!Ids.isBlank(dto.parentCategoryId())) {
            if (dto.parentCategoryId().equals(entity.getId())) {
                throw new IllegalArgumentException("Category cannot be its own parent");
            }
            requireCategory(dto.parentCategoryId());
        }
        entity.setName(dto.name());
        entity.setParentCategory(Ids.trimToNull(dto.parentCategoryId()));
        entity.setIcon(dto.icon());
        entity.setProviderId(dto.providerId());
        if (dto.deleted() != null) {
            entity.setDeleted(dto.deleted());
        }
    }

    private List<CategoryServiceDto> toCategoryDtos(List<CategoryServiceEntity> rows) {
        if (rows.isEmpty()) {
            return List.of();
        }
        Lookups lookups = lookupFactory.load();
        return rows.stream()
                .map(row -> new CategoryServiceDto(
                        row.getId(),
                        row.getName(),
                        row.getParentCategory(),
                        lookups.categoryName(row.getParentCategory()),
                        row.getIcon(),
                        row.getDeleted(),
                        row.getProviderId(),
                        lookups.providerName(row.getProviderId())))
                .toList();
    }

    // -------------------------------------------------------------- провайдеры

    @Transactional(readOnly = true)
    public List<ProviderDto> getAllProviders() {
        return toProviderDtos(providerRepository.findAll());
    }

    @Transactional(readOnly = true)
    public ProviderDto getProvider(String id) {
        return toProviderDtos(List.of(requireProvider(id))).get(0);
    }

    @Transactional(readOnly = true)
    public List<ProviderDto> getProvidersByOrganisation(String organisationId) {
        return toProviderDtos(providerRepository.findByOrganisationId(organisationId));
    }

    public ProviderDto createProvider(ProviderRequest dto) {
        ProviderEntity entity = new ProviderEntity();
        entity.setId(Ids.newId());
        applyProvider(entity, dto);
        return getProvider(providerRepository.save(entity).getId());
    }

    public ProviderDto updateProvider(String id, ProviderRequest dto) {
        ProviderEntity entity = requireProvider(id);
        applyProvider(entity, dto);
        providerRepository.save(entity);
        return getProvider(id);
    }

    public void deleteProvider(String id) {
        requireProvider(id);
        providerRepository.deleteById(id);
    }

    private void applyProvider(ProviderEntity entity, ProviderRequest dto) {
        if (!Ids.isBlank(dto.commissionId()) && !commissionRepository.existsById(dto.commissionId())) {
            throw new NotFoundException("Commission not found: " + dto.commissionId());
        }
        if (!Ids.isBlank(dto.organisationId()) && !organisationRepository.existsById(dto.organisationId())) {
            throw new NotFoundException("Organisation not found: " + dto.organisationId());
        }
        if (!Ids.isBlank(dto.bankFilialId()) && !bankFilialRepository.existsById(dto.bankFilialId())) {
            throw new NotFoundException("Bank filial not found: " + dto.bankFilialId());
        }

        entity.setName(dto.name());
        entity.setAccount(dto.account());
        entity.setCode(dto.code());
        entity.setInn(dto.inn());
        if (dto.password() != null) {
            entity.setPassword(dto.password());
        }
        entity.setCommissionLevel(dto.commissionLevel() == null ? null : dto.commissionLevel().name());
        entity.setCommissionId(Ids.trimToNull(dto.commissionId()));
        entity.setOrganisationId(Ids.trimToNull(dto.organisationId()));
        entity.setAddress(dto.address());
        entity.setPhoneNumber(dto.phoneNumber());
        entity.setPostalCode(dto.postalCode());
        entity.setDirector(dto.director());
        entity.setChiefAccountant(dto.chiefAccountant());
        entity.setBik(dto.bik());
        entity.setGni(dto.gni());
        entity.setOkpo(dto.okpo());
        entity.setBankAddress(dto.bankAddress());
        entity.setBankFilialId(Ids.trimToNull(dto.bankFilialId()));
        entity.setLevelIdPl(dto.levelIdPl());
        entity.setPaycodeManual(dto.paycodeManual());
        entity.setFioInnHide(dto.fioInnHide());
        entity.setDisableStatQr(dto.disableStatQr());
        entity.setRequireInnForPayment(dto.requireInnForPayment());
    }

    private List<ProviderDto> toProviderDtos(List<ProviderEntity> rows) {
        if (rows.isEmpty()) {
            return List.of();
        }
        Lookups lookups = lookupFactory.load();
        return rows.stream()
                .map(row -> new ProviderDto(
                        row.getId(),
                        row.getName(),
                        row.getAccount(),
                        row.getCode(),
                        row.getInn(),
                        row.getCommissionLevel(),
                        row.getCommissionId(),
                        lookups.commissionDetails(row.getCommissionId()),
                        row.getOrganisationId(),
                        lookups.organisationName(row.getOrganisationId()),
                        row.getAddress(),
                        row.getPhoneNumber(),
                        row.getPostalCode(),
                        row.getDirector(),
                        row.getChiefAccountant(),
                        row.getBik(),
                        row.getGni(),
                        row.getOkpo(),
                        row.getBankAddress(),
                        row.getBankFilialId(),
                        lookups.bankFilialName(row.getBankFilialId()),
                        row.getLevelIdPl(),
                        row.getPaycodeManual(),
                        row.getFioInnHide(),
                        row.getDisableStatQr(),
                        row.getRequireInnForPayment()))
                .toList();
    }

    // ---------------------------------------------------------- специализации

    @Transactional(readOnly = true)
    public List<SpecializationDto> getAllSpecializations(boolean includeDeleted) {
        return toSpecializationDtos(filterDeleted(specializationRepository.findAll(),
                SpecializationEntity::getDeleted, includeDeleted));
    }

    @Transactional(readOnly = true)
    public SpecializationDto getSpecialization(String id) {
        return toSpecializationDtos(List.of(requireSpecialization(id))).get(0);
    }

    @Transactional(readOnly = true)
    public List<SpecializationDto> getSpecializationsByProvider(String providerId) {
        return toSpecializationDtos(filterDeleted(specializationRepository.findByProviderId(providerId),
                SpecializationEntity::getDeleted, false));
    }

    public SpecializationDto createSpecialization(SpecializationRequest dto) {
        SpecializationEntity entity = new SpecializationEntity();
        entity.setId(Ids.newId());
        entity.setDeleted(false);
        applySpecialization(entity, dto);
        return getSpecialization(specializationRepository.save(entity).getId());
    }

    public SpecializationDto updateSpecialization(String id, SpecializationRequest dto) {
        SpecializationEntity entity = requireSpecialization(id);
        applySpecialization(entity, dto);
        specializationRepository.save(entity);
        return getSpecialization(id);
    }

    public void deleteSpecialization(String id) {
        SpecializationEntity entity = requireSpecialization(id);
        entity.setDeleted(true);
        specializationRepository.save(entity);
    }

    private void applySpecialization(SpecializationEntity entity, SpecializationRequest dto) {
        requireProvider(dto.providerId());
        entity.setName(dto.name());
        entity.setProviderId(dto.providerId());
        if (dto.deleted() != null) {
            entity.setDeleted(dto.deleted());
        }
    }

    private List<SpecializationDto> toSpecializationDtos(List<SpecializationEntity> rows) {
        if (rows.isEmpty()) {
            return List.of();
        }
        Lookups lookups = lookupFactory.load();
        return rows.stream()
                .map(row -> new SpecializationDto(
                        row.getId(),
                        row.getName(),
                        row.getDeleted(),
                        row.getProviderId(),
                        lookups.providerName(row.getProviderId())))
                .toList();
    }

    // ------------------------------------------------------------ пользователи

    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers(boolean includeDeleted) {
        return toUserDtos(filterDeleted(userRepository.findAll(), UserEntity::getDeleted, includeDeleted));
    }

    @Transactional(readOnly = true)
    public UserDto getUser(String id) {
        return toUserDtos(List.of(requireUser(id))).get(0);
    }

    @Transactional(readOnly = true)
    public List<UserDto> getUsersByProvider(String providerId) {
        return toUserDtos(filterDeleted(userRepository.findByProviderId(providerId), UserEntity::getDeleted, false));
    }

    @Transactional(readOnly = true)
    public List<UserDto> getUsersBySpecialization(String specializationId) {
        requireSpecialization(specializationId);
        Set<String> userIds = userSpecializationRepository.findBySpecializationId(specializationId).stream()
                .map(UserSpecializationEntity::getUserId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        if (userIds.isEmpty()) {
            return List.of();
        }
        return toUserDtos(filterDeleted(userRepository.findAllById(userIds), UserEntity::getDeleted, false));
    }

    public UserDto createUser(UserRequest dto) {
        UserEntity entity = new UserEntity();
        entity.setId(Ids.newId());
        entity.setDeleted(false);
        entity.setFailedLoginAttempts(0);
        entity.setMustChangePassword(false);
        applyUser(entity, dto);
        UserEntity saved = userRepository.save(entity);
        syncUserRoles(saved.getId(), dto.roleIds());
        syncUserSpecializations(saved.getId(), dto.specializationIds());
        return getUser(saved.getId());
    }

    public UserDto updateUser(String id, UserRequest dto) {
        UserEntity entity = requireUser(id);
        applyUser(entity, dto);
        userRepository.save(entity);
        syncUserRoles(id, dto.roleIds());
        syncUserSpecializations(id, dto.specializationIds());
        return getUser(id);
    }

    public void deleteUser(String id) {
        UserEntity entity = requireUser(id);
        entity.setDeleted(true);
        userRepository.save(entity);
    }

    private void applyUser(UserEntity entity, UserRequest dto) {
        if (!Ids.isBlank(dto.providerId())) {
            requireProvider(dto.providerId());
        }
        entity.setName(dto.name());
        entity.setEmail(dto.email());
        entity.setPassword(dto.password());
        entity.setProviderId(Ids.trimToNull(dto.providerId()));
        if (dto.deleted() != null) {
            entity.setDeleted(dto.deleted());
        }
        if (dto.mustChangePassword() != null) {
            entity.setMustChangePassword(dto.mustChangePassword());
        }
        if (entity.getFailedLoginAttempts() == null) {
            entity.setFailedLoginAttempts(0);
        }
        if (entity.getMustChangePassword() == null) {
            entity.setMustChangePassword(false);
        }
    }

    private List<UserDto> toUserDtos(List<UserEntity> rows) {
        if (rows.isEmpty()) {
            return List.of();
        }
        Lookups lookups = lookupFactory.load();
        Collection<String> userIds = ids(rows);

        Map<String, List<String>> roles = new HashMap<>();
        for (UserRoleEntity link : userRoleRepository.findByUserIdIn(userIds)) {
            String name = lookups.roleName(link.getRoleId());
            if (name != null) {
                roles.computeIfAbsent(link.getUserId(), key -> new ArrayList<>()).add(name);
            }
        }

        Map<String, List<String>> specializations = new HashMap<>();
        for (UserSpecializationEntity link : userSpecializationRepository.findByUserIdIn(userIds)) {
            String name = lookups.specializationName(link.getSpecializationId());
            if (name != null) {
                specializations.computeIfAbsent(link.getUserId(), key -> new ArrayList<>()).add(name);
            }
        }

        return rows.stream()
                .map(row -> new UserDto(
                        row.getId(),
                        row.getName(),
                        row.getEmail(),
                        row.getPassword(),
                        row.getProviderId(),
                        lookups.providerName(row.getProviderId()),
                        row.getDeleted(),
                        row.getFailedLoginAttempts(),
                        row.getLockoutEnd(),
                        row.getMustChangePassword(),
                        roles.getOrDefault(row.getId(), List.of()),
                        specializations.getOrDefault(row.getId(), List.of())))
                .toList();
    }

    private void syncUserRoles(String userId, List<String> roleIds) {
        if (roleIds == null) {
            return;
        }
        userRoleRepository.deleteAll(userRoleRepository.findByUserId(userId));
        userRoleRepository.flush();
        for (String roleId : distinct(roleIds)) {
            if (!roleRepository.existsById(roleId)) {
                throw new NotFoundException("Role not found: " + roleId);
            }
            UserRoleEntity link = new UserRoleEntity();
            link.setId(Ids.newId());
            link.setUserId(userId);
            link.setRoleId(roleId);
            userRoleRepository.save(link);
        }
    }

    private void syncUserSpecializations(String userId, List<String> specializationIds) {
        if (specializationIds == null) {
            return;
        }
        userSpecializationRepository.deleteAll(userSpecializationRepository.findByUserId(userId));
        userSpecializationRepository.flush();
        for (String specializationId : distinct(specializationIds)) {
            if (!specializationRepository.existsById(specializationId)) {
                throw new NotFoundException("Specialization not found: " + specializationId);
            }
            UserSpecializationEntity link = new UserSpecializationEntity();
            link.setId(Ids.newId());
            link.setUserId(userId);
            link.setSpecializationId(specializationId);
            userSpecializationRepository.save(link);
        }
    }

    // ------------------------------------------------------------------- роли

    @Transactional(readOnly = true)
    public List<RoleDto> getAllRoles() {
        return roleRepository.findAll().stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public RoleDto getRole(String id) {
        return toDto(requireRole(id));
    }

    @Transactional(readOnly = true)
    public List<RoleDto> getRolesByUser(String userId) {
        requireUser(userId);
        Set<String> roleIds = userRoleRepository.findByUserId(userId).stream()
                .map(UserRoleEntity::getRoleId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        if (roleIds.isEmpty()) {
            return List.of();
        }
        return roleRepository.findAllById(roleIds).stream().map(this::toDto).toList();
    }

    public RoleDto createRole(RoleRequest dto) {
        RoleEntity entity = new RoleEntity();
        entity.setId(Ids.newId());
        entity.setName(dto.name());
        return toDto(roleRepository.save(entity));
    }

    public RoleDto updateRole(String id, RoleRequest dto) {
        RoleEntity entity = requireRole(id);
        entity.setName(dto.name());
        return toDto(roleRepository.save(entity));
    }

    public void deleteRole(String id) {
        requireRole(id);
        roleRepository.deleteById(id);
    }

    private RoleDto toDto(RoleEntity entity) {
        return new RoleDto(entity.getId(), entity.getName());
    }

    // ----------------------------------------------------------------- счета

    @Transactional(readOnly = true)
    public List<AccountDto> getAllAccounts() {
        return toAccountDtos(accountRepository.findAll());
    }

    @Transactional(readOnly = true)
    public AccountDto getAccount(String id) {
        return toAccountDtos(List.of(requireAccount(id))).get(0);
    }

    @Transactional(readOnly = true)
    public List<AccountDto> getAccountsByProvider(String providerId) {
        return toAccountDtos(accountRepository.findByProviderId(providerId));
    }

    public AccountDto createAccount(AccountRequest dto) {
        AccountTypeEntity entity = new AccountTypeEntity();
        entity.setId(Ids.newId());
        applyAccount(entity, dto);
        return getAccount(accountRepository.save(entity).getId());
    }

    public AccountDto updateAccount(String id, AccountRequest dto) {
        AccountTypeEntity entity = requireAccount(id);
        applyAccount(entity, dto);
        accountRepository.save(entity);
        return getAccount(id);
    }

    public void deleteAccount(String id) {
        requireAccount(id);
        accountRepository.deleteById(id);
    }

    private void applyAccount(AccountTypeEntity entity, AccountRequest dto) {
        ProviderEntity provider = requireProvider(dto.providerId());
        if (!Ids.isBlank(dto.commissionId()) && !commissionRepository.existsById(dto.commissionId())) {
            throw new NotFoundException("Commission not found: " + dto.commissionId());
        }
        entity.setName(dto.name());
        entity.setPaymentCode(dto.paymentCode());
        entity.setServiceIdPl(dto.serviceIdPl());
        entity.setIsManualSum(dto.isManualSum());
        entity.setProviderId(provider.getId());
        entity.setProviderName(provider.getName());
        entity.setCommissionId(Ids.trimToNull(dto.commissionId()));
        entity.setEnableMultiSelectServices(dto.enableMultiSelectServices());
        entity.setMccCode(dto.mccCode());
        entity.setQrInteractionModel(dto.qrInteractionModel());
    }

    private List<AccountDto> toAccountDtos(List<AccountTypeEntity> rows) {
        if (rows.isEmpty()) {
            return List.of();
        }
        Lookups lookups = lookupFactory.load();
        return rows.stream()
                .map(row -> new AccountDto(
                        row.getId(),
                        row.getName(),
                        row.getPaymentCode(),
                        row.getServiceIdPl(),
                        row.getIsManualSum(),
                        row.getProviderId(),
                        row.getProviderName() != null ? row.getProviderName() : lookups.providerName(row.getProviderId()),
                        row.getCommissionId(),
                        lookups.commissionDetails(row.getCommissionId()),
                        row.getEnableMultiSelectServices(),
                        row.getMccCode(),
                        row.getQrInteractionModel()))
                .toList();
    }

    // ------------------------------------------------------------- комиссии

    @Transactional(readOnly = true)
    public List<CommissionDto> getAllCommissions() {
        return commissionRepository.findAll().stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public CommissionDto getCommission(String id) {
        return toDto(requireCommission(id));
    }

    public CommissionDto createCommission(CommissionRequest dto) {
        Commission entity = new Commission();
        entity.setId(Ids.newId());
        entity.setName(dto.commissionType().name());
        entity.setValue(dto.commissionValue().stripTrailingZeros().toPlainString());
        return toDto(commissionRepository.save(entity));
    }

    public CommissionDto updateCommission(String id, CommissionRequest dto) {
        Commission entity = requireCommission(id);
        entity.setName(dto.commissionType().name());
        entity.setValue(dto.commissionValue().stripTrailingZeros().toPlainString());
        return toDto(commissionRepository.save(entity));
    }

    public void deleteCommission(String id) {
        requireCommission(id);
        commissionRepository.deleteById(id);
    }

    private CommissionDto toDto(Commission entity) {
        return new CommissionDto(entity.getId(), Lookups.commissionType(entity), Lookups.commissionValue(entity));
    }

    /** Отдельной таблицы уровней комиссии в billing нет - это перечисление. */
    @Transactional(readOnly = true)
    public List<CommissionLevelDto> getAllCommissionLevels() {
        return Arrays.stream(CommissionLevelName.values())
                .map(level -> new CommissionLevelDto(level, commissionLevelDescription(level)))
                .toList();
    }

    @Transactional(readOnly = true)
    public CommissionLevelDto getCommissionLevel(String name) {
        return Arrays.stream(CommissionLevelName.values())
                .filter(level -> level.name().equalsIgnoreCase(name))
                .findFirst()
                .map(level -> new CommissionLevelDto(level, commissionLevelDescription(level)))
                .orElseThrow(() -> new NotFoundException("Commission level not found: " + name));
    }

    private String commissionLevelDescription(CommissionLevelName level) {
        return switch (level) {
            case ProviderLevel -> "Комиссия берётся из providers.commission";
            case AccountLevel -> "Комиссия берётся из account_type.CommissionId";
            case ServiceLevel -> "Комиссия берётся из provider_services.commission";
        };
    }

    // ---------------------------------------------------------- организации

    @Transactional(readOnly = true)
    public List<OrganisationDto> getAllOrganisations() {
        return organisationRepository.findAll().stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public OrganisationDto getOrganisation(String id) {
        return toDto(requireOrganisation(id));
    }

    public OrganisationDto createOrganisation(OrganisationRequest dto) {
        OrganisationEntity entity = new OrganisationEntity();
        entity.setId(Ids.newId());
        entity.setCreatedDate(LocalDateTime.now());
        applyOrganisation(entity, dto);
        return toDto(organisationRepository.save(entity));
    }

    public OrganisationDto updateOrganisation(String id, OrganisationRequest dto) {
        OrganisationEntity entity = requireOrganisation(id);
        applyOrganisation(entity, dto);
        return toDto(organisationRepository.save(entity));
    }

    public void deleteOrganisation(String id) {
        requireOrganisation(id);
        organisationRepository.deleteById(id);
    }

    private void applyOrganisation(OrganisationEntity entity, OrganisationRequest dto) {
        entity.setName(dto.name());
        entity.setShortName(dto.shortName());
        entity.setDescription(dto.description());
        entity.setIsActive(dto.isActive());
        entity.setDateOfSigning(dto.dateOfSigning());
        entity.setTypeOfQr(dto.typeOfQr());
        entity.setTypeOfOrganisation(dto.typeOfOrganisation());
        entity.setCurrentStatus(dto.currentStatus());
        entity.setLowerCommission(dto.lowerCommission() == null ? null : Amounts.formatSom(dto.lowerCommission()));
        entity.setTypeLowerCommission(dto.typeLowerCommission() == null ? null : dto.typeLowerCommission().name());
    }

    private OrganisationDto toDto(OrganisationEntity entity) {
        return new OrganisationDto(
                entity.getId(),
                entity.getName(),
                entity.getShortName(),
                entity.getDescription(),
                entity.getIsActive(),
                entity.getCreatedDate(),
                entity.getDateOfSigning(),
                entity.getTypeOfQr(),
                entity.getTypeOfOrganisation(),
                entity.getCurrentStatus(),
                Amounts.parseSom(entity.getLowerCommission()),
                entity.getTypeLowerCommission());
    }

    // ------------------------------------------------------- филиалы банка

    @Transactional(readOnly = true)
    public List<BankFilialDto> getAllBankFilials() {
        return bankFilialRepository.findAll().stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public BankFilialDto getBankFilial(String id) {
        return toDto(bankFilialRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Bank filial not found: " + id)));
    }

    private BankFilialDto toDto(BankFilialEntity entity) {
        return new BankFilialDto(
                entity.getId(),
                entity.getFilialNumber(),
                entity.getName(),
                entity.getAddress(),
                entity.getRegion(),
                entity.getResponsibleManager(),
                entity.getPhoneNumber());
    }

    // ------------------------------------------------------------- служебное

    @Transactional(readOnly = true)
    public ProviderServiceEntity requireService(String id) {
        return serviceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Service not found: " + id));
    }

    @Transactional(readOnly = true)
    public ProviderEntity requireProvider(String id) {
        return providerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Provider not found: " + id));
    }

    @Transactional(readOnly = true)
    public UserEntity requireUser(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found: " + id));
    }

    private CategoryServiceEntity requireCategory(String id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found: " + id));
    }

    private SpecializationEntity requireSpecialization(String id) {
        return specializationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Specialization not found: " + id));
    }

    private RoleEntity requireRole(String id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Role not found: " + id));
    }

    private AccountTypeEntity requireAccount(String id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Account not found: " + id));
    }

    private Commission requireCommission(String id) {
        return commissionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Commission not found: " + id));
    }

    private OrganisationEntity requireOrganisation(String id) {
        return organisationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Organisation not found: " + id));
    }

    private <T> List<T> filterDeleted(List<T> rows,
                                      java.util.function.Function<T, Boolean> deletedFlag,
                                      boolean includeDeleted) {
        if (includeDeleted) {
            return rows;
        }
        return rows.stream().filter(row -> !Boolean.TRUE.equals(deletedFlag.apply(row))).toList();
    }

    private List<String> distinct(List<String> values) {
        return new ArrayList<>(new LinkedHashSet<>(values.stream()
                .filter(Objects::nonNull)
                .filter(value -> !value.isBlank())
                .toList()));
    }

    private <T extends com.example.providerapiemulator.entity.BaseEntity> Collection<String> ids(List<T> rows) {
        return rows.stream().map(com.example.providerapiemulator.entity.BaseEntity::getId).filter(Objects::nonNull).toList();
    }
}
