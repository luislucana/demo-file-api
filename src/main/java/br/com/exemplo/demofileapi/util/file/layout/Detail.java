package br.com.exemplo.demofileapi.util.file.layout;

import java.util.List;

public class Detail {

    private List<Field> fields;

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    @Override
    public String toString() {
        return "Detail {" +
                "fields=" + fields +
                '}';
    }
}
