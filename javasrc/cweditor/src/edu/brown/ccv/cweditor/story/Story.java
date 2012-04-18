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
	
	public Story() {
		this.objects = new ArrayList<StoryObject>();
		this.groups = new ArrayList<Group>();
		this.timelines = new ArrayList<Timeline>();
		this.placements = createDefaultPlacements();
		this.sounds = new ArrayList<Sound>();
		this.events = new ArrayList<Event>();
		this.particleActions = new ArrayList<ParticleAction>();
		this.globals = new Global(this.placements);
		this.version = "8";
		this.lastXpath = null;
	}
	
	private static List<NamedPlacement> createDefaultPlacements() {
		ArrayList<NamedPlacement> ret = new ArrayList<NamedPlacement>();
	    
		NamedPlacement center = new NamedPlacement(null, new Vector3(0,0,0), new Rotation.Axis(new Vector3(0, 1, 0), 0), "Center");
	    center.setRelativeTo(center);
	    
	    NamedPlacement frontWall = new NamedPlacement(center, new Vector3(0,0,-4), new Rotation.LookAt(new Vector3(0, 0, 0), new Vector3(0, 1, 0)), "FrontWall");
	    NamedPlacement leftWall = new NamedPlacement(center, new Vector3(-4,0,0), new Rotation.LookAt(new Vector3(0, 0, 0), new Vector3(0, 1, 0)), "LeftWall");
	    NamedPlacement rightWall = new NamedPlacement(center, new Vector3(4,0,0), new Rotation.LookAt(new Vector3(0, 0, 0), new Vector3(0, 1, 0)), "RightWall");
	    NamedPlacement floorWall = new NamedPlacement(center, new Vector3(0,-4,0), new Rotation.LookAt(new Vector3(0, 0, 0), new Vector3(0, 0, -1)), "FloorWall");
	    
	    ret.add(center);
	    ret.add(frontWall);
	    ret.add(leftWall);
	    ret.add(rightWall);
	    ret.add(floorWall);
	    
		return ret;
    }

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
