package com.taotao.manager.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.abel533.mapper.Mapper;
import com.taotao.common.bean.EasyUIResult;
import com.taotao.common.bean.ItemCatData;
import com.taotao.common.bean.ItemCatResult;
import com.taotao.common.service.RedisService;
import com.taotao.manager.mapper.ItemCatMapper;
import com.taotao.manager.pojo.ItemCat;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ellen on 2017/7/2.
 */
@Service
public class ItemCatService extends BaseService<ItemCat> {

    @Autowired
    private RedisService redisService;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final int REDISTEMP = 60 * 24 * 30 * 3;
    private static final String REDISKEY = "TAOTAO_MANAGER_ITEM_CAT_ALL";
    private static final Logger LOGGER = LoggerFactory.getLogger(ItemCatService.class);

    /**
     * 全部查询，并且生成树状结构
     *
     * @return
     */
    public ItemCatResult queryAllToTree() {
        //从redis中获取数据，如果命中返回，没命中就继续查询

        try {
            String key = REDISKEY;
            String cacheData = redisService.get(key);
            if (StringUtils.isNotEmpty(cacheData)) {
                return objectMapper.readValue(cacheData, ItemCatResult.class);
            }
        } catch (IOException e) {
            LOGGER.error("从REDIS中反序列化数据失败");
        }

        ItemCatResult result = new ItemCatResult();
        // 全部查出，并且在内存中生成树形结构
        List<ItemCat> cats = queryAll();

        // 转为map存储，key为父节点ID，value为数据集合
        Map<Long, List<ItemCat>> itemCatMap = new HashMap<>();
        for (ItemCat itemCat : cats) {
            if (!itemCatMap.containsKey(itemCat.getParentId())) {
                itemCatMap.put(itemCat.getParentId(), new ArrayList<>());
            }
            itemCatMap.get(itemCat.getParentId()).add(itemCat);
        }

        // 封装一级对象
        List<ItemCat> itemCatList1 = itemCatMap.get(0L);
        for (ItemCat itemCat : itemCatList1) {
            ItemCatData itemCatData = new ItemCatData();
            itemCatData.setUrl("/products/" + itemCat.getId() + ".html");
            itemCatData.setNname("<a href='" + itemCatData.getUrl() + "'>" + itemCat.getName() + "</a>");
            result.getItemCats().add(itemCatData);
            if (!itemCat.getIsParent()) {
                continue;
            }

            // 封装二级对象
            List<ItemCat> itemCatList2 = itemCatMap.get(itemCat.getId());
            List<ItemCatData> itemCatData2 = new ArrayList<>();
            itemCatData.setItems(itemCatData2);
            for (ItemCat itemCat2 : itemCatList2) {
                ItemCatData id2 = new ItemCatData();
                id2.setNname(itemCat2.getName());
                id2.setUrl("/products/" + itemCat2.getId() + ".html");
                itemCatData2.add(id2);
                if (itemCat2.getIsParent()) {
                    // 封装三级对象
                    List<ItemCat> itemCatList3 = itemCatMap.get(itemCat2.getId());
                    List<String> itemCatData3 = new ArrayList<>();
                    id2.setItems(itemCatData3);
                    for (ItemCat itemCat3 : itemCatList3) {
                        itemCatData3.add("/products/" + itemCat3.getId() + ".html|" + itemCat3.getName());
                    }
                }
            }
            if (result.getItemCats().size() >= 14) {
                break;
            }
        }

        try {
            redisService.setExpire(REDISKEY, objectMapper.writeValueAsString(result), REDISTEMP);
        } catch (Exception e) {
            LOGGER.error("REDIS存储数据失败", e.getMessage());
        }

        return result;
    }
}
