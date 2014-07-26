package wida.reader.gesture;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.GestureDetector.SimpleOnGestureListener;

public class GestureListener extends SimpleOnGestureListener {
	private Context mContext;
	//启用开关   -1 为不启用
    public static int toDrag = -1;
	
	public GestureListener(Context context) {
		mContext = context;
	}

	// 双击的第二下Touch down时触发
	public boolean onDoubleTap(MotionEvent e) {
		// Log.i("MyGesture", "onDoubleTap");
		return super.onDoubleTap(e);
	}

	// 双击的第二下Touch down和up都会触发，可用e.getAction()区分
	public boolean onDoubleTapEvent(MotionEvent e) {
		// Log.i("MyGesture", "onDoubleTapEvent");
		return super.onDoubleTapEvent(e);
	}

	// Touch down时触发
	public boolean onDown(MotionEvent e) {
		// Log.i("MyGesture", "onDown");
		toDrag = -1;
		return false;
	}

	// Touch了滑动一点距离后，up时触发
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// Log.i("MyGesture", "onFling");
		return super.onFling(e1, e2, velocityX, velocityY);
	}

	// Touch了不移动一直Touch down时触发
	public void onLongPress(MotionEvent e) {
		// Log.i("MyGesture", "onLongPress");
		super.onLongPress(e);
	}

	// // 参数解释：
	// e1：第1个ACTION_DOWN MotionEvent
	// e2：最后一个ACTION_MOVE MotionEvent
	// velocityX：X轴上的移动速度，像素/秒
	// velocityY：Y轴上的移动速度，像素/秒

	// 触发条件 ：
	// X轴的坐标位移大于FLING_MIN_DISTANCE，且移动速度大于FLING_MIN_VELOCITY个像素/秒
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		Log.v("PageWeiget", "onScroll");
		final int FLING_MIN_DISTANCE = 10, FLING_MIN_VELOCITY = 10;
		if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE
				&& Math.abs(distanceX) > FLING_MIN_VELOCITY) {
			toDrag = e2.getX() > e1.getX() ? 1 : 0; 
			Log.v("PageWeiget", "toDrag:"+toDrag);
			return false;
		} else if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE
				&& Math.abs(distanceX) > FLING_MIN_VELOCITY) {
			toDrag = e2.getX() > e1.getX() ? 1 : 0; 
			Log.v("PageWeiget", "toDrag:"+toDrag);
			return false;
		}
		return true;
	}

	/*
	 * Touch了还没有滑动时触发 (1)onDown只要Touch Down一定立刻触发 (2)Touch
	 * Down后过一会没有滑动先触发onShowPress再触发onLongPress So: Touch Down后一直不滑动，onDown ->
	 * onShowPress -> onLongPress这个顺序触发。
	 */
	public void onShowPress(MotionEvent e) {
		// Log.i("MyGesture", "onShowPress");
		super.onShowPress(e);
	}

	/*
	 * 两个函数都是在Touch Down后又没有滑动(onScroll)，又没有长按(onLongPress)，然后Touch Up时触发
	 * 点击一下非常快的(不滑动)Touch Up: onDown->onSingleTapUp->onSingleTapConfirmed
	 * 点击一下稍微慢点的(不滑动)Touch Up:
	 * onDown->onShowPress->onSingleTapUp->onSingleTapConfirmed
	 */
	public boolean onSingleTapConfirmed(MotionEvent e) {
		// Log.i("MyGesture", "onSingleTapConfirmed");
		return super.onSingleTapConfirmed(e);
	}

	public boolean onSingleTapUp(MotionEvent e) {
		// Log.i("MyGesture", "onSingleTapUp");
		return super.onSingleTapUp(e);
	}
	
	public int getToDrag()
	{
		return toDrag;
	}
}