package edu.brown.ccv.cweditor.xml;

import java.util.Iterator;

import org.dom4j.Element;

/**
 * Class with helper methods for reading in XML stuff. Should be used by all XML
 * parser classes.
 */
public class XMLUtilities {
	public static Element addStringAttributeIfNotNull(Element element, String attributeName, String value) {
		if (value != null)
			element.addAttribute(attributeName, value);
		
		return element;
	}
	
	public static void addDoubleAttribute(Element element, String attributeName, double value) {
		element.addAttribute(attributeName, Double.toString(value));
	}
	
	public static void addIntAttribute(Element element, String attributeName, int value) {
		element.addAttribute(attributeName, Integer.toString(value));
	}

	public static void addBooleanAttribute(Element element, String attributeName, boolean value) {
	    element.addAttribute(attributeName, Boolean.toString(value));
    }
	
	public static <T extends Enum<T>> void addElementEnum(Element element, String subElementName, T value) {
		element.addElement(subElementName).addElement(value.name());
	}
	
	public static void addElementBoolean(Element element, String subElementName, boolean value) {
		element.addElement(subElementName).setText(Boolean.toString(value));
	}
	
	public static void addElementDouble(Element element, String subElementName, double value) {
		element.addElement(subElementName).setText(Double.toString(value));
	}
	
	// Attribute helper methods
	
	public static String getStringAttribute(Element element, String attributeName) throws XMLParseException {
		String attribute = element.attributeValue(attributeName);
		if (attribute == null)
			throw new XMLParseException("Attribute " + attributeName + " did not exist for element " + element.getQualifiedName());
		return attribute;
	}
	
	public static int getIntAttribute(Element element, String attributeName) throws XMLParseException {
		String attribute = getStringAttribute(element, attributeName);
		int value;
		try {
			value = Integer.parseInt(attribute);
		} catch (NumberFormatException e) {
			throw new XMLParseException("Attribute " + attributeName + " for element " + element.getQualifiedName() + " was not an int");
		}
		return value;
	}
	
	public static double getDoubleAttribute(Element element, String attributeName) throws XMLParseException {
		String attribute = getStringAttribute(element, attributeName);
		double value;
		try {
			value = Double.parseDouble(attribute);
		} catch (NumberFormatException e) {
			throw new XMLParseException("Attribute " + attributeName + " for element " + element.getQualifiedName() + " was not a double");
		}
		return value;
	}
	
	public static boolean getBooleanAttribute(Element element, String attributeName) throws XMLParseException {
		String attribute = getStringAttribute(element, attributeName);
		boolean value = attribute.equalsIgnoreCase("true");
		if (!value && !attribute.equalsIgnoreCase("false")) {
			throw new XMLParseException("Attribute " + attributeName + " for element " + element.getQualifiedName() + " was not \"true\" or \"false\"");
		}
		return value;
	}
	
	public static <T extends Enum<T>> T getEnumAttribute(Element element, String attributeName, Class<T> enumClass) throws XMLParseException {
		String attribute = getStringAttribute(element, attributeName);
		for (T t : enumClass.getEnumConstants()) {
			if (t.name().equalsIgnoreCase(attribute)) {
				return t;
			}
		}
		throw new XMLParseException("Attribute " + attributeName + " for element " + element.getQualifiedName() + " was not a valid "+enumClass.getSimpleName());
	}
	
	// element text -> various primitives
	
	public static int getElementTextInt(Element baseElement, String elementNameToGetTextFrom) throws XMLParseException {
		String elementText = getElementTextString(baseElement, elementNameToGetTextFrom);
		int value;
		try {
			value = Integer.parseInt(elementText);
		} catch (NumberFormatException e) {
			throw new XMLParseException("Sub-element " + elementNameToGetTextFrom + " for element " + baseElement.getQualifiedName() + " was not a boolean");
		}
		return value;
	}
	
	public static boolean getElementTextBoolean(Element baseElement, String elementNameToGetTextFrom) throws XMLParseException {
		String elementText = getElementTextString(baseElement, elementNameToGetTextFrom);
		boolean value;
		try {
			value = Boolean.parseBoolean(elementText);
		} catch (NumberFormatException e) {
			throw new XMLParseException("Sub-element " + elementNameToGetTextFrom + " for element " + baseElement.getQualifiedName() + " was not a boolean");
		}
		return value;
	}
	
	public static double getElementTextDouble(Element baseElement, String elementNameToGetTextFrom) throws XMLParseException {
		String elementText = getElementTextString(baseElement, elementNameToGetTextFrom);
		double value;
		try {
			value = Double.parseDouble(elementText);
		} catch (NumberFormatException e) {
			throw new XMLParseException("Sub-element " + elementNameToGetTextFrom + " for element " + baseElement.getQualifiedName() + " was not a double");
		}
		return value;
	}
	
	public static String getElementTextString(Element baseElement, String elementNameToGetTextFrom) throws XMLParseException {
		Element subElement = baseElement.element(elementNameToGetTextFrom);
		if (subElement == null)
			throw new XMLParseException("Sub-element " + elementNameToGetTextFrom + " for element " + baseElement.getQualifiedName() + " did not exist");
		String text = subElement.getText();
		if (text == null)
			throw new XMLParseException("Sub-element " + elementNameToGetTextFrom + " for element " + baseElement.getQualifiedName() + " did not have text");
		return text;
	}
	
	public static Element getElement(Element baseElement, String subElement) throws XMLParseException {
		Element ret = baseElement.element(subElement);
		if (ret == null)
			throw new XMLParseException(baseElement.getQualifiedName()+" did not have a subelement "+subElement);
		return ret;
	}

	// misc
	
	public static <T extends Enum<T>> T getElementChildEnum(Element baseElement, String subElementName, Class<T> enumClass) throws XMLParseException {
		T[] ts = enumClass.getEnumConstants();

		for (@SuppressWarnings("unchecked") Iterator<Element> iterator = getElement(baseElement, subElementName).elementIterator(); iterator.hasNext();) {
			Element element = iterator.next();
			for (T t : ts) {
				if (t.name().equalsIgnoreCase(element.getName()))
					return t;
			}
		}
		
//		if (!iterator.hasNext())
//			throw new XMLParseException("Empty "+subElementName+"tag within a "+baseElement.getName()+" tag");
//		Element element = iterator.next();
//		if (iterator.hasNext())
//			throw new XMLParseException("Multiple children of a "+subElementName+"tag within a "+baseElement.getName()+" tag");
		
		throw new XMLParseException("Missing or unknown tag for a \""+enumClass.getSimpleName()+"\" in a "+subElementName+" tag within a "+baseElement.getName()+" tag");
	}
}
