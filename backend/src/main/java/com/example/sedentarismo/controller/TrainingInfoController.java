package com.example.sedentarismo.controller;

import com.example.sedentarismo.dto.TrainingInfo;
import com.example.sedentarismo.service.TrainingInfoService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"http://localhost:4200", "http://127.0.0.1:4200"})
public class TrainingInfoController {

    private final TrainingInfoService trainingInfoService;

    public TrainingInfoController(TrainingInfoService trainingInfoService) {
        this.trainingInfoService = trainingInfoService;
    }

    @GetMapping("/training-info")
    public TrainingInfo trainingInfo() {
        return trainingInfoService.get();
    }
}
