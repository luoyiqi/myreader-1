package wida.reader.db;

import java.util.ArrayList;
import wida.reader.util.BookCatalogue;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CatalogueDao {

	final private static String DB_NAME = "widaReader.db";
	
	/**
	 * 增加目录
	 * @param context
	 * @param values
	 * @return
	 */
	public static long addBookCatalogue(Context context ,ContentValues values)
	{
		long ret = 0;
		DBHelper dbHelper = new DBHelper(context, DB_NAME);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ret =  db.insert("catalogue", "catalogueId", values);
		db.close();
		return ret;
	}
	
	/**
	 * 获取目录
	 * @param context
	 * @return
	 */
	public static ArrayList<BookCatalogue> getBookCatalogue(Context context,String bookPath)
	{
		ArrayList<BookCatalogue> list = new ArrayList<BookCatalogue>();
		DBHelper dbHelper = new DBHelper(context, DB_NAME);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor c = db.rawQuery("select * from catalogue where filePath = ?", new String [] {bookPath});
		c.moveToFirst();
		while (!c.isAfterLast()) {
			int index1 = c.getColumnIndex("position");
			int index2 = c.getColumnIndex("filePath");
			int index3 = c.getColumnIndex("catalogueName");
			BookCatalogue bookCatalogue = new BookCatalogue();
			bookCatalogue.position = c.getInt(index1);
			bookCatalogue.bookPath = c.getString(index2);
			bookCatalogue.catalogueName = c.getString(index3);
			list.add(bookCatalogue);
			c.moveToNext();
		}
		c.close();
		db.close();
		return list;
	}
	/**
	 * 删除目录
	 * @param context
	 * @param bookPath
	 */
	public static void deleteBookCatalogue(Context context,String bookPath)
	{
		DBHelper dbHelper = new DBHelper(context, DB_NAME);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.execSQL("delete from catalogue where filePath=?",new String[]{bookPath});
		db.close();
	}
	

}
