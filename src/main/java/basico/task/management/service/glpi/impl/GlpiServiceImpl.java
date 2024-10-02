package basico.task.management.service.glpi.impl;


import basico.task.management.model.primary.Status;
import basico.task.management.projection.GlpiFileResponse;
import basico.task.management.model.primary.*;
import basico.task.management.projection.GlpiResponse;
import basico.task.management.repository.glpi.GlpiRepository;
import basico.task.management.service.*;
import basico.task.management.service.glpi.GlpiService;
import basico.task.management.util.DocumentLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GlpiServiceImpl implements GlpiService {

    private final GlpiRepository glpiRepository;
    private final DocumentLoader documentLoader;
    @Value("${url.glpi.basico}")
    private String glpiUrl;
    @Value("${url.glpi.document.download}")
    private String downloadUrl;

    private final SocietyService societyService;
    private final PromotionService promotionService;
    private final LocationService locationService;
    private final StorageRoomService storageRoomService;
    private final GarajeService garajeService;


    @Override
    public List<GlpiTask> taskList(Status status) {
        List<GlpiResponse> giplResponses = glpiRepository.findAllTickets();
        List<GlpiTask> glpiTasks = new LinkedList<>();
        for (GlpiResponse gipl : giplResponses) {
            GlpiTask glpiTask = new GlpiTask();
            glpiTask.setStatus(status);
            glpiTask.setDescription(gipl.getDescription());
            glpiTask.setCode(gipl.getCode());
            glpiTask.setTicketId(gipl.getTicketId());
            glpiTask.setTicketName(gipl.getTicketName());
            glpiTask.setEmail(gipl.getEmail());
            glpiTask.setUserGroup(gipl.getUserGroup());
            if(gipl.getCompleteName().contains(">")){
                String[] data=gipl.getCompleteName().split(">");
                Optional<Society> society=societyService.findByIdOptional(Long.parseLong(data[0].split("-")[0].trim()));
                if(society.isPresent()){
                    glpiTask.setSociety(society.get());
                }
                Optional<Promotion> promotion= promotionService.findByIdOptional(Long.parseLong(data[1].split("-")[0].trim()));
                if(promotion.isPresent()){
                    glpiTask.setPromotion(promotion.get());
                }
                if(Objects.nonNull(gipl.getCode())&&!gipl.getCode().isEmpty()){
                    if(promotion.isPresent()){
                        gipl.getCode().replace(promotion.get().getId()+"-","");;
                    }
                    if (gipl.getCode().contains("VI")) {
                        Optional<Location> location = locationService.findById(gipl.getCode());
                        if (location.isPresent()) {
                            glpiTask.setLocation(location.get());
                        }
                    } else if (gipl.getCode().contains("TR")) {
                        Optional<StorageRoom> storageRoom = storageRoomService.findByIdOptional(gipl.getCode());
                        if (storageRoom.isPresent()) {
                            glpiTask.setStorageRoom(storageRoom.get());
                        }
                    } else if (gipl.getCode().contains("GA")) {
                        Optional<Garaje> garaje = garajeService.findByIdOptional(gipl.getCode());
                        if (garaje.isPresent()) {
                            glpiTask.setGarajes(garaje.get());
                        }
                    }
                }
            }else {
               if(!gipl.getCompleteName().equalsIgnoreCase("BASICO")){
                   Optional<Society> society=societyService.findByIdOptional(Long.parseLong(gipl.getCompleteName().split("-")[0].trim()));
                   if(society.isPresent()){
                       glpiTask.setSociety(society.get());
                   }
               }
            }
            glpiTask.setCity(gipl.getCity());
            glpiTask.setProvince(gipl.getProvince());
            glpiTask.setDirection(gipl.getDirection());
            glpiTasks.add(glpiTask);
        }
        return glpiTasks;
    }


    public InputStream getGLPIFiles(Integer ticketId, String fileId) {
        InputStream in = null;
        String urlString = glpiUrl+ downloadUrl.replace("{id}", fileId).replace("{ticket_id}", ticketId.toString());
        in = documentLoader.getFiles(urlString);
        return in;
    }



    @Override
    public List<GlpiFileResponse> getAllGlpiFile(Long ticketId) {
        return glpiRepository.findAllGlpiFile(ticketId);
    }

}
