package wida.reader.adapter;

import java.util.ArrayList;
import java.util.List;
import wida.reader.R;
import wida.reader.util.Book;
import android.content.Context;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class BookAdapter extends BaseAdapter {
	private List<Book> mList;
	private Context mContext;
	//public static final int APP_PAGE_SIZE = 9;
	private PackageManager pm;

	public BookAdapter(Context context, List<Book> list, int page,int pageSize) {
		mContext = context;
		pm = context.getPackageManager();

		mList = new ArrayList<Book>();
		int i = page * pageSize;
		int iEnd = i + pageSize;
		while ((i < list.size()) && (i < iEnd)) {
			mList.add(list.get(i));
			i++;
		}
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
		Book book = mList.get(position);
		BookItem bookItem;
		if (convertView == null) {
			View v = LayoutInflater.from(mContext).inflate(R.layout.book_item,
					null);

			bookItem = new BookItem();
			bookItem.icon = (ImageView) v.findViewById(R.id.imgdetail);
			bookItem.name = (TextView) v.findViewById(R.id.tuaninfo);
			v.setTag(bookItem);
			convertView = v;
		} else {
			bookItem = (BookItem) convertView.getTag();
		}
		// set icon
		bookItem.icon.setImageResource(R.drawable.cover_txt);
		// set name
		bookItem.name.setText(book.bookName);

		return convertView;
	}

	/**
	 * 每个应用显示的内容，包括图标和名称
	 */
	class BookItem {
		ImageView icon;
		TextView name;
	}
}
