package br.dev.smds.OSApiApplication.domain.service;

import br.dev.smds.OSApiApplication.domain.exception.DomainException;
import br.dev.smds.OSApiApplication.domain.model.OrdemServico;
import br.dev.smds.OSApiApplication.domain.model.StatusOrdemServico;
import br.dev.smds.OSApiApplication.domain.repository.ClienteRepository;
import br.dev.smds.OSApiApplication.domain.repository.OrdemServicoRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class OrdemServicoService {

    @Autowired
    private OrdemServicoRepository ordemServicoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    public OrdemServico criar(OrdemServico OrdemServico) {
        OrdemServico.setStatus(StatusOrdemServico.ABERTA);
        OrdemServico.setDataAbertura(LocalDateTime.now());

        return ordemServicoRepository.save(OrdemServico);
    }

    public OrdemServico atualizar(OrdemServico OrdemServico) {
        OrdemServico.setDataFinalizacao(LocalDateTime.now());
        OrdemServico.setStatus(StatusOrdemServico.FINALIZADA);

        return ordemServicoRepository.save(OrdemServico);
    }

    public void excluir(Long ordemServicoID) {
        ordemServicoRepository.deleteById(ordemServicoID);
    }

    public List<OrdemServico> listarPorId() {
        return ordemServicoRepository.findAll();
    }

    public List<OrdemServico> listar() {
        List<OrdemServico> result = ordemServicoRepository.findAll();
        return result;
    }

    public List<OrdemServico> listaPorCliente(Long clienteId) {
        if (!clienteRepository.existsById(clienteId)) {
            return (List<OrdemServico>) ResponseEntity.notFound().build();
        }

        return ordemServicoRepository.findByClienteId(clienteId);
    }

    public Optional<OrdemServico> atualizaStatus(Long ordemServicoID, StatusOrdemServico status) {
        Optional<OrdemServico> optOrdemServico = ordemServicoRepository.findById(ordemServicoID);

        if (optOrdemServico.isPresent()) {
            OrdemServico ordemServico = optOrdemServico.get();

            if (ordemServico.getStatus() == StatusOrdemServico.ABERTA && status != StatusOrdemServico.ABERTA) {
                
                ordemServico.setStatus(status);
                ordemServico.setDataFinalizacao(LocalDateTime.now());
                ordemServicoRepository.save(ordemServico);
                
                return Optional.of(ordemServico);
                
            } else {
                return Optional.empty();
            }

        } else {
            throw new DomainException("Não existe OS com o id " + ordemServicoID);
        }
    }

}
