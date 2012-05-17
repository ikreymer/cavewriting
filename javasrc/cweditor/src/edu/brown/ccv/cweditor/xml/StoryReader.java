package edu.brown.ccv.cweditor.xml;

import java.io.InputStream;
import java.util.*;

import org.dom4j.*;
import org.dom4j.io.SAXReader;

import edu.brown.ccv.cweditor.story.*;
import edu.brown.ccv.cweditor.story.StoryObject.Link;
import static edu.brown.ccv.cweditor.xml.XMLUtilities.*;

public class StoryReader {
	private StoryReader() {}
	
	public static Story readStory(InputStream in) throws XMLParseException {
		return new StoryReader().instanceReadStory(in);
	}
	
	List<NamedPlacement> placements = new ArrayList<NamedPlacement>();
	List<StoryObject> objects = new ArrayList<StoryObject>();
	List<Group> groups = new ArrayList<Group>();
	List<Sound> sounds = new ArrayList<Sound>();
	List<Timeline> timelines = new ArrayList<Timeline>();
	List<Event> events = new ArrayList<Event>();
	List<ParticleActionList> particleActionLists = new ArrayList<ParticleActionList>();
	
	private Story instanceReadStory(InputStream in) throws XMLParseException {
		
		Element root;
		try {
	        root = new SAXReader().read(in).getRootElement();
        } catch (DocumentException e) {
	        throw new XMLParseException("Could not get root element due to DocumentException", e);
        }
		
		if (!root.getName().equals("Story")) {
			throw new XMLParseException("Root element was not a Story");
		}
		
		Element tempElement;
		
		// Lots of things depend on Placements, so probably best to always do these first
		// TODO: create all the placements and then connect them to each other rather than allowLookupFailure hax
		if ((tempElement = root.element("PlacementRoot")) != null) {
			List<Element> deferredProcessing = new ArrayList<Element>();

			// first process nodes that are relative to themselves, add others to deferred processing list
			for (@SuppressWarnings("unchecked") Iterator<Element> it = tempElement.elementIterator("Placement"); it.hasNext();) {
				Element e = it.next();
				if (getStringAttribute(e, "name").equals(getElementTextString(e, "RelativeTo")))
					placements.add(readNamedPlacement(e));
				else
					deferredProcessing.add(e);
			}
			
			for (Element e : deferredProcessing) {
				placements.add(readNamedPlacement(e));
			}
		}
		
		// Globals are easy
		Global globals = readGlobal(getElement(root, "Global"));
		
		// Sounds also don't depend on anything else
		if ((tempElement = root.element("SoundRoot")) != null) {
			for (@SuppressWarnings("unchecked") Iterator<Element> it = tempElement.elementIterator("Sound"); it.hasNext();) {
				sounds.add(readSound(it.next()));
			}
		}
		
		// Past this point lies all the things that can have circular dependencies, so these things must all be created and THEN linked
		
		List<Element> objectElements = new ArrayList<Element>();
		if ((tempElement = root.element("ObjectRoot")) != null) {
			for (@SuppressWarnings("unchecked") Iterator<Element> it = tempElement.elementIterator("Object"); it.hasNext();) {
				Element element = it.next();
				objects.add(new StoryObject(getStringAttribute(element, "name")));
				objectElements.add(element);
			}
		}
		
		List<Element> groupElements = new ArrayList<Element>();
		if ((tempElement = root.element("GroupRoot")) != null) {
			for (@SuppressWarnings("unchecked") Iterator<Element> it = tempElement.elementIterator("Group"); it.hasNext();) {
				Element element = it.next();
				groups.add(new Group(getStringAttribute(element, "name")));
				groupElements.add(element);
			}
		}
		
		List<Element> timelineElements = new ArrayList<Element>();
		if ((tempElement = root.element("TimelineRoot")) != null) {
			for (@SuppressWarnings("unchecked") Iterator<Element> it = tempElement.elementIterator("Timeline"); it.hasNext();) {
				Element element = it.next();
				timelines.add(new Timeline(getStringAttribute(element, "name")));
				timelineElements.add(element);
			}
		}
		
		List<Element> particleActionListElements = new ArrayList<Element>();
		if ((tempElement = root.element("ParticleActionRoot")) != null) {
			for (@SuppressWarnings("unchecked") Iterator<Element> it = tempElement.elementIterator("ParticleActionList"); it.hasNext();) {
				Element element = it.next();
				particleActionLists.add(new ParticleActionList(getStringAttribute(element, "name")));
				particleActionListElements.add(element);
			}
		}
		
		List<Element> eventElements = new ArrayList<Element>();
		if ((tempElement = root.element("EventRoot")) != null) {
			for (@SuppressWarnings("unchecked") Iterator<Element> it = tempElement.elementIterator("EventTrigger"); it.hasNext();) {
				Element element = it.next();
				events.add(new Event(getStringAttribute(element, "name")));
				eventElements.add(element);
			}
		}
		
		// now that the objects have been created with their names, we can actually flesh them out

		for (int i=0, len=eventElements.size(); i < len; ++i) {
			readEvent(events.get(i), eventElements.get(i));
		}
		
		for (int i=0, len=objectElements.size(); i < len; ++i) {
			readObject(objects.get(i), objectElements.get(i));
		}
		
		for (int i=0, len=groupElements.size(); i < len; ++i) {
			readGroup(groups.get(i), groupElements.get(i));
		}
		
		for (int i=0, len=timelineElements.size(); i < len; ++i) {
			readTimeline(timelines.get(i), timelineElements.get(i));
		}
		
		for (int i=0, len=particleActionListElements.size(); i < len; ++i) {
			readParticleActionList(particleActionLists.get(i), particleActionListElements.get(i));
		}
		
		Attribute lastXPath = root.attribute("last_xpath");
		return new Story(objects, groups, timelines, placements, sounds, events, particleActionLists, globals, root.attributeValue("version"), lastXPath == null ? null : lastXPath.getValue());
	}
	
