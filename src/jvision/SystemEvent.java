/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jvision;

/**
 *
 * @author hexaredecimal
 */
public enum SystemEvent {
	OpenTerminal(2010),
	OpenAboutDialog(2011),
	OpenSettings(2012),
	OpenTurboEd(2012),
	OpenPicem(2013),
	OpenClockem(2014),

	
	// Menu events
	PicemChooseFile(3000),
	ClockemAbout(3020);
	
	private int id;

	SystemEvent(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}
}
