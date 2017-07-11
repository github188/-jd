package com.taotao.web.mq.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.service.RedisService;
import com.taotao.web.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * Created by Ellen on 2017/7/11.
 */
public class ItemMqHandler {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    private RedisService redisService;

    public void execute(String msg) {
        try {
            JsonNode jsonNode = MAPPER.readTree(msg);
            long itemId = jsonNode.get("itemId").asLong();

            //删除redis中的缓存
            String key = ItemService.REDIS_KEY + itemId;
            redisService.delete(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
