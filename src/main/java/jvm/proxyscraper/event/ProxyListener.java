package jvm.proxyscraper.event;

import jvm.proxyscraper.Provider;

import java.net.Proxy;

public interface ProxyListener {

    public void onProxy(final Provider provider, final Proxy proxy);
}
