package jvm.proxyscraper.test;

import jvm.proxyscraper.Provider;
import jvm.proxyscraper.ProxyScraperService;
import jvm.proxyscraper.event.ProxyListener;

import java.net.Proxy;
import java.util.Date;

public class Test {

    public static void main(String[] args) throws Exception{
        ProxyScraperService.addListener(
                new ProxyListener() {
                    public void onProxy(final Provider provider, final Proxy proxy){
                        System.out.printf("[%s - %s] Scraped Proxy %s\n", new Date(), provider, proxy);
                    }
                }
        );
        ProxyScraperService.start();
    }
}
