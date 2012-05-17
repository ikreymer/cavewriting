package edu.brown.ccv.cweditor.story;

public class TransitionAction {
	public static final class Visible extends TransitionAction {
		boolean visible;

		public Visible(double duration, boolean visible) {
			super(duration);
	        this.visible = visible;
        }

		public boolean isVisible() {
        	return visible;
        }

		public void setVisible(boolean visible) {
        	this.visible = visible;
        }
	}
	
	public static final class Movement extends TransitionAction {
		Placement placement;

		public Movement(double duration, Placement placement) {
			super(duration);
	        this.placement = placement;
        }

		public Placement getPlacement() {
        	return placement;
        }

		public void setPlacement(Placement placement) {
        	this.placement = placement;
        }
	}
	
	public static final class MoveRel extends TransitionAction {
		Placement placement;
		
		public MoveRel(double duration, Placement placement) {
			super(duration);
			this.placement = placement;
		}
		
		public Placement getPlacement() {
			return placement;
		}
		
		public void setPlacement(Placement placement) {
			this.placement = placement;
		}
	}
	
	public static final class Color extends TransitionAction {
		edu.brown.ccv.cweditor.story.Color color;

		public Color(double duration, edu.brown.ccv.cweditor.story.Color color) {
			super(duration);
	        this.color = color;
        }

		public edu.brown.ccv.cweditor.story.Color getColor() {
        	return color;
        }

		public void setColor(edu.brown.ccv.cweditor.story.Color color) {
        	this.color = color;
        }
	}
	
	public static final class Scale extends TransitionAction {
		double scale;

		public Scale(double duration, double scale) {
	        super(duration);
	        this.scale = scale;
        }

		public double getScale() {
        	return scale;
        }

		public void setScale(double scale) {
        	this.scale = scale;
        }
	}
	
	public static final class Sound extends TransitionAction {
		public static enum Action {
			PLAY, STOP;
		}
		
		Action action;

		public Sound(double duration, Action action) {
	        super(duration);
	        this.action = action;
        }

		public Action getAction() {
        	return action;
        }

		public void setAction(Action action) {
        	this.action = action;
        }
	}
	
	public static final class LinkChange extends TransitionAction {
		public static enum Action {
			link_on, link_off, activate, activate_if_on;
		}
		
		Action action;
		
		public LinkChange(double duration, Action action) {
			super(duration);
			this.action = action;
		}

		public Action getAction() {
        	return action;
        }

		public void setAction(Action action) {
        	this.action = action;
        }
	}
	
	double duration;

	TransitionAction(double duration) {
	    this.duration = duration;
    }

	public double getDuration() {
    	return duration;
    }

	public void setDuration(double duration) {
    	this.duration = duration;
    }
}
