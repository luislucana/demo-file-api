package br.com.exemplo.demofileapi.util;

import java.nio.file.Paths;

/**
 *
 *
 */
public class FileConstants {

    public static final Character CSV_SEPARATOR = ';';

    public static final String DEFAULT_DIRECTORY =
            Paths.get(System.getProperty("user.home") + "/arquivosteste/").toAbsolutePath().toString();

    // Supported file extensions for this app
    public static final class Extension {
        public static final String TXT = "txt";
        public static final String CSV = "csv";
        public static final String XLS = "xls";
    }

}
