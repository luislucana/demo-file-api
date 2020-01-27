package br.com.exemplo.demofileapi.service;

import br.com.exemplo.demofileapi.config.FileStorageProperties;
import br.com.exemplo.demofileapi.model.PessoaJuridica;
import br.com.exemplo.demofileapi.util.FileHelper;
import br.com.exemplo.demofileapi.util.FileSplitterV3;
import br.com.exemplo.demofileapi.validation.ValidationGroupSequence;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class FileService {

    private final Path fileStorageLocation;

    @Autowired
    public FileService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();

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

        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                //throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
                throw new RuntimeException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            File realFile = new File(file.getOriginalFilename());

            // Copy file to the target location (Replacing existing file with the same name)
            // Path targetLocation = this.fileStorageLocation.resolve(fileName);
            // Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            FileSplitterV3.splitFile(realFile, 1); // 1 MB

            File tempDir = FileUtils.getTempDirectory();
            FileUtils.copyFileToDirectory(realFile, tempDir);
            File newTempFile = FileUtils.getFile(tempDir, file.getName());

            String fileData = FileUtils.readFileToString(newTempFile, Charset.defaultCharset());

            List<String> linhasDoArquivo = FileUtils.readLines(newTempFile, Charset.defaultCharset());

            PessoaJuridica pessoaJuridica = FileHelper.extractPessoaJuridica(linhasDoArquivo.get(0));

            // TODO enviar um objeto para validar
            Set<ConstraintViolation<Object>> violations = validator.validate(pessoaJuridica, ValidationGroupSequence.class);

            return fileName;
        } catch (IOException ex) {
            //throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
            throw new RuntimeException("Could not store file " + fileName + ". Please try again!", ex);
        }
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
}