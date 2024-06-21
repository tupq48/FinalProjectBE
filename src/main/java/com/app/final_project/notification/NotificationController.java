package com.app.final_project.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/notification")
public class NotificationController {
    @Autowired
    NotificationService notificationService;

    @GetMapping
    public ResponseEntity<?> getAllNotification() {
        return ResponseEntity.ok(notificationService.getAllNotification());
    }

    @PostMapping("/read")
    public ResponseEntity<?> setReadNotification(@RequestParam(value = "id") Integer id) {
        return ResponseEntity.ok(notificationService.setReadNotification(id));
    }

    @PostMapping("/readAll")
    public ResponseEntity<?> setReadAllNotification() {
        return ResponseEntity.ok(notificationService.setReadAllNotification());
    }
}
