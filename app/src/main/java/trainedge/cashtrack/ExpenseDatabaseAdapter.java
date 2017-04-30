package trainedge.cashtrack;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Hi ! HARSH on 08-Apr-17.
 */

public class ExpenseDatabaseAdapter {
    //Table name
    public static final String TABLE_EXPENSE = "expenses";
    // Table id
    public static final String COL_ID = "_id";
    //Table names and columns
    public static final String COL_CATEGORY = "category";
    public static final String COL_TITLE = "title";
    public static final String COL_AMOUNT = "amount";
    public static final String COL_DAY = "day";
    public static final String COL_MONTH = "month";
    public static final String COL_YEAR = "year";
    public static final String COL_HOUR = "hour";
    public static final String COL_MINUTE = "minute ";
    private static final java.lang.String CREATED_ON = "created_on";


    private Context context;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;


    public ExpenseDatabaseAdapter(Context context) {
        this.context = context;
    }

    public ExpenseDatabaseAdapter open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    /**
     * Closes the database.
     */
    public void close() {
        dbHelper.close();
    }

    //Add Expense Method
    public long addExpense(String category, String title, double amount) {

        ContentValues values = getContentValues(category, title, amount);
        return database.insert(TABLE_EXPENSE, null, values);

    }

    @NonNull
    private ContentValues getContentValues(String category, String title, double amount) {
        ContentValues values = new ContentValues();
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int minute = Calendar.getInstance().get(Calendar.MINUTE);
        values.put(COL_CATEGORY, category);
        values.put(COL_TITLE, title);
        values.put(COL_AMOUNT, amount);
        values.put(COL_YEAR, year);
        values.put(COL_MONTH, month);
        values.put(COL_DAY, day);
        values.put(COL_HOUR, hour);
        values.put(COL_MINUTE, minute);
        return values;
    }

    // Edit Expense
    public int editExpense(long id, String category, String title, double amount) {
        ContentValues values = getContentValues(category, title, amount);
        String where = COL_ID + "=?";
        String[] whereArgs = new String[]{String.valueOf(id)};
        return database.update(TABLE_EXPENSE, values, where, whereArgs);
    }

    //Calculate Total Expenditure
    public double calculateTotalExpenditure() {
        Cursor cursor = database.query(TABLE_EXPENSE, null, null, null, null, null, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                double total = 0;
                while (cursor.moveToNext()) {
                    double amt = cursor.getDouble(cursor.getColumnIndex(COL_AMOUNT));
                    total += amt;
                }
                return total;
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return 0.00;
    }

    //Get All Expenses
    public Cursor getAllExpense() {
        return database.query(TABLE_EXPENSE, new String[]{COL_ID,COL_AMOUNT,COL_YEAR,COL_DAY,COL_MONTH,COL_TITLE,COL_CATEGORY}, null, null, null, null, CREATED_ON + " DESC");
    }

    //Get Expense By Date
    public Cursor getExpenseByDate(int day, int month, int year) {
        String where = COL_DAY + "=? and " + COL_MONTH + "=? and " + COL_YEAR + " = ?";

        String[] whereArgs = new String[]{String.valueOf(day), String.valueOf(month), String.valueOf(year)};
        return database.query(TABLE_EXPENSE, null, where, whereArgs, null, null, null, null);
    }

    //Get Expense By Category
    public Cursor getExpenseByCategory(String category) {
        String where = COL_CATEGORY + " = ?";
        String[] whereArgs = new String[]{category};
        return database.query(TABLE_EXPENSE, null, where, whereArgs, null, null, null, null);
    }


    //Delete Expense
    public int deleteExpense(long id) {
        String where = COL_ID + "=?";
        String[] whereArgs = new String[]{String.valueOf(id)};
        return database.delete(TABLE_EXPENSE, where, whereArgs);
    }


    public long addExpense(String category, String title, double amount, int day, int month, int year) {
        ContentValues values = new ContentValues();
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int minute = Calendar.getInstance().get(Calendar.MINUTE);
        values.put(COL_CATEGORY, category);
        values.put(COL_TITLE, title);
        values.put(COL_AMOUNT, amount);
        values.put(COL_YEAR, year);
        values.put(COL_MONTH, month);
        values.put(COL_DAY, day);
        values.put(COL_HOUR, hour);
        values.put(COL_MINUTE, minute);
        return database.insert(TABLE_EXPENSE, null, values);
    }

    public void deleteAll() {
        database.execSQL("DELETE from "+TABLE_EXPENSE);
    }
}
