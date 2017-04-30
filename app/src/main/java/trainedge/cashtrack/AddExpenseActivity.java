package trainedge.cashtrack;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

import static android.view.View.GONE;

public class AddExpenseActivity extends AppCompatActivity {

    private Spinner spinCategory;
    private EditText etExpenseTitle;
    private EditText etExpAmount;
    private Button btnDatePick;
    private ExpenseDatabaseAdapter dbAdapter;
    private boolean error;
    private FloatingActionButton fab;
    private int year;
    private int month;
    private int dayOfMonth;
    private boolean customDate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dbAdapter = new ExpenseDatabaseAdapter(this).open();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    addExpense();
                } catch (Exception e) {
                    Toast.makeText(AddExpenseActivity.this, "Enter data please", Toast.LENGTH_SHORT).show();
                }


            }
        });

        spinCategory = (Spinner) findViewById(R.id.spinCategory);
        etExpenseTitle = (EditText) findViewById(R.id.etExpenseTitle);
        etExpAmount = (EditText) findViewById(R.id.etExpAmount);
        etExpenseTitle.setVisibility(GONE);
        btnDatePick = (Button) findViewById(R.id.btnDatePick);
        int monthdisp = Calendar.getInstance().get(Calendar.MONTH) + 1;
        btnDatePick.setText(Calendar.getInstance().get(Calendar.YEAR) + "/"
                + monthdisp + "/"
                + Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        btnDatePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        spinCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 6) {
                    etExpenseTitle.setVisibility(View.VISIBLE);
                    etExpenseTitle.setHint("Expense Title");
                } else {
                    etExpenseTitle.setHint("");
                    etExpenseTitle.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                btnDatePick.setText("" + dayOfMonth + "/" + (month + 1) + "/" + year);
                AddExpenseActivity.this.year = year;
                AddExpenseActivity.this.month = month + 1;
                AddExpenseActivity.this.dayOfMonth = dayOfMonth;
                customDate = true;
            }
        }, Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void addExpense() throws Exception {

        error = false;
        String expensetitle = etExpenseTitle.getText().toString();
        String expamount = etExpAmount.getText().toString();
        String category = getResources().getStringArray(R.array.categories)[spinCategory.getSelectedItemPosition()];
        if (category.equals("Others")) {
            if (expensetitle.isEmpty() || expensetitle.length() < 3) {
                etExpenseTitle.setError("Required valid Expense Title");
                error = true;
            }
        } else {
            expensetitle = "item";
        }
        String amount = etExpAmount.getText().toString();
        if (amount.isEmpty() || expamount.length() < 0) {
            etExpAmount.setError("Required valid amount ");
            error = true;
        }
        if (category.equals("Others")) {
            category = expensetitle;
        }

        ExpenseDatabaseAdapter dbAdapter = new ExpenseDatabaseAdapter(this).open();

        long rowId;
        if (customDate) {
            rowId = dbAdapter.addExpense(category, expensetitle, Double.parseDouble(expamount), dayOfMonth, month, year);
        } else {
            rowId = dbAdapter.addExpense(category, expensetitle, Double.parseDouble(expamount));
        }
        if (rowId != -1) {
            doAwesomeAnimation();
            resetUI();
        } else {
            Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show();
        }

    }

    private void resetUI() {
        customDate = false;
        int monthdisp = Calendar.getInstance().get(Calendar.MONTH) + 1;
        btnDatePick.setText(Calendar.getInstance().get(Calendar.YEAR) + "/" + monthdisp + "/" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        spinCategory.setSelection(0);
        etExpenseTitle.setHint("");
        etExpenseTitle.setVisibility(GONE);
    }

    private void doAwesomeAnimation() {
        fab.animate().rotationYBy(360).setDuration(500).setInterpolator(new AccelerateInterpolator()).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                Toast.makeText(AddExpenseActivity.this, "saved", Toast.LENGTH_SHORT).show();

            }
        });
    }

}
