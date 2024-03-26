/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package apps;

import java.util.ArrayList;
import jexer.TAction;
import jexer.TApplication;
import jexer.TExceptionDialog;
import jexer.TTerminalWidget;
import jexer.TWindow;
import jexer.event.TMenuEvent;
import jexer.event.TResizeEvent;
import jexer.menu.TMenu;
import jvision.Helpers;
import jvision.SystemEvent;
import libvision.TVTerminalWindow;

/**
 *
 * @author hexaredecimal
 */
public class Konsole extends TVTerminalWindow {
	private final ArrayList<TMenu> menus;
	private final TApplication parent;

	public Konsole(TApplication application, String title, int width, int height) {
		super(application, application.getDesktop().getWidth() / 2, 20, "zsh");
		this.parent = application;
		this.menus = new ArrayList<>();
		addMenus();
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
	public void onMenu(TMenuEvent event) {
		TWindow active = parent.getActiveWindow();
		int event_id = event.getId();

		if (event_id == 0x0A111) {
			new TExceptionDialog(parent, new Exception("Opacity event: We got it o lock!!"));
		} else if (event_id == 0x25500A) {
			Helpers.showAboutMessage(parent, "Konsole Terminal");
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
