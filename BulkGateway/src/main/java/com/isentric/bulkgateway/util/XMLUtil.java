package com.isentric.bulkgateway.util;

import com.isentric.bulkgateway.tga.webservice.QSResponse;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.security.Security;
import java.util.Random;

public class XMLUtil {
    public QSResponse getQSResponseDetailXML(String xmlStr) throws Exception {
        new QSResponse();
        QSResponse detail = this.getRequestHeaderXML(xmlStr);
        if (detail != null && detail.getRespCode().equalsIgnoreCase("0000")) {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new InputSource(new StringReader(xmlStr)));
            doc.getDocumentElement().normalize();
            System.out.println("Root element of the doc is " + doc.getDocumentElement().getNodeName());
            NodeList listOfTag = doc.getElementsByTagName("SubInfo");
            int totalTag = listOfTag.getLength();
            System.out.println("Total no of people : " + totalTag);

            for (int s = 0; s < listOfTag.getLength(); ++s) {
                Node firstNode = listOfTag.item(s);
                if (firstNode.getNodeType() == 1) {
                    Element firstElement = (Element) firstNode;
                    NodeList FrBankCodeList = firstElement.getElementsByTagName("MSISDN");
                    Element FrBankCodeElement = (Element) FrBankCodeList.item(0);
                    NodeList textFrBankCode = FrBankCodeElement.getChildNodes();
                    System.out.println("MSISDN : " + textFrBankCode.item(0).getNodeValue().trim());
                    detail.setMsisdn(textFrBankCode.item(0).getNodeValue().trim());
                    NodeList FrAcctNoList = firstElement.getElementsByTagName("RN");
                    Element FrAcctNoElement = (Element) FrAcctNoList.item(0);
                    String rn = "";
                    NodeList textFrAcctNo = FrAcctNoElement.getChildNodes();
                    if (textFrAcctNo != null && textFrAcctNo.getLength() > 0) {
                        System.out.println("RN : " + textFrAcctNo.item(0).getNodeValue().trim());
                        rn = textFrAcctNo.item(0).getNodeValue().trim().substring(0, 3);
                    }

                    if (rn.equalsIgnoreCase("202")) {
                        detail.setTelco("maxis");
                        detail.setRegionCode(textFrAcctNo.item(0).getNodeValue().trim().substring(3));
                    } else if (rn.equalsIgnoreCase("206")) {
                        detail.setTelco("digi");
                        detail.setRegionCode(textFrAcctNo.item(0).getNodeValue().trim().substring(3));
                    } else if (rn.equalsIgnoreCase("208")) {
                        detail.setTelco("umobile");
                        detail.setRegionCode(textFrAcctNo.item(0).getNodeValue().trim().substring(3));
                    } else if (rn.equalsIgnoreCase("209")) {
                        detail.setTelco("celcom");
                        detail.setRegionCode(textFrAcctNo.item(0).getNodeValue().trim().substring(3));
                    } else if (rn.equalsIgnoreCase("203")) {
                        detail.setTelco("YTLCOMMS");
                        detail.setRegionCode(textFrAcctNo.item(0).getNodeValue().trim().substring(3));
                    } else if (rn.equalsIgnoreCase("991")) {
                        detail.setTelco("TUNETALK");
                        detail.setRegionCode(textFrAcctNo.item(0).getNodeValue().trim().substring(3));
                    } else if (rn.equalsIgnoreCase("992")) {
                        detail.setTelco("XOX");
                        detail.setRegionCode(textFrAcctNo.item(0).getNodeValue().trim().substring(3));
                    } else if (rn.equalsIgnoreCase("204")) {
                        detail.setTelco("P1");
                        detail.setRegionCode(textFrAcctNo.item(0).getNodeValue().trim().substring(3));
                    } else {
                        detail.setTelco("other");
                        detail.setRegionCode("");
                    }
                }
            }
        }

