package com.morgan.eshop.cache.base.zookeeper;

import com.morgan.eshop.cache.base.constant.GlobalConstants;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @Description Zookeeper连接管理
 * @Author Morgan
 * @Date 2020/11/2 10:57
 **/
public class ZookeeperSession {

    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    private ZooKeeper zooKeeper;

//    @Value("${zookeeper.connectString}")
    private String zookeeperLink = "172.16.165.135:2181,172.16.165.134:2181,172.16.165.133:2181";

    public ZookeeperSession() {
        // 去连接Zookeeper Server，创建会话的时候，是异步进行的
        // 要给一个监听器，监听Zookeeper端返回的响应信息
        try {
            this.zooKeeper = new ZooKeeper(zookeeperLink, 50, new ZookeeperWatcher());
            System.out.println(zooKeeper.getState());
            countDownLatch.await();
            System.out.println("Zookeeper session established.....");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void acquireDistributedLock(Long productId){
        String path = "/product-lock-" + productId;
        try {
            zooKeeper.create(path,"".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            System.out.println("success to acquire lock for product[id=" + productId + "]");
        } catch (KeeperException e) {
            // 如果锁已被获取，会进入这里
            // a:使用重试机制继续尝试获取锁/b:其他也可以使用监听机制，监听获取锁的节点，如果节点被删除，代表锁已经被释放，再去尝试竞争锁/c:刚开始就创建多个有序临时节点，谁最小谁先获取锁，然后后续的依次监听前一个节点的变化
            int count = 0;
            long startMills = System.currentTimeMillis();
            while (true){
                try {
                    long current = System.currentTimeMillis();
                    if (current - startMills > 10000){
                        break;
                    }
                    Thread.sleep(200);
                    zooKeeper.create(path,"".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                } catch (KeeperException ex) {
                    ex.printStackTrace();
                    count++;
                    continue;
                }
                System.out.println("success to acquire lock for product[id=" + productId + "] after " + count + "times try....");
                break;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void acquireDistributedLock(String path){
        try {
            zooKeeper.create(path,"".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            System.out.println("success to acquire lock for " + path);
        } catch (KeeperException e) {
            // 如果锁已被获取，会进入这里
            // a:使用重试机制继续尝试获取锁/b:其他也可以使用监听机制，监听获取锁的节点，如果节点被删除，代表锁已经被释放，再去尝试竞争锁/c:刚开始就创建多个有序临时节点，谁最小谁先获取锁，然后后续的依次监听前一个节点的变化
            int count = 0;
            while (true){
                try {
                    Thread.sleep(20);
                    zooKeeper.create(path,"".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                } catch (KeeperException ex) {
                    ex.printStackTrace();
                    count++;
                    continue;
                }
                System.out.println("success to acquire lock for " + path + " after " + count + "times try....");
                break;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Boolean acquireFastFailDistributedLock(String path){
        try {
            zooKeeper.create(path,"".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            System.out.println("success to acquire lock for " + path);
            return true;
        } catch (InterruptedException | KeeperException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void releaseDistributedLock(Long productId){
        String path = "/product-lock-" + productId;
        try {
            zooKeeper.delete(path,-1);
            System.out.println("release product-lock-" + productId + "锁");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    public void releaseDistributedLock(String path){
        try {
            zooKeeper.delete(path,-1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    public String getNodeData(String path){
        try {
            return new String(zooKeeper.getData(path,false,new Stat()));
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
        return GlobalConstants.SpecialChar.BLANK;
    }

    public void setNodeData(String path,String data){
        try {
            zooKeeper.setData(path,data.getBytes(),-1);
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * zookeeper连接监听器
     */
    private class ZookeeperWatcher implements Watcher{
        @Override
        public void process(WatchedEvent event) {
            System.out.println("Receive watched event：" + event.getState());
            if (Event.KeeperState.SyncConnected == event.getState()){
                countDownLatch.countDown();
            }
        }
    }

    /**
     * 静态内部类实现单例
     */
    private static class Singleton{

        private static ZookeeperSession zookeeperSession;

        static {
            zookeeperSession = new ZookeeperSession();
        }

        private static ZookeeperSession getInstance(){
            return zookeeperSession;
        }
    }

    /**
     * 获取单例
     * @return
     */
    public static ZookeeperSession getInstance(){
        return Singleton.getInstance();
    }

    public static void init(){
        getInstance();
    }
}
