package com.taotao.manager.controller;

import com.github.pagehelper.PageInfo;
import com.taotao.common.bean.EasyUIResult;
import com.taotao.manager.pojo.Content;
import com.taotao.manager.service.ContenService;
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
 * Created by Ellen on 2017/7/6.
 */
@Controller
@RequestMapping("content")
public class ContentCtroller {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContentCtroller.class);

    @Autowired
    private ContenService contentService;

    /**
     * 添加广告
     *
     * @param content
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> saveContent(Content content) {
        try {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("添加广告，content = {}", content);
            }

            contentService.save(content);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("添加广告成功，content = {}", content);
            }
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            LOGGER.error("添加广告失败，content = {}", content);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 查询内容列表，根据更新时间的倒序
     * @param categoryId
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<EasyUIResult> queryList(@RequestParam(value = "categoryId") long categoryId,
                                                  @RequestParam(value = "page", defaultValue = "1") int page,
                                                  @RequestParam(value = "rows", defaultValue = "10") int rows) {
        try {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("查询内容列表，categoryId = {}", categoryId);
            }
            PageInfo<Content> pageInfo = contentService.queryListByUpdate(categoryId, page, rows);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("查询内容列表成功，pageInfo = {}", pageInfo.getList());
            }

            return ResponseEntity.status(HttpStatus.OK).body(new EasyUIResult(pageInfo.getTotal(), pageInfo.getList()));
        } catch (Exception e) {
            LOGGER.error("查询内容列表失败，categoryId = {}", categoryId);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
