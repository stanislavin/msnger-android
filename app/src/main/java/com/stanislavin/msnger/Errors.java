package com.stanislavin.msnger;

/**
 * Created by Stanislav Slavin on 25/03/16.
 */
public class Errors {
    static final int ERROR_CODE_SUCCESS                      = 0;
    static final int ERROR_CODE_UNSUCCESSFUL                 = -1;
    static final int ERROR_CODE_TIMEOUT                      = -5;
    static final int ERROR_CODE_GIS_FAILURE                  = -6;
    static final int ERROR_GIS_TIMEOUT                       = -7;
    static final int ERROR_INFOBIP_TIMEOUT                   = -8;
    static final int ERROR_INFOBIP_REJECTED_NO_DESTINATION   = -9;
    static final int ERROR_INFOBIP_PENDING                   = -10;
    static final int ERROR_HOST_NOT_FOUND                    = -11;
    static final int ERROR_GIS_RESULTS_EMPTY                 = -12;
    static final int ERROR_INFOBIP_REJECTED_NO_PREFIX        = -13;

    public static String getTitleFromErrorCode(int code) {
        switch (code) {
            case ERROR_CODE_SUCCESS:
            case ERROR_INFOBIP_PENDING:
                return "Message sent";
            case ERROR_CODE_UNSUCCESSFUL:
            case ERROR_CODE_TIMEOUT:
            case ERROR_CODE_GIS_FAILURE:
            case ERROR_GIS_TIMEOUT:
            case ERROR_INFOBIP_TIMEOUT:
            case ERROR_INFOBIP_REJECTED_NO_DESTINATION:
            case ERROR_HOST_NOT_FOUND:
            case ERROR_GIS_RESULTS_EMPTY:
            case ERROR_INFOBIP_REJECTED_NO_PREFIX:
                return "Message not sent";
        }
        return "Message was sent";
    }

    public static String getMessageFromErrorCode(int code) {
        switch (code) {
            case ERROR_CODE_SUCCESS:
                return "";
            case ERROR_CODE_UNSUCCESSFUL:
                return "Please try again later.";
            case ERROR_CODE_TIMEOUT:
                return "Timed out while waiting for result. Try again later.";
            case ERROR_CODE_GIS_FAILURE:
                return "Cannot retrieve street location. Try again later.";
            case ERROR_GIS_TIMEOUT:
                return "Timed out while getting street location. Try again later.";
            case ERROR_INFOBIP_TIMEOUT:
                return "Timed out while sending message. Try again later.";
            case ERROR_INFOBIP_REJECTED_NO_DESTINATION:
                return "Please check if recipient number is correct.";
            case ERROR_INFOBIP_PENDING:
                return "Your message was submitted to operator for delivery.";
            case ERROR_HOST_NOT_FOUND:
                return "Cannot connect to the server. Please check your connectivity settings.";
            case ERROR_GIS_RESULTS_EMPTY:
                return "Cannot obtain street address. Please check your GPS settings.";
            case ERROR_INFOBIP_REJECTED_NO_PREFIX:
                return "Invalid prefix in recipient number.";
        }
        return "Message was sent";
    }
}
