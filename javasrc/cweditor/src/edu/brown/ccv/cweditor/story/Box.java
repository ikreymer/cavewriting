package edu.brown.ccv.cweditor.story;

public final class Box implements Event.Track.HeadTrack.Position {
	// TODO: separate xml names from internal names
	public static enum Movement {
		Inside, Outside;
	}
	
	boolean ignoreY;
	Vector3 corner1, corner2;
	Movement movement;
	
	public Box(boolean ignoreY, Vector3 corner1, Vector3 corner2, Movement movement) {
        this.ignoreY = ignoreY;
        this.corner1 = corner1;
        this.corner2 = corner2;
        this.movement = movement;
    }

	public boolean isIgnoreY() {
    	return ignoreY;
    }

	public void setIgnoreY(boolean ignoreY) {
    	this.ignoreY = ignoreY;
    }

	public Vector3 getCorner1() {
    	return corner1;
    }

	public void setCorner1(Vector3 corner1) {
    	this.corner1 = corner1;
    }

	public Vector3 getCorner2() {
    	return corner2;
    }

	public void setCorner2(Vector3 corner2) {
    	this.corner2 = corner2;
    }

	public Movement getMovement() {
    	return movement;
    }

	public void setMovement(Movement movement) {
    	this.movement = movement;
    }
}