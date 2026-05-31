package com.example.providerapiemulator.service;

import com.example.providerapiemulator.dto.*;
import com.example.providerapiemulator.entity.*;
import com.example.providerapiemulator.exception.NotFoundException;
import com.example.providerapiemulator.model.*;
import com.example.providerapiemulator.repository.*;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProviderCatalogService {

    private final ProviderServiceRepository providerServiceRepository;
    private final CategoryServiceRepository categoryServiceRepository;
    private final ProviderRepository providerRepository;
    private final SpecializationRepository specializationRepository;
    private final UserRepository userRepository;
    private final UserSpecializationRepository userSpecializationRepository;
    private final ServiceSpecializationRepository serviceSpecializationRepository;
    private final AccountRepository accountRepository;
    private final CommissionRepository commissionRepository;
    private final CommissionLevelRepository commissionLevelRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PaymentRepository paymentRepository;

    public ProviderCatalogService(
            ProviderServiceRepository providerServiceRepository,
            CategoryServiceRepository categoryServiceRepository,
            ProviderRepository providerRepository,
            SpecializationRepository specializationRepository,
            UserRepository userRepository,
            UserSpecializationRepository userSpecializationRepository,
            ServiceSpecializationRepository serviceSpecializationRepository,
            AccountRepository accountRepository,
            CommissionRepository commissionRepository,
            CommissionLevelRepository commissionLevelRepository,
            RoleRepository roleRepository,
            UserRoleRepository userRoleRepository,
            PaymentRepository paymentRepository
    ) {
        this.providerServiceRepository = providerServiceRepository;
        this.categoryServiceRepository = categoryServiceRepository;
        this.providerRepository = providerRepository;
        this.specializationRepository = specializationRepository;
        this.userRepository = userRepository;
        this.userSpecializationRepository = userSpecializationRepository;
        this.serviceSpecializationRepository = serviceSpecializationRepository;
        this.accountRepository = accountRepository;
        this.commissionRepository = commissionRepository;
        this.commissionLevelRepository = commissionLevelRepository;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
        this.paymentRepository = paymentRepository;
    }

    @Transactional(readOnly = true)
    public List<ProviderServiceDto> getAllServices() {
        return providerServiceRepository.findAll().stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public ProviderServiceDto getService(UUID id) {
        return toDto(providerServiceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Service not found: " + id)));
    }

    @Transactional(readOnly = true)
    public List<ProviderServiceDto> getServicesByCategory(UUID categoryId) {
        return providerServiceRepository.findByCategoryId(categoryId).stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public List<ProviderServiceDto> getServicesByProvider(UUID providerId) {
        return providerServiceRepository.findByProvId(providerId).stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public List<ProviderServiceDto> getServicesByAccount(UUID accountId) {
        return providerServiceRepository.findByAccountId(accountId).stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public List<ProviderServiceDto> getServicesByUserSpecialization(UUID userId) {
        Set<UUID> specialIds = userSpecializationRepository.findByUserId(userId).stream()
                .map(UserSpecializationEntity::getSpecialId)
                .collect(Collectors.toSet());

        if (specialIds.isEmpty()) {
            return List.of();
        }

        Set<UUID> serviceIds = serviceSpecializationRepository.findAll().stream()
                .filter(ss -> specialIds.contains(ss.getSpecialId()))
                .map(ServiceSpecializationEntity::getServiceId)
                .collect(Collectors.toSet());

        return providerServiceRepository.findAllById(serviceIds).stream().map(this::toDto).toList();
    }

    public ProviderServiceDto createService(ProviderServiceRequest dto) {
        ProviderServiceEntity e = new ProviderServiceEntity();
        e.setName(dto.name());
        e.setCategoryId(dto.categoryId());
        e.setProvId(dto.provId());
        e.setAccountId(dto.accountId());
        e.setCommId(dto.commId());
        ProviderServiceEntity saved = providerServiceRepository.save(e);
        syncServiceSpecializations(saved.getId(), dto.specializationIds());
        return toDto(saved);
    }

    public ProviderServiceDto updateService(UUID id, ProviderServiceRequest dto) {
        ProviderServiceEntity e = providerServiceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Service not found: " + id));
        e.setName(dto.name());
        e.setCategoryId(dto.categoryId());
        e.setProvId(dto.provId());
        e.setAccountId(dto.accountId());
        e.setCommId(dto.commId());
        ProviderServiceEntity saved = providerServiceRepository.save(e);
        syncServiceSpecializations(saved.getId(), dto.specializationIds());
        return toDto(saved);
    }

    public void deleteService(UUID id) {
        if (!providerServiceRepository.existsById(id)) {
            throw new NotFoundException("Service not found: " + id);
        }
        providerServiceRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<CategoryServiceDto> getAllCategories() {
        return categoryServiceRepository.findAll().stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public CategoryServiceDto getCategory(UUID id) {
        return toDto(categoryServiceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found: " + id)));
    }

    @Transactional(readOnly = true)
    public List<CategoryServiceDto> getCategoriesByProvider(UUID providerId) {
        return categoryServiceRepository.findByProvId(providerId).stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public List<CategoryServiceDto> getCategoriesByParent(UUID parentId) {
        return categoryServiceRepository.findByPrntCategory(parentId).stream().map(this::toDto).toList();
    }

    public CategoryServiceDto createCategory(CategoryServiceRequest dto) {
        CategoryServiceEntity e = new CategoryServiceEntity();
        e.setName(dto.name());
        e.setPrntCategory(dto.prntCategory());
        e.setProvId(dto.provId());
        return toDto(categoryServiceRepository.save(e));
    }

    public CategoryServiceDto updateCategory(UUID id, CategoryServiceRequest dto) {
        CategoryServiceEntity e = categoryServiceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found: " + id));
        e.setName(dto.name());
        e.setPrntCategory(dto.prntCategory());
        e.setProvId(dto.provId());
        return toDto(categoryServiceRepository.save(e));
    }

    public void deleteCategory(UUID id) {
        if (!categoryServiceRepository.existsById(id)) {
            throw new NotFoundException("Category not found: " + id);
        }
        categoryServiceRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<ProviderDto> getAllProviders() {
        return providerRepository.findAll().stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public ProviderDto getProvider(UUID id) {
        return toDto(providerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Provider not found: " + id)));
    }

    public ProviderDto createProvider(ProviderRequest dto) {
        ProviderEntity e = new ProviderEntity();
        e.setFullName(dto.fullName());
        e.setShortName(dto.shortName());
        e.setCommLvl(dto.commLvl());
        e.setCommId(dto.commId());
        return toDto(providerRepository.save(e));
    }

    public ProviderDto updateProvider(UUID id, ProviderRequest dto) {
        ProviderEntity e = providerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Provider not found: " + id));
        e.setFullName(dto.fullName());
        e.setShortName(dto.shortName());
        e.setCommLvl(dto.commLvl());
        e.setCommId(dto.commId());
        return toDto(providerRepository.save(e));
    }

    public void deleteProvider(UUID id) {
        if (!providerRepository.existsById(id)) {
            throw new NotFoundException("Provider not found: " + id);
        }
        providerRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<SpecializationDto> getAllSpecializations() {
        return specializationRepository.findAll().stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public SpecializationDto getSpecialization(UUID id) {
        return toDto(specializationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Specialization not found: " + id)));
    }

    @Transactional(readOnly = true)
    public List<SpecializationDto> getSpecializationsByProvider(UUID providerId) {
        return specializationRepository.findByProvId(providerId).stream().map(this::toDto).toList();
    }

    public SpecializationDto createSpecialization(SpecializationRequest dto) {
        SpecializationEntity e = new SpecializationEntity();
        e.setName(dto.name());
        e.setProvId(dto.provId());
        return toDto(specializationRepository.save(e));
    }

    public SpecializationDto updateSpecialization(UUID id, SpecializationRequest dto) {
        SpecializationEntity e = specializationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Specialization not found: " + id));
        e.setName(dto.name());
        e.setProvId(dto.provId());
        return toDto(specializationRepository.save(e));
    }

    public void deleteSpecialization(UUID id) {
        if (!specializationRepository.existsById(id)) {
            throw new NotFoundException("Specialization not found: " + id);
        }
        specializationRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public UserDto getUser(UUID id) {
        return toDto(userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found: " + id)));
    }

    @Transactional(readOnly = true)
    public List<UserDto> getUsersByProvider(UUID providerId) {
        return userRepository.findByProvId(providerId).stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public List<UserDto> getUsersBySpecialization(UUID specialId) {
        Set<UUID> userIds = userSpecializationRepository.findBySpecialId(specialId).stream()
                .map(UserSpecializationEntity::getUserId)
                .collect(Collectors.toSet());
        return userRepository.findAllById(userIds).stream().map(this::toDto).toList();
    }

    public UserDto createUser(UserRequest dto) {
        UserEntity e = new UserEntity();
        e.setName(dto.name());
        e.setEmail(dto.email());
        e.setPassword(dto.password());
        e.setProvId(dto.provId());
        UserEntity saved = userRepository.save(e);
        syncUserRoles(saved.getId(), dto.roleIds());
        syncUserSpecializations(saved.getId(), dto.specializationIds());
        return toDto(saved);
    }

    public UserDto updateUser(UUID id, UserRequest dto) {
        UserEntity e = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found: " + id));
        e.setName(dto.name());
        e.setEmail(dto.email());
        e.setPassword(dto.password());
        e.setProvId(dto.provId());
        UserEntity saved = userRepository.save(e);
        syncUserRoles(saved.getId(), dto.roleIds());
        syncUserSpecializations(saved.getId(), dto.specializationIds());
        return toDto(saved);
    }

    public void deleteUser(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User not found: " + id);
        }
        userRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<AccountDto> getAllAccounts() {
        return accountRepository.findAll().stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public AccountDto getAccount(UUID id) {
        return toDto(accountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Account not found: " + id)));
    }

    @Transactional(readOnly = true)
    public List<AccountDto> getAccountsByProvider(UUID providerId) {
        return accountRepository.findByProvId(providerId).stream().map(this::toDto).toList();
    }

    public AccountDto createAccount(AccountRequest dto) {
        AccountEntity e = new AccountEntity();
        e.setName(dto.name());
        e.setOwnerName(dto.ownerName());
        e.setProvId(dto.provId());
        return toDto(accountRepository.save(e));
    }

    public AccountDto updateAccount(UUID id, AccountRequest dto) {
        AccountEntity e = accountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Account not found: " + id));
        e.setName(dto.name());
        e.setOwnerName(dto.ownerName());
        e.setProvId(dto.provId());
        return toDto(accountRepository.save(e));
    }

    public void deleteAccount(UUID id) {
        if (!accountRepository.existsById(id)) {
            throw new NotFoundException("Account not found: " + id);
        }
        accountRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<CommissionDto> getAllCommissions() {
        return commissionRepository.findAll().stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public CommissionDto getCommission(UUID id) {
        return toDto(commissionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Commission not found: " + id)));
    }

    public CommissionDto createCommission(CommissionRequest dto) {
        Commission e = new Commission();
        e.setCommissionType(dto.commissionType());
        e.setCommissionValue(dto.commissionValue());
        return toDto(commissionRepository.save(e));
    }

    public CommissionDto updateCommission(UUID id, CommissionRequest dto) {
        Commission e = commissionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Commission not found: " + id));
        e.setCommissionType(dto.commissionType());
        e.setCommissionValue(dto.commissionValue());
        return toDto(commissionRepository.save(e));
    }

    public void deleteCommission(UUID id) {
        if (!commissionRepository.existsById(id)) {
            throw new NotFoundException("Commission not found: " + id);
        }
        commissionRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<CommissionLevelDto> getAllCommissionLevels() {
        return commissionLevelRepository.findAll().stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public CommissionLevelDto getCommissionLevel(UUID id) {
        return toDto(commissionLevelRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Commission level not found: " + id)));
    }

    public CommissionLevelDto createCommissionLevel(CommissionLevelRequest dto) {
        CommissionLevel e = new CommissionLevel();
        e.setName(dto.name());
        return toDto(commissionLevelRepository.save(e));
    }

    public CommissionLevelDto updateCommissionLevel(UUID id, CommissionLevelRequest dto) {
        CommissionLevel e = commissionLevelRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Commission level not found: " + id));
        e.setName(dto.name());
        return toDto(commissionLevelRepository.save(e));
    }

    public void deleteCommissionLevel(UUID id) {
        if (!commissionLevelRepository.existsById(id)) {
            throw new NotFoundException("Commission level not found: " + id);
        }
        commissionLevelRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<RoleDto> getAllRoles() {
        return roleRepository.findAll().stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public RoleDto getRole(UUID id) {
        return toDto(roleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Role not found: " + id)));
    }

    @Transactional(readOnly = true)
    public List<RoleDto> getRolesByUser(UUID userId) {
        Set<UUID> roleIds = userRoleRepository.findByUserId(userId).stream()
                .map(UserRoleEntity::getRoleId)
                .collect(Collectors.toSet());
        return roleRepository.findAllById(roleIds).stream().map(this::toDto).toList();
    }

    public RoleDto createRole(RoleRequest dto) {
        RoleEntity e = new RoleEntity();
        e.setName(dto.name());
        return toDto(roleRepository.save(e));
    }

    public RoleDto updateRole(UUID id, RoleRequest dto) {
        RoleEntity e = roleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Role not found: " + id));
        e.setName(dto.name());
        return toDto(roleRepository.save(e));
    }

    public void deleteRole(UUID id) {
        if (!roleRepository.existsById(id)) {
            throw new NotFoundException("Role not found: " + id);
        }
        roleRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<PaymentDto> getAllPayments() {
        return paymentRepository.findAll().stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public PaymentDto getPayment(UUID id) {
        return toDto(paymentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Payment not found: " + id)));
    }

    @Transactional(readOnly = true)
    public List<PaymentDto> getPaymentsByProvider(UUID providerId) {
        return paymentRepository.findByProvId(providerId).stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public List<PaymentDto> getPaymentsByStatus(PaymentStatus status) {
        return paymentRepository.findByStatus(status).stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public String getPaymentQrCode(UUID id) {
        PaymentEntity payment = paymentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Payment not found: " + id));
        if (payment.getQrCode() == null || payment.getQrCode().isBlank()) {
            throw new NotFoundException("QR code not found for payment: " + id);
        }
        return payment.getQrCode();
    }

    public PaymentDto createPayment(PaymentRequest dto) {
        ProviderEntity provider = providerRepository.findById(dto.provId())
                .orElseThrow(() -> new NotFoundException("Provider not found: " + dto.provId()));

        PaymentEntity e = new PaymentEntity();
        e.setId(UUID.randomUUID());
        e.setSum(dto.sum());
        e.setFee(resolvePaymentFee(dto.sum(), dto.fee(), provider));
        e.setStatus(dto.status() == null ? PaymentStatus.processing : dto.status());
        e.setProvId(dto.provId());
        e.setQrLink(paymentQrLink(e.getId()));
        e.setQrCode(generateQrCode(e.getQrLink()));
        return toDto(paymentRepository.save(e));
    }

    public PaymentDto updatePaymentStatus(UUID id, PaymentStatusRequest dto) {
        PaymentEntity e = paymentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Payment not found: " + id));
        e.setStatus(dto.status());
        return toDto(paymentRepository.save(e));
    }

    public void deletePayment(UUID id) {
        if (!paymentRepository.existsById(id)) {
            throw new NotFoundException("Payment not found: " + id);
        }
        paymentRepository.deleteById(id);
    }

    public Map<String, Object> executeExternalRequest(String operation, Map<String, Object> params) {
        return switch (operation.toUpperCase(Locale.ROOT)) {
            case "ALL_SERVICES" -> Map.of("services", getAllServices());
            case "SERVICE_BY_ID" -> Map.of("service", getService(uuid(params, "id")));
            case "SERVICES_BY_CATEGORY" -> Map.of("services", getServicesByCategory(uuid(params, "categoryId")));
            case "SERVICES_BY_PROVIDER" -> Map.of("services", getServicesByProvider(uuid(params, "providerId")));
            case "SERVICES_BY_ACCOUNT" -> Map.of("services", getServicesByAccount(uuid(params, "accountId")));
            case "SERVICES_BY_USER_SPECIALIZATION" -> Map.of("services", getServicesByUserSpecialization(uuid(params, "userId")));
            case "CREATE_SERVICE" -> Map.of("service", createService(providerServiceRequest(params)));
            case "ALL_CATEGORIES" -> Map.of("categories", getAllCategories());
            case "CATEGORY_BY_ID" -> Map.of("category", getCategory(uuid(params, "id")));
            case "CATEGORIES_BY_PROVIDER" -> Map.of("categories", getCategoriesByProvider(uuid(params, "providerId")));
            case "CATEGORIES_BY_PARENT" -> Map.of("categories", getCategoriesByParent(uuid(params, "parentId")));
            case "CREATE_CATEGORY" -> Map.of("category", createCategory(categoryServiceRequest(params)));
            case "ALL_PROVIDERS" -> Map.of("providers", getAllProviders());
            case "PROVIDER_BY_ID" -> Map.of("provider", getProvider(uuid(params, "id")));
            case "CREATE_PROVIDER" -> Map.of("provider", createProvider(providerRequest(params)));
            case "ALL_SPECIALIZATIONS" -> Map.of("specializations", getAllSpecializations());
            case "SPECIALIZATION_BY_ID" -> Map.of("specialization", getSpecialization(uuid(params, "id")));
            case "SPECIALIZATIONS_BY_PROVIDER" -> Map.of("specializations", getSpecializationsByProvider(uuid(params, "providerId")));
            case "CREATE_SPECIALIZATION" -> Map.of("specialization", createSpecialization(specializationRequest(params)));
            case "ALL_USERS" -> Map.of("users", getAllUsers());
            case "USER_BY_ID" -> Map.of("user", getUser(uuid(params, "id")));
            case "USERS_BY_PROVIDER" -> Map.of("users", getUsersByProvider(uuid(params, "providerId")));
            case "USERS_BY_SPECIALIZATION" -> Map.of("users", getUsersBySpecialization(uuid(params, "specialId")));
            case "CREATE_USER" -> Map.of("user", createUser(userRequest(params)));
            case "ALL_ACCOUNTS" -> Map.of("accounts", getAllAccounts());
            case "ACCOUNT_BY_ID" -> Map.of("account", getAccount(uuid(params, "id")));
            case "ACCOUNTS_BY_PROVIDER" -> Map.of("accounts", getAccountsByProvider(uuid(params, "providerId")));
            case "CREATE_ACCOUNT" -> Map.of("account", createAccount(accountRequest(params)));
            case "ALL_COMMISSIONS" -> Map.of("commissions", getAllCommissions());
            case "COMMISSION_BY_ID" -> Map.of("commission", getCommission(uuid(params, "id")));
            case "CREATE_COMMISSION" -> Map.of("commission", createCommission(commissionRequest(params)));
            case "ALL_COMMISSION_LEVELS" -> Map.of("commissionLevels", getAllCommissionLevels());
            case "COMMISSION_LEVEL_BY_ID" -> Map.of("commissionLevel", getCommissionLevel(uuid(params, "id")));
            case "ALL_ROLES" -> Map.of("roles", getAllRoles());
            case "ROLE_BY_ID" -> Map.of("role", getRole(uuid(params, "id")));
            case "ROLES_BY_USER" -> Map.of("roles", getRolesByUser(uuid(params, "userId")));
            case "ALL_PAYMENTS" -> Map.of("payments", getAllPayments());
            case "PAYMENT_BY_ID" -> Map.of("payment", getPayment(uuid(params, "id")));
            case "PAYMENTS_BY_PROVIDER" -> Map.of("payments", getPaymentsByProvider(uuid(params, "providerId")));
            case "PAYMENTS_BY_STATUS" -> Map.of("payments", getPaymentsByStatus(paymentStatus(params, "status")));
            case "CREATE_PAYMENT" -> Map.of("payment", createPayment(paymentRequest(params)));
            case "UPDATE_PAYMENT_STATUS" -> Map.of("payment", updatePaymentStatus(uuid(params, "id"), new PaymentStatusRequest(paymentStatus(params, "status"))));
            default -> throw new IllegalArgumentException("Unsupported operation: " + operation);
        };
    }

    private void syncServiceSpecializations(UUID serviceId, List<UUID> specialIds) {
        if (specialIds == null) {
            return;
        }
        serviceSpecializationRepository.deleteAll(serviceSpecializationRepository.findByServiceId(serviceId));
        serviceSpecializationRepository.flush();
        specialIds.stream()
                .filter(Objects::nonNull)
                .distinct()
                .forEach(specialId -> {
                    if (!specializationRepository.existsById(specialId)) {
                        throw new NotFoundException("Specialization not found: " + specialId);
                    }
                    ServiceSpecializationEntity link = new ServiceSpecializationEntity();
                    link.setServiceId(serviceId);
                    link.setSpecialId(specialId);
                    serviceSpecializationRepository.save(link);
                });
    }

    private void syncUserRoles(UUID userId, List<UUID> roleIds) {
        if (roleIds == null) {
            return;
        }
        userRoleRepository.deleteAll(userRoleRepository.findByUserId(userId));
        userRoleRepository.flush();
        roleIds.stream()
                .filter(Objects::nonNull)
                .distinct()
                .forEach(roleId -> {
                    if (!roleRepository.existsById(roleId)) {
                        throw new NotFoundException("Role not found: " + roleId);
                    }
                    UserRoleEntity link = new UserRoleEntity();
                    link.setUserId(userId);
                    link.setRoleId(roleId);
                    userRoleRepository.save(link);
                });
    }

    private void syncUserSpecializations(UUID userId, List<UUID> specialIds) {
        if (specialIds == null) {
            return;
        }
        userSpecializationRepository.deleteAll(userSpecializationRepository.findByUserId(userId));
        userSpecializationRepository.flush();
        specialIds.stream()
                .filter(Objects::nonNull)
                .distinct()
                .forEach(specialId -> {
                    if (!specializationRepository.existsById(specialId)) {
                        throw new NotFoundException("Specialization not found: " + specialId);
                    }
                    UserSpecializationEntity link = new UserSpecializationEntity();
                    link.setUserId(userId);
                    link.setSpecialId(specialId);
                    userSpecializationRepository.save(link);
                });
    }

    private BigDecimal resolvePaymentFee(BigDecimal sum, BigDecimal explicitFee, ProviderEntity provider) {
        if (explicitFee != null) {
            return explicitFee;
        }
        if (provider.getCommId() == null) {
            return BigDecimal.ZERO;
        }
        Commission commission = commissionRepository.findById(provider.getCommId()).orElse(null);
        if (commission == null) {
            return BigDecimal.ZERO;
        }
        if (commission.getCommissionType() == CommissionType.fixed) {
            return commission.getCommissionValue();
        }
        return sum.multiply(commission.getCommissionValue())
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }

    private String paymentQrLink(UUID paymentId) {
        try {
            return ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/payments/{id}/qr")
                    .buildAndExpand(paymentId)
                    .toUriString();
        } catch (IllegalStateException ex) {
            return "/api/payments/" + paymentId + "/qr";
        }
    }

    private String generateQrCode(String payload) {
        try {
            BitMatrix matrix = new QRCodeWriter().encode(payload, BarcodeFormat.QR_CODE, 300, 300);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(matrix, "PNG", output);
            return Base64.getEncoder().encodeToString(output.toByteArray());
        } catch (WriterException | IOException ex) {
            throw new IllegalStateException("Failed to generate QR code", ex);
        }
    }

    private ProviderServiceDto toDto(ProviderServiceEntity e) {
        return new ProviderServiceDto(
                e.getId(),
                e.getName(),
                categoryName(e.getCategoryId()),
                providerName(e.getProvId()),
                accountName(e.getAccountId()),
                commissionDetails(e.getCommId()),
                serviceSpecializationNames(e.getId())
        );
    }

    private CategoryServiceDto toDto(CategoryServiceEntity e) {
        return new CategoryServiceDto(
                e.getId(),
                e.getName(),
                categoryName(e.getPrntCategory()),
                providerName(e.getProvId())
        );
    }

    private ProviderDto toDto(ProviderEntity e) {
        return new ProviderDto(
                e.getId(),
                e.getFullName(),
                e.getShortName(),
                commissionLevelName(e.getCommLvl()),
                commissionDetails(e.getCommId())
        );
    }

    private SpecializationDto toDto(SpecializationEntity e) {
        return new SpecializationDto(e.getId(), e.getName(), providerName(e.getProvId()));
    }

    private UserDto toDto(UserEntity e) {
        return new UserDto(
                e.getId(),
                e.getName(),
                e.getEmail(),
                e.getPassword(),
                providerName(e.getProvId()),
                userRoleNames(e.getId()),
                userSpecializationNames(e.getId())
        );
    }

    private AccountDto toDto(AccountEntity e) {
        return new AccountDto(e.getId(), e.getName(), e.getOwnerName(), providerName(e.getProvId()));
    }

    private CommissionDto toDto(Commission e) {
        return new CommissionDto(e.getId(), e.getCommissionType(), e.getCommissionValue());
    }

    private CommissionLevelDto toDto(CommissionLevel e) {
        return new CommissionLevelDto(e.getId(), e.getName());
    }

    private RoleDto toDto(RoleEntity e) {
        return new RoleDto(e.getId(), e.getName());
    }

    private PaymentDto toDto(PaymentEntity e) {
        return new PaymentDto(
                e.getId(),
                e.getSum(),
                e.getFee(),
                e.getStatus(),
                providerName(e.getProvId()),
                e.getQrLink(),
                e.getQrCode(),
                e.getCreatedAt(),
                e.getUpdatedAt()
        );
    }

    private String providerName(UUID id) {
        if (id == null) {
            return null;
        }
        return providerRepository.findById(id)
                .map(provider -> provider.getShortName() == null || provider.getShortName().isBlank()
                        ? provider.getFullName()
                        : provider.getShortName())
                .orElse(null);
    }

    private String categoryName(UUID id) {
        if (id == null) {
            return null;
        }
        return categoryServiceRepository.findById(id)
                .map(CategoryServiceEntity::getName)
                .orElse(null);
    }

    private String accountName(UUID id) {
        if (id == null) {
            return null;
        }
        return accountRepository.findById(id)
                .map(AccountEntity::getName)
                .orElse(null);
    }

    private String commissionLevelName(UUID id) {
        if (id == null) {
            return null;
        }
        return commissionLevelRepository.findById(id)
                .map(CommissionLevel::getName)
                .map(Enum::name)
                .orElse(null);
    }

    private CommissionDto commissionDto(UUID id) {
        if (id == null) {
            return null;
        }
        return commissionRepository.findById(id)
                .map(this::toDto)
                .orElse(null);
    }

    private CommissionDetailsDto commissionDetails(UUID id) {
        if (id == null) {
            return null;
        }
        return commissionRepository.findById(id)
                .map(commission -> new CommissionDetailsDto(
                        commission.getCommissionType(),
                        commission.getCommissionValue()
                ))
                .orElse(null);
    }

    private List<String> serviceSpecializationNames(UUID serviceId) {
        return serviceSpecializationRepository.findByServiceId(serviceId).stream()
                .map(ServiceSpecializationEntity::getSpecialId)
                .map(this::specializationName)
                .filter(Objects::nonNull)
                .toList();
    }

    private List<String> userRoleNames(UUID userId) {
        return userRoleRepository.findByUserId(userId).stream()
                .map(UserRoleEntity::getRoleId)
                .map(this::roleName)
                .filter(Objects::nonNull)
                .toList();
    }

    private List<String> userSpecializationNames(UUID userId) {
        return userSpecializationRepository.findByUserId(userId).stream()
                .map(UserSpecializationEntity::getSpecialId)
                .map(this::specializationName)
                .filter(Objects::nonNull)
                .toList();
    }

    private String specializationName(UUID id) {
        return specializationRepository.findById(id)
                .map(SpecializationEntity::getName)
                .orElse(null);
    }

    private String roleName(UUID id) {
        return roleRepository.findById(id)
                .map(RoleEntity::getName)
                .map(Enum::name)
                .orElse(null);
    }

    private ProviderServiceRequest providerServiceRequest(Map<String, Object> params) {
        return new ProviderServiceRequest(
                text(params, "name"),
                uuid(params, "categoryId"),
                uuid(params, "provId"),
                uuid(params, "accountId"),
                optionalUuid(params, "commId"),
                uuidList(params, "specializationIds")
        );
    }

    private CategoryServiceRequest categoryServiceRequest(Map<String, Object> params) {
        return new CategoryServiceRequest(
                text(params, "name"),
                optionalUuid(params, "prntCategory"),
                uuid(params, "provId")
        );
    }

    private ProviderRequest providerRequest(Map<String, Object> params) {
        return new ProviderRequest(
                text(params, "fullName"),
                optionalText(params, "shortName"),
                uuid(params, "commLvl"),
                optionalUuid(params, "commId")
        );
    }

    private SpecializationRequest specializationRequest(Map<String, Object> params) {
        return new SpecializationRequest(text(params, "name"), uuid(params, "provId"));
    }

    private UserRequest userRequest(Map<String, Object> params) {
        return new UserRequest(
                text(params, "name"),
                text(params, "email"),
                text(params, "password"),
                uuid(params, "provId"),
                uuidList(params, "roleIds"),
                uuidList(params, "specializationIds")
        );
    }

    private AccountRequest accountRequest(Map<String, Object> params) {
        return new AccountRequest(text(params, "name"), text(params, "ownerName"), uuid(params, "provId"));
    }

    private CommissionRequest commissionRequest(Map<String, Object> params) {
        return new CommissionRequest(
                enumValue(params, "commissionType", CommissionType.class),
                decimal(params, "commissionValue")
        );
    }

    private PaymentRequest paymentRequest(Map<String, Object> params) {
        return new PaymentRequest(
                decimal(params, "sum"),
                optionalDecimal(params, "fee"),
                optionalPaymentStatus(params, "status"),
                uuid(params, "provId")
        );
    }

    private UUID uuid(Map<String, Object> params, String key) {
        return UUID.fromString(String.valueOf(param(params, key)));
    }

    private UUID optionalUuid(Map<String, Object> params, String key) {
        Object value = optionalParam(params, key);
        return value == null || String.valueOf(value).isBlank() ? null : UUID.fromString(String.valueOf(value));
    }

    private String text(Map<String, Object> params, String key) {
        return String.valueOf(param(params, key));
    }

    private String optionalText(Map<String, Object> params, String key) {
        Object value = optionalParam(params, key);
        return value == null ? null : String.valueOf(value);
    }

    private BigDecimal decimal(Map<String, Object> params, String key) {
        return new BigDecimal(String.valueOf(param(params, key)));
    }

    private BigDecimal optionalDecimal(Map<String, Object> params, String key) {
        Object value = optionalParam(params, key);
        return value == null || String.valueOf(value).isBlank() ? null : new BigDecimal(String.valueOf(value));
    }

    private PaymentStatus paymentStatus(Map<String, Object> params, String key) {
        return enumValue(params, key, PaymentStatus.class);
    }

    private PaymentStatus optionalPaymentStatus(Map<String, Object> params, String key) {
        Object value = optionalParam(params, key);
        return value == null || String.valueOf(value).isBlank()
                ? null
                : enumValue(String.valueOf(value), PaymentStatus.class);
    }

    private List<UUID> uuidList(Map<String, Object> params, String key) {
        Object value = optionalParam(params, key);
        if (value == null) {
            return null;
        }
        if (value instanceof Collection<?> collection) {
            return collection.stream()
                    .filter(Objects::nonNull)
                    .map(item -> UUID.fromString(String.valueOf(item)))
                    .toList();
        }
        String raw = String.valueOf(value);
        if (raw.isBlank()) {
            return List.of();
        }
        return Arrays.stream(raw.split(","))
                .map(String::trim)
                .filter(part -> !part.isBlank())
                .map(UUID::fromString)
                .toList();
    }

    private <E extends Enum<E>> E enumValue(Map<String, Object> params, String key, Class<E> enumType) {
        return enumValue(String.valueOf(param(params, key)), enumType);
    }

    private <E extends Enum<E>> E enumValue(String value, Class<E> enumType) {
        return Arrays.stream(enumType.getEnumConstants())
                .filter(item -> item.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported " + enumType.getSimpleName() + ": " + value));
    }

    private Object param(Map<String, Object> params, String key) {
        Object value = optionalParam(params, key);
        if (value == null) {
            throw new IllegalArgumentException("Missing parameter: " + key);
        }
        return value;
    }

    private Object optionalParam(Map<String, Object> params, String key) {
        if (params == null) {
            return null;
        }
        if (params.containsKey(key)) {
            return params.get(key);
        }
        return params.entrySet().stream()
                .filter(entry -> entry.getKey().equalsIgnoreCase(key))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);
    }
}
