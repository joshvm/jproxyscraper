package jvm.proxyscraper.test;

import jvm.proxyscraper.Provider;
import jvm.proxyscraper.ProxyScraperService;
import jvm.proxyscraper.event.ProxyListener;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Proxy;
import java.util.Date;

public class Test {

    public static void main(String[] args) throws Exception{
        final BufferedWriter writer = new BufferedWriter(new FileWriter("logs.log"));
        ProxyScraperService.addListener(
                new ProxyListener() {
                    public void onProxy(final Provider provider, final Proxy proxy){
                        try{
                            writer.write(String.format("[%s - %s] Scraped Proxy %s\n", new Date(), provider, proxy));
                            writer.flush();
                        }catch(IOException e){
                            e.printStackTrace();
                        }
                    }
                }
        );
        ProxyScraperService.start();
    }
}
