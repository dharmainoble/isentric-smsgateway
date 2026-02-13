package com.isentric.smsserver.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isentric.smsserver.model.avatar.ExtMtPushReceive;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Service for calling external APIs (BulkGateway and others)
 * Uses asynchronous HTTP calls to prevent blocking
 */
@Service
@Slf4j
public class ExternalApiService {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private static final String BULKGATEWAY_MESSAGES_ENDPOINT = "http://localhost:8090/api/v1/messages/send";
    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(60); // 60 second timeout
    private static final Duration CONNECT_TIMEOUT = Duration.ofSeconds(30); // 30 second connect timeout

    public ExternalApiService(ObjectMapper objectMapper) {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(CONNECT_TIMEOUT)
                .build();
        this.objectMapper = objectMapper;
    }

    /**
     * Send SMS to BulkGateway using REST API (Asynchronous)
     * Maps ExtMtPushReceive data to the required payload format
     * Non-blocking call - returns immediately without waiting for response
     *
     * @param mtRecord The ExtMtPushReceive record containing SMS details
     * @param currentMsisdn The current MSISDN being processed
     * @return true (always returns true since this is async)
     */
    public boolean sendSmsToBulkGateway(ExtMtPushReceive mtRecord, String currentMsisdn) {
        // Fire and forget - send async in background thread
        sendSmsToBulkGatewayAsync(mtRecord, currentMsisdn);
        return true; // Return immediately without waiting for response
    }

    /**
     * Asynchronous HTTP call to BulkGateway
     * Runs in a separate thread pool to prevent blocking the main request
     */
    @Async
    public void sendSmsToBulkGatewayAsync(ExtMtPushReceive mtRecord, String currentMsisdn) {
        try {
            // Create payload from mtRecord
            Map<String, Object> payload = createBulkGatewayPayload(mtRecord, currentMsisdn);

            // Convert payload to JSON
            String jsonPayload = objectMapper.writeValueAsString(payload);
            log.debug("Sending SMS to BulkGateway - MTID: {}, MSISDN: {}", mtRecord.getMtid(), currentMsisdn);

            // Create HTTP request with longer timeout
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BULKGATEWAY_MESSAGES_ENDPOINT))
                    .header("Content-Type", "application/json")
                    .header("User-Agent", "ExtMTPAPI/1.0")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .timeout(REQUEST_TIMEOUT)
                    .build();

