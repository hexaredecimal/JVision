package apps;

import java.util.ArrayList;
import jexer.TApplication;
import jexer.TLabel;
import jexer.TPanel;
import jexer.TWindow;
import jexer.event.TMenuEvent;
import jexer.menu.TMenu;
import jvision.Helpers;
import jvision.SystemEvent;

/**
 * @author hexaredecimal
 */
public class Clock extends TWindow {
	private TLabel clock_label; 
	private final TPanel main_panel;
	private final ArrayList<TMenu> menus;
	private final TApplication parent; 

	public Clock(TApplication application, String title, int width, int height) {
		super(application, title, width, height);
		setResizable(false);
		main_panel = new TPanel(this ,0, 0, width, height);
		clock_label = main_panel.addLabel(Helpers.getTime(), width / 4,  height/4); 
		menus = new ArrayList<>();
		this.parent = application;
		addMenus();
	}

	@Override
	public void onIdle() {
		clock_label.remove();
		clock_label = main_panel.addLabel(Helpers.getTime(), main_panel.getWidth() / 4, main_panel.getHeight() / 4); 
	}


	private void addMenus() {
		menus.add(getAppMenu());
		menus.add(getDisplayMenu());
	}


	private TMenu getDisplayMenu() {
		TMenu display = parent.addMenu("D&ispay");
		display.addItem(0x0A111, "&Full D&ate");
		display.addItem(0x0A0B0, "S&how AM");
		return display; 
	}
	
	private TMenu getAppMenu() {
		TMenu app = parent.addMenu("Cloc&k");
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
			Helpers.showAboutMessage(parent, "Clockem");
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
