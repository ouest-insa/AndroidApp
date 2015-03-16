package fr.ouestinsa.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import fr.ouestinsa.object.Study;

public class ApplicableDAO {
	private SQLiteDatabase db = null;
	private MySQLiteHelper helper = null;
	private static ApplicableDAO mInstance = null;
	
	public static final String NAME_TABLE = "Applicable";
	
	public static final String REFERENCE = "reference";
	public static final String TIMESTAMP = "timestamp";
	
	public static final String CREATE_TABLE = 
			"CREATE TABLE " + NAME_TABLE + " ("
					+ REFERENCE + " INTEGER NOT NULL UNIQUE, "
					+ TIMESTAMP + " INTEGER NOT NULL, "
					+ "PRIMARY KEY (" + REFERENCE + ")"
				+ ");";
		public static final String CREATE_INDEX_1 = 
				"CREATE INDEX fk_" + NAME_TABLE + "_1 ON " + NAME_TABLE + " (" + REFERENCE + " ASC);";
		public static final String DELETE_TABLE = 
				"DROP TABLE " + NAME_TABLE + ";";

	private ApplicableDAO(Context c) {
		helper = MySQLiteHelper.getInstance(c);
		db = helper.getWritableDatabase();
	}
	
	public static ApplicableDAO getInstance(Context c) {
		if(mInstance == null) {
			mInstance = new ApplicableDAO(c);
		}
		return mInstance;
	}

	public void clear() {
		db.delete(NAME_TABLE, null, null);
	}
	
	public boolean isApplicable(Study study) {
		Cursor c = db.rawQuery(
				"SELECT " + TIMESTAMP + 
				" FROM " + NAME_TABLE + "" +
				" WHERE " + REFERENCE + " = ?", 
				new String[]{String.valueOf(study.getReference())});
		if(c == null || c.getCount() == 0) {
			return true;
		}
		c.moveToNext();
		long lastApply = c.getLong(c.getColumnIndexOrThrow(TIMESTAMP));
		long now = new java.util.Date().getTime();
		
		return (now - lastApply > 1000 * 60 * 60 * 24 * 15) ? true : false;
	}
	
	public int justApply(Study study) {
		Cursor c = db.rawQuery(
				"SELECT " + TIMESTAMP + 
				" FROM " + NAME_TABLE + "" +
				" WHERE " + REFERENCE + " = ?", 
				new String[]{String.valueOf(study.getReference())});
		if(c == null || c.getCount() == 0) {
			ContentValues values = new ContentValues();
			values.put(REFERENCE, study.getReference());
			values.put(TIMESTAMP, new java.util.Date().getTime());
			
			return (int) db.insert(NAME_TABLE, null, values);
		} else {
			ContentValues values = new ContentValues();
			values.put(TIMESTAMP, new java.util.Date().getTime());
			
			return db.update(
					NAME_TABLE, 
					values, 
					REFERENCE + " = ?", 
					new String[] {String.valueOf(study.getReference())});
		}
	}
}
