/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jvision;

import java.util.ArrayList;
import java.util.Calendar;
import jexer.TApplication;
import jexer.menu.TMenu;

/**
 *
 * @author hexaredecimal
 */
public class Helpers {
	public static void removeMenus(TApplication app, ArrayList<TMenu> menus){
		if (app == null || menus == null)
			return; 
		for (TMenu menu: menus) 
			app.removeMenu(menu);
		app.getDesktop().restore();
	}

	public static void addMenus(TApplication app, ArrayList<TMenu> menus){
		if (app == null || menus == null)
			return; 
		for (TMenu menu: menus) 
			app.addMenu(menu);
		app.getDesktop().restore();
	}

	public static String startDir() { return System.getProperty("user.dir"); }
	public static String extractPath(String filename) {
		String[] splits = filename.split("/");
		String path = ""; 
		for (int i = 0; i < splits.length; ++i) {
			if (i != splits.length -1) {
				path += splits[i] + "/"; 
			}
		}
		return path;
	}

	public static String getTime() {
		Calendar c = Calendar.getInstance(); 
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		int seconds = c.get(Calendar.SECOND);

		String hour_str = "" + hour; 
		String minute_str = "" + minute; 
		String seconds_str = "" + seconds; 

		if (hour < 10)
			hour_str = "0" + hour; 
		if (minute < 10)
			minute_str = "0" + minute; 
		if (seconds < 10)
			seconds_str = "0" + seconds; 
		
		String time = hour_str + ":" + minute_str + ":" + seconds_str;
		return time;
	}

}
