package pers.lxx.cryptocurrency.pricemonitor.connector;


import org.java_websocket.client.DefaultSSLWebSocketClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import pers.lxx.cryptocurrency.pricemonitor.client.WebSocketClient;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.net.URI;
import java.security.cert.X509Certificate;

/**
 * Created by luox on 2018/10/9.
 */
@Component("webSocketConnector")
public class Connector {

    private static Logger logger = LoggerFactory.getLogger(Connector.class);
    private WebSocketClient client;

    public static String KLINE = "market.%s.kline.%s";
    public static String DEPTH = "market.%s.depth.%s";
    public static String TRADE = "market.%s.trade.detail";
    public static String DETAIL = "market.%s.detail";
    public static String TICKERS = "market.tickers";
    public static String PERIOD[] = {"1min", "5min", "15min", "30min", "60min", "4hour", "1day", "1mon", "1week", "1year"};
    public static String TYPE[] = {"step0", "step1", "step2", "step3", "step4", "step5", "percent10"};

    @PostConstruct
    public void connect() {
        try {
            client = new WebSocketClient(new URI("wss://api.huobi.pro/ws"));

            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[]{};
                }

                @Override
                public void checkClientTrusted(X509Certificate[] chain,
                                               String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain,
                                               String authType) {
                }
            }};

            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, null);
            client.setWebSocketFactory(new DefaultSSLWebSocketClientFactory(sc));
            logger.info("开始连接...");
            client.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
