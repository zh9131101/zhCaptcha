package com.github.zh9131101.textimage.arithmetic;

import com.github.zh9131101.textimage.AbstractCaptcha;
import com.github.zh9131101.textimage.ICaptchaFactory;

/**
 * <p>
 * 算术 验证码工厂
 * </p>
 *
 * @author zh9131101
 * @version V1.0.0
 * @date 2021-01-07 22:01
 * @since 1.0
 */

public class ArithmeticCaptchaFactory implements ICaptchaFactory {
    @Override
    public AbstractCaptcha createCaptcha() {
        return new ArithmeticCaptcha();
    }
}
