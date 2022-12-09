package no.shoppifly;

import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.distribution.DistributionStatisticConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController()
public class ShoppingCartController {

    @Autowired
    private MeterRegistry meterRegistry;

    @Autowired
    private final CartService cartService;

//    @Autowired
//    private MeterRegistry meterRegistry;


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
    public Cart updateCart(@RequestBody Cart cart, Item item) {

        meterRegistry.counter("carts").increment();
        cart.setItems(cart.getItems());
        cart.setId(cart.getId());

        //meterRegistry.counter("cartsvalue").increment(item.unitPrice()); // getunitprice?
        //meterRegistry.counter("cartsvalue", "cart", cart.getId()).increment(item.getUnitPrice());

        //return new ResponseEntity<>(cartService.update(cart), HttpStatus.OK);
        //return ResponseEntity.ok(cartService.update(cart));
        return cartService.update(cart);
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