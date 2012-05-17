package edu.brown.ccv.cweditor.xml;

import static edu.brown.ccv.cweditor.xml.XMLUtilities.*;

import java.io.*;
import java.util.List;

import org.dom4j.*;
import org.dom4j.io.*;

import edu.brown.ccv.cweditor.story.*;

public class StoryWriter {
	private StoryWriter(Story story) {
		this.story = story;
	}
	
	Story story;
	
	public static void writeStory(OutputStream outputStream, Story story) throws IOException {
		new StoryWriter(story).writeStoryInstance(outputStream);
	}
	
	private void writeStoryInstance(OutputStream outputStream) throws IOException {
		Document doc = DocumentFactory.getInstance().createDocument();
		
		Element root = DocumentHelper.createElement("Story");
		
		addStringAttributeIfNotNull(root, "version", story.getVersion());
		addStringAttributeIfNotNull(root, "last_xpath", story.getLastXpath());
		
		List<StoryObject> objects;
		if ((objects = story.getObjects()) != null && objects.size() > 0) {
			Element objectRoot = root.addElement("ObjectRoot");
    		for (StoryObject object : objects) {
    			writeObject(objectRoot, object);
    		}
		}
		
		List<Group> groups;
		if ((groups = story.getGroups()) != null && groups.size() > 0) {
			Element groupRoot = root.addElement("GroupRoot");
			for (Group group : groups) {
				writeGroup(groupRoot, group);
			}
		}
		
		List<Timeline> timelines;
		if ((timelines = story.getTimelines()) != null && timelines.size() > 0) {
			Element timelineRoot = root.addElement("TimelineRoot");
			for (Timeline timeline : timelines) {
				writeTimeline(timelineRoot, timeline);
			}
		}
		
		List<NamedPlacement> namedPlacements;
		if ((namedPlacements = story.getPlacements()) != null && namedPlacements.size() > 0) {
			Element placementsRoot = root.addElement("PlacementRoot");
    		for (NamedPlacement placement : namedPlacements) {
    			writeNamedPlacement(placementsRoot, placement);
    		}
		}
		
		List<Sound> sounds;
		if ((sounds = story.getSounds()) != null && sounds.size() > 0) {
			Element soundsRoot = root.addElement("SoundRoot");
			for (Sound sound : sounds) {
				writeSound(soundsRoot, sound);
			}
		}
		
		List<Event> events;
		if ((events = story.getEvents()) != null && events.size() > 0) {
			Element eventsRoot = root.addElement("EventRoot");
			for (Event event : events) {
				writeEvent(eventsRoot, event);
			}
		}
		
		List<ParticleActionList> particleActionLists;
		if ((particleActionLists = story.getParticleActionLists()) != null && particleActionLists.size() > 0) {
			Element particleActionListsRoot = root.addElement("ParticleActionRoot");
			for (ParticleActionList particleActionList : particleActionLists) {
				writeParticleActionList(particleActionListsRoot, particleActionList);
			}
		}
		
		writeGlobal(root, story.getGlobals(), story.getPlacements());
		
		doc.setRootElement(root);
		
		OutputFormat pretty = OutputFormat.createPrettyPrint();
		pretty.setTrimText(false); // VERY IMPORTANT; WITHOUT THIS NEWLINES IN TEXT BECOME SPACES
		XMLWriter filewriter = new XMLWriter(new OutputStreamWriter(outputStream), pretty);
		filewriter.write(doc);
		filewriter.flush();
	}

	/*
	 * -----------------------------------------------------------------------------------------------------
	 */
	
	/*
	 * PARTICLEACTION---------------------------------------------------------------------------------------
	 */
	
