package pers.lxx.cryptocurrency.pricemonitor.client;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.lxx.cryptocurrency.pricemonitor.utils.ZipUtil;

import java.net.URI;
import java.nio.ByteBuffer;

public class WebSocketClient extends org.java_websocket.client.WebSocketClient {

    private static Logger logger = LoggerFactory.getLogger(WebSocketClient.class);
    private boolean errorFlag = false;

    public static String KLINE = "market.%s.kline.%s";
    public static String DEPTH = "market.%s.depth.%s";
    public static String TRADE = "market.%s.trade.detail";
    public static String DETAIL = "market.%s.detail";
    public static String TICKERS = "market.tickers";
    public static String PERIOD[] = {"1min", "5min", "15min", "30min", "60min", "4hour", "1day", "1mon", "1week", "1year"};
    public static String TYPE[] = {"step0", "step1", "step2", "step3", "step4", "step5", "percent10"};

    public WebSocketClient(URI serverURI) {
        super(serverURI, new Draft_17());
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        logger.info("握手成功");
        //连接成功,发送信息
        JSONObject req = new JSONObject();
        req.put("sub",TICKERS);
        req.put("id","6666666666");
        send(req.toString());
    }

    @Override
    public void onMessage(String msg) {
        if (msg != null) {
            logger.info(msg);
        }

    }

    @Override
    public void onMessage(ByteBuffer bytes) {
        try {
            String message = new String(ZipUtil.decompress(bytes.array()), "UTF-8");
            JSONObject jsonObjectMessage = JSON.parseObject(message);
            if (message.indexOf("ping") > 0) {
                logger.info("心跳检测："+message);
                send(message.replace("ping", "pong"));
            }else

            if (message.indexOf("subbed") > 0&&jsonObjectMessage.get("status").equals("ok")) {
                logger.info("订阅成功："+jsonObjectMessage.get("subbed"));
            }else

            if (message.indexOf("tick") > 0) {
//                logger.info("当前行情："+jsonObjectMessage.get("tick").toString());
                logger.info(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClose(int i, String s, boolean b) {
        if (errorFlag) {
            int count = 0;
            try {
                while (!this.getReadyState().equals(WebSocket.READYSTATE.OPEN)) {
                    count ++;
                    logger.info("重连次数："+count);
                    Thread.sleep(1000);
                    this.connect();
                }
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }else{
            logger.info("链接已关闭");
        }
    }

    @Override
    public void onError(Exception e){
        e.printStackTrace();
        logger.info("发生错误链接关闭，尝试重连……");
        errorFlag = true;
    }
}
