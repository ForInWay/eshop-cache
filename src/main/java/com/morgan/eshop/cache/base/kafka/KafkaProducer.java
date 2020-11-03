package com.morgan.eshop.cache.base.kafka;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

/**
 * @Description: kafka生产者
 * @Date:2020/9/13
 * @User:morgan.b.chen
 */
@Component
public class KafkaProducer {

    @Autowired
    private KafkaTemplate<String,Object> kafkaTemplate;

    /**
     * 自定义主题
     */
    public static final String TOPIC_TEST = "eshop.cache";

    public static final String TOPIC_GROUP = "eshop.cache.group";

    /**
     * 发送消息
     * @param obj
     */
    public void send(Object obj){
        String jsonString = JSON.toJSONString(obj);
        System.out.println("准备发送的消息：" + jsonString);
        // 发送消息
        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(TOPIC_TEST, jsonString);
        // 监听消息响应
        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {

            /**
             * 发送失败
             * @param throwable
             */
            @Override
            public void onFailure(Throwable throwable) {
                System.out.println(TOPIC_TEST + "-  生产者,发送消息失败：" + throwable.getMessage());
            }

            /**
             * 发送成功
             * @param stringObjectSendResult
             */
            @Override
            public void onSuccess(SendResult<String, Object> stringObjectSendResult) {
                System.out.println(TOPIC_TEST + "-  生产者,发送消息成功：" + stringObjectSendResult.toString());
            }
        });
    }
}
