package com.app.final_project.product;

import com.app.final_project.event.Event;
import com.app.final_project.event.dto.EventRequest;
import com.app.final_project.producer.MessageProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/product")
public class ProductController {
    private final ProductService productService;
    private final MessageProducer producer;
    private final TelegramLogService telegramLogService;

    @Autowired
    public ProductController(ProductService productService, MessageProducer producer, TelegramLogService telegramLogService) {
        this.productService = productService;
        this.producer = producer;
        this.telegramLogService = telegramLogService;
    }

    @GetMapping("/products")
    public List<Product> getAllProducts() {
        return productService.getAllProduct();
    }

    @PostMapping(consumes = {"multipart/form-data"}, value = ("/add"))
    public Product insertProduct(Product product,
                                 @RequestParam("images")List<MultipartFile> images) {
        return productService.saveProduct(product, images);
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable int id) {
        return productService.getProductById(id);
    }

    @GetMapping("/send/{msg}")
    public String sendMessage(@PathVariable String msg) {
        producer.sendMessage(msg);
        return "Message sent: " + msg;
    }
    @GetMapping("/send1")
    public void sendMessage1() {
        producer.sendMessageFanout(); // Không cần routing key
    }
    @PostMapping("/sendEmail")
    public ResponseEntity<String> sendNotification(@RequestParam String email) {
        producer.sendEmailNotification(email, "Test Email", "This is a test notification.");
        return ResponseEntity.ok("Email notification sent to RabbitMQ.");
    }
    @PostMapping("/sendTele")
    public String sendMessageTele(@RequestParam String message) {
        producer.sendMessageToQueue(message);
        return "Message sent to Producer: " + message;
    }
}
