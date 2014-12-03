package fr.ouestinsa.db;

import android.content.Context;
import fr.ouestinsa.db.properties.AccountDAO;
import fr.ouestinsa.db.sqlite.StudyDAO;

public class SQLiteDAOFactory extends DAOFactory {
	@Override
	public StudyDAO getStudyDAO(Context c) {
		return new StudyDAO(c);
	}

	@Override
	public AccountDAO getAccountDAO(Context c) {
		return null;
	}
}
