package cn.android.contact.daompl;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import cn.android.contact.dao.IGroup;
import cn.android.contact.db.DBHelper;
import cn.android.contact.dbconstant.DB.TABLES.CONTACT;
import cn.android.contact.dbconstant.DB.TABLES.GROUP;
import cn.android.contact.model.Group;




public class GroupService implements IGroup {
	
	private DBHelper dbHelper = null;

	public GroupService(Context context) {

		dbHelper = new DBHelper(context);

	}

	public void insert(Group group) {
		String sql = String.format(GROUP.SQL.INSERT, group.getGroupName());
		
		dbHelper.ExecuteSQL(sql);
		
	}

	public void delete(int id) {
		String sql = String.format(GROUP.SQL.DELETE_BY_ID, id);
		dbHelper.ExecuteSQL(sql);
		
	}

	public void update(Group group) {
		String sql = String.format(GROUP.SQL.UPDATE, group.getGroupName(),group.getGroupId());
		dbHelper.ExecuteSQL(sql);
		
	}
	
	/**
	 * 获得所有分组
	 */

	public List<Group> getAll() {
		try {
			String sql = String.format(GROUP.SQL.SELECT, "1=1");
			return this.select(sql);
		} finally {
			dbHelper.closeDataBase();
		}
	}
	
	/**
	 * 通过ID获得分组
	 * 
	 * @param productId
	 * @return
	 */
	public Group getById(int groupId) {
		String sql = String.format(GROUP.SQL.SELECT, GROUP.FIELDS.GROUPID + "="
				+ groupId);
		try {
			return this.select(sql).get(0);
		} catch (Exception ex) {
			return null;
		} finally {
			dbHelper.closeDataBase();
		}

	}
	public Group getByName(String name){
		String sql = String.format(GROUP.SQL.SELECT, GROUP.FIELDS.GROUPNAME+" = "+name);
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

	public List<Group> select(String sql) throws SQLException {
		List<Group> list_product = new ArrayList<Group>();

		try {
			Cursor cursor = dbHelper.SELECT(sql);

			while (cursor.moveToNext()) {

				Group group = new Group();
				
				group.setGroupId(cursor.getInt(cursor.getColumnIndex(GROUP.FIELDS.GROUPID)));
				group.setGroupName(cursor.getString(cursor
						.getColumnIndex(GROUP.FIELDS.GROUPNAME)));
				

				list_product.add(group);
			}
			cursor.close();

			return list_product;
		} catch (Exception e) {
			return null;
		} finally {
			dbHelper.closeDataBase();
		}

	}
	
	/**
	 * 获得分组总数
	 */
	public int getCount() {
		try {
			String sql_count = GROUP.SQL.COUNT;
			Cursor cursor = dbHelper.SELECT(sql_count);
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
