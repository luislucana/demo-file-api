package br.com.exemplo.demofileapi.service;

import br.com.exemplo.demofileapi.persistence.model.FileImport;
import br.com.exemplo.demofileapi.persistence.model.FileImportData;
import br.com.exemplo.demofileapi.persistence.model.Layout;
import br.com.exemplo.demofileapi.persistence.repository.FileImportRepository;
import br.com.exemplo.demofileapi.persistence.repository.LayoutRepository;
import br.com.exemplo.demofileapi.to.UploadFileResponse;
import br.com.exemplo.demofileapi.util.FileConstants;
import br.com.exemplo.demofileapi.util.file.FileHandlerFactory;
import br.com.exemplo.demofileapi.util.file.FileHandlerSingleton;
import br.com.exemplo.demofileapi.util.file.layout.LayoutData;
import br.com.exemplo.demofileapi.util.file.layout.LayoutValidator;
import com.google.gson.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

/**
 * Verificar
 * https://www.java-success.com/processing-large-files-efficiently-java-part-1/
 */
@Service
@Transactional
public class FileService {

    private final Path fileStorageLocation;

    @Autowired
    private LayoutValidator layoutValidator;

    @Autowired
    private FileImportRepository fileImportRepository;

    @Autowired
    private LayoutRepository layoutRepository;

    private GsonBuilder builder = new GsonBuilder();

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

    @Transactional
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
            //layoutValidator.validate(file);

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
                //fileHandler.splitAndStore(file, 10);// 10 KB
                //FileHelper.createTextFiles(filePartsMap);
            } else {
                // file split not required
                //FileUtils.copyFileToDirectory(file, fileStorageLocation.toFile());
                Files.copy(file.toPath(), fileStorageLocation.resolve(file.getName()), StandardCopyOption.REPLACE_EXISTING);
            }

            persistFile(file);

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

    private void persistFile(File file) throws IOException {
        String extension = FilenameUtils.getExtension(file.getName());

        List<String> lines = FileUtils.readLines(file, StandardCharsets.UTF_8);

        long fileSize = FileUtils.sizeOf(file); // bytes

        Layout layout = layoutRepository.getOne((long) 50);

        FileImport fileImport = new FileImport();
        fileImport.setFileExtension(extension);
        fileImport.setFilename(file.getName());
        fileImport.setSeparator(";");
        fileImport.setSizeInKb(String.valueOf(fileSize / 1024));
        fileImport.setUsername("[NOME DO USUARIO]");
        fileImport.setLayout(layout);

        List<FileImportData> fileImportDataList = new ArrayList<>();
        for (String line : lines) {
            FileImportData fileImportData = new FileImportData();
            fileImportData.setFileImport(fileImport);
            fileImportData.setRow(line);
            fileImportDataList.add(fileImportData);
        }
        fileImport.setFileImportData(fileImportDataList);

        FileImport savedFileImport = fileImportRepository.save(fileImport);
    }

    public String createCustomLayout(final String customLayoutData) throws IOException {

        //File customLayoutFile = multipartFileToFile(layoutMultipartFile);

        //String customLayout = FileUtils.readFileToString(customLayoutFile, StandardCharsets.UTF_8);

        try {
            Gson gson = builder.create();
            LayoutData layoutData = gson.fromJson(customLayoutData, LayoutData.class);
            System.out.println("deu certo");

            List<String> atributosEsperadosJson = new ArrayList<>();
            atributosEsperadosJson.add("name");
            atributosEsperadosJson.add("description");
            atributosEsperadosJson.add("version");
            atributosEsperadosJson.add("layout");
            atributosEsperadosJson.add("separator");

            JsonElement element = JsonParser.parseString(customLayoutData);
            JsonObject obj = element.getAsJsonObject(); // since you know it's a JsonObject
            Set<Map.Entry<String, JsonElement>> entries = obj.entrySet(); // will return members of your object

            for (Map.Entry<String, JsonElement> entry: entries) {
                //if (!entry.getValue().isJsonPrimitive()) {
                if (entry.getKey().equals("layout")) {
                    JsonElement jsonElement = entry.getValue();
                    JsonObject layoutJsonObject = jsonElement.getAsJsonObject();
                    Set<Map.Entry<String, JsonElement>> layoutEntries = layoutJsonObject.entrySet();

                    for (Map.Entry<String, JsonElement> layoutEntry : layoutEntries) {
                        if (!atributosEsperadosJson.contains(layoutEntry.getKey())) {
                            throw new RuntimeException("Atributo " + layoutEntry.getKey() + " desconhecido!!");
                        }
                    }
                }

                if (!atributosEsperadosJson.contains(entry.getKey())) {
                    throw new RuntimeException("Atributo " + entry.getKey() + " desconhecido!");
                }
            }

            System.out.println("deu certo tb");

        } catch (JsonSyntaxException jsonSyntaxException) {
            throw new RuntimeException("Layout invalido!");
        }

        return "sucesso";
    }

    private void persistLayout(final LayoutData layoutData) {
        layoutRepository.save(layoutData)
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