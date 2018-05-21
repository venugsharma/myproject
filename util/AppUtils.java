package com.tattleup.app.tattleup.util;

/**
 * Created by Shiva on 6/21/2016.
 */

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

//import com.google.android.gms.maps.model.LatLng;

public class AppUtils {
    public static final String IsFirstTimeLogin = "isFirstTimeLogin";
    public static final String LOGIN_USER_ID = "LOGIN_USER_ID";
    public static final String LOGIN_USER_EMAIL = "LOGIN_USER_EMAIL";
    public static final String LOGIN_USER_NAME = "LOGIN_USER_NAME";
    public static final String LOGIN_USER_TOKEN = "LOGIN_USER_TOKEN";
    public static final String LOGIN_USER_VIA = "LOGIN_USER_VIA";
    public static final String IS_USER_LOGGED_IN = "isUserLoggedIn";
    public static final String USER_IMAGE = "USER_IMAGE";
    public static final String USER_IMAGE_PATH = "USER_IMAGE_PATH";
    public static final String USER_GPS_LOCATION = "USER_GPS_LOCATION";
    public static final String USER_LAST_LOCATION = "USER_LAST_LOCATION";
    public static final String LOGIN_USER_PINCODE = "LOGIN_USER_PINCODE";
    public static final String LOGIN_USER_CITY = "LOGIN_USER_CITY";
    public static final String LOGIN_USER_LOCALITY = "LOGIN_USER_LOCALITY";


    public static final String USER_SUBLOCALITY = "USER_SUBLOCALITY";
    public static final String USER_CITY = "USER_CITY";
    public static final String LOGIN_USER_DOB= "LOGIN_USER_DOB";

    public static final String LOGIN_USER_GENDER= "LOGIN_USER_GENDER";

    //http://cldilhlanaapp02.cloudapp.net/cms/mobile-auth/

    //messages
    public static final String NETWORK_NOT_AVAILABLE = "Sorry Can't connect to internet";

    private static final boolean isLoggingEnabled = true;

    public static final String LOG_TYPE_VERBOSE = "verbose";
    public static final String LOG_TYPE_WARNING = "warning";
    public static final String LOG_TYPE_ERROR = "error";

    public static final String SERVICE_HOST = "\n" + "http://api.tattleup.leoinfotech.in/";
    public static final String SERVICE_PATH = "index.php/api/version1/";
    public static final String SERVICE_API = "index.php/api/version1/";
    public static final String SERVICE_MOBILE_AUTH = "cms/mobile-auth/";
    public static final String SERVICE_BASE_API = SERVICE_HOST + SERVICE_API;
    //http://api.tattleup.leoinfotech.in/index.php/api/version1/
    public static final String SERVICE_BASE_AUTH = SERVICE_HOST + SERVICE_PATH;
    public static final String SERVICE_BASE_MOBILE_AUTH = SERVICE_HOST + SERVICE_MOBILE_AUTH;
    public static final String Login = "login";
    public static final String Logout = "logout";
    public static final String Registration = "register";

    public static final String USERS = "users/";
    public static final String MY_AUTH = "me";
    public static final String DECODE_APIS = "code/decode/";
    public static final String GET_PROCEDURE_SPECIALITY_MAPPINGS = "code/procspec/";
    public static final String USER_DETAIL = USERS + "userdetail/";
    public static final String AUTH_LIST = USERS + "authlist/";
    public static final String PROVIDER_PHOTO_URL = "http://healthadvtest.blob.core.windows.net/healthadvisor/";
    public static final String LOGIN_USER_ADDRESS = "LOGIN_USER_ADDRESS";

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    public static String getCityfromLatLong(Context context, Location location) throws IOException {
        Geocoder gcd = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        if (addresses.size() > 0)
            return addresses.get(0).getLocality();
        return "";
    }

    public static void log(Context context, String message, String type) {
        if (isLoggingEnabled) {
            switch (type) {
                case LOG_TYPE_VERBOSE:
                    Log.v(context.getClass().getSimpleName(), message);
                    break;

                case LOG_TYPE_WARNING:
                    Log.w(context.getClass().getSimpleName(), message);
                    break;

                case LOG_TYPE_ERROR:
                    Log.e(context.getClass().getSimpleName(), message);
                    break;
            }
        }
    }

    public static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit.equals("K")) {
            dist = dist * 1.609344;
        } else if (unit.equals("N")) {
            dist = dist * 0.8684;
        }
        return (Math.floor(dist * 100) / 100);
    }

    public static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    public static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

    public static String makeURL(double sourcelat, double sourcelog, double destlat, double destlog) {
        StringBuilder urlString = new StringBuilder();
        urlString.append("http://maps.googleapis.com/maps/api/directions/json");
        urlString.append("?origin=");// from
        urlString.append(Double.toString(sourcelat));
        urlString.append(",");
        urlString
                .append(Double.toString(sourcelog));
        urlString.append("&destination=");// to
        urlString
                .append(Double.toString(destlat));
        urlString.append(",");
        urlString.append(Double.toString(destlog));
        urlString.append("&sensor=false&mode=driving&alternatives=true");
        return urlString.toString();
    }

//    public static List<LatLng> decodePoly(String encoded) {
//
//        List<LatLng> poly = new ArrayList<LatLng>();
//        int index = 0, len = encoded.length();
//        int lat = 0, lng = 0;
//
//        while (index < len) {
//            int b, shift = 0, result = 0;
//            do {
//                b = encoded.charAt(index++) - 63;
//                result |= (b & 0x1f) << shift;
//                shift += 5;
//            } while (b >= 0x20);
//            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
//            lat += dlat;
//
//            shift = 0;
//            result = 0;
//            do {
//                b = encoded.charAt(index++) - 63;
//                result |= (b & 0x1f) << shift;
//                shift += 5;
//            } while (b >= 0x20);
//            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
//            lng += dlng;
//
//            LatLng p = new LatLng((((double) lat / 1E5)),
//                    (((double) lng / 1E5)));
//            poly.add(p);
//        }
//
//        return poly;
//    }

    public static String firstLetterCapitalized(String str) {
        return str.substring(0, 1).toUpperCase(Locale.getDefault()) + str.substring(1);
    }

    public static boolean isAlphanumeric(String str) {
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (!Character.isDigit(c) && !Character.isLetter(c))
                return false;
        }

        return true;
    }

    public static boolean hasDigitsOrSpecialChars(String str) {
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (!(Character.isLetter(c) || (c == ' ') || (c == '.')))
                return true;
        }

        return false;
    }


}
