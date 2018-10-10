package pers.lxx.cryptocurrency.pricemonitor.engine;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

/**
 * Created by luox on 2018/10/10.
 */
@Component("tickEngine")
public class TickEngine {

    public static void tickHandle(JSONObject jsonObjectMessage) {

        Long currentPrice = (Long)jsonObjectMessage.get("close");

    }

}
