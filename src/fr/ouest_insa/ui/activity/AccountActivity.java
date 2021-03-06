package fr.ouest_insa.ui.activity;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import fr.ouest_insa.R;
import fr.ouest_insa.db.AccountDAO;
import fr.ouest_insa.exception.AccountNotFillException;
import fr.ouest_insa.exception.MailInsaException;
import fr.ouest_insa.object.Account;
import fr.ouest_insa.object.Department;

/**
 * This class allow the user to fill his profile informations.
 * 
 * @author Lo�c Pelleau
 */
public class AccountActivity extends ActionBarActivity {
	private Department departments[] = Department.values();
	private Spinner department;
	private EditText firstname;
	private EditText lastname;
	private EditText mailINSA;
	private EditText viadeo;
	private EditText linkedIn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		department = (Spinner) findViewById(R.id.department);
		firstname = (EditText) findViewById(R.id.firstname);
		lastname = (EditText) findViewById(R.id.lastname);
		mailINSA = (EditText) findViewById(R.id.mailINSA);
		viadeo = (EditText) findViewById(R.id.viadeo);
		linkedIn = (EditText) findViewById(R.id.linkedIn);

		@SuppressWarnings({ "unchecked", "rawtypes" })
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.spinner_item,
				new ArrayList(Arrays.asList(departments)));

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		department.setAdapter(adapter);

		Account account = new Account();

		// Try to load the accound informations
		AccountDAO accountDAO = AccountDAO.getInstance(this);
		try {
			account = accountDAO.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(account.getDepartment() != null) {
			for (int i = 0; i < departments.length; i++) {
				if (departments[i].equals(account.getDepartment())) {
					department.setSelection(i);
					break;
				}
			}
		}
		firstname.setText(account.getFirstname() == null ? "" : account
				.getFirstname().toString());
		lastname.setText(account.getLastname() == null ? "" : account
				.getLastname().toString());
		mailINSA.setText(account.getMailINSA() == null ? "" : account
				.getMailINSA().toString());
		viadeo.setText(account.getViadeo() == null ? "" : account.getViadeo()
				.toString());
		linkedIn.setText(account.getLinkedIn() == null ? "" : account
				.getLinkedIn().toString());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.account, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == android.R.id.home) {
			finish();
			return true;
		} else if (itemId == R.id.save) {
			try {
				// Create a new Account object with te new informations
				Account account = new Account();
				account.setDepartment(departments[department
						.getSelectedItemPosition()]);
				account.setFirstname(firstname.getText().toString());
				account.setLastname(lastname.getText().toString());
				account.setMailINSA(mailINSA.getText().toString());
				if (viadeo.getText() != null
						&& !viadeo.getText().toString().equals("")) {
					account.setViadeo(new URL(viadeo.getText().toString()));
				}
				if (linkedIn.getText() != null
						&& !linkedIn.getText().toString().equals("")) {
					account.setLinkedIn(new URL(linkedIn.getText().toString()));
				}

				// Save the Accound on the phone with the DAO
				AccountDAO accountDAO = AccountDAO.getInstance(this);
				accountDAO.save(account);

				Toast.makeText(this, R.string.succed_save_account,
						Toast.LENGTH_SHORT).show();
				finish();
			} catch (AccountNotFillException e) {
				Toast.makeText(this, R.string.error_account_not_fill,
						Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			} catch (MalformedURLException e) {
				Toast.makeText(this, R.string.error_malformed_url,
						Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			} catch (MailInsaException e) {
				Toast.makeText(this, R.string.error_mail_insa,
						Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}
}