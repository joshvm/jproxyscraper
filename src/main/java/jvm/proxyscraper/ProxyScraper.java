package jvm.proxyscraper;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import jvm.proxyscraper.util.ProxyUtils;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProxyScraper implements Runnable{

    private static final Pattern ADDRESS_PATTERN =  Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\D+\\d{2,5}");
    private static final Pattern LINE_PATTERN = Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\D+\\d{2,5}.*");
    private static final int PAGE_DELAY = 2000;

    public static boolean validProxiesOnly;

    private final Provider provider;

    protected ProxyScraper(final Provider provider){
        this.provider = provider;
    }

    public void run(){
        while(true){
            for(int pageNumber = provider.getMinPage(); pageNumber <= provider.getMaxPage(); pageNumber++){
                try(final WebClient client = new WebClient()){
                    client.getOptions().setThrowExceptionOnScriptError(false);
                    client.getOptions().setThrowExceptionOnFailingStatusCode(false);
                    final HtmlPage page = client.getPage(provider.getUrl(pageNumber));
                    final String html = page.getBody().asText();
                    final Matcher lineMatcher = LINE_PATTERN.matcher(html);
                    while(lineMatcher.find()){
                        final String text = lineMatcher.group();
                        final String typeStr = text.toLowerCase().contains("socks") ? "SOCKS" : "HTTP";
                        final Matcher addressMatcher = ADDRESS_PATTERN.matcher(text);
                        while(addressMatcher.find()){
                            final String match = addressMatcher.group();
                            final String address = match.replaceAll("[:\\s*]", ":");
                            final int i = address.indexOf(':');
                            if(i == -1)
                                continue;
                            final String ip = address.substring(0, i);
                            final String portStr = address.substring(i+1);
                            if(!portStr.matches("\\d+"))
                                continue;
                            final int port = Integer.parseInt(portStr);
                            final Proxy proxy = new Proxy(typeStr.equals("SOCKS") ? Proxy.Type.SOCKS : Proxy.Type.HTTP, new InetSocketAddress(ip, port));
                            if(validProxiesOnly && ProxyUtils.test(proxy))
                                ProxyScraperService.onProxy(provider, proxy);
                        }
                    }
                }catch(Exception ex){
                    //ex.printStackTrace();
                }
                sleep(PAGE_DELAY);
            }
            sleep(provider.getInterval());
        }
    }

    private void sleep(final int ms){
        try{
            Thread.sleep(ms);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
