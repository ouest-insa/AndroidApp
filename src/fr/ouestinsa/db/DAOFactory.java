package fr.ouestinsa.db;

import android.content.Context;
import fr.ouestinsa.db.properties.AccountDAO;
import fr.ouestinsa.db.sqlite.StudyDAO;

public abstract class DAOFactory {
	public static final int SQLITE = 0;
	public static final int PROPERTIES = 1;

	public abstract StudyDAO getStudyDAO(Context c);

	public abstract AccountDAO getAccountDAO(Context c);

	public static DAOFactory getFactory(int type) {
		switch (type) {
			case SQLITE:
				return new SQLiteDAOFactory();
			case PROPERTIES:
				return new PropertiesDAOFactory();
			default:
				return null;
		}
	}
}