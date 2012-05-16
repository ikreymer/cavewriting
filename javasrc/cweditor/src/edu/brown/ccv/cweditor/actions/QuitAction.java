package edu.brown.ccv.cweditor.actions;

import java.awt.Toolkit;
import java.awt.event.*;

import javax.swing.*;

import edu.brown.ccv.cweditor.CaveWritingEditorFrame;

@SuppressWarnings("serial")
public class QuitAction extends AbstractAction {
	CaveWritingEditorFrame frame;
	
	public QuitAction(CaveWritingEditorFrame frame) {
		super("Quit");
		this.frame = frame;
		
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Q, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		putValue(Action.MNEMONIC_KEY, KeyEvent.VK_Q);
		putValue(Action.DISPLAYED_MNEMONIC_INDEX_KEY, 0);
		putValue(Action.SHORT_DESCRIPTION, "Exit the program");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (frame.anyUnsavedChanges()) {
    		switch (JOptionPane.showConfirmDialog(frame, "Really quit?", "Are you sure you want to quit?", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE)) {
    		case JOptionPane.YES_OPTION:
    			break;
    		case JOptionPane.NO_OPTION:
    		case JOptionPane.CANCEL_OPTION:
    			return;
    		default:
    			throw new UnsupportedOperationException("Bad value returned from window closing dialog?");
    		}
		}
		frame.kill();
	}
	
}
