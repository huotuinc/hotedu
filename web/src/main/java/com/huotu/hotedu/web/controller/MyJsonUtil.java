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

   /* public static void main(String[] args){
        MyJsonUtil m = new MyJsonUtil();
        String s = "5,4,3,2,1";
        ArrayList<Long> arrayList =  m.convertJsonBytesToArrayList(s);
        String json = m.convertObjectToJsonBytes(arrayList);
        System.out.println(json);
        String retUrl = "http://localhost:8080/hotedu/pc/index?errInfo=hhh&aa=1";
        String ans="";
        if(retUrl != null){
            String[] url = retUrl.split("/");
            ans="redirect:";
            for (int i = 0 ; i <url.length ; i++ ) {
                if("hotedu".equals(url[i])){
                    for(int j=i+1; j<url.length; j++){
                        if(url[j].contains("?")) {
                            ans += "/"+url[j].substring(0,url[j].indexOf("?"));
                            break;
                        }
                        ans += "/" + url[j];
                    }
                    break;
                }
            }
        }
        System.out.println(ans);
    }*/
}
