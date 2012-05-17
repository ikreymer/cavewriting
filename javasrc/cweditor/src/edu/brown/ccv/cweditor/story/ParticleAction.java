package edu.brown.ccv.cweditor.story;

// TODO: Research how RandomVel, RandomAccel, RandomDisplace, TargetColor, TargetSize, and TargetVel work and implement them
public class ParticleAction {
	ParticleAction() {}
	
	public static final class Gravity extends ParticleAction {
		Vector3 direction;

		public Gravity(Vector3 direction) {
	        this.direction = direction;
        }

		public Vector3 getDirection() {
        	return direction;
        }

		public void setDirection(Vector3 direction) {
        	this.direction = direction;
        }
	}
	
	public static final class Damping extends ParticleAction {
		Vector3 direction;
		double velLow, velHigh;

		public Damping(Vector3 direction, double velLow, double velHigh) {
	        this.direction = direction;
	        this.velLow = velLow;
	        this.velHigh = velHigh;
        }

		public Vector3 getDirection() {
        	return direction;
        }

		public void setDirection(Vector3 direction) {
        	this.direction = direction;
        }

		public double getVelLow() {
        	return velLow;
        }

		public void setVelLow(double velLow) {
        	this.velLow = velLow;
        }

		public double getVelHigh() {
        	return velHigh;
        }

		public void setVelHigh(double velHigh) {
        	this.velHigh = velHigh;
        }
	}
	
	public static final class Gravitate extends ParticleAction {
		double magnitude, epsilon, maxRadius;

		public Gravitate(double magnitude, double epsilon, double maxRadius) {
	        this.magnitude = magnitude;
	        this.epsilon = epsilon;
	        this.maxRadius = maxRadius;
        }

		public double getMagnitude() {
        	return magnitude;
        }

		public void setMagnitude(double magnitude) {
        	this.magnitude = magnitude;
        }

		public double getEpsilon() {
        	return epsilon;
        }

		public void setEpsilon(double epsilon) {
        	this.epsilon = epsilon;
        }

		public double getMaxRadius() {
        	return maxRadius;
        }

		public void setMaxRadius(double maxRadius) {
        	this.maxRadius = maxRadius;
        }
	}
	
	public static final class Follow extends ParticleAction {
		double magnitude, epsilon, maxRadius;
		
		public Follow(double magnitude, double epsilon, double maxRadius) {
			this.magnitude = magnitude;
			this.epsilon = epsilon;
			this.maxRadius = maxRadius;
		}
		
		public double getMagnitude() {
			return magnitude;
		}
		
		public void setMagnitude(double magnitude) {
			this.magnitude = magnitude;
		}
		
		public double getEpsilon() {
			return epsilon;
		}
		
		public void setEpsilon(double epsilon) {
			this.epsilon = epsilon;
		}
		
		public double getMaxRadius() {
			return maxRadius;
		}
		
		public void setMaxRadius(double maxRadius) {
			this.maxRadius = maxRadius;
		}
	}
	
	public static final class MatchVel extends ParticleAction {
		double magnitude, epsilon, maxRadius;
		
		public MatchVel(double magnitude, double epsilon, double maxRadius) {
			this.magnitude = magnitude;
			this.epsilon = epsilon;
			this.maxRadius = maxRadius;
		}
		
		public double getMagnitude() {
			return magnitude;
		}
		
		public void setMagnitude(double magnitude) {
			this.magnitude = magnitude;
		}
		
		public double getEpsilon() {
			return epsilon;
		}
		
		public void setEpsilon(double epsilon) {
			this.epsilon = epsilon;
		}
		
		public double getMaxRadius() {
			return maxRadius;
		}
		
		public void setMaxRadius(double maxRadius) {
			this.maxRadius = maxRadius;
		}
	}
	
	public static final class OrbitPoint extends ParticleAction {
		Vector3 center;
		double magnitude, epsilon, maxRadius;
		
		public OrbitPoint(Vector3 center, double magnitude, double epsilon, double maxRadius) {
	        this.center = center;
	        this.magnitude = magnitude;
	        this.epsilon = epsilon;
	        this.maxRadius = maxRadius;
        }

		public Vector3 getCenter() {
        	return center;
        }

		public void setCenter(Vector3 center) {
        	this.center = center;
        }

		public double getMagnitude() {
        	return magnitude;
        }

		public void setMagnitude(double magnitude) {
        	this.magnitude = magnitude;
        }

		public double getEpsilon() {
        	return epsilon;
        }

		public void setEpsilon(double epsilon) {
        	this.epsilon = epsilon;
        }

		public double getMaxRadius() {
        	return maxRadius;
        }

		public void setMaxRadius(double maxRadius) {
        	this.maxRadius = maxRadius;
        }
	}
	
	public static final class Jet extends ParticleAction {
		ParticleDomain domain, accelDomain;

		public Jet(ParticleDomain domain, ParticleDomain accelDomain) {
	        this.domain = domain;
	        this.accelDomain = accelDomain;
        }

		public ParticleDomain getDomain() {
        	return domain;
        }

		public void setDomain(ParticleDomain domain) {
        	this.domain = domain;
        }

		public ParticleDomain getAccelDomain() {
        	return accelDomain;
        }

		public void setAccelDomain(ParticleDomain accelDomain) {
        	this.accelDomain = accelDomain;
        }
	}
	
	public static final class Bounce extends ParticleAction {
		double friction, resilience, cutoff;
		ParticleDomain domain;
		
		public Bounce(double friction, double resilience, double cutoff, ParticleDomain domain) {
	        this.friction = friction;
	        this.resilience = resilience;
	        this.cutoff = cutoff;
	        this.domain = domain;
        }

		public double getFriction() {
        	return friction;
        }

		public void setFriction(double friction) {
        	this.friction = friction;
        }

		public double getResilience() {
        	return resilience;
        }

		public void setResilience(double resilience) {
        	this.resilience = resilience;
        }

		public double getCutoff() {
        	return cutoff;
        }

		public void setCutoff(double cutoff) {
        	this.cutoff = cutoff;
        }

		public ParticleDomain getDomain() {
        	return domain;
        }

		public void setDomain(ParticleDomain domain) {
        	this.domain = domain;
        }
	}
	
	public static final class Avoid extends ParticleAction {
		double magnitude, epsilon, lookahead;
		ParticleDomain domain;
		
		public Avoid(double magnitude, double epsilon, double lookahead, ParticleDomain domain) {
	        this.magnitude = magnitude;
	        this.epsilon = epsilon;
	        this.lookahead = lookahead;
	        this.domain = domain;
        }

		public double getMagnitude() {
        	return magnitude;
        }

		public void setMagnitude(double magnitude) {
        	this.magnitude = magnitude;
        }

		public double getEpsilon() {
        	return epsilon;
        }

		public void setEpsilon(double epsilon) {
        	this.epsilon = epsilon;
        }

		public double getLookahead() {
        	return lookahead;
        }

		public void setLookahead(double lookahead) {
        	this.lookahead = lookahead;
        }

		public ParticleDomain getDomain() {
        	return domain;
        }

		public void setDomain(ParticleDomain domain) {
        	this.domain = domain;
        }
	}
	
}
