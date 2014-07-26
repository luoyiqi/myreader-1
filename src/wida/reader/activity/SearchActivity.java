package wida.reader.activity;

import wida.reader.R;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;


public class SearchActivity extends Activity{

	private  WebView mWebView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_search); 
		 mWebView = (WebView) findViewById(R.id.search_webview);       
	        WebSettings webSettings = mWebView.getSettings();       
	        webSettings.setJavaScriptEnabled(true);            
	        mWebView.loadUrl("http://localhost/test/"); 
	}
}