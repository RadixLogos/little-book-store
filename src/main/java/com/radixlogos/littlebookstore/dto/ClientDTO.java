package com.radixlogos.littlebookstore.dto;

import com.radixlogos.littlebookstore.entities.Client;

public record ClientDTO(Long id, String name, String cellphone) {
    public static ClientDTO fromClient(Client client) {
        return new ClientDTO(client.getId(), client.getName(), client.getCellphone());
    }
}
