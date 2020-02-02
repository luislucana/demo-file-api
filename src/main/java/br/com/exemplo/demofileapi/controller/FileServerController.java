package br.com.exemplo.demofileapi.controller;

import br.com.exemplo.demofileapi.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/demo-file-api")
public class FileServerController {

    @Autowired
    private FileService fileService;

    @GetMapping(value="/blah", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> doSomething() {
        return (new ResponseEntity<>("funcionou!", HttpStatus.OK));
    }

    @PostMapping(value = "/singlefileupload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> processFile(@RequestParam("file") MultipartFile file) throws IOException {

        //byte[] bytes = file.getBytes();

        System.out.println("File Name: " + file.getOriginalFilename());
        System.out.println("File Content Type: " + file.getContentType());
        //System.out.println("File Content:\n" + new String(bytes));

        String arquivo = fileService.storeFile(file);

        return (new ResponseEntity<>("Arquivo processado com sucesso", null, HttpStatus.OK));
    }

    @RequestMapping(path = "/multiplefileupload/", method = RequestMethod.POST)
    public ResponseEntity<String> processFile(@RequestParam("files") List<MultipartFile> files) throws IOException {

        for (MultipartFile file : files) {
            byte[] bytes = file.getBytes();

            System.out.println("File Name: " + file.getOriginalFilename());
            System.out.println("File Content Type: " + file.getContentType());
            System.out.println("File Content:\n" + new String(bytes));
        }

        return (new ResponseEntity<>("Successful", null, HttpStatus.OK));
    }

    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = fileService.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            //logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
