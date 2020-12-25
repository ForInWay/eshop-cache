package com.morgan.eshop.cache.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @Description 时间工具处理类
 * @Author Morgan
 * @Date 2020/11/5 14:52
 **/
public class DateUtils {

    public static void main(String[] args) {
        Instant instant = Instant.now();
        System.out.println(instant);
        LocalDate nowDate = LocalDate.now();
        System.out.println(nowDate);
        LocalTime nowTime = LocalTime.now();
        System.out.println(nowTime);
        LocalDateTime now = LocalDateTime.now();
        System.out.println(now);
    }
}