	/*
	 * -----------------------------------------------------------------------------------------------------
	 */
	
	/*
	 * PARTICLEACTION---------------------------------------------------------------------------------------
	 */
	
	private void readParticleActionList(ParticleActionList dest, Element src) throws XMLParseException {
		Element sourceElement = getElement(src, "Source");
		dest.setSourceRate(getDoubleAttribute(sourceElement, "rate"));
		dest.setSourceDomain(readParticleDomain(sourceElement));
		dest.setVelDomain(readParticleDomain(getElement(src, "Vel")));
		
		for (@SuppressWarnings("unchecked") Iterator<Element> it = src.elementIterator("ParticleAction"); it.hasNext();) {
			dest.getActions().add(readParticleAction(it.next()));
		}

		Element removeConditionSource = getElement(src, "RemoveCondition");
		ParticleActionList.RemoveCondition condition;
		Element removeConditionElement;
		if ((removeConditionElement = removeConditionSource.element("Age")) != null) {
			condition = new ParticleActionList.RemoveCondition.Age(getDoubleAttribute(removeConditionElement, "age"), getBooleanAttribute(removeConditionElement, "younger-than"));
		} else if ((removeConditionElement = removeConditionSource.element("Position")) != null) {
			condition = new ParticleActionList.RemoveCondition.Position(getBooleanAttribute(removeConditionElement, "inside"), readParticleDomain(removeConditionElement));
		} else if ((removeConditionElement = removeConditionSource.element("Velocity")) != null) {
			condition = new ParticleActionList.RemoveCondition.Velocity(getBooleanAttribute(removeConditionElement, "inside"), readParticleDomain(removeConditionElement));
		} else
			throw new XMLParseException("Missing or unknown remove condition for particle action list");
		
		dest.setRemoveCondition(condition);
    }
	
