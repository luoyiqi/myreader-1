package wida.reader.flipover;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region.Op;
import android.graphics.drawable.GradientDrawable;
import android.nfc.tech.TagTechnology;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

/**
 * 滑动翻书效果
 * @author xiechuanxia
 *
 */
public class PageFilp extends PageTurnning {
	private PointF touchPt;
	private GradientDrawable shadowDrawableRL;
	private GradientDrawable shadowDrawableLR;
	private ColorMatrixColorFilter mColorMatrixFilter;
	private Scroller mScroller;
	private int lastTouchX, fristTouchX = -200;
	//private Bitmap tmepBitmap;
	private float width;
	private int action = -1;
	private Bitmap memBm;
	private static String TAG = "Pagefilp";
	 //private static PageActivity activity;
	private int awidth;	
	public PageFilp(Context context, AttributeSet attrs) {
		super(context, attrs);
	//	this.mMaxLength = (float) Math.hypot(mWidth, mHeight);
		// TODO Auto-generated constructor stub
		touchPt = new PointF(-1, -1);
		// ARGB A(0-透明,255-不透明)
		int[] color = { 0xb0333333, 0x00333333 };
		shadowDrawableRL = new GradientDrawable(
				GradientDrawable.Orientation.RIGHT_LEFT, color);
		shadowDrawableRL.setGradientType(GradientDrawable.LINEAR_GRADIENT);

		shadowDrawableLR = new GradientDrawable(
				GradientDrawable.Orientation.LEFT_RIGHT, color);
		shadowDrawableLR.setGradientType(GradientDrawable.LINEAR_GRADIENT);

		float array[] = { 0.55f, 0, 0, 0, 80.0f, 0, 0.55f, 0, 0, 80.0f, 0, 0,
				0.55f, 0, 80.0f, 0, 0, 0, 0.2f, 0 };
		ColorMatrix cm = new ColorMatrix();
		cm.set(array);
		/*
		 * |A*0.55 + 80| |R*0.55 + 80| |G*0.55 + 80| |B*0.2|
		 */
		// cm.setSaturation(0);
		mColorMatrixFilter = new ColorMatrixColorFilter(cm);

		// 利用滚动条来实现接触点放开后的动画效果
		mScroller = new Scroller(context);
	}
	
	@Override
	public void computeScroll() {
		// TODO Auto-generated method stub
		if (mScroller.computeScrollOffset()) {
			touchPt.x = mScroller.getCurrX();
			touchPt.y = mScroller.getCurrY();
			// Log.v(TAG, "touchPt.x:"+touchPt.x + ",touchPt.y:"+touchPt.y);
			postInvalidate();
		} else {
			// action = -1;
		}
		super.computeScroll();
	}

	public void SetScreen(int screenWidth, int screenHeight) {
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		memBm = Bitmap.createBitmap(screenWidth, screenHeight,
				Bitmap.Config.ARGB_8888);
	}

	public void setBitmaps(Bitmap currentImage, Bitmap foreImage, Bitmap bgImage) {
		this.foreImage = foreImage;
		this.bgImage = bgImage;
		this.currentImage = currentImage;
	}

	public void abortAnimation() {
		if (!mScroller.isFinished()) {
			mScroller.abortAnimation();
		}
	}

	public int DragTo() {
		return action;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		//final Canvas c = new Canvas(memBm);
		drawPageEffect(canvas);
		//canvas.drawBitmap(memBm, 0, 0, null);
		super.onDraw(canvas);
	}

	private void drawCurrentImage(Canvas canvas) {
		// TODO Auto-generated method stub
		Paint mPaint = new Paint();
		if (currentImage != null) {
			canvas.drawBitmap(currentImage, 0, 0, mPaint);
		}
	}
	
	/**
	 * 画背景图片
	 * 
	 * @param canvas
	 */
	private void drawCurrentImage(Canvas canvas, Path path) {
		// TODO Auto-generated method stub
		Paint mPaint = new Paint();

		if (currentImage != null) {
			canvas.save();
			// 只在与路径相交处画图
			canvas.clipPath(path, Op.INTERSECT);
			canvas.drawBitmap(currentImage, 0, 0, mPaint);
			canvas.restore();
		}
	}

	/**
	 * 画背景图片
	 * 
	 * @param canvas
	 */
	private void drawBgImgae(Canvas canvas, Path path) {
		// TODO Auto-generated method stub
		Paint mPaint = new Paint();

		if (bgImage != null) {
			canvas.save();
			// 只在与路径相交处画图
			canvas.clipPath(path, Op.INTERSECT);
			canvas.drawBitmap(bgImage, 0, 0, mPaint);
			canvas.restore();
		}
	}

