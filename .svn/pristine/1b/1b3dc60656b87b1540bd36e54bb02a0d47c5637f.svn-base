package wida.reader.db;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import wida.reader.util.Book;
import wida.reader.util.Bookmark;
import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

public class BookmarkDao {

	final private static String DB_NAME = "widaReader.db";
	
	/**
	 * 增加书签
	 * @param context
	 * @param values
	 * @return
	 */
	public static long addBookmark(Context context ,ContentValues values)
	{
		long ret = 0;
		DBHelper dbHelper = new DBHelper(context, DB_NAME);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ret =  db.insert("bookmark", "bookmarkId", values);
		db.close();
		return ret;
	}
	
	/**
	 * 获取图书书签
	 * @param context
	 * @return
	 */
	public static ArrayList<Bookmark> getBookmark(Context context,String bookPath)
	{
		ArrayList<Bookmark> list = new ArrayList<Bookmark>();
		DBHelper dbHelper = new DBHelper(context, DB_NAME);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor c = db.rawQuery("select * from bookmark where filePath = ? order by bookmarkId desc", new String [] {bookPath});
		c.moveToFirst();
		while (!c.isAfterLast()) {
			int index1 = c.getColumnIndex("position");
			int index2 = c.getColumnIndex("filePath");
			int index3 = c.getColumnIndex("markString");
			int index4 = c.getColumnIndex("createTime");
			int index5 = c.getColumnIndex("percent");
			Bookmark book = new Bookmark();
			book.position = c.getInt(index1);
			book.bookPath = c.getString(index2);
			book.markString = c.getString(index3);
			book.createTime = c.getString(index4);
			book.percert = c.getString(index5);
			list.add(book);
			c.moveToNext();
		}
		c.close();
		db.close();
		return list;
	}
	
	/**
	 * 删除书签
	 * @param context
	 * @param bookPath
	 */
	public static void deleteBookmark(Context context,String bookPath)
	{
		DBHelper dbHelper = new DBHelper(context, DB_NAME);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.execSQL("delete from bookmark where filePath=?",new String[]{bookPath});
		db.close();
	}

	public static int getBookmarkByPosition(Context context,int position,String bookPath)
	{
		DBHelper dbHelper = new DBHelper(context, DB_NAME);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor c = db.rawQuery("select * from bookmark where filePath = ? and position = ?", new String [] {bookPath,position+""});
		return c.getCount();
	}
	
}
