//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.isentric.bulkgateway.utility;


import com.isentric.bulkgateway.infobip.webservice.InfobipDipInterface;
import com.isentric.bulkgateway.infobip.webservice.InfobipDipInterfaceService;
import com.isentric.bulkgateway.infobip.webservice.InfobipDipInterfaceServiceLocator;
import com.isentric.bulkgateway.manager.LoggerManager;
import com.isentric.bulkgateway.tga.webservice.QSResponse;
import org.apache.log4j.Logger;

import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

public class Infobip161Util {
    private static final Logger logger = LoggerManager.createLoggerPattern(Infobip161Util.class);
    private static Infobip161Util infobidInstance;
    private InfobipDipInterfaceService service = null;
    private InfobipDipInterface port = null;

    // default timeouts (ms)
    private static final int CONNECT_TIMEOUT_MS = 3000; // connect timeout
    private static final int INVOKE_TIMEOUT_MS = 5000;  // overall invoke timeout

    public Infobip161Util(String serverIP) throws ServiceException, MalformedURLException {
        URL portAddress = new URL("http://192.168.26.161:8002/InfobipDipping/services/infobipws");
        this.service = new InfobipDipInterfaceServiceLocator();
        // set the endpoint address on the locator (helps some Axis setups)
        try {
            ((InfobipDipInterfaceServiceLocator)this.service).setInfobipwsEndpointAddress(portAddress.toString());
        } catch (Throwable ignored) {}

        this.port = this.service.getInfobipws(portAddress);

        // If the returned port is an Axis Stub, set reasonable timeouts to avoid long blocking
        try {
            if (this.port instanceof org.apache.axis.client.Stub) {
                org.apache.axis.client.Stub stub = (org.apache.axis.client.Stub) this.port;

                // set overall call timeout (milliseconds)
                try {
                    stub.setTimeout(Integer.valueOf(INVOKE_TIMEOUT_MS));
                } catch (Throwable t) {
                    // ignore if not supported
                }

                // set properties that some Axis HTTP transport implementations respect
                try {
                    // Older Axis versions may not expose setProperty on Stub. Use reflection to set
                    // entries into the protected 'cachedProperties' Hashtable which the generated
                    // stub copies into each Call in createCall(). This keeps compatibility.
                    try {
                        java.lang.reflect.Field f = org.apache.axis.client.Stub.class.getDeclaredField("cachedProperties");
                        f.setAccessible(true);
                        java.util.Hashtable props = (java.util.Hashtable) f.get(stub);
                        if (props == null) {
                            props = new java.util.Hashtable();
                        }
                        props.put("http.socket.timeout", Integer.valueOf(INVOKE_TIMEOUT_MS));
                        props.put("http.connection.timeout", Integer.valueOf(CONNECT_TIMEOUT_MS));
                        f.set(stub, props);
                    } catch (NoSuchFieldException nsf) {
                        // Fallback: if cachedProperties not present, try calling setProperty if available
                        try {
                            java.lang.reflect.Method m = org.apache.axis.client.Stub.class.getMethod("setProperty", String.class, Object.class);
                            m.invoke(stub, "http.socket.timeout", Integer.valueOf(INVOKE_TIMEOUT_MS));
                            m.invoke(stub, "http.connection.timeout", Integer.valueOf(CONNECT_TIMEOUT_MS));
                        } catch (Throwable ignore) {
                            // best-effort only
                        }
                    }
                } catch (Throwable t) {
                    // ignore
                }
            }
        } catch (Throwable t) {
            logger.debug("Could not set Axis stub timeouts: " + t.getMessage());
        }
    }

    public static Infobip161Util getInstance(String serverIP) throws MalformedURLException {
        try {
            synchronized(Infobip161Util.class) {
                if (infobidInstance == null) {
                    infobidInstance = new Infobip161Util(serverIP);
                }
            }
        } catch (ServiceException sExp) {
            logger.fatal(sExp);
        }

        return infobidInstance;
    }

    public String queryTelcoFromInfobid(String inMSISDN) {
        QSResponse infobidResp = null;
        String retTelco = null;

        try {
            infobidResp = this.port.InfobipDipping(inMSISDN);
            if (infobidResp != null && "200".equals(infobidResp.getRespCode()) && infobidResp.getTelco() != null) {
                retTelco = infobidResp.getTelco();
            } else {
                logger.info("[ MNP Query Fail ]");
                if (infobidResp != null) {
                    logger.info("--------------------");
                    logger.info(infobidResp.getMsisdn());
                    logger.info(infobidResp.getRegionCode());
                    logger.info(infobidResp.getRespCode());
                    logger.info(infobidResp.getTxnId());
                    logger.info(infobidResp.getTelco());
                    logger.info("--------------------");
                }
            }
        } catch (RemoteException rExp) {
            logger.warn("Infobip query RemoteException: " + rExp.getMessage());
            logger.debug(rExp);
        } catch (Exception e) {
            logger.warn("Infobip query exception: " + e.getMessage());
            logger.debug(e);
        }

        return retTelco;
    }

    public String queryTelcoFromInfobidSkipFilter(String inMSISDN) {
        QSResponse infobidResp = null;
        String retTelco = null;

        try {
            infobidResp = this.port.InfobipDippingSkipFilter(inMSISDN);
            if (infobidResp != null && "200".equals(infobidResp.getRespCode()) && infobidResp.getTelco() != null) {
                retTelco = infobidResp.getTelco();
            } else {
                logger.info("[ MNP Query SkipFilter Fail ]");
                if (infobidResp != null) {
                    logger.info("--------------------");
                    logger.info(infobidResp.getMsisdn());
                    logger.info(infobidResp.getRegionCode());
                    logger.info(infobidResp.getRespCode());
                    logger.info(infobidResp.getTxnId());
                    logger.info(infobidResp.getTelco());
                    logger.info("--------------------");
                }
            }
        } catch (RemoteException rExp) {
            logger.warn("Infobip query RemoteException (SkipFilter): " + rExp.getMessage());
            logger.debug(rExp);
        } catch (Exception e) {
            logger.warn("Infobip query exception (SkipFilter): " + e.getMessage());
            logger.debug(e);
        }

        return retTelco;
    }

    public static void main(String[] args) throws MalformedURLException {
        System.out.print(getInstance("161").queryTelcoFromInfobidSkipFilter("60126669590"));
    }
}