	private void writeParticleActionList(Element root, ParticleActionList particleActionList) {
		Element element = root.addElement("ParticleActionList");
		element.addAttribute("name", particleActionList.getName());
		Element sourceElement = element.addElement("Source");
		addDoubleAttribute(sourceElement, "rate", particleActionList.getSourceRate());
		writeParticleDomain(sourceElement, particleActionList.getSourceDomain());
		writeParticleDomain(element.addElement("Vel"), particleActionList.getVelDomain());
		
		for (ParticleAction action : particleActionList.getActions()) {
			writeParticleAction(element.addElement("ParticleAction"), action);
		}
		
		Element removeConditionElement = element.addElement("RemoveCondition");
		ParticleActionList.RemoveCondition condition = particleActionList.getRemoveCondition();
		if (condition.getClass() == ParticleActionList.RemoveCondition.Age.class) {
			ParticleActionList.RemoveCondition.Age age = (ParticleActionList.RemoveCondition.Age) condition;
			Element ageElement = removeConditionElement.addElement("Age");
			addDoubleAttribute(ageElement, "age", age.getAge());
			addBooleanAttribute(ageElement, "younger-than", age.isYoungerThan());
		} else if (condition.getClass() == ParticleActionList.RemoveCondition.Position.class) {
			ParticleActionList.RemoveCondition.Position position = (ParticleActionList.RemoveCondition.Position)condition;
			Element positionElement = removeConditionElement.addElement("Position");
			addBooleanAttribute(positionElement, "inside", position.isInside());
			writeParticleDomain(positionElement, position.getDomain());
		} else if (condition.getClass() == ParticleActionList.RemoveCondition.Velocity.class) {
			ParticleActionList.RemoveCondition.Velocity velocity = (ParticleActionList.RemoveCondition.Velocity)condition;
			Element velocityElement = removeConditionElement.addElement("Velocity");
			addBooleanAttribute(velocityElement, "inside", velocity.isInside());
			writeParticleDomain(velocityElement, velocity.getDomain());
		} else
			throw new RuntimeException();
    }
	
	private void writeParticleAction(Element actionElement, ParticleAction action) {
		if (action.getClass() == ParticleAction.Gravity.class) {
			actionElement.addElement("Gravity").addAttribute("direction", ((ParticleAction.Gravity)action).getDirection().toString());
		} else if (action.getClass() == ParticleAction.Damping.class) {
			ParticleAction.Damping damping = (ParticleAction.Damping) action;
			Element e = actionElement.addElement("Damping").addAttribute("direction", damping.getDirection().toString());
			addDoubleAttribute(e, "vel_low", damping.getVelLow());
			addDoubleAttribute(e, "vel_high", damping.getVelHigh());
		} else if (action.getClass() == ParticleAction.Gravitate.class) {
			ParticleAction.Gravitate gravitate = (ParticleAction.Gravitate) action;
			Element e = actionElement.addElement("Gravitate");
			addDoubleAttribute(e, "magnitude", gravitate.getMagnitude());
			addDoubleAttribute(e, "epsilon", gravitate.getEpsilon());
			addDoubleAttribute(e, "max_radius", gravitate.getMaxRadius());
		} else if (action.getClass() == ParticleAction.Follow.class) {
			ParticleAction.Follow follow = (ParticleAction.Follow) action;
			Element e = actionElement.addElement("Follow");
			addDoubleAttribute(e, "magnitude", follow.getMagnitude());
			addDoubleAttribute(e, "epsilon", follow.getEpsilon());
			addDoubleAttribute(e, "max_radius", follow.getMaxRadius());
		} else if (action.getClass() == ParticleAction.MatchVel.class) {
			ParticleAction.MatchVel matchVel = (ParticleAction.MatchVel) action;
			Element e = actionElement.addElement("MatchVel");
			addDoubleAttribute(e, "magnitude", matchVel.getMagnitude());
			addDoubleAttribute(e, "epsilon", matchVel.getEpsilon());
			addDoubleAttribute(e, "max_radius", matchVel.getMaxRadius());
		} else if (action.getClass() == ParticleAction.OrbitPoint.class) {
			ParticleAction.OrbitPoint orbitPoint = (ParticleAction.OrbitPoint) action;
			Element e = actionElement.addElement("OrbitPoint").addAttribute("center", orbitPoint.getCenter().toString());
			addDoubleAttribute(e, "magnitude", orbitPoint.getMagnitude());
			addDoubleAttribute(e, "epsilon", orbitPoint.getEpsilon());
			addDoubleAttribute(e, "max_radius", orbitPoint.getMaxRadius());
		} else if (action.getClass() == ParticleAction.Jet.class) {
			ParticleAction.Jet jet = (ParticleAction.Jet) action;
			Element e = actionElement.addElement("Jet");
			writeParticleDomain(e, jet.getDomain());
			writeParticleDomain(e, jet.getAccelDomain(), "AccelDomain");
		} else if (action.getClass() == ParticleAction.Bounce.class) {
			ParticleAction.Bounce bounce = (ParticleAction.Bounce) action;
			Element e = actionElement.addElement("Bounce");
			addDoubleAttribute(e, "friction", bounce.getFriction());
			addDoubleAttribute(e, "resilience", bounce.getResilience());
			addDoubleAttribute(e, "cutoff", bounce.getCutoff());
			writeParticleDomain(e, bounce.getDomain());
		} else if (action.getClass() == ParticleAction.Avoid.class) {
			ParticleAction.Avoid avoid = (ParticleAction.Avoid) action;
			Element e = actionElement.addElement("Avoid");
			addDoubleAttribute(e, "magnitude", avoid.getMagnitude());
			addDoubleAttribute(e, "epsilon", avoid.getEpsilon());
			addDoubleAttribute(e, "lookahead", avoid.getLookahead());
			writeParticleDomain(e, avoid.getDomain());
		} else
			throw new RuntimeException();
    }

