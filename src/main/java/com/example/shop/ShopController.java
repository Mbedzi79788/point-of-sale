package com.example.shop;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
public class ShopController {

    /** One row in the cart view. */
    public record CartLine(Product product, int quantity) {
        public BigDecimal getSubtotal() {
            return product.getPrice().multiply(BigDecimal.valueOf(quantity));
        }
        public Product getProduct() { return product; }
        public int getQuantity() { return quantity; }
    }

    private final ProductRepository products;
    private final Cart cart;

    public ShopController(ProductRepository products, Cart cart) {
        this.products = products;
        this.cart = cart;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("products", products.findAll());
        model.addAttribute("cartCount", cart.getCount());
        return "products";
    }

    @PostMapping("/cart/add/{id}")
    public String add(@PathVariable Long id) {
        if (products.existsById(id)) {
            cart.add(id);
        }
        return "redirect:/";
    }

    @PostMapping("/cart/remove/{id}")
    public String remove(@PathVariable Long id) {
        cart.remove(id);
        return "redirect:/cart";
    }

    @GetMapping("/cart")
    public String viewCart(Model model) {
        List<CartLine> lines = buildLines();
        BigDecimal total = lines.stream()
                .map(CartLine::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        model.addAttribute("lines", lines);
        model.addAttribute("total", total);
        model.addAttribute("cartCount", cart.getCount());
        return "cart";
    }

    @PostMapping("/checkout")
    public String checkout(Model model) {
        if (cart.getItems().isEmpty()) {
            return "redirect:/cart";
        }
        List<CartLine> lines = buildLines();
        BigDecimal total = lines.stream()
                .map(CartLine::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.clear();
        model.addAttribute("orderNumber", UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        model.addAttribute("total", total);
        model.addAttribute("cartCount", 0);
        return "confirmation";
    }

    private List<CartLine> buildLines() {
        List<CartLine> lines = new ArrayList<>();
        for (Map.Entry<Long, Integer> e : cart.getItems().entrySet()) {
            products.findById(e.getKey())
                    .ifPresent(p -> lines.add(new CartLine(p, e.getValue())));
        }
        return lines;
    }
}
