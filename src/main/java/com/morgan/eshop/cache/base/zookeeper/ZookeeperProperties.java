package com.morgan.eshop.cache.base.zookeeper;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Description Zookeeper属性配置
 * @Author Morgan
 * @Date 2020/11/2 11:13
 **/
@Data
//@ConfigurationProperties(prefix = "zookeeper")
public class ZookeeperProperties {

    /**
     * zookeeper集群链接
     */
    private String connectString;
}