	private ParticleAction readParticleAction(Element actionElement) throws XMLParseException {
		Element particleElement;
		if ((particleElement = actionElement.element("Gravity")) != null) {
			return new ParticleAction.Gravity(readVector3(particleElement, "direction"));
		} else if ((particleElement = actionElement.element("Damping")) != null) {
			return new ParticleAction.Damping(readVector3(particleElement, "direction"), getDoubleAttribute(particleElement, "vel_low"), getDoubleAttribute(particleElement, "vel_high"));
		} else if ((particleElement = actionElement.element("Gravitate")) != null) {
			return new ParticleAction.Gravitate(getDoubleAttribute(particleElement, "magnitude"), getDoubleAttribute(particleElement, "epsilon"), getDoubleAttribute(particleElement, "max_radius"));
		} else if ((particleElement = actionElement.element("Follow")) != null) {
			return new ParticleAction.Follow(getDoubleAttribute(particleElement, "magnitude"), getDoubleAttribute(particleElement, "epsilon"), getDoubleAttribute(particleElement, "max_radius"));
		} else if ((particleElement = actionElement.element("MatchVel")) != null) {
			return new ParticleAction.MatchVel(getDoubleAttribute(particleElement, "magnitude"), getDoubleAttribute(particleElement, "epsilon"), getDoubleAttribute(particleElement, "max_radius"));
		} else if ((particleElement = actionElement.element("OrbitPoint")) != null) {
			return new ParticleAction.OrbitPoint(readVector3(particleElement, "center"),
			                                     getDoubleAttribute(particleElement, "magnitude"),
			                                     getDoubleAttribute(particleElement, "epsilon"),
			                                     getDoubleAttribute(particleElement, "max_radius"));
		} else if ((particleElement = actionElement.element("Jet")) != null) {
			return new ParticleAction.Jet(readParticleDomain(particleElement), readParticleDomain(particleElement, "AccelDomain"));
		} else if ((particleElement = actionElement.element("Bounce")) != null) {
			return new ParticleAction.Bounce(getDoubleAttribute(particleElement, "friction"),
			                                 getDoubleAttribute(particleElement, "resilience"),
			                                 getDoubleAttribute(particleElement, "cutoff"),
			                                 readParticleDomain(particleElement));
		} else if ((particleElement = actionElement.element("Avoid")) != null) {
			return new ParticleAction.Avoid(getDoubleAttribute(particleElement, "magnitude"),
			                                getDoubleAttribute(particleElement, "epsilon"),
			                                getDoubleAttribute(particleElement, "lookahead"),
			                                readParticleDomain(particleElement));
		} else
			throw new XMLParseException("Missing or unknown particle action");
	}
	
	private ParticleDomain readParticleDomain(Element parent) throws XMLParseException {
		return readParticleDomain(parent, "ParticleDomain");
	}
	
	private ParticleDomain readParticleDomain(Element parent, String subElementName) throws XMLParseException {
		parent = getElement(parent, subElementName);
		Element domainElement;
		if ((domainElement = parent.element("Point")) != null) {
			return new ParticleDomain.Point(readVector3(domainElement, "point"));
		} else if ((domainElement = parent.element("Line")) != null) {
			return new ParticleDomain.Line(readVector3(domainElement, "p1"), readVector3(domainElement, "p2"));
		} else if ((domainElement = parent.element("Triangle")) != null) {
			return new ParticleDomain.Triangle(readVector3(domainElement, "p1"), readVector3(domainElement, "p2"), readVector3(domainElement, "p3"));
		} else if ((domainElement = parent.element("Plane")) != null) {
			return new ParticleDomain.Plane(readVector3(domainElement, "point"), readVector3(domainElement, "normal"));
		} else if ((domainElement = parent.element("Rect")) != null) {
			return new ParticleDomain.Rect(readVector3(domainElement, "point"), readVector3(domainElement, "u-dir"), readVector3(domainElement, "v-dir"));
		} else if ((domainElement = parent.element("Box")) != null) {
			return new ParticleDomain.Box(readVector3(domainElement, "p1"), readVector3(domainElement, "p2"));
		} else if ((domainElement = parent.element("Sphere")) != null) {
			return new ParticleDomain.Sphere(readVector3(domainElement, "center"), getDoubleAttribute(domainElement, "radius"), getDoubleAttribute(domainElement, "radius-inner"));
		} else if ((domainElement = parent.element("Cylinder")) != null) {
			return new ParticleDomain.Cylinder(readVector3(domainElement, "p1"), readVector3(domainElement, "p2"), getDoubleAttribute(domainElement, "radius"), getDoubleAttribute(domainElement, "radius-inner"));
		} else if ((domainElement = parent.element("Cone")) != null) {
			return new ParticleDomain.Cylinder(readVector3(domainElement, "base-center"), readVector3(domainElement, "apex"), getDoubleAttribute(domainElement, "radius"), getDoubleAttribute(domainElement, "radius-inner"));
		} else if ((domainElement = parent.element("Blob")) != null) {
			return new ParticleDomain.Blob(readVector3(domainElement, "center"), getDoubleAttribute(domainElement, "stdev"));
		} else if ((domainElement = parent.element("Disc")) != null) {
			return new ParticleDomain.Disc(readVector3(domainElement, "center"), readVector3(domainElement, "normal"), getDoubleAttribute(domainElement, "radius"), getDoubleAttribute(domainElement, "radius-inner"));
		} else
			throw new XMLParseException("Missing or unknown particle domain type");
	}

