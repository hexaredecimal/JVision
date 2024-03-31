/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package apps;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import jexer.TAction;
import jexer.TApplication;
import jexer.TDesktop;
import jexer.TEditorWidget;
import jexer.TExceptionDialog;
import jexer.TFileOpenBox;
import jexer.TWidget;
import jexer.TWindow;
import jexer.event.TMenuEvent;
import jexer.event.TResizeEvent;
import jexer.menu.TMenu;
import jexer.menu.TSubMenu;
import jexer.ttree.TDirectoryTreeItem;
import jexer.ttree.TTreeItem;
import jexer.ttree.TTreeView;
import jexer.ttree.TTreeViewWidget;
import jvision.Helpers;
import jvision.SystemEvent;
import libvision.TVEditorWindow;
import libvision.TVTerminalWindow;

/**
 *
 * @author hexaredecimal
 */
public class TurboEd extends TVEditorWindow {

	private static final int WIDTH = 100;
	private static final int HEIGHT = 50;

	private final TApplication parent;
	private final ArrayList<TMenu> menus;
	private File opened_file;

	private TWindow files;
	private TVTerminalWindow runner;

	public TurboEd(TApplication parent) {
		super(parent, "TurboEd - New document");
		this.parent = parent;
		this.menus = new ArrayList<>();
		addMenus();
		setWidth(WIDTH);
		setHeight(HEIGHT);
		opened_file = null;

		TDesktop desktop = parent.getDesktop();
		int sceen_w = desktop.getWidth();
		int sceen_h = desktop.getHeight();

		int turbo_x = (int) ((double) sceen_w * 35 / 100);
		int turbo_y = (int) ((double) sceen_h * 10 / 100);
		setX(turbo_x);
		setY(turbo_y);

		TEditorWidget editor = getEditor();

		// createFileTree(".");
		// createRunner("echo 'Hello, world!!! in the terminal'");
	}

	private void createRunner(String command) {
		TDesktop desktop = parent.getDesktop();
		int sceen_w = desktop.getWidth();
		int sceen_h = desktop.getHeight();

		int runner_w = (int) ((double) sceen_w * 80 / 100);
		int files_x = (int) ((double) sceen_w * 5 / 100);
		if (runner != null) {
			runner.close();
		}
		runner = new TVTerminalWindow(parent, files_x, 0, command);
		runner.setWidth(runner_w);
		runner.setHeight(10);
		int runner_y = (int) ((double) sceen_h * 75 / 100);
		runner.setX(files_x);
		runner.setY(runner_y);

		TResizeEvent editSize = new TResizeEvent(parent.getBackend(),
			TResizeEvent.Type.WIDGET, runner_w, 10);
		runner.onResize(editSize);
	}

	private void createFileTree(String path) {
		TDesktop desktop = parent.getDesktop();
		int sceen_w = desktop.getWidth();
		int sceen_h = desktop.getHeight();

		int turbo_y = (int) ((double) sceen_h * 10 / 100);
		if (files != null) {
			files.close();
		}
		files = parent.addWindow("File Tree", 0, 0, 50, HEIGHT, 0);
		int files_x = (int) ((double) sceen_w * 5 / 100);
		files.setX(files_x);
		files.setY(turbo_y);

		TTreeViewWidget treeView = null;
		treeView = files.addTreeViewWidget(
			0,
			0,
			(int) ((double) files.getWidth() / 2) - 2,
			files.getHeight() - 2,
			new TAction() {
			@Override
			public void DO() {
				TTreeView view = (TTreeView) this.source;
				TTreeItem item = view.getSelected();
				File selectedDir = ((TDirectoryTreeItem) item).getFile();
				files.addDirectoryList(
					selectedDir.getPath(),
					(int) ((double) files.getWidth() / 2),
					0,
					(int) ((double) files.getWidth() / 2),
					files.getHeight() - 2
				);
			}
		});
		try {

			files.addDirectoryList(
				path,
				(int) ((double) files.getWidth() / 2),
				0,
				(int) ((double) files.getWidth() / 2),
				files.getHeight() - 2
			);
			new TDirectoryTreeItem(treeView, path, true);
		} catch (Exception ex) {
			new TExceptionDialog(parent, ex);
		}
	}

