package no.shoppifly;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CartService {

    Cart getCart(String id);

    Cart update(Cart cart);

    String checkout(Cart cart);

    List<String> getAllsCarts();

    float total();

    int cartsCheckedOut();

//    void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent);
}
