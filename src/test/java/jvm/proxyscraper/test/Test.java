package jvm.proxyscraper.test;

import jvm.proxyscraper.Provider;
import jvm.proxyscraper.ProxyScraperService;
import jvm.proxyscraper.event.ProxyListener;
import jvm.proxyscraper.io.ProxyWriter;

import java.io.File;
import java.net.Proxy;

public class Test {

    public static void main(String[] args) throws Exception{
        final ProxyWriter writer = ProxyWriter.to(new File("proxies.txt"));
        ProxyScraperService.addListener(
                new ProxyListener() {
                    public void onProxy(final Provider provider, final Proxy proxy){
                        writer.write(proxy, true);
                        System.out.printf("[%s] Cache: %,d%n", provider, ProxyScraperService.getCache().size());
                    }
                }
        );
        ProxyScraperService.start();
    }
}
