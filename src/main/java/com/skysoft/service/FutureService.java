package com.skysoft.service;

import com.skysoft.dao.futureBatisDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * User: pinaster
 * Date: 13-11-12
 * Time: 下午5:33
 */
@Service
public class FutureService {

    @Autowired
    private futureBatisDao fetureDao;

    // Queue<Identification> queue;

    public void save(SortedMap map,String TableName)
    {
        // this.queue=queue;
        Iterator<Map.Entry<Integer, Object>> iter = map.entrySet()
                .iterator();
        while (iter.hasNext())
        {
            Map.Entry<Integer, Object> entry = iter.next();
            Object ident = (Object) entry.getValue();
            fetureDao.save(TableName+".insert", ident);
            map.remove(entry.getKey());
        }
        
    }

}
