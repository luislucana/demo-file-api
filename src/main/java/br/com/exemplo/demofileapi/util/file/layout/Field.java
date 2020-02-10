package br.com.exemplo.demofileapi.util.file.layout;

public class Field {

    private Integer size;

    private String name;

    private String fixedValue;

    private String description;

    private String type;

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFixedValue() {
        return fixedValue;
    }

    public void setFixedValue(String fixedValue) {
        this.fixedValue = fixedValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Field [size = " + size + ", name = " + name + ", fixedValue = " + fixedValue
                + ", description = " + description + ", type = " + type + "]";
    }
}
