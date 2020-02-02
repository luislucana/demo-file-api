package br.com.exemplo.demofileapi.util.file.layout;

import java.util.List;

/**
 * Json to JAVA
 * http://pojo.sodhanalibrary.com/
 */
public class LayoutFile {

    private String separator;

    private Header header;

    private Detail detail;

    private List<Detail> details;

    private Trailler trailler;

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Detail getDetail() {
        return detail;
    }

    public void setDetail(Detail detail) {
        this.detail = detail;
    }

    public List<Detail> getDetails() {
        return details;
    }

    public void setDetails(List<Detail> details) {
        this.details = details;
    }

    public Trailler getTrailler() {
        return trailler;
    }

    public void setTrailler(Trailler trailler) {
        this.trailler = trailler;
    }

    @Override
    public String toString() {
        return "LayoutFile [separator = " + separator + ", header = " + header + ", details = "
                + details + ", trailler = " + trailler + "]";
    }
}
