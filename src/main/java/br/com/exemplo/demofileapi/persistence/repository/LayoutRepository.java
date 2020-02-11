package br.com.exemplo.demofileapi.persistence.repository;

import br.com.exemplo.demofileapi.persistence.model.Layout;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LayoutRepository extends JpaRepository<Layout, Long> {

}
