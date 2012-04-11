package edu.brown.ccv.cweditor.xml;

import org.dom4j.Element;

/**
 * Class with helper methods for reading in XML stuff. Should be used by all XML
 * parser classes.
 */
public class XMLUtilities {
	public static boolean addStringAttributeIfNotNull(Element element, String attributeName, String value) {
		if (value == null)
			return false;
		
		element.addAttribute(attributeName, value);
		return true;
	}
	
	public static void addDoubleAttribute(Element element, String attributeName, double value) {
		element.addAttribute(attributeName, Double.toString(value));
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
			throw new XMLParseException("Attribute " + attributeName + " for element " + element.getQualifiedName() + " was not an int");
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
	
}
