package br.com.exemplo.demofileapi.util.file;

enum CSVHeaders {
    AUTHOR("author"), TITLE("title");

    final String value;

    CSVHeaders(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
