package com.mg.dao;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.mg.util.Pager;



@SuppressWarnings("unchecked")
public class PublicDaoImpl<T> {
	@Autowired
	private SessionFactory sessionFactory;
	
	private Class<T> entityClass;
	
	public PublicDaoImpl() {
		entityClass =(Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	public List<T> findAll(){
		List<T> list= null;
		try {
			Session s = sessionFactory.getCurrentSession();
			String hql = "from "+entityClass.getName();
			list = s.createQuery(hql).list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	public List<T> findAll(String order_str){
		List<T> list= null;
		try {
			Session s = sessionFactory.getCurrentSession();
			String hql = "from "+entityClass.getName()+" "+order_str;
			list = s.createQuery(hql).list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public List<T> findBycondition(String where) {
		List<T> list= null;
		try {
			Session s = sessionFactory.getCurrentSession();
			String hql = "from "+entityClass.getName()+" "+where;
			list = s.createQuery(hql).list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public List<T> findBycondition(String where, Integer limit) {
		List<T> list= null;
		try {
			Session s = sessionFactory.getCurrentSession();
			String hql = "from "+entityClass.getName()+" "+where;
			Query q = s.createQuery(hql);
			if(limit!=null){
				q.setFirstResult(0);
				q.setMaxResults(limit);
			}
			list = q.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public Pager findBycondition(Integer page,Integer pageSize,String where) {
		try {
			if(page==null)
				page = 1;
			if(pageSize==null)
				pageSize = Pager.PAGE_SIZE;
			page--;
			if (page < 0)
				page = 0;
			if (pageSize < 1)
				pageSize = Pager.PAGE_SIZE;

			String hql = "from "+entityClass.getName();

			Session s = sessionFactory.getCurrentSession();
			Query q = s.createQuery("select count(id) from "+entityClass.getName()+" "+ where);

			Long rows = (Long) q.uniqueResult();

			q = s.createQuery(hql + where);
			q.setFirstResult(pageSize * page);
			q.setMaxResults(pageSize);

			Pager p = new Pager();
			p.setItems(q.list());
			p.setTotal(rows.intValue());
			p.setPageSize(pageSize);
			return p;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public T findById(String id) {
		T t = null;
		try {
			Session s = sessionFactory.getCurrentSession();
			t = (T) s.createQuery("from "+entityClass.getName()+" where id=:id").setString("id", id).uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;
	}
	
	public T findOne(String where) {
		T t = null;
		try {
			Session s = sessionFactory.getCurrentSession();
			t = (T) s.createQuery("from "+entityClass.getName()+where).uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;
	}

	public void add(T t) {
		try {
			Session s = sessionFactory.getCurrentSession();
			s.save(t);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void update(T t) {
		try {
			Session s = sessionFactory.getCurrentSession();
			s.update(t);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void deleteById(String id) {
		try {
			Session s = sessionFactory.getCurrentSession();
			s.createQuery("delete from "+entityClass.getName()+" where id=:id").setString("id", id).executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public Query getQuery(String where) {
		Query q = null;
		try {
			Session s = sessionFactory.getCurrentSession();
			String hql = "from "+entityClass.getName()+" "+where;
			q = s.createQuery(hql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return q;
	}
}
