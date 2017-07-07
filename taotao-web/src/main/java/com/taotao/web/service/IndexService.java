package com.taotao.web.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.taotao.common.bean.EasyUIResult;
import com.taotao.common.service.ApiService;
import com.taotao.manager.pojo.Content;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * Created by Ellen on 2017/7/7.
 */
@Service
public class IndexService {

    @Value("${MANAGER_TAOTAO_URL}")
    private String MANAGER_TAOTAO_URL;
    @Value("${INDEX_AD1_URL}")
    private String INDEX_AD1_URL;

    @Autowired
    private ApiService apiService;

    private static final ObjectMapper MAPPER = new ObjectMapper();

//    public String getIndexAd1() {
//        String url = MANAGER_TAOTAO_URL + INDEX_AD1_URL;
//        try {
//            String json = apiService.doGet(url);
//            //解析json
//            JsonNode jsonNode = MAPPER.readTree(json);
//            ArrayNode arrayNode = (ArrayNode) jsonNode.get("rows");
//
//            List<Map<String, Object>> result = new ArrayList<>();
//            for (JsonNode node : arrayNode) {
//                Map<String, Object> map = new LinkedHashMap<>();
//                map.put("srcB", node.get("pic").asText());
//                map.put("height", 240);
//                map.put("alt", node.get("title").asText());
//                map.put("width", 670);
//                map.put("src", node.get("pic").asText());
//                map.put("widthB", 550);
//                map.put("href", node.get("url").asText());
//                map.put("heightB", 240);
//                result.add(map);
//            }
//            return MAPPER.writeValueAsString(result);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    public String getIndexAd1() {
        String url = MANAGER_TAOTAO_URL + INDEX_AD1_URL;
        try {
            String json = apiService.doGet(url);
            //解析json
            EasyUIResult easyUIResult = EasyUIResult.formatToList(json, Content.class);
            List<Content> contents = (List<Content>) easyUIResult.getRows();
            List<Map<String, Object>> result = new ArrayList<>();

            for (Content content : contents) {
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("srcB", content.getPic());
                map.put("height", 240);
                map.put("alt", content.getTitle());
                map.put("width", 670);
                map.put("src", content.getPic());
                map.put("widthB", 550);
                map.put("href", content.getUrl());
                map.put("heightB", 240);
                result.add(map);
            }
            return MAPPER.writeValueAsString(result);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
