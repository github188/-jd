package com.taotao.manager.service;

import com.github.abel533.entity.Example;
import com.github.abel533.mapper.Mapper;
import com.github.pagehelper.PageInfo;
import com.taotao.manager.mapper.ItemMapper;
import com.taotao.manager.pojo.Item;
import com.taotao.manager.pojo.ItemDesc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ellen on 2017/7/2.
 */
@Service
public class ItemService extends BaseService<Item> {

    @Autowired
    private ItemDescService itemDescService;


    /**
     * 在一个事务中添加item项和itemdesc项
     * @param item
     * @param desc
     */
    public void saveItem(Item item, ItemDesc desc) {
        item.setStatus(1);
        item.setId(null);
        desc.setItemId(item.getId());
        save(item);
        itemDescService.save(desc);
    }

    public PageInfo<Item> queryItemList(int page, int rows, Object o) {
        Example example = new Example(Item.class);
        List<Object> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        example.createCriteria().andIn("status", list);
        example.setOrderByClause("updated DESC");
        return queryPageListByExample(page, rows, example);
    }

    public void updateItems(List<Object> ids) {
        Mapper<Item> mapper = getMapper();
        Example example = new Example(Item.class);
        example.createCriteria().andIn("id", ids);
        Item item = new Item();
        item.setStatus(3);
        mapper.updateByExampleSelective(item, example);
    }
}
