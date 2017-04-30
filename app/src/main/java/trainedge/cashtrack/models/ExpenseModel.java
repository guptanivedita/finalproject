package trainedge.cashtrack.models;

import android.database.Cursor;

import java.util.Calendar;

import static trainedge.cashtrack.ExpenseDatabaseAdapter.*;

/**
 * Created by xaidi on 06-04-2017.
 */

public class ExpenseModel {
    int year;
    int month;
    int day;
    long id;
    String category;
    String title;
    double amt;

    public long getId() {
        return id;
    }

    public ExpenseModel(Cursor cursor) {
        amt = cursor.getDouble(cursor.getColumnIndex(COL_AMOUNT));
        title = cursor.getString(cursor.getColumnIndex(COL_TITLE));
        category = cursor.getString(cursor.getColumnIndex(COL_CATEGORY));
        day = cursor.getInt(cursor.getColumnIndex(COL_DAY));
        month = cursor.getInt(cursor.getColumnIndex(COL_MONTH));
        year = cursor.getInt(cursor.getColumnIndex(COL_YEAR));
        id=cursor.getLong(cursor.getColumnIndex(COL_ID));


    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }



    public String getCategory() {
        return category;
    }

    public String getTitle() {
        return category;
    }

    public double getAmt() {
        return amt;
    }

    @Override
    public String toString() {
        return "100";
    }
}
