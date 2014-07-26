package wida.reader.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;


public class HomeActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TextView textView=new TextView(this);
		textView.setText("ÕâÊÇÊ×Ò³£¡");
		setContentView(textView);
	}
}