	/*
	 * EVENT------------------------------------------------------------------------------------------------
	 */
	private void readEvent(Event dest, Element src) throws XMLParseException {
		dest.setEnabled(getBooleanAttribute(src, "enabled"));
		dest.setDuration(getDoubleAttribute(src, "duration"));
		dest.setRemainEnabled(getBooleanAttribute(src, "remain-enabled"));
		
		Element trackElement;
		if ((trackElement = src.element("HeadTrack")) != null) {
			Element positionSource = getElement(trackElement, "Position");
			Event.Track.HeadTrack.Position position;
			
			@SuppressWarnings("unused")
            Element positionElement;
			if ((positionElement = positionSource.element("Anywhere")) != null) {
				position = Event.Track.HeadTrack.ANYWHERE;
			} else if ((positionElement = positionSource.element("Box")) != null) {
				position = readBox(positionSource);
			} else
				throw new XMLParseException("Missing or unknown position for HeadTrack");
			
			Element directionSource = getElement(trackElement, "Direction");
			Event.Track.HeadTrack.Direction direction;
			Element directionElement;
			if ((directionElement = directionSource.element("None")) != null) {
				direction = Event.Track.HeadTrack.Direction.NONE;
			} else if ((directionElement = directionSource.element("ObjectTarget")) != null) {
				direction = new Event.Track.HeadTrack.Direction.ObjectTarget(find(objects, getStringAttribute(directionElement, "name")));
			} else if ((directionElement = directionSource.element("DirectionTarget")) != null) {
				direction = new Event.Track.HeadTrack.Direction.DirectionTarget(parseVector3(getStringAttribute(directionElement, "direction")), getDoubleAttribute(directionElement, "angle"));
			} else if ((directionElement = directionSource.element("PointTarget")) != null) {
				direction = new Event.Track.HeadTrack.Direction.PointTarget(parseVector3(getStringAttribute(directionElement, "point")), getDoubleAttribute(directionElement, "angle"));
			} else
				throw new XMLParseException("Missing or unknown direction for HeadTrack");
			
			dest.setTrack(new Event.Track.HeadTrack(position, direction));
		} else if ((trackElement = src.element("MoveTrack")) != null) {
			Element sourceSource = getElement(trackElement, "Source");
			Event.Track.MoveTrack.Source source;
			Element sourceElement;
			
			if ((sourceElement = sourceSource.element("ObjectRef")) != null) {
				source = new Event.Track.MoveTrack.Source.ObjectRef(find(objects, getStringAttribute(sourceElement, "name")));
			} else if ((sourceElement = sourceSource.element("GroupObj")) != null) {
				Event.Track.MoveTrack.Source.GroupObj.Selection selection = null;
				
				String selectionString = sourceElement.attributeValue("objects");
				if (selectionString != null) {
					if (selectionString.equals("Any Object")) {
						selection = Event.Track.MoveTrack.Source.GroupObj.Selection.ANY;
					} else if (selectionString.equals("All Objects")) {
						selection = Event.Track.MoveTrack.Source.GroupObj.Selection.ALL;
					} else
						throw new XMLParseException("Invalid selection type for MoveTrack source");
				}
				
				source = new Event.Track.MoveTrack.Source.GroupObj(find(groups, getStringAttribute(sourceElement, "name")), selection);
			} else
				throw new XMLParseException("Missing or unknown source for MoveTrack");
			
			dest.setTrack(new Event.Track.MoveTrack(readBox(trackElement), source));
		} else
			throw new XMLParseException("Missing or unknown track type for event");
		
		for (@SuppressWarnings("unchecked") Iterator<Element> it = src.elementIterator("Actions"); it.hasNext();) {
			dest.getActions().add(readStoryAction(it.next()));
		}
    }

	/*
	 * SOUND------------------------------------------------------------------------------------------------
	 */
	private Sound readSound(Element next) throws XMLParseException {
		Element settings = getElement(next, "Settings");
		
		Element repeatSource = getElement(next, "Repeat");
		Sound.Repeat repeat;
		
		@SuppressWarnings("unused") // not used now but maybe later, good to keep the assignments in just in case even if it's never read
        Element repeatElement;
		
		if ((repeatElement = repeatSource.element("NoRepeat")) != null) {
			repeat = Sound.Repeat.NONE;
		} else if ((repeatElement = repeatSource.element("RepeatForever")) != null) {
			repeat = Sound.Repeat.FOREVER;
		} else if ((repeatElement = repeatSource.element("RepeatNum")) != null) {
			repeat = new Sound.Repeat.Num(getElementTextInt(repeatSource, "RepeatNum"));
		} else
			throw new XMLParseException("Invalid or missing sound repeat");
		
	    return new Sound(getStringAttribute(next, "name"),
	                     next.attributeValue("filename"),
	                     getBooleanAttribute(next, "autostart"),
	                     getElementChildEnum(next, "Mode", Sound.Mode.class),
	                     repeat,
	                     getDoubleAttribute(settings, "freq"),
	                     getDoubleAttribute(settings, "volume"),
	                     getDoubleAttribute(settings, "pan"));
    }

