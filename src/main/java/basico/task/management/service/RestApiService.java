package basico.task.management.service;

import basico.task.management.model.primary.AcdcTask;
import basico.task.management.model.primary.Status;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.text.ParseException;
import java.util.List;

public interface RestApiService {

    List<AcdcTask> listAcdcTask(Status status) throws JsonProcessingException, ParseException;

}
