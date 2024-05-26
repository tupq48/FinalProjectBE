package com.app.final_project.AImodel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/ai-model")
public class AIModelController {

    @Autowired
    private AIModelService aiModelService;

    @GetMapping("/isTraining")
    public ResponseEntity<?> isTrainingModel() {
        return ResponseEntity.ok(aiModelService.isTraining());
    }

    @GetMapping("/isModelExist")
    public ResponseEntity<?> isModelExist() {
        return ResponseEntity.ok(aiModelService.isModelExist());
    }

    @PostMapping("/trainModel")
    public ResponseEntity<?> trainModel(@RequestParam(value = "images", required = true) List<MultipartFile> images) {
        System.out.println(images.size());
        return ResponseEntity.ok(aiModelService.trainModel(images));
    }

    @PostMapping("/predict")
    public ResponseEntity<?> predict(@RequestParam(value = "image", required = true) MultipartFile image) {
        return ResponseEntity.ok(aiModelService.predict(image));
    }

    @GetMapping("/imagesTrain")
    public ResponseEntity<?> getImagesTrain() {
        return ResponseEntity.ok(aiModelService.getAllUserImage());
    }

}
