package edu.brown.ccv.cweditor.story;

import java.util.*;

public class Timeline implements Named {
	public static class TimedAction {
		double secondsTime;
		StoryAction action;

		public TimedAction(double secondsTime, StoryAction action) {
	        this.secondsTime = secondsTime;
	        this.action = action;
        }

		public double getSecondsTime() {
        	return secondsTime;
        }

		public void setSecondsTime(double secondsTime) {
        	this.secondsTime = secondsTime;
        }

		public StoryAction getAction() {
        	return action;
        }

		public void setAction(StoryAction action) {
        	this.action = action;
        }
	}
	
	String name;
	boolean startImmediately;
	List<TimedAction> actions = new ArrayList<TimedAction>();

	public Timeline(String name) {
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

	public boolean isStartImmediately() {
    	return startImmediately;
    }

	public void setStartImmediately(boolean startImmediately) {
    	this.startImmediately = startImmediately;
    }

	public List<TimedAction> getActions() {
    	return actions;
    }
}
