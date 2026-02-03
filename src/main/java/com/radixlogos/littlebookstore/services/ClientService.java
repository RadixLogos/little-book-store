package com.radixlogos.littlebookstore.services;

import com.radixlogos.littlebookstore.dto.ClientDTO;
import com.radixlogos.littlebookstore.entities.Client;
import com.radixlogos.littlebookstore.repositories.ClientRepository;
import com.radixlogos.littlebookstore.services.exceptions.DatabaseException;
import com.radixlogos.littlebookstore.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClientService {
    @Autowired
    private ClientRepository clientRepository;

    @Transactional
    public Page<ClientDTO> findAllClients(Pageable pageable, String name){
        return clientRepository.findAllClients(pageable,name).map(ClientDTO::fromClient);
    }

    @Transactional
    public ClientDTO findClientById(Long id){
        return ClientDTO
                .fromClient(clientRepository.findById(id)
                        .orElseThrow(()->new ResourceNotFoundException("Cliente não encontrado")));
    }
    @Transactional
    public ClientDTO insertClient(ClientDTO clientDTO){
        var clientEntity = new Client();
        copyDtoToEntity(clientDTO,clientEntity);
        clientEntity = clientRepository.save(clientEntity);
        return ClientDTO.fromClient(clientEntity);
    }

    @Transactional
    public ClientDTO updateClient(Long id, ClientDTO clientDTO){
        if(!clientRepository.existsById(id)){
            throw new ResourceNotFoundException("Cliente não encontrado");
        }
        var clientEntity = clientRepository.getReferenceById(id);
        copyDtoToEntity(clientDTO,clientEntity);
        clientEntity = clientRepository.save(clientEntity);
        return ClientDTO.fromClient(clientEntity);
    }

    @Transactional
    public void deleteClient(Long id){
        if (!clientRepository.existsById(id))
            throw new ResourceNotFoundException("Cliente não encontrado");
        try{
            clientRepository.deleteById(id);
        } catch (Exception e) {
            throw new DatabaseException("Falha de integridade referencial");
        }
    }
    private void copyDtoToEntity(ClientDTO clientDTO, Client clientEntity) {
        clientEntity.setName(clientDTO.name());
        clientEntity.setCellphone(clientDTO.cellphone());
    }
}
