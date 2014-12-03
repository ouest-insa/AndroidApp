package fr.ouestinsa.db.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.SparseArray;
import fr.ouestinsa.object.Study;

public class StudyDAO implements DAO {
	protected SQLiteDatabase db = null;
	protected MySQLiteHelper helper = null;
	
	protected SparseArray<Study> cache;
	
	public static final String NAME_TABLE = "Study";
	
	public static final String ID = "id";
	public static final String NAME = "name";
	public static final String DESCRIPTION = "description";
	public static final String LOGO = "logo";
	public static final String DEPARTMENT = "department";
	public static final String SKILL = "skill";
	public static final String CREATED_DATE = "created_date";
	public static final String STARTING_DATE = "starting_date";
	public static final String ENDING_DATE = "ending_date";
	public static final String STUDENT_NUMBER = "student_number";
	public static final String ACTIVE = "active";
	public static final String ACTIVE_YES = "1";
	public static final String ACTIVE_NO = "0";
	
	public static final String CREATE_TABLE = 
			"CREATE TABLE " + NAME_TABLE + " ("
					+ ID + " INTEGER NOT NULL UNIQUE, "
					+ NAME + " TEXT NOT NULL, "
					+ DESCRIPTION + " TEXT NOT NULL, "
					+ LOGO + " TEXT, "
					+ DEPARTMENT + " TEXT NOT NULL, "
					+ SKILL + " TEXT NOT NULL, "
					+ CREATED_DATE + " INTEGER NOT NULL, "
					+ STARTING_DATE + " INTEGER NOT NULL, "
					+ ENDING_DATE + " INTEGER NOT NULL, "
					+ STUDENT_NUMBER + " INTEGER NOT NULL, "
					+ ACTIVE + " INTEGER NOT NULL, "
					+ "PRIMARY KEY (" + ID + ")"
				+ ");";
		public static final String CREATE_INDEX_1 = 
				"CREATE INDEX fk_" + NAME_TABLE + "_1 ON " + NAME_TABLE + " (" + ID + " ASC);";
		public static final String DELETE_TABLE = 
				"DROP TABLE " + NAME_TABLE + ";";

	public StudyDAO(Context c) {
		cache = new SparseArray<Study>();
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
		values.put(NAME, study.getName());
		values.put(DESCRIPTION, study.getDescription());
		values.put(LOGO, study.getLogo());
		values.put(DEPARTMENT, study.getDepartment());
		values.put(SKILL, study.getSkill());
		values.put(CREATED_DATE, study.getCreated_date());
		values.put(STARTING_DATE, study.getStarting_date());
		values.put(ENDING_DATE, study.getEnding_date());
		values.put(STUDENT_NUMBER, study.getStudent_number());
		values.put(ACTIVE, (study.isActive() ? 1 : 0));
		
		if(db.insert(NAME_TABLE, null, values) == -1) {
			return false;
		} else {
			return true;
		}
	}
}
