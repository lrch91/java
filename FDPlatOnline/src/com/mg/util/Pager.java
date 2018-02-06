package com.mg.util;

import java.util.List;

public final class Pager {

	public static final int PAGE_SIZE = 10;
	
	private int total = 0;
	private List<?> items = null;
	private int pageSize = PAGE_SIZE;
	
	public Pager(){
		
	}
	
	public Pager(int total, List<?> items){
		this.total = total;
		this.items = items;
	}

	public Pager(int total, List<?> items, int pageSize) {
		this.total = total;
		this.items = items;
		this.pageSize = pageSize;
	}
	
	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<?> getItems() {
		return items;
	}

	public void setItems(List<?> items) {
		this.items = items;
	}
	
	public int getPages(){
		return (total + pageSize - 1)/pageSize;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

}
