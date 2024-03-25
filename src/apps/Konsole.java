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
import jvision.SystemEvent;

/**
 *
 * @author hexaredecimal
 */
public class Konsole extends TWindow {
	private final TTerminalWidget terminal;
	private final ArrayList<TMenu> menus;
	private final TApplication parent;

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
		menus.add(getAppMenu());
		menus.add(getConfigMenu());
	}


	private TMenu getConfigMenu() {
		TMenu display = parent.addMenu("C&onfigure");
		display.addItem(0x0A111, "Op&acity");
		return display; 
	}

	private TMenu getAppMenu() {
		TMenu app = parent.addMenu("K&onsole");
		app.addItem(0x25500A, "A&bout");
		app.addSeparator();
		app.addItem(SystemEvent.CloseApp.getId(), "E&xit");
		return app;
	}
	
	@Override
	public void onFocus() {
		if (parent != null) {
			Helpers.addMenus(parent, menus);
		}
	}

	@Override
	public void onUnfocus() {
		if (parent != null) {
			Helpers.removeMenus(parent, menus);
		}
	}
	
	@Override 
	public void onClose() {
		this.onUnfocus();
		super.onClose();
	}
}
