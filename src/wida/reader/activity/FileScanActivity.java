package wida.reader.activity;

import java.io.File;
import java.io.FileFilter;
import java.text.SimpleDateFormat;
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
import wida.reader.filter.BookFilter;
import wida.reader.util.FileComarator;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.view.KeyEvent;
import android.widget.TextView;

public class FileScanActivity extends Activity {
	
	private static String TAG = "file_scan";
	private ListView mListView;
	private View mBackView;
	private TextView mTvPath ;
	private HashMap<String, FileInfo> mSelectBooks;
	
	private String mScanPath ; 
	private ArrayList<FileInfo> mFileLists = new ArrayList<FileInfo>();
	private FileChooserAdapter mAdatper ;
	private Handler handler;
    private Button btAddBook;
    private ImageView imgStopScan;
	private LinearLayout operateLinearLayout;
	
	static Comparator<FileInfo> FComparator = new FileComarator();
	private static BookFilter BookFilter = new BookFilter();
	private boolean StopScan = false;
	private boolean canSelect  = false;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.filescan_fileshow);

		Intent intent = getIntent();
		mScanPath = intent.getStringExtra("mScanPath");
		
		mBackView = findViewById(R.id.filechooser_goback);
		mBackView.setOnClickListener(mClickListener);
		
		mTvPath = (TextView)findViewById(R.id.tvPath);
		mListView = (ListView)findViewById(R.id.bookview);
		mListView.setEmptyView(findViewById(R.id.tvEmptyHint));
		mListView.setOnItemClickListener(mItemClickListener);

		btAddBook = (Button) findViewById(R.id.btAddBook);
		btAddBook.setOnClickListener(mClickListener);
		
		imgStopScan = (ImageView) findViewById(R.id.imgStopScan);
		imgStopScan.setOnClickListener(mClickListener);
		
		
		operateLinearLayout = (LinearLayout) findViewById(R.id.operateLinear);
		
		mSelectBooks = new HashMap<String,FileInfo>();
		
		handler = new Handler() {
             public void handleMessage(Message msg) {
            	    switch (msg.what) {
					case 1:
						mTvPath.setText((String)msg.obj);
						break;
					case 2:
						if(mAdatper != null){
							FileInfo book = (FileInfo) msg.obj;
							mFileLists.add(book);
							//是否需要排序
							//Collections.sort(mFileLists, FComparator);
	        			    mAdatper.notifyDataSetChanged(); 
						}else
						{
							mAdatper = new FileChooserAdapter(FileScanActivity.this , mFileLists,mSelectBooks);
							mListView.setAdapter(mAdatper);
						}
						break;
					case 3:
						mTvPath.setText("扫描完毕总共"+mFileLists.size()+"文件");
						imgStopScan.setVisibility(View.INVISIBLE);
						canSelect = true;
						break;
					default:
						break;
					}
             }
         };
         new Thread(ScanBook).start();   
	}
	
	private File[] folderScan(String path) {
		File file = new File(path);
		File[] files = file.listFiles(BookFilter);
		return files;
	}
	
	private View.OnClickListener mClickListener = new  OnClickListener() {
		public void onClick(View v) {
			Intent myIntent = new Intent();
			switch (v.getId()) {
			case R.id.filechooser_goback :
	            myIntent = new Intent(FileScanActivity.this, MainTabActivity.class); 
	            startActivity(myIntent);
	            finish();
			    break ;
			case R.id.btAddBook:
				Iterator iter = mSelectBooks.entrySet().iterator();
				Log.v(TAG, "size"+mSelectBooks.size());
				while (iter.hasNext()) {
						Map.Entry entry = (Map.Entry) iter.next();
						FileInfo f = (FileInfo) entry.getValue();
				        ContentValues values =new ContentValues();
				      //Log.v(TAG, "fileInfo:"+f.getFileName().substring(0,f.getFileName().lastIndexOf(".")));
				      //Log.v(TAG, "fileInfo:"+f.getFilePath());
				        values.put("name", f.getFileName().substring(0,f.getFileName().lastIndexOf(".")));
				        values.put("filePath", f.getFilePath());
				        Log.v(TAG,values.toString());
				        BooksDao.addBook(FileScanActivity.this, values);
				}
				
	            myIntent = new Intent(FileScanActivity.this, MainTabActivity.class);
	            myIntent.putExtra("reflashBookshelf", true); 
	            startActivity(myIntent);
	            finish();
				break; 
			case R.id.imgStopScan:
				StopScan = true;
				handler.sendMessage(handler.obtainMessage(3,"finish"));
				handler.removeCallbacks(ScanBook); 
				imgStopScan.setVisibility(View.INVISIBLE);
				break;
			default :
			    	break ;
			}
		}
	};
	
	private AdapterView.OnItemClickListener mItemClickListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> adapterView, View view, int position,
				long id) {
			
			if (!canSelect) return;
			FileInfo fileInfo = (FileInfo)(((FileChooserAdapter)adapterView.getAdapter()).getItem(position));
			Log.v(TAG, "position:"+position);
			Log.v(TAG, "fileInfo:"+fileInfo.toString());
			if(fileInfo.isTxTFile()){  			    				
				ImageView imgSelect = (ImageView) view.findViewById(R.id.book_select);
				String bookNameString = fileInfo.getFileName().substring(0,fileInfo.getFileName().lastIndexOf("."));
				if (BooksDao.isInBookshelf(FileScanActivity.this, fileInfo.getFilePath())){
					toast(bookNameString+"已经在书架上不需要重现加入") ;
					imgSelect.setImageResource(R.drawable.checkbox_empty);
				}else {
					if (mSelectBooks.containsKey(fileInfo.getFilePath()))
					{
						mSelectBooks.remove(fileInfo.getFilePath());
						//imgSelect.setImageResource(R.drawable.checkbox_empty);
					}else{
						mSelectBooks.put(fileInfo.getFilePath(), fileInfo);
						//imgSelect.setImageResource(R.drawable.checkbox_ok);
					}
					changeLinear();
				}
			}
			else { 
				toast(getText(R.string.open_file_error_format));
			}
		}
	};
    
	public void changeLinear()
	{
		int size = mSelectBooks.size();
		if (size > 0){	
			operateLinearLayout.setVisibility(View.VISIBLE);
			btAddBook.setText("添加到书架("+size+")");
		}else {
			operateLinearLayout.setVisibility(View.GONE);
			btAddBook.setText("添加到书架");
		}
		mAdatper.notifyDataSetChanged(); 
	}
	
	public boolean onKeyDown(int keyCode , KeyEvent event){
		if(event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode()
			== KeyEvent.KEYCODE_BACK){
			finish(); 
			return true ;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void toast(CharSequence hint){
	    Toast.makeText(this, hint , Toast.LENGTH_SHORT).show();
	}
	
    Runnable ScanBook = new Runnable() {
        public void run() {
	        scanFileItems(mScanPath);
	        StopScan = true;
	        handler.sendMessage(handler.obtainMessage(3,"finish"));
	        handler.removeCallbacks(ScanBook); 
        }
    	private void scanFileItems(String filePath) {
    		if (StopScan) return;
    		File[] files = folderScan(filePath);
    		if(files == null) 
    			return ;
    		
    		for (int i = 0; i < files.length; i++) {	
    			    if (StopScan) return;
    				String fileAbsolutePath = files[i].getAbsolutePath() ;
    				String fileName = files[i].getName();
    			    boolean isDirectory = false ;
    				if (files[i].isDirectory()){
    					if (!StopScan){
    						 handler.sendMessage(handler.obtainMessage(1,fileAbsolutePath));
    						 scanFileItems(fileAbsolutePath);
    					}else{
    						return;
    					}
    					continue;
    				}
    				//Log.v(TAG, fileAbsolutePath + fileName + isDirectory);
    			    FileInfo fileInfo = new FileInfo(fileAbsolutePath , fileName , isDirectory) ;
    			    if (!StopScan)handler.sendMessage(handler.obtainMessage(2,fileInfo));
    			}
    	}
    }; 
}

