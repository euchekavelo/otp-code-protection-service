package ru.promo_z.otpcodeprotectionservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.smpp.Connection;
import org.smpp.Session;
import org.smpp.TCPIPConnection;
import org.smpp.pdu.BindResponse;
import org.smpp.pdu.BindTransmitter;
import org.smpp.pdu.SubmitSM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.promo_z.otpcodeprotectionservice.config.properties.SmppProperties;
import ru.promo_z.otpcodeprotectionservice.service.SmsService;

@Slf4j
@Service
public class SmsServiceImpl implements SmsService {

    private final SmppProperties smppProperties;

    @Autowired
    public SmsServiceImpl(SmppProperties smppProperties) {
        this.smppProperties = smppProperties;
    }

    public void sendCode(String destination, String code) {
        try {
            Connection connection = new TCPIPConnection(smppProperties.getHost(), smppProperties.getPort());
            Session session = new Session(connection);

            BindTransmitter bindRequest = new BindTransmitter();
            bindRequest.setSystemId(smppProperties.getSystemId());
            bindRequest.setPassword(smppProperties.getPassword());
            bindRequest.setSystemType(smppProperties.getSystemType());
            bindRequest.setInterfaceVersion((byte) 0x34); // SMPP v3.4
            bindRequest.setAddressRange(smppProperties.getSourceAddr());

            BindResponse bindResponse = session.bind(bindRequest);
            if (bindResponse.getCommandStatus() != 0) {
                throw new Exception("Bind failed: " + bindResponse.getCommandStatus());
            }

            SubmitSM submitSM = new SubmitSM();
            submitSM.setSourceAddr(smppProperties.getSourceAddr());
            submitSM.setDestAddr(destination);
            submitSM.setShortMessage("Your code: " + code);

            session.submit(submitSM);
            log.info("SMS submitted for destination {} with OTP-code: {}", destination, code);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
