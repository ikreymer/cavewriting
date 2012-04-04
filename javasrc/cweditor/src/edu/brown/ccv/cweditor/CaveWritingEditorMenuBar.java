package edu.brown.ccv.cweditor;

import java.awt.event.KeyEvent;

import javax.swing.*;

@SuppressWarnings("serial")
public class CaveWritingEditorMenuBar extends JMenuBar {
	CaveWritingEditorFrame frame;
	
	public CaveWritingEditorMenuBar(CaveWritingEditorFrame frame) {
		this.frame = frame;
		
	    JMenu file = createFileMenu();
	    JMenu edit = createEditMenu();
	    JMenu run = createRunMenu();
	    
	    add(Box.createHorizontalStrut(3));
	    add(file);
	    add(Box.createHorizontalStrut(5));
	    add(edit);
	    add(Box.createHorizontalStrut(5));
	    add(run);
    }
	
	protected JMenu createRunMenu() {
		JMenu ret = new JMenu("Run");
		ret.setMnemonic(KeyEvent.VK_R);
		
		ret.add(new JMenuItem("Windowed Preview"));
		ret.add(new JMenuItem("Fullscreen Preview"));
		ret.addSeparator();
		ret.add(new JMenuItem("Run in Cave"));
		
		return ret;
	}

	protected JMenu createEditMenu() {
		JMenu ret = new JMenu("Edit");
		ret.setMnemonic(KeyEvent.VK_E);
		
		ret.add(new JMenuItem("Cut"));
		ret.add(new JMenuItem("Copy"));
		ret.add(new JMenuItem("Paste"));
		ret.addSeparator();
		ret.add(new JMenuItem("Find..."));
		
	    return ret;
    }

	protected JMenu createFileMenu() {
	    JMenu ret = new JMenu("File");
	    ret.setMnemonic(KeyEvent.VK_F);
	    
	    ret.add(frame.newAction);
	    ret.add(frame.openAction);
	    ret.add(new JMenuItem("Save"));
	    ret.add(new JMenuItem("Save As..."));
	    ret.addSeparator();
	    ret.add(new JMenuItem("Quit"));
	    
	    return ret;
    }
	
}
