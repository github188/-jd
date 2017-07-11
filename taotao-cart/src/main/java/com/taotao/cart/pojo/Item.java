package com.taotao.cart.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.StringUtils;

public class Item extends com.taotao.manager.pojo.Item{

    public String[] getImages() {
        return StringUtils.split(getImage(), ',');
    }


}
