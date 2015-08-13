package com.skysoft.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ArrayUtils {
    /**
     * 数组N等分
     *
     * @param arr            待分割数组
     * @param subArrayLength 子数组元素个数
     */
    public static Object[] splitArray(Object[] arr, int subArrayLength) {
        int start = 0;
        List<Object[]> params = new ArrayList<Object[]>();
        Object[] dest = null;
        while (start < arr.length - 1) {
            int n = arr.length - start;
            if (n > subArrayLength) {
                if (arr instanceof File[]) {
                    dest = new File[subArrayLength];
                } else if (arr instanceof String[]) {
                    dest = new String[subArrayLength];
                } else {
                    dest = new Object[subArrayLength];
                }
                System.arraycopy(arr, start, dest, 0, subArrayLength);
            } else {
                if (arr instanceof File[]) {
                    dest = new File[n];
                } else if (arr instanceof String[]) {
                    dest = new String[subArrayLength];
                } else {
                    dest = new Object[n];
                }
                System.arraycopy(arr, start, dest, 0, n);
            }
            params.add(dest);
            start += subArrayLength;
        }
        return params.toArray();
    }

    public static ArrayList<List<Map<String, String>>> splitArray(List<Map<String, String>> list, int subArrayLength) {
        int start = 0;
        ArrayList<List<Map<String, String>>> params = new ArrayList<List<Map<String, String>>>();
        while (start < list.size()) {
            int n = list.size() - start;
            List<Map<String, String>> dest = (n > subArrayLength) ? list.subList(start, start + subArrayLength) : list.subList(start, list.size());
            params.add(dest);
            start += subArrayLength;
        }
        return params;
    }

    /**
     * 合并两个数组
     *
     * @param arr1
     * @param arr2
     * @return
     */
    public static Object[] mergeArray(Object[] arr1, Object[] arr2) {
        if (GerneralUtils.isEmptyArray(arr1) && GerneralUtils.isEmptyArray(arr2)) {
            return null;
        }
        if (GerneralUtils.isEmptyArray(arr1)) {
            return arr2;
        }
        if (GerneralUtils.isEmptyArray(arr2)) {
            return arr1;
        }
        Object[] arr3 = new Object[arr1.length + arr2.length];
        System.arraycopy(arr1, 0, arr3, 0, arr1.length);
        System.arraycopy(arr2, 0, arr3, arr1.length, arr2.length);
        return arr3;
    }

    public static void main(String[] args) {
        Object a[] = {1, 2, 3};
        Object b[] = {4, 5, 6};
        Object[] c = mergeArray(a, b);
        for (Object object : c) {
            System.out.println(object);
        }
    }
}
