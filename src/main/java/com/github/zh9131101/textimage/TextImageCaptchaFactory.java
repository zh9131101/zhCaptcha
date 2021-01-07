package com.github.zh9131101.textimage;

/**
 * <p>
 * 图文验证码工厂
 * </p>
 *
 * @author zh9131101
 * @version V1.0.0
 * @date 2021-01-07 22:00
 * @since 1.0
 */

public class TextImageCaptchaFactory implements ICaptchaFactory {

    @Override
    public AbstractCaptcha createCaptcha() {
        return new TextImageCaptcha();
    }
}