	private void drawPageEffect(Canvas canvas) {
		// TODO Auto-generated method stub

		// if (currentImage != null ) drawCurrentImage(canvas);
		if (action == -1)
			drawCurrentImage(canvas);
		//canvas.save();
		Paint mPaint = new Paint();
		if (touchPt.x != -1 && touchPt.y != -1) {

			width = touchPt.x - fristTouchX;
			Log.v(TAG, "touchPt.x" + touchPt.x + "fristTouchX : " + fristTouchX);
			Log.v(TAG, "" + width + "action : " + action);
			if (action == 1) {
				if (width < 0)
					width = 0;
				if (width > foreImage.getWidth()) {
					width = foreImage.getWidth();
				}
			}
			// 翻前一页
			if (width <= foreImage.getWidth() && action == 1) {
				if (width < 0)
					width = 0;
				if (width > foreImage.getWidth())
					width = foreImage.getWidth();
				if ((int) width != 0) {
					drawImage(canvas,foreImage,0,0,(int) width,(int)foreImage.getHeight(),(int)(getWidth() - width),0);
				}
				Path bgPath = new Path();
				Log.v("pageWidget", "fristTouchX:" + fristTouchX
						+ ",touchPt.x:" + touchPt.x + ",width:" + width);
				// 可以显示背景图的区域
				bgPath.addRect(new RectF(width, 0, screenWidth, screenHeight),
						Direction.CW);
				drawCurrentImage(canvas, bgPath);

				// 画阴影
				shadowDrawableLR.setBounds((int) width, 0, (int) width + 15,
						screenHeight);
				shadowDrawableLR.draw(canvas);
				//canvas.save();
			}

			if (Math.abs((int) width) >= currentImage.getWidth() && action == 0) {
				if (touchPt.x > 0)
					width = currentImage.getWidth();
				else {
					width = 0;
				}
			}
			// 翻当前页
			if ((Math.abs((int) width) <= currentImage.getWidth())
					&& action == 0) {
				if (width > 0)
					width = 0;
				awidth = Math.abs((int) width);
				Log.v("pageWidget", "awidth" + awidth);
			//	drawImage(canvas, foreImage);
				drawImage(canvas,currentImage,0,0,
						(int) (currentImage.getWidth() - awidth),
						currentImage.getHeight(),(int) awidth, 0);

				Path bgPath = new Path();
				// 可以显示背景图的区域
				bgPath.addRect(new RectF(foreImage.getWidth() - awidth, 0,
						screenWidth, screenHeight), Direction.CW);
				// 对折出右侧画背景
				drawBgImgae(canvas, bgPath);

				// 画阴影
				shadowDrawableLR.setBounds((int) (bgImage.getWidth() - awidth),
						0, (int) (int) (bgImage.getWidth() - awidth) + 15,
						screenHeight);
				shadowDrawableLR.draw(canvas);
				//canvas.save();
			}
		}
	}

	public boolean doTouchEvent(MotionEvent event, int action) {
		// TODO Auto-generated method stub
		this.action = action;
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			touchPt.x = event.getX();
			touchPt.y = event.getY();
			fristTouchX = (int) touchPt.x;
		} else if (event.getAction() == MotionEvent.ACTION_MOVE) {

			lastTouchX = (int) touchPt.x;
			touchPt.x = event.getX();
			touchPt.y = event.getY();
			postInvalidate();
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			int dx, dy;
			dy = 0;
			dx = 0;
			// 翻一页
			if (action == 1) {
				dx = foreImage.getWidth() + (int) width + 50;
				mScroller.startScroll((int) touchPt.x, (int) touchPt.y, dx, dy,
						1000);
				postInvalidate();
			} else if (action == 0) {
				if ((lastTouchX - fristTouchX) < 0){
					dx = -(foreImage.getWidth()  - (int)(lastTouchX - fristTouchX) + 30);
					currentImage = bgImage;
					mScroller.startScroll((int) touchPt.x, (int) touchPt.y, dx, dy,
							1000);
				}else {
					dx = -(foreImage.getWidth() + (int) (currentImage.getWidth() - awidth) + 30);
					currentImage = bgImage;
					mScroller.startScroll((int) touchPt.x, (int) touchPt.y, dx, dy,
							1000);
				}
				postInvalidate();
			}
			
		}

		// 必须为true，否则无法获取ACTION_MOVE及ACTION_UP事件
		return true;
	}
	
	private void drawImage(Canvas canvas, Bitmap blt, int x, int y, int w, int h, int bx, int by)
	  {                                                        //x,y表示绘画的起点，
	      Rect src = new Rect();// 图片
	      Rect dst = new Rect();// 屏幕位置及尺寸
	      //src 这个是表示绘画图片的大小
	      src.left = bx;   //0,0  
	      src.top = by;
	      src.right = bx + w;// mBitDestTop.getWidth();,这个是桌面图的宽度，
	      src.bottom = by + h;//mBitDestTop.getHeight()/2;// 这个是桌面图的高度的一半
	      // 下面的 dst 是表示 绘画这个图片的位置
	      dst.left = x;    //miDTX,//这个是可以改变的，也就是绘图的起点X位置
	      dst.top = y;    //mBitQQ.getHeight();//这个是QQ图片的高度。 也就相当于 桌面图片绘画起点的Y坐标
	      dst.right = x + w;    //miDTX + mBitDestTop.getWidth();// 表示需绘画的图片的右上角
	      dst.bottom = y + h;    // mBitQQ.getHeight() + mBitDestTop.getHeight();//表示需绘画的图片的右下角
	      canvas.drawBitmap(blt, src, dst, null);//这个方法  第一个参数是图片原来的大小，第二个参数是 绘画该图片需显示多少。也就是说你想绘画该图片的某一些地方，而不是全部图片，第三个参数表示该图片绘画的位置
	      src = null;
	      dst = null;
	  }
}