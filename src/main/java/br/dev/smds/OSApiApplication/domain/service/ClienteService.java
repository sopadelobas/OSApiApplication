/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.dev.smds.OSApiApplication.domain.service;

import br.dev.smds.OSApiApplication.domain.exception.DomainException;
import br.dev.smds.OSApiApplication.domain.model.Cliente;
import br.dev.smds.OSApiApplication.domain.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {
    
    @Autowired
    
    private ClienteRepository clienteRepository;
    
    public Cliente salvar(Cliente cliente) {
        Cliente clienteExistente = clienteRepository.findByEmail(cliente.getEmail());
        
        if (clienteExistente != null && !clienteExistente.equals(cliente)) {
        throw new DomainException("Já existe um cliente cadastrado com esse email!");
    }
        return clienteRepository.save(cliente);
    }
    public void excluir (Long clienteId) {
        clienteRepository.deleteById(clienteId);
    }
}
