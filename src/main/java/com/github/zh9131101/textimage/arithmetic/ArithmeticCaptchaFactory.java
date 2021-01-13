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

    private ArithmeticCaptchaFactory() {
    }

    @Override
    public AbstractCaptcha createCaptcha() {
        return new ArithmeticCaptcha();
    }

    /**
     * 获取ArithmeticCaptchaFactory实例
     *
     * @return ArithmeticCaptchaFactory
     */
    public static ArithmeticCaptchaFactory getInstance() {
        return SingletonInstance.INSTANCE;
    }

    private static class SingletonInstance {
        private static final ArithmeticCaptchaFactory INSTANCE = new ArithmeticCaptchaFactory();
    }
}
