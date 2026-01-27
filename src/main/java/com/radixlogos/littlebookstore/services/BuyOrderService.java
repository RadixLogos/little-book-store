package com.radixlogos.littlebookstore.services;

import com.radixlogos.littlebookstore.dto.BuyOrderDTO;
import com.radixlogos.littlebookstore.repositories.BuyOrderRepository;
import com.radixlogos.littlebookstore.repositories.OrderBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BuyOrderService {
    @Autowired
    private BuyOrderRepository buyOrderRepository;
    @Autowired
    private OrderBookRepository orderBookRepository;

    @Transactional(readOnly = true)
    public Page<BuyOrderDTO> findAllBuyOrders(Pageable pageable){
        return buyOrderRepository.findAll(pageable).map(BuyOrderDTO::fromBuyOrder);
    }


}
