package wida.reader.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import wida.reader.R;
import wida.reader.adapter.BookAdapter;
import wida.reader.adapter.ReaderMenuAdapter;
import wida.reader.adapter.FileChooserAdapter.FileInfo;
import wida.reader.db.BookmarkDao;
import wida.reader.db.BooksDao;
import wida.reader.db.CatalogueDao;
import wida.reader.db.DBHelper;
import wida.reader.util.Book;
import wida.reader.util.MenuInfo;
import wida.reader.util.MenuUtils;
import wida.reader.view.PageControlView;
import wida.reader.viewGroup.ScrollLayout;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import wida.reader.viewGroup.ScrollLayout.OnScreenChangeListenerDataLoad;

/**
 * 书架
 */
public class BookshelfActivity extends Activity implements View.OnClickListener {
	private ScrollLayout mScrollLayout;
	private PageControlView pageControl;
	private int pageSize = 9;
	public int n = 0;
	private DataLoading dataLoad;
	private Intent fileChooserIntent;
	private ImageView OpenFile;
	private static final int REQUEST_CODE = 1; // 请求码
	public static final String EXTRA_FILE_CHOOSER = "file_chooser";
	public static final String CHOOSER_BOOKNAME = "book_name";
	final static String TAG = "BOOKSHELF";
	private PopupWindow popup;
	private BookshelfBroadcastReceiver bookshelfBroadcastReceiver = null;

	private Book popBookInfo;
	private View popMemuView ;
	
