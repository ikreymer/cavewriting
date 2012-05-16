package edu.brown.ccv.cweditor.story;

public class ParticleAction implements Named {
	String name;

	public ParticleAction(String name) {
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
