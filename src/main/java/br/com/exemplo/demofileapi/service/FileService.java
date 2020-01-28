package br.com.exemplo.demofileapi.service;

import br.com.exemplo.demofileapi.config.FileStorageProperties;
import br.com.exemplo.demofileapi.util.FileSplitterV3;
import br.com.exemplo.demofileapi.util.file.CustomFileHandler;
import br.com.exemplo.demofileapi.util.file.FileHandlerFactory;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.LineIterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Validation;
import javax.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class FileService {

    private final Path fileStorageLocation;

    @Autowired
    public FileService(FileStorageProperties fileStorageProperties) {
        //this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                //.toAbsolutePath().normalize();
        this.fileStorageLocation = Paths.get(System.getProperty("user.home") + "/arquivosteste").toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            //throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public String storeFile(MultipartFile file) {
        Optional<String> originalFilename = Optional.ofNullable(file.getOriginalFilename());

        // Normalize file name
        String fileName = StringUtils.cleanPath(originalFilename.orElse(""));

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        // Check if the file's name contains invalid characters
        if (fileName.contains("..")) {
            //throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            throw new RuntimeException("Sorry! Filename contains invalid path sequence " + fileName);
        }

        File realFile = multipartFileToFile(file);
        String extension = FilenameUtils.getExtension(fileName);

        CustomFileHandler fileHandler = FileHandlerFactory.getFileHandler(extension);
        List<String> lines = fileHandler.read(realFile);

        // https://blog.caelum.com.br/entendendo-unicode-e-os-character-encodings/


        // Copy file to the target location (Replacing existing file with the same name)
        // Path targetLocation = this.fileStorageLocation.resolve(fileName);
        // Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        //FileSplitterV3.splitFile(realFile, 10); // 10 KB

        try {
            fileHandler.split(realFile, 10);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //File tempDir = FileUtils.getTempDirectory();
        //FileUtils.copyFileToDirectory(realFile, tempDir);
        //File newTempFile = FileUtils.getFile(tempDir, file.getName());

        //String fileData = FileUtils.readFileToString(newTempFile, Charset.defaultCharset());

        //List<String> linhasDoArquivo = FileUtils.readLines(newTempFile, Charset.defaultCharset());

        //PessoaJuridica pessoaJuridica = FileHelper.extractPessoaJuridica(linhasDoArquivo.get(0));

        // TODO enviar um objeto para validar
        //Set<ConstraintViolation<Object>> violations = validator.validate(pessoaJuridica, ValidationGroupSequence.class);

        return fileName;
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                //throw new MyFileNotFoundException("File not found " + fileName);
                throw new RuntimeException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            //throw new MyFileNotFoundException("File not found " + fileName, ex);
            throw new RuntimeException("File not found " + fileName, ex);
        }
    }

    public File multipartFileToFile(MultipartFile mpfile) {

        File file = new File(fileStorageLocation.toString(), mpfile.getOriginalFilename());

        // Create the file using the touch method of the FileUtils class.
        // FileUtils.touch(file);

        // Write bytes from the multipart file to disk.
        try {
            FileUtils.writeByteArrayToFile(file, mpfile.getBytes());
        } catch (IOException e) {
            file = null;
        }

        return file;
    }
}