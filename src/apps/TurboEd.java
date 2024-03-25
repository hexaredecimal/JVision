/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package apps;

import java.util.ArrayList;
import jexer.TApplication;
import jexer.TEditorWidget;
import jexer.menu.TMenu;
import jvision.Helpers;
import jvision.SystemEvent;
import libvision.TVEditorWindow;

/**
 *
 * @author hexaredecimal
 */
public class TurboEd extends TVEditorWindow {

	private static final int WIDTH = 100; 
	private static final int HEIGHT = 50; 

	private final TApplication parent;
	private final ArrayList<TMenu> menus; 

	public TurboEd(TApplication parent ){
		super(parent, "TurboEd - New document");
		this.parent = parent; 
		this.menus = new ArrayList<>();
		addMenus();
		setWidth(WIDTH);
		setHeight(HEIGHT);
		TEditorWidget editor = getEditor(); 
		editor.setText("Hello, world");
	}

	private void addMenus() {
		Helpers.addNonExist(menus, getAppMenu());
		Helpers.addNonExist(menus, parent.addFileMenu());
		Helpers.addNonExist(menus, parent.addEditMenu());
	}

	private TMenu getAppMenu() {
		TMenu app = parent.addMenu("&Turbo");
		app.addItem(0x25500A, "A&bout");
		app.addSeparator();
		app.addItem(SystemEvent.CloseApp.getId(), "E&xit");
		return app;
	}

	@Override
	public void onFocus() {
		if (parent != null)
			Helpers.addMenus(parent, menus);
	}

	@Override
	public void onUnfocus() {
		if (parent != null)
			Helpers.removeMenus(parent, menus);
	}

	@Override 
	public void onClose() {
		this.onUnfocus();
		super.onClose();
	}
}
