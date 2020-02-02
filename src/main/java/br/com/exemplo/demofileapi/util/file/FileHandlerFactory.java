package br.com.exemplo.demofileapi.util.file;

import br.com.exemplo.demofileapi.util.FileConstants;
import org.apache.commons.lang3.StringUtils;

/**
 *
 */
public class FileHandlerFactory {

    public static FileHandlerSingleton getFileHandler(String extension) {

        FileHandlerSingleton fileHandler = null;

        if (StringUtils.isBlank(extension)) {
            throw new RuntimeException("Extensao de arquivo invalida.");
        }

        extension = extension.toLowerCase();

        switch (extension) {
            case FileConstants.Extension.TXT:
                //fileHandler = new TXTFileHandler();
                fileHandler = FileHandlerSingleton.TXT_INSTANCE;
                break;
            case FileConstants.Extension.CSV:
                //fileHandler = new CSVFileHandler();
                fileHandler = FileHandlerSingleton.CSV_INSTANCE;
                break;
            case FileConstants.Extension.XLS:
                //fileHandler = new XLSFileHandler();
                fileHandler = FileHandlerSingleton.XLS_INSTANCE;
                break;
            default:
                throw new RuntimeException("Extensao invalida/nao suportada.");
        }

        return fileHandler;
    }
}
