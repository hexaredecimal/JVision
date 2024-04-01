package apps;

import java.util.ArrayList;
import jexer.TAction;
import jexer.TApplication;
import jexer.TCalendar;
import jexer.TDesktop;
import jexer.TWindow;
import jexer.event.TMenuEvent;
import jexer.menu.TMenu;
import jvision.Helpers;
import jvision.SystemEvent;

/**
 *
 * @author hexaredecimal
 */
public class Callizy extends TWindow {

	private TApplication parent;
	private ArrayList<TMenu> menus; 
	public Callizy(TApplication parent) {
		super(parent, "Callizy", 32, 12);
		this.parent = parent;
		this.menus = new ArrayList<>(); 
		addMenus();

		TDesktop desktop = parent.getDesktop();
		int sceen_w = desktop.getWidth();
		int sceen_h = desktop.getHeight();

		int x = (int) ((double) sceen_w * 35 / 100);
		int y = (int) ((double) sceen_h * 10 / 100);
		setX(x);
		setY(y);
		TCalendar calendar = addCalendar(1, 1,
			new TAction() {
				public void DO() {
				}
			}
		);
	}

	private void addMenus() {
		Helpers.addNonExist(menus, getAppMenu());
	}

	private TMenu getAppMenu() {
		TMenu app = parent.addMenu("&Calizzy");
		app.addItem(0x25500A, "A&bout");
		app.addSeparator();
		app.addItem(SystemEvent.CloseApp.getId(), "E&xit");
		return app;
	}

	@Override
	public void onMenu(TMenuEvent event) {
		TWindow active = parent.getActiveWindow();
		int event_id = event.getId();

		if (event_id == 0x25500A) {
			Helpers.showAboutMessage(parent, "Callizy Calender");
		}
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
		this.onUnfocus();
		super.onClose();
	}

}
