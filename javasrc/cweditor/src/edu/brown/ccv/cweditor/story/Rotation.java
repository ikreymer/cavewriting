package edu.brown.ccv.cweditor.story;

public abstract class Rotation {
	Rotation() {}
	
	public static class Normal extends Rotation {
		Vector3 normal;
		double angle;
		
		public Normal(Vector3 normal, double angle) {
	        this.normal = normal;
	        this.angle = angle;
        }

		public Vector3 getNormal() {
        	return normal;
        }
		
		public void setNormal(Vector3 normal) {
        	this.normal = normal;
        }
		
		public double getAngle() {
        	return angle;
        }
		
		public void setAngle(double angle) {
        	this.angle = angle;
        }
	}
	
	public static class LookAt extends Rotation {
		Vector3 target, up;

		public LookAt(Vector3 target, Vector3 up) {
	        this.target = target;
	        this.up = up;
        }

		public Vector3 getTarget() {
        	return target;
        }

		public void setTarget(Vector3 target) {
        	this.target = target;
        }

		public Vector3 getUp() {
        	return up;
        }

		public void setUp(Vector3 up) {
        	this.up = up;
        }
	}
	
	public static class Axis extends Rotation {
		Vector3 rotation;
		double angle;
		
		public Axis(Vector3 rotation, double angle) {
	        this.rotation = rotation;
	        this.angle = angle;
        }
		
		public Vector3 getRotation() {
        	return rotation;
        }
		
		public void setRotation(Vector3 rotation) {
        	this.rotation = rotation;
        }
		
		public double getAngle() {
        	return angle;
        }
		
		public void setAngle(double angle) {
        	this.angle = angle;
        }
		
	}
}
