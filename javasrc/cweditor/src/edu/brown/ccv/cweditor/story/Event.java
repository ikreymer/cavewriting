package edu.brown.ccv.cweditor.story;

import java.util.*;

public class Event implements Named {
	public static class Track {
		Track() {}
		
		public static final class HeadTrack extends Track {
			public interface Position {}
			
			public static final class Anywhere implements Position {}
			public static final Anywhere ANYWHERE = new Anywhere();
			
			public static class Direction {
				Direction() {}
				
				public static final class None extends Direction {}
				public static final None NONE = new None();
				
				public static final class ObjectTarget extends Direction {
					StoryObject object;

					public ObjectTarget(StoryObject object) {
	                    this.object = object;
                    }

					public StoryObject getObject() {
                    	return object;
                    }

					public void setObject(StoryObject object) {
                    	this.object = object;
                    }
				}
				
				public static final class DirectionTarget extends Direction {
					Vector3 direction;
					double angle;
					
					public DirectionTarget(Vector3 direction, double angle) {
	                    this.direction = direction;
	                    this.angle = angle;
                    }

					public Vector3 getDirection() {
                    	return direction;
                    }

					public void setDirection(Vector3 direction) {
                    	this.direction = direction;
                    }

					public double getAngle() {
                    	return angle;
                    }

					public void setAngle(double angle) {
                    	this.angle = angle;
                    }
				}
				
				public static final class PointTarget extends Direction {
					Vector3 point;
					double angle;
					
					public PointTarget(Vector3 point, double angle) {
	                    this.point = point;
	                    this.angle = angle;
                    }

					public Vector3 getPoint() {
                    	return point;
                    }

					public void setPoint(Vector3 point) {
                    	this.point = point;
                    }

					public double getAngle() {
                    	return angle;
                    }

					public void setAngle(double angle) {
                    	this.angle = angle;
                    }
				}
			}
			
			Position position;
			Direction direction;
			
			public HeadTrack(Position position, Direction direction) {
	            this.position = position;
	            this.direction = direction;
            }
			
			public Position getPosition() {
            	return position;
            }
			
			public void setPosition(Position position) {
            	this.position = position;
            }
			
			public Direction getDirection() {
            	return direction;
            }
			
			public void setDirection(Direction direction) {
            	this.direction = direction;
            }
		}
		
		public static final class MoveTrack extends Track {
			public static class Source {
				Source() {}
				
				public static final class ObjectRef extends Source {
					StoryObject object;

					public ObjectRef(StoryObject object) {
	                    this.object = object;
                    }

					public StoryObject getObject() {
                    	return object;
                    }

					public void setObject(StoryObject object) {
                    	this.object = object;
                    }
				}
				
				public static final class GroupObj extends Source {
					public static enum Selection {
						ANY, ALL;
					}
					
					Group group;
					Selection selection;
					
					public GroupObj(Group group, Selection selection) {
	                    this.group = group;
	                    this.selection = selection;
                    }

					public Group getGroup() {
                    	return group;
                    }

					public void setGroup(Group group) {
                    	this.group = group;
                    }

					public Selection getSelection() {
                    	return selection;
                    }

					public void setSelection(Selection selection) {
                    	this.selection = selection;
                    }
				}
			}
			
			Box box;
			Source source;
			
			public MoveTrack(Box box, Source source) {
	            this.box = box;
	            this.source = source;
            }

			public Box getBox() {
            	return box;
            }

			public void setBox(Box box) {
            	this.box = box;
            }

			public Source getSource() {
            	return source;
            }

			public void setSource(Source source) {
            	this.source = source;
            }
		}
	}
	
	boolean enabled;
	String name;
	double duration;
	boolean remainEnabled;
	Track track;
	List<StoryAction> actions = new ArrayList<StoryAction>();

	public Event(String name) {
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

	public Track getTrack() {
    	return track;
    }

	public void setTrack(Track track) {
    	this.track = track;
    }

	public List<StoryAction> getActions() {
    	return actions;
    }

	public boolean isEnabled() {
    	return enabled;
    }

	public void setEnabled(boolean enabled) {
    	this.enabled = enabled;
    }

	public double getDuration() {
    	return duration;
    }

	public void setDuration(double duration) {
    	this.duration = duration;
    }

	public boolean isRemainEnabled() {
    	return remainEnabled;
    }

	public void setRemainEnabled(boolean remainEnabled) {
    	this.remainEnabled = remainEnabled;
    }
}