	private void writeParticleDomain(Element parent, ParticleDomain domain) {
		writeParticleDomain(parent, domain, "ParticleDomain");
	}
	private void writeParticleDomain(Element parent, ParticleDomain domain, String elementName) {
		parent = parent.addElement(elementName);
		if (domain.getClass() == ParticleDomain.Point.class) {
			ParticleDomain.Point pd = (ParticleDomain.Point) domain;
			Element e = parent.addElement("Point");
			writeVector3(e, "point", pd.getPoint());
		} else if (domain.getClass() == ParticleDomain.Line.class) {
			ParticleDomain.Line pd = (ParticleDomain.Line) domain;
			Element e = parent.addElement("Line");
			writeVector3(e, "p1", pd.getP1());
			writeVector3(e, "p2", pd.getP2());
		} else if (domain.getClass() == ParticleDomain.Triangle.class) {
			ParticleDomain.Triangle pd = (ParticleDomain.Triangle) domain;
			Element e = parent.addElement("Triangle");
			writeVector3(e, "p1", pd.getP1());
			writeVector3(e, "p2", pd.getP2());
			writeVector3(e, "p3", pd.getP3());
		} else if (domain.getClass() == ParticleDomain.Plane.class) {
			ParticleDomain.Plane pd = (ParticleDomain.Plane) domain;
			Element e = parent.addElement("Plane");
			writeVector3(e, "point", pd.getPoint());
			writeVector3(e, "normal", pd.getNormal());
		} else if (domain.getClass() == ParticleDomain.Rect.class) {
			ParticleDomain.Rect pd = (ParticleDomain.Rect) domain;
			Element e = parent.addElement("Rect");
			writeVector3(e, "point", pd.getPoint());
			writeVector3(e, "u-dir", pd.getUDir());
			writeVector3(e, "v-dir", pd.getVDir());
		} else if (domain.getClass() == ParticleDomain.Box.class) {
			ParticleDomain.Box pd = (ParticleDomain.Box) domain;
			Element e = parent.addElement("Box");
			writeVector3(e, "p1", pd.getP1());
			writeVector3(e, "p2", pd.getP2());
		} else if (domain.getClass() == ParticleDomain.Sphere.class) {
			ParticleDomain.Sphere pd = (ParticleDomain.Sphere) domain;
			Element e = parent.addElement("Sphere");
			writeVector3(e, "center", pd.getCenter());
			addDoubleAttribute(e, "radius", pd.getRadius());
			addDoubleAttribute(e, "radius-inner", pd.getRadiusInner());
		} else if (domain.getClass() == ParticleDomain.Cylinder.class) {
			ParticleDomain.Cylinder pd = (ParticleDomain.Cylinder) domain;
			Element e = parent.addElement("Cylinder");
			writeVector3(e, "p1", pd.getP1());
			writeVector3(e, "p2", pd.getP2());
			addDoubleAttribute(e, "radius", pd.getRadius());
			addDoubleAttribute(e, "radius-inner", pd.getRadiusInner());
		} else if (domain.getClass() == ParticleDomain.Cone.class) {
			ParticleDomain.Cone pd = (ParticleDomain.Cone) domain;
			Element e = parent.addElement("Cone");
			writeVector3(e, "base-center", pd.getBaseCenter());
			writeVector3(e, "apex", pd.getApex());
			addDoubleAttribute(e, "radius", pd.getRadius());
			addDoubleAttribute(e, "radius-inner", pd.getRadiusInner());
		} else if (domain.getClass() == ParticleDomain.Blob.class) {
			ParticleDomain.Blob pd = (ParticleDomain.Blob) domain;
			Element e = parent.addElement("Blob");
			writeVector3(e, "center", pd.getCenter());
			addDoubleAttribute(e, "stdev", pd.getStdev());
		} else if (domain.getClass() == ParticleDomain.Disc.class) {
			ParticleDomain.Disc pd = (ParticleDomain.Disc) domain;
			Element e = parent.addElement("Disc");
			writeVector3(e, "center", pd.getCenter());
			writeVector3(e, "normal", pd.getNormal());
			addDoubleAttribute(e, "radius", pd.getRadius());
			addDoubleAttribute(e, "radius-inner", pd.getRadiusInner());
		} else
			throw new RuntimeException();
	}

