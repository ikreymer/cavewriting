package edu.brown.ccv.cweditor.xml;

/**
 * Exception to be thrown when an XML parser is unable to parse something for
 * any reason cannot be read in. See the javadocs for java.lang.Exception if you
 * want to know what each constructor does.
 */
@SuppressWarnings("serial")
public class XMLParseException extends Exception {
	public XMLParseException() {}
	
	public XMLParseException(String message) {
		super(message);
	}
	
	public XMLParseException(Throwable cause) {
		super(cause);
	}
	
	public XMLParseException(String message, Throwable cause) {
		super(message, cause);
	}
}
