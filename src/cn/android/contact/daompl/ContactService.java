package cn.android.contact.daompl;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;
import cn.android.contact.dao.IContact;
import cn.android.contact.db.DBHelper;
import cn.android.contact.dbconstant.DB.TABLES.CONTACT;
import cn.android.contact.model.Contact;

public class ContactService implements IContact {
	private DBHelper dbHelper = null;

	public ContactService(Context context) {

		dbHelper = new DBHelper(context);

	}

	/**
	 * 数据库的增加操作
	 * 
	 * @param product
	 * @throws SQLException
	 */
	
	public void insert(Contact c) {
		//this.ps.add(p);
		ContentValues values =  new ContentValues();
		values.put(CONTACT.FIELDS.IMAGE, c.getImage());
		values.put(CONTACT.FIELDS.NAME, c.getName());
		values.put(CONTACT.FIELDS.GROUPID,c.getGroupId());
		values.put(CONTACT.FIELDS.PHONE, c.getPhone());
		values.put(CONTACT.FIELDS.HOMEPHONE,c.getHomePhone());
		values.put(CONTACT.FIELDS.OTHERPHONE,c.getOtherPhone());
		values.put(CONTACT.FIELDS.EMAIL,c.getE_mail());
		values.put(CONTACT.FIELDS.ADDRESS,c.getAddress());
		values.put(CONTACT.FIELDS.BIRTHDAY,c.getBirthday());
		values.put(CONTACT.FIELDS.COMMENT,c.getComment());
		values.put(CONTACT.FIELDS.ISMARK,c.getIsMark());
		this.dbHelper.insert(CONTACT.TABLENAME, values);
	}
//	public void insert(Contact contact) throws SQLException {
//		String sql = String.format(CONTACT.SQL.INSERT, 
//				contact.getImage(),
//				contact.getName(), 
//				contact.getGroupId(), 
//				contact.getPhone(),
//				contact.getHomePhone(), 
//				contact.getOtherPhone(),
//				contact.getE_mail(),
//				contact.getAddress(),
//				contact.getBirthday(),
//				contact.getComment(),
//				contact.getIsMark());
//		System.out.println(sql + "  insert");
//		dbHelper.ExecuteSQL(sql);
//	}

	/**
	 * 数据库的删除操作
	 * 
	 * @param 
	 * @throws SQLException
	 */
	public void delete(String condition) throws SQLException {
		String sql = String.format(CONTACT.SQL.DELETE_BY_ID, condition);
		dbHelper.ExecuteSQL(sql);
	}

	/**
	 * 数据库的修改操作
	 * 
	 * @param product
	 * @throws SQLException
	 * 
	 * 
	 */
	
	public void update(Contact c) throws SQLException {
		ContentValues values =  new ContentValues();
		values.put(CONTACT.FIELDS.IMAGE, c.getImage());
		values.put(CONTACT.FIELDS.NAME, c.getName());
		values.put(CONTACT.FIELDS.GROUPID,c.getGroupId());
		values.put(CONTACT.FIELDS.PHONE, c.getPhone());
		values.put(CONTACT.FIELDS.HOMEPHONE,c.getHomePhone());
		values.put(CONTACT.FIELDS.OTHERPHONE,c.getOtherPhone());
		values.put(CONTACT.FIELDS.EMAIL,c.getE_mail());
		values.put(CONTACT.FIELDS.ADDRESS,c.getAddress());
		values.put(CONTACT.FIELDS.BIRTHDAY,c.getBirthday());
		values.put(CONTACT.FIELDS.COMMENT,c.getComment());
		values.put(CONTACT.FIELDS.ISMARK,c.getIsMark());
		this.dbHelper.update(CONTACT.TABLENAME, values, CONTACT.FIELDS.ID+"= ? ", new String[]{c.getId()+""});
		
	}
//	public void update(Contact contact) throws SQLException {
//		String sql = String.format(CONTACT.SQL.UPDATE, contact.getImage(),
//				contact.getName(), contact.getGroupId(), contact.getPhone(),
//				contact.getHomePhone(), contact.getOtherPhone(),
//				contact.getE_mail(), contact.getAddress(),
//				contact.getBirthday(), contact.getComment(),
//				contact.getIsMark(), contact.getId());
//		dbHelper.ExecuteSQL(sql);
//	}

