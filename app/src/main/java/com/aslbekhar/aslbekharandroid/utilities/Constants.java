package com.aslbekhar.aslbekharandroid.utilities;

import java.text.DecimalFormat;

/**
 * Created by Amin on 11/16/2014.
 * be cause this app will be used for different websites, it needs different Constants too
 */
public class Constants {

    public static String WEB_SERVER ="https://buyoriginal.herokuapp.com/";

    //AMIN: urls:
    public static String AMIN_TEST_URL ="http://www.aminkeshavarzian.ir/amintest.txt";

    // WS1
    public static String CITY_STORE_URL = WEB_SERVER + "services/v1/dev/stores/storelist/city/";

    // WS15
    public static String BRAND_LIST_URL = WEB_SERVER + "services/v1/dev/brands/brandlist";

    // WS2
    public static String STORESLIST_NEARBY = WEB_SERVER + "services/v1/dev/stores/storelist/all/";

    // WS3
    public static String DEALS_NEARBY_URL = WEB_SERVER + "services/v1/dev/stores/storelist/discounts/";

    // WS4
    public static String BRAND_VERIFICATION_URL = WEB_SERVER + "services/v1/dev/brands/verification/";

    // D2
    public static String BRAND_LOGO_URL = WEB_SERVER + "images/logos/";

    //AMIN: Name Strings:
    public static String LOG_TAG = "****AMIN ** DEBUG****";
    public static String BRAND_LIST_DOWNLOAD = "BRAND_LIST_DOWNLOAD";
    public static String DOWNLOAD = "DOWNLOAD";
    public static String BRAND_VERIFICATION_DOWNLOAD = "BRAND_VERIFICATION_DOWNLOAD";



    //AMIN Shared Pref Keys
    public static String SP_FILE_NAME_BASE = "sp_file_base";
    public static String FALSE = "FALSE";
    public static String TRUE = "TRUE";
    public static String YES = "YES";
    public static String NO = "NO";
    public static String CITY_LIST = "CITY_LIST";
    public static String STORE_LIST = "STORE_LIST";
    public static String CAT_LIST = "CAT_LIST";
    public static String BRAND_LIST = "CAT_LIST";
    public static String STORE_LIST_FILE_POSTFIX = "-StoreList.txt";
    public static String SENT_TOKEN_TO_SERVER = "SENT_TOKEN_TO_SERVER";
    public static String REGISTRATION_COMPLETE = "REGISTRATION_COMPLETE";
    public static String GPS_ON_OR_OFF = "GPS_ON_OR_OFF";



    //AMIN INTENT KEYS:
    public static String PARENT_ID = "PARENT_ID";
    public static String TITLE = "TITLE";
    public static String WORK_HOUR = "WORK_HOUR";
    public static String TELL = "TELL";
    public static String ADDRESS = "ADDRESS";
    public static String LOGO = "LOGO";
    public static String DISCOUNT = "DISCOUNT";
    public static String VERIFIED = "VERIFIED";
    public static String STORE_TYPE = "STORE_TYPE";
    public static String STORE_TYPE_VERIFIED = "STORE_TYPE_VERIFIED";
    public static String STORE_TYPE_VERIFIED_DISCOUNT = "STORE_TYPE_VERIFIED_DISCOUNT";
    public static String STORE_TYPE_DISCOUT = "STORE_TYPE_DISCOUT";
    public static String STORE_TYPE_NONE = "STORE_TYPE_NONE";
    public static String MAX_NUMBER = "MAX_NUMBER";
    public static String HEADER_IMAGE = "HEADER_IMAGE";
    public static String LINK = "LINK";
    public static String POSITION = "POSITION";
    public static String FROM_SPLASH = "FROM_SPLASH";
    public static String TYPE = "TYPE";
    public static String IMAGE = "IMAGE";
    public static String NOTIFICATION_SERVICE_TAG = "NOTIFICATION_SERVICE_TAG";
    public static String IS_FROM_NOTIFICATION = "IS_FROM_NOTIFICATION";
    public static String NOTIFICATION_TYPE = "NOTIFICATION_TYPE";
    public static String NOTIFICATION_STRING = "NOTIFICATION_STRING";
    public static String CITY_CODE = "CITY_CODE";
    public static String DEFAULT_CITY_CODE = "021";
    public static String CITY_NAME = "CITY_NAME";
    public static String CAT_NAME = "CAT_NAME";
    public static String BRAND_ID = "BRAND_ID";
    public static String OFFLINE_MODE = "OFFLINE_MODE";

    public static String LATITUDE = "LATITUDE";
    public static String LONGITUDE = "LONGITUDE";
    public static String MAP_TYPE = "MAP_TYPE";
    public static int MAP_TYPE_SHOW_NEAR_BY = 1;
    public static int MAP_TYPE_SHOW_SINGLE_STORE = 2;



    // 0 : new items
    // 1 : top items
    public static String LIST_TYPE = "LIST_TYPE";
    public static String GROUP_ID = "GROUP_ID";
    public static String HOME_LIST_TYPE = "HOME_LIST_TYPE";
    public static String PRODUCT_ID = "PRODUCT_ID";
    public static String MODEL_JSON = "MODEL_JSON";


    //AMIN Numbers:
    public static int DIFAULT_DISTANCE = 10;
    public static int NOTIFICATION_CHECK_INTERVAL = 30 * 60 * 1000;
    public static final int VOLLEY_TIME_OUT = 25000;
    public static int COUNT = 7;


    public static final int NOTIFICATION_ID = 900;
    public static boolean DEBUG = true;

    public static String persianNumbers(String num){
        try {
            char[] persianNumbers = {'٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩'};
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < num.length(); i++) {
                if (Character.isDigit(num.charAt(i))) {
                    builder.append(persianNumbers[(int) (num.charAt(i)) - 48]);
                } else {
                    builder.append(num.charAt(i));
                }
            }
            return builder.toString();
        } catch (Exception e) {
            return num;
        }
    }

    public static String formatPrice(String num) {
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        return formatter.format(Float.valueOf(num));
    }

}
