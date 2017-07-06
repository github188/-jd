package com.taotao.manager.service;

import com.taotao.manager.pojo.ContentCategory;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ellen on 2017/7/6.
 */
@Service
public class ContentCategoryService extends BaseService<ContentCategory> {

    public void saveContentCatgory(ContentCategory contentCategory) {
        ContentCategory parent = queryById(contentCategory.getParentId());
        if (!parent.getIsParent()) {
            parent.setIsParent(true);
            updateSelective(parent);
        }
        contentCategory.setIsParent(false);
        contentCategory.setSortOrder(1);
        contentCategory.setStatus(1);
        save(contentCategory);
    }

    public void deleteContentCatgory(ContentCategory contentCategory) {
        ArrayList<Object> ids = new ArrayList<>();
        ids.add(contentCategory.getId());
        deleteCat(contentCategory.getId(), ids);

        ContentCategory param = new ContentCategory();
        param.setParentId(contentCategory.getParentId());

        //递归删除所有的子节点
        deleteByIds(ContentCategory.class, "id", ids);

        //获得父节点，判断是否还有子节点
        List<ContentCategory> list = queryListByWhere(param);
        if (list == null || list.size() == 0) {
            ContentCategory parent = new ContentCategory();
            parent.setId(param.getParentId());
            parent.setIsParent(false);
            updateSelective(parent);
        }
    }

    private void deleteCat(long parentId, List<Object> ids) {
        ContentCategory param = new ContentCategory();
        param.setParentId(parentId);
        List<ContentCategory> list = queryListByWhere(param);
        for (ContentCategory contentCategory : list) {
            ids.add(contentCategory.getId());
            if (contentCategory.getIsParent()) {
                deleteCat(contentCategory.getId(), ids);
            }
        }
    }
}
