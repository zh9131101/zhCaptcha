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

import com.github.zh9131101.constant.CaptchaConst;
import com.github.zh9131101.textimage.AbstractCaptcha;
import com.github.zh9131101.utils.RandomUtils;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * <p>
 * 算术 验证码
 * </p>
 *
 * @author zh9131101
 * @version V1.0.0
 * @date 2021-01-07 22:01
 * @since 1.0
 */

public class ArithmeticCaptcha extends AbstractCaptcha {
    @Override
    public String generateCaptcha() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(CaptchaConst.NUM_ARABIC[RandomUtils.randomNumber(CaptchaConst.NUM_ARABIC.length)]);
            if (i < length - 1) {
                sb.append(CaptchaConst.OPERATOR_EN[RandomUtils.randomNumber(CaptchaConst.OPERATOR_EN.length)]);
            } else {
                calcArithmeticResult(sb.toString());
                sb.append("=?");
            }
        }
        captchaChars = sb.toString();
        return captcha;

    }

    private void calcArithmeticResult(String chars){
        // 计算公式结果，赋值chars
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("javascript");
        try {
            String formula = chars.replaceAll("[÷]", "/").replaceAll("[x]", "*");
            captcha = String.valueOf(engine.eval(formula));
        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }
}
