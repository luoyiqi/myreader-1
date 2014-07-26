package wida.reader.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import wida.reader.db.CatalogueDao;
import android.R.integer;
import android.R.string;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.text.GetChars;
import android.util.Log;

public class BookPageFactory {

	final static String TAG = "BookPageFactory";
	private File book_file = null;
	private MappedByteBuffer m_mbBuf = null; // 内存中的图书字符
	private int m_mbBufLen = 0; // 图书总长度
	private int m_mbBufBegin = 0;//当前页起始位置 
	private int m_mbBufEnd = 0; // 当前页终点位置
	private String m_strCharsetName = "GBK";
	private Bitmap m_book_bg = null;
	private int mWidth;
	private int mHeight;
	private String bookName;

	private Vector<String> m_lines = new Vector<String>();

	private int m_fontSize = 34;
	private int m_textColor = Color.argb(180, 0, 0, 51);
	private int m_backColor = 0xffff9e85; // 背景颜色
	private int marginWidth = 30; // 左右与边缘的距离
	private int marginTop = 30; // 上边缘的距离
	private int marginBottom = 55; // 下边缘距离

	private int mLineCount; // 每页可以显示的行数
	private float mVisibleHeight; // 绘制内容的宽
	private float mVisibleWidth; // 绘制内容的宽
	private boolean m_isfirstPage,m_islastPage;

	private int m_nLineSpaceing = m_fontSize / 2 ;

	private Paint mPaint;

	public BookPageFactory(int w, int h,String bookName) {
		// TODO Auto-generated constructor stub
		mWidth = w;
		mHeight = h;

		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		//文本绘制方向
		mPaint.setTextAlign(Align.LEFT);
	    mPaint.setFakeBoldText(false);
		mPaint.setTextSize(m_fontSize);
		mPaint.setColor(m_textColor);
		
		mVisibleWidth = mWidth - marginWidth * 2;
		mVisibleHeight = mHeight - marginTop - marginBottom;
		mLineCount = (int) (mVisibleHeight / (m_fontSize+m_nLineSpaceing) )  - 1; // 可显示的行数
		this.bookName = bookName;
	}

	
	public int getFontSize()
	{
		return m_fontSize;
	}
	
