package cn.android.contact.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


public class DBHelper {
	private SQLiteHelper helper = null;

	public DBHelper(Context context) {
		helper = new SQLiteHelper(context);

	}
	
	
	/**
	 * 添加一条记录
	 * 
	 * @param table
	 *            表名
	 * @param values
	 *            表字段及值的HashMap
	 */
	public void insert(String table, ContentValues values) {
		SQLiteDatabase db = null;
		try {
			db = this.helper.getWritableDatabase();
			db.insert(table, null, values);
		} catch (SQLException ex) {
			throw ex;
		} finally {
			db.close();
		}
	}
	
	/**
	 * 用于执行增删改操作
	 * @param sql  以insert,update,delete打头的sql语句
	 */
	public void ExecuteSQL(String sql){
		SQLiteDatabase db = null;
		try
		{
			db = helper.getWritableDatabase();
		   db.execSQL(sql);
		}catch(SQLException ex){
			throw ex;
		}finally{
			db.close();
		}
		
		
	}
	/**
	 * 根据条件修改记录
	 * 
	 * @param table
	 *            表名
	 * @param values
	 *            表字段及值的HashMap
	 * @param whereClause
	 *            Where语句部分(eg: id=?)
	 * @param whereArgs
	 *            where部分应填充的值(eg:new String[]{1});
	 */
	public void update(String table, ContentValues values, String whereClause,
			String[] whereArgs) {
		SQLiteDatabase db = null;
		try {
			db = this.helper.getWritableDatabase();
			db.update(table, values, whereClause, whereArgs);
		} catch (SQLException ex) {
			throw ex;
		} finally {
			db.close();
		}
	}
	/**
	 * 用于完成数据的查询操作
	 * @param sql 以Select打头的SQL语句
	 * @return Cursor
	 * @throws SQLException
	 */
	public Cursor SELECT(String sql) throws SQLException{
		SQLiteDatabase db = null;
		Cursor cursor=null;
		
		try
		{
			
			db = helper.getReadableDatabase();
			 cursor=db.rawQuery(sql,null);
		}catch(SQLException ex){
			throw ex;
		}
		return cursor;
	}
	
	/**
	 * 关闭DataBase
	 */
	public void closeDataBase() {
		this.helper.close();
	}


}
