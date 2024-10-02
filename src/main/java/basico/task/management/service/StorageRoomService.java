package basico.task.management.service;

import basico.task.management.model.primary.StorageRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

public interface StorageRoomService {

    Page<StorageRoom> findAllStorageRoom(Long societyId, Long promotionId, Pageable pageable);

    List<StorageRoom> findAllStorageRoomByName(Long societyId, Long promotionId, String name);

    StorageRoom findById(String storageRoom, Locale locale);

    Optional<StorageRoom> findByIdOptional(String code);

    StorageRoom save(StorageRoom storageSave);
}
