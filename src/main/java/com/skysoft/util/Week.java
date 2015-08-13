package com.skysoft.util;

import java.util.Calendar;

/**
 * ����[ö��]
 */
public enum Week {
    SUNDAY {             //������

        int getValue() {
            return WEEK_SUNDAY;
        }

        String getChinese() {
            return "������";
        }
    },
    MONDAY {            //����һ

        int getValue() {
            return WEEK_MONDAY;
        }

        String getChinese() {
            return "����һ";
        }
    },
    TUESDAY {            //���ڶ�

        int getValue() {
            return WEEK_TUESDAY;
        }

        String getChinese() {
            return "���ڶ�";
        }
    },
    WEDNESDAY {            //������

        int getValue() {
            return WEEK_WEDNESDAY;
        }

        String getChinese() {
            return "������";
        }
    },
    THURSDAY {            //������

        int getValue() {
            return WEEK_THURSDAY;
        }

        String getChinese() {
            return "������";
        }
    },
    FRIDAY {            //������

        int getValue() {
            return WEEK_FRIDAY;
        }

        String getChinese() {
            return "������";
        }
    },
    SATURDAY {            //������

        int getValue() {
            return WEEK_SATURDAY;
        }

        String getChinese() {
            return "������";
        }
    };

    /**
     * ����ժҪ����ȡ���ڵ����ֱ�ʶ��1��ʾ�����գ�7��ʾ������
     *
     * @return int
     */
    abstract int getValue();

    /**
     * ����ժҪ����ȡ���ڵ���������
     *
     * @return String
     */
    abstract String getChinese();

    /**
     * ������
     */
    public static final int WEEK_SUNDAY = Calendar.SUNDAY;
    /**
     * ����һ
     */
    public static final int WEEK_MONDAY = Calendar.MONDAY;
    /**
     * ���ڶ�
     */
    public static final int WEEK_TUESDAY = Calendar.TUESDAY;
    /**
     * ������
     */
    public static final int WEEK_WEDNESDAY = Calendar.WEDNESDAY;
    /**
     * ������
     */
    public static final int WEEK_THURSDAY = Calendar.THURSDAY;
    /**
     * ������
     */
    public static final int WEEK_FRIDAY = Calendar.FRIDAY;
    /**
     * ������
     */
    public static final int WEEK_SATURDAY = Calendar.SATURDAY;
}
