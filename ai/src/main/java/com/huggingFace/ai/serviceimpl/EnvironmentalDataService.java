package com.huggingFace.ai.serviceimpl;

import com.huggingFace.ai.domain.Device;
import com.huggingFace.ai.domain.EnvironmentData;
import com.huggingFace.ai.dto.EnvironmentDataDTO;
import com.huggingFace.ai.mapper.EnvironmentDataMapper;
import com.huggingFace.ai.repository.DeviceRepository;
import com.huggingFace.ai.repository.EnvironmentDataRepository;
import com.huggingFace.ai.util.EnvironmentalDataValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class EnvironmentalDataService {

    private final EnvironmentDataRepository environmentDataRepository;
    private final DeviceRepository deviceRepository;
    private final EnvironmentalDataValidator environmentalDataValidator;
    private final EnvironmentDataMapper environmentDataMapper;

    @Autowired
    public EnvironmentalDataService(EnvironmentDataRepository environmentDataRepository,
                                    DeviceRepository deviceRepository,
                                    EnvironmentalDataValidator environmentalDataValidator,
                                    EnvironmentDataMapper environmentDataMapper) {
        this.environmentDataRepository = environmentDataRepository;
        this.deviceRepository = deviceRepository;
        this.environmentalDataValidator = environmentalDataValidator;
        this.environmentDataMapper = environmentDataMapper;
    }

    @Transactional
    public void processEnvironmentalData(EnvironmentDataDTO environmentalDataDTO) {
        environmentalDataValidator.validate(environmentalDataDTO);

        Device device = deviceRepository.findByIdentifier(environmentalDataDTO.getDeviceIdentifier())
                .orElseGet(() -> createNewDevice(environmentalDataDTO));

        EnvironmentData environmentData = environmentDataMapper.toEntity(environmentalDataDTO);
        environmentData.setDeviceIdentifier(device.getIdentifier());
        environmentData.setTimestamp(Instant.now());

        environmentDataRepository.save(environmentData);
    }

    private Device createNewDevice(EnvironmentDataDTO environmentalDataDTO) {
        return deviceRepository.save(Device.builder()
                .identifier(environmentalDataDTO.getDeviceIdentifier())
                .deviceType("IoT_sensor")
                .location("Unknown")
                .registrationDate(Instant.now())
                .build());
    }
}