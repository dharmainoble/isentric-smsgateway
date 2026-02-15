// 
// Decompiled by Procyon v0.5.32
// 

package com.isentric.bulkgateway.constant;

public class Constant
{
    public static final int STATUS_SUCCESS = 0;
    public static final int STATUS_FAILURE = 1;
    public static final String DEFAULT_DATASOURCE_NAME = "bulkgatewayDS";
    public static final String DB = "bulk_gateway";
    public static final String MODB = "umno_sms";
    public static final String TBL_SMPP_IN = "bulk_gateway.tbl_smpp_in";
    public static final String TBL_SMPP_SENT = "bulk_gateway.tbl_smpp_sent";
    public static final String TBL_MODEM_SENT = "bulk_gateway.tbl_modem_sent";
    public static final String TBL_SMPP_DN = "bulk_gateway.tbl_smpp_dn";
    public static final String TBL_SMPP_RESPONSE = "bulk_gateway.tbl_smpp_response";
    public static final String TBL_SMPP_MO = "umno_sms.umno_sms_in";
    public static final String SMPP_BIND_CONF = "/jsms_smpp.conf";
    public static final int SMPP_STATUS_INITIATED = 1;
    public static final int SMPP_STATUS_UNINITIATED = 0;
    public static final int SMPP_STATUS_CONNECTED = 1;
    public static final int SMPP_STATUS_NOTCONNECT = 0;
    public static final String SMSC_SMPP = "smpp";
    public static final int MESSAGE_TYPE_TEXT = 0;
    public static final int MESSAGE_TYPE_WAP = 1;
    public static final int MESSAGE_TYPE_OTA = 2;
    public static final int MESSAGE_TYPE_RINGTONE = 3;
    public static final int MESSAGE_TYPE_LOGO = 4;
    public static final int MESSAGE_TYPE_PICTURE = 5;
    public static final int MESSAGE_TYPE_CHINESE_TEXT = 6;
    public static final String SMPP_GUID_PROPERTY = "SMPP_GUID";
    public static final String SMPP_NAME_PROPERTY = "SMPP_NAME";
    public static final String TELCO_MAXIS = "maxis";
    public static final String TELCO_DIGI = "digi";
    public static final String TELCO_CELCOM = "celcom";
    public static final String TELCO_CLICKATELL = "clickatell";
    public static final String TELCO_MOBILE365 = "mobile365";
    public static final String TELCO_OTHER = "other";
    public static final String MOBILE_PREFIX_1 = "601";
    public static final String MOBILE_PREFIX_2 = "+601";
    public static final String EJB3_REMOTE_SMPP = "SmppMessageServiceBinderBean/remote";
    public static final String EJB3_LOCAL_SMPP = "SmppMessageServiceBinderBean/local";
    public static final String MQ_SMS_SMPP = "queue/SMPPSMSMessageQueue";
    public static final int MDB_SLEEP = 25000;
    public static final String TGA_QUERY_SUCCESS = "0000";
    public static final String INFOBID_QUERY_SUCCESS = "200";
    public static final String SIGNAL_ACTION_NONE = "05";
    public static final String SIGNAL_ACTION_LOW = "06";
    public static final String SIGNAL_ACTION_MEDIUM = "07";
    public static final String SIGNAL_ACTION_HIGH = "08";
    public static final String SIGNAL_ACTION_DELETE = "09";
    public static final String KEYSTORE = "MyMaxisKeyStore.jks";


    public static String USERID = "isentric";
    public static String PASSWORD = "isentr1";
    public static String QS_URL = "http://10.10.200.1/QSWebApp/services/QSSoapService";
    public static final String HOST_IP_LOCAL = "fermat";
    //public static final String DEFAULT_DATASOURCE_NAME = "fermat";
    public static final String EXTMT_DB = "extmt";
    public static final String TBL_QS_TRANSACTION = "tbl_qs_transaction";
    public static final String TGA_BASE = "http://localhost:8001/TGA";
    public static final String QS_WEB_APP_SOAP_SERVICE = "http://192.168.26.162:6669/QSWebApp/services/QSSoapService";
    public static final String BULK_ENCODE_QUERY = "select key_1 from key_db.key_table where row_id =1";
    public static final boolean IS_PRODUCTION = true;
    public static String DEVELOPMENT_QS_URL = "http://tga.isentric.com:10888/api/execute/url";
    public static String X_API_KEY = "8617b8d5861a4a0b00be492df3458aeb3f5f6183";


}
