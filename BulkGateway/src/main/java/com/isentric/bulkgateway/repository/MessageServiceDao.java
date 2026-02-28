package com.isentric.bulkgateway.repository;

import com.isentric.bulkgateway.bg.model.SMSMessageSent;
import com.isentric.bulkgateway.bg.model.SMSMessageSmpp;
import com.isentric.bulkgateway.dto.extMTObject;
import com.isentric.bulkgateway.utility.DateUtil;
import com.isentric.bulkgateway.utility.SmsUtil;
import com.isentric.bulkgateway.utility.StringUtil;
import com.objectxp.msg.SmsMessage;
import msg.ems.EMSMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import com.isentric.bulkgateway.utility.EntityManagerFactoryProvider;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

@Component
public class MessageServiceDao {


    @Autowired
    private SMSMessageSmppRepository smsMessageSmppRepository;


    @Autowired
    private SMSMessageRepository smsMessageRepository;


    @Autowired
    private SMSMessageSentRepository smsMessageSentRepository;

    // helper: save SMSMessageSent using repository if available, otherwise use EntityManager
    private void saveSMSMessageSent(SMSMessageSent sent) throws SQLException {
        if (sent == null) return;
        if (smsMessageSentRepository != null) {
            smsMessageSentRepository.save(sent);
            return;
        }
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = EntityManagerFactoryProvider.getEntityManager("bulkgateway");
            tx = em.getTransaction();
            tx.begin();
            // Try JPA merge first (works when EM manages the entity)
            try {
                em.merge(sent);
                tx.commit();
                return;
            } catch (Exception iae) {
                // Catch broad Exception so Hibernate UnknownEntityTypeException (a runtime) is handled
                iae.printStackTrace();
                // This indicates the EM doesn't manage the entity type (UnknownEntityTypeException)
                if (tx != null && tx.isActive()) {
                    try { tx.rollback(); } catch (Exception e) {}
                }
                // fallthrough to native insert
            }
            // As a fallback, perform native INSERT into tbl_smpp_sent to keep legacy behavior
            String sql = "INSERT INTO bulk_gateway.tbl_smpp_sent (row_id,guid,groupId,telco,smppName,smppId,moid,sender,recipient,senderType,date,message,shortcode,userGroup,keyword,message_sequence,credit,price,smppType,smppStatus,timestamp,bytes,transactionId,error) VALUES (null,'" + StringUtil.trimToEmpty(sent.getGuid()) + "','" + StringUtil.trimToEmpty(sent.getGroupId()) + "','" + StringUtil.trimToEmpty(sent.getTelco()) + "','" + StringUtil.trimToEmpty(sent.getSmppName()) + "','" + StringUtil.trimToEmpty(sent.getSmppId()) + "','" + StringUtil.trimToEmpty(sent.getMoid()) + "','" + StringUtil.trimToEmpty(sent.getSender()) + "','" + StringUtil.trimToEmpty(sent.getRecipient()) + "','" + (sent.getSenderType() != null ? StringUtil.trimToEmpty(String.valueOf(sent.getSenderType())) : "0") + "',now(),'" + StringUtil.replaceSingleQuote(StringUtil.replaceBackSlash(StringUtil.trimToEmpty(sent.getMessage()))) + "','" + StringUtil.trimToEmpty(sent.getShortcode()) + "','" + StringUtil.trimToEmpty(sent.getUserGroup()) + "','" + StringUtil.trimToEmpty(sent.getKeyword()) + "'," + (sent.getMessageSequence() != null ? sent.getMessageSequence() : 0) + ", '" + StringUtil.trimToEmpty(sent.getCredit()) + "','" + StringUtil.trimToEmpty(sent.getPrice()) + "','" + StringUtil.trimToEmpty(sent.getSmppType()) + "','" + StringUtil.trimToEmpty(sent.getSmppStatus()) + "','" + StringUtil.trimToEmpty(sent.getTimestamp()) + "','" + StringUtil.replaceSingleQuote(StringUtil.replaceBackSlash(StringUtil.trimToEmpty(sent.getBytes()))) + "','" + StringUtil.trimToEmpty(sent.getTransactionId()) + "','" + StringUtil.trimToEmpty(sent.getError()) + "')";
            this.update(sql);
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            if (tx != null && tx.isActive()) {
                try { tx.rollback(); } catch (Exception e) {}
            }
            throw new SQLException("Error persisting SMSMessageSent", ex);
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
    }

