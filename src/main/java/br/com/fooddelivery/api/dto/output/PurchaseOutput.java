package br.com.fooddelivery.api.dto.output;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PurchaseOutput extends RepresentationModel<PurchaseOutput> {
    private UUID purchaseCode;
    private BigDecimal subtotal;
    private BigDecimal shippingFee;
    private BigDecimal amount;
    private OffsetDateTime creationDate;
    private OffsetDateTime confirmationDate;
    private OffsetDateTime cancellationDate;
    private OffsetDateTime deliveryDate;
    private String orderStatus;
    private AddressOutput deliveryAddress;
    private PaymentOutput payment;
    private RestaurantSummaryOutput restaurant;
    private UserOutput client;
    private List<OrderItemOutput> items;
}
