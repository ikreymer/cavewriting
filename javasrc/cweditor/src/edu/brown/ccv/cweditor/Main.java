package edu.brown.ccv.cweditor;

import javax.swing.UIManager;

public class Main {
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		CaveWritingEditorFrame frame = new CaveWritingEditorFrame();
		frame.setVisible(true);
	}
}
