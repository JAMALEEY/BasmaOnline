package ma.youcode.orderservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ma.youcode.orderservice.commons.PaymentDto;
import ma.youcode.orderservice.commons.TransactionRequest;
import ma.youcode.orderservice.commons.TransactionResponse;
import ma.youcode.orderservice.entity.Order;
import ma.youcode.orderservice.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RefreshScope
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    @Lazy
    private RestTemplate restTemplate;

    @Value("${microservice.payment-service.endpoints.endpoint.uri}")
    private String ENDPOINT_URL;;
    // centralize logging using ELK stack in microservice architecture
    private Logger log = LoggerFactory.getLogger(OrderService.class);

    public TransactionResponse saveOrder(TransactionRequest transactionRequest) throws JsonProcessingException {
        String response = "";
        Order order = transactionRequest.getOrder();
        PaymentDto paymentDto = transactionRequest.getPaymentDto();
        paymentDto.setOrderId(order.getId());
        paymentDto.setAmount(order.getPrice());

        log.info("OrderService request : {}", new ObjectMapper().writeValueAsString(transactionRequest));
        // rest call ot be changed after to eureka
        //        "http://localhost:9191/payment/ensurePayment",
        PaymentDto paymentResponse = restTemplate.postForObject(ENDPOINT_URL, paymentDto, PaymentDto.class);
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
