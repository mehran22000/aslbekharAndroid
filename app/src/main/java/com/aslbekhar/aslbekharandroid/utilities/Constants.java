package com.aslbekhar.aslbekharandroid.utilities;

import java.text.DecimalFormat;

/**
 * Created by Amin on 11/16/2014.
 * be cause this app will be used for different websites, it needs different Constants too
 */
public class Constants {

    public static String APP_VERSION = "2.0.0";


    public static String WEB_SERVER ="https://buyoriginal.herokuapp.com/";

    //AMIN: urls:
    public static String AMIN_TEST_URL ="http://www.aminkeshavarzian.ir/amintest.txt";

    // WS1
    public static String CITY_STORE_URL =
            WEB_SERVER + "services/v1/dev/stores/storelist/city/";

    // WS15
    public static String BRAND_LIST_URL =
            WEB_SERVER + "services/v1/dev/brands/brandlist";

    // WS2
    public static String STORESLIST_NEARBY =
            WEB_SERVER + "services/v1/dev/stores/storelist/all/";

    // WS3
    public static String DEALS_NEARBY_URL =
            WEB_SERVER + "services/v1/dev/stores/storelist/discounts/";

    // WS4
    public static String BRAND_VERIFICATION_URL =
            WEB_SERVER + "services/v1/dev/brands/verification/";

    // WS5
    public static String REGISTER_ANDROID_DEVICE_LINK =
            WEB_SERVER + "services/v1/dev/users/register/android";

    // WS6
    public static String SEND_ANALYTICS_LINK =
            WEB_SERVER + "services/v1/dev/users/interests/base64";

    // WS7
    public static String SEND_ANALYTICS_ADVERTISEMENT_LINK =
            WEB_SERVER + "services/v1/dev/users/business/ad/displays";

    // WS8
    public static String LOGIN_URL =
            WEB_SERVER + "services/v1/dev/users/business/login";

    // WS11
    public static String UPDATE_USER_URL =
            WEB_SERVER + "services/v1/dev/users/business/updateuser";

    // WS16
    public static String REGISTER_USER =
            WEB_SERVER + "services/v1/dev/users/business/adduser";

    // WS17
    public static String CHECK_VERSION =
            WEB_SERVER + "services/v1/dev/appInfo/version/android";

    // D2
    public static String BRAND_LOGO_URL = WEB_SERVER + "images/logos/";

    // D ?
    public static String FULLSCREEN_ADD_IMAGE_URL = WEB_SERVER + "images/ads/";


    //ADVERTISEMENT LINKS
    public static String CITY_TO_CAT_FULL_AD = WEB_SERVER + "images/ads/fullpage/ad.";
    public static String CAT_BANNER_AD = WEB_SERVER + "images/ads/banner/ad.";
    public static String DEALS_BANNER_AD = WEB_SERVER + "images/ads/banner/deals/ad.";
    public static String NEARME_BANNER_AD = WEB_SERVER + "images/ads/banner/nearme/ad.";
    public static String BRAND_VERIFICATION_IMAGE = WEB_SERVER + "images/verifications/";


    //AMIN: Name Strings:
    public static String LOG_TAG = "****AMIN ** DEBUG****";
    public static String BRAND_LIST_DOWNLOAD = "BRAND_LIST_DOWNLOAD";
    public static String DOWNLOAD = "DOWNLOAD";
    public static String BRAND_VERIFICATION_DOWNLOAD = "BRAND_VERIFICATION_DOWNLOAD";
    public static String PASSWORD_ERROR = "err_invalid_password";
    public static String SUCCESS = "success";



    //AMIN Shared Pref Keys
    public static String SP_FILE_NAME_BASE = "sp_file_base";
    public static String FALSE = "FALSE";
    public static String TRUE = "TRUE";
    public static String YES = "YES";
    public static String NO = "NO";
    public static String LAST_LAT = "LAST_LAT";
    public static String LAST_LONG = "LAST_LONG";
    public static String IS_LOGGED_IN = "IS_LOGGED_IN";
    public static String USER_INFO = "USER_INFO";
    public static String EMAIL = "EMAIL";
    public static String PASSWORD = "PASSWORD";
    public static String CITY_LIST = "CITY_LIST";
    public static String STORE_LIST = "STORE_LIST";
    public static String CAT_LIST = "CAT_LIST";
    public static String BRAND_LIST = "CAT_LIST";
    public static String SAVED_ANALYTICS = "SAVED_ANALYTICS";
    public static String STORE_LIST_FILE_POSTFIX = "-StoreList.txt";
    public static String CAT_LIST_FILE_POSTFIX = "-CatList.txt";
    public static String SENT_TOKEN_TO_SERVER = "SENT_TOKEN_TO_SERVER";
    public static String REGISTRATION_COMPLETE = "REGISTRATION_COMPLETE";
    public static String DEVICE_ID = "DEVICE_ID";
    public static String REGISTERED_DEVICE_ID = "REGISTERED_DEVICE_ID";
    public static String GPS_ON_OR_OFF = "GPS_ON_OR_OFF";
    public static String PLAY_SERVICES_ON_OR_OFF = "PLAY_SERVICES_ON_OR_OFF";
    public static String LOCATION_PERMISSION = "LOCATION_PERMISSION";
    public static String DATA_PROCESSED_OR_NOT = "DATA_PROCESSED_OR_NOT";
    public static String LAST_CITY_CODE = "LAST_CITY_CODE";
    public static String LAST_CITY_ENGLISH_NAME = "LAST_CITY_ENGLISH_NAME";
    public static String RESULT = "RESULT";

