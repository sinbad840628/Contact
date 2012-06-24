package cn.android.contact.dao;

import java.util.List;

import cn.android.contact.model.Contact;

public interface IContact {
	public void insert(Contact contact) ;
	public void delete(String condition);
	public void update(Contact contact);
	public void initIsMark(String state,String condition);
	public List<Contact> getAll();
	public List<Contact> getProductByCondition(String condition);
	public Contact getById(int productId);
	public int getCount(String condition);
	public void changeGroup(String sql);
}
