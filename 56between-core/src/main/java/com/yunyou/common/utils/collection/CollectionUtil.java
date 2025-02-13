/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.yunyou.common.utils.collection;

import com.yunyou.common.utils.Reflections;
import com.yunyou.common.utils.collection.type.Pair;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Ordering;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 通用Collection的工具集
 * <p>
 * 关于List, Map, Queue, Set的特殊工具集，另见特定的Utils.
 * <p>
 * 另JDK中缺少ComparableComparator和NullComparator，直到JDK8才补上。
 * <p>
 * 因此平时请使用guava的Ordering,fluentable的API更好用，可以链式设置nullFirst，nullLast,reverse
 *
 * @see Ordering
 */
public class CollectionUtil {

    /**
     * 判断是否为空.
     */
    public static boolean isEmpty(Collection<?> collection) {
        return (collection == null) || collection.isEmpty();
    }

    /**
     * 判断是否不为空.
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return (collection != null) && !(collection.isEmpty());
    }

    /**
     * 取得Collection的第一个元素，如果collection为空返回null.
     */
    public static <T> T getFirst(Collection<T> collection) {
        if (isEmpty(collection)) {
            return null;
        }
        if (collection instanceof List) {
            return ((List<T>) collection).get(0);
        }
        return collection.iterator().next();
    }

    /**
     * 获取Collection的最后一个元素，如果collection为空返回null.
     */
    public static <T> T getLast(Collection<T> collection) {
        if (isEmpty(collection)) {
            return null;
        }

        // 当类型List时，直接取得最后一个元素.
        if (collection instanceof List) {
            List<T> list = (List<T>) collection;
            return list.get(list.size() - 1);
        }

        return Iterators.getLast(collection.iterator());
    }

    /**
     * 两个集合中的所有元素按顺序相等.
     */
    public static boolean elementsEqual(Iterable<?> iterable1, Iterable<?> iterable2) {
        return Iterables.elementsEqual(iterable1, iterable2);
    }

    /**
     * 返回无序集合中的最小值，使用元素默认排序
     */
    public static <T extends Object & Comparable<? super T>> T min(Collection<? extends T> coll) {
        return Collections.min(coll);
    }

    /**
     * 返回无序集合中的最小值
     */
    public static <T> T min(Collection<? extends T> coll, Comparator<? super T> comp) {
        return Collections.min(coll, comp);
    }

    /**
     * 返回无序集合中的最大值，使用元素默认排序
     */
    public static <T extends Object & Comparable<? super T>> T max(Collection<? extends T> coll) {
        return Collections.max(coll);
    }

    /**
     * 返回无序集合中的最大值
     */
    public static <T> T max(Collection<? extends T> coll, Comparator<? super T> comp) {
        return Collections.max(coll, comp);
    }

    /**
     * 返回无序集合中的最小值和最大值，使用元素默认排序
     */
    public static <T extends Object & Comparable<? super T>> Pair<T, T> minAndMax(Collection<? extends T> coll) {
        Iterator<? extends T> i = coll.iterator();
        T minCandidate = i.next();
        T maxCandidate = minCandidate;

        while (i.hasNext()) {
            T next = i.next();
            if (next.compareTo(minCandidate) < 0) {
                minCandidate = next;
            } else if (next.compareTo(maxCandidate) > 0) {
                maxCandidate = next;
            }
        }
        return Pair.of(minCandidate, maxCandidate);
    }

    /**
     * 返回无序集合中的最小值和最大值
     */
    public static <T> Pair<T, T> minAndMax(Collection<? extends T> coll, Comparator<? super T> comp) {
        Iterator<? extends T> i = coll.iterator();
        T minCandidate = i.next();
        T maxCandidate = minCandidate;

        while (i.hasNext()) {
            T next = i.next();
            if (comp.compare(next, minCandidate) < 0) {
                minCandidate = next;
            } else if (comp.compare(next, maxCandidate) > 0) {
                maxCandidate = next;
            }
        }
        return Pair.of(minCandidate, maxCandidate);
    }

    /**
     * 排序最高的N个对象, guava已优化.
     */
    public static <T extends Comparable> List<T> topN(Iterable<T> coll, int n) {
        return Ordering.natural().greatestOf(coll, n);
    }

    /**
     * 排序最高的N个对象, guava已优化.
     */
    public static <T extends Comparable> List<T> topN(Iterable<T> coll, int n, Comparator<? super T> comp) {
        return Ordering.from(comp).greatestOf(coll, n);
    }

    /**
     * 排序最低的N个对象, guava已优化.
     */
    public static <T extends Comparable> List<T> bottomN(Iterable<T> coll, int n) {
        return Ordering.natural().leastOf(coll, n);
    }

    /**
     * 排序最低的N个对象, guava已优化.
     */
    public static <T extends Comparable> List<T> bottomN(Iterable<T> coll, int n, Comparator<? super T> comp) {
        return Ordering.from(comp).leastOf(coll, n);
    }

    public static <T> T mapToJavaBean(Map<String, Object> map, Class<T> clazz) {
        T obj = null;
        try {
            // new 出一个对象
            obj = clazz.newInstance();
            // 获取javaBean的BeanInfo对象
            BeanInfo beanInfo = Introspector.getBeanInfo(clazz, Object.class);
            // 获取属性描述器
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                // 获取属性名
                String key = propertyDescriptor.getName();
                Object value = map.get(key);
                Method writeMethod = propertyDescriptor.getWriteMethod();
                if (map.containsKey(key)) {
                    // 解决 argument type mismatch 的问题，转换成对应的javaBean属性类型
                    String typeName = propertyDescriptor.getPropertyType().getTypeName();
                    if ("java.lang.Integer".equals(typeName)) {
                        value = null != value ? Integer.parseInt(value.toString()) : null;
                    } else if ("java.lang.Double".equals(typeName)) {
                        value = null != value ? Double.parseDouble(value.toString()) : null;
                    } else if ("java.lang.Long".equals(typeName)) {
                        value = null != value ? Long.parseLong(value.toString()) : null;
                    } else if ("java.util.Date".equals(typeName)) {
                        value = null != value ? new SimpleDateFormat("yyyy-MM-dd").parse(value.toString()) : null;
                    }
                    // 通过反射来调用javaBean定义的setName()方法
                    writeMethod.invoke(obj, value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static Object instance(Class clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 提取集合中的对象的一个属性(通过Getter函数), 组合成List.
     *
     * @param collection   来源集合.
     * @param propertyName 要提取的属性名.
     */
    @SuppressWarnings("unchecked")
    public static List extractToList(final Collection collection, final String propertyName) {
        List list = new ArrayList(collection.size());

        try {
            for (Object obj : collection) {
                list.add(PropertyUtils.getProperty(obj, propertyName));
            }
        } catch (Exception e) {
            throw Reflections.convertReflectionExceptionToUnchecked(e);
        }

        return list;
    }

    /**
     * 提取集合中的对象的一个属性(通过Getter函数), 组合成由分割符分隔的字符串.
     *
     * @param collection   来源集合.
     * @param propertyName 要提取的属性名.
     * @param separator    分隔符.
     */
    public static String extractToString(final Collection collection, final String propertyName, final String separator) {
        List list = extractToList(collection, propertyName);
        return StringUtils.join(list, separator);
    }

}
