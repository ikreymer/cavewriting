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
		return new StoryReader().instanceReadStory(in);
	}
	
	List<NamedPlacement> placements = new ArrayList<NamedPlacement>();
	List<StoryObject> objects = new ArrayList<StoryObject>();
	List<Group> groups = new ArrayList<Group>();
	List<Sound> sounds = new ArrayList<Sound>();
	List<Timeline> timelines = new ArrayList<Timeline>();
	List<Event> events = new ArrayList<Event>();
	List<ParticleAction> particleActions = new ArrayList<ParticleAction>();
	
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
		
		List<Element> particleActionElements = new ArrayList<Element>();
		if ((tempElement = root.element("ParticleActionRoot")) != null) {
			for (@SuppressWarnings("unchecked") Iterator<Element> it = tempElement.elementIterator("ParticleActionList"); it.hasNext();) {
				Element element = it.next();
				particleActions.add(new ParticleAction(getStringAttribute(element, "name")));
				particleActionElements.add(element);
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
		
		for (int i=0, len=particleActionElements.size(); i < len; ++i) {
			readParticleAction(particleActions.get(i), particleActionElements.get(i));
		}
		
		Attribute lastXPath = root.attribute("last_xpath");
		return new Story(objects, groups, timelines, placements, sounds, events, particleActions, globals, root.attributeValue("version"), lastXPath == null ? null : lastXPath.getValue());
	}
	
	/*
	 * -----------------------------------------------------------------------------------------------------
	 */
	
	/*
	 * PARTICLEACTION---------------------------------------------------------------------------------------
	 */
	
	private void readParticleAction(ParticleAction dest, Element src) throws XMLParseException {
    }

	/*
	 * EVENT------------------------------------------------------------------------------------------------
	 */
	private void readEvent(Event dest, Element src) throws XMLParseException {
		
    }

	/*
	 * SOUND------------------------------------------------------------------------------------------------
	 */
	private Sound readSound(Element next) throws XMLParseException {
		Element settings = getElement(next, "Settings");
		
		// pseudo-enum parsing is LAME
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
    }

	/*
	 * GROUP------------------------------------------------------------------------------------------------
	 */
	private void readGroup(Group dest, Element src) throws XMLParseException {
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
			                                                 find(particleActions, getStringAttribute(contentElement, "actions-name")),
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
	 * HELPER-----------------------------------------------------------------------------------------------
	 */
	
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
