package br.com.fooddelivery.domain.listener;

import br.com.fooddelivery.domain.event.PurchaseConfirmedEvent;
import br.com.fooddelivery.domain.model.Purchase;
import br.com.fooddelivery.domain.service.SendingEmailService;
import br.com.fooddelivery.domain.service.SendingEmailService.Message;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class NotifyConfirmedCustomerOrderListener {
    private final SendingEmailService sendingEmailService;

    public NotifyConfirmedCustomerOrderListener(SendingEmailService sendingEmailService) {
        this.sendingEmailService = sendingEmailService;
    }

    @TransactionalEventListener
    public void whenPurchaseConfirmed(PurchaseConfirmedEvent purchaseConfirmedEvent) {
        Purchase purchase = purchaseConfirmedEvent.getPurchase();

        var message = Message.builder()
                .subjectMatter(purchase.getRestaurant().getName() + " - Purchase has been confirmed!")
                .variable("purchase", purchase)
                .body("order-confirmed.html")
                .recipient(purchase.getClient().getEmail())
                .build();

        this.sendingEmailService.send(message);
    }
}
