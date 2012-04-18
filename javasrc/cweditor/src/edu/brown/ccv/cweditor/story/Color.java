package edu.brown.ccv.cweditor.story;

public class Color {
	private int r, g, b;

	public Color(int r, int g, int b) {
	    super();
	    if (r > 255 || r < 0 || g > 255 || g < 0 || b > 255 || b < 0)
	    	throw new IllegalArgumentException("Color components must be in the range [0, 255]");
	    this.r = r;
	    this.g = g;
	    this.b = b;
    }

	public int getR() {
    	return r;
    }

	public void setR(int r) {
		if (r > 255 || r < 0)
			throw new IllegalArgumentException("Color components must be in the range [0, 255]");
    	this.r = r;
    }

	public int getG() {
    	return g;
    }

	public void setG(int g) {
		if (g > 255 || g < 0)
			throw new IllegalArgumentException("Color components must be in the range [0, 255]");
    	this.g = g;
    }

	public int getB() {
    	return b;
    }

	public void setB(int b) {
		if (b > 255 || b < 0)
			throw new IllegalArgumentException("Color components must be in the range [0, 255]");
    	this.b = b;
    }
	
	@Override
    public String toString() {
		// !! XML writing uses this for the moment, so change with caution
		return String.format("%d, %d, %d", r, g, b);
	}
	
}
