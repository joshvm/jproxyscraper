package jvm.proxyscraper;

import jvm.proxyscraper.event.ProxyListener;

import java.net.Proxy;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class ProxyScraperService {

    public static int cacheSize = 100000;

    private static ExecutorService service;
    private static List<ProxyListener> listeners = new ArrayList<>();

    private static Set<Proxy> cache = new LinkedHashSet<>();

    private ProxyScraperService(){}

    public static void addListener(final ProxyListener listener){
        listeners.add(listener);
    }

    public static Set<Proxy> getCache(){
        return cache;
    }

    protected static void onProxy(final Provider provider, final Proxy proxy){
        if(cache.size() >= cacheSize)
            cache.clear();
        if(!cache.add(proxy))
            return;
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
