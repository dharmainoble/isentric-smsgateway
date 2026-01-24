package com.isentric.bulkgateway.repository;

import com.isentric.bulkgateway.model.SMSMessageResponse;
import com.isentric.bulkgateway.model.SMSMessageSmpp;
import com.isentric.bulkgateway.model.SMSMessageSent;
import com.isentric.bulkgateway.utility.DateUtil;
import com.isentric.bulkgateway.utility.SmsUtil;
import com.isentric.bulkgateway.utility.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MessageServiceDao {

    @Autowired
    private SMSMessageSmppRepository smsMessageSmppRepository;

    @Autowired
    private SMSMessageRepository smsMessageRepository;

    @Autowired
    private SMSMessageSentRepository smsMessageSentRepository;

    // Insert incoming SMPP record (tbl_smpp_in)
    public int insertSmppIn(SMSMessageSmpp smsMessageSmpp, String invokerIpAddress) throws SQLException {
        // set ip from invoker
        if (smsMessageSmpp != null) {
            smsMessageSmpp.setIp(invokerIpAddress);
            smsMessageSmppRepository.save(smsMessageSmpp);
            return 1;
        }
        return 0;
    }

    // Insert sent SMPP record (tbl_smpp_sent) - save into SMSMessageSent entity
    public int insertSmppSent(SMSMessageSmpp smsMessageSmpp) throws SQLException {
        if (smsMessageSmpp != null) {
            SMSMessageSent sent = new SMSMessageSent();
            sent.setGuid(smsMessageSmpp.getGuid());
            sent.setGroupId(smsMessageSmpp.getGroupId());
            sent.setTelco(smsMessageSmpp.getTelco());
            sent.setSmppName(smsMessageSmpp.getSmppName());
            sent.setMoid(smsMessageSmpp.getMoid());
            sent.setSender(smsMessageSmpp.getSender());
            sent.setRecipient(smsMessageSmpp.getRecipient());
            sent.setMessage(smsMessageSmpp.getMessage());
            //sent.setShortcode(smsMessageSmpp.getShortcode());
            sent.setUserGroup(smsMessageSmpp.getUserGroup());
            sent.setKeyword(smsMessageSmpp.getKeyword());
            sent.setCredit(smsMessageSmpp.getCredit());
            smsMessageSentRepository.save(sent);
            return 1;
        }
        return 0;
    }

    // convenience methods used throughout the codebase
    public int insertSmppSent2(String Guid, String GroupId, String Telco, String SmppName, String SmppId, String Moid, String Sender, String Recipient, int SenderType, String Message, String Shortcode, String UserGroup, String Keyword, int sequence, String credit) throws SQLException {
        SMSMessageSent sent = new SMSMessageSent();
        sent.setGuid(Guid);
        sent.setGroupId(GroupId);
        sent.setTelco(Telco);
        sent.setSmppName(SmppName);
        sent.setSmppId(SmppId);
        sent.setMoid(Moid);
        sent.setSender(Sender);
        sent.setRecipient(Recipient);
        sent.setSenderType(SenderType);
        sent.setMessage(Message);
        sent.setShortcode(Shortcode);
        sent.setUserGroup(UserGroup);
        sent.setKeyword(Keyword);
        sent.setMessageSequence(sequence);
        sent.setCredit(credit);
        smsMessageSentRepository.save(sent);
        return 1;
    }

    public int insertSmppSent3(String Guid, String GroupId, String Telco, String SmppName, String SmppId, String Moid, String Sender, String Recipient, int SenderType, String Message, String Shortcode, String UserGroup, String Keyword, int sequence, String credit, String mtPrice) throws SQLException {
        SMSMessageSent sent = new SMSMessageSent();
        sent.setGuid(Guid);
        sent.setGroupId(GroupId);
        sent.setTelco(Telco);
        sent.setSmppName(SmppName);
        sent.setSmppId(SmppId);
        sent.setMoid(Moid);
        sent.setSender(Sender);
        sent.setRecipient(Recipient);
        sent.setSenderType(SenderType);
        sent.setMessage(Message);
        sent.setShortcode(Shortcode);
        sent.setUserGroup(UserGroup);
        sent.setKeyword(Keyword);
        sent.setMessageSequence(sequence);
        sent.setCredit(credit);
        sent.setPrice(mtPrice);
        smsMessageSentRepository.save(sent);
        return 1;
    }

    public int insertSmppDn(String smppName, Object statusReportMessage) throws SQLException {
        // This method previously built a SQL string with various status types. Keep a minimal implementation: save an entry in sent with status bytes
        SMSMessageSent sent = new SMSMessageSent();
        sent.setSmppName(smppName);
        sent.setSmppStatus(statusReportMessage != null ? statusReportMessage.toString() : null);
        smsMessageSentRepository.save(sent);
        return 1;
    }

    public int updateSmppSent(SMSMessageSmpp smsMessage, String eventStatus) throws SQLException {
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        SMSMessageSent existing = smsMessageSentRepository.findByGuid(smsMessage.getProperty("SMPP_GUID"));
        if (existing != null) {
            existing.setSmppType(SmsUtil.getSmppStatusType(smsMessage.getType()));
            existing.setSmppStatus(StringUtil.trimToEmpty(eventStatus));
            existing.setTimestamp(StringUtil.trimToEmpty(DateUtil.getDateYYYYMMDDHHMMSS(smsMessage.getTimestamp())));
            existing.setBytes(StringUtil.replaceSingleQuote(StringUtil.replaceBackSlash(StringUtil.byteToString(smsMessage.getBytes()))));
            smsMessageSentRepository.save(existing);
            return 1;
        }
        return 0;
    }

    public int updateSmppId(SMSMessageSmpp smsMessage, String smppId) throws SQLException {
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        SMSMessageSent existing = smsMessageSentRepository.findByGuid(smsMessage.getProperty("SMPP_GUID"));
        if (existing != null) {
            existing.setSmppId(smppId);
            smsMessageSentRepository.save(existing);
            return 1;
        }
        return 0;
    }

    public int updateSmppError(SMSMessageSmpp smsMessage, String error) throws SQLException {
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        SMSMessageSent existing = smsMessageSentRepository.findByGuid(smsMessage.getProperty("SMPP_GUID"));
        if (existing != null) {
            existing.setError(error);
            smsMessageSentRepository.save(existing);
            return 1;
        }
        return 0;
    }

    public int insertSmppResponse(SMSMessageResponse smsMessageResponse) throws SQLException {
        if (smsMessageResponse != null) {
            smsMessageRepository.save(smsMessageResponse);
            return 1;
        }
        return 0;
    }

    public ArrayList<SMSMessageSmpp> findSmppSentDnByGroupId(String groupId) throws SQLException {
        List<SMSMessageSent> rows = smsMessageSentRepository.findByGroupId(groupId);
        ArrayList<SMSMessageSmpp> sentDnList = new ArrayList<>();
        if (rows != null && !rows.isEmpty()) {
            for (SMSMessageSent r : rows) {
                SMSMessageSmpp sms = new SMSMessageSmpp();
                sms.setGuid(r.getGuid());
                sms.setGroupId(r.getGroupId());
                sms.setTelco(r.getTelco());
                sms.setSmppId(r.getSmppId());
                sms.setSmppName(r.getSmppName());
                sms.setMessage(r.getMessage());
                sms.setTimestamp(r.getTimestamp());
                sms.setSender(r.getSender());
                sms.setRecipient(r.getRecipient());
                sms.setSmppStatus(r.getSmppStatus());
                sentDnList.add(sms);
            }
        }
        return sentDnList;
    }

}
