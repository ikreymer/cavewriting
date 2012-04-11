package edu.brown.ccv.cweditor.xml;

import java.io.*;
import java.util.List;

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
		Element element = writePlacement(root, namedPlacement);
		element.addAttribute("name", namedPlacement.getName());
//		Placement temp = readPlacement(next, placements, true);
//		
//		String name = getStringAttribute(next, "name");
//		
//		NamedPlacement relativeTo = temp.getRelativeTo();
//		NamedPlacement ret = new NamedPlacement(relativeTo, temp.getPosition(), temp.getRotation(), name);
//		
//		if (relativeTo == null) {
//			if (getElementTextString(next, "RelativeTo").equals(name)) {
//				ret.setRelativeTo(ret);
//			} else {
//				throw new XMLParseException("Could not find RelativeTo placement: "+relativeTo);
//			}
//		}
//		
//		return ret;
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
		
		// TODO: FINISH
		
//		Camera desktopCamera = readCamera(getElement(element, "CameraPos"), placements);
//		Camera caveCamera = readCamera(getElement(element, "CaveCameraPos"), placements);
//		
//		Color backgroundColor = parseColor(getStringAttribute(getElement(element, "Background"), "color"));
//		
//		Element wandNavigation = getElement(element, "WandNavigation");
//		boolean allowWandRotation = getBooleanAttribute(wandNavigation, "allow-rotation");
//		boolean allowWandMovement = getBooleanAttribute(wandNavigation, "allow-movement");
//		
//		return new Global(desktopCamera, caveCamera, backgroundColor, allowWandRotation, allowWandMovement);
	}
	
	private static void writeCamera(Element root, String elementName, Camera camera) {
		Element element = root.addElement(elementName);
		
		writePlacement(element, camera.getPlacement());
		addDoubleAttribute(element, "far-clip", camera.getFarClip());
//		Placement placement = readPlacement(getElement(element, "Placement"), placements);
//		double farClip = getDoubleAttribute(element, "far-clip");
//		
//		return new Camera(placement, farClip);
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
				// TODO: FINISH
			} else if (rotation.getClass() == Rotation.LookAt.class) {
				
			} else if (rotation.getClass() == Rotation.Normal.class) {
				
			}
		}
		
		return element;
	}
}