	List<Book> list = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 隐藏应用标题栏
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.bookshelf_activity);

		OpenFile = (ImageView) findViewById(R.id.btn_import);
		OpenFile.setOnClickListener(this);
		fileChooserIntent = new Intent(this, FileChooserActivity.class);

		int screenWidth = getWindowManager().getDefaultDisplay().getWidth(); // 屏幕宽（像素，如：480px）
		int screenHeight = getWindowManager().getDefaultDisplay().getHeight(); // 屏幕高（像素，如：800p）
		Log.v(TAG, "screenWidth=" + screenWidth + "; screenHeight="
				+ screenHeight);
		// 屏幕超过960显示4行 12本书
		if (screenHeight >= 960) {
			pageSize = 12;
		}

		dataLoad = new DataLoading();
		mScrollLayout = (ScrollLayout) findViewById(R.id.ScrollLayoutTest);

		// 数据库读取图书
		list = BooksDao.getBooks(this);

		int pageNo = (int) Math.ceil(list.size() / new Float(pageSize));
		Log.v(TAG, "pageNo=" + pageNo);
		for (int i = 0; i < pageNo; i++) {
			GridView bookPage = new GridView(this);
			// get the "i" page data
			bookPage.setAdapter(new BookAdapter(this, list, i, pageSize));
			// 每行显示3个元素
			bookPage.setNumColumns(3);
			bookPage.setOnItemClickListener(listener);
			bookPage.setOnItemLongClickListener(lcListener);
			mScrollLayout.addView(bookPage);
		}
		// 加载分页
		pageControl = (PageControlView) findViewById(R.id.pageControl);
		pageControl.bindScrollViewGroup(mScrollLayout);
		// 加载分页数据
		dataLoad.bindScrollViewGroup(mScrollLayout);
		initPopuWindows();

		bookshelfBroadcastReceiver = new BookshelfBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("bookshelf.reflash");
		registerReceiver(bookshelfBroadcastReceiver, intentFilter);
	}

	/**
	 * gridView 的onItemLick响应事件
	 */
	public OnItemClickListener listener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// 计算选择元素的在list中的索引值
			position = (PageControlView.currentIndex) * pageSize + position;
			Book bk = list.get(position);
			Intent intent = new Intent(
					wida.reader.activity.BookshelfActivity.this,
					wida.reader.activity.BookReaderActivity.class);
			intent.putExtra("bookPath", bk.bookPath);
			intent.putExtra("bookName", bk.bookName);
			BookshelfActivity.this.startActivity(intent);
		}
	};

	public OnItemLongClickListener lcListener = new OnItemLongClickListener() {
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				int position, long id) {
			// 计算选择元素的在list中的索引值
			position = (PageControlView.currentIndex) * pageSize + position;
			popBookInfo = list.get(position);
			showMenuPopWindow();
			TextView bookNameTextView = (TextView) popMemuView.findViewById(R.id.long_click_bookname);
			bookNameTextView.setText(popBookInfo.bookName);
			return true;
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	// 分页数据
	class DataLoading {
		private int count;

		public void bindScrollViewGroup(ScrollLayout scrollViewGroup) {
			this.count = scrollViewGroup.getChildCount();
			scrollViewGroup
					.setOnScreenChangeListenerDataLoad(new OnScreenChangeListenerDataLoad() {
						public void onScreenChange(int currentIndex) {
							// TODO Auto-generated method stub
							// generatePageControl(currentIndex);
						}
					});
		}
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_import:
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				startActivity(fileChooserIntent);
				// finish();
			} else
				Toast.makeText(this, getText(R.string.sdcard_unmonted_hint),
						Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	}

	/**
	 * 设置PopupWindows
	 */
	private void initPopuWindows() {
		LayoutInflater inflater = LayoutInflater.from(this);
	    popMemuView = (View) inflater.inflate(R.layout.bookshelf_longclick_menu,
				null);
		popup = new PopupWindow(popMemuView, LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		popup.setBackgroundDrawable(new BitmapDrawable());
		//popup.setFocusable(true);
		// 设置显示和隐藏的动画
		// popup.setAnimationStyle(R.style.menushow);
		popup.update();
		popup.setOutsideTouchable(true);
		// 设置触摸获取焦点
		//popMemuView.setFocusableInTouchMode(true);
		// 设置键盘事件,如果按下菜单键则隐藏菜单
		popMemuView.setOnKeyListener(new android.view.View.OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if (((keyCode == KeyEvent.KEYCODE_MENU) || (keyCode == KeyEvent.KEYCODE_BACK))
						&& (popup.isShowing())) {
					closePopWindow();
					return true;
				}
				return false;
			}
		});

		LinearLayout ll = (LinearLayout) popMemuView.findViewById(R.id.linearlayout);
		ll.setOnClickListener(mClickListener);
		LinearLayout llo = (LinearLayout) popMemuView.findViewById(R.id.longClickMenu);
		llo.setOnClickListener(mClickListener);
		TextView linkTextView  = (TextView) popMemuView.findViewById(R.id.long_click_link);
		linkTextView.setOnClickListener(mClickListener);
		TextView deleteTextView  = (TextView) popMemuView.findViewById(R.id.long_click_delete);
		deleteTextView.setOnClickListener(mClickListener);
	}

	// 监听
	private View.OnClickListener mClickListener = new OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.linearlayout:
				closePopWindow();
				break;
			case R.id.long_click_link://创建快捷方式
				Toast.makeText(BookshelfActivity.this, popBookInfo.bookName,
						 Toast.LENGTH_SHORT).show();
				closePopWindow();
				break;
			case R.id.long_click_delete: //删除图书
				final String filePathString = popBookInfo.bookPath;
				AlertDialog.Builder alertDialog = new  AlertDialog.Builder(BookshelfActivity.this);
				alertDialog.setTitle("删除图书" ).setMessage("你确认要删除《"+ popBookInfo.bookName +"》吗？")  
				.setPositiveButton("是" ,  new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                            int whichButton) {
                      BooksDao.deleteBooks(BookshelfActivity.this, filePathString);
                      BookmarkDao.deleteBookmark(BookshelfActivity.this, filePathString);
                      CatalogueDao.deleteBookCatalogue(BookshelfActivity.this, filePathString);
                      dialog.dismiss();
                      reflashBookshelf();
                    }
                } )  
				.setNegativeButton("否" , new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                            int whichButton) {
                       dialog.cancel();
                    }
                }).show();
				closePopWindow();
				break;
			default:
				break;
			}
		}
	};
	
	


	/**
	 * 关闭popwindow
	 */
	private void closePopWindow() {
		popBookInfo = null;
		popup.dismiss();
	}

	private void showMenuPopWindow() {
		if (popup != null) {
			LinearLayout ll = (LinearLayout) findViewById(R.id.bookshelf_linearLayout);
			getWindow().setFlags(
					WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
			popup.showAtLocation(ll, Gravity.CENTER, 0, 0);
		}
	}

	/** 接收书架信息修改的广播 **/
	public class BookshelfBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals("bookshelf.reflash")) {
				Log.v(TAG, "bookshelf.reflash");
				reflashBookshelf();
			}
		}
	}

	/**
	 * 更新书架信息
	 */
	private void reflashBookshelf() {
		/* 清空原来的数据 */
		list.clear();
		mScrollLayout.removeAllViews();
		pageControl.removeAllViews();

		// 数据库读取图书
		list = BooksDao.getBooks(this);

		int pageNo = (int) Math.ceil(list.size() / new Float(pageSize));
		Log.v(TAG, "pageNo=" + pageNo);
		for (int i = 0; i < pageNo; i++) {
			GridView bookPage = new GridView(this);
			// get the "i" page data
			bookPage.setAdapter(new BookAdapter(this, list, i, pageSize));
			// 每行显示3个元素
			bookPage.setNumColumns(3);
			bookPage.setOnItemClickListener(listener);
			bookPage.setOnItemLongClickListener(lcListener);
			mScrollLayout.addView(bookPage);
		}
		// 加载分页
		pageControl.bindScrollViewGroup(mScrollLayout);
		// 加载分页数据
		dataLoad.bindScrollViewGroup(mScrollLayout);
	}

	@Override
	public void onDestroy() {
		if (bookshelfBroadcastReceiver != null) {
			unregisterReceiver(bookshelfBroadcastReceiver);
		}
		super.onDestroy();
	}
}