	/*
	 * EVENT------------------------------------------------------------------------------------------------
	 */
	private void writeEvent(Element root, Event event) {
		Element element = root.addElement("EventTrigger");
		addBooleanAttribute(element, "enabled", event.isEnabled());
		element.addAttribute("name", event.getName());
		addDoubleAttribute(element, "duration", event.getDuration());
		addBooleanAttribute(element, "remain-enabled", event.isRemainEnabled());
		
		Event.Track track = event.getTrack();
		if (track.getClass() == Event.Track.HeadTrack.class) {
			Event.Track.HeadTrack headTrack = (Event.Track.HeadTrack) track;
			Element headTrackElement = element.addElement("HeadTrack");
			
			Element positionElement = headTrackElement.addElement("Position");
			if (headTrack.getPosition().getClass() == Box.class) {
				writeBox(positionElement, (Box)headTrack.getPosition());
			} else if (headTrack.getPosition().getClass() == Event.Track.HeadTrack.Anywhere.class) {
				positionElement.addElement("Anywhere");
			} else
				throw new RuntimeException();
			
			Element directionElement = headTrackElement.addElement("Direction");
			if (headTrack.getDirection().getClass() == Event.Track.HeadTrack.Direction.None.class) {
				directionElement.addElement("None");
			} else if (headTrack.getDirection().getClass() == Event.Track.HeadTrack.Direction.ObjectTarget.class) {
				directionElement.addElement("ObjectTarget").addAttribute("name", ((Event.Track.HeadTrack.Direction.ObjectTarget)headTrack.getDirection()).getObject().getName());
			} else if (headTrack.getDirection().getClass() == Event.Track.HeadTrack.Direction.DirectionTarget.class) {
				Event.Track.HeadTrack.Direction.DirectionTarget target = (Event.Track.HeadTrack.Direction.DirectionTarget) headTrack.getDirection();
				addDoubleAttribute(directionElement.addElement("DirectionTarget").addAttribute("direction", target.getDirection().toString()), "angle", target.getAngle());
			} else if (headTrack.getDirection().getClass() == Event.Track.HeadTrack.Direction.PointTarget.class) {
				Event.Track.HeadTrack.Direction.PointTarget target = (Event.Track.HeadTrack.Direction.PointTarget) headTrack.getDirection();
				addDoubleAttribute(directionElement.addElement("PointTarget").addAttribute("point", target.getPoint().toString()), "angle", target.getAngle());
    		} else
    			throw new RuntimeException();
			
		} else if (track.getClass() == Event.Track.MoveTrack.class) {
			Event.Track.MoveTrack moveTrack = (Event.Track.MoveTrack) track;
			Element moveTrackElement = element.addElement("MoveTrack");
			
			Element sourceElement = moveTrackElement.addElement("Source");
			if (moveTrack.getSource().getClass() == Event.Track.MoveTrack.Source.ObjectRef.class) {
				sourceElement.addElement("ObjectRef").addAttribute("name", ((Event.Track.MoveTrack.Source.ObjectRef)moveTrack.getSource()).getObject().getName());
			} else if (moveTrack.getSource().getClass() == Event.Track.MoveTrack.Source.GroupObj.class) {
				Element groupElement = sourceElement.addElement("GroupObj");
				Event.Track.MoveTrack.Source.GroupObj group = (Event.Track.MoveTrack.Source.GroupObj) moveTrack.getSource();
				groupElement.addAttribute("name", group.getGroup().getName());
				if (group.getSelection() != null) {
					switch (group.getSelection()) {
					case ALL:
						groupElement.addAttribute("objects", "All Objects");
						break;
					case ANY:
						groupElement.addAttribute("objects", "Any Object");
						break;
					default:
						throw new RuntimeException();
					}
				}
			} else
				throw new RuntimeException();
			
			writeBox(moveTrackElement, moveTrack.getBox());
		} else
    		throw new RuntimeException();
		
		for (StoryAction action : event.getActions()) {
			Element actionElement = element.addElement("Actions");
			writeStoryAction(actionElement, action);
		}
    }

