package br.com.exemplo.demofileapi.util.file;

import br.com.exemplo.demofileapi.util.FileConstants;
import org.apache.commons.lang3.StringUtils;

/**
 *
 */
public class FileHandlerFactory {

    public static CustomFileHandler getFileHandler(String extension) {

        CustomFileHandler fileReader = null;

        if (StringUtils.isBlank(extension)) {
            throw new RuntimeException("Extensao nao informada.");
        }

        extension = extension.toLowerCase();

        switch (extension) {
            case FileConstants.Extension.TXT:
                fileReader = new TXTFileHandler();
                break;
            case FileConstants.Extension.CSV:
                fileReader = new CSVFileHandler();
                break;
            case FileConstants.Extension.XLS:
                fileReader = new XLSFileHandler();
                break;
            default:
                throw new RuntimeException("Extensao invalida/nao suportada.");
        }

        return fileReader;
    }
}
