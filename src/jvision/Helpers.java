/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jvision;

import java.util.ArrayList;
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
}
