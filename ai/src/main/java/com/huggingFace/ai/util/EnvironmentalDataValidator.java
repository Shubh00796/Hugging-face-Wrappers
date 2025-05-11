package com.huggingFace.ai.util;

import com.huggingFace.ai.dto.EnvironmentDataDTO;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Instant;

/**
 * Validates environment data to ensure it meets predefined criteria.
 */
@Component
public class EnvironmentalDataValidator {

    private static final Logger logger = LoggerFactory.getLogger(EnvironmentalDataValidator.class);

    // Define safe ranges for environmental conditions
    private static final double MIN_TEMPERATURE = 15.0;
    private static final double MAX_TEMPERATURE = 30.0;
    private static final double MIN_HUMIDITY = 20.0;
    private static final double MAX_HUMIDITY = 80.0;
    private static final double MIN_AIR_QUALITY = 0.0;
    private static final double MAX_AIR_QUALITY = 50.0;

    /**
     * Validates the environment data and throws an IllegalArgumentException if any condition is violated.
     *
     * @param environmentalDataDTO The data to validate.
     * @throws IllegalArgumentException if any validation fails.
     */
    public void validate(@NotNull EnvironmentDataDTO environmentalDataDTO) {
        if (environmentalDataDTO == null) {
            throw new IllegalArgumentException("Environment data cannot be null.");
        }

        validateTemperature(environmentalDataDTO.getTemperature());
        validateHumidity(environmentalDataDTO.getHumidity());
        validateAirQuality(environmentalDataDTO.getAirQuality());

        logger.info("Environmental data validation passed successfully.");
    }

    /**
     * Validates the temperature value.
     *
     * @param temperature The temperature value to validate.
     * @throws IllegalArgumentException if temperature is out of range.
     */
    private void validateTemperature(Double temperature) {
        if (temperature < MIN_TEMPERATURE || temperature > MAX_TEMPERATURE) {
            logger.warn("Temperature {} is outside the safe range of {}-{}.", temperature, MIN_TEMPERATURE, MAX_TEMPERATURE);
            throw new IllegalArgumentException(String.format(
                    "Temperature must be between %.1f and %.1f degrees Celsius.", MIN_TEMPERATURE, MAX_TEMPERATURE));
        }
    }

    /**
     * Validates the humidity value.
     *
     * @param humidity The humidity value to validate.
     * @throws IllegalArgumentException if humidity is out of range.
     */
    private void validateHumidity(Double humidity) {
        if (humidity < MIN_HUMIDITY || humidity > MAX_HUMIDITY) {
            logger.warn("Humidity {}% is outside the safe range of {}%-{}%.", humidity, MIN_HUMIDITY, MAX_HUMIDITY);
            throw new IllegalArgumentException(String.format(
                    "Humidity must be between %.1f%% and %.1f%%.", MIN_HUMIDITY, MAX_HUMIDITY));
        }
    }

    /**
     * Validates the air quality value.
     *
     * @param airQuality The air quality value to validate.
     * @throws IllegalArgumentException if air quality is out of range.
     */
    private void validateAirQuality(Double airQuality) {
        if (airQuality < MIN_AIR_QUALITY || airQuality > MAX_AIR_QUALITY) {
            logger.warn("Air quality index {} is outside the safe range of {}-{} AQI.", airQuality, MIN_AIR_QUALITY, MAX_AIR_QUALITY);
            throw new IllegalArgumentException(String.format(
                    "Air quality must be between %.1f AQI and %.1f AQI.", MIN_AIR_QUALITY, MAX_AIR_QUALITY));
        }
    }
}