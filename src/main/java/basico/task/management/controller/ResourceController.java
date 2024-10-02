package basico.task.management.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping(value = "/resource")
public class ResourceController {

    private final ResourceLoader resourceLoader;

    public ResourceController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @GetMapping("/{fileName}")
    public void viewFile(@PathVariable String fileName,HttpServletRequest request,
                             HttpServletResponse response) throws IOException {
        Resource resource = resourceLoader
                .getResource("classpath:" +"static/"+fileName);
        if (resource.exists()) {
            response.setHeader("Content-Disposition", String.format("inline; filename=\"" + "" + "\""));
            response.setContentLength((int) resource.contentLength());
            response.setContentType("image/x-png");
            InputStream inputStream = resource.getInputStream();
            FileCopyUtils.copy(inputStream, response.getOutputStream());
        }
    }

}
