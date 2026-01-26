//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.isentric.bulkgateway.webservice;

import java.rmi.RemoteException;
import java.util.Calendar;

public class SDPServicesHttpBindingImpl implements SDPServicesInterface {
    public VSDBQueryResult vsdbAdd(String login_name, String service_id, String cp_id, String destination_mobtel, String keyword, Calendar sub_exp_date, String description, AdditionalInfo[] array_of_info) throws RemoteException {
        return null;
    }

    public VSDBQueryResult vsdbRnw(String login_name, String service_id, String cp_id, String destination_mobtel, String sub_id, String keyword, Calendar sub_exp_date, AdditionalInfo[] array_of_info) throws RemoteException {
        return null;
    }

    public VSDBQueryResult vsdbTerm(String login_name, String service_id, String cp_id, String destination_mobtel, String keyword, AdditionalInfo[] array_of_info) throws RemoteException {
        return null;
    }

    public VSDBQueryResult vsdbQuery(String login_name, String service_id, String cp_id, String destination_mobtel, String sub_id, String keyword, AdditionalInfo[] array_of_info) throws RemoteException {
        return null;
    }

    public SyncResult subQueryFile(String login_name, String service_id, String cp_id, AdditionalInfo[] array_of_info) throws RemoteException {
        return null;
    }

    public SubQueryResult subQuery(String login_name, String service_id, String cp_id, String destination_mobtel, String keyword, AdditionalInfo[] array_of_info) throws RemoteException {
        return null;
    }

    public SDPResult smsMt(String login_name, String service_id, String cp_id, String price_code, String charge_party, String source_mobtel, String destination_mobtel, String sub_id, String sender_name, String keyword, String interactive_session_ind, String interactive_term_session, int status, String transaction_id, String ref_id, String short_code_suffix_ind, String short_code_suffix, int notification_ind, String response_url, SMSContent[] sms_contents, AdditionalInfo[] array_of_info) throws RemoteException {
        return null;
    }

    public SDPResult smsBulk(String login_name, String service_id, String cp_id, String destination_mobtel, String sender_name, String ref_id, int notification_ind, String response_url, SMSContent[] sms_contents, AdditionalInfo[] array_of_info) throws RemoteException {
        return null;
    }

    public SDPResult mmsMt(String login_name, String service_id, String cp_id, String price_code, String charge_party, String source_mobtel, String destination_mobtel, String sub_id, String sender_name, String keyword, int status, String transaction_id, String ref_id, int notification_ind, String response_url, String subject, MMSContent[] mms_contents, AttachmentType attachment, AdditionalInfo[] array_of_info) throws RemoteException {
        return null;
    }

    public SDPResult mmsBulk(String login_name, String service_id, String cp_id, String destination_mobtel, String sender_name, String subject, String ref_id, int notification_ind, String response_url, MMSContent[] mms_contents, AttachmentType attachment, AdditionalInfo[] array_of_info) throws RemoteException {
        return null;
    }

    public SDPResult wappush(String login_name, String service_id, String cp_id, String price_code, String destination_mobtel, String sub_id, String keyword, int status, String transaction_id, String ref_id, int notification_ind, String response_url, WAPPushContent wappush_content, AdditionalInfo[] array_of_info) throws RemoteException {
        return null;
    }

    public SDPResult wappushBulk(String login_name, String service_id, String cp_id, String destination_mobtel, String ref_id, int notification_ind, String response_url, WAPPushContent wappush_content, AdditionalInfo[] array_of_info) throws RemoteException {
        return null;
    }

    public SDPResult subRmdMt(String login_name, String service_id, String cp_id, String price_code, String destination_mobtel, String sub_id, String sender_name, String keyword, int status, String ref_id, int notification_ind, String response_url, SMSContent[] sms_contents) throws RemoteException {
        return null;
    }

    public SDPResult subRnwMt(String login_name, String service_id, String cp_id, String price_code, String destination_mobtel, String sub_id, String sender_name, String keyword, int status, String ref_id, int notification_ind, String response_url, String sub_rem_txnid, SMSContent[] sms_contents) throws RemoteException {
        return null;
    }

    public SDPResult subRegMt(String login_name, String service_id, String cp_id, String destination_mobtel, String sender_name, String keyword, String transaction_id, String ref_id, String response_url, AdditionalInfo[] array_of_info) throws RemoteException {
        return null;
    }

    public SDPResult subSmsMt(String login_name, String service_id, String cp_id, String destination_mobtel, String sender_name, String keyword, String ref_id, int notification_ind, String response_url, SMSContent[] sms_contents, AdditionalInfo[] array_of_info) throws RemoteException {
        return null;
    }

    public SDPResult subMmsMt(String login_name, String service_id, String cp_id, String destination_mobtel, String sender_name, String keyword, String ref_id, int notification_ind, String response_url, String subject, MMSContent[] mms_contents, AttachmentType attachment, AdditionalInfo[] array_of_info) throws RemoteException {
        return null;
    }

    public SDPResult subWappush(String login_name, String service_id, String cp_id, String destination_mobtel, String keyword, String ref_id, int notification_ind, String response_url, WAPPushContent wappush_content, AdditionalInfo[] array_of_info) throws RemoteException {
        return null;
    }

    public SDPResult subTermSvc(String login_name, String service_id, String cp_id, String destination_mobtel, String ref_id, AdditionalInfo[] array_of_info) throws RemoteException {
        return null;
    }

    public SDPResult subBcSms(String login_name, String service_id, String cp_id, String sender_name, String keyword, String ref_id, String sub_bc_id, int sub_bc_valid_hour, String sub_bc_again, Calendar sub_bc_start_time, SMSContent[] sms_contents, AdditionalInfo[] array_of_info) throws RemoteException {
        return null;
    }

    public SDPResult subBcMms(String login_name, String service_id, String cp_id, String sender_name, String keyword, String ref_id, String sub_bc_id, int sub_bc_valid_hour, String sub_bc_again, Calendar sub_bc_start_time, String subject, MMSContent[] mms_contents, AttachmentType attachment, AdditionalInfo[] array_of_info) throws RemoteException {
        return null;
    }

    public SDPResult subBcWappush(String login_name, String service_id, String cp_id, String keyword, String ref_id, String sub_bc_id, int sub_bc_valid_hour, String sub_bc_again, Calendar sub_bc_start_time, WAPPushContent wappush_content, AdditionalInfo[] array_of_info) throws RemoteException {
        return null;
    }
}
