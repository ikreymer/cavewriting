package edu.brown.ccv.cweditor.actions;

import java.awt.Toolkit;
import java.awt.event.*;

import javax.swing.*;

import edu.brown.ccv.cweditor.CaveWritingEditorFrame;

@SuppressWarnings("serial")
public class OpenAction extends AbstractAction {
	CaveWritingEditorFrame frame;
	
	public OpenAction(CaveWritingEditorFrame frame) {
		super("Open...");
		this.frame = frame;
		
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		putValue(Action.MNEMONIC_KEY, KeyEvent.VK_O);
		putValue(Action.DISPLAYED_MNEMONIC_INDEX_KEY, 0);
		putValue(Action.SHORT_DESCRIPTION, "Open a story from an existing file");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		int result = frame.getStoryFileChooser().showOpenDialog(frame);
		switch (result) {
		case JFileChooser.APPROVE_OPTION:
			frame.openStoryTab(frame.getStoryFileChooser().getSelectedFile());
			break;
		case JFileChooser.CANCEL_OPTION:
		case JFileChooser.ERROR_OPTION:
			break;
		default:
			throw new UnsupportedOperationException("Invalid return from JFileChooser.showOpenDialog()");
		}
	}
	
}
