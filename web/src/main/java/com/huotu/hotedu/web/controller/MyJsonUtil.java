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
    public static String convertObjectToJsonBytes(ArrayList<Object> arrayList){
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
    public static ArrayList<Object> convertJsonBytesToArrayList(String json){
        ArrayList<Object> arrayList = new ArrayList<>(20);
        String[] stringArr= json.split(",");
        for(int i=0;i<stringArr.length;i++){
            arrayList.add(stringArr[i]);
        }
        return arrayList;
    }


    public static void main(String[] args){
        MyJsonUtil m = new MyJsonUtil();
        String s = "5,4,3,2,1";
        ArrayList<Object> arrayList =  m.convertJsonBytesToArrayList(s);
        for(int i=0; i<arrayList.size(); i++){
            System.out.println(arrayList.get(i));
        }
        String json = m.convertObjectToJsonBytes(arrayList);
        System.out.println(json);
    }
}
