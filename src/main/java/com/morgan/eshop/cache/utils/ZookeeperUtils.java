package com.morgan.eshop.cache.utils;

import com.morgan.eshop.cache.base.constant.GlobalConstants;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @Description zookeeper工具类
 * @Author Morgan
 * @Date 2020/12/24 15:54
 **/
@Component
public class ZookeeperUtils {

    @Value("${zookeeper.connectString}")
    private String zookeeperLink;

    private CuratorFramework client;

    @PostConstruct
    public void initZookeeperClient() {
        this.client = getClient();
    }

    public CuratorFramework getClient() {
        return CuratorFrameworkFactory.builder().connectString(zookeeperLink).retryPolicy(new ExponentialBackoffRetry(1000, 3)).connectionTimeoutMs(15 * 1000).sessionTimeoutMs(60 * 1000).namespace(
                GlobalConstants.ZookeeperBasePath.BASEPATH).build();
    }

    public void acquireDistributedLock(final String path, final String data) throws Exception {
//        client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path, data.getBytes());
    }

    public void acquireFastFailDistributedLock(final String path, final String data) throws Exception {
//        client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path, data.getBytes());
    }
}
