package jvision;


import apps.Clock;
import apps.DeskTopWindow;
import apps.Konsole;
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
import jexer.TTerminalWidget;
import jexer.TSplitPane;
import jexer.TWidget;
import jexer.event.TMenuEvent;
import jexer.menu.TMenu;
import jexer.menu.TSubMenu;

public class JVision extends TApplication {
	private static final ResourceBundle i18n = ResourceBundle.getBundle(TApplication.class.getName());
	final static int WIDTH = 200;
	final static int HEIGHT = 80;
	final static int FONT_SIZE = 12;

	public static void main(String[] args) throws Exception {
		System.setProperty("jexer.TTerminal.ptypipe", "true");
		System.setProperty("jexer.hideStatusBar", "true");

		final JVision jtwm = new JVision();
		(new Thread(jtwm)).start();

		jtwm.invokeLater(() -> {
			System.out.println("jvision.JVision.main()");
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

		//getTheme().setFemme();
		System.setProperty("jexer.TWindow.borderStyleForeground", "round");
		System.setProperty("jexer.TWindow.borderStyleModal", "round");
		System.setProperty("jexer.TWindow.borderStyleMoving", "round");
		System.setProperty("jexer.TWindow.borderStyleInactive", "round");

		// The stock tool menu has items for redrawing the screen, opening
		// images, and (when using the Swing backend) setting the font.
		// addToolMenu();
		URL wall_paper = this.getClass().getResource("/wallpapers/default.jpg");
		DeskTopWindow win = new DeskTopWindow(this, new File(wall_paper.getPath()), WIDTH, HEIGHT);
		
		win.setActive(false);
		win.setEnabled(false);
		win.setTitle("");

		// We will have one menu containing a mix of new and stock commands
		TMenu tileMenu = addMenu(i18n.getString("toolMenuTitle"));
		TMenu windowMenu = addWindowMenu();

		// Stock commands: a new shell with resizable window, and exit
		// program.
		tileMenu.addItem(SystemEvent.OpenTerminal.getId(), "T&erminal");
		tileMenu.addItem(SystemEvent.OpenPicem.getId(), "Pic&em");
		tileMenu.addItem(SystemEvent.OpenTurboEd.getId(), "Turbo&Ed");
		tileMenu.addItem(SystemEvent.OpenClockem.getId(), "Clock&em");

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
		TWidget active = this.getActiveWindow();
		TSplitPane split = null;

		// TODO: Fix this mess of code. Load classes on runtime, maybe?
		//	 Definetely should try

		int event_id = event.getId();
		
		if (event_id == SystemEvent.OpenTerminal.getId()) {
			Konsole konsole = new Konsole(this, "Konsole", 80, 40);
		}else if (event_id == SystemEvent.OpenClockem.getId()) {
			Clock clock = new Clock(this,"Clockem", 20, 5);
		}else if (event_id == SystemEvent.OpenAboutDialog.getId()) {
			this.messageBox("About", "TurboVision OS v0.1");
		} else if (event_id == SystemEvent.OpenTurboEd.getId()) {
			TurboEd ed = new TurboEd(this);
			this.activateWindow(ed);
		} else if (event_id == SystemEvent.OpenPicem.getId()) {
			String filename = Picem.readFile(this);
			try {
				System.out.println("jvision.JVision.onMenu(): " + Helpers.extractPath(filename));
				Picem picem = new Picem(this, new File(filename));
				this.activateWindow(picem);
			} catch (Exception ex) {
				TExceptionDialog e = new TExceptionDialog(this, ex);
			}
		} else if (event_id == SystemEvent.PicemChooseFile.getId()) {
			String filename = Picem.readFile(this);
			Picem picem = (Picem) active;
			int x = picem.getX();
			int y = picem.getY();
			picem.close();
			try {
				File file = new File(filename);
				picem = new Picem(this, new File(filename));
				picem.setX(x);
				picem.setY(y);
			} catch (IOException ex) {
				TExceptionDialog e = new TExceptionDialog(this, ex);
			}
		} else {
			super.onMenu(event);
		}

		return true;
	}
}
