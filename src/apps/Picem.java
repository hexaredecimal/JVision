/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package apps;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jexer.TApplication;
import jexer.TExceptionDialog;
import jexer.TFileOpenBox;
import jexer.TImageWindow;
import jexer.menu.TMenu;
import jvision.Helpers;

/**
 *
 * @author hexaredecimal
 */
public class Picem extends TImageWindow {

	private TApplication parent;
	private ArrayList<TMenu> menus;

	public Picem(TApplication parent, File file) throws IOException {
		super(parent, file);
		this.parent = parent;
		this.menus = new ArrayList<>();
		addMenus();
		setWidth(100);
		setHeight(50);
	}

	private void addMenus() {
		menus.add(getImages());
		menus.add(parent.addHelpMenu());
	}

	private TMenu getImages() {
		TMenu menu = parent.addMenu("I&mages");
		menu.addItem(5000, "N&ext");
		menu.addItem(5000 + 1, "P&rev");
		return menu;
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

	public static String readFile(TApplication parent) {
		List<String> filters = new ArrayList<String>();
		filters.add("^.*\\.[Jj][Pp][Gg]$");
		filters.add("^.*\\.[Jj][Pp][Ee][Gg]$");
		filters.add("^.*\\.[Pp][Nn][Gg]$");
		filters.add("^.*\\.[Gg][Ii][Ff]$");
		filters.add("^.*\\.[Bb][Mm][Pp]$");
		try {
			String filename = parent.fileOpenBox(".", TFileOpenBox.Type.OPEN, filters);
			return filename;
		} catch (IOException ex) {
			new TExceptionDialog(parent, ex);
		}

		return "";
	}
}