	/*
	 * PLACEMENT--------------------------------------------------------------------------------------------
	 */
	private NamedPlacement readNamedPlacement(Element next) throws XMLParseException {
		Placement temp = readPlacement(next, true);
		
		String name = getStringAttribute(next, "name");
		
		NamedPlacement relativeTo = temp.getRelativeTo();
		NamedPlacement ret = new NamedPlacement(relativeTo, temp.getPosition(), temp.getRotation(), name);
		
		if (relativeTo == null) {
			if (getElementTextString(next, "RelativeTo").equals(name)) {
				ret.setRelativeTo(ret);
			} else {
				throw new XMLParseException("Could not find RelativeTo placement: "+relativeTo);
			}
		}
		
		return ret;
	}

	/*
	 * TIMELINE---------------------------------------------------------------------------------------------
	 */
	private void readTimeline(Timeline dest, Element src) throws XMLParseException {
		dest.setStartImmediately(getBooleanAttribute(src, "start-immediately"));
		
		for (@SuppressWarnings("unchecked") Iterator<Element> it = src.elementIterator("TimedActions"); it.hasNext();) {
			Element actionElement = it.next();
			dest.getActions().add(new Timeline.TimedAction(getDoubleAttribute(actionElement, "seconds-time"), readStoryAction(actionElement)));
		}
    }

	/*
	 * GROUP------------------------------------------------------------------------------------------------
	 */
	private void readGroup(Group dest, Element src) throws XMLParseException {
		@SuppressWarnings("unchecked") Iterator<Element> objects = src.elementIterator("Objects");
		@SuppressWarnings("unchecked") Iterator<Element> groups = src.elementIterator("Groups");
		
		if (objects.hasNext()) {
			dest.setMode(Group.Mode.OBJECTS);
		} else if (groups.hasNext()) {
			dest.setMode(Group.Mode.GROUPS);
		}
		
		while (objects.hasNext()) {
			dest.getObjects().add(find(this.objects, getStringAttribute(objects.next(), "name")));
		}
		while (groups.hasNext()) {
			dest.getGroups().add(find(this.groups, getStringAttribute(groups.next(), "name")));
		}
    }
	
