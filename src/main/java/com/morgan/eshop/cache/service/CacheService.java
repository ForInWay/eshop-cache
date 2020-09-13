package com.morgan.eshop.cache.service;

import com.morgan.eshop.cache.entity.ProductInfo; /**
 * @Description: 缓存服务Service
 * @Date:2020/9/6
 * @User:morgan.b.chen
 */
public interface CacheService {


    ProductInfo testSaveCache(ProductInfo productInfo);

    ProductInfo testGetCache(Long id);
}
