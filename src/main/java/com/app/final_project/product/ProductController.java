package com.app.final_project.product;

import com.app.final_project.producer.MessageProducer;
import com.app.final_project.product.document.ProductES;
import com.app.final_project.product.dto.CreateProductRequest;
import com.app.final_project.product.dto.SePayTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.processing.Completion;
import java.util.List;

@RestController
@RequestMapping("api/product")
public class ProductController {
    @Value("${api-key}")
    private String apiKey;
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
    public Product insertProduct(CreateProductRequest product,
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
    
    @PostMapping("/delete/{id}")
    public Product deleteProduct(@PathVariable int id) {
        return productService.deleteProduct(id);
    }

    @PostMapping("/update")
    public Product updateProduct(@RequestBody Product product) {
        return productService.updateProduct(product, null, false);
    }
    @PostMapping("/sepay-payment")
    public ResponseEntity<String> receiveWebhook(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @RequestBody SePayTransaction payload) {
        System.out.println(payload + authorization);
        String keyAPI= "Apikey " + apiKey;
        // Kiểm tra API key nếu bạn muốn bảo mật webhook
        if (!keyAPI.equals(authorization)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid API Key");
        }

        String message = "💸 Có tiền vào tài khoản:\\n" +
                "Số tiền: " + payload.getTransferAmount() + " VND\\n" +
                "Nội dung: " + payload.getContent() + "\\n" +
                "Từ: " + payload.getAccountNumber() + " " + payload.getGateway() + "\\n" +
                "Thời gian: " + payload.getTransactionDate();

        producer.sendMessageToQueue(message);

        return ResponseEntity.ok("OK");
    }

    @GetMapping("/searchFuzzy")
    public List<ProductES> searchFuzzy(@RequestParam String keyword) {
        return productService.searchProductByName(keyword);
    }

    @GetMapping("/search1")
    public List<ProductES> searchProductByName(@RequestParam String name) {
        return productService.search(name);
    }

//    @GetMapping("/suggestions")
//    public List<String> suggest(@RequestParam String keyword) {
//        return productService.searchProductByName(keyword);
//    }
}
