package com.github.zh9131101.textimage;

import com.github.zh9131101.constant.CaptchaConst;

import java.awt.*;

/**
 * <p>
 * 验证码 抽象类
 * </p>
 *
 * @author zh9131101
 * @version V1.0.0
 * @date 2021-01-07 21:57
 * @since 1.0
 */

public abstract class AbstractCaptcha {
    /**
     * 验证码的字体：默认宋体
     */
    protected Font font;

    /**
     * 验证码的背景色：默认白色
     */
    protected Color bgColor;

    /**
     * 验证码随机字符长度：默认4
     */
    protected Integer length = 4;

    /**
     * 验证码显示宽度：默认130
     */
    protected Integer width = 130;

    /**
     * 验证码显示高度：默认48
     */
    protected Integer height = 48;

    /**
     * 随机文本内容：默认字母+数字混合
     */
    protected char[] randomText = CaptchaConst.NUM_EN_MIX;

    /**
     * 字体路径：默认/font/
     */
    protected String fontPath;

    /**
     * 字体名：默认microsoft_yahei（微软雅黑）
     */
    protected String fontName = "microsoft_yahei.ttf";

    /**
     * 字体样式：默认0
     */
    protected Integer fontStyle = Font.PLAIN;

    /**
     * 字体大小：默认32
     */
    protected Integer fontSize = 28;

    /**
     * 显示验证码
     */
    protected String captchaChars;

    /**
     * 结果验证码
     */
    protected String captcha;

    /**
     * 干扰直线数量：默认1
     */
    protected Integer obstructLineCount = 2;

    /**
     * 干扰圆数量：默认3
     */
    protected Integer obstructOvalCount = 2;

    /**
     * 干扰点数量：默认1
     */
    protected Integer obstructPointCount = 20;

    /**
     * 干扰线（贝塞尔曲线）数量：默认3
     */
    protected Integer bezierCurvesCount = 2;

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public Color getBgColor() {
        return bgColor;
    }

    public void setBgColor(Color bgColor) {
        this.bgColor = bgColor;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public char[] getRandomText() {
        return randomText;
    }

    public void setRandomText(char[] randomText) {
        this.randomText = randomText;
    }

    public String getFontPath() {
        return fontPath;
    }

    public void setFontPath(String fontPath) {
        this.fontPath = fontPath;
    }

    public String getFontName() {
        return fontName;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    public Integer getFontStyle() {
        return fontStyle;
    }

    public void setFontStyle(Integer fontStyle) {
        this.fontStyle = fontStyle;
    }

    public Integer getFontSize() {
        return fontSize;
    }

    public void setFontSize(Integer fontSize) {
        this.fontSize = fontSize;
    }

    public String getCaptchaChars() {
        return captchaChars;
    }

    public void setCaptchaChars(String captchaChars) {
        this.captchaChars = captchaChars;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public Integer getObstructLineCount() {
        return obstructLineCount;
    }

    public void setObstructLineCount(Integer obstructLineCount) {
        this.obstructLineCount = obstructLineCount;
    }

    public Integer getObstructOvalCount() {
        return obstructOvalCount;
    }

    public void setObstructOvalCount(Integer obstructOvalCount) {
        this.obstructOvalCount = obstructOvalCount;
    }

    public Integer getObstructPointCount() {
        return obstructPointCount;
    }

    public void setObstructPointCount(Integer obstructPointCount) {
        this.obstructPointCount = obstructPointCount;
    }

    public Integer getBezierCurvesCount() {
        return bezierCurvesCount;
    }

    public void setBezierCurvesCount(Integer bezierCurvesCount) {
        this.bezierCurvesCount = bezierCurvesCount;
    }

    /**
     * 生成验证码
     *
     * @return 验证码字符数组
     */
    public abstract String generateCaptcha();
}
