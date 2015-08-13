package com.skysoft.service;

import com.skysoft.domain.Druggds;
import com.skysoft.repository.DruggdsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CFDAService {

    @Autowired
    private DruggdsMapper druggdsDao;

    public void save(SortedMap map, String TableName) {
        List<Object> list = new ArrayList<Object>();
        Iterator<Map.Entry<Integer, Object>> iter = map.entrySet()
                .iterator();
        while (iter.hasNext()) {
            Map.Entry<Integer, Object> entry = iter.next();
            Object ident = (Object) entry.getValue();
            list.add(ident);
            map.remove(entry.getKey());
        }
    }

    public void saveBeans(List<Druggds> druggdsList) {
        for (Druggds druggds : druggdsList) {
            druggdsDao.insert(druggds);
        }
    }
}
