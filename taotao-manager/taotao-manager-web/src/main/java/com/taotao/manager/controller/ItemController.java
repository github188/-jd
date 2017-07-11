package com.taotao.manager.controller;

import com.github.pagehelper.PageInfo;
import com.taotao.common.bean.EasyUIResult;
import com.taotao.manager.pojo.Item;
import com.taotao.manager.pojo.ItemDesc;
import com.taotao.manager.pojo.ItemParam;
import com.taotao.manager.pojo.ItemParamItem;
import com.taotao.manager.service.ItemService;
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
                                   @RequestParam(value = "desc") String desc,
                                   @RequestParam("itemParams") String itemParams) {
        try {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("添加商品： item = {}", item);
            }

            ItemDesc itemDesc = new ItemDesc();
            itemDesc.setItemDesc(desc);

            itemService.saveItem(item, itemDesc, itemParams);

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
     *
     * @param ids
     * @return
     */
    @RequestMapping(value = "{tock}", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateItems(@RequestParam("ids") List<Object> ids,
                                            @PathVariable(value = "tock") String tock) {
        try {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("刷新商品： ids = {}", ids);
            }
            if (tock == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
            int status = 1;
            if (tock.equals("delete")) {
                status = 3;
            } else if (tock.equals("instock")) {
                status = 2;
            }
            itemService.updateItems(ids, status);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("刷新商品成功： itemId = {}", ids);
            }

            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            LOGGER.error("刷新商品失败： item = ", ids);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 更新商品
     *
     * @param item
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Void> updateItem(Item item, @RequestParam("desc") String desc,
                                           @RequestParam("itemParams") String itemParams,
                                           @RequestParam("itemParamId") Long itemParamId) {
        try {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("开启更新商品: item = {}", item);
            }
            ItemParamItem itemParamItem = null;
            if (itemParamId != null) {
                itemParamItem = new ItemParamItem();
                itemParamItem.setId(itemParamId);
                itemParamItem.setParamData(itemParams);
            }
            itemService.updateItem(item, desc, itemParamItem);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("更新商品成功，item = {},itemParams ={}", item, itemParams);
            }
            return ResponseEntity.ok(null);
        } catch (Exception e) {
            LOGGER.error("更新商品失败: item = {}", item);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 根据商品ID查询商品
     *
     * @param itemId
     * @return
     */
    @RequestMapping(value = "{itemId}", method = RequestMethod.GET)
    public ResponseEntity<Item> queryById(@PathVariable("itemId") long itemId) {
        try {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("查询商品: itemId = {}", itemId);
            }
            Item item = itemService.queryById(itemId);
            if (item == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("查询商品成功，item = {}", item);
            }
            return ResponseEntity.ok(item);
        } catch (Exception e) {
            LOGGER.error("更新商品失败: itemId = {}", itemId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
