package fr.ouest_insa.db;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import android.content.Context;
import fr.ouest_insa.exception.AccountNotFillException;
import fr.ouest_insa.exception.MailInsaException;
import fr.ouest_insa.object.Account;
import fr.ouest_insa.object.Department;

/**
 * Write and read the properties of an Account in a <i>.properties</i> file.<br>
 * Use the pattern singleton to have only one instance.
 * @see Account
 * @author Loïc Pelleau
 */
public class AccountDAO {
	private static AccountDAO mInstance;
	String FILE_NAME = "account.properties";

	public static final String FIRSTNAME = "firstname";
	public static final String LASTNAME = "lastname";
	public static final String DEPARTMENT = "department";
	public static final String MAIL_INSA = "mailINSA";
	public static final String VIADEO = "viadeo";
	public static final String LINKEDIN = "linkedin";

	private Context context;

	private AccountDAO(Context c) {
		context = c;
	}

	public static AccountDAO getInstance(Context c) {
		if (mInstance == null) {
			mInstance = new AccountDAO(c);
		}
		return mInstance;
	}

	/**
	 * Save the fields of Account in the properties file.
	 * @param account Account to save
	 * @see Account
	 * @throws IOException
	 * @throws AccountNotFillException
	 */
	public void save(Account account) throws IOException,
			AccountNotFillException {
		if (account.getFirstname() == null || account.getFirstname().equals("")
				|| account.getLastname() == null
				|| account.getLastname().equals("")
				|| account.getDepartment() == null
				|| account.getDepartment().toString().equals("")
				|| account.getMailINSA() == null
				|| account.getMailINSA().equals("")) {
			throw new AccountNotFillException("Données de profil non remplies");
		}

		OutputStream os = context.openFileOutput(FILE_NAME,
				Context.MODE_PRIVATE);

		Properties prop = new Properties();
		prop.setProperty(DEPARTMENT, account.getDepartment().toString());
		prop.setProperty(FIRSTNAME, account.getFirstname());
		prop.setProperty(LASTNAME, account.getLastname());
		prop.setProperty(MAIL_INSA, account.getMailINSA());
		try {
			prop.setProperty(VIADEO, account.getViadeo().toString());
		} catch(NullPointerException e) {
		}
		try {
			prop.setProperty(LINKEDIN, account.getLinkedIn().toString());
		} catch(NullPointerException e) {
		}
		prop.store(os, "Account properties");

		os.close();
	}

	/**
	 * Load an Account from the properties file.<br>
	 * throw an exception if the file doesn't exist.
	 * @see Account
	 * @return Account Account loaded from the file
	 * @throws IOException
	 */
	public Account load() throws IOException {
		InputStream is = context.openFileInput(FILE_NAME);

		Properties prop = new Properties();
		prop.load(is);

		Account account = new Account();
		account.setDepartment(Department.valueOf(prop.getProperty(DEPARTMENT)));
		account.setFirstname(prop.getProperty(FIRSTNAME));
		account.setLastname(prop.getProperty(LASTNAME));
		try {
			account.setMailINSA(prop.getProperty(MAIL_INSA));
		} catch (MailInsaException e) {}

		try {
			account.setViadeo(new URL(prop.getProperty(VIADEO)));
		} catch (MalformedURLException e) {}
		try {
			account.setLinkedIn(new URL(prop.getProperty(LINKEDIN)));
		} catch (MalformedURLException e) {}

		is.close();
		return account;
	}
}
