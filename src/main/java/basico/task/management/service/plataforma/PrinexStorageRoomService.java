package basico.task.management.service.plataforma;

import basico.task.management.model.plataforma.PrinexStorageRoom;
import basico.task.management.model.primary.StorageRoom;

import java.util.List;

public interface PrinexStorageRoomService {

    List<StorageRoom> findAllStorageRoom();
}
