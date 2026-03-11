package br.dev.smds.OSApiApplication.api.controller;

import br.dev.smds.OSApiApplication.domain.dto.AtualizaStatusDTO;
import br.dev.smds.OSApiApplication.domain.model.OrdemServico;
import br.dev.smds.OSApiApplication.domain.repository.OrdemServicoRepository;
import br.dev.smds.OSApiApplication.domain.service.OrdemServicoService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cliente")
public class OrdemServicoController {

    @Autowired
    private OrdemServicoRepository ordemServicoRepository;

    @Autowired
    private OrdemServicoService ordemServicoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrdemServico criar(@RequestBody OrdemServico ordemServico) {
        return ordemServicoService.criar(ordemServico);
    }

    @PutMapping("/ordem-servico/{ordemServicoID}")
    public ResponseEntity<OrdemServico> atualizar(@Valid @PathVariable Long ordemServicoID, @RequestBody OrdemServico ordemServico) {

        Optional<OrdemServico> ordemServicoOld = ordemServicoRepository.findById(ordemServicoID);

        if (ordemServicoOld.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        ordemServico.setId(ordemServicoID);
        ordemServico.setDataAbertura(ordemServicoOld.get().getDataAbertura());
        ordemServico.setStatus(ordemServicoOld.get().getStatus());
        ordemServico = ordemServicoRepository.save(ordemServico);
        return ResponseEntity.ok(ordemServico);
    }

    @DeleteMapping("/ordem-servico/{ordemServicoID}")
    public ResponseEntity<Void> excluir(@PathVariable Long ordemServicoID) {

        if (!ordemServicoRepository.existsById(ordemServicoID)) {
            return ResponseEntity.notFound().build();

        }
        ordemServicoService.excluir(ordemServicoID);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/ordem-servico/{ordemServicoID}")
    public ResponseEntity<OrdemServico> listarPorId(@PathVariable Long ordemServicoID) {
        Optional<OrdemServico> ordemServico = ordemServicoRepository.findById(ordemServicoID);

        if (ordemServico.isPresent()) {
            return ResponseEntity.ok(ordemServico.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/")
    public List<OrdemServico> listar() {
        List<OrdemServico> result = ordemServicoService.listar();
        return result;
    }

    @GetMapping("/{clienteID}/ordem-servico")
    public ResponseEntity<List<OrdemServico>> listarPorIdDoCliente(@PathVariable Long clienteID) {

        List<OrdemServico> ordemServicoCliente = ordemServicoRepository.findByClienteId(clienteID);
        return ResponseEntity.ok(ordemServicoCliente);
    }
    @PutMapping("/atualiza-status/{ordemServicoID}")
    public ResponseEntity<OrdemServico> atualizaStatus(@PathVariable Long ordemServicoID, @Valid @RequestBody AtualizaStatusDTO statusDTO) {
        Optional<OrdemServico> optOS = ordemServicoService.atualizaStatus(ordemServicoID, statusDTO.status());
        
        if (optOS.isPresent()) {
            return ResponseEntity.ok(optOS.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

