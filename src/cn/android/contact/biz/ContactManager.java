package cn.android.contact.biz;

import java.util.List;

import android.content.Context;
import android.util.Log;
import cn.android.contact.dao.IContact;
import cn.android.contact.daompl.ContactService;
import cn.android.contact.dbconstant.DB.TABLES.CONTACT;
import cn.android.contact.model.Contact;

public class ContactManager {
	private IContact contactService;

	public ContactManager(Context context) {
		contactService = new ContactService(context);

	}

	public List<Contact> getContactByCondition(String condition) {

		condition = CONTACT.FIELDS.NAME + " like " + "'" + condition + "%'"
				+ " or " + CONTACT.FIELDS.PHONE + " like " + "'" + condition
				+ "%'";

		if (condition.equals("")) {
			condition = "1=1";
		}
		return contactService.getProductByCondition(condition);

	}

	public int getCountByGroupId(int groupId) {
		String condition = CONTACT.FIELDS.GROUPID + " = " + groupId;
		return contactService.getCount(condition);

	}

	public int getAllCount() {
		return contactService.getCount("1=1");
	}

	public List<Contact> getAllContacts() {
		return contactService.getAll();
	}

	public void addContact(Contact contact) {
		contactService.insert(contact);

	}

	public void modifyContact(Contact contact) {
		contactService.update(contact);

	}

	public Contact getContactById(int id) {
		return contactService.getById(id);

	}

	/**
	 * ͨ�����ɾ��
	 */
	public void deletByIsMark() {
		contactService.delete(CONTACT.FIELDS.ISMARK + " = " + "'true'");
	}

	/**
	 * ͨ��IDɾ��
	 */
	public void deleteById(int id) {
		contactService.delete(CONTACT.FIELDS.ID + " = " + id);

	}

	/**
	 * ͨ�����ID�����ϵ��
	 * 
	 * @param groupId
	 * @return
	 */
	public List<Contact> getContactByGroupId(int groupId) {
		System.out.println("1");
		System.out.println("2");
		return contactService.getProductByCondition(CONTACT.FIELDS.GROUPID
				+ "=" + groupId);
	}

	// public Contact getContactById(int groupId,int contactId){
	// return
	// contactService.getProductByCondition(CONTACT.FIELDS.GROUPID+"="+groupId).get(0);
	//
	// }
	/**
	 * ȫ���Ա����
	 */
	public void changeGroupByGroup(int reGroupId, int toGroupId) {
		String sql = String.format(CONTACT.SQL.CHANGE_GROUP, toGroupId,
				CONTACT.FIELDS.GROUPID + " = " + reGroupId);
		Log.i("Item", sql);
		contactService.changeGroup(sql);

	}

	/**
	 * ��ǻ���
	 */
	public void changeGroupByMark(int toGroupId) {
		String sql = String.format(CONTACT.SQL.CHANGE_GROUP, toGroupId,
				CONTACT.FIELDS.ISMARK + " = " + "'true'");
		Log.i("Item", sql);
		contactService.changeGroup(sql);

	}

	/**
	 * ���˻���
	 * 
	 * @param contactId
	 * @param toGroupID
	 */
	public void changeGroupByCotact(int contactId, int toGroupId) {
		String sql = String.format(CONTACT.SQL.CHANGE_GROUP, toGroupId,
				CONTACT.FIELDS.ID + " = " + contactId);
		Log.i("Item", sql);
		contactService.changeGroup(sql);
	}

	public void initIsMark(String state) {
		contactService.initIsMark(state, "1=1");

	}

	public void initIsMarkById(String state, int id) {
		contactService.initIsMark(state, CONTACT.FIELDS.ID + " = " + id);
	}

}
