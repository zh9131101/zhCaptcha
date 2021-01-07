/*
 * Copyright 2021-2039 ZH9131101.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.zh9131101.utils;

import java.io.Serializable;
import java.util.HashMap;

/**
 * <p>
 * 前端统一返回 工具类
 * </p>
 *
 * @author zh9131101
 * @version V1.0.0
 * @date 2021-01-07 22:08
 * @since 1.0
 */

public class ReplyUtils extends HashMap<String, Object> implements Serializable {

    private static final long serialVersionUID = -534882529055502206L;

    /**
     * 响应码
     */
    private Integer code;

    /**
     * 响应信息
     */
    private String msg;

    /**
     * 响应数据
     */
    private Object data;

    /**
     * 获取响应码
     *
     * @return 响应码
     */
    public Integer getCode() {
        return code;
    }

    /**
     * 设置响应码
     *
     * @param code 响应码
     */
    public void setCode(Integer code) {
        this.code = code;
        this.put("code", code);
    }

    /**
     * 获取响应信息
     *
     * @return 响应信息
     */
    public String getMsg() {
        return msg;
    }

    /**
     * 设置响应信息
     *
     * @param msg 响应信息
     */
    public void setMsg(String msg) {
        this.msg = msg;
        this.put("msg", msg);
    }

    /**
     * 获取响应数据
     *
     * @return 响应数据
     */
    public Object getData() {
        return data;
    }

    /**
     * 设置响应数据
     *
     * @param data 响应数据
     */
    public void setData(Object data) {
        this.data = data;
        this.put("data", data);
    }

    /**
     * 无参构造函数
     */
    private ReplyUtils() {
        super();
    }

    /**
     * 带容量的无参构造函数
     *
     * @param initialCapacity 容量
     */
    private ReplyUtils(int initialCapacity) {
        super(initialCapacity);
    }

    /**
     * 有参构造函数
     *
     * @param code 响应码
     * @param msg  响应信息
     * @param data 响应数据
     */
    public ReplyUtils(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        init(this, code, msg, data);
    }

    /**
     * 获取响应体实例
     *
     * @param initialCapacity 容量
     * @return 响应体
     */
    public static ReplyUtils getInstance(int initialCapacity) {
        return new ReplyUtils(initialCapacity);
    }

    /**
     * 获取响应体实例
     *
     * @param initialCapacity 容量
     * @param code            响应码
     * @param msg             响应信息
     * @param data            响应数据
     * @return 响应体
     */
    public static ReplyUtils getInstance(int initialCapacity, Integer code, String msg, Object data) {
        return init(new ReplyUtils(initialCapacity), code, msg, data);
    }

    /**
     * 生成响应体
     *
     * @param code 响应码
     * @param msg  响应信息
     * @param data 响应数据
     * @return 响应体
     */
    public static ReplyUtils getInstance(Integer code, String msg, Object data) {
        return new ReplyUtils(code, msg, data);
    }

    /**
     * 初始化响应体
     *
     * @param replyUtils 响应对象
     * @param code       响应码
     * @param msg        响应信息
     * @param data       响应数据
     * @return 响应体
     */
    private static ReplyUtils init(ReplyUtils replyUtils, Integer code, String msg, Object data) {
        replyUtils.put("code", code);
        replyUtils.put("msg", msg);
        replyUtils.put("data", data);
        return replyUtils;
    }

    /**
     * 成功响应信息体
     *
     * @return 响应体
     */
    public static ReplyUtils success() {
        return getInstance(OpcodeTypeEnum.SUCCESS.getCode(), OpcodeTypeEnum.SUCCESS.getMsg(), null);
    }

    /**
     * 成功响应信息体
     *
     * @param code 响应码
     * @return 响应体
     */
    public static ReplyUtils success(Integer code) {
        return getInstance(code, OpcodeTypeEnum.SUCCESS.getMsg(), null);
    }

