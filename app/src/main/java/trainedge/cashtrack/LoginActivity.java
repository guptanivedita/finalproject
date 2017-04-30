package trainedge.cashtrack;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtId;
    private EditText edtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button btnlogin = (Button) findViewById(R.id.btnlogin);
        TextView forgotpassword = (TextView) findViewById(R.id.txtforgotPassword);
        TextView signup = (TextView) findViewById(R.id.txtSingnup);
        edtId = (EditText) findViewById(R.id.edtID);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        signup.setOnClickListener(this);
        btnlogin.setOnClickListener(this);

        forgotpassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnlogin:

                if (validateUserLogin()) {
                    Intent signup = new Intent(LoginActivity.this, ListActivity.class);
                    startActivity(signup);
                    finish();
                }
                break;
            case R.id.txtSingnup:

                    Intent login = new Intent(LoginActivity.this, SignupActivity.class);
                    startActivity(login);
                break;
            case R.id.txtforgotPassword:
                SharedPreferences settings = getSharedPreferences(Constants.MY_PREFS, MODE_PRIVATE);

                if (settings.contains("email")) {
                    String savedEmail = settings.getString("email", "");
                    String savedPassword = settings.getString("password", "");
                    composeEmail(new String[]{savedEmail}, "Password recovery mail", savedPassword);
                } else {
                    Snackbar.make(edtId, "You have not registered yet", Snackbar.LENGTH_INDEFINITE).show();
                }
                break;
        }
    }

    public void composeEmail(String[] addresses, String subject, String msg) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_HTML_TEXT, "<h1>" + msg + "</h1>");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private boolean validateUserLogin() {

        SharedPreferences settings = getSharedPreferences(Constants.MY_PREFS, MODE_PRIVATE);
        String email = edtId.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        if (email.isEmpty() || email.length() < 5 || !email.contains("@")) {
            edtId.setError("Please enter a valid email address");
            return false;
        } else if (password.isEmpty() || password.length() < 3) {
            edtPassword.setError("Please enter a valid password");
            return false;
        } else {
            String savedEmail = settings.getString("email", "");
            if (!email.equals(savedEmail)) {
                Snackbar.make(edtId, "email address does not match", Snackbar.LENGTH_INDEFINITE).show();
                return false;
            } else {
                boolean savedPassword = password.equals(settings.getString("password", ""));
                if (!savedPassword) {
                    Snackbar.make(edtId, "password does not match", Snackbar.LENGTH_INDEFINITE).show();
                    return false;
                } else {
                    return true;
                }
            }
        }

    }
}
