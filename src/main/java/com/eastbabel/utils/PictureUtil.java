package com.eastbabel.utils;

import java.util.Calendar;
import java.util.Random;

public class PictureUtil {
    public static String generateRandomFilename(){
        String ranname = "";  //定义文件名
        Random rand = new Random();//生成随机数
        int r = rand.nextInt();

        Calendar calCurrent = Calendar.getInstance();
        int intDay = calCurrent.get(Calendar.DATE);
        int intMonth = calCurrent.get(Calendar.MONTH) + 1;
        int intYear = calCurrent.get(Calendar.YEAR);
        String now = String.valueOf(intYear) + "_" + String.valueOf(intMonth) + "_" +String.valueOf(intDay) + "_";
        ranname = now + String.valueOf(r > 0 ? r : ( -1) * r);
        return ranname;
    }

    public static void main(String[] args) {
        PictureUtil.generateRandomFilename();
    }

}