	/*
	 * SOUND------------------------------------------------------------------------------------------------
	 */
	private void writeSound(Element root, Sound sound) {
		Element element = root.addElement("Sound");
		element.addAttribute("name", sound.getName());
		addStringAttributeIfNotNull(element, "filename", sound.getFilename());
		addBooleanAttribute(element, "autostart", sound.isAutostart());
		
		addElementEnum(element, "Mode", sound.getMode());
		
		Element repeatElement = element.addElement("Repeat");
		Sound.Repeat repeat = sound.getRepeat();
		if (repeat.getClass() == Sound.Repeat.Num.class) {
			repeatElement.addElement("RepeatNum").setText(Integer.toString(((Sound.Repeat.Num)repeat).getNum()));
		} else if (repeat.getClass() == Sound.Repeat.Forever.class) {
			repeatElement.addElement("RepeatForever");
		} else if (repeat.getClass() == Sound.Repeat.None.class) {
			repeatElement.addElement("NoRepeat");
		} else
			throw new RuntimeException();
		
		Element settings = element.addElement("Settings");
		addDoubleAttribute(settings, "freq", sound.getFreq());
		addDoubleAttribute(settings, "volume", sound.getVolume());
		addDoubleAttribute(settings, "pan", sound.getPan());
    }

	/*
	 * PLACEMENT--------------------------------------------------------------------------------------------
	 */
	private void writeNamedPlacement(Element root, NamedPlacement namedPlacement) {
		Element element = writePlacement(root, namedPlacement); // most of the work is done
		element.addAttribute("name", namedPlacement.getName());
	}

	/*
	 * TIMELINE---------------------------------------------------------------------------------------------
	 */
	private void writeTimeline(Element root, Timeline timeline) {
		Element element = root.addElement("Timeline");
		element.addAttribute("name", timeline.getName());
		addBooleanAttribute(element, "start-immediately", timeline.isStartImmediately());
		
		for (Timeline.TimedAction action : timeline.getActions()) {
			Element actionElement = element.addElement("TimedActions");
			addDoubleAttribute(actionElement, "seconds-time", action.getSecondsTime());
			writeStoryAction(actionElement, action.getAction());
		}
    }

	/*
	 * GROUP------------------------------------------------------------------------------------------------
	 */
	private void writeGroup(Element root, Group group) {
		Element element = root.addElement("Group");
		element.addAttribute("name", group.getName());
		
		List<? extends Named> members;
		String type;
		
		if (group.getMode() == null) {
			element.setText(""); // always have separate group close tag to better mirror old editor output
			return;
		}
		
		switch (group.getMode()) {
		case OBJECTS:
			members = group.getObjects();
			type = "Objects";
			break;
		case GROUPS:
			members = group.getGroups();
			type = "Groups";
			break;
		default:
			throw new RuntimeException();
		}
		
		for (Named n : members) {
			element.addElement(type).addAttribute("name", n.getName());
		}
    }
	
	/*
	 * OBJECT-----------------------------------------------------------------------------------------------
	 */

