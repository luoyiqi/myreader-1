package wida.reader.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import android.R.bool;
import android.R.raw;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import wida.reader.R;
import wida.reader.filter.BookFilter;

public class FileChooserAdapter extends BaseAdapter {
	private ArrayList<FileInfo> mFileLists;
	private LayoutInflater mLayoutInflater = null;
	private static ArrayList<String> BOOK_SUFFIX = new ArrayList<String>();
	private BookFilter mBookFilter;
	private HashMap<String, FileInfo> mSelectBooks;
	
	static {
		BOOK_SUFFIX.add(".txt");
		// BOOK_SUFFIX.add(".pptx");
	}

	public FileChooserAdapter(Context context, ArrayList<FileInfo> fileLists, HashMap<String, FileInfo> fileSelected) {
		super();
		mFileLists = fileLists;
		mSelectBooks = fileSelected;
		mLayoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mFileLists.size();
	}

	@Override
	public FileInfo getItem(int position) {
		// TODO Auto-generated method stub
		return mFileLists.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = null;
		ViewHolder holder = null;
		FileInfo fileInfo = getItem(position);
		if (convertView == null || convertView.getTag() == null) {
			view = mLayoutInflater.inflate(R.layout.filechooser_bookitem, null);
			holder = new ViewHolder(view);
			view.setTag(holder);
		} else {
			view = convertView;
			holder = (ViewHolder) convertView.getTag();
		}

		holder.tvFileName.setText(fileInfo.getFileName());

		if (fileInfo.isDirectory()) {
			holder.imgFileIcon.setImageResource(R.drawable.filechooser_folder);
			File temp = new File(fileInfo.getFilePath());
			mBookFilter = new BookFilter();
			int fileNum = temp.listFiles(mBookFilter).length;
			holder.tvText.setText(fileNum + "По");
			holder.tvFileName.setTextColor(Color.GRAY);
			holder.imgSelect.setVisibility(View.INVISIBLE);
			holder.tvText.setVisibility(View.VISIBLE);
		} else if (fileInfo.isTxTFile()) {
			holder.imgFileIcon.setImageResource(R.drawable.filechooser_txt);
			holder.tvFileName.setTextColor(Color.RED);
			holder.imgSelect.setVisibility(View.VISIBLE);
			if (mSelectBooks.containsKey(fileInfo.getFilePath()))
				holder.imgSelect.setImageResource(R.drawable.checkbox_ok);
			else
				holder.imgSelect.setImageResource(R.drawable.checkbox_empty);
			holder.tvText.setVisibility(View.INVISIBLE);
		}
		return view;
	}

	static class ViewHolder {
		ImageView imgFileIcon;
		TextView tvFileName;
		ImageView imgSelect;
		TextView tvText;

		public ViewHolder(View view) {
			imgSelect = (ImageView) view.findViewById(R.id.book_select);
			imgFileIcon = (ImageView) view.findViewById(R.id.array_image);
			tvFileName = (TextView) view.findViewById(R.id.array_title);
			tvText = (TextView) view.findViewById(R.id.array_text);
		}
	}

	enum FileType {
		FILE, DIRECTORY;
	}

	// =========================
	// Model
	// =========================
	static public class FileInfo {
		private FileType fileType;
		private String fileName;
		private String filePath;

		public FileInfo(String filePath, String fileName, boolean isDirectory) {
			this.filePath = filePath;
			this.fileName = fileName;
			fileType = isDirectory ? FileType.DIRECTORY : FileType.FILE;
		}

		public boolean isTxTFile() {
			if (fileName.lastIndexOf(".") < 0) // Don't have the suffix
				return false;
			String fileSuffix = fileName.substring(fileName.lastIndexOf("."));
			if (!isDirectory() && BOOK_SUFFIX.contains(fileSuffix))
				return true;
			else
				return false;
		}

		public boolean isDirectory() {
			if (fileType == FileType.DIRECTORY)
				return true;
			else
				return false;
		}

		public String getFileName() {
			return fileName;
		}

		public void setFileName(String fileName) {
			this.fileName = fileName;
		}

		public String getFilePath() {
			return filePath;
		}

		public void setFilePath(String filePath) {
			this.filePath = filePath;
		}

		@Override
		public String toString() {
			return "FileInfo [fileType=" + fileType + ", fileName=" + fileName
					+ ", filePath=" + filePath + "]";
		}
	}

}
