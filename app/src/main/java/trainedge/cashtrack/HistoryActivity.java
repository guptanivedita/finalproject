package trainedge.cashtrack;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import trainedge.cashtrack.models.ExpenseModel;

import static trainedge.cashtrack.CalenderActivity.DAY;
import static trainedge.cashtrack.CalenderActivity.MONTH;
import static trainedge.cashtrack.CalenderActivity.YEAR;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView rvExpenses;
    private ArrayList<ExpenseModel> expenseList;
    private Cursor expenseCursor;
    private ExpenseAdapter adapter;
    private int day;
    private int month;
    private int year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        try {
            Intent intent = getIntent();
            day = intent.getIntExtra(DAY, 0);
            month = intent.getIntExtra(MONTH, 0);
            year = intent.getIntExtra(YEAR, 0);

            toolbarLayout.setTitle(day + "/" + month + "/" + year);
            if (intent!=null){
                String str = intent.getExtras().getString("passdate");


            }
        } catch (Exception e) {
            finish();
        }
        rvExpenses = (RecyclerView) findViewById(R.id.rvExpenses);
        expenseList = new ArrayList<>();
        cursorToList();
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvExpenses.setLayoutManager(layoutManager);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rvExpenses.scrollToPosition(expenseList.size() - 1);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    private void cursorToList() {

        ExpenseDatabaseAdapter dbAdapter = new ExpenseDatabaseAdapter(this).open();
        expenseCursor = new ExpenseDatabaseAdapter(this).open().getExpenseByDate(day, month, year);

        if (expenseCursor != null) {
            if (expenseCursor.getCount() > 0) {
                double total = 0;
                while (expenseCursor.moveToNext()) {
                    expenseList.add(new ExpenseModel(expenseCursor));
                }
                adapter = new ExpenseAdapter(expenseList);
                rvExpenses.setAdapter(adapter);
            }
        }
    }
}
