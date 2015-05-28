package jvm.proxyscraper.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ProxyWriter implements AutoCloseable{

    public static final int PROXY_CACHE_CAPACITY = 100000;

    private final BufferedWriter writer;
    private final Set<Proxy> cache;

    public ProxyWriter(final Writer out){
        writer = new BufferedWriter(out);

        cache = new HashSet<>();
    }

    public void cache(final Collection<Proxy> proxies){
        cache.addAll(proxies);
    }

    public void cache(final Proxy proxy){
        cache.add(proxy);
    }

    public boolean cacheAndWrite(final Proxy proxy, final boolean flush){
        if(cache.size() >= PROXY_CACHE_CAPACITY)
            cache.clear();
        return cache.add(proxy) && write(proxy, flush);
    }

    public boolean write(final Proxy proxy, final boolean flush){
        final InetSocketAddress addr = (InetSocketAddress) proxy.address();
        try{
            writer.write(String.format("%s %s %d", proxy.type().name(), addr.getHostString(), addr.getPort()));
            writer.newLine();
            return !flush || flush();
        }catch(IOException e){
            e.printStackTrace();
            return false;
        }
    }

    public int writeAll(final Collection<Proxy> proxies, final boolean flush){
        int count = 0;
        for(final Proxy p : proxies)
            if(write(p, false))
                ++count;
        if(flush)
            flush();
        return count;
    }

    public boolean flush(){
        try{
            writer.flush();
            return true;
        }catch(IOException e){
            e.printStackTrace();
            return false;
        }
    }

    public void close(){
        try{
            writer.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public static ProxyWriter to(final Writer out){
        return new ProxyWriter(out);
    }

    public static ProxyWriter to(final File out, final boolean append){
        try{
            return to(new FileWriter(out, append));
        }catch(IOException e){
            e.printStackTrace();
            return null;
        }
    }
}
