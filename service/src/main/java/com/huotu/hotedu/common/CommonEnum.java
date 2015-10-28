package com.huotu.hotedu.common;

/**
 * 枚举类
 * Created by WenbinChen on 2015/10/28 11:13.
 */
public interface CommonEnum {

    /**
     * 公告类型
     */
    enum NoticeType {

        Course(1,"课程");

        NoticeType(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private int value;
        private String name;

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
