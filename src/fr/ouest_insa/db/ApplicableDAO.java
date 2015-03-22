package fr.ouest_insa.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import fr.ouest_insa.object.Study;

/**
 * This class enable to know if the user has 
 * already apply during the X last days.<br> 
 * Use the pattern singleton to have only one instance.
 * @author Loïc Pelleau
 */
public class ApplicableDAO {
	private SQLiteDatabase db = null;
	private MySQLiteHelper helper = null;
	private static ApplicableDAO mInstance = null;
	
	/**
	 * Number of days where applying to the study is disable
	 */
	private static final int nbDaysNotApplicable = 15;
	
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
	
	/**
	 * Search in the table if the user already apply for 
	 * the specific study during the <b>nbDaysNotApplicable</b> days.
	 * @param study Study to search
	 * @return boolean
	 */
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
		
		return (now - lastApply > 1000 * 60 * 60 * 24 * nbDaysNotApplicable) ? true : false;
	}
	
	/**
	 * Write in the database that the user just applied to the specific study.
	 * @param study Study to apply
	 * @return int result of the query (-1 if an error occured)
	 */
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
