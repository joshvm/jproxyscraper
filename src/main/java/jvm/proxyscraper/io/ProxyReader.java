package jvm.proxyscraper.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProxyReader implements AutoCloseable{

    private static final Pattern DELIMITER = Pattern.compile("(HTTP|SOCKS|DIRECT) \\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3} \\d{2,5}");

    private final Scanner input;
    private Matcher matcher;

    public ProxyReader(final InputStream in){
        input = new Scanner(in);
    }

    public boolean hasNext(){
        if(matcher != null && matcher.find())
            return true;
        while(input.hasNextLine()){
            matcher = DELIMITER.matcher(input.nextLine());
            if(matcher.find())
                return true;
        }
        return false;
    }

    public Proxy read(){
        final String line = matcher.group();
        final String[] split = line.split(" ");
        final Proxy.Type type = Proxy.Type.valueOf(split[0]);
        final String ip = split[1];
        final int port = Integer.parseInt(split[2]);
        return new Proxy(type, new InetSocketAddress(ip, port));
    }

    public Set<Proxy> readAll(final boolean close){
        final Set<Proxy> set = new LinkedHashSet<>();
        while(hasNext())
            set.add(read());
        try{
            return set;
        }finally{
            if(close)
                close();
        }
    }

    public Set<Proxy> readAll(){
        return readAll(true);
    }

    public void close(){
        input.close();
    }

    public static ProxyReader from(final InputStream input){
        return new ProxyReader(input);
    }

    public static ProxyReader from(final File file){
        try{
            return from(new FileInputStream(file));
        }catch(FileNotFoundException e){
            e.printStackTrace();
            return null;
        }
    }

    public static ProxyReader from(final URL url){
        try{
            return from(url.openStream());
        }catch(IOException e){
            e.printStackTrace();
            return null;
        }
    }
}
