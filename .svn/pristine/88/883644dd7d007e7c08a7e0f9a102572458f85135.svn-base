package wida.reader.activity;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import wida.reader.R;
import wida.reader.adapter.ReaderMenuAdapter;
import wida.reader.db.BookmarkDao;
import wida.reader.db.BooksDao;
import wida.reader.flipover.Page3D;
import wida.reader.flipover.PageFilp;
import wida.reader.util.BookPageFactory;
import wida.reader.util.Bookmark;
import wida.reader.util.MenuInfo;
import wida.reader.util.MenuUtils;
import android.R.integer;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.ToggleButton;
import wida.reader.gesture.GestureListener;

public class BookReaderActivity extends Activity implements  OnSeekBarChangeListener {
	/** Called when the activity is first created. */
	private Page3D Page3D;
	private PageFilp PageFilp;
	private String bookPath;
	private String bookName;
    private int  position = 0;
    private static final String TAG = "BookReaderActivity";
    private Paint mPaint;
	private int width,height;
	private Handler handler;
	private String nowTime;
	private BookPageFactory pagefactory;
    private static String timeFormatString = "HH:mm";
	private Bitmap  bgBitmap;
	private PopupWindow popup , toolPopFontSize,toolPopLight,toolPopProgress;
	private int batteryPower  = -1;
	public Bitmap mCurPageBitmap,mNextImageBitmap,mForeImageBitmap;
	public Canvas mCurPageCanvas,mNextPageCanvas,mForePageCanvas;
	private GestureDetector mGestureDetector;
	private TextView vPercent;
	private boolean threadRun = true;
	private View toolFontSize, toolLight, toolProgress;
	private TextView progress_percent;
	private SeekBar tool_progress_seekBar,brightnessBar;
	private WindowManager.LayoutParams windowLayout;
	
	/**
	 * 定义适配器
	 */
	private ReaderMenuAdapter menuAdapter,menuAdapter2;
	//菜单项列表
	private List<MenuInfo> menulists,menulists2;
	//定义gridview
	private GridView menuGridView,menuGridView2;
    private ImageView  imageView,menuGoBackimgageView,hr_line;

    private ImageView BatteryimageView;
    private TextView nowTextView,bookNameView;
    private BatteryBroadcastReceiver batteryBroadcastReceiver = null;
    SharedPreferences readerPreferences = null;
    //系统亮度值
    int brightness = 255;
    int reader_brightness = 0;
    int toolTop = 0;
    int brightnessMode = 0;
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.main);
		Intent intent = getIntent();
		bookPath = intent.getStringExtra("bookPath");
		bookName = intent.getStringExtra("bookName");
		
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		//文本绘制方向
		mPaint.setTextAlign(Align.LEFT);
	    mPaint.setFakeBoldText(false);
		mPaint.setTextSize(14);
		mPaint.setColor( Color.argb(180, 0, 0, 51));
		
		SimpleDateFormat sDateFormat = new SimpleDateFormat(timeFormatString);     
		nowTime = sDateFormat.format(new java.util.Date());
  
	    mGestureDetector = new GestureDetector(this, new GestureListener(this));	   
	    position = BooksDao.getBookReaderPostion(this, bookPath);
	   
	   //app无标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//系统全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		/* 获取屏幕尺寸 */
		Display display = getWindowManager().getDefaultDisplay();
		width = display.getWidth();
		height =display.getHeight();
		//mPageWidget = new PageWidget(this, xWidth, yHeight);
		//setContentView(mPageWidget);
		setContentView(R.layout.book_reader_flip);
		BatteryimageView = (ImageView) findViewById(R.id.Battery);
		PageFilp = (PageFilp) findViewById(R.id.PageFilp);
		PageFilp.SetScreen(width, height);
		
	    bookNameView = (TextView) findViewById(R.id.bookName);
		nowTextView = (TextView) findViewById(R.id.nowTime);
		vPercent = (TextView) findViewById(R.id.percent);
		hr_line = (ImageView) findViewById(R.id.hr_line);
	    bookNameView.setText(bookName);
		

		mCurPageBitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		mForeImageBitmap = mNextImageBitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		readerPreferences  =  getSharedPreferences("widaReader",MODE_PRIVATE);
	/*	mForeImageBitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);*/
		
		mCurPageCanvas = new Canvas(mCurPageBitmap);
		mForePageCanvas = mNextPageCanvas = new Canvas(mNextImageBitmap);
