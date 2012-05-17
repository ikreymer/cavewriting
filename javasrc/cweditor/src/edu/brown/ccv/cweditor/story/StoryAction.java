package edu.brown.ccv.cweditor.story;

public class StoryAction {
	private StoryAction() {}
	
	public static final class ObjectChange extends StoryAction {
		StoryObject object;
		TransitionAction action;
		
		public ObjectChange(StoryObject object, TransitionAction action) {
	        this.object = object;
	        this.action = action;
        }
		
		public StoryObject getObject() {
        	return object;
        }
		
		public void setObject(StoryObject object) {
        	this.object = object;
        }
		
		public TransitionAction getAction() {
        	return action;
        }
		
		public void setAction(TransitionAction action) {
        	this.action = action;
        }
		
	}
	
	public static final class GroupChange extends StoryAction {
		public static enum Random {
			SELECT_ONE_RANDOMLY
		}
		
		Group group;
		TransitionAction action;
		Random random;

		public GroupChange(Group group, TransitionAction action, Random random) {
			this.group = group;
			this.action = action;
			this.random = random;
		}
		
		public Group getGroup() {
			return group;
		}
		
		public void setGroup(Group group) {
			this.group = group;
		}
		
		public TransitionAction getAction() {
			return action;
		}
		
		public void setAction(TransitionAction action) {
			this.action = action;
		}

		public Random getRandom() {
        	return random;
        }

		public void setRandom(Random random) {
        	this.random = random;
        }
	}
	
	public static final class TimelineChange extends StoryAction {
		public static enum Change {
			START, STOP, CONTINUE, START_IF_NOT_STARTED;
		}
		
		Change change;
		Timeline timeline;
		
		public TimelineChange(Change change, Timeline timeline) {
	        super();
	        this.change = change;
	        this.timeline = timeline;
        }
		
		public Change getChange() {
        	return change;
        }
		
		public void setChange(Change change) {
        	this.change = change;
        }
		
		public Timeline getTimeline() {
        	return timeline;
        }
		
		public void setTimeline(Timeline timeline) {
        	this.timeline = timeline;
        }
	}
	
	public static final class SoundRef extends StoryAction {
		Sound sound;

		public SoundRef(Sound sound) {
	        this.sound = sound;
        }

		public Sound getSound() {
        	return sound;
        }

		public void setSound(Sound sound) {
        	this.sound = sound;
        }
	}
	
	public static final class EventChange extends StoryAction {
		Event event;
		boolean enable;
		
		public EventChange(Event event, boolean enable) {
	        this.event = event;
	        this.enable = enable;
        }

		public Event getEvent() {
        	return event;
        }

		public void setEvent(Event event) {
        	this.event = event;
        }

		public boolean isEnable() {
        	return enable;
        }

		public void setEnable(boolean enable) {
        	this.enable = enable;
        }
	}
	
	public static final class MoveCave extends StoryAction {
		// TODO: separate internal name from XML name
		public static enum Type {
			Absolute, Relative;
		}
		
		double duration;
		Type type;
		Placement placement;
		
		public MoveCave(double duration, Type type, Placement placement) {
	        super();
	        this.duration = duration;
	        this.type = type;
	        this.placement = placement;
        }

		public double getDuration() {
        	return duration;
        }

		public void setDuration(double duration) {
        	this.duration = duration;
        }

		public Type getType() {
        	return type;
        }

		public void setType(Type type) {
        	this.type = type;
        }

		public Placement getPlacement() {
        	return placement;
        }

		public void setPlacement(Placement placement) {
        	this.placement = placement;
        }
	}
	
	public static final class Restart extends StoryAction { Restart() {} }
	public static final Restart RESTART = new Restart();
}
