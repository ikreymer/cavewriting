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
		
		Element tempElement;
		
		List<NamedPlacement> placements = new ArrayList<NamedPlacement>();
		if ((tempElement = root.element("PlacementRoot")) != null) {
			List<Element> deferredProcessing = new ArrayList<Element>();

			// first process nodes that are relative to themselves, add others to deferred processing list
			for (@SuppressWarnings("unchecked") Iterator<Element> it = tempElement.elementIterator("Placement"); it.hasNext();) {
				Element e = it.next();
				if (getStringAttribute(e, "name").equals(getElementTextString(e, "RelativeTo")))
					placements.add(readNamedPlacement(e, placements));
				else
					deferredProcessing.add(e);
			}
			
			for (Element e : deferredProcessing) {
				placements.add(readNamedPlacement(e, placements));
			}
		}
		
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
		
		Global globals = readGlobal(getElement(root, "Global"), placements);
		
		Attribute lastXPath = root.attribute("last_xpath");
		return new Story(objects, groups, timelines, placements, sounds, events, particleActions, globals, root.attributeValue("version"), lastXPath == null ? null : lastXPath.getValue());
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

	/*
	 * EVENT------------------------------------------------------------------------------------------------
	 */
	private static Event readEvent(Element next) throws XMLParseException {
	    return null;
    }

	/*
	 * EVENT------------------------------------------------------------------------------------------------
	 */
	private static Sound readSound(Element next) throws XMLParseException {
	    return null;
    }

	/*
	 * PLACEMENT--------------------------------------------------------------------------------------------
	 */
	private static NamedPlacement readNamedPlacement(Element next, List<NamedPlacement> placements) throws XMLParseException {
		Placement temp = readPlacement(next, placements, true);
		
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
	private static Timeline readTimeline(Element next) throws XMLParseException {
	    return null;
    }

	/*
	 * GROUP------------------------------------------------------------------------------------------------
	 */
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

	private static Global readGlobal(Element element, List<NamedPlacement> placements) throws XMLParseException {
		Camera desktopCamera = readCamera(getElement(element, "CameraPos"), placements);
		Camera caveCamera = readCamera(getElement(element, "CaveCameraPos"), placements);
		
		Color backgroundColor = parseColor(getStringAttribute(getElement(element, "Background"), "color"));
		
		Element wandNavigation = getElement(element, "WandNavigation");
		boolean allowWandRotation = getBooleanAttribute(wandNavigation, "allow-rotation");
		boolean allowWandMovement = getBooleanAttribute(wandNavigation, "allow-movement");
		
		return new Global(desktopCamera, caveCamera, backgroundColor, allowWandRotation, allowWandMovement);
	}
	
	private static Camera readCamera(Element element, List<NamedPlacement> placements) throws XMLParseException {
		Placement placement = readPlacement(getElement(element, "Placement"), placements);
		double farClip = getDoubleAttribute(element, "far-clip");
		
		return new Camera(placement, farClip);
	}
	
	/*
	 * HELPER-----------------------------------------------------------------------------------------------
	 */
	
	private static Placement readPlacement(Element next, List<NamedPlacement> placements) throws XMLParseException {
		return readPlacement(next, placements, false);
	}
	
	private static Placement readPlacement(Element next, List<NamedPlacement> placements, boolean allowLookupFailure) throws XMLParseException {
		String relativeTo = getElementTextString(next, "RelativeTo");
		NamedPlacement relativePlacement = null;
		for (NamedPlacement namedPlacement : placements) {
			if (namedPlacement.getName().equals(relativeTo)) {
				relativePlacement = namedPlacement;
				break;
			}
		}
		if (relativePlacement == null && !allowLookupFailure) {
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
	
	private static Vector3 parseVector3(String text) throws XMLParseException {
		text = text.trim();
		if (text.length() < 1) {
			throw new XMLParseException("Empty text for vector");
		}
		if (text.charAt(0) != '(' || text.charAt(text.length()-1) != ')') {
			throw new XMLParseException("Vector not surrounded in parens");
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
