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

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.*;

/**
 * <p>
 * 字符串 工具类
 * </p>
 *
 * @author zh9131101
 * @version V1.0.0
 * @date 2021-01-07 22:09
 * @since 1.0
 */

public class StringUtils {

    private StringUtils() {
    }

    public static final String[] EMPTY_STRING_ARRAY = {};

    public static final String FOLDER_SEPARATOR = "/";

    public static final String WINDOWS_FOLDER_SEPARATOR = "\\";

    public static final String TOP_PATH = "..";

    public static final String CURRENT_PATH = ".";

    public static final char EXTENSION_SEPARATOR = '.';

    public static final int STRING_BUILDER_SIZE = 256;

    /**
     * 空格字符的字符串。
     */
    public static final String SPACE = " ";

    /**
     * 空的字符串"" 。
     */
    public static final String EMPTY = "";

    /**
     * 换行符LF（“ \ n”）的字符串。
     */
    public static final String LF = "\n";

    /**
     * 回车CR（“ \ r”）的字符串。
     */
    public static final String CR = "\r";

    /**
     * 表示索引搜索失败。
     */
    public static final int INDEX_NOT_FOUND = -1;

    /**
     * 填充常数可以扩展到的最大大小。
     */
    private static final int PAD_LIMIT = 8192;

    /* Part of common lang3*/

    // Abbreviating
    //-----------------------------------------------------------------------
    /**
     *使用省略号缩写字符串。 这将把“现在是所有好男人的时间”变成“现在是...的时间”。
     * 特别：
     * 如果str的字符数小于或等于maxWidth ，则返回str 。
     * 否则将其缩写为(substring(str, 0, max-3) + "...") 。
     * 如果maxWidth小于4 ，则抛出IllegalArgumentException 。
     * 在任何情况下都不会返回长度大于maxWidth的String。
     *
     * @param str  要检查的字符串，可以为null
     * @param maxWidth  结果字符串的最大长度，必须至少为4
     * @return 简写字符串， null如果空字符串输入
     * @throws IllegalArgumentException 如果宽度太小
     */
    public static String abbreviate(String str, int maxWidth) {
        return abbreviate(str, "...", 0, maxWidth);
    }

    /**
     * 使用省略号缩写字符串。 这会将“现在是所有好男人的时间”变成“ ...是...的时间”
     * 与abbreviate(String, int)相似，但允许您指定“左边缘”偏移量。 请注意，此左边缘不一定会成为结果中最左边的字符，也不一定是椭圆后面的第一个字符，但是它将出现在结果中的某个位置。
     * 在任何情况下都不会返回长度大于maxWidth的String。
     *
     *
     * @param str  要检查的字符串，可以为null
     * @param offset 源字符串的左边缘
     * @param maxWidth  结果字符串的最大长度，必须至少为4
     * @return 简写字符串， null如果空字符串输入
     * @throws IllegalArgumentException 如果宽度太小
     */
    public static String abbreviate(String str, int offset, int maxWidth) {
        return abbreviate(str, "...", offset, maxWidth);
    }

    /**
     * 使用另一个给定的String作为替换标记来缩写String。 如果将“ ...”定义为替换标记，这会将“现在是所有好男人的时间”变成“现在是...的时间”。
     * 特别：
     * 如果str的字符数小于或等于maxWidth ，则返回str 。
     * 否则将其缩写为(substring(str, 0, max-abbrevMarker.length) + abbrevMarker) 。
     * 如果maxWidth小于abbrevMarker.length + 1 ，则抛出IllegalArgumentException 。
     * 在任何情况下都不会返回长度大于maxWidth的String。
     *
     * @param str 要检查的字符串，可以为null
     * @param abbrevMarker 用作替换标记的字符串
     * @param maxWidth  结果字符串的最大长度，必须至少为abbrevMarker.length + 1
     * @return 简写字符串， null如果空字符串输入
     * @throws IllegalArgumentException 如果宽度太小
     */
    public static String abbreviate(String str, String abbrevMarker, int maxWidth) {
        return abbreviate(str, abbrevMarker, 0, maxWidth);
    }

    /**
     * 使用给定的替换标记缩写String。 如果将“ ...”定义为替换标记，这会将“现在是所有好男人的时间”变成“ ...是...的时间”。
     * 类似于abbreviate(String, String, int) ，但允许您指定“左边缘”偏移量。 请注意，此左边缘不一定会成为结果中最左边的字符，也不会是替换标记之后的第一个字符，但是它将出现在结果中的某个位置。
     * 在任何情况下都不会返回长度大于maxWidth的String。
     *
     * @param str  要检查的字符串，可以为null
     * @param abbrevMarker  用作替换标记的字符串
     * @param offset  源字符串的左边缘
     * @param maxWidth  结果字符串的最大长度，必须至少为4
     * @return 简写字符串， null如果空字符串输入
     * @throws IllegalArgumentException 如果宽度太小
     */
    public static String abbreviate(String str, String abbrevMarker, int offset, int maxWidth) {
        if (isEmpty(str) && isEmpty(abbrevMarker)) {
            return str;
        } else if (isNotEmpty(str) && EMPTY.equals(abbrevMarker) && maxWidth > 0) {
            return str.substring(0, maxWidth);
        } else if (isEmpty(str) || isEmpty(abbrevMarker)) {
            return str;
        }
        int abbrevMarkerLength = abbrevMarker.length();
        int minAbbrevWidth = abbrevMarkerLength + 1;
        int minAbbrevWidthOffset = abbrevMarkerLength + abbrevMarkerLength + 1;

        if (maxWidth < minAbbrevWidth) {
            throw new IllegalArgumentException(String.format("Minimum abbreviation width is %d", minAbbrevWidth));
        }
        if (str.length() <= maxWidth) {
            return str;
        }
        if (offset > str.length()) {
            offset = str.length();
        }
        if (str.length() - offset < maxWidth - abbrevMarkerLength) {
            offset = str.length() - (maxWidth - abbrevMarkerLength);
        }
        if (offset <= abbrevMarkerLength+1) {
            return str.substring(0, maxWidth - abbrevMarkerLength) + abbrevMarker;
        }
        if (maxWidth < minAbbrevWidthOffset) {
            throw new IllegalArgumentException(String.format("Minimum abbreviation width with offset is %d", minAbbrevWidthOffset));
        }
        if (offset + maxWidth - abbrevMarkerLength < str.length()) {
            return abbrevMarker + abbreviate(str.substring(offset), abbrevMarker, maxWidth - abbrevMarkerLength);
        }
        return abbrevMarker + str.substring(str.length() - (maxWidth - abbrevMarkerLength));
    }

    /**
     * 将字符串缩写为所传递的长度，用提供的替换字符串替换中间字符。
     * 仅在满足以下条件时才会出现此缩写：
     * 缩写字符串或替换字符串都不为null或为空
     * 要截断的长度小于提供的String的长度
     * 要截断的长度大于0
     * 缩写的String将有足够的空间容纳所提供的替换字符串的长度以及所提供的String的首个和最后一个字符以供缩写
     * 否则，返回的字符串将与提供的缩写字符串相同。
     *
     * @param str  要缩写的字符串，可以为null
     * @param middle 用于替换中间字符的字符串，可以为null
     * @param length 缩写为str的长度
     * @return   如果满足上述条件，则为缩写字符串，或者提供原始的缩写字符串。
     */
    public static String abbreviateMiddle(String str, String middle, int length) {
        if (isEmpty(str) || isEmpty(middle)) {
            return str;
        }

        if (length >= str.length() || length < middle.length()+2) {
            return str;
        }

        int targetSting = length-middle.length();
        int startOffset = targetSting/2+targetSting%2;
        int endOffset = str.length()-targetSting/2;

        return str.substring(0, startOffset) +
                middle +
                str.substring(endOffset);
    }


    // Compare
    //-----------------------------------------------------------------------
    /**
     * 按照String.compareTo(String)字典上比较两个String.compareTo(String) ，返回：
     * 如果str1等于str2 （或者均为null ），则int = 0
     * 如果str1小于str2 ，则int < 0
     * 如果str1大于str2 ，则int > 0
     * 这是的null安全版本：
     * str1.compareTo(str2)
     * null值被认为小于非null值。 两个null引用被视为相等。
     *
     * @param str1  要比较的字符串
     * @param str2  要比较的字符串
     * @return <0、0，> 0，如果str1分别小于，等于或大于str2
     */
    public static int compare(String str1, String str2) {
        return compare(str1, str2, true);
    }

    /**
     * 按照String.compareTo(String)字典上比较两个String.compareTo(String) ，返回：
     * 如果str1等于str2 （或者均为null ），则int = 0
     * 如果str1小于str2 ，则int < 0
     * 如果str1大于str2 ，则int > 0
     * 这是的null安全版本：
     * str1.compareTo(str2)
     * null输入将根据nullIsLess参数进行处理。 两个null引用被视为相等。
     *
     * @param str1  要比较的字符串
     * @param str2  要比较的字符串
     * @param nullIsLess  是否考虑null值小于非null值
     * @return <0、0，> 0，如果str1分别小于或等于ou大于str2
     */
    public static int compare(String str1, String str2, boolean nullIsLess) {
        if (str1 == str2) {
            return 0;
        }
        if (str1 == null) {
            return nullIsLess ? -1 : 1;
        }
        if (str2 == null) {
            return nullIsLess ? 1 : - 1;
        }
        return str1.compareTo(str2);
    }

    /**
     * 按照String.compareToIgnoreCase(String)字典上比较两个String，忽略大小写差异，返回：
     * 如果str1等于str2 （或者均为null ），则int = 0
     * 如果str1小于str2 ，则int < 0
     * 如果str1大于str2 ，则int > 0
     * 这是的null安全版本：
     * str1.compareToIgnoreCase(str2)
     * null值被认为小于非null值。 两个null引用被视为相等。 比较不区分大小写。
     *
     * @param str1  要比较的字符串
     * @param str2  要比较的字符串
     * @return <0、0，> 0，如果str1分别小于，等于ou或大于str2 ，则忽略大小写差异。
     */
    public static int compareIgnoreCase(String str1, String str2) {
        return compareIgnoreCase(str1, str2, true);
    }

    /**
     * 按照String.compareToIgnoreCase(String)字典上比较两个String，忽略大小写差异，返回：
     * 如果str1等于str2 （或者均为null ），则int = 0
     * 如果str1小于str2 ，则int < 0
     * 如果str1大于str2 ，则int > 0
     * 这是的null安全版本：
     * str1.compareToIgnoreCase(str2)
     * null输入将根据nullIsLess参数进行处理。 两个null引用被视为相等。 比较不区分大小写。
     *
     * @param str1  要比较的字符串
     * @param str2  要比较的字符串
     * @param nullIsLess 是否考虑null值小于非null值
     * @return <0、0，> 0，如果str1分别小于，等于ou或大于str2 ，则忽略大小写差异。
     */
    public static int compareIgnoreCase(String str1, String str2, boolean nullIsLess) {
        if (str1 == str2) {
            return 0;
        }
        if (str1 == null) {
            return nullIsLess ? -1 : 1;
        }
        if (str2 == null) {
            return nullIsLess ? 1 : - 1;
        }
        return str1.compareToIgnoreCase(str2);
    }

