package com.taotao.web.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.taotao.common.service.ApiService;
import com.taotao.common.service.RedisService;
import com.taotao.manager.pojo.ItemDesc;
import com.taotao.manager.pojo.ItemParam;
import com.taotao.manager.pojo.ItemParamItem;
import com.taotao.web.bean.Item;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by Ellen on 2017/7/7.
 */
@Service
public class ItemService {

    @Autowired
    private ApiService apiService;

    @Value("${MANAGER_TAOTAO_URL}")
    private String MANAGER_TAOTAO_URL;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private RedisService redisService;

    public static final String REDIS_KEY = "TAOTAO_WEB_ITEM_DETAIL";
    private static final int REDIS_TIME = 60 * 60 * 24;

    public Item queryById(long itemId) {
        String key = REDIS_KEY + itemId;
        try {
            String cacheData = redisService.get(key);
            if (cacheData != null) {
                return OBJECT_MAPPER.readValue(cacheData, Item.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String url = MANAGER_TAOTAO_URL + "/rest/item/" + itemId;
        try {
            String jsonData = apiService.doGet(url);
            if (StringUtils.isNotEmpty(jsonData)) {
                try {
                    redisService.setExpire(key, jsonData, REDIS_TIME);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return OBJECT_MAPPER.readValue(jsonData, Item.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ItemDesc queryDescByItemId(long itemId) {
        String url = MANAGER_TAOTAO_URL + "/rest/item/desc/" + itemId;
        try {
            String jsonData = apiService.doGet(url);
            if (StringUtils.isNotEmpty(jsonData)) {
                return OBJECT_MAPPER.readValue(jsonData, ItemDesc.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String queryItemParamByItemId(long itemId) {
        String url = MANAGER_TAOTAO_URL + "/rest/item/param/item/" + itemId;
        try {
            String jsonData = apiService.doGet(url);
            if (StringUtils.isNotEmpty(jsonData)) {
                ItemParamItem itemParam = OBJECT_MAPPER.readValue(jsonData, ItemParamItem.class);
                String paramData = itemParam.getParamData();

                ArrayNode arrayNode = (ArrayNode) OBJECT_MAPPER.readTree(paramData);
                StringBuilder sb = new StringBuilder();
                sb.append("<table cellpadding=\"0\" cellspacing=\"1\" width=\"100%\" border=\"0\" class=\"Ptable\"><tbody>");
                for (JsonNode param : arrayNode) {
                    sb.append("<tr><th class=\"tdTitle\" colspan=\"2\">" + param.get("group").asText()
                            + "</th></tr>");
                    ArrayNode params = (ArrayNode) param.get("params");
                    for (JsonNode p : params) {
                        sb.append("<tr><td class=\"tdTitle\">" + p.get("k").asText() + "</td><td>"
                                + p.get("v").asText() + "</td></tr>");
                    }
                }
                sb.append("</tbody></table>");

                return sb.toString();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
