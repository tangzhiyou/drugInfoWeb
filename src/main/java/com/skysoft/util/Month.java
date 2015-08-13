package com.skysoft.util;

import java.util.Calendar;

/**
 * 月份[枚举]
 *
 */
public enum Month {
    JANUARY {                 //一月

        int getValue() {
            return MONTH_JANUARY;
        }

        String getChinese() {
            return "一月";
        }
    },
    FEBRUARY {                 //二月

        int getValue() {
            return MONTH_FEBRUARY;
        }

        String getChinese() {
            return "二月";
        }
    },
    MARCH {                 //三月

        int getValue() {
            return MONTH_MARCH;
        }

        String getChinese() {
            return "三月";
        }
    },
    APRIL {                 //四月

        int getValue() {
            return MONTH_APRIL;
        }

        String getChinese() {
            return "四月";
        }
    },
    MAY {                 //五月

        int getValue() {
            return MONTH_MAY;
        }

        String getChinese() {
            return "五月";
        }
    },
    JUNE {                 //六月

        int getValue() {
            return MONTH_JUNE;
        }

        String getChinese() {
            return "六月";
        }
    },
    JULY {                 //七月

        int getValue() {
            return MONTH_JULY;
        }

        String getChinese() {
            return "七月";
        }
    },
    AUGUST {                 //八月

        int getValue() {
            return MONTH_AUGUST;
        }

        String getChinese() {
            return "八月";
        }
    },
    SEPTEMBER {                 //九月

        int getValue() {
            return MONTH_SEPTEMBER;
        }

        String getChinese() {
            return "九月";
        }
    },
    OCTOBER {                 //十月

        int getValue() {
            return MONTH_OCTOBER;
        }

        String getChinese() {
            return "十月";
        }
    },
    NOVEMBER {                 //十一月

        int getValue() {
            return MONTH_NOVEMBER;
        }

        String getChinese() {
            return "十一月";
        }
    },
    DECEMBER {                 //十二月

        int getValue() {
            return MONTH_DECEMBER;
        }

        String getChinese() {
            return "十二月";
        }
    };

    /**
     * 方法摘要：获取月份的数字标识，1表示一月，12表示十二月
     *
     * @return int
     */
    abstract int getValue();

    /**
     * 方法摘要：获取月份的中文名称
     *
     * @return String
     */
    abstract String getChinese();

    /**
     * 一月
     */
    public static final int MONTH_JANUARY = Calendar.JANUARY;
    /**
     * 二月
     */
    public static final int MONTH_FEBRUARY = Calendar.FEBRUARY;
    /**
     * 三月
     */
    public static final int MONTH_MARCH = Calendar.MARCH;
    /**
     * 四月
     */
    public static final int MONTH_APRIL = Calendar.APRIL;
    /**
     * 五月
     */
    public static final int MONTH_MAY = Calendar.MAY;
    /**
     * 六月
     */
    public static final int MONTH_JUNE = Calendar.JUNE;
    /**
     * 七月
     */
    public static final int MONTH_JULY = Calendar.JULY;
    /**
     * 八月
     */
    public static final int MONTH_AUGUST = Calendar.AUGUST;
    /**
     * 九月
     */
    public static final int MONTH_SEPTEMBER = Calendar.SEPTEMBER;
    /**
     * 十月
     */
    public static final int MONTH_OCTOBER = Calendar.OCTOBER;
    /**
     * 十一月
     */
    public static final int MONTH_NOVEMBER = Calendar.NOVEMBER;
    /**
     * 十二月
     */
    public static final int MONTH_DECEMBER = Calendar.DECEMBER;
}
