package com.taotao.web.bean;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by Ellen on 2017/7/7.
 */
public class Item extends com.taotao.manager.pojo.Item {
    public String[] getImages() {
        return StringUtils.split(getImage(), ',');
    }
}
