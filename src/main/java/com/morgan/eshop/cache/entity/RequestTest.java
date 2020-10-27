package com.morgan.eshop.cache.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description:
 * @Date:2020/9/14
 * @User:morgan.b.chen
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestTest {

    private String serviceId;
    private Long shopId;
    private Long productId;
}
