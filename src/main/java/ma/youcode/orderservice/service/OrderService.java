package ma.youcode.orderservice.service;

import ma.youcode.orderservice.commons.PaymentDto;
import ma.youcode.orderservice.commons.TransactionRequest;
import ma.youcode.orderservice.commons.TransactionResponse;
import ma.youcode.orderservice.entity.Order;
import ma.youcode.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RestTemplate restTemplate;

    public TransactionResponse saveOrder(TransactionRequest transactionRequest){
        String response = "";
        Order order = transactionRequest.getOrder();
        PaymentDto paymentDto = transactionRequest.getPaymentDto();
        paymentDto.setOrderId(order.getId());
        paymentDto.setAmount(order.getPrice());
        // rest call ot be changed after to eureka
        PaymentDto paymentResponse = restTemplate.postForObject(
        "http://localhost:9191/payment/ensurePayment",
        paymentDto,
        PaymentDto.class
        );
// to remove that hard coded error msgs hestrix will do the job

        assert paymentResponse != null;
               response = paymentResponse.getPaymentStatus().equals(
                        "success"
                ) ? "payment processing successful and the order placed successfully" :
                        " failure in payment api, order added to cart";
            orderRepository.save(order);

//        return orderRepository.save(order);
        // we will pass the payment response because it will return the actual object of the payment rather than only
        // having as a return the orderID the payment and the setamount wich is not enough
        // plus the order because we want to return the order
        return new TransactionResponse(order,
                paymentResponse.getTransactionId(),
                paymentResponse.getAmount(),
                response
                );
    }
}
