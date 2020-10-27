package com.morgan.eshop.cache.entity;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 店铺信息
 * @Date:2020/9/13
 * @User:morgan.b.chen
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopInfo {

    private Long id;
    private String name;
    private Integer level;
    private Double goodCommentRate;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
