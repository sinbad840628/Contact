package cn.android.contact.db;

import cn.android.contact.dbconstant.DB;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {

	public SQLiteHelper(Context context) {
		super(context, DB.DATABASE_NAME, null, DB.DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DB.TABLES.CONTACT.SQL.CREAT);
		db.execSQL(DB.TABLES.GROUP.SQL.CREAT);
		
		
		String sql = String.format(DB.TABLES.GROUP.SQL.INSERT, "Î´·Ö×é");
		db.execSQL(sql);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(DB.TABLES.CONTACT.SQL.DROP);
		db.execSQL(DB.TABLES.GROUP.SQL.DROP);
		this.onCreate(db);
		

	}

}
