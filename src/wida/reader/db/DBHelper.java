package wida.reader.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {
	   private static final int VERSION = 1; 
	   final String CREATE_BOOK_TABLE_SQL = "create table if not exists books(bid INTEGER PRIMARY KEY AUTOINCREMENT, " +
	   		"name VARCHAR(100) NOT NULL,filePath VARCHAR(150) NOT NULL,position INTEGER NOT NULL DEFAULT  0)";
	 //  final String CREATE_USERDATA_TABLE_SQLSTRING = "create table if not exists userData(userDataId INTEGER PRIMARY KEY AUTOINCREMENT,"
	   			//	+ "name TEXT NOT NULL,filePath TEXT NOT NULL,position INTEGER NOT NULL DEFAULT  0)";
	   final String CREATE_BOOKMARK_TABLE_SQLSTRING = "create table if not exists bookmark(bookmarkId INTEGER PRIMARY KEY AUTOINCREMENT," +
	   		"markString TEXT NOT NULL,filePath TEXT NOT NULL,position INTEGER NOT NULL DEFAULT  0,percent VARCHAR(10) not null,createTime varchar(20) not null)";
	   final String CREATE_CATALOGUE_TABLE_SQLSTRING = "create table if not exists catalogue(catalogueId INTEGER PRIMARY KEY AUTOINCREMENT," +
	   		"catalogueName VARCHAR(150) NOT NULL,filePath TEXT NOT NULL,position INTEGER NOT NULL DEFAULT  0)";
	   
	   
	    //在SQLiteOepnHelper的子类当中，必须有该构造函数 
	    public DBHelper(Context context, String name, CursorFactory factory, 
	            int version) { 
	        //必须通过super调用父类当中的构造函数 
	        super(context, name, factory, version); 
	        // TODO Auto-generated constructor stub 
	    } 
	    public DBHelper(Context context,String name){ 
	        this(context,name,VERSION); 
	    } 
	    public DBHelper(Context context,String name,int version){ 
	        this(context, name,null,version); 
	    } 
	  
	    @Override  
	    public void onCreate(SQLiteDatabase db) {  
	        // TODO Auto-generated method stub  
	        //第一个使用数据库时自动建表  
	        db.execSQL( CREATE_BOOK_TABLE_SQL);  
	        db.execSQL( CREATE_BOOKMARK_TABLE_SQLSTRING); 
	        db.execSQL( CREATE_CATALOGUE_TABLE_SQLSTRING); 
	    }  
	  
	    @Override  
	    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {  
	        // TODO Auto-generated method stub        
	    }  
}
