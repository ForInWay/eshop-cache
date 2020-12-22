package com.morgan.eshop.cache.base.thread;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.morgan.eshop.cache.base.constant.GlobalConstants;
import com.morgan.eshop.cache.base.context.SpringContextHolder;
import com.morgan.eshop.cache.base.zookeeper.ZookeeperSession;
import com.morgan.eshop.cache.entity.ProductInfo;
import com.morgan.eshop.cache.service.CacheService;
import com.morgan.eshop.cache.utils.Tools;

/**
 * @Description 缓存预热程序
 * @Author Morgan
 * @Date 2020/12/22 15:51
 **/
public class CachePreWarmThread extends Thread {

    private final static String TASK_ID_LIST = "task-id-list";
    private final static String TASK_ID_LOCK = "task-id-lock-";
    private final static String TASK_ID_STATUS_LOCK = "task-id-status-lock-";
    private final static String TASK_ID_STATUS = "task-id-status-";
    private final static String PRODUCT_LIST_PREFIX = "task-hot-product-list-";

    @Override
    public void run() {
        CacheService cacheService = SpringContextHolder.getBean(CacheService.class);
        ZookeeperSession zkSession = ZookeeperSession.getInstance();
        String taskIdList = zkSession.getNodeData(GlobalConstants.SpecialChar.FORWARD_SLASH + TASK_ID_LIST);
        if (Tools.isNotEmpty(taskIdList)){
            String[] taskIds = taskIdList.split(",");
            for (String taskId: taskIds){
                Boolean flag = zkSession.acquireFastFailDistributedLock(GlobalConstants.SpecialChar.FORWARD_SLASH + TASK_ID_LOCK  + taskId);
                if (!flag){
                    continue;
                }
                zkSession.acquireDistributedLock(GlobalConstants.SpecialChar.FORWARD_SLASH + TASK_ID_STATUS_LOCK + taskId);
                String taskStatus = zkSession.getNodeData(GlobalConstants.SpecialChar.FORWARD_SLASH + TASK_ID_STATUS + taskId);
                if (Tools.isEmpty(taskStatus)){
                    String hotProductList = zkSession.getNodeData(GlobalConstants.SpecialChar.FORWARD_SLASH + PRODUCT_LIST_PREFIX + taskId);
                    JSONArray hotProductArray = JSONArray.parseArray(hotProductList);
                    for (int i=0;i<hotProductArray.size();i++){
                        Long productId = hotProductArray.getLong(i);
                        String  productInfoJSON = "{\"id\": "+ productId +", \"name\": \"iphone7手机\", \"price\": 5599, \"pictureList\":\"a.jpg,b.jpg\", \"specification\": \"iphone7的规格\", \"service\": \"iphone7的售后服务\", \"color\": \"红色,白色,黑色\", \"size\": \"5.5\", \"shopId\": 1, \"modifiedTime\": \"2017-01-01 12:00:00\"}";
                        ProductInfo productInfo = JSONObject.parseObject(productInfoJSON, ProductInfo.class);
                        cacheService.saveProductInfoLocalCache(productInfo);
                        cacheService.saveProductInfoRedisCache(productInfo);
                    }
                    zkSession.setNodeData(GlobalConstants.SpecialChar.FORWARD_SLASH + TASK_ID_STATUS + taskId,"success");
                }
                zkSession.releaseDistributedLock(GlobalConstants.SpecialChar.FORWARD_SLASH + TASK_ID_STATUS_LOCK + taskId);
                zkSession.releaseDistributedLock(GlobalConstants.SpecialChar.FORWARD_SLASH + TASK_ID_LOCK  + taskId);
            }
        }
    }
}
