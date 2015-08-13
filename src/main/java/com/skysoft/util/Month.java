package com.skysoft.util;

import java.util.Calendar;

/**
 * �·�[ö��]
 *
 */
public enum Month {
    JANUARY {                 //һ��

        int getValue() {
            return MONTH_JANUARY;
        }

        String getChinese() {
            return "һ��";
        }
    },
    FEBRUARY {                 //����

        int getValue() {
            return MONTH_FEBRUARY;
        }

        String getChinese() {
            return "����";
        }
    },
    MARCH {                 //����

        int getValue() {
            return MONTH_MARCH;
        }

        String getChinese() {
            return "����";
        }
    },
    APRIL {                 //����

        int getValue() {
            return MONTH_APRIL;
        }

        String getChinese() {
            return "����";
        }
    },
    MAY {                 //����

        int getValue() {
            return MONTH_MAY;
        }

        String getChinese() {
            return "����";
        }
    },
    JUNE {                 //����

        int getValue() {
            return MONTH_JUNE;
        }

        String getChinese() {
            return "����";
        }
    },
    JULY {                 //����

        int getValue() {
            return MONTH_JULY;
        }

        String getChinese() {
            return "����";
        }
    },
    AUGUST {                 //����

        int getValue() {
            return MONTH_AUGUST;
        }

        String getChinese() {
            return "����";
        }
    },
    SEPTEMBER {                 //����

        int getValue() {
            return MONTH_SEPTEMBER;
        }

        String getChinese() {
            return "����";
        }
    },
    OCTOBER {                 //ʮ��

        int getValue() {
            return MONTH_OCTOBER;
        }

        String getChinese() {
            return "ʮ��";
        }
    },
    NOVEMBER {                 //ʮһ��

        int getValue() {
            return MONTH_NOVEMBER;
        }

        String getChinese() {
            return "ʮһ��";
        }
    },
    DECEMBER {                 //ʮ����

        int getValue() {
            return MONTH_DECEMBER;
        }

        String getChinese() {
            return "ʮ����";
        }
    };

    /**
     * ����ժҪ����ȡ�·ݵ����ֱ�ʶ��1��ʾһ�£�12��ʾʮ����
     *
     * @return int
     */
    abstract int getValue();

    /**
     * ����ժҪ����ȡ�·ݵ���������
     *
     * @return String
     */
    abstract String getChinese();

    /**
     * һ��
     */
    public static final int MONTH_JANUARY = Calendar.JANUARY;
    /**
     * ����
     */
    public static final int MONTH_FEBRUARY = Calendar.FEBRUARY;
    /**
     * ����
     */
    public static final int MONTH_MARCH = Calendar.MARCH;
    /**
     * ����
     */
    public static final int MONTH_APRIL = Calendar.APRIL;
    /**
     * ����
     */
    public static final int MONTH_MAY = Calendar.MAY;
    /**
     * ����
     */
    public static final int MONTH_JUNE = Calendar.JUNE;
    /**
     * ����
     */
    public static final int MONTH_JULY = Calendar.JULY;
    /**
     * ����
     */
    public static final int MONTH_AUGUST = Calendar.AUGUST;
    /**
     * ����
     */
    public static final int MONTH_SEPTEMBER = Calendar.SEPTEMBER;
    /**
     * ʮ��
     */
    public static final int MONTH_OCTOBER = Calendar.OCTOBER;
    /**
     * ʮһ��
     */
    public static final int MONTH_NOVEMBER = Calendar.NOVEMBER;
    /**
     * ʮ����
     */
    public static final int MONTH_DECEMBER = Calendar.DECEMBER;
}
