package com.skysoft.dao;

import org.mybatis.spring.support.SqlSessionDaoSupport;

import java.io.Serializable;
import java.util.List;

/**
 * User: pinaster
 * Date: 13-11-12
 * Time: 下午5:30
 */
public class futureBatisDao extends SqlSessionDaoSupport {
    public void save(String key, Object object)
    {
        getSqlSession().insert(key, object);
    }

    public void delete(String key, Serializable id)
    {
        getSqlSession().delete(key, id);
    }

    public void delete(String key, Object object)
    {
        getSqlSession().delete(key, object);
    }

    public <T> T get(String key, Object params)
    {
        return (T) getSqlSession().selectOne(key, params);
    }

    public <T> List<T> getList(String key)
    {
        return getSqlSession().selectList(key);
    }

    public <T> List<T> getList(String key, Object params)
    {
        return getSqlSession().selectList(key, params);
    }

}
