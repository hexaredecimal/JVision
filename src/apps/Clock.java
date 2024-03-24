/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package apps;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import jexer.TApplication;
import jexer.TLabel;
import jexer.TPanel;
import jexer.TWindow;
import jexer.bits.ColorTheme;
import jexer.menu.TMenu;
import jvision.Helpers;

/**
 *
 * @author hexaredecimal
 */
public class Clock extends TWindow {
	private TLabel clock_label; 
	private TPanel main_panel;
	private ArrayList<TMenu> menus;
	private TApplication parent; 
	public Clock(TApplication application, String title, int width, int height) {
		super(application, title, width, height);
		setResizable(false);
		main_panel = new TPanel(this ,0, 0, width, height);
		clock_label = main_panel.addLabel(Helpers.getTime(), width / 4,  height/4); 
		menus = new ArrayList<>();
		this.parent = application;
		addMenus();
	}

	public void onIdle() {
		clock_label.remove();
		clock_label = main_panel.addLabel(Helpers.getTime(), main_panel.getWidth() / 4, main_panel.getHeight() / 4); 
	}


	private void addMenus() {
		menus.add(getAppMenu());
		menus.add(getDisplayMenu());
	}


	private TMenu getDisplayMenu() {
		TMenu display = parent.addMenu("D&ispay");
		display.addItem(0x0A111, "&Full D&ate");
		display.addItem(0x0A0B0, "S&how AM");
		return display; 
	}
	
	private TMenu getAppMenu() {
		TMenu app = parent.addMenu("Cloc&k");
		app.addItem(0x25500A, "A&bout");
		app.addSeparator();
		app.addItem(0x33111, "Exit");
		return app;
	}
	
	@Override
	public void onFocus() {
		System.out.println("apps.Picem.onFocus()");
		if (parent != null) {
			Helpers.addMenus(parent, menus);
		}
	}

	@Override
	public void onUnfocus() {
		System.out.println("apps.Picem.onUnfocus()");
		if (parent != null) {
			Helpers.removeMenus(parent, menus);
		}
	}
}
