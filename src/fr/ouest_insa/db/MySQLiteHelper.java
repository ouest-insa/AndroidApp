package fr.ouest_insa.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * This class create a new database if not already exist.<br>
 * Change the database version when modify the structure to don't crash the application.<br>
 * Use the pattern singleton to have only one instance.
 * @author Lo�c Pelleau
 */
public class MySQLiteHelper extends SQLiteOpenHelper {
	public static final int VERSION_DATABASE = 1;
	public static final String NAME_DATABASE = "Ouest-INSA.db";

	private static MySQLiteHelper instance = null;

	private MySQLiteHelper(Context context) {
		super(context, NAME_DATABASE, null, VERSION_DATABASE);
	}

	public static MySQLiteHelper getInstance(Context context) {
		if (instance == null) {
			instance = new MySQLiteHelper(context);
		}
		return instance;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		createStudy(db);
	}

	private void createStudy(SQLiteDatabase db) {
		db.execSQL(StudyDAO.CREATE_TABLE);
		db.execSQL(StudyDAO.CREATE_INDEX_1);
		db.execSQL(ApplicableDAO.CREATE_TABLE);
		db.execSQL(ApplicableDAO.CREATE_INDEX_1);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(StudyDAO.DELETE_TABLE);
		db.execSQL(ApplicableDAO.DELETE_TABLE);
		onCreate(db);
	}

	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onUpgrade(db, oldVersion, newVersion);
	}
}
