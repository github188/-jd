package com.taotao.manager.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.manager.mapper.ContentMapper;
import com.taotao.manager.pojo.Content;
import com.taotao.manager.pojo.ContentCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Ellen on 2017/7/6.
 */
@Service
public class ContenService extends BaseService<Content>{

    @Autowired
    private ContentMapper contentMapper;

    public PageInfo<Content> queryListByUpdate(long categoryId, int page, int rows) {
        PageHelper.startPage(page, rows);
        PageInfo<Content> list = new PageInfo<Content>(contentMapper.queryListByUpdate(categoryId));
        return list;
    }
}
