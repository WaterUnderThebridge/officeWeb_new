package com.tlgc.dao;

import com.tlgc.entity.FranApp;
import com.tlgc.entity.GoTong;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by TONY on 2019/6/27.
 */
public interface GongTongDao extends JpaRepository<GoTong,Integer> {
  List<GoTong> findAllByFranAppId(Integer franAppId);
}