	private void writeObject(Element root, StoryObject object) {
		Element element = root.addElement("Object");
		element.addAttribute("name", object.getName());
		addElementBoolean(element, "Visible", object.isVisible());
		element.addElement("Color").setText(object.getColor().toString());
		addElementBoolean(element, "Lighting", object.isLighting());
		addElementBoolean(element, "ClickThrough", object.isClickThrough());
		addElementBoolean(element, "AroundSelfAxis", object.isAroundSelfAxis());
		addElementDouble(element, "Scale", object.getScale());
		if (object.getSound() != null) {
			element.addElement("SoundRef").setText(object.getSound().getName());
		}
		writePlacement(element, object.getPlacement());
		
		Element contentElement = element.addElement("Content");
		StoryObject.Content content = object.getContent();
		if (content.getClass() == StoryObject.Content.Text.class) {
			StoryObject.Content.Text text = (StoryObject.Content.Text) content;
			Element textElement = contentElement.addElement("Text");
			textElement.addAttribute("horiz-align", text.getHorizAlign().toString());
			textElement.addAttribute("vert-align", text.getVertAlign().toString());
			addStringAttributeIfNotNull(textElement, "font", text.getFont());
			addDoubleAttribute(textElement, "depth", text.getDepth());
			textElement.addElement("text").setText(text.getText());
		} else if (content.getClass() == StoryObject.Content.Image.class) {
			addStringAttributeIfNotNull(contentElement.addElement("Image"), "filename", ((StoryObject.Content.Image)content).getFilename());
		} else if (content.getClass() == StoryObject.Content.ParticleSystem.class) {
			StoryObject.Content.ParticleSystem particles = (StoryObject.Content.ParticleSystem) content;
			Element particleElement = contentElement.addElement("ParticleSystem");
			addIntAttribute(particleElement, "max-particles", particles.getMaxParticles());
			addStringAttributeIfNotNull(particleElement, "actions-name", particles.getActions().getName());
			addStringAttributeIfNotNull(particleElement, "particle-group", particles.getParticleGroup().getName());
			addBooleanAttribute(particleElement, "look-at-camera", particles.isLookAtCamera());
			addBooleanAttribute(particleElement, "sequential", particles.isSequential());
			addDoubleAttribute(particleElement, "speed", particles.getSpeed());
		} else if (content.getClass() == StoryObject.Content.Light.class) {
			StoryObject.Content.Light light = (StoryObject.Content.Light) content;
			Element lightElement = contentElement.addElement("Light");
			addBooleanAttribute(lightElement, "diffuse", light.isDiffuse());
			addBooleanAttribute(lightElement, "specular", light.isSpecular());
			addDoubleAttribute(lightElement, "const_atten", light.getConstAtten());
			addDoubleAttribute(lightElement, "lin_atten", light.getLinAtten());
			addDoubleAttribute(lightElement, "quad_atten", light.getQuadAtten());
			
			StoryObject.Content.Light.Type type = light.getType();
			if (type.getClass() == StoryObject.Content.Light.Type.Directional.class) {
				lightElement.addElement("Directional");
			} else if (type.getClass() == StoryObject.Content.Light.Type.Point.class) {
				lightElement.addElement("Point");
			} else if (type.getClass() == StoryObject.Content.Light.Type.Spot.class) {
				addDoubleAttribute(lightElement.addElement("spot"), "angle", ((StoryObject.Content.Light.Type.Spot)type).getAngle());
			} else
				throw new RuntimeException();
		} else if (content.getClass() == StoryObject.Content.StereoImage.class) {
			StoryObject.Content.StereoImage stereoImage = (StoryObject.Content.StereoImage) content;
			addStringAttributeIfNotNull(addStringAttributeIfNotNull(contentElement.addElement("StereoImage"), "left-image", stereoImage.getLeftImage()),
			                            "right-image", stereoImage.getRightImage());
		} else if (content.getClass() == StoryObject.Content.Model.class) {
			StoryObject.Content.Model model = (StoryObject.Content.Model) content;
			addBooleanAttribute(addStringAttributeIfNotNull(contentElement.addElement("Model"), "filename", model.getFilename()), "check-collisions", model.isCheckCollisions()); 
		} else if (content.getClass() == StoryObject.Content.None.class) {
			contentElement.addElement("None");
		} else
			throw new RuntimeException();
		
		StoryObject.Link link = object.getLink();
		if (link != null) {
			Element linkElement = element.addElement("LinkRoot").addElement("Link");
			addElementBoolean(linkElement, "Enabled", link.isEnabled());
			addElementBoolean(linkElement, "RemainEnabled", link.isRemainEnabled());
			linkElement.addElement("EnabledColor").setText(link.getEnabledColor().toString());
			linkElement.addElement("SelectedColor").setText(link.getSelectedColor().toString());
			for (StoryObject.Link.LinkAction action : link.getActions()) {
				Element actionElement = linkElement.addElement("Actions");
				writeStoryAction(actionElement, action.getAction());
				Element clicksElement = actionElement.addElement("Clicks");
				if (action.getClicks().getClass() == StoryObject.Link.LinkAction.Clicks.Any.class) {
					clicksElement.addElement("Any");
				} else if (action.getClicks().getClass() == StoryObject.Link.LinkAction.Clicks.Num.class) {
					StoryObject.Link.LinkAction.Clicks.Num num = (StoryObject.Link.LinkAction.Clicks.Num) action.getClicks();
					Element numElement = clicksElement.addElement("NumClicks");
					addIntAttribute(numElement, "num_clicks", num.getNumClicks());
					addBooleanAttribute(numElement, "reset", num.isReset());
				} else
					throw new RuntimeException();
			}
		}
    }
	
