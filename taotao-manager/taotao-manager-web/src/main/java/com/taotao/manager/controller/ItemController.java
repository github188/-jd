package com.taotao.manager.controller;

import com.github.pagehelper.PageInfo;
import com.taotao.common.bean.EasyUIResult;
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

import java.util.List;

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
     *
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

    /**
     * 查询列表
     *
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<EasyUIResult> queryItemList(@RequestParam(value = "page", defaultValue = "1") int page,
                                                      @RequestParam(value = "rows", defaultValue = "30") int rows) {
        try {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("查询所有的商品：page = {}, rows = {}", page, rows);
            }

            PageInfo<Item> pageInfo = itemService.queryItemList(page, rows, null);

            if (pageInfo == null && pageInfo.getTotal() > 0) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("查询商品为空");
                }
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("查询到的商品：item = {}", pageInfo.getList());
            }
            return ResponseEntity.ok(new EasyUIResult(pageInfo.getTotal(), pageInfo.getList()));
        } catch (Exception e) {
            LOGGER.error("查询商品出错", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * 逻辑删除数据
     * @param ids
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<Void> updateItems(@RequestParam("ids") List<Object> ids) {
        try {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("修改商品： ids = {}", ids);
            }

            itemService.updateItems(ids);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("修改商品成功： itemId = {}", ids);
            }
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            LOGGER.error("修改商品失败： item = ", ids);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
