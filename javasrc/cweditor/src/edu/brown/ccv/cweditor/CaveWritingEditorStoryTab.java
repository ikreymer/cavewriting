package edu.brown.ccv.cweditor;

import java.io.File;

import javax.swing.JPanel;

import edu.brown.ccv.cweditor.story.Story;

@SuppressWarnings("serial")
public class CaveWritingEditorStoryTab extends JPanel {
	Story story;
	File file;
	CaveWritingEditorFrame frame;

	public CaveWritingEditorStoryTab(CaveWritingEditorFrame caveWritingEditorFrame, Story story, File file) {
		this.frame = caveWritingEditorFrame;
	    this.story = story;
	    this.file = file;
    }

	public String getTabTitle() {
	    return file == null ? "Untitled" : file.getName();
    }
	
}
