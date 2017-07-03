package com.taotao.manager.controller;

import com.taotao.manager.pojo.ItemDesc;
import com.taotao.manager.service.ItemDescService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Ellen on 2017/7/3.
 */
@Controller
@RequestMapping("item/desc")
public class ItemDescController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemDescController.class);

    @Autowired
    private ItemDescService itemDescService;


    /**
     * 查询itemDesc用于回显
     *
     * @param itemId
     * @return
     */
    @RequestMapping(value = "{itemId}", method = RequestMethod.GET)
    public ResponseEntity<ItemDesc> queryDescById(@PathVariable("itemId") long itemId) {
        try {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("开启itemDesc查询：itemDescId = {}", itemId);
            }

            ItemDesc itemDesc = itemDescService.queryById(itemId);
            if (itemDesc == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("成功itemDesc查询：itemDesc = {}", itemDesc);
            }
            return ResponseEntity.ok(itemDesc);
        } catch (Exception e) {
            LOGGER.error("itemDesc查询失败：", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
