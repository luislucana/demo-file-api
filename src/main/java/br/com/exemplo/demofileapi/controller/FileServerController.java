package br.com.exemplo.demofileapi.controller;

import br.com.exemplo.demofileapi.service.FileService;
import br.com.exemplo.demofileapi.to.UploadFileResponse;
import br.com.exemplo.demofileapi.util.file.layout.LayoutData;
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
import java.nio.file.Files;

@RestController
@RequestMapping("/demo-file-api")
public class FileServerController {

    @Autowired
    private FileService fileService;

    @GetMapping(value="/blah", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> doSomething() {
        return (new ResponseEntity<>("funcionou!", HttpStatus.OK));
    }

    /**
     * Para gerar mocks de arquivo:
     * https://mockaroo.com/
     * https://www.generatedata.com/
     *
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping(value = "/singlefileupload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody UploadFileResponse processFile(@RequestParam("file") MultipartFile file) throws IOException {

        System.out.println("File Name: " + file.getOriginalFilename());
        System.out.println("File Content Type: " + file.getContentType());
        //System.out.println("File Content:\n" + new String(bytes));

        UploadFileResponse uploadFileResponse = fileService.storeFile(file);

        return uploadFileResponse;
    }

    // TODO Implementar envio de layout?
    @PostMapping(value = "/layout", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createCustomLayout(@RequestBody String layoutData) throws IOException {

        System.out.println(layoutData);

        String response = fileService.createCustomLayout(layoutData);

        return (new ResponseEntity<>("Layout recebido com sucesso! (" + response + ")", null, HttpStatus.OK));
    }

    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = fileService.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
             contentType = Files.probeContentType(resource.getFile().toPath());
        } catch (IOException ex) {
            //logger.info("Could not determine file type.");
            // TODO Verificar se este erro deve ser tratado aqui ou no Service
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
