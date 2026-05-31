package com.example.providerapiemulator.service;

import com.example.providerapiemulator.dto.*;
import com.example.providerapiemulator.entity.*;
import com.example.providerapiemulator.exception.NotFoundException;
import com.example.providerapiemulator.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public ProviderCatalogService(
            ProviderServiceRepository providerServiceRepository,
            CategoryServiceRepository categoryServiceRepository,
            ProviderRepository providerRepository,
            SpecializationRepository specializationRepository,
            UserRepository userRepository,
            UserSpecializationRepository userSpecializationRepository,
            ServiceSpecializationRepository serviceSpecializationRepository
    ) {
        this.providerServiceRepository = providerServiceRepository;
        this.categoryServiceRepository = categoryServiceRepository;
        this.providerRepository = providerRepository;
        this.specializationRepository = specializationRepository;
        this.userRepository = userRepository;
        this.userSpecializationRepository = userSpecializationRepository;
        this.serviceSpecializationRepository = serviceSpecializationRepository;
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
    public List<ProviderServiceDto> getServicesByUserSpecialization(UUID userId) {
        Set<UUID> specialIds = userSpecializationRepository.findByUserId(userId).stream()
                .map(UserSpecializationEntity::getSpecialId)
                .collect(Collectors.toSet());

        if (specialIds.isEmpty()) return List.of();

        Set<UUID> serviceIds = serviceSpecializationRepository.findAll().stream()
                .filter(ss -> specialIds.contains(ss.getSpecialId()))
                .map(ServiceSpecializationEntity::getServiceId)
                .collect(Collectors.toSet());

        return providerServiceRepository.findAllById(serviceIds).stream().map(this::toDto).toList();
    }

    public ProviderServiceDto createService(ProviderServiceDto dto) {
        ProviderServiceEntity e = new ProviderServiceEntity();
        e.setName(dto.name());
        e.setCategoryId(dto.categoryId());
        e.setProvId(dto.provId());
        e.setAccountId(dto.accountId());
        e.setCommId(dto.commId());
        return toDto(providerServiceRepository.save(e));
    }

    public ProviderServiceDto updateService(UUID id, ProviderServiceDto dto) {
        ProviderServiceEntity e = providerServiceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Service not found: " + id));
        e.setName(dto.name());
        e.setCategoryId(dto.categoryId());
        e.setProvId(dto.provId());
        e.setAccountId(dto.accountId());
        e.setCommId(dto.commId());
        return toDto(providerServiceRepository.save(e));
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

    public CategoryServiceDto createCategory(CategoryServiceDto dto) {
        CategoryServiceEntity e = new CategoryServiceEntity();
        e.setName(dto.name());
        e.setPrntCategory(dto.prntCategory());
        e.setProvId(dto.provId());
        return toDto(categoryServiceRepository.save(e));
    }

    public CategoryServiceDto updateCategory(UUID id, CategoryServiceDto dto) {
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

    public ProviderDto createProvider(ProviderDto dto) {
        ProviderEntity e = new ProviderEntity();
        e.setFullName(dto.fullName());
        e.setShortName(dto.shortName());
        e.setCommLvl(dto.commLvl());
        e.setCommId(dto.commId());
        return toDto(providerRepository.save(e));
    }

    public ProviderDto updateProvider(UUID id, ProviderDto dto) {
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

    public SpecializationDto createSpecialization(SpecializationDto dto) {
        SpecializationEntity e = new SpecializationEntity();
        e.setName(dto.name());
        e.setProvId(dto.provId());
        return toDto(specializationRepository.save(e));
    }

    public SpecializationDto updateSpecialization(UUID id, SpecializationDto dto) {
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

    public UserDto createUser(UserDto dto) {
        UserEntity e = new UserEntity();
        e.setName(dto.name());
        e.setEmail(dto.email());
        e.setPassword(dto.password());
        e.setProvId(dto.provId());
        return toDto(userRepository.save(e));
    }

    public UserDto updateUser(UUID id, UserDto dto) {
        UserEntity e = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found: " + id));
        e.setName(dto.name());
        e.setEmail(dto.email());
        e.setPassword(dto.password());
        e.setProvId(dto.provId());
        return toDto(userRepository.save(e));
    }

    public void deleteUser(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User not found: " + id);
        }
        userRepository.deleteById(id);
    }

    public Map<String, Object> executeExternalRequest(String operation, Map<String, Object> params) {
        return switch (operation.toUpperCase(Locale.ROOT)) {
            case "ALL_SERVICES" -> Map.of("services", getAllServices());
            case "SERVICE_BY_ID" -> Map.of("service", getService(uuid(params, "id")));
            case "SERVICES_BY_CATEGORY" -> Map.of("services", getServicesByCategory(uuid(params, "categoryid")));
            case "SERVICES_BY_PROVIDER" -> Map.of("services", getServicesByProvider(uuid(params, "providerid")));
            case "SERVICES_BY_USER_SPECIALIZATION" -> Map.of("services", getServicesByUserSpecialization(uuid(params, "userid")));
            case "ALL_CATEGORIES" -> Map.of("categories", getAllCategories());
            case "CATEGORY_BY_ID" -> Map.of("category", getCategory(uuid(params, "id")));
            case "ALL_PROVIDERS" -> Map.of("providers", getAllProviders());
            case "PROVIDER_BY_ID" -> Map.of("provider", getProvider(uuid(params, "id")));
            case "ALL_SPECIALIZATIONS" -> Map.of("specializations", getAllSpecializations());
            case "SPECIALIZATION_BY_ID" -> Map.of("specialization", getSpecialization(uuid(params, "id")));
            case "ALL_USERS" -> Map.of("users", getAllUsers());
            case "USER_BY_ID" -> Map.of("user", getUser(uuid(params, "id")));
            case "USERS_BY_PROVIDER" -> Map.of("users", getUsersByProvider(uuid(params, "providerid")));
            case "USERS_BY_SPECIALIZATION" -> Map.of("users", getUsersBySpecialization(uuid(params, "specialid")));
            default -> throw new IllegalArgumentException("Unsupported operation: " + operation);
        };
    }

    private UUID uuid(Map<String, Object> params, String key) {
        Object value = params == null ? null : params.get(key);
        if (value == null) throw new IllegalArgumentException("Missing parameter: " + key);
        return UUID.fromString(String.valueOf(value));
    }

    private ProviderServiceDto toDto(ProviderServiceEntity e) {
        return new ProviderServiceDto(e.getId(), e.getName(), e.getCategoryId(), e.getProvId(), e.getAccountId(), e.getCommId());
    }

    private CategoryServiceDto toDto(CategoryServiceEntity e) {
        return new CategoryServiceDto(e.getId(), e.getName(), e.getPrntCategory(), e.getProvId());
    }

    private ProviderDto toDto(ProviderEntity e) {
        return new ProviderDto(e.getId(), e.getFullName(), e.getShortName(), e.getCommLvl(), e.getCommId());
    }

    private SpecializationDto toDto(SpecializationEntity e) {
        return new SpecializationDto(e.getId(), e.getName(), e.getProvId());
    }

    private UserDto toDto(UserEntity e) {
        return new UserDto(e.getId(), e.getName(), e.getEmail(), e.getPassword(), e.getProvId());
    }
}