	/*
	 * OBJECT-----------------------------------------------------------------------------------------------
	 */
	private void readObject(StoryObject dest, Element src) throws XMLParseException {
		// easy things
		dest.setVisible(getElementTextBoolean(src, "Visible"));
		dest.setColor(parseColor(getElementTextString(src, "Color")));
		dest.setLighting(getElementTextBoolean(src, "Lighting"));
		dest.setClickThrough(getElementTextBoolean(src, "ClickThrough"));
		dest.setAroundSelfAxis(getElementTextBoolean(src, "AroundSelfAxis"));
		dest.setScale(getElementTextDouble(src, "Scale"));
		dest.setPlacement(readPlacement(getElement(src, "Placement")));
		
		if (src.element("SoundRef") != null) {
			dest.setSound(find(sounds, getElementTextString(src, "SoundRef")));
		}
		
		// content
		Element contentSource = getElement(src, "Content");
		StoryObject.Content content;
		Element contentElement;
		if ((contentElement = contentSource.element("Text")) != null) {
			content = new StoryObject.Content.Text(getEnumAttribute(contentElement, "horiz-align", StoryObject.Content.Text.HorizontalAlign.class),
			                                       getEnumAttribute(contentElement, "vert-align", StoryObject.Content.Text.VerticalAlign.class),
			                                       contentElement.attributeValue("font"),
			                                       getDoubleAttribute(contentElement, "depth"),
			                                       getElementTextString(contentElement, "text"));
		} else if ((contentElement = contentSource.element("Image")) != null) {
			content = new StoryObject.Content.Image(contentElement.attributeValue("filename"));
		} else if ((contentElement = contentSource.element("ParticleSystem")) != null) {
			content = new StoryObject.Content.ParticleSystem(getIntAttribute(contentElement, "max-particles"),
			                                                 find(particleActionLists, getStringAttribute(contentElement, "actions-name")),
			                                                 find(groups, getStringAttribute(contentElement, "particle-group")),
			                                                 getBooleanAttribute(contentElement, "look-at-camera"),
			                                                 getBooleanAttribute(contentElement, "sequential"),
			                                                 getDoubleAttribute(contentElement, "speed"));
		} else if ((contentElement = contentSource.element("None")) != null) {
			content = StoryObject.Content.NONE;
		} else if ((contentElement = contentSource.element("StereoImage")) != null) {
			content = new StoryObject.Content.StereoImage(contentElement.attributeValue("left-image"), contentElement.attributeValue("right-image"));
		} else if ((contentElement = contentSource.element("Model")) != null) {
			content = new StoryObject.Content.Model(contentElement.attributeValue("filename"), getBooleanAttribute(contentElement, "check-collisions"));
		} else if ((contentElement = contentSource.element("Light")) != null) {
			StoryObject.Content.Light.Type type;
			Element typeElement;
			if ((typeElement = contentElement.element("Directional")) != null) {
				type = StoryObject.Content.Light.Type.DIRECTIONAL;
			} else if ((typeElement = contentElement.element("Point")) != null) {
				type = StoryObject.Content.Light.Type.POINT;
			} else if ((typeElement = contentElement.element("Spot")) != null) {
				type = new StoryObject.Content.Light.Type.Spot(getDoubleAttribute(typeElement, "angle"));
			} else
				throw new XMLParseException("Missing or unknown light type");
			
			content = new StoryObject.Content.Light(getBooleanAttribute(contentElement, "diffuse"),
			                                        getBooleanAttribute(contentElement, "specular"),
			                                        getDoubleAttribute(contentElement, "const_atten"),
			                                        getDoubleAttribute(contentElement, "lin_atten"),
			                                        getDoubleAttribute(contentElement, "quad_atten"),
			                                        type);
		} else
			throw new XMLParseException("Missing or unknown content in a content tag");
		
		dest.setContent(content);
		
		// link
		Element linkElement = src.element("LinkRoot");
		if (linkElement != null) {
			linkElement = linkElement.element("Link");
			if (linkElement == null)
				throw new XMLParseException("LinkRoot did not contain a link");
			
			StoryObject.Link link = new Link(getElementTextBoolean(linkElement, "Enabled"),
			                                 getElementTextBoolean(linkElement, "RemainEnabled"),
			                                 parseColor(getElementTextString(linkElement, "EnabledColor")),
			                                 parseColor(getElementTextString(linkElement, "SelectedColor")));
			
			List<Link.LinkAction> actions = link.getActions();
			
			for (@SuppressWarnings("unchecked") Iterator<Element> it = linkElement.elementIterator("Actions"); it.hasNext();) {
				Element actionElement = it.next();
				
				Element clicksSource = actionElement.element("Clicks");
				StoryObject.Link.LinkAction.Clicks clicks;
				if (clicksSource == null) {
					clicks = StoryObject.Link.LinkAction.Clicks.ANY;
				} else {
    				Element clicksElement;
    				if ((clicksElement = clicksSource.element("Any")) != null) {
    					clicks = StoryObject.Link.LinkAction.Clicks.ANY;
    				} else if ((clicksElement = clicksSource.element("NumClicks")) != null) {
    					clicks = new StoryObject.Link.LinkAction.Clicks.Num(getIntAttribute(clicksElement, "num_clicks"), getBooleanAttribute(clicksElement, "reset"));
    				} else
    					throw new XMLParseException("Missing or unknown Clicks option in a link action");
				}
				
				actions.add(new Link.LinkAction(readStoryAction(actionElement), clicks));
			}
			
			dest.setLink(link);
		}
    }
	
	/*
	 * GLOBAL-----------------------------------------------------------------------------------------------
	 */
	private Global readGlobal(Element element) throws XMLParseException {
		Camera desktopCamera = readCamera(getElement(element, "CameraPos"));
		Camera caveCamera = readCamera(getElement(element, "CaveCameraPos"));
		
		Color backgroundColor = parseColor(getStringAttribute(getElement(element, "Background"), "color"));
		
		Element wandNavigation = getElement(element, "WandNavigation");
		boolean allowWandRotation = getBooleanAttribute(wandNavigation, "allow-rotation");
		boolean allowWandMovement = getBooleanAttribute(wandNavigation, "allow-movement");
		
		return new Global(desktopCamera, caveCamera, backgroundColor, allowWandRotation, allowWandMovement);
	}
	
