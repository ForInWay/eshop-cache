package com.morgan.eshop.cache.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.morgan.eshop.cache.base.queue.RebuildCacheQueue;
import com.morgan.eshop.cache.base.thread.CachePreWarmThread;
import com.morgan.eshop.cache.entity.ProductInfo;
import com.morgan.eshop.cache.entity.RequestTest;
import com.morgan.eshop.cache.entity.ShopInfo;
import com.morgan.eshop.cache.base.kafka.KafkaProducer;
import com.morgan.eshop.cache.service.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;


/**
 * @Description: 缓存控制器
 * @Date:2020/9/6
 * @User:morgan.b.chen
 */
@RestController
@Slf4j
public class CacheController {

    @Autowired
    private CacheService cacheService;
    @Autowired
    private KafkaProducer kafkaProducer;
    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("testSaveCache")
    public String testSaveCache(ProductInfo productInfo){
        cacheService.testSaveCache(productInfo);
        return "success";
    }

    @GetMapping("testGetCache")
    public ProductInfo testGetCache(Long id){
        return cacheService.testGetCache(id);
    }

    /**
     * 发送商品变更消息
     * @param requestTest
     */
    @PostMapping("/kafka/productInfo")
    public void sendProductInfo(@RequestBody RequestTest requestTest){
        kafkaProducer.send(requestTest);
    }

    /**
     * 发送店铺变更消息
     * @param requestTest
     */
    @PostMapping("/kafka/shopInfo")
    public void sendShopInfo(@RequestBody RequestTest requestTest){
        kafkaProducer.send(requestTest);
    }

    @GetMapping("getProductInfo")
    public ProductInfo getProductInfo(Long productId){
        // 首先从redis中获取，没有则再从ehcache中获取，如再没有则从数据库获取(逻辑暂不写)
        ProductInfo productInfo = null;
        String productKey = "product_info_" + productId;
        String productJson = (String) redisTemplate.opsForValue().get(productKey);
        if (productJson != null && !"".equals(productJson.trim())){
            productInfo = JSON.parseObject(productJson,ProductInfo.class);
            log.info("从redis中获取商品信息");
        }
        if (null == productInfo){
            productInfo = cacheService.getProductInfoLocalCache(productId);
            log.info("从本地缓存中获取商品信息");
        }
        if (null == productInfo){
            // 重建缓存
            // 直接构造，模拟从数据库中拿取数据
            String productInfoJSON = "{\"id\": 2, \"name\": \"iphone7手机\", \"price\": 5599, \"pictureList\":\"a.jpg,b.jpg\", \"specification\": \"iphone7的规格\", \"service\": \"iphone7的售后服务\", \"color\": \"红色,白色,黑色\", \"size\": \"5.5\", \"shopId\": 1, \"modifiedTime\": \"2017-01-01 12:00:00\"}";
            productInfo = JSONObject.parseObject(productInfoJSON,ProductInfo.class);
            // 将数据推送到一个内存队列
            RebuildCacheQueue rebuildCacheQueue = RebuildCacheQueue.getInstance();
            rebuildCacheQueue.putProductInfo(productInfo);
            log.info("缓存重建，从数据库加载");
        }
        return productInfo;
    }

    @GetMapping("getShopInfo")
    public ShopInfo getShopInfo(Long shopId){
        // 首先从redis中获取，没有则再从ehcache中获取，如再没有则从数据库获取(逻辑暂不写)
        ShopInfo shopInfo = null;
        String shopKey = "shop_info_" + shopId;
        String shopJson = (String) redisTemplate.opsForValue().get(shopKey);
        if (shopJson != null && !"".equals(shopJson.trim())){
            shopInfo = JSON.parseObject(shopJson,ShopInfo.class);
            log.info("从redis中获取店铺信息");
        }
        if (null == shopInfo){
            shopInfo = cacheService.getShopInfoLocalCache(shopId);
            log.info("从本地缓存中获取店铺信息");
        }
        if (null == shopInfo){
            shopInfo = new ShopInfo();
            shopInfo.setId(1L);
            shopInfo.setName("小王的店铺");
            shopInfo.setLevel(1);
            shopInfo.setGoodCommentRate(5d);
            log.info("缓存重建，从数据库加载");
        }
        return shopInfo;
    }

    /**
     * 缓存预热
     */
    @PostMapping("preWarmCache")
    @ResponseBody
    public void preWarmCache(){
        new CachePreWarmThread().start();
    }
}
