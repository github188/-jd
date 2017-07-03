package com.taotao.manager.controller;

import com.taotao.manager.pojo.Item;
import com.taotao.manager.pojo.ItemDesc;
import com.taotao.manager.service.ItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by Ellen on 2017/7/2.
 */
@Controller
@RequestMapping("item")
public class ItemController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemController.class);

    @Autowired
    private ItemService itemService;

    /**
     * 保存一个item
     * @param item
     * @param desc
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity saveItem(Item item,
                                   @RequestParam(value = "desc") String desc) {
        try {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("添加商品： item = {}", item);
            }

            ItemDesc itemDesc = new ItemDesc();
            itemDesc.setItemDesc(desc);

            itemService.saveItem(item, itemDesc);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("添加商品成功： itemId = {}", item.getId());
            }
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            LOGGER.error("添加商品失败： item = ", item);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
