package trainedge.cashtrack;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import static android.provider.ContactsContract.CommonDataKinds.Email.CONTENT_URI;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText edtName;
    private EditText edtEmail;
    private EditText edtPass;
    private EditText edtConPass;
    private EditText edtPhoneno;
    private EditText edtSal;
    private EditText edtOccupation;
    private Button signup;
    private UserDatabaseAdapter dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        SharedPreferences settings = getSharedPreferences(Constants.MY_PREFS, 0);
        settings.edit().putLong("uid", 0).apply();


        edtName = (EditText) findViewById(R.id.edtname);
        edtEmail = (EditText) findViewById(R.id.edtemail);
        edtPass = (EditText) findViewById(R.id.edtpass);
        edtConPass = (EditText) findViewById(R.id.edtPassConfirm);
        edtPhoneno = (EditText) findViewById(R.id.edtphoneno);
        edtSal = (EditText) findViewById(R.id.edtsal);
        edtOccupation = (EditText) findViewById(R.id.etoccupation);
        dbHelper = new UserDatabaseAdapter(this);
        dbHelper.open();
        signup = (Button) findViewById(R.id.btnsignup);
        signup.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        boolean error = false;
        String name = edtName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String pass = edtPass.getText().toString().trim();
        String confirmPass = edtConPass.getText().toString().trim();
        String phone = edtPhoneno.getText().toString().trim();
        String occupation = edtOccupation.getText().toString().trim();
        String salary = edtSal.getText().toString().trim();
        if (name.isEmpty() || name.length() < 3) {
            edtName.setError("Valid Username is required");
            error = true;
        }
        if (email.isEmpty() || email.length() < 3) {
            edtEmail.setError("Valid Username is required");
            error = true;
        }
        if (pass.isEmpty() || pass.length() < 3) {
            edtPass.setError("Valid Username is required");
            error = true;
        }
        if (confirmPass.isEmpty() || confirmPass.length() < 3) {
            edtConPass.setError("Valid Username is required");
            error = true;
        }
        if (phone.isEmpty() || phone.length() < 3) {
            edtPhoneno.setError("Valid Username is required");
            error = true;
        }
        if (occupation.isEmpty() || occupation.length() < 3) {
            edtOccupation.setError("Valid Username is required");
            error = true;
        }
        if (salary.isEmpty() || salary.length() < 3) {
            edtSal.setError("Valid Username is required");
            error = true;
        }
        if (!confirmPass.equals(pass)) {
            edtConPass.setError("password should be same");
            error = true;
        }
        //final
        if (!error) {
            double salaryVal = Double.parseDouble(salary);
            savetoDB(name, pass, email, phone, salaryVal, occupation);
        }
    }

    private void savetoDB(String name, String pass, String email, String phone, double salaryVal, String occupation) {
        //Create the new user.
        long id = dbHelper.createUser(name, pass, email, phone, salaryVal, occupation);
        if (id > 0) {
            saveLoggedInUId(id,email,pass);
            BackToLogin();
        } else {
            Snackbar.make(signup, "Failed to create new username", Snackbar.LENGTH_SHORT).show();
            ClearForm();
        }

    }




    private void saveLoggedInUId(long id, String email, String password) {
        SharedPreferences settings = getSharedPreferences(Constants.MY_PREFS, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong("uid", id);
        editor.putString("email", email);
        editor.putString("password", password);
        editor.apply();
    }


    /**
     * Clears the registration fields.
     */
    private void ClearForm() {

        edtPass.setText("");
        edtSal.setText("");
        edtOccupation.setText("");
        edtEmail.setText("");
        edtName.setText("");
        edtConPass.setText("");
        edtPhoneno.setText("");
    }

    /**
     * Takes user back to login.
     */
    private void BackToLogin() {
        finish();
    }

}
