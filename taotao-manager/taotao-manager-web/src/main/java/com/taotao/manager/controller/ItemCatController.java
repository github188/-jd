package com.taotao.manager.controller;

import com.taotao.common.bean.ItemCatResult;
import com.taotao.manager.pojo.ItemCat;
import com.taotao.manager.service.ItemCatService;
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
@RequestMapping("/item/cat")
public class ItemCatController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemCatController.class);

    @Autowired
    private ItemCatService itemCatService;

    /**
     * 查询商品类目
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<ItemCat>> queryItemCat(@RequestParam(value = "id", defaultValue = "0") long id) {
        try {
            ItemCat itemCat = new ItemCat();
            itemCat.setParentId(id);
            List<ItemCat> list = itemCatService.queryListByWhere(itemCat);
            if (list == null || list.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * 按照前台系统的结构返回商品数据
     *
     * @return
     */
    @RequestMapping(value = "all", method = RequestMethod.GET)
    public ResponseEntity<ItemCatResult> queryCatAll() {
        try {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("查询所有商品");
            }

            ItemCatResult catResult = itemCatService.queryAllToTree();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("查询所有商品成功", catResult.getItemCats().size());
            }

            return ResponseEntity.ok(catResult);
        } catch (Exception e) {
            LOGGER.error("查询商品失败");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
