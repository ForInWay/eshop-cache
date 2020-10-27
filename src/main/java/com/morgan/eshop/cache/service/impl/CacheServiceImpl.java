package com.morgan.eshop.cache.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.morgan.eshop.cache.entity.ProductInfo;
import com.morgan.eshop.cache.entity.ShopInfo;
import com.morgan.eshop.cache.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @Description: 缓存服务Service实现
 * @Date:2020/9/6
 * @User:morgan.b.chen
 */
@Service
public class CacheServiceImpl implements CacheService{

    @Autowired
    private RedisTemplate redisTemplate;

    private static final String CACHE_NAME = "local";

    @Override
    @CachePut(value = CACHE_NAME,key = "#productInfo.id")
    public ProductInfo testSaveCache(ProductInfo productInfo) {
        return productInfo;
    }

    @Override
    @Cacheable(value = CACHE_NAME,key = "#id")
    public ProductInfo testGetCache(Long id) {
        System.out.println("缓存获取成功");
        ProductInfo productInfo = new ProductInfo();
        productInfo.setId(2L);
        productInfo.setName("测试");
        productInfo.setPrice(23.5);
        return productInfo;
    }

    @Override
    @CachePut(value = CACHE_NAME,key = "'product_info_'+#productInfo.id")
    public ProductInfo saveProductInfoLocalCache(ProductInfo productInfo) {
        return productInfo;
    }

    @Override
    public void saveProductInfoRedisCache(ProductInfo productInfo) {
        String key = "product_info_" + productInfo.getId();
        redisTemplate.opsForValue().set(key, JSONObject.toJSONString(productInfo));
    }

    @Override
    @Cacheable(value = CACHE_NAME,key = "'product_info_'+#productId")
    public ProductInfo getProductInfoLocalCache(Long productId) {
        return null;
    }

    @Override
    @CachePut(value = CACHE_NAME,key = "'shop_info_'+#shopInfo.id")
    public ShopInfo saveShopInfoLocalCache(ShopInfo shopInfo) {
        return shopInfo;
    }

    @Override
    public void saveShopInfoRedisCache(ShopInfo shopInfo) {
        String key = "shop_info_" + shopInfo.getId();
//        String k2 = (String) redisTemplate.opsForValue().get("k2");
//        System.out.println(k2);
        redisTemplate.opsForValue().set(key,JSONObject.toJSONString(shopInfo));
    }

    @Override
    @Cacheable(value = CACHE_NAME,key = "'shop_info_'+#shopId")
    public ShopInfo getShopInfoLocalCache(Long shopId) {
        return null;
    }
}
