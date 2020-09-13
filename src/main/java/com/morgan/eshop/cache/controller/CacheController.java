package com.morgan.eshop.cache.controller;

import com.morgan.eshop.cache.entity.ProductInfo;
import com.morgan.eshop.cache.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 缓存控制器
 * @Date:2020/9/6
 * @User:morgan.b.chen
 */
@RequestMapping("/cache")
@RestController
public class CacheController {

    @Autowired
    private CacheService cacheService;

    @GetMapping("testSaveCache")
    public String testSaveCache(ProductInfo productInfo){
        cacheService.testSaveCache(productInfo);
        return "success";
    }

    @GetMapping("testGetCache")
    public ProductInfo testGetCache(Long id){
        return cacheService.testGetCache(id);
    }
}
