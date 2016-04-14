package io.leee13.springxd.processor;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.xd.dirt.server.singlenode.SingleNodeApplication;
import org.springframework.xd.dirt.test.SingleNodeIntegrationTestSupport;
import org.springframework.xd.dirt.test.SingletonModuleRegistry;
import org.springframework.xd.dirt.test.process.SingleNodeProcessingChain;
import org.springframework.xd.module.ModuleType;
import org.springframework.xd.test.RandomConfigurationSupport;

import static org.junit.Assert.assertEquals;
import static org.springframework.xd.dirt.test.process.SingleNodeProcessingChainSupport.chain;

/**
 * Created by makil on 2016/4/14.
 */
public class SinaStockTransformerIntegrationTest {
    private static SingleNodeApplication application;
    private static int RECEIVE_TIMEOUT = 50000;
    private static String moduleName = "sinaStock-transformer";

    @BeforeClass
    public static void setUp(){
        RandomConfigurationSupport randomConfigurationSupport = new RandomConfigurationSupport();
        application = new SingleNodeApplication().run();
        SingleNodeIntegrationTestSupport singleNodeIntegrationTestSupport = new SingleNodeIntegrationTestSupport(application);
        singleNodeIntegrationTestSupport.addModuleRegistry(new SingletonModuleRegistry(ModuleType.processor, moduleName));
    }

    @Test
    public void test(){
        String streamName = "sinaStockTransformerTest";
        String stock = "var hq_str_sz300024=\"机器人,27.790,27.560,28.320,29.550,27.680,28.320,28.330,69669607,1993985024.270,26362,28.320,283880,28.310,78455,28.300,6000,28.290,6100,28.280,46330,28.330,81140,28.340,59620,28.350,34440,28.360,9900,28.370,2016-04-13,15:05:56,00\";\n";

        String processingChainUnderTest = moduleName;
        SingleNodeProcessingChain singleNodeProcessingChain = chain(application,streamName, processingChainUnderTest);
        singleNodeProcessingChain.sendPayload(stock);
        String result = (String) singleNodeProcessingChain.receivePayload(RECEIVE_TIMEOUT);
        System.out.println(result);
        assertEquals("{\"最高成交价\":\"29.550\",\"卖价位四\":\"28.360\",\"卖价位三\":\"28.350\",\"买数量一\":\"26362\",\"卖价位五\":\"28.370\",\"买数量二\":\"283880\",\"行情日期\":\"2016-04-13\",\"成交金额\":\"1993985024.270\",\"卖价位一\":\"28.330\",\"买价位四\":\"28.290\",\"停牌状态\":\"00\",\"卖价位二\":\"28.340\",\"买价位三\":\"28.300\",\"买数量三 \":\"78455\",\"买价位五\":\"28.280\",\"买入价\":\"28.320\",\"昨日收盘价\":\"27.560\",\"买价位一\":\"28.320\",\"买价位二\":\"28.310\",\"最近成交价\":\"28.320\",\"卖数量四\":\"34440\",\"卖数量三\":\"59620\",\"成交数量\":\"69669607\",\"卖数量五\":\"9900\",\"证券简称\":\"机器人\",\"卖出价\":\"28.330\",\"卖数量一\":\"46330\",\"买数量四\":\"6000\",\"最低成交价\":\"27.680\",\"行情时间\":\"15:05:56\",\"卖数量二\":\"81140\",\"今日开盘价\":\"27.790\",\"买数量五\":\"6100\"}", result);

        singleNodeProcessingChain.destroy();

    }
}
