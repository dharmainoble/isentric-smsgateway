package com.isentric.bulkgateway.service;

import com.isentric.bulkgateway.dto.OperationError;
import com.isentric.bulkgateway.dto.SMSMessageDTO;
import com.isentric.bulkgateway.manager.PrefixManager;
import com.isentric.bulkgateway.model.SMSMessageResponse;
import com.isentric.bulkgateway.model.SMSMessageSmpp;
import com.isentric.bulkgateway.repository.SMSMessageRepository;
import com.isentric.bulkgateway.repository.SMSMessageSmppRepository;
import com.isentric.bulkgateway.utility.JmsUtil;
import com.isentric.bulkgateway.utility.StringUtil;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.UUID;
import com.fasterxml.uuid.Generators;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.nio.charset.StandardCharsets;

public class SMSMessageService {

    SMSMessageRepository smsMessageRepository;

    SMSMessageSmppRepository sMSMessageSmppRepository;

    public SMSMessageResponse sendMessage(SMSMessageDTO dto) {
        SMSMessageResponse smsMessageResponse = new SMSMessageResponse();
        SMSMessageSmpp smpp = new SMSMessageSmpp();
        JmsUtil jmsUtil = new JmsUtil();
        dto.setGuid(createGUID());
        String invokerIpAddress = "127.0.0.1";
        dto.setMessageType(0);
        SMSMessageResponse var7 = smsMessageResponse;
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        try {
            PrefixManager cache = PrefixManager.getInstance();
            if (!this.isAuthenticationFailed(invokerIpAddress)) {
                if (this.isTextMessageValidationFailed(dto, smsMessageResponse)) {
                    smsMessageResponse.setGuid(smpp.getGuid());
                    smsMessageRepository.save(smsMessageResponse);
                    var7 = smsMessageResponse;
                    return var7;
                }
                if (this.isSmscSmpp(dto)) {
                    SMSMessageSmpp smsMessageSmpp = new SMSMessageSmpp();
                    BeanUtils.copyProperties(smsMessageSmpp, dto);
                    boolean flag = false;
                    if (!smsMessageSmpp.getTelco().equals("other")) {
                        if (cache.getPrefixObj(smsMessageSmpp.getTelco()).getRoute() == null) {
                            flag = false;
                        } else if (cache.getPrefixObj(smsMessageSmpp.getTelco()).getRoute().equals("")) {
                            flag = false;
                        } else {
                            smsMessageSmpp.setSmppName(cache.getPrefixObj(smsMessageSmpp.getTelco()).getRoute());
                            smsMessageSmpp.setCredit(cache.getPrefixObj(smsMessageSmpp.getTelco()).getCredit());
                            flag = true;
                        }
                    } else {
                        flag = false;
                    }
                    smsMessageSmpp.setSmppConfig("/jsms_smpp.conf");
                    if (flag) {
                        sMSMessageSmppRepository.save(smsMessageSmpp);
                        //jmsUtil.postQueue("queue/SMPPSMSMessageQueue" + dto.getQueueSequence(), smsMessageResponse);
                        smsMessageResponse.setGuid(StringUtil.trimToEmpty(dto.getGuid()));
                        try {
                            byte[] serialized = mapper.writeValueAsBytes(smsMessageResponse);
                            smsMessageResponse.setValue(serialized);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        smsMessageResponse.setStatus("STATUS_SUCCESS");
                    } else {
                        smsMessageResponse.setGuid(StringUtil.trimToEmpty(dto.getGuid()));
                        try {
                            byte[] serialized = mapper.writeValueAsBytes(smsMessageResponse);
                            smsMessageResponse.setValue(serialized);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        smsMessageResponse.setStatus("STATUS_FAILURE");
                        OperationError operationError = new OperationError();
                        operationError.setMessage("Telco parameter value is invalid.");
                    }
                } else {
                    smsMessageResponse.setGuid(StringUtil.trimToEmpty(dto.getGuid()));
                    smsMessageResponse.setStatus("STATUS_FAILURE");
                    smsMessageResponse.setMessage("SMSC type is not supported.".getBytes(StandardCharsets.UTF_8));
                    OperationError operationError = new OperationError();
                    operationError.setMessage("SMSC type is not supported.");
                }
                // persist response
                smsMessageRepository.save(smsMessageResponse);
                return smsMessageResponse;
            }
            smsMessageResponse.setGuid(StringUtil.trimToEmpty(dto.getGuid()));
            smsMessageResponse.setStatus("STATUS_FAILURE");
            smsMessageResponse.setMessage("Authentication failure.".getBytes(StandardCharsets.UTF_8));
            smsMessageRepository.save(smsMessageResponse);
            var7 = smsMessageResponse;
        } catch (Exception jsmsExp) {
            jsmsExp.printStackTrace();
        }
        return var7;
    }


    public static String createGUID() {
        // Use the time-based UUID generator from java-uuid-generator library
        UUID uuid = Generators.timeBasedGenerator().generate();
        return uuid.toString();
    }

    public boolean isAuthenticationFailed(String ip) {
        return false;
    }

    // helper to populate failure response with message bytes and serialized value
    private void setFailureMessageResponse(SMSMessageResponse resp, java.util.List<String> errors) {
        String combined = String.join("; ", errors);
        resp.setMessage(combined.getBytes(StandardCharsets.UTF_8));
        // also set serialized value for downstream processing
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            byte[] serialized = mapper.writeValueAsBytes(resp);
            resp.setValue(serialized);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isTextMessageValidationFailed(SMSMessageDTO smsMessage, SMSMessageResponse smsMessageResponse) {
        boolean validationFailedFlag = false;
        ArrayList<String> errorList = new ArrayList<>();
        if (StringUtil.isBlank(smsMessage.getSmsc())) {
            errorList.add("Smsc parameter is blank.");
            validationFailedFlag = true;
        }

        if (StringUtil.isBlank(smsMessage.getTelco())) {
            errorList.add("Telco parameter is blank.");
            validationFailedFlag = true;
        }

        if (StringUtil.isBlank(smsMessage.getMoid())) {
            errorList.add("Moid parameter is blank.");
            validationFailedFlag = true;
        }

        if (StringUtil.isBlank(smsMessage.getRecipient())) {
            errorList.add("Recipient parameter is blank.");
            validationFailedFlag = true;
        }

        if (StringUtil.isBlank(smsMessage.getMessage()) && (smsMessage.getMessageBytes() == null || smsMessage.getMessageBytes().length == 0)) {
            errorList.add("Message parameter is blank.");
            validationFailedFlag = true;
        }

        if (StringUtil.isBlank(String.valueOf(smsMessage.getMessageType()))) {
            errorList.add("Message type parameter is blank.");
            validationFailedFlag = true;
        }

        if (!StringUtil.trimToEmpty(smsMessage.getSmsc()).equalsIgnoreCase("smpp")) {
            errorList.add("Smsc parameter value is invalid.");
            validationFailedFlag = true;
        }

        if (validationFailedFlag) {
            this.setFailureMessageResponse(smsMessageResponse, errorList);
        }

        return validationFailedFlag;
    }

    public boolean isSmscSmpp(SMSMessageDTO smsMessage) {
        return smsMessage.getSmsc().equalsIgnoreCase("smpp");
    }


}
