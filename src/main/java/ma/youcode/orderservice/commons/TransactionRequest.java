package ma.youcode.orderservice.commons;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.youcode.orderservice.entity.Order;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class TransactionRequest {
    private Order order;
    private PaymentDto paymentDto;


}
