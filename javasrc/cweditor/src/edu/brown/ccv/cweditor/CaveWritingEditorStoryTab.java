package edu.brown.ccv.cweditor;

import java.io.*;

import javax.swing.*;

import edu.brown.ccv.cweditor.story.Story;
import edu.brown.ccv.cweditor.xml.StoryWriter;

@SuppressWarnings("serial")
public class CaveWritingEditorStoryTab extends JPanel {
	Story story;
	File file;
	CaveWritingEditorFrame frame;
	
	JTextArea testArea;

	public CaveWritingEditorStoryTab(CaveWritingEditorFrame caveWritingEditorFrame, Story story, File file) {
		this.frame = caveWritingEditorFrame;
	    this.story = story;
	    this.file = file;
	    
	    ByteArrayOutputStream output = new ByteArrayOutputStream();
	    try {
	        StoryWriter.writeStory(output, story);
        } catch (IOException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
        }
	    
	    testArea = new JTextArea(output.toString());
	    add(new JScrollPane(testArea));
    }

	public String getTabTitle() {
	    return file == null ? "Untitled" : file.getName();
    }

	/**
	 * Should be called when the tab is about to be closed. If it returns false,
	 * the user likely had unsaved changes and clicked "cancel" on the dialog
	 * that showed up.
	 * 
	 * @return true if the tab can be closed, false if it should not be closed
	 */
	public boolean prepareToClose() {
	    return true;
    }
	
}