            // Send request asynchronously
            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(response -> handleBulkGatewayResponse(response, mtRecord, currentMsisdn))
                    .exceptionally(ex -> {
                        log.error("Async error sending SMS to BulkGateway for MTID: {} - {}",
                                mtRecord.getMtid(), ex.getMessage());
                        return null;
                    });

        } catch (Exception e) {
            log.error("Error creating BulkGateway request for MTID: {}", mtRecord.getMtid(), e);
        }
    }

    /**
     * Handle response from BulkGateway
     */
    private void handleBulkGatewayResponse(HttpResponse<String> response, ExtMtPushReceive mtRecord, String currentMsisdn) {
        if (response.statusCode() == 200 || response.statusCode() == 201) {
            log.info("SMS forwarded to BulkGateway successfully - MTID: {}, MSISDN: {}, Status: {}",
                    mtRecord.getMtid(), currentMsisdn, response.statusCode());
        } else {
            log.warn("BulkGateway returned status {} for MTID: {} - Response: {}",
                    response.statusCode(), mtRecord.getMtid(), response.body());
        }
    }

    /**
     * Create payload map from ExtMtPushReceive record
     * Maps database fields to API expected format
     */
    private Map<String, Object> createBulkGatewayPayload(ExtMtPushReceive mtRecord,String currentMsisdn) {
        Map<String, Object> payload = new HashMap<>();
        System.out.println("ssssssssssssss");
        System.out.println(checkMalaysiaOperatorHLR(currentMsisdn));
        payload.put("groupId", "api_request");
        payload.put("ip", "127.0.0.1");
        payload.put("smsc", "smpp");
        payload.put("telco", getSMPPTelco(currentMsisdn));
        payload.put("smppName", "WSDL_DEFAULT");
        payload.put("smppConfig", "default");
        payload.put("moid", mtRecord.getMtid());
        payload.put("sender", mtRecord.getSmsisdn() != null ? mtRecord.getSmsisdn() : "API");
        payload.put("recipient", currentMsisdn);
        payload.put("senderType", 0);
        payload.put("keyword", mtRecord.getCustid() != null ? mtRecord.getCustid() : "");
        payload.put("message", mtRecord.getDataStr() != null ? mtRecord.getDataStr() : "");
        payload.put("messageType", mtRecord.getProductType() != null ? mtRecord.getProductType() : 0);
        payload.put("price", mtRecord.getMtprice() != null ? mtRecord.getMtprice() : "0.00");
        payload.put("callbackURL", "");
        payload.put("shortcode", mtRecord.getShortcode());
        payload.put("userGroup", mtRecord.getCustid());
        payload.put("queueSequence", 0);
        payload.put("cFlag", 0);

        return payload;
    }

    /**
     * Map shortcode to telco name
     * This is a basic mapping - extend as needed
     */
    private String getTelcoFromShortcode(String shortcode) {
        if (shortcode == null) {
            return "Unknown";
        }

        // Add mappings based on shortcodes
        switch (shortcode) {
            case "10086":
            case "10010":
                return "Digi";
            case "10088":
            case "10011":
                return "Maxis";
            case "10089":
            case "10012":
                return "Celcom";
            case "10087":
            case "10013":
                return "U-Mobile";
            default:
                return "Unknown";
        }
    }

    public static Map<String, String> checkMalaysiaOperatorHLR(String msisdn) {

        Map<String, String> result = new HashMap<>();

        try {
            String user = "arstone";
            String pass = "scen-29";
            String output = "json";

            String url =
                    "http://193.105.74.159/api/hlr/sync" +
                            "?user=" + user +
                            "&pass=" + pass +
                            "&destination=" + msisdn +
                            "&output=" + output;

            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(url, String.class);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);

            String mccmnc = root.path("mccmnc").asText();
            String isPorted = root.path("is_ported").asText();
            String stat = root.path("stat").asText();

            String operator = "Unknown";

            if (mccmnc.startsWith("502")) { // Malaysia
                switch (mccmnc) {
                    case "50212":
                    case "50217":
                        operator = "Maxis";
                        break;
                    case "50219":
                    case "50201":
                        operator = "Celcom";
                        break;
                    case "50216":
                    case "50213":
                        operator = "Digi";
                        break;
                    case "50218":
                        operator = "U Mobile";
                        break;
                    case "50211":
                        operator = "TM Unifi";
                        break;
                    case "50210":
                        operator = "Tune Talk";
                        break;
                    case "502195":
                        operator = "XOX";
                        break;
                    case "502152":
                        operator = "YTL Yes";
                        break;
                }
            }

            result.put("msisdn", msisdn);
            result.put("status", stat);
            result.put("mccmnc", mccmnc);
            result.put("operator", operator);
            result.put("is_ported", isPorted);

        } catch (Exception e) {
            result.put("error", e.getMessage());
        }

        return result;
    }



    public String getSMPPTelco(String msisdn) {
        if (!msisdn.startsWith("6012") && !msisdn.startsWith("012") && !msisdn.startsWith("+6012")) {
            if (!msisdn.startsWith("6017") && !msisdn.startsWith("017") && !msisdn.startsWith("+6017")) {
                if (!msisdn.startsWith("60142") && !msisdn.startsWith("0142") && !msisdn.startsWith("+60142")) {
                    if (!msisdn.startsWith("6016") && !msisdn.startsWith("016") && !msisdn.startsWith("+6016")) {
                        if (!msisdn.startsWith("60143") && !msisdn.startsWith("0143") && !msisdn.startsWith("+60143")) {
                            if (!msisdn.startsWith("60146") && !msisdn.startsWith("0146") && !msisdn.startsWith("+60146")) {
                                if (!msisdn.startsWith("60149") && !msisdn.startsWith("0149") && !msisdn.startsWith("+60149")) {
                                    if (!msisdn.startsWith("6019") && !msisdn.startsWith("019") && !msisdn.startsWith("+6019")) {
                                        if (!msisdn.startsWith("6013") && !msisdn.startsWith("013") && !msisdn.startsWith("+6013")) {
                                            if (!msisdn.startsWith("60148") && !msisdn.startsWith("0148") && !msisdn.startsWith("+60148")) {
                                                if (!msisdn.startsWith("6018") && !msisdn.startsWith("018") && !msisdn.startsWith("+6018")) {
                                                    if (!msisdn.startsWith("011150") && !msisdn.startsWith("0111501") && !msisdn.startsWith("011152") && !msisdn.startsWith("011153") && !msisdn.startsWith("011154") && !InRange(msisdn)) {
                                                        if (!msisdn.startsWith("6010333") && !msisdn.startsWith("010333") && !msisdn.startsWith("+6010333")) {
                                                            if (!msisdn.startsWith("6010") && !msisdn.startsWith("010") && !msisdn.startsWith("+6010")) {
                                                                if (!msisdn.startsWith("60") && !msisdn.startsWith("+60")) {
                                                                    if (!msisdn.startsWith("011100") && !msisdn.startsWith("011101") && !msisdn.startsWith("011102") && !msisdn.startsWith("011103") && !msisdn.startsWith("011104")) {
                                                                        if (!msisdn.startsWith("011105") && !msisdn.startsWith("011106") && !msisdn.startsWith("011107") && !msisdn.startsWith("011108") && !msisdn.startsWith("011109")) {
                                                                            if (!msisdn.startsWith("011110") && !msisdn.startsWith("011111") && !msisdn.startsWith("011112") && !msisdn.startsWith("011113") && !msisdn.startsWith("011114")) {
                                                                                if (!msisdn.startsWith("011120") && !msisdn.startsWith("011121") && !msisdn.startsWith("011122") && !msisdn.startsWith("011123") && !msisdn.startsWith("011124") && !msisdn.startsWith("01112")) {
                                                                                    if (!msisdn.startsWith("011130") && !msisdn.startsWith("011131") && !msisdn.startsWith("011132") && !msisdn.startsWith("011133") && !msisdn.startsWith("011134")) {
                                                                                        if (!msisdn.startsWith("011135") && !msisdn.startsWith("011136") && !msisdn.startsWith("011137") && !msisdn.startsWith("011138") && !msisdn.startsWith("011139")) {
                                                                                            if (!msisdn.startsWith("011160") && !msisdn.startsWith("011161") && !msisdn.startsWith("011162") && !msisdn.startsWith("011163") && !msisdn.startsWith("011164") && !msisdn.startsWith("01116")) {
                                                                                                if (!msisdn.startsWith("011170") && !msisdn.startsWith("011171") && !msisdn.startsWith("011172") && !msisdn.startsWith("011173") && !msisdn.startsWith("011174")) {
                                                                                                    if (!msisdn.startsWith("011180") && !msisdn.startsWith("011181") && !msisdn.startsWith("011182") && !msisdn.startsWith("011183") && !msisdn.startsWith("011184")) {
                                                                                                        if (!msisdn.startsWith("011190") && !msisdn.startsWith("011191") && !msisdn.startsWith("011192") && !msisdn.startsWith("011193") && !msisdn.startsWith("011194") && !msisdn.startsWith("01119") && !msisdn.startsWith("0159")) {
                                                                                                            //ArrayList creditList = CreditSession.getSharedCredit().getCreditList();
                                                                                                            //creditObject creditObj = null;

                                                                                                           // for(int i = 0; i < creditList.size(); ++i) {
                                                                                                            //    creditObj = (creditObject)creditList.get(i);
                                                                                                            //    if (msisdn.startsWith(creditObj.getPrefix())) {
                                                                                                                   // return creditObj.getCountry();
                                                                                                             //   }
                                                                                                            //}

                                                                                                            return "other";
                                                                                                        } else {
                                                                                                            return "Celcom";
                                                                                                        }
                                                                                                    } else {
                                                                                                        return "Telekom";
                                                                                                    }
                                                                                                } else {
                                                                                                    return "YTL";
                                                                                                }
                                                                                            } else {
                                                                                                return "digi";
                                                                                            }
                                                                                        } else {
                                                                                            return "Baraka";
                                                                                        }
                                                                                    } else {
                                                                                        return "XOX_Com";
                                                                                    }
                                                                                } else {
                                                                                    return "maxis";
                                                                                }
                                                                            } else {
                                                                                return "umobile";
                                                                            }
                                                                        } else {
                                                                            return "Red_Tone";
                                                                        }
                                                                    } else {
                                                                        return "Packet_One";
                                                                    }
                                                                } else {
                                                                    return "maxis";
                                                                }
                                                            } else {
                                                                return "celcom";
                                                            }
                                                        } else {
                                                            return "celcom";
                                                        }
                                                    } else {
                                                        return "Tune_Talk";
                                                    }
                                                } else {
                                                    return "umobile";
                                                }
                                            } else {
                                                return "celcom";
                                            }
                                        } else {
                                            return "celcom";
                                        }
                                    } else {
                                        return "celcom";
                                    }
                                } else {
                                    return "digi";
                                }
                            } else {
                                return "digi";
                            }
                        } else {
                            return "digi";
                        }
                    } else {
                        return "digi";
                    }
                } else {
                    return "maxis";
                }
            } else {
                return "maxis";
            }
        } else {
            return "maxis";
        }
    }

    public static boolean InRange(String msisdnString) {
        boolean inRange = false;

        try {
            if (msisdnString.startsWith("0105") || msisdnString.startsWith("0107") || msisdnString.startsWith("0108") || msisdnString.startsWith("01115")) {
                inRange = true;
            }

            return inRange;
        } catch (Exception var3) {
            return false;
        }
    }

}

