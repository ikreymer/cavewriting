package edu.brown.ccv.cweditor;

import java.awt.event.*;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.*;

@SuppressWarnings("serial")
public class CaveWritingEditorFrame extends JFrame implements WindowListener {
	JTabbedPane tabs;
	
	public CaveWritingEditorFrame() {
		super("Cave Writing Text Editor");
		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(this);
		setJMenuBar(new CaveWritingEditorMenuBar());
		
		trySettingIcon();
		
		tabs = new JTabbedPane();
		tabs.addTab("Project 1", makeClosableTab());
		tabs.addTab("Project 2", makeClosableTab());
		add(tabs);
		
		pack();
	}
	
	private JComponent makeClosableTab() {
		JButton closeButton = new JButton("Close");
		closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tabs.remove(tabs.getSelectedIndex());
			}
		});
		
		JPanel ret = new JPanel();
		ret.setLayout(new BoxLayout(ret, BoxLayout.LINE_AXIS));
		ret.add(closeButton);
		
		return ret;
	}

	private void trySettingIcon() {
		try {
    		URL url = getClass().getResource("resources/appicon_120x90.gif");
	        setIconImage(ImageIO.read(url));
        } catch (Exception e) {
	        System.err.println("Failed to set icon!");
	        e.printStackTrace();
        }
    }

	@Override
    public void windowClosing(WindowEvent arg0) {
		switch (JOptionPane.showConfirmDialog(this, "Really quit?", "Are you sure you want to quit?", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE)) {
		case JOptionPane.YES_OPTION:
			dispose();
			break;
		case JOptionPane.NO_OPTION:
		case JOptionPane.CANCEL_OPTION:
			break;
		default:
			throw new UnsupportedOperationException("Bad value returned from window closing dialog?");
		}
    }

	@Override public void windowActivated(WindowEvent arg0) {}
	@Override public void windowClosed(WindowEvent arg0) {}
	@Override public void windowDeactivated(WindowEvent arg0) {}
	@Override public void windowDeiconified(WindowEvent arg0) {}
	@Override public void windowIconified(WindowEvent arg0) {}
	@Override public void windowOpened(WindowEvent arg0) {}
}
