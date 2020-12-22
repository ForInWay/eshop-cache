package com.morgan.eshop.cache.service;

import com.morgan.eshop.cache.entity.ProductInfo;
import com.morgan.eshop.cache.entity.ShopInfo; /**
 * @Description: 缓存服务Service
 * @Date:2020/9/6
 * @User:morgan.b.chen
 */
public interface CacheService {

    ProductInfo saveProductInfoLocalCache(ProductInfo productInfo);

    void saveProductInfoRedisCache(ProductInfo productInfo);

    ProductInfo getProductInfoLocalCache(Long productId);

    ShopInfo saveShopInfoLocalCache(ShopInfo shopInfo);

    void saveShopInfoRedisCache(ShopInfo shopInfo);

    ShopInfo getShopInfoLocalCache(Long shopId);

    ProductInfo getProductInfoFromRedisCache(Long productId);
}
