package edu.brown.ccv.cweditor;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class CaveWritingEditorFrame extends JFrame implements WindowListener {
	private static final long serialVersionUID = 1151513687984553951L;
	
	public CaveWritingEditorFrame() {
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(this);
	}

	@Override
    public void windowActivated(WindowEvent arg0) {
    }

	@Override
    public void windowClosed(WindowEvent arg0) {
    }

	@Override
    public void windowClosing(WindowEvent arg0) {
		switch (JOptionPane.showConfirmDialog(this, "Really quit?", "Are you sure you want to quit?", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE)) {
		case JOptionPane.YES_OPTION:
			dispose();
			break;
		case JOptionPane.NO_OPTION:
			break;
		default:
			throw new UnsupportedOperationException("Bad value returned from window closing dialog?");
		}
    }

	@Override
    public void windowDeactivated(WindowEvent arg0) {
    }

	@Override
    public void windowDeiconified(WindowEvent arg0) {
    }

	@Override
    public void windowIconified(WindowEvent arg0) {
    }

	@Override
    public void windowOpened(WindowEvent arg0) {
    }

}
