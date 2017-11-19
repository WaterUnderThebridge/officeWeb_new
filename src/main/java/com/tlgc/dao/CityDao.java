package com.tlgc.dao;

import com.tlgc.entity.City;
import com.tlgc.entity.Province;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by TONY on 2017/11/19.
 */
public interface CityDao extends JpaRepository<City,Integer> {
   public List<City> findAllByProvince(Province province);

}
