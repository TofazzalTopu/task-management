package basico.task.management.service.plataforma.impl;

import basico.task.management.model.plataforma.PrinexStorageRoom;
import basico.task.management.model.primary.StorageRoom;
import basico.task.management.repository.plataforma.PrinexStorageRoomRepository;
import basico.task.management.service.PromotionService;
import basico.task.management.service.SocietyService;
import basico.task.management.service.plataforma.PrinexStorageRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PrinexStorageRoomServiceImpl implements PrinexStorageRoomService {

    private final PrinexStorageRoomRepository prinexStorageRoomRepository;
    private final SocietyService societyService;
    private final PromotionService promotionService;


    @Override
    public List<StorageRoom> findAllStorageRoom() {
        List<PrinexStorageRoom> prinexStorageRooms = prinexStorageRoomRepository.findAll();
        List<StorageRoom> storageRooms = new ArrayList<>();
        for (PrinexStorageRoom prinexStorageRoom : prinexStorageRooms) {
            StorageRoom storageRoom = new StorageRoom();
            storageRoom.setId(prinexStorageRoom.getId());
            storageRoom.setRent(prinexStorageRoom.getRent());
            storageRoom.setDescription(prinexStorageRoom.getDescription());
            storageRoom.setPromotion(promotionService.findById(prinexStorageRoom.getPromotionId()));
            storageRoom.setSociety(societyService.findById(prinexStorageRoom.getSocietyId()));
            storageRooms.add(storageRoom);
        }
    return storageRooms;
    }
}
