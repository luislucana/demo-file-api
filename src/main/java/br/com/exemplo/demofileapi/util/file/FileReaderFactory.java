package br.com.exemplo.demofileapi.util.file;

import br.com.exemplo.demofileapi.util.FileConstants;
import org.apache.commons.lang3.StringUtils;

/**
 *
 */
public class FileReaderFactory {

    public static CustomFileReader getFileReader(String extension) {

        CustomFileReader fileReader = null;

        if (StringUtils.isBlank(extension)) {
            throw new RuntimeException("Extensao nao informada.");
        }

        extension = extension.toUpperCase();

        switch (extension) {
            case FileConstants.Extension.TXT:
                fileReader = new TXTFileReader();
                break;
            case FileConstants.Extension.CSV:
                fileReader = new CSVFileReader();
                break;
            case FileConstants.Extension.XLS:
                fileReader = new XLSFileReader();
                break;
            default:
                throw new RuntimeException("Extensao invalida/nao suportada.");
        }

        return fileReader;
    }
}
