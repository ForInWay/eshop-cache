package com.morgan.eshop.cache.base.thread;

import com.morgan.eshop.cache.base.constant.GlobalConstants;
import com.morgan.eshop.cache.base.context.SpringContextHolder;
import com.morgan.eshop.cache.base.queue.RebuildCacheQueue;
import com.morgan.eshop.cache.base.zookeeper.ZookeeperSession;
import com.morgan.eshop.cache.entity.ProductInfo;
import com.morgan.eshop.cache.service.CacheService;
import com.morgan.eshop.cache.utils.Tools;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Description 缓存重建线程
 * @Author Morgan
 * @Date 2020/11/5 16:01
 **/
public class RebuildCacheThread implements Runnable{

    @Override
    public void run() {
        RebuildCacheQueue rebuildCacheQueue = RebuildCacheQueue.getInstance();
        ZookeeperSession zookeeperSession = ZookeeperSession.getInstance();
        CacheService cacheService = (CacheService)SpringContextHolder.getBean("cacheService");
        while (true){
            ProductInfo productInfo = rebuildCacheQueue.takeProductInfo();
            // 获取分布式锁
            zookeeperSession.acquireDistributedLock(productInfo.getId());
            // 获取到锁之后，先去redis中拿，看存在不存在及数据的版本
            ProductInfo existProductInfo = cacheService.getProductInfoFromRedisCache(productInfo.getId());
            if (Tools.isNotEmpty(existProductInfo)){
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(GlobalConstants.DateTimeFormatter.DEFAULT_DATE_TIME);
                LocalDateTime modifiedTime = LocalDateTime.parse(productInfo.getModifiedTime());
                LocalDateTime existModifiedTime = LocalDateTime.parse(existProductInfo.getModifiedTime());
                if (modifiedTime.isBefore(existModifiedTime)){
                    System.out.println("current date[" + productInfo.getModifiedTime() + "is before existDate[" + existProductInfo.getModifiedTime() + "]");
                    return;
                }
                System.out.println("current date[" + productInfo.getModifiedTime() + "is after existDate[" + existProductInfo.getModifiedTime() + "]");
            }else{
                System.out.println("exist productInfo is null");
            }
            // 走到这里，代表productInfo为最新版本的信息，需要保存到缓存中去
            // 第一步：保存到本地缓存
            cacheService.saveProductInfoLocalCache(productInfo);
            System.out.println("获取刚刚保存到本地缓存的商品信息：" + cacheService.getProductInfoLocalCache(productInfo.getId()));
            // 第二步：保存到redis缓存
            cacheService.saveProductInfoRedisCache(productInfo);
            // 最后释放分布式锁
            zookeeperSession.releaseDistributedLock(productInfo.getId());
        }
    }
}
