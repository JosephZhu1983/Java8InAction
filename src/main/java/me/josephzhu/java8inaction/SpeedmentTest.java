package me.josephzhu.java8inaction;

import com.company.apollo_query.ApolloQueryApplication;
import com.company.apollo_query.ApolloQueryApplicationBuilder;
import com.company.apollo_query.apollo_query.apollo_query.tb_shipping_order_main.TbShippingOrderMain;
import com.company.apollo_query.apollo_query.apollo_query.tb_shipping_order_main.TbShippingOrderMainManager;
import com.speedment.runtime.core.ApplicationBuilder;
import org.jooq.lambda.tuple.Tuple2;
import org.jooq.lambda.tuple.Tuple3;

import java.util.Comparator;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by zhuye on 04/01/2017.
 */
public class SpeedmentTest
{
    public static void main(String[] args) throws NoSuchMethodException
    {
        ApolloQueryApplication app = new ApolloQueryApplicationBuilder()
                .withPassword("kmztyae")
                .withLogging(ApplicationBuilder.LogType.STREAM)
                .build();

        final TbShippingOrderMainManager shippingOrderMainManager = app.getOrThrow(TbShippingOrderMainManager.class);
        System.out.println(
            shippingOrderMainManager.stream()
                    .filter(TbShippingOrderMain.SHIPPING_STATE.in((byte)40, (byte)60, (byte)80)
                            .and(TbShippingOrderMain.CARRIER_ID.equal(5)))
                    .map(o->new Tuple3<>(o.getId(), o.getShippingState(), o.getCreatedAt()))
                    .sorted(Comparator.comparing(Tuple3::v3))
                    .limit(100)
                    .collect(Collectors.toList()));

    }

}
