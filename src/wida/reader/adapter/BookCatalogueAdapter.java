package wida.reader.adapter;

import java.util.ArrayList;
import java.util.List;
import wida.reader.R;
import wida.reader.util.Book;
import wida.reader.util.BookCatalogue;
import android.content.Context;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class BookCatalogueAdapter extends BaseAdapter {
	private List<BookCatalogue> mList;
	private Context mContext;
	//public static final int APP_PAGE_SIZE = 9;
	private PackageManager pm;

	public BookCatalogueAdapter(Context context, List<BookCatalogue> list) {
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
		BookCatalogue bookCatalogue = mList.get(position);
		BookCalalogueItem catalogue;
		if (convertView == null) {
			View v = LayoutInflater.from(mContext).inflate(R.layout.book_catalogue_item,
					null);

			catalogue = new BookCalalogueItem();
			catalogue.catalogueName = (TextView) v.findViewById(R.id.catalogue_name);
			v.setTag(catalogue);
			convertView = v;
		} else {
			catalogue = (BookCalalogueItem) convertView.getTag();
		}
		catalogue.catalogueName.setText(bookCatalogue.catalogueName);
		return convertView;
	}

	/**
	 * 每个应用显示的内容，包括图标和名称
	 */
	class BookCalalogueItem {
		TextView catalogueName;
	}
}
