package wida.reader.db;

import java.util.ArrayList;
import wida.reader.util.Book;
import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

public class BooksDao {

	final private static String DB_NAME = "widaReader.db";
	
	/**
	 * 增加图书
	 * @param context
	 * @param values
	 * @return
	 */
	public static long addBook(Context context ,ContentValues values)
	{
		long ret = 0;
		DBHelper dbHelper = new DBHelper(context, DB_NAME);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ret =  db.insert("books", "bid", values);
		db.close();
		return ret;
	}
	
	/**
	 * 判断图书是否已经存在
	 * @param context
	 * @param bookPath
	 * @return
	 */
	public static boolean isInBookshelf(Context context , String bookPath)
	{
		DBHelper dbHelper = new DBHelper(context, DB_NAME);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor c = db.rawQuery("select * from books where filePath=? limit 1", new String[]{bookPath});
		boolean ret = c.getCount() > 0 ? true : false;
		c.close();
		db.close();
		return ret;
	
	}
	
	/**
	 * 获取所以图书
	 * @param context
	 * @return
	 */
	public static ArrayList<Book> getBooks(Context context)
	{
		ArrayList<Book> list = new ArrayList<Book>();
		DBHelper dbHelper = new DBHelper(context, DB_NAME);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor c = db.rawQuery("select * from books", null);
		c.moveToFirst();
		while (!c.isAfterLast()) {
			int index1 = c.getColumnIndex("name");
			int index2 = c.getColumnIndex("filePath");
			Book book = new Book();
			book.bookName = c.getString(index1);
			book.bookPath = c.getString(index2);
			list.add(book);
			c.moveToNext();
		}
		c.close();
		db.close();
		return list;
	}
	
	public static void deleteBooks(Context context,String bookPath)
	{
		DBHelper dbHelper = new DBHelper(context, DB_NAME);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.execSQL("delete from books where filePath=?",new String[]{bookPath});
		db.close();
	}
	
	public static int updatePosition(Context context,String bookPath,long postion)
	{
		DBHelper dbHelper = new DBHelper(context, DB_NAME);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues updateValue =new ContentValues();
        updateValue.put("position", postion);
        String[] whereArgs = {bookPath};
		int ret = db.update("books", updateValue, "filePath = ?", whereArgs);
		db.close();
		return ret;
	}
	
	public static int getBookReaderPostion(Context context,String bookPath)
	{
		DBHelper dbHelper = new DBHelper(context, DB_NAME);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor c = db.rawQuery("select * from books where filePath= ? limit 1", new String[]{bookPath});
		if (c.moveToNext()) {
			int index1 = c.getColumnIndex("position");
			int position = c.getInt(index1);
			return position;
		}
		return 0;
	}
}
