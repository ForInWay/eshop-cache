package com.morgan.eshop.cache.service.impl;

import com.morgan.eshop.cache.entity.ProductInfo;
import com.morgan.eshop.cache.service.CacheService;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @Description: 缓存服务Service实现
 * @Date:2020/9/6
 * @User:morgan.b.chen
 */
@Service
public class CacheServiceImpl implements CacheService{

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
}
