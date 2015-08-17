package com.huotu.hotedu.util;
import com.alibaba.fastjson.JSON;
import com.huotu.hotedu.model.EnumModel;
import com.huotu.hotedu.model.ICommonEnum;

import java.util.ArrayList;
import java.util.List;


/**
 * 枚举处理类
 *
 * @author Administrator
 */
public class EnumHelper {

    public static String GetEnumName(Class<? extends ICommonEnum> cls, int value) {
        ICommonEnum ice = getEnumType(cls, value);
        if (ice != null) {
            return ice.getName();
//            try {
//                return new String(ice.getName().getBytes("UTF-8"));
//            } catch (UnsupportedEncodingException e) {
//                return "";
//            }
        }
        return "";
    }

    public static <T extends ICommonEnum> T getEnumType(Class<T> cls, int value) {
        for (T item : cls.getEnumConstants()) {
            if (item.getValue() == value) {
                return item;
            }
        }
        return null;
    }

    public static String toJsonString(Class<? extends ICommonEnum> cls) {
        List<EnumModel> list = new ArrayList<EnumModel>();
        for (ICommonEnum e : cls.getEnumConstants()) {
            EnumModel model = new EnumModel();
            model.setValue(e.getValue());
            model.setName(e.getName());
            list.add(model);
        }

        return JSON.toJSONString(list);
    }

    public static List<EnumModel> list(Class<? extends ICommonEnum> cls) {
        List<EnumModel> list = new ArrayList<EnumModel>();
        for (ICommonEnum e : cls.getEnumConstants()) {
            EnumModel model = new EnumModel();
            model.setValue(e.getValue());
            model.setName(e.getName());
            list.add(model);
        }

        return list;
    }
}
