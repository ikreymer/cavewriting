package edu.brown.ccv.cweditor;

import java.awt.Dimension;

import javax.swing.UIManager;

public class Main {
	public static final String APPLICATION_NAME = "Cave Writing Text Editor";
	
	public static void main(String[] args) {
		// set properties for apple look-and-feel
		try {
			System.setProperty("apple.laf.useScreenMenuBar", "true");
            System.setProperty("com.apple.mrj.application.apple.menu.about.name", APPLICATION_NAME);
            System.setProperty("apple.awt.showGrowBox", "true");
            System.setProperty("apple.awt.brushMetalLook", "true");
		} catch (Exception e) {
			// too bad
		}
		
		// set native look-and-feel
        try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		CaveWritingEditorFrame frame = new CaveWritingEditorFrame(true);
		frame.setMinimumSize(new Dimension(600, 400));
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