    // ADVERTISEMENT TYPES:
    public static String ADVERTISE_MAIN = "MAIN";
    public static String ADVERTISE_CATEGORIES = "CATEGORIES";
    public static String ADVERTISE_STORES = "STORES";
    public static String ADVERTISE_BRANDS = "BRANDS";
    public static String ADVERTISE_DETAILS = "DETAILS";
    public static String ADVERTISE_NEARME = "NEARME";
    public static String ADVERTISE_NEARME_DETAILS = "NEARME_DETAILS";
    public static String ADVERTISE_DEALS = "DEALS";
    public static String ADVERTISE_DEALS_DETAIL = "DEALS_DETAIL";
    public static String BANNER  = "BANNER";
    public static String FULL = "FULL";


    //AMIN INTENT KEYS:
    public static String PARENT_ID = "PARENT_ID";
    public static String TITLE = "TITLE";
    public static String WORK_HOUR = "WORK_HOUR";
    public static String TELL = "TELL";
    public static String ADDRESS = "ADDRESS";
    public static String LOGO = "LOGO";
    public static String DISCOUNT = "DISCOUNT";
    public static String VERIFIED = "VERIFIED";
    public static String STORE_DETAILS = "STORE_DETAILS";
    public static String IMAGE = "IMAGE";
    public static String NORMAL_OR_DEAL = "NORMAL_OR_DEAL";
    public static String DEAL = "DEAL";
    public static String NORMAL = "NORMAL";
    public static String DISTANCE = "DISTANCE";
    public static String NOTIFICATION_SERVICE_TAG = "NOTIFICATION_SERVICE_TAG";
    public static String IS_FROM_NOTIFICATION = "IS_FROM_NOTIFICATION";
    public static String NOTIFICATION_TYPE = "NOTIFICATION_TYPE";
    public static String NOTIFICATION_STRING = "NOTIFICATION_STRING";
    public static String CITY_CODE = "CITY_CODE";
    public static String DEFAULT_CITY_CODE = "021";
    public static String CITY_NAME = "CITY_NAME";
    public static String CITY = "CITY";
    public static String CAT_NAME = "CAT_NAME";
    public static String CAT_NUMBER = "CAT_NUMBER";
    public static String BRAND_ID = "BRAND_ID";
    public static String BRAND_NAME = "BRAND_NAME";
    public static String VERIFICATION_TIPS = "VERIFICATION_TIPS";
    public static String OFFLINE_MODE = "OFFLINE_MODE";
    public static String ADD_TO_BACK = "ADD_TO_BACK";
    public static String CHANGE_PASSWORD = "CHANGE_PASSWORD";
    public static String REGISTRATION = "REGISTRATION";
    public static String STORE_DISCOUNT_PERCENTAGE = "STORE_DISCOUNT_PERCENTAGE";
    public static String STORE_DISTANCE = "STORE_DISTANCE";
    public static String STORE_SALE_END = "STORE_SALE_END";
    public static String STORE_SALE_START = "STORE_SALE_START";
    

    public static String LATITUDE = "LATITUDE";
    public static String LONGITUDE = "LONGITUDE";
    public static String LIST_OR_SINGLE_STORE = "LIST_OR_SINGLE_STORE";
    public static String CATEGORY = "category";
    public static String BRAND = "brand";
    public static String BRAND_STORE = "Brand_Store";
    public static String NEAR_ME_BRAND_STORE = "NearMe_Brand_Store";
    public static String NEAR_ME_BRAND = "NearMe_Brand";
    public static String DEALS_BRAND = "Deal_Brand";
    public static String DEALS_BRAND_STORE = "Deal_Brand_Store";
    public static String VERIFICATION = "Verification";
    public static int LIST_OF_STORES = 1;
    public static int SINGLE_STORE = 2;



    // 0 : new items
    // 1 : top items
    public static String LIST_TYPE = "LIST_TYPE";
    public static String GROUP_ID = "GROUP_ID";
    public static String HOME_LIST_TYPE = "HOME_LIST_TYPE";
    public static String PRODUCT_ID = "PRODUCT_ID";
    public static String MODEL_JSON = "MODEL_JSON";


    //AMIN Numbers:
    public static int DEFAULT_DISTANCE = 10;
    public static int ADVERTISEMENT_TIMEOUT = 3500;
    public static int ADVERTISEMENT_MAX_COUNT = 4;
    public static int ADVERTISEMENT_VIEW_TIMEOUT = 3000;
    public static int CHECK_FOR_NEW_VERSION_TIMEOUT = 2000;
    public static int NOTIFICATION_CHECK_INTERVAL = 30 * 60 * 1000;
    public static final int VOLLEY_TIME_OUT = 25000;
    public static int COUNT = 7;
    public static int TRANSITION_DURATION = 500;


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
