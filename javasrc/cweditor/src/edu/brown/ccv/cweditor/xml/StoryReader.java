package edu.brown.ccv.cweditor.xml;

import java.io.InputStream;
import java.util.*;

import org.dom4j.*;
import org.dom4j.io.SAXReader;

import edu.brown.ccv.cweditor.story.*;
import static edu.brown.ccv.cweditor.xml.XMLUtilities.*;

public class StoryReader {
	private StoryReader() {}
	
	public static Story readStory(InputStream in) throws XMLParseException {
		Element root;
		try {
	        root = new SAXReader().read(in).getRootElement();
        } catch (DocumentException e) {
	        throw new XMLParseException("Could not get root element due to DocumentException", e);
        }
		
		if (!root.getName().equals("Story")) {
			throw new XMLParseException("Root element was not a Story");
		}
		
		Global globals = readGlobal(getElement(root, "Global"));
		Element tempElement;
		
		List<StoryObject> objects = new ArrayList<StoryObject>();
		if ((tempElement = root.element("ObjectRoot")) != null) {
			for (@SuppressWarnings("unchecked") Iterator<Element> it = tempElement.elementIterator("Object"); it.hasNext();) {
				objects.add(readObject(it.next()));
			}
		}
		
		List<Group> groups = new ArrayList<Group>();
		if ((tempElement = root.element("GroupRoot")) != null) {
			for (@SuppressWarnings("unchecked") Iterator<Element> it = tempElement.elementIterator("Group"); it.hasNext();) {
				groups.add(readGroup(it.next()));
			}
		}
		
		List<Timeline> timelines = new ArrayList<Timeline>();
		if ((tempElement = root.element("TimelineRoot")) != null) {
			for (@SuppressWarnings("unchecked") Iterator<Element> it = tempElement.elementIterator("Timeline"); it.hasNext();) {
				timelines.add(readTimeline(it.next()));
			}
		}
		
		List<NamedPlacement> placements = new ArrayList<NamedPlacement>();
		if ((tempElement = root.element("PlacementRoot")) != null) {
			for (@SuppressWarnings("unchecked") Iterator<Element> it = tempElement.elementIterator("Placement"); it.hasNext();) {
				placements.add(readNamedPlacement(it.next()));
			}
		}
		
		List<Sound> sounds = new ArrayList<Sound>();
		if ((tempElement = root.element("SoundRoot")) != null) {
			for (@SuppressWarnings("unchecked") Iterator<Element> it = tempElement.elementIterator("Sound"); it.hasNext();) {
				sounds.add(readSound(it.next()));
			}
		}
		
		List<Event> events = new ArrayList<Event>();
		if ((tempElement = root.element("EventRoot")) != null) {
			for (@SuppressWarnings("unchecked") Iterator<Element> it = tempElement.elementIterator("EventTrigger"); it.hasNext();) {
				events.add(readEvent(it.next()));
			}
		}
		
		List<ParticleAction> particleActions = new ArrayList<ParticleAction>();
		if ((tempElement = root.element("ParticleActionRoot")) != null) {
			for (@SuppressWarnings("unchecked") Iterator<Element> it = tempElement.elementIterator("ParticleActionList"); it.hasNext();) {
				particleActions.add(readParticleAction(it.next()));
			}
		}
		
		Attribute lastXPath = root.attribute("last_xpath");
		return new Story(objects, groups, timelines, placements, sounds, events, particleActions, globals, getStringAttribute(root, "version"), lastXPath == null ? null : lastXPath.getValue());
	}
	
	/*
	 * -----------------------------------------------------------------------------------------------------
	 */
	
	/*
	 * PARTICLEACTION---------------------------------------------------------------------------------------
	 */
	
	private static ParticleAction readParticleAction(Element next) throws XMLParseException {
	    return null;
    }

	private static Event readEvent(Element next) throws XMLParseException {
	    return null;
    }

	private static Sound readSound(Element next) throws XMLParseException {
	    return null;
    }

	private static NamedPlacement readNamedPlacement(Element next) throws XMLParseException {
		return null;
	}

	private static Timeline readTimeline(Element next) throws XMLParseException {
	    return null;
    }

	private static Group readGroup(Element next) throws XMLParseException {
	    return null;
    }
	
	/*
	 * OBJECT-----------------------------------------------------------------------------------------------
	 */

	private static StoryObject readObject(Element next) throws XMLParseException {
	    return null;
    }
	
	/*
	 * GLOBAL-----------------------------------------------------------------------------------------------
	 */

	private static Global readGlobal(Element element) throws XMLParseException {
		Camera desktopCamera = readCamera(getElement(element, "CameraPos"));
		Camera caveCamera = readCamera(getElement(element, "CaveCameraPos"));
		
		Color backgroundColor = parseColor(getStringAttribute(getElement(element, "Background"), "color"));
		
		Element wandNavigation = getElement(element, "WandNavigation");
		boolean allowWandRotation = getBooleanAttribute(wandNavigation, "allow-rotation");
		boolean allowWandMovement = getBooleanAttribute(wandNavigation, "allow-movement");
		
		return new Global(desktopCamera, caveCamera, backgroundColor, allowWandRotation, allowWandMovement);
	}
	
	private static Camera readCamera(Element element) throws XMLParseException {
		Placement placement = readPlacement(getElement(element, "Placement"));
		double farClip = getDoubleAttribute(element, "far-clip");
		
		return new Camera(placement, farClip);
	}
	
	/*
	 * HELPER-----------------------------------------------------------------------------------------------
	 */
	
	private static Placement readPlacement(Element next) throws XMLParseException {
		return null;
	}
	
	private static Color parseColor(String text) throws XMLParseException {
		String[] components = text.split(",");
		if (components.length != 3)
			throw new XMLParseException("Color did not have three components");
		
		try {
			int r = Integer.parseInt(components[0]);
			int g = Integer.parseInt(components[1]);
			int b = Integer.parseInt(components[2]);
			
			return new Color(r, g, b);
		} catch (Exception e) {
			throw new XMLParseException("Invalid color component", e);
		}
	}
}
