package fr.ouestinsa.db.sqlite;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import fr.ouestinsa.object.Status;
import fr.ouestinsa.object.Study;

public class StudyDAO implements DAO {
	protected SQLiteDatabase db = null;
	protected MySQLiteHelper helper = null;
	
	public static final String NAME_TABLE = "Study";
	
	public static final String ID = "id";
	public static final String JEH = "jeh";
	public static final String NAME = "name";
	public static final String STATUS = "status";
	public static final String APPLICABLE = "applicable";
	public static final String TYPE = "type";
	public static final String TYPE_ID = "type_id";
	public static final String DETAILS = "details";
	
	public static final String CREATE_TABLE = 
			"CREATE TABLE " + NAME_TABLE + " ("
					+ ID + " INTEGER NOT NULL UNIQUE, "
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

	public StudyDAO(Context c) {
		helper = MySQLiteHelper.getInstance(c);
	}

	public void open() {
		db = helper.getWritableDatabase();
	}
	
	public void close() {
		helper.close();
	}

	public void clear() {
		db.delete(NAME_TABLE, null, null);
	}

	public boolean add(Study study) {
		ContentValues values = new ContentValues();
		values.put(ID, study.getId());
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

	public int addDetails(int id, String details) {
		ContentValues values = new ContentValues();
		values.put(DETAILS, details);
		
		return db.update(NAME_TABLE, values, ID + " = ?", new String[] {String.valueOf(id)});
	}

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

	public Study get(int id) {
		Cursor c = db.rawQuery(
				"SELECT *" +
				" FROM " + NAME_TABLE + "" +
				" WHERE " + ID + " = ?", 
				new String[]{String.valueOf(id)});
		if(c == null || c.getCount() == 0) {
			return null;
		}
		c.moveToNext();
		Study study = new Study();
		study.setId(c.getInt(c.getColumnIndexOrThrow(ID)));
		study.setJeh(c.getInt(c.getColumnIndexOrThrow(JEH)));
		study.setName(c.getString(c.getColumnIndexOrThrow(NAME)));
		study.setStatus(Status.valueOf(c.getString(c.getColumnIndexOrThrow(STATUS))));
		study.setType(c.getString(c.getColumnIndexOrThrow(TYPE)));
		study.setTypeId(c.getInt(c.getColumnIndexOrThrow(TYPE_ID)));
		study.setDetails(c.getString(c.getColumnIndexOrThrow(DETAILS)));
		return study;
	}
}
