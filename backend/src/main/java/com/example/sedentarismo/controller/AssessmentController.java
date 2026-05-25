package com.example.sedentarismo.controller;

import com.example.sedentarismo.dto.AssessmentRequest;
import com.example.sedentarismo.dto.AssessmentResponse;
import com.example.sedentarismo.dto.TrainingInfo;
import com.example.sedentarismo.service.AssessmentService;
import com.example.sedentarismo.service.TrainingInfoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"http://localhost:4200", "http://127.0.0.1:4200"})
public class AssessmentController {

    private final AssessmentService assessmentService;
    private final TrainingInfoService trainingInfoService;

    public AssessmentController(AssessmentService assessmentService, TrainingInfoService trainingInfoService) {
        this.assessmentService = assessmentService;
        this.trainingInfoService = trainingInfoService;
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "UP", "service", "sedentarismo-api");
    }

    @PostMapping("/assessments")
    public AssessmentResponse assess(@Valid @RequestBody AssessmentRequest request) {
        return assessmentService.assess(request);
    }

    @GetMapping("/training-info")
    public TrainingInfo trainingInfo() {
        return trainingInfoService.get();
    }
}