        return detail;
    }

    public QSResponse getRequestHeaderXML(String xmlStr) throws Exception {
        QSResponse header = new QSResponse();
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(new InputSource(new StringReader(xmlStr)));
        doc.getDocumentElement().normalize();
        System.out.println("Root element of the doc is " + doc.getDocumentElement().getNodeName());
        NodeList listOfTag = doc.getElementsByTagName("QueryRNResponse");
        int totalTag = listOfTag.getLength();
        System.out.println("Total no of people : " + totalTag);

        for (int s = 0; s < listOfTag.getLength(); ++s) {
            Node firstNode = listOfTag.item(s);
            if (firstNode.getNodeType() == 1) {
                Element firstElement = (Element) firstNode;
                NodeList transIDList = firstElement.getElementsByTagName("TxID");
                Element transIDElement = (Element) transIDList.item(0);
                NodeList textTransID = transIDElement.getChildNodes();
                System.out.println("TxID : " + textTransID.item(0).getNodeValue().trim());
                header.setTxnId(textTransID.item(0).getNodeValue().trim());
                NodeList msgTypeList = firstElement.getElementsByTagName("RespCode");
                Element msgTypeElement = (Element) msgTypeList.item(0);
                NodeList textMsgType = msgTypeElement.getChildNodes();
                System.out.println("RespCode : " + textMsgType.item(0).getNodeValue().trim());
                header.setRespCode(textMsgType.item(0).getNodeValue().trim());
            }
        }

        return header;
    }

    public String generateQSXML(String msisdn) throws Exception {
        String xml = "";
        String txnId = encrypt(generateUniqueNumber(), generateUniqueKey());
        xml = "<QueryRNRequest><Version>1.0.0</Version><TxID>" + txnId + "</TxID><SubInfo><MSISDN>" + msisdn + "</MSISDN></SubInfo></QueryRNRequest>";
        return xml;
    }

    public static String generateUniqueNumber() {
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

    public static String generateUniqueKey() {
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

    public static String binaryToHexadecimal(byte[] bBinary) {
        StringBuffer sb = new StringBuffer();
        String temp = null;

        for (int i = 0; i < bBinary.length; ++i) {
            temp = Integer.toHexString(bBinary[i] & 255);
            sb = sb.append(temp.length() == 1 ? "0" + temp : temp);
        }

        return sb.toString().toUpperCase();
    }

    /*public static String encrypt(String data, String k) throws Exception {
        Security.addProvider(new SunJCE());
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
    }*/

    public static String encrypt(String data, String k) throws Exception {

        byte[] key = k.getBytes("UTF-8");   // simpler way

        DESKeySpec spec = new DESKeySpec(key);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("DES");
        SecretKey secret = factory.generateSecret(spec);

        Cipher desCipher = Cipher.getInstance("DES");
        desCipher.init(Cipher.ENCRYPT_MODE, secret);

        byte[] cleartext = data.getBytes("UTF-8");
        byte[] ciphertext = desCipher.doFinal(cleartext);

        return binaryToHexadecimal(ciphertext);
    }


    public static void main(String[] args) throws Exception {
        XMLUtil xml = new XMLUtil();
        QSResponse qs = xml.getQSResponseDetailXML("<?xml version=\"1.0\" encoding=\"UTF-8\"?><QueryRNResponse><TxID>10</TxID><RespCode>0000</RespCode><SubInfo><MSISDN>60162090700</MSISDN><RN>2060</RN></SubInfo></QueryRNResponse>");
        System.out.println(qs.toString());
    }

    public static String getRequestBodyXML(String xmlStr) throws Exception {
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(new InputSource(new StringReader(xmlStr)));
        doc.getDocumentElement().normalize();
        NodeList listOfTag = doc.getElementsByTagName("ns:queryRNResponse");
        if (listOfTag.getLength() < 1) {
            return null;
        } else {
            Node firstNode = listOfTag.item(0);
            NodeList childNodesList = firstNode.getChildNodes();
            if (childNodesList.getLength() < 1) {
                return null;
            } else {
                Node childNode = childNodesList.item(0);
                return childNode.getTextContent();
            }
        }
    }

}
