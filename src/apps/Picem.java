package apps;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import jexer.TApplication;
import jexer.TExceptionDialog;
import jexer.TFileOpenBox;
import jexer.TImageWindow;
import jexer.menu.TMenu;
import jvision.Helpers;
import jvision.SystemEvent;
/**
 *
 * @author hexaredecimal
 */
public class Picem extends TImageWindow {

	private TApplication parent;
	private ArrayList<TMenu> menus;

	private String workingDir;
	public Picem(TApplication parent, File file) throws IOException {
		super(parent, file);
		this.parent = parent;
		this.menus = new ArrayList<>();
		addMenus();
		setWidth(100);
		setHeight(50);
		workingDir = Helpers.extractPath(file.getAbsolutePath());
	}

	private void addMenus() {
		menus.add(getAppMenu());
		menus.add(getImagesMenu());
	}

	private TMenu getImagesMenu() {
		TMenu menu = parent.addMenu("I&mages");
		menu.addItem(SystemEvent.PicemChooseFile.getId(), "Op&en"); 
		menu.addSeparator();
		menu.addItem(5000, "N&ext");
		menu.addItem(5000 + 1, "P&rev");
		menu.addSeparator();
		menu.addItem(5000 + 2, "E&xit");
		return menu;
	}
	
	private TMenu getAppMenu() {
		TMenu app = parent.addMenu("Pi&cem");
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
