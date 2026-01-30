package com.radixlogos.littlebookstore.dto;

import com.radixlogos.littlebookstore.entities.Client;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

public record ClientDTO(
        Long id,
        @NotBlank(message = "The name is required")
        String name,
        @NotBlank(message = "The cellphone is required")
        String cellphone,
        List<BuyOrderDTO> orders) {
    public static ClientDTO fromClient(Client client) {
        var orders = new ArrayList<BuyOrderDTO>();
        client.getOrders().forEach(bo ->{
            orders.add(BuyOrderDTO.fromBuyOrder(bo));
        });
        return new ClientDTO(client.getId(), client.getName(), client.getCellphone(),orders);
    }
}
