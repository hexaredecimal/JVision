/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package apps;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import jexer.TApplication;
import jexer.TEditorWindow;
import jexer.TWindow;
import jexer.menu.TMenu;
import jvision.Helpers;

/**
 *
 * @author hexaredecimal
 */
public class TurboEd extends TEditorWindow {

	private static final int WIDTH = 100; 
	private static final int HEIGHT = 50; 

	private TApplication parent;
	private ArrayList<TMenu> menus; 
	public TurboEd(TApplication parent ){
		super(parent, "TurboEd - New document");
		this.parent = parent; 
		this.menus = new ArrayList<>();
		addMenus();
		setWidth(WIDTH);
		setHeight(HEIGHT);
	}

	private void addMenus() {
		menus.add(parent.addFileMenu());
		menus.add(parent.addEditMenu());
	}

	@Override
	public void onFocus() {
		System.out.println("apps.TurboEd.onFocus()");
		if (parent != null)
			Helpers.addMenus(parent, menus);
	}

	@Override
	public void onUnfocus() {
		System.out.println("apps.TurboEd.onUnfocus()");
		if (parent != null)
			Helpers.removeMenus(parent, menus);
	}
}
