package br.com.exemplo.demofileapi.util.file.layout;

import br.com.exemplo.demofileapi.util.FileConstants;
import br.com.exemplo.demofileapi.util.exception.InvalidLayoutException;
import br.com.exemplo.demofileapi.util.file.FileHandlerFactory;
import br.com.exemplo.demofileapi.util.file.FileHandlerSingleton;
import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ansi.Ansi8BitColor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

/**
 * https://www.baeldung.com/spring-classpath-file-access
 */
@Component
public class LayoutValidator {

    //@Autowired
    //private ApplicationContext appContext;

    @Value("classpath:layouts/layout_txt.json")
    private Resource layoutTXT;

    @Value("classpath:layouts/layout_csv.json")
    private Resource layoutCSV;

    @Value("classpath:layouts/layout_xls.json")
    private Resource layoutXLS;

    public void validate(File file) throws InvalidLayoutException, IOException {
        // TODO terminar de implementar

        String extension = FilenameUtils.getExtension(file.getName());

        LayoutFile layoutFile = null;
        File jsonLayoutConfig = null;

        if (StringUtils.equalsIgnoreCase(extension, FileConstants.Extension.TXT)
                || StringUtils.equalsIgnoreCase(extension, FileConstants.Extension.CSV)) {

            if (StringUtils.equalsIgnoreCase(extension, FileConstants.Extension.TXT)) {
                jsonLayoutConfig = layoutTXT.getFile();
            } else {
                jsonLayoutConfig = layoutCSV.getFile();
            }

            String jsonConfig = FileUtils.readFileToString(jsonLayoutConfig, StandardCharsets.UTF_8);

            layoutFile = new Gson().fromJson(jsonConfig, LayoutFile.class);

            FileHandlerSingleton fileHandler = FileHandlerFactory.getFileHandler(extension);
            List<String> lines = fileHandler.read(file);

            Map<String, String> rowIdentifiers = layoutFile.getRowIdentifiers();
            Map<String, List<Field>> rows = layoutFile.getRowFields();

            if (rowIdentifiers.size() != rows.size()) {
                throw new RuntimeException("Configuracao de layout invalida!");
            }

        } else if (StringUtils.equalsIgnoreCase(extension, FileConstants.Extension.XLS)) {
            jsonLayoutConfig = layoutXLS.getFile();

            String jsonConfig = FileUtils.readFileToString(jsonLayoutConfig, StandardCharsets.UTF_8);

            layoutFile = new Gson().fromJson(jsonConfig, LayoutFile.class);

            FileInputStream fileInputStream = new FileInputStream(file);

            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheetAt(0);
            Row firstRow = sheet.getRow(sheet.getFirstRowNum());
            Row lastRow = sheet.getRow(sheet.getLastRowNum());

            validateHeaderXLS(layoutFile, firstRow);
            validateDetailXLS(layoutFile, sheet);
            validateTraillerXLS(layoutFile, lastRow);
        } else {
            // TODO throw exception!
        }

        System.out.println(layoutFile.toString());
    }

    /*private void validateHeader(final LayoutFile layout, final String firstLine) throws InvalidLayoutException {
        String[] headerTitlesFromFile = null;

        if (StringUtils.isNotBlank(layout.getSeparator())) {
            // TODO validar separador
            headerTitlesFromFile = firstLine.split(layout.getSeparator());
        } else {
            // TODO fazer split pela qtde de caracteres por nome de coluna
            //  (obter a qtde de caracteres no layout_[extensao].json)
        }

        Header header = layout.getHeader();
        List<Field> headerFields = header.getFields();

        for (Field headerField : headerFields) {
            String name = headerField.getName();
            Integer size = headerField.getSize();
            String type = headerField.getType();
            String fixedValue = headerField.getFixedValue();

            for (String headerTitle : headerTitlesFromFile) {
                if (fixedValue != null && !StringUtils.equals(headerTitle, fixedValue)) {
                    throw new InvalidLayoutException("Arquivo com layout invalido (valor informado nao corresponde ao valor fixo esperado).");
                }

                if (!StringUtils.equalsIgnoreCase(headerTitle, name)) {
                    throw new InvalidLayoutException("Arquivo com layout invalido.");
                }
                if (size != null && headerTitle.length() != size) {
                    throw new InvalidLayoutException("Arquivo com layout invalido.");
                }

                switch (type.toUpperCase()) {
                    case FileConstants.VariableType.NUMBER:
                        try {
                            Integer.valueOf(headerTitle);
                        } catch (NumberFormatException nfe) {
                            throw new InvalidLayoutException("Arquivo com layout invalido.", nfe);
                        }
                        break;
                    case FileConstants.VariableType.DECIMAL:
                        try {
                            new BigDecimal(headerTitle);
                        } catch (NumberFormatException nfe) {
                            throw new InvalidLayoutException("Arquivo com layout invalido.", nfe);
                        }
                        break;
                    case FileConstants.VariableType.DATE:
                        // TODO implementar
                        System.out.println("IMPLEMENTAR!");
                        break;
                    default:
                        break;
                }
            }
        }
    }*/

    private void validateDetail(final LayoutFile layout, List<String> lines) throws InvalidLayoutException {
        // TODO implementar
    }

    private void validateTrailler(final LayoutFile layout, final String lastLine) throws InvalidLayoutException {
        // TODO implementar
    }

    private void validateHeaderXLS(final LayoutFile layout, Row firstRow) throws InvalidLayoutException {
        // TODO implementar
    }

    private void validateDetailXLS(final LayoutFile layout, Sheet sheet) throws InvalidLayoutException {
        // TODO implementar
    }

    private void validateTraillerXLS(final LayoutFile layout, Row lastRow) throws InvalidLayoutException {
        // TODO implementar
    }
}
