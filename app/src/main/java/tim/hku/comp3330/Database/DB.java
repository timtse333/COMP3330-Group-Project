package tim.hku.comp3330.Database;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import tim.hku.comp3330.DataClass.Message;
import tim.hku.comp3330.DataClass.ProgressPost;
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
    public static final String PROJECT_PIC = "Project_Pic";
    public static final String PROJECT_DESCRIPTION = "Project_Description";
    public static final String OWNER_ID = "Owner_ID";

    //Table: Project Progress Post
    public static final String PROGRESS = "Progress_Post";
    public static final String POST_ID = "Post_ID";
    public static final String POST_TITLE = "Title";
    public static final String POST_CONTENT = "Content";
    public static final String CREATED_TIME = "Created_Time";

    //Table: Message
    public static final String MESSAGE = "Message";
    public static final String MSG_ID = "Message_ID";
    public static final String MSG_CONTENT = "Message_Content";
    public static final String SENDER_ID = "Sender_ID";
    public static final String RECEIVER_ID = "Receiver_ID";
    public static final String IS_DELETED = "is_Deleted"; // Sqlite does not support boolean, use 0 for false and 1 for true

    //Table: User-Project-Association
    public static final String ASSOCIATION = "Association";
    public static final String PARTICIPANT_ID = "Participant_ID";
    // Table Create statements
    String CREATE_USER_TABLE = String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY AUTOINCREMENT,%s TEXT,%s BLOB,%s TEXT,%s TEXT)", USERS, USER_ID, USER_NAME, USER_ICON, LOGIN_NAME, PASSWORD);
    String CREATE_PROJECT_TABLE = String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY AUTOINCREMENT,%s TEXT,%s TEXT,%s TEXT,%s INTEGER, FOREIGN KEY (%s) REFERENCES %s(%s))", PROJECT, PROJECT_ID, PROJECT_NAME, PROJECT_DESCRIPTION,PROJECT_PIC,OWNER_ID, OWNER_ID, USERS, USER_ID);
    String CREATE_PROGRESS_TABLE = String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY AUTOINCREMENT,%s INTEGER,%s INTEGER,%s TEXT,%s TEXT,%s TEXT)", PROGRESS, POST_ID, PROJECT_ID, OWNER_ID, POST_TITLE, POST_CONTENT, CREATED_TIME);
    String CREATE_MESSSAGE_TABLE = String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY AUTOINCREMENT,%s TEXT,%s INTEGER,%s INTEGER,%s INTEGER,%s INTEGER DEFAULT 0)", MESSAGE, MSG_ID, MSG_CONTENT, SENDER_ID, RECEIVER_ID, PROJECT_ID ,IS_DELETED);
    String CREATE_USER_PROJECT_ASSOCIATION_TABLE = String.format("CREATE TABLE %s(%sINTEGER,%s INTEGER)", ASSOCIATION, PROJECT_ID, PARTICIPANT_ID);
    public DB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_PROJECT_TABLE);
        db.execSQL(CREATE_PROGRESS_TABLE);
        db.execSQL(CREATE_MESSSAGE_TABLE);
        db.execSQL(CREATE_USER_PROJECT_ASSOCIATION_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + USERS);
        db.execSQL("DROP TABLE IF EXISTS " + PROJECT);
        db.execSQL("DROP TABLE IF EXISTS " + PROGRESS);
        db.execSQL("DROP TABLE IF EXISTS " + MESSAGE);
        db.execSQL("DROP TABLE IF EXISTS " + ASSOCIATION);
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
    public User GetUserByLoginName(String loginName){
        try {
            String query = "Select * FROM " + USERS + " WHERE " + LOGIN_NAME + " = '" + loginName + "'";
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
        }catch (SQLiteException ex){
            User users = new User();
            users = null;
            return users;
        }

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
        values.put(PROJECT_NAME, project.getProjectName());
        values.put(PROJECT_DESCRIPTION, project.getProjectDescription());
        values.put(PROJECT_PIC, project.getProjectPic());
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
            project.setProjectPic(cursor.getString(3));
            project.setOwnerID(Integer.parseInt(cursor.getString(4)));
            cursor.close();
        } else {
            project = null;
        }
        db.close();
        return project;
    }
    public ArrayList<Project> GetProjectByUserID(int userID){
        String query = "Select * FROM " + PROJECT + " WHERE " + OWNER_ID + " = " + userID;
        String query2 = "Select * FROM " + ASSOCIATION + " WHERE " + PARTICIPANT_ID + " = " + userID;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<Project> projList = new ArrayList<Project>();
        if (cursor.moveToFirst()) {
            do{
                Project project = new Project();
                project.setProjectID(Integer.parseInt(cursor.getString(0)));
                project.setProjectName(cursor.getString(1));
                project.setProjectDescription(cursor.getString(2));
                project.setProjectPic(cursor.getString(3));
                project.setOwnerID(Integer.parseInt(cursor.getString(4)));
                projList.add(project);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return projList;
    }
    public int GetProjectNum() { // for getting the total number of projects in the system.
        String query = "Select COUNT(*) FROM " + PROJECT;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        int total;
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            total=(Integer.parseInt(cursor.getString(0)));
            cursor.close();
        } else {
            total = 0;
        }
        db.close();
        return total;
    }

    public boolean deleteProject(int projID) {
        boolean result = false;
        String query = "Select * FROM " + PROJECT + " WHERE " + PROJECT_ID + " = " + projID;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Project project = new Project();
        if (cursor.moveToFirst()) {
            project.setProjectID(Integer.parseInt(cursor.getString(0)));
            db.delete(PROJECT, PROJECT_ID + " =? ", new String[]{
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
        values.put(PROJECT_PIC, project.getProjectPic());
        db.update(PROJECT, values, PROJECT_ID + " = " + project.getProjectID(), null);
    }

    public void CreateNewPost(ProgressPost post){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PROJECT_ID,post.getProjectId());
        values.put(POST_TITLE, post.getTitle());
        values.put(POST_CONTENT, post.getContent());
        values.put(OWNER_ID, post.getOwnerID());
        values.put(CREATED_TIME, post.getCreated());
        db.insert(PROGRESS, null, values);
        db.close();
    }
    public ArrayList<ProgressPost> GetPostsByProjectID(int projID){
        ArrayList<ProgressPost> postList = new ArrayList<ProgressPost>();
        try {
            String query = "Select * FROM " + PROGRESS + " WHERE " + PROJECT_ID + " = " + projID;
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do{
                    ProgressPost post = new ProgressPost();
                    post.setProgressPostID(Integer.parseInt(cursor.getString(0)));
                    post.setProjectId(Integer.parseInt(cursor.getString(1)));
                    post.setOwnerID(Integer.parseInt(cursor.getString(2)));
                    post.setTitle(cursor.getString(3));
                    post.setContent(cursor.getString(4));
                    post.setCreated(cursor.getString(5));
                    postList.add(post);
                }while(cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return postList;
        }catch (SQLiteException ex){
            return postList;
        }
    }
    public void CreateMessage(Message message){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MSG_CONTENT, message.getMessageContent());
        values.put(SENDER_ID, message.getSenderID());
        values.put(RECEIVER_ID, message.getReceiverID());
        values.put(PROJECT_ID, message.getProjID());
        values.put(IS_DELETED, 0);
        db.insert(MESSAGE, null, values);
        db.close();
    }

    public void SoftDeleteMessages(Message msg){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(IS_DELETED, true);
        db.update(MESSAGE, values, MSG_ID + " = " + msg.getMessageID(), null);
    }

    // Get all messages sent by this user where the messages have not been accepted/rejected
    public ArrayList<Message> GetAliveSendingMessages(int userID){
        ArrayList<Message> msgList = new ArrayList<Message>();
        try {
            String query = "Select * FROM " + MESSAGE + " WHERE " + SENDER_ID + " = " + userID + " AND " + IS_DELETED + " = " + 0;
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do{
                    Message msg = new Message();
                    msg.setMessageID(Integer.parseInt(cursor.getString(0)));
                    msg.setMessageContent(cursor.getString(1));
                    msg.setSenderID(Integer.parseInt(cursor.getString(2)));
                    msg.setReceiverID(Integer.parseInt(cursor.getString(3)));
                    msg.setProjID(Integer.parseInt(cursor.getString(4)));
                    msg.setDeleted(false); // only un-deleted message will be added to this list, can assume this to be false for all msgs
                    msgList.add(msg);
                }while(cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return msgList;
        }catch (SQLiteException ex){
            return msgList;
        }
    }

    // Get all messages sent to this user where the messages have not been accepted/rejected
    public ArrayList<Message> GetAliveIncomingMessages(int userID){
        ArrayList<Message> msgList = new ArrayList<Message>();
        try {
            String query = "Select * FROM " + MESSAGE + " WHERE " + RECEIVER_ID + " = " + userID + " AND " + IS_DELETED + " = " + 0;
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do{
                    Message msg = new Message();
                    msg.setMessageID(Integer.parseInt(cursor.getString(0)));
                    msg.setMessageContent(cursor.getString(1));
                    msg.setSenderID(Integer.parseInt(cursor.getString(2)));
                    msg.setReceiverID(Integer.parseInt(cursor.getString(3)));
                    msg.setProjID(Integer.parseInt(cursor.getString(4)));
                    msg.setDeleted(false); // only un-deleted message will be added to this list, can assume this to be false for all msgs
                    msgList.add(msg);
                }while(cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return msgList;
        }catch (SQLiteException ex){
            return msgList;
        }
    }
    public Message GetMessageByID(int msgID){
            String query = "Select * FROM " + MESSAGE + " WHERE " + MSG_ID + " = " + msgID;
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(query, null);
            Message msg = new Message();
            if (cursor.moveToFirst()) {
                msg.setMessageID(Integer.parseInt(cursor.getString(0)));
                msg.setMessageContent(cursor.getString(1));
                msg.setSenderID(Integer.parseInt(cursor.getString(2)));
                msg.setReceiverID(Integer.parseInt(cursor.getString(3)));
                msg.setProjID(Integer.parseInt(cursor.getString(4)));
                msg.setDeleted(Integer.parseInt(cursor.getString(5)) == 0 ? false : true);
                cursor.close();
            }
            else{
                msg = null;
            }
            db.close();
            return msg;
    }
}