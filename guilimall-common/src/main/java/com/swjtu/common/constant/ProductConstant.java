package com.swjtu.common.constant;

/**
 * @Author: Lil_boat
 * @Date: 2022/8/24 21:56
 * @Description:
 */
public class ProductConstant {

    public enum AttrEnum {
        /**
         * 商品的属性
         * 1表示基本属性
         * 2表示销售属性
         */
        ATTR_TYPE_BASE(1, "基本属性"), ATTR_TYPE_SALE(0, "销售属性");

        private int code;
        private String msg;

        AttrEnum(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }

    public enum StatusEnum {
        /**
         * 商品的属性
         * 0：新建
         * 1：商品上架
         * 2：商品下架
         */
        NEW_SPU(0, "新建"), SPU_UP(1, "商品上架"),
        SPU_DOWN(1, "商品下架");

        private int code;
        private String msg;

        StatusEnum(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }

}