	/**
	 * 标记状态的设置
	 * 
	 * @param state
	 */
	public void initIsMark(String state,String condition) {
		String sql = String.format(CONTACT.SQL.INIT_ISMARK, state,condition);
		System.out.println(sql + "  mark");
		dbHelper.ExecuteSQL(sql);
	}
	/**
	 * 改变组别
	 * @param sql
	 */
	public void changeGroup(String sql){
		dbHelper.ExecuteSQL(sql);
		
		
	}

	/**
	 * 获得所有联系人
	 * 
	 * @return
	 */
	public List<Contact> getAll() {
		try {
			String sql = String.format(CONTACT.SQL.SELECT, " 1=1 ");
			return this.select(sql);
		} finally {
			dbHelper.closeDataBase();
		}
	}

	/**
	 * 通过条件获得联系人
	 * 
	 * @param condition
	 * @return
	 */
	public List<Contact> getProductByCondition(String condition) {
		//System.out.println("3");
		
		try {
			String sql = String.format(CONTACT.SQL.SELECT,  condition );
			//System.out.println(sql);
			Log.i("Item", sql);
		
			return this.select(sql);
		} finally {
			dbHelper.closeDataBase();
		}
	}

	/**
	 * 通过商品ID获得联系人
	 * 
	 * @param productId
	 * @return
	 */
	public Contact getById(int productId) {
		String sql = String.format(CONTACT.SQL.SELECT, CONTACT.FIELDS.ID + "="
				+ productId);
		try {
			return this.select(sql).get(0);
		} catch (Exception ex) {
			return null;
		} finally {
			dbHelper.closeDataBase();
		}

	}

	/**
	 * 数据库的查询操作
	 * 
	 * @param condition
	 * @return
	 * @throws SQLException
	 */

	public List<Contact> select(String sql) throws SQLException {
		List<Contact> list_contact = new ArrayList<Contact>();

		try {
			Cursor cursor = dbHelper.SELECT(sql);

			while (cursor.moveToNext()) {

				Contact contact = new Contact();
				contact.setId(cursor.getInt(cursor
						.getColumnIndex(CONTACT.FIELDS.ID)));
				contact.setImage(cursor.getBlob(cursor
						.getColumnIndex(CONTACT.FIELDS.IMAGE)));
				contact.setName(cursor.getString(cursor
						.getColumnIndex(CONTACT.FIELDS.NAME)));

				contact.setGroupId(cursor.getInt(cursor
						.getColumnIndex(CONTACT.FIELDS.GROUPID)));
				contact.setPhone(cursor.getString(cursor
						.getColumnIndex(CONTACT.FIELDS.PHONE)));
				contact.setHomePhone(cursor.getString(cursor
						.getColumnIndex(CONTACT.FIELDS.HOMEPHONE)));
				contact.setOtherPhone(cursor.getString(cursor
						.getColumnIndex(CONTACT.FIELDS.OTHERPHONE)));
				contact.setE_mail(cursor.getString(cursor
						.getColumnIndex(CONTACT.FIELDS.EMAIL)));
				contact.setAddress(cursor.getString(cursor
						.getColumnIndex(CONTACT.FIELDS.ADDRESS)));
				contact.setBirthday(cursor.getString(cursor
						.getColumnIndex(CONTACT.FIELDS.BIRTHDAY)));
				contact.setComment(cursor.getString(cursor
						.getColumnIndex(CONTACT.FIELDS.COMMENT)));
				contact.setIsMark(cursor.getString(cursor
						.getColumnIndex(CONTACT.FIELDS.ISMARK)));

				list_contact.add(contact);
			}
			cursor.close();

			return list_contact;
		} catch (Exception e) {
			return null;
		} finally {
			dbHelper.closeDataBase();
		}

	}
	/**
	 * 获得联系人总数
	 */
	public int getCount(String condition) {
		try {
			String sql_count = String.format(CONTACT.SQL.COUNT, condition);

			Cursor cursor = dbHelper.SELECT(sql_count);
			System.out.println(sql_count);
			int totalCount = -1;
			if (cursor.moveToNext()) {
				totalCount = (int) cursor.getDouble(0);

			}
			cursor.close();
			return totalCount;
		} finally {
			dbHelper.closeDataBase();
		}

	}

}
