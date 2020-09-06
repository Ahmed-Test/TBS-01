package sa.tamkeentech.tbs.config;

import org.apache.commons.lang3.StringUtils;

import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;

/**
 * Application constants.
 */
public final class Constants {

    // Regex for acceptable logins
    public static final String LOGIN_REGEX = "^[_.@A-Za-z0-9-]*$";

    public static final String SYSTEM_ACCOUNT = "system";
    public static final String DEFAULT_LANGUAGE = "en";
    public static final String ANONYMOUS_USER = "anonymoususer";

    // Payment
    public static final String SADAD = "SADAD";
    public static final String CREDIT_CARD = "CREDIT_CARD";
    public static final String CASH = "CASH";
    public static final String STC_PAY = "STC_PAY";

    // STS Payment codes
    public static final String STS_PAYMENT_SUCCESS_CODE = "00000";
    public static final String STS_PAYMENT_FAILURE_CODE = "10001";

    // Payfort Payment codes
    public static final String PAYFORT_PAYMENT_SUCCESS_CODE = "14";
    public static final String PAYFORT_PAYMENT_CHECK_PAYMENT_SUCCESS_CODE = "12";
    // Purchase Failure, Invalid Request
    public static final List<String> PAYFORT_PAYMENT_FAILURE_CODE = Arrays.asList("13", "00");

    public static Integer INVOICE_EXPIRY_DAYS = 1;

    public static String TRANSACTION_IDENTIFIER_BASE_64 = "dHJhbnNhY3Rpb25JZGVudGlmaWVy";

    // Default language
    public static final String DEFAULT_HEADER_LANGUAGE = "ar";
    public static final String REQUEST_PARAM_LANGUAGE = "lang";


    public static final String emailRegex = "^(.+)@(.+)$";




    /**
     * Language enumeration
     */
    public enum LANGUAGE {
        ARABIC("ar-ly", "ar"),
        ENGLISH("en", "en");
        LANGUAGE(String languageKey, String headerKey) {
            this.languageKey = languageKey;
            this.headerKey = headerKey;
        }

        public static String getLanguageByHeaderKey(String headerKey) {
            if (StringUtils.isEmpty(headerKey)) {
                return ARABIC.getLanguageKey();
            }
            switch (headerKey.toLowerCase()){
                case "en": return ENGLISH.getLanguageKey();
                default: return ARABIC.getLanguageKey();
            }
        }
        String languageKey;
        String headerKey;

        public String getLanguageKey() {
            return this.languageKey;
        }

        public String getHeaderKey() {
            return headerKey;
        }
    }

    public static final String INVOICE_DEFAULT_SEQ = "invoice_%s_id_seq";

    /**
     * The ProcessStatus enumeration.
     */
    public enum EventType {

        INVOICE_CREATE("Create invoice by client app"), //ok

        SADAD_INITIATE("Upload invoice to Sadad"), //ok
        SADAD_NOTIFICATION("Receive payment notification from Sadad"), //ok

        CREDIT_CARD_PAYMENT_REQUEST("Request new credit card payment by client app"), //ok
        CREDIT_CARD_INITIATE("Upload invoice to the online payment provider"), //ok
        CREDIT_CARD_NOTIFICATION("Receive payment notification from the online payment provider"), //ok

        INVOICE_REFUND_REQUEST("Refund request by client app"), // ok

        SADAD_REFUND_REQUEST("Send Refund request to Sadad"), // ok
        SADAD_REFUND_NOTIFICATION("Receive Refund notification from Sadad"), // ..

        CREDIT_CARD_REFUND_REQUEST("Send Refund request to the online payment provider"); // ok

        EventType (String description) {
            this.description = description;
        }

        String description;


    }

    // ZoneDateTime
    public static ZoneId RIYADH_ZONE_ID = ZoneId.of("Asia/Riyadh");
    public static String RIYADH_OFFSET = "+03:00";
    public static ZoneId UTC_ZONE_ID = ZoneId.of("UTC");

    // PayFort
    public enum ShaType {
        SHA_256,
        SHA_512
    }

    public enum PaymentOperation {
        TOKENIZATION,
        PURCHASE,
        REFUND,
        CHECK_STATUS
    }

    public enum PayfortResponseStatus {
        TOKEN_SUCCESS("200");

        PayfortResponseStatus (String status) {
            this.Status = status;
        }
        String Status;

        public String getStatus() {
            return Status;
        }
    }

    public enum STCPayMobileValidationStatus {
        UNSET,
        VALID,
        INVALID
    }

    private Constants() {
    }
}
