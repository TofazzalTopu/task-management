package basico.task.management.service.glpi;

import basico.task.management.model.primary.GlpiTask;
import basico.task.management.model.primary.Status;
import basico.task.management.projection.GlpiFileResponse;

import java.io.InputStream;
import java.util.List;
import java.util.Locale;

public interface GlpiService {

    List<GlpiTask> taskList(Status status);

    public InputStream getGLPIFiles(Integer ticketId, String fileId);

    List<GlpiFileResponse> getAllGlpiFile(Long ticketId);
}
