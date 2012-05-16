package edu.brown.ccv.cweditor.story;

public class Group implements Named{
	String name;

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
	
}
