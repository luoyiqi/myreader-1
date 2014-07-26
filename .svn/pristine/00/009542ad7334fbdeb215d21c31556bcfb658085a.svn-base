package wida.reader.view;

import wida.reader.R;
import wida.reader.viewGroup.ScrollLayout;
import wida.reader.viewGroup.ScrollLayout.OnScreenChangeListener;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import wida.reader.viewGroup.ScrollLayout;

public class PageControlView extends LinearLayout {
	private Context context;

	private int count;
	public static int currentIndex;

	public void bindScrollViewGroup(ScrollLayout scrollViewGroup) {
		this.count = scrollViewGroup.getChildCount();
		//System.out.println("count=" + count);
		generatePageControl(scrollViewGroup.getCurrentScreenIndex());

		scrollViewGroup.setOnScreenChangeListener(new OnScreenChangeListener() {

			public void onScreenChange(int currentIndex) {
				// TODO Auto-generated method stub
				generatePageControl(currentIndex);
			}
		});
	}

	public PageControlView(Context context) {
		super(context);
		this.init(context);
	}

	public PageControlView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.init(context);
	}

	private void init(Context context) {
		this.context = context;
	}

	private void generatePageControl(int currentIndex) {
		this.removeAllViews();

		this.currentIndex = currentIndex;
		int pageNum = 10; // 显示多少个
		int pageNo = currentIndex + 1; // 第几页
		int pageSum = this.count; // 总共多少页

		if (pageSum > 1) {
			int currentNum = (pageNo % pageNum == 0 ? (pageNo / pageNum) - 1
					: (int) (pageNo / pageNum)) * pageNum;

			if (currentNum < 0)
				currentNum = 0;

			if (pageNo > pageNum) {
				ImageView imageView = new ImageView(context);

				imageView.setImageResource(R.drawable.ic_launcher);
				this.addView(imageView);
			}

			for (int i = 0; i < pageNum; i++) {
				if ((currentNum + i + 1) > pageSum || pageSum < 2)
					break;

				ImageView imageView = new ImageView(context);
				//调整分页条
				imageView.setScaleType(ImageView.ScaleType.FIT_XY); 
				imageView.setLayoutParams(new Gallery.LayoutParams(20,20)); 
				if (currentNum + i + 1 == pageNo) {
					imageView.setImageResource(R.drawable.scroll_point_light);
				} else {
					imageView.setImageResource(R.drawable.scroll_point_dark);
				}
				this.addView(imageView);
			}

			if (pageSum > (currentNum + pageNum)) {
				ImageView imageView = new ImageView(context);
				imageView.setImageResource(R.drawable.clogo);
				this.addView(imageView);
			}
		}
	}
}