package edu.brown.ccv.cweditor.xml;

import java.io.*;
import java.util.*;

import org.dom4j.*;
import org.dom4j.io.*;

import edu.brown.ccv.cweditor.story.*;
import static edu.brown.ccv.cweditor.xml.XMLUtilities.*;

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
		
		List<ParticleAction> particleActions;
		if ((particleActions = story.getParticleActions()) != null && particleActions.size() > 0) {
			Element particleActionsRoot = root.addElement("ParticleActionRoot");
			for (ParticleAction particleAction : particleActions) {
				writeParticleAction(particleActionsRoot, particleAction);
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
	private static void writeNamedPlacement(Element root, NamedPlacement namedPlacement) {
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
		Element element = root.addElement("Object");
		element.addAttribute("name", object.getName());
		addElementBoolean(element, "Visible", object.isVisible());
		element.addElement("Color").setText(object.getColor().toString());
		addElementBoolean(element, "Lighting", object.isLighting());
		addElementBoolean(element, "ClickThrough", object.isClickThrough());
		addElementBoolean(element, "AroundSelfAxis", object.isAroundSelfAxis());
		addElementDouble(element, "Scale", object.getScale());
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
		} else if (content.getClass() == StoryObject.Content.Image.class) {
			addStringAttributeIfNotNull(contentElement.addElement("Image"), "filename", ((StoryObject.Content.Image)content).getFilename());
		} else if (content.getClass() == StoryObject.Content.ParticleSystem.class) {
			StoryObject.Content.ParticleSystem particles = (StoryObject.Content.ParticleSystem) content;
			Element particleElement = contentElement.addElement("ParticleSystem");
//			addIntAttribute("max-particles", particles.getMaxParticles());
		} else if (content.getClass() == StoryObject.Content.Light.class) {
		} else if (content.getClass() == StoryObject.Content.StereoImage.class) {
		} else if (content.getClass() == StoryObject.Content.Model.class) {
		} else if (content.getClass() == StoryObject.Content.None.class) {
			contentElement.addElement("None");
		}
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
	
	/**
	 * Writes a placement as a subelement of <code>root</code>.
	 * @param root       the element to which to add the &lt;Placement&gt; element.
	 * @param placement  the placement to write out
	 * @return the element representing the placement
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
			} else
				throw new RuntimeException();
		}
		
		return element;
	}
}
