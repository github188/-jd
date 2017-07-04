package com.taotao.manager.controller;

import com.github.abel533.entity.Example;
import com.github.pagehelper.PageInfo;
import com.taotao.common.bean.EasyUIResult;
import com.taotao.manager.pojo.ItemParam;
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

import java.util.List;

/**
 * Created by Ellen on 2017/7/3.
 */
@Controller
@RequestMapping("item/param")
public class ItemParamController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemParamController.class);

    @Autowired
    private ItemParamService itemParamService;

    /**
     * 根据类目ID查找模板
     *
     * @param itemCatId
     * @return
     */
    @RequestMapping(value = "{itemCatId}", method = RequestMethod.GET)
    public ResponseEntity<ItemParam> queryByItemCatId(@PathVariable("itemCatId") long itemCatId) {
        try {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("开启查询： itemCatId = {}", itemCatId);
            }

            ItemParam t = new ItemParam();
            t.setItemCatId(itemCatId);
            ItemParam itemParam = itemParamService.queryOne(t);
            if (itemParam == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("查询成功： itemParam = {}", itemParam);
            }
            return ResponseEntity.ok(itemParam);
        } catch (Exception e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("开启失败： itemCatId = {}", itemCatId);
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * 新增模板
     *
     * @param itemCatId
     * @return
     */
    @RequestMapping(value = "{itemCatId}", method = RequestMethod.POST)
    public ResponseEntity<Void> saveByItemCatId(@RequestParam("paramData") String paramData,
                                                @PathVariable("itemCatId") long itemCatId) {
        try {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("新增模板： paramDate = {}", paramData);
            }

            ItemParam t = new ItemParam();
            t.setItemCatId(itemCatId);
            t.setParamData(paramData);
            itemParamService.save(t);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("新增模板成功： paramDate = {}", paramData);
            }
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("新增模板失败： paramDate = {}", paramData);
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * 查询模板列表
     *
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<EasyUIResult> queryList(@RequestParam(value = "page", defaultValue = "1") int page,
                                                  @RequestParam(value = "rows", defaultValue = "30") int rows) {
        try {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("查询模板列表： page = {}, rows = {}", page, rows);
            }

            Example example = new Example(ItemParam.class);
            example.setOrderByClause("updated DESC");
            PageInfo<ItemParam> pageInfo = itemParamService.queryPageListByExample(page, rows, example);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("查询模板列表成功： pageInfo = {}", pageInfo);
            }
            return ResponseEntity.ok(new EasyUIResult(pageInfo.getTotal(), pageInfo.getList()));
        } catch (Exception e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("查询模板列表失败： page = {}, rows = {}", page, rows);
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
