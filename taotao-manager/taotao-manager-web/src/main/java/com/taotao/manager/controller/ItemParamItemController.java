package com.taotao.manager.controller;

import com.github.abel533.entity.Example;
import com.github.pagehelper.PageInfo;
import com.taotao.common.bean.EasyUIResult;
import com.taotao.manager.pojo.ItemParam;
import com.taotao.manager.pojo.ItemParamItem;
import com.taotao.manager.service.ItemParamItemService;
import com.taotao.manager.service.ItemParamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by Ellen on 2017/7/3.
 */
@Controller
@RequestMapping("item/param/item")
public class ItemParamItemController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemParamItemController.class);

    @Autowired
    private ItemParamItemService itemParamItemService;

    /**
     * 根据商品ID查找模板
     *
     * @param itemId
     * @return
     */
    @RequestMapping(value = "{itemId}", method = RequestMethod.GET)
    public ResponseEntity<ItemParamItem> queryByItemId(@PathVariable("itemId") long itemId) {
        try {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("开启模板查询： itemId = {}", itemId);
            }

            ItemParamItem item = new ItemParamItem();
            item.setItemId(itemId);
            ItemParamItem itemParamItem = itemParamItemService.queryOne(item);
            if (itemParamItem == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("模板查询成功： itemParamItem = {}", itemParamItem);
            }
            return ResponseEntity.ok(itemParamItem);
        } catch (Exception e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("模板查询失败： itemId = {}", itemId);
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
