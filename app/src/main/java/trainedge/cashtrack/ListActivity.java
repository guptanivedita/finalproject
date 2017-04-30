package trainedge.cashtrack;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import trainedge.cashtrack.models.ExpenseModel;

import static trainedge.cashtrack.ExpenseDatabaseAdapter.COL_AMOUNT;
import static trainedge.cashtrack.ExpenseDatabaseAdapter.COL_CATEGORY;
import static trainedge.cashtrack.ExpenseDatabaseAdapter.COL_MONTH;
import static trainedge.cashtrack.ExpenseDatabaseAdapter.COL_YEAR;

public class ListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SharedPreferences expPref;
    private ArrayList<ExpenseModel> expenseList;
    private TextView tvAmtRemaining;
    private ExpenseAdapter adapter;
    private RecyclerView rvExpenses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchNewExpenseActivity();
            }
        });

        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                launchFaqActivity();
                return true;
            }
        });
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        expPref = getSharedPreferences("exp_pref", MODE_PRIVATE);
        tvAmtRemaining = (TextView) findViewById(R.id.tvRemainingAmount);
        rvExpenses = (RecyclerView) findViewById(R.id.rvExpenses);
        //setupExpensesRecyclerView();
    }

    private void setupExpensesRecyclerView() {
        expenseList = new ArrayList<>();

        rvExpenses.setLayoutManager(new LinearLayoutManager(this));

        cursorToList();


    }

    @Override
    protected void onResume() {
        super.onResume();

        setupExpensesRecyclerView();
    }

    private void cursorToList() {

        ExpenseDatabaseAdapter dbAdapter = new ExpenseDatabaseAdapter(this).open();

        Cursor expenseCursor = dbAdapter.getAllExpense();
        double totalExpenseThisMonth = calculateMonthTotal(expenseCursor);
        float budget = expPref.getFloat("budget", 0.0f);
        if (budget > 0) {
            double remainingBudget = budget - totalExpenseThisMonth;
            tvAmtRemaining.setText(budget + " - " + totalExpenseThisMonth + " = " + remainingBudget);
            expPref.edit().putFloat("remaining", (float) remainingBudget).apply();
        }
        dbAdapter.close();
        expenseCursor = null;
        expenseCursor = new ExpenseDatabaseAdapter(this).open().getAllExpense();

        if (expenseCursor != null) {
            if (expenseCursor.getCount() > 0) {
                double total = 0;
                while (expenseCursor.moveToNext()) {
                    expenseList.add(new ExpenseModel(expenseCursor));
                }
                adapter = new ExpenseAdapter(expenseList);
                rvExpenses.setAdapter(adapter);
            }
        } else {
            TextView tLabel = new TextView(this);

        }
    }

    private double calculateMonthTotal(Cursor cursor) {
        double currentYear = Calendar.getInstance().get(Calendar.YEAR);
        double currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                double total = 0;
                while (cursor.moveToNext()) {
                    double year = cursor.getInt(cursor.getColumnIndex(COL_YEAR));
                    double month = cursor.getInt(cursor.getColumnIndex(COL_MONTH));
                    if (year == currentYear && month == currentMonth) {
                        double amt = cursor.getDouble(cursor.getColumnIndex(COL_AMOUNT));
                        total += amt;
                    }
                }
                return total;
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return 0;
    }

    private void launchNewExpenseActivity() {
        startActivity(new Intent(this, AddExpenseActivity.class));
    }

    private void launchFaqActivity() {

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settings = new Intent(ListActivity.this, SettingsActivity.class);
            startActivity(settings);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_cal) {
            // Handle the calender action
            Intent cal = new Intent(ListActivity.this, CalenderActivity.class);
            startActivity(cal);
        } else if (id == R.id.nav_graph) {
            Intent graph = new Intent(ListActivity.this, GraphActivity.class);
            startActivity(graph);

        } else if (id == R.id.nav_edit) {
            Intent edit = new Intent(ListActivity.this, EditSalaryActivity.class);
            startActivity(edit);

        } else if (id == R.id.nav_setting) {
            Intent settings = new Intent(ListActivity.this, SettingsActivity.class);
            startActivity(settings);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
