package ma.youcode.orderservice.controller;

import ma.youcode.orderservice.commons.PaymentDto;
import ma.youcode.orderservice.commons.TransactionRequest;
import ma.youcode.orderservice.commons.TransactionResponse;
import ma.youcode.orderservice.entity.Order;
import ma.youcode.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/productOrder")
    public TransactionResponse productOrder(@RequestBody TransactionRequest transactionRequest){
//        Order order = transactionRequest.getOrder();
//        PaymentDto paymentDto = transactionRequest.getPaymentDto();
//        paymentDto.setPaymentId(order.getId());
//        paymentDto.setAmount(paymentDto.getAmount());
        return orderService.saveOrder(transactionRequest);
    }


}
