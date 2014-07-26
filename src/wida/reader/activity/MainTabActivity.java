package wida.reader.activity;

import wida.reader.R;
import android.annotation.SuppressLint;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class MainTabActivity extends TabActivity implements
		OnCheckedChangeListener {
	private static String TAG = "MainTabActivity";
	private RadioGroup mainTab;
	private TabHost tabhost;
	private Intent iHome;
	private Intent iSearch;
	private Intent iBookshelf;
	private Intent iSetting;
    private RadioButton radioButton ;
    private static final int REQUEST_CODE = 1;   //请求码
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_activity);
		mainTab = (RadioGroup) findViewById(R.id.main_tab);
		mainTab.setOnCheckedChangeListener(this);
		tabhost = getTabHost();

		iBookshelf = new Intent(this, BookshelfActivity.class);
		tabhost.addTab(tabhost
				.newTabSpec("iBookshelf")
				.setIndicator(
						getResources().getString(R.string.main_bookshelf),
						getResources().getDrawable(R.drawable.icon_3_n))
				.setContent(iBookshelf));

		iHome = new Intent(this, HomeActivity.class);
		tabhost.addTab(tabhost
				.newTabSpec("iHome")
				.setIndicator(getResources().getString(R.string.main_home),
						getResources().getDrawable(R.drawable.icon_1_n))
				.setContent(iHome));

		iSearch = new Intent(this, SearchActivity.class);
		tabhost.addTab(tabhost
				.newTabSpec("iSearch")
				.setIndicator(getResources().getString(R.string.main_search),
						getResources().getDrawable(R.drawable.icon_4_n))
				.setContent(iSearch));

		iSetting = new Intent(this, SettingActivity.class);
		tabhost.addTab(tabhost
				.newTabSpec("iSetting")
				.setIndicator(getResources().getString(R.string.main_setting),
						getResources().getDrawable(R.drawable.home_setting))
				.setContent(iSetting));
		
		//默认显示书架
		radioButton = (RadioButton) findViewById(R.id.radio_button2);
		radioButton.setChecked(true);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.radio_button0:
			this.tabhost.setCurrentTabByTag("iHome");
			break;
		case R.id.radio_button1:
			this.tabhost.setCurrentTabByTag("iSearch");
			break;
		case R.id.radio_button2:
			this.tabhost.setCurrentTabByTag("iBookshelf");
			break;
		case R.id.radio_button3:
			Log.v(TAG, "ddd");
			this.tabhost.setCurrentTabByTag("iSetting");
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
	
	protected void onNewIntent(Intent intent)
	{
		/*是否需要刷新书架*/
		if (intent.getBooleanExtra("reflashBookshelf", false))
		{
			Intent reflashintent = new Intent("bookshelf.reflash");
			sendBroadcast(reflashintent);
		}
	}
	
}