package com.morgan.eshop.cache.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description:
 * @Date:2020/9/6
 * @User:morgan.b.chen
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductInfo {

    private Long id;
    private String name;
    private Double price;
}
