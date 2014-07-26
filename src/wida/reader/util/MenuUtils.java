package wida.reader.util;

import java.util.ArrayList;
import java.util.List;

import wida.reader.R;


public class MenuUtils {
	public static final int MENU_BOOKMARK=1;
	public static final int MENU_MENU=2;
	public static final int MENU_SETTING=1;
	public static final int MENU_SIZE=2;
	public static final int MENU_LINGHT=3;
	public static final int MENU_JUMP=4;
	public static final int MENU_BOOKMARK_N=5;

	
	private static List<MenuInfo> initMenu(){
		List<MenuInfo> list=new ArrayList<MenuInfo>();
		list.add(new MenuInfo(MENU_BOOKMARK,"��ǩ",R.drawable.menu_sign,false));
		list.add(new MenuInfo(MENU_MENU,"Ŀ¼",R.drawable.menu_menu,false));
		return list;
	}
	
	/**
	 * ��ȡ��ǰ�˵��б�
	 * @return
	 */
	public static List<MenuInfo> getMenuList(boolean markpositon){

		List<MenuInfo> list=new ArrayList<MenuInfo>();
		if (markpositon)
		{
			list.add(new MenuInfo(MENU_BOOKMARK_N,"��ǩ",R.drawable.menu_menu,false));
			list.add(new MenuInfo(MENU_MENU,"Ŀ¼",R.drawable.menu_menu,false));
		}else {
			list=initMenu();	
		}
		return list;
	}
	
	public static List<MenuInfo> getMenuList2(){
		List<MenuInfo> list= new ArrayList<MenuInfo>();
		list.add(0,new MenuInfo(MENU_SETTING,"����",R.drawable.menu_setting,false));
		list.add(0,new MenuInfo(MENU_SIZE,"�����С",R.drawable.menu_size,false));
		list.add(0,new MenuInfo(MENU_LINGHT,"����",R.drawable.menu_light,false));
		list.add(0,new MenuInfo(MENU_JUMP,"ת��",R.drawable.menu_go,false));
		return list;
	}
}
