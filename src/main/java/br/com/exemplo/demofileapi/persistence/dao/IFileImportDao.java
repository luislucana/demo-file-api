package br.com.exemplo.demofileapi.persistence.dao;

import br.com.exemplo.demofileapi.persistence.model.FileImport;

import java.util.List;

public interface IFileImportDao {

    FileImport findOne(long id);

    List<FileImport> findAll();

    FileImport create(FileImport entity);

    FileImport update(FileImport entity);

    void delete(FileImport entity);

    void deleteById(long entityId);
}
