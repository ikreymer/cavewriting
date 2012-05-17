package edu.brown.ccv.cweditor.story;

public class ParticleDomain {
	ParticleDomain() {}
	
	public static final class Point extends ParticleDomain {
		Vector3 point;

		public Point(Vector3 point) {
	        this.point = point;
        }

		public Vector3 getPoint() {
        	return point;
        }

		public void setPoint(Vector3 point) {
        	this.point = point;
        }
	}
	
	public static final class Line extends ParticleDomain {
		Vector3 p1, p2;

		public Line(Vector3 p1, Vector3 p2) {
	        this.p1 = p1;
	        this.p2 = p2;
        }

		public Vector3 getP1() {
        	return p1;
        }

		public void setP1(Vector3 p1) {
        	this.p1 = p1;
        }

		public Vector3 getP2() {
        	return p2;
        }

		public void setP2(Vector3 p2) {
        	this.p2 = p2;
        }
	}
	
	public static final class Triangle extends ParticleDomain {
		Vector3 p1, p2, p3;

		public Triangle(Vector3 p1, Vector3 p2, Vector3 p3) {
	        this.p1 = p1;
	        this.p2 = p2;
	        this.p3 = p3;
        }

		public Vector3 getP1() {
        	return p1;
        }

		public void setP1(Vector3 p1) {
        	this.p1 = p1;
        }

		public Vector3 getP2() {
        	return p2;
        }

		public void setP2(Vector3 p2) {
        	this.p2 = p2;
        }

		public Vector3 getP3() {
        	return p3;
        }

		public void setP3(Vector3 p3) {
        	this.p3 = p3;
        }
	}
	
	public static final class Plane extends ParticleDomain {
		Vector3 point, normal;

		public Plane(Vector3 point, Vector3 normal) {
	        this.point = point;
	        this.normal = normal;
        }

		public Vector3 getPoint() {
        	return point;
        }

		public void setPoint(Vector3 point) {
        	this.point = point;
        }

		public Vector3 getNormal() {
        	return normal;
        }

		public void setNormal(Vector3 normal) {
        	this.normal = normal;
        }
	}
	
	public static final class Rect extends ParticleDomain {
		Vector3 point, uDir, vDir;

		public Rect(Vector3 point, Vector3 uDir, Vector3 vDir) {
	        this.point = point;
	        this.uDir = uDir;
	        this.vDir = vDir;
        }

		public Vector3 getPoint() {
        	return point;
        }

		public void setPoint(Vector3 point) {
        	this.point = point;
        }

		public Vector3 getUDir() {
        	return uDir;
        }

		public void setUDir(Vector3 uDir) {
        	this.uDir = uDir;
        }

		public Vector3 getVDir() {
        	return vDir;
        }

		public void setVDir(Vector3 vDir) {
        	this.vDir = vDir;
        }
	}
	
	public static final class Box extends ParticleDomain {
		Vector3 p1, p2;

		public Box(Vector3 p1, Vector3 p2) {
	        this.p1 = p1;
	        this.p2 = p2;
        }

		public Vector3 getP1() {
        	return p1;
        }

		public void setP1(Vector3 p1) {
        	this.p1 = p1;
        }

		public Vector3 getP2() {
        	return p2;
        }

		public void setP2(Vector3 p2) {
        	this.p2 = p2;
        }
	}
	
	public static final class Sphere extends ParticleDomain {
		Vector3 center;
		double radius, radiusInner;

		public Sphere(Vector3 center, double radius, double radiusInner) {
	        this.center = center;
	        this.radius = radius;
	        this.radiusInner = radiusInner;
        }

		public Vector3 getCenter() {
        	return center;
        }

		public void setCenter(Vector3 center) {
        	this.center = center;
        }

		public double getRadius() {
        	return radius;
        }

		public void setRadius(double radius) {
        	this.radius = radius;
        }

		public double getRadiusInner() {
        	return radiusInner;
        }

		public void setRadiusInner(double radiusInner) {
        	this.radiusInner = radiusInner;
        }
	}
	
	public static final class Cylinder extends ParticleDomain {
		Vector3 p1, p2;
		double radius, radiusInner;

		public Cylinder(Vector3 p1, Vector3 p2, double radius, double radiusInner) {
	        this.p1 = p1;
	        this.p2 = p2;
	        this.radius = radius;
	        this.radiusInner = radiusInner;
        }

		public Vector3 getP1() {
        	return p1;
        }

		public void setP1(Vector3 p1) {
        	this.p1 = p1;
        }

		public Vector3 getP2() {
        	return p2;
        }

		public void setP2(Vector3 p2) {
        	this.p2 = p2;
        }

		public double getRadius() {
        	return radius;
        }

		public void setRadius(double radius) {
        	this.radius = radius;
        }

		public double getRadiusInner() {
        	return radiusInner;
        }

		public void setRadiusInner(double radiusInner) {
        	this.radiusInner = radiusInner;
        }
	}
	
	public static final class Cone extends ParticleDomain {
		Vector3 baseCenter, apex;
		double radius, radiusInner;
		
		public Cone(Vector3 baseCenter, Vector3 apex, double radius, double radiusInner) {
	        this.baseCenter = baseCenter;
	        this.apex = apex;
	        this.radius = radius;
	        this.radiusInner = radiusInner;
        }

		public Vector3 getBaseCenter() {
        	return baseCenter;
        }

		public void setBaseCenter(Vector3 baseCenter) {
        	this.baseCenter = baseCenter;
        }

		public Vector3 getApex() {
        	return apex;
        }

		public void setApex(Vector3 apex) {
        	this.apex = apex;
        }

		public double getRadius() {
        	return radius;
        }

		public void setRadius(double radius) {
        	this.radius = radius;
        }

		public double getRadiusInner() {
        	return radiusInner;
        }

		public void setRadiusInner(double radiusInner) {
        	this.radiusInner = radiusInner;
        }
	}
	
	public static final class Blob extends ParticleDomain {
		Vector3 center;
		double stdev;
		
		public Blob(Vector3 center, double stdev) {
	        this.center = center;
	        this.stdev = stdev;
        }

		public Vector3 getCenter() {
        	return center;
        }

		public void setCenter(Vector3 center) {
        	this.center = center;
        }

		public double getStdev() {
        	return stdev;
        }

		public void setStdev(double stdev) {
        	this.stdev = stdev;
        }
	}
	
	public static final class Disc extends ParticleDomain {
		Vector3 center, normal;
		double radius, radiusInner;

		public Disc(Vector3 center, Vector3 normal, double radius, double radiusInner) {
	        this.center = center;
	        this.normal = normal;
	        this.radius = radius;
	        this.radiusInner = radiusInner;
        }

		public Vector3 getCenter() {
        	return center;
        }

		public void setCenter(Vector3 center) {
        	this.center = center;
        }

		public Vector3 getNormal() {
        	return normal;
        }

		public void setNormal(Vector3 normal) {
        	this.normal = normal;
        }

		public double getRadius() {
        	return radius;
        }

		public void setRadius(double radius) {
        	this.radius = radius;
        }

		public double getRadiusInner() {
        	return radiusInner;
        }

		public void setRadiusInner(double radiusInner) {
        	this.radiusInner = radiusInner;
        }
	}
}
