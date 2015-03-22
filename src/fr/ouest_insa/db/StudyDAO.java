package fr.ouest_insa.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import fr.ouest_insa.object.Status;
import fr.ouest_insa.object.Study;

/**
 * This class is used to store the data for the studies (including details).<br>
 * Use the pattern singleton to have only one instance.
 * @author Loïc Pelleau
 * @see Study
 */
public class StudyDAO {
	private SQLiteDatabase db = null;
	private MySQLiteHelper helper = null;
	private static StudyDAO mInstance = null;
	
	public static final String NAME_TABLE = "Study";
	
	public static final String ID = "id";
	public static final String REFERENCE = "reference";
	public static final String JEH = "jeh";
	public static final String NAME = "name";
	public static final String STATUS = "status";
	public static final String APPLICABLE = "applicable";
	public static final String TYPE = "type";
	public static final String TYPE_ID = "type_id";
	public static final String DETAILS = "details";
	
	public static final String CREATE_TABLE = 
			"CREATE TABLE " + NAME_TABLE + " ("
					+ ID + " INTEGER NOT NULL, "
					+ REFERENCE + " INTEGER NOT NULL UNIQUE, "
					+ JEH + " INTEGER, "
					+ NAME + " TEXT NOT NULL, "
					+ STATUS + " TEXT NOT NULL, "
					+ APPLICABLE + " INTEGER DEFAULT(0), "
					+ TYPE + " TEXT NOT NULL, "
					+ TYPE_ID + " INTEGER NOT NULL, "
					+ DETAILS + " TEXT, "
					+ "PRIMARY KEY (" + ID + ")"
				+ ");";
		public static final String CREATE_INDEX_1 = 
				"CREATE INDEX fk_" + NAME_TABLE + "_1 ON " + NAME_TABLE + " (" + ID + " ASC);";
		public static final String DELETE_TABLE = 
				"DROP TABLE " + NAME_TABLE + ";";

	private StudyDAO(Context c) {
		helper = MySQLiteHelper.getInstance(c);
		db = helper.getWritableDatabase();
	}
	
	public static StudyDAO getInstance(Context c) {
		if(mInstance == null) {
			mInstance = new StudyDAO(c);
		}
		return mInstance;
	}

	/**
	 * Clear all the content of the table.
	 */
	public void clear() {
		db.delete(NAME_TABLE, null, null);
	}

	/**
	 * Add a study to the database.
	 * @param study Study to add
	 * @return boolean result of the query (false if an error occured)
	 */
	public boolean add(Study study) {
		ContentValues values = new ContentValues();
		values.put(ID, study.getId());
		values.put(REFERENCE, study.getReference());
		values.put(JEH, study.getJeh());
		values.put(NAME, study.getName());
		values.put(STATUS, study.getStatus().toString());
		if(study.getStatus().toString().equals(Status.CONTACT.toString())) {
			values.put(APPLICABLE, 1);
		}
		values.put(TYPE, study.getType());
		values.put(TYPE_ID, study.getTypeId());
		
		if(db.insert(NAME_TABLE, null, values) == -1) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Add the details for the specific study
	 * @param reference Reference of the study
	 * @param details Details to add to the study
	 * @return int result of the query (-1 if an error occured)
	 */
	public int addDetails(int reference, String details) {
		ContentValues values = new ContentValues();
		values.put(DETAILS, details);
		
		return db.update(
				NAME_TABLE, 
				values, 
				REFERENCE + " = ?", 
				new String[] {String.valueOf(reference)});
	}

	/**
	 * Retrieve all the studies from the database
	 * @return list List of studies from the database
	 */
	public List<Study> getAll() {

		List<Study> list = new ArrayList<Study>();
		
		Cursor c = db.rawQuery(
				"SELECT *" +
				" FROM " + NAME_TABLE +
				" ORDER BY " + APPLICABLE + " DESC", 
				null);
		while(c.moveToNext()) {
			Study study = new Study();
			study.setId(c.getInt(c.getColumnIndexOrThrow(ID)));
			study.setReference(c.getInt(c.getColumnIndexOrThrow(REFERENCE)));
			study.setJeh(c.getInt(c.getColumnIndexOrThrow(JEH)));
			study.setName(c.getString(c.getColumnIndexOrThrow(NAME)));
			study.setStatus(Status.valueOf(c.getString(c.getColumnIndexOrThrow(STATUS))));
			study.setType(c.getString(c.getColumnIndexOrThrow(TYPE)));
			study.setTypeId(c.getInt(c.getColumnIndexOrThrow(TYPE_ID)));
			study.setDetails(c.getString(c.getColumnIndexOrThrow(DETAILS)));
			list.add(study);
		}
		
		return list;
	}

	/**
	 * retrieve a specific study from the database
	 * @param reference Reference of the study to retrieve
	 * @return Study
	 */
	public Study get(int reference) {
		Cursor c = db.rawQuery(
				"SELECT *" +
				" FROM " + NAME_TABLE + "" +
				" WHERE " + REFERENCE + " = ?", 
				new String[]{String.valueOf(reference)});
		if(c == null || c.getCount() == 0) {
			return null;
		}
		c.moveToNext();
		Study study = new Study();
		study.setId(c.getInt(c.getColumnIndexOrThrow(ID)));
		study.setReference(c.getInt(c.getColumnIndexOrThrow(REFERENCE)));
		study.setJeh(c.getInt(c.getColumnIndexOrThrow(JEH)));
		study.setName(c.getString(c.getColumnIndexOrThrow(NAME)));
		study.setStatus(Status.valueOf(c.getString(c.getColumnIndexOrThrow(STATUS))));
		study.setType(c.getString(c.getColumnIndexOrThrow(TYPE)));
		study.setTypeId(c.getInt(c.getColumnIndexOrThrow(TYPE_ID)));
		study.setDetails(c.getString(c.getColumnIndexOrThrow(DETAILS)));
		return study;
	}
}
