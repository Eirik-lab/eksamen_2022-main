package no.shoppifly;

import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.distribution.DistributionStatisticConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.function.Supplier;

@RestController()
public class ShoppingCartController {

    private MeterRegistry meterRegistry;
    @Autowired
    private final CartService cartService;

    //private MeterRegistry meterRegistry;


    public ShoppingCartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping(path = "/cart/{id}")
    public Cart getCart(@PathVariable String id) {
        return cartService.getCart(id);
    }

    /**
     * Checks out a shopping cart. Removes the cart, and returns an order ID
     *
     * @return an order ID
     */
    @PostMapping(path = "/cart/checkout")
    public String checkout(@RequestBody Cart cart) {

        meterRegistry.counter("checkout").increment();
        meterRegistry.counter("checkout", "cart", cart.getId()).increment();
        DistributionStatisticConfig.builder()
                .percentilesHistogram(true)
                .build();
        DistributionSummary.builder("latency")
                .publishPercentileHistogram()
                .register(meterRegistry);
        this.meterRegistry.timer("checkout_latency").record(() -> cartService.checkout(cart));
        return cartService.checkout(cart);
    }

    /**
     * Updates a shopping cart, replacing it's contents if it already exists. If no cart exists (id is null)
     * a new cart is created.
     *
     * @return the updated cart
     */
    @PostMapping(path = "/cart", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Cart> updateCart(@RequestBody Cart cart) {

        meterRegistry.counter("update_cart").increment();
        cart.setItems(cart.getItems());
        cart.setId(cart.getId());

        return new ResponseEntity<>(cartService.update(cart), HttpStatus.OK);
        //return ResponseEntity.ok(cartService.update(cart));
        //return cartService.update(cart);
    }

    /**
     * return all cart IDs
     *
     * @return
     */
    @GetMapping(path = "/carts", consumes = "application/json", produces = "application/json")
    public List<String> getAllCarts() {
     //   Gauge.builder("carts", (Supplier<Number>) getAllCarts());
        meterRegistry.counter("get_all_carts").increment(); // Increment a metric called "get_all_carts" every time this is called
        return cartService.getAllsCarts();
    }




}