package wida.reader.filter;

import java.io.File;
import java.io.FileFilter;

public class BookFilter implements FileFilter
{
	public boolean accept(File file) {
		if ( file.isHidden() || !file.canRead() || file.canWrite()) {
			return false;
		}
		String tmp = file.getName().toLowerCase();
		if (!tmp.endsWith(".txt") && !file.isDirectory())
		{
			return false;
		}
		return true;
	}
}