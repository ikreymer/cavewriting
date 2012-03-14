package edu.brown.ccv.cweditor.story;

import java.util.*;

public class Story {
	List<StoryObject> objects;
	List<Group> groups;
	List<Timeline> timelines;
	List<NamedPlacement> placements;
	List<Sound> sounds;
	List<Event> events;
	List<ParticleAction> particleActions;
	Global globals;
	
	String version;
	String lastXpath;
	
	public Story(List<StoryObject> objects, List<Group> groups, List<Timeline> timelines, List<NamedPlacement> placements, List<Sound> sounds, List<Event> events, List<ParticleAction> particleActions, Global globals, String version, String lastXpath) {
	    this.objects = objects;
	    this.groups = groups;
	    this.timelines = timelines;
	    this.placements = placements;
	    this.sounds = sounds;
	    this.events = events;
	    this.particleActions = particleActions;
	    this.globals = globals;
	    
	    this.version = version;
	    this.lastXpath = lastXpath;
    }

	public List<Event> getEvents() {
    	return events;
    }

	public Global getGlobals() {
    	return globals;
    }

	public List<Group> getGroups() {
    	return groups;
    }

	public String getLastXpath() {
    	return lastXpath;
    }

	public List<StoryObject> getObjects() {
    	return objects;
    }

	public List<NamedPlacement> getPlacements() {
    	return placements;
    }

	public List<ParticleAction> getParticleActions() {
    	return particleActions;
    }

	public List<Sound> getSounds() {
    	return sounds;
    }

	public List<Timeline> getTimelines() {
    	return timelines;
    }

	public String getVersion() {
    	return version;
    }

	public void setLastXpath(String lastXpath) {
    	this.lastXpath = lastXpath;
    }

	public void setVersion(String version) {
    	this.version = version;
    }
	
}
