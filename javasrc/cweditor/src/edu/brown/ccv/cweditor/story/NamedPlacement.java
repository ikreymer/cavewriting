package edu.brown.ccv.cweditor.story;

public class NamedPlacement extends Placement implements Named {
	String name;
	
	public NamedPlacement(NamedPlacement relativeTo, Vector3 position, Rotation rotation, String name) {
	    super(relativeTo, position, rotation);
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
