package com.morgan.eshop.cache.base.queue;

import com.morgan.eshop.cache.entity.ProductInfo;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * @Description 重建缓存的内存队列
 * @Author Morgan
 * @Date 2020/11/5 15:48
 **/
public class RebuildCacheQueue {

    private ArrayBlockingQueue<ProductInfo> queue = new ArrayBlockingQueue<ProductInfo>(1000);

    public void putProductInfo(ProductInfo productInfo){
        try {
            queue.put(productInfo);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public ProductInfo takeProductInfo(){
        try {
            return queue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int size(){
        return queue.size();
    }

    private RebuildCacheQueue() {

    }

    private static class Singleton {
        private static RebuildCacheQueue rebuildCacheQueue;
        static {
            rebuildCacheQueue = new RebuildCacheQueue();
        }

        public static RebuildCacheQueue getInstance(){
            return rebuildCacheQueue;
        }
    }

    public static RebuildCacheQueue getInstance(){
        return Singleton.getInstance();
    }
}
