package com.taotao.web.controller;

import com.taotao.manager.pojo.ItemDesc;
import com.taotao.web.bean.Item;
import com.taotao.web.service.ItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Ellen on 2017/7/7.
 */
@Controller
@RequestMapping("item")
public class ItemController {

    @Autowired
    private ItemService itemService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemController.class);

    @RequestMapping(value = "{itemId}", method = RequestMethod.GET)
    public ModelAndView showItemDetail(@PathVariable("itemId") long itemId) {
        try {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("商品查询，itenId = {}", itemId);
            }

            ModelAndView mv = new ModelAndView("item");
            Item item = itemService.queryById(itemId);
            mv.addObject("item", item);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("商品查询, item = {}", item);
            }
            ItemDesc itemDesc = itemService.queryDescByItemId(itemId);
            mv.addObject("itemDesc", itemDesc);

            String itemParam = itemService.queryItemParamByItemId(itemId);
            mv.addObject("itemParam", itemParam);
            return mv;
        } catch (Exception e) {
            LOGGER.error("商品查询失败", e.getMessage());
            return null;
        }
    }
}
