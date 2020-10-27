package com.morgan.eshop.cache.kafka;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.morgan.eshop.cache.entity.ProductInfo;
import com.morgan.eshop.cache.entity.ShopInfo;
import com.morgan.eshop.cache.service.CacheService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @Description: kafka消费者
 * @Date:2020/9/13
 * @User:morgan.b.chen
 */
@Component
public class KafkaConsumer {

    @Autowired
    private CacheService cacheService;

    /**
     * 监听商品服务信息
     */
    @KafkaListener(topics = KafkaProducer.TOPIC_TEST,groupId = KafkaProducer.TOPIC_GROUP)
    public void productInfo(ConsumerRecord<?,?> record, Acknowledgment ack, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic){
        System.out.println("进入消费者...");
        Optional message = Optional.ofNullable(record.value());
        if (message.isPresent()){
            Object msg = message.get();
            JSONObject messageObject = JSONObject.parseObject((String) msg);
            if (messageObject == null){
                return;
            }
            // 两级缓存架构处理，路由到不同的服务处理
            String serviceId = messageObject.getString("serviceId");
            if ("productServiceInfo".equals(serviceId)){
                processProductInfoChangeMessage(messageObject);
            }else if ("shopServiceInfo".equals(serviceId)){
                processShopInfoChangeMessage(messageObject);
            }
//            ack.acknowledge();
        }
    }

    /**
     * 处理商品变更的消息
     * @param messageObject
     */
    private void processProductInfoChangeMessage(JSONObject messageObject) {
        // 取出商品id
        Long productId = messageObject.getLong("productId");
        // 此处一般为查询数据库，但为了简化构造json字符串
        String productInfoJSON = "{\"id\": 1, \"name\": \"iphone7手机\", \"price\": 5599, \"pictureList\":\"a.jpg,b.jpg\", \"specification\": \"iphone7的规格\", \"service\": \"iphone7的售后服务\", \"color\": \"红色,白色,黑色\", \"size\": \"5.5\", \"shopId\": 1}";
        ProductInfo productInfo = JSONObject.parseObject(productInfoJSON, ProductInfo.class);
        // 第一步：保存到本地缓存
        cacheService.saveProductInfoLocalCache(productInfo);
        System.out.println("获取刚刚保存到本地缓存的商品信息：" + cacheService.getProductInfoLocalCache(productId));
        // 第二步：保存到redis缓存
        cacheService.saveProductInfoRedisCache(productInfo);
    }

    /**
     * 处理店铺变更的消息
     * @param messageObject
     */
    private void processShopInfoChangeMessage(JSONObject messageObject) {
        // 取出商品id
        Long shopId = messageObject.getLong("shopId");
        // 此处一般为查询数据库，但为了简化构造json字符串
        String shopInfoJSON = "{\"id\": 1, \"name\": \"小王的手机店\", \"level\": 5, \"goodCommentRate\":0.99}";
        ShopInfo shopInfo = JSONObject.parseObject(shopInfoJSON, ShopInfo.class);
        // 第一步：保存到本地缓存
        cacheService.saveShopInfoLocalCache(shopInfo);
        System.out.println("获取刚刚保存到本地缓存的店铺信息：" + cacheService.getShopInfoLocalCache(shopId));
        // 第二步：保存到redis缓存
        cacheService.saveShopInfoRedisCache(shopInfo);
    }
}
