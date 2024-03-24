/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package apps;

import java.util.ArrayList;
import jexer.TAction;
import jexer.TApplication;
import jexer.TTerminalWidget;
import jexer.TWindow;
import jexer.event.TResizeEvent;
import jexer.menu.TMenu;
import jvision.Helpers;

/**
 *
 * @author hexaredecimal
 */
public class Konsole extends TWindow {
	private TTerminalWidget terminal;
	private ArrayList<TMenu> menus;
	private TApplication parent;
	public Konsole(TApplication application, String title, int width, int height) {
		super(application, title, width, height);
		this.parent = application;
		terminal = new TTerminalWidget(this, 0, 0, width, height,
			new TAction() {
			public void DO() {
				// This action will be executed when the shell process exits.
			}
		});
		terminal.setWidth(width);
		terminal.setHeight(height);
		this.menus = new ArrayList<>();
		addMenus();
	}

	@Override
	public void onResize(TResizeEvent event) {
		int width = event.getWidth();
		int height = event.getHeight(); 
		this.setWidth(width);
		this.setHeight(height);
		terminal.setWidth(width);
		terminal.setHeight(height);
	}

	private void addMenus() {
		menus.add(getConfigMenu());
		menus.add(getHelpMenu());
	}

	private TMenu getHelpMenu() {
		TMenu help = parent.addMenu("H&elp");
		help.addItem(0x25500A, "A&bout");
		return help;
	}

	private TMenu getConfigMenu() {
		TMenu display = parent.addMenu("C&onfigure");
		display.addItem(0x0A111, "Op&acity");
		return display; 
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