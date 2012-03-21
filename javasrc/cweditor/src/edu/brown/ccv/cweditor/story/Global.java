package edu.brown.ccv.cweditor.story;

import java.util.List;

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

	public Global(List<NamedPlacement> placements) {
	    this(new Camera(new Placement(getPlacement("Center", placements), new Vector3(0, 0, 6), null), 100.0), new Camera(new Placement(getPlacement("Center", placements), new Vector3(0, 0, 0), null), 100.0), new Color(0, 0, 0), false, false);
    }

	private static NamedPlacement getPlacement(String string, List<NamedPlacement> placements) {
		NamedPlacement relativePlacement = null;
		for (NamedPlacement namedPlacement : placements) {
			if (namedPlacement.getName().equals(string)) {
				relativePlacement = namedPlacement;
				break;
			}
		}
		if (relativePlacement == null) {
			throw new IllegalArgumentException("Could not find placement \""+string+"\" while creating a default Global object");
		}
		return relativePlacement;
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
