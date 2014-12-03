package fr.ouestinsa.db;

import android.content.Context;
import fr.ouestinsa.db.properties.AccountDAO;
import fr.ouestinsa.db.sqlite.StudyDAO;

public class PropertiesDAOFactory extends DAOFactory {
	@Override
	public AccountDAO getAccountDAO(Context c) {
		return new AccountDAO(c);
	}

	@Override
	public StudyDAO getStudyDAO(Context c) {
		return null;
	}
}
