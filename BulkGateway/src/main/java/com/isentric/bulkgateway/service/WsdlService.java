package com.isentric.bulkgateway.service;

import com.isentric.bulkgateway.dto.SmsGatewayResult;
import com.isentric.bulkgateway.webservice.AdditionalInfo;
import com.isentric.bulkgateway.webservice.SMSContent;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class WsdlService {

    private static final String ENDPOINT =
            "http://192.100.86.203:8001/cxf/services/SDPServices";

    public static SmsGatewayResult smsBulk(
            String login_name,
            String service_id,
            String cp_id,
            String destination_mobtel,
            String sender_name,
            String ref_id,
            int notification_ind,
            String response_url,
            String message
    ) throws Exception {

        // Build SOAP XML
        String soapXML =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                        "<soap12:Envelope xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\" " +
                        "xmlns:tns=\"http://xsd.gateway.sdp.digi.com\">" +
                        "<soap12:Body>" +
                        "<tns:SmsBulk>" +
                        "<tns:login_name>" + login_name + "</tns:login_name>" +
                        "<tns:service_id>" + service_id + "</tns:service_id>" +
                        "<tns:cp_id>" + cp_id + "</tns:cp_id>" +
                        "<tns:destination_mobtel>" + destination_mobtel + "</tns:destination_mobtel>" +
                        "<tns:sender_name>" + (sender_name == null ? "" : sender_name) + "</tns:sender_name>" +
                        "<tns:ref_id>" + ref_id + "</tns:ref_id>" +
                        "<tns:notification_ind>" + notification_ind + "</tns:notification_ind>" +
                        "<tns:response_url>" + response_url + "</tns:response_url>" +
                        "<tns:sms_contents>" +
                        "<tns:sms_content>" +
                        "<tns:content>" + message + "</tns:content>" +
                        "<tns:ucp_data_coding_id>0</tns:ucp_data_coding_id>" +
                        "<tns:ucp_msg_class></tns:ucp_msg_class>" +
                        "<tns:ucp_msg_type>3</tns:ucp_msg_type>" +
                        "</tns:sms_content>" +
                        "</tns:sms_contents>" +
                        "<tns:array_of_info/>" +
                        "</tns:SmsBulk>" +
                        "</soap12:Body>" +
                        "</soap12:Envelope>";

        System.out.println("===== SOAP REQUEST =====");
        System.out.println(soapXML);

        // Send Request
        URL url = new URL(ENDPOINT);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/soap+xml; charset=UTF-8");
        conn.setConnectTimeout(15000);
        conn.setReadTimeout(30000);
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(soapXML.getBytes("UTF-8"));
        }

        int responseCode = conn.getResponseCode();
        System.out.println("HTTP Response Code: " + responseCode);

        InputStream stream = responseCode >= 200 && responseCode < 300
                ? conn.getInputStream()
                : conn.getErrorStream();

        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = br.readLine()) != null) {
            response.append(line);
        }

        br.close();
        conn.disconnect();

        String rawResponse = response.toString();

        System.out.println("===== RAW RESPONSE =====");
        System.out.println(rawResponse);
        System.out.println("========================");

        // Extract SOAP envelope from multipart
        int start = rawResponse.indexOf("<soap:Envelope");
        int end = rawResponse.lastIndexOf("</soap:Envelope>");

        if (start == -1 || end == -1) {
            throw new RuntimeException("SOAP Envelope not found in response.");
        }

        String cleanXml = rawResponse.substring(start, end + "</soap:Envelope>".length());

        System.out.println("===== CLEAN SOAP =====");
        System.out.println(cleanXml);
        System.out.println("======================");

        return parseSmsResponse(cleanXml);
    }

    private static SmsGatewayResult parseSmsResponse(String xml) throws Exception {

        SmsGatewayResult result = new SmsGatewayResult();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);

        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(
                new ByteArrayInputStream(xml.getBytes("UTF-8"))
        );

        String ns = "http://xsd.gateway.sdp.digi.com";

        result.setTransactionId(
                doc.getElementsByTagNameNS(ns, "transaction_id")
                        .item(0).getTextContent()
        );

        result.setErrorCode(
                Integer.parseInt(
                        doc.getElementsByTagNameNS(ns, "error_code")
                                .item(0).getTextContent()
                )
        );

        result.setErrorDesc(
                doc.getElementsByTagNameNS(ns, "error_desc")
                        .item(0).getTextContent()
        );

        result.setErrorList(
                doc.getElementsByTagNameNS(ns, "error_list")
                        .item(0).getTextContent()
        );

        result.setSuccessList(
                doc.getElementsByTagNameNS(ns, "success_list")
                        .item(0).getTextContent()
        );

        return result;
    }
}