	private Camera readCamera(Element element) throws XMLParseException {
		Placement placement = readPlacement(getElement(element, "Placement"));
		double farClip = getDoubleAttribute(element, "far-clip");
		
		return new Camera(placement, farClip);
	}
	
	/*
	 * STORYACTION------------------------------------------------------------------------------------------
	 */
	private StoryAction readStoryAction(Element parent) throws XMLParseException {
		Element actionElement;
		if ((actionElement = parent.element("ObjectChange")) != null) {
			return new StoryAction.ObjectChange(find(objects, getStringAttribute(actionElement, "name")), readTransitionAction(actionElement));
		} else if ((actionElement = parent.element("GroupRef")) != null) {
			StoryAction.GroupChange.Random random = null;
			String randomAttribute = actionElement.attributeValue("random");
			if (randomAttribute != null && randomAttribute.equals("Select One Randomly"))
				random = StoryAction.GroupChange.Random.SELECT_ONE_RANDOMLY;
			
			return new StoryAction.GroupChange(find(groups, getStringAttribute(actionElement, "name")), readTransitionAction(actionElement), random);
		} else if ((actionElement = parent.element("TimerChange")) != null) {
			StoryAction.TimelineChange.Change change;
			if (actionElement.element("continue") != null) {
				change = StoryAction.TimelineChange.Change.CONTINUE;
			} else if (actionElement.element("start") != null) {
				change = StoryAction.TimelineChange.Change.START;
			} else if (actionElement.element("start_if_not_started") != null) {
				change = StoryAction.TimelineChange.Change.START_IF_NOT_STARTED;
			} else if (actionElement.element("stop") != null) {
				change = StoryAction.TimelineChange.Change.STOP;
			} else
				throw new XMLParseException("Missing or unknown timeline change type");
			
			return new StoryAction.TimelineChange(change, find(timelines, getStringAttribute(actionElement, "name")));
		} else if ((actionElement = parent.element("SoundRef")) != null) {
			return new StoryAction.SoundRef(find(sounds, getStringAttribute(actionElement, "name")));
		} else if ((actionElement = parent.element("Event")) != null) {
			return new StoryAction.EventChange(find(events, getStringAttribute(actionElement, "name")), getBooleanAttribute(actionElement, "enable"));
		} else if ((actionElement = parent.element("MoveCave")) != null) {
			return new StoryAction.MoveCave(getDoubleAttribute(actionElement, "duration"), getElementChildEnum(parent, "MoveCave", StoryAction.MoveCave.Type.class), readPlacement(getElement(actionElement, "Placement")));
		} else if ((actionElement = parent.element("Restart")) != null) {
			return StoryAction.RESTART;
		} else
			throw new XMLParseException("Missing or unknown story action in "+parent.getName()+" tag");
	}
	
	private TransitionAction readTransitionAction(Element parent) throws XMLParseException {
		Element transitionElement = parent.element("Transition");
		if (transitionElement == null)
			throw new XMLParseException("No Transition element for "+parent.getName()+" action");
		
		double duration = getDoubleAttribute(transitionElement, "duration");
		
		Element actionElement;
		if ((actionElement = transitionElement.element("Visible")) != null) {
			return new TransitionAction.Visible(duration, getElementTextBoolean(transitionElement, "Visible"));
		} else if ((actionElement = transitionElement.element("Movement")) != null) {
			return new TransitionAction.Movement(duration, readPlacement(getElement(actionElement, "Placement")));
		} else if ((actionElement = transitionElement.element("MoveRel")) != null) {
			return new TransitionAction.MoveRel(duration, readPlacement(getElement(actionElement, "Placement")));
		} else if ((actionElement = transitionElement.element("Color")) != null) {
			return new TransitionAction.Color(duration, parseColor(actionElement.getText()));
		} else if ((actionElement = transitionElement.element("Scale")) != null) {
			return new TransitionAction.Scale(duration, getElementTextDouble(transitionElement, "Scale"));
		} else if ((actionElement = transitionElement.element("Sound")) != null) {
			TransitionAction.Sound.Action action = null;
			String actionString = actionElement.attributeValue("action");
			if (actionString != null) {
				if (actionString.equals("Play Sound")) {
					action = TransitionAction.Sound.Action.PLAY;
				} else if (actionString.equals("Stop Sound")) {
					action = TransitionAction.Sound.Action.STOP;
				}
			}
			return new TransitionAction.Sound(duration, action);
		} else if ((actionElement = transitionElement.element("LinkChange")) != null) {
			return new TransitionAction.LinkChange(duration, getElementChildEnum(transitionElement, "LinkChange", TransitionAction.LinkChange.Action.class));
		} else
			throw new XMLParseException("Missing or unknown child of Transition action");
	}
	
