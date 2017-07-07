package com.taotao.common.service;

/**
 * Created by Ellen on 2017/7/7.
 */
public interface Function<T, E> {

    public T callback(E e);
}
