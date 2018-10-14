package pers.lxx.cryptocurrency.pricemonitor.engine;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sun.xml.internal.bind.v2.TODO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by luox on 2018/10/10.
 */
@Component("tickEngine")
public class TickEngine {

    private static Logger logger = LoggerFactory.getLogger(TickEngine.class);

    public static void tickHandle(JSONObject jsonObjectMessage) {

        JSONArray jsonObjectData = (JSONArray)jsonObjectMessage.get("data");
        Map<String, Double> changeList = new HashMap<>();

        for (Object data : jsonObjectData) {
            String symbol = ((JSONObject) data).getString("symbol");
            Double open = Double.valueOf(((JSONObject) data).getString("open"));
            Double close = Double.valueOf(((JSONObject) data).getString("close"));
            Double change = (close - open) / open * 100;
            changeList.put(symbol, change);
        }

        List<Map.Entry<String,Double>> changeList2Set = new ArrayList<>(changeList.entrySet());
        //对Set集合中的Map中Value排序
        Collections.sort(changeList2Set, new Comparator<Map.Entry<String,Double>>() {
            @Override
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                return -o1.getValue().compareTo(o2.getValue());
            }
        });
        //取涨幅最高的前10条
        List<Map.Entry<String,Double>> changeList2SetTop10 = changeList2Set.subList(0, 10);

        for (Map.Entry<String,Double> eachChange:changeList2SetTop10){

        }

        //TODO 当前获取的是发生变化的List
        logger.info(changeList2SetTop10.toString());
        logger.info("当前Map大小："+changeList.size());
    }

}
