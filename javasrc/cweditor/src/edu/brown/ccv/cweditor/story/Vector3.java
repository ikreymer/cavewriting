package edu.brown.ccv.cweditor.story;

public class Vector3 {
	double x, y, z;
	
	public Vector3(double x, double y, double z) {
		set(x, y, z);
	}
	
	public void set(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public double getX() {
    	return x;
    }

	public void setX(double x) {
    	this.x = x;
    }

	public double getY() {
    	return y;
    }

	public void setY(double y) {
    	this.y = y;
    }

	public double getZ() {
    	return z;
    }

	public void setZ(double z) {
    	this.z = z;
    }
	
	@Override
	public String toString() {
		// !! XML writing uses this for the moment, so change with caution
		return "(" + Double.toString(x) + ", " + Double.toString(y) + ", " + Double.toString(z) + ")";
		// String.format is cleaner, but Double.toString appears to have behavior closer to that of the old editor
//		return String.format("(%f, %f, %f)", x, y, z);
	}
}
