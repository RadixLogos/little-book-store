package com.radixlogos.littlebookstore.dto;

import com.radixlogos.littlebookstore.entities.Client;

import java.util.ArrayList;
import java.util.List;

public record ClientDTO(Long id, String name, String cellphone, List<BuyOrderDTO> orders) {
    public static ClientDTO fromClient(Client client) {
        var orders = new ArrayList<BuyOrderDTO>();
        client.getOrders().forEach(bo ->{
            orders.add(BuyOrderDTO.fromBuyOrder(bo));
        });
        return new ClientDTO(client.getId(), client.getName(), client.getCellphone(),orders);
    }
}
