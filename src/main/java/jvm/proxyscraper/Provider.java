package jvm.proxyscraper;

public enum Provider {

    HIDE_MY_ASS("http://proxylist.hidemyass.com/%d", 1, 14),
    NINJA_PROXY("http://ninjaproxies.com/"),
    ULTRA_PROXIES("http://www.ultraproxies.com/"),
    PROXY_LISTY("http://www.proxylisty.com/ip-proxylist-%d", 1, 63),
    SAMAIR("http://www.samair.ru/proxy/ip-address-%02d.htm", 1, 30),
    PROXY_NOVA("http://www.proxynova.com/proxy-server-list/"),
    NN_TIME("http://nntime.com/proxy-list-%02d.htm", 1, 30),
    GATHER_PROXY("http://www.gatherproxy.com/"),
    COOL_PROXY("http://www.cool-proxy.net/proxies/http_proxy_list/page:%d", 1, 30),
    TOR_VPN("http://torvpn.com/proxylist.html"),
    NORD_VPN("https://nordvpn.com/free-proxy-list/%d/", 1, 8371),
    FREE_PROXY_LISTS("http://www.freeproxylists.net/?page=%d", 1, 31),
    PROXY_LIST("http://proxy-list.org/english/index.php?p=%d", 1, 10),
    PROXY_FIRE_ELITE("http://www.proxyfire.net/index.php?pageid=eliteproxylist"),
    PROXY_FIRE_ANON("http://www.proxyfire.net/index.php?pageid=anonymousproxylist"),
    PROXY_FIRE_SOCKS4("http://www.proxyfire.net/index.php?pageid=socks4proxylist"),
    PROXY_FIRE_SOCKS5("http://www.proxyfire.net/index.php?pageid=socks5proxylist"),
    SOCKS_LIST("http://sockslist.net/proxy/server-socks-hide-ip-address/1", 1, 3),
    IP_ADRESS("http://www.ip-adress.com/proxy_list/"),
    PROXY_LIST_RO("http://www.proxylist.ro/free-fresh-proxy-list-%d.html", 0, 31),
    XROXY("http://www.xroxy.com/proxylist.php?port=&type=&ssl=&country=&latency=&reliability=&sort=reliability&desc=true&pnum=%d", 0, 186);

    private final String urlFormat;
    private final int minPage;
    private final int maxPage;
    private final int interval;

    private ProxyScraper scraper;

    private Provider(final String urlFormat, final int minPage, final int maxPage){
        this.urlFormat = urlFormat;
        this.minPage = minPage;
        this.maxPage = maxPage;

        this.interval = (30 + (maxPage * 5)) * 60 * 1000;
    }

    private Provider(final String url){
        this(url, 1, 1);
    }

    public ProxyScraper getScraper(){
        if(scraper == null)
            scraper = new ProxyScraper(this);
        return scraper;
    }

    public String getUrl(final int page){
        return hasPages() ? String.format(urlFormat, page) : urlFormat;
    }

    private boolean hasPages(){
        return urlFormat.contains("%02d") || urlFormat.contains("%d");
    }

    public int getMinPage(){
        return minPage;
    }

    public int getMaxPage(){
        return maxPage;
    }

    public int getInterval(){
        return interval;
    }
}
