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

    public static final class VariableType {
        public static final String STRING = "STRING";
        public static final String NUMBER = "NUMBER";
        public static final String DECIMAL = "DECIMAL";
        public static final String DATE = "DATE";
    }
}
