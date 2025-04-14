package ru.promo_z.otpcodeprotectionservice.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "smpp")
public class SmppProperties {

    private String host;

    private int port;

    private String systemId;

    private String password;

    private String systemType;

    private String sourceAddr;
}
