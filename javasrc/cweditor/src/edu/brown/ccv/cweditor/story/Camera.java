package edu.brown.ccv.cweditor.story;

public class Camera {
	Placement placement;
	double farClip;
	
	public Camera(Placement placement, double farClip) {
	    this.placement = placement;
	    this.farClip = farClip;
    }

	public double getFarClip() {
    	return farClip;
    }

	public void setFarClip(double farClip) {
    	this.farClip = farClip;
    }

	public Placement getPlacement() {
    	return placement;
    }
}
