package edu.brown.ccv.cweditor.actions;

import java.awt.Toolkit;
import java.awt.event.*;

import javax.swing.*;

import edu.brown.ccv.cweditor.CaveWritingEditorFrame;
import edu.brown.ccv.cweditor.story.Story;

@SuppressWarnings("serial")
public class NewAction extends AbstractAction {
	CaveWritingEditorFrame frame;
	
	public NewAction(CaveWritingEditorFrame frame) {
		super("New");
		this.frame = frame;
		
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		putValue(Action.MNEMONIC_KEY, KeyEvent.VK_N);
		putValue(Action.DISPLAYED_MNEMONIC_INDEX_KEY, 0);
		putValue(Action.SHORT_DESCRIPTION, "Create a new story");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		frame.openStoryTab(new Story());
	}
	
}
