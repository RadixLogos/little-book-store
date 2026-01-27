package com.radixlogos.littlebookstore.dto;

import com.radixlogos.littlebookstore.entities.BuyOrder;

import java.util.ArrayList;
import java.util.List;

public record BuyOrderDTO(Long id, String paymentType, ClientDTO client, List<OrderBookDTO> books) {
    public static BuyOrderDTO fromBuyOrder(BuyOrder buyOrder){
        var client = ClientDTO.fromClient(buyOrder.getClient());
        List<OrderBookDTO> orderBooks = new ArrayList<>();
        buyOrder.getOrderBooks().forEach(ob ->{
            orderBooks.add(OrderBookDTO.fromOrderBook(ob));
        });
        return new BuyOrderDTO(buyOrder.getId(),buyOrder.getPaymentType().toString(),client,orderBooks);
    }
}
