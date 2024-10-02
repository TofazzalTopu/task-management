package basico.task.management.service.impl;

import basico.task.management.dto.NewOrderAssetsDto;
import basico.task.management.enums.Entity;
import basico.task.management.enums.Status;
import basico.task.management.exception.NotFoundException;
import basico.task.management.model.primary.Assets;
import basico.task.management.projection.GarajProjection;
import basico.task.management.projection.PromotionProjection;
import basico.task.management.projection.SocietyAndLocationProjection;
import basico.task.management.projection.StorageRoomProjection;
import basico.task.management.repository.primary.AssetsRepository;
import basico.task.management.service.*;
import basico.task.management.service.assets.AssetsExternalService;
import basico.task.management.util.Numeric;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AssetServiceImpl implements AssetsService {

    private final AssetsRepository assetsRepository;
    private final AssetsExternalService assetsExternalService;
    private final MessageSource messageSource;
    private final StatusService statusService;
    private final SocietyService societyService;
    private final PromotionService promotionService;
    private final LocationService locationService;
    private final GarajeService garajeService;
    private final StorageRoomService storageRoomService;

    @Scheduled(cron = "0 0 0 * * *")
    private void saveAssetPrecarious() {
        basico.task.management.model.primary.Status status = statusService.findByNameAndEntity(Status.NEW.name(), Entity.TASK.name());
        saveAllAssets(assetsExternalService.AssetListPrecarious(status));
    }

    @Scheduled(cron = "0 0 0 * * *")
    private void saveAssetSquatted() {
        basico.task.management.model.primary.Status status = statusService.findByNameAndEntity(Status.NEW.name(), Entity.TASK.name());
        saveAllAssets(assetsExternalService.assetListSquatted(status));
    }

    private void saveAllAssets(List<Assets> assets) {
        for (Assets assetsExt : assets) {
            Assets optionalAssets = assetsRepository.findByArrId(assetsExt.getArrId());
            if (Objects.isNull(optionalAssets)) {
                assetsRepository.save(assetsExt);
            }
        }
    }

    @Override
    public Assets save(NewOrderAssetsDto assetsNewOrder, Locale locale) throws Exception {
        basico.task.management.model.primary.Status status = statusService.findByNameAndEntity(Status.DRAFT.name(), Entity.ASSETS.name());
        Assets assets = new Assets();
        assets.setSociety(societyService.findById(assetsNewOrder.getSocietyId()));
        assets.setPromotion(promotionService.findById(assetsNewOrder.getPromotionId()));
        if (Objects.nonNull(assetsNewOrder.getLocationId())) {
            assets.setLocation(locationService.findByIdNotOptional(assetsNewOrder.getLocationId(),locale));
        }
        if (Objects.nonNull(assetsNewOrder.getGarajeId())) {
            assets.setGarajes(garajeService.findById(assetsNewOrder.getGarajeId(),locale));
        }
        if (Objects.nonNull(assetsNewOrder.getStorageRoomId())) {
            assets.setStorageRoom(storageRoomService.findById(assetsNewOrder.getStorageRoomId(),locale));
        }
        assets.setType(assetsNewOrder.getType());
        assets.setTaskStatus(status);
        return assetsRepository.save(assets);
    }

    @Override
    public Assets update(Long id, Assets assets, Locale locale) throws Exception {
        Assets savedAssets = findById(id, locale);
        savedAssets.setProvince(assets.getProvince());
        savedAssets.setType(assets.getType());
        savedAssets.setLocation(assets.getLocation());
        savedAssets.setTaskStatus(statusService.findById(assets.getTaskStatus().getId()));
        return assetsRepository.save(savedAssets);
    }

    @Override
    public List<PromotionProjection> findAllPromotion(String name) {
        return assetsRepository.findAllByPromotionNameIsNotNullOrderByIdDesc(name.toLowerCase());
    }

    @Override
    public List<StorageRoomProjection> findAllStorage(String name) {
        return assetsRepository.findAllByStorageRoomIsNotNullOrderByIdDesc(name.toLowerCase());
    }

    @Override
    public List<GarajProjection> findAllGaraje(String name) {
        return assetsRepository.findAllByGarajeIsNotNullOrderByIdDesc(name.toLowerCase());
    }

    @Override
    public Assets updateStatus(Long id, Locale locale) throws Exception {
        Assets savedAssets = findById(id, locale);
        basico.task.management.model.primary.Status status = statusService.findByNameAndEntity(Status.PROCESSED.name(), Entity.ASSETS.name());
        savedAssets.setTaskStatus(status);
        return assetsRepository.save(savedAssets);
    }

    @Override
    public List<SocietyAndLocationProjection> findAllLocation(String name) {
        return assetsRepository.findAllByLocationName(name.toLowerCase());
    }

    @Override
    public List<SocietyAndLocationProjection> findAllSociety(String name) {
        return assetsRepository.findAllSociety(name.toLowerCase());
    }

    @Override
    public Page<Assets> findAll(Pageable pageable) {
        saveAssetSquatted();
        return assetsRepository.findAll(pageable);
    }


    @Override
    public Assets findById(Long id, Locale locale) throws Exception {
        return assetsRepository.findById(id).orElseThrow(() -> new NotFoundException(messageSource.getMessage("assets.not.found", null, locale)));
    }

    @Override
    public Page<Assets> findAllByType(Pageable pageable, String type) throws Exception {
        return assetsRepository.findAllByTypeOrderByIdDesc(pageable, type);
    }

    @Override
    public Page<Assets> findAllByTypes(Pageable pageable, List<String> types) throws Exception {
        return assetsRepository.findAllByTypeInOrderByIdDesc(pageable, types);
    }

    @Override
    public Page<Assets> filterNewOrders(Pageable pageable, List<String> types, String searchText) throws Exception {
        String uppcaseFilter = null;
        Long id = (long) 0;
        if (Numeric.isNumeric(searchText)) {
            id=Long.parseLong(searchText);
            uppcaseFilter=searchText;
        } else {
            uppcaseFilter=searchText.toUpperCase();
        }
        return assetsRepository.findAllByTypeInAndIdIsLikeOrProvinceIgnoreCaseContainsOrLocation_DescriptionIgnoreCaseContainsOrPromotion_PromotionNameIgnoreCaseContainsOrSociety_SocietyNameIgnoreCaseContainsOrGarajes_DescriptionIgnoreCaseContainsOrStorageRoom_DescriptionIgnoreCaseContainsOrProvinceIgnoreCaseContainsOrCommunityIgnoreCaseContainsOrMunicipalityIgnoreCaseContains(pageable, types, id,searchText,  searchText, searchText, searchText, searchText, searchText, searchText, searchText, searchText);
    }

    @Override
    public void delete(Long id, Locale locale) throws Exception {
        findById(id, locale);
        assetsRepository.deleteById(id);
    }

}
