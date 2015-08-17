package com.huotu.hotedu.util;

import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Random;

public class StringHelper {
    public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (!StringUtils.isEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            int index = ip.indexOf(",");
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if (!StringUtils.isEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            return ip;
        }
        return request.getRemoteAddr();
    }

    public static String RandomNo(Random ran, int xLen) {
        String[] char_array = new String[34];
        char_array[0] = "1";
        char_array[1] = "2";
        char_array[2] = "3";
        char_array[3] = "4";
        char_array[4] = "5";
        char_array[5] = "6";
        char_array[6] = "7";
        char_array[7] = "8";
        char_array[8] = "9";
        char_array[9] = "A";
        char_array[10] = "B";
        char_array[11] = "C";
        char_array[12] = "D";
        char_array[13] = "E";
        char_array[14] = "F";
        char_array[15] = "G";
        char_array[16] = "H";
        char_array[17] = "I";
        char_array[18] = "J";
        char_array[19] = "K";
        char_array[20] = "L";
        char_array[21] = "M";
        char_array[22] = "N";
        char_array[23] = "P";
        char_array[24] = "Q";
        char_array[25] = "R";
        char_array[26] = "S";
        char_array[27] = "T";
        char_array[28] = "W";
        char_array[29] = "U";
        char_array[30] = "V";
        char_array[31] = "X";
        char_array[32] = "Y";
        char_array[33] = "Z";

        String output = "";
        double tmp = 0;
        while (output.length() < xLen) {
            tmp = ran.nextFloat();
            output = output + char_array[(int) (tmp * 34)];
        }
        return output;
    }

    public static String RandomNum(Random ran, int xLen) {
        String[] char_array = new String[9];
        char_array[0] = "1";
        char_array[1] = "2";
        char_array[2] = "3";
        char_array[3] = "4";
        char_array[4] = "5";
        char_array[5] = "6";
        char_array[6] = "7";
        char_array[7] = "8";
        char_array[8] = "9";

        String output = "";
        double tmp = 0;
        while (output.length() < xLen) {
            tmp = ran.nextFloat();
            output = output + char_array[(int) (tmp * 9)];
        }
        return output;
    }

    /**
     * 父数据给子数据
     * @param parent
     * @param pcls
     * @param cls
     * @param <T>
     * @param <X>
     * @return
     */
    public static <T extends X, X> T SupperDataToSub(X parent, Class<X> pcls, Class<T> cls) {
        try {
            T result = null;
            if (parent != null) {
                result = cls.newInstance();

                BeanInfo beanInfo = Introspector.getBeanInfo(cls);
                PropertyDescriptor[] propertyDescriptors = beanInfo
                        .getPropertyDescriptors();

                for (PropertyDescriptor property : propertyDescriptors) {
                    String key = property.getName();
                    if(!"class".equals(key)) {
                        Object value = getSupperValue(parent, pcls, key);
                        // 得到property对应的setter方法
                        Method setter = property.getWriteMethod();
                        setter.invoke(result, value);
                    }
                }
            }

            return result;
        } catch (Exception ex) {
            return null;
        }
    }

    private static <X> Object getSupperValue(X parent,Class<X> cls, String key) {
        Object result = null;
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(cls);
            PropertyDescriptor[] propertyDescriptors = beanInfo
                    .getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                if (key.equals(property.getName())) {
                    // 得到property对应的setter方法
                    Method getter = property.getReadMethod();
                    return getter.invoke(parent,null);
                }
            }
        } catch (Exception ex) {

        }
        return result;
    }

    public static <T, X> T CopyMemberValue(X parent, Class<X> pcls, Class<T> cls) {
        try {
            T result = null;
            if (parent != null) {
                result = cls.newInstance();

                BeanInfo beanInfo = Introspector.getBeanInfo(cls);
                PropertyDescriptor[] propertyDescriptors = beanInfo
                        .getPropertyDescriptors();

                for (PropertyDescriptor property : propertyDescriptors) {
                    String key = property.getName();
                    if(!"class".equals(key)) {
                        Object value = getSupperValue(parent, pcls, key);
                        // 得到property对应的setter方法
                        Method setter = property.getWriteMethod();
                        setter.invoke(result, value);
                    }
                }
            }

            return result;
        } catch (Exception ex) {
            return null;
        }
    }

}
