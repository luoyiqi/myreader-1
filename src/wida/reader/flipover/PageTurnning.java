package wida.reader.flipover;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


/**
 * 3D·­ÊéÐ§¹û
 * @author xiechuanxia
 *
 */
public class PageTurnning extends View {
	protected Bitmap foreImage;
	protected Bitmap bgImage;
	protected Bitmap currentImage;
	protected int screenWidth;
	protected int screenHeight;
	
	public PageTurnning(Context context, AttributeSet attrs) {
		super(context, attrs);
	//	this.mMaxLength = (float) Math.hypot(mWidth, mHeight);
		// TODO Auto-generated constructor stub
		
	}
	
	
	public void setBitmaps(Bitmap currentImage, Bitmap foreImage, Bitmap bgImage) {
		this.foreImage = foreImage;
		this.bgImage = bgImage;
		this.currentImage = currentImage;
	}

	public void SetScreen(int w, int h) {
		screenWidth = w;
		screenHeight = h;
	}
	
	boolean doTouchEvent(MotionEvent event, int action){
		return false;
	}
	void abortAnimation()
	{}

}
