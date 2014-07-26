/**
 *  Author :  wida
 *  Description :
 */
package wida.reader.util;

import java.util.Comparator;

import wida.reader.adapter.FileChooserAdapter.FileInfo;

public class  FileComarator  implements Comparator<FileInfo> {
	public int compare(FileInfo f1, FileInfo f2) {
		if (f1 == null || f2 == null) {// �ȱȽ�null
			if (f1 == null) {
				{
					return -1;
				}
			} else {
				return 1;
			}
		} else {
			if (f1.isDirectory() == true && f2.isDirectory() == true) { // �ٱȽ��ļ���
				return f1.getFileName().compareToIgnoreCase(f2.getFileName());
			} else {
				if ((f1.isDirectory() && !f2.isDirectory()) == true) {
					return -1;
				} else if ((f2.isDirectory() && !f1.isDirectory()) == true) {
					return 1;
				} else {
					return f1.getFileName().compareToIgnoreCase(
							f2.getFileName());// ���Ƚ��ļ�
				}
			}
		}
	}
}