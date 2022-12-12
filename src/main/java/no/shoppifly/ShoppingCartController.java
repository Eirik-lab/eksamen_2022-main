package no.shoppifly;

import com.sun.xml.bind.annotation.OverrideAnnotationOf;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.distribution.DistributionStatisticConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    private Meter meter;

    public ShoppingCartController(CartService cartService, MeterRegistry meterRegistry) {
        this.meter = Gauge.builder("cartsvalue", cartService, CartService::total).register(meterRegistry);
        this.cartService = cartService;
    }

    @Timed
    @GetMapping(path = "/cart/{id}")
    public Cart getCart(@PathVariable String id) {
        return cartService.getCart(id);
    }

    /**
     * Checks out a shopping cart. Removes the cart, and returns an order ID
     *
     * @return an order ID
     */
    @Timed
    @PostMapping(path = "/cart/checkout", consumes = "application/json", produces = "application/json")
    public String checkout(@RequestBody Cart cart) {

        // base checkout
        meterRegistry.counter("checkouts").increment();


        meterRegistry.counter("checkout", "cart", cart.getId()).increment();
        DistributionStatisticConfig.builder()
                .percentilesHistogram(true)
                .build();
        DistributionSummary.builder("latency")
                .publishPercentileHistogram()
                .register(meterRegistry);
        this.meterRegistry.timer("checkout_latency").record(() -> cartService.checkout(cart));

        // Denne meter-typen "Gauge" rapporterer hvor mye penger som totalt finnes i alle carts
        Gauge.builder("cartvalue", cart,
                b -> b.items.stream().mapToDouble(i -> i.unitPrice).sum())
                .register(meterRegistry);

        return cartService.checkout(cart);
    }

    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent, Item item) {
        Gauge.builder("account_count", item.getUnitPrice(), value -> value);
    }

    /**
     * Updates a shopping cart, replacing it's contents if it already exists. If no cart exists (id is null)
     * a new cart is created.
     *
     * @return the updated cart
     */
    @Timed
    @PostMapping(path = "/cart", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Cart> updateCart(@RequestBody Cart cart, Item item) {

        meterRegistry.counter("carts").increment();
        cart.setItems(cart.getItems());
        cart.setId(cart.getId());

//        meterRegistry.counter("cartsvalue").increment(item.unitPrice); // getunitprice?
//        meterRegistry.counter("cartsvalue", "cart", cart.getId()).increment(item.getUnitPrice());

        //return new ResponseEntity<>(cartService.update(cart), HttpStatus.OK);
        //return ResponseEntity.ok(cartService.update(cart));
        return new ResponseEntity<Cart>(cartService.update(cart), HttpStatus.OK);
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