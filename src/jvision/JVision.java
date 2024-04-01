package jvision;

import apps.Callizy;
import apps.Clock;
import apps.DeskTopWindow;
import apps.Fileman;
import apps.Konsole;
import apps.Picem;
import apps.TurboEd;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import jexer.TApplication;
import jexer.TDesktop;
import jexer.TEditColorThemeWindow;
import jexer.TExceptionDialog;
import jexer.TTerminalWidget;
import jexer.TWidget;
import jexer.TWindow;
import jexer.event.TMenuEvent;
import jexer.menu.TMenu;
import jexer.menu.TMenuItem;
import jexer.menu.TSubMenu;

public class JVision extends TApplication {

	private static final ResourceBundle i18n = ResourceBundle.getBundle(TApplication.class.getName());
	final static int WIDTH = 200;
	final static int HEIGHT = 80;
	final static int FONT_SIZE = 12;

	private final ArrayList<TMenu> root;

	public static void main(String[] args) throws Exception {
		System.setProperty("jexer.TTerminal.ptypipe", "true");
		System.setProperty("jexer.hideStatusBar", "true");

		final JVision jtwm = new JVision();
		(new Thread(jtwm)).start();

		jtwm.invokeLater(() -> {
			System.out.println("jvision.JVision.main()");
		});
	}

	public JVision() throws Exception {
		super(BackendType.SWING, WIDTH, HEIGHT, FONT_SIZE);
		root = new ArrayList<>();
		System.setProperty("jexer.TWindow.borderStyleForeground", "round");
		System.setProperty("jexer.TWindow.borderStyleModal", "round");
		System.setProperty("jexer.TWindow.borderStyleMoving", "round");
		System.setProperty("jexer.TWindow.borderStyleInactive", "round");
		
		try {
			URL wall_paper = this.getClass().getResource("/wallpapers/default.jpg");
			DeskTopWindow win = new DeskTopWindow(this, new File(wall_paper.getPath()), WIDTH, HEIGHT);
			win.setActive(false);
			win.setEnabled(false);
			win.setTitle("");
		} catch (Exception e) {
			new TExceptionDialog(this, e);
		}

		TMenu tileMenu = addMenu(i18n.getString("toolMenuTitle"));
		TMenu windowMenu = addWindowMenu();

		tileMenu.addItem(SystemEvent.OpenTerminal.getId(), "Ko&nsole");
		tileMenu.addItem(SystemEvent.OpenPicem.getId(), "Pic&em");
		tileMenu.addItem(SystemEvent.OpenTurboEd.getId(), "Turbo&Ed");
		tileMenu.addItem(SystemEvent.OpenClockem.getId(), "Clock&em");

		tileMenu.addSeparator();
		TSubMenu utils = tileMenu.addSubMenu("Ut&ils");
		utils.addItem(0x23110, "Ca&lender");
		utils.addItem(0xbad2c, "F&ile man");

		tileMenu.addSeparator();
		TSubMenu sub = tileMenu.addSubMenu("Se&ttings");
		TSubMenu screen_set = sub.addSubMenu("De&sktop");
		screen_set.addDefaultItem(TMenu.MID_REPAINT);
		sub.addDefaultItem(TMenu.MID_SCREEN_OPTIONS);
		sub.addItem(0x01121, "T&heme");

		tileMenu.addSeparator();
		tileMenu.addItem(SystemEvent.OpenAboutDialog.getId(), "&About");

		root.add(tileMenu);
		root.add(windowMenu);

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

	@Override
	protected boolean onMenu(TMenuEvent event) {
		TWindow active = this.getActiveWindow();

		super.onMenu(event);
		TMenuItem item = this.getMenuItem(event.getId());
		if (active != null) {
			active.onMenu(event);
		}
		// TODO: Fix this mess of code. Load classes on runtime, maybe?
		//	 Definetely should try
		int event_id = event.getId();

		if (event_id == 0xbad2c) {
			List<String> filters = new ArrayList<String>();
			filters.add("^.*$");
			new Fileman(this, ".", filters);
		} else if (event_id == 0x23110) {
			new Callizy(this);
		} else if (event_id == 0x01121) {
			new TEditColorThemeWindow(this);
		} else if (event_id == SystemEvent.CloseApp.getId()) {
			active.close();
		} else if (event_id == SystemEvent.OpenTerminal.getId()) {
			new Konsole(this, "Konsole", 80, 40);
		} else if (event_id == SystemEvent.OpenClockem.getId()) {
			new Clock(this, "Clockem", 20, 5);
		} else if (event_id == SystemEvent.OpenAboutDialog.getId()) {
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
		}

		return true;
	}
}