/*		mForePageCanvas = new Canvas(mForeImageBitmap);*/
	    
		pagefactory = new BookPageFactory(width, height,bookName);

		/**设置屏幕亮度**/
		reader_brightness = readerPreferences.getInt("reader_brightness", 0);
		brightnessMode = getScreenMode();
		
        try{  
        	brightness = (int) (Settings.System.getInt(
                    getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS, 255));
        } catch (Exception localException){  
            
        }
        windowLayout = getWindow().getAttributes();
        boolean use_system_brightness =  readerPreferences.getBoolean("use_system_brightness", true);
        
        if (!use_system_brightness)
        {
        	setBrightness(reader_brightness);
        	setScreenMode(1);
		}
	    		
		/** 设置背景图片 **/
		Bitmap dstbmp = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.readerbackground);
		Matrix matrix = new Matrix();// 使用matri控制图形变换
		float w = ((float) width / dstbmp.getWidth());// 屏幕宽度除图片宽度
		float h = ((float) height / dstbmp.getHeight());// 屏幕高度除图片高度
		matrix.postScale(w, h);// 获取缩放比例
		// 根据缩放比例获得新的位图
		bgBitmap = Bitmap.createBitmap(dstbmp, 0, 0,
				dstbmp.getWidth(), dstbmp.getHeight(), matrix, true);
		
		//是否夜晚模式
		if (readerPreferences.getBoolean("night_mode", false))
		{
			pagefactory.setTextColor(Color.rgb(128, 128, 128));
			pagefactory.setBgBitmap(null);
			pagefactory.setBgcolor(Color.BLACK);
	
			hr_line.setBackgroundColor(Color.rgb(128, 128, 128));
		    bookNameView.setTextColor(Color.rgb(128, 128, 128));
			nowTextView.setTextColor(Color.rgb(128, 128, 128));
			vPercent.setTextColor(Color.rgb(128, 128, 128));
		}else {
			pagefactory.setBgBitmap(bgBitmap);
		}
		
		
		try {
			File file = new File(bookPath);
			if (!file.exists()) {
				throw new IOException(bookPath + " is not exists!");
			}
			Log.v(TAG, "position:"+position);
			pagefactory.openbook(bookPath,position);
			
			//SharedPreferences readerPreferences  =  getSharedPreferences("widaReader",MODE_PRIVATE);
			int fontSize = readerPreferences.getInt("fontSize", 0);
			if (fontSize > 0)
			{
				pagefactory.setFontSize(fontSize);
			}
			pagefactory.Draw(mCurPageCanvas);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			Toast.makeText(this, bookPath + "文件不存在", Toast.LENGTH_SHORT).show();
		}

		PageFilp.setBitmaps(mCurPageBitmap, mForeImageBitmap,mNextImageBitmap);
        drawTime(null);
        batteryBroadcastReceiver = new BatteryBroadcastReceiver();
		registerReceiver(batteryBroadcastReceiver , new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
		
		 handler = new Handler() {
             public void handleMessage(Message msg) {
                // textView.setText((String)msg.obj);
            	//显示百分比
            	drawPercent();
            	String time = (String)msg.obj;
            	if (!nowTime.equals(time))
            	{
            		drawTime(time);
            		nowTime = time;
            	}
             }
         };
         
        new Thread(runNowTime).start();        
        initPopuWindows();
        initToolsPopWindows();
      //  PageFilp.setFocusableInTouchMode(true);
        PageFilp.setOnTouchListener(new OnTouchListener() {
        	public boolean onTouch(View v, MotionEvent e) {
				// TODO Auto-generated method stub
				boolean ret = false;
				if (v == PageFilp) {
					if (e.getAction() == MotionEvent.ACTION_DOWN) {
						mGestureDetector.onTouchEvent(e); 
						GestureListener.toDrag = -1;
						PageFilp.abortAnimation();
						pagefactory.Draw(mCurPageCanvas);
						ret = PageFilp.doTouchEvent(e,-1);
						return ret;
					}else if(e.getAction() == MotionEvent.ACTION_MOVE)
					{
						long book_positon = -1;
						if (GestureListener.toDrag == -1){
							if( false == mGestureDetector.onTouchEvent(e)){
								Log.v("PageWeiget", "action_move");
								if (GestureListener.toDrag ==1)
								{
									try {
										book_positon = pagefactory.prePage();
									} catch (IOException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
									Log.v("PageWeiget", pagefactory.isfirstPage()+"");
									if (pagefactory.isfirstPage()){
										GestureListener.toDrag = -1;
										return false;
									}
									pagefactory.Draw(mForePageCanvas);
								}else if (GestureListener.toDrag ==0) {
									try {
										book_positon = pagefactory.nextPage();
									} catch (IOException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
									if (pagefactory.islastPage())
									{
										GestureListener.toDrag = -1;
										return false;
									}
									pagefactory.Draw(mNextPageCanvas);
								}
								
								if (book_positon != -1){
									BooksDao.updatePosition(BookReaderActivity.this, bookPath, book_positon);
								}
								PageFilp.setBitmaps(mCurPageBitmap,mForeImageBitmap, mNextImageBitmap);
								
								return false;
							}
						}
						ret = PageFilp.doTouchEvent(e,GestureListener.toDrag);
						return ret;
						
					}else {
						ret = PageFilp.doTouchEvent(e,GestureListener.toDrag);
					}
				}
				return false;
			}
		});
	}
	
	 /**接受电量改变广播*/
    public class BatteryBroadcastReceiver extends BroadcastReceiver{
    	@Override
    	public void onReceive(Context context, Intent intent) {
    		if(intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)){
    			 if(intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)){  
    	                int level = intent.getIntExtra("level", 0);  
    	                int scale = intent.getIntExtra("scale", 100);  
    	                int curPower = (level * 100 / scale)/25;  
    	                Log.v(TAG, "batteryPower");
    	                if (batteryPower != curPower ){
    	                	batteryPower = curPower;
	    	                switch (curPower) {  
		    	                case 0:  
		    	                	BatteryimageView.setImageBitmap(BitmapFactory.decodeResource(BookReaderActivity.this.getResources(), R.drawable.power0));  
		    	                    break;  
		    	                case 1:  
		    	                	BatteryimageView.setImageBitmap(BitmapFactory.decodeResource(BookReaderActivity.this.getResources(), R.drawable.power1));  
		    	                    break;  
		    	                case 2:  
		    	                	BatteryimageView.setImageBitmap(BitmapFactory.decodeResource(BookReaderActivity.this.getResources(), R.drawable.power2));  
		    	                    break;  
		    	                case 3:  
		    	                	BatteryimageView.setImageBitmap(BitmapFactory.decodeResource(BookReaderActivity.this.getResources(), R.drawable.power3));  
		    	                    break;  
		    	                case 4:  
		    	                	BatteryimageView.setImageBitmap(BitmapFactory.decodeResource(BookReaderActivity.this.getResources(), R.drawable.power4));  
		    	                    break;  
	    	                }  
    			 		}
    	            }  
    	        }
    		}
    	
    }
    
    @Override
    public void onDestroy()
    {
	     super.onDestroy();
	     if(batteryBroadcastReceiver != null)
	     {
	    	 unregisterReceiver(batteryBroadcastReceiver);
	     }
	     threadRun = false;
	     pagefactory = null;
	     PageFilp = null;
	     mCurPageBitmap   = null;
	     mNextImageBitmap = null;
	     mForeImageBitmap = null;
	     
	     Log.v(TAG, "destroy");
	     finish();
    }
    
    private void drawTime(String time)
    {
    	time  = (time != null) ? time : nowTime;
    	nowTextView.setText(time);
    }
    
    private void drawPercent()
    {
    	if (threadRun && pagefactory != null ){
	    	String percentString = pagefactory.getPercent();
	    	vPercent.setText(percentString);
    	}
    }
    
    Runnable runNowTime = new Runnable() {  
        public void run() {
            // TODO Auto-generated method stub
            try {
                while(threadRun){
                	SimpleDateFormat sDateFormat = new SimpleDateFormat(timeFormatString);     
            		String time = sDateFormat.format(new java.util.Date());
                    handler.sendMessage(handler.obtainMessage(100,time));
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }; 
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return true;
	}
    
	/**
	 * 设置PopupWindows
	 */
	private void initPopuWindows() {
    	//初始化gridview
		//menuGridView=(GridView)View.inflate(this, R.layout.gridview_menu, null);
		
		//初始化PopupWindow,LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT控制显示
		LayoutInflater inflater = LayoutInflater.from(this); 
		View view = (View)inflater.inflate(R.layout.reader_gridview_menu, null); 
		menuGridView=(GridView)view.findViewById(R.id.gvmenu);
		menuGridView2=(GridView)view.findViewById(R.id.gvmenu2);
		imageView = (ImageView)view.findViewById(R.id.imageView1);
		imageView.setAlpha(100);
		
		menuGoBackimgageView = (ImageView)view.findViewById(R.id.menu_goback);
		
		popup = new PopupWindow(view, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		popup.setFocusable(true);
		//设置显示和隐藏的动画
		//popup.setAnimationStyle(R.style.menushow);
		popup.update();
		popup.setOutsideTouchable(true);
		//设置触摸获取焦点
		view.setFocusableInTouchMode(true);
		//设置键盘事件,如果按下菜单键则隐藏菜单
		view.setOnKeyListener(new android.view.View.OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if (((keyCode == KeyEvent.KEYCODE_MENU) || (keyCode == KeyEvent.KEYCODE_BACK)))
					{
					  if(popup.isShowing())closePopWindowAndFullscreen();
					  toolPopDismiss();
					return true;
				}
				return false;
			}

		});
		imageView.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				closePopWindowAndFullscreen();  
			}
		}
		);
		
		menuGoBackimgageView.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
				}
			}
		);
		
		//添加菜单按钮事件
		menuGridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				MenuInfo mInfo = menulists.get(arg2);
				//closePopWindowAndFullscreen();
				if (mInfo.ishide) {
					return;
				}
				Log.v("ClickListener", "menuGridView1 num" + mInfo.menuId);
				switch (mInfo.menuId) {
					case MenuUtils.MENU_BOOKMARK:
						//Toast.makeText(BookReaderActivity.this, "书签", 1).show();
						int position = pagefactory.getBufBegin();
						ContentValues values = new ContentValues();
						values.put("filePath", bookPath);
						values.put("markString", pagefactory.getFirstLineText());
						values.put("position", position);
						Log.v(TAG, "bookmark_position"+position);
						values.put("percent", pagefactory.getPercent());
						//values.put("createTime",System.currentTimeMillis());
						SimpleDateFormat   formatter   =   new   SimpleDateFormat   ("yyyy/MM/dd HH:mm:ss");     
						Date   curDate   =   new   Date(System.currentTimeMillis());//获取当前时间     
						String   timeString   =   formatter.format(curDate); 
						values.put("createTime",timeString);
						BookmarkDao.addBookmark(BookReaderActivity.this, values);
						closePopWindowAndFullscreen();
						Toast.makeText(BookReaderActivity.this, "添加书签成功", 1).show();
						break;
					case MenuUtils.MENU_MENU:
						 Intent intent=new Intent(BookReaderActivity.this,BookCatalogueTabActivity.class); 
					     intent.putExtra("bookPath", bookPath); 
					     intent.putExtra("bookName", bookName); 
					     BookCatalogueTabActivity.bookPageFactory = pagefactory;
					     BookReaderActivity.this.startActivity(intent); 
					     closePopWindowAndFullscreen(); 
						break;
					case  MenuUtils.MENU_BOOKMARK_N :
						break;
				}
			}
		});
		menuGridView2.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				MenuInfo mInfo = menulists2.get(arg2);
				//popup.dismiss();
				//closePopWindowAndFullscreen();
				if (mInfo.ishide) {
					return;
				}
				Log.v("ClickListener", "menuGridView2 num" + mInfo.menuId);
				switch (mInfo.menuId) {
					case MenuUtils.MENU_SETTING:
						//Toast.makeText(BookReaderActivity.this, "设置", 1).show();
						Intent  mIntent = new Intent(BookReaderActivity.this,ReaderSettingActivity.class);
						BookReaderActivity.this.startActivity(mIntent);
						closePopWindowAndFullscreen(); 
						break;
					case MenuUtils.MENU_LINGHT:
						//Toast.makeText(BookReaderActivity.this, "调整亮度", 1).show();
						setToolPop(2);
						break;
					case MenuUtils.MENU_SIZE:
						//Toast.makeText(BookReaderActivity.this, "字体大小", 1).show();
						setToolPop(1);
						break;	
					case MenuUtils.MENU_JUMP:
						setToolPop(3);
						break;
				}
			}
		});
	}
    
    @Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		// TODO Auto-generated method stub
    	showMenuPopWindow();
		return false;// 返回为true 则显示系统menu
	}
    	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add("menu");
		return super.onPrepareOptionsMenu(menu);
	}
	
	/**
	 *  关闭popwindow 并且宣示全屏
	 *  Author :  wida
	 *  Version:  1.0 
	 *  Description :
	 */
	private void  closePopWindowAndFullscreen(){
		toolTop = 0;
		toolPopDismiss();
		popup.dismiss();
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}
	
	private void showMenuPopWindow()
	{
		if (popup != null) {
			boolean flag = false;
			int now_positon  = pagefactory.getBufBegin();
			int count  = BookmarkDao.getBookmarkByPosition(this,now_positon,bookPath);
			if(count > 0) flag = true;
			Log.v(TAG, "now_positon"+now_positon);
			Log.v(TAG, "count"+count);
			menulists = MenuUtils.getMenuList(flag);
			menuAdapter = new ReaderMenuAdapter(this, menulists);
			menuGridView.setAdapter(menuAdapter);
			
			menulists2 = MenuUtils.getMenuList2();
			menuAdapter2 = new ReaderMenuAdapter(this, menulists2);
			menuGridView2.setAdapter(menuAdapter2);
			//强制显示不全屏
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN, WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
			popup.showAtLocation(PageFilp, Gravity.TOP, 0, 0);
		}
	}

	/**
	 * 初始化工具按钮popwindows
	 */
	private void initToolsPopWindows()
	{
		toolFontSize = this.getLayoutInflater().inflate(R.layout.reader_tool_fontsize, null);
		toolPopFontSize = new PopupWindow(toolFontSize, 410, LayoutParams.WRAP_CONTENT);
		//toolPopFontSize.setFocusable(true);
		toolPopFontSize.setOutsideTouchable(true);
		
		toolLight = this.getLayoutInflater().inflate(R.layout.reader_tool_light, null);
		toolPopLight = new PopupWindow(toolLight, 410, LayoutParams.WRAP_CONTENT);
		//toolPopLight.setFocusable(true);
		toolPopLight.setOutsideTouchable(true);
		
		toolProgress = this.getLayoutInflater().inflate(R.layout.reader_tool_progress, null);
		toolPopProgress = new PopupWindow(toolProgress, 410, LayoutParams.WRAP_CONTENT);
		//toolPopProgress.setFocusable(true);
		toolPopProgress.setOutsideTouchable(true);
	}
	
	
	private void toolPopDismiss()
	{
		toolPopProgress.dismiss();
		toolPopLight.dismiss();
		toolPopFontSize.dismiss();
	}
	
	public void setToolPop(int a) {

		if (a == toolTop) return ;
		toolPopDismiss();
			// 点击字体按钮
			if (a == 1) {
				final int  postion = pagefactory.getBufBegin();
				toolPopFontSize.showAtLocation(PageFilp, Gravity.BOTTOM, 0, width* 45 / 320);
				ImageView tool_fontSize_down = (ImageView) toolFontSize.findViewById(R.id.reader_tool_size_down);
				ImageView tool_fontSize_up = (ImageView) toolFontSize.findViewById(R.id.reader_tool_size_up);
				tool_fontSize_down.setOnClickListener(new View.OnClickListener() {	
					@Override
					public void onClick(View v) {
						int fontSize = pagefactory.getFontSize();
						pagefactory.setFontSize(--fontSize);
						pagefactory.setBufBegin(postion);
						pagefactory.setBufEnd(postion);
						pagefactory.clearLine();
						postInvalidateUI();

						SharedPreferences.Editor editor = readerPreferences.edit();
						editor.putInt("fontSize", fontSize);
						editor.commit();
					//	Toast.makeText(BookReaderActivity.this, ""+fontSize, Toast.LENGTH_SHORT).show();
					}
				});
				tool_fontSize_up.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						int fontSize = pagefactory.getFontSize();
						pagefactory.setFontSize(++fontSize);
						pagefactory.setBufBegin(postion);
						pagefactory.setBufEnd(postion);
						pagefactory.clearLine();
						postInvalidateUI();
						SharedPreferences.Editor editor = readerPreferences.edit();
						editor.putInt("fontSize", fontSize);
						editor.commit();
						//Toast.makeText(BookReaderActivity.this, ""+fontSize, Toast.LENGTH_SHORT).show();
					}
				});
			}
			// 点击亮度按钮
			if (a == 2) {
				toolPopLight.showAtLocation(PageFilp, Gravity.BOTTOM, 0, width* 45 / 320);
				boolean useSystemBrightness = readerPreferences.getBoolean("use_system_brightness", true);
				ToggleButton useSystemLightButton = (ToggleButton) toolLight.findViewById(R.id.use_system_light_button);
				ToggleButton neightModeButton = (ToggleButton) toolLight.findViewById(R.id.neight_mode_button);
				brightnessBar = (SeekBar) toolLight.findViewById(R.id.tool_progress_light);
				
				if (useSystemBrightness)
				{
					useSystemLightButton.setChecked(true);
					//brightnessBar.setProgress(brightness);
					brightnessBar.setEnabled(false);
					brightnessBar.setClickable(false);
					brightnessBar.setSelected(false);
					brightnessBar.setFocusable(false);
				}else {
					brightnessBar.setProgress(reader_brightness);
					useSystemLightButton.setChecked(false);
				}

				if(readerPreferences.getBoolean("night_mode", false))
				{
					neightModeButton.setChecked(true);
				}else{
					neightModeButton.setChecked(false);
				}
				
				brightnessBar.setOnSeekBarChangeListener(this);
				//使用系统亮度
				useSystemLightButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						if(isChecked){
							//brightnessBar.setProgress(brightness);
							brightnessBar.setEnabled(false);
							brightnessBar.setClickable(false);
							brightnessBar.setSelected(false);
							brightnessBar.setFocusable(false);							
							setBrightness(brightness + 20+ 10);
							setScreenMode(brightnessMode);
						}
						else{
							brightnessBar.setEnabled(true);
							brightnessBar.setClickable(true);
							brightnessBar.setSelected(true);
							brightnessBar.setFocusable(true);
							setBrightness(reader_brightness);
							setScreenMode(1);
						}
						SharedPreferences.Editor editor = readerPreferences.edit();
						editor.putBoolean("use_system_brightness", isChecked);
						editor.commit();
					}
				});
				//夜晚模式
				neightModeButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						if(isChecked){
							pagefactory.setTextColor(Color.rgb(128, 128, 128));
							pagefactory.setBgBitmap(null);
							pagefactory.setBgcolor(Color.BLACK);

							hr_line.setBackgroundColor(Color.rgb(128, 128, 128));
						    bookNameView.setTextColor(Color.rgb(128, 128, 128));
							nowTextView.setTextColor(Color.rgb(128, 128, 128));
							vPercent.setTextColor(Color.rgb(128, 128, 128));
						}
						else{
							pagefactory.setTextColor(Color.argb(180, 0, 0, 51));
							pagefactory.setBgBitmap(bgBitmap);
							hr_line.setBackgroundColor(Color.argb(180, 0, 0, 51));
							bookNameView.setTextColor(Color.argb(180, 0, 0, 51));
							nowTextView.setTextColor(Color.argb(180, 0, 0, 51));
							vPercent.setTextColor(Color.argb(180, 0, 0, 51));
						}
						SharedPreferences.Editor editor = readerPreferences.edit();
						editor.putBoolean("night_mode", isChecked);
						editor.commit();
						pagefactory.setBufBegin(pagefactory.getBufBegin());
						pagefactory.setBufEnd(pagefactory.getBufBegin());
						postInvalidateUI();
					}
				});
			}
			// 点击转跳按钮
			if (a == 3) {
				toolPopProgress.showAtLocation(PageFilp, Gravity.BOTTOM, 0, width* 45 / 320);
				TextView progress_book_name = (TextView) toolProgress.findViewById(R.id.tool_progress_book_name);
				progress_book_name.setText(bookName);
				progress_percent = (TextView) toolProgress.findViewById(R.id.tool_progress_percent);
				tool_progress_seekBar = (SeekBar) toolProgress.findViewById(R.id.tool_progress_seekbar);
				float fPercent = (float) (pagefactory.getBufEnd() * 1.0 / pagefactory.getBufLen());
				DecimalFormat df = new DecimalFormat("#0");
				String strPercent = df.format(fPercent * 100) + "%";
				progress_percent.setText(strPercent);
				tool_progress_seekBar.setProgress(Integer.parseInt(df.format(fPercent * 100)));
				tool_progress_seekBar.setOnSeekBarChangeListener(this);
			}
			toolTop = a;
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		switch (seekBar.getId()) {
		// 亮度进度条
		case R.id.tool_progress_light:
			reader_brightness = brightnessBar.getProgress();
			Log.v(TAG, "light"+reader_brightness);
			setBrightness(reader_brightness);
			SharedPreferences.Editor editor = readerPreferences.edit();
			editor.putInt("reader_brightness", reader_brightness);
			editor.commit();
			break;
		// 跳转进度条
		case R.id.tool_progress_seekbar:
			if( !fromUser) break; 
			int s = tool_progress_seekBar.getProgress();
			Log.v(TAG, "s:"+s);
			progress_percent.setText(s + "%");
			int postion = (pagefactory.getBufLen() * s) / 100;
			Log.v(TAG, "vpostion:"+postion);
			pagefactory.setBufBegin(postion);
			pagefactory.clearLine();
		//	pagefactory.prePage();
			try {
				if (s == 100) {
					pagefactory.prePage();
					pagefactory.getBufBegin();
					postion = pagefactory.getBufBegin();
					pagefactory.setBufBegin(postion);
					pagefactory.setBufEnd(postion);
				}else if(s == 0){
					pagefactory.setBufBegin(0);
					pagefactory.setBufEnd(0);
				}else{
					//校准进度
					pagefactory.prePage();
				}
			} catch (IOException e) {
				Log.e(TAG, "onProgressChanged seekBar4-> IOException error", e);
			}
			Log.v(TAG, "progress_postion:"+postion);
		//	position = pagefactory.getBufBegin();
			BooksDao.updatePosition(BookReaderActivity.this, bookPath, pagefactory.getBufEnd());
			postInvalidateUI();
			break;
		}
		
	}

	/**
	 * 刷新界面
	 */
	public void postInvalidateUI() {
		PageFilp.abortAnimation();
		pagefactory.Draw(mCurPageCanvas);
		pagefactory.Draw(mNextPageCanvas);
		PageFilp.setBitmaps(mCurPageBitmap,mForeImageBitmap,mNextImageBitmap);
		PageFilp.postInvalidate();
	}
	
	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 设置亮度
	 * @param brightnesss
	 */
	private void setBrightness(int brightnesss)
	{
		float tmpFloat = (float) brightnesss / 255;  
        if (tmpFloat > 0 && tmpFloat <= 1) {  
            windowLayout.screenBrightness = tmpFloat;  
        } 
		getWindow().setAttributes(windowLayout);
	}
	
	@Override
	protected void onNewIntent(Intent intent)
	{
/*		bookPath = intent.getStringExtra("bookPath");
		bookName = intent.getStringExtra("bookName");*/
		int begin = intent.getIntExtra("position", 0);
		Log.v(TAG, "begin"+begin);
		pagefactory.setBufBegin(begin);
		pagefactory.setBufEnd(begin);
		pagefactory.clearLine();
		postInvalidateUI();
	}	
	
	/**
	* 获得当前屏幕亮度的模式 
	* SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 为自动调节屏幕亮度
	* SCREEN_BRIGHTNESS_MODE_MANUAL=0 为手动调节屏幕亮度
	*/
	private int getScreenMode(){
		int screenMode=0;
		try{
			screenMode = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE);
		}
		catch (Exception localException){

		}
		return screenMode;
	}
	
	/**
	* 设置当前屏幕亮度的模式 
	* SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 为自动调节屏幕亮度
	* SCREEN_BRIGHTNESS_MODE_MANUAL=0 为手动调节屏幕亮度
	*/
	private void setScreenMode(int paramInt){
		try{
			Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, paramInt);
		}catch (Exception localException){
			localException.printStackTrace();
		}
	}
	
}