	/*
	 * GLOBAL-----------------------------------------------------------------------------------------------
	 */

	private void writeGlobal(Element root, Global global, List<NamedPlacement> placements) {
		Element element = root.addElement("Global");
		
		writeCamera(element, "CameraPos", global.getDesktopCamera());
		writeCamera(element, "CaveCameraPos", global.getCaveCamera());
		
		element.addElement("Background").addAttribute("color", global.getBackgroundColor().toString());
		
		element.addElement("WandNavigation").addAttribute("allow-rotation", Boolean.toString(global.isAllowWandRotation())).addAttribute("allow-movement", Boolean.toString(global.isAllowWandMovement()));
	}
	
	private void writeCamera(Element root, String elementName, Camera camera) {
		Element element = root.addElement(elementName);
		
		writePlacement(element, camera.getPlacement());
		addDoubleAttribute(element, "far-clip", camera.getFarClip());
	}
	
	/*
	 * STORYACTION------------------------------------------------------------------------------------------
	 */
	
	private void writeStoryAction(Element actionElement, StoryAction storyAction) {
		if (storyAction.getClass() == StoryAction.MoveCave.class) {
			StoryAction.MoveCave moveCave = (StoryAction.MoveCave) storyAction;
			Element moveCaveElement = actionElement.addElement("MoveCave");
			addDoubleAttribute(moveCaveElement, "duration", moveCave.getDuration());
			moveCaveElement.addElement(moveCave.getType().name());
			writePlacement(moveCaveElement, moveCave.getPlacement());
		} else if (storyAction.getClass() == StoryAction.EventChange.class) {
			StoryAction.EventChange eventChange = (StoryAction.EventChange) storyAction;
			Element eventChangeElement = actionElement.addElement("Event");
			addBooleanAttribute(eventChangeElement, "enable", eventChange.isEnable());
			eventChangeElement.addAttribute("name", eventChange.getEvent().getName());
		} else if (storyAction.getClass() == StoryAction.GroupChange.class) {
			StoryAction.GroupChange groupChange = (StoryAction.GroupChange) storyAction;
			Element groupChangeElement = actionElement.addElement("GroupRef");
			groupChangeElement.addAttribute("name", groupChange.getGroup().getName());
			if (groupChange.getRandom() != null) {
				switch (groupChange.getRandom()) {
				case SELECT_ONE_RANDOMLY:
					groupChangeElement.addAttribute("random", "Select One Randomly");
					break;
				default:
					throw new RuntimeException();
				}
			}
			writeTransitionAction(groupChangeElement, groupChange.getAction());
		} else if (storyAction.getClass() == StoryAction.ObjectChange.class) {
			StoryAction.ObjectChange objectChange = (StoryAction.ObjectChange) storyAction;
			Element objectChangeElement = actionElement.addElement("ObjectChange");
			objectChangeElement.addAttribute("name", objectChange.getObject().getName());
			writeTransitionAction(objectChangeElement, objectChange.getAction());
		} else if (storyAction.getClass() == StoryAction.SoundRef.class) {
			actionElement.addElement("SoundRef").addAttribute("name", ((StoryAction.SoundRef) storyAction).getSound().getName());
		} else if (storyAction.getClass() == StoryAction.TimelineChange.class) {
			StoryAction.TimelineChange timelineChange = (StoryAction.TimelineChange) storyAction;
			Element timelineChangeElement = actionElement.addElement("TimerChange");
			timelineChangeElement.addAttribute("name", timelineChange.getTimeline().getName());
			switch (timelineChange.getChange()) {
			case START:
				timelineChangeElement.addElement("start");
				break;
			case STOP:
				timelineChangeElement.addElement("stop");
				break;
			case CONTINUE:
				timelineChangeElement.addElement("continue");
				break;
			case START_IF_NOT_STARTED:
				timelineChangeElement.addElement("start_if_not_started");
				break;
			default:
				throw new RuntimeException();
			}
		} else if (storyAction.getClass() == StoryAction.Restart.class) {
			actionElement.addElement("Restart");
		} else
			throw new RuntimeException();
	}
	
