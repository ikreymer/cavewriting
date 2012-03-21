package edu.brown.ccv.cweditor.story;

public class Placement {
	NamedPlacement relativeTo;
	Vector3 position;
	Rotation rotation;
	
	public Placement(NamedPlacement relativeTo, Vector3 position, Rotation rotation) {
	    super();
	    this.relativeTo = relativeTo;
	    this.position = position;
	    this.rotation = rotation;
    }

	public NamedPlacement getRelativeTo() {
    	return relativeTo;
    }

	public void setRelativeTo(NamedPlacement relativeTo) {
    	this.relativeTo = relativeTo;
    }

	public Vector3 getPosition() {
    	return position;
    }

	public void setPosition(Vector3 position) {
    	this.position = position;
    }

	public Rotation getRotation() {
    	return rotation;
    }

	public void setRotation(Rotation rotation) {
    	this.rotation = rotation;
    }
	
	
}
