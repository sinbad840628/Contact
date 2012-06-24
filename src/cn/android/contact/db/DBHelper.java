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
	 * ���һ����¼
	 * 
	 * @param table
	 *            ����
	 * @param values
	 *            ���ֶμ�ֵ��HashMap
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
	 * ����ִ����ɾ�Ĳ���
	 * @param sql  ��insert,update,delete��ͷ��sql���
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
	 * ���������޸ļ�¼
	 * 
	 * @param table
	 *            ����
	 * @param values
	 *            ���ֶμ�ֵ��HashMap
	 * @param whereClause
	 *            Where��䲿��(eg: id=?)
	 * @param whereArgs
	 *            where����Ӧ����ֵ(eg:new String[]{1});
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
	 * ����������ݵĲ�ѯ����
	 * @param sql ��Select��ͷ��SQL���
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
	 * �ر�DataBase
	 */
	public void closeDataBase() {
		this.helper.close();
	}


}
