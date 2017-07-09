package com.taotao.web.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.bean.HttpResult;
import com.taotao.common.service.ApiService;
import com.taotao.web.bean.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by Ellen on 2017/7/9.
 */
@Service
public class OrderService {

    @Autowired
    private ApiService apiService;

    @Value("${ORDER_TAOTAO_URL}")
    private String ORDER_TAOTAO_URL;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public String sumitOrder(Order order) {
        String url = ORDER_TAOTAO_URL + "/order/create";

        try {
            HttpResult httpResult = apiService.doPostJson(url, OBJECT_MAPPER.writeValueAsString(order));
            if (httpResult.getCode() == 200) {
                JsonNode jsonNode = OBJECT_MAPPER.readTree(httpResult.getData());
                if (jsonNode.get("status").intValue() == 200) {
                    //提交成功
                    return jsonNode.get("data").asText();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
