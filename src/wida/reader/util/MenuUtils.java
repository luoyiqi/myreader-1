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
		list.add(new MenuInfo(MENU_BOOKMARK,"书签",R.drawable.menu_sign,false));
		list.add(new MenuInfo(MENU_MENU,"目录",R.drawable.menu_menu,false));
		return list;
	}
	
	/**
	 * 获取当前菜单列表
	 * @return
	 */
	public static List<MenuInfo> getMenuList(boolean markpositon){

		List<MenuInfo> list=new ArrayList<MenuInfo>();
		if (markpositon)
		{
			list.add(new MenuInfo(MENU_BOOKMARK_N,"书签",R.drawable.menu_menu,false));
			list.add(new MenuInfo(MENU_MENU,"目录",R.drawable.menu_menu,false));
		}else {
			list=initMenu();	
		}
		return list;
	}
	
	public static List<MenuInfo> getMenuList2(){
		List<MenuInfo> list= new ArrayList<MenuInfo>();
		list.add(0,new MenuInfo(MENU_SETTING,"设置",R.drawable.menu_setting,false));
		list.add(0,new MenuInfo(MENU_SIZE,"字体大小",R.drawable.menu_size,false));
		list.add(0,new MenuInfo(MENU_LINGHT,"亮度",R.drawable.menu_light,false));
		list.add(0,new MenuInfo(MENU_JUMP,"转跳",R.drawable.menu_go,false));
		return list;
	}
}
