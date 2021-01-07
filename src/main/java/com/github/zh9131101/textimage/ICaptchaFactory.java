package com.github.zh9131101.textimage;

/**
 * <p>
 * 验证码工厂
 * </p>
 *
 * @author zh9131101
 * @version V1.0.0
 * @date 2021-01-07 21:59
 * @since 1.0
 */

public interface ICaptchaFactory {
    /**
     * 创建验证码对象
     *
     * @return 验证码对象
     */
    AbstractCaptcha createCaptcha();
}
