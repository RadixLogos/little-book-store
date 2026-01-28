package com.radixlogos.littlebookstore.dto;

import com.radixlogos.littlebookstore.entities.BuyOrder;
import com.radixlogos.littlebookstore.entities.enums.PaymentType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public record BuyOrderDTO(Long id, PaymentType paymentType, ClientDTO client, LocalDate orderDate, List<OrderBookDTO> orderBooks) {
    public static BuyOrderDTO fromBuyOrder(BuyOrder buyOrder){
        var client = ClientDTO.fromClient(buyOrder.getClient());
        List<OrderBookDTO> orderBooks = new ArrayList<>();
        buyOrder.getOrderBooks().forEach(ob ->{
            orderBooks.add(OrderBookDTO.fromOrderBook(ob));
        });
        return new BuyOrderDTO(buyOrder.getId(),buyOrder.getPaymentType(),client,buyOrder.getOrderDate(),orderBooks);
    }
}
