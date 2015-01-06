package com.skysoft.test;

import com.skysoft.util.FileIOStreamTools;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User: pinaster
 * Date: 13-11-28
 * Time: 下午10:00
 */
public class TestFile {
    public  static  void main(String[] args)
    {

        DuplicateSet();

    }

    public static void DuplicateSet()
    {
        Set<String> links= FileIOStreamTools.readerDataSet("/home/pinaster/work/何卫明/PHINImage12.txt", "");
        for (String link:links)
        {
            FileIOStreamTools.writeDtatFile(link,"/home/pinaster/work/何卫明/PINImage.txt");

        }

    }

    public static  void spiltFiles()
    {
        String host="";
        List<String> links=new ArrayList<String>();
        links=FileIOStreamTools.readerDataList("/home/pinaster/work/何卫明/PHINImage1.txt","");
        int number=14000;
        int count=1,m=1;
        for (String link:links)
        {
            if (count%number==0)
            {
                System.out.println("当前的分隔线..................................."+m);
                FileIOStreamTools.writeDtatFile(link,"/home/pinaster/work/何卫明/PImage"+m+".txt");
                count=1;
                m++;

                continue;
            }
            System.out.println("当前的分隔线1..................................."+m);
            FileIOStreamTools.writeDtatFile(link,"/home/pinaster/work/何卫明/PImage"+m+".txt");
            count++;

        }
    }
}