    public int updateSmppSentMO(SmsMessage smsMessage, String eventStatus) throws SQLException {
        System.out.println("updateSmppSentMO for message with GUID: " + smsMessage.getProperty("SMPP_GUID") + " and eventStatus: " + eventStatus);
        System.out.println(SmsUtil.getSmppStatusType(smsMessage.getType()));

        String sql = "UPDATE bulk_gateway.tbl_smpp_sent SET smppType='" + SmsUtil.getSmppStatusType(smsMessage.getType()) + "', smppStatus='" + StringUtil.trimToEmpty(eventStatus) + "', timestamp='" + StringUtil.trimToEmpty(DateUtil.getDateYYYYMMDDHHMMSS(smsMessage.getTimestamp())) + "', bytes='" + StringUtil.replaceSingleQuote(StringUtil.replaceBackSlash(StringUtil.byteToString(smsMessage.getBytes()))) + "' " + "WHERE guid='" + smsMessage.getProperty("SMPP_GUID") + "';";
        int updateResult = 0;
        try {
            try {
                Thread.sleep(1000L);
                System.out.println(sql);
                updateResult = this.update(sql);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return updateResult;
        } finally {
            ;
        }
    }

    // helper: save SMSMessageSmpp using repository if available, otherwise use EntityManager
    private void saveSMSMessageSmpp(SMSMessageSmpp in) throws SQLException {
        if (in == null) return;
        if (smsMessageSmppRepository != null) {
            smsMessageSmppRepository.save(in);
            return;
        }
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = EntityManagerFactoryProvider.getEntityManager("bulkgateway");
            tx = em.getTransaction();
            tx.begin();
            em.merge(in);
            tx.commit();
        } catch (RuntimeException ex) {
            if (tx != null && tx.isActive()) {
                try { tx.rollback(); } catch (Exception e) {}
            }
            throw new SQLException("Error persisting SMSMessageSmpp", ex);
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
    }

    // Insert incoming SMPP record (tbl_smpp_in)
    public int insertSmppIn(SMSMessageSmpp smsMessageSmpp, String invokerIpAddress) throws SQLException {
        // set ip from invoker
        if (smsMessageSmpp != null) {
            smsMessageSmpp.setIp(invokerIpAddress);
            saveSMSMessageSmpp(smsMessageSmpp);
            return 1;
        }
        return 0;
    }

    // Insert sent SMPP record (tbl_smpp_sent) - save into SMSMessageSent entity
    public int insertSmppSent(SMSMessageSmpp smsMessageSmpp,String Status) throws SQLException {
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
            sent.setShortcode(smsMessageSmpp.getShortcode());
            sent.setUserGroup(smsMessageSmpp.getUserGroup());
            sent.setKeyword(smsMessageSmpp.getKeyword());
            sent.setCredit(smsMessageSmpp.getCredit());
            sent.setSmppStatus(Status);
            saveSMSMessageSent(sent);
            return 1;
        }
        return 0;
    }

    // convenience methods used throughout the codebase
    public int insertSmppSent2(String Guid, String GroupId, String Telco, String SmppName, String SmppId, String Moid, String Sender, String Recipient, int SenderType, String Message, String Shortcode, String UserGroup, String Keyword, int sequence, String credit) throws SQLException {
        System.out.println("inster insertSmppSent2");
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
        saveSMSMessageSent(sent);
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
        saveSMSMessageSent(sent);
        return 1;
    }

    public int insertSmppDn(String smppName, Object statusReportMessage) throws SQLException {
        // This method previously built a SQL string with various status types. Keep a minimal implementation: save an entry in sent with status bytes
        SMSMessageSent sent = new SMSMessageSent();
        sent.setSmppName(smppName);
        sent.setSmppStatus(statusReportMessage != null ? statusReportMessage.toString() : null);
        saveSMSMessageSent(sent);
        return 1;
    }

