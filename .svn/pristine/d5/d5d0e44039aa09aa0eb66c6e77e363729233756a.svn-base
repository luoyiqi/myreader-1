package wida.reader.activity;

import java.util.List;
import wida.reader.R;
import wida.reader.adapter.BookCatalogueAdapter;
import wida.reader.adapter.BookmarkAdapter;
import wida.reader.adapter.FileChooserAdapter;
import wida.reader.adapter.FileChooserAdapter.FileInfo;
import wida.reader.db.CatalogueDao;
import wida.reader.util.BookCatalogue;
import wida.reader.util.BookPageFactory;
import wida.reader.util.Bookmark;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;


public class BookCatalogueActivity extends Activity{
	
	private List<BookCatalogue> mlist;
	private ListView catalogueListView;
	private BookCatalogueAdapter mAdatper;
	private String bookPath;
	private Handler handler;
	private static String TAG = "BookCatalogueActivity";
	public static BookPageFactory bookPageFactory;
	private ProgressDialog m_pDialog = null;	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.book_catalogue);
		catalogueListView = (ListView) findViewById(R.id.book_catalogue_list);
		Intent intent = getIntent();
		bookPath = intent.getStringExtra("bookPath");
		mlist = CatalogueDao.getBookCatalogue(this, bookPath);
		
		mAdatper = new BookCatalogueAdapter(BookCatalogueActivity.this, mlist);
		catalogueListView.setAdapter(mAdatper);
		catalogueListView.setEmptyView(findViewById(R.id.tvEmptyCatelogue));
		catalogueListView.setOnItemClickListener(mItemClickListener);

		Button buildCatalogueButton  = (Button) findViewById(R.id.build_catalogue_button);
		buildCatalogueButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.v(TAG, "buildCatalogueButton");
		        new Thread(buildCatalogue).start();
				// 创建ProgressDialog对象
				m_pDialog = new ProgressDialog(BookCatalogueActivity.this);
				// 设置进度条风格，风格为圆形，旋转的
				m_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				// 设置ProgressDialog 标题
				m_pDialog.setTitle("目录正在生成中");
				// 设置ProgressDialog 提示信息
			    m_pDialog.setMessage("请稍后…");
				// 设置ProgressDialog 的进度条是否不明确
				m_pDialog.setIndeterminate(false);
				// 设置ProgressDialog 是否可以按退回按键取消
				m_pDialog.setCancelable(false);
				// 让ProgressDialog显示
				m_pDialog.show();   
			}
		});
		
		handler = new Handler() {
		        public void handleMessage(Message msg) {
		    	    switch (msg.what) {
					case 1:
						Log.v(TAG, "list"+mlist.size());
						mAdatper.notifyDataSetChanged();
						handler.removeCallbacks(buildCatalogue);
						m_pDialog.cancel();
						break;
					default:
						break;
					}
		     }
			};
	}
	
	private AdapterView.OnItemClickListener mItemClickListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> adapterView, View view,
				int position, long id) {
			BookCatalogue bookCatalogue = (BookCatalogue) (((BookCatalogueAdapter) adapterView
					.getAdapter()).getItem(position));
			Intent intent = new Intent(BookCatalogueActivity.this,
					BookReaderActivity.class);
			intent.putExtra("bookPath", bookPath);
			intent.putExtra("position", bookCatalogue.position);
			BookCatalogueActivity.this.startActivity(intent);
			finish();
		}
	};
	
	private Runnable buildCatalogue = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Log.v(TAG, "buildCatalogue");
			List<BookCatalogue> list = bookPageFactory.buildCatalogue();
			mlist.clear();
			for (BookCatalogue bookCatalogue : list) {
				mlist.add(bookCatalogue);
				ContentValues value = new ContentValues();
				value.put("position", bookCatalogue.position);
				value.put("catalogueName", bookCatalogue.catalogueName);
				value.put("filePath", bookPath);
				CatalogueDao.addBookCatalogue(BookCatalogueActivity.this, value);
			}
			handler.sendMessage(handler.obtainMessage(1,"finish"));
		}
	};
}