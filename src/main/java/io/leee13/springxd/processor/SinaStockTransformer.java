package io.leee13.springxd.processor;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.integration.transformer.MessageTransformationException;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by makil on 2016/4/14.
 */
public class SinaStockTransformer {
    private ObjectMapper mapper = new ObjectMapper();

    public String transform(String payload){
        Map<String, String> stock = new Hashtable<>();
        try {
            Matcher matcher = Pattern.compile("^var hq_str_sz300024=\"(.*)\";\\n$").matcher(payload);

            if (matcher.matches()) {
                String[] keys = "证券简称,今日开盘价,昨日收盘价,最近成交价,最高成交价,最低成交价,买入价,卖出价,成交数量,成交金额,买数量一,买价位一,买数量二,买价位二,买数量三 ,买价位三,买数量四,买价位四,买数量五,买价位五,卖数量一,卖价位一,卖数量二,卖价位二,卖数量三,卖价位三,卖数量四,卖价位四,卖数量五,卖价位五,行情日期,行情时间,停牌状态".split(",");
                String[] vals = matcher.group(1).split(",");
                for (int i = 0; i < keys.length; i++) {
                    String key = keys[i];
                    stock.put(key, vals[i]);
                }
            }else {
                stock.put("result","matches fail!");
                stock.put("payload",payload);
            }
            return mapper.writeValueAsString(stock);
        } catch (IOException e) {
            throw new MessageTransformationException("Unable to transform stock: " + e.getMessage(), e);
        }
    }
}
