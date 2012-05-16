package edu.brown.ccv.cweditor.story;

public class Event implements Named {
	String name;

	public Event(String name) {
	    super();
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
