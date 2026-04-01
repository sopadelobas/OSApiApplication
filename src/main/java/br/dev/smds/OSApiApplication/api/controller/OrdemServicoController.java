package br.dev.smds.OSApiApplication.api.controller;

import br.dev.smds.OSApiApplication.domain.dto.AtualizaStatusDTO;
import br.dev.smds.OSApiApplication.domain.model.OrdemServico;
import br.dev.smds.OSApiApplication.domain.repository.OrdemServicoRepository;
import br.dev.smds.OSApiApplication.domain.service.OrdemServicoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
@SecurityRequirement(name = "ApiKeyAuth")
public class OrdemServicoController {

    @Autowired
    private OrdemServicoRepository ordemServicoRepository;

    @Autowired
    private OrdemServicoService ordemServicoService;
    
//-----------------------------------------------------------------------------------------------------------------------------------//
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Criar um pedido", description = "Cria um pedido no repositório")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Sucesso na criação"),
        @ApiResponse(responseCode = "404", description = "Não foi possível criar o pedido")
    })
    public OrdemServico criar(@RequestBody OrdemServico ordemServico) {
        return ordemServicoService.criar(ordemServico);
    }
    
//-----------------------------------------------------------------------------------------------------------------------------------//    
    
    @PutMapping("/ordem-servico/{ordemServicoID}")
    @Operation(summary = "Atualizar um pedido", description = "Atualiza as informações de um pedido no repositório")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Atualizado!"),
        @ApiResponse(responseCode = "404", description = "Não foi possível atualizar o pedido")
    })
    
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
    
//-----------------------------------------------------------------------------------------------------------------------------------//
    
    @DeleteMapping("/ordem-servico/{ordemServicoID}")
    @Operation(summary = "Excluir um pedido", description = "Exclui um pedido no repositório")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Excluído!"),
        @ApiResponse(responseCode = "404", description = "Não foi possível excluir o pedido")
    })
    public ResponseEntity<Void> excluir(@PathVariable("ordemServicoID") 
        @Parameter(name = "id", description = "ID do produto", example = "1") Long ordemServicoID){

        if (!ordemServicoRepository.existsById(ordemServicoID)) {
            return ResponseEntity.notFound().build();

        }
        ordemServicoService.excluir(ordemServicoID);
        return ResponseEntity.noContent().build();
    }
 
//-----------------------------------------------------------------------------------------------------------------------------------//
    
    @GetMapping("/ordem-servico/{ordemServicoID}")
    @Operation(summary = "Recolher um pedido por ID", description = "Puxa um pedido pelo ID no repositório")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "O cliente existe!"),
        @ApiResponse(responseCode = "404", description = "O cliente não existe!")
    })
    public ResponseEntity<OrdemServico> listarPorId (@PathVariable("ordemServicoID") 
    @Parameter( name = "id", description = "ID do produto", example = "1") Long ordemServicoID) {
        Optional<OrdemServico> ordemServico = ordemServicoService.listarPorId(ordemServicoID);

        if (ordemServico.isPresent()) {
            return ResponseEntity.ok(ordemServico.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

//-----------------------------------------------------------------------------------------------------------------------------------//
    
    @GetMapping("/")
    @Operation(summary = "Lista todos os pedidos", description = "Lista todos os pedidos no repositório")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Lista feita!"),
        @ApiResponse(responseCode = "404", description = "Não foi possível montar a lista")
    })
    public List<OrdemServico> listar(){
    //@Parameter(name = "lista", description = "ID do produto", example = "1") {
        List<OrdemServico> result = ordemServicoService.listar();
        return result;
    }
    
//-----------------------------------------------------------------------------------------------------------------------------------//
    
    @GetMapping("/{clienteID}/ordem-servico")
    @Operation(summary = "Lista todos os pedidos de um cliente por seu ID", description = "Lista todos os pedidos no repositório pelo ID do cliente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Lista feita!"),
        @ApiResponse(responseCode = "404", description = "Não foi possível montar a lista")
    })
    public ResponseEntity<List<OrdemServico>> listarPorIdDoCliente(@PathVariable Long clienteID) {

        List<OrdemServico> ordemServicoCliente = ordemServicoRepository.findByClienteId(clienteID);
        return ResponseEntity.ok(ordemServicoCliente);
    }
    
//-----------------------------------------------------------------------------------------------------------------------------------//

    @PutMapping("/atualiza-status/{ordemServicoID}")
    @Operation(summary = "Atualiza os status do pedido", description = "Atualiza o status do pedido no repositório")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Pedido atualizado!"),
        @ApiResponse(responseCode = "404", description = "Não foi possível atualizar o pedido")
    })
    public ResponseEntity<OrdemServico> atualizaStatus(@PathVariable Long ordemServicoID, @Valid @RequestBody AtualizaStatusDTO statusDTO) {
        Optional<OrdemServico> optOS = ordemServicoService.atualizaStatus(ordemServicoID, statusDTO.status());

        if (optOS.isPresent()) {
            return ResponseEntity.ok(optOS.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}



