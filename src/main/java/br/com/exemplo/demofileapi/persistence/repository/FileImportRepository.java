package br.com.exemplo.demofileapi.persistence.repository;

import br.com.exemplo.demofileapi.persistence.model.FileImport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileImportRepository extends JpaRepository<FileImport, Long> {

}
