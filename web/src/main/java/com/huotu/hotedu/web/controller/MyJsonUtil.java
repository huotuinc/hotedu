package com.huotu.hotedu.web.controller;

import java.util.ArrayList;

/**
 * Created by jiashubing on 2015/7/22.
 */
public class MyJsonUtil {

    /**
     * 将list类型转换为String类型
     * @param arrayList 需要转换的list
     * @return          String类型数据
     */
    public String convertObjectToJsonBytes(ArrayList<Long> arrayList){
        String json = "";
        json += arrayList.get(0).toString();
        for(int i=1; i<arrayList.size(); i++){
            json +=","+ arrayList.get(i).toString();
        }
        return json;
    }

    /**
     * 将String类型转换为list类型
     * @param json      需要转换的String
     * @return          ArrayList类型数据
     */
    public ArrayList<Long> convertJsonBytesToArrayList(String json){
        ArrayList<Long> arrayList = new ArrayList<Long>(20);
        if(json.equals("")){
            System.out.print("字符串为空");
        }
        String[] stringArr= json.split(",");
        for(int i=0;i<stringArr.length;i++){
            System.out.println("未分班学员的编号 " + Long.parseLong(stringArr[i]));
            arrayList.add(Long.parseLong(stringArr[i]));
        }
        return arrayList;
    }

}
