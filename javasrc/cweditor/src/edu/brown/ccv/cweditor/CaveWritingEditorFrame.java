package edu.brown.ccv.cweditor;

import java.awt.event.*;
import java.io.*;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.*;

import edu.brown.ccv.cweditor.actions.*;
import edu.brown.ccv.cweditor.story.Story;
import edu.brown.ccv.cweditor.xml.*;

@SuppressWarnings("serial")
public class CaveWritingEditorFrame extends JFrame implements WindowListener {
	JTabbedPane tabs;
	
	
	NewAction newAction = new NewAction(this);
	OpenAction openAction = new OpenAction(this);
	
	public CaveWritingEditorFrame() {
		super("Cave Writing Text Editor");
		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(this);
		setJMenuBar(new CaveWritingEditorMenuBar(this));
		
		trySettingIcon();
		
		tabs = new JTabbedPane();
		add(tabs);
		
		pack();
	}
	
	public void openStoryTab(Story story) {
		openStoryTab(new CaveWritingEditorStoryTab(this, story, null));
	}
	
	public void openStoryTab(File file) {
		Story story = null;
		
		// TODO: display error to user
		try {
			story = StoryReader.readStory(new FileInputStream(file));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XMLParseException e) {
	        e.printStackTrace();
        }
		
		if (story != null) {
			openStoryTab(new CaveWritingEditorStoryTab(this, story, file));
		}
	}
	
	private void openStoryTab(CaveWritingEditorStoryTab tab) {
		tabs.addTab(tab.getTabTitle(), tab);
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
	
	static final javax.swing.filechooser.FileFilter STORY_FILE_FILTER = new javax.swing.filechooser.FileFilter() {
		@Override
		public boolean accept(File file) {
			return file.isDirectory() || file.getName().endsWith(".xml");
		}
		
		@Override
		public String getDescription() {
			return "Story XML files (*.xml)";
		}
	};
	JFileChooser storyFileChooser = new JFileChooser();
	{
		storyFileChooser.setFileFilter(STORY_FILE_FILTER);
	}

	public JFileChooser getStoryFileChooser() {
    	return storyFileChooser;
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

	// for testing - can be removed eventually
//	private JComponent makeClosableTab() {
//		JButton closeButton = new JButton("Close");
//		closeButton.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				tabs.remove(tabs.getSelectedIndex());
//			}
//		});
//		
//		JPanel ret = new JPanel();
//		ret.setLayout(new BoxLayout(ret, BoxLayout.LINE_AXIS));
//		ret.add(closeButton);
//		
//		return ret;
//	}
}