	/*
	 * HELPER-----------------------------------------------------------------------------------------------
	 */
	
	private Box readBox(Element parent) throws XMLParseException {
		Element boxElement = getElement(parent, "Box");
		return new Box(getBooleanAttribute(boxElement, "ignore-Y"),
		               parseVector3(getStringAttribute(boxElement, "corner1")),
		               parseVector3(getStringAttribute(boxElement, "corner2")),
		               getElementChildEnum(boxElement, "Movement", Box.Movement.class));
	}
	
	private <T extends Named> T find(Collection<T> list, String name) throws XMLParseException {
		for (T t : list) {
			if (t.getName().equals(name))
				return t;
		}
		throw new XMLParseException("Could not find '"+name+"'");
	}
	
	private Placement readPlacement(Element next) throws XMLParseException {
		return readPlacement(next, false);
	}
	
	private Placement readPlacement(Element next, boolean allowLookupFailure) throws XMLParseException {
		String relativeTo = getElementTextString(next, "RelativeTo");
		NamedPlacement relativePlacement = null;
		for (NamedPlacement namedPlacement : placements) {
			if (namedPlacement.getName().equals(relativeTo)) {
				relativePlacement = namedPlacement;
				break;
			}
		}
		if (relativePlacement == null && !allowLookupFailure) { // allowLookupFailure is a hack to ensure placements referring to themselves is okay
			throw new XMLParseException("Could not find RelativeTo placement: "+relativeTo);
		}
		
		Vector3 position = parseVector3(getElementTextString(next, "Position"));
		
		Rotation rotation = null;
		
		Element rotationElement;
		if ((rotationElement = next.element("Axis")) != null) {
			rotation = new Rotation.Axis(parseVector3(getStringAttribute(rotationElement, "rotation")), getDoubleAttribute(rotationElement, "angle"));
		} else if ((rotationElement = next.element("Normal")) != null) {
			rotation = new Rotation.Normal(parseVector3(getStringAttribute(rotationElement, "normal")), getDoubleAttribute(rotationElement, "angle"));
		} else if ((rotationElement = next.element("LookAt")) != null) {
			rotation = new Rotation.LookAt(parseVector3(getStringAttribute(rotationElement, "target")), parseVector3(getStringAttribute(rotationElement, "up")));
		}
		
		return new Placement(relativePlacement, position, rotation);
	}
	
	private Vector3 readVector3(Element parent, String attributeName) throws XMLParseException {
		return parseVector3(getStringAttribute(parent, attributeName));
	}
	
	private static Vector3 parseVector3(String text) throws XMLParseException {
		text = text.trim();
		if (text.length() < 1) {
			throw new XMLParseException("Empty text for vector");
		}
		if (text.charAt(0) != '(' || text.charAt(text.length()-1) != ')') {
			throw new XMLParseException("Vector not surrounded by parens");
		}
		text = text.substring(1, text.length()-1);
		
		String[] components = text.split(",");
		if (components.length != 3)
			throw new XMLParseException("Vector did not have three components");
		
		try {
			double x = Double.parseDouble(components[0].trim());
			double y = Double.parseDouble(components[1].trim());
			double z = Double.parseDouble(components[2].trim());
			
			return new Vector3(x, y, z);
		} catch (NumberFormatException e) {
			throw new XMLParseException("Invalid vector component");
		}
	}
	
	private static Color parseColor(String text) throws XMLParseException {
		String[] components = text.split(",");
		if (components.length != 3)
			throw new XMLParseException("Color did not have three components");
		
		try {
			int r = Integer.parseInt(components[0].trim());
			int g = Integer.parseInt(components[1].trim());
			int b = Integer.parseInt(components[2].trim());
			
			return new Color(r, g, b);
		} catch (Exception e) {
			throw new XMLParseException("Invalid color component", e);
		}
	}
}
