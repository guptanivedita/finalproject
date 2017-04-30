package trainedge.cashtrack;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * This class creates the relation with the SQLite Database Helper
 * through which queries can be SQL called.
 *
 * @author Zaid
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    // The database name and version
    private static final String DB_NAME = "expense_db";
    private static final int DB_VERSION = 3;
    // The database user table
    private static final String DB_USER_TABLE = "create table user (_id integer primary key autoincrement, "
            + "name text not null,"
            + "occupation text not null,"
            + "email text not null,"
            + "phone text not null,"
            + "salary double not null,"
            + " password text not null);";

    private static final String DB_EXPENSE_TABLE="create table expenses (_id integer primary key autoincrement, "
            + "category TEXT not null,"
            + "title TEXT not null,"
            + "amount DOUBLE not null,"
            + "day INTEGER not null,"
            + "month INTEGER not null,"
            + "year INTEGER not null,"
            + "hour INTEGER not null,"
            + "minute INTEGER not null,"
            + "created_on datetime default current_timestamp);";
    /**
     * Database Helper constructor.
     *
     * @param context
     */
    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    /**
     * Creates the database tables.
     */
    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DB_USER_TABLE);
        database.execSQL(DB_EXPENSE_TABLE);
    }

    /**
     * Handles the table version and the drop of a table.
     */
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.w(DatabaseHelper.class.getName(),
                "Upgrading databse from version" + oldVersion + "to "
                        + newVersion + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS user");
        database.execSQL("DROP TABLE IF EXISTS expenses");
        onCreate(database);
    }
}
