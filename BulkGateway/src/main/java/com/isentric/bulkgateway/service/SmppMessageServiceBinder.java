package com.isentric.bulkgateway.service;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.isentric.bulkgateway.bg.model.SMSMessageSmpp;
import com.isentric.bulkgateway.dto.*;
import com.isentric.bulkgateway.exception.MessageException;
import com.isentric.bulkgateway.exception.SMSException;
import com.isentric.bulkgateway.extmt.webservice.ExternalMTPushInterface;
import com.isentric.bulkgateway.extmt.webservice.ExternalMTPushInterfaceService;
import com.isentric.bulkgateway.extmt.webservice.ExternalMTPushInterfaceServiceLocator;
import com.isentric.bulkgateway.listener.MOListenerSmpp;
import com.isentric.bulkgateway.manager.LoggerManager;
import com.isentric.bulkgateway.manager.MaxisDNStatusManager;
import com.isentric.bulkgateway.manager.PrefixManager;
import com.isentric.bulkgateway.manager.VoidMessagesPrefixManager;
import com.isentric.bulkgateway.repository.DigiSessionRepository;
import com.isentric.bulkgateway.repository.BulkSkipAutoResendRepository;
import com.isentric.bulkgateway.repository.MessageServiceDao;
import com.isentric.bulkgateway.util.UidUtil;
import com.isentric.bulkgateway.utility.*;
import com.isentric.bulkgateway.webservice.*;
import com.objectxp.msg.*;
import com.objectxp.msg.ota.ServiceIndication;
import com.objectxp.msg.smart.OperatorLogo;
import com.objectxp.msg.smart.PictureMessage;
import com.objectxp.msg.smart.Ringtone;
import msg.ems.EMSMessage;
import org.apache.commons.jcs.access.exception.CacheException;
import org.apache.log4j.Logger;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import javax.net.ssl.SSLException;
import java.io.*;
import java.lang.annotation.Annotation;
import java.math.BigInteger;
import java.net.*;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.hibernate.service.spi.ServiceException;
import org.safehaus.uuid.UUIDGenerator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsProperties;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class SmppMessageServiceBinder {

    private static int value = 0;
    private static final Logger logger = LoggerManager.createLoggerPattern(SmppMessageServiceBinder.class);
    private static Hashtable<String, Object> binderHashtable = new Hashtable();
    private static Hashtable<String, Object> maskingHashtable = new Hashtable();
    public static final String PROVIDER_ID = "P-R0Sr4BNJxH3";
    public static final String PROVIDER_PASSWORD = "isentric123";

    @Autowired
    private MessageServiceDao messageServiceDao;

    //private static final Dao dao = new Dao();

    private String void_custid = "";

    /**
     * Open a configuration file as an InputStream.
     * Tries (in order):
     *  - filesystem path (absolute or relative)
     *  - classpath resource via classloader
     *  - class resource with a leading '/'
     *
     * Throws FileNotFoundException when none found so Properties.load(...) never receives a null stream.
     */
    private InputStream openConfigStream(String path) throws IOException {
        if (path == null) {
            throw new FileNotFoundException("Configuration path is null");
        }

        // Try filesystem first (absolute or relative)
        File f = new File(path);
        if (f.exists() && f.isFile() && f.canRead()) {
            return new FileInputStream(f);
        }

        // Try classloader resource (no leading slash)
        String cpPath = path.startsWith("/") ? path.substring(1) : path;
        InputStream is = SmppMessageServiceBinder.class.getClassLoader().getResourceAsStream(cpPath);
        if (is != null) {
            return is;
        }

        // Try class resource with leading slash
        String classResPath = path.startsWith("/") ? path : "/" + path;
        is = SmppMessageServiceBinder.class.getResourceAsStream(classResPath);
        if (is != null) {
            return is;
        }

        throw new FileNotFoundException("Configuration file not found on filesystem or classpath: " + path);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int inValue) {
        value = inValue;
    }

    public synchronized void setupGSM(String smppName, String smppConfig, boolean receiveFlag) throws SMSException, MessageException, IOException, com.objectxp.msg.MessageException {
        if (!binderHashtable.containsKey(smppName)) {
            Properties jSMSPropertiesSmpp = new Properties();
            try (InputStream is = openConfigStream(smppConfig)) {
                jSMSPropertiesSmpp.load(is);
            }
            GsmSmsService gsmSmsServiceBinder = new GsmSmsService();
            logger.info("GSM binder initiated. (name=" + smppName + ",config=" + smppConfig + ")");
            MOListenerSmpp moListenerSmpp = new MOListenerSmpp(smppName);
            gsmSmsServiceBinder.addMessageEventListener(moListenerSmpp);
            gsmSmsServiceBinder.connect();
            if (receiveFlag) {
                gsmSmsServiceBinder.startReceiving();
            }

            logger.info("GSM listener connected.  (name=" + smppName + ",config=" + smppConfig + ")");
            logger.info("- Service Name   : " + gsmSmsServiceBinder.getServiceName());
            binderHashtable.put(smppName, gsmSmsServiceBinder);
        } else {
            logger.info("GSM connection is currently binded and it can be setup once only. (name=" + smppName + ",config=" + smppConfig + ")");
            throw new SMSException("GSM connection is currently binded and it can be setup once only. (name=" + smppName + ",config=" + smppConfig + ")");
        }
    }

    public void setupSmpp(String smppName, String smppConfig) throws SMSException, MessageException, IOException, com.objectxp.msg.MessageException {
        if (!binderHashtable.containsKey(smppName)) {
            Properties jSMSPropertiesSmpp = new Properties();
            System.out.println(smppConfig);
            try (InputStream is = openConfigStream(smppConfig)) {
                jSMSPropertiesSmpp.load(is);
            }
            SmppSmsService smppSmsServiceBinder = new SmppSmsService();
            // Try to initialize the service if an init/initialize method exists on the implementation (use reflection
            // because the compile-time interface may not declare it). Some objectxp versions require init(Properties).
            try {
                boolean initialized = false;
                try {
                    java.lang.reflect.Method m = smppSmsServiceBinder.getClass().getMethod("init", java.util.Properties.class);
                    m.setAccessible(true);
                    m.invoke(smppSmsServiceBinder, jSMSPropertiesSmpp);
                    initialized = true;
                    logger.info("Called init(Properties) on SmppSmsService implementation for " + smppName);
                } catch (NoSuchMethodException nsme) {
                    // try alternative method names / signatures
                    try {
                        java.lang.reflect.Method m2 = smppSmsServiceBinder.getClass().getMethod("initialize", java.util.Properties.class);
                        m2.setAccessible(true);
                        m2.invoke(smppSmsServiceBinder, jSMSPropertiesSmpp);
                        initialized = true;
                        logger.info("Called initialize(Properties) on SmppSmsService implementation for " + smppName);
                    } catch (NoSuchMethodException nsme2) {
                        try {
                            java.lang.reflect.Method m3 = smppSmsServiceBinder.getClass().getMethod("init");
                            m3.setAccessible(true);
                            m3.invoke(smppSmsServiceBinder);
                            initialized = true;
                            logger.info("Called init() on SmppSmsService implementation for " + smppName);
                        } catch (NoSuchMethodException nsme3) {
                            // no known init method found - proceed and rely on implementation defaults
                            logger.info("No init method found on SmppSmsService implementation for " + smppName + ", proceeding without explicit init.");
                        }
                    }
                }

                if (!initialized) {
                    // Some implementations require setting properties via setProperty or put; try common patterns
                    try {
                        java.lang.reflect.Method setProps = smppSmsServiceBinder.getClass().getMethod("setProperties", java.util.Properties.class);
                        setProps.setAccessible(true);
                        setProps.invoke(smppSmsServiceBinder, jSMSPropertiesSmpp);
                        logger.info("Called setProperties(Properties) on SmppSmsService implementation for " + smppName);
                    } catch (NoSuchMethodException ignored) {
                        // ignore - nothing more we can do safely
                    }
                }
            } catch (IllegalAccessException | java.lang.reflect.InvocationTargetException ex) {
                logger.fatal("Failed to invoke init/initialize on SmppSmsService implementation: " + ex.getMessage());
            }
            //smppSmsServiceBinder.init(jSMSPropertiesSmpp); - init() method not available in current objectxp library
            logger.info("SMPP binder initiated. (name=" + smppName + ",config=" + smppConfig + ")");
            MOListenerSmpp moListenerSmpp = new MOListenerSmpp(smppName);
            // Some SmppSmsService implementations require the service to be initialized/connected
            // before addMessageEventListener can be called (otherwise they throw "Service must be initialized first").
            // Connect first, then register the listener and start receiving.
            System.out.println(jSMSPropertiesSmpp.getProperty("smpp.login") );
            smppSmsServiceBinder.connect();
            // Attempt to register listener; if the implementation complains the service is not initialized,
            // try a few additional fallback init/initialize signatures via reflection and retry.
            boolean listenerAdded = false;
            int attempts = 0;
            while (!listenerAdded && attempts < 3) {
                try {
                    smppSmsServiceBinder.addMessageEventListener(moListenerSmpp);
                    listenerAdded = true;
                } catch (IllegalStateException ise) {
                    logger.warn("addMessageEventListener failed due to illegal state (attempt " + (attempts+1) + "): " + ise.getMessage());
                    // Try several fallback initialization approaches via reflection
                    try {
                        try {
                            java.lang.reflect.Method initNoArg = smppSmsServiceBinder.getClass().getMethod("initialize");
                            initNoArg.setAccessible(true);
                            initNoArg.invoke(smppSmsServiceBinder);
                            logger.info("Called initialize() on SmppSmsService implementation for " + smppName + " (fallback)");
                        } catch (NoSuchMethodException ns1) {
                            try {
                                java.lang.reflect.Method initProps = smppSmsServiceBinder.getClass().getMethod("initialize", java.util.Properties.class);
                                initProps.setAccessible(true);
                                initProps.invoke(smppSmsServiceBinder, jSMSPropertiesSmpp);
                                logger.info("Called initialize(Properties) on SmppSmsService implementation for " + smppName + " (fallback)");
                            } catch (NoSuchMethodException ns2) {
                                try {
                                    java.lang.reflect.Method setProps = smppSmsServiceBinder.getClass().getMethod("setProperties", java.util.Properties.class);
                                    setProps.setAccessible(true);
                                    setProps.invoke(smppSmsServiceBinder, jSMSPropertiesSmpp);
                                    logger.info("Called setProperties(Properties) on SmppSmsService implementation for " + smppName + " (fallback)");
                                } catch (NoSuchMethodException ns3) {
                                    logger.debug("No known fallback init method found on SmppSmsService implementation for " + smppName);
                                }
                            }
                        }
                    } catch (IllegalAccessException | java.lang.reflect.InvocationTargetException ex) {
                        logger.warn("Fallback initialization invocation failed for " + smppName + ": " + ex.getMessage());
                    }

                    // As a last resort try to set any boolean "initialized"-like fields via reflection
                    try {
                        java.lang.reflect.Field[] fields = smppSmsServiceBinder.getClass().getDeclaredFields();
                        for (java.lang.reflect.Field f : fields) {
                            String fname = f.getName().toLowerCase();
                            if (f.getType() == boolean.class && (fname.contains("init") || fname.contains("initialized") || fname.contains("inited") || fname.contains("service"))) {
                                f.setAccessible(true);
                                f.setBoolean(smppSmsServiceBinder, true);
                                logger.info("Set boolean field '" + f.getName() + "' = true on SmppSmsService implementation for " + smppName);
                            }
                        }
                    } catch (Throwable t) {
                        logger.debug("Could not set boolean initialized-like fields via reflection for " + smppName + ": " + t.getMessage());
                    }

                    // Attempt to reconnect (some implementations require connect after init)
                    try {
                        if (!smppSmsServiceBinder.isConnected()) {
                            smppSmsServiceBinder.connect();
                            logger.info("Reconnected SmppSmsService for " + smppName + " after fallback init attempt");
                        }
                    } catch (Throwable connEx) {
                        logger.warn("Reconnection attempt after fallback initialization failed for " + smppName + ": " + connEx.getMessage());
                    }

                    // small backoff before retry
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }

                    attempts++;
                }
            }

            if (!listenerAdded) {
                // failed after retries - throw an SMSException so higher-level code can cleanup
                throw new SMSException("Failed to add message listener to SmppSmsService after multiple attempts for " + smppName);
            }
            smppSmsServiceBinder.startReceiving();
            System.out.println(jSMSPropertiesSmpp.getProperty("smpp.login") );
            logger.info("SMPP listener connected.  (name=" + smppName + ",config=" + smppConfig + ")");
            logger.info("- Service Name   : " + smppSmsServiceBinder.getServiceName());
            logger.info("- Window Size    : " + smppSmsServiceBinder.getWindowSize());
            logger.info("- Pending Msgs   : " + smppSmsServiceBinder.getNumberOfPendingMessages());
            logger.info("- username   : " + jSMSPropertiesSmpp.getProperty("smpp.login"));
            logger.info("- password   : " + jSMSPropertiesSmpp.getProperty("smpp.password"));
            binderHashtable.put(smppName, smppSmsServiceBinder);
            maskingHashtable.put(smppName, StringUtil.trimToEmpty(jSMSPropertiesSmpp.getProperty("sms.masking")));
        } else {
            throw new SMSException("SMPP connection is currently binded and it can be setup once only. (name=" + smppName + ",config=" + smppConfig + ")");
        }
    }

    public void setupUcp(String ucpName, String ucpConfig) throws SMSException, MessageException, IOException, com.objectxp.msg.MessageException {
        if (!binderHashtable.containsKey(ucpName)) {
            Properties jSMSPropertiesSmpp = new Properties();
            try (InputStream is = openConfigStream(ucpConfig)) {
                jSMSPropertiesSmpp.load(is);
            }
            UcpSmsService ucpSmsServiceBinder = new UcpSmsService();
            // ucpSmsServiceBinder.init(jSMSPropertiesSmpp); - init() method not available in current objectxp library
            logger.info("UCP binder initiated. (name=" + ucpName + ",config=" + ucpConfig + ")");
            MOListenerSmpp moListenerSmpp = new MOListenerSmpp(ucpName);
            ucpSmsServiceBinder.addMessageEventListener(moListenerSmpp);
            ucpSmsServiceBinder.connect();
            ucpSmsServiceBinder.startReceiving();
            logger.info("UCP listener connected.  (name=" + ucpName + ",config=" + ucpConfig + ")");
            logger.info("- Service Name   : " + ucpSmsServiceBinder.getServiceName());
            logger.info("- Window Size    : " + ucpSmsServiceBinder.getWindowSize());
            logger.info("- Pending Msgs   : " + ucpSmsServiceBinder.getNumberOfPendingMessages());
            binderHashtable.put(ucpName, ucpSmsServiceBinder);
        } else {
            throw new SMSException("SMPP connection is currently binded and it can be setup once only. (name=" + ucpName + ",config=" + ucpConfig + ")");
        }
    }

    public void setupHttp(String Http, String httpConfig, String apiKey) throws SMSException, MessageException, IOException {
        if (binderHashtable.containsKey(Http)) {
            throw new SMSException("SMPP connection is currently binded and it can be setup once only. (name=" + Http + ",config=" + httpConfig + ")");
        } else {
            try {
                Properties p = new Properties();
                try (InputStream is = openConfigStream(httpConfig)) {
                    p.load(is);
                }
                String URL = p.getProperty(Http);
                String ch = p.getProperty(Http + ".msgType.ch");
                String en = p.getProperty(Http + ".msgType.en");
                String binary = p.getProperty(Http + ".msgType.bn");
                String msgHeader = p.getProperty(Http + ".msgHeader");
                String req_session = p.getProperty(Http + ".sessionRequire");
                String incoming_dn = p.getProperty(Http + ".incomingDN");
                String setProperty = p.getProperty(Http + ".setProperty");
                String session = "";
                String dynamicTxID = p.getProperty(Http + ".dynamicTxID");
                if (req_session.toLowerCase().equals("y")) {
                    // use repository helper to fetch digi_session_id by route
                    com.isentric.bulkgateway.repository.DigiSessionRepository digiRepo = new com.isentric.bulkgateway.repository.DigiSessionRepository();
                    session = digiRepo.findDigiSessionByRoute(Http);
                }

                HttpObj obj = new HttpObj(Http, httpConfig, URL, ch, en, binary, msgHeader, session, incoming_dn, setProperty, dynamicTxID);
                logger.info("Http setup sucessful.  (name=" + Http + ",config=" + httpConfig + ")");
                logger.info("- URL    : " + obj.getURL());
                binderHashtable.put(Http, obj);
                binderHashtable.put(Http + "#apiKey", apiKey);
            } catch (Exception e) {
                logger.fatal(e.getMessage());
            }

        }
    }


    public void setupWsdl(String wsdl, String wsdlConfig) throws SMSException, MessageException, IOException {
        if (binderHashtable.containsKey(wsdl)) {
            throw new SMSException("SMPP connection is currently binded and it can be setup once only. (name=" + wsdl + ",config=" + wsdlConfig + ")");
        } else {
            try {
                Properties p = new Properties();
                try (InputStream is = openConfigStream(wsdlConfig)) {
                    p.load(is);
                }
                String URL = p.getProperty(wsdl);
                String ch = p.getProperty(wsdl + ".msgType.ch");
                String en = p.getProperty(wsdl + ".msgType.en");
                String binary = p.getProperty(wsdl + ".msgType.bn");
                String msgHeader = p.getProperty(wsdl + ".msgHeader");
                String req_session = p.getProperty(wsdl + ".sessionRequire");
                String incoming_dn = p.getProperty(wsdl + ".incomingDN");
                String setProperty = p.getProperty(wsdl + ".setProperty");
                String session = "";
                String dynamicTxID = p.getProperty(wsdl + ".dynamicTxID");
                String loginName = p.getProperty(wsdl + ".loginName");
                String password = p.getProperty(wsdl + ".password");
                String cpId = p.getProperty(wsdl + ".cpId");
                String serviceId = p.getProperty(wsdl + ".serviceId");
                String responseURL = p.getProperty(wsdl + ".responseURL");
                int notificationId = Integer.valueOf(p.getProperty(wsdl + ".notificationId"));
                if (req_session.toLowerCase().equals("y")) {
                    DigiSessionRepository digiRepo = new DigiSessionRepository();
                    session = digiRepo.findDigiSessionByRoute(wsdl);
                }

                WsdlObj obj = new WsdlObj(wsdl, wsdlConfig, URL, ch, en, binary, msgHeader, session, incoming_dn, setProperty, dynamicTxID, loginName, password, cpId, serviceId, responseURL, notificationId);
                logger.info("Wsdl setup sucessful.  (name=" + wsdl + ",config=" + wsdlConfig + ")");
                logger.info("- URL    : " + obj.getURL());
                binderHashtable.put(wsdl, obj);
            } catch (Exception e) {
                logger.fatal(e.getMessage());
            }

        }
    }

    public void setupChargeService(String chargeName, String chargeConfig) throws SMSException, MessageException, IOException {
        if (binderHashtable.containsKey(chargeName)) {
            throw new SMSException("SMPP connection is currently binded and it can be setup once only. (name=" + chargeName + ",config=" + chargeConfig + ")");
        } else {
            try {
                Properties p = new Properties();
                try (InputStream is = openConfigStream(chargeConfig)) {
                    p.load(is);
                }
                String URL = p.getProperty(chargeName);
                String ch = p.getProperty(chargeName + ".msgType.ch");
                String en = p.getProperty(chargeName + ".msgType.en");
                String binary = p.getProperty(chargeName + ".msgType.bn");
                String msgHeader = p.getProperty(chargeName + ".msgHeader");
                String req_session = p.getProperty(chargeName + ".sessionRequire");
                String incoming_dn = p.getProperty(chargeName + ".incomingDN");
                String setProperty = p.getProperty(chargeName + ".setProperty");
                String session = "";
                String dynamicTxID = p.getProperty(chargeName + ".dynamicTxID");
                String loginName = p.getProperty(chargeName + ".loginName");
                String password = p.getProperty(chargeName + ".password");
                String cpId = p.getProperty(chargeName + ".cpId");
                String serviceId = p.getProperty(chargeName + ".serviceId");
                String responseURL = p.getProperty(chargeName + ".responseURL");
                int notificationId = Integer.valueOf(p.getProperty(chargeName + ".notificationId"));
                String chargeParty = p.getProperty(chargeName + ".chargeParty");
                String keyword = p.getProperty(chargeName + ".keyword");
                String deliveryChannel = p.getProperty(chargeName + ".deliveryChannel");
                if (req_session.toLowerCase().equals("y")) {
                    com.isentric.bulkgateway.repository.DigiSessionRepository digiRepo = new com.isentric.bulkgateway.repository.DigiSessionRepository();
                    session = digiRepo.findDigiSessionByRoute(chargeName);
                }

                ChargeObj obj = new ChargeObj(chargeName, chargeConfig, URL, ch, en, binary, msgHeader, session, incoming_dn, setProperty, dynamicTxID, loginName, password, cpId, serviceId, responseURL, notificationId, chargeParty, keyword, deliveryChannel);
                logger.info("Charge Service setup sucessful.  (name=" + chargeName + ",config=" + chargeConfig + ")");
                logger.info("- URL    : " + obj.getURL());
                binderHashtable.put(chargeName, obj);
            } catch (Exception e) {
                logger.fatal(e.getMessage());
            }

        }
    }

    public synchronized void destroySmpp(String smppName) throws MessageException, com.objectxp.msg.MessageException {
        SmsService smppSmsServiceBinder = (SmsService) binderHashtable.get(smppName);
        if (smppSmsServiceBinder != null && !smppSmsServiceBinder.isAlive() && !smppSmsServiceBinder.isConnected()) {
            logger.info("Binder is not null [" + smppSmsServiceBinder.toString() + "], disconnecting and destroying ...");
            if (smppSmsServiceBinder.isConnected()) {
                smppSmsServiceBinder.disconnect();
            }

            if (smppSmsServiceBinder.isInitialized()) {
                smppSmsServiceBinder.destroy();
            }

            SmsService var3 = null;
            binderHashtable.remove(smppName);
        }

        logger.info("Binder table size=" + binderHashtable.size());
        logger.info("Binder table keys=" + binderHashtable.keys().toString());
    }

    public void sendSmpp(String smppName, String credit, SMSMessageSmpp smsMessage) throws MessageException, SQLException, com.objectxp.msg.MessageException {
        String sender = smsMessage.getSender();
        String recipient = smsMessage.getRecipient();

        try {
            if (StringUtil.isNotBlank((String) maskingHashtable.get(smppName))) {
                sender = (String) maskingHashtable.get(smppName);
                logger.info("- Masking id\t  : " + sender);
            } else {
                logger.info("- Masking id\t  : " + sender);
            }
        } catch (Exception ex) {
            ex.getMessage();
            sender = smsMessage.getSender();
            logger.info("- Masking id\t  : " + sender);
        }

        if (smppName.trim().toUpperCase().startsWith("SMPP_TUNETALK")) {
            if (recipient.trim().startsWith("60")) {
                recipient = "+" + recipient;
            } else if (!recipient.trim().startsWith("+") && recipient.trim().startsWith("01")) {
                recipient = "+6" + recipient;
            }
        }

        if (smppName.trim().toUpperCase().startsWith("SMPP_BANKISLAM_115") && !recipient.trim().startsWith("+")) {
            recipient = "+" + recipient;
        }

        if (smppName.toLowerCase().startsWith("ucp")) {
            sender = "66399";
            recipient = smsMessage.getRecipient().substring(1);
        }

        if (smsMessage.getMessageType() == 0) {
            EMSMessage sms = new EMSMessage();
            sms.requestStatusReport(true);
            sms.setSender(sender);
            sms.setRecipient(recipient);
            sms.setID(smsMessage.getGuid());
            sms.setCodingGroup((short) 0);
            sms.setProperty("SMPP_GUID", smsMessage.getGuid());
            sms.setProperty("SMPP_NAME", smppName);
            sms.setParent(sms);
            sms.setMessageClass((short) -1);
            smsMessage.setMessage(this.checkPriceTag(smsMessage.getMessage()));
            sms.setMessage(smsMessage.getMessage());
            int MSG_MAX_SIZE = 160;
            int messageLength = smsMessage.getMessage().length();
            int nMessages = (short) ((int) Math.ceil((double) messageLength / Double.parseDouble(String.valueOf(MSG_MAX_SIZE))));
            if (smppName.trim().toUpperCase().startsWith("SMPP_TUNETALK") && messageLength > MSG_MAX_SIZE) {
                String fullMessage = smsMessage.getMessage();

                for (int i = 0; i < nMessages; ++i) {
                    String messagePart = getContentPart(fullMessage, i, MSG_MAX_SIZE);
                    sms = new EMSMessage();
                    sms.requestStatusReport(true);
                    sms.setSender(sender);
                    sms.setRecipient(recipient);
                    sms.setID(smsMessage.getGuid());
                    sms.setCodingGroup((short) 0);
                    sms.setProperty("SMPP_GUID", smsMessage.getGuid());
                    sms.setProperty("SMPP_NAME", smppName);
                    sms.setParent(sms);
                    sms.setMessageClass((short) -1);
                    sms.setMessage(this.checkPriceTag(messagePart));
                    System.out.println("Message Part [" + i + "] : " + sms.getMessage());
                    ((SmsService) binderHashtable.get(smppName)).sendMessage(sms);

                    for (int j = 0; j < sms.getParts().length; ++j) {
                        this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), smppName, sms.getID(), smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessage()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), i, credit);
                    }
                }
            } else {
                ((SmsService) binderHashtable.get(smppName)).sendMessage(sms);

                for (int i = 0; i < sms.getParts().length; ++i) {
                    this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), smppName,  sms.getID(), smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessage()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), i, credit);
                }
            }
        } else if (smsMessage.getMessageType() == 6) {
            EMSMessage sms = new EMSMessage();
            sms.requestStatusReport(true);
            sms.setSender(smsMessage.getSender());
            sms.setRecipient(recipient);
            sms.setID(smsMessage.getGuid());
            sms.setMessageId(smsMessage.getGuid());
            sms.setProperty("SMPP_GUID", smsMessage.getGuid());
            sms.setProperty("SMPP_NAME", smppName);
            sms.setParent(sms);
            smsMessage.setMessage(this.checkChinesePriceTag(smsMessage.getMessage()));
            byte[] data = GsmHelper.decodeIA5(smsMessage.getMessage());
            sms.setUserData(data);
            sms.setCodingGroup((short) 0);
            sms.setAlphabet((short) 8);
            sms.setMessageClass((short) -1);
            ((SmsService) binderHashtable.get(smppName)).sendMessage(sms);

            for (int i = 0; i < sms.getParts().length; ++i) {
                this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), smppName,  sms.getID(), smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), i, credit);
            }
        } else if (smsMessage.getMessageType() == 5) {
            if (smsMessage.getMessage().startsWith("3000000946756E46757A696F6E02")) {
                smsMessage.setMessage(smsMessage.getMessage().substring(32));
            }

            PictureMessage picture = new PictureMessage();
            picture.setPictureMessage("", GsmHelper.decodeIA5(smsMessage.getMessage()));
            picture.setRecipient(recipient);
            picture.requestStatusReport(true);
            picture.setSender(smsMessage.getSender());
            picture.setID(smsMessage.getGuid());
            picture.setMessageId(smsMessage.getGuid());
            picture.setProperty("SMPP_GUID", smsMessage.getGuid());
            picture.setProperty("SMPP_NAME", smppName);
            picture.setParent(picture);
            ((SmsService) binderHashtable.get(smppName)).sendMessage(picture);

            for (int i = 0; i < picture.getParts().length; ++i) {
                this.messageServiceDao.insertSmppSent2((String) picture.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), smppName, picture.getParts()[i].getID(), smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), i, credit);
            }
        } else if (smsMessage.getMessageType() == 4) {
            OperatorLogo logo = new OperatorLogo();
            logo.setOperatorLogo(GsmHelper.decodeIA5(smsMessage.getMessage()));
            logo.setRecipient(recipient);
            logo.requestStatusReport(true);
            logo.setSender(smsMessage.getSender());
            logo.setID(smsMessage.getGuid());
            logo.setMessageId(smsMessage.getGuid());
            logo.setProperty("SMPP_GUID", smsMessage.getGuid());
            logo.setProperty("SMPP_NAME", smppName);
            logo.setParent(logo);
            ((SmsService) binderHashtable.get(smppName)).sendMessage(logo);

            for (int i = 0; i < logo.getParts().length; ++i) {
                this.messageServiceDao.insertSmppSent2((String) logo.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), smppName, logo.getParts()[i].getID(), smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), i, credit);
            }
        } else if (smsMessage.getMessageType() == 3) {
            Ringtone rt = new Ringtone(GsmHelper.decodeIA5(smsMessage.getMessage()));
            rt.setRecipient(recipient);
            rt.setAlphabet((short) 4);
            rt.requestStatusReport(true);
            rt.setSender(smsMessage.getSender());
            rt.setID(smsMessage.getGuid());
            rt.setMessageId(smsMessage.getGuid());
            rt.setProperty("SMPP_GUID", smsMessage.getGuid());
            rt.setProperty("SMPP_NAME", smppName);
            rt.setParent(rt);
            ((SmsService) binderHashtable.get(smppName)).sendMessage(rt);

            for (int i = 0; i < rt.getParts().length; ++i) {
                this.messageServiceDao.insertSmppSent2((String) rt.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), smppName, rt.getParts()[i].getID(), smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), i, credit);
            }
        } else if (smsMessage.getMessageType() == 1) {
            ServiceIndication indication = new ServiceIndication(smsMessage.getMessage(), smsMessage.getCallbackURL());
            indication.requestStatusReport(true);
            indication.setSender(smsMessage.getSender());
            indication.setRecipient(recipient);
            indication.setID(smsMessage.getGuid());
            indication.setMessageId(smsMessage.getGuid());
            indication.setProperty("SMPP_GUID", smsMessage.getGuid());
            indication.setProperty("SMPP_NAME", smppName);
            indication.setCreated(new Date());
            indication.setParent(indication);
            ((SmsService) binderHashtable.get(smppName)).sendMessage(indication);

            for (int i = 0; i < indication.getParts().length; ++i) {
                logger.info("indication.getParts()[" + i + "].toString()" + indication.getParts()[i].toString());
                this.messageServiceDao.insertSmppSent2((String) indication.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), smppName, indication.getParts()[i].getID(), smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), i, credit);
            }
        }

    }

    public void sendRedPremioSmpp(String smppName, String credit, SMSMessageSmpp smsMessage) throws MessageException, SQLException, com.objectxp.msg.MessageException {
        String sender = smsMessage.getSender();
        String recipient = smsMessage.getRecipient();
        logger.info("---------------------sendRedPremioSmpp----------------------");

        try {
            if (StringUtil.isNotBlank((String) maskingHashtable.get(smppName))) {
                sender = (String) maskingHashtable.get(smppName);
                logger.info("- Masking id\t  : " + sender);
            } else {
                logger.info("- Masking id\t  : " + sender);
            }
        } catch (Exception ex) {
            ex.getMessage();
            sender = smsMessage.getSender();
            logger.info("- Masking id\t  : " + sender);
        }

        logger.info("---------------------------" + smsMessage.getMessageType() + "---------------------------");
        if (smsMessage.getMessageType() == 0) {
            logger.info("---------------------------" + smsMessage.getMessageType() + "---------------------------");
            EMSMessage sms = new EMSMessage();
            sms.requestStatusReport(true);
            sms.setSender(sender);
            sms.setRecipient(recipient);
            sms.setID(smsMessage.getGuid());
            sms.setCodingGroup((short) 0);
            sms.setProperty("SMPP_GUID", smsMessage.getGuid());
            sms.setProperty("SMPP_NAME", smppName);
            sms.setParent(sms);
            sms.setMessageClass((short) -1);
            sms.setMessage(smsMessage.getMessage());
            ((SmsService) binderHashtable.get(smppName)).sendMessage(sms);

            for (int i = 0; i < sms.getParts().length; ++i) {
                this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), smppName,  sms.getID(), smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), i, credit);
            }
        } else if (smsMessage.getMessageType() == 6) {
            EMSMessage sms = new EMSMessage();
            sms.requestStatusReport(true);
            sms.setSender(smsMessage.getSender());
            sms.setRecipient(recipient);
            sms.setID(smsMessage.getGuid());
            sms.setMessageId(smsMessage.getGuid());
            sms.setProperty("SMPP_GUID", smsMessage.getGuid());
            sms.setProperty("SMPP_NAME", smppName);
            sms.setParent(sms);
            byte[] data = GsmHelper.decodeIA5(smsMessage.getMessage());
            sms.setUserData(data);
            sms.setCodingGroup((short) 0);
            sms.setAlphabet((short) 8);
            sms.setMessageClass((short) -1);
            ((SmsService) binderHashtable.get(smppName)).sendMessage(sms);

            for (int i = 0; i < sms.getParts().length; ++i) {
                this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), smppName,  sms.getID(), smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), i, credit);
            }
        } else if (smsMessage.getMessageType() == 5) {
            if (smsMessage.getMessage().startsWith("3000000946756E46757A696F6E02")) {
                smsMessage.setMessage(smsMessage.getMessage().substring(32));
            }

            PictureMessage picture = new PictureMessage();
            picture.setPictureMessage("", GsmHelper.decodeIA5(smsMessage.getMessage()));
            picture.setRecipient(recipient);
            picture.requestStatusReport(true);
            picture.setSender(smsMessage.getSender());
            picture.setID(smsMessage.getGuid());
            picture.setMessageId(smsMessage.getGuid());
            picture.setProperty("SMPP_GUID", smsMessage.getGuid());
            picture.setProperty("SMPP_NAME", smppName);
            picture.setParent(picture);
            ((SmsService) binderHashtable.get(smppName)).sendMessage(picture);

            for (int i = 0; i < picture.getParts().length; ++i) {
                this.messageServiceDao.insertSmppSent2((String) picture.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), smppName, picture.getParts()[i].getID(), smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), i, credit);
            }
        } else if (smsMessage.getMessageType() == 4) {
            OperatorLogo logo = new OperatorLogo();
            logo.setOperatorLogo(GsmHelper.decodeIA5(smsMessage.getMessage()));
            logo.setRecipient(recipient);
            logo.requestStatusReport(true);
            logo.setSender(smsMessage.getSender());
            logo.setID(smsMessage.getGuid());
            logo.setMessageId(smsMessage.getGuid());
            logo.setProperty("SMPP_GUID", smsMessage.getGuid());
            logo.setProperty("SMPP_NAME", smppName);
            logo.setParent(logo);
            ((SmsService) binderHashtable.get(smppName)).sendMessage(logo);

            for (int i = 0; i < logo.getParts().length; ++i) {
                this.messageServiceDao.insertSmppSent2((String) logo.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), smppName, logo.getParts()[i].getID(), smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), i, credit);
            }
        } else if (smsMessage.getMessageType() == 3) {
            Ringtone rt = new Ringtone(GsmHelper.decodeIA5(smsMessage.getMessage()));
            rt.setRecipient(recipient);
            rt.setAlphabet((short) 4);
            rt.requestStatusReport(true);
            rt.setSender(smsMessage.getSender());
            rt.setID(smsMessage.getGuid());
            rt.setMessageId(smsMessage.getGuid());
            rt.setProperty("SMPP_GUID", smsMessage.getGuid());
            rt.setProperty("SMPP_NAME", smppName);
            rt.setParent(rt);
            ((SmsService) binderHashtable.get(smppName)).sendMessage(rt);

            for (int i = 0; i < rt.getParts().length; ++i) {
                this.messageServiceDao.insertSmppSent2((String) rt.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), smppName, rt.getParts()[i].getID(), smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), i, credit);
            }
        } else if (smsMessage.getMessageType() == 1) {
            ServiceIndication indication = new ServiceIndication(smsMessage.getMessage(), smsMessage.getCallbackURL());
            indication.requestStatusReport(true);
            indication.setSender(smsMessage.getSender());
            indication.setRecipient(recipient);
            indication.setID(smsMessage.getGuid());
            indication.setMessageId(smsMessage.getGuid());
            indication.setProperty("SMPP_GUID", smsMessage.getGuid());
            indication.setProperty("SMPP_NAME", smppName);
            indication.setCreated(new Date());
            indication.setParent(indication);
            ((SmsService) binderHashtable.get(smppName)).sendMessage(indication);

            for (int i = 0; i < indication.getParts().length; ++i) {
                logger.info("indication.getParts()[" + i + "].toString()" + indication.getParts()[i].toString());
                this.messageServiceDao.insertSmppSent2((String) indication.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), smppName, indication.getParts()[i].getID(), smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), i, credit);
            }
        }

    }

    public void sendHttp(String httpName, String credit, SMSMessageSmpp smsMessage) throws MessageException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException, SQLException {
       System.out.println("---------------------sendHttp----------------------");
        System.out.println(httpName);
        String sender = smsMessage.getSender();
        String recipient = smsMessage.getRecipient();
        short nMessages = 1;
        new DESProcessor();
        String cFlag = "" + smsMessage.getcFlag();
        double cFlagMaxLength = (double) 134.0F;
        double cChineseFlagMaxLength = (double) 268.0F;
        int cFlagMaxLengthInt = Integer.parseInt(String.valueOf(cFlagMaxLength).substring(0, String.valueOf(cFlagMaxLength).length() - 2));
        int cChineseFlagMaxLengthInt = Integer.parseInt(String.valueOf(cChineseFlagMaxLength).substring(0, String.valueOf(cChineseFlagMaxLength).length() - 2));
        boolean notCelcomConcate = true;
        EMSMessage sms = new EMSMessage();
        sms.requestStatusReport(true);
        sms.setSender(sender);
        sms.setRecipient(recipient);
        sms.setID(smsMessage.getGuid());
        sms.setProperty("SMPP_GUID", smsMessage.getGuid());
        sms.setProperty("SMPP_NAME", httpName);
        sms.setParent(sms);
        HttpObj httpSms = (HttpObj) binderHashtable.get(httpName);
        System.out.println("httpSms - "+httpSms);
        String response = "";
        String url = "";
        String dnid = "";
        int code = 0;
        new HttpClient();
        new GetMethod();
        new PostMethod();
        System.setProperty("javax.net.ssl.trustStore", "/home/arun/Documents/rec/MyMaxisKeyStore.jks");
        System.setProperty("https.protocols", "TLSv1.2");
        System.out.println(cFlag);
        if (cFlag.equalsIgnoreCase("1")) {
            logger.info("Concatenated Message");
        } else {
            logger.info("Normal Message");
        }

        if (smsMessage.getTelco().equalsIgnoreCase("celcom") && cFlag.equalsIgnoreCase("1")) {
            notCelcomConcate = false;
        }

        if (smsMessage.getMessageType() == 0) {
            smsMessage.setMessage(this.checkPriceTag(smsMessage.getMessage()));
            sms.setMessage(smsMessage.getMessage());
            int messageLength = smsMessage.getMessage().length();
            if (messageLength > 160 && notCelcomConcate) {
                String randomHex = generateRandomHexCode();
                if (smsMessage.getTelco().equalsIgnoreCase("celcom")) {
                    nMessages = (short) ((int) Math.ceil(((double) messageLength - (double) 9.0F) / (double) 151.0F));
                } else if (cFlag.equalsIgnoreCase("1")) {
                    nMessages = (short) ((int) Math.ceil((double) messageLength / cFlagMaxLength));
                } else {
                    nMessages = (short) ((int) Math.ceil((double) messageLength / (double) 160.0F));
                }

                logger.info("messageLength >> " + messageLength);

                for (int i = 0; i < nMessages; ++i) {
                    System.out.print("message number count: " + (i + 1));
                    response = "";
                    dnid = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
                    url = httpSms.getURL();
                    url = url.replace("<msisdn>", sms.getRecipient());
                    url = url.replace("<msgType>", httpSms.getEnText());
                    url = url.replace("<masking>", sms.getSender());
                    if (smsMessage.getTelco().equalsIgnoreCase("celcom")) {
                        if (i == 0) {
                            smsMessage.setMessage(smsMessage.getMessage().substring(9));
                        }

                        String checkPriceTagMsg = getContentPart(smsMessage.getMessage(), i, 151);
                        checkPriceTagMsg = this.checkPriceTag(checkPriceTagMsg);
                        url = url.replace("<msg>", URLEncoder.encode(checkPriceTagMsg));
                    } else if (cFlag.equalsIgnoreCase("1")) {
                        url = url.replace("<msg>", URLEncoder.encode(getContentPart(smsMessage.getMessage(), i, cFlagMaxLengthInt)));
                    } else {
                        url = url.replace("<msg>", URLEncoder.encode(getContentPart(smsMessage.getMessage(), i, 160).trim()));
                    }

                    if (httpSms.getMsgHeader().toLowerCase().equals("y") && cFlag.equalsIgnoreCase("1")) {
                        url = url + "&UserHeader=" + setTextHeader(nMessages, i + 1, randomHex);
                    }

                    if (httpSms.getDynamicTxID().equalsIgnoreCase("y")) {
                        dnid = sms.getRecipient().substring(sms.getRecipient().length() - 4, sms.getRecipient().length()) + dnid.substring(0, 3);
                        url = url.replace("<TXID>", dnid);
                    }

                    try {
                        MaxisDNStatusManager maxisCache = MaxisDNStatusManager.getInstance();
                        String oriCust = smsMessage.getKeyword().replace("extmt_", "").trim();
                        if (httpName.equalsIgnoreCase("HTTP_MAXIS_ESMS_PREFERRED") && maxisCache.getMessagesObj(oriCust) != null && maxisCache.getMessagesObj(oriCust).getMaxisdn_flag().equalsIgnoreCase("1")) {
                            url = url + "&DR=true";
                        }
                    } catch (CacheException e) {
                        e.printStackTrace();
                    }
                    System.out.println("---------------Here No3----------------");
                    System.out.println("1. URL : " + url);
                    logger.debug("1. URL : " + url);
                    GetMethod var74 = new GetMethod(url);
                    smsMessage.setMessage(this.checkPriceTag(sms.getMessage()));
                    this.performHTTPRequest(var74, response, sms, smsMessage, httpName, dnid, sender, credit, i);
                }
            } else {
                url = httpSms.getURL();
                dnid = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
                url = url.replace("<msisdn>", sms.getRecipient());
                url = url.replace("<msgType>", httpSms.getEnText());
                url = url.replace("<masking>", sms.getSender());
                String checkPriceTagMsg = smsMessage.getMessage();
                checkPriceTagMsg = this.checkPriceTag(checkPriceTagMsg);
                url = url.replace("<msg>", URLEncoder.encode(checkPriceTagMsg));
                if (httpSms.getDynamicTxID().equalsIgnoreCase("y")) {
                    dnid = sms.getRecipient().substring(sms.getRecipient().length() - 4, sms.getRecipient().length()) + dnid.substring(0, 3);
                    url = url.replace("<TXID>", dnid);
                }

                try {
                    MaxisDNStatusManager maxisCache = MaxisDNStatusManager.getInstance();
                    String oriCust = smsMessage.getKeyword().replace("extmt_", "").trim();
                    if (httpName.equalsIgnoreCase("HTTP_MAXIS_ESMS_PREFERRED") && maxisCache.getMessagesObj(oriCust) != null && maxisCache.getMessagesObj(oriCust).getMaxisdn_flag().equalsIgnoreCase("1")) {
                        url = url + "&DR=true";
                    }
                } catch (CacheException e) {
                    e.printStackTrace();
                }

                logger.debug("2. URL : " + url);
                GetMethod httpGet = new GetMethod(url);

                try {
                    if (messageLength > 160 && smsMessage.getTelco().equalsIgnoreCase("celcom") && cFlag.equalsIgnoreCase("1")) {
                        nMessages = (short) ((int) Math.ceil((double) messageLength / cFlagMaxLength));

                        for (int k = 1; k < nMessages; ++k) {
                            this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, dnid, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), k, credit);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                System.out.println(httpGet);
                System.out.println(response);
                System.out.println(sms);
                System.out.println(smsMessage);
                System.out.println(httpName);
                System.out.println(dnid);
                System.out.println(sender);
                System.out.println(credit);

                this.performHTTPRequest(httpGet, response, sms, smsMessage, httpName, dnid, sender, credit, 0);
            }
        } else if (smsMessage.getMessageType() == 6) {
            smsMessage.setMessage(this.checkChinesePriceTag(smsMessage.getMessage()));
            byte[] data = GsmHelper.decodeIA5(smsMessage.getMessage());
            sms.setUserData(data);
            sms.setCodingGroup((short) 240);
            sms.setAlphabet((short) 8);
            int messageLength = smsMessage.getMessage().length();
            if (messageLength > 280 && notCelcomConcate) {
                String randomHex = generateRandomHexCode();
                if (smsMessage.getTelco().equalsIgnoreCase("celcom")) {
                    nMessages = (short) ((int) Math.ceil(((double) messageLength - (double) 36.0F) / (double) 244.0F));
                } else if (cFlag.equalsIgnoreCase("1")) {
                    nMessages = (short) ((int) Math.ceil((double) messageLength / cChineseFlagMaxLength));
                } else {
                    nMessages = (short) ((int) Math.ceil((double) messageLength / (double) 280.0F));
                }

                logger.info("messageLength >> " + messageLength);
                logger.info("nMessages >> " + nMessages);

                for (int i = 0; i < nMessages; ++i) {
                    System.out.print("message number count: " + (i + 1));
                    response = "";
                    dnid = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
                    url = httpSms.getURL();
                    url = url.replace("<msisdn>", sms.getRecipient());
                    url = url.replace("<msgType>", httpSms.getChText());
                    url = url.replace("<masking>", sms.getSender());
                    if (smsMessage.getTelco().equalsIgnoreCase("celcom")) {
                        logger.info("CELCOM CELCOM CELCOM CELCOM");
                        if (i == 0) {
                            smsMessage.setMessage(smsMessage.getMessage().substring(36));
                        }

                        String checkPriceTagMsg = getContentPart(smsMessage.getMessage(), i, 244);
                        checkPriceTagMsg = this.checkChinesePriceTag(checkPriceTagMsg);
                        url = url.replace("<msg>", URLEncoder.encode(checkPriceTagMsg));
                        logger.info("===============Here No4");
                    } else if (cFlag.equalsIgnoreCase("1")) {
                        url = url.replace("<msg>", URLEncoder.encode(getContentPart(smsMessage.getMessage(), i, cChineseFlagMaxLengthInt).trim()));
                    } else {
                        url = url.replace("<msg>", URLEncoder.encode(getContentPart(smsMessage.getMessage(), i, 280).trim()));
                    }

                    if (httpSms.getMsgHeader().toLowerCase().equals("y") && cFlag.equalsIgnoreCase("1")) {
                        url = url + "&UserHeader=" + setTextHeader(nMessages, i + 1, randomHex);
                    }

                    if (httpSms.getDynamicTxID().equalsIgnoreCase("y")) {
                        dnid = sms.getRecipient().substring(sms.getRecipient().length() - 4, sms.getRecipient().length()) + dnid.substring(0, 3);
                        url = url.replace("<TXID>", dnid);
                    }

                    try {
                        MaxisDNStatusManager maxisCache = MaxisDNStatusManager.getInstance();
                        String oriCust = smsMessage.getKeyword().replace("extmt_", "").trim();
                        if (httpName.equalsIgnoreCase("HTTP_MAXIS_ESMS_PREFERRED") && maxisCache.getMessagesObj(oriCust) != null && maxisCache.getMessagesObj(oriCust).getMaxisdn_flag().equalsIgnoreCase("1")) {
                            url = url + "&DR=true";
                        }
                    } catch (CacheException e) {
                        e.printStackTrace();
                    }

                    logger.debug("3. URL : " + url);
                    GetMethod var76 = new GetMethod(url);
                    this.performHTTPRequest(var76, response, sms, smsMessage, httpName, dnid, sender, credit, i);
                }
            } else {
                url = httpSms.getURL();
                dnid = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
                url = url.replace("<msisdn>", sms.getRecipient());
                url = url.replace("<msgType>", httpSms.getChText());
                url = url.replace("<masking>", sms.getSender());
                String checkPriceTagMsg = smsMessage.getMessage();
                checkPriceTagMsg = this.checkChinesePriceTag(checkPriceTagMsg);
                url = url.replace("<msg>", URLEncoder.encode(checkPriceTagMsg));
                if (httpSms.getDynamicTxID().equalsIgnoreCase("y")) {
                    dnid = sms.getRecipient().substring(sms.getRecipient().length() - 4, sms.getRecipient().length()) + dnid.substring(0, 3);
                    url = url.replace("<TXID>", dnid);
                }

                try {
                    MaxisDNStatusManager maxisCache = MaxisDNStatusManager.getInstance();
                    String oriCust = smsMessage.getKeyword().replace("extmt_", "").trim();
                    if (httpName.equalsIgnoreCase("HTTP_MAXIS_ESMS_PREFERRED") && maxisCache.getMessagesObj(oriCust) != null && maxisCache.getMessagesObj(oriCust).getMaxisdn_flag().equalsIgnoreCase("1")) {
                        url = url + "&DR=true";
                    }
                } catch (CacheException e) {
                    e.printStackTrace();
                }

                logger.debug("4. URL : " + url);
                GetMethod var75 = new GetMethod(url);

                try {
                    if (messageLength > 280 && smsMessage.getTelco().equalsIgnoreCase("celcom") && cFlag.equalsIgnoreCase("1")) {
                        nMessages = (short) ((int) Math.ceil((double) messageLength / cFlagMaxLength));

                        for (int k = 1; k < nMessages; ++k) {
                            this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, dnid, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), k, credit);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                this.performHTTPRequest(var75, response, sms, smsMessage, httpName, dnid, sender, credit, 0);
            }
        } else if (smsMessage.getMessageType() == 5) {
            logger.info("Picture Message not sent to " + smsMessage.getRecipient());
        } else if (smsMessage.getMessageType() == 4) {
            logger.info("Logo Message not sent to " + smsMessage.getRecipient());
        } else if (smsMessage.getMessageType() == 3) {
            logger.info("Ringtone Message not sent to " + smsMessage.getRecipient());
        } else if (smsMessage.getMessageType() == 1) {
            url = httpSms.getURL();
            dnid = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
            url = url.replaceAll("<msisdn>", sms.getRecipient());
            url = url.replaceAll("<msgType>", httpSms.getBinary());
            url = url.replaceAll("<masking>", sms.getSender());
            url = url.replace("<msg>", URLEncoder.encode(smsMessage.getMessage()));
            if (httpSms.getDynamicTxID().equalsIgnoreCase("y")) {
                dnid = sms.getRecipient().substring(sms.getRecipient().length() - 4, sms.getRecipient().length()) + dnid.substring(0, 3);
                url = url.replace("<TXID>", dnid);
            }

            GetMethod var77 = new GetMethod(url);
            logger.debug("5. URL : " + url);
            this.performHTTPRequest(var77, response, sms, smsMessage, httpName, dnid, sender, credit, 0);
            logger.info("WAP Message not sent to " + smsMessage.getRecipient());
        }

    }

    public void sendHttpCelcom(String httpName, String credit, SMSMessageSmpp smsMessage) throws MessageException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException, SQLException {
        String sender = smsMessage.getSender();
        String recipient = smsMessage.getRecipient();
        short nMessages = 1;
        String cFlag = "" + smsMessage.getcFlag();
        double cFlagMaxLength = (double) 134.0F;
        double cChineseFlagMaxLength = (double) 268.0F;
        int cFlagMaxLengthInt = Integer.parseInt(String.valueOf(cFlagMaxLength).substring(0, String.valueOf(cFlagMaxLength).length() - 2));
        int cChineseFlagMaxLengthInt = Integer.parseInt(String.valueOf(cChineseFlagMaxLength).substring(0, String.valueOf(cChineseFlagMaxLength).length() - 2));
        boolean notCelcomConcate = true;
        EMSMessage sms = new EMSMessage();
        sms.requestStatusReport(true);
        sms.setSender(sender);
        sms.setRecipient(recipient);
        sms.setID(smsMessage.getGuid());
        sms.setProperty("SMPP_GUID", smsMessage.getGuid());
        sms.setProperty("SMPP_NAME", httpName);
        sms.setParent(sms);
        HttpObj httpSms = (HttpObj) binderHashtable.get(httpName);
        String response = "";
        String url = "";
        String dnid = "";
        int code = 0;
        new HttpClient();
        new GetMethod();
        new PostMethod();
        System.setProperty("javax.net.ssl.trustStore", "C:\\bulk-cert\\MyMaxisKeyStore.jks");
        if (cFlag.equalsIgnoreCase("1")) {
            logger.info("Concatenated Message");
        } else {
            logger.info("Normal Message");
        }

        if (smsMessage.getTelco().equalsIgnoreCase("celcom") && cFlag.equalsIgnoreCase("1")) {
            notCelcomConcate = false;
        }

        if (smsMessage.getMessageType() == 0) {
            smsMessage.setMessage(this.checkPriceTag(smsMessage.getMessage()));
            sms.setMessage(smsMessage.getMessage());
            int messageLength = smsMessage.getMessage().length();
            if (messageLength > 160 && notCelcomConcate) {
                url = httpSms.getURL();
                dnid = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
                url = url.replace("<msisdn>", sms.getRecipient());
                url = url.replace("<msgType>", httpSms.getEnText());
                url = url.replace("<masking>", sms.getSender());
                String checkPriceTagMsg = smsMessage.getMessage();
                checkPriceTagMsg = this.checkPriceTag(checkPriceTagMsg);
                url = url.replace("<msg>", URLEncoder.encode(checkPriceTagMsg));
                if (httpSms.getDynamicTxID().equalsIgnoreCase("y")) {
                    url = url.replace("<TXID>", dnid);
                }

                GetMethod var69 = new GetMethod(url);
                logger.debug("2. URL : " + url);

                try {
                    if (messageLength > 160 && smsMessage.getTelco().equalsIgnoreCase("celcom") && cFlag.equalsIgnoreCase("1")) {
                        nMessages = (short) ((int) Math.ceil((double) messageLength / cFlagMaxLength));

                        for (int k = 1; k < nMessages; ++k) {
                            this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, dnid, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), k, credit);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                this.performHTTPCelcomRequest(var69, response, sms, smsMessage, httpName, dnid, sender, credit, 0);
            } else {
                url = httpSms.getURL();
                dnid = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
                url = url.replace("<msisdn>", sms.getRecipient());
                url = url.replace("<msgType>", httpSms.getEnText());
                url = url.replace("<masking>", sms.getSender());
                String checkPriceTagMsg = smsMessage.getMessage();
                checkPriceTagMsg = this.checkPriceTag(checkPriceTagMsg);
                url = url.replace("<msg>", URLEncoder.encode(checkPriceTagMsg));
                if (httpSms.getDynamicTxID().equalsIgnoreCase("y")) {
                    url = url.replace("<TXID>", dnid);
                }

                GetMethod httpGet = new GetMethod(url);
                logger.debug("2. URL : " + url);

                try {
                    if (messageLength > 160 && smsMessage.getTelco().equalsIgnoreCase("celcom") && cFlag.equalsIgnoreCase("1")) {
                        nMessages = (short) ((int) Math.ceil((double) messageLength / cFlagMaxLength));

                        for (int k = 1; k < nMessages; ++k) {
                            this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, dnid, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), k, credit);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                this.performHTTPCelcomRequest(httpGet, response, sms, smsMessage, httpName, dnid, sender, credit, 0);
            }
        } else if (smsMessage.getMessageType() == 6) {
            new Encoder();
            smsMessage.setMessage(this.checkChinesePriceTag(smsMessage.getMessage()));
            byte[] data = GsmHelper.decodeIA5(smsMessage.getMessage());
            sms.setUserData(data);
            sms.setCodingGroup((short) 240);
            sms.setAlphabet((short) 8);
            int messageLength = smsMessage.getMessage().length();
            if (messageLength > 280 && notCelcomConcate) {
                String randomHex = generateRandomHexCode();
                nMessages = (short) ((int) Math.ceil(((double) messageLength - (double) 36.0F) / (double) 244.0F));
                logger.info("messageLength >> " + messageLength);
                logger.info("nMessages >> " + nMessages);

                for (int i = 0; i < nMessages; ++i) {
                    System.out.print("message number count: " + (i + 1));
                    response = "";
                    dnid = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
                    smsMessage.setMessage(smsMessage.getMessage().substring(36));
                    String checkPriceTagMsg = getContentPart(smsMessage.getMessage(), i, 244);
                    checkPriceTagMsg = this.checkChinesePriceTag(checkPriceTagMsg);
                    url = httpSms.getURL();
                    url = url.replace("<msisdn>", sms.getRecipient());
                    url = url.replace("<msgType>", httpSms.getChText());
                    url = url.replace("<masking>", sms.getSender());
                    url = url.replace("<msg>", URLEncoder.encode(this.ucs2ToUTF8(Encoder.hexToByte(checkPriceTagMsg))));
                    if (httpSms.getMsgHeader().toLowerCase().equals("y") && cFlag.equalsIgnoreCase("1")) {
                        url = url + "&UserHeader=" + setTextHeader(nMessages, i + 1, randomHex);
                    }

                    if (httpSms.getDynamicTxID().equalsIgnoreCase("y")) {
                        url = url.replace("<TXID>", dnid);
                    }

                    logger.debug("3. URL : " + url);
                    GetMethod var71 = new GetMethod(url);
                    this.performHTTPCelcomRequest(var71, response, sms, smsMessage, httpName, dnid, sender, credit, i);
                }
            } else {
                url = httpSms.getURL();
                dnid = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
                url = url.replace("<msisdn>", sms.getRecipient());
                url = url.replace("<msgType>", httpSms.getChText());
                url = url.replace("<masking>", sms.getSender());
                String checkPriceTagMsg = smsMessage.getMessage();
                checkPriceTagMsg = this.checkChinesePriceTag(checkPriceTagMsg);
                url = url.replace("<msg>", URLEncoder.encode(checkPriceTagMsg));
                if (httpSms.getDynamicTxID().equalsIgnoreCase("y")) {
                    url = url.replace("<TXID>", dnid);
                }

                GetMethod var70 = new GetMethod(url);
                logger.debug("4. URL : " + url);

                try {
                    if (messageLength > 280 && smsMessage.getTelco().equalsIgnoreCase("celcom") && cFlag.equalsIgnoreCase("1")) {
                        nMessages = (short) ((int) Math.ceil((double) messageLength / cFlagMaxLength));

                        for (int k = 1; k < nMessages; ++k) {
                            this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, dnid, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), k, credit);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                this.performHTTPCelcomRequest(var70, response, sms, smsMessage, httpName, dnid, sender, credit, 0);
            }
        } else if (smsMessage.getMessageType() == 5) {
            logger.info("Picture Message not sent to " + smsMessage.getRecipient());
        } else if (smsMessage.getMessageType() == 4) {
            logger.info("Logo Message not sent to " + smsMessage.getRecipient());
        } else if (smsMessage.getMessageType() == 3) {
            logger.info("Ringtone Message not sent to " + smsMessage.getRecipient());
        } else if (smsMessage.getMessageType() == 1) {
            url = httpSms.getURL();
            dnid = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
            url = url.replaceAll("<msisdn>", sms.getRecipient());
            url = url.replaceAll("<msgType>", httpSms.getBinary());
            url = url.replaceAll("<masking>", sms.getSender());
            url = url.replace("<msg>", URLEncoder.encode(smsMessage.getMessage()));
            if (httpSms.getDynamicTxID().equalsIgnoreCase("y")) {
                dnid = sms.getRecipient().substring(sms.getRecipient().length() - 4, sms.getRecipient().length()) + dnid.substring(0, 3);
                url = url.replace("<TXID>", dnid);
            }

            GetMethod var72 = new GetMethod(url);
            logger.debug("5. URL : " + url);
            this.performHTTPRequest(var72, response, sms, smsMessage, httpName, dnid, sender, credit, 0);
            logger.info("WAP Message not sent to " + smsMessage.getRecipient());
        }

    }

    public void sendHttpInfobip(String httpName, String credit, SMSMessageSmpp smsMessage) throws MessageException, SQLException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, MalformedURLException, IOException, SQLException {
        String sender = smsMessage.getSender();
        String recipient = smsMessage.getRecipient();
        short nMessages = 1;
        String cFlag = "" + smsMessage.getcFlag();
        double cFlagMaxLength = (double) 153.0F;
        double cFlagChineseMaxLength = (double) 256.0F;
        EMSMessage sms = new EMSMessage();
        sms.requestStatusReport(true);
        sms.setSender(sender);
        sms.setRecipient(recipient);
        sms.setID(smsMessage.getGuid());
        sms.setProperty("SMPP_GUID", smsMessage.getGuid());
        sms.setProperty("SMPP_NAME", httpName);
        sms.setParent(sms);
        HttpObj httpSms = (HttpObj) binderHashtable.get(httpName);
        String response = "";
        String url = "";
        String dnid = "";
        int code = 0;
        new GetMethod();
        System.setProperty("javax.net.ssl.trustStore", "C:\\bulk-cert\\MyMaxisKeyStore.jks");
        if (cFlag.equalsIgnoreCase("1")) {
            logger.info("Concatenated Message");
        } else {
            logger.info("Normal Message");
        }

        if (smsMessage.getMessageType() == 0) {
            smsMessage.setMessage(this.removePriceTag(smsMessage.getMessage()));
            sms.setMessage(smsMessage.getMessage());
            int messageLength = smsMessage.getMessage().length();
            if (messageLength > 153 && !cFlag.equalsIgnoreCase("1")) {
                String randomHex = generateRandomHexCode();
                nMessages = (short) ((int) Math.ceil((double) messageLength / cFlagMaxLength));
                logger.info("messageLength >> " + messageLength);

                for (int i = 0; i < nMessages; ++i) {
                    System.out.print("message number count: " + (i + 1));
                    response = "";
                    dnid = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
                    url = httpSms.getURL();
                    url = url.replace("<msisdn>", sms.getRecipient());
                    url = url.replace("<sendType>", "SMSText");
                    url = url.replace("<masking>", sms.getSender());
                    String checkPriceTagMsg = getContentPart(smsMessage.getMessage(), i, 153);
                    url = url.replace("<msg>", URLEncoder.encode(checkPriceTagMsg));
                    logger.debug("1. URL : " + url);
                    GetMethod var63 = new GetMethod(url);
                    smsMessage.setMessage(this.removePriceTag(sms.getMessage()));
                    this.performHTTPInfobipRequest(var63, response, sms, smsMessage, httpName, dnid, sender, credit, i);
                }
            } else {
                dnid = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
                url = httpSms.getURL();
                url = url.replace("<msisdn>", sms.getRecipient());
                url = url.replace("<sendType>", "SMSText");
                url = url.replace("<masking>", sms.getSender());
                String checkPriceTagMsg = smsMessage.getMessage();
                checkPriceTagMsg = this.removePriceTag(checkPriceTagMsg);
                url = url.replace("<msg>", URLEncoder.encode(checkPriceTagMsg));
                if (messageLength > 160 && cFlag.equalsIgnoreCase("1")) {
                    url = url + "&type=LongSMS";
                }

                GetMethod httpGet = new GetMethod(url);
                logger.debug("2. URL : " + url);

                try {
                    if (messageLength > 153 && cFlag.equalsIgnoreCase("1")) {
                        nMessages = (short) ((int) Math.ceil((double) messageLength / cFlagMaxLength));

                        for (int k = 1; k < nMessages; ++k) {
                            this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, dnid, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), k, credit);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                this.performHTTPInfobipRequest(httpGet, response, sms, smsMessage, httpName, dnid, sender, credit, 0);
            }
        } else if (smsMessage.getMessageType() == 6) {
            smsMessage.setMessage(this.removeChinesePriceTag(smsMessage.getMessage()));
            byte[] data = GsmHelper.decodeIA5(smsMessage.getMessage());
            sms.setUserData(data);
            sms.setCodingGroup((short) 240);
            sms.setAlphabet((short) 8);
            int messageLength = smsMessage.getMessage().length();
            if (messageLength > 256) {
                String randomHex = generateRandomHexCode();
                nMessages = (short) ((int) Math.ceil((double) messageLength / cFlagChineseMaxLength));
                logger.info("messageLength >>" + messageLength);

                for (int i = 0; i < nMessages; ++i) {
                    response = "";
                    url = httpSms.getURL();
                    dnid = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
                    url = url.replaceAll("<msisdn>", sms.getRecipient());
                    url = url.replace("<sendType>", "binary");
                    url = url.replaceAll("<masking>", sms.getSender());
                    url = url.replace("<msg>", URLEncoder.encode(getContentPart(smsMessage.getMessage(), i, 256)));
                    url = url + "&datacoding=8";
                    GetMethod var64 = new GetMethod(url);
                    logger.debug("3. URL : " + url);
                    this.performHTTPInfobipRequest(var64, response, sms, smsMessage, httpName, dnid, sender, credit, i);
                }
            } else {
                url = httpSms.getURL();
                dnid = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
                url = url.replaceAll("<msisdn>", sms.getRecipient());
                url = url.replace("<sendType>", "binary");
                url = url.replaceAll("<masking>", sms.getSender());
                String checkPriceTagMsg = smsMessage.getMessage();
                checkPriceTagMsg = this.removeChinesePriceTag(checkPriceTagMsg);
                url = url.replace("<msg>", URLEncoder.encode(checkPriceTagMsg));
                url = url + "&datacoding=8";
                GetMethod var65 = new GetMethod(url);
                logger.debug("4. URL : " + url);
                this.performHTTPInfobipRequest(var65, response, sms, smsMessage, httpName, dnid, sender, credit, 0);
            }
        } else if (smsMessage.getMessageType() == 5) {
            logger.info("Picture Message not sent to " + smsMessage.getRecipient());
        } else if (smsMessage.getMessageType() == 4) {
            logger.info("Logo Message not sent to " + smsMessage.getRecipient());
        } else if (smsMessage.getMessageType() == 3) {
            logger.info("Ringtone Message not sent to " + smsMessage.getRecipient());
        } else if (smsMessage.getMessageType() == 1) {
            url = httpSms.getURL();
            dnid = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
            url = url.replaceAll("<msisdn>", sms.getRecipient());
            url = url.replace("<sendType>", "Bookmark");
            url = url.replaceAll("<masking>", sms.getSender());
            url = url.replace("<msg>", URLEncoder.encode(smsMessage.getMessage()));
            url = url + "&type=bookmark";
            GetMethod var66 = new GetMethod(url);
            logger.debug("5. URL : " + url);
            logger.info("5. URL : " + url);
            this.performHTTPInfobipRequest(var66, response, sms, smsMessage, httpName, dnid, sender, credit, 0);
        }

    }

    public void sendHttpWebe(String httpName, String credit, SMSMessageSmpp smsMessage) throws MessageException, SQLException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, MalformedURLException, IOException, SQLException {
        String sender = smsMessage.getSender();
        String recipient = smsMessage.getRecipient();
        short nMessages = 1;
        String cFlag = "" + smsMessage.getcFlag();
        double cFlagMaxLength = (double) 153.0F;
        double cFlagChineseMaxLength = (double) 256.0F;
        EMSMessage sms = new EMSMessage();
        sms.requestStatusReport(true);
        sms.setSender(sender);
        sms.setRecipient(recipient);
        sms.setID(smsMessage.getGuid());
        sms.setProperty("SMPP_GUID", smsMessage.getGuid());
        sms.setProperty("SMPP_NAME", httpName);
        sms.setParent(sms);
        HttpObj httpSms = (HttpObj) binderHashtable.get(httpName);
        String response = "";
        String url = "";
        String dnid = "";
        int code = 0;
        new GetMethod();
        System.setProperty("javax.net.ssl.trustStore", "C:\\bulk-cert\\MyMaxisKeyStore.jks");
        if (cFlag.equalsIgnoreCase("1")) {
            logger.info("Concatenated Message");
        } else {
            logger.info("Normal Message");
        }

        if (smsMessage.getMessageType() == 0) {
            smsMessage.setMessage(this.removePriceTag(smsMessage.getMessage()));
            sms.setMessage(smsMessage.getMessage());
            int messageLength = smsMessage.getMessage().length();
            if (messageLength > 153 && !cFlag.equalsIgnoreCase("1")) {
                String randomHex = generateRandomHexCode();
                nMessages = (short) ((int) Math.ceil((double) messageLength / cFlagMaxLength));
                logger.info("messageLength >> " + messageLength);

                for (int i = 0; i < nMessages; ++i) {
                    System.out.print("message number count: " + (i + 1));
                    response = "";
                    dnid = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
                    url = httpSms.getURL();
                    url = url.replace("<msisdn>", sms.getRecipient());
                    url = url.replace("<msgType>", "1");
                    String checkPriceTagMsg = getContentPart(smsMessage.getMessage(), i, 153);
                    url = url.replace("<msg>", URLEncoder.encode(checkPriceTagMsg));
                    logger.debug("1. URL : " + url);
                    GetMethod var57 = new GetMethod(url);
                    smsMessage.setMessage(this.removePriceTag(sms.getMessage()));
                    this.performHTTPWebeRequest(var57, response, sms, smsMessage, httpName, dnid, sender, credit, i);
                }
            } else {
                dnid = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
                url = httpSms.getURL();
                url = url.replace("<msisdn>", sms.getRecipient());
                url = url.replace("<msgType>", "1");
                String checkPriceTagMsg = smsMessage.getMessage();
                checkPriceTagMsg = this.removePriceTag(checkPriceTagMsg);
                url = url.replace("<msg>", URLEncoder.encode(checkPriceTagMsg));
                GetMethod httpGet = new GetMethod(url);
                logger.debug("2. URL : " + url);

                try {
                    if (messageLength > 153 && cFlag.equalsIgnoreCase("1")) {
                        nMessages = (short) ((int) Math.ceil((double) messageLength / cFlagMaxLength));

                        for (int k = 1; k < nMessages; ++k) {
                            this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, dnid, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), k, credit);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                this.performHTTPWebeRequest(httpGet, response, sms, smsMessage, httpName, dnid, sender, credit, 0);
            }
        } else if (smsMessage.getMessageType() == 6) {
            smsMessage.setMessage(this.removeChinesePriceTag(smsMessage.getMessage()));
            byte[] data = GsmHelper.decodeIA5(smsMessage.getMessage());
            sms.setUserData(data);
            sms.setCodingGroup((short) 240);
            sms.setAlphabet((short) 8);
            int messageLength = smsMessage.getMessage().length();
            if (messageLength > 256) {
                String randomHex = generateRandomHexCode();
                nMessages = (short) ((int) Math.ceil((double) messageLength / cFlagChineseMaxLength));
                logger.info("messageLength >>" + messageLength);

                for (int i = 0; i < nMessages; ++i) {
                    response = "";
                    url = httpSms.getURL();
                    dnid = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
                    url = url.replaceAll("<msisdn>", sms.getRecipient());
                    url = url.replace("<msgType>", "3");
                    url = url.replace("<msg>", URLEncoder.encode(getContentPart(smsMessage.getMessage(), i, 256)));
                    GetMethod var58 = new GetMethod(url);
                    logger.debug("3. URL : " + url);
                    this.performHTTPWebeRequest(var58, response, sms, smsMessage, httpName, dnid, sender, credit, i);
                }
            } else {
                url = httpSms.getURL();
                dnid = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
                url = url.replaceAll("<msisdn>", sms.getRecipient());
                url = url.replace("<msgType>", "1");
                String checkPriceTagMsg = smsMessage.getMessage();
                checkPriceTagMsg = this.removeChinesePriceTag(checkPriceTagMsg);
                url = url.replace("<msg>", URLEncoder.encode(checkPriceTagMsg));
                GetMethod var59 = new GetMethod(url);
                logger.debug("4. URL : " + url);
                this.performHTTPWebeRequest(var59, response, sms, smsMessage, httpName, dnid, sender, credit, 0);
            }
        } else if (smsMessage.getMessageType() == 5) {
            logger.info("Picture Message not sent to " + smsMessage.getRecipient());
        } else if (smsMessage.getMessageType() == 4) {
            logger.info("Logo Message not sent to " + smsMessage.getRecipient());
        } else if (smsMessage.getMessageType() == 3) {
            logger.info("Ringtone Message not sent to " + smsMessage.getRecipient());
        } else if (smsMessage.getMessageType() == 1) {
            url = httpSms.getURL();
            dnid = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
            url = url.replaceAll("<msisdn>", sms.getRecipient());
            url = url.replace("<sendType>", "Bookmark");
            url = url.replaceAll("<masking>", sms.getSender());
            url = url.replace("<msg>", URLEncoder.encode(smsMessage.getMessage()));
            url = url + "&type=bookmark";
            GetMethod var60 = new GetMethod(url);
            logger.debug("5. URL : " + url);
            logger.info("5. URL : " + url);
            this.performHTTPWebeRequest(var60, response, sms, smsMessage, httpName, dnid, sender, credit, 0);
        }

    }

    public void sendHttpTATA(String httpName, String credit, SMSMessageSmpp smsMessage) throws MessageException, SQLException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, MalformedURLException, IOException, SQLException {
        System.out.println("################[HTTP_TATA]##############################");
        String sender = smsMessage.getSender();
        String recipient = smsMessage.getRecipient();
        logger.info("sender id >>" + sender);
        short nMessages = 1;
        double cFlagMaxLength = (double) 153.0F;
        double cFlagChineseMaxLength = (double) 252.0F;
            new DESProcessor();
        String cFlag = "" + smsMessage.getcFlag();
        String refid = UidUtil.generateUid();

        EMSMessage sms = new EMSMessage();
        sms.requestStatusReport(true);
        sms.setSender(sender);
        sms.setRecipient(recipient);
        sms.setID(smsMessage.getGuid());
        sms.setProperty("SMPP_GUID", smsMessage.getGuid());
        sms.setProperty("SMPP_NAME", httpName);
        sms.setParent(sms);
        HttpObj httpSms = (HttpObj) binderHashtable.get(httpName);
        String response = "";
        String url = "";
        String dnid = "";
        int code = 0;
        new GetMethod();
        if (cFlag.equalsIgnoreCase("1")) {
            logger.info("Concatenated Message");
        } else {
            logger.info("Normal Message");
        }

        if (smsMessage.getMessageType() == 0) {
            smsMessage.setMessage(this.checkPriceTag(smsMessage.getMessage()));
            int messageLength = smsMessage.getMessage().length();
            System.out.println("");
            if (messageLength > 153 && !cFlag.equalsIgnoreCase("1")) {
                String randomHex = generateRandomHexCode();
                nMessages = (short) ((int) Math.ceil((double) messageLength / cFlagMaxLength));
                logger.info("messageLength >> " + messageLength);

                for (int i = 0; i < nMessages; ++i) {
                    System.out.print("message number count: " + (i + 1));
                    response = "";
                    dnid = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
                    url = httpSms.getURL();
                    url = url.replace("<msisdn>", sms.getRecipient());
                    if (sms.getRecipient().startsWith("601")) {
                        url = url.replace("<from>", "66399");
                    } else {
                        url = url.replace("<from>", "66398");
                    }

                    url = url.replace("<coding>", "0");
                    url = url.replace("<msg>", URLEncoder.encode(getContentPart(this.checkPriceTag(smsMessage.getMessage()), i, 153).trim(), "UTF-8"));
                    logger.info("1. URL : " + url);
                    GetMethod var59 = new GetMethod(url);
                    this.performHTTPTATARequest(var59, response, sms, smsMessage, httpName, dnid, sender, credit, i);
                }
            } else {
                dnid = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
                url = httpSms.getURL();
                url = url.replace("<msisdn>", sms.getRecipient());
                url = url.replace("<coding>", "0");
                url = url.replace("<msg>", URLEncoder.encode(this.checkPriceTag(smsMessage.getMessage()), "UTF-8"));
                GetMethod httpGet = new GetMethod(url);
                logger.debug("2. URL : " + url);
                logger.info("2. URL : " + url);

                try {
                    if (messageLength > 160 && cFlag.equalsIgnoreCase("1")) {
                        nMessages = (short) ((int) Math.ceil((double) messageLength / cFlagMaxLength));

                        for (int k = 1; k < nMessages; ++k) {
                            this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, dnid, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), k, credit);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                this.performHTTPTATARequest(httpGet, response, sms, smsMessage, httpName, dnid, sender, credit, 0);
            }
        } else if (smsMessage.getMessageType() == 6) {
            new Decoder();
            smsMessage.setMessage(this.checkChinesePriceTag(smsMessage.getMessage()));
            String message = smsMessage.getMessage();
            byte[] data = GsmHelper.decodeIA5(smsMessage.getMessage());
            sms.setUserData(data);
            sms.setCodingGroup((short) 240);
            sms.setAlphabet((short) 8);
            int messageLength = smsMessage.getMessage().length();
            logger.info("chinese message length ::" + messageLength);
            if (messageLength > 252 && !cFlag.equalsIgnoreCase("1")) {
                String randomHex = generateRandomHexCode();
                nMessages = (short) ((int) Math.ceil((double) messageLength / cFlagChineseMaxLength));
                logger.info("messageLength >>" + messageLength);

                for (int i = 0; i < nMessages; ++i) {
                    System.out.print("message number count: " + (i + 1));
                    dnid = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
                    url = httpSms.getURL();
                    url = url.replace("<msisdn>", sms.getRecipient());
                    if (sms.getRecipient().startsWith("601")) {
                        url = url.replace("<from>", "66399");
                    } else {
                        url = url.replace("<from>", "66398");
                    }

                    url = url.replace("<coding>", "2");
                    url = url.replace("<msg>", URLEncoder.encode(getContentPart(this.checkChinesePriceTag(smsMessage.getMessage()), i, 256), "UTF-8"));
                    logger.debug("3. URL : " + url);
                    logger.info("3. URL : " + url);
                    GetMethod var61 = new GetMethod(url);
                    this.performHTTPTATARequest(var61, response, sms, smsMessage, httpName, dnid, sender, credit, i);
                }
            } else {
                url = httpSms.getURL();
                url = url.replace("<msisdn>", sms.getRecipient());
                url = url.replace("<coding>", "0");
                url = url.replace("<msg>", URLEncoder.encode(this.checkChinesePriceTag(smsMessage.getMessage()), "UTF-8"));
                GetMethod var60 = new GetMethod(url);
                logger.info("4. URL : " + url);

                try {
                    if (messageLength > 252 && cFlag.equalsIgnoreCase("1")) {
                        nMessages = (short) ((int) Math.ceil((double) messageLength / cFlagChineseMaxLength));

                        for (int k = 1; k < nMessages; ++k) {
                            System.out.println("insert into db >>" + k);
                            this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, dnid, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), k, credit);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                this.performHTTPTATARequest(var60, response, sms, smsMessage, httpName, dnid, sender, credit, 0);
            }
        }

    }

    public void sendHttpSoprano(String httpName, String credit, SMSMessageSmpp smsMessage) throws MessageException, SQLException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, MalformedURLException, IOException, SQLException {
        System.out.println("################[HTTP_SOPRANO]##############################");
        String sender = smsMessage.getSender();
        String recipient = smsMessage.getRecipient();
        logger.info("sender id >>" + sender);
        short nMessages = 1;
        double cFlagMaxLength = (double) 160.0F;
        double cFlagChineseMaxLength = (double) 252.0F;
        new DESProcessor();
        String cFlag = "" + smsMessage.getcFlag();
        String refid = null;

        try {
            refid = UidUtil.generateUid();
        } catch (Exception e) {
            logger.error("Error generating UID", e);
        }

        EMSMessage sms = new EMSMessage();
        sms.requestStatusReport(true);
        sms.setSender(sender);
        sms.setRecipient(recipient);
        sms.setID(smsMessage.getGuid());
        sms.setProperty("SMPP_GUID", smsMessage.getGuid());
        sms.setProperty("SMPP_NAME", httpName);
        sms.setParent(sms);
        HttpObj httpSms = (HttpObj) binderHashtable.get(httpName);
        String response = "";
        String url = "";
        String dnid = "";
        int code = 0;
        new GetMethod();
        if (cFlag.equalsIgnoreCase("1")) {
            logger.info("Concatenated Message");
        } else {
            logger.info("Normal Message");
        }

        if (smsMessage.getMessageType() == 0) {
            smsMessage.setMessage(this.checkPriceTag(smsMessage.getMessage()));
            int messageLength = smsMessage.getMessage().length();
            System.out.println("");
            if (messageLength > 160 && !cFlag.equalsIgnoreCase("1")) {
                String randomHex = generateRandomHexCode();
                nMessages = (short) ((int) Math.ceil((double) messageLength / cFlagMaxLength));
                logger.info("messageLength >> " + messageLength);

                for (int i = 0; i < nMessages; ++i) {
                    System.out.print("message number count: " + (i + 1));
                    response = "";
                    dnid = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
                    url = httpSms.getURL();
                    url = url.replace("<msisdn>", sms.getRecipient());
                    url = url.replace("<msg>", "text=" + URLEncoder.encode(getContentPart(this.checkPriceTag(smsMessage.getMessage()), i, 153).trim(), "UTF-8"));
                    logger.info("1. URL : " + url);
                    GetMethod var53 = new GetMethod(url);
                    this.performHTTPSopranoRequest(var53, response, sms, smsMessage, httpName, dnid, sender, credit, i);
                }
            } else {
                dnid = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
                url = httpSms.getURL();
                url = url.replace("<msisdn>", sms.getRecipient());
                url = url.replace("<msg>", "text=" + URLEncoder.encode(this.checkPriceTag(smsMessage.getMessage()), "UTF-8"));
                GetMethod httpGet = new GetMethod(url);
                logger.debug("2. URL : " + url);
                logger.info("2. URL : " + url);

                try {
                    if (messageLength > 160 && cFlag.equalsIgnoreCase("1")) {
                        nMessages = (short) ((int) Math.ceil((double) messageLength / cFlagMaxLength));

                        for (int k = 1; k < nMessages; ++k) {
                            this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, dnid, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), k, credit);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                this.performHTTPSopranoRequest(httpGet, response, sms, smsMessage, httpName, dnid, sender, credit, 0);
            }
        } else if (smsMessage.getMessageType() == 6) {
            new Decoder();
            smsMessage.setMessage(this.checkChinesePriceTag(smsMessage.getMessage()));
            String message = smsMessage.getMessage();
            byte[] data = GsmHelper.decodeIA5(smsMessage.getMessage());
            sms.setUserData(data);
            sms.setCodingGroup((short) 240);
            sms.setAlphabet((short) 8);
            int messageLength = smsMessage.getMessage().length();
            logger.info("chinese message length ::" + messageLength);
            if (messageLength > 252 && !cFlag.equalsIgnoreCase("1")) {
                String randomHex = generateRandomHexCode();
                nMessages = (short) ((int) Math.ceil((double) messageLength / cFlagChineseMaxLength));
                logger.info("messageLength >>" + messageLength);

                for (int i = 0; i < nMessages; ++i) {
                    System.out.print("message number count: " + (i + 1));
                    dnid = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
                    url = httpSms.getURL();
                    url = url.replace("<msisdn>", sms.getRecipient());
                    url = url.replace("<msg>", "content_type=1&dcs=8&data=" + URLEncoder.encode(getContentPart(this.checkChinesePriceTag(smsMessage.getMessage()), i, 256), "UTF-8"));
                    logger.debug("3. URL : " + url);
                    logger.info("3. URL : " + url);
                    GetMethod var55 = new GetMethod(url);
                    this.performHTTPSopranoRequest(var55, response, sms, smsMessage, httpName, dnid, sender, credit, i);
                }
            } else {
                url = httpSms.getURL();
                url = url.replace("<msisdn>", sms.getRecipient());
                url = url.replace("<msg>", "content_type=1&dcs=8&data=" + URLEncoder.encode(this.checkChinesePriceTag(smsMessage.getMessage()), "UTF-8"));
                GetMethod var54 = new GetMethod(url);
                logger.info("4. URL : " + url);

                try {
                    if (messageLength > 252 && cFlag.equalsIgnoreCase("1")) {
                        nMessages = (short) ((int) Math.ceil((double) messageLength / cFlagChineseMaxLength));

                        for (int k = 1; k < nMessages; ++k) {
                            System.out.println("insert into db >>" + k);
                            this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, dnid, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), k, credit);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                this.performHTTPSopranoRequest(var54, response, sms, smsMessage, httpName, dnid, sender, credit, 0);
            }
        }

    }

    public void sendHttpSopranoTAC(String httpName, String credit, SMSMessageSmpp smsMessage) throws MessageException, SQLException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, MalformedURLException, IOException, SQLException {
        System.out.println("################[HTTP_SOPRANO_TAC]##############################");
        String sender = smsMessage.getSender();
        String recipient = smsMessage.getRecipient();
        logger.info("sender id >>" + sender);
        short nMessages = 1;
        double cFlagMaxLength = (double) 160.0F;
        double cFlagChineseMaxLength = (double) 252.0F;
        new DESProcessor();
        String cFlag = "" + smsMessage.getcFlag();
        String refid = null;

        try {
            refid = UidUtil.generateUid();
        } catch (Exception e) {
            logger.error("Error generating UID", e);
        }

        EMSMessage sms = new EMSMessage();
        sms.requestStatusReport(true);
        sms.setSender(sender);
        sms.setRecipient(recipient);
        sms.setID(smsMessage.getGuid());
        sms.setProperty("SMPP_GUID", smsMessage.getGuid());
        sms.setProperty("SMPP_NAME", httpName);
        sms.setParent(sms);
        HttpObj httpSms = (HttpObj) binderHashtable.get(httpName);
        String response = "";
        String url = "";
        String dnid = "";
        int code = 0;
        new GetMethod();
        if (cFlag.equalsIgnoreCase("1")) {
            logger.info("Concatenated Message");
        } else {
            logger.info("Normal Message");
        }

        if (smsMessage.getMessageType() == 0) {
            smsMessage.setMessage(this.checkPriceTag(smsMessage.getMessage()));
            int messageLength = smsMessage.getMessage().length();
            System.out.println("");
            if (messageLength > 160 && !cFlag.equalsIgnoreCase("1")) {
                String randomHex = generateRandomHexCode();
                nMessages = (short) ((int) Math.ceil((double) messageLength / cFlagMaxLength));
                logger.info("messageLength >> " + messageLength);

                for (int i = 0; i < nMessages; ++i) {
                    System.out.print("message number count: " + (i + 1));
                    response = "";
                    dnid = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
                    url = httpSms.getURL();
                    url = url.replace("<msisdn>", sms.getRecipient());
                    url = url.replace("<msg>", "text=" + URLEncoder.encode(getContentPart(this.checkPriceTag(smsMessage.getMessage()), i, 153).trim(), "UTF-8"));
                    logger.info("1. URL : " + url);
                    GetMethod var53 = new GetMethod(url);
                    this.performHTTPSopranoTacRequest(var53, response, sms, smsMessage, httpName, dnid, sender, credit, i);
                }
            } else {
                dnid = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
                url = httpSms.getURL();
                url = url.replace("<msisdn>", sms.getRecipient());
                url = url.replace("<msg>", "text=" + URLEncoder.encode(this.checkPriceTag(smsMessage.getMessage()), "UTF-8"));
                GetMethod httpGet = new GetMethod(url);
                logger.debug("2. URL : " + url);
                logger.info("2. URL : " + url);

                try {
                    if (messageLength > 160 && cFlag.equalsIgnoreCase("1")) {
                        nMessages = (short) ((int) Math.ceil((double) messageLength / cFlagMaxLength));

                        for (int k = 1; k < nMessages; ++k) {
                            this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, dnid, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), k, credit);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                this.performHTTPSopranoTacRequest(httpGet, response, sms, smsMessage, httpName, dnid, sender, credit, 0);
            }
        } else if (smsMessage.getMessageType() == 6) {
            new Decoder();
            smsMessage.setMessage(this.checkChinesePriceTag(smsMessage.getMessage()));
            String message = smsMessage.getMessage();
            byte[] data = GsmHelper.decodeIA5(smsMessage.getMessage());
            sms.setUserData(data);
            sms.setCodingGroup((short) 240);
            sms.setAlphabet((short) 8);
            int messageLength = smsMessage.getMessage().length();
            logger.info("chinese message length ::" + messageLength);
            if (messageLength > 252 && !cFlag.equalsIgnoreCase("1")) {
                String randomHex = generateRandomHexCode();
                nMessages = (short) ((int) Math.ceil((double) messageLength / cFlagChineseMaxLength));
                logger.info("messageLength >>" + messageLength);

                for (int i = 0; i < nMessages; ++i) {
                    System.out.print("message number count: " + (i + 1));
                    dnid = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
                    url = httpSms.getURL();
                    url = url.replace("<msisdn>", sms.getRecipient());
                    url = url.replace("<msg>", "content_type=1&dcs=8&data=" + URLEncoder.encode(getContentPart(this.checkChinesePriceTag(smsMessage.getMessage()), i, 256), "UTF-8"));
                    logger.debug("3. URL : " + url);
                    logger.info("3. URL : " + url);
                    GetMethod var55 = new GetMethod(url);
                    this.performHTTPSopranoTacRequest(var55, response, sms, smsMessage, httpName, dnid, sender, credit, i);
                }
            } else {
                url = httpSms.getURL();
                url = url.replace("<msisdn>", sms.getRecipient());
                url = url.replace("<msg>", "content_type=1&dcs=8&data=" + URLEncoder.encode(this.checkChinesePriceTag(smsMessage.getMessage()), "UTF-8"));
                GetMethod var54 = new GetMethod(url);
                logger.info("4. URL : " + url);

                try {
                    if (messageLength > 252 && cFlag.equalsIgnoreCase("1")) {
                        nMessages = (short) ((int) Math.ceil((double) messageLength / cFlagChineseMaxLength));

                        for (int k = 1; k < nMessages; ++k) {
                            System.out.println("insert into db >>" + k);
                            this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, dnid, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), k, credit);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                this.performHTTPSopranoTacRequest(var54, response, sms, smsMessage, httpName, dnid, sender, credit, 0);
            }
        }

    }

    public void sendHttpICE(String httpName, String credit, SMSMessageSmpp smsMessage) throws MessageException, SQLException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, MalformedURLException, IOException, SQLException {
        String sender = smsMessage.getSender();
        String recipient = smsMessage.getRecipient();
        short nMessages = 1;
        String cFlag = "" + smsMessage.getcFlag();
        double cFlagMaxLength = (double) 153.0F;
        double cFlagChineseMaxLength = (double) 256.0F;
        EMSMessage sms = new EMSMessage();
        sms.requestStatusReport(true);
        sms.setSender(sender);
        sms.setRecipient(recipient);
        sms.setID(smsMessage.getGuid());
        sms.setProperty("SMPP_GUID", smsMessage.getGuid());
        sms.setProperty("SMPP_NAME", httpName);
        sms.setParent(sms);
        HttpObj httpSms = (HttpObj) binderHashtable.get(httpName);
        String response = "";
        String url = "";
        String dnid = "";
        int code = 0;
        new GetMethod();
        System.setProperty("javax.net.ssl.trustStore", "C:\\bulk-cert\\MyMaxisKeyStore.jks");
        if (cFlag.equalsIgnoreCase("1")) {
            logger.info("Concatenated Message");
        } else {
            logger.info("Normal Message");
        }

        if (smsMessage.getMessageType() == 0) {
            smsMessage.setMessage(this.removePriceTag(smsMessage.getMessage()));
            sms.setMessage(smsMessage.getMessage());
            int messageLength = smsMessage.getMessage().length();
            if (messageLength > 153 && !cFlag.equalsIgnoreCase("1")) {
                String randomHex = generateRandomHexCode();
                nMessages = (short) ((int) Math.ceil((double) messageLength / cFlagMaxLength));
                logger.info("messageLength >> " + messageLength);

                for (int i = 0; i < nMessages; ++i) {
                    System.out.print("message number count: " + (i + 1));
                    response = "";
                    dnid = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
                    url = httpSms.getURL();
                    url = url.replace("<gw-to>", sms.getRecipient());
                    url = url.replace("<gw-coding>", "1");
                    String checkPriceTagMsg = getContentPart(smsMessage.getMessage(), i, 153);
                    url = url.replace("<msg>", URLEncoder.encode(checkPriceTagMsg));
                    logger.debug("1. URL : " + url);
                    GetMethod var57 = new GetMethod(url);
                    smsMessage.setMessage(this.removePriceTag(sms.getMessage()));
                    this.performHTTPICERequest(var57, response, sms, smsMessage, httpName, dnid, sender, credit, i);
                }
            } else {
                dnid = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
                url = httpSms.getURL();
                url = url.replace("<gw-to>", sms.getRecipient());
                url = url.replace("<gw-coding>", "1");
                String checkPriceTagMsg = smsMessage.getMessage();
                checkPriceTagMsg = this.removePriceTag(checkPriceTagMsg);
                url = url.replace("<msg>", URLEncoder.encode(checkPriceTagMsg));
                GetMethod httpGet = new GetMethod(url);
                logger.debug("2. URL : " + url);

                try {
                    if (messageLength > 153 && cFlag.equalsIgnoreCase("1")) {
                        nMessages = (short) ((int) Math.ceil((double) messageLength / cFlagMaxLength));

                        for (int k = 1; k < nMessages; ++k) {
                            this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, dnid, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), k, credit);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                this.performHTTPICERequest(httpGet, response, sms, smsMessage, httpName, dnid, sender, credit, 0);
            }
        } else if (smsMessage.getMessageType() == 6) {
            smsMessage.setMessage(this.removeChinesePriceTag(smsMessage.getMessage()));
            byte[] data = GsmHelper.decodeIA5(smsMessage.getMessage());
            sms.setUserData(data);
            sms.setCodingGroup((short) 240);
            sms.setAlphabet((short) 8);
            int messageLength = smsMessage.getMessage().length();
            if (messageLength > 256) {
                String randomHex = generateRandomHexCode();
                nMessages = (short) ((int) Math.ceil((double) messageLength / cFlagChineseMaxLength));
                logger.info("messageLength >>" + messageLength);

                for (int i = 0; i < nMessages; ++i) {
                    response = "";
                    url = httpSms.getURL();
                    dnid = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
                    url = url.replaceAll("<gw-to>", sms.getRecipient());
                    url = url.replace("<gw-coding>", "3");
                    url = url.replace("<msg>", URLEncoder.encode(getContentPart(smsMessage.getMessage(), i, 256)));
                    GetMethod var58 = new GetMethod(url);
                    logger.debug("3. URL : " + url);
                    this.performHTTPICERequest(var58, response, sms, smsMessage, httpName, dnid, sender, credit, i);
                }
            } else {
                url = httpSms.getURL();
                dnid = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
                url = url.replaceAll("<gw-to>", sms.getRecipient());
                url = url.replace("<gw-coding>", "3");
                String checkPriceTagMsg = smsMessage.getMessage();
                checkPriceTagMsg = this.removeChinesePriceTag(checkPriceTagMsg);
                url = url.replace("<msg>", URLEncoder.encode(checkPriceTagMsg));
                GetMethod var59 = new GetMethod(url);
                logger.debug("4. URL : " + url);
                this.performHTTPICERequest(var59, response, sms, smsMessage, httpName, dnid, sender, credit, 0);
            }
        } else if (smsMessage.getMessageType() == 5) {
            logger.info("Picture Message not sent to " + smsMessage.getRecipient());
        } else if (smsMessage.getMessageType() == 4) {
            logger.info("Logo Message not sent to " + smsMessage.getRecipient());
        } else if (smsMessage.getMessageType() == 3) {
            logger.info("Ringtone Message not sent to " + smsMessage.getRecipient());
        } else if (smsMessage.getMessageType() == 1) {
            url = httpSms.getURL();
            dnid = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
            url = url.replaceAll("<msisdn>", sms.getRecipient());
            url = url.replace("<sendType>", "Bookmark");
            url = url.replaceAll("<masking>", sms.getSender());
            url = url.replace("<msg>", URLEncoder.encode(smsMessage.getMessage()));
            url = url + "&type=bookmark";
            GetMethod var60 = new GetMethod(url);
            logger.debug("5. URL : " + url);
            logger.info("5. URL : " + url);
            this.performHTTPICERequest(var60, response, sms, smsMessage, httpName, dnid, sender, credit, 0);
        }

    }

    public void sendHttpICETAC(String httpName, String credit, SMSMessageSmpp smsMessage) throws MessageException, SQLException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, MalformedURLException, IOException, SQLException {
        String sender = smsMessage.getSender();
        String recipient = smsMessage.getRecipient();
        short nMessages = 1;
        String cFlag = "" + smsMessage.getcFlag();
        double cFlagMaxLength = (double) 153.0F;
        double cFlagChineseMaxLength = (double) 256.0F;
        EMSMessage sms = new EMSMessage();
        sms.requestStatusReport(true);
        sms.setSender(sender);
        sms.setRecipient(recipient);
        sms.setID(smsMessage.getGuid());
        sms.setProperty("SMPP_GUID", smsMessage.getGuid());
        sms.setProperty("SMPP_NAME", httpName);
        sms.setParent(sms);
        HttpObj httpSms = (HttpObj) binderHashtable.get(httpName);
        String response = "";
        String url = "";
        String dnid = "";
        int code = 0;
        new GetMethod();
        System.setProperty("javax.net.ssl.trustStore", "C:\\bulk-cert\\MyMaxisKeyStore.jks");
        if (cFlag.equalsIgnoreCase("1")) {
            logger.info("Concatenated Message");
        } else {
            logger.info("Normal Message");
        }

        if (smsMessage.getMessageType() == 0) {
            smsMessage.setMessage(this.removePriceTag(smsMessage.getMessage()));
            sms.setMessage(smsMessage.getMessage());
            int messageLength = smsMessage.getMessage().length();
            if (messageLength > 153 && !cFlag.equalsIgnoreCase("1")) {
                String randomHex = generateRandomHexCode();
                nMessages = (short) ((int) Math.ceil((double) messageLength / cFlagMaxLength));
                logger.info("messageLength >> " + messageLength);

                for (int i = 0; i < nMessages; ++i) {
                    System.out.print("message number count: " + (i + 1));
                    response = "";
                    dnid = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
                    url = httpSms.getURL();
                    url = url.replace("<gw-to>", sms.getRecipient());
                    url = url.replace("<gw-coding>", "1");
                    String checkPriceTagMsg = getContentPart(smsMessage.getMessage(), i, 153);
                    url = url.replace("<msg>", URLEncoder.encode(checkPriceTagMsg));
                    logger.debug("1. URL : " + url);
                    GetMethod var57 = new GetMethod(url);
                    smsMessage.setMessage(this.removePriceTag(sms.getMessage()));
                    this.performHTTPICETACRequest(var57, response, sms, smsMessage, httpName, dnid, sender, credit, i);
                }
            } else {
                dnid = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
                url = httpSms.getURL();
                url = url.replace("<gw-to>", sms.getRecipient());
                url = url.replace("<gw-coding>", "1");
                String checkPriceTagMsg = smsMessage.getMessage();
                checkPriceTagMsg = this.removePriceTag(checkPriceTagMsg);
                url = url.replace("<msg>", URLEncoder.encode(checkPriceTagMsg));
                GetMethod httpGet = new GetMethod(url);
                logger.debug("2. URL : " + url);

                try {
                    if (messageLength > 153 && cFlag.equalsIgnoreCase("1")) {
                        nMessages = (short) ((int) Math.ceil((double) messageLength / cFlagMaxLength));

                        for (int k = 1; k < nMessages; ++k) {
                            this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, dnid, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), k, credit);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                this.performHTTPICETACRequest(httpGet, response, sms, smsMessage, httpName, dnid, sender, credit, 0);
            }
        } else if (smsMessage.getMessageType() == 6) {
            smsMessage.setMessage(this.removeChinesePriceTag(smsMessage.getMessage()));
            byte[] data = GsmHelper.decodeIA5(smsMessage.getMessage());
            sms.setUserData(data);
            sms.setCodingGroup((short) 240);
            sms.setAlphabet((short) 8);
            int messageLength = smsMessage.getMessage().length();
            if (messageLength > 256) {
                String randomHex = generateRandomHexCode();
                nMessages = (short) ((int) Math.ceil((double) messageLength / cFlagChineseMaxLength));
                logger.info("messageLength >>" + messageLength);

                for (int i = 0; i < nMessages; ++i) {
                    response = "";
                    url = httpSms.getURL();
                    dnid = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
                    url = url.replaceAll("<gw-to>", sms.getRecipient());
                    url = url.replace("<gw-coding>", "3");
                    url = url.replace("<msg>", URLEncoder.encode(getContentPart(smsMessage.getMessage(), i, 256)));
                    GetMethod var58 = new GetMethod(url);
                    logger.debug("3. URL : " + url);
                    this.performHTTPICETACRequest(var58, response, sms, smsMessage, httpName, dnid, sender, credit, i);
                }
            } else {
                url = httpSms.getURL();
                dnid = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
                url = url.replaceAll("<gw-to>", sms.getRecipient());
                url = url.replace("<gw-coding>", "3");
                String checkPriceTagMsg = smsMessage.getMessage();
                checkPriceTagMsg = this.removeChinesePriceTag(checkPriceTagMsg);
                url = url.replace("<msg>", URLEncoder.encode(checkPriceTagMsg));
                GetMethod var59 = new GetMethod(url);
                logger.debug("4. URL : " + url);
                this.performHTTPICETACRequest(var59, response, sms, smsMessage, httpName, dnid, sender, credit, 0);
            }
        } else if (smsMessage.getMessageType() == 5) {
            logger.info("Picture Message not sent to " + smsMessage.getRecipient());
        } else if (smsMessage.getMessageType() == 4) {
            logger.info("Logo Message not sent to " + smsMessage.getRecipient());
        } else if (smsMessage.getMessageType() == 3) {
            logger.info("Ringtone Message not sent to " + smsMessage.getRecipient());
        } else if (smsMessage.getMessageType() == 1) {
            url = httpSms.getURL();
            dnid = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
            url = url.replaceAll("<msisdn>", sms.getRecipient());
            url = url.replace("<sendType>", "Bookmark");
            url = url.replaceAll("<masking>", sms.getSender());
            url = url.replace("<msg>", URLEncoder.encode(smsMessage.getMessage()));
            url = url + "&type=bookmark";
            GetMethod var60 = new GetMethod(url);
            logger.debug("5. URL : " + url);
            logger.info("5. URL : " + url);
            this.performHTTPICETACRequest(var60, response, sms, smsMessage, httpName, dnid, sender, credit, 0);
        }

    }

    public void sendHttpIsentricModem(String httpName, String credit, SMSMessageSmpp smsMessage) throws MessageException, SQLException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, MalformedURLException, IOException, SQLException {
        String sender = smsMessage.getSender();
        String recipient = smsMessage.getRecipient();
        int MSG_MAX_SIZE = 160;
        int messageLength = smsMessage.getMessage().length();
        int nMessages = (short) ((int) Math.ceil((double) messageLength / (double) 160.0F));
        if (httpName.trim().toUpperCase().startsWith("HTTP_ISENTRIC_MODEM") && recipient.trim().startsWith("+")) {
            recipient = recipient.replace("+", "").trim();
        }

        EMSMessage sms = new EMSMessage();
        sms.requestStatusReport(true);
        sms.setSender(sender);
        sms.setRecipient(recipient);
        sms.setID(smsMessage.getGuid());
        sms.setProperty("SMPP_GUID", smsMessage.getGuid());
        sms.setProperty("SMPP_NAME", httpName);
        sms.setParent(sms);
        HttpObj httpSms = (HttpObj) binderHashtable.get(httpName);
        String response = "";
        String url = "";
        String dnid = "";
        int code = 0;
        new GetMethod();
        System.setProperty("javax.net.ssl.trustStore", "C:\\bulk-cert\\MyMaxisKeyStore.jks");
        if (smsMessage.getMessageType() == 0) {
            smsMessage.setMessage(this.checkPriceTag(smsMessage.getMessage()));
            sms.setMessage(smsMessage.getMessage());
            messageLength = smsMessage.getMessage().length();

            try {
                if (messageLength > MSG_MAX_SIZE) {
                    for (int k = 0; k < nMessages; ++k) {
                        url = httpSms.getURL();
                        url = url.replace("<msisdn>", sms.getRecipient());
                        url = url.replace("<type>", "text");
                        url = url.replace("<masking>", sms.getSender());
                        url = url.replace("<msg>", URLEncoder.encode(getContentPart(smsMessage.getMessage(), k, 160)));
                        url = url.replace("<guid>", (String) sms.getProperty("SMPP_GUID"));
                        url = url.replace("<groupid>", URLEncoder.encode(smsMessage.getGroupId()));
                        url = url.replace("<moid>", URLEncoder.encode(smsMessage.getMoid()));
                        url = url.replace("<keyword>", URLEncoder.encode(smsMessage.getKeyword()));
                        url = url + "&msgSeq=" + k;
                        GetMethod httpGet = new GetMethod(url);
                        logger.debug("1. URL : " + url);
                        this.performHTTPModemRequest(httpGet, response, sms, smsMessage, httpName, dnid, sender, credit, k);
                    }
                } else {
                    url = httpSms.getURL();
                    url = url.replace("<msisdn>", sms.getRecipient());
                    url = url.replace("<type>", "text");
                    url = url.replace("<masking>", sms.getSender());
                    String checkPriceTagMsg = smsMessage.getMessage();
                    url = url.replace("<msg>", URLEncoder.encode(checkPriceTagMsg));
                    url = url.replace("<guid>", (String) sms.getProperty("SMPP_GUID"));
                    url = url.replace("<groupid>", URLEncoder.encode(smsMessage.getGroupId()));
                    url = url.replace("<moid>", URLEncoder.encode(smsMessage.getMoid()));
                    url = url.replace("<keyword>", URLEncoder.encode(smsMessage.getKeyword()));
                    GetMethod var68 = new GetMethod(url);
                    logger.debug("2. URL : " + url);
                    this.performHTTPModemRequest(var68, response, sms, smsMessage, httpName, dnid, sender, credit, 0);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (smsMessage.getMessageType() == 6) {
            smsMessage.setMessage(this.checkChinesePriceTag(smsMessage.getMessage()));
            messageLength = smsMessage.getMessage().length();
            logger.info("message length for chinese word ::" + messageLength);
            if (messageLength > 280) {
                String randomHex = generateRandomHexCode();
                nMessages = (short) ((int) Math.ceil((double) messageLength / (double) 280.0F));
                logger.info("messageLength >>" + messageLength);

                for (int i = 0; i < nMessages; ++i) {
                    response = "";
                    url = httpSms.getURL();
                    url = url.replace("<msisdn>", sms.getRecipient());
                    url = url.replace("<type>", "data");
                    url = url.replace("<masking>", sms.getSender());
                    url = url.replace("<msg>", URLEncoder.encode(getContentPart(smsMessage.getMessage(), i, 280)));
                    url = url.replace("<guid>", (String) sms.getProperty("SMPP_GUID"));
                    url = url.replace("<groupid>", URLEncoder.encode(smsMessage.getGroupId()));
                    url = url.replace("<moid>", URLEncoder.encode(smsMessage.getMoid()));
                    url = url.replace("<keyword>", URLEncoder.encode(smsMessage.getKeyword()));
                    url = url + "&dcs=08&binary=1&&msgSeq=" + i;
                    url = url + "&UDH=" + setTextHeader(nMessages, i + 1, randomHex);
                    GetMethod var69 = new GetMethod(url);
                    logger.debug("3. URL : " + url);
                    this.performHTTPModemRequest(var69, response, sms, smsMessage, httpName, dnid, sender, credit, i);
                }
            } else {
                url = httpSms.getURL();
                url = url.replace("<msisdn>", sms.getRecipient());
                url = url.replace("<type>", "data");
                url = url.replace("<masking>", sms.getSender());
                String checkPriceTagMsg = smsMessage.getMessage();
                url = url.replace("<msg>", URLEncoder.encode(checkPriceTagMsg));
                url = url.replace("<guid>", (String) sms.getProperty("SMPP_GUID"));
                url = url.replace("<groupid>", URLEncoder.encode(smsMessage.getGroupId()));
                url = url.replace("<moid>", URLEncoder.encode(smsMessage.getMoid()));
                url = url.replace("<keyword>", URLEncoder.encode(smsMessage.getKeyword()));
                url = url + "&dcs=08&binary=1";
                GetMethod var70 = new GetMethod(url);
                logger.debug("4. URL : " + url);
                this.performHTTPModemRequest(var70, response, sms, smsMessage, httpName, dnid, sender, credit, 0);
            }
        } else if (smsMessage.getMessageType() == 5) {
            logger.info("Picture Message not sent to " + smsMessage.getRecipient());
        } else if (smsMessage.getMessageType() == 4) {
            logger.info("Logo Message not sent to " + smsMessage.getRecipient());
        } else if (smsMessage.getMessageType() == 3) {
            logger.info("Ringtone Message not sent to " + smsMessage.getRecipient());
        } else if (smsMessage.getMessageType() == 1) {
            url = httpSms.getURL();
            dnid = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
            url = url.replaceAll("<msisdn>", sms.getRecipient());
            url = url.replaceAll("<masking>", sms.getSender());
            url = url.replace("<msg>", URLEncoder.encode(smsMessage.getMessage()));
            GetMethod var71 = new GetMethod(url);
            logger.debug("5. URL : " + url);
            logger.info("5. URL : " + url);
            this.performHTTPModemRequest(var71, response, sms, smsMessage, httpName, dnid, sender, credit, 0);
        }

    }

    public void sendHttpUmobileModem(String httpName, String credit, SMSMessageSmpp smsMessage) throws MessageException, SQLException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, MalformedURLException, IOException, SQLException {
        System.out.println("[sendHttpUmobileModem]");
        String sender = smsMessage.getSender();
        String recipient = smsMessage.getRecipient();
        int MSG_MAX_SIZE = 160;
        int messageLength = smsMessage.getMessage().length();
        int nMessages = (short) ((int) Math.ceil((double) messageLength / (double) 160.0F));
        if (httpName.trim().toUpperCase().startsWith("HTTP_UMOBILE_MODEM") && recipient.trim().startsWith("+")) {
            recipient = recipient.replace("+", "").trim();
        }

        EMSMessage sms = new EMSMessage();
        sms.requestStatusReport(true);
        sms.setSender(sender);
        sms.setRecipient(recipient);
        sms.setID(smsMessage.getGuid());
        sms.setProperty("SMPP_GUID", smsMessage.getGuid());
        sms.setProperty("SMPP_NAME", httpName);
        sms.setParent(sms);
        HttpObj httpSms = (HttpObj) binderHashtable.get(httpName);
        String response = "";
        String url = "";
        String dnid = "";
        int code = 0;
        new GetMethod();
        System.setProperty("javax.net.ssl.trustStore", "C:\\bulk-cert\\MyMaxisKeyStore.jks");
        if (smsMessage.getMessageType() == 0) {
            smsMessage.setMessage(this.checkPriceTag(smsMessage.getMessage()));
            sms.setMessage(smsMessage.getMessage());
            messageLength = smsMessage.getMessage().length();

            try {
                if (messageLength > MSG_MAX_SIZE) {
                    for (int k = 0; k < nMessages; ++k) {
                        url = httpSms.getURL();
                        url = url.replace("<msisdn>", sms.getRecipient());
                        url = url.replace("<type>", "text");
                        url = url.replace("<masking>", sms.getSender());
                        url = url.replace("<msg>", URLEncoder.encode(getContentPart(smsMessage.getMessage(), k, 160)));
                        url = url.replace("<guid>", (String) sms.getProperty("SMPP_GUID"));
                        url = url.replace("<groupid>", URLEncoder.encode(smsMessage.getGroupId()));
                        url = url.replace("<moid>", URLEncoder.encode(smsMessage.getMoid()));
                        url = url.replace("<keyword>", URLEncoder.encode(smsMessage.getKeyword()));
                        url = url + "&msgSeq=" + k;
                        GetMethod httpGet = new GetMethod(url);
                        logger.debug("1. URL : " + url);
                        this.performHTTPModemRequest(httpGet, response, sms, smsMessage, httpName, dnid, sender, credit, k);
                    }
                } else {
                    url = httpSms.getURL();
                    url = url.replace("<msisdn>", sms.getRecipient());
                    url = url.replace("<type>", "text");
                    url = url.replace("<masking>", sms.getSender());
                    String checkPriceTagMsg = smsMessage.getMessage();
                    url = url.replace("<msg>", URLEncoder.encode(checkPriceTagMsg));
                    url = url.replace("<guid>", (String) sms.getProperty("SMPP_GUID"));
                    url = url.replace("<groupid>", URLEncoder.encode(smsMessage.getGroupId()));
                    url = url.replace("<moid>", URLEncoder.encode(smsMessage.getMoid()));
                    url = url.replace("<keyword>", URLEncoder.encode(smsMessage.getKeyword()));
                    GetMethod var68 = new GetMethod(url);
                    logger.debug("2. URL : " + url);
                    this.performHTTPModemRequest(var68, response, sms, smsMessage, httpName, dnid, sender, credit, 0);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (smsMessage.getMessageType() == 6) {
            smsMessage.setMessage(this.checkChinesePriceTag(smsMessage.getMessage()));
            messageLength = smsMessage.getMessage().length();
            logger.info("message length for chinese word ::" + messageLength);
            if (messageLength > 280) {
                String randomHex = generateRandomHexCode();
                nMessages = (short) ((int) Math.ceil((double) messageLength / (double) 280.0F));
                logger.info("messageLength >>" + messageLength);

                for (int i = 0; i < nMessages; ++i) {
                    response = "";
                    url = httpSms.getURL();
                    url = url.replace("<msisdn>", sms.getRecipient());
                    url = url.replace("<type>", "data");
                    url = url.replace("<masking>", sms.getSender());
                    url = url.replace("<msg>", URLEncoder.encode(getContentPart(smsMessage.getMessage(), i, 280)));
                    url = url.replace("<guid>", (String) sms.getProperty("SMPP_GUID"));
                    url = url.replace("<groupid>", URLEncoder.encode(smsMessage.getGroupId()));
                    url = url.replace("<moid>", URLEncoder.encode(smsMessage.getMoid()));
                    url = url.replace("<keyword>", URLEncoder.encode(smsMessage.getKeyword()));
                    url = url + "&dcs=08&binary=1&&msgSeq=" + i;
                    url = url + "&UDH=" + setTextHeader(nMessages, i + 1, randomHex);
                    GetMethod var69 = new GetMethod(url);
                    logger.debug("3. URL : " + url);
                    this.performHTTPModemRequest(var69, response, sms, smsMessage, httpName, dnid, sender, credit, i);
                }
            } else {
                url = httpSms.getURL();
                url = url.replace("<msisdn>", sms.getRecipient());
                url = url.replace("<type>", "data");
                url = url.replace("<masking>", sms.getSender());
                String checkPriceTagMsg = smsMessage.getMessage();
                url = url.replace("<msg>", URLEncoder.encode(checkPriceTagMsg));
                url = url.replace("<guid>", (String) sms.getProperty("SMPP_GUID"));
                url = url.replace("<groupid>", URLEncoder.encode(smsMessage.getGroupId()));
                url = url.replace("<moid>", URLEncoder.encode(smsMessage.getMoid()));
                url = url.replace("<keyword>", URLEncoder.encode(smsMessage.getKeyword()));
                url = url + "&dcs=08&binary=1";
                GetMethod var70 = new GetMethod(url);
                logger.debug("4. URL : " + url);
                this.performHTTPModemRequest(var70, response, sms, smsMessage, httpName, dnid, sender, credit, 0);
            }
        } else if (smsMessage.getMessageType() == 5) {
            logger.info("Picture Message not sent to " + smsMessage.getRecipient());
        } else if (smsMessage.getMessageType() == 4) {
            logger.info("Logo Message not sent to " + smsMessage.getRecipient());
        } else if (smsMessage.getMessageType() == 3) {
            logger.info("Ringtone Message not sent to " + smsMessage.getRecipient());
        } else if (smsMessage.getMessageType() == 1) {
            url = httpSms.getURL();
            dnid = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
            url = url.replaceAll("<msisdn>", sms.getRecipient());
            url = url.replaceAll("<masking>", sms.getSender());
            url = url.replace("<msg>", URLEncoder.encode(smsMessage.getMessage()));
            GetMethod var71 = new GetMethod(url);
            logger.debug("5. URL : " + url);
            logger.info("5. URL : " + url);
            this.performHTTPModemRequest(var71, response, sms, smsMessage, httpName, dnid, sender, credit, 0);
        }

    }

    public void sendHttpXOX(String httpName, String credit, SMSMessageSmpp smsMessage) throws MessageException, SQLException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, MalformedURLException, IOException, SQLException {
        System.out.println("################[XOX]##############################");
        String sender = smsMessage.getSender();
        String recipient = smsMessage.getRecipient();
        short nMessages = 1;
        double cFlagMaxLength = (double) 153.0F;
        double cFlagChineseMaxLength = (double) 252.0F;
        new DESProcessor();
        String cFlag = "" + smsMessage.getcFlag();
        UUIDGenerator uuid = UUIDGenerator.getInstance();
        String company = "isentric";
        String user = "isentric";
        String password = "95Hn28TqG";
        String signature = "";
        EMSMessage sms = new EMSMessage();
        sms.requestStatusReport(true);
        sms.setSender(sender);
        sms.setRecipient(recipient);
        sms.setID(smsMessage.getGuid());
        sms.setProperty("SMPP_GUID", smsMessage.getGuid());
        sms.setProperty("SMPP_NAME", httpName);
        sms.setParent(sms);
        HttpObj httpSms = (HttpObj) binderHashtable.get(httpName);
        String response = "";
        String url = "";
        String dnid = "";
        int code = 0;
        new GetMethod();
        if (cFlag.equalsIgnoreCase("1")) {
            logger.info("Concatenated Message");
        } else {
            logger.info("Normal Message");
        }

        if (smsMessage.getMessageType() == 0) {
            smsMessage.setMessage(this.removePriceTag(smsMessage.getMessage()));
            double messageLength = (double) smsMessage.getMessage().length();
            System.out.println("");
            if (messageLength > (double) 153.0F) {
                String randomHex = generateRandomHexCode();
                nMessages = (short) ((int) Math.ceil(messageLength / cFlagMaxLength));
                logger.info("messageLength >> " + messageLength);

                for (int i = 0; i < nMessages; ++i) {
                    System.out.print("message number count: " + (i + 1));
                    response = "";
                    dnid = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
                    signature = "##" + company + "##" + user + "##" + password + "##" + dnid + "##";
                    url = httpSms.getURL();
                    url = url.replace("<msisdn>", sms.getRecipient());
                    url = url.replace("<msgType>", httpSms.getEnText());
                    url = url.replace("<refid>", dnid);
                    url = url.replace("<signature>", URLEncoder.encode(SHA1(signature), "UTF-8"));
                    url = url.replace("<msg>", URLEncoder.encode(getContentPart(smsMessage.getMessage(), i, 153).trim(), "UTF-8"));
                    logger.debug("1. URL : " + url);
                    logger.info("1. URL : " + url);
                    GetMethod httpGet = new GetMethod(url);
                    this.performHTTPRequest(httpGet, response, sms, smsMessage, httpName, dnid, sender, credit, i);
                }
            } else {
                dnid = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
                signature = "##" + company + "##" + user + "##" + password + "##" + dnid + "##";
                url = httpSms.getURL();
                url = url.replace("<msisdn>", sms.getRecipient());
                url = url.replace("<msgType>", httpSms.getEnText());
                url = url.replace("<refid>", dnid);
                url = url.replace("<signature>", URLEncoder.encode(SHA1(signature), "UTF-8"));
                url = url.replace("<msg>", URLEncoder.encode(smsMessage.getMessage(), "UTF-8"));
                GetMethod var71 = new GetMethod(url);
                logger.info("2. URL : " + url);
                this.performHTTPRequest(var71, response, sms, smsMessage, httpName, dnid, sender, credit, 0);
            }
        } else if (smsMessage.getMessageType() == 6) {
            Decoder de = new Decoder();
            smsMessage.setMessage(this.removeChinesePriceTag(smsMessage.getMessage()));
            String message = smsMessage.getMessage();
            byte[] data = GsmHelper.decodeIA5(smsMessage.getMessage());
            sms.setUserData(data);
            sms.setCodingGroup((short) 240);
            sms.setAlphabet((short) 8);
            double messageLength = (double) smsMessage.getMessage().length();
            if (messageLength > (double) 252.0F) {
                String randomHex = generateRandomHexCode();
                nMessages = (short) ((int) Math.ceil(messageLength / cFlagChineseMaxLength));
                logger.info("messageLength >>" + messageLength);

                for (int i = 0; i < nMessages; ++i) {
                    message = getContentPart(smsMessage.getMessage(), i, 252).trim();
                    logger.info("message ::" + message);

                    try {
                        String var77 = de.insertUtfIdentifier(message);
                        message = var77.replace("\\", "");
                        logger.info("after convert ::" + message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    System.out.print("message number count: " + (i + 1));
                    dnid = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
                    signature = "##" + company + "##" + user + "##" + password + "##" + dnid + "##";
                    url = httpSms.getURL();
                    url = url.replace("<msisdn>", sms.getRecipient());
                    url = url.replace("<msgType>", httpSms.getChText());
                    url = url.replace("<refid>", dnid);
                    url = url.replace("<signature>", URLEncoder.encode(SHA1(signature), "UTF-8"));
                    url = url.replace("<msg>", URLEncoder.encode(message, "UTF-8"));
                    logger.debug("3. URL : " + url);
                    logger.info("3. URL : " + url);
                    GetMethod var72 = new GetMethod(url);
                    this.performHTTPRequest(var72, response, sms, smsMessage, httpName, dnid, sender, credit, i);
                }
            } else {
                try {
                    String var78 = de.insertUtfIdentifier(message);
                    message = var78.replace("\\", "");
                    logger.info("after convert ::" + message);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                dnid = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
                signature = "##" + company + "##" + user + "##" + password + "##" + dnid + "##";
                url = httpSms.getURL();
                url = url.replace("<msisdn>", sms.getRecipient());
                url = url.replace("<msgType>", httpSms.getChText());
                url = url.replace("<refid>", dnid);
                url = url.replace("<signature>", URLEncoder.encode(SHA1(signature), "UTF-8"));
                url = url.replace("<msg>", URLEncoder.encode(message, "UTF-8"));
                GetMethod var73 = new GetMethod(url);
                logger.debug("4. URL : " + url);
                logger.info("4. URL : " + url);
                this.performHTTPRequest(var73, response, sms, smsMessage, httpName, dnid, sender, credit, 0);
            }
        } else if (smsMessage.getMessageType() == 5) {
            logger.info("Picture Message not sent to " + smsMessage.getRecipient());
        } else if (smsMessage.getMessageType() == 4) {
            logger.info("Logo Message not sent to " + smsMessage.getRecipient());
        } else if (smsMessage.getMessageType() == 3) {
            logger.info("Ringtone Message not sent to " + smsMessage.getRecipient());
        } else if (smsMessage.getMessageType() == 1) {
            logger.info("Ringtone Message not sent to " + smsMessage.getRecipient());
        }

    }

    public void sendHttpNexMO(String httpName, String credit, SMSMessageSmpp smsMessage) throws MessageException, SQLException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, MalformedURLException, IOException, SQLException {
        System.out.println("################[NEXMO]##############################");
        String sender = smsMessage.getSender();
        String recipient = smsMessage.getRecipient();
        short nMessages = 1;
        double cFlagMaxLength = (double) 153.0F;
        double cFlagChineseMaxLength = (double) 252.0F;
        new DESProcessor();
        String cFlag = "" + smsMessage.getcFlag();
        UUIDGenerator uuid = UUIDGenerator.getInstance();
        String company = "isentric";
        String user = "isentric";
        String password = "95Hn28TqG";
        String signature = "";
        EMSMessage sms = new EMSMessage();
        sms.requestStatusReport(true);
        sms.setSender(sender);
        sms.setRecipient(recipient);
        sms.setID(smsMessage.getGuid());
        sms.setProperty("SMPP_GUID", smsMessage.getGuid());
        sms.setProperty("SMPP_NAME", httpName);
        sms.setParent(sms);
        HttpObj httpSms = (HttpObj) binderHashtable.get(httpName);
        String response = "";
        String url = "";
        String dnid = "";
        int code = 0;
        if (cFlag.equalsIgnoreCase("1")) {
            logger.info("Concatenated Message");
        } else {
            logger.info("Normal Message");
        }

        if (smsMessage.getMessageType() == 0) {
            smsMessage.setMessage(this.checkPriceTag(smsMessage.getMessage()));
            double messageLength = (double) smsMessage.getMessage().length();
            if (messageLength > (double) 153.0F) {
                String randomHex = generateRandomHexCode();
                nMessages = (short) ((int) Math.ceil(messageLength / cFlagMaxLength));
                logger.info("messageLength >> " + messageLength);

                for (int i = 0; i < nMessages; ++i) {
                    System.out.print("message number count: " + (i + 1));
                    response = "";
                    dnid = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
                    url = httpSms.getURL();
                    url = url.replace("<from>", sms.getSender());
                    url = url.replace("<type>", httpSms.getEnText());
                    url = url.replace("<to>", sms.getRecipient());
                    url = url.replace("<text>", URLEncoder.encode(getContentPart(smsMessage.getMessage(), i, 153).trim(), "UTF-8"));
                    logger.debug("1. URL : " + url);
                    new URL(url);
                    this.performHTTPNEXMO(sms, smsMessage, httpName, dnid, sender, credit, i, url);
                }
            } else {
                dnid = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
                url = httpSms.getURL();
                url = url.replace("<from>", sms.getSender());
                url = url.replace("<type>", httpSms.getEnText());
                url = url.replace("<to>", sms.getRecipient());
                url = url.replace("<text>", URLEncoder.encode(smsMessage.getMessage(), "UTF-8"));
                logger.debug("2. URL : " + url);
                this.performHTTPNEXMO(sms, smsMessage, httpName, dnid, sender, credit, 0, url);
            }
        } else if (smsMessage.getMessageType() == 6) {
            Decoder de = new Decoder();
            smsMessage.setMessage(this.removeChinesePriceTag(smsMessage.getMessage()));
            String message = smsMessage.getMessage();
            byte[] data = GsmHelper.decodeIA5(smsMessage.getMessage());
            sms.setUserData(data);
            sms.setCodingGroup((short) 240);
            sms.setAlphabet((short) 8);
            double messageLength = (double) smsMessage.getMessage().length();
            if (messageLength > (double) 252.0F) {
                String randomHex = generateRandomHexCode();
                nMessages = (short) ((int) Math.ceil(messageLength / cFlagChineseMaxLength));
                logger.info("messageLength >>" + messageLength);

                for (int i = 0; i < nMessages; ++i) {
                    message = getContentPart(this.checkChinesePriceTag(smsMessage.getMessage()), i, 252).trim();
                    logger.info("message ::" + message);

                    try {
                        String var66 = de.insertUtfIdentifier(message);
                        message = var66.replace("\\", "");
                        logger.info("after convert[" + i + "]=" + message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    System.out.print("message number count: " + (i + 1));
                    dnid = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
                    url = httpSms.getURL();
                    url = url.replace("<from>", sms.getSender());
                    url = url.replace("<to>", sms.getRecipient());
                    url = url.replace("<type>", httpSms.getChText());
                    url = url.replace("<text>", URLEncoder.encode(message, "UTF-8"));
                    logger.debug("3. URL : " + url);
                    this.performHTTPNEXMO(sms, smsMessage, httpName, dnid, sender, credit, i, url);
                }
            } else {
                try {
                    String var67 = de.insertUtfIdentifier(message);
                    message = var67.replace("\\", "");
                    logger.info("after convert =" + message);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                dnid = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
                url = httpSms.getURL();
                url = url.replace("<from>", sms.getSender());
                url = url.replace("<to>", sms.getRecipient());
                url = url.replace("<type>", httpSms.getChText());
                url = url.replace("<text>", URLEncoder.encode(message, "UTF-8"));
                logger.debug("4. URL : " + url);
                this.performHTTPNEXMO(sms, smsMessage, httpName, dnid, sender, credit, 0, url);
            }
        } else if (smsMessage.getMessageType() == 5) {
            logger.info("Picture Message not sent to " + smsMessage.getRecipient());
        } else if (smsMessage.getMessageType() == 4) {
            logger.info("Logo Message not sent to " + smsMessage.getRecipient());
        } else if (smsMessage.getMessageType() == 3) {
            logger.info("Ringtone Message not sent to " + smsMessage.getRecipient());
        } else if (smsMessage.getMessageType() == 1) {
            logger.info("Ringtone Message not sent to " + smsMessage.getRecipient());
        }

    }

    public void sendHttpGINSMS(String httpName, String credit, SMSMessageSmpp smsMessage) throws MessageException, SQLException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, MalformedURLException, IOException, SQLException {
        System.out.println("################[GINSMS]##############################");
        String sender = smsMessage.getSender();
        String recipient = smsMessage.getRecipient();
        short nMessages = 1;
        double cFlagMaxLength = (double) 153.0F;
        double cFlagChineseMaxLength = (double) 252.0F;
        String cFlag = "" + smsMessage.getcFlag();
        EMSMessage sms = new EMSMessage();
        sms.requestStatusReport(true);
        sms.setSender(sender);
        sms.setRecipient(recipient);
        sms.setID(smsMessage.getGuid());
        sms.setProperty("SMPP_GUID", smsMessage.getGuid());
        sms.setProperty("SMPP_NAME", httpName);
        sms.setParent(sms);
        HttpObj httpSms = (HttpObj) binderHashtable.get(httpName);
        String response = "";
        String url = "";
        String dnid = "";
        int code = 0;
        System.setProperty("javax.net.ssl.trustStore", "C:\\bulk-cert\\MyMaxisKeyStore.jks");
        new HttpClient();
        new GetMethod();
        new PostMethod();
        if (cFlag.equalsIgnoreCase("1")) {
            logger.info("Concatenated Message");
        } else {
            logger.info("Normal Message");
        }

        if (smsMessage.getMessageType() == 0) {
            smsMessage.setMessage(this.checkPriceTag(smsMessage.getMessage()));
            double messageLength = (double) smsMessage.getMessage().length();
            if (messageLength > (double) 153.0F) {
                String randomHex = generateRandomHexCode();
                nMessages = (short) ((int) Math.ceil(messageLength / cFlagMaxLength));
                logger.info("messageLength >> " + messageLength);

                for (int i = 0; i < nMessages; ++i) {
                    System.out.print("message number count: " + (i + 1));
                    response = "";
                    dnid = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
                    url = httpSms.getURL();
                    url = url.replace("<srcAddr>", sms.getSender());
                    url = url.replace("<type>", httpSms.getEnText());
                    url = url.replace("<dstAddr>", sms.getRecipient());
                    url = url.replace("<text>", URLEncoder.encode(getContentPart(smsMessage.getMessage(), i, 153).trim(), "UTF-8"));
                    logger.debug("1. URL : " + url);
                    GetMethod httpGet = new GetMethod(url);
                    this.performHTTPGINSMSRequest(httpGet, response, sms, smsMessage, httpName, dnid, sender, credit, i);
                }
            } else {
                dnid = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
                url = httpSms.getURL();
                url = url.replace("<srcAddr>", sms.getSender());
                url = url.replace("<type>", httpSms.getEnText());
                url = url.replace("<dstAddr>", sms.getRecipient());
                url = url.replace("<text>", URLEncoder.encode(smsMessage.getMessage(), "UTF-8"));
                logger.debug("2. URL : " + url);
                GetMethod var59 = new GetMethod(url);
                this.performHTTPGINSMSRequest(var59, response, sms, smsMessage, httpName, dnid, sender, credit, 0);
            }
        } else if (smsMessage.getMessageType() == 6) {
            Decoder de = new Decoder();
            smsMessage.setMessage(this.removeChinesePriceTag(smsMessage.getMessage()));
            String message = smsMessage.getMessage();
            byte[] data = GsmHelper.decodeIA5(smsMessage.getMessage());
            sms.setUserData(data);
            sms.setCodingGroup((short) 240);
            sms.setAlphabet((short) 8);
            double messageLength = (double) smsMessage.getMessage().length();
            if (messageLength > (double) 252.0F) {
                String randomHex = generateRandomHexCode();
                nMessages = (short) ((int) Math.ceil(messageLength / cFlagChineseMaxLength));
                logger.info("messageLength >>" + messageLength);

                for (int i = 0; i < nMessages; ++i) {
                    message = getContentPart(this.checkChinesePriceTag(smsMessage.getMessage()), i, 252).trim();
                    logger.info("message ::" + message);

                    try {
                        String var65 = de.insertUtfIdentifier(message);
                        message = var65.replace("\\", "");
                        logger.info("after convert[" + i + "]=" + message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    System.out.print("message number count: " + (i + 1));
                    dnid = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
                    url = httpSms.getURL();
                    url = url.replace("<srcAddr>", sms.getSender());
                    url = url.replace("<type>", httpSms.getEnText());
                    url = url.replace("<dstAddr>", sms.getRecipient());
                    url = url.replace("<text>", URLEncoder.encode(message, "UTF-8"));
                    logger.debug("3. URL : " + url);
                    GetMethod var60 = new GetMethod(url);
                    this.performHTTPGINSMSRequest(var60, response, sms, smsMessage, httpName, dnid, sender, credit, i);
                }
            } else {
                try {
                    String var66 = de.insertUtfIdentifier(message);
                    message = var66.replace("\\", "");
                    logger.info("after convert =" + message);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                dnid = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
                url = httpSms.getURL();
                url = url.replace("<srcAddr>", sms.getSender());
                url = url.replace("<type>", httpSms.getEnText());
                url = url.replace("<dstAddr>", sms.getRecipient());
                url = url.replace("<text>", URLEncoder.encode(message, "UTF-8"));
                logger.debug("4. URL : " + url);
                GetMethod var61 = new GetMethod(url);
                this.performHTTPGINSMSRequest(var61, response, sms, smsMessage, httpName, dnid, sender, credit, 0);
            }
        } else if (smsMessage.getMessageType() == 5) {
            logger.info("Picture Message not sent to " + smsMessage.getRecipient());
        } else if (smsMessage.getMessageType() == 4) {
            logger.info("Logo Message not sent to " + smsMessage.getRecipient());
        } else if (smsMessage.getMessageType() == 3) {
            logger.info("Ringtone Message not sent to " + smsMessage.getRecipient());
        } else if (smsMessage.getMessageType() == 1) {
            logger.info("Ringtone Message not sent to " + smsMessage.getRecipient());
        }

    }

    public void sendHttpUmobile(String httpName, String credit, SMSMessageSmpp smsMessage) throws MessageException, SQLException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, MalformedURLException, IOException, SQLException {
        String sender = smsMessage.getSender();
        String recipient = smsMessage.getRecipient();
        short nMessages = 1;
        String cFlag = "" + smsMessage.getcFlag();
        EMSMessage sms = new EMSMessage();
        sms.requestStatusReport(true);
        sms.setSender(sender);
        sms.setRecipient(recipient);
        sms.setID(smsMessage.getGuid());
        sms.setProperty("SMPP_GUID", smsMessage.getGuid());
        sms.setProperty("SMPP_NAME", httpName);
        sms.setParent(sms);
        String url = "https://gw.cpa-umobile.com/CPABULK/MT.aspx";
        String dnid = "";
        int code = 0;
        if (cFlag.equalsIgnoreCase("1")) {
            logger.info("Concatenated Message");
        } else {
            logger.info("Normal Message");
        }

        if (smsMessage.getMessageType() == 0) {
            logger.info("=========================================normal text=========================================");
            smsMessage.setMessage(this.checkPriceTag(smsMessage.getMessage()));
            double messageLength = (double) smsMessage.getMessage().length();
            dnid = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
            StringBuffer URLBuffer = null;
            logger.info("[userID] isentric");
            logger.info("[pass] isentric2@13");
            logger.info("[shortcode] 66399");
            logger.info("[msisdn] " + sms.getRecipient());
            logger.info("[msgtype] 1");
            logger.info("[msg] " + nMessages);
            logger.info("[msgID] " + dnid);
            URLBuffer = new StringBuffer(url);
            URLBuffer.append("?userID=" + URLEncoder.encode("isentric", "UTF-8"));
            URLBuffer.append("&pass=" + URLEncoder.encode("isentric2@13", "UTF-8"));
            URLBuffer.append("&shortcode=" + URLEncoder.encode("66399", "UTF-8"));
            URLBuffer.append("&msisdn=" + URLEncoder.encode(sms.getRecipient(), "UTF-8"));
            URLBuffer.append("&msgtype=" + URLEncoder.encode("1", "UTF-8"));
            URLBuffer.append("&msg=" + URLEncoder.encode(smsMessage.getMessage(), "UTF-8"));
            this.performHTTPUmobile(URLBuffer.toString(), sms, smsMessage, httpName, dnid, sender, credit, 0);
        } else if (smsMessage.getMessageType() == 6) {
            logger.info("=========================================CHINESE=========================================");
            logger.info("msisdn >>" + smsMessage.getRecipient());
            logger.info("keyword >>" + smsMessage.getKeyword());
            logger.info("ORI MSG >>" + smsMessage.getMessage());
            smsMessage.setMessage(this.checkChinesePriceTag(smsMessage.getMessage()));
            String message = smsMessage.getMessage();
            logger.info("APPENDED MSG " + smsMessage.getMessage());
            byte[] data = GsmHelper.decodeIA5(smsMessage.getMessage());
            sms.setUserData(data);
            sms.setCodingGroup((short) 240);
            sms.setAlphabet((short) 8);
            logger.info("1.\tsms getType =" + sms.getType());
            dnid = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
            StringBuffer URLBuffer = null;
            logger.info("[userID] isentric");
            logger.info("[pass] isentric2@13");
            logger.info("[shortcode] 66399");
            logger.info("[msisdn] " + sms.getRecipient());
            logger.info("[msgtype] 3");
            logger.info("[msg] " + nMessages);
            logger.info("[msgID] " + dnid);
            URLBuffer = new StringBuffer(url);
            URLBuffer.append("?userID=" + URLEncoder.encode("isentric", "UTF-8"));
            URLBuffer.append("&pass=" + URLEncoder.encode("isentric2@13", "UTF-8"));
            URLBuffer.append("&shortcode=" + URLEncoder.encode("66399", "UTF-8"));
            URLBuffer.append("&msisdn=" + URLEncoder.encode(sms.getRecipient(), "UTF-8"));
            URLBuffer.append("&msgtype=" + URLEncoder.encode("3", "UTF-8"));
            URLBuffer.append("&msg=" + URLEncoder.encode(smsMessage.getMessage(), "UTF-8"));
            this.performHTTPUmobile(URLBuffer.toString(), sms, smsMessage, httpName, dnid, sender, credit, 0);
        }

    }

    public void sendHttpUmobile2way(String httpName, String credit, SMSMessageSmpp smsMessage) throws MessageException, SQLException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, MalformedURLException, IOException, SQLException {
        String sender = smsMessage.getSender();
        String recipient = smsMessage.getRecipient();
        short nMessages = 1;
        double cFlagMaxLength = (double) 153.0F;
        double cFlagChineseMaxLength = (double) 252.0F;
        new DESProcessor();
        String cFlag = "" + smsMessage.getcFlag();
        UUIDGenerator uuid = UUIDGenerator.getInstance();
        String signature = "";
        EMSMessage sms = new EMSMessage();
        sms.requestStatusReport(true);
        sms.setSender(sender);
        sms.setRecipient(recipient);
        sms.setID(smsMessage.getGuid());
        sms.setProperty("SMPP_GUID", smsMessage.getGuid());
        sms.setProperty("SMPP_NAME", httpName);
        sms.setParent(sms);
        HttpObj httpSms = (HttpObj) binderHashtable.get(httpName);
        String response = "";
        String url = "";
        String dnid = "";
        int code = 0;
        new GetMethod();
        if (cFlag.equalsIgnoreCase("1")) {
            logger.info("Concatenated Message");
        } else {
            logger.info("Normal Message");
        }

        if (smsMessage.getMessageType() == 0) {
            smsMessage.setMessage(this.checkPriceTag(smsMessage.getMessage()));
            double messageLength = (double) smsMessage.getMessage().length();
            if (messageLength > (double) 160.0F) {
                String randomHex = generateRandomHexCode();
                nMessages = (short) ((int) Math.ceil(messageLength / cFlagMaxLength));
                logger.info("messageLength >> " + messageLength);

                for (int i = 0; i < nMessages; ++i) {
                    System.out.print("message number count: " + (i + 1));
                    response = "";
                    dnid = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
                    int responseCode = -3;
                    new HttpClient();
                    String mpg_response = "";
                    PostMethod postMethod = new PostMethod("http://123.136.102.34:8080/Gatekeeper/GatekeeperServlet");
                    String result = null;
                    String signatureApi = "";
                    String timeStamp = getTimeStamp();
                    String X_DRUTT_MSGMT_PROVIDER_SIGNATURE = "P-R0Sr4BNJxH3;S-yQSr4BNJxH3;isentric123;" + timeStamp;
                    MessageDigest m = MessageDigest.getInstance("MD5");
                    m.update(X_DRUTT_MSGMT_PROVIDER_SIGNATURE.getBytes(), 0, X_DRUTT_MSGMT_PROVIDER_SIGNATURE.length());
                    signature = (new BigInteger(1, m.digest())).toString(16);
                    logger.info("X-DRUTT-MSGMT-TYPE: bulkSMS");
                    logger.info("X-DRUTT-PROVIDER: P-R0Sr4BNJxH3");
                    logger.info("X-DRUTT-SERVICE: S-yQSr4BNJxH3");
                    logger.info("X-DRUTT-MSGMT-PROVIDER-SIGNATURE: " + signatureApi);
                    logger.info("X-DRUTT-MSGMT-PROVIDER-TIME: " + timeStamp);
                    logger.info("X-DRUTT-MSGMT-ACTION: DeliverContent");
                    logger.info("X-DRUTT-PREMIUM-RESOURCE-TYPE: sms_bulk_0000");
                    logger.info("Content-Type: text/xml;charset=UTF-8");
                    logger.info("<?xml version=\"1.0\" encoding=\"UTF-8\"?><deliverDistributionRequest><distributionList>" + sms.getRecipient() + "</distributionList><deliverContent><textSMS><![CDATA[" + escapeURIPathParam(getContentPart(smsMessage.getMessage(), i, 160).trim()) + "]]></textSMS></deliverContent></deliverDistributionRequest>");
                    postMethod.addRequestHeader("X-DRUTT-MSGMT-TYPE", "bulkSMS");
                    postMethod.addRequestHeader("X-DRUTT-PROVIDER", "P-R0Sr4BNJxH3");
                    postMethod.addRequestHeader("X-DRUTT-SERVICE", "S-yQSr4BNJxH3");
                    postMethod.addRequestHeader("X-DRUTT-MSGMT-PROVIDER-SIGNATURE", signature);
                    postMethod.addRequestHeader("X-DRUTT-MSGMT-PROVIDER-TIME", timeStamp);
                    postMethod.addRequestHeader("X-DRUTT-MSGMT-ACTION", "DeliverContent");
                    postMethod.addRequestHeader("X-DRUTT-PREMIUM-RESOURCE-TYPE", "sms_bulk_0000");
                    postMethod.addRequestHeader("Content-Type", "text/xml;charset=UTF-8");
                    postMethod.setRequestBody("<?xml version=\"1.0\" encoding=\"UTF-8\"?><deliverDistributionRequest><distributionList>" + sms.getRecipient() + "</distributionList><deliverContent><textSMS><![CDATA[" + escapeURIPathParam(getContentPart(smsMessage.getMessage(), i, 160).trim()) + "]]></textSMS></deliverContent></deliverDistributionRequest>");
                }
            } else {
                dnid = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
                response = "";
                dnid = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
                int responseCode = -3;
                new HttpClient();
                String mpg_response = "";
                PostMethod postMethod = new PostMethod("http://123.136.102.34:8080/Gatekeeper/GatekeeperServlet");
                String result = null;
                String signatureApi = "";
                String timeStamp = getTimeStamp();
                String X_DRUTT_MSGMT_PROVIDER_SIGNATURE = "P-R0Sr4BNJxH3;S-yQSr4BNJxH3;isentric123;" + timeStamp;
                MessageDigest m = MessageDigest.getInstance("MD5");
                m.update(X_DRUTT_MSGMT_PROVIDER_SIGNATURE.getBytes(), 0, X_DRUTT_MSGMT_PROVIDER_SIGNATURE.length());
                signature = (new BigInteger(1, m.digest())).toString(16);
                logger.info("X-DRUTT-MSGMT-TYPE: bulkSMS");
                logger.info("X-DRUTT-PROVIDER: P-R0Sr4BNJxH3");
                logger.info("X-DRUTT-SERVICE: S-yQSr4BNJxH3");
                logger.info("X-DRUTT-MSGMT-PROVIDER-SIGNATURE: " + signatureApi);
                logger.info("X-DRUTT-MSGMT-PROVIDER-TIME: " + timeStamp);
                logger.info("X-DRUTT-MSGMT-ACTION: DeliverContent");
                logger.info("X-DRUTT-PREMIUM-RESOURCE-TYPE: sms_bulk_0000");
                logger.info("Content-Type: text/xml;charset=UTF-8");
                logger.info("<?xml version=\"1.0\" encoding=\"UTF-8\"?><deliverDistributionRequest><distributionList>" + sms.getRecipient() + "</distributionList><deliverContent><textSMS><![CDATA[" + smsMessage.getMessage() + "]]></textSMS></deliverContent></deliverDistributionRequest>");
                postMethod.addRequestHeader("X-DRUTT-MSGMT-TYPE", "bulkSMS");
                postMethod.addRequestHeader("X-DRUTT-PROVIDER", "P-R0Sr4BNJxH3");
                postMethod.addRequestHeader("X-DRUTT-SERVICE", "S-yQSr4BNJxH3");
                postMethod.addRequestHeader("X-DRUTT-MSGMT-PROVIDER-SIGNATURE", signature);
                postMethod.addRequestHeader("X-DRUTT-MSGMT-PROVIDER-TIME", timeStamp);
                postMethod.addRequestHeader("X-DRUTT-MSGMT-ACTION", "DeliverContent");
                postMethod.addRequestHeader("X-DRUTT-PREMIUM-RESOURCE-TYPE", "sms_bulk_0000");
                postMethod.addRequestHeader("Content-Type", "text/xml;charset=UTF-8");
                postMethod.setRequestBody("<?xml version=\"1.0\" encoding=\"UTF-8\"?><deliverDistributionRequest><distributionList>" + sms.getRecipient() + "</distributionList><deliverContent><textSMS><![CDATA[" + smsMessage.getMessage() + "]]></textSMS></deliverContent></deliverDistributionRequest>");
            }
        } else if (smsMessage.getMessageType() == 6) {
            logger.info("=========================================CHINESE=========================================");
            logger.info("msisdn >>" + smsMessage.getRecipient());
            logger.info("keyword >>" + smsMessage.getKeyword());
            logger.info("ORI MSG >>" + smsMessage.getMessage());
            Decoder de = new Decoder();
            smsMessage.setMessage(this.checkChinesePriceTag(smsMessage.getMessage()));
            String message = smsMessage.getMessage();
            logger.info("APPENDED MSG " + smsMessage.getMessage());
            byte[] data = GsmHelper.decodeIA5(smsMessage.getMessage());
            sms.setUserData(data);
            sms.setCodingGroup((short) 240);
            sms.setAlphabet((short) 8);
            double messageLength = (double) smsMessage.getMessage().length();
            logger.info("messageLength >>" + messageLength);
            logger.info("1.\tsms getType =" + sms.getType());
            if (messageLength > (double) 252.0F) {
                logger.info("message > 252");
                String randomHex = generateRandomHexCode();
                nMessages = (short) ((int) Math.ceil(messageLength / cFlagChineseMaxLength));
                logger.info("2.\tnMessages =" + nMessages);

                for (int i = 0; i < nMessages; ++i) {
                    try {
                        String var60 = de.insertUtfIdentifier(message);
                        message = var60.replace("\\", "");
                        logger.info("after convert ::" + message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    System.out.print("message number count: " + (i + 1));
                    response = "";
                    dnid = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
                    int responseCode = -3;
                    new HttpClient();
                    String mpg_response = "";
                    PostMethod postMethod = new PostMethod("http://123.136.102.34:8080/Gatekeeper/GatekeeperServlet");
                    String result = null;
                    String signatureApi = "";
                    String timeStamp = getTimeStamp();
                    String X_DRUTT_MSGMT_PROVIDER_SIGNATURE = "P-R0Sr4BNJxH3;S-yQSr4BNJxH3;isentric123;" + timeStamp;
                    MessageDigest m = MessageDigest.getInstance("MD5");
                    m.update(X_DRUTT_MSGMT_PROVIDER_SIGNATURE.getBytes(), 0, X_DRUTT_MSGMT_PROVIDER_SIGNATURE.length());
                    signature = (new BigInteger(1, m.digest())).toString(16);
                    logger.info("X-DRUTT-MSGMT-TYPE: bulkSMS");
                    logger.info("X-DRUTT-PROVIDER: P-R0Sr4BNJxH3");
                    logger.info("X-DRUTT-SERVICE: S-yQSr4BNJxH3");
                    logger.info("X-DRUTT-MSGMT-PROVIDER-SIGNATURE: " + signatureApi);
                    logger.info("X-DRUTT-MSGMT-PROVIDER-TIME: " + timeStamp);
                    logger.info("X-DRUTT-MSGMT-ACTION: DeliverContent");
                    logger.info("X-DRUTT-PREMIUM-RESOURCE-TYPE: sms_bulk_0000");
                    logger.info("Content-Type: text/xml;charset=UTF-8");
                    logger.info("<?xml version=\"1.0\" encoding=\"UTF-8\"?><deliverDistributionRequest><distributionList>" + sms.getRecipient() + "</distributionList><deliverContent><textSMS><![CDATA[" + message + "]]></textSMS></deliverContent></deliverDistributionRequest>");
                    postMethod.addRequestHeader("X-DRUTT-MSGMT-TYPE", "bulkSMS");
                    postMethod.addRequestHeader("X-DRUTT-PROVIDER", "P-R0Sr4BNJxH3");
                    postMethod.addRequestHeader("X-DRUTT-SERVICE", "S-yQSr4BNJxH3");
                    postMethod.addRequestHeader("X-DRUTT-MSGMT-PROVIDER-SIGNATURE", signature);
                    postMethod.addRequestHeader("X-DRUTT-MSGMT-PROVIDER-TIME", timeStamp);
                    postMethod.addRequestHeader("X-DRUTT-MSGMT-ACTION", "DeliverContent");
                    postMethod.addRequestHeader("X-DRUTT-PREMIUM-RESOURCE-TYPE", "sms_bulk_0000");
                    postMethod.addRequestHeader("Content-Type", "text/xml;charset=UTF-8");
                    postMethod.addRequestHeader("X-DRUTT-MSGMT-ENCODING", "ISO-10646-UCS-2");
                    postMethod.setRequestBody("<?xml version=\"1.0\" encoding=\"UTF-8\"?><deliverDistributionRequest><distributionList>" + sms.getRecipient() + "</distributionList><deliverContent><textSMS><![CDATA[" + message + "]]></textSMS></deliverContent></deliverDistributionRequest>");
                }
            } else {
                try {
                    String var61 = de.insertUtfIdentifier(message);
                    message = var61.replace("\\", "");
                    logger.info("after convert ::" + message);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                dnid = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
                response = "";
                dnid = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
                int responseCode = -3;
                new HttpClient();
                String mpg_response = "";
                PostMethod postMethod = new PostMethod("http://123.136.102.34:8080/Gatekeeper/GatekeeperServlet");
                String result = null;
                String signatureApi = "";
                String timeStamp = getTimeStamp();
                String X_DRUTT_MSGMT_PROVIDER_SIGNATURE = "P-R0Sr4BNJxH3;S-yQSr4BNJxH3;isentric123;" + timeStamp;
                MessageDigest m = MessageDigest.getInstance("MD5");
                m.update(X_DRUTT_MSGMT_PROVIDER_SIGNATURE.getBytes(), 0, X_DRUTT_MSGMT_PROVIDER_SIGNATURE.length());
                signature = (new BigInteger(1, m.digest())).toString(16);
                logger.info("X-DRUTT-MSGMT-TYPE: bulkSMS");
                logger.info("X-DRUTT-PROVIDER: P-R0Sr4BNJxH3");
                logger.info("X-DRUTT-SERVICE: S-yQSr4BNJxH3");
                logger.info("X-DRUTT-MSGMT-PROVIDER-SIGNATURE: " + signatureApi);
                logger.info("X-DRUTT-MSGMT-PROVIDER-TIME: " + timeStamp);
                logger.info("X-DRUTT-MSGMT-ACTION: DeliverContent");
                logger.info("X-DRUTT-PREMIUM-RESOURCE-TYPE: sms_bulk_0000");
                logger.info("Content-Type: text/xml;charset=UTF-8");
                logger.info("<?xml version=\"1.0\" encoding=\"UTF-8\"?><deliverDistributionRequest><distributionList>" + sms.getRecipient() + "</distributionList><deliverContent><textSMS><![CDATA[" + message + "]]></textSMS></deliverContent></deliverDistributionRequest>");
                postMethod.addRequestHeader("X-DRUTT-MSGMT-TYPE", "bulkSMS");
                postMethod.addRequestHeader("X-DRUTT-PROVIDER", "P-R0Sr4BNJxH3");
                postMethod.addRequestHeader("X-DRUTT-SERVICE", "S-yQSr4BNJxH3");
                postMethod.addRequestHeader("X-DRUTT-MSGMT-PROVIDER-SIGNATURE", signature);
                postMethod.addRequestHeader("X-DRUTT-MSGMT-PROVIDER-TIME", timeStamp);
                postMethod.addRequestHeader("X-DRUTT-MSGMT-ACTION", "DeliverContent");
                postMethod.addRequestHeader("X-DRUTT-PREMIUM-RESOURCE-TYPE", "sms_bulk_0000");
                postMethod.addRequestHeader("Content-Type", "text/xml;charset=UTF-8");
                postMethod.addRequestHeader("X-DRUTT-MSGMT-ENCODING", "ISO-10646-UCS-2");
                postMethod.setRequestBody("<?xml version=\"1.0\" encoding=\"UTF-8\"?><deliverDistributionRequest><distributionList>" + sms.getRecipient() + "</distributionList><deliverContent><textSMS><![CDATA[" + message + "]]></textSMS></deliverContent></deliverDistributionRequest>");
            }
        }

    }

    public void sendHttpTuneTalk(String httpName, String credit, SMSMessageSmpp smsMessage) throws MessageException, SQLException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, MalformedURLException, IOException, SQLException {
        String sender = smsMessage.getSender();
        String recipient = smsMessage.getRecipient();
        short nMessages = 1;
        String cFlag = "" + smsMessage.getcFlag();
        EMSMessage sms = new EMSMessage();
        sms.requestStatusReport(true);
        sms.setSender(sender);
        sms.setRecipient(recipient);
        sms.setID(smsMessage.getGuid());
        sms.setProperty("SMPP_GUID", smsMessage.getGuid());
        sms.setProperty("SMPP_NAME", httpName);
        sms.setParent(sms);
        String url = "https://gw.cpa-tunetalk.com/tuneBulkMT/bulkmt.aspx";
        String dnid = "";
        int code = 0;
        if (cFlag.equalsIgnoreCase("1")) {
            logger.info("Concatenated Message");
        } else {
            logger.info("Normal Message");
        }

        if (smsMessage.getMessageType() == 0) {
            logger.info("=========================================normal text=========================================");
            smsMessage.setMessage(this.checkPriceTag(smsMessage.getMessage()));
            double messageLength = (double) smsMessage.getMessage().length();
            dnid = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
            StringBuffer URLBuffer = null;
            logger.info("[userID] Isentric");
            logger.info("[pass] Isentric2@18");
            logger.info("[shortcode] 66399");
            logger.info("[msisdn] " + sms.getRecipient());
            logger.info("[msgtype] 1");
            logger.info("[msg] " + nMessages);
            logger.info("[msgID] " + dnid);
            URLBuffer = new StringBuffer(url);
            URLBuffer.append("?userID=" + URLEncoder.encode("Isentric", "UTF-8"));
            URLBuffer.append("&pass=" + URLEncoder.encode("Isentric2@18", "UTF-8"));
            URLBuffer.append("&shortcode=" + URLEncoder.encode("66399", "UTF-8"));
            URLBuffer.append("&msisdn=" + URLEncoder.encode(sms.getRecipient(), "UTF-8"));
            URLBuffer.append("&msgtype=" + URLEncoder.encode("1", "UTF-8"));
            URLBuffer.append("&msg=" + URLEncoder.encode(smsMessage.getMessage(), "UTF-8"));
            this.performHTTPTuneTalk(URLBuffer.toString(), sms, smsMessage, httpName, dnid, sender, credit, 0);
        } else if (smsMessage.getMessageType() == 6) {
            logger.info("=========================================CHINESE=========================================");
            logger.info("msisdn >>" + smsMessage.getRecipient());
            logger.info("keyword >>" + smsMessage.getKeyword());
            logger.info("ORI MSG >>" + smsMessage.getMessage());
            smsMessage.setMessage(this.checkChinesePriceTag(smsMessage.getMessage()));
            String message = smsMessage.getMessage();
            logger.info("APPENDED MSG " + smsMessage.getMessage());
            byte[] data = GsmHelper.decodeIA5(smsMessage.getMessage());
            sms.setUserData(data);
            sms.setCodingGroup((short) 240);
            sms.setAlphabet((short) 8);
            logger.info("1.\tsms getType =" + sms.getType());
            dnid = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
            StringBuffer URLBuffer = null;
            logger.info("[userID] isentric");
            logger.info("[pass] isentric2@13");
            logger.info("[shortcode] 66399");
            logger.info("[msisdn] " + sms.getRecipient());
            logger.info("[msgtype] 3");
            logger.info("[msg] " + nMessages);
            logger.info("[msgID] " + dnid);
            URLBuffer = new StringBuffer(url);
            URLBuffer.append("?userID=" + URLEncoder.encode("isentric", "UTF-8"));
            URLBuffer.append("&pass=" + URLEncoder.encode("isentric2@13", "UTF-8"));
            URLBuffer.append("&shortcode=" + URLEncoder.encode("66399", "UTF-8"));
            URLBuffer.append("&msisdn=" + URLEncoder.encode(sms.getRecipient(), "UTF-8"));
            URLBuffer.append("&msgtype=" + URLEncoder.encode("3", "UTF-8"));
            URLBuffer.append("&msg=" + URLEncoder.encode(smsMessage.getMessage(), "UTF-8"));
            this.performHTTPTuneTalk(URLBuffer.toString(), sms, smsMessage, httpName, dnid, sender, credit, 0);
        }

    }

    public void sendHttpCardBoardFish(String httpName, String credit, SMSMessageSmpp smsMessage) throws MessageException, SQLException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, MalformedURLException, IOException, SQLException {
        System.out.println("################[Card Board Fish]##############################");
        String sender = smsMessage.getSender();
        String recipient = smsMessage.getRecipient();
        logger.info("sender id >>" + sender);
        short nMessages = 1;
        double cFlagMaxLength = (double) 153.0F;
        double cFlagChineseMaxLength = (double) 252.0F;
        new DESProcessor();
        String cFlag = "" + smsMessage.getcFlag();
        String refid = null;

        try {
            refid = UidUtil.generateUid();
        } catch (Exception e) {
            logger.error("Error generating UID", e);
        }

        EMSMessage sms = new EMSMessage();
        sms.requestStatusReport(true);
        sms.setSender(sender);
        sms.setRecipient(recipient);
        sms.setID(smsMessage.getGuid());
        sms.setProperty("SMPP_GUID", smsMessage.getGuid());
        sms.setProperty("SMPP_NAME", httpName);
        sms.setParent(sms);
        HttpObj httpSms = (HttpObj) binderHashtable.get(httpName);
        String response = "";
        String url = "";
        String dnid = "";
        int code = 0;
        new GetMethod();
        if (cFlag.equalsIgnoreCase("1")) {
            logger.info("Concatenated Message");
        } else {
            logger.info("Normal Message");
        }

        if (smsMessage.getMessageType() == 0) {
            smsMessage.setMessage(this.checkPriceTag(smsMessage.getMessage()));
            int messageLength = smsMessage.getMessage().length();
            System.out.println("");
            if (messageLength > 153 && !cFlag.equalsIgnoreCase("1")) {
                String randomHex = generateRandomHexCode();
                nMessages = (short) ((int) Math.ceil((double) messageLength / cFlagMaxLength));
                logger.info("messageLength >> " + messageLength);

                for (int i = 0; i < nMessages; ++i) {
                    System.out.print("message number count: " + (i + 1));
                    response = "";
                    dnid = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
                    url = httpSms.getURL();
                    url = url.replace("<msisdn>", sms.getRecipient());
                    url = url.replace("<msgType>", httpSms.getEnText());
                    url = url.replace("<refid>", refid);
                    url = url.replace("<sender>", URLEncoder.encode(sender, "UTF-8"));
                    url = url.replace("<msg>", URLEncoder.encode(getContentPart(this.checkPriceTag(smsMessage.getMessage()), i, 153).trim(), "UTF-8"));
                    logger.info("1. URL : " + url);
                    GetMethod var64 = new GetMethod(url);
                    this.performHTTPCardBoardFishRequest(var64, response, sms, smsMessage, httpName, dnid, sender, credit, i);
                }
            } else {
                dnid = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
                url = httpSms.getURL();
                url = url.replace("<msisdn>", sms.getRecipient());
                url = url.replace("<msgType>", httpSms.getEnText());
                url = url.replace("<refid>", refid);
                url = url.replace("<sender>", URLEncoder.encode(sender, "UTF-8"));
                url = url.replace("<msg>", URLEncoder.encode(this.checkPriceTag(smsMessage.getMessage()), "UTF-8"));
                GetMethod httpGet = new GetMethod(url);
                logger.debug("2. URL : " + url);
                logger.info("2. URL : " + url);

                try {
                    if (messageLength > 160 && cFlag.equalsIgnoreCase("1")) {
                        nMessages = (short) ((int) Math.ceil((double) messageLength / cFlagMaxLength));

                        for (int k = 1; k < nMessages; ++k) {
                            this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, dnid, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), k, credit);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                this.performHTTPCardBoardFishRequest(httpGet, response, sms, smsMessage, httpName, dnid, sender, credit, 0);
            }
        } else if (smsMessage.getMessageType() == 6) {
            new Decoder();
            smsMessage.setMessage(this.checkChinesePriceTag(smsMessage.getMessage()));
            String message = smsMessage.getMessage();
            byte[] data = GsmHelper.decodeIA5(smsMessage.getMessage());
            sms.setUserData(data);
            sms.setCodingGroup((short) 240);
            sms.setAlphabet((short) 8);
            int messageLength = smsMessage.getMessage().length();
            logger.info("chinese message length ::" + messageLength);
            if (messageLength > 252 && !cFlag.equalsIgnoreCase("1")) {
                String randomHex = generateRandomHexCode();
                nMessages = (short) ((int) Math.ceil((double) messageLength / cFlagChineseMaxLength));
                logger.info("messageLength >>" + messageLength);

                for (int i = 0; i < nMessages; ++i) {
                    System.out.print("message number count: " + (i + 1));
                    dnid = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
                    url = httpSms.getURL();
                    url = url.replace("<msisdn>", sms.getRecipient());
                    url = url.replace("<msgType>", httpSms.getChText());
                    url = url.replace("<refid>", refid);
                    url = url.replace("<sender>", URLEncoder.encode(sender, "UTF-8"));
                    url = url.replace("<msg>", URLEncoder.encode(getContentPart(this.checkChinesePriceTag(smsMessage.getMessage()), i, 256), "UTF-8"));
                    logger.debug("3. URL : " + url);
                    logger.info("3. URL : " + url);
                    GetMethod var66 = new GetMethod(url);
                    this.performHTTPCardBoardFishRequest(var66, response, sms, smsMessage, httpName, dnid, sender, credit, i);
                }
            } else {
                url = httpSms.getURL();
                url = url.replace("<msisdn>", sms.getRecipient());
                url = url.replace("<msgType>", httpSms.getChText());
                url = url.replace("<refid>", refid);
                url = url.replace("<sender>", URLEncoder.encode(sender, "UTF-8"));
                url = url.replace("<msg>", URLEncoder.encode(this.checkChinesePriceTag(smsMessage.getMessage()), "UTF-8"));
                GetMethod var65 = new GetMethod(url);
                logger.info("4. URL : " + url);

                try {
                    if (messageLength > 252 && cFlag.equalsIgnoreCase("1")) {
                        nMessages = (short) ((int) Math.ceil((double) messageLength / cFlagChineseMaxLength));

                        for (int k = 1; k < nMessages; ++k) {
                            System.out.println("insert into db >>" + k);
                            this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, dnid, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), k, credit);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                this.performHTTPCardBoardFishRequest(var65, response, sms, smsMessage, httpName, dnid, sender, credit, 0);
            }
        } else if (smsMessage.getMessageType() == 5) {
            logger.info("Picture Message not sent to " + smsMessage.getRecipient());
        } else if (smsMessage.getMessageType() == 4) {
            logger.info("Logo Message not sent to " + smsMessage.getRecipient());
        } else if (smsMessage.getMessageType() == 3) {
            logger.info("Ringtone Message not sent to " + smsMessage.getRecipient());
        } else if (smsMessage.getMessageType() == 1) {
            logger.info("Ringtone Message not sent to " + smsMessage.getRecipient());
        }

    }

    public void sendHttpFireMobile(String httpName, String credit, SMSMessageSmpp smsMessage) throws MessageException, SQLException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, MalformedURLException, IOException, SQLException {
        System.out.println("################[HTTP_FIREMOBILE]##############################");
        String sender = smsMessage.getSender();
        String recipient = smsMessage.getRecipient();
        logger.info("sender id >>" + sender);
        short nMessages = 1;
        double cFlagMaxLength = (double) 153.0F;
        double cFlagChineseMaxLength = (double) 252.0F;
        new DESProcessor();
        String cFlag = "" + smsMessage.getcFlag();
        String refid = null;

        try {
            refid = UidUtil.generateUid();
        } catch (Exception e) {
            logger.error("Error generating UID", e);
        }

        EMSMessage sms = new EMSMessage();
        sms.requestStatusReport(true);
        sms.setSender(sender);
        sms.setRecipient(recipient);
        sms.setID(smsMessage.getGuid());
        sms.setProperty("SMPP_GUID", smsMessage.getGuid());
        sms.setProperty("SMPP_NAME", httpName);
        sms.setParent(sms);
        HttpObj httpSms = (HttpObj) binderHashtable.get(httpName);
        String response = "";
        String url = "";
        String dnid = "";
        int code = 0;
        new GetMethod();
        if (cFlag.equalsIgnoreCase("1")) {
            logger.info("Concatenated Message");
        } else {
            logger.info("Normal Message");
        }

        if (smsMessage.getMessageType() == 0) {
            smsMessage.setMessage(this.checkPriceTag(smsMessage.getMessage()));
            int messageLength = smsMessage.getMessage().length();
            System.out.println("");
            if (messageLength > 153 && !cFlag.equalsIgnoreCase("1")) {
                String randomHex = generateRandomHexCode();
                nMessages = (short) ((int) Math.ceil((double) messageLength / cFlagMaxLength));
                logger.info("messageLength >> " + messageLength);

                for (int i = 0; i < nMessages; ++i) {
                    System.out.print("message number count: " + (i + 1));
                    response = "";
                    dnid = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
                    url = httpSms.getURL();
                    url = url.replace("<msisdn>", sms.getRecipient());
                    url = url.replace("<msgType>", "gw-text");
                    url = url.replace("<msg>", URLEncoder.encode(getContentPart(this.checkPriceTag(smsMessage.getMessage()), i, 153).trim(), "UTF-8"));
                    logger.info("1. URL : " + url);
                    GetMethod var58 = new GetMethod(url);
                    this.performHTTPFireMobileRequest(var58, response, sms, smsMessage, httpName, dnid, sender, credit, i);
                }
            } else {
                dnid = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
                url = httpSms.getURL();
                url = url.replace("<msisdn>", sms.getRecipient());
                url = url.replace("<msgType>", "gw-text");
                url = url.replace("<msg>", URLEncoder.encode(this.checkPriceTag(smsMessage.getMessage()), "UTF-8"));
                GetMethod httpGet = new GetMethod(url);
                logger.debug("2. URL : " + url);
                logger.info("2. URL : " + url);

                try {
                    if (messageLength > 160 && cFlag.equalsIgnoreCase("1")) {
                        nMessages = (short) ((int) Math.ceil((double) messageLength / cFlagMaxLength));

                        for (int k = 1; k < nMessages; ++k) {
                            this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, dnid, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), k, credit);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                this.performHTTPFireMobileRequest(httpGet, response, sms, smsMessage, httpName, dnid, sender, credit, 0);
            }
        } else if (smsMessage.getMessageType() == 6) {
            new Decoder();
            smsMessage.setMessage(this.checkChinesePriceTag(smsMessage.getMessage()));
            String message = smsMessage.getMessage();
            byte[] data = GsmHelper.decodeIA5(smsMessage.getMessage());
            sms.setUserData(data);
            sms.setCodingGroup((short) 240);
            sms.setAlphabet((short) 8);
            int messageLength = smsMessage.getMessage().length();
            logger.info("chinese message length ::" + messageLength);
            if (messageLength > 252 && !cFlag.equalsIgnoreCase("1")) {
                String randomHex = generateRandomHexCode();
                nMessages = (short) ((int) Math.ceil((double) messageLength / cFlagChineseMaxLength));
                logger.info("messageLength >>" + messageLength);

                for (int i = 0; i < nMessages; ++i) {
                    System.out.print("message number count: " + (i + 1));
                    dnid = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
                    url = httpSms.getURL();
                    url = url.replace("<msisdn>", sms.getRecipient());
                    url = url.replace("<msgType>", "gw-coding=3&gw-text");
                    url = url.replace("<msg>", URLEncoder.encode(getContentPart(this.checkChinesePriceTag(smsMessage.getMessage()), i, 256), "UTF-8"));
                    logger.debug("3. URL : " + url);
                    logger.info("3. URL : " + url);
                    GetMethod var60 = new GetMethod(url);
                    this.performHTTPFireMobileRequest(var60, response, sms, smsMessage, httpName, dnid, sender, credit, i);
                }
            } else {
                url = httpSms.getURL();
                url = url.replace("<msisdn>", sms.getRecipient());
                url = url.replace("<msgType>", "gw-coding=3&gw-text");
                url = url.replace("<sender>", URLEncoder.encode(sender, "UTF-8"));
                url = url.replace("<msg>", URLEncoder.encode(this.checkChinesePriceTag(smsMessage.getMessage()), "UTF-8"));
                GetMethod var59 = new GetMethod(url);
                logger.info("4. URL : " + url);

                try {
                    if (messageLength > 252 && cFlag.equalsIgnoreCase("1")) {
                        nMessages = (short) ((int) Math.ceil((double) messageLength / cFlagChineseMaxLength));

                        for (int k = 1; k < nMessages; ++k) {
                            System.out.println("insert into db >>" + k);
                            this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, dnid, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), k, credit);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                this.performHTTPFireMobileRequest(var59, response, sms, smsMessage, httpName, dnid, sender, credit, 0);
            }
        } else if (smsMessage.getMessageType() == 5) {
            logger.info("Picture Message not sent to " + smsMessage.getRecipient());
        } else if (smsMessage.getMessageType() == 4) {
            logger.info("Logo Message not sent to " + smsMessage.getRecipient());
        } else if (smsMessage.getMessageType() == 3) {
            logger.info("Ringtone Message not sent to " + smsMessage.getRecipient());
        } else if (smsMessage.getMessageType() == 1) {
            logger.info("Ringtone Message not sent to " + smsMessage.getRecipient());
        }

    }

    public void sendHttpTriomobile(String httpName, String credit, SMSMessageSmpp smsMessage) throws MessageException, SQLException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, MalformedURLException, IOException, SQLException {
        System.out.println("################[Trio Mobile]##############################");
        String sender = smsMessage.getSender();
        String recipient = smsMessage.getRecipient();
        short nMessages = 1;
        double cFlagMaxLength = (double) 153.0F;
        double cFlagChineseMaxLength = (double) 252.0F;
        new DESProcessor();
        String cFlag = "" + smsMessage.getcFlag();
        EMSMessage sms = new EMSMessage();
        sms.requestStatusReport(true);
        sms.setSender(sender);
        sms.setRecipient(recipient);
        sms.setID(smsMessage.getGuid());
        sms.setProperty("SMPP_GUID", smsMessage.getGuid());
        sms.setProperty("SMPP_NAME", httpName);
        sms.setParent(sms);
        HttpObj httpSms = (HttpObj) binderHashtable.get(httpName);
        String response = "";
        String url = "";
        String dnid = "";
        int code = 0;
        String apiKey = (String) binderHashtable.get(httpName + "#apiKey");
        new GetMethod();
        if (cFlag.equalsIgnoreCase("1")) {
            logger.info("Concatenated Message");
        } else {
            logger.info("Normal Message");
        }

        if (smsMessage.getMessageType() == 0) {
            smsMessage.setMessage(this.removePriceTag(smsMessage.getMessage()));
            smsMessage.setMessage(this.getSpecificPrefix(this.void_custid, "0", smsMessage.getMessage()) + smsMessage.getMessage());
            dnid = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
            url = httpSms.getURL();
            url = url.replace("<apiKey>", apiKey);
            url = url.replace("<msisdn>", sms.getRecipient());
            url = url.replace("<msgType>", httpSms.getEnText());
            url = url.replace("<msg>", URLEncoder.encode(smsMessage.getMessage(), "UTF-8"));
            GetMethod httpGet = new GetMethod(url);
            logger.debug("2. URL : " + url);
            logger.info("2. URL : " + url);
            this.performHTTPTriomobile(httpGet, sms, smsMessage, httpName, dnid, sender, credit, 0);
        } else if (smsMessage.getMessageType() == 6) {
            smsMessage.setMessage(this.removePriceTag(smsMessage.getMessage()));
            smsMessage.setMessage(this.getSpecificPrefix(this.void_custid, "0", smsMessage.getMessage()) + smsMessage.getMessage());
            dnid = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
            url = httpSms.getURL();
            url = url.replace("<apiKey>", apiKey);
            url = url.replace("<msisdn>", sms.getRecipient());
            url = url.replace("<msgType>", httpSms.getChText());
            url = url.replace("<msg>", URLEncoder.encode(smsMessage.getMessage(), "UTF-8"));
            smsMessage.setMessage(URLEncoder.encode(smsMessage.getMessage(), "UTF-8"));
            GetMethod var35 = new GetMethod(url);
            logger.debug("4. URL : " + url);
            logger.info("4. URL : " + url);
            this.performHTTPTriomobile(var35, sms, smsMessage, httpName, dnid, sender, credit, 0);
        } else if (smsMessage.getMessageType() == 5) {
            logger.info("Picture Message not sent to " + smsMessage.getRecipient());
        } else if (smsMessage.getMessageType() == 4) {
            logger.info("Logo Message not sent to " + smsMessage.getRecipient());
        } else if (smsMessage.getMessageType() == 3) {
            logger.info("Ringtone Message not sent to " + smsMessage.getRecipient());
        } else if (smsMessage.getMessageType() == 1) {
            logger.info("Ringtone Message not sent to " + smsMessage.getRecipient());
        }

    }

    public static String getTimeStamp() {
        Calendar cal = new GregorianCalendar(TimeZone.getDefault());
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        df.format(cal.getTime());
        SimpleDateFormat dfScheduleTime = new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss");
        String dtmScheduleTime = dfScheduleTime.format(cal.getTime());
        return dtmScheduleTime;
    }

    public void sendWsdl(String wsdlName, String credit, SMSMessageSmpp smsMessage) throws  InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException, SQLException {
        String sender = "";
        String recipient = validateDigiRecipient(smsMessage.getRecipient());
        short nMessages = 1;
        DESProcessor des = new DESProcessor();
        EMSMessage sms = new EMSMessage();
        sms.requestStatusReport(true);
        sms.setSender(sender);
        sms.setRecipient(recipient);
        sms.setID(smsMessage.getGuid());
        sms.setProperty("SMPP_GUID", smsMessage.getGuid());
        sms.setProperty("SMPP_NAME", wsdlName);
        sms.setParent(sms);
        WsdlObj wsdlSms = (WsdlObj) binderHashtable.get(wsdlName);
        String response = "";
        String url = "";
        String dnid = "";
        int code = 0;
        String transactionId = "";
        String datacoding = "3";
        String cFlag = "" + smsMessage.getcFlag();
        double maxLength = (double) 160.0F;
        double maxChineseLength = (double) 280.0F;
        if (cFlag.equalsIgnoreCase("1")) {
            logger.info("Digi Concatenated Message Feature Not Available");
            System.out.println("sendWsdl: cFlag=1 - Digi Concatenated Message Feature Not Available");
        } else {
            logger.info("Normal Message");
            System.out.println("sendWsdl: cFlag!=1 - Normal Message");
            System.out.println("");
        }
        System.out.println("smsMessage.getMessageType()" + smsMessage.getMessageType());
        URL objUrl = null;
        if (smsMessage.getMessageType() == 0) {
            smsMessage.setMessage(this.checkPriceTag(smsMessage.getMessage()));
            sms.setMessage(smsMessage.getMessage());
            int messageLength = smsMessage.getMessage().length();
            if (messageLength > 160) {
                String randomHex = generateRandomHexCode();
                nMessages = (short) ((int) Math.ceil((double) messageLength / maxLength));
                logger.debug("nMessages>>" + nMessages);
                url = wsdlSms.getURL();
                System.out.println("sendWsdl: (multipart) URL=" + url);
                System.out.println("");
                String password = wsdlSms.getPassword();
                String login_name = des.encrypt(wsdlSms.getLoginName(), password);
                String service_id = des.encrypt(wsdlSms.getServiceId(), password);
                String cp_id = wsdlSms.getCpId();
                String ref_id = "";
                String response_url = wsdlSms.getResponseURL();
                int notification_ind = wsdlSms.getNotificationId();
                AdditionalInfo[] array_of_info = null;
                SMSContent[] sms_contents = new SMSContent[nMessages + 1];
                String contentPart = "";
                String contentHeader = "";
                ArrayList message = new ArrayList();
                int contentMaxLength = Integer.parseInt(String.valueOf(maxLength).substring(0, String.valueOf(maxLength).length() - 2));

                for (int j = 0; j < nMessages; ++j) {
                    this.getContentHeaderDigi(sms.getType(), j + 1, nMessages);
                    this.getContentPartDigi(sms.getType(), sms.getMessage(), j, nMessages, contentMaxLength);
                    if (cFlag.equalsIgnoreCase("1")) {
                        message.add(getContentPart(smsMessage.getMessage(), j, contentMaxLength));
                    } else {
                        message.add(getContentPart(smsMessage.getMessage(), j, contentMaxLength));
                    }
                }

                for (int i = 0; i < nMessages; ++i) {
                    sms_contents[0] = new SMSContent();
                    sms_contents[0].setUcp_data_coding_id("0");
                    sms_contents[0].setUcp_msg_type(datacoding);
                    sms_contents[0].setUcp_msg_class("");
                    sms_contents[0].setContent(message.get(i).toString());
                    ref_id = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
                    this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), wsdlName, ref_id, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), i, credit);

                    try {
                        URL portAddress = new URL(url);
                        System.out.println("sendWsdl: invoking WSDL endpoint URL=" + url + " ref_id=" + ref_id);
                        System.out.println("");
                        SDPServices service = new SDPServicesLocator();
                        SDPServicesInterface port = service.getSDPServicesHttpPort(portAddress);
                        SDPResult result = port.smsBulk(login_name, service_id, cp_id, recipient, sender, ref_id, notification_ind, response_url, sms_contents, array_of_info);
                        if (cFlag.equalsIgnoreCase("1")) {
                            logger.info("=================================END MESSAGE_TYPE_TEXT[" + nMessages + "]END===============================");
                        }

                        code = result.getError_code();
                        if (!result.getTransaction_id().equals("")) {
                            transactionId = result.getTransaction_id();
                        }
                    } catch (ServiceException e) {
                        e.printStackTrace();
                    } catch (javax.xml.rpc.ServiceException e) {
                        throw new RuntimeException(e);
                    }
                }
            } else {
                url = wsdlSms.getURL();
                System.out.println("sendWsdl111: (single) URL=" + url);
                System.out.println("");
                String password = wsdlSms.getPassword();
                String login_name = des.encrypt(wsdlSms.getLoginName(), password);
                String service_id = des.encrypt(wsdlSms.getServiceId(), password);
                String cp_id = wsdlSms.getCpId();
                String ref_id = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
                String response_url = wsdlSms.getResponseURL();
                int notification_ind = wsdlSms.getNotificationId();
                SMSContent[] sms_contents = new SMSContent[1];
                sms_contents[0] = new SMSContent();
                sms_contents[0] = new SMSContent();
                sms_contents[0].setUcp_data_coding_id("");
                sms_contents[0].setUcp_msg_type("3");
                sms_contents[0].setUcp_msg_class("");
                String checkPriceTagMsg = smsMessage.getMessage();
                checkPriceTagMsg = this.checkPriceTag(checkPriceTagMsg);
                sms_contents[0].setContent(checkPriceTagMsg);
                AdditionalInfo[] array_of_info = null;
                MessageServiceDao messageServiceDao=new MessageServiceDao();
                messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), wsdlName, ref_id, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), 0, credit);

                try {
                    URL portAddress = new URL(url);
                    System.out.println("portAddress" +portAddress);
                    System.out.println("sendWsdl: invoking WSDL endpoint URL=" + url + " ref_id=" + ref_id);
                    System.out.println("");
                    SDPServices service = new SDPServicesLocator();
                    SDPServicesInterface port = service.getSDPServicesHttpPort(portAddress);
                    SDPResult result = port.smsBulk(login_name, service_id, cp_id, recipient, sender, ref_id, notification_ind, response_url, sms_contents, array_of_info);
                    code = result.getError_code();
                    System.out.println("result.getTransaction_id()"+result.getTransaction_id());
                    if (!result.getTransaction_id().equals("")) {
                        transactionId = result.getTransaction_id();
                    } else if (result.getTransaction_id().equalsIgnoreCase("")) {
                        boolean checkSkipAutoResends = this.checkSkipAutoResend(smsMessage.getKeyword());
                        if (checkSkipAutoResends) {
                            this.messageServiceDao.updateSmppSent(sms, "UNDELIVERED");
                        } else {
                            String smppName = "HTTP_INFOBIP";
                            try {
                                this.sendSmsMessageSmpp(smppName, credit, smsMessage);
                            } catch (InvalidKeyException e) {
                                e.printStackTrace();
                            } catch (NoSuchAlgorithmException e) {
                                e.printStackTrace();
                            } catch (InvalidKeySpecException e) {
                                e.printStackTrace();
                            } catch (NoSuchPaddingException e) {
                                e.printStackTrace();
                            } catch (IllegalBlockSizeException e) {
                                e.printStackTrace();
                            } catch (BadPaddingException e) {
                                e.printStackTrace();
                            } catch (MessageException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (ServiceException e) {
                    e.printStackTrace();
                } catch (javax.xml.rpc.ServiceException e) {
                    throw new RuntimeException(e);
                } catch (com.objectxp.msg.MessageException e) {
                    throw new RuntimeException(e);
                }
            }
        } else if (smsMessage.getMessageType() == 6) {
            logger.info("=========================================CHINESE=========================================");
            logger.info("msisdn >>" + smsMessage.getRecipient());
            logger.info("keyword >>" + smsMessage.getKeyword());
            logger.info("ORI MSG >>" + smsMessage.getMessage());
            smsMessage.setMessage(this.checkChinesePriceTag(smsMessage.getMessage()));
            logger.info("APPENDED MSG " + smsMessage.getMessage());
            byte[] data = GsmHelper.decodeIA5(smsMessage.getMessage());
            sms.setUserData(data);
            sms.setCodingGroup((short) 240);
            sms.setAlphabet((short) 8);
            int messageLength = smsMessage.getMessage().length();
            logger.info("messageLength >>" + messageLength);
            logger.info("1.\tsms getType =" + sms.getType());
            if (messageLength > 280) {
                logger.info("message > 280");
                String randomHex = generateRandomHexCode();
                nMessages = (short) ((int) Math.ceil((double) messageLength / maxChineseLength));
                logger.info("2.\tnMessages =" + nMessages);
                url = wsdlSms.getURL();
                String password = wsdlSms.getPassword();
                String login_name = des.encrypt(wsdlSms.getLoginName(), password);
                String service_id = des.encrypt(wsdlSms.getServiceId(), password);
                String cp_id = wsdlSms.getCpId();
                String ref_id = "";
                String response_url = wsdlSms.getResponseURL();
                int notification_ind = wsdlSms.getNotificationId();
                AdditionalInfo[] array_of_info = null;
                SMSContent[] sms_contents = new SMSContent[nMessages + 1];
                String contentPart = "";
                ArrayList message = new ArrayList();
                int contentMaxLength = Integer.parseInt(String.valueOf(maxChineseLength).substring(0, String.valueOf(maxChineseLength).length() - 2));
                logger.info("3.\tcontentMaxLength>>" + contentMaxLength);

                for (int j = 0; j < nMessages; ++j) {
                    contentPart = getContentPart(smsMessage.getMessage(), j, contentMaxLength);
                    if (cFlag.equalsIgnoreCase("1") && messageLength > 280) {
                        logger.info("else>>");
                        message.add(getContentPart(smsMessage.getMessage(), j, contentMaxLength));
                        logger.info("Normal Message Content" + contentPart.trim());
                    } else {
                        logger.info("else>>");
                        message.add(getContentPart(smsMessage.getMessage(), j, contentMaxLength));
                        logger.info("Normal Message Content" + contentPart.trim());
                    }
                }

                for (int i = 0; i < nMessages; ++i) {
                    logger.info("wsdl CN-" + nMessages);
                    sms_contents[0] = new SMSContent();
                    sms_contents[0].setUcp_data_coding_id("8");
                    sms_contents[0].setUcp_msg_type("4");
                    sms_contents[0].setUcp_msg_class("");
                    sms_contents[0].setContent(message.get(i).toString());
                    ref_id = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
                    this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), wsdlName, ref_id, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), i, credit);

                    try {
                        URL portAddress = new URL(url);
                        SDPServices service = new SDPServicesLocator();
                        SDPServicesInterface port = service.getSDPServicesHttpPort(portAddress);
                        SDPResult result = port.smsBulk(login_name, service_id, cp_id, recipient, sender, ref_id, notification_ind, response_url, sms_contents, array_of_info);
                        logger.info("sms_contents>>" + sms_contents.length);
                        logger.info("sms_contents>>" + sms_contents.length);
                        if (cFlag.equalsIgnoreCase("1")) {
                            logger.info("concatenated MESSAGE_TYPE_TEXT");
                            logger.info("======================MESSAGE_TYPE_TEXT[" + i + "]===============================");
                            logger.info("[" + nMessages + "]-sms_contents[0].getUcp_data_coding_id()>>" + sms_contents[0].getUcp_data_coding_id());
                            logger.info("[" + nMessages + "]-sms_contents[0].getUcp_msg_type()>>" + sms_contents[0].getUcp_msg_type());
                            logger.info("[" + nMessages + "]-sms_contents[0].getUcp_msg_class()>>" + sms_contents[0].getUcp_msg_class());
                            logger.info("[" + nMessages + "]-sms_contents[0].getContent()>>" + sms_contents[0].getContent());
                            logger.info("=================================END MESSAGE_TYPE_TEXT[" + nMessages + "]END===============================");
                        }

                        code = result.getError_code();
                        if (!result.getTransaction_id().equals("")) {
                            transactionId = result.getTransaction_id();
                        }
                    } catch (ServiceException | javax.xml.rpc.ServiceException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                url = wsdlSms.getURL();
                String password = wsdlSms.getPassword();
                String login_name = des.encrypt(wsdlSms.getLoginName(), password);
                String service_id = des.encrypt(wsdlSms.getServiceId(), password);
                String cp_id = wsdlSms.getCpId();
                String ref_id = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
                String response_url = wsdlSms.getResponseURL();
                int notification_ind = wsdlSms.getNotificationId();
                SMSContent[] sms_contents = new SMSContent[1];
                sms_contents[0] = new SMSContent();
                sms_contents[0] = new SMSContent();
                sms_contents[0].setUcp_data_coding_id("8");
                sms_contents[0].setUcp_msg_type("4");
                sms_contents[0].setUcp_msg_class("");
                String checkPriceTagMsg = smsMessage.getMessage();
                checkPriceTagMsg = this.checkChinesePriceTag(checkPriceTagMsg);
                sms_contents[0].setContent(checkPriceTagMsg);
                AdditionalInfo[] array_of_info = null;
                this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), wsdlName, ref_id, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), 0, credit);

                try {
                    URL portAddress = new URL(url);
                    SDPServices service = new SDPServicesLocator();
                    SDPServicesInterface port = service.getSDPServicesHttpPort(portAddress);
                    SDPResult result = port.smsBulk(login_name, service_id, cp_id, recipient, sender, ref_id, notification_ind, response_url, sms_contents, array_of_info);
                    code = result.getError_code();
                    if (!result.getTransaction_id().equals("")) {
                        transactionId = result.getTransaction_id();
                    }
                } catch (ServiceException | javax.xml.rpc.ServiceException e) {
                    e.printStackTrace();
                }
            }
        } else if (smsMessage.getMessageType() == 5) {
            logger.info("Picture Message not sent to " + smsMessage.getRecipient());
        } else if (smsMessage.getMessageType() == 4) {
            logger.info("Logo Message not sent to " + smsMessage.getRecipient());
        } else if (smsMessage.getMessageType() == 3) {
            logger.info("Ringtone Message not sent to " + smsMessage.getRecipient());
        } else if (smsMessage.getMessageType() == 1) {
            url = wsdlSms.getURL();
            String password = wsdlSms.getPassword();
            String login_name = des.encrypt(wsdlSms.getLoginName(), password);
            String service_id = des.encrypt(wsdlSms.getServiceId(), password);
            String cp_id = wsdlSms.getCpId();
            String ref_id = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
            String response_url = wsdlSms.getResponseURL();
            int notification_ind = wsdlSms.getNotificationId();
            SMSContent[] sms_contents = new SMSContent[1];
            sms_contents[0] = new SMSContent();
            sms_contents[0] = new SMSContent();
            sms_contents[0].setUcp_data_coding_id("8");
            sms_contents[0].setUcp_msg_type("4");
            sms_contents[0].setUcp_msg_class("");
            sms_contents[0].setContent(smsMessage.getMessage());
            AdditionalInfo[] array_of_info = null;
            this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), wsdlName, ref_id, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), 0, credit);

            try {
                URL portAddress = new URL(url);
                SDPServices service = new SDPServicesLocator();
                SDPServicesInterface port = service.getSDPServicesHttpPort(portAddress);
                SDPResult result = port.smsBulk(login_name, service_id, cp_id, recipient, sender, ref_id, notification_ind, response_url, sms_contents, array_of_info);
                code = result.getError_code();
                if (!result.getTransaction_id().equals("")) {
                    transactionId = result.getTransaction_id();
                }
            } catch (ServiceException e) {
                e.printStackTrace();
            } catch (javax.xml.rpc.ServiceException e) {
                throw new RuntimeException(e);
            }
        }

        logger.info("CUST ID : " + smsMessage.getKeyword() + " , code : " + code + " " + ", response : " + response);
        if (code != 1 && !response.equals("01010")) {
            this.messageServiceDao.updateSmppSent2(sms, "UNDELIVERED", transactionId);
        } else {
            this.messageServiceDao.updateSmppSent2(sms, "DELIVERED", transactionId);
        }

        logger.info("=========================================END sendWsdl=========================================");
    }

    public void sendChargeService(String chargeName, String credit, SMSMessageSmpp smsMessage) {
        try {
            String sender = validateDigiRecipient(smsMessage.getRecipient());
            DESProcessor des = new DESProcessor();
            EMSMessage sms = new EMSMessage();
            sms.requestStatusReport(true);
            sms.setSender(sender);
            sms.setRecipient(sender);
            sms.setID(smsMessage.getGuid());
            sms.setProperty("SMPP_GUID", smsMessage.getGuid());
            sms.setProperty("SMPP_NAME", chargeName);
            sms.setParent(sms);
            ChargeObj chargeSms = (ChargeObj) binderHashtable.get(chargeName);
            String response = "";
            String url = "";
            String dnid = "";
            int code = 0;
            url = chargeSms.getURL();
            String password = chargeSms.getPassword();
            String login_name = des.encrypt(chargeSms.getLoginName(), password);
            String service_id = des.encrypt(chargeSms.getServiceId(), password);
            String cp_id = chargeSms.getCpId();
            String ref_id = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
            String response_url = chargeSms.getResponseURL();
            int notification_ind = chargeSms.getNotificationId();
            String price_code = addDigiPriceTag(smsMessage.getPrice());
            String charge_party = chargeSms.getChargeParty();
            String keyword = chargeSms.getKeyword();
            String delivery_channel = chargeSms.getDeliveryChannel();
            String mtPrice = smsMessage.getPrice();
            String sub_id = "";
            int status = 1;
            String transactionId = "";
            this.messageServiceDao.insertSmppSent3((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), chargeName, ref_id, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), 0, credit, mtPrice);
            URL portAddress = new URL(url);
            SDPValidateBill service = new SDPValidateBillLocator();
            SDPValidateBillServicesInterface port = service.getSDPValidateBillServicesHttpPort(portAddress);
            SDPResult result = port.validate(login_name, service_id, cp_id, price_code, charge_party, sender, sender, sub_id, keyword, delivery_channel, status, ref_id,  null,  null);
            if (result != null) {
                int validate = result.getError_code();
                if (validate == 1) {
                    SDPResult billresult = port.bill(login_name, service_id, cp_id, sender, delivery_channel, result.getTransaction_id(), ref_id, null);
                    String dnStatus = "";
                    String errorList = "";
                    if (billresult.getError_code() == 1) {
                        dnStatus = "STATE_DELIVERED";
                    } else {
                        dnStatus = "STATE_UNDELIVERABLE";
                        errorList = billresult.getError_list();
                    }

                    if (!billresult.getTransaction_id().equals("")) {
                        transactionId = billresult.getTransaction_id();
                    }

                    this.messageServiceDao.insertSmppDn2("CHARGE_DIGI", URLDecoder.decode(ref_id), sender, sender, dnStatus, String.valueOf(billresult.getError_code()), errorList);
                    this.messageServiceDao.updateSmppSent2(sms, "DELIVERED", transactionId);
                } else {
                    this.messageServiceDao.insertSmppDn2("CHARGE_DIGI", URLDecoder.decode(ref_id), sender, sender, "STATE_UNDELIVERABLE", String.valueOf(result.getError_code()), result.getError_list());
                    this.messageServiceDao.updateSmppSent2(sms, "UNDELIVERED", transactionId);
                }
            } else {
                System.out.println("result null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public  void sendSmsMessageSmpp(String smppName, String credit, SMSMessageSmpp smsMessage) throws MessageException, SQLException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException, com.objectxp.msg.MessageException {
        this.void_custid = smsMessage.getKeyword();
        if (binderHashtable.get(smppName) instanceof GsmSmsService) {
           System.out.println("sendSmsMessageSmpp-1");
            this.sendGSM(smppName, credit, smsMessage);
        } else if (binderHashtable.get(smppName) instanceof SmsService) {
            System.out.println("sendSmsMessageSmpp-2");
            if (smppName.equalsIgnoreCase("SMPP_RED_PREMIO")) {
                this.sendRedPremioSmpp(smppName, credit, smsMessage);
            } else {
                this.sendSmpp(smppName, credit, smsMessage);
            }
        } else if (binderHashtable.get(smppName) instanceof HttpObj) {
            System.out.println("sendSmsMessageSmpp-3");
            if (!smppName.equalsIgnoreCase("HTTP_INFOBIP") && !smppName.equalsIgnoreCase("HTTP_INFOBIP_MULTROUTE") && !smppName.equalsIgnoreCase("HTTP_INFOBIP_INTERNATIONAL")) {
                if (smppName.equalsIgnoreCase("HTTP_ISENTRIC_MODEM")) {
                    this.sendHttpIsentricModem(smppName, credit, smsMessage);
                } else if (smppName.equalsIgnoreCase("HTTP_UMOBILE_MODEM")) {
                    this.sendHttpUmobileModem(smppName, credit, smsMessage);
                } else if (smppName.equalsIgnoreCase("HTTP_CELCOM")) {
                    this.sendHttpCelcom(smppName, credit, smsMessage);
                } else if (smppName.equalsIgnoreCase("HTTP_XOX")) {
                    this.sendHttpXOX(smppName, credit, smsMessage);
                } else if (smppName.equalsIgnoreCase("HTTP_UMOBILE")) {
                    this.sendHttpUmobile(smppName, credit, smsMessage);
                } else if (smppName.equalsIgnoreCase("HTTP_TUNETALK")) {
                    this.sendHttpTuneTalk(smppName, credit, smsMessage);
                } else if (smppName.equalsIgnoreCase("HTTP_UMOBILE_TWOWAY")) {
                    this.sendHttpUmobile2way(smppName, credit, smsMessage);
                } else if (smppName.equalsIgnoreCase("HTTP_CARD_BOARD_FISH")) {
                    this.sendHttpCardBoardFish(smppName, credit, smsMessage);
                } else if (smppName.equalsIgnoreCase("HTTP_TRIOMOBILE")) {
                    this.sendHttpTriomobile(smppName, credit, smsMessage);
                } else if (smppName.equalsIgnoreCase("HTTP_TRIOMOBILE_MODEM")) {
                    this.sendHttpTriomobile(smppName, credit, smsMessage);
                } else if (smppName.equalsIgnoreCase("HTTP_NEXMO")) {
                    this.sendHttpNexMO(smppName, credit, smsMessage);
                } else if (smppName.equalsIgnoreCase("HTTP_GINSMS")) {
                    this.sendHttpGINSMS(smppName, credit, smsMessage);
                } else if (smppName.equalsIgnoreCase("HTTP_FIREMOBILE")) {
                    this.sendHttpFireMobile(smppName, credit, smsMessage);
                } else if (smppName.equalsIgnoreCase("HTTP_WEBE")) {
                    this.sendHttpWebe(smppName, credit, smsMessage);
                } else if (smppName.equalsIgnoreCase("HTTP_TATA")) {
                    this.sendHttpTATA(smppName, credit, smsMessage);
                } else if (smppName.equalsIgnoreCase("HTTP_ICE")) {
                    this.sendHttpICE(smppName, credit, smsMessage);
                } else if (smppName.equalsIgnoreCase("HTTP_ICE_TAC")) {
                    this.sendHttpICETAC(smppName, credit, smsMessage);
                } else if (smppName.equalsIgnoreCase("HTTP_SOPRANO")) {
                    this.sendHttpSoprano(smppName, credit, smsMessage);
                } else if (smppName.equalsIgnoreCase("HTTP_SOPRANO_TAC")) {
                    this.sendHttpSopranoTAC(smppName, credit, smsMessage);
                } else {
                    this.sendHttp(smppName, credit, smsMessage);
                }
            } else {
                this.sendHttpInfobip(smppName, credit, smsMessage);
            }
        } else if (binderHashtable.get(smppName) instanceof WsdlObj) {
            System.out.println("sendSmsMessageSmpp-4");
            this.sendWsdl(smppName, credit, smsMessage);
        } else if (binderHashtable.get(smppName) instanceof ChargeObj) {
            System.out.println("sendSmsMessageSmpp-5");
            this.sendChargeService(smppName, credit, smsMessage);
        }

    }

    public void sendGSM(String smppName, String credit, SMSMessageSmpp smsMessage) throws MessageException, SQLException, com.objectxp.msg.MessageException {
        String sender = smsMessage.getSender();
        String recipient = smsMessage.getRecipient();
        if (smsMessage.getMessageType() == 0) {
            SmsMessage sms = new SmsMessage();
            sms.setRecipient("+" + recipient);
            sms.setMessage(smsMessage.getMessage());
            sms.requestStatusReport(true);
            sms.setProperty("SMPP_GUID", smsMessage.getGuid());

            try {
                this.wait(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            ((SmsService) binderHashtable.get(smppName)).sendMessage(sms);
        } else {
            logger.info("Message of Type : " + smsMessage.getMessageType());
            logger.info("Logo Message not sent to " + smsMessage.getRecipient());
        }

    }

    public boolean isInitialized(String smppName) {
        if (binderHashtable.get(smppName) instanceof SmsService) {
            SmsService smppSmsServiceBinder = (SmsService) binderHashtable.get(smppName);
            return smppSmsServiceBinder == null ? false : smppSmsServiceBinder.isInitialized();
        } else if (binderHashtable.get(smppName) instanceof HttpObj) {
            HttpObj HttpSmsServiceBinder = (HttpObj) binderHashtable.get(smppName);
            return HttpSmsServiceBinder != null;
        } else {
            return false;
        }
    }

    public boolean isConnected(String smppName) {
        if (binderHashtable.get(smppName) instanceof SmsService) {
            SmsService smppSmsServiceBinder = (SmsService) binderHashtable.get(smppName);
            return smppSmsServiceBinder == null ? false : ((SmsService) binderHashtable.get(smppName)).isConnected();
        } else if (binderHashtable.get(smppName) instanceof HttpObj) {
            HttpObj HttpSmsServiceBinder = (HttpObj) binderHashtable.get(smppName);
            return HttpSmsServiceBinder != null;
        } else if (binderHashtable.get(smppName) instanceof WsdlObj) {
            WsdlObj WsdlSmsServiceBinder = (WsdlObj) binderHashtable.get(smppName);
            return WsdlSmsServiceBinder != null;
        } else if (binderHashtable.get(smppName) instanceof ChargeObj) {
            ChargeObj ChargeServiceBinder = (ChargeObj) binderHashtable.get(smppName);
            return ChargeServiceBinder != null;
        } else {
            return false;
        }
    }

    public boolean isAlive(String smppName) {
        // Print detailed contents of binderHashtable for debugging
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("isAlive - binderHashtable entries:\n");
            for (Object keyObj : binderHashtable.keySet()) {
                try {
                    Object val = binderHashtable.get(keyObj);
                    sb.append("key=").append(String.valueOf(keyObj)).append(", type=");
                    if (val == null) {
                        sb.append("null");
                    } else {
                        sb.append(val.getClass().getSimpleName());
                        if (val instanceof SmsService) {
                            SmsService s = (SmsService) val;
                            try {
                                sb.append(", isAlive=").append(s.isAlive());
                                sb.append(", isConnected=").append(s.isConnected());
                                sb.append(", isInitialized=").append(s.isInitialized());
                            } catch (Throwable t) {
                                sb.append(", stateError=").append(t.getMessage());
                            }
                        }
                    }
                    sb.append("\n");
                } catch (Throwable te) {
                    sb.append("error reading entry: ").append(te.getMessage()).append("\n");
                }
            }
            System.out.println(sb.toString());
        } catch (Throwable ex) {
            System.out.println("isAlive: error printing binderHashtable: " + ex.getMessage());
        }
        if (binderHashtable.get(smppName) instanceof SmsService) {
            SmsService smppSmsServiceBinder = (SmsService) binderHashtable.get(smppName);
            return smppSmsServiceBinder == null ? false : ((SmsService) binderHashtable.get(smppName)).isAlive();
        } else if (binderHashtable.get(smppName) instanceof HttpObj) {
            HttpObj HttpSmsServiceBinder = (HttpObj) binderHashtable.get(smppName);
            return HttpSmsServiceBinder != null;
        } else if (binderHashtable.get(smppName) instanceof WsdlObj) {
            WsdlObj WsdlSmsServiceBinder = (WsdlObj) binderHashtable.get(smppName);
            return WsdlSmsServiceBinder != null;
        } else if (binderHashtable.get(smppName) instanceof ChargeObj) {
            ChargeObj ChargeServiceBinder = (ChargeObj) binderHashtable.get(smppName);
            return ChargeServiceBinder != null;
        } else {
            return false;
        }
    }

    public void setDestroy(String smppName) {
        if (binderHashtable.get(smppName) instanceof SmsService) {
            SmsService smppSmsServiceBinder = (SmsService) binderHashtable.get(smppName);

            try {
                smppSmsServiceBinder.disconnect();
            } catch (com.objectxp.msg.MessageException msgExp) {
                msgExp.printStackTrace();
                logger.fatal(msgExp);
            }
        }

    }

    public void updateDigiSession(String httpName, String session) {
        HttpObj http = (HttpObj) binderHashtable.get(httpName);
        http.setSessionID(session);
        binderHashtable.put(httpName, http);
    }

    private static String generateUniqueNumber() {
        double random = (double) 0.0F;
        String temp = "";
        random = Math.random() * (double) 10000.0F * (double) 10000.0F * (double) 10000.0F * (double) 100000.0F;
        Random generator = new Random((long) random + System.currentTimeMillis());
        int tempNum = generator.nextInt(10000000);
        if (tempNum < 10) {
            temp = "000000";
        } else if (tempNum < 100) {
            temp = "00000";
        } else if (tempNum < 1000) {
            temp = "0000";
        } else if (tempNum < 10000) {
            temp = "000";
        } else if (tempNum < 100000) {
            temp = "00";
        } else if (tempNum < 1000000) {
            temp = "0";
        } else {
            temp = "";
        }

        return temp + String.valueOf(tempNum);
    }

    private static String generateUniqueKey() {
        double random = (double) 0.0F;
        String temp = "";
        random = Math.random() * (double) 10000.0F * (double) 10000.0F * (double) 10000.0F * (double) 100000.0F;
        Random generator = new Random((long) random + System.currentTimeMillis());
        int tempNum = generator.nextInt(100000000);
        if (tempNum < 10) {
            temp = "0000000";
        } else if (tempNum < 100) {
            temp = "000000";
        } else if (tempNum < 1000) {
            temp = "00000";
        } else if (tempNum < 10000) {
            temp = "0000";
        } else if (tempNum < 100000) {
            temp = "000";
        } else if (tempNum < 1000000) {
            temp = "00";
        } else if (tempNum < 10000000) {
            temp = "0";
        } else {
            temp = "";
        }

        return temp + String.valueOf(tempNum);
    }

    private static String setTextHeader(int total, int partNumber, String random) {
        if (total < 10) {
            return "050003" + random + "0" + total + "0" + partNumber;
        } else {
            return partNumber < 10 ? "050003" + random + total + "0" + partNumber : "050003" + random + total + partNumber;
        }
    }

    private static String generateRandomHexCode() {
        String hex = "";

        for (int z = 0; z < 2; ++z) {
            Random ran = new Random();
            int a = ran.nextInt(16);
            hex = hex + Integer.toHexString(a);
        }

        logger.info("randomHex >>" + hex);
        return hex;
    }

    private static String getContentPart(String content, int partNumber, int totalBytes) {
        String contentPart = null;
        int firstIndex = partNumber * totalBytes;
        int followIndex = (partNumber + 1) * totalBytes;
        int messageLength = content.length();
        if (followIndex < messageLength) {
            contentPart = content.substring(firstIndex, followIndex);
        } else {
            contentPart = content.substring(firstIndex, messageLength);
        }

        return contentPart;
    }

    public static String encrypt(String data, String k) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        // SunJCE provider is built-in to modern Java, no need to explicitly add it
        // Security.addProvider(new SunJCE());
        byte[] key = new byte[k.getBytes().length];

        for (int i = 0; i < k.length(); ++i) {
            key[i] = (byte) k.charAt(i);
        }

        DESKeySpec spec = new DESKeySpec(key);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("DES");
        SecretKey secret = factory.generateSecret(spec);
        Cipher desCipher = Cipher.getInstance("DES");
        desCipher.init(1, secret);
        byte[] cleartext = data.getBytes();
        byte[] ciphertext = desCipher.doFinal(cleartext);
        String stringData = binaryToHexadecimal(ciphertext);
        return stringData;
    }

    public static String binaryToHexadecimal(byte[] bBinary) {
        StringBuffer sb = new StringBuffer();
        String temp = null;

        for (int i = 0; i < bBinary.length; ++i) {
            temp = Integer.toHexString(bBinary[i] & 255);
            sb = sb.append(temp.length() == 1 ? "0" + temp : temp);
        }

        return sb.toString().toUpperCase();
    }

    public static String validateDigiRecipient(String receipient) {
        String validNo = "";
        if (receipient.startsWith("60")) {
            validNo = receipient.substring(1, receipient.length());
        } else {
            validNo = receipient;
        }

        return validNo;
    }

    public static String addDigiPriceTag(String price) {
        String prefix = "VAS22";
        String digiPriceTag = "";
        if (price.length() == 3) {
            digiPriceTag = prefix + "0" + price;
        } else if (price.length() == 4) {
            digiPriceTag = prefix + price;
        }

        return digiPriceTag;
    }

    private void setDigiConnectionParams(HttpURLConnection con, HttpObj httpSms, EMSMessage sms, SMSMessageSmpp smsMessage, String dnid, int i, DESProcessor des, boolean multipart, boolean unicode) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        con.setRequestProperty("cpa_cp_id", "136");
        con.setRequestProperty("cpa_short_code", "66399");
        con.setRequestProperty("cpa_service_id", "BSMS_IST_66399");
        con.setRequestProperty("cpa_cp_session_id", URLEncoder.encode(httpSms.getSessionID()));
        con.setRequestProperty("cpa_cp_response_url", URLEncoder.encode("http://192.168.26.163:8001/ExtMTPush/DigiDN"));
        con.setRequestProperty("cpa_delivery_channel", "SMS");
        con.setRequestProperty("cpa_destination_mobtel", sms.getRecipient().substring(1));
        con.setRequestProperty("cpa_source_mobtel", sms.getSender());
        con.setRequestProperty("cpa_status", "1");
        con.setRequestProperty("cpa_error_code", "1");
        if (multipart) {
            con.setRequestProperty("cpa_content", URLEncoder.encode(getContentPart(smsMessage.getMessage(), i, 153)));
        } else {
            con.setRequestProperty("cpa_content", URLEncoder.encode(smsMessage.getMessage()));
        }

        con.setRequestProperty("cpa_prepaid_price_code", URLEncoder.encode(des.encrypt("VAS220000", "isentric88")));
        con.setRequestProperty("cpa_postpaid_price_code", URLEncoder.encode(des.encrypt("VAS220000", "isentric88")));
        con.setRequestProperty("cpa_cp_dnres_required", URLEncoder.encode("Y"));
        con.setRequestProperty("cpa_cp_mtres_required", "Y");
        if (unicode) {
            con.setRequestProperty("cpa_sms_datacoding_id", URLEncoder.encode("8"));
            con.setRequestProperty("cpa_ucp_message_type", URLEncoder.encode("4"));
        }

        con.setRequestProperty("cpa_cp_ref_id", URLEncoder.encode(dnid));
    }

    public Class[] value() {
        return null;
    }

    public Class<? extends Annotation> annotationType() {
        return null;
    }

    public int performHTTPRequest(GetMethod httpGet, String response, EMSMessage sms, SMSMessageSmpp smsMessage, String httpName, String dnid, String sender, String credit, int msgSequence) throws IOException, ProtocolException, SQLException, HttpException {
        String line = "";
        int retCode = 0;
        BufferedReader buff = null;
        String oriCust = smsMessage.getKeyword().replace("extmt_", "").trim();
        HttpClient httpclient = new HttpClient();

        try {
            MaxisDNStatusManager maxisCache = MaxisDNStatusManager.getInstance();
            System.out.println("oriCust>>" + oriCust);
            System.out.println("httpGet>>" + httpGet);
            retCode = httpclient.executeMethod(httpGet);
            System.out.println("retCode>>" + retCode);
            if (retCode != 200) {
                System.err.println("Method failed: " + httpGet.getStatusLine());
            }
            System.out.println(retCode);
            InputStream rstream = null;
            rstream = httpGet.getResponseBodyAsStream();
            buff = new BufferedReader(new InputStreamReader(rstream));

            for (int a = 0; (line = buff.readLine()) != null; ++a) {
                response = response + line;
                if (a > 20000) {
                    break;
                }
            }

            logger.info("CUST ID >>>" + smsMessage.getKeyword() + ", Response Code >>>" + retCode);
            logger.info("CUST ID >>>" + smsMessage.getKeyword() + ", Response >>>" + response);
            buff.close();
            httpGet.releaseConnection();
            this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, dnid, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), msgSequence, credit);
            if (retCode == 200) {
                if (!response.equals("")) {
                    if (response.startsWith("01010,")) {
                        response = response.replace("01010,", "").trim();
                        this.messageServiceDao.updateSmppSent(sms, "DELIVERED");
                        this.messageServiceDao.updateSmppId(sms, response);
                    } else if (!response.equals("01010") && !response.equals("10") && !response.equals("100-success")) {
                        if (response.equals("01012")) {
                            this.messageServiceDao.updateSmppSent(sms, "UNDELIVERED");
                            if (smsMessage.getTelco().equals("maxis")) {
                                logger.info("TELCO NO CREDIT : " + smsMessage.getTelco() + " >>>" + response);
                                this.messageServiceDao.updateTelcoRoute();

                                try {
                                    PrefixManager cache = PrefixManager.getInstance();
                                    cache.resetPrefixVObj(smsMessage.getTelco(), response.toString());
                                } catch (CacheException e) {
                                    e.printStackTrace();
                                } catch (org.apache.jcs.access.exception.CacheException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        } else if (response == "01010" && response == "01012" && response == "10" && response == "100-success") {
                            boolean checkSkipAutoResends = this.checkSkipAutoResend(smsMessage.getKeyword());
                            if (checkSkipAutoResends) {
                                this.messageServiceDao.updateSmppSent(sms, "UNDELIVERED");
                            } else {
                                String smppName = "HTTP_INFOBIP";

                                try {
                                    this.sendSmsMessageSmpp(smppName, credit, smsMessage);
                                } catch (InvalidKeyException e) {
                                    e.printStackTrace();
                                } catch (NoSuchAlgorithmException e) {
                                    e.printStackTrace();
                                } catch (InvalidKeySpecException e) {
                                    e.printStackTrace();
                                } catch (NoSuchPaddingException e) {
                                    e.printStackTrace();
                                } catch (IllegalBlockSizeException e) {
                                    e.printStackTrace();
                                } catch (BadPaddingException e) {
                                    e.printStackTrace();
                                } catch (MessageException e) {
                                    e.printStackTrace();
                                } catch (com.objectxp.msg.MessageException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        } else {
                            boolean checkSkipAutoResends = this.checkSkipAutoResend(smsMessage.getKeyword());
                            if (checkSkipAutoResends) {
                                this.messageServiceDao.updateSmppSent(sms, "UNDELIVERED");
                            } else {
                                String smppName = "HTTP_INFOBIP";

                                try {
                                    this.sendSmsMessageSmpp(smppName, credit, smsMessage);
                                } catch (InvalidKeyException e) {
                                    e.printStackTrace();
                                } catch (NoSuchAlgorithmException e) {
                                    e.printStackTrace();
                                } catch (InvalidKeySpecException e) {
                                    e.printStackTrace();
                                } catch (NoSuchPaddingException e) {
                                    e.printStackTrace();
                                } catch (IllegalBlockSizeException e) {
                                    e.printStackTrace();
                                } catch (BadPaddingException e) {
                                    e.printStackTrace();
                                } catch (MessageException e) {
                                    e.printStackTrace();
                                } catch (com.objectxp.msg.MessageException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    } else {
                        this.messageServiceDao.updateSmppSent(sms, "DELIVERED");
                    }
                } else if (httpName.equalsIgnoreCase("HTTP_XOX")) {
                    boolean checkSkipAutoResends = this.checkSkipAutoResend(smsMessage.getKeyword());
                    if (checkSkipAutoResends) {
                        this.messageServiceDao.updateSmppSent(sms, "UNDELIVERED");
                    } else {
                        String smppName = "HTTP_INFOBIP";

                        try {
                            this.sendSmsMessageSmpp(smppName, credit, smsMessage);
                        } catch (InvalidKeyException e) {
                            e.printStackTrace();
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        } catch (InvalidKeySpecException e) {
                            e.printStackTrace();
                        } catch (NoSuchPaddingException e) {
                            e.printStackTrace();
                        } catch (IllegalBlockSizeException e) {
                            e.printStackTrace();
                        } catch (BadPaddingException e) {
                            e.printStackTrace();
                        } catch (MessageException e) {
                            e.printStackTrace();
                        } catch (com.objectxp.msg.MessageException e) {
                            throw new RuntimeException(e);
                        }
                    }
                } else {
                    this.messageServiceDao.updateSmppSent(sms, "DELIVERED");
                }
            } else {
                boolean checkSkipAutoResends = this.checkSkipAutoResend(smsMessage.getKeyword());
                if (checkSkipAutoResends) {
                    this.messageServiceDao.updateSmppSent(sms, "UNDELIVERED");
                } else {
                    String smppName = "HTTP_INFOBIP";

                    try {
                        this.sendSmsMessageSmpp(smppName, credit, smsMessage);
                    } catch (InvalidKeyException e) {
                        e.printStackTrace();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (InvalidKeySpecException e) {
                        e.printStackTrace();
                    } catch (NoSuchPaddingException e) {
                        e.printStackTrace();
                    } catch (IllegalBlockSizeException e) {
                        e.printStackTrace();
                    } catch (BadPaddingException e) {
                        e.printStackTrace();
                    } catch (MessageException | com.objectxp.msg.MessageException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (SSLException sslex) {
            sslex.printStackTrace();
            logger.fatal(sslex);

            try {
                JavaSSLCertificate.retImportCert(httpGet.getURI().getHost(), 443);
                ArrayList<extMTObject> customerList = this.messageServiceDao.queryCustInfo("mruncittest");
                logger.info("customerList size ::" + customerList.size());
                String[] msisdnArr = new String[]{"60122926888", "60122555363", "60172818748", "60139410699", "60163911245", "60179258399"};

                for (int i = 0; i < customerList.size(); ++i) {
                    for (int k = 0; k < msisdnArr.length; ++k) {
                        String shortcode = ((extMTObject) customerList.get(i)).getShortcode();
                        String custid = ((extMTObject) customerList.get(i)).getCustId();
                        String rmsisdn = msisdnArr[k].toString();
                        String smsisdn = ((extMTObject) customerList.get(i)).getSMsisdn();
                        String mtid = String.valueOf(System.currentTimeMillis());
                        String mtprice = "000";
                        String productType = "4";
                        String productCode = "";
                        String keyword = "mruncittest";
                        String dataEncoding = "0";
                        String dataStr = httpGet.getURI().getHost() + " cert expired and renewed. Please restart bulk gateway to take effect.";
                        String dataUrl = "";
                        String dnRep = "0";
                        String groupTag = "10";
                        String urlTitle = "";
                        String IP = ((extMTObject) customerList.get(i)).getCpip();
                        String ewigFlag = "0";
                        Thread.sleep(2000L);
                        URL portAddress = new URL("http://203.223.130.115:8001/ExtMTPush/services/ExternalMTPushInterface?wsdl");
                        ExternalMTPushInterfaceService service = new ExternalMTPushInterfaceServiceLocator();
                        ExternalMTPushInterface port = service.getExternalMTPushInterface(portAddress);
                        logger.info("--------------------- START WEB SERVICE-----------------------------------");
                        int returnCode = port.receiveExtMTPush(shortcode, custid, rmsisdn, smsisdn, mtid, mtprice, Integer.parseInt(productType), productCode, keyword, Integer.parseInt(dataEncoding), dataStr, dataUrl, Integer.parseInt(dnRep), groupTag, IP, urlTitle, ewigFlag);
                        logger.info("Insert MTPush result for mtid " + mtid + " returnCode = " + returnCode);
                        logger.info("--------------------- END WEB SERVICE-----------------------------------");
                    }
                }
            } catch (URIException uriex) {
                logger.error("Error: sending sms to extmt");
                uriex.printStackTrace();
                logger.fatal(uriex);
            } catch (Exception e) {
                e.printStackTrace();
                logger.fatal(e);
            }
        } catch (HttpException httpExp) {
            httpExp.printStackTrace();
            logger.fatal(httpExp);
            throw httpExp;
        } catch (ProtocolException pExp) {
            pExp.printStackTrace();
            logger.fatal(pExp);
            throw pExp;
        } catch (IOException ioExp) {
            ioExp.printStackTrace();
            logger.fatal(ioExp);
            throw ioExp;
        } catch (SQLException sqlE) {
            sqlE.printStackTrace();
            logger.fatal(sqlE);
            throw sqlE;
        } catch (CacheException e1) {
            e1.printStackTrace();
        } finally {
            if (buff != null) {
                buff.close();
            }

            if (httpGet != null) {
                httpGet.releaseConnection();
            }

        }

        return retCode;
    }

    public int performHTTPUmobile(String url, EMSMessage sms, SMSMessageSmpp smsMessage, String httpName, String dnid, String sender, String credit, int msgSequence) throws IOException, ProtocolException, SQLException, HttpException {
        int responseCode = -3;
        HttpClient client = new HttpClient();
        String mpg_response = "";
        String result = null;
        String statusCode = "-3";

        try {
            try {
                GetMethod getMethod = new GetMethod(url);
                client.executeMethod(getMethod);
                mpg_response = getMethod.getResponseBodyAsString();
            } catch (HttpException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (result == null) {
                result = "-3";
            }

            String[] arrResponse = mpg_response.split(",");
            String msgID = "";
            if (arrResponse.length > 2) {
                String msisdn = mpg_response.split(",")[0];
                msgID = mpg_response.split(",")[1];
                statusCode = mpg_response.split(",")[2];
            } else {
                statusCode = mpg_response;
            }

            logger.info("statusCode---" + statusCode);

            try {
                Integer.parseInt(statusCode);
            } catch (NumberFormatException var40) {
                statusCode = "-3";
            }

            int messageLength = smsMessage.getMessage().length();
            String dnid2 = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
            String dnid3 = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
            String dnid4 = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
            String dnid5 = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
            String dnid6 = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
            String dnid7 = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
            if (messageLength < 161) {
                this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, msgID, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), msgSequence, credit);
            } else if (messageLength > 160 && messageLength < 320) {
                this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, msgID, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), msgSequence, credit);
                this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, dnid2, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), 1, credit);
            } else if (messageLength > 320 && messageLength < 480) {
                this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, msgID, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), msgSequence, credit);
                this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, dnid2, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), 1, credit);
                this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, dnid3, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), 2, credit);
            } else if (messageLength > 480 && messageLength < 640) {
                this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, msgID, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), msgSequence, credit);
                this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, dnid2, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), 1, credit);
                this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, dnid3, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), 2, credit);
                this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, dnid4, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), 3, credit);
            } else if (messageLength > 640 && messageLength < 800) {
                this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, msgID, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), msgSequence, credit);
                this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, dnid2, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), 1, credit);
                this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, dnid3, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), 2, credit);
                this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, dnid4, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), 3, credit);
                this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, dnid5, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), 4, credit);
            } else if (messageLength > 800 && messageLength < 960) {
                this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, msgID, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), msgSequence, credit);
                this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, dnid2, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), 1, credit);
                this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, dnid3, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), 2, credit);
                this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, dnid4, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), 3, credit);
                this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, dnid5, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), 4, credit);
                this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, dnid6, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), 5, credit);
            } else if (messageLength > 960 && messageLength < 1120) {
                this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, msgID, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), msgSequence, credit);
                this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, dnid2, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), 1, credit);
                this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, dnid3, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), 2, credit);
                this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, dnid4, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), 3, credit);
                this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, dnid5, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), 4, credit);
                this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, dnid6, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), 5, credit);
                this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, dnid7, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), 6, credit);
            }

            if (Integer.parseInt(statusCode) == 100) {
                this.messageServiceDao.updateSmppSent(sms, "DELIVERED");
            } else if (Integer.parseInt(statusCode) != 100) {
                boolean checkSkipAutoResends = this.checkSkipAutoResend(smsMessage.getKeyword());
                if (checkSkipAutoResends) {
                    this.messageServiceDao.updateSmppSent(sms, "UNDELIVERED");
                } else {
                    String smppName = "HTTP_INFOBIP";

                    try {
                        this.sendSmsMessageSmpp(smppName, credit, smsMessage);
                    } catch (InvalidKeyException e) {
                        e.printStackTrace();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (InvalidKeySpecException e) {
                        e.printStackTrace();
                    } catch (NoSuchPaddingException e) {
                        e.printStackTrace();
                    } catch (IllegalBlockSizeException e) {
                        e.printStackTrace();
                    } catch (BadPaddingException e) {
                        e.printStackTrace();
                    } catch (MessageException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                boolean checkSkipAutoResends = this.checkSkipAutoResend(smsMessage.getKeyword());
                if (checkSkipAutoResends) {
                    this.messageServiceDao.updateSmppSent(sms, "UNDELIVERED");
                } else {
                    String smppName = "HTTP_INFOBIP";

                    try {
                        this.sendSmsMessageSmpp(smppName, credit, smsMessage);
                    } catch (InvalidKeyException e) {
                        e.printStackTrace();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (InvalidKeySpecException e) {
                        e.printStackTrace();
                    } catch (NoSuchPaddingException e) {
                        e.printStackTrace();
                    } catch (IllegalBlockSizeException e) {
                        e.printStackTrace();
                    } catch (BadPaddingException e) {
                        e.printStackTrace();
                    } catch (MessageException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return Integer.parseInt(statusCode);
    }

    public int performHTTPTuneTalk(String url, EMSMessage sms, SMSMessageSmpp smsMessage, String httpName, String dnid, String sender, String credit, int msgSequence) throws IOException, ProtocolException, SQLException, HttpException {
        int responseCode = -3;
        HttpClient client = new HttpClient();
        String mpg_response = "";
        String result = null;
        String statusCode = "-3";

        try {
            try {
                GetMethod getMethod = new GetMethod(url);
                client.executeMethod(getMethod);
                mpg_response = getMethod.getResponseBodyAsString();
            } catch (HttpException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (result == null) {
                result = "-3";
            }

            String[] arrResponse = mpg_response.split(",");
            String msgID = "";
            if (arrResponse.length > 2) {
                String msisdn = mpg_response.split(",")[0];
                msgID = mpg_response.split(",")[1];
                statusCode = mpg_response.split(",")[2];
            } else {
                statusCode = mpg_response;
            }

            logger.info("statusCode---" + statusCode);

            try {
                Integer.parseInt(statusCode);
            } catch (NumberFormatException var40) {
                statusCode = "-3";
            }

            int messageLength = smsMessage.getMessage().length();
            String dnid2 = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
            String dnid3 = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
            String dnid4 = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
            String dnid5 = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
            String dnid6 = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
            String dnid7 = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
            if (messageLength < 161) {
                this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, msgID, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), msgSequence, credit);
            } else if (messageLength > 160 && messageLength < 320) {
                this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, msgID, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), msgSequence, credit);
                this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, dnid2, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), 1, credit);
            } else if (messageLength > 320 && messageLength < 480) {
                this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, msgID, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), msgSequence, credit);
                this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, dnid2, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), 1, credit);
                this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, dnid3, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), 2, credit);
            } else if (messageLength > 480 && messageLength < 640) {
                this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, msgID, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), msgSequence, credit);
                this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, dnid2, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), 1, credit);
                this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, dnid3, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), 2, credit);
                this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, dnid4, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), 3, credit);
            } else if (messageLength > 640 && messageLength < 800) {
                this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, msgID, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), msgSequence, credit);
                this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, dnid2, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), 1, credit);
                this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, dnid3, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), 2, credit);
                this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, dnid4, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), 3, credit);
                this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, dnid5, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), 4, credit);
            } else if (messageLength > 800 && messageLength < 960) {
                this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, msgID, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), msgSequence, credit);
                this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, dnid2, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), 1, credit);
                this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, dnid3, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), 2, credit);
                this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, dnid4, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), 3, credit);
                this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, dnid5, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), 4, credit);
                this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, dnid6, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), 5, credit);
            } else if (messageLength > 960 && messageLength < 1120) {
                this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, msgID, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), msgSequence, credit);
                this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, dnid2, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), 1, credit);
                this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, dnid3, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), 2, credit);
                this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, dnid4, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), 3, credit);
                this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, dnid5, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), 4, credit);
                this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, dnid6, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), 5, credit);
                this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, dnid7, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), 6, credit);
            }

            if (Integer.parseInt(statusCode) == 100) {
                this.messageServiceDao.updateSmppSent(sms, "DELIVERED");
            } else if (Integer.parseInt(statusCode) != 100) {
                boolean checkSkipAutoResends = this.checkSkipAutoResend(smsMessage.getKeyword());
                if (checkSkipAutoResends) {
                    this.messageServiceDao.updateSmppSent(sms, "UNDELIVERED");
                } else {
                    String smppName = "HTTP_INFOBIP";

                    try {
                        this.sendSmsMessageSmpp(smppName, credit, smsMessage);
                    } catch (InvalidKeyException e) {
                        e.printStackTrace();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (InvalidKeySpecException e) {
                        e.printStackTrace();
                    } catch (NoSuchPaddingException e) {
                        e.printStackTrace();
                    } catch (IllegalBlockSizeException e) {
                        e.printStackTrace();
                    } catch (BadPaddingException e) {
                        e.printStackTrace();
                    } catch (MessageException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                boolean checkSkipAutoResends = this.checkSkipAutoResend(smsMessage.getKeyword());
                if (checkSkipAutoResends) {
                    this.messageServiceDao.updateSmppSent(sms, "UNDELIVERED");
                } else {
                    String smppName = "HTTP_INFOBIP";

                    try {
                        this.sendSmsMessageSmpp(smppName, credit, smsMessage);
                    } catch (InvalidKeyException e) {
                        e.printStackTrace();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (InvalidKeySpecException e) {
                        e.printStackTrace();
                    } catch (NoSuchPaddingException e) {
                        e.printStackTrace();
                    } catch (IllegalBlockSizeException e) {
                        e.printStackTrace();
                    } catch (BadPaddingException e) {
                        e.printStackTrace();
                    } catch (MessageException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return Integer.parseInt(statusCode);
    }

    public int performHTTPCelcomRequest(GetMethod httpGet, String response, EMSMessage sms, SMSMessageSmpp smsMessage, String httpName, String dnid, String sender, String credit, int msgSequence) throws IOException, ProtocolException, SQLException, HttpException {
        String line = "";
        int retCode = 0;
        BufferedReader buff = null;
        String celcom_smp_txid = null;
        String errorcode = null;
        HttpClient httpclient = new HttpClient();

        try {
            retCode = httpclient.executeMethod(httpGet);
            if (retCode != 200) {
                System.err.println("Method failed: " + httpGet.getStatusLine());
            }

            InputStream rstream = null;
            rstream = httpGet.getResponseBodyAsStream();
            buff = new BufferedReader(new InputStreamReader(rstream));

            for (int a = 0; (line = buff.readLine()) != null; ++a) {
                response = response + line;
                if (a > 20000) {
                    break;
                }
            }

            logger.info("CUST ID >>>" + smsMessage.getKeyword() + ", Response Code >>>" + retCode);
            logger.info("CUST ID >>>" + smsMessage.getKeyword() + ", Response >>>" + response);
            Header smpTxidHeader = httpGet.getResponseHeader("SMP_Txid");
            Header errorCodeHeader = httpGet.getResponseHeader("Errorcode");
            logger.info("####### START CELCOM ##################");
            if (smpTxidHeader != null) {
                celcom_smp_txid = smpTxidHeader.getValue();
                logger.info("celcom_smp_txid >>" + celcom_smp_txid);
                dnid = celcom_smp_txid;
                this.messageServiceDao.updateSmppId(sms, celcom_smp_txid);
            }

            logger.info("######### END CELCOM ##################");
            buff.close();
            httpGet.releaseConnection();
            this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, dnid, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), msgSequence, credit);
            if (retCode == 200) {
                if (response.indexOf("200") == -1) {
                    logger.info(sms.getRecipient() + "message is UNDELIVERED");
                    this.messageServiceDao.updateSmppSent(sms, "UNDELIVERED");
                } else {
                    logger.info(sms.getRecipient() + "message is DELIVERED");
                    this.messageServiceDao.updateSmppSent(sms, "DELIVERED");
                }
            } else if (retCode != 200) {
                boolean checkSkipAutoResends = this.checkSkipAutoResend(smsMessage.getKeyword());
                if (checkSkipAutoResends) {
                    this.messageServiceDao.updateSmppSent(sms, "UNDELIVERED");
                } else {
                    String smppName = "HTTP_SOPRANO";

                    try {
                        this.sendSmsMessageSmpp(smppName, credit, smsMessage);
                    } catch (InvalidKeyException e) {
                        e.printStackTrace();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (InvalidKeySpecException e) {
                        e.printStackTrace();
                    } catch (NoSuchPaddingException e) {
                        e.printStackTrace();
                    } catch (IllegalBlockSizeException e) {
                        e.printStackTrace();
                    } catch (BadPaddingException e) {
                        e.printStackTrace();
                    } catch (MessageException | com.objectxp.msg.MessageException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                logger.info("fail to call celcom http");
                this.messageServiceDao.updateSmppSent(sms, "UNDELIVERED");
                if (errorCodeHeader != null) {
                    errorcode = errorCodeHeader.getValue();
                    logger.info("errorcode >>" + errorcode);
                }
            }
        } catch (HttpException httpExp) {
            httpExp.printStackTrace();
            logger.fatal(httpExp);
            throw httpExp;
        } catch (ProtocolException pExp) {
            pExp.printStackTrace();
            logger.fatal(pExp);
            throw pExp;
        } catch (IOException ioExp) {
            ioExp.printStackTrace();
            logger.fatal(ioExp);
            throw ioExp;
        } catch (SQLException sqlE) {
            sqlE.printStackTrace();
            logger.fatal(sqlE);
            throw sqlE;
        } finally {
            if (buff != null) {
                buff.close();
            }

            if (httpGet != null) {
                httpGet.releaseConnection();
            }

        }

        return retCode;
    }

    public int performHTTPInfobipRequest(GetMethod httpGet, String response, EMSMessage sms, SMSMessageSmpp smsMessage, String httpName, String dnid, String sender, String credit, int msgSequence) throws IOException, ProtocolException, SQLException, HttpException {
        String line = "";
        int retCode = 0;
        BufferedReader buff = null;
        HttpClient httpclient = new HttpClient();

        try {
            this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, dnid, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), msgSequence, credit);
            retCode = httpclient.executeMethod(httpGet);
            if (retCode != 200) {
                System.err.println("Method failed: " + httpGet.getStatusLine());
            }

            InputStream rstream = null;
            rstream = httpGet.getResponseBodyAsStream();
            buff = new BufferedReader(new InputStreamReader(rstream));

            for (int a = 0; (line = buff.readLine()) != null; ++a) {
                response = response + line;
                if (a > 20000) {
                    break;
                }
            }

            logger.info("CUST ID >>>" + smsMessage.getKeyword() + ", Response Code >>>" + retCode);
            logger.info("CUST ID >>>" + smsMessage.getKeyword() + ", Response >>>" + response);
            buff.close();
            httpGet.releaseConnection();
            if (retCode == 200) {
                if (!response.equals("")) {
                    BigInteger bi1 = new BigInteger(response);
                    BigInteger bi2 = new BigInteger("0");
                    System.out.println();
                    if (bi1.compareTo(bi2) == 1) {
                        System.out.println("###############DELIVERED#####################");
                        this.messageServiceDao.updateSmppSent(sms, "DELIVERED");
                        this.messageServiceDao.updateSmppId(sms, response);
                    } else {
                        boolean checkSkipAutoResends = this.checkSkipAutoResend(smsMessage.getKeyword());
                        if (checkSkipAutoResends) {
                            this.messageServiceDao.updateSmppSent(sms, "UNDELIVERED");
                        } else {
                            String smppName = "HTTP_ICE";

                            try {
                                this.sendSmsMessageSmpp(smppName, credit, smsMessage);
                            } catch (InvalidKeyException e) {
                                e.printStackTrace();
                            } catch (NoSuchAlgorithmException e) {
                                e.printStackTrace();
                            } catch (InvalidKeySpecException e) {
                                e.printStackTrace();
                            } catch (NoSuchPaddingException e) {
                                e.printStackTrace();
                            } catch (IllegalBlockSizeException e) {
                                e.printStackTrace();
                            } catch (BadPaddingException e) {
                                e.printStackTrace();
                            } catch (MessageException e) {
                                e.printStackTrace();
                            } catch (com.objectxp.msg.MessageException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                } else {
                    boolean checkSkipAutoResends = this.checkSkipAutoResend(smsMessage.getKeyword());
                    if (checkSkipAutoResends) {
                        this.messageServiceDao.updateSmppSent(sms, "UNDELIVERED");
                    } else {
                        String smppName = "HTTP_ICE";

                        try {
                            this.sendSmsMessageSmpp(smppName, credit, smsMessage);
                        } catch (InvalidKeyException e) {
                            e.printStackTrace();
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        } catch (InvalidKeySpecException e) {
                            e.printStackTrace();
                        } catch (NoSuchPaddingException e) {
                            e.printStackTrace();
                        } catch (IllegalBlockSizeException e) {
                            e.printStackTrace();
                        } catch (BadPaddingException e) {
                            e.printStackTrace();
                        } catch (MessageException e) {
                            e.printStackTrace();
                        } catch (com.objectxp.msg.MessageException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            } else {
                boolean checkSkipAutoResends = this.checkSkipAutoResend(smsMessage.getKeyword());
                if (checkSkipAutoResends) {
                    this.messageServiceDao.updateSmppSent(sms, "UNDELIVERED");
                } else {
                    String smppName = "HTTP_ICE";

                    try {
                        this.sendSmsMessageSmpp(smppName, credit, smsMessage);
                    } catch (InvalidKeyException e) {
                        e.printStackTrace();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (InvalidKeySpecException e) {
                        e.printStackTrace();
                    } catch (NoSuchPaddingException e) {
                        e.printStackTrace();
                    } catch (IllegalBlockSizeException e) {
                        e.printStackTrace();
                    } catch (BadPaddingException e) {
                        e.printStackTrace();
                    } catch (MessageException e) {
                        e.printStackTrace();
                    } catch (com.objectxp.msg.MessageException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        } catch (HttpException httpExp) {
            this.messageServiceDao.updateSmppSent(sms, "UNDELIVERED");
            httpExp.printStackTrace();
            logger.fatal(httpExp);
            throw httpExp;
        } catch (ProtocolException pExp) {
            this.messageServiceDao.updateSmppSent(sms, "UNDELIVERED");
            pExp.printStackTrace();
            logger.fatal(pExp);
            throw pExp;
        } catch (IOException ioExp) {
            this.messageServiceDao.updateSmppSent(sms, "UNDELIVERED");
            ioExp.printStackTrace();
            logger.fatal(ioExp);
            throw ioExp;
        } catch (SQLException sqlE) {
            sqlE.printStackTrace();
            logger.fatal(sqlE);
            throw sqlE;
        } finally {
            if (buff != null) {
                buff.close();
            }

            if (httpGet != null) {
                httpGet.releaseConnection();
            }

        }

        return retCode;
    }

    public int performHTTPCardBoardFishRequest(GetMethod httpGet, String response, EMSMessage sms, SMSMessageSmpp smsMessage, String httpName, String dnid, String sender, String credit, int msgSequence) throws IOException, ProtocolException, SQLException, HttpException {
        String line = "";
        int retCode = 0;
        BufferedReader buff = null;
        HttpClient httpclient = new HttpClient();

        try {
            this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, dnid, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), msgSequence, credit);
            retCode = httpclient.executeMethod(httpGet);
            if (retCode != 200) {
                System.err.println("Method failed: " + httpGet.getStatusLine());
            }

            InputStream rstream = null;
            rstream = httpGet.getResponseBodyAsStream();
            buff = new BufferedReader(new InputStreamReader(rstream));

            for (int a = 0; (line = buff.readLine()) != null; ++a) {
                response = response + line;
                if (a > 20000) {
                    break;
                }
            }

            logger.info("CUST ID >>>" + smsMessage.getKeyword() + ", Response Code >>>" + retCode);
            logger.info("CUST ID >>>" + smsMessage.getKeyword() + ", Response >>>" + response);
            buff.close();
            httpGet.releaseConnection();
            if (retCode == 200) {
                if (!response.equals("")) {
                    String[] splitText = response.split(" ");
                    if (splitText.length == 2) {
                        response = splitText[1];
                    } else {
                        response = splitText[0];
                    }

                    BigInteger bi1 = new BigInteger(response);
                    BigInteger bi2 = new BigInteger("0");
                    logger.info("bi1-response>>>" + bi1);
                    logger.info("bi2-0>>>" + bi2);
                    logger.info("compare bi1 and bi2 >>" + bi1.compareTo(bi2));
                    if (bi1.compareTo(bi2) == 1) {
                        System.out.println("###############DELIVERED#####################");
                        this.messageServiceDao.updateSmppSent(sms, "DELIVERED");
                        this.messageServiceDao.updateSmppId(sms, response);
                    } else {
                        this.messageServiceDao.updateSmppSent(sms, "UNDELIVERED");
                        logger.info("CARD BOARD FISH UNDELIVERED : " + smsMessage.getTelco() + " >>>" + response);
                    }
                } else {
                    this.messageServiceDao.updateSmppSent(sms, "UNDELIVERED");
                }
            } else {
                this.messageServiceDao.updateSmppSent(sms, "UNDELIVERED");
            }
        } catch (HttpException httpExp) {
            this.messageServiceDao.updateSmppSent(sms, "UNDELIVERED");
            httpExp.printStackTrace();
            logger.fatal(httpExp);
            throw httpExp;
        } catch (ProtocolException pExp) {
            this.messageServiceDao.updateSmppSent(sms, "UNDELIVERED");
            pExp.printStackTrace();
            logger.fatal(pExp);
            throw pExp;
        } catch (IOException ioExp) {
            this.messageServiceDao.updateSmppSent(sms, "UNDELIVERED");
            ioExp.printStackTrace();
            logger.fatal(ioExp);
            throw ioExp;
        } catch (SQLException sqlE) {
            sqlE.printStackTrace();
            logger.fatal(sqlE);
            throw sqlE;
        } finally {
            if (buff != null) {
                buff.close();
            }

            if (httpGet != null) {
                httpGet.releaseConnection();
            }

        }

        return retCode;
    }

    public int performHTTPFireMobileRequest(GetMethod httpGet, String response, EMSMessage sms, SMSMessageSmpp smsMessage, String httpName, String dnid, String sender, String credit, int msgSequence) throws IOException, ProtocolException, SQLException, HttpException {
        String line = "";
        int retCode = 0;
        BufferedReader buff = null;
        HttpClient httpclient = new HttpClient();

        try {
            this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, dnid, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), msgSequence, credit);
            retCode = httpclient.executeMethod(httpGet);
            if (retCode != 200) {
                System.err.println("Method failed: " + httpGet.getStatusLine());
            }

            InputStream rstream = null;
            rstream = httpGet.getResponseBodyAsStream();
            buff = new BufferedReader(new InputStreamReader(rstream));

            for (int a = 0; (line = buff.readLine()) != null; ++a) {
                response = response + line;
                if (a > 20000) {
                    break;
                }
            }

            logger.info("CUST ID >>>" + smsMessage.getKeyword() + ", Response Code >>>" + retCode);
            logger.info("CUST ID >>>" + smsMessage.getKeyword() + ", Response >>>" + response);
            buff.close();
            httpGet.releaseConnection();
            if (retCode != 202 && retCode != 200) {
                this.messageServiceDao.updateSmppSent(sms, "UNDELIVERED");
            } else if (response.startsWith("status=0")) {
                System.out.println("###############DELIVERED#####################");
                this.messageServiceDao.updateSmppSent(sms, "DELIVERED");
                this.messageServiceDao.updateSmppId(sms, response);
            } else {
                this.messageServiceDao.updateSmppSent(sms, "UNDELIVERED");
                logger.info("HTTP FIREMOBILE : " + smsMessage.getTelco() + " >>>" + response);
            }
        } catch (HttpException httpExp) {
            this.messageServiceDao.updateSmppSent(sms, "UNDELIVERED");
            httpExp.printStackTrace();
            logger.fatal(httpExp);
            throw httpExp;
        } catch (ProtocolException pExp) {
            this.messageServiceDao.updateSmppSent(sms, "UNDELIVERED");
            pExp.printStackTrace();
            logger.fatal(pExp);
            throw pExp;
        } catch (IOException ioExp) {
            this.messageServiceDao.updateSmppSent(sms, "UNDELIVERED");
            ioExp.printStackTrace();
            logger.fatal(ioExp);
            throw ioExp;
        } catch (SQLException sqlE) {
            sqlE.printStackTrace();
            logger.fatal(sqlE);
            throw sqlE;
        } finally {
            if (buff != null) {
                buff.close();
            }

            if (httpGet != null) {
                httpGet.releaseConnection();
            }

        }

        return retCode;
    }

    public int performHTTPTATARequest(GetMethod httpGet, String response, EMSMessage sms, SMSMessageSmpp smsMessage, String httpName, String dnid, String sender, String credit, int msgSequence) throws IOException, ProtocolException, SQLException, HttpException {
        String line = "";
        int retCode = 0;
        BufferedReader buff = null;
        HttpClient httpclient = new HttpClient();

        try {
            this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, dnid, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), msgSequence, credit);
            retCode = httpclient.executeMethod(httpGet);
            if (retCode != 200) {
                System.err.println("Method failed: " + httpGet.getStatusLine());
            }

            InputStream rstream = null;
            rstream = httpGet.getResponseBodyAsStream();
            buff = new BufferedReader(new InputStreamReader(rstream));

            for (int a = 0; (line = buff.readLine()) != null; ++a) {
                response = response + line;
                if (a > 20000) {
                    break;
                }
            }

            logger.info("CUST ID >>>" + smsMessage.getKeyword() + ", Response Code >>>" + retCode);
            logger.info("CUST ID >>>" + smsMessage.getKeyword() + ", Response >>>" + response);
            buff.close();
            httpGet.releaseConnection();
            if (retCode != 202 && retCode != 200) {
                if (retCode == 202 && retCode == 200) {
                    this.messageServiceDao.updateSmppSent(sms, "UNDELIVERED");
                } else {
                    boolean checkSkipAutoResends = this.checkSkipAutoResend(smsMessage.getKeyword());
                    if (checkSkipAutoResends) {
                        this.messageServiceDao.updateSmppSent(sms, "UNDELIVERED");
                    } else {
                        String smppName = "HTTP_INFOBIP";

                        try {
                            this.sendSmsMessageSmpp(smppName, credit, smsMessage);
                        } catch (InvalidKeyException e) {
                            e.printStackTrace();
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        } catch (InvalidKeySpecException e) {
                            e.printStackTrace();
                        } catch (NoSuchPaddingException e) {
                            e.printStackTrace();
                        } catch (IllegalBlockSizeException e) {
                            e.printStackTrace();
                        } catch (BadPaddingException e) {
                            e.printStackTrace();
                        } catch (MessageException e) {
                            e.printStackTrace();
                        } catch (com.objectxp.msg.MessageException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            } else {
                System.out.println("###############DELIVERED#####################");
                this.messageServiceDao.updateSmppSent(sms, "DELIVERED");
                this.messageServiceDao.updateSmppId(sms, response);
            }
        } catch (HttpException httpExp) {
            this.messageServiceDao.updateSmppSent(sms, "UNDELIVERED");
            httpExp.printStackTrace();
            logger.fatal(httpExp);
            throw httpExp;
        } catch (ProtocolException pExp) {
            this.messageServiceDao.updateSmppSent(sms, "UNDELIVERED");
            pExp.printStackTrace();
            logger.fatal(pExp);
            throw pExp;
        } catch (IOException ioExp) {
            this.messageServiceDao.updateSmppSent(sms, "UNDELIVERED");
            ioExp.printStackTrace();
            logger.fatal(ioExp);
            throw ioExp;
        } catch (SQLException sqlE) {
            sqlE.printStackTrace();
            logger.fatal(sqlE);
            throw sqlE;
        } finally {
            if (buff != null) {
                buff.close();
            }

            if (httpGet != null) {
                httpGet.releaseConnection();
            }

        }

        return retCode;
    }

    public int performHTTPSopranoRequest(GetMethod httpGet, String response, EMSMessage sms, SMSMessageSmpp smsMessage, String httpName, String dnid, String sender, String credit, int msgSequence) throws IOException, ProtocolException, SQLException, HttpException {
        String line = "";
        int retCode = 0;
        BufferedReader buff = null;
        HttpClient httpclient = new HttpClient();

        try {
            this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, dnid, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), msgSequence, credit);
            retCode = httpclient.executeMethod(httpGet);
            if (retCode != 200) {
                System.err.println("Method failed: " + httpGet.getStatusLine());
            }

            InputStream rstream = null;
            rstream = httpGet.getResponseBodyAsStream();
            buff = new BufferedReader(new InputStreamReader(rstream));

            for (int a = 0; (line = buff.readLine()) != null; ++a) {
                response = response + line;
                if (a > 20000) {
                    break;
                }
            }

            logger.info("CUST ID >>>" + smsMessage.getKeyword() + ", Response Code >>>" + retCode);
            logger.info("CUST ID >>>" + smsMessage.getKeyword() + ", Response >>>" + response);
            buff.close();
            httpGet.releaseConnection();
            String search = "0 001 OK";
            if (response.startsWith(search)) {
                System.out.println("###############DELIVERED#####################");
                String[] response1 = response.split("Message-ID: ");
                response = response1[1].toString();
                this.messageServiceDao.updateSmppSent(sms, "DELIVERED");
                this.messageServiceDao.updateSmppId(sms, response);
            } else if (!response.startsWith(search)) {
                boolean checkSkipAutoResends = this.checkSkipAutoResend(smsMessage.getKeyword());
                if (checkSkipAutoResends) {
                    this.messageServiceDao.updateSmppSent(sms, "UNDELIVERED");
                } else {
                    String smppName = "HTTP_CELCOM";

                    try {
                        this.sendSmsMessageSmpp(smppName, credit, smsMessage);
                    } catch (InvalidKeyException e) {
                        e.printStackTrace();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (InvalidKeySpecException e) {
                        e.printStackTrace();
                    } catch (NoSuchPaddingException e) {
                        e.printStackTrace();
                    } catch (IllegalBlockSizeException e) {
                        e.printStackTrace();
                    } catch (BadPaddingException e) {
                        e.printStackTrace();
                    } catch (MessageException e) {
                        e.printStackTrace();
                    } catch (com.objectxp.msg.MessageException e) {
                        throw new RuntimeException(e);
                    }
                }
            } else {
                boolean checkSkipAutoResends = this.checkSkipAutoResend(smsMessage.getKeyword());
                if (checkSkipAutoResends) {
                    this.messageServiceDao.updateSmppSent(sms, "UNDELIVERED");
                } else {
                    String smppName = "HTTP_CELCOM";

                    try {
                        this.sendSmsMessageSmpp(smppName, credit, smsMessage);
                    } catch (InvalidKeyException e) {
                        e.printStackTrace();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (InvalidKeySpecException e) {
                        e.printStackTrace();
                    } catch (NoSuchPaddingException e) {
                        e.printStackTrace();
                    } catch (IllegalBlockSizeException e) {
                        e.printStackTrace();
                    } catch (BadPaddingException e) {
                        e.printStackTrace();
                    } catch (MessageException e) {
                        e.printStackTrace();
                    } catch (com.objectxp.msg.MessageException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        } catch (HttpException httpExp) {
            this.messageServiceDao.updateSmppSent(sms, "UNDELIVERED");
            httpExp.printStackTrace();
            logger.fatal(httpExp);
            throw httpExp;
        } catch (ProtocolException pExp) {
            this.messageServiceDao.updateSmppSent(sms, "UNDELIVERED");
            pExp.printStackTrace();
            logger.fatal(pExp);
            throw pExp;
        } catch (IOException ioExp) {
            this.messageServiceDao.updateSmppSent(sms, "UNDELIVERED");
            ioExp.printStackTrace();
            logger.fatal(ioExp);
            throw ioExp;
        } catch (SQLException sqlE) {
            sqlE.printStackTrace();
            logger.fatal(sqlE);
            throw sqlE;
        } finally {
            if (buff != null) {
                buff.close();
            }

            if (httpGet != null) {
                httpGet.releaseConnection();
            }

        }

        return retCode;
    }

    public int performHTTPSopranoTacRequest(GetMethod httpGet, String response, EMSMessage sms, SMSMessageSmpp smsMessage, String httpName, String dnid, String sender, String credit, int msgSequence) throws IOException, ProtocolException, SQLException, HttpException {
        String line = "";
        int retCode = 0;
        BufferedReader buff = null;
        HttpClient httpclient = new HttpClient();

        try {
            this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, dnid, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), msgSequence, credit);
            retCode = httpclient.executeMethod(httpGet);
            if (retCode != 200) {
                System.err.println("Method failed: " + httpGet.getStatusLine());
            }

            InputStream rstream = null;
            rstream = httpGet.getResponseBodyAsStream();
            buff = new BufferedReader(new InputStreamReader(rstream));

            for (int a = 0; (line = buff.readLine()) != null; ++a) {
                response = response + line;
                if (a > 20000) {
                    break;
                }
            }

            logger.info("CUST ID >>>" + smsMessage.getKeyword() + ", Response Code >>>" + retCode);
            logger.info("CUST ID >>>" + smsMessage.getKeyword() + ", Response >>>" + response);
            buff.close();
            httpGet.releaseConnection();
            String search = "0 001 OK";
            if (response.startsWith(search)) {
                System.out.println("###############DELIVERED#####################");
                String[] response1 = response.split("Message-ID: ");
                response = response1[1].toString();
                this.messageServiceDao.updateSmppSent(sms, "DELIVERED");
                this.messageServiceDao.updateSmppId(sms, response);
            } else if (!response.startsWith(search)) {
                boolean checkSkipAutoResends = this.checkSkipAutoResend(smsMessage.getKeyword());
                if (checkSkipAutoResends) {
                    this.messageServiceDao.updateSmppSent(sms, "UNDELIVERED");
                } else {
                    String smppName = "HTTP_CELCOM";

                    try {
                        this.sendSmsMessageSmpp(smppName, credit, smsMessage);
                    } catch (InvalidKeyException e) {
                        e.printStackTrace();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (InvalidKeySpecException e) {
                        e.printStackTrace();
                    } catch (NoSuchPaddingException e) {
                        e.printStackTrace();
                    } catch (IllegalBlockSizeException e) {
                        e.printStackTrace();
                    } catch (BadPaddingException e) {
                        e.printStackTrace();
                    } catch (MessageException e) {
                        e.printStackTrace();
                    } catch (com.objectxp.msg.MessageException e) {
                        throw new RuntimeException(e);
                    }
                }
            } else {
                boolean checkSkipAutoResends = this.checkSkipAutoResend(smsMessage.getKeyword());
                if (checkSkipAutoResends) {
                    this.messageServiceDao.updateSmppSent(sms, "UNDELIVERED");
                } else {
                    String smppName = "HTTP_CELCOM";

                    try {
                        this.sendSmsMessageSmpp(smppName, credit, smsMessage);
                    } catch (InvalidKeyException e) {
                        e.printStackTrace();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (InvalidKeySpecException e) {
                        e.printStackTrace();
                    } catch (NoSuchPaddingException e) {
                        e.printStackTrace();
                    } catch (IllegalBlockSizeException e) {
                        e.printStackTrace();
                    } catch (BadPaddingException e) {
                        e.printStackTrace();
                    } catch (MessageException e) {
                        e.printStackTrace();
                    } catch (com.objectxp.msg.MessageException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        } catch (HttpException httpExp) {
            this.messageServiceDao.updateSmppSent(sms, "UNDELIVERED");
            httpExp.printStackTrace();
            logger.fatal(httpExp);
            throw httpExp;
        } catch (ProtocolException pExp) {
            this.messageServiceDao.updateSmppSent(sms, "UNDELIVERED");
            pExp.printStackTrace();
            logger.fatal(pExp);
            throw pExp;
        } catch (IOException ioExp) {
            this.messageServiceDao.updateSmppSent(sms, "UNDELIVERED");
            ioExp.printStackTrace();
            logger.fatal(ioExp);
            throw ioExp;
        } catch (SQLException sqlE) {
            sqlE.printStackTrace();
            logger.fatal(sqlE);
            throw sqlE;
        } finally {
            if (buff != null) {
                buff.close();
            }

            if (httpGet != null) {
                httpGet.releaseConnection();
            }

        }

        return retCode;
    }

    public int performHTTPWebeRequest(GetMethod httpGet, String response, EMSMessage sms, SMSMessageSmpp smsMessage, String httpName, String dnid, String sender, String credit, int msgSequence) throws IOException, ProtocolException, SQLException, HttpException {
        String line = "";
        int retCode = 0;
        BufferedReader buff = null;
        HttpClient httpclient = new HttpClient();

        try {
            this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, dnid, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), msgSequence, credit);
            retCode = httpclient.executeMethod(httpGet);
            if (retCode != 200) {
                System.err.println("Method failed: " + httpGet.getStatusLine());
            }

            InputStream rstream = null;
            rstream = httpGet.getResponseBodyAsStream();
            buff = new BufferedReader(new InputStreamReader(rstream));

            for (int a = 0; (line = buff.readLine()) != null; ++a) {
                response = response + line;
                if (a > 20000) {
                    break;
                }
            }

            logger.info("CUST ID >>>" + smsMessage.getKeyword() + ", Response Code >>>" + retCode);
            logger.info("CUST ID >>>" + smsMessage.getKeyword() + ", Response >>>" + response);
            buff.close();
            httpGet.releaseConnection();
            if (retCode != 202 && retCode != 200) {
                boolean checkSkipAutoResends = this.checkSkipAutoResend(smsMessage.getKeyword());
                if (checkSkipAutoResends) {
                    this.messageServiceDao.updateSmppSent(sms, "UNDELIVERED");
                } else {
                    String smppName = "HTTP_INFOBIP";

                    try {
                        this.sendSmsMessageSmpp(smppName, credit, smsMessage);
                    } catch (InvalidKeyException e) {
                        e.printStackTrace();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (InvalidKeySpecException e) {
                        e.printStackTrace();
                    } catch (NoSuchPaddingException e) {
                        e.printStackTrace();
                    } catch (IllegalBlockSizeException e) {
                        e.printStackTrace();
                    } catch (BadPaddingException e) {
                        e.printStackTrace();
                    } catch (MessageException e) {
                        e.printStackTrace();
                    } catch (com.objectxp.msg.MessageException e) {
                        throw new RuntimeException(e);
                    }
                }
            } else {
                String search = "status=0";
                if (response.startsWith(search)) {
                    System.out.println("###############DELIVERED#####################");
                    response = response.replace("status=0&msgid=", "").trim();
                    this.messageServiceDao.updateSmppSent(sms, "DELIVERED");
                    this.messageServiceDao.updateSmppId(sms, response);
                } else if (!response.startsWith(search)) {
                    boolean checkSkipAutoResends = this.checkSkipAutoResend(smsMessage.getKeyword());
                    if (checkSkipAutoResends) {
                        this.messageServiceDao.updateSmppSent(sms, "UNDELIVERED");
                    } else {
                        String smppName = "HTTP_INFOBIP";

                        try {
                            this.sendSmsMessageSmpp(smppName, credit, smsMessage);
                        } catch (InvalidKeyException e) {
                            e.printStackTrace();
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        } catch (InvalidKeySpecException e) {
                            e.printStackTrace();
                        } catch (NoSuchPaddingException e) {
                            e.printStackTrace();
                        } catch (IllegalBlockSizeException e) {
                            e.printStackTrace();
                        } catch (BadPaddingException e) {
                            e.printStackTrace();
                        } catch (MessageException e) {
                            e.printStackTrace();
                        } catch (com.objectxp.msg.MessageException e) {
                            throw new RuntimeException(e);
                        }
                    }
                } else {
                    String[] response1 = response.split("msgid=");
                    response = response1[1].toString();
                    this.messageServiceDao.updateSmppSent(sms, "UNDELIVERED");
                    this.messageServiceDao.updateSmppId(sms, response);
                    logger.info("HTTP WEBE : " + smsMessage.getTelco() + " >>>" + response);
                }
            }
        } catch (HttpException httpExp) {
            this.messageServiceDao.updateSmppSent(sms, "UNDELIVERED");
            httpExp.printStackTrace();
            logger.fatal(httpExp);
            throw httpExp;
        } catch (ProtocolException pExp) {
            this.messageServiceDao.updateSmppSent(sms, "UNDELIVERED");
            pExp.printStackTrace();
            logger.fatal(pExp);
            throw pExp;
        } catch (IOException ioExp) {
            this.messageServiceDao.updateSmppSent(sms, "UNDELIVERED");
            ioExp.printStackTrace();
            logger.fatal(ioExp);
            throw ioExp;
        } catch (SQLException sqlE) {
            sqlE.printStackTrace();
            logger.fatal(sqlE);
            throw sqlE;
        } finally {
            if (buff != null) {
                buff.close();
            }

            if (httpGet != null) {
                httpGet.releaseConnection();
            }

        }

        return retCode;
    }

    public int performHTTPICERequest(GetMethod httpGet, String response, EMSMessage sms, SMSMessageSmpp smsMessage, String httpName, String dnid, String sender, String credit, int msgSequence) throws IOException, ProtocolException, SQLException, HttpException {
        String line = "";
        int retCode = 0;
        BufferedReader buff = null;
        HttpClient httpclient = new HttpClient();

        try {
            this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, dnid, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), msgSequence, credit);
            retCode = httpclient.executeMethod(httpGet);
            if (retCode != 200) {
                System.err.println("Method failed: " + httpGet.getStatusLine());
            }

            InputStream rstream = null;
            rstream = httpGet.getResponseBodyAsStream();
            buff = new BufferedReader(new InputStreamReader(rstream));

            for (int a = 0; (line = buff.readLine()) != null; ++a) {
                response = response + line;
                if (a > 20000) {
                    break;
                }
            }

            logger.info("CUST ID >>>" + smsMessage.getKeyword() + ", Response Code >>>" + retCode);
            logger.info("CUST ID >>>" + smsMessage.getKeyword() + ", Response >>>" + response);
            buff.close();
            httpGet.releaseConnection();
            if (retCode != 202 && retCode != 200) {
                this.messageServiceDao.updateSmppSent(sms, "UNDELIVERED");
            } else {
                String search = "status=0";
                if (response.startsWith(search)) {
                    System.out.println("###############DELIVERED#####################");
                    response = response.replace("status=0&msgid=", "").trim();
                    this.messageServiceDao.updateSmppSent(sms, "DELIVERED");
                    this.messageServiceDao.updateSmppId(sms, response);
                } else {
                    String[] response1 = response.split("err_msg=");
                    response = response1[1].toString();
                    this.messageServiceDao.updateSmppSent(sms, "UNDELIVERED");
                    this.messageServiceDao.updateSmppId(sms, response);
                    logger.info("HTTP ICE : " + smsMessage.getTelco() + " >>>" + response);
                }
            }
        } catch (HttpException httpExp) {
            this.messageServiceDao.updateSmppSent(sms, "UNDELIVERED");
            httpExp.printStackTrace();
            logger.fatal(httpExp);
            throw httpExp;
        } catch (ProtocolException pExp) {
            this.messageServiceDao.updateSmppSent(sms, "UNDELIVERED");
            pExp.printStackTrace();
            logger.fatal(pExp);
            throw pExp;
        } catch (IOException ioExp) {
            this.messageServiceDao.updateSmppSent(sms, "UNDELIVERED");
            ioExp.printStackTrace();
            logger.fatal(ioExp);
            throw ioExp;
        } catch (SQLException sqlE) {
            sqlE.printStackTrace();
            logger.fatal(sqlE);
            throw sqlE;
        } finally {
            if (buff != null) {
                buff.close();
            }

            if (httpGet != null) {
                httpGet.releaseConnection();
            }

        }

        return retCode;
    }

    public int performHTTPICETACRequest(GetMethod httpGet, String response, EMSMessage sms, SMSMessageSmpp smsMessage, String httpName, String dnid, String sender, String credit, int msgSequence) throws IOException, ProtocolException, SQLException, HttpException {
        String line = "";
        int retCode = 0;
        BufferedReader buff = null;
        HttpClient httpclient = new HttpClient();

        try {
            this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, dnid, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), msgSequence, credit);
            retCode = httpclient.executeMethod(httpGet);
            if (retCode != 200) {
                System.err.println("Method failed: " + httpGet.getStatusLine());
            }

            InputStream rstream = null;
            rstream = httpGet.getResponseBodyAsStream();
            buff = new BufferedReader(new InputStreamReader(rstream));

            for (int a = 0; (line = buff.readLine()) != null; ++a) {
                response = response + line;
                if (a > 20000) {
                    break;
                }
            }

            logger.info("CUST ID >>>" + smsMessage.getKeyword() + ", Response Code >>>" + retCode);
            logger.info("CUST ID >>>" + smsMessage.getKeyword() + ", Response >>>" + response);
            buff.close();
            httpGet.releaseConnection();
            if (retCode != 202 && retCode != 200) {
                this.messageServiceDao.updateSmppSent(sms, "UNDELIVERED");
            } else {
                String search = "status=0";
                if (response.startsWith(search)) {
                    System.out.println("###############DELIVERED#####################");
                    response = response.replace("status=0&msgid=", "").trim();
                    this.messageServiceDao.updateSmppSent(sms, "DELIVERED");
                    this.messageServiceDao.updateSmppId(sms, response);
                } else {
                    String[] response1 = response.split("err_msg=");
                    response = response1[1].toString();
                    this.messageServiceDao.updateSmppSent(sms, "UNDELIVERED");
                    this.messageServiceDao.updateSmppId(sms, response);
                    logger.info("HTTP ICE : " + smsMessage.getTelco() + " >>>" + response);
                }
            }
        } catch (HttpException httpExp) {
            this.messageServiceDao.updateSmppSent(sms, "UNDELIVERED");
            httpExp.printStackTrace();
            logger.fatal(httpExp);
            throw httpExp;
        } catch (ProtocolException pExp) {
            this.messageServiceDao.updateSmppSent(sms, "UNDELIVERED");
            pExp.printStackTrace();
            logger.fatal(pExp);
            throw pExp;
        } catch (IOException ioExp) {
            this.messageServiceDao.updateSmppSent(sms, "UNDELIVERED");
            ioExp.printStackTrace();
            logger.fatal(ioExp);
            throw ioExp;
        } catch (SQLException sqlE) {
            sqlE.printStackTrace();
            logger.fatal(sqlE);
            throw sqlE;
        } finally {
            if (buff != null) {
                buff.close();
            }

            if (httpGet != null) {
                httpGet.releaseConnection();
            }

        }

        return retCode;
    }

    public int performHTTPTriomobile(GetMethod httpGet, EMSMessage sms, SMSMessageSmpp smsMessage, String httpName, String dnid, String sender, String credit, int msgSequence) throws IOException, ProtocolException, SQLException, HttpException {
        int responseCode = 0;
        String response = "";
        BufferedReader buff = null;
        String line = "";
        HttpClient httpclient = new HttpClient();

        try {
            try {
                responseCode = httpclient.executeMethod(httpGet);
                InputStream rstream = null;
                rstream = httpGet.getResponseBodyAsStream();
                buff = new BufferedReader(new InputStreamReader(rstream));

                for (int a = 0; (line = buff.readLine()) != null; ++a) {
                    response = response + line;
                    if (a > 20000) {
                        break;
                    }
                }
            } catch (HttpException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            buff.close();
            httpGet.releaseConnection();
            response = response.replaceAll("[^a-zA-Z0-9 -]", "");
            logger.info("CUST ID >>>" + smsMessage.getKeyword() + ", Response Code >>>" + responseCode);
            logger.info("CUST ID >>>" + smsMessage.getKeyword() + ", Response >>>" + response);
            this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, dnid, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), msgSequence, credit);
            if (responseCode == 200) {
                try {
                    if (Integer.parseInt(response) <= 0) {
                        this.messageServiceDao.updateSmppSent(sms, "UNDELIVERED");
                        this.messageServiceDao.updateSmppError(sms, response);
                    } else {
                        this.messageServiceDao.updateSmppSent(sms, "DELIVERED");
                        this.messageServiceDao.updateSmppId(sms, response);
                    }
                } catch (NumberFormatException var22) {
                    this.messageServiceDao.updateSmppSent(sms, "DELIVERED");
                    this.messageServiceDao.updateSmppId(sms, response);
                }
            } else {
                this.messageServiceDao.updateSmppSent(sms, "UNDELIVERED");
                this.messageServiceDao.updateSmppError(sms, "" + responseCode);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (httpGet != null) {
                httpGet.releaseConnection();
            }

        }

        return responseCode;
    }

    public int performHTTPNEXMO(EMSMessage sms, SMSMessageSmpp smsMessage, String httpName, String dnid, String sender, String credit, int msgSequence, String targetURL) throws IOException, ProtocolException, SQLException, HttpException {
        int responseCode = 0;
        String response = "";
        BufferedReader buff = null;
        String line = "";
        new HttpClient();
        HttpURLConnection connection = null;

        try {
            try {
                URL url = new URL(targetURL);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setRequestProperty("Content-Length", Integer.toString(targetURL.getBytes().length));
                connection.setRequestProperty("Content-Language", "en-US");
                connection.setUseCaches(false);
                connection.setDoInput(true);
                connection.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
                wr.writeBytes(targetURL);
                wr.flush();
                wr.close();
                InputStream rstream = null;
                rstream = connection.getInputStream();
                buff = new BufferedReader(new InputStreamReader(rstream));

                for (int a = 0; (line = buff.readLine()) != null; ++a) {
                    response = response + line;
                    if (a > 20000) {
                        break;
                    }
                }

                responseCode = connection.getResponseCode();
            } catch (HttpException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                buff.close();
                if (connection != null) {
                    connection.disconnect();
                }

            }

            logger.info("CUST ID >>>" + smsMessage.getKeyword() + ", Response Code >>>" + responseCode);
            this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, dnid, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), msgSequence, credit);
            if (responseCode == 200) {
                try {
                    JSONParser parser = new JSONParser();
                    Object obj = parser.parse(response.toString());
                    JSONObject jsonObject = (JSONObject) obj;
                    JSONArray lang = (JSONArray) jsonObject.get("messages");
                    Iterator i = lang.iterator();
                    String msgId = "";

                    while (i.hasNext()) {
                        JSONObject innerObj = (JSONObject) i.next();
                        logger.info("CUST ID >>>" + smsMessage.getKeyword() + ", Response >>>" + innerObj.get("status"));
                        if (!innerObj.get("status").toString().equalsIgnoreCase("0")) {
                            logger.debug("status= " + innerObj.get("status"));
                            logger.debug("error-text=" + innerObj.get("error-text"));
                            this.messageServiceDao.updateSmppSent(sms, "UNDELIVERED");
                            this.messageServiceDao.updateSmppId(sms, innerObj.get("message-id").toString());
                        } else {
                            msgId = innerObj.get("message-id").toString();
                            logger.debug("to= " + innerObj.get("to"));
                            logger.debug("message-id= " + innerObj.get("message-id"));
                            logger.debug("status= " + innerObj.get("to"));
                            logger.debug("remaining-balance= " + innerObj.get("remaining-balance"));
                            logger.debug("message-price= " + innerObj.get("message-price"));
                            logger.debug("network= " + innerObj.get("network"));
                            this.messageServiceDao.updateSmppSent(sms, "DELIVERED");
                            this.messageServiceDao.updateSmppId(sms, innerObj.get("message-id").toString());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    this.messageServiceDao.updateSmppSent(sms, "DELIVERED");
                }
            } else {
                this.messageServiceDao.updateSmppSent(sms, "UNDELIVERED");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }

        }

        return responseCode;
    }

    public int performHTTPGINSMSRequest(GetMethod httpGet, String response, EMSMessage sms, SMSMessageSmpp smsMessage, String httpName, String dnid, String sender, String credit, int msgSequence) throws IOException, ProtocolException, SQLException, HttpException {
        String line = "";
        int retCode = 0;
        String status = "";
        String messageId = "";
        String desc = "";
        BufferedReader buff = null;
        HttpClient httpclient = new HttpClient();

        try {
            this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, dnid, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), msgSequence, credit);
            retCode = httpclient.executeMethod(httpGet);
            if (retCode != 200) {
                System.err.println("Method failed: " + httpGet.getStatusLine());
            }

            InputStream rstream = null;
            rstream = httpGet.getResponseBodyAsStream();
            buff = new BufferedReader(new InputStreamReader(rstream));

            for (int a = 0; (line = buff.readLine()) != null; ++a) {
                logger.debug("line=" + line);
                if (line.startsWith("status")) {
                    status = line.replace("status=", "").trim();
                } else if (line.startsWith("messageId")) {
                    messageId = line.replace("messageId=", "").trim();
                } else if (line.startsWith("desc")) {
                    desc = line.replace("desc=", "").trim();
                }

                response = response + line;
                if (a > 20000) {
                    break;
                }
            }

            logger.info("CUST ID >>>" + smsMessage.getKeyword() + ", Response Code >>>" + retCode);
            logger.info("CUST ID >>>" + smsMessage.getKeyword() + ", Response >>>" + response);
            buff.close();
            httpGet.releaseConnection();
            if (retCode == 200) {
                if (!response.equals("") && status.equalsIgnoreCase("0")) {
                    System.out.println("###############DELIVERED#####################");
                    this.messageServiceDao.updateSmppSent(sms, "DELIVERED");
                    this.messageServiceDao.updateSmppId(sms, messageId);
                } else if (status != "0") {
                    boolean checkSkipAutoResends = this.checkSkipAutoResend(smsMessage.getKeyword());
                    if (checkSkipAutoResends) {
                        this.messageServiceDao.updateSmppSent(sms, "UNDELIVERED");
                    } else {
                        String smppName = "HTTP_INFOBIP";

                        try {
                            this.sendSmsMessageSmpp(smppName, credit, smsMessage);
                        } catch (InvalidKeyException e) {
                            e.printStackTrace();
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        } catch (InvalidKeySpecException e) {
                            e.printStackTrace();
                        } catch (NoSuchPaddingException e) {
                            e.printStackTrace();
                        } catch (IllegalBlockSizeException e) {
                            e.printStackTrace();
                        } catch (BadPaddingException e) {
                            e.printStackTrace();
                        } catch (MessageException e) {
                            e.printStackTrace();
                        } catch (com.objectxp.msg.MessageException e) {
                            throw new RuntimeException(e);
                        }
                    }
                } else {
                    this.messageServiceDao.updateSmppSent(sms, "UNDELIVERED");
                    this.messageServiceDao.updateSmppId(sms, messageId);
                }
            } else if (retCode != 200) {
                boolean checkSkipAutoResends = this.checkSkipAutoResend(smsMessage.getKeyword());
                if (checkSkipAutoResends) {
                    this.messageServiceDao.updateSmppSent(sms, "UNDELIVERED");
                } else {
                    String smppName = "HTTP_INFOBIP";

                    try {
                        this.sendSmsMessageSmpp(smppName, credit, smsMessage);
                    } catch (InvalidKeyException e) {
                        e.printStackTrace();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (InvalidKeySpecException e) {
                        e.printStackTrace();
                    } catch (NoSuchPaddingException e) {
                        e.printStackTrace();
                    } catch (IllegalBlockSizeException e) {
                        e.printStackTrace();
                    } catch (BadPaddingException e) {
                        e.printStackTrace();
                    } catch (MessageException e) {
                        e.printStackTrace();
                    } catch (com.objectxp.msg.MessageException e) {
                        throw new RuntimeException(e);
                    }
                }
            } else {
                this.messageServiceDao.updateSmppSent(sms, "UNDELIVERED");
            }
        } catch (HttpException httpExp) {
            this.messageServiceDao.updateSmppSent(sms, "UNDELIVERED");
            httpExp.printStackTrace();
            logger.fatal(httpExp);
            throw httpExp;
        } catch (ProtocolException pExp) {
            this.messageServiceDao.updateSmppSent(sms, "UNDELIVERED");
            pExp.printStackTrace();
            logger.fatal(pExp);
            throw pExp;
        } catch (IOException ioExp) {
            this.messageServiceDao.updateSmppSent(sms, "UNDELIVERED");
            ioExp.printStackTrace();
            logger.fatal(ioExp);
            throw ioExp;
        } catch (SQLException sqlE) {
            sqlE.printStackTrace();
            logger.fatal(sqlE);
            throw sqlE;
        } finally {
            if (buff != null) {
                buff.close();
            }

            if (httpGet != null) {
                httpGet.releaseConnection();
            }

        }

        return retCode;
    }

    public String checkPriceTag(String msg) {
        String finalResult = "";
        String finalMsg = "";
        if (!this.isVoidList(this.void_custid)) {
            if (!msg.toLowerCase().startsWith("rm0") && !msg.toLowerCase().startsWith("rm 0")) {
                finalMsg = "RM0.00 : " + msg;
            } else {
                String RM = msg.substring(0, 2);
                String price = msg.substring(2, 9);
                char[] arrayPrice = new char[]{'0', '.', ' ', ':', ','};
                String exceptWord = "";
                boolean status = true;

                for (int x = 0; x < price.length(); ++x) {
                    for (int y = 0; y < arrayPrice.length; ++y) {
                        if (price.charAt(x) == arrayPrice[y]) {
                            finalResult = finalResult + price.charAt(x);
                        } else if (exceptWord.indexOf(price.charAt(x)) < 0 && price.charAt(x) != '0' && price.charAt(x) != '.' && price.charAt(x) != ' ' && price.charAt(x) != ':' && price.charAt(x) != '.' && price.charAt(x) != ',' && status) {
                            exceptWord = "" + price.charAt(x);
                            status = false;
                        }
                    }
                }

                if (exceptWord.length() > 0) {
                    String tempOriginal = msg.substring(2);
                    finalResult = tempOriginal.substring(0, tempOriginal.indexOf(exceptWord));
                }

                finalMsg = msg.replaceAll(RM + finalResult, "RM0.00 : ");
            }
        } else {
            finalMsg = this.getSpecificPrefix(this.void_custid, "0", msg) + msg;
        }

        return finalMsg;
    }

    public String removePriceTag(String msg) {
        String finalResult = "";
        String finalMsg = "";
        if (msg.toLowerCase().startsWith("rm")) {
            String RM = msg.substring(0, 2);
            String price = msg.substring(2, 9);
            char[] arrayPrice = new char[]{'0', '.', ' ', ':', ','};
            String exceptWord = "";
            boolean status = true;

            for (int x = 0; x < price.length(); ++x) {
                for (int y = 0; y < arrayPrice.length; ++y) {
                    if (price.charAt(x) == arrayPrice[y]) {
                        finalResult = finalResult + price.charAt(x);
                    } else if (exceptWord.indexOf(price.charAt(x)) < 0 && price.charAt(x) != '0' && price.charAt(x) != '.' && price.charAt(x) != ' ' && price.charAt(x) != ':' && price.charAt(x) != '.' && price.charAt(x) != ',' && status) {
                        exceptWord = "" + price.charAt(x);
                        status = false;
                    }
                }
            }

            if (exceptWord.length() > 0) {
                String tempOriginal = msg.substring(2);
                finalResult = tempOriginal.substring(0, tempOriginal.indexOf(exceptWord));
            }

            finalMsg = msg.replaceAll(RM + finalResult, "RM0.00 : ");
        } else {
            finalMsg = "RM0.00 : " + msg;
        }

        finalMsg = finalMsg.replace("RM0.00 : ", "").trim();
        return finalMsg;
    }

    public String checkInfobipPriceTag(String msg) {
        String finalResult = "";
        String finalMsg = "";
        if (!this.isVoidList(this.void_custid)) {
            if (!msg.toLowerCase().startsWith("rm0") && !msg.toLowerCase().startsWith("rm 0")) {
                finalMsg = "RM0.00 " + msg;
            } else {
                String RM = msg.substring(0, 2);
                String price = msg.substring(2, 9);
                char[] arrayPrice = new char[]{'0', '.', ' ', ':', ','};
                String exceptWord = "";
                boolean status = true;

                for (int x = 0; x < price.length(); ++x) {
                    for (int y = 0; y < arrayPrice.length; ++y) {
                        if (price.charAt(x) == arrayPrice[y]) {
                            finalResult = finalResult + price.charAt(x);
                        } else if (exceptWord.indexOf(price.charAt(x)) < 0 && price.charAt(x) != '0' && price.charAt(x) != '.' && price.charAt(x) != ' ' && price.charAt(x) != ':' && price.charAt(x) != '.' && price.charAt(x) != ',' && status) {
                            exceptWord = "" + price.charAt(x);
                            status = false;
                        }
                    }
                }

                if (exceptWord.length() > 0) {
                    String tempOriginal = msg.substring(2);
                    finalResult = tempOriginal.substring(0, tempOriginal.indexOf(exceptWord));
                }

                finalMsg = msg.replaceAll(RM + finalResult, "RM0.00 ");
            }
        } else {
            finalMsg = this.getSpecificPrefix(this.void_custid, "0", msg) + msg;
        }

        return finalMsg;
    }

    public String checkChinesePriceTag(String msg) {
        String finalResult = "";
        String finalMsg = "";
        boolean inList = false;
        if (!this.isVoidList(this.void_custid)) {
            if (!msg.toLowerCase().startsWith("0052004d") && !msg.toLowerCase().startsWith("0072006d")) {
                finalMsg = "0052004D0030002E003000300020003A0020" + msg;
            } else {
                String RM = msg.substring(0, 8).toUpperCase();
                String price = msg.substring(8, 40).toUpperCase();
                String[] arrayPrice = new String[]{"0030", "002E", "0020", "003A", "002C"};

                for (int x = 0; x < price.length(); x += 4) {
                    String chineseMsg;
                    if (x == 0) {
                        chineseMsg = price.substring(0, 4);
                    } else {
                        chineseMsg = price.substring(x, x + 4);
                    }

                    for (int y = 0; y < arrayPrice.length; ++y) {
                        if (chineseMsg.equalsIgnoreCase(arrayPrice[y])) {
                            if (x == 0) {
                                finalResult = finalResult + chineseMsg;
                            } else {
                                finalResult = finalResult + chineseMsg;
                            }
                        }
                    }
                }

                finalMsg = msg.toUpperCase().replaceAll(RM.toUpperCase() + finalResult.toUpperCase(), "0052004D0030002E003000300020003A0020");
            }
        } else {
            finalMsg = this.getSpecificPrefix(this.void_custid, "8", msg) + msg;
        }

        return finalMsg;
    }

    public String removeChinesePriceTag(String msg) {
        String finalResult = "";
        String finalMsg = "";
        if (!msg.toLowerCase().startsWith("0052004d") && !msg.toLowerCase().startsWith("0072006d")) {
            finalMsg = "0052004D0030002E003000300020003A0020" + msg;
        } else {
            String RM = msg.substring(0, 8).toUpperCase();
            String price = msg.substring(8, 40).toUpperCase();
            String[] arrayPrice = new String[]{"0030", "002E", "0020", "003A", "002C"};

            for (int x = 0; x < price.length(); x += 4) {
                String chineseMsg;
                if (x == 0) {
                    chineseMsg = price.substring(0, 4);
                } else {
                    chineseMsg = price.substring(x, x + 4);
                }

                for (int y = 0; y < arrayPrice.length; ++y) {
                    if (chineseMsg.equalsIgnoreCase(arrayPrice[y])) {
                        if (x == 0) {
                            finalResult = finalResult + chineseMsg;
                        } else {
                            finalResult = finalResult + chineseMsg;
                        }
                    }
                }
            }

            finalMsg = msg.toUpperCase().replaceAll(RM.toUpperCase() + finalResult.toUpperCase(), "0052004D0030002E003000300020003A0020");
        }

        finalMsg = finalMsg.replace("0052004D0030002E003000300020003A0020", "").trim();
        return finalMsg;
    }

    public String checkChinesePriceTagWSDL(String msg) {
        String finalResult = "";
        String finalMsg = "";
        boolean inList = false;
        if (!this.isVoidList(this.void_custid)) {
            if (!msg.toLowerCase().startsWith("524d") && !msg.toLowerCase().startsWith("726d")) {
                finalMsg = "0052004D0030002E003000300020003A0020" + msg;
            } else {
                String RM = msg.substring(0, 8).toUpperCase();
                String price = msg.substring(8, 40).toUpperCase();
                String[] arrayPrice = new String[]{"0030", "002E", "0020", "003A", "002C"};

                for (int x = 0; x < price.length(); x += 4) {
                    String chineseMsg;
                    if (x == 0) {
                        chineseMsg = price.substring(0, 4);
                    } else {
                        chineseMsg = price.substring(x, x + 4);
                    }

                    for (int y = 0; y < arrayPrice.length; ++y) {
                        if (chineseMsg.equalsIgnoreCase(arrayPrice[y])) {
                            if (x == 0) {
                                finalResult = finalResult + chineseMsg;
                            } else {
                                finalResult = finalResult + chineseMsg;
                            }
                        }
                    }
                }

                finalMsg = msg.toUpperCase().replaceAll(RM.toUpperCase() + finalResult.toUpperCase(), "0052004D0030002E003000300020003A0020");
            }
        } else {
            finalMsg = this.getSpecificPrefix(this.void_custid, "8", msg) + msg;
        }

        return finalMsg;
    }

    public String checkInfobipChinesePriceTag(String msg) {
        String finalResult = "";
        String finalMsg = "";
        boolean inList = false;
        if (!this.isVoidList(this.void_custid)) {
            if (!msg.toLowerCase().startsWith("0052004d") && !msg.toLowerCase().startsWith("0072006d")) {
                finalMsg = msg;
            } else {
                String RM = msg.substring(0, 8).toUpperCase();
                String price = msg.substring(8, 40).toUpperCase();
                String[] arrayPrice = new String[]{"0030", "002E", "0020", "003A", "002C"};

                for (int x = 0; x < price.length(); x += 4) {
                    String chineseMsg;
                    if (x == 0) {
                        chineseMsg = price.substring(0, 4);
                    } else {
                        chineseMsg = price.substring(x, x + 4);
                    }

                    for (int y = 0; y < arrayPrice.length; ++y) {
                        if (chineseMsg.equalsIgnoreCase(arrayPrice[y])) {
                            if (x == 0) {
                                finalResult = finalResult + chineseMsg;
                            } else {
                                finalResult = finalResult + chineseMsg;
                            }
                        }
                    }
                }

                finalMsg = msg.toUpperCase().replaceAll(RM.toUpperCase() + finalResult.toUpperCase(), "0052004d0030002E003000300020");
            }
        } else {
            finalMsg = this.getSpecificPrefix(this.void_custid, "8", msg) + msg;
        }

        return finalMsg;
    }

    private String getContentHeaderDigi(int contentType, int partNumber, int totalPart) {
        if (totalPart > 1) {
            System.out.println("11111111111111111111111");
            return getContentHeaderWAP(partNumber, totalPart);
        } else {
            System.out.println("22222222222222");
            return getContentHeaderWAP();
        }
    }

    private String getContentPartDigi(int contentType, String content, int partNumber, int totalPart, int totalBytes) {
        return contentType == 0 ? getContentPartWAP(content, partNumber, totalPart) : getContentPart(content, partNumber, totalBytes);
    }

    public static String getContentPartWAP(String content, int partNumber, int total) {
        String contentPart = null;
        int maxLength = Integer.parseInt(content.substring(content.length() - 5, content.length()));
        content = content.substring(0, content.length() - 5);
        int firstIndex = partNumber * maxLength;
        int followIndex = (partNumber + 1) * maxLength;
        int messageLength = content.length();
        if (followIndex < messageLength) {
            contentPart = content.substring(firstIndex, followIndex);
        } else {
            contentPart = content.substring(firstIndex, messageLength);
        }

        return partNumber == 0 ? "350601AE" + contentPart : contentPart;
    }

    public static String getContentHeaderWAP() {
        String header = "0605040B8423F0";
        return header;
    }

    public static String getContentHeaderWAP(int partNumber, int total) {
        String header = "0B0504C34F23F00003C20" + total + "0" + partNumber;
        return header;
    }

    public boolean isVoidList(String custid) {
        try {
            if (custid != null && custid.startsWith("extmt_")) {
                custid = custid.replaceFirst("extmt_", "");
            }

            VoidMessagesPrefixManager cache4 = VoidMessagesPrefixManager.getInstance();
            return cache4.getMessagesObj(custid).getEnable_flag() != null && cache4.getMessagesObj(custid).getEnable_flag().equalsIgnoreCase("0");
        } catch (Exception ex) {
            logger.debug("SmppMessageServiceBeanBinder : isVoidList error = " + ex.toString());
            ex.printStackTrace();
            return false;
        }
    }

    public String getSpecificPrefix(String custid, String datacoding, String msg) {
        String result = "";

        try {
            if (custid != null && custid.startsWith("extmt_")) {
                custid = custid.replaceFirst("extmt_", "");
            }

            VoidMessagesPrefixManager cache4 = VoidMessagesPrefixManager.getInstance();
            if (cache4.getMessagesObj(custid).getEnable_flag() != null && cache4.getMessagesObj(custid).getEnable_flag().equalsIgnoreCase("0") && cache4.getMessagesObj(custid).getIs_specific_prefix() != null && cache4.getMessagesObj(custid).getIs_specific_prefix().equalsIgnoreCase("0") && cache4.getMessagesObj(custid).getPrefix() != null) {
                result = cache4.getMessagesObj(custid).getPrefix().trim() + " ";
                if (!result.equalsIgnoreCase("") && datacoding.equalsIgnoreCase("8")) {
                    result = "0052004d00300020";
                }

                if (msg.startsWith(result)) {
                    result = "";
                }
            }

            return result;
        } catch (Exception ex) {
            logger.debug("SmppMessageServiceBeanBinder : getSpecificPrefix error = " + ex.toString());
            ex.printStackTrace();
            return result;
        }
    }

    public int performHTTPModemRequest(GetMethod httpGet, String response, EMSMessage sms, SMSMessageSmpp smsMessage, String httpName, String dnid, String sender, String credit, int msgSequence) throws IOException, ProtocolException, SQLException, HttpException {
        String line = "";
        int retCode = 0;
        BufferedReader buff = null;
        HttpClient httpclient = new HttpClient();

        try {
            this.messageServiceDao.insertSmppSent2((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, dnid, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), msgSequence, credit);
            retCode = httpclient.executeMethod(httpGet);
            if (retCode != 200) {
                System.err.println("Method failed: " + httpGet.getStatusLine());
            }

            InputStream rstream = null;
            rstream = httpGet.getResponseBodyAsStream();
            buff = new BufferedReader(new InputStreamReader(rstream));

            for (int a = 0; (line = buff.readLine()) != null; ++a) {
                response = response + line;
                if (a > 20000) {
                    break;
                }
            }

            logger.info("CUST ID >>>" + smsMessage.getKeyword() + ", Response Code >>>" + retCode);
            logger.info("CUST ID >>>" + smsMessage.getKeyword() + ", Response >>>" + response);
            buff.close();
            httpGet.releaseConnection();
            if (retCode == 200) {
                if (!response.equals("") && response.equalsIgnoreCase("UNDELIVERED")) {
                    logger.info("==============================");
                    this.messageServiceDao.updateSmppSent(sms, "UNDELIVERED");
                    logger.info("MODEM UNDELIVERED : " + smsMessage.getTelco() + " >>>" + response);
                } else {
                    this.messageServiceDao.updateSmppSent(sms, "DELIVERED");
                }
            } else {
                this.messageServiceDao.updateSmppSent(sms, "UNDELIVERED");
                this.messageServiceDao.insertModemSent((String) sms.getProperty("SMPP_GUID"), smsMessage.getGroupId(), smsMessage.getTelco(), httpName, dnid, smsMessage.getMoid(), sender, smsMessage.getRecipient(), smsMessage.getMessageType(), (String) (StringUtil.isNotBlank(smsMessage.getMessage()) ? StringUtil.replaceSingleQuote(StringUtil.trimToEmpty(smsMessage.getMessage())) : smsMessage.getMessageBytes()), smsMessage.getShortcode(), smsMessage.getUserGroup(), smsMessage.getKeyword(), msgSequence, credit, "UNDELIVERED");
            }
        } catch (HttpException httpExp) {
            httpExp.printStackTrace();
            logger.fatal(httpExp);
            throw httpExp;
        } catch (ProtocolException pExp) {
            pExp.printStackTrace();
            logger.fatal(pExp);
            throw pExp;
        } catch (IOException ioExp) {
            ioExp.printStackTrace();
            logger.fatal(ioExp);
            throw ioExp;
        } catch (SQLException sqlE) {
            sqlE.printStackTrace();
            logger.fatal(sqlE);
            throw sqlE;
        } finally {
            if (httpGet != null) {
                httpGet.releaseConnection();
            }

        }

        return retCode;
    }

    public String ucs2ToUTF8(byte[] ucs2Bytes) throws UnsupportedEncodingException {
        String utf8 = new String(ucs2Bytes, "UTF-16");
        String unicode = this.toHexString(utf8);
        return unicode;
    }

    public String toHexString(String str) {
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < str.length(); ++i) {
            sb.append(toHexString(str.charAt(i)));
        }

        return sb.toString();
    }

    public static String toHexString(char ch) {
        String hex;
        for (hex = Integer.toHexString(ch); hex.length() < 4; hex = "0" + hex) {
        }

        hex = "\\u" + hex;
        return hex;
    }

    public static String SHA1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(text.getBytes("iso-8859-1"), 0, text.length());
        byte[] sha1hash = md.digest();
        return convertToHex(sha1hash);
    }

    private static String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();

        for (byte b : data) {
            int halfbyte = b >>> 4 & 15;
            int two_halfs = 0;

            while (true) {
                buf.append(halfbyte >= 0 && halfbyte <= 9 ? (char) (48 + halfbyte) : (char) (97 + (halfbyte - 10)));
                halfbyte = b & 15;
                if (two_halfs++ >= 1) {
                    break;
                }
            }
        }

        return buf.toString();
    }

    public static String escapeURIPathParam(String input) {
        StringBuilder resultStr = new StringBuilder();

        char[] var5;
        for (char ch : var5 = input.toCharArray()) {
            if (isUnsafe(ch)) {
                resultStr.append('%');
                resultStr.append(toHex(ch / 16));
                resultStr.append(toHex(ch % 16));
            } else {
                resultStr.append(ch);
            }
        }

        return resultStr.toString();
    }

    private static char toHex(int ch) {
        return (char) (ch < 10 ? 48 + ch : 65 + ch - 10);
    }

    private static boolean isUnsafe(char ch) {
        if (ch <= 128 && ch >= 0) {
            return " %$&+,/:;=?@<>#%".indexOf(ch) >= 0;
        } else {
            return true;
        }
    }

    public static void main(String[] ags) throws Exception {
        String response = "200 Sucecss";
        if (response.indexOf("200") == -1) {
            System.out.println("fdfdfdfdf");
        } else {
            System.out.println("delivered");
        }

        DESProcessor des = new DESProcessor();
        String login_name = des.encrypt("isentric", "isentric88");
        String service_id = des.encrypt("BSMS_IST_66399", "isentric88");
        System.out.println("login_name>>" + login_name);
        System.out.println("service_id>>" + service_id);
        SmppMessageServiceBinder s = new SmppMessageServiceBinder();
        System.out.println("message >>" + s.checkPriceTag("RM0.00 testing 123."));
        String dnid = "";
        dnid = String.valueOf(encrypt(generateUniqueNumber(), generateUniqueKey()));
        System.out.println("dnid ::" + dnid);
        System.out.println("1." + URLEncoder.encode("Hello World !@#$%^&*()_+", "UTF-8").replace("+", "%20"));
        System.out.println("2." + URLEncoder.encode("Hello World !@#$%^&*()_+", "UTF-8").replace("+", "%20").replace("&", "%26").replace("%", "%25"));
    }

    public boolean checkSkipAutoResend(String keyword) throws SQLException {
        // Use the JPA-backed repository helper which has been wired with the EntityManagerFactory
        try {
            BulkSkipAutoResendRepository repo = new BulkSkipAutoResendRepository();
            return repo.existsByCustId(keyword);
        } catch (Exception e) {
            logger.error("Error while checking skip autoresend for keyword=" + keyword, e);
            // fall back to false on error
            return false;
        }
    }

}
