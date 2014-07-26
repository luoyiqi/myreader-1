package wida.reader.activity;

import wida.reader.R;
import wida.reader.util.BookPageFactory;
import android.annotation.SuppressLint;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class BookCatalogueTabActivity extends TabActivity implements
		OnCheckedChangeListener {
	private static String TAG = "BookCatalogueTabActivity";
	private RadioGroup mainTab;
	private TabHost tabhost;
	private Intent iBookmark;
	private Intent iBookCatalog;
    private RadioButton radioButton ;
    private static final int REQUEST_CODE = 1;   //请求码
	private String bookPath,bookName;
	public static BookPageFactory bookPageFactory;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.book_catalogue_activity);
		
		Intent intent = getIntent();
		bookPath = intent.getStringExtra("bookPath");
		bookName = intent.getStringExtra("bookName");
		
		TextView bookNameView =  (TextView) findViewById(R.id.catalogue_bookname);
		bookNameView.setText(bookName);
		
		ImageView  gobackImageView = (ImageView) findViewById(R.id.catalogue_goback);
		gobackImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		mainTab = (RadioGroup) findViewById(R.id.main_tab);
		mainTab.setOnCheckedChangeListener(this);
		tabhost = getTabHost();

		iBookmark = new Intent(this, BookmarkActivity.class);
		iBookmark.putExtra("bookPath", bookPath);
		iBookmark.putExtra("bookName", bookName);
		tabhost.addTab(tabhost
				.newTabSpec("iBookmark")
				.setIndicator(getResources().getString(R.string.main_home),
						getResources().getDrawable(R.drawable.icon_1_n))
				.setContent(iBookmark));

		iBookCatalog = new Intent(this,BookCatalogueActivity.class);
		iBookCatalog.putExtra("bookPath", bookPath);
		iBookCatalog.putExtra("bookName", bookName);
		BookCatalogueActivity.bookPageFactory = bookPageFactory;
		tabhost.addTab(tabhost
				.newTabSpec("iBookCatalog")
				.setIndicator(getResources().getString(R.string.main_search),
						getResources().getDrawable(R.drawable.icon_4_n))
				.setContent(iBookCatalog));
		
		//默认显示书架
		radioButton = (RadioButton) findViewById(R.id.radio_button1);
		radioButton.setChecked(true);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.radio_button0:
			this.tabhost.setCurrentTabByTag("iBookCatalog");
			break;
		case R.id.radio_button1:
			this.tabhost.setCurrentTabByTag("iBookmark");
			break;
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	@Override
	protected void onPause() {
           super.onPause();
   }	
}