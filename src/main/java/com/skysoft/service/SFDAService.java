package com.skysoft.service;

import com.skysoft.domain.Druggds;
import com.skysoft.repository.DruggdsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SFDAService {

    @Autowired
    private DruggdsMapper druggdsDao;

    public void save(TreeMap<String,Druggds> map, String TableName) {


        List<Druggds> druggdsList = new ArrayList<Druggds>();
        for(Map.Entry<String,Druggds> entry:map.entrySet()){
            druggdsList.add(entry.getValue());
//            map.remove(entry.getKey());
        }
//        druggdsDao.batchInsert(druggdsList);
    }

    public void saveBeans(List<Druggds> druggdsList) {
        for (Druggds druggds : druggdsList) {
            druggdsDao.insert(druggds);
        }
    }
}