    public int updateSmppSent(EMSMessage smsMessage, String eventStatus) throws SQLException {
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        SMSMessageSent existing = null;
        if (smsMessageSentRepository != null) {
            existing = smsMessageSentRepository.findByGuid(smsMessage.getProperty("SMPP_GUID"));
        } else {
            // fallback: query using EntityManager
            EntityManager em = null;
            try {
                em = EntityManagerFactoryProvider.getEntityManager("bulkgateway");
                existing = em.find(SMSMessageSent.class, smsMessage.getProperty("SMPP_GUID"));
            } catch (Exception ex) {
                // ignore - existing stays null
            } finally {
                if (em != null && em.isOpen()) em.close();
            }
        }

        if (existing != null) {
            existing.setSmppType(SmsUtil.getSmppStatusType(smsMessage.getType()));
            existing.setSmppStatus(StringUtil.trimToEmpty(eventStatus));
            existing.setTimestamp(StringUtil.trimToEmpty(DateUtil.getDateYYYYMMDDHHMMSS(smsMessage.getTimestamp())));
            existing.setBytes(StringUtil.replaceSingleQuote(StringUtil.replaceBackSlash(StringUtil.byteToString(smsMessage.getBytes()))));
            saveSMSMessageSent(existing);
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

        SMSMessageSent existing = null;
        if (smsMessageSentRepository != null) {
            existing = smsMessageSentRepository.findByGuid(smsMessage.getProperty("SMPP_GUID"));
        } else {
            EntityManager em = null;
            try {
                em = EntityManagerFactoryProvider.getEntityManager("bulkgateway");
                existing = em.find(SMSMessageSent.class, smsMessage.getProperty("SMPP_GUID"));
            } catch (Exception ex) {
            } finally {
                if (em != null && em.isOpen()) em.close();
            }
        }
        if (existing != null) {
            existing.setSmppId(smppId);
            saveSMSMessageSent(existing);
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

        SMSMessageSent existing = null;
        if (smsMessageSentRepository != null) {
            existing = smsMessageSentRepository.findByGuid(smsMessage.getProperty("SMPP_GUID"));
        } else {
            EntityManager em = null;
            try {
                em = EntityManagerFactoryProvider.getEntityManager("bulkgateway");
                existing = em.find(SMSMessageSent.class, smsMessage.getProperty("SMPP_GUID"));
            } catch (Exception ex) {
            } finally {
                if (em != null && em.isOpen()) em.close();
            }
        }
        if (existing != null) {
            existing.setError(error);
            saveSMSMessageSent(existing);
            return 1;
        }
        return 0;
    }

   /* public int insertSmppResponse(SMSMessageResponse smsMessageResponse) throws SQLException {
        if (smsMessageResponse != null) {
            smsMessageRepository.save(smsMessageResponse);
            return 1;
        }
        return 0;
    }*/

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
        return bulkinfo;
    }

    public int insertModemSent(String Guid, String GroupId, String Telco, String SmppName, String SmppId, String Moid, String Sender, String Recipient, int SenderType, String Message, String Shortcode, String UserGroup, String Keyword, int sequence, String credit, String eventStatus) throws SQLException {
        String sql = "INSERT INTO bulk_gateway.tbl_modem_sent (row_id,guid,groupId,telco,smppName,smppId,moid,sender,recipient,senderType,date,message,shortcode,userGroup,keyword,message_sequence,credit,smppStatus) VALUES (null,'" + StringUtil.trimToEmpty(Guid) + "','" + StringUtil.trimToEmpty(GroupId) + "','" + StringUtil.trimToEmpty(Telco) + "','" + StringUtil.trimToEmpty(SmppName) + "','" + StringUtil.trimToEmpty(SmppId) + "','" + StringUtil.trimToEmpty(Moid) + "','" + StringUtil.trimToEmpty(Sender) + "','" + StringUtil.trimToEmpty(Recipient) + "','" + StringUtil.trimToEmpty(String.valueOf(SenderType)) + "',now(),'" + StringUtil.replaceSingleQuote(StringUtil.replaceBackSlash(StringUtil.trimToEmpty(Message))) + "','" + StringUtil.trimToEmpty(Shortcode) + "','" + StringUtil.trimToEmpty(UserGroup) + "','" + StringUtil.trimToEmpty(Keyword) + "', " + sequence + ",'" + credit + "','" + StringUtil.trimToEmpty(eventStatus) + "')";
        return this.update(sql);
    }

    // helper to execute raw update SQL when needed (keeps compatibility with legacy code)
    private int update(String sql) throws SQLException {
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = EntityManagerFactoryProvider.getEntityManager("bulkgateway");
            tx = em.getTransaction();
            tx.begin();
            Query q = em.createNativeQuery(sql);
            int result = q.executeUpdate();
            tx.commit();
            return result;
        } catch (RuntimeException ex) {
            if (tx != null && tx.isActive()) {
                try { tx.rollback(); } catch (Exception e) {}
            }
            throw new SQLException("Error executing update", ex);
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
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

