package wida.reader.activity;

import java.util.List;

import wida.reader.R;

import wida.reader.adapter.BookmarkAdapter;
import wida.reader.adapter.FileChooserAdapter;
import wida.reader.adapter.FileChooserAdapter.FileInfo;
import wida.reader.db.BookmarkDao;
import wida.reader.db.BooksDao;
import wida.reader.util.Bookmark;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class BookmarkActivity extends Activity {

	private List<Bookmark> mlist;
	private ListView bookmarkListView;
	private BookmarkAdapter mAdatper;
	private String bookPath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bookmark);

		Intent intent = getIntent();
		bookPath = intent.getStringExtra("bookPath");
		mlist = BookmarkDao.getBookmark(this, bookPath);

		bookmarkListView = (ListView) findViewById(R.id.bookmark_list);
		mAdatper = new BookmarkAdapter(this, mlist);
		bookmarkListView.setAdapter(mAdatper);
		bookmarkListView.setOnItemClickListener(mItemClickListener);
	}

	private AdapterView.OnItemClickListener mItemClickListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> adapterView, View view,
				int position, long id) {
			Bookmark bookmark = (Bookmark) (((BookmarkAdapter) adapterView
					.getAdapter()).getItem(position));
			Intent intent = new Intent(BookmarkActivity.this,
					BookReaderActivity.class);
			intent.putExtra("bookPath", bookPath);
			intent.putExtra("position", bookmark.position);
			BookmarkActivity.this.startActivity(intent);
			finish();
		}
	};
}