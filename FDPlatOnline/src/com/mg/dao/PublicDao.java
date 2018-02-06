package com.mg.dao;

import java.util.List;

import org.hibernate.Query;

import com.mg.util.Pager;


public interface PublicDao<T> {
	Pager findBycondition(Integer page,Integer pageSize,String where);
	List<T> findBycondition(String where, Integer limit);
	List<T> findBycondition(String where);
	List<T> findAll();
	List<T> findAll(String order_str);
	T findById(String id);
	T findOne(String where);
	void add(T t);
	void update(T t);
	void deleteById(String id);
	Query getQuery(String where);
}