	/**
	 * 打开文件
	 *  Author :  wida
	 *  Version:  1.0 
	 *  Description :
	 */
	public void openbook(String strFilePath,int positon) throws IOException {
		File file = new File(strFilePath);
		try {
			m_strCharsetName = Utils.getJavaEncode(file);
			Log.v(TAG,"m_strCharsetName:"+m_strCharsetName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.v(TAG,"m_strCharsetName:"+m_strCharsetName);
		//打开文件
		
		book_file = new File(strFilePath);
		//获取文件字节
		long lLen = book_file.length();
		
		/**内存映射**/
		m_mbBufLen = (int) lLen;
		m_mbBuf = new RandomAccessFile(book_file, "r").getChannel().map(FileChannel.MapMode.READ_ONLY, 0, lLen);
/*		m_mbBufBegin  = positon;
		//调整用户位置
		if (m_mbBufBegin > 0){
			//@todo 可能会有bug
			pageUp();
			pageDown();
		}*/
		
		if(positon > 0)
		{
			m_mbBufBegin = m_mbBufEnd = positon;
		}
		
	}
	
	public void setCharset(String charset)
	{
		m_strCharsetName = charset;
	}
	
	protected byte[] readParagraphBack(int nFromPos) {
		int nEnd = nFromPos;
		int i;
		byte b0, b1;
		if (m_strCharsetName.equals("UTF-16LE")) {
			i = nEnd - 2;
			while (i > 0) {
				b0 = m_mbBuf.get(i);
				b1 = m_mbBuf.get(i + 1);
				if (b0 == 0x0a && b1 == 0x00 && i != nEnd - 2) {
					i += 2;
					break;
				}
				i--;
			}

		} else if (m_strCharsetName.equals("UTF-16BE")) {
			i = nEnd - 2;
			while (i > 0) {
				b0 = m_mbBuf.get(i);
				b1 = m_mbBuf.get(i + 1);
				if (b0 == 0x00 && b1 == 0x0a && i != nEnd - 2) {
					i += 2;
					break;
				}
				i--;
			}
		} else {
			i = nEnd - 1;
			while (i > 0) {
				b0 = m_mbBuf.get(i);
				if (b0 == 0x0a && i != nEnd - 1) {
					i++;
					break;
				}
				i--;
			}
		}
		if (i < 0)
			i = 0;
		int nParaSize = nEnd - i;
		int j;
		byte[] buf = new byte[nParaSize];
		for (j = 0; j < nParaSize; j++) {
			buf[j] = m_mbBuf.get(i + j);
		}
		return buf;
	}
	
	public void currentPage() throws IOException {
		m_lines.clear();
		m_lines = pageDown();
	}

	// 读取上一段落
	protected byte[] readParagraphForward(int nFromPos) {
		int nStart = nFromPos;
		int i = nStart;
		byte b0, b1;
		// 根据编码格式判断换行
		if (m_strCharsetName.equals("UTF-16LE")) {
			while (i < m_mbBufLen - 1) {
				b0 = m_mbBuf.get(i++);
				b1 = m_mbBuf.get(i++);
				if (b0 == 0x0a && b1 == 0x00) {
					break;
				}
			}
		} else if (m_strCharsetName.equals("UTF-16BE")) {
			while (i < m_mbBufLen - 1) {
				b0 = m_mbBuf.get(i++);
				b1 = m_mbBuf.get(i++);
				if (b0 == 0x00 && b1 == 0x0a) {
					break;
				}
			}
		} else {
			while (i < m_mbBufLen) {
				b0 = m_mbBuf.get(i++);
				if (b0 == 0x0a) {
					break;
				}
			}
		}
		int nParaSize = i - nStart;
		byte[] buf = new byte[nParaSize];
		for (i = 0; i < nParaSize; i++) {
			buf[i] = m_mbBuf.get(nFromPos + i);
		}
		return buf;
	}

	protected Vector<String> pageDown() {
		String strParagraph = "";
		Vector<String> lines = new Vector<String>();
		while (lines.size() < mLineCount && m_mbBufEnd < m_mbBufLen) {
			byte[] paraBuf = readParagraphForward(m_mbBufEnd); // 读取一个段落
			m_mbBufEnd += paraBuf.length;
			try {
				strParagraph = new String(paraBuf, m_strCharsetName);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String strReturn = "";
			if (strParagraph.indexOf("\r\n") != -1) {
				strReturn = "\r\n";
				strParagraph = strParagraph.replaceAll("\r\n", "");
			} else if (strParagraph.indexOf("\n") != -1) {
				strReturn = "\n";
				strParagraph = strParagraph.replaceAll("\n", "");
			}

			if (strParagraph.length() == 0) {
				lines.add(strParagraph);
			}
			while (strParagraph.length() > 0) {
				int nSize = mPaint.breakText(strParagraph, true, mVisibleWidth,
						null);
				lines.add(strParagraph.substring(0, nSize));
				strParagraph = strParagraph.substring(nSize);
				if (lines.size() >= mLineCount) {
					break;
				}
			}
			if (strParagraph.length() != 0) {
				try {
					m_mbBufEnd -= (strParagraph + strReturn)
							.getBytes(m_strCharsetName).length;
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return lines;
	}

	protected void pageUp() {
		if (m_mbBufBegin < 0)
			m_mbBufBegin = 0;
		Vector<String> lines = new Vector<String>();
		String strParagraph = "";
		while (lines.size() < mLineCount && m_mbBufBegin > 0) {
			Vector<String> paraLines = new Vector<String>();
			byte[] paraBuf = readParagraphBack(m_mbBufBegin);
			m_mbBufBegin -= paraBuf.length;
			try {
				strParagraph = new String(paraBuf, m_strCharsetName);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			strParagraph = strParagraph.replaceAll("\r\n", "");
			strParagraph = strParagraph.replaceAll("\n", "");

			if (strParagraph.length() == 0) {
				paraLines.add(strParagraph);
			}
			while (strParagraph.length() > 0) {
				int nSize = mPaint.breakText(strParagraph, true, mVisibleWidth,null);
				paraLines.add(strParagraph.substring(0, nSize));
				strParagraph = strParagraph.substring(nSize);
			}
			lines.addAll(0, paraLines);
		}
		while (lines.size() > mLineCount) {
			try {
				m_mbBufBegin += lines.get(0).getBytes(m_strCharsetName).length;
				lines.remove(0);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		m_mbBufEnd = m_mbBufBegin;
		return;
	}

	public int prePage() throws IOException {
		if (m_mbBufBegin <= 0) {
			m_mbBufBegin = 0;
			m_isfirstPage=true;
			return 0;
		}else m_isfirstPage=false;
		m_lines.clear();
		pageUp();
		m_lines = pageDown();
		return m_mbBufBegin;
	}

	public int nextPage() throws IOException {
		if (m_mbBufEnd >= m_mbBufLen) {
			m_islastPage=true;
			return 0;
		}else m_islastPage=false;
		m_lines.clear();
		m_mbBufBegin = m_mbBufEnd;
		m_lines = pageDown();
		return m_mbBufBegin;
	}

	public void Draw(Canvas c) {
		mPaint.setTextSize(m_fontSize);
		mPaint.setColor(m_textColor);
		Log.v(TAG, "size"+m_lines.size());
		if (m_lines.size() == 0)
			m_lines = pageDown();
		if (m_lines.size() > 0) {
			if (m_book_bg == null)
				c.drawColor(m_backColor);
			else
				c.drawBitmap(m_book_bg, 0, 0, null);
			int y = marginTop;
			for (String strLine : m_lines) {
				y += m_fontSize + m_nLineSpaceing;
				c.drawText(strLine, marginWidth, y, mPaint);
			}
		}
	}

	public void setBgBitmap(Bitmap BG) {
		m_book_bg = BG;
	}
	
	public boolean isfirstPage() {
		return m_isfirstPage;
	}
	public boolean islastPage() {
		return m_islastPage;
	}
	
	public String getPercent()
	{
		float fPercent = (float) (m_mbBufEnd * 1.0 / m_mbBufLen);
		DecimalFormat df = new DecimalFormat("#0.0");
		String strPercent = df.format(fPercent * 100) + "%";
		return strPercent;
	}
	
	public void setFontSize(int m_fontSize) {
		this.m_fontSize = m_fontSize;
		mLineCount = (int) (mVisibleHeight / (m_fontSize+m_nLineSpaceing) )  - 1; // 可显示的行数
	}

	// 设置页面起始点
	public void setBufBegin(int m_mbBufBegin) {
		this.m_mbBufBegin = m_mbBufBegin;
	}

	// 设置页面结束点
	public void setBufEnd(int m_mbBufEnd) {
		this.m_mbBufEnd = m_mbBufEnd;
	}

	public int getBufBegin() {
		return m_mbBufBegin;
	}

	public String getFirstLineText() {
		 String tempString = m_lines.size() > 0 ? m_lines.get(0) : "";
		 tempString = tempString.trim() + m_lines.get(1);
		 return tempString;
	}

	public int getTextColor() {
		return m_textColor;
	}

	public void setTextColor(int m_textColor) {
		this.m_textColor = m_textColor;
	}

	public int getBufLen() {
		return m_mbBufLen;
	}

	public int getBufEnd() {
		return m_mbBufEnd;
	}
	
	public void clearLine()
	{
		m_lines.clear();
	}
	
	public  byte getPostionByte(int postion)
	{
		return m_mbBuf.get(postion);
	}
	
	public void setBgcolor (int color)
	{
		m_backColor = color;
	}
	
	/**
	 * 生成目录
	 * @return
	 */
	public List<BookCatalogue> buildCatalogue()
	{
		List<BookCatalogue> catalogue = new ArrayList<BookCatalogue>();
		Pattern pattern = Pattern.compile("第[\\d一二三四五六七八九十百千]+[章节集]\\s*.{0,40}\\r{0,1}\\n");
		String strParagraph = null;
		int Position = 0;
		 try {  
           while(Position < m_mbBufLen)  
            {  
        	   Log.v(TAG, "Position"+Position);
        	   byte[] paraBuf  = readParagraphForward(Position);
	   		   try {
					strParagraph = new String(paraBuf, m_strCharsetName);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	   
                Matcher matcher = pattern.matcher(strParagraph);  
                while(matcher.find())  
                {  
                    int start = matcher.start();  
                    int end   = matcher.end();  
                    BookCatalogue bookCatalogue = new  BookCatalogue();
                    bookCatalogue.catalogueName = strParagraph.substring(start, end).trim();  
                    int currentSize = strParagraph.substring(0, start).getBytes(m_strCharsetName).length;  
                    bookCatalogue.position = Position+currentSize;  
                    catalogue.add(bookCatalogue);
                }  
                Position = Position + paraBuf.length;
            }  
        }catch (UnsupportedEncodingException e) {  
            e.printStackTrace();  
        } 
		return catalogue;
	}
}
