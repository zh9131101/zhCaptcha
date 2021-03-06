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

    private TextImageCaptchaFactory() {
    }

    @Override
    public AbstractCaptcha createCaptcha() {
        return new TextImageCaptcha();
    }

    /**
     * 获取TextImageCaptchaFactory实例
     *
     * @return TextImageCaptchaFactory
     */
    public static TextImageCaptchaFactory getInstance() {
        return SingletonInstance.INSTANCE;
    }

    private static class SingletonInstance {
        private static final TextImageCaptchaFactory INSTANCE = new TextImageCaptchaFactory();
    }
}
