package jvision;

import apps.Picem;
import apps.TurboEd;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import jexer.TApplication;
import jexer.TDesktop;
import jexer.TExceptionDialog;
import jexer.TImageWindow;
import jexer.TTerminalWidget;
import jexer.TSplitPane;
import jexer.TWidget;
import jexer.event.TMenuEvent;
import jexer.menu.TMenu;
import jexer.menu.TSubMenu;

/**
 * Implements a simple tiling window manager. A terminal widget is added to the
 * desktop, which can be split horizontally or vertically. A close action is
 * provided to each window to remove the split when its shell exits.
 *
 * This example shows what can be done with minimal changes to stock Jexer
 * widgets.
 */
public class JVision extends TApplication {

	private static final ResourceBundle i18n = ResourceBundle.getBundle(TApplication.class.getName());
	/**
	 * Handle to the root widget.
	 */
	final static int WIDTH = 200;
	final static int HEIGHT = 80;
	final static int FONT_SIZE = 12;

	/**
	 * Main entry point.
	 */
	public static void main(String[] args) throws Exception {
		// For this application, we must use ptypipe so that the terminal
		// shells can be aware of their size.
		// System.setProperty("jexer.TTerminal.ptypipe", "true");

		// Let's also suppress the status line.
		System.setProperty("jexer.hideStatusBar", "true");

		final JVision jtwm = new JVision();
		(new Thread(jtwm)).start();

		jtwm.invokeLater(new Runnable() {
			public void run() {
			}
		});
	}

	private ArrayList<TMenu> root = new ArrayList<>();

	/**
	 * Public constructor chooses the ECMA-48 / Xterm backend.
	 *
	 * @throws java.lang.Exception
	 */
	public JVision() throws Exception {

		super(BackendType.SWING, WIDTH, HEIGHT, FONT_SIZE);
		// The stock tool menu has items for redrawing the screen, opening
		// images, and (when using the Swing backend) setting the font.
		// addToolMenu();
		URL wall_paper = this.getClass().getResource("/wallpapers/default.jpg");
		TImageWindow win = new TImageWindow(
			this, 
			new File(wall_paper.getPath()), 
			-1, 
			-1, 
			WIDTH + 2, 
			HEIGHT + 1
		);
		win.setActive(false);
		win.setEnabled(false);
		win.setTitle("");

		// We will have one menu containing a mix of new and stock commands
		TMenu tileMenu = addMenu(i18n.getString("toolMenuTitle"));
		TMenu windowMenu = addWindowMenu();

		// Stock commands: a new shell with resizable window, and exit
		// program.
		tileMenu.addItem(TMenu.MID_SHELL, "T&erminal");
		tileMenu.addItem(SystemEvent.OpenPicem.getId(), "Pic&em");
		tileMenu.addItem(SystemEvent.OpenTurboEd.getId(), "Turbo&Ed");

		tileMenu.addSeparator();
		TSubMenu sub = tileMenu.addSubMenu("Se&ttings");
		TSubMenu screen_set = sub.addSubMenu("De&sktop");
		screen_set.addDefaultItem(TMenu.MID_REPAINT);
		sub.addDefaultItem(TMenu.MID_SCREEN_OPTIONS);

		tileMenu.addSeparator();
		tileMenu.addItem(SystemEvent.OpenAboutDialog.getId(), "&About");

		root.add(tileMenu);
		root.add(windowMenu);
		// TTerminalWidget can request the text-block mouse pointer be
		// suppressed, but the default TDesktop will ignore it.  Let's set a
		// new TDesktop to pass that mouse pointer visibility option to
		// TApplication.
		setDesktop(new TDesktop(this) {
			@Override
			public boolean hasHiddenMouse() {
				TWidget active = getActiveChild();
				if (active instanceof TTerminalWidget) {
					return ((TTerminalWidget) active).hasHiddenMouse();
				}
				return false;
			}
		});

	}

	/**
	 * Process menu events.
	 */
	@Override
	protected boolean onMenu(TMenuEvent event) {
		TWidget active = getDesktop().getActiveChild();
		TSplitPane split = null;

		int event_id = event.getId();
		if (event_id == SystemEvent.OpenAboutDialog.getId()) {
			this.messageBox("About", "TurboVision OS v0.1");
		} else if (event_id == SystemEvent.OpenTurboEd.getId()) {
			TurboEd ed = new TurboEd(this);
			this.activateWindow(ed);
		} else if (event_id == SystemEvent.OpenPicem.getId()) {
			String filename = Picem.readFile(this);
			try {
				Picem picem = new Picem(this, new File(filename));
			} catch (IOException ex) {
				TExceptionDialog e = new TExceptionDialog(this, ex);
			}
		} else {
			super.onMenu(event);
		}

		return true;
	}
}
