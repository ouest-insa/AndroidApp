package fr.ouestinsa.db.properties;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Properties;

import android.content.Context;
import fr.ouestinsa.object.Account;


public class AccountDAO {/*
	protected SQLiteDatabase db = null;
	protected MySQLiteHelper helper = null;

	protected SparseArray<Account> cache;
	
	public static final String NAME_TABLE = "Account";
	
	public static final String FIRSTNAME = "firstname";
	public static final String LASTNAME = "lastname";
	public static final String DEPARTMENT = "department";
	public static final String MAIL_INSA = "mailINSA";
	public static final String VIADEO = "viadeo";
	public static final String LINKEDIN = "linkedin";
	
	public static final String CREATE_TABLE = 
			"CREATE TABLE " + NAME_TABLE + " ("
					+ FIRSTNAME + " TEXT, "
					+ LASTNAME + " TEXT, "
					+ DEPARTMENT + " TEXT, "
					+ MAIL_INSA + " TEXT, "
					+ VIADEO + " TEXT, "
					+ LINKEDIN + " TEXT, "
					+ "PRIMARY KEY (" + FIRSTNAME + ", " + LASTNAME + ")"
				+ ");";
		public static final String CREATE_INDEX_1 = 
				"CREATE INDEX fk_" + NAME_TABLE + "_1 ON " + NAME_TABLE + " (" + FIRSTNAME + " ASC);";
		public static final String CREATE_INDEX_2 = 
				"CREATE INDEX fk_" + NAME_TABLE + "_2 ON " + NAME_TABLE + " (" + LASTNAME + " ASC);";
		public static final String DELETE_TABLE = 
				"DROP TABLE " + NAME_TABLE + ";";

	public AccountDAO(Context c) {
		cache = new SparseArray<Account>();
		helper = MySQLiteHelper.getInstance(c);
	}

	public void open() {
		db = helper.getWritableDatabase();
	}

	public void close() {
		helper.close();
	}*/
	String FILE_NAME = "account.properties";
	
	public static final String FIRSTNAME = "firstname";
	public static final String LASTNAME = "lastname";
	public static final String DEPARTMENT = "department";
	public static final String MAIL_INSA = "mailINSA";
	public static final String VIADEO = "viadeo";
	public static final String LINKEDIN = "linkedin";
	
	private Context context;
	
	public AccountDAO(Context c) {
		context = c;
	}
	
	public boolean save(Account account) throws IOException {
		//OutputStream os = new FileOutputStream(FILE_NAME);
		OutputStream os = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
		
		Properties prop = new Properties();
		prop.setProperty(FIRSTNAME, account.getFirstname());
		prop.setProperty(LASTNAME, account.getLastname());
		prop.setProperty(DEPARTMENT, account.getDepartment());
		prop.setProperty(MAIL_INSA, account.getMailINSA());
		prop.setProperty(VIADEO, account.getViadeo().toString());
		prop.setProperty(LINKEDIN, account.getLinkedIn().toString());
		prop.store(os, "Account properties");
		
		os.close();
		return false;
	}
	
	public Account load() throws IOException {
		//InputStream is = new FileInputStream(FILE_NAME);
		InputStream is = context.openFileInput(FILE_NAME);
		
		Properties prop = new Properties();
		prop.load(is);
		
		Account account = new Account();
		account.setFirstname(prop.getProperty(FIRSTNAME));
		account.setLastname(prop.getProperty(LASTNAME));
		account.setDepartment(prop.getProperty(DEPARTMENT));
		account.setMailINSA(prop.getProperty(MAIL_INSA));
		account.setViadeo(new URL(prop.getProperty(VIADEO)));
		account.setLinkedIn(new URL(prop.getProperty(LINKEDIN)));
		
		is.close();
		return account;
	}
}
