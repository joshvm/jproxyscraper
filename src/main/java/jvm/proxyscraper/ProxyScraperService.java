package jvm.proxyscraper;

import jvm.proxyscraper.event.ProxyListener;

import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class ProxyScraperService {

    private static ExecutorService service;
    private static List<ProxyListener> listeners = new ArrayList<>();

    private ProxyScraperService(){}

    public static void addListener(final ProxyListener listener){
        listeners.add(listener);
    }

    protected static void onProxy(final Provider provider, final Proxy proxy){
        for(final ProxyListener l : listeners)
            l.onProxy(provider, proxy);
    }

    public static void stop(){
        service.shutdown();
    }

    public static void stopNow(){
        service.shutdownNow();
    }

    public static void start(){
        Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF);
        final Provider[] providers = Provider.values();
        service = Executors.newFixedThreadPool(providers.length);
        for(final Provider provider : providers)
            service.execute(provider.getScraper());
    }
}