    /**
     * 成功响应信息体
     *
     * @param msg 响应信息
     * @return 响应体
     */
    public static ReplyUtils success(String msg) {
        return getInstance(OpcodeTypeEnum.SUCCESS.getCode(), msg, null);
    }

    /**
     * 成功响应信息体
     *
     * @param data 响应数据
     * @return 响应体
     */
    public static ReplyUtils success(Object data) {
        return getInstance(OpcodeTypeEnum.SUCCESS.getCode(), OpcodeTypeEnum.SUCCESS.getMsg(), data);
    }

    /**
     * 成功响应信息体
     *
     * @param code 响应码
     * @param msg  响应信息
     * @return 响应体
     */
    public static ReplyUtils success(Integer code, String msg) {
        return getInstance(code, msg, null);
    }

    /**
     * 成功响应信息体
     *
     * @param msg  响应信息
     * @param data 响应数据
     * @return 响应体
     */
    public static ReplyUtils success(String msg, Object data) {
        return getInstance(OpcodeTypeEnum.SUCCESS.getCode(), msg, data);
    }

    /**
     * 成功响应信息体
     *
     * @param code 响应码
     * @param msg  响应信息
     * @param data 响应数据
     * @return 响应体
     */
    public static ReplyUtils success(Integer code, String msg, Object data) {
        return getInstance(code, msg, data);
    }

    /**
     * 错误响应信息体
     *
     * @return 响应体
     */
    public static ReplyUtils fail() {
        return getInstance(OpcodeTypeEnum.FAIL.getCode(), OpcodeTypeEnum.FAIL.getMsg(), null);
    }

    /**
     * 错误响应信息体
     *
     * @param code 响应码
     * @return 响应体
     */
    public static ReplyUtils fail(Integer code) {
        return getInstance(code, OpcodeTypeEnum.FAIL.getMsg(), null);
    }

    /**
     * 错误响应信息体
     *
     * @param msg 响应信息
     * @return 响应体
     */
    public static ReplyUtils fail(String msg) {
        return getInstance(OpcodeTypeEnum.FAIL.getCode(), msg, null);
    }

    /**
     * 错误响应信息体
     *
     * @param data 响应数据
     * @return 响应体
     */
    public static ReplyUtils fail(Object data) {
        return getInstance(OpcodeTypeEnum.FAIL.getCode(), OpcodeTypeEnum.FAIL.getMsg(), data);
    }

    /**
     * 错误响应信息体
     *
     * @param code 响应码
     * @param msg  响应信息
     * @return 响应体
     */
    public static ReplyUtils fail(Integer code, String msg) {
        return getInstance(code, msg, null);
    }

    /**
     * 错误响应信息体
     *
     * @param msg  响应信息
     * @param data 响应数据
     * @return 响应体
     */
    public static ReplyUtils fail(String msg, Object data) {
        return getInstance(OpcodeTypeEnum.FAIL.getCode(), msg, data);
    }

    /**
     * 错误响应信息体
     *
     * @param code 响应码
     * @param msg  响应信息
     * @param data 响应数据
     * @return 响应体
     */
    public static ReplyUtils fail(Integer code, String msg, Object data) {
        return getInstance(code, msg, data);
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}

enum OpcodeTypeEnum {
    // 操作成功
    SUCCESS(0, "成功"),
    // 操作失败
    FAIL(1, "失败");

    /**
     * 操作码
     */
    private Integer code;

    /**
     * 操作信息
     */
    private String msg;

    /**
     * 有参构造函数
     *
     * @param code 操作码
     * @param msg  操作信息
     */
    OpcodeTypeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 获取操作信息
     *
     * @return 操作信息
     */
    public String getMsg() {
        return this.msg;
    }

    /**
     * 获取操作码
     *
     * @return 操作码
     */
    public Integer getCode() {
        return this.code;
    }

    /**
     * 通过操作码获取操作信息
     *
     * @return 操作信息
     */
    public static String getEnumMsg(Integer code) {
        for (OpcodeTypeEnum e : OpcodeTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e.msg;
            }
        }
        return null;
    }

}

