package br.com.exemplo.demofileapi.persistence.model;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

/**
 * As stated in the JPA specification under section 2.9, it's a good practice to mark many-to-one side as the owning side.
 */
@Entity
@Table(name = "FILE_IMPORT_DATA")
public class FileImportData {

    @Id
    @SequenceGenerator(name = "seqGeneratorFID", sequenceName = "FILEIMPORTDATA_SEQ", initialValue = 1, allocationSize = 1)
    @GeneratedValue(generator = "seqGeneratorFID")
    private long fileImportDataId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FILE_IMPORT_ID", nullable = false)
    private FileImport fileImport;

    @Lob
    @Column(nullable = false)
    private String row;

    //private String sectionName;

    //private Integer rowNumber;

    //private String columnName;

    //private String value;

    @Column(nullable = false)
    @CreationTimestamp
    private Date operationDate;

    public long getFileImportDataId() {
        return fileImportDataId;
    }

    public void setFileImportDataId(long fileImportDataId) {
        this.fileImportDataId = fileImportDataId;
    }

    public FileImport getFileImport() {
        return fileImport;
    }

    public void setFileImport(FileImport fileImport) {
        this.fileImport = fileImport;
    }

    public String getRow() {
        return row;
    }

    public void setRow(String row) {
        this.row = row;
    }

    public Date getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(Date operationDate) {
        this.operationDate = operationDate;
    }
// file_import_id | section_name   | rowNumber | column_name     | valor
    // 1              | Header         | 1         | ID              | 0
    // 1              | Header         | 1         | NOME            | Fulano de tal
    // 1              | Header         | 1         | DATA_NASCIMENTO | 01011980
    // 1              | Header         | 1         | ENDERECO        | Rua Sem Saida
    // 1              | Detail         | 2         | ID              | 1
    // 1              | Detail         | 2         | NOME            | Fulano de tal
    // 1              | Detail         | 2         | DATA_NASCIMENTO | 01011980
    // 1              | Detail         | 2         | ENDERECO        | Rua Sem Saida
    // 1              | Trailler       | 3         | ID              | 1
    // 1              | Trailler       | 3         | NOME            | Fulano de tal
    // 1              | Trailler       | 3         | DATA_NASCIMENTO | 01011980
    // 1              | Trailler       | 3         | ENDERECO        | Rua Sem Saida
    // 2              | Header         | 1         | ID              | 0
    // 2              | Header         | 1         | NOME            | Fulano de tal
    // 2              | Header         | 1         | DATA_NASCIMENTO | 01011980
    // 2              | Header         | 1         | ENDERECO        | Rua Sem Saida
    // 2              | Detail         | 2         | ID              | 1
    // 2              | Detail         | 2         | NOME            | Fulano de tal
    // 2              | Detail         | 2         | DATA_NASCIMENTO | 01011980
    // 2              | Detail         | 2         | ENDERECO        | Rua Sem Saida
    // 2              | Trailler       | 3         | ID              | 1
    // 2              | Trailler       | 3         | NOME            | Fulano de tal
    // 2              | Trailler       | 3         | DATA_NASCIMENTO | 01011980
    // 2              | Trailler       | 3         | ENDERECO        | Rua Sem Saida
}
