package ignorar.br.com.exemplo.demofileapi.util;

import java.io.File;

public class StringInutils {

    static final String TAB = "\t";

    public static String getCompanyCodeFromFileMapping(File aInFile) {
        return aInFile.getName().
                substring(aInFile.getName().indexOf("_") + 1, aInFile.getName().indexOf(".")) + "";
    }

    public static String getPremnoFromline(String aInLine) {
        return aInLine.substring(0, aInLine.indexOf(TAB));
    }
}