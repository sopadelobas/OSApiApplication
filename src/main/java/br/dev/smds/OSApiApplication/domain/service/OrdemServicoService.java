
package br.dev.smds.OSApiApplication.domain.service;

import br.dev.smds.OSApiApplication.domain.model.OrdemServico;
import br.dev.smds.OSApiApplication.domain.model.StatusOrdemServico;
import br.dev.smds.OSApiApplication.domain.repository.OrdemServicoRepository;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrdemServicoService {
    
    @Autowired
    private OrdemServicoRepository ordemServicoRepository;
    
    public OrdemServico criar(OrdemServico OrdemServico) {
        OrdemServico.setStatus(StatusOrdemServico.ABERTA);
        OrdemServico.setDataAbertura(LocalDateTime.now());
        
        return ordemServicoRepository.save(OrdemServico);
    }
}
