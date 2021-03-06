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
import com.github.zh9131101.textimage.AbstractCaptcha;
import com.github.zh9131101.textimage.ICaptchaFactory;
import com.github.zh9131101.textimage.TextImageCaptchaFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

/**
 * <p>
 * 验证码 工具类
 * </p>
 *
 * @author zh9131101
 * @version V1.0.0
 * @date 2021-01-07 22:05
 * @since 1.0
 */

public class CaptchaUtils {

    private CaptchaUtils() {
    }

    private static final ICaptchaFactory TEXT_IMAGE_CAPTCHA_FACTORY;

    static {
        TEXT_IMAGE_CAPTCHA_FACTORY = TextImageCaptchaFactory.getInstance();
    }

    /* ----->>>模版相关<<<------*/

    /**
     * 字母+数字Png验证码模版
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @throws IOException IO异常
     */
    public static void rendererPng(HttpServletRequest request, HttpServletResponse response) throws IOException {
        AbstractCaptcha captcha = TEXT_IMAGE_CAPTCHA_FACTORY.createCaptcha();
        captcha.setRandomText(CaptchaConst.NUM_EN_MIX);
        sesseionCache(captcha.generateCaptcha(), request);
        rendererPngCaptcha(captcha, response);
    }

    /**
     * 字母+数字验证码Gif模版
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @throws IOException IO异常
     */
    public static void rendererGif(HttpServletRequest request, HttpServletResponse response) throws IOException {
        AbstractCaptcha captcha = TEXT_IMAGE_CAPTCHA_FACTORY.createCaptcha();
        captcha.setRandomText(CaptchaConst.NUM_EN_MIX);
        sesseionCache(captcha.generateCaptcha(), request);
        rendererGifCaptcha(captcha, response);
    }

    /* ----->>>渲染相关<<<------*/

    /**
     * 渲染PNG验证码
     *
     * @param captcha  验证码实体
     * @param response HttpServletResponse
     * @throws IOException 输入输出流异常
     */
    public static void rendererPngCaptcha(AbstractCaptcha captcha, HttpServletResponse response) throws IOException {
        // 设置请求头为输出图片类型
        setResponseHeader(response);
        RendererUtils.renderer(CaptchaConst.PNG, response.getOutputStream(), captcha);
    }

    /**
     * 渲染GIF验证码
     *
     * @param captcha  验证码实体
     * @param response HttpServletResponse
     * @throws IOException 输入输出流异常
     */
    public static void rendererGifCaptcha(AbstractCaptcha captcha, HttpServletResponse response) throws IOException {
        // 设置请求头为输出图片类型
        setResponseHeader(response);
        RendererUtils.renderer(CaptchaConst.GIF, response.getOutputStream(), captcha);
    }

    /**
     * 设置响应头
     *
     * @param response HttpServletResponse
     */
    public static void setResponseHeader(HttpServletResponse response) {
        response.setContentType("image/gif");
        response.setHeader("Pragma", "No-sesseionCache");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, no-sesseionCache");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setDateHeader("Expires", 0);
    }

    /* ----->>>缓存相关<<<------*/

    /**
     * 使用session缓存验证码
     *
     * @param captcha 验证码实体
     * @param request HttpServletRequest
     */
    public static void sesseionCache(String captcha, HttpServletRequest request) {
        request.getSession().setAttribute(CaptchaConst.SESSION_KEY, captcha);
    }

    /**
     * 验证并清除session中的验证码
     *
     * @param captcha 用户输入的验证码
     * @param request HttpServletRequest
     * @return true or false
     */
    public static boolean sessionVerify(String captcha, HttpServletRequest request) {
        if (captcha != null) {
            String cacheCaptcha = (String) request.getSession().getAttribute(CaptchaConst.SESSION_KEY);
            if (captcha.equalsIgnoreCase(cacheCaptcha)) {
                sessionClear(request);
                return captcha.equalsIgnoreCase(cacheCaptcha.trim());
            }
        }
        return false;
    }

    /**
     * 清除session中的验证码
     *
     * @param request HttpServletRequest
     */
    public static void sessionClear(HttpServletRequest request) {
        request.getSession().removeAttribute(CaptchaConst.SESSION_KEY);
    }

    /**
     * 输出base64编码
     * 参考：https://www.cnblogs.com/OpenCoder/p/7127256.html
     *
     * @param outputStream 字节数组输出流
     * @param type         编码头
     * @return base64编码字符串
     */
    public static String toBase64(ByteArrayOutputStream outputStream, String type) {
        return type + Base64.getEncoder().encodeToString(outputStream.toByteArray());
    }
}
