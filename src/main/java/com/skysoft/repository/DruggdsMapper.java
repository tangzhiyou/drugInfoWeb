package com.skysoft.repository;

import com.skysoft.domain.Druggds;

public interface DruggdsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Druggds record);

    int insertSelective(Druggds record);

    Druggds selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Druggds record);

    int updateByPrimaryKeyWithBLOBs(Druggds record);

    int updateByPrimaryKey(Druggds record);
}