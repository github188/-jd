package cn.taotao.crawler.mapper;

import java.util.Collection;

import cn.taotao.crawler.pojo.Item;
import org.apache.ibatis.annotations.Param;

public interface ItemMapper {

	/**
	 * 新增商品
	 * 
	 * @param item
	 *            商品对象
	 * @return
	 */
	public Long saveItems(@Param("items") Collection<Item> items);
	
}
