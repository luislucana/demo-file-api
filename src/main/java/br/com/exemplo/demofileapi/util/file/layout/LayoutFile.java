package br.com.exemplo.demofileapi.util.file.layout;

import java.util.List;
import java.util.Map;

/**
 * Json to JAVA
 * http://pojo.sodhanalibrary.com/
 */
public class LayoutFile {

    private String separator;

    private Map<String, String> rowIdentifiers;

    private Map<String, List<Field>> rowFields;

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public Map<String, String> getRowIdentifiers() {
        return rowIdentifiers;
    }

    public void setRowIdentifiers(Map<String, String> rowIdentifiers) {
        this.rowIdentifiers = rowIdentifiers;
    }

    public Map<String, List<Field>> getRowFields() {
        return rowFields;
    }

    public void setRowFields(Map<String, List<Field>> rowFields) {
        this.rowFields = rowFields;
    }

//@Override
    //public String toString() {
//        return "LayoutFile [separator = " + separator + ", header = " + header + ", detail = "
//                + detail + ", trailler = " + trailler + "]";
//    }

    @Override
    public String toString() {
        return "LayoutFile {" +
                "separator='" + separator + '\'' +
                ", rowIdentifiers=" + rowIdentifiers +
                ", rowFields=" + rowFields +
                '}';
    }
}
