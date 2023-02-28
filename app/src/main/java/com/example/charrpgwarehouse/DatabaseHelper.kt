package com.example.charrpgwarehouse

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "mydatabase.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "mytable";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_EMAIL = "email";

    private static final String CREATE_TABLE =
        "CREATE TABLE " + TABLE_NAME + "("
    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
    + COLUMN_NAME + " TEXT,"
    + COLUMN_EMAIL + " TEXT"
    + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long addRecord(String name, String email) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_EMAIL, email);

        long id = db.insert(TABLE_NAME, null, values);

        db.close();

        return id;
    }

    public List<Record> getAllRecords() {
        List<Record> records = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Record record = new Record();
                record.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                record.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
                record.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)));

                records.add(record);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return records;
    }

    public void updateRecord(int id, String name, String email) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_EMAIL, email);

        db.update(TABLE_NAME, values, COLUMN_ID + " = ?",
            new String[] { String.valueOf(id) });

        db.close();
    }
