package wida.reader.adapter;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import wida.reader.R;
import wida.reader.util.Book;
import wida.reader.util.BookCatalogue;
import wida.reader.util.Bookmark;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class BookmarkAdapter extends BaseAdapter {
	private List<Bookmark> mList;
	private Context mContext;
	//public static final int APP_PAGE_SIZE = 9;
	private PackageManager pm;
	private static String TAG = "BookmarkAdapter";
	
	public BookmarkAdapter(Context context, List<Bookmark> list) {
		mContext = context;
		pm = context.getPackageManager();
		mList = list;
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Bookmark bookmark = mList.get(position);
		BookmarkItem bookItem;
		if (convertView == null) {
			View v = LayoutInflater.from(mContext).inflate(R.layout.bookmark_item,
					null);

			bookItem = new BookmarkItem();
			bookItem.bookmarkString = (TextView) v.findViewById(R.id.bookmark_string);
			bookItem.percentTextView = (TextView) v.findViewById(R.id.bookmark_percent);
			bookItem.createTimeTextView = (TextView) v.findViewById(R.id.bookmark_createtime);
			//bookItem.name = (TextView) v.findViewById(R.id.tuaninfo);
			v.setTag(bookItem);
			convertView = v;
		} else {
			bookItem = (BookmarkItem) convertView.getTag();
		}
		// set icon
		//bookItem.icon.setImageResource(R.drawable.cover_txt);
		// set name
		bookItem.bookmarkString.setText(bookmark.markString);
		bookItem.percentTextView.setText(bookmark.percert);
		bookItem.createTimeTextView.setText(bookmark.createTime);
		//bookItem.createTimeTextView.setText(timeString);
		return convertView;
	}

	/**
	 * 每个应用显示的内容，包括图标和名称
	 */
	class BookmarkItem {
		//ImageView icon;
		TextView bookmarkString;
		TextView percentTextView;
	    TextView createTimeTextView;
	}
}
