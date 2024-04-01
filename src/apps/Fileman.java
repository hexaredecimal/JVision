package apps;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

import jexer.TAction;
import jexer.TApplication;
import jexer.TDirectoryList;
import jexer.TEditorWidget;
import jexer.TExceptionDialog;
import jexer.TImage;
import jexer.backend.SwingTerminal;
import jexer.event.TKeypressEvent;
import jexer.event.TResizeEvent;
import jexer.ttree.TDirectoryTreeItem;
import jexer.ttree.TTreeItem;
import jexer.ttree.TTreeViewWidget;
import static jexer.TKeypress.*;
import jexer.TWindow;
import jexer.event.TMenuEvent;
import jexer.menu.TMenu;
import jvision.Helpers;
import jvision.SystemEvent;

public class Fileman extends TWindow {
	private TTreeViewWidget treeView;
	private TDirectoryTreeItem treeViewRoot;

	private TDirectoryList directoryList;
	private TEditorWidget editor;
	private final ArrayList<TMenu> menus;

	private TImage imageWidget;
	private TApplication parent;

	public Fileman(final TApplication application, final String path,
		final List<String> filters) {
		
		super(application, "File Manager", 50, 40);
		setActive(true);
		parent = application;
		menus = new ArrayList<>();
		addMenus();

		treeView = addTreeViewWidget(0, 0, getWidth() / 2, getHeight(),
			new TAction() {
			public void DO() {
				TTreeItem item = treeView.getSelected();
				File selectedDir = ((TDirectoryTreeItem) item).getFile();
				try {
					directoryList.setPath(selectedDir.getCanonicalPath());
					if (directoryList.getList().size() > 0) {
						setThumbnail(directoryList.getPath());
					} else {
						if (imageWidget != null) {
							getChildren().remove(imageWidget);
						}

						if (editor != null) {
							getChildren().remove(editor);
						}
						editor = null;
						imageWidget = null;
					}
					activate(treeView);
				} catch (IOException e) {
					// If the backend is Swing, we can emit the stack
					// trace to stderr.  Otherwise, just squash it.
					if (getScreen() instanceof SwingTerminal) {
						e.printStackTrace();
					}
				}
			}
		}
		);
		try {
			treeViewRoot = new TDirectoryTreeItem(treeView, path, true);
		} catch (Exception ex) {
			new TExceptionDialog(application, ex);
		}

		directoryList = addDirectoryList(path, getWidth() / 2 + 1, 0,
			getWidth() / 2 - 1, getHeight() / 2,
			new TAction() {
			public void DO() {
				setThumbnail(directoryList.getPath());
			}
		},
			new TAction() {

			public void DO() {
				setThumbnail(directoryList.getPath());
			}
		},
			filters);

		if (directoryList.getList().size() > 0) {
			activate(directoryList);
			setThumbnail(directoryList.getPath());
		} else {
			activate(treeView);
		}
	}


	private void addMenus() {
		menus.add(getAppMenu());
	}


	private TMenu getAppMenu() {
		TMenu app = parent.addMenu("F&ile manager");
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
			Helpers.showAboutMessage(parent, "File man");
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

	
	@Override
	public void onResize(final TResizeEvent event) {

		treeView.setY(1);
		treeView.setWidth(getWidth() / 2);
		treeView.setHeight(getHeight() - 1);
		treeView.onResize(new TResizeEvent(event.getBackend(),
			TResizeEvent.Type.WIDGET,
			treeView.getWidth(),
			treeView.getHeight()));
		treeView.getTreeView().onResize(new TResizeEvent(event.getBackend(),
			TResizeEvent.Type.WIDGET,
			treeView.getWidth() - 1,
			treeView.getHeight() - 1));
		directoryList.setX(getWidth() / 2 + 1);
		directoryList.setY(1);
		directoryList.setWidth(getWidth() / 2 - 1);
		directoryList.setHeight(getHeight() / 2 - 1);
		directoryList.onResize(new TResizeEvent(event.getBackend(),
			TResizeEvent.Type.WIDGET,
			directoryList.getWidth(),
			directoryList.getHeight()));

		if (imageWidget != null) {
			getChildren().remove(imageWidget);
		}

		if (editor != null) {
			getChildren().remove(editor);
		}

		imageWidget = null;
		editor = null;
		if (directoryList.getList().size() > 0) {
			activate(directoryList);
			setThumbnail(directoryList.getPath());
		} else {
			activate(treeView);
		}
	}

	@Override
	public void onKeypress(final TKeypressEvent keypress) {

		if (treeView.isActive() || directoryList.isActive()) {
			if ((keypress.equals(kbEnter))
				|| (keypress.equals(kbUp))
				|| (keypress.equals(kbDown))
				|| (keypress.equals(kbPgUp))
				|| (keypress.equals(kbPgDn))
				|| (keypress.equals(kbHome))
				|| (keypress.equals(kbEnd))) {
				super.onKeypress(keypress);

				TTreeItem item = treeView.getSelected();
				File selectedDir = ((TDirectoryTreeItem) item).getFile();
				try {
					if (treeView.isActive()) {
						directoryList.setPath(selectedDir.getCanonicalPath());
					}
					if (directoryList.getList().size() > 0) {
						activate(directoryList);
						setThumbnail(directoryList.getPath());
					} else {
						if (imageWidget != null) {
							getChildren().remove(imageWidget);
						}

						if (editor != null) {
							getChildren().remove(editor);
						}
						editor = null;
						imageWidget = null;
						activate(treeView);
					}
				} catch (IOException e) {
					if (getScreen() instanceof SwingTerminal) {
						e.printStackTrace();
					}
				}
				return;
			}
		}

		super.onKeypress(keypress);
	}

	private void setThumbnail(final File file) {
		if (file == null) {
			return;
		}
		if (!file.exists() || !file.isFile()) {
			return;
		}
		String extension = "/dir";
		int i = file.getPath().lastIndexOf('.');
		if (i > 0) {
			extension = file.getPath().substring(i + 1);
		}

		ArrayList<String> exts = new ArrayList<>();
		exts.add("jpg");
		exts.add("png");
		exts.add("gif");
		exts.add("jpeg");
		if (exts.contains(extension)) {
			BufferedImage image = null;
			try {
				image = ImageIO.read(file);
			} catch (IOException e) {
				if (getScreen() instanceof SwingTerminal) {
					e.printStackTrace();
				}
				return;
			}

			if (imageWidget != null) {
				getChildren().remove(imageWidget);
			}
			int width = getWidth() / 2 - 1;
			int height = getHeight() / 2 - 1;

			imageWidget = new TImage(this, getWidth() - width,
				getHeight() - height, width, height, image, 0, 0, null);

			imageWidget.setScaleType(TImage.Scale.SCALE);

			imageWidget.setActive(false);
			activate(directoryList);
		} else {
			Scanner sc = null;
			try {
				sc = new Scanner(file);
			} catch (FileNotFoundException ex) {
				return;
			}

			String contents = ""; 
			while (sc.hasNextLine())
				contents += sc.nextLine() + "\n"; 
			sc.close();

			if (editor != null) {
				getChildren().remove(editor); 
			}

			int width = getWidth() / 2 - 1;
			int height = getHeight() / 2 - 1;

			editor = addEditor(contents,getWidth() - width, getHeight() - height, width, height); 
			editor.setActive(false);
		}
	}

}
