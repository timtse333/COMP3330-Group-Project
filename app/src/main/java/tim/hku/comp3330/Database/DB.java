package tim.hku.comp3330.Database;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import tim.hku.comp3330.DataClass.User;
import tim.hku.comp3330.DataClass.Project;

public class DB extends SQLiteOpenHelper {
    //information of database
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "DB.db";

    // Table: User
    public static final String USERS = "User";
    public static final String USER_ID = "User_ID";
    public static final String USER_NAME = "User_Name";
    public static final String USER_ICON = "UserIcon";
    public static final String LOGIN_NAME = "LoginName";
    public static final String PASSWORD = "Password";

    // Table: Projects
    public static final String PROJECT = "Project";
    public static final String PROJECT_ID = "Project_ID";
    public static final String PROJECT_NAME = "Project_Name";
    public static final String PROJECT_DESCRIPTION = "Project_Description";
    public static final String OWNER_ID = "Owner_ID";
    // Table Create statements
    String CREATE_USER_TABLE = String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY AUTOINCREMENT,%s TEXT,%s BLOB,%s TEXT,%s TEXT)", USERS, USER_ID, USER_NAME, USER_ICON, LOGIN_NAME, PASSWORD);
    String CREATE_PROJECT_TABLE = String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY AUTOINCREMENT,%s TEXT,%s TEXT,%s INTEGER, FOREIGN KEY (%s) REFERENCES %s(%s))", PROJECT, PROJECT_ID, PROJECT_NAME, PROJECT_DESCRIPTION, OWNER_ID, OWNER_ID, USERS, USER_ID);


    public DB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_PROJECT_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + USERS);
        db.execSQL("DROP TABLE IF EXISTS " + PROJECT);
        // create new tables
        onCreate(db);
    }

    // Handlers for users
    public List<User> loadAllUser() {
        List<User> result = new ArrayList<User>();
        String query = "Select * FROM " + USERS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        User users = new User();
        while (cursor.moveToNext()) {
            users.setUserID(cursor.getInt(0));
            users.setUserName(cursor.getString(1));
            users.setLoginName(cursor.getString(3));
            users.setPassword(cursor.getString(4));
            result.add(users);
        }
        cursor.close();
        db.close();
        return result;
    }

    public boolean DuplicateLoginName(String loginName){
        String[] columns = {
                "*"
        };
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = LOGIN_NAME + " = ?";
        String[] selectionArgs = new String[]{loginName};
        Cursor cursor = db.query(USERS, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }
        return false;
    }
    public void CreateUser(User users) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USER_NAME, users.getUserName());
        values.put(LOGIN_NAME, users.getLoginName());
        values.put(PASSWORD, users.getPassword());
        /*byte[] img = DBUtil.getBytes(users.getUserIcon());
        values.put(USER_ICON, img);*/
        db.insert(USERS, null, values);
        db.close();
    }

    public User GetUserByID(int ID) {
        String query = "Select * FROM " + USERS + " WHERE " + USER_ID + " = " + ID;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        User users = new User();
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            users.setUserID(Integer.parseInt(cursor.getString(0)));
            users.setUserName(cursor.getString(1));
            //users.setUserIcon(DBUtil.getImage(cursor.getBlob(2)));
            cursor.close();
        } else {
            users = null;
        }
        db.close();
        return users;
    }


    public boolean deleteUser(int ID) {
        boolean result = false;
        String query = "Select * FROM " + USERS + " WHERE " + USER_ID + " = " + ID;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        User users = new User();
        if (cursor.moveToFirst()) {
            users.setUserID(Integer.parseInt(cursor.getString(0)));
            db.delete(USERS, USER_ID + " =? ", new String[]{
                    String.valueOf(users.getUserID())
            });
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }

    public void updateUser(User users) {
        // Only name, icon and password should be able to update
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USER_NAME, users.getUserName());
        //values.put(USER_ICON, DBUtil.getBytes(users.getUserIcon()));
        values.put(PASSWORD, users.getPassword());
        db.update(USERS, values, USER_ID + " = " + users.getUserID(), null);
    }

    // Handlers for Project
    public String loadAllProjects() {
        String result = "";
        String query = "Select * FROM " + PROJECT;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            int result_0 = cursor.getInt(0);
            String result_1 = cursor.getString(1);
            result += String.valueOf(result_0) + " " + result_1 +
                    System.getProperty("line.separator");
        }
        cursor.close();
        db.close();
        return result;
    }
    public void CreateProject(Project project) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PROJECT_ID, project.getProjectID());
        values.put(PROJECT_NAME, project.getProjectName());
        values.put(PROJECT_DESCRIPTION, project.getProjectDescription());
        values.put(OWNER_ID, project.getOwnerID());
        db.insert(PROJECT, null, values);
        db.close();
    }

    public Project GetProject(int projectID) {
        String query = "Select * FROM " + PROJECT + " WHERE " + PROJECT_ID + " = " + projectID;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Project project = new Project();
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            project.setProjectID(Integer.parseInt(cursor.getString(0)));
            project.setProjectName(cursor.getString(1));
            project.setProjectDescription(cursor.getString(2));
            project.setOwnerID(Integer.parseInt(cursor.getString(3)));
            cursor.close();
        } else {
            project = null;
        }
        db.close();
        return project;
    }
    public boolean deleteProject(int projID) {
        boolean result = false;
        String query = "Select * FROM " + PROJECT + " WHERE " + PROJECT_ID + " = " + projID;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Project project = new Project();
        if (cursor.moveToFirst()) {
            project.setProjectID(Integer.parseInt(cursor.getString(0)));
            db.delete(USERS, USER_ID + " =? ", new String[]{
                    String.valueOf(project.getProjectID())
            });
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }

    public void updateProject(Project project) {
        // Only name and description should be able to update
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PROJECT_NAME, project.getProjectName());
        values.put(PROJECT_DESCRIPTION, project.getProjectDescription());
        db.update(PROJECT, values, PROJECT_ID + " = " + project.getProjectID(), null);
    }

}