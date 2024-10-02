package basico.task.management.service;

import java.util.List;
import java.util.Locale;

import basico.task.management.dto.NewOrderAssetsDto;
import basico.task.management.model.primary.Assets;
import basico.task.management.projection.GarajProjection;
import basico.task.management.projection.PromotionProjection;
import basico.task.management.projection.SocietyAndLocationProjection;
import basico.task.management.projection.StorageRoomProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AssetsService {
    Page<Assets>findAll(Pageable pageable) ;
    Assets findById(Long id, Locale locale) throws Exception;
    Page<Assets> findAllByType(Pageable pageable, String type) throws Exception;
    Page<Assets> findAllByTypes(Pageable pageable, List<String> types) throws Exception;
    Page<Assets> filterNewOrders(Pageable pageable, List<String> types, String searchText) throws Exception;
    void delete(Long id, Locale locale) throws Exception;
	Assets save(NewOrderAssetsDto assets, Locale locale) throws Exception;
	Assets update(Long id, Assets acdcTask, Locale locale) throws Exception;
    List<PromotionProjection> findAllPromotion(String name);
    List<StorageRoomProjection> findAllStorage(String name);
    List<GarajProjection> findAllGaraje(String name);
    Assets updateStatus(Long id, Locale locale) throws Exception;
    List<SocietyAndLocationProjection> findAllLocation(String name);
    List<SocietyAndLocationProjection> findAllSociety(String name);

}
