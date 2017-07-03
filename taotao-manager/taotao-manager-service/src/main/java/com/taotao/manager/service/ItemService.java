package com.taotao.manager.service;

import com.github.abel533.mapper.Mapper;
import com.taotao.manager.mapper.ItemMapper;
import com.taotao.manager.pojo.Item;
import com.taotao.manager.pojo.ItemDesc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
