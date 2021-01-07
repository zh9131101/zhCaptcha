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

import com.github.zh9131101.constant.CaptchaConst;

import java.security.SecureRandom;
import java.util.UUID;
/**
 * <p>
 * 随机 工具类
 * </p>
 *
 * @author zh9131101
 * @version V1.0.0
 * @date 2021-01-07 22:02
 * @since 1.0
 */

public class RandomUtils {

    private RandomUtils() {
    }

    private static final SecureRandom RANDOM = new SecureRandom();


    /**
     * 产生两个数之间的随机数
     *
     * @param min 最小值
     * @param max 最大值
     * @return 随机数
     */
    public static int randomNumber(int min, int max) {
        return Math.min(min, max) + RANDOM.nextInt(Math.abs(max - min));
    }

    /**
     * 产生0-max的随机数,不包括max
     *
     * @param max 最大值
     * @return 随机数
     */
    public static int randomNumber(int max) {
        return RANDOM.nextInt(max);
    }

    /**
     * 返回ALPHA中的随机字符
     *
     * @return 随机字符
     */
    public static char randomChar() {
        return CaptchaConst.NUM_EN_MIX[randomNumber(CaptchaConst.NUM_EN_MIX.length)];
    }

    /**
     * 返回ALPHA中第0位到第num位的随机字符
     *
     * @param max 到第几位结束
     * @return 随机字符
     */
    public static char randomChar(int max) {
        return CaptchaConst.NUM_EN_MIX[randomNumber(max)];
    }

    /**
     * 返回ALPHA中第min位到第max位的随机字符
     *
     * @param min 从第几位开始
     * @param max 到第几位结束
     * @return 随机字符
     */
    public static char randomChar(int min, int max) {
        return CaptchaConst.NUM_EN_MIX[randomNumber(min, max)];
    }

    /**
     * 返回随机汉字
     *
     * @return 随机汉字
     */
    public static char randomChineseChar() {
        return CaptchaConst.CHINESE[RandomUtils.randomNumber(CaptchaConst.CHINESE.length)];
    }


    /**
     * 生成UUID
     *
     * @return UUID
     */
    public static String getUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 获取随机字符串
     *
     * @param length 随机字符长度
     * @return 随机字符串
     */
    public static String randomString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(CaptchaConst.NUM_EM_ALL[randomNumber(62)]);
        }
        return sb.toString();
    }

}
