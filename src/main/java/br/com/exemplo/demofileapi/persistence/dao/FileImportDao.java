package br.com.exemplo.demofileapi.persistence.dao;

import br.com.exemplo.demofileapi.persistence.model.FileImport;

public class FileImportDao extends AbstractJpaDao<FileImport> implements IFileImportDao {
    public FileImportDao() {
        super();

        setClazz(FileImport.class);
    }

    // API
}
