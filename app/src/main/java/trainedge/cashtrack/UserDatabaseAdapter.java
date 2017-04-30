package trainedge.cashtrack;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Adapts the database to deal with the front end.
 *
 * @author Zaid
 */
public class UserDatabaseAdapter {
    //Table name
    private static final String LOGIN_TABLE = "user";
    //Table unique id
    public static final String COL_ID = "_id";
    //Table username and password columns
    public static final String COL_USERNAME = "name";
    public static final String COL_PASSWORD = "password";
    public static final String COL_OCCUPATION = "occupation";
    public static final String COL_EMAIL = "email";
    public static final String COL_SALARY = "salary";
    public static final String COL_PHONE = "phone";

    private Context context;
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    /**
     * The adapter constructor.
     *
     * @param context
     */
    public UserDatabaseAdapter(Context context) {
        this.context = context;
    }

    /**
     * Creates the database helper and gets the database.
     *
     * @return
     * @throws SQLException
     */
    public UserDatabaseAdapter open() throws SQLException {
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

    /**
     * Creates the user name and password.
     *
     * @param username The username.
     * @param password The password.
     * @return
     */
    public long createUser(String username, String password, String email, String contact, double salary, String occupation) {
        ContentValues initialValues = createUserTableContentValues(username, password, email, contact, salary, occupation);
        return database.insert(LOGIN_TABLE, null, initialValues);
    }

    /**
     * Removes a user's details given an id.
     *
     * @param rowId Column id.
     * @return
     */
    public boolean deleteUser(long rowId) {
        return database.delete(LOGIN_TABLE, COL_ID + "=" + rowId, null) > 0;
    }

    public boolean updateUserTable(long rowId, String username, String password, String email, String contact, double salary, String occupation) {
        ContentValues updateValues = createUserTableContentValues(username, password, email, contact, salary, occupation);
        return database.update(LOGIN_TABLE, updateValues, COL_ID + "=" + rowId, null) > 0;
    }

    /**
     * Retrieves the details of all the users stored in the login table.
     *
     * @return
     */
    public Cursor fetchAllUsers() {
        return database.query(LOGIN_TABLE, new String[]{COL_ID, COL_USERNAME,
                COL_PASSWORD}, null, null, null, null, null);
    }

    /**
     * Retrieves the details of a specific user, given a username and password.
     *
     * @return
     */
    public Cursor fetchUser(String username, String password) {
        String[] columns = {COL_ID, COL_USERNAME, COL_PASSWORD};
        String selection = COL_USERNAME + "= ?" + " AND " + COL_PASSWORD + "= ?";
        String[] selectionArgs = new String[]{username, password};
        Cursor myCursor = database.query(LOGIN_TABLE, columns, selection, selectionArgs, null, null, null);

        if (myCursor != null) {
            myCursor.moveToFirst();
        }
        return myCursor;
    }

    /**
     * Returns the table details given a row id.
     *
     * @param rowId The table row id.
     * @return
     * @throws SQLException
     */
    public Cursor fetchUserById(long rowId) throws SQLException {
        Cursor myCursor = database.query(LOGIN_TABLE,
                new String[]{COL_ID, COL_USERNAME, COL_PASSWORD},
                COL_ID + "=" + rowId, null, null, null, null);
        if (myCursor != null) {
            myCursor.moveToFirst();
        }
        return myCursor;
    }

    /**
     * Stores the username and password upon creation of new login details.
     *
     * @param username   The user name.
     * @param password   The password.
     * @param email
     * @param contact
     * @param salary
     * @param occupation
     * @return The entered values.
     */
    private ContentValues createUserTableContentValues(String username, String password, String email, String contact, double salary, String occupation) {
        ContentValues values = new ContentValues();
        values.put(COL_USERNAME, username);
        values.put(COL_PASSWORD, password);
        values.put(COL_EMAIL, email);
        values.put(COL_PHONE, contact);
        values.put(COL_SALARY, salary);
        values.put(COL_OCCUPATION, occupation);
        return values;
    }
}