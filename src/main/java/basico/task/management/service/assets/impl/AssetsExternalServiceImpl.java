package basico.task.management.service.assets.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import basico.task.management.model.primary.*;
import basico.task.management.service.*;
import basico.task.management.service.assets.AssetsExternalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import basico.task.management.projection.AssetsExternalResponse;
import basico.task.management.repository.assets.AssetsExternalRepository;


@Service
@RequiredArgsConstructor
public class AssetsExternalServiceImpl implements AssetsExternalService {

    private final AssetsExternalRepository assetsExternalRepository;
    private final SocietyService societyService;
    private final PromotionService promotionService;
    private final LocationService locationService;
    private final GarajeService garajeService;
    private final StorageRoomService storageRoomService;


    @Override
    public List<Assets> AssetListPrecarious(Status status) {
        List<AssetsExternalResponse> assetsExternalResponses = assetsExternalRepository.findAllPrecarious();
		return  assetsList(assetsExternalResponses,"Precarious",status) ;
    }

	@Override
	public List<Assets> assetListSquatted(Status status) {
		List<AssetsExternalResponse> assetsExternalResponses = assetsExternalRepository.findAllSquatted();
		return  assetsList(assetsExternalResponses,"Squatted",status) ;
	}


	private List<Assets> assetsList( List<AssetsExternalResponse> assetsExternalResponses,String type,Status status){
		List<Assets> assets = new LinkedList<>();
		for (AssetsExternalResponse assetsExternalResponse : assetsExternalResponses) {
			Assets asset = new Assets();
			asset.setArrId(assetsExternalResponse.getId());
			asset.setType(type);
			asset.setTaskStatus(status);
			asset.setCommunity(assetsExternalResponse.getCommunity());
			asset.setProvince(assetsExternalResponse.getProvince());
			asset.setMunicipality(assetsExternalResponse.getMunicipality());
			Optional<Society> society = societyService.findByIdOptional(assetsExternalResponse.getSocietyId());
			if (society.isPresent()) {
				asset.setSociety(society.get());
				Optional<Promotion> promotion = promotionService.findByIdOptional(assetsExternalResponse.getPromotionId());
				Promotion promotionExt = null;
				if (promotion.isPresent()) {
					promotionExt = promotion.get();
					asset.setPromotion(promotion.get());
				} else {
					Promotion promotionSave = new Promotion();
					promotionSave.setId(assetsExternalResponse.getPromotionId());
					promotionSave.setSociety(society.get());
					promotionSave.setPromotionName(assetsExternalResponse.getPromotionName());
					promotionExt = promotionService.save(promotionSave);
					asset.setPromotion(promotionExt);
				}
				if (assetsExternalResponse.getArrType().equalsIgnoreCase("VI")) {
					Optional<Location> location = locationService.findById(assetsExternalResponse.getCode());
					if (location.isPresent()) {
						asset.setLocation(location.get());
					} else {
						Location locationSave = new Location();
						locationSave.setId(assetsExternalResponse.getCode());
						locationSave.setPromotion(promotionExt);
						locationSave.setSociety(society.get());
						locationSave.setDescription(assetsExternalResponse.getDescription());
						locationService.save(locationSave);
						asset.setLocation(locationSave);
					}
				} else if (assetsExternalResponse.getArrType().equalsIgnoreCase("TR")) {
					Optional<StorageRoom> storageRoom = storageRoomService.findByIdOptional(assetsExternalResponse.getCode());
					if (storageRoom.isPresent()) {
						asset.setStorageRoom(storageRoom.get());
					} else {
						StorageRoom storageSave = new StorageRoom();
						storageSave.setId(assetsExternalResponse.getCode());
						storageSave.setPromotion(promotionExt);
						storageSave.setSociety(society.get());
						storageSave.setDescription(assetsExternalResponse.getDescription());
						storageRoomService.save(storageSave);
						asset.setStorageRoom(storageSave);
					}
				} else if (assetsExternalResponse.getArrType().equalsIgnoreCase("GA")) {
					Optional<Garaje> garaje = garajeService.findByIdOptional(assetsExternalResponse.getCode());
					if (garaje.isPresent()) {
						asset.setGarajes(garaje.get());
					} else {
						Garaje garajeSave = new Garaje();
						garajeSave.setId(assetsExternalResponse.getCode());
						garajeSave.setPromotion(promotionExt);
						garajeSave.setSociety(society.get());
						garajeSave.setDescription(assetsExternalResponse.getDescription());
						garajeService.save(garajeSave);
						asset.setGarajes(garajeSave);
					}
				}
				assets.add(asset);
			}

		}
		return assets;


	}


}
