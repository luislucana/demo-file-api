package br.com.exemplo.demofileapi.upload;

import br.com.exemplo.demofileapi.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/demo-file-api")
public class FileServerResource {

    @Autowired
    private FileService fileService;

    @RequestMapping(path = "/singlefileupload/", method = RequestMethod.POST)
    public ResponseEntity<String> processFile(@RequestParam("file") MultipartFile file) throws IOException {

        byte[] bytes = file.getBytes();

        System.out.println("File Name: " + file.getOriginalFilename());
        System.out.println("File Content Type: " + file.getContentType());
        System.out.println("File Content:\n" + new String(bytes));

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
}
