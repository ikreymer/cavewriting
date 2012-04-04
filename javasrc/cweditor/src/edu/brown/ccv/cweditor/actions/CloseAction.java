package edu.brown.ccv.cweditor.actions;

import java.awt.Toolkit;
import java.awt.event.*;

import javax.swing.*;

import edu.brown.ccv.cweditor.CaveWritingEditorFrame;

@SuppressWarnings("serial")
public class CloseAction extends AbstractAction {
	CaveWritingEditorFrame frame;
	
	public CloseAction(CaveWritingEditorFrame frame) {
		super("Close");
		this.frame = frame;
		
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_W, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		putValue(Action.MNEMONIC_KEY, KeyEvent.VK_C);
		putValue(Action.DISPLAYED_MNEMONIC_INDEX_KEY, 0);
		putValue(Action.SHORT_DESCRIPTION, "Close the current story");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		frame.closeCurrentTab();
	}
	
}
