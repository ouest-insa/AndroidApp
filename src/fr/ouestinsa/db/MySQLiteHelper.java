package fr.ouestinsa.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(StudyDAO.DELETE_TABLE);
		onCreate(db);
	}

	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onUpgrade(db, oldVersion, newVersion);
	}
}
