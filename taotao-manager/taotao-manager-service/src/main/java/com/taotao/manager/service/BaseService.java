package com.taotao.manager.service;

import com.github.abel533.entity.Example;
import com.github.abel533.mapper.Mapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.manager.pojo.BasePojo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * Created by Ellen on 2017/7/2.
 */
public abstract class BaseService<T extends BasePojo> {

    @Autowired
    private Mapper<T> mapper;


    /**
     * 通过主键查询
     * @param id
     * @return
     */
    public T queryById(long id) {
        return mapper.selectByPrimaryKey(id);
    }

    /**
     * 查询所有的记录
     * @return
     */
    public List<T> queryAll() {
        return mapper.select(null);
    }

    /**
     * 根据条件查询记录，如果记录有多条，抛出异常
     * @return
     */
    public T queryOne(T t) {
        return mapper.selectOne(t);
    }

    /**
     * 根据条件批量查询
     * @param record
     * @return
     */
    public List<T> queryListByWhere(T record) {
        return mapper.select(record);
    }

    /**
     * 分页查询
     * @param page
     * @param row
     * @param record
     * @return
     */
    public PageInfo<T> queryPageListByWhere(int page, int row, T record) {
        PageHelper.startPage(page, row);
        List<T> list = queryListByWhere(record);
        return new PageInfo<T>(list);
    }

    /**
     * 插入数据,返回插入数据条数
     * @param t
     * @return
     */
    public int save(T t) {
        t.setCreated(new Date());
        t.setUpdated(t.getCreated());
        return mapper.insert(t);
    }

    /**
     * 插入非null的字段
     * @param record
     * @return
     */
    public int saveSelective(T record) {
        record.setCreated(new Date());
        record.setUpdated(record.getCreated());
        return mapper.insertSelective(record);
    }

    /**
     * 更新数据，返回成功条数
     * @param t
     * @return
     */
    public int update(T t) {
        t.setUpdated(new Date());
        return mapper.updateByPrimaryKey(t);
    }

    /**
     * 更新非null字段数据，返回成功条数
     * @param record
     * @return
     */
    public int updateSelective(T record) {
        record.setUpdated(new Date());
        return mapper.updateByPrimaryKeySelective(record);
    }

    /**
     * 根据主键删除数据
     * @param t
     * @return
     */
    public int delete(T t) {
        return mapper.deleteByPrimaryKey(t);
    }

    /**
     * 批量删除数据
     * @param clazz
     * @param propertity
     * @param values
     * @return
     */
    public int deleteByIds(Class<T> clazz, String propertity, List<Object> values) {
        Example example = new Example(clazz);
        example.createCriteria().andIn(propertity, values);
        return mapper.deleteByExample(example);
    }

    /**
     * 根据条件删除数据
     * @param record
     * @return
     */
    public int deleteByWhere(T record) {
        return mapper.delete(record);
    }
}
