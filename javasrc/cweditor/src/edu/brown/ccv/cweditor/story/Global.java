package edu.brown.ccv.cweditor.story;

public class Global {
	Camera desktopCamera, caveCamera;
	Color backgroundColor;
	boolean allowWandRotation, allowWandMovement;
	
	public Global(Camera desktopCamera, Camera caveCamera, Color backgroundColor, boolean allowWandRotation, boolean allowWandMovement) {
	    super();
	    this.desktopCamera = desktopCamera;
	    this.caveCamera = caveCamera;
	    this.backgroundColor = backgroundColor;
	    this.allowWandRotation = allowWandRotation;
	    this.allowWandMovement = allowWandMovement;
    }

	public Color getBackgroundColor() {
    	return backgroundColor;
    }

	public void setBackgroundColor(Color backgroundColor) {
    	this.backgroundColor = backgroundColor;
    }

	public boolean isAllowWandRotation() {
    	return allowWandRotation;
    }

	public void setAllowWandRotation(boolean allowWandRotation) {
    	this.allowWandRotation = allowWandRotation;
    }

	public boolean isAllowWandMovement() {
    	return allowWandMovement;
    }

	public void setAllowWandMovement(boolean allowWandMovement) {
    	this.allowWandMovement = allowWandMovement;
    }

	public Camera getDesktopCamera() {
    	return desktopCamera;
    }

	public Camera getCaveCamera() {
    	return caveCamera;
    }
	
}
