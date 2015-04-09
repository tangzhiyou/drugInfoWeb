package com.skysoft.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.SortedMap;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skysoft.domain.Druggds;
import com.skysoft.repository.DruggdsMapper;

@Service
public class CFDAService {

	@Autowired
	private DruggdsMapper druggdsDao;

	public void save(SortedMap map,String TableName)
	{
		// this.queue=queue;
			List<Object> list = new ArrayList<Object>();
			Iterator<Map.Entry<Integer, Object>> iter = map.entrySet()
					.iterator();
			while (iter.hasNext())
			{
				Map.Entry<Integer, Object> entry = iter.next();
				Object ident = (Object) entry.getValue();
				list.add(ident);
				map.remove(entry.getKey());
			}
//			cfdaDao.save(TableName+".insert", list);
	}
    public void saveBeans(List<Druggds> druggdsList,String TableName)
    {
//        Iterator<Map.Entry<Integer, Object>> iter = map.entrySet()
//                .iterator();
        for(Druggds druggds:druggdsList)
        {
//            Map.Entry<Integer, Object> entry = iter.next();
//			Druggds ident = (Druggds) entry.getValue();
//            cfdaDao.save(TableName+".insert", ident);
			druggdsDao.insert(druggds);
//            map.remove(entry.getKey());
        }
    }
}
