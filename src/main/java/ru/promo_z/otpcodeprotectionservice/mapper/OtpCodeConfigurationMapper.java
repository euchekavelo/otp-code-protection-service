package ru.promo_z.otpcodeprotectionservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.promo_z.otpcodeprotectionservice.dto.request.OtpCodeConfigRequestDto;
import ru.promo_z.otpcodeprotectionservice.model.OtpCodeConfiguration;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OtpCodeConfigurationMapper {

    OtpCodeConfiguration otpCodeConfigRequestDtoToOtpCodeConfiguration(OtpCodeConfigRequestDto otpCodeConfigRequestDto);
}