    // ContainsNone
    //-----------------------------------------------------------------------
    /**
     * 检查CharSequence是否不包含某些字符。
     * null CharSequence将返回true 。 null无效字符数组将返回true 。 空的CharSequence（length（）= 0）始终返回true。
     *
     * @param cs  要检查的CharSequence，可以为null
     * @param searchChars 无效字符数组，可以为null
     * @return 如果不包含任何无效字符，则为true；否则为null
     */
    public static boolean containsNone(CharSequence cs, char... searchChars) {
        if (cs == null || searchChars == null) {
            return true;
        }
        int csLen = cs.length();
        int csLast = csLen - 1;
        int searchLen = searchChars.length;
        int searchLast = searchLen - 1;
        for (int i = 0; i < csLen; i++) {
            char ch = cs.charAt(i);
            for (int j = 0; j < searchLen; j++) {
                if (searchChars[j] == ch) {
                    if (Character.isHighSurrogate(ch)) {
                        if (j == searchLast) {
                            // missing low surrogate, fine, like String.indexOf(String)
                            return false;
                        }
                        if (i < csLast && searchChars[j + 1] == cs.charAt(i + 1)) {
                            return false;
                        }
                    } else {
                        // ch is in the Basic Multilingual Plane
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * 检查CharSequence是否不包含某些字符。
     * null CharSequence将返回true 。 null无效字符数组将返回true 。 空的字符串（“”）始终返回true。
     *
     * @param cs  要检查的CharSequence，可以为null
     * @param invalidChars  无效字符的字符串，可以为null
     * @return 如果不包含任何无效字符，则为true；否则为null
     */
    public static boolean containsNone(CharSequence cs, String invalidChars) {
        if (cs == null || invalidChars == null) {
            return true;
        }
        return containsNone(cs, invalidChars.toCharArray());
    }

    // ContainsOnly
    //-----------------------------------------------------------------------

    private static void convertRemainingAccentCharacters(StringBuilder decomposed) {
        for (int i = 0; i < decomposed.length(); i++) {
            if (decomposed.charAt(i) == '\u0141') {
                decomposed.deleteCharAt(i);
                decomposed.insert(i, 'L');
            } else if (decomposed.charAt(i) == '\u0142') {
                decomposed.deleteCharAt(i);
                decomposed.insert(i, 'l');
            }
        }
    }

    /**
     * 计算char在给定字符串中出现多少次。
     * null或空（“”）字符串输入返回0 。
     *
     * @param str 要检查的CharSequence，可以为null
     * @param ch  要计数的字符
     * @return  出现的次数，如果CharSequence为null ，则为0
     */
    public static int countMatches(CharSequence str, char ch) {
        if (isEmpty(str)) {
            return 0;
        }
        int count = 0;
        // We could also call str.toCharArray() for faster look ups but that would generate more garbage.
        for (int i = 0; i < str.length(); i++) {
            if (ch == str.charAt(i)) {
                count++;
            }
        }
        return count;
    }

    // Count matches
    /**
     * 返回传入的CharSequence，或者如果CharSequence为空或null ，则返回defaultStr的值。
     *
     * @param <T>  CharSequence的特定种类
     * @param str  要检查的CharSequence，可以为null
     * @param defaultStr  如果输入为空（“”）或null ，则返回的默认CharSequence，可以为null
     * @return 传入CharSequence或默认值
     */
    public static <T extends CharSequence> T defaultIfEmpty(T str, T defaultStr) {
        return isEmpty(str) ? defaultStr : str;
    }

    /**
     * 返回传入的String，或者如果String为null ，则返回一个空String（“”）。
     *
     * @param str  要检查的字符串，可以为null
     * @return 传入的字符串，如果为null ，则返回空字符串
     */
    public static String defaultString(String str) {
        return defaultString(str, EMPTY);
    }

    /**
     * 返回传入的String，或者如果String为null ，则返回defaultStr的值。
     *
     * @param str  要检查的字符串，可以为null
     * @param defaultStr  如果输入为null ，则返回的默认String，可以为null
     * @return 传入的String，如果为null则为默认null
     */
    public static String defaultString(String str, String defaultStr) {
        return str == null ? defaultStr : str;
    }

    // Delete
    //-----------------------------------------------------------------------
    /**
     * 从Character.isWhitespace(char)定义的String中删除所有空格。
     *
     * @param str  要从中删除空格的字符串，可以为null
     * @return 没有空格的字符串， null如果空字符串输入
     */
    public static String deleteWhitespace(String str) {
        if (isEmpty(str)) {
            return str;
        }
        int sz = str.length();
        char[] chs = new char[sz];
        int count = 0;
        for (int i = 0; i < sz; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                chs[count++] = str.charAt(i);
            }
        }
        if (count == sz) {
            return str;
        }
        return new String(chs, 0, count);
    }

    // Difference
    //-----------------------------------------------------------------------
    /**
     * 比较两个字符串，并返回它们不同的部分。 更准确地说，从第二个String与第一个String不同的地方开始，返回其余的String。 这意味着“ abc”和“ ab”之间的区别是空字符串而不是“ c”。
     * 例如， difference("i am a machine", "i am a robot") -> "robot" 。
     *
     * @param str1  第一个String，可以为null
     * @param str2  第二个String，可以为null
     * @return str2与str1不同的部分； 返回
     */
    public static String difference(String str1, String str2) {
        if (str1 == null) {
            return str2;
        }
        if (str2 == null) {
            return str1;
        }
        int at = indexOfDifference(str1, str2);
        if (at == INDEX_NOT_FOUND) {
            return EMPTY;
        }
        return str2.substring(at);
    }

    // Equals
    //-----------------------------------------------------------------------
    /**
     * 比较两个CharSequences，如果它们表示相等的字符序列，则返回true 。
     * null的处理没有例外。 两个null引用被认为是相等的。 比较是区分大小写的。
     *
     * @param cs1 第一个CharSequence，可以为null
     * @param cs2 第二个CharSequence，可以为null
     * @return 如果CharSequences相等（区分大小写），则为true ，或者两者都为null
     */
    public static boolean equals(CharSequence cs1, CharSequence cs2) {
        if (cs1 == cs2) {
            return true;
        }
        if (cs1 == null || cs2 == null) {
            return false;
        }
        if (cs1.length() != cs2.length()) {
            return false;
        }
        if (cs1 instanceof String && cs2 instanceof String) {
            return cs1.equals(cs2);
        }
        // Step-wise comparison
        int length = cs1.length();
        for (int i = 0; i < length; i++) {
            if (cs1.charAt(i) != cs2.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 返回数组中不为空的第一个值。
     * 如果所有值都为空，或者数组为null或空，则返回null 。
     *
     * @param <T> CharSequence的特定种类
     * @param values  要测试的值，可以为null或为空
     * @return 从所述第一值values ，其不为空，或者null ，如果没有非空值
     */
    @SafeVarargs
    public static <T extends CharSequence> T firstNonEmpty(T... values) {
        if (values != null) {
            for (T val : values) {
                if (isNotEmpty(val)) {
                    return val;
                }
            }
        }
        return null;
    }

    /**
     * 检查String str包含Unicode数字，如果是，则连接str所有数字并将其作为String返回。
     * 如果在str找不到数字，则将返回一个空（“”）字符串。
     *
     * @param str 要从中提取数字的字符串，可以为null
     * @return 仅包含数字的字符串，如果没有数字，则为空（“”）字符串，或者为null如果str为null
     */
    public static String getDigits(String str) {
        if (isEmpty(str)) {
            return str;
        }
        int sz = str.length();
        StringBuilder strDigits = new StringBuilder(sz);
        for (int i = 0; i < sz; i++) {
            char tempChar = str.charAt(i);
            if (Character.isDigit(tempChar)) {
                strDigits.append(tempChar);
            }
        }
        return strDigits.toString();
    }

    /**
     * 比较两个CharSequences，并返回CharSequences开始不同的索引。
     * 例如， indexOfDifference("i am a machine", "i am a robot") -> 7
     *
     * @param cs1  第一个CharSequence，可以为null
     * @param cs2  第二个CharSequence，可以为null
     * @return cs1和cs2开始不同的索引； 如果它们相等，则为-1
     */
    public static int indexOfDifference(CharSequence cs1, CharSequence cs2) {
        if (cs1 == cs2) {
            return INDEX_NOT_FOUND;
        }
        if (cs1 == null || cs2 == null) {
            return 0;
        }
        int i;
        for (i = 0; i < cs1.length() && i < cs2.length(); ++i) {
            if (cs1.charAt(i) != cs2.charAt(i)) {
                break;
            }
        }
        if (i < cs2.length() || i < cs1.length()) {
            return i;
        }
        return INDEX_NOT_FOUND;
    }


    /**
     * 检查CharSequence是否仅包含小写字符。
     * null将返回false 。 空的CharSequence（length（）= 0）将返回false 。
     *
     * @param cs  要检查的CharSequence，可以为null
     * @return {如果仅包含小写字符并且为非null，则为true
     */
    public static boolean isAllLowerCase(CharSequence cs) {
        if (isEmpty(cs)) {
            return false;
        }
        int sz = cs.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isLowerCase(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检查CharSequence是否仅包含大写字符。
     * null将返回false 。 空的String（length（）= 0）将返回false 。
     *
     * @param cs 要检查的CharSequence，可以为null
     * @return 如果仅包含大写字符且为非null，则为true
     */
    public static boolean isAllUpperCase(CharSequence cs) {
        if (isEmpty(cs)) {
            return false;
        }
        int sz = cs.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isUpperCase(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    // Character Tests
    //-----------------------------------------------------------------------
    /**
     * 检查CharSequence是否仅包含Unicode字母。
     * null将返回false 。 空的CharSequence（length（）= 0）将返回false 。
     *
     * @param cs  要检查的CharSequence，可以为null
     * @return 如果仅包含字母并且为非null，则为true
     */
    public static boolean isAlpha(CharSequence cs) {
        if (isEmpty(cs)) {
            return false;
        }
        int sz = cs.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isLetter(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检查CharSequence是否仅包含Unicode字母或数字。
     * null将返回false 。 空的CharSequence（length（）= 0）将返回false 。
     *
     * @param cs  要检查的CharSequence，可以为null
     * @return 如果仅包含字母或数字，并且为非null，则为true
     */
    public static boolean isAlphanumeric(CharSequence cs) {
        if (isEmpty(cs)) {
            return false;
        }
        int sz = cs.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isLetterOrDigit(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检查CharSequence是否仅包含Unicode字母，数字或空格（ ' ' ）。
     * null将返回false 。 空的CharSequence（length（）= 0）将返回true 。
     *
     * @param cs  要检查的CharSequence，可以为null
     * @return 如果仅包含字母，数字或空格，并且为非null，则为true
     */
    public static boolean isAlphanumericSpace(CharSequence cs) {
        if (cs == null) {
            return false;
        }
        int sz = cs.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isLetterOrDigit(cs.charAt(i)) && cs.charAt(i) != ' ') {
                return false;
            }
        }
        return true;
    }

    /**
     * 检查CharSequence是否仅包含Unicode字母和空格（''）。
     * null将返回false 。空CharSequence（length（）= 0）将返回true 。
     *
     * @param cs  要检查的CharSequence，可以为null
     * @return 如果仅包含字母和空格，并且为非null，则为true
     */
    public static boolean isAlphaSpace(CharSequence cs) {
        if (cs == null) {
            return false;
        }
        int sz = cs.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isLetter(cs.charAt(i)) && cs.charAt(i) != ' ') {
                return false;
            }
        }
        return true;
    }

    /**
     * 检查CharSequence是否包含大小写混合的大小写。
     * null将返回false 。 空的CharSequence（ length()=0 ）将返回false 。
     *
     * @param cs 要检查的CharSequence，可以为null
     * @return 如果CharSequence同时包含大写和小写字符，则为true
     */
    public static boolean isMixedCase(CharSequence cs) {
        if (isEmpty(cs) || cs.length() == 1) {
            return false;
        }
        boolean containsUppercase = false;
        boolean containsLowercase = false;
        int sz = cs.length();
        for (int i = 0; i < sz; i++) {
            if (containsUppercase && containsLowercase) {
                return true;
            } else if (Character.isUpperCase(cs.charAt(i))) {
                containsUppercase = true;
            } else if (Character.isLowerCase(cs.charAt(i))) {
                containsLowercase = true;
            }
        }
        return containsUppercase && containsLowercase;
    }

    /**
     *检查CharSequence是否不为空（“”）并且不为null。
     *
     * @param cs 要检查的CharSequence，可以为null
     * @return 如果CharSequence不为空且不为null，则返回true
     */
    public static boolean isNotEmpty(CharSequence cs) {
        return !isEmpty(cs);
    }

    /**
     * 检查CharSequence是否仅包含Unicode数字。 小数点不是Unicode数字，并且返回false。
     * null将返回false 。 空的CharSequence（length（）= 0）将返回false 。
     * 请注意，该方法不允许使用正号或负号的前导符号。 另外，如果字符串通过数字测试，则在由Integer.parseInt或Long.parseLong解析时，例如，如果值分别超出int或long的范围，则它仍可能生成NumberFormatException。
     *
     * @param cs 要检查的CharSequence，可以为null
     * @return 如果仅包含数字并且为非null，则为true
     */
    public static boolean isNumeric(CharSequence cs) {
        if (isEmpty(cs)) {
            return false;
        }
        int sz = cs.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isDigit(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检查CharSequence是否仅包含Unicode数字或空格（ ' ' ）。 小数点不是Unicode数字，并且返回false。
     * null将返回false 。 空的CharSequence（length（）= 0）将返回true 。
     *
     * @param cs  要检查的CharSequence，可以为null
     * @return 如果仅包含数字或空格并且为非null，则为true
     */
    public static boolean isNumericSpace(CharSequence cs) {
        if (cs == null) {
            return false;
        }
        int sz = cs.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isDigit(cs.charAt(i)) && cs.charAt(i) != ' ') {
                return false;
            }
        }
        return true;
    }

    /**
     * 检查CharSequence是否仅包含空格。
     * 空格由Character.isWhitespace(char)定义。
     * null将返回false 。 空的CharSequence（length（）= 0）将返回true 。
     *
     * @param cs  要检查的CharSequence，可以为null
     * @return 如果仅包含空格并且为非null，则为true
     */
    public static boolean isWhitespace(CharSequence cs) {
        if (cs == null) {
            return false;
        }
        int sz = cs.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 将提供的数组的元素连接到包含提供的元素列表的单个String中。
     * 在列表之前或之后没有添加定界符。 空对象或数组中的空字符串由空字符串表示。
     *
     * @param array 要连接在一起的值的数组，可以为null
     * @param separator 要使用的分隔符
     * @return 接合的字符串， null如果空数组输入
     */
    public static String join(byte[] array, char separator) {
        if (array == null) {
            return null;
        }
        return join(array, separator, 0, array.length);
    }

    /**
     * 将提供的数组的元素连接到包含提供的元素列表的单个String中。
     * 在列表之前或之后没有添加定界符。 空对象或数组中的空字符串由空字符串表示。
     *
     * @param array 要连接在一起的值的数组，可以为null
     * @param separator 要使用的分隔符
     * @return 接合的字符串， null如果空数组输入
     */
    public static String join(char[] array, char separator) {
        if (array == null) {
            return null;
        }
        return join(array, separator, 0, array.length);
    }

    /**
     * 将提供的数组的元素连接到包含提供的元素列表的单个String中。
     * 在列表之前或之后没有添加定界符。 空对象或数组中的空字符串由空字符串表示。
     *
     * @param array 要连接在一起的值的数组，可以为null
     * @param separator 要使用的分隔符
     * @param startIndex 第一个开始加入的索引。 将起始索引传递到数组的末尾是错误的
     * @param endIndex 停止从其开始的索引（不包括）。 将结束索引传递到数组的末尾是错误的
     * @return 接合的字符串， null如果空数组输入
     */
    public static String join(char[] array, char separator, int startIndex, int endIndex) {
        if (array == null) {
            return null;
        }
        int noOfItems = endIndex - startIndex;
        if (noOfItems <= 0) {
            return EMPTY;
        }
        StringBuilder buf = new StringBuilder(noOfItems);
        buf.append(array[startIndex]);
        for (int i = startIndex + 1; i < endIndex; i++) {
            buf.append(separator);
            buf.append(array[i]);
        }
        return buf.toString();
    }

    /**
     * 将提供的数组的元素连接到包含提供的元素列表的单个String中。
     * 在列表之前或之后没有添加定界符。 空对象或数组中的空字符串由空字符串表示。
     *
     * @param array 要连接在一起的值的数组，可以为null
     * @param separator 要使用的分隔符
     * @return 接合的字符串， null如果空数组输入
     */
    public static String join(double[] array, char separator) {
        if (array == null) {
            return null;
        }
        return join(array, separator, 0, array.length);
    }

    /**
     * 将提供的数组的元素连接到包含提供的元素列表的单个String中。
     * 在列表之前或之后没有添加定界符。 空对象或数组中的空字符串由空字符串表示。
     *
     * @param array 要连接在一起的值的数组，可以为null
     * @param separator 要使用的分隔符
     * @param startIndex 第一个开始加入的索引。 将起始索引传递到数组的末尾是错误的
     * @param endIndex 停止从其开始的索引（不包括）。 将结束索引传递到数组的末尾是错误的
     * @return 接合的字符串， null如果空数组输入
     */
    public static String join(double[] array, char separator, int startIndex, int endIndex) {
        if (array == null) {
            return null;
        }
        int noOfItems = endIndex - startIndex;
        if (noOfItems <= 0) {
            return EMPTY;
        }
        StringBuilder buf = new StringBuilder(noOfItems);
        buf.append(array[startIndex]);
        for (int i = startIndex + 1; i < endIndex; i++) {
            buf.append(separator);
            buf.append(array[i]);
        }
        return buf.toString();
    }

    /**
     * 将提供的数组的元素连接到包含提供的元素列表的单个String中。
     * 在列表之前或之后没有添加定界符。 空对象或数组中的空字符串由空字符串表示。
     *
     * @param array 要连接在一起的值的数组，可以为null
     * @param separator 要使用的分隔符
     * @return 接合的字符串， null如果空数组输入
     */
    public static String join(float[] array, char separator) {
        if (array == null) {
            return null;
        }
        return join(array, separator, 0, array.length);
    }

    /**
     * 将提供的数组的元素连接到包含提供的元素列表的单个String中。
     * 在列表之前或之后没有添加定界符。 空对象或数组中的空字符串由空字符串表示。
     *
     * @param array 要连接在一起的值的数组，可以为null
     * @param separator 要使用的分隔符
     * @param startIndex 第一个开始加入的索引。 将起始索引传递到数组的末尾是错误的
     * @param endIndex 停止从其开始的索引（不包括）。 将结束索引传递到数组的末尾是错误的
     * @return 接合的字符串， null如果空数组输入
     */
    public static String join(float[] array, char separator, int startIndex, int endIndex) {
        if (array == null) {
            return null;
        }
        int noOfItems = endIndex - startIndex;
        if (noOfItems <= 0) {
            return EMPTY;
        }
        StringBuilder buf = new StringBuilder(noOfItems);
        buf.append(array[startIndex]);
        for (int i = startIndex + 1; i < endIndex; i++) {
            buf.append(separator);
            buf.append(array[i]);
        }
        return buf.toString();
    }

    /**
     * 将提供的数组的元素连接到包含提供的元素列表的单个String中。
     * 在列表之前或之后没有添加定界符。 空对象或数组中的空字符串由空字符串表示。
     *
     * @param array 要连接在一起的值的数组，可以为null
     * @param separator 要使用的分隔符
     * @return 接合的字符串， null如果空数组输入
     */
    public static String join(int[] array, char separator) {
        if (array == null) {
            return null;
        }
        return join(array, separator, 0, array.length);
    }

    /**
     * 将提供的数组的元素连接到包含提供的元素列表的单个String中。
     * 在列表之前或之后没有添加定界符。 空对象或数组中的空字符串由空字符串表示。
     *
     * @param array 要连接在一起的值的数组，可以为null
     * @param separator 要使用的分隔符
     * @param startIndex 第一个开始加入的索引。 将起始索引传递到数组的末尾是错误的
     * @param endIndex 停止从其开始的索引（不包括）。 将结束索引传递到数组的末尾是错误的
     * @return 接合的字符串， null如果空数组输入
     */
    public static String join(int[] array, char separator, int startIndex, int endIndex) {
        if (array == null) {
            return null;
        }
        int noOfItems = endIndex - startIndex;
        if (noOfItems <= 0) {
            return EMPTY;
        }
        StringBuilder buf = new StringBuilder(noOfItems);
        buf.append(array[startIndex]);
        for (int i = startIndex + 1; i < endIndex; i++) {
            buf.append(separator);
            buf.append(array[i]);
        }
        return buf.toString();
    }

    /**
     * 将提供的Iterable的元素连接到包含提供的元素的单个String中。
     * 在列表之前或之后没有添加定界符。 空对象或迭代中的空字符串由空字符串表示。
     * 请参见此处的示例： join(Object[], char) 。
     *
     * @param iterable  提供值连接在一起的Iterable ，可以为null
     * @param separator  要使用的分隔符
     * @return 在加入字符串， null如果空迭代器输入
     */
    public static String join(Iterable<?> iterable, char separator) {
        if (iterable == null) {
            return null;
        }
        return join(iterable.iterator(), separator);
    }

    /**
     * 将提供的Iterable的元素连接到包含提供的元素的单个String中。
     * 在列表之前或之后没有添加定界符。 null分隔符与空字符串（“”）相同。
     * 请参见此处的示例： join(Object[], String) 。
     *
     * @param iterable  提供值连接在一起的Iterable ，可以为null
     * @param separator  要使用的分隔符，将null视为“”
     * @return 在加入字符串， null如果空迭代器输入
     */
    public static String join(Iterable<?> iterable, String separator) {
        if (iterable == null) {
            return null;
        }
        return join(iterable.iterator(), separator);
    }

    /**
     * 将提供的Iterator的元素连接到包含提供的元素的单个String中。
     * 在列表之前或之后没有添加定界符。 空对象或迭代中的空字符串由空字符串表示。
     * 请参见此处的示例： join(Object[], char) 。
     *
     * @param iterator  要连接在一起的值的Iterator ，可以为null
     * @param separator 要使用的分隔符
     * @return 在加入字符串， null如果空迭代器输入
     */
    public static String join(Iterator<?> iterator, char separator) {
        // handle null, zero and one elements before building a buffer
        if (iterator == null) {
            return null;
        }
        if (!iterator.hasNext()) {
            return EMPTY;
        }
        Object first = iterator.next();
        if (!iterator.hasNext()) {
            return Objects.toString(first, EMPTY);
        }
        // two or more elements
        StringBuilder buf = new StringBuilder(STRING_BUILDER_SIZE); // Java default is 16, probably too small
        if (first != null) {
            buf.append(first);
        }

        while (iterator.hasNext()) {
            buf.append(separator);
            Object obj = iterator.next();
            if (obj != null) {
                buf.append(obj);
            }
        }
        return buf.toString();
    }

    /**
     * 将提供的Iterator的元素连接到包含提供的元素的单个String中。
     * 在列表之前或之后没有添加定界符。 null分隔符与空字符串（“”）相同。
     * 请参见此处的示例： join(Object[], String) 。
     *
     * @param iterator  要连接在一起的值的Iterator ，可以为null
     * @param separator  要使用的分隔符，将null视为“”
     * @return 在加入字符串， null如果空迭代器输入
     */
    public static String join(Iterator<?> iterator, String separator) {
        // handle null, zero and one elements before building a buffer
        if (iterator == null) {
            return null;
        }
        if (!iterator.hasNext()) {
            return EMPTY;
        }
        Object first = iterator.next();
        if (!iterator.hasNext()) {
            return Objects.toString(first, "");
        }
        // two or more elements
        StringBuilder buf = new StringBuilder(STRING_BUILDER_SIZE); // Java default is 16, probably too small
        if (first != null) {
            buf.append(first);
        }

        while (iterator.hasNext()) {
            if (separator != null) {
                buf.append(separator);
            }
            Object obj = iterator.next();
            if (obj != null) {
                buf.append(obj);
            }
        }
        return buf.toString();
    }

    /**
     * 将提供的List的元素连接到包含提供的元素列表的单个String中。
     * 在列表之前或之后没有添加定界符。 空对象或数组中的空字符串由空字符串表示。
     *
     * @param list 要连接在一起的值List ，可以为null
     * @param separator  要使用的分隔符
     * @param startIndex 第一个开始加入的索引。 将起始索引传递到列表末尾是错误的
     * @param endIndex 停止从其开始的索引（不包括）。 将结束索引传递到列表末尾是错误的
     * @return 在加入字符串， null如果空列表输入
     */
    public static String join(List<?> list, char separator, int startIndex, int endIndex) {
        if (list == null) {
            return null;
        }
        int noOfItems = endIndex - startIndex;
        if (noOfItems <= 0) {
            return EMPTY;
        }
        List<?> subList = list.subList(startIndex, endIndex);
        return join(subList.iterator(), separator);
    }

    /**
     * 将提供的List的元素连接到包含提供的元素列表的单个String中。
     * 在列表之前或之后没有添加定界符。 空对象或数组中的空字符串由空字符串表示。
     *
     * @param list  要连接在一起的值List ，可以为null
     * @param separator  要使用的分隔符
     * @param startIndex 第一个开始加入的索引。 将起始索引传递到列表末尾是错误的
     * @param endIndex  停止从其开始的索引（不包括）。 将结束索引传递到列表末尾是错误的
     * @return 在加入字符串， null如果空列表输入
     */
    public static String join(List<?> list, String separator, int startIndex, int endIndex) {
        if (list == null) {
            return null;
        }
        int noOfItems = endIndex - startIndex;
        if (noOfItems <= 0) {
            return EMPTY;
        }
        List<?> subList = list.subList(startIndex, endIndex);
        return join(subList.iterator(), separator);
    }

    /**
     * 将提供的数组的元素连接到包含提供的元素列表的单个String中。
     * 在列表之前或之后没有添加定界符。 空对象或数组中的空字符串由空字符串表示。
     *
     * @param array 要连接在一起的值的数组，可以为null
     * @param separator 要使用的分隔符
     * @return 接合的字符串， null如果空数组输入
     */
    public static String join(long[] array, char separator) {
        if (array == null) {
            return null;
        }
        return join(array, separator, 0, array.length);
    }

    /**
     * 将提供的数组的元素连接到包含提供的元素列表的单个String中。
     * 在列表之前或之后没有添加定界符。 空对象或数组中的空字符串由空字符串表示。
     *
     * @param array 要连接在一起的值的数组，可以为null
     * @param separator  要使用的分隔符
     * @param startIndex 第一个开始加入的索引。 将起始索引传递到列表末尾是错误的
     * @param endIndex  停止从其开始的索引（不包括）。 将结束索引传递到列表末尾是错误的
     * @return 接合的字符串， null如果空数组输入
     */
    public static String join(long[] array, char separator, int startIndex, int endIndex) {
        if (array == null) {
            return null;
        }
        int noOfItems = endIndex - startIndex;
        if (noOfItems <= 0) {
            return EMPTY;
        }
        StringBuilder buf = new StringBuilder(noOfItems);
        buf.append(array[startIndex]);
        for (int i = startIndex + 1; i < endIndex; i++) {
            buf.append(separator);
            buf.append(array[i]);
        }
        return buf.toString();
    }

    /**
     * 将提供的数组的元素连接到包含提供的元素列表的单个String中。
     * 在列表之前或之后没有添加定界符。 空对象或数组中的空字符串由空字符串表示。
     *
     * @param array 要连接在一起的值的数组，可以为null
     * @param separator  要使用的分隔符
     * @return 接合的字符串， null如果空数组输入
     */
    public static String join(Object[] array, char separator) {
        if (array == null) {
            return null;
        }
        return join(array, separator, 0, array.length);
    }

    /**
     * 将提供的数组的元素连接到包含提供的元素列表的单个String中。
     * 在列表之前或之后没有添加定界符。 空对象或数组中的空字符串由空字符串表示。
     *
     * @param array  要连接在一起的值的数组，可以为null
     * @param separator  要使用的分隔符
     * @param startIndex 第一个开始加入的索引。 将起始索引传递到数组的末尾是错误的
     * @param endIndex 停止从其开始的索引（不包括）。 将结束索引传递到数组的末尾是错误的
     * @return 接合的字符串， null如果空数组输入
     */
    public static String join(Object[] array, char separator, int startIndex, int endIndex) {
        if (array == null) {
            return null;
        }
        int noOfItems = endIndex - startIndex;
        if (noOfItems <= 0) {
            return EMPTY;
        }
        StringBuilder buf = new StringBuilder(noOfItems);
        if (array[startIndex] != null) {
            buf.append(array[startIndex]);
        }
        for (int i = startIndex + 1; i < endIndex; i++) {
            buf.append(separator);
            if (array[i] != null) {
                buf.append(array[i]);
            }
        }
        return buf.toString();
    }

    /**
     * 将提供的数组的元素连接到包含提供的元素列表的单个String中。
     * 在列表之前或之后没有添加定界符。 null分隔符与空字符串（“”）相同。 空对象或数组中的空字符串由空字符串表示。
     *
     * @param array  要连接在一起的值的数组，可以为null
     * @param separator  要使用的分隔符，将null视为“”
     * @return 接合的字符串， null如果空数组输入
     */
    public static String join(Object[] array, String separator) {
        if (array == null) {
            return null;
        }
        return join(array, separator, 0, array.length);
    }

    /**
     * 将提供的数组的元素连接到包含提供的元素列表的单个String中。
     * 在列表之前或之后没有添加定界符。 null分隔符与空字符串（“”）相同。 空对象或数组中的空字符串由空字符串表示。
     *
     * @param array  the array of values to join together, may be null
     * @param separator  the separator character to use, null treated as ""
     * @param startIndex the first index to start joining from.
     * @param endIndex the index to stop joining from (exclusive).
     * @return the joined String, {@code null} if null array input; or the empty string
     */
    public static String join(Object[] array, String separator, int startIndex, int endIndex) {
        if (array == null) {
            return null;
        }
        if (separator == null) {
            separator = EMPTY;
        }
        // endIndex - startIndex > 0:   Len = NofStrings *(length(firstString) + length(separator))
        //           (Assuming that all Strings are roughly equally long)
        int noOfItems = endIndex - startIndex;
        if (noOfItems <= 0) {
            return EMPTY;
        }
        StringBuilder buf = new StringBuilder(noOfItems);

        if (array[startIndex] != null) {
            buf.append(array[startIndex]);
        }
        for (int i = startIndex + 1; i < endIndex; i++) {
            buf.append(separator);

            if (array[i] != null) {
                buf.append(array[i]);
            }
        }
        return buf.toString();
    }

    /**
     * 将提供的数组的元素连接到包含提供的元素列表的单个String中。
     * 在列表之前或之后没有添加定界符。 空对象或数组中的空字符串由空字符串表示。
     *
     * @param array 要连接在一起的值的数组，可以为null
     * @param separator 要使用的分隔符
     * @return 接合的字符串， null如果空数组输入
     */
    public static String join(short[] array, char separator) {
        if (array == null) {
            return null;
        }
        return join(array, separator, 0, array.length);
    }

    /**
     * 将提供的数组的元素连接到包含提供的元素列表的单个String中。
     * 在列表之前或之后没有添加定界符。 空对象或数组中的空字符串由空字符串表示。
     *
     * @param array 要连接在一起的值的数组，可以为null
     * @param separator 要使用的分隔符
     * @param startIndex 第一个开始加入的索引。 将起始索引传递到数组的末尾是错误的
     * @param endIndex 停止从其开始的索引（不包括）。 将结束索引传递到数组的末尾是错误的
     * @return 接合的字符串， null如果空数组输入
     */
    public static String join(short[] array, char separator, int startIndex, int endIndex) {
        if (array == null) {
            return null;
        }
        int noOfItems = endIndex - startIndex;
        if (noOfItems <= 0) {
            return EMPTY;
        }
        StringBuilder buf = new StringBuilder(noOfItems);
        buf.append(array[startIndex]);
        for (int i = startIndex + 1; i < endIndex; i++) {
            buf.append(separator);
            buf.append(array[i]);
        }
        return buf.toString();
    }


    // Joining
    //-----------------------------------------------------------------------
    /**
     * 将提供的数组的元素连接到包含提供的元素列表的单个String中。
     * 没有分隔符添加到连接的字符串。 空对象或数组中的空字符串由空字符串表示
     *
     * @param <T> 连接在一起的特定值类型
     * @param elements  要连接在一起的值，可以为null
     * @return 接合的字符串， null如果空数组输入
     */
    @SafeVarargs
    public static <T> String join(T... elements) {
        return join(elements, null);
    }

    /**
     * 将提供的varargs的元素连接到包含提供的元素的单个String中。
     * 在列表之前或之后没有添加定界符。 null元素和分隔符被视为空字符串（“”）。
     * @param separator 要使用的分隔符，将null视为“”
     * @param objects 提供连接值的变量。 null元素被视为“”
     * @return 联接的字符串
     * @throws java.lang.IllegalArgumentException 抛出异常
     */
    public static String joinWith(String separator, Object... objects) {
        if (objects == null) {
            throw new IllegalArgumentException("Object varargs must not be null");
        }

        String sanitizedSeparator = defaultString(separator);

        StringBuilder result = new StringBuilder();

        Iterator<Object> iterator = Arrays.asList(objects).iterator();
        while (iterator.hasNext()) {
            String value = Objects.toString(iterator.next(), "");
            result.append(value);

            if (iterator.hasNext()) {
                result.append(sanitizedSeparator);
            }
        }

        return result.toString();
    }

    private static int[] matches(CharSequence first, CharSequence second) {
        CharSequence max, min;
        if (first.length() > second.length()) {
            max = first;
            min = second;
        } else {
            max = second;
            min = first;
        }
        int range = Math.max(max.length() / 2 - 1, 0);
        int[] matchIndexes = new int[min.length()];
        Arrays.fill(matchIndexes, -1);
        boolean[] matchFlags = new boolean[max.length()];
        int matches = 0;
        for (int mi = 0; mi < min.length(); mi++) {
            char c1 = min.charAt(mi);
            for (int xi = Math.max(mi - range, 0), xn = Math.min(mi + range + 1, max.length()); xi < xn; xi++) {
                if (!matchFlags[xi] && c1 == max.charAt(xi)) {
                    matchIndexes[mi] = xi;
                    matchFlags[xi] = true;
                    matches++;
                    break;
                }
            }
        }
        char[] ms1 = new char[matches];
        char[] ms2 = new char[matches];
        for (int i = 0, si = 0; i < min.length(); i++) {
            if (matchIndexes[i] != -1) {
                ms1[si] = min.charAt(i);
                si++;
            }
        }
        for (int i = 0, si = 0; i < max.length(); i++) {
            if (matchFlags[i]) {
                ms2[si] = max.charAt(i);
                si++;
            }
        }
        int transpositions = 0;
        for (int mi = 0; mi < ms1.length; mi++) {
            if (ms1[mi] != ms2[mi]) {
                transpositions++;
            }
        }
        int prefix = 0;
        for (int mi = 0; mi < min.length(); mi++) {
            if (first.charAt(mi) == second.charAt(mi)) {
                prefix++;
            } else {
                break;
            }
        }
        return new int[] { matches, transpositions / 2, prefix, max.length() };
    }


    // Overlay
    //-----------------------------------------------------------------------
    /**
     * 用另一个字符串覆盖一个字符串的一部分。
     * null字符串输入返回null 。 负索引被视为零。 大于字符串长度的索引将被视为字符串长度。 起始索引始终是两个索引中较小的一个。
     *
     * @param str  要覆盖的String，可以为null
     * @param overlay  要覆盖的字符串，可以为null
     * @param start  开始重叠的位置
     * @param end  停止重叠的位置
     * @return 重叠字符串， null如果空字符串输入
     */
    public static String overlay(String str, String overlay, int start, int end) {
        if (str == null) {
            return null;
        }
        if (overlay == null) {
            overlay = EMPTY;
        }
        int len = str.length();
        if (start < 0) {
            start = 0;
        }
        if (start > len) {
            start = len;
        }
        if (end < 0) {
            end = 0;
        }
        if (end > len) {
            end = len;
        }
        if (start > end) {
            int temp = start;
            start = end;
            end = temp;
        }
        return str.substring(0, start) +
                overlay +
                str.substring(end);
    }

    /**
     * 交换字符串的大小写，将大写和标题大小写更改为小写，将小写更改为大写。
     * 大写字符转换为小写
     * 标题字符转换为小写
     * 小写字符转换为大写
     * 对于基于单词的算法， null输入String返回null 。
     *
     * @param str  要交换大小写的字符串，可以为null
     * @return 改变后的字符串， null如果空字符串输入
     */
    public static String swapCase(String str) {
        if (isEmpty(str)) {
            return str;
        }

        int strLen = str.length();
        int[] newCodePoints = new int[strLen]; // cannot be longer than the char array
        int outOffset = 0;
        for (int i = 0; i < strLen; ) {
            int oldCodepoint = str.codePointAt(i);
            int newCodePoint;
            if (Character.isUpperCase(oldCodepoint)) {
                newCodePoint = Character.toLowerCase(oldCodepoint);
            } else if (Character.isTitleCase(oldCodepoint)) {
                newCodePoint = Character.toLowerCase(oldCodepoint);
            } else if (Character.isLowerCase(oldCodepoint)) {
                newCodePoint = Character.toUpperCase(oldCodepoint);
            } else {
                newCodePoint = oldCodepoint;
            }
            newCodePoints[outOffset++] = newCodePoint;
            i += Character.charCount(newCodePoint);
        }
        return new String(newCodePoints, 0, outOffset);
    }

    /* Part of spring */
    //---------------------------------------------------------------------
    // 处理字符串的一般方便方法
    //---------------------------------------------------------------------

    /**
     * 检查给定的对象（可能是String ）是否为空。 这实际上是!hasLength(String)的快捷方式。
     * 此方法接受任何Object作为参数，将其与null和空String进行比较。 因此，对于非null非String对象，此方法将永远不会返回true 。
     * 对象签名对于通常处理字符串但通常必须遍历对象的常规属性处理代码很有用，因为属性也可以是例如原始值对象。
     * 注意：如果对象预先输入为String ，则最好使用hasLength(String)或hasText(String) 。

     * @param str 候选对象（可能是String ）
     * @see #hasLength(String)
     * @see #hasText(String)
     */
    public static boolean isEmpty(Object str) {
        return (str == null || "".equals(str));
    }

    /**
     * 检查给定的CharSequence既不为null也不为长度0。
     * 注意：对于仅由空格组成的CharSequence ，此方法返回true 。
     *
     * @param str 要检查的CharSequence （可以为null ）
     * @return 如果CharSequence不为null并且具有长度，则为true
     * @see #hasLength(String)
     * @see #hasText(CharSequence)
     */
    public static boolean hasLength(CharSequence str) {
        return (str != null && str.length() > 0);
    }

    /**
     * 检查给定的String既不为null也不为0。
     * 注意：对于仅由空格组成的String ，此方法返回true 。
     *
     * @param str 要检查的String （可以为null ）
     * @return 如果String不为null并且具有长度，则为true
     * @see #hasLength(CharSequence)
     * @see #hasText(String)
     */
    public static boolean hasLength(String str) {
        return (str != null && !str.isEmpty());
    }

    /**
     * 检查给定的CharSequence是否包含实际文本。
     * 更具体地说，如果CharSequence不为null ，其长度大于0且包含至少一个非空白字符，则此方法返回true 。
     *
     * @param str 要检查的CharSequence （可以为null ）
     * @return 如果CharSequence不为null ，其长度大于0，并且不仅包含空格，则为true
     * @see #hasText(String)
     * @see #hasLength(CharSequence)
     * @see Character#isWhitespace
     */
    public static boolean hasText(CharSequence str) {
        return (str != null && str.length() > 0 && containsText(str));
    }

    /**
     * 检查给定的String是否包含实际文本。
     * 更具体地说，如果String不为null ，并且其长度大于0，并且包含至少一个非空白字符，则此方法返回true 。
     *
     * @param str 要检查的String （可以为null ）
     * @return 如果String不为null ，其长度大于0，并且不仅包含空格，则为true
     * @see #hasText(CharSequence)
     * @see #hasLength(String)
     * @see Character#isWhitespace
     */
    public static boolean hasText(String str) {
        return (str != null && !str.isEmpty() && containsText(str));
    }

    private static boolean containsText(CharSequence str) {
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查给定的CharSequence是否包含任何空格字符。
     *
     * @param str 要检查的CharSequence （可以为null ）
     * @return 如果CharSequence不为空并且包含至少1个空格字符，则为true
     * @see Character#isWhitespace
     */
    public static boolean containsWhitespace(CharSequence str) {
        if (!hasLength(str)) {
            return false;
        }

        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查给定的String是否包含任何空格字符。
     *
     * @param str 要检查的String （可以为null ）
     * @return 如果String不为空并且包含至少1个空格字符，则为true
     * @see #containsWhitespace(CharSequence)
     */
    public static boolean containsWhitespace(String str) {
        return containsWhitespace((CharSequence) str);
    }

    /**
     * 修剪给定String前导和尾随空格。
     *
     * @param str 要检查的String
     * @return 修剪过的String
     * @see java.lang.Character#isWhitespace
     */
    public static String trimWhitespace(String str) {
        if (!hasLength(str)) {
            return str;
        }

        int beginIndex = 0;
        int endIndex = str.length() - 1;

        while (beginIndex <= endIndex && Character.isWhitespace(str.charAt(beginIndex))) {
            beginIndex++;
        }

        while (endIndex > beginIndex && Character.isWhitespace(str.charAt(endIndex))) {
            endIndex--;
        }

        return str.substring(beginIndex, endIndex + 1);
    }

    /**
     * 修剪给定String所有空格：开头，结尾和字符之间。
     *
     * @param str 要检查的String
     * @return 修剪过的String
     * @see java.lang.Character#isWhitespace
     */
    public static String trimAllWhitespace(String str) {
        if (!hasLength(str)) {
            return str;
        }

        int len = str.length();
        StringBuilder sb = new StringBuilder(str.length());
        for (int i = 0; i < len; i++) {
            char c = str.charAt(i);
            if (!Character.isWhitespace(c)) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 修剪给定String前导空格。
     *
     * @param str 要检查的String
     * @return 修剪过的String
     * @see java.lang.Character#isWhitespace
     */
    public static String trimLeadingWhitespace(String str) {
        if (!hasLength(str)) {
            return str;
        }

        StringBuilder sb = new StringBuilder(str);
        while (sb.length() > 0 && Character.isWhitespace(sb.charAt(0))) {
            sb.deleteCharAt(0);
        }
        return sb.toString();
    }

    /**
     * 修剪给定String结尾空格。
     *
     * @param str 要检查的String
     * @return 修剪过的String
     * @see java.lang.Character#isWhitespace
     */
    public static String trimTrailingWhitespace(String str) {
        if (!hasLength(str)) {
            return str;
        }

        StringBuilder sb = new StringBuilder(str);
        while (sb.length() > 0 && Character.isWhitespace(sb.charAt(sb.length() - 1))) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    /**
     * 修剪给定String所有出现的提供的前导字符。
     *
     * @param str 要检查的String
     * @param leadingCharacter 要修剪的主角
     * @return 修剪过的String
     */
    public static String trimLeadingCharacter(String str, char leadingCharacter) {
        if (!hasLength(str)) {
            return str;
        }

        StringBuilder sb = new StringBuilder(str);
        while (sb.length() > 0 && sb.charAt(0) == leadingCharacter) {
            sb.deleteCharAt(0);
        }
        return sb.toString();
    }

    /**
     * 修剪给定String所有出现的尾随字符。
     *
     * @param str 要检查的String
     * @param trailingCharacter 要修剪的尾随字符
     * @return 修剪过的String
     */
    public static String trimTrailingCharacter(String str, char trailingCharacter) {
        if (!hasLength(str)) {
            return str;
        }

        StringBuilder sb = new StringBuilder(str);
        while (sb.length() > 0 && sb.charAt(sb.length() - 1) == trailingCharacter) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    /**
     * 测试给定的String匹配给定的单个字符。
     *
     * @param str 要检查的String
     * @param singleCharacter 要比较的字符
     */
    public static boolean matchesCharacter(String str, char singleCharacter) {
        return (str != null && str.length() == 1 && str.charAt(0) == singleCharacter);
    }

    /**
     * T测试给定的String是否以指定的前缀开头，忽略大小写。
     *
     * @param str 要检查的String
     * @param prefix 查找的前缀
     * @see java.lang.String#startsWith
     */
    public static boolean startsWithIgnoreCase(String str, String prefix) {
        return (str != null && prefix != null && str.length() >= prefix.length() &&
                str.regionMatches(true, 0, prefix, 0, prefix.length()));
    }

    /**
     * 测试给定的String是否以指定的后缀结尾，忽略大小写。
     *
     * @param str 要检查的String
     * @param suffix 寻找的后缀
     * @see java.lang.String#endsWith
     */
    public static boolean endsWithIgnoreCase(String str, String suffix) {
        return (str != null && suffix != null && str.length() >= suffix.length() &&
                str.regionMatches(true, str.length() - suffix.length(), suffix, 0, suffix.length()));
    }

    /**
     * 测试给定字符串是否与给定索引处的给定子字符串匹配。
     *
     * @param str 原始字符串（或StringBuilder）
     * @param index 原始字符串中要开始匹配的索引
     * @param substring 在给定索引处匹配的子字符串
     */
    public static boolean substringMatch(CharSequence str, int index, CharSequence substring) {
        if (index + substring.length() > str.length()) {
            return false;
        }
        for (int i = 0; i < substring.length(); i++) {
            if (str.charAt(index + i) != substring.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 计算字符串str sub字符串sub的出现次数。
     *
     * @param str 被搜索的字符串
     * @param sub 要搜索的字符串
     */
    public static int countOccurrencesOf(String str, String sub) {
        if (!hasLength(str) || !hasLength(sub)) {
            return 0;
        }

        int count = 0;
        int pos = 0;
        int idx;
        while ((idx = str.indexOf(sub, pos)) != -1) {
            ++count;
            pos = idx + sub.length();
        }
        return count;
    }

    /**
     * 用另一个字符串替换一个字符串中所有出现的子字符串。
     *
     * @param inString 要检查的String
     * @param oldPattern 要替换的String
     * @param newPattern 要插入的String
     * @return 带有替换的String
     */
    public static String replace(String inString, String oldPattern, String newPattern) {
        if (!hasLength(inString) || !hasLength(oldPattern) || newPattern == null) {
            return inString;
        }
        int index = inString.indexOf(oldPattern);
        if (index == -1) {
            // no occurrence -> can return input as-is
            return inString;
        }

        int capacity = inString.length();
        if (newPattern.length() > oldPattern.length()) {
            capacity += 16;
        }
        StringBuilder sb = new StringBuilder(capacity);

        // 我们在旧字符串上的位置
        int pos = 0;
        int patLen = oldPattern.length();
        while (index >= 0) {
            sb.append(inString, pos, index);
            sb.append(newPattern);
            pos = index + patLen;
            index = inString.indexOf(oldPattern, pos);
        }

        // 在匹配项的右侧附加任何字符
        sb.append(inString, pos, inString.length());
        return sb.toString();
    }

    /**
     * 删除所有出现的给定子字符串。
     *
     * @param inString 原始String
     * @param pattern 删除所有出现的模式
     * @return 结果String
     */
    public static String delete(String inString, String pattern) {
        return replace(inString, pattern, "");
    }

    /**
     * 删除给定String任何字符。
     *
     * @param inString 原始String
     * @param charsToDelete 一组要删除的字符。 例如，“ az \ n”将删除“ a”，“ z”和新行。
     * @return 结果String
     */
    public static String deleteAny(String inString, String charsToDelete) {
        if (!hasLength(inString) || !hasLength(charsToDelete)) {
            return inString;
        }

        int lastCharIndex = 0;
        char[] result = new char[inString.length()];
        for (int i = 0; i < inString.length(); i++) {
            char c = inString.charAt(i);
            if (charsToDelete.indexOf(c) == -1) {
                result[lastCharIndex++] = c;
            }
        }
        if (lastCharIndex == inString.length()) {
            return inString;
        }
        return new String(result, 0, lastCharIndex);
    }

    //---------------------------------------------------------------------
    // 处理格式化字符串的简便方法
    //---------------------------------------------------------------------

    /**
     * 用单引号将给定的String引号引起来。
     *
     * @param str 输入String （例如“ myString”）
     * @return 引用的String （例如，“‘的myString’”），或null如果输入是null
     */
    public static String quote(String str) {
        return (str != null ? "'" + str + "'" : null);
    }

    /**
     * 如果给定的对象是String ，则将其转换为带单引号的String ； 保持Object不变。
     *
     * @param obj 输入对象（例如“ myString”）
     * @return 带引号的String （例如“'myString'”），如果不是String ，则按原样输入对象
     */
    public static Object quoteIfString(Object obj) {
        return (obj instanceof String ? quote((String) obj) : obj);
    }

    /**
     * 取消用“。”限定的字符串的资格。 点字符。 例如，“ this.name.is.qualified”返回“ qualified”。
     *
     * @param qualifiedName 合格名称
     */
    public static String unqualify(String qualifiedName) {
        return unqualify(qualifiedName, '.');
    }

    /**
     * 取消使用分隔符限定的字符串的资格。 例如，如果使用'：'分隔符，则“ this：name：is：qualified”将返回“ qualified”。
     *
     * @param qualifiedName 合格名称
     * @param separator 分隔符
     */
    public static String unqualify(String qualifiedName, char separator) {
        return qualifiedName.substring(qualifiedName.lastIndexOf(separator) + 1);
    }

    /**
     * 大写一个String ，按照Character.toUpperCase(char)将首字母更改为大写。 没有其他字母被更改。
     *
     *
     * @param str 要大写的String
     * @return 大写的String
     */
    public static String capitalize(String str) {
        return changeFirstCharacterCase(str, true);
    }

    /**
     * 将String取消大写，根据Character.toLowerCase(char)将首字母更改为小写。 没有其他字母被更改。
     *
     * @param str 要取消大写的String
     * @return 没有大写的String
     */
    public static String uncapitalize(String str) {
        return changeFirstCharacterCase(str, false);
    }

    private static String changeFirstCharacterCase(String str, boolean capitalize) {
        if (!hasLength(str)) {
            return str;
        }

        char baseChar = str.charAt(0);
        char updatedChar;
        if (capitalize) {
            updatedChar = Character.toUpperCase(baseChar);
        }
        else {
            updatedChar = Character.toLowerCase(baseChar);
        }
        if (baseChar == updatedChar) {
            return str;
        }

        char[] chars = str.toCharArray();
        chars[0] = updatedChar;
        return new String(chars, 0, chars.length);
    }

    /**
     * 从给定的Java资源路径中提取文件名，例如"mypath/myfile.txt" -> "myfile.txt" 。
     *
     *
     * @param path 文件路径（可以为null ）
     * @return 提取的文件名；如果没有，则返回null
     */
    public static String getFilename(String path) {
        if (path == null) {
            return null;
        }

        int separatorIndex = path.lastIndexOf(FOLDER_SEPARATOR);
        return (separatorIndex != -1 ? path.substring(separatorIndex + 1) : path);
    }

    /**
     * 从给定的Java资源路径中提取文件扩展名，例如“ mypath / myfile.txt”->“ txt”。
     *
     * @param path 文件路径（可以为null ）
     * @return 提取的文件扩展名，如果没有，则为null
     */
    public static String getFilenameExtension(String path) {
        if (path == null) {
            return null;
        }

        int extIndex = path.lastIndexOf(EXTENSION_SEPARATOR);
        if (extIndex == -1) {
            return null;
        }

        int folderIndex = path.lastIndexOf(FOLDER_SEPARATOR);
        if (folderIndex > extIndex) {
            return null;
        }

        return path.substring(extIndex + 1);
    }

    /**
     * 从给定的Java资源路径中删除文件扩展名，例如“ mypath / myfile.txt”->“ mypath / myfile”。
     *
     * @param path 文件路径
     * @return 带扩展名的路径
     */
    public static String stripFilenameExtension(String path) {
        int extIndex = path.lastIndexOf(EXTENSION_SEPARATOR);
        if (extIndex == -1) {
            return path;
        }

        int folderIndex = path.lastIndexOf(FOLDER_SEPARATOR);
        if (folderIndex > extIndex) {
            return path;
        }

        return path.substring(0, extIndex);
    }

    /**
     * 假定标准Java文件夹分隔（即“ /”分隔符），将给定的相对路径应用于给定的Java资源路径。
     *
     * @param path 起始路径（通常是完整文件路径）
     * @param relativePath 要应用的相对路径（相对于上面的完整文件路径）
     * @return 应用相对路径产生的完整文件路径
     */
    public static String applyRelativePath(String path, String relativePath) {
        int separatorIndex = path.lastIndexOf(FOLDER_SEPARATOR);
        if (separatorIndex != -1) {
            String newPath = path.substring(0, separatorIndex);
            if (!relativePath.startsWith(FOLDER_SEPARATOR)) {
                newPath += FOLDER_SEPARATOR;
            }
            return newPath + relativePath;
        }
        else {
            return relativePath;
        }
    }

    /**
     * 通过抑制“ path / ..”和内部简单点之类的序列来规范化路径。
     * 结果便于进行路径比较。 对于其他用途，请注意，Windows分隔符（\）替换为简单的斜杠。
     * 注意， cleanPath不应在安全上下文中依赖。 应该使用其他机制来防止路径遍历问题。
     *
     * @param path 原始路径
     * @return 归一化路径
     */
    public static String cleanPath(String path) {
        if (!hasLength(path)) {
            return path;
        }
        String pathToUse = replace(path, WINDOWS_FOLDER_SEPARATOR, FOLDER_SEPARATOR);

        // Shortcut if there is no work to do
        if (pathToUse.indexOf('.') == -1) {
            return pathToUse;
        }

        // Strip prefix from path to analyze, to not treat it as part of the
        // first path element. This is necessary to correctly parse paths like
        // "file:core/../core/io/Resource.class", where the ".." should just
        // strip the first "core" directory while keeping the "file:" prefix.
        int prefixIndex = pathToUse.indexOf(':');
        String prefix = "";
        if (prefixIndex != -1) {
            prefix = pathToUse.substring(0, prefixIndex + 1);
            if (prefix.contains(FOLDER_SEPARATOR)) {
                prefix = "";
            }
            else {
                pathToUse = pathToUse.substring(prefixIndex + 1);
            }
        }
        if (pathToUse.startsWith(FOLDER_SEPARATOR)) {
            prefix = prefix + FOLDER_SEPARATOR;
            pathToUse = pathToUse.substring(1);
        }

        String[] pathArray = delimitedListToStringArray(pathToUse, FOLDER_SEPARATOR);
        Deque<String> pathElements = new ArrayDeque<>();
        int tops = 0;

        for (int i = pathArray.length - 1; i >= 0; i--) {
            String element = pathArray[i];
            if (CURRENT_PATH.equals(element)) {
                // Points to current directory - drop it.
            }
            else if (TOP_PATH.equals(element)) {
                // Registering top path found.
                tops++;
            }
            else {
                if (tops > 0) {
                    // Merging path element with element corresponding to top path.
                    tops--;
                }
                else {
                    // Normal path element found.
                    pathElements.addFirst(element);
                }
            }
        }

        // All path elements stayed the same - shortcut
        if (pathArray.length == pathElements.size()) {
            return prefix + pathToUse;
        }
        // Remaining top paths need to be retained.
        for (int i = 0; i < tops; i++) {
            pathElements.addFirst(TOP_PATH);
        }
        // If nothing else left, at least explicitly point to current path.
        if (pathElements.size() == 1 && pathElements.getLast().isEmpty() && !prefix.endsWith(FOLDER_SEPARATOR)) {
            pathElements.addFirst(CURRENT_PATH);
        }

        return prefix + collectionToDelimitedString(pathElements, FOLDER_SEPARATOR);
    }

    /**
     * 将两条路径规范化后比较它们。
     * @param path1 第一条比较路径
     * @param path2 第二条比较路径
     * @return 规范化后两条路径是否等价
     */
    public static boolean pathEquals(String path1, String path2) {
        return cleanPath(path1).equals(cleanPath(path2));
    }

    /**
     * 解码给定的已编码URI组件值。 基于以下规则：
     * 字母数字字符"a"至"z" ， "A"至"Z"和"0"至"9"保持不变。
     * 特殊字符"-" ， "_" ， "." ，和"*"保持不变。
     * 序列“ %<i>xy</i> ”被解释为字符的十六进制表示。
     *
     * @param source 编码的字符串
     * @param charset 字符集
     * @return 解码值
     * @throws IllegalArgumentException IllegalArgumentException当给定源包含无效的编码序列时
     * @see java.net.URLDecoder#decode(String, String)
     */
    public static String uriDecode(String source, Charset charset) throws UnsupportedEncodingException {
        int length = source.length();
        if (length == 0) {
            return source;
        }
        // Assert.notNull(charset, "Charset must not be null");
        ByteArrayOutputStream baos = new ByteArrayOutputStream(length);
        boolean changed = false;
        for (int i = 0; i < length; i++) {
            int ch = source.charAt(i);
            if (ch == '%') {
                if (i + 2 < length) {
                    char hex1 = source.charAt(i + 1);
                    char hex2 = source.charAt(i + 2);
                    int u = Character.digit(hex1, 16);
                    int l = Character.digit(hex2, 16);
                    if (u == -1 || l == -1) {
                        throw new IllegalArgumentException("Invalid encoded sequence \"" + source.substring(i) + "\"");
                    }
                    baos.write((char) ((u << 4) + l));
                    i += 2;
                    changed = true;
                }
                else {
                    throw new IllegalArgumentException("Invalid encoded sequence \"" + source.substring(i) + "\"");
                }
            }
            else {
                baos.write(ch);
            }
        }
        return (changed ? baos.toString(charset.name()) : source);
    }

    /**
     * 将给定的String值解析为Locale ，接受Locale.toString格式以及BCP 47语言标记。
     *
     * @param localeValue 语言环境值：遵循Locale's toString()格式（“ en”，“ en_UK”等），也接受空格作为分隔符（作为下划线的替代），或BCP 47（例如“ en-UK”）作为由Java 7+上的Locale.forLanguageTag指定
     * @return 相应的Locale实例；如果没有，则为null
     * @throws IllegalArgumentException IllegalArgumentException如果无效的语言环境规范
     * @see #parseLocaleString
     * @see Locale#forLanguageTag
     */
    public static Locale parseLocale(String localeValue) {
        String[] tokens = tokenizeLocaleSource(localeValue);
        if (tokens.length == 1) {
            validateLocalePart(localeValue);
            Locale resolved = Locale.forLanguageTag(localeValue);
            if (resolved.getLanguage().length() > 0) {
                return resolved;
            }
        }
        return parseLocaleTokens(localeValue, tokens);
    }

    /**
     * 将给定的String表示形式解析为Locale 。
     * 对于许多解析方案，从宽大的角度来看，这是Locale's toString的逆运算。 此方法并不旨在严格遵守Locale设计。 它是专门为典型的Spring解析需求量身定制的。
     * 注意：此委托人不接受BCP 47语言标记格式。 请使用parseLocale解析这两种格式。
     *
     * @param localeString 语言环境String ：遵循Locale's toString()格式（“ en”，“ en_UK”等），还接受空格作为分隔符（作为下划线的替代方法）
     * @return 相应的Locale实例；如果没有，则为null
     * @throws IllegalArgumentException IllegalArgumentException如果无效的语言环境规范
     */
    public static Locale parseLocaleString(String localeString) {
        return parseLocaleTokens(localeString, tokenizeLocaleSource(localeString));
    }

    private static String[] tokenizeLocaleSource(String localeSource) {
        return tokenizeToStringArray(localeSource, "_ ", false, false);
    }

    private static Locale parseLocaleTokens(String localeString, String[] tokens) {
        String language = (tokens.length > 0 ? tokens[0] : "");
        String country = (tokens.length > 1 ? tokens[1] : "");
        validateLocalePart(language);
        validateLocalePart(country);

        String variant = "";
        if (tokens.length > 2) {
            // There is definitely a variant, and it is everything after the country
            // code sans the separator between the country code and the variant.
            int endIndexOfCountryCode = localeString.indexOf(country, language.length()) + country.length();
            // Strip off any leading '_' and whitespace, what's left is the variant.
            variant = trimLeadingWhitespace(localeString.substring(endIndexOfCountryCode));
            if (variant.startsWith("_")) {
                variant = trimLeadingCharacter(variant, '_');
            }
        }

        if (variant.isEmpty() && country.startsWith("#")) {
            variant = country;
            country = "";
        }

        return (language.length() > 0 ? new Locale(language, country, variant) : null);
    }

    private static void validateLocalePart(String localePart) {
        for (int i = 0; i < localePart.length(); i++) {
            char ch = localePart.charAt(i);
            if (ch != ' ' && ch != '_' && ch != '-' && ch != '#' && !Character.isLetterOrDigit(ch)) {
                throw new IllegalArgumentException(
                        "Locale part \"" + localePart + "\" contains invalid characters");
            }
        }
    }

    /**
     * 将给定的timeZoneString值解析为TimeZone 。
     *
     *
     * @param timeZoneString 时区String ，紧随TimeZone.getTimeZone(String)但在无效时区规范的情况下抛出IllegalArgumentException
     * @return 相应的TimeZone实例
     * @throws IllegalArgumentException IllegalArgumentException如果时区指定无效
     */
    public static TimeZone parseTimeZoneString(String timeZoneString) {
        TimeZone timeZone = TimeZone.getTimeZone(timeZoneString);
        if ("GMT".equals(timeZone.getID()) && !timeZoneString.startsWith("GMT")) {
            // 我们不想让格林威治时间倒退。。。
            throw new IllegalArgumentException("Invalid time zone specification '" + timeZoneString + "'");
        }
        return timeZone;
    }


    //---------------------------------------------------------------------
    // 使用字符串数组的简便方法
    //---------------------------------------------------------------------

    /**
     * 将给定的Collection复制到String数组中。
     * Collection只能包含String元素。
     *
     * @param collection 要复制的Collection （可能为null或为空）
     * @return 结果String数组
     */
    public static String[] toStringArray(Collection<String> collection) {
        return (!(collection == null || collection.isEmpty()) ? collection.toArray(EMPTY_STRING_ARRAY) : EMPTY_STRING_ARRAY);
    }

    /**
     * 将给定的Enumeration复制到String数组中。
     * Enumeration必须仅包含String元素。
     *
     * @param enumeration 要复制的Enumeration （可能为null或为空）
     * @return 结果String数组
     */
    public static String[] toStringArray(Enumeration<String> enumeration) {
        return (enumeration != null ? toStringArray(Collections.list(enumeration)) : EMPTY_STRING_ARRAY);
    }

    /**
     * 将给定的String附加到给定的String数组，返回一个新数组，该数组由输入数组内容加上给定的String 。
     *
     * @param array 要追加到的数组（可以为null ）
     * @param str 要附加的String
     * @return 新数组（绝不为null ）
     */
    public static String[] addStringToArray(String[] array, String str) {
        if (ObjectUtils.isEmpty(array)) {
            return new String[] {str};
        }

        String[] newArr = new String[array.length + 1];
        System.arraycopy(array, 0, newArr, 0, array.length);
        newArr[array.length] = str;
        return newArr;
    }

    /**
     * 将给定的String数组连接为一个，其中重叠的数组元素包含两次。
     * 保留原始数组中元素的顺序。
     *
     * @param array1 第一个数组（可以为null ）
     * @param array2 第二个数组（可以为null ）
     * @return 新数组（ null如果两个给定的数组是null ）
     */
    public static String[] concatenateStringArrays(String[] array1, String[] array2) {
        if (ObjectUtils.isEmpty(array1)) {
            return array2;
        }
        if (ObjectUtils.isEmpty(array2)) {
            return array1;
        }

        String[] newArr = new String[array1.length + array2.length];
        System.arraycopy(array1, 0, newArr, 0, array1.length);
        System.arraycopy(array2, 0, newArr, array1.length, array2.length);
        return newArr;
    }

    /**
     * 如有必要，对给定的String数组进行排序。
     *
     * @param array 原始数组（可能为空）
     * @return 排序形式的数组（绝不为null ）
     */
    public static String[] sortStringArray(String[] array) {
        if (ObjectUtils.isEmpty(array)) {
            return array;
        }

        Arrays.sort(array);
        return array;
    }

    /**
     * 修剪给定String数组的元素，在每个非null元素上调用String.trim() 。
     *
     * @param array 原始的String数组（可能为空）
     * @return 带有修剪元素的结果数组（大小相同）
     */
    public static String[] trimArrayElements(String[] array) {
        if (ObjectUtils.isEmpty(array)) {
            return array;
        }

        String[] result = new String[array.length];
        for (int i = 0; i < array.length; i++) {
            String element = array[i];
            result[i] = (element != null ? element.trim() : null);
        }
        return result;
    }

    /**
     * 从给定数组中删除重复的字符串。
     * 从4.2开始，它使用LinkedHashSet保留原始顺序。
     *
     * @param array String数组（可能为空）
     * @return 没有重复的数组，按自然排序顺序
     */
    public static String[] removeDuplicateStrings(String[] array) {
        if (ObjectUtils.isEmpty(array)) {
            return array;
        }

        Set<String> set = new LinkedHashSet<>(Arrays.asList(array));
        return toStringArray(set);
    }

    /**
     * 在第一次出现分隔符时拆分一个String 。 结果中不包含定界符。
     *
     * @param toSplit 要分割的字符串（可能为null或为空）
     * @param delimiter 使用分隔符（可能为null或为空）
     * @return 一个两个元素的数组，其中索引0在定界符之前，索引1在定界符之后（两个元素都不包括定界符）； 如果在给定的输入String找不到分隔符，则返回null
     */
    public static String[] split(String toSplit, String delimiter) {
        if (!hasLength(toSplit) || !hasLength(delimiter)) {
            return null;
        }
        int offset = toSplit.indexOf(delimiter);
        if (offset < 0) {
            return null;
        }

        String beforeDelimiter = toSplit.substring(0, offset);
        String afterDelimiter = toSplit.substring(offset + delimiter.length());
        return new String[] {beforeDelimiter, afterDelimiter};
    }

    /**
     * 取一个字符串数组，并根据给定的定界符分割每个元素。 然后将生成一个Properties实例，其中分隔符的左侧提供键，而分隔符的右侧提供值。
     * 在将键和值添加到Properties之前，将对其进行修整。
     *
     * @param array 要处理的数组
     * @param delimiter 使用（通常是等号）分割每个元素
     * @return 一个Properties实例表示该数组的内容，或者null ，如果该阵列过程是null或空
     */
    public static Properties splitArrayElementsIntoProperties(String[] array, String delimiter) {
        return splitArrayElementsIntoProperties(array, delimiter, null);
    }

    /**
     * 取一个字符串数组，并根据给定的定界符分割每个元素。 然后将生成一个Properties实例，其中分隔符的左侧提供键，而分隔符的右侧提供值。
     * 在将键和值添加到Properties实例之前，将对其进行修整。
     *
     * @param array 要处理的数组
     * @param delimiter 使用（通常是等号）分割每个元素
     * @param charsToDelete 一个或多个字符，在尝试进行拆分操作之前，应从每个元素中删除（通常是引号符号）；如果不应该进行删除，则为null
     * @return 一个Properties实例表示该数组的内容，或者null ，如果该阵列过程是null或空
     */
    public static Properties splitArrayElementsIntoProperties(
            String[] array, String delimiter, String charsToDelete) {

        if (ObjectUtils.isEmpty(array)) {
            return null;
        }

        Properties result = new Properties();
        for (String element : array) {
            if (charsToDelete != null) {
                element = deleteAny(element, charsToDelete);
            }
            String[] splittedElement = split(element, delimiter);
            if (splittedElement == null) {
                continue;
            }
            result.setProperty(splittedElement[0].trim(), splittedElement[1].trim());
        }
        return result;
    }

    /**
     * 通过StringTokenizer将给定的String标记为String数组。
     * 修剪令牌并省略空令牌。
     * 给定的delimiters字符串可以包含任意数量的定界符字符。 这些字符中的每一个都可以用于分隔标记。 分隔符始终是单个字符； 对于多字符定界符，请考虑使用delimitedListToStringArray 。
     *
     * @param str 要标记化的String （可能为null或为空）
     * @param delimiters 分隔符，组合为一个String （每个字符都单独视为分隔符）
     * @return 令牌数组
     * @see java.util.StringTokenizer
     * @see String#trim()
     * @see #delimitedListToStringArray
     */
    public static String[] tokenizeToStringArray(String str, String delimiters) {
        return tokenizeToStringArray(str, delimiters, true, true);
    }

    /**
     * 通过StringTokenizer将给定的String标记为String数组。
     * 给定的delimiters字符串可以包含任意数量的定界符字符。 这些字符中的每一个都可以用于分隔标记。 分隔符始终是单个字符； 对于多字符定界符，请考虑使用delimitedListToStringArray 。
     *
     * @param str 要标记化的String （可能为null或为空）
     * @param delimiters 分隔符，组合为一个String （每个字符都单独视为分隔符）
     * @param trimTokens 通过String.trim()修剪令牌
     * @param ignoreEmptyTokens 从结果数组中省略空标记（仅适用于修剪后为空的标记； StringTokenizer首先不会将后续的分隔符视为标记）。
     * @return 令牌数组
     * @see java.util.StringTokenizer
     * @see String#trim()
     * @see #delimitedListToStringArray
     */
    public static String[] tokenizeToStringArray(
            String str, String delimiters, boolean trimTokens, boolean ignoreEmptyTokens) {

        if (str == null) {
            return EMPTY_STRING_ARRAY;
        }

        StringTokenizer st = new StringTokenizer(str, delimiters);
        List<String> tokens = new ArrayList<>();
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (trimTokens) {
                token = token.trim();
            }
            if (!ignoreEmptyTokens || token.length() > 0) {
                tokens.add(token);
            }
        }
        return toStringArray(tokens);
    }

    /**
     * 取得一个String ，该String是一个定界列表，并将其转换为String数组。
     * 单个delimiter可以包含多个字符，但是与tokenizeToStringArray相比，它仍将被视为单个定界符字符串，而不是一堆潜在的定界符。
     *
     * @param str 输入String （可能为null或为空）
     * @param delimiter 元素之间的定界符（这是单个定界符，而不是一堆单独的定界符）
     * @return 列表中的令牌数组
     * @see #tokenizeToStringArray
     */
    public static String[] delimitedListToStringArray(String str, String delimiter) {
        return delimitedListToStringArray(str, delimiter, null);
    }

    /**
     * 取得一个String ，该String是一个定界列表，并将其转换为String数组。
     * 单个delimiter可以包含多个字符，但是与tokenizeToStringArray相比，它仍将被视为单个定界符字符串，而不是一堆潜在的定界符。
     *
     * @param str 输入String （可能为null或为空）
     * @param delimiter 元素之间的定界符（这是单个定界符，而不是一堆单独的定界符）
     * @param charsToDelete 一组要删除的字符； 对于删除不需要的换行符很有用：例如，“ \ r \ n \ f”将删除String所有新行和换行符
     * @return 列表中的令牌数组
     * @see #tokenizeToStringArray
     */
    public static String[] delimitedListToStringArray(
            String str, String delimiter, String charsToDelete) {

        if (str == null) {
            return EMPTY_STRING_ARRAY;
        }
        if (delimiter == null) {
            return new String[] {str};
        }

        List<String> result = new ArrayList<>();
        if (delimiter.isEmpty()) {
            for (int i = 0; i < str.length(); i++) {
                result.add(deleteAny(str.substring(i, i + 1), charsToDelete));
            }
        }
        else {
            int pos = 0;
            int delPos;
            while ((delPos = str.indexOf(delimiter, pos)) != -1) {
                result.add(deleteAny(str.substring(pos, delPos), charsToDelete));
                pos = delPos + delimiter.length();
            }
            if (str.length() > 0 && pos <= str.length()) {
                //添加字符串的其余部分，但不能在输入为空的情况下添加。
                result.add(deleteAny(str.substring(pos), charsToDelete));
            }
        }
        return toStringArray(result);
    }

    /**
     * 将逗号分隔的列表（例如，CSV文件中的一行）转换为字符串数组。
     *
     * @param str 输入String （可能为null或为空）
     * @return 字符串数组，或在输入为空的情况下为空数组
     */
    public static String[] commaDelimitedListToStringArray(String str) {
        return delimitedListToStringArray(str, ",");
    }

    /**
     * 将逗号分隔列表（例如，CSV文件中的一行）转换为一组。
     *
     * @param str 输入String （可能为null或为空）
     * @return 列表中的一组String条目
     * @see #removeDuplicateStrings(String[])
     */
    public static Set<String> commaDelimitedListToSet(String str) {
        String[] tokens = commaDelimitedListToStringArray(str);
        return new LinkedHashSet<>(Arrays.asList(tokens));
    }

    /**
     * 将Collection转换为定界String （例如CSV）。
     * 对于toString()实现很有用。
     *
     * @param coll t要转换的Collection （可能为null或为空）
     * @param delim 要使用的分隔符（通常为“，”）
     * @param prefix 以每个元素开头的String
     * @param suffix 以每个元素结尾的String
     * @return 分隔String
     */
    public static String collectionToDelimitedString(
            Collection<?> coll, String delim, String prefix, String suffix) {

        if (coll == null || coll.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        Iterator<?> it = coll.iterator();
        while (it.hasNext()) {
            sb.append(prefix).append(it.next()).append(suffix);
            if (it.hasNext()) {
                sb.append(delim);
            }
        }
        return sb.toString();
    }

    /**
     * 将Collection转换为定界String （例如CSV）。
     * 对于toString()实现很有用。
     *
     * @param coll 要转换的Collection （可能为null或为空）
     * @param delim 要使用的分隔符（通常为“，”）
     * @return 分隔String
     */
    public static String collectionToDelimitedString(Collection<?> coll, String delim) {
        return collectionToDelimitedString(coll, delim, "", "");
    }

    /**
     * 将Collection转换为定界String （例如CSV）。
     * 对于toString()实现很有用。
     *
     * @param coll 要转换的Collection （可能为null或为空）
     * @return 分隔String
     */
    public static String collectionToCommaDelimitedString(Collection<?> coll) {
        return collectionToDelimitedString(coll, ",");
    }

    /**
     * 将String数组转换为定界的String （例如CSV）。
     * 对于toString()实现很有用。
     *
     * @param arr 要显示的数组（可能为null或为空）
     * @param delim 要使用的分隔符（通常为“，”）
     * @return 分隔String
     */
    public static String arrayToDelimitedString(Object[] arr, String delim) {
        if (ObjectUtils.isEmpty(arr)) {
            return "";
        }
        if (arr.length == 1) {
            return ObjectUtils.nullSafeToString(arr[0]);
        }

        StringJoiner sj = new StringJoiner(delim);
        for (Object o : arr) {
            sj.add(String.valueOf(o));
        }
        return sj.toString();
    }

    /**
     * 将String数组转换为以逗号分隔的String （即CSV）。
     * 对于toString()实现很有用。
     *
     * @param arr 要显示的数组（可能为null或为空）
     * @return 分隔String
     */
    public static String arrayToCommaDelimitedString(Object[] arr) {
        return arrayToDelimitedString(arr, ",");
    }

}

