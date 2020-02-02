package br.com.exemplo.demofileapi.service;

import br.com.exemplo.demofileapi.to.UploadFileResponse;
import br.com.exemplo.demofileapi.util.FileConstants;
import br.com.exemplo.demofileapi.util.Utils;
import br.com.exemplo.demofileapi.util.file.FileHandlerFactory;
import br.com.exemplo.demofileapi.util.file.FileHandlerSingleton;
import br.com.exemplo.demofileapi.util.file.FileHelper;
import br.com.exemplo.demofileapi.util.file.layout.LayoutValidator;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Verificar
 * https://www.java-success.com/processing-large-files-efficiently-java-part-1/
 */
@Service
public class FileService {

    private final Path fileStorageLocation;

    @Autowired
    private LayoutValidator layoutValidator;

    @Autowired
    public FileService() {
        this.fileStorageLocation =
            Paths.get(FileConstants.DEFAULT_DIRECTORY).toAbsolutePath().normalize();

        try {
            Files.createDirectories(Paths.get(FileConstants.DEFAULT_DIRECTORY));
        } catch (Exception ex) {
            //throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public UploadFileResponse storeFile(MultipartFile multipartFile) {
        UploadFileResponse uploadFileResponse = null;

        String fileName = null;
        String extension = null;
        long fileSize = 0;

        try {
            Optional<String> originalFilename = Optional.ofNullable(multipartFile.getOriginalFilename());

            System.out.println("Lendo arquivo " + originalFilename.get());

            // Normalize file name
            fileName = StringUtils.cleanPath(originalFilename.orElse(""));

            File file = multipartFileToFile(multipartFile);
            extension = FilenameUtils.getExtension(fileName);

            // validar layout
            layoutValidator.validate(file);

            // Copy file to the target location (Replacing existing file with the same name)
            // Path targetLocation = this.fileStorageLocation.resolve(fileName);
            //Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            long kbPerSplit = 10L;
            final long bytesPerSplit = 1024L * kbPerSplit;

            // (1) Get size from standard java io
            // long fileSize = file.length(); // in bytes

            // (2) With java NIO
            //final FileChannel fileChannel = FileChannel.open(file.toPath());
            //final long fileSize = fileChannel.size();

            // (3) Apache Commons IO
            fileSize = FileUtils.sizeOf(file); // bytes

            if (fileSize > bytesPerSplit) {
                // file split required
                FileHandlerSingleton fileHandler = FileHandlerFactory.getFileHandler(extension);
                fileHandler.splitAndStore(file, 10);// 10 KB
                //FileHelper.createTextFiles(filePartsMap);
            } else {
                // file split not required
                //FileUtils.copyFileToDirectory(file, fileStorageLocation.toFile());
                Files.copy(file.toPath(), fileStorageLocation.resolve(file.getName()), StandardCopyOption.REPLACE_EXISTING);
            }

            //File tempDir = FileUtils.getTempDirectory();
            //FileUtils.copyFileToDirectory(realFile, tempDir);
            //File newTempFile = FileUtils.getFile(tempDir, file.getName());

            //String fileData = FileUtils.readFileToString(newTempFile, Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
            // TODO implementar
        }

        return new UploadFileResponse(fileName, "", extension, fileSize, "Upload realizado com sucesso.");
    }

    public Resource loadFileAsResource(String fileName) {
        Resource resource = null;

        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();

            resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                //throw new MyFileNotFoundException("File not found " + fileName);
                throw new RuntimeException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            //throw new MyFileNotFoundException("File not found " + fileName, ex);
            throw new RuntimeException("File not found " + fileName, ex);
        }

        return resource;
    }

    // TODO Esta incompleto, nao funciona!
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