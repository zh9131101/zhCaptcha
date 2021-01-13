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

import java.lang.reflect.Array;
import java.util.*;

/**
 * <p>
 * Object工具类
 * </p>
 *
 * @author zh9131101
 * @version V1.0.0
 * @date 2021-01-07 22:07
 * @since 1.0
 */

public class ObjectUtils {

    private ObjectUtils() {
    }

    public static final int INITIAL_HASH = 7;
    public static final int MULTIPLIER = 31;

    public static final String EMPTY_STRING = "";
    public static final String NULL_STRING = "null";
    public static final String ARRAY_START = "{";
    public static final String ARRAY_END = "}";
    public static final String EMPTY_ARRAY = ARRAY_START + ARRAY_END;
    public static final String ARRAY_ELEMENT_SEPARATOR = ", ";
    public static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];


    /**
     * 返回给定的throwable是否为检查的异常：即，既不是RuntimeException也不是Error。
     *
     * @param ex 抛出检查
     * @return throwable是否为检查异常
     * @see java.lang.Exception
     * @see java.lang.RuntimeException
     * @see java.lang.Error
     */
    public static boolean isCheckedException(Throwable ex) {
        return !(ex instanceof RuntimeException || ex instanceof Error);
    }

    /**
     * 检查throws子句中声明的给定异常是否与指定的异常类型兼容。
     *
     * @param ex 检查的异常
     * @param declaredExceptions 在throws子句中声明的异常类型
     * @return 给定的异常是否兼容
     */
    public static boolean isCompatibleWithThrowsClause(Throwable ex, Class<?>... declaredExceptions) {
        if (!isCheckedException(ex)) {
            return true;
        }
        if (declaredExceptions != null) {
            for (Class<?> declaredException : declaredExceptions) {
                if (declaredException.isInstance(ex)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 确定给定的对象是数组：Object数组还是基本数组。
     *
     * @param obj 要检查的对象
     */
    public static boolean isArray(Object obj) {
        return (obj != null && obj.getClass().isArray());
    }

    /**
     * 确定给定数组是否为空：即null或长度为零。
     *
     * @param array 要检查的数组
     * @see #isEmpty(Object)
     */
    public static boolean isEmpty(Object[] array) {
        return (array == null || array.length == 0);
    }

    /**
     * 确定给定对象是否为空。
     * 此方法支持以下对象类型。
     * Optional ：如果Optional.empty()则视为空
     * Array ：如果长度为零，则视为空
     * CharSequence ：如果长度为零，则视为空
     * Collection ：委托给Collection.isEmpty()
     * Map ： Map.isEmpty()委托
     * 如果给定对象为非null且不是上述支持的类型之一，则此方法返回false 。
     *
     * @param obj 要检查的对象
     * @return 如果对象为null或为空，则为true
     * @see Optional#isPresent()
     * @see ObjectUtils#isEmpty(Object[])
     * @see StringUtils#hasLength(CharSequence)
     * @see StringUtils#isEmpty(Object)
     */
    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof Optional) {
            return !((Optional<?>) obj).isPresent();
        }
        if (obj instanceof CharSequence) {
            return ((CharSequence) obj).length() == 0;
        }
        if (obj.getClass().isArray()) {
            return Array.getLength(obj) == 0;
        }
        if (obj instanceof Collection) {
            return ((Collection<?>) obj).isEmpty();
        }
        if (obj instanceof Map) {
            return ((Map<?, ?>) obj).isEmpty();
        }
        // else
        return false;
    }

    /**
     * 解开给定的对象，该对象可能是Optional 。
     *
     * @param obj 候选对象
     * @return 在Optional保存的值；如果Optional为空，则为null ；或者按原样仅提供给定对象
     */
    public static Object unwrapOptional(Object obj) {
        if (obj instanceof Optional) {
            Optional<?> optional = (Optional<?>) obj;
            if (!optional.isPresent()) {
                return null;
            }
            Object result = optional.get();
            return result;
        }
        return obj;
    }

    /**
     * 检查给定数组是否包含给定元素。
     *
     * @param array 要检查的数组（可以为null ，在这种情况下，返回值将始终为false ）
     * @param element 要检查的元素
     * @return 是否在给定数组中找到了元素
     */
    public static boolean containsElement(Object[] array, Object element) {
        if (array == null) {
            return false;
        }
        for (Object arrayEle : array) {
            if (nullSafeEquals(arrayEle, element)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查给定的枚举常量数组是否包含具有给定名称的常量，在确定匹配项时忽略大小写。
     *
     * @param enumValues 要检查的枚举值，通常通过MyEnum.values()获得
     * @param constant 要查找的常量名称（不得为null或空字符串）
     * @return 是否在给定数组中找到常数
     */
    public static boolean containsConstant(Enum<?>[] enumValues, String constant) {
        return containsConstant(enumValues, constant, false);
    }

    /**
     * 检查给定的枚举常量数组是否包含具有给定名称的常量。
     *
     * @param enumValues 要检查的枚举值，通常通过MyEnum.values()获得
     * @param constant 要查找的常量名称（不得为null或空字符串）
     * @param caseSensitive 在确定匹配项时是否区分大小写
     * @return whether 是否在给定数组中找到常数
     */
    public static boolean containsConstant(Enum<?>[] enumValues, String constant, boolean caseSensitive) {
        for (Enum<?> candidate : enumValues) {
            if (caseSensitive ? candidate.toString().equals(constant) :
                    candidate.toString().equalsIgnoreCase(constant)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 不区分大小写的替代Enum.valueOf(Class, String) 。
     *
     * @param <E> the 具体的Enum类型
     * @param enumValues 有问题的所有Enum常量的数组，通常每个Enum.values()
     * @param constant 获得以下枚举值的常量
     * @throws IllegalArgumentException IllegalArgumentException如果在给定的枚举值数组中找不到给定的常量。 使用containsConstant(Enum[], String)作为防范措施来避免此异常。
     */
    public static <E extends Enum<?>> E caseInsensitiveValueOf(E[] enumValues, String constant) {
        for (E candidate : enumValues) {
            if (candidate.toString().equalsIgnoreCase(constant)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("Constant [" + constant + "] does not exist in enums type " +
                enumValues.getClass().getComponentType().getName());
    }

    /**
     * 将给定对象附加到给定数组，返回一个新数组，该数组由输入数组内容和给定对象组成。
     *
     * @param array 要追加到的数组（可以为null ）
     * @param obj 要附加的对象
     * @return 新数组（具有相同的组件类型；绝不为null ）
     */
    public static <A, O extends A> A[] addObjectToArray(A[] array, O obj) {
        Class<?> compType = Object.class;
        if (array != null) {
            compType = array.getClass().getComponentType();
        }
        else if (obj != null) {
            compType = obj.getClass();
        }
        int newArrLength = (array != null ? array.length + 1 : 1);
        @SuppressWarnings("unchecked")
        A[] newArr = (A[]) Array.newInstance(compType, newArrLength);
        if (array != null) {
            System.arraycopy(array, 0, newArr, 0, array.length);
        }
        newArr[newArr.length - 1] = obj;
        return newArr;
    }

    /**
     * 将给定的数组（可能是原始数组）转换为对象数组（如果需要原始包装对象）。
     * null源值将转换为空的Object数组。
     *
     * @param source （可能是原始的）数组
     * @return 对应的对象数组（从不null ）
     * @throws IllegalArgumentException IllegalArgumentException如果参数不是数组
     */
    public static Object[] toObjectArray(Object source) {
        if (source instanceof Object[]) {
            return (Object[]) source;
        }
        if (source == null) {
            return EMPTY_OBJECT_ARRAY;
        }
        if (!source.getClass().isArray()) {
            throw new IllegalArgumentException("Source is not an array: " + source);
        }
        int length = Array.getLength(source);
        if (length == 0) {
            return EMPTY_OBJECT_ARRAY;
        }
        Class<?> wrapperType = Array.get(source, 0).getClass();
        Object[] newArray = (Object[]) Array.newInstance(wrapperType, length);
        for (int i = 0; i < length; i++) {
            newArray[i] = Array.get(source, i);
        }
        return newArray;
    }


    //---------------------------------------------------------------------
    // 基于内容的等式/哈希代码处理的方便方法
    //---------------------------------------------------------------------

    /**
     * 确定给定对象是否相等，如果两个都为null ，则返回true如果只有一个null ，则返回false 。
     * 将数组与Arrays.equals进行比较，基于数组元素而不是数组引用执行相等性检查。
     *
     * @param o1 要比较的第一个对象
     * @param o2 要比较的第二个对象
     * @return 给定对象是否相等
     * @see Object#equals(Object)
     * @see java.util.Arrays#equals
     */
    public static boolean nullSafeEquals(Object o1, Object o2) {
        if (o1 == o2) {
            return true;
        }
        if (o1 == null || o2 == null) {
            return false;
        }
        if (o1.equals(o2)) {
            return true;
        }
        if (o1.getClass().isArray() && o2.getClass().isArray()) {
            return arrayEquals(o1, o2);
        }
        return false;
    }

    /**
     * 将给定的数组与Arrays.equals进行比较，基于数组元素而不是数组引用执行相等性检查。
     *
     * @param o1 比较的第一个数组
     * @param o2 比较的第二个数组
     * @return 给定对象是否相等
     * @see #nullSafeEquals(Object, Object)
     * @see java.util.Arrays#equals
     */
    private static boolean arrayEquals(Object o1, Object o2) {
        if (o1 instanceof Object[] && o2 instanceof Object[]) {
            return Arrays.equals((Object[]) o1, (Object[]) o2);
        }
        if (o1 instanceof boolean[] && o2 instanceof boolean[]) {
            return Arrays.equals((boolean[]) o1, (boolean[]) o2);
        }
        if (o1 instanceof byte[] && o2 instanceof byte[]) {
            return Arrays.equals((byte[]) o1, (byte[]) o2);
        }
        if (o1 instanceof char[] && o2 instanceof char[]) {
            return Arrays.equals((char[]) o1, (char[]) o2);
        }
        if (o1 instanceof double[] && o2 instanceof double[]) {
            return Arrays.equals((double[]) o1, (double[]) o2);
        }
        if (o1 instanceof float[] && o2 instanceof float[]) {
            return Arrays.equals((float[]) o1, (float[]) o2);
        }
        if (o1 instanceof int[] && o2 instanceof int[]) {
            return Arrays.equals((int[]) o1, (int[]) o2);
        }
        if (o1 instanceof long[] && o2 instanceof long[]) {
            return Arrays.equals((long[]) o1, (long[]) o2);
        }
        if (o1 instanceof short[] && o2 instanceof short[]) {
            return Arrays.equals((short[]) o1, (short[]) o2);
        }
        return false;
    }

    /**
     * 返回给定对象的哈希码； 通常是Object#hashCode()的值。 如果对象是数组，则此方法将委派给nullSafeHashCode中数组的任何nullSafeHashCode方法。 如果对象为null ，则此方法返回0
     * @see Object#hashCode()
     * @see #nullSafeHashCode(Object[])
     * @see #nullSafeHashCode(boolean[])
     * @see #nullSafeHashCode(byte[])
     * @see #nullSafeHashCode(char[])
     * @see #nullSafeHashCode(double[])
     * @see #nullSafeHashCode(float[])
     * @see #nullSafeHashCode(int[])
     * @see #nullSafeHashCode(long[])
     * @see #nullSafeHashCode(short[])
     */
    public static int nullSafeHashCode(Object obj) {
        if (obj == null) {
            return 0;
        }
        if (obj.getClass().isArray()) {
            if (obj instanceof Object[]) {
                return nullSafeHashCode((Object[]) obj);
            }
            if (obj instanceof boolean[]) {
                return nullSafeHashCode((boolean[]) obj);
            }
            if (obj instanceof byte[]) {
                return nullSafeHashCode((byte[]) obj);
            }
            if (obj instanceof char[]) {
                return nullSafeHashCode((char[]) obj);
            }
            if (obj instanceof double[]) {
                return nullSafeHashCode((double[]) obj);
            }
            if (obj instanceof float[]) {
                return nullSafeHashCode((float[]) obj);
            }
            if (obj instanceof int[]) {
                return nullSafeHashCode((int[]) obj);
            }
            if (obj instanceof long[]) {
                return nullSafeHashCode((long[]) obj);
            }
            if (obj instanceof short[]) {
                return nullSafeHashCode((short[]) obj);
            }
        }
        return obj.hashCode();
    }

    /**
     * 根据指定数组的内容返回哈希码。 如果array为null ，则此方法返回0。
     */
    public static int nullSafeHashCode(Object[] array) {
        if (array == null) {
            return 0;
        }
        int hash = INITIAL_HASH;
        for (Object element : array) {
            hash = MULTIPLIER * hash + nullSafeHashCode(element);
        }
        return hash;
    }

    /**
     * 根据指定数组的内容返回哈希码。 如果array为null ，则此方法返回0。
     */
    public static int nullSafeHashCode(boolean[] array) {
        if (array == null) {
            return 0;
        }
        int hash = INITIAL_HASH;
        for (boolean element : array) {
            hash = MULTIPLIER * hash + Boolean.hashCode(element);
        }
        return hash;
    }

    /**
     * 根据指定数组的内容返回哈希码。 如果array为null ，则此方法返回0。
     */
    public static int nullSafeHashCode(byte[] array) {
        if (array == null) {
            return 0;
        }
        int hash = INITIAL_HASH;
        for (byte element : array) {
            hash = MULTIPLIER * hash + element;
        }
        return hash;
    }

    /**
     * 根据指定数组的内容返回哈希码。 如果array为null ，则此方法返回0。
     */
    public static int nullSafeHashCode(char[] array) {
        if (array == null) {
            return 0;
        }
        int hash = INITIAL_HASH;
        for (char element : array) {
            hash = MULTIPLIER * hash + element;
        }
        return hash;
    }

    /**
     * 根据指定数组的内容返回哈希码。 如果array为null ，则此方法返回0。
     */
    public static int nullSafeHashCode(double[] array) {
        if (array == null) {
            return 0;
        }
        int hash = INITIAL_HASH;
        for (double element : array) {
            hash = MULTIPLIER * hash + Double.hashCode(element);
        }
        return hash;
    }

    /**
     * 根据指定数组的内容返回哈希码。 如果array为null ，则此方法返回0。
     */
    public static int nullSafeHashCode(float[] array) {
        if (array == null) {
            return 0;
        }
        int hash = INITIAL_HASH;
        for (float element : array) {
            hash = MULTIPLIER * hash + Float.hashCode(element);
        }
        return hash;
    }

    /**
     * 根据指定数组的内容返回哈希码。 如果array为null ，则此方法返回0。
     */
    public static int nullSafeHashCode(int[] array) {
        if (array == null) {
            return 0;
        }
        int hash = INITIAL_HASH;
        for (int element : array) {
            hash = MULTIPLIER * hash + element;
        }
        return hash;
    }

    /**
     * 根据指定数组的内容返回哈希码。 如果array为null ，则此方法返回0。
     */
    public static int nullSafeHashCode(long[] array) {
        if (array == null) {
            return 0;
        }
        int hash = INITIAL_HASH;
        for (long element : array) {
            hash = MULTIPLIER * hash + Long.hashCode(element);
        }
        return hash;
    }

    /**
     * 根据指定数组的内容返回哈希码。 如果array为null ，则此方法返回0。
     */
    public static int nullSafeHashCode(short[] array) {
        if (array == null) {
            return 0;
        }
        int hash = INITIAL_HASH;
        for (short element : array) {
            hash = MULTIPLIER * hash + element;
        }
        return hash;
    }

    //---------------------------------------------------------------------
    // 串输出的简便方法
    //---------------------------------------------------------------------

    /**
     * 返回对象整体身份的String表示形式。
     *
     * @param obj 对象（可以为null ）
     * @return 对象的身份，以String表示形式；如果对象为null则为空String
     */
    public static String identityToString(Object obj) {
        if (obj == null) {
            return EMPTY_STRING;
        }
        return obj.getClass().getName() + "@" + getIdentityHexString(obj);
    }

    /**
     * 返回对象身份哈希码的十六进制字符串形式。
     *
     * @param obj 对象
     * @return 对象的标识码（十六进制）
     */
    public static String getIdentityHexString(Object obj) {
        return Integer.toHexString(System.identityHashCode(obj));
    }

    /**
     * 如果obj不为null ，则返回基于内容的String表示形式； 否则返回一个空字符串。
     * 与nullSafeToString(Object)不同之处在于，它返回一个空字符串，而不是一个null值。
     *
     * @param obj 要为其建立显示字符串的对象
     * @return obj的显示字符串表示形式
     * @see #nullSafeToString(Object)
     */
    public static String getDisplayString(Object obj) {
        if (obj == null) {
            return EMPTY_STRING;
        }
        return nullSafeToString(obj);
    }

    /**
     * D确定给定对象的类名称。
     * 如果obj为null则返回"null"字符串。
     *
     * @param obj 自省对象（可以为null ）
     * @return 对应的类名
     */
    public static String nullSafeClassName(Object obj) {
        return (obj != null ? obj.getClass().getName() : NULL_STRING);
    }

    /**
     * 返回指定对象的字符串表示形式。
     * 如果是数组，则构建内容的字符串表示形式。 如果obj为null则返回"null"字符串。
     *
     * @param obj 为该对象构建String表示形式的对象
     * @return obj的字符串表示形式
     */
    public static String nullSafeToString(Object obj) {
        if (obj == null) {
            return NULL_STRING;
        }
        if (obj instanceof String) {
            return (String) obj;
        }
        if (obj instanceof Object[]) {
            return nullSafeToString((Object[]) obj);
        }
        if (obj instanceof boolean[]) {
            return nullSafeToString((boolean[]) obj);
        }
        if (obj instanceof byte[]) {
            return nullSafeToString((byte[]) obj);
        }
        if (obj instanceof char[]) {
            return nullSafeToString((char[]) obj);
        }
        if (obj instanceof double[]) {
            return nullSafeToString((double[]) obj);
        }
        if (obj instanceof float[]) {
            return nullSafeToString((float[]) obj);
        }
        if (obj instanceof int[]) {
            return nullSafeToString((int[]) obj);
        }
        if (obj instanceof long[]) {
            return nullSafeToString((long[]) obj);
        }
        if (obj instanceof short[]) {
            return nullSafeToString((short[]) obj);
        }
        String str = obj.toString();
        return (str != null ? str : EMPTY_STRING);
    }

    /**
     * 返回指定数组内容的String表示形式。
     * String表示形式由数组的元素列表组成，并用花括号（ "{}" ）括起来。 相邻元素由字符", " （逗号后跟空格）分隔。 如果array为null则返回"null"字符串。
     *
     * @param array 用于为其构建String表示形式的数组
     * @return array的字符串表示形式
     */
    public static String nullSafeToString(Object[] array) {
        if (array == null) {
            return NULL_STRING;
        }
        int length = array.length;
        if (length == 0) {
            return EMPTY_ARRAY;
        }
        StringJoiner stringJoiner = new StringJoiner(ARRAY_ELEMENT_SEPARATOR, ARRAY_START, ARRAY_END);
        for (Object o : array) {
            stringJoiner.add(String.valueOf(o));
        }
        return stringJoiner.toString();
    }

    /**
     * 返回指定数组内容的String表示形式。
     * String表示形式由数组的元素列表组成，并用花括号（ "{}" ）括起来。 相邻元素由字符", " （逗号后跟空格）分隔。 如果array为null则返回"null"字符串。
     *
     * @param array 用于为其构建String表示形式的数组
     * @return array的字符串表示形式
     */
    public static String nullSafeToString(boolean[] array) {
        if (array == null) {
            return NULL_STRING;
        }
        int length = array.length;
        if (length == 0) {
            return EMPTY_ARRAY;
        }
        StringJoiner stringJoiner = new StringJoiner(ARRAY_ELEMENT_SEPARATOR, ARRAY_START, ARRAY_END);
        for (boolean b : array) {
            stringJoiner.add(String.valueOf(b));
        }
        return stringJoiner.toString();
    }

    /**
     * 返回指定数组内容的String表示形式。
     * String表示形式由数组的元素列表组成，并用花括号（ "{}" ）括起来。 相邻元素由字符", " （逗号后跟空格）分隔。 如果array为null则返回"null"字符串。
     *
     * @param array 用于为其构建String表示形式的数组
     * @return array的字符串表示形式
     */
    public static String nullSafeToString(byte[] array) {
        if (array == null) {
            return NULL_STRING;
        }
        int length = array.length;
        if (length == 0) {
            return EMPTY_ARRAY;
        }
        StringJoiner stringJoiner = new StringJoiner(ARRAY_ELEMENT_SEPARATOR, ARRAY_START, ARRAY_END);
        for (byte b : array) {
            stringJoiner.add(String.valueOf(b));
        }
        return stringJoiner.toString();
    }

    /**
     * 返回指定数组内容的String表示形式。
     * String表示形式由数组的元素列表组成，并用花括号（ "{}" ）括起来。 相邻元素由字符", " （逗号后跟空格）分隔。 如果array为null则返回"null"字符串。
     *
     * @param array 用于为其构建String表示形式的数组
     * @return array的字符串表示形式
     */
    public static String nullSafeToString(char[] array) {
        if (array == null) {
            return NULL_STRING;
        }
        int length = array.length;
        if (length == 0) {
            return EMPTY_ARRAY;
        }
        StringJoiner stringJoiner = new StringJoiner(ARRAY_ELEMENT_SEPARATOR, ARRAY_START, ARRAY_END);
        for (char c : array) {
            stringJoiner.add('\'' + String.valueOf(c) + '\'');
        }
        return stringJoiner.toString();
    }

    /**
     * 返回指定数组内容的String表示形式。
     * String表示形式由数组的元素列表组成，并用花括号（ "{}" ）括起来。 相邻元素由字符", " （逗号后跟空格）分隔。 如果array为null则返回"null"字符串。
     *
     * @param array 用于为其构建String表示形式的数组
     * @return array的字符串表示形式
     */
    public static String nullSafeToString(double[] array) {
        if (array == null) {
            return NULL_STRING;
        }
        int length = array.length;
        if (length == 0) {
            return EMPTY_ARRAY;
        }
        StringJoiner stringJoiner = new StringJoiner(ARRAY_ELEMENT_SEPARATOR, ARRAY_START, ARRAY_END);
        for (double d : array) {
            stringJoiner.add(String.valueOf(d));
        }
        return stringJoiner.toString();
    }

    /**
     * 返回指定数组内容的String表示形式。
     * String表示形式由数组的元素列表组成，并用花括号（ "{}" ）括起来。 相邻元素由字符", " （逗号后跟空格）分隔。 如果array为null则返回"null"字符串。
     *
     * @param array 用于为其构建String表示形式的数组
     * @return array的字符串表示形式
     */
    public static String nullSafeToString(float[] array) {
        if (array == null) {
            return NULL_STRING;
        }
        int length = array.length;
        if (length == 0) {
            return EMPTY_ARRAY;
        }
        StringJoiner stringJoiner = new StringJoiner(ARRAY_ELEMENT_SEPARATOR, ARRAY_START, ARRAY_END);
        for (float f : array) {
            stringJoiner.add(String.valueOf(f));
        }
        return stringJoiner.toString();
    }

    /**
     * 返回指定数组内容的String表示形式。
     * String表示形式由数组的元素列表组成，并用花括号（ "{}" ）括起来。 相邻元素由字符", " （逗号后跟空格）分隔。 如果array为null则返回"null"字符串。
     *
     * @param array 用于为其构建String表示形式的数组
     * @return array的字符串表示形式
     */
    public static String nullSafeToString(int[] array) {
        if (array == null) {
            return NULL_STRING;
        }
        int length = array.length;
        if (length == 0) {
            return EMPTY_ARRAY;
        }
        StringJoiner stringJoiner = new StringJoiner(ARRAY_ELEMENT_SEPARATOR, ARRAY_START, ARRAY_END);
        for (int i : array) {
            stringJoiner.add(String.valueOf(i));
        }
        return stringJoiner.toString();
    }

    /**
     * 返回指定数组内容的String表示形式。
     * String表示形式由数组的元素列表组成，并用花括号（ "{}" ）括起来。 相邻元素由字符", " （逗号后跟空格）分隔。 如果array为null则返回"null"字符串。
     *
     * @param array 用于为其构建String表示形式的数组
     * @return array的字符串表示形式
     */
    public static String nullSafeToString(long[] array) {
        if (array == null) {
            return NULL_STRING;
        }
        int length = array.length;
        if (length == 0) {
            return EMPTY_ARRAY;
        }
        StringJoiner stringJoiner = new StringJoiner(ARRAY_ELEMENT_SEPARATOR, ARRAY_START, ARRAY_END);
        for (long l : array) {
            stringJoiner.add(String.valueOf(l));
        }
        return stringJoiner.toString();
    }

    /**
     * 返回指定数组内容的String表示形式。
     * String表示形式由数组的元素列表组成，并用花括号（ "{}" ）括起来。 相邻元素由字符", " （逗号后跟空格）分隔。 如果array为null则返回"null"字符串。
     *
     * @param array 用于为其构建String表示形式的数组
     * @return array的字符串表示形式
     */
    public static String nullSafeToString(short[] array) {
        if (array == null) {
            return NULL_STRING;
        }
        int length = array.length;
        if (length == 0) {
            return EMPTY_ARRAY;
        }
        StringJoiner stringJoiner = new StringJoiner(ARRAY_ELEMENT_SEPARATOR, ARRAY_START, ARRAY_END);
        for (short s : array) {
            stringJoiner.add(String.valueOf(s));
        }
        return stringJoiner.toString();
    }

}