	private void addMenus() {
		Helpers.addNonExist(menus, getAppMenu());
		Helpers.addNonExist(menus, getFile());
		Helpers.addNonExist(menus, parent.addEditMenu());
		Helpers.addNonExist(menus, getViewMenu());
		Helpers.addNonExist(menus, getDebugMenu());
	}

	private TMenu getAppMenu() {
		TMenu app = parent.addMenu("&Turbo");
		app.addItem(0x25500A, "A&bout");
		app.addSeparator();
		app.addItem(SystemEvent.CloseApp.getId(), "E&xit");
		return app;
	}

	private TMenu getFile() {
		TMenu fileMenu = parent.addMenu("F&ile");
		fileMenu.addItem(0x10111, "New f&ile");
		fileMenu.addItem(0x11011, "N&ew project");
		fileMenu.addSeparator();
		fileMenu.addItem(0x11101, "Op&en file");
		fileMenu.addItem(0x11110, "Op&en project");
		fileMenu.addSubMenu("R&ecent");
		fileMenu.addSeparator();
		fileMenu.addItem(0x12111, "S&ave");
		fileMenu.addItem(0x11211, "Save a&ll");
		return fileMenu;
	}

	private TMenu getViewMenu() {
		TMenu view_menu = parent.addMenu("V&iew");
		view_menu.addItem(0xcadee, "Fi&le tree");
		view_menu.addItem(0xcadac, "Ter&minal");
		return view_menu;
	}

	private TMenu getDebugMenu() {
		TMenu debug = parent.addMenu("De&bug");
		TSubMenu language = debug.addSubMenu("Language");
		String[] langs = {
			"Ja&va",
			"JavaSc&ript",
			"&C",
			"C&++",
			"&Rust",
			"Pyt&hon3"
		};

		for (int i = 0; i < langs.length; i++) {
			language.addItem(0x0212 + i, langs[i].toUpperCase());
		}

		return debug;
	}

	@Override
	public void onMenu(TMenuEvent event) {
		TWindow active = parent.getActiveWindow();
		int event_id = event.getId();
		super.onMenu(event);

		if (event_id == 0xcadee) {
			createFileTree(".");
		} else if (event_id == 0xcadac) {
			createRunner("zsh");
		} else if (event_id == 0x11101) {
			try {
				String filename = fileOpenBox(".", TFileOpenBox.Type.OPEN);
				if (filename == null) {
					return;
				}

				File fp = new File(filename);
				Scanner sc = new Scanner(fp);
				String contents = "";
				while (sc.hasNextLine()) {
					contents += sc.nextLine() + "\n";
				}

				sc.close();
				setTitle(fp.getName());
				getEditor().setText(contents);
			} catch (IOException ex) {
				Logger.getLogger(TurboEd.class.getName()).log(Level.SEVERE, null, ex);
			}
		} else if (event_id == 0x25500A) {
			Helpers.showAboutMessage(parent, "TurboEd");
		} else if (event_id == 0x012111) {
			TEditorWidget editor = getEditor();
			if (!editor.isDirty()) {
				return;
			}

			if (opened_file != null) {
				save(opened_file.getPath());
				return;
			}

			try {
				String filename = fileOpenBox(".", TFileOpenBox.Type.SAVE);
				if (filename == null) {
					return;
				}
				save(filename);
			} catch (IOException ex) {
				Logger.getLogger(TurboEd.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	private void save(String filename) {
		TEditorWidget editor = getEditor();
		File fp = new File(filename);
		if (!fp.exists()) {
			try {
				fp.createNewFile();
			} catch (IOException ex) {
				Logger.getLogger(TurboEd.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		opened_file = fp;
		FileWriter fw;
		try {
			fw = new FileWriter(fp);
			fw.write(editor.getText());
			fw.close();
		} catch (IOException ex) {
			Logger.getLogger(TurboEd.class.getName()).log(Level.SEVERE, null, ex);
		}
		editor.setText(editor.getText());
		setTitle(opened_file.getName());
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
		if (runner != null) {
			runner.close();
		}

		if (files != null) {
			files.close();
		}
		this.onUnfocus();
		super.onClose();
	}
}