	private void writeTransitionAction(Element parent, TransitionAction action) {
		Element transitionElement = parent.addElement("Transition");
		addDoubleAttribute(transitionElement, "duration", action.getDuration());
		
		if (action.getClass() == TransitionAction.Color.class) {
			transitionElement.addElement("Color").setText(((TransitionAction.Color)action).getColor().toString());
		} else if (action.getClass() == TransitionAction.LinkChange.class) {
			addElementEnum(transitionElement, "LinkChange", ((TransitionAction.LinkChange) action).getAction());
		} else if (action.getClass() == TransitionAction.Scale.class) {
			addElementDouble(transitionElement, "Scale", ((TransitionAction.Scale) action).getScale());
		} else if (action.getClass() == TransitionAction.Sound.class) {
			Element soundElement = transitionElement.addElement("Sound");
			TransitionAction.Sound sound = (TransitionAction.Sound) action;
			if (sound.getAction() != null) {
				switch (sound.getAction()) {
    			case PLAY:
    				soundElement.addAttribute("action", "Play Sound");
    				break;
    			case STOP:
    				soundElement.addAttribute("action", "Stop Sound");
    				break;
    			default:
    				throw new RuntimeException();
    			}
			}
		} else if (action.getClass() == TransitionAction.Movement.class) {
			writePlacement(transitionElement.addElement("Movement"), ((TransitionAction.Movement)action).getPlacement());
		} else if (action.getClass() == TransitionAction.Visible.class) {
			addElementBoolean(transitionElement, "Visible", ((TransitionAction.Visible)action).isVisible());
		} else if (action.getClass() == TransitionAction.MoveRel.class) {
			writePlacement(transitionElement.addElement("MoveRel"), ((TransitionAction.MoveRel)action).getPlacement());
		} else
			throw new RuntimeException();
	}
	
	/*
	 * HELPER-----------------------------------------------------------------------------------------------
	 */
	
	private void writeVector3(Element base, String attributeName, Vector3 vector) {
		base.addAttribute(attributeName, vector.toString());
	}
	
	private void writeBox(Element root, Box box) {
		Element element = root.addElement("Box");
		addBooleanAttribute(element, "ignore-Y", box.isIgnoreY());
		element.addAttribute("corner1", box.getCorner1().toString());
		element.addAttribute("corner2", box.getCorner2().toString());
		addElementEnum(element, "Movement", box.getMovement());
	}
	
	/**
	 * Writes a placement as a subelement of <code>root</code>.
	 * @param root       the element to which to add the &lt;Placement&gt; element.
	 * @param placement  the placement to write out
	 * @return the element representing the placement
	 */
	private Element writePlacement(Element root, Placement placement) {
		Element element = root.addElement("Placement");
		
		element.addElement("RelativeTo").setText(placement.getRelativeTo().getName());
		element.addElement("Position").setText(placement.getPosition().toString());
		
		Rotation rotation = placement.getRotation();
		
		if (rotation != null) {
			if (rotation.getClass() == Rotation.Axis.class) {
				Rotation.Axis axis = (Rotation.Axis) rotation;
				Element rotationElement = element.addElement("Axis");
				
				rotationElement.addAttribute("rotation", axis.getRotation().toString());
				addDoubleAttribute(rotationElement, "angle", axis.getAngle());
				
			} else if (rotation.getClass() == Rotation.LookAt.class) {
				Rotation.LookAt lookAt = (Rotation.LookAt) rotation;
				Element rotationElement = element.addElement("LookAt");
				
				rotationElement.addAttribute("target", lookAt.getTarget().toString());
				rotationElement.addAttribute("up", lookAt.getUp().toString());
				
			} else if (rotation.getClass() == Rotation.Normal.class) {
				Rotation.Normal normal = (Rotation.Normal) rotation;
				Element rotationElement = element.addElement("Normal");
				
				rotationElement.addAttribute("normal", normal.getNormal().toString());
				addDoubleAttribute(rotationElement, "angle", normal.getAngle());
			} else
				throw new RuntimeException();
		}
		
		return element;
	}
}
