package com.taotao.search.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.service.ApiService;
import com.taotao.search.bean.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by Ellen on 2017/7/11.
 */
@Service
public class ItemService {

    @Autowired
    private ApiService apiService;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public Item queryItemBuId(long id) {
        try {
            String url = "http://manager.taotao.com/rest/item/" + id;
            String jsonData = apiService.doGet(url);
            if (jsonData != null) {
                return MAPPER.readValue(jsonData, Item.class);
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
