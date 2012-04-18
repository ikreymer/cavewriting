package edu.brown.ccv.cweditor.xml;

import java.io.*;
import java.util.*;

import org.dom4j.*;
import org.dom4j.io.*;

import edu.brown.ccv.cweditor.story.*;
import static edu.brown.ccv.cweditor.xml.XMLUtilities.*;

public class StoryWriter {
	private StoryWriter() {}
	
	public static void writeStory(OutputStream outputStream, Story story) throws IOException {
		Document doc = DocumentFactory.getInstance().createDocument();
		OutputFormat pretty = OutputFormat.createPrettyPrint();
		
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
		
//		List<Group> groups = new ArrayList<Group>();
//		if ((tempElement = root.element("GroupRoot")) != null) {
//			for (@SuppressWarnings("unchecked") Iterator<Element> it = tempElement.elementIterator("Group"); it.hasNext();) {
//				groups.add(readGroup(it.next()));
//			}
//		}
//		
//		List<Timeline> timelines = new ArrayList<Timeline>();
//		if ((tempElement = root.element("TimelineRoot")) != null) {
//			for (@SuppressWarnings("unchecked") Iterator<Element> it = tempElement.elementIterator("Timeline"); it.hasNext();) {
//				timelines.add(readTimeline(it.next()));
//			}
//		}
		
		List<NamedPlacement> namedPlacements;
		if ((namedPlacements = story.getPlacements()) != null && namedPlacements.size() > 0) {
			Element placementsRoot = root.addElement("PlacementRoot");
    		for (NamedPlacement placement : namedPlacements) {
    			writeNamedPlacement(placementsRoot, placement, story.getPlacements());
    		}
		}
		
//		List<Sound> sounds = new ArrayList<Sound>();
//		if ((tempElement = root.element("SoundRoot")) != null) {
//			for (@SuppressWarnings("unchecked") Iterator<Element> it = tempElement.elementIterator("Sound"); it.hasNext();) {
//				sounds.add(readSound(it.next()));
//			}
//		}
//		
//		List<Event> events = new ArrayList<Event>();
//		if ((tempElement = root.element("EventRoot")) != null) {
//			for (@SuppressWarnings("unchecked") Iterator<Element> it = tempElement.elementIterator("EventTrigger"); it.hasNext();) {
//				events.add(readEvent(it.next()));
//			}
//		}
//		
//		List<ParticleAction> particleActions = new ArrayList<ParticleAction>();
//		if ((tempElement = root.element("ParticleActionRoot")) != null) {
//			for (@SuppressWarnings("unchecked") Iterator<Element> it = tempElement.elementIterator("ParticleActionList"); it.hasNext();) {
//				particleActions.add(readParticleAction(it.next()));
//			}
//		}
		
		writeGlobal(root, story.getGlobals(), story.getPlacements());
		
		doc.setRootElement(root);
		
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
	
	private static void writeParticleAction(Element root, ParticleAction particleAction) {
    }

	/*
	 * EVENT------------------------------------------------------------------------------------------------
	 */
	private static void writeEvent(Element root, Event event) {
    }

	/*
	 * EVENT------------------------------------------------------------------------------------------------
	 */
	private static void writeSound(Element root, Sound sound) {
    }

	/*
	 * PLACEMENT--------------------------------------------------------------------------------------------
	 */
	private static void writeNamedPlacement(Element root, NamedPlacement namedPlacement, List<NamedPlacement> placements) {
		Element element = writePlacement(root, namedPlacement); // most of the work is done
		element.addAttribute("name", namedPlacement.getName());
	}

	/*
	 * TIMELINE---------------------------------------------------------------------------------------------
	 */
	private static void writeTimeline(Element root, Timeline timeline) {
    }

	/*
	 * GROUP------------------------------------------------------------------------------------------------
	 */
	private static void writeGroup(Element root, Group group) {
    }
	
	/*
	 * OBJECT-----------------------------------------------------------------------------------------------
	 */

	private static void writeObject(Element root, StoryObject object) {
    }
	
	/*
	 * GLOBAL-----------------------------------------------------------------------------------------------
	 */

	private static void writeGlobal(Element root, Global global, List<NamedPlacement> placements) {
		Element element = root.addElement("Global");
		
		writeCamera(element, "CameraPos", global.getDesktopCamera());
		writeCamera(element, "CaveCameraPos", global.getCaveCamera());
		
		element.addElement("Background").addAttribute("color", global.getBackgroundColor().toString());
		
		element.addElement("WandNavigation").addAttribute("allow-rotation", Boolean.toString(global.isAllowWandRotation())).addAttribute("allow-movement", Boolean.toString(global.isAllowWandMovement()));
	}
	
	private static void writeCamera(Element root, String elementName, Camera camera) {
		Element element = root.addElement(elementName);
		
		writePlacement(element, camera.getPlacement());
		addDoubleAttribute(element, "far-clip", camera.getFarClip());
	}
	
	/*
	 * HELPER-----------------------------------------------------------------------------------------------
	 */
	
	private static Element writePlacement(Element root, Placement placement) {
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
			}
		}
		
		return element;
	}
}
