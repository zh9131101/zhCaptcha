package com.github.zh9131101.textimage;

import com.github.zh9131101.utils.RandomUtils;

/**
 * <p>
 * 验证码 抽象类
 * </p>
 *
 * @author zh9131101
 * @version V1.0.0
 * @date 2021-01-07 22:00
 * @since 1.0
 */

public class TextImageCaptcha extends AbstractCaptcha {

    @Override
    public String generateCaptcha() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(randomText[RandomUtils.randomNumber(randomText.length)]);
        }
        captchaChars = sb.toString();
        captcha = captchaChars;
        return captcha;
    }
}
