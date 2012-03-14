package edu.brown.ccv.cweditor;

import java.awt.Dimension;

import javax.swing.UIManager;

public class Main {
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		CaveWritingEditorFrame frame = new CaveWritingEditorFrame();
		frame.setMinimumSize(new Dimension(300, 200));
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
