package br.com.exemplo.demofileapi.persistence.model;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "FILE_IMPORT")
public class FileImport implements Serializable {

    @Id
    @SequenceGenerator(name = "seqGeneratorFI", sequenceName = "FILEIMPORT_SEQ", initialValue = 1, allocationSize = 1)
    @GeneratedValue(generator = "seqGeneratorFI")
    private long fileImportId;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String filename;

    @Column
    private String sizeInKb;

    @Column(nullable = false)
    private String fileExtension;

    @Column
    private String separator;

    @ManyToOne
    @JoinColumn(name = "layout_id", referencedColumnName = "layout_id", nullable = false)
    private Layout layout;

    @OneToMany(mappedBy = "fileImport", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    //@OrderBy("rowNumber ASC")
    private List<FileImportData> fileImportData;

    @CreationTimestamp
    private Date operationDate;

    public long getFileImportId() {
        return fileImportId;
    }

    public void setFileImportId(long fileImportId) {
        this.fileImportId = fileImportId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getSizeInKb() {
        return sizeInKb;
    }

    public void setSizeInKb(String sizeInKb) {
        this.sizeInKb = sizeInKb;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public Layout getLayout() {
        return layout;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
    }

    public Date getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(Date operationDate) {
        this.operationDate = operationDate;
    }

    public List<FileImportData> getFileImportData() {
        return fileImportData;
    }

    public void setFileImportData(List<FileImportData> fileImportData) {
        this.fileImportData = fileImportData;
    }
}
