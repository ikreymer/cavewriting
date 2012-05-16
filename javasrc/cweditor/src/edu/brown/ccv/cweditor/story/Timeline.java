package edu.brown.ccv.cweditor.story;

public class Timeline implements Named {
	String name;

	public Timeline(String name) {
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
	
}
