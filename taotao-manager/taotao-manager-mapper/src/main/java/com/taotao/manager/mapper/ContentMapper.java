package com.taotao.manager.mapper;

import com.github.abel533.mapper.Mapper;
import com.taotao.manager.pojo.Content;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.Mapping;

import java.util.List;

/**
 * Created by Ellen on 2017/7/6.
 */
public interface ContentMapper extends Mapper<Content> {

    List<Content> queryListByUpdate(long categoryId);

}
