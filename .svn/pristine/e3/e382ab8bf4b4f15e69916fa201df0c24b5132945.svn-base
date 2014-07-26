package wida.reader.activity;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import wida.reader.R;
import wida.reader.adapter.FileChooserAdapter;
import wida.reader.adapter.FileChooserAdapter.FileInfo;
import wida.reader.db.BooksDao;
import wida.reader.db.DBHelper;
import wida.reader.filter.BookFilter;
import wida.reader.gesture.GestureListener;
import wida.reader.util.FileComarator;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.GestureDetector;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.view.KeyEvent;
import android.widget.TextView;

public class FileChooserActivity extends Activity {
	
	private static String TAG = "files";
	private ListView mListView;
	private View mBackView;
	private View mBtExit;
	private View btnScan;
	private TextView mTvPath ;
	
	private String mSdcardRootPath ; 
	private String mLastFilePath ;   
	private ArrayList<FileInfo> mFileLists  ;
	private HashMap<String, FileInfo> mSelectBooks;
	private FileChooserAdapter mAdatper ;
	private String LimitParentFilePath;
    private Button btAddBook;
    
	static Comparator<FileInfo> FComparator = new FileComarator();
	private static BookFilter BookFilter;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.filechooser_fileshow);

		mSdcardRootPath = Environment.getExternalStorageDirectory().getAbsolutePath();	
		LimitParentFilePath = Environment.getExternalStorageDirectory().getParent();
		mBackView = findViewById(R.id.imgBackFolder);
		mBackView.setOnClickListener(mClickListener);
		mBtExit = findViewById(R.id.btExit);
		mBtExit.setOnClickListener(mClickListener);
		btnScan= findViewById(R.id.btnScan);
		btnScan.setOnClickListener(mClickListener);
		mTvPath = (TextView)findViewById(R.id.tvPath);
		btAddBook = (Button) findViewById(R.id.btAddBook);
		btAddBook.setOnClickListener(mClickListener);
		
		
		mListView = (ListView)findViewById(R.id.bookview);
		mListView.setEmptyView(findViewById(R.id.tvEmptyHint));
		mListView.setOnItemClickListener(mItemClickListener);
		
		mSelectBooks = new HashMap<String,FileInfo>();
		setListViewAdapter(mSdcardRootPath);

	}

	private void setListViewAdapter(String filePath) {
		updateFileItems(filePath);
		mAdatper = new FileChooserAdapter(this , mFileLists,mSelectBooks);
		mListView.setAdapter(mAdatper);
	}
	

	private void updateFileItems(String filePath) {
		mLastFilePath = filePath ;
		mTvPath.setText(mLastFilePath);
		
		if(mFileLists == null)
			mFileLists = new ArrayList<FileInfo>() ;
		if(!mFileLists.isEmpty())
			mFileLists.clear() ;
		File[] files = folderScan(filePath);
		if(files == null) 
			return ;
		
	for (int i = 0; i < files.length; i++) {			
			String fileAbsolutePath = files[i].getAbsolutePath() ;
			String fileName = files[i].getName();
		    boolean isDirectory = false ;
			if (files[i].isDirectory()){
				isDirectory = true ;
			}
			
			Log.v(TAG, fileAbsolutePath + fileName + isDirectory);
		    FileInfo fileInfo = new FileInfo(fileAbsolutePath , fileName , isDirectory) ;
			mFileLists.add(fileInfo);
			Collections.sort(mFileLists, FComparator);
		}
		//When first enter , the object of mAdatper don't initialized
		if(mAdatper != null)
		    mAdatper.notifyDataSetChanged(); 
	}

	private File[] folderScan(String path) {
		File file = new File(path);
		BookFilter = new BookFilter();
		File[] files = file.listFiles(BookFilter);
		return files;
	}
	
	private View.OnClickListener mClickListener = new  OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.imgBackFolder:
				backProcess();
				break;
			case R.id.btExit :
				//setResult(RESULT_CANCELED);
				finish();
			    break ;
			case R.id.btnScan:
				 Intent intent=new Intent(FileChooserActivity.this,FileScanActivity.class); 
			     intent.putExtra("mScanPath", mLastFilePath); 
			     FileChooserActivity.this.startActivity(intent); 
			     finish();
			    break ;
			case R.id.btAddBook:
				Iterator iter = mSelectBooks.entrySet().iterator();
				while (iter.hasNext()) {
						Map.Entry entry = (Map.Entry) iter.next();
						FileInfo f = (FileInfo) entry.getValue();
				        ContentValues values =new ContentValues();
				        values.put("name", f.getFileName().substring(0,f.getFileName().lastIndexOf(".")));
				        values.put("filePath", f.getFilePath());
						BooksDao.addBook(FileChooserActivity.this, values);
				}
				Intent myIntent = new Intent();
	            myIntent = new Intent(FileChooserActivity.this, MainTabActivity.class);
	            myIntent.putExtra("reflashBookshelf", true); 
	            FileChooserActivity.this.startActivity(myIntent);
	            finish();
				break;
			default :
			    	break ;
			}
		}
	};
	
	private AdapterView.OnItemClickListener mItemClickListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> adapterView, View view, int position,
				long id) {
			FileInfo fileInfo = (FileInfo)(((FileChooserAdapter)adapterView.getAdapter()).getItem(position));
			if(fileInfo.isDirectory()) 			
				updateFileItems(fileInfo.getFilePath()) ;
			else if(fileInfo.isTxTFile()){  			    
				String bookNameString = fileInfo.getFileName().substring(0,fileInfo.getFileName().lastIndexOf("."));
				ImageView imgSelect = (ImageView) view.findViewById(R.id.book_select);
				if (BooksDao.isInBookshelf(FileChooserActivity.this, fileInfo.getFilePath())){
					toast(bookNameString+"已经在书架上不需要重现加入") ;
					imgSelect.setImageResource(R.drawable.checkbox_empty);
				}else {
					if (mSelectBooks.containsKey(fileInfo.getFilePath()))
					{
						mSelectBooks.remove(fileInfo.getFilePath());
					}else{
						mSelectBooks.put(fileInfo.getFilePath(), fileInfo);
					}
					changeButtonText();
				}
			}
			else { 
				toast(getText(R.string.open_file_error_format));
			}
		}
	};
    
	public void changeButtonText()
	{
		int size = mSelectBooks.size();
		if (size > 0){	
			btAddBook.setText("添加到书架("+size+")");
		}else {
			btAddBook.setText("添加到书架");
		}
		mAdatper.notifyDataSetChanged(); 
	}
	
	public boolean onKeyDown(int keyCode , KeyEvent event){
		if(event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode()
			== KeyEvent.KEYCODE_BACK){
			backProcess();   
			return true ;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void backProcess(){
		if (!mLastFilePath.equals(LimitParentFilePath)) {  
			File thisFile = new File(mLastFilePath);
			String parentFilePath = thisFile.getParent();
			updateFileItems(parentFilePath);
		} 
		else { 		
			//setResult(RESULT_CANCELED);
			finish();
		}
	}
	private void toast(CharSequence hint){
	    Toast.makeText(this, hint , Toast.LENGTH_SHORT).show();
	}
}