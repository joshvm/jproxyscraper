package jvm.proxyscraper.util;

import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;

public final class ProxyUtils {

    private static final String DEFAULT_ADDRESS = "http://wwww.blanksite.com";
    private static final int DEFAULT_TIMEOUT = 10000;

    private ProxyUtils(){}

    public static boolean test(final String address, final int timeout, final Proxy proxy){
        try{
            final URL url = new URL(address);
            final HttpURLConnection con = (HttpURLConnection) url.openConnection(proxy);
            con.setConnectTimeout(timeout);
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            con.connect();
            con.disconnect();
            return true;
        }catch(Exception ex){
            return false;
        }
    }

    public static boolean test(final String address, final Proxy proxy){
        return test(address, DEFAULT_TIMEOUT, proxy);
    }

    public static boolean test(final Proxy proxy){
        return test(DEFAULT_ADDRESS, DEFAULT_TIMEOUT, proxy);
    }

}
