
package br.dev.smds.OSApiApplication.domain.repository;

import br.dev.smds.OSApiApplication.domain.model.OrdemServico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdemServicoRepository extends JpaRepository<OrdemServico, Long> {
    
}
