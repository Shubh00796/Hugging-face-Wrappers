package com.huggingFace.ai.controller;

import com.huggingFace.ai.dto.EnvironmentDataDTO;

import com.huggingFace.ai.exceptions.ResourceNotFoundException;
import com.huggingFace.ai.serviceimpl.EnvironmentalDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/environment")
public class EnvironmentalDataController {

    private static final Logger logger = LoggerFactory.getLogger(EnvironmentalDataController.class);

    private final EnvironmentalDataService environmentalDataService;

    @Autowired
    public EnvironmentalDataController(EnvironmentalDataService environmentalDataService) {
        this.environmentalDataService = environmentalDataService;
    }

    @PostMapping("/data")
    public ResponseEntity<Map<String, String>> processEnvironmentalData(@RequestBody EnvironmentDataDTO environmentalDataDTO) {
        try {
            environmentalDataService.processEnvironmentalData(environmentalDataDTO);
            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Environmental data processed successfully.");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            logger.error("Validation error while processing environmental data: {}", e.getMessage(), e);
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("Error processing environmental data: {}", e.getMessage(), e);
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "An unexpected error occurred while processing the environmental data.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}