package apps;

import java.io.File;
import java.io.IOException;
import jexer.TApplication;
import jexer.TExceptionDialog;
import jexer.TImageWindow;

/**
 *
 * @author hexaredecimal
 */
public class DeskTopWindow extends TImageWindow {
	private final TApplication parent;
	private File buf;
	public DeskTopWindow(TApplication parent, File file, int width, int height) throws IOException {
		super(parent, file, -1, -1, width + 2, height + 2);
		this.parent = parent;
		this.buf = file;
		//setZ(-1000);
	}

	@Override
	public void onClose() {
		int width = getWidth() - 2; 
		int height = getHeight() - 2; 
		try {
			
			//new DeskTopWindow(this.parent, this.buf, width, height);
			//this.close();
		} catch (Exception ex) {
			System.out.println("apps.DeskTopWindow.onClose(): " + ex.getMessage());
			new TExceptionDialog(this.parent, ex);
		}
	}
}
