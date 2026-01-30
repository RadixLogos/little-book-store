package com.radixlogos.littlebookstore.services;

import com.radixlogos.littlebookstore.dto.BookDTO;
import com.radixlogos.littlebookstore.dto.BuyOrderDTO;
import com.radixlogos.littlebookstore.dto.OrderBookDTO;
import com.radixlogos.littlebookstore.entities.BuyOrder;
import com.radixlogos.littlebookstore.entities.OrderBook;
import com.radixlogos.littlebookstore.entities.enums.PaymentType;
import com.radixlogos.littlebookstore.repositories.BookRepository;
import com.radixlogos.littlebookstore.repositories.BuyOrderRepository;
import com.radixlogos.littlebookstore.repositories.ClientRepository;
import com.radixlogos.littlebookstore.repositories.OrderBookRepository;
import com.radixlogos.littlebookstore.services.exceptions.DatabaseException;
import com.radixlogos.littlebookstore.services.exceptions.ResourceNotFoundException;
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
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private BookRepository bookRepository;

    @Transactional(readOnly = true)
    public Page<BuyOrderDTO> findAllBuyOrders(Pageable pageable){
        return buyOrderRepository.findAll(pageable).map(BuyOrderDTO::fromBuyOrder);
    }
    @Transactional(readOnly = true)
    public BuyOrderDTO findOrderById(Long bookId){
        return BuyOrderDTO
                .fromBuyOrder(buyOrderRepository.findById(bookId)
                        .orElseThrow(()-> new ResourceNotFoundException("Pedido não encontrado")));
    }
    @Transactional
    public BuyOrderDTO insertOrder(BuyOrderDTO buyOrderDTO){
        var buyOrderEntity = new BuyOrder();
        copyDtoToEntity(buyOrderDTO,buyOrderEntity);
        buyOrderEntity = buyOrderRepository.save(buyOrderEntity);
        return BuyOrderDTO.fromBuyOrder(buyOrderEntity);
    }
    @Transactional
    public BuyOrderDTO updateBuyOrder(Long id, BuyOrderDTO buyOrderDTO){
        if (!buyOrderRepository.existsById(id)){
            throw new ResourceNotFoundException("Pedido não encontrado");
        }
        var buyOrderEntity = buyOrderRepository.getReferenceById(id);
        copyDtoToEntity(buyOrderDTO,buyOrderEntity);
        buyOrderEntity = buyOrderRepository.save(buyOrderEntity);
        return BuyOrderDTO.fromBuyOrder(buyOrderEntity);
    }
    @Transactional
    public void deleteBuyOrder(Long id){
        if(!buyOrderRepository.existsById(id)){
            throw new ResourceNotFoundException("Pedido não encontrado");
        }
        try {
            buyOrderRepository.deleteById(id);
        }catch (Exception e){
            throw new DatabaseException("Falha de integridade referencial");
        }
    }
    private void copyDtoToEntity(BuyOrderDTO buyOrderDTO, BuyOrder buyOrderEntity) {
        var client = clientRepository.findById(buyOrderDTO.client().id())
                .orElseThrow(()-> new ResourceNotFoundException("Cliente não encontrado"));
        buyOrderEntity.setClient(client);
        buyOrderEntity.setOrderDate(buyOrderDTO.orderDate());
        buyOrderEntity.setPaymentType(buyOrderDTO.paymentType());
        Double total = 0.0;
        for(OrderBookDTO ob : buyOrderDTO.orderBooks()){
            var orderBookEntity = new OrderBook();
            orderBookEntity.setId(ob.id());
            orderBookEntity.setQuantity(ob.quantity());
            orderBookEntity.setSoldValue(ob.soldValue());
            orderBookEntity.setSubTotal(ob.subtotal());
            total += ob.subtotal();
            buyOrderEntity.addOrderBooks(orderBookEntity);
        };
        buyOrderEntity.setTotal(total);

    }
}
