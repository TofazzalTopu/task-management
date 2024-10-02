package basico.task.management.service.impl;

import basico.task.management.exception.NotFoundException;
import basico.task.management.model.plataforma.PrinexStorageRoom;
import basico.task.management.model.primary.Garaje;
import basico.task.management.model.primary.StorageRoom;
import basico.task.management.repository.primary.StorageRoomRepository;
import basico.task.management.service.StorageRoomService;
import basico.task.management.service.plataforma.PrinexStorageRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StorageRoomServiceImpl implements StorageRoomService {

    private final StorageRoomRepository storageRoomRepository;
    private final PrinexStorageRoomService prinexStorageRoomService;
    private final MessageSource messageSource;

    @Scheduled(cron = "0 0 1 * * *")
    public void updateDaily(){
        List<StorageRoom> storageRoomsExtAll= prinexStorageRoomService.findAllStorageRoom();
        for (StorageRoom storageRoomExt:storageRoomsExtAll) {
            Optional<StorageRoom> society=storageRoomRepository.findById(storageRoomExt.getId());
            if(!society.isPresent()){
                storageRoomRepository.save(storageRoomExt);
            }
        }
    }

    @Override
    public Page<StorageRoom> findAllStorageRoom(Long societyId, Long promotionId, Pageable pageable) {
        return storageRoomRepository.findAllBySocietyIdAndPromotionId(pageable,societyId,promotionId);
    }

    @Override
    public List<StorageRoom> findAllStorageRoomByName(Long societyId, Long promotionId, String name) {
        return storageRoomRepository.findAllBySocietyIdAndPromotionIdAndDescriptionContaining(societyId,promotionId,name);
    }

    @Override
    public StorageRoom findById(String storageRoom, Locale locale) {
        return storageRoomRepository.findById(storageRoom).orElseThrow(() -> new NotFoundException(messageSource.getMessage("assets.not.found", null, locale)));
    }

    @Override
    public Optional<StorageRoom> findByIdOptional(String code) {
        return storageRoomRepository.findById(code);
    }

    @Override
    public StorageRoom save(StorageRoom storageSave) {
        return storageRoomRepository.save(storageSave);
    }
}
