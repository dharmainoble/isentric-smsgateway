package com.isentric.bulkgateway.repository;

import com.isentric.bulkgateway.dto.extMTObject;
import com.isentric.bulkgateway.model.SMSMessageResponse;
import com.isentric.bulkgateway.model.SMSMessageSmpp;
import com.isentric.bulkgateway.model.SMSMessageSent;
import com.isentric.bulkgateway.utility.DateUtil;
import com.isentric.bulkgateway.utility.SmsUtil;
import com.isentric.bulkgateway.utility.StringUtil;
import msg.SmsMessage;
import msg.ems.EMSMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

    public int updateSmppSent(EMSMessage smsMessage, String eventStatus) throws SQLException {
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

    public int updateSmppId(EMSMessage smsMessage, String smppId) throws SQLException {
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

    public int updateSmppError(EMSMessage smsMessage, String error) throws SQLException {
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

    public int updateSmppSent2(com.objectxp.msg.SmsMessage smsMessage, String eventStatus, String transactionId) throws SQLException {
        String sql = "UPDATE bulk_gateway.tbl_smpp_sent SET smppType='" + SmsUtil.getSmppStatusType(smsMessage.getType()) + "', smppStatus='" + StringUtil.trimToEmpty(eventStatus) + "', timestamp='" + StringUtil.trimToEmpty(DateUtil.getDateYYYYMMDDHHMMSS(smsMessage.getTimestamp())) + "', bytes='" + StringUtil.replaceSingleQuote(StringUtil.replaceBackSlash(StringUtil.byteToString(smsMessage.getBytes()))) + "', transactionId = '" + StringUtil.trimToEmpty(transactionId) + "' " + "WHERE guid='" + smsMessage.getProperty("SMPP_GUID") + "';";
        int updateResult = 0;

        try {
            try {
                Thread.sleep(1000L);
                updateResult = this.update(sql);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return updateResult;
        } finally {
            ;
        }
    }

    public int insertSmppDn2(String smppName, String smppId, String sender, String recipient, String smppStatus, String errorCode, String errorList) throws SQLException {
        String sql = "INSERT INTO bulk_gateway.tbl_smpp_dn(row_id,smppName,smppId,smppType,sender,recipient,timestamp,smppStatus,errorCode,dcs,date,bytes) VALUES (null,'" + smppName + "','" + smppId + "','MT_STATUS','" + sender + "','6" + recipient + "',now(),'" + smppStatus + "','" + errorCode + "','240',now(),'" + errorList + "')";
        return this.update(sql);
    }

    public int updateTelcoRoute() throws SQLException {
        String sql = "UPDATE bulk_config.credit_control set route = 'HTTP_INFOBIP' WHERE country='maxis'";
        return this.update(sql);
    }

    public ArrayList<extMTObject> queryCustInfo(String cpidentity) throws SQLException {
        Connection conn = null;
        ArrayList<extMTObject> bulkinfo = new ArrayList();

        try {
            conn = DBManagerDs.getManager().getConnection();
            String SQL = "SELECT c.cp_ip,c.shortcode,c.cpidentity,b.masking_id FROM bulk_config.cpip c LEFT JOIN bulk_config.bulk_mask_id_control b ON c.cpidentity = b.custid WHERE c.cpidentity='" + cpidentity + "' AND b.enabled_flag=0 LIMIT 1;";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);
            System.out.println(SQL);
            extMTObject ext = new extMTObject();
            System.out.println("rs>>>" + rs.next());

            while(rs.next()) {
                ext.setCpip(rs.getString("c.cp_ip") != null ? rs.getString("c.cp_ip") : "");
                ext.setShortcode(rs.getString("c.shortcode") != null ? rs.getString("c.shortcode") : "");
                ext.setCustId(rs.getString("c.cpidentity") != null ? rs.getString("c.cpidentity") : "");
                ext.setSMsisdn(rs.getString("b.masking_id") != null ? rs.getString("b.masking_id") : "");
                bulkinfo.add(ext);
            }

            rs.close();
        } catch (Exception e) {
            System.out.println("Error in queryCustInfo >>>" + e.getMessage());
        } finally {
            ;
        }

        System.out.println("bulkinfo>>>" + bulkinfo.size());
        DBManagerDs.getManager().freeConnection(conn);
        return bulkinfo;
    }

    public int insertModemSent(String Guid, String GroupId, String Telco, String SmppName, String SmppId, String Moid, String Sender, String Recipient, int SenderType, String Message, String Shortcode, String UserGroup, String Keyword, int sequence, String credit, String eventStatus) throws SQLException {
        String sql = "INSERT INTO bulk_gateway.tbl_modem_sent (row_id,guid,groupId,telco,smppName,smppId,moid,sender,recipient,senderType,date,message,shortcode,userGroup,keyword,message_sequence,credit,smppStatus) VALUES (null,'" + StringUtil.trimToEmpty(Guid) + "','" + StringUtil.trimToEmpty(GroupId) + "','" + StringUtil.trimToEmpty(Telco) + "','" + StringUtil.trimToEmpty(SmppName) + "','" + StringUtil.trimToEmpty(SmppId) + "','" + StringUtil.trimToEmpty(Moid) + "','" + StringUtil.trimToEmpty(Sender) + "','" + StringUtil.trimToEmpty(Recipient) + "','" + StringUtil.trimToEmpty(String.valueOf(SenderType)) + "',now(),'" + StringUtil.replaceSingleQuote(StringUtil.replaceBackSlash(StringUtil.trimToEmpty(Message))) + "','" + StringUtil.trimToEmpty(Shortcode) + "','" + StringUtil.trimToEmpty(UserGroup) + "','" + StringUtil.trimToEmpty(Keyword) + "', " + sequence + ",'" + credit + "','" + StringUtil.trimToEmpty(eventStatus) + "')";
        return this.update(sql);
    }

  /*  public ArrayList<SMSMessageSmpp> findSmppSentDnByGroupId(String groupId) throws SQLException {
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
    }*/

}
