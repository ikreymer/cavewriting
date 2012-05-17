package edu.brown.ccv.cweditor.story;

import java.util.*;

public class Group implements Named {
	public static enum Mode {
		OBJECTS, GROUPS;
	}
	String name;
	List<StoryObject> objects = new ArrayList<StoryObject>();
	List<Group> groups = new ArrayList<Group>();
	Mode mode;

	public Group(String name) {
	    this.name = name;
    }

	@Override
    public String getName() {
	    return name;
    }

	@Override
    public void setName(String name) {
	    this.name = name;
    }

	public List<StoryObject> getObjects() {
    	return objects;
    }

	public List<Group> getGroups() {
    	return groups;
    }

	public Mode getMode() {
    	return mode;
    }

	public void setMode(Mode mode) {
    	this.mode = mode;
    }
	
}
