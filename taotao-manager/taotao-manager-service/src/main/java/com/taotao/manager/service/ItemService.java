package com.taotao.manager.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.abel533.entity.Example;
import com.github.abel533.mapper.Mapper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.service.ApiService;
import com.taotao.manager.mapper.ItemMapper;
import com.taotao.manager.pojo.Item;
import com.taotao.manager.pojo.ItemDesc;
import com.taotao.manager.pojo.ItemParamItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * Created by Ellen on 2017/7/2.
 */
@Service
public class ItemService extends BaseService<Item> {

    @Autowired
    private ItemDescService itemDescService;

    @Autowired
    private ItemParamItemService itemParamItemService;

    @Autowired
    private ApiService apiService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Value("${WEB_TAOTAO_URL}")
    private String URL;

    /**
     * 在一个事务中添加item项和itemdesc项
     *
     * @param item
     * @param desc
     */
    public void saveItem(Item item, ItemDesc desc, String itemParams) {
        item.setStatus(1);
        item.setId(null);
        saveSelective(item);
        desc.setItemId(item.getId());
        itemDescService.save(desc);

        if (StringUtils.isNotEmpty(itemParams)) {
            ItemParamItem itemParamItem = new ItemParamItem();
            itemParamItem.setItemId(item.getId());
            itemParamItem.setParamData(itemParams);
            itemParamItemService.save(itemParamItem);
        }
        sendMag(item.getId(), "insert");
    }

    public PageInfo<Item> queryItemList(int page, int rows, Object o) {
        Example example = new Example(Item.class);
        example.setOrderByClause("status ASC");
        return queryPageListByExample(page, rows, example);
    }

    public void updateItems(List<Object> ids, int status) {
        Mapper<Item> mapper = getMapper();
        Example example = new Example(Item.class);
        example.createCriteria().andIn("id", ids);
        Item item = new Item();
        item.setStatus(status);
        mapper.updateByExampleSelective(item, example);
        sendMag(item.getId(), "status");
    }

    public void updateItem(Item item, String desc, ItemParamItem itemParamItem) {
        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemDesc(desc);
        itemDesc.setItemId(item.getId());
        itemDesc.setUpdated(new Date());

        updateSelective(item);
        itemDescService.updateSelective(itemDesc);

        if (itemParamItem != null) {
            itemParamItemService.updateSelective(itemParamItem);
        }

        //通知其他系统该商品已经更新
//        String url = URL + "/item/cache/" + item.getId() + ".html";
//        try {
//            apiService.doPost(url);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            return;
//        }

        sendMag(item.getId(), "update");

    }

    private void sendMag(long itemId, String type) {
        try {
            Map<String, Object> msg = new HashMap<>();
            msg.put("itemId", itemId);
            msg.put("type", "update");
            msg.put("date", System.currentTimeMillis());
            rabbitTemplate.convertAndSend("item.update", MAPPER.writeValueAsString(msg));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
