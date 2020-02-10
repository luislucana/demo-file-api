package br.com.exemplo.demofileapi.util.file.layout;

public class LayoutData {

    private String name;

    private String version;

    private String description;

    private LayoutFile layout;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LayoutFile getLayout() {
        return layout;
    }

    public void setLayout(LayoutFile layout) {
        this.layout = layout;
    }

    @Override
    public String toString() {
        return "LayoutData {" +
                "name='" + name + '\'' +
                ", version='" + version + '\'' +
                ", description='" + description + '\'' +
                ", layout=" + layout +
                '}';
    }
}
