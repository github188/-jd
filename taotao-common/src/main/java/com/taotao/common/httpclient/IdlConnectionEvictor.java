package com.taotao.common.httpclient;

import org.apache.http.conn.HttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Ellen on 2017/7/7.
 */
public class IdlConnectionEvictor extends Thread {

    private static final Logger LOGGER = LoggerFactory.getLogger(IdlConnectionEvictor.class);

    private final HttpClientConnectionManager connMgr;
    private volatile boolean shutdown;
    public IdlConnectionEvictor(HttpClientConnectionManager connMgr) {
        this.connMgr = connMgr;
        this.start();
    }

    @Override
    public void run() {
        try {
            while (!shutdown) {
                synchronized (this) {
                    wait(5000);
                    connMgr.closeExpiredConnections();
                }
            }
        } catch (InterruptedException e) {
            LOGGER.error("关闭连接", e.getMessage());
        }
    }

    public void shutDown() {
        shutdown = true;
        synchronized (this) {
            notifyAll();
        }
    }
}
