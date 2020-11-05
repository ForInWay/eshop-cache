
package com.morgan.eshop.cache.base.constant;

/**
 * 全局的常量类
 *
 * @date 2019-07-05
 */
public interface GlobalConstants {

    public interface TrueFalse {
        /**
         * 真
         */
        String TRUE = "true";

        /**
         * 假
         */
        String FALSE = "false";
    }

    /**
     * 
     * 是否
     */
    public interface YN {
        String Y = "Y";
        
        String N = "N";
    }

    /***
     * 逻辑键 AND OR XOR NOT
     */
    public interface LOGICKEY {
        /**
         * 交
         */
        String AND = "AND";

        /**
         * 并
         */
        String OR = "OR";

        /**
         * 异或
         */
        String XOR = "XOR";

        /**
         * 非
         */
        String NOT = "NOT";
    }

    /***
     * 数字常量
     */
    public interface NumberString {
        String ZERO = "0";
        String ONE = "1";
        String TWO = "2";
        String THREE = "3";
        String FOUR = "4";
        String FIVE = "5";
        String SIX = "6";
        String SEVEN = "7";
        String EIGHT = "8";
        String NINE = "9";
    }


    /***
     * 数字常量
     */
    public interface DIGITAL {
        Integer ZERO = 0;
        Integer ONE = 1;
        Integer TWO = 2;
        Integer THREE = 3;
        Integer FOUR = 4;
        Integer FIVE = 5;
        Integer SIX = 6;
        Integer SEVEN = 7;
        Integer EIGHT = 8;
        Integer NINE = 9;
    }

    /**
     * 缓存区域设置
     */
    interface CacheRegion {
        // 系统缓存域
        String SYS_CACHE = "sysCache";
        // 用户限制代打卡域
        String USER_CLOCKIN_LIMIT = "userClockinLimit";
        // 预算控制编辑限制域
        String PROJECT_BUDGET_EDIT_LIMIT = "projectBudgetEditLimit";
        // 库存盘点限制域
        String STOCK_TAKING_LIMIT = "stockTakingLimit";
        // 月统计-公司级缓存域
        String COMPANY_STATISTIC_MONTHLY = "companyStatisticMonthly";
        String ATTENDANCE_EXPORT = "fileCache";
    }

    interface DateTimeFormatter {
        String DEFAULT_DATE_TIME = "yyyy-MM-dd HH:mm:ss";
        String DEFAULT_DATE = "yyyy-MM-dd";
        String DEFAULT_TIME = "HH:mm:ss";
        String YYYYMMDD = "YYYYMMDD";
    }
}
