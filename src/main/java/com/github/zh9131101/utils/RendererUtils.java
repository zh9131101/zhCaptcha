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

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.QuadCurve2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * 验证码渲染 工具类
 * </p>
 *
 * @author zh9131101
 * @version V1.0.0
 * @date 2021-01-07 22:08
 * @since 1.0
 */

public class RendererUtils {
    private RendererUtils() {
    }

    private static final String PNG = "png";

    /**
     * 渲染2D验证码
     *
     * @param out     输出流
     * @param captcha 验证码
     * @return true or false
     */
    public static boolean rendererPng(OutputStream out, AbstractCaptcha captcha) {
        try {
            checkCaptcha(captcha);
            // 验证码图片宽度
            Integer width = captcha.getWidth();
            // 验证码图片长度
            int height = captcha.getHeight();
            checkFont(captcha);
            Font font = captcha.getFont();
            char[] captchaChars = captcha.getCaptchaChars().toCharArray();
            BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = (Graphics2D) bi.getGraphics();
            // 填充背景
            g2d.setColor(captcha.getBgColor() == null ? Color.WHITE : captcha.getBgColor());
            g2d.fillRect(0, 0, width, height);
            // 抗锯齿
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            // 画干扰圆
            drawObstructOval(width, height, captcha.getObstructOvalCount(), null, g2d);
            // 画干扰线
            drawObstructLine(width, height, captcha.getObstructLineCount(), null, g2d);
            // 画干扰点
            drawObstructPoint(width, height, captcha.getObstructPointCount(), null, g2d);
            // 画干扰线（贝塞尔曲线）
            g2d.setStroke(new BasicStroke(1.2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
            drawBezierCurves(width, height, captcha.getBezierCurvesCount(), null, g2d);
            // 画字符串
            g2d.setFont(font);
            FontMetrics fontMetrics = g2d.getFontMetrics();
            // 每一个字符所占的宽度
            int fW = width / captchaChars.length;
            // 字符的左右边距
            int fSp = (fW - (int) fontMetrics.getStringBounds(alphaPattern(String.valueOf(captchaChars)), g2d).getWidth()) / 2;
            for (int i = 0; i < captchaChars.length; i++) {
                g2d.setColor(color());
                // 文字的纵坐标
                int fY = height - ((height - (int) fontMetrics.getStringBounds(String.valueOf(captchaChars[i]), g2d).getHeight()) >> 1);
                g2d.drawString(String.valueOf(captchaChars[i]), i * fW + fSp + 3, fY - 3);
            }
            g2d.dispose();
            ImageIO.write(bi, PNG, out);
            out.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 渲染GIF验证码
     *
     * @param os      输出流
     * @param captcha 验证码
     * @return true or false
     */
    public static boolean rendererGif(OutputStream os, AbstractCaptcha captcha) {
        try {
            checkCaptcha(captcha);
            // 验证码图片宽度
            Integer width = captcha.getWidth();
            // 验证码图片长度
            int height = captcha.getHeight();
            // 随机生成每个文字的颜色
            int len = captcha.getCaptchaChars().length();
            Color[] fontColor = new Color[len];
            for (int i = 0; i < len; i++) {
                fontColor[i] = color();
            }
            // 随机生成贝塞尔曲线参数
            int x1 = 5;
            int y1 = RandomUtils.randomNumber(5, height / 2);
            int x2 = width - 5;
            int y2 = RandomUtils.randomNumber(height / 2, height - 5);
            int ctrlx = RandomUtils.randomNumber(width / 4, width / 4 * 3);
            int ctrly = RandomUtils.randomNumber(5, height - 5);
            if (RandomUtils.randomNumber(2) == 0) {
                int ty = y1;
                y1 = y2;
                y2 = ty;
            }
            int ctrlx1 = RandomUtils.randomNumber(width / 4, width / 4 * 3);
            int ctrly1 = RandomUtils.randomNumber(5, height - 5);
            int[][] besselXY = new int[][]{{x1, y1}, {ctrlx, ctrly}, {ctrlx1, ctrly1}, {x2, y2}};
            // 开始画gif每一帧
            AnimatedGifEncoder gifEncoder = new AnimatedGifEncoder();
            gifEncoder.setQuality(180);
            gifEncoder.setDelay(100);
            gifEncoder.setRepeat(0);
            gifEncoder.start(os);
            for (int i = 0; i < captcha.getCaptchaChars().length(); i++) {
                BufferedImage frame = graphicsGifImage(captcha, fontColor, i, besselXY);
                gifEncoder.addFrame(frame);
                frame.flush();
            }
            gifEncoder.finish();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 画gif随机验证码图
     *
     * @param captcha   验证码
     * @param fontColor 随机字体颜色
     * @param flag      透明度
     * @param besselXY  干扰线参数
     * @return BufferedImage
     */
    private static BufferedImage graphicsGifImage(AbstractCaptcha captcha, Color[] fontColor, int flag, int[][] besselXY) {
        Integer width = captcha.getWidth();
        Integer height = captcha.getHeight();
        checkFont(captcha);
        Font font = captcha.getFont();
        char[] captchaChars = captcha.getCaptchaChars().toCharArray();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = (Graphics2D) image.getGraphics();
        // 填充背景颜色
        g2d.setColor(captcha.getBgColor() == null ? Color.WHITE : captcha.getBgColor());
        g2d.fillRect(0, 0, width, height);
        // 抗锯齿
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // 画干扰圆圈, 设置透明度
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f * RandomUtils.randomNumber(10)));
        drawObstructOval(width, height, captcha.getObstructOvalCount(), null, g2d);
        // 画干扰线
        drawObstructLine(width, height, captcha.getObstructLineCount(), null, g2d);
        // 画干扰点
        drawObstructPoint(width, height, captcha.getObstructPointCount(), null, g2d);
        // 画干扰线, 设置透明度
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
        drawBezierCurves(width, height, captcha.getBezierCurvesCount(), null, g2d);
        g2d.setStroke(new BasicStroke(1.2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
        g2d.setColor(fontColor[0]);
        CubicCurve2D shape = new CubicCurve2D.Double(besselXY[0][0], besselXY[0][1], besselXY[1][0], besselXY[1][1], besselXY[2][0], besselXY[2][1], besselXY[3][0], besselXY[3][1]);
        g2d.draw(shape);
        // 画验证码
        g2d.setFont(font);
        FontMetrics fontMetrics = g2d.getFontMetrics();
        int len = captchaChars.length;
        // 每一个字符所占的宽度
        int fW = width / len;
        // 字符的左右边距
        int fSp = (fW - (int) fontMetrics.getStringBounds(alphaPattern(String.valueOf(captchaChars)), g2d).getWidth()) / 2;
        for (int i = 0; i < len; i++) {
            // 设置透明度
            AlphaComposite ac3 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha(len, flag, i));
            g2d.setComposite(ac3);
            g2d.setColor(fontColor[i % fontColor.length]);
            // 文字的纵坐标
            int fY = height - ((height - (int) fontMetrics.getStringBounds(String.valueOf(captchaChars[i]), g2d).getHeight()) >> 1);
            g2d.drawString(String.valueOf(captchaChars[i]), i * fW + fSp + 3, fY - 3);
        }
        g2d.dispose();
        return image;
    }

    /**
     * 检查生成验证码
     *
     * @param captcha 验证码抽象类
     */
    private static void checkCaptcha(AbstractCaptcha captcha){
        if (StringUtils.isEmpty(captcha.getCaptcha()) || StringUtils.isEmpty(captcha.getCaptchaChars())) {
            captcha.generateCaptcha();
        }
    }

    /**
     * 检查生成字体
     *
     * @param captcha 验证码抽象类
     */
    private static void checkFont(AbstractCaptcha captcha){
        Font font = captcha.getFont();
        if (font == null) {
            font = RendererUtils.createFont(captcha.getFontPath(), captcha.getFontStyle(), captcha.getFontSize(), captcha.getFontName());
            captcha.setFont(font);
        }
    }

    /**
     * 设置字体左右边距
     *
     * @param captcha 验证码
     * @return String
     */
    private static String alphaPattern(String captcha) {
        String patternString = captcha.trim();
        // 正则匹配数字
        String number = "\\d+";
        // 正则匹配中文
        String chinese = "[\\u4e00-\\u9fa5]+";
        Pattern compile = Pattern.compile(number);
        Matcher matcher = compile.matcher(patternString);
        if (matcher.matches()) {
            return "8";
        }
        compile = Pattern.compile(chinese);
        matcher = compile.matcher(patternString);
        if (matcher.matches()) {
            return "苏";
        }
        return "Z";
    }

    /**
     * 获取透明度,从0到1,自动计算步长
     *
     * @param length 长度
     * @param i      验证码字符数组遍历值
     * @param j      验证码字符数组遍历值
     * @return 透明度
     */
    private static float alpha(Integer length, Integer i, Integer j) {
        int num = i + j;
        float r = (float) 1 / (length - 1);
        float s = length * r;
        return num >= length ? (num * r - s) : num * r;
    }

    /**
     * 随机画干扰线
     *
     * @param num   数量
     * @param color 颜色
     * @param g     Graphics2D
     */
    public static void drawObstructLine(Integer width, Integer height, int num, Color color, Graphics2D g) {
        for (int i = 0; i < num; i++) {
            g.setColor(color == null ? color() : color);
            int x1 = RandomUtils.randomNumber(-10, width - 10);
            int y1 = RandomUtils.randomNumber(5, height - 5);
            int x2 = RandomUtils.randomNumber(10, width + 10);
            int y2 = RandomUtils.randomNumber(2, height - 2);
            g.drawLine(x1, y1, x2, y2);
        }
    }

    /**
     * 随机产生干扰点
     *
     * @param num   数量
     * @param color 颜色
     * @param g     Graphics2D
     */
    private static void drawObstructPoint(Integer width, Integer height, int num, Color color, Graphics2D g)
    {  // 随机产生干扰点
        for (int i=0;i<num;i++) {
            int x = RandomUtils.randomNumber(width);
            int y = RandomUtils.randomNumber(height);
            g.setColor(color == null ? color() : color);
            g.drawOval(x,y,RandomUtils.randomNumber(3),RandomUtils.randomNumber(3));
        }
    }

    /**
     * 随机画干扰圆
     *
     * @param num   数量
     * @param color 颜色
     * @param g     Graphics2D
     */
    private static void drawObstructOval(Integer width, Integer height, int num, Color color, Graphics2D g) {
        for (int i = 0; i < num; i++) {
            g.setColor(color == null ? color() : color);
            int w = 5 + RandomUtils.randomNumber(10);
            g.drawOval(RandomUtils.randomNumber(width - 25), RandomUtils.randomNumber(height - 15), w, w);
        }
    }

    /**
     * 随机画贝塞尔曲线
     *
     * @param num   数量
     * @param color 颜色
     * @param g     Graphics2D
     */
    private static void drawBezierCurves(Integer width, Integer height, int num, Color color, Graphics2D g) {
        for (int i = 0; i < num; i++) {
            g.setColor(color == null ? color() : color);
            int x1 = 5;
            int y1 = RandomUtils.randomNumber(5, height / 2);
            int x2 = width - 5;
            int y2 = RandomUtils.randomNumber(height / 2, height - 5);
            int ctrlx = RandomUtils.randomNumber(width / 4, width / 4 * 3);
            int ctrly = RandomUtils.randomNumber(5, height - 5);
            if (RandomUtils.randomNumber(2) == 0) {
                int ty = y1;
                y1 = y2;
                y2 = ty;
            }
            // 二阶贝塞尔曲线
            if (RandomUtils.randomNumber(2) == 0) {
                QuadCurve2D shape = new QuadCurve2D.Double();
                shape.setCurve(x1, y1, ctrlx, ctrly, x2, y2);
                g.draw(shape);
            } else {  // 三阶贝塞尔曲线
                int ctrlx1 = RandomUtils.randomNumber(width / 4, width / 4 * 3);
                int ctrly1 = RandomUtils.randomNumber(5, height - 5);
                CubicCurve2D shape = new CubicCurve2D.Double(x1, y1, ctrlx, ctrly, ctrlx1, ctrly1, x2, y2);
                g.draw(shape);
            }
        }
    }

    /**
     * 指定颜色中获取随机颜色值
     *
     * @return RGB颜色
     */
    public static Color color() {
        int[] rgb = CaptchaConst.COLOR[RandomUtils.randomNumber(CaptchaConst.COLOR.length)];
        return new Color(rgb[0], rgb[1], rgb[2]);
    }

    /**
     * 创建字体
     *
     * @return 字体
     */
    public static Font createFont(String fontPath, Integer fontStyle, Integer fontSize, String fontName) {
        Font font;
        try {
            String path = fontPath;
            if (StringUtils.isEmpty(fontPath)) {
                path = StringUtils.FOLDER_SEPARATOR + "font" + StringUtils.FOLDER_SEPARATOR;
            } else {
                if (!fontPath.startsWith(StringUtils.FOLDER_SEPARATOR)) {
                    path = StringUtils.FOLDER_SEPARATOR + fontPath;
                }
                if (!fontPath.endsWith(StringUtils.FOLDER_SEPARATOR)) {
                    path = fontPath + StringUtils.FOLDER_SEPARATOR;
                }
            }
            if (fontSize == null || fontSize <= 0) {
                fontSize = 28;
            }
            if (fontStyle == null || fontStyle <= 0) {
                fontStyle = Font.PLAIN;
            }
            if (StringUtils.isEmpty(fontName)) {
                fontName = "microsoft_yahei.ttf";
            }
            font = Font.createFont(Font.TRUETYPE_FONT, RendererUtils.class.getResourceAsStream(path + fontName)).deriveFont(fontStyle, fontSize);
        } catch (Exception e) {
            font = new Font("Arial", Font.PLAIN, 28);
            e.printStackTrace();
        }
        return font;
    }

}

