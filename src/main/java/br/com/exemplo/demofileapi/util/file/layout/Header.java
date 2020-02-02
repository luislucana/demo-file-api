package br.com.exemplo.demofileapi.util.file.layout;

import java.util.List;

public class Header {

    private List<Field> fields;

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    @Override
    public String toString() {
        return "Header [fields = " + fields + "]";
    }
}
