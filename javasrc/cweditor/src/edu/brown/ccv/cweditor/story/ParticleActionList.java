package edu.brown.ccv.cweditor.story;

import java.util.*;

public class ParticleActionList implements Named {
	public static class RemoveCondition {
		RemoveCondition() {}
		
		public static final class Age extends RemoveCondition {
			double age;
			boolean youngerThan;

			public Age(double age, boolean youngerThan) {
	            this.age = age;
	            this.youngerThan = youngerThan;
            }

			public double getAge() {
            	return age;
            }

			public void setAge(double age) {
            	this.age = age;
            }

			public boolean isYoungerThan() {
            	return youngerThan;
            }

			public void setYoungerThan(boolean youngerThan) {
            	this.youngerThan = youngerThan;
            }
		}
		
		public static final class Position extends RemoveCondition {
			boolean inside;
			ParticleDomain domain;
			
			public Position(boolean inside, ParticleDomain domain) {
	            this.inside = inside;
	            this.domain = domain;
            }

			public boolean isInside() {
            	return inside;
            }

			public void setInside(boolean inside) {
            	this.inside = inside;
            }

			public ParticleDomain getDomain() {
            	return domain;
            }

			public void setDomain(ParticleDomain domain) {
            	this.domain = domain;
            }
		}
		
		public static final class Velocity extends RemoveCondition {
			boolean inside;
			ParticleDomain domain;
			
			public Velocity(boolean inside, ParticleDomain domain) {
	            this.inside = inside;
	            this.domain = domain;
            }

			public boolean isInside() {
            	return inside;
            }

			public void setInside(boolean inside) {
            	this.inside = inside;
            }

			public ParticleDomain getDomain() {
            	return domain;
            }

			public void setDomain(ParticleDomain domain) {
            	this.domain = domain;
            }
		}
	}
	
	String name;
	double sourceRate;
	ParticleDomain sourceDomain, velDomain;
	List<ParticleAction> actions = new ArrayList<ParticleAction>();
	RemoveCondition removeCondition;

	public ParticleActionList(String name) {
	    this.name = name;
    }

	@Override
    public String getName() {
    	return name;
    }

	@Override
    public void setName(String name) {
    	this.name = name;
    }

	public double getSourceRate() {
    	return sourceRate;
    }

	public void setSourceRate(double sourceRate) {
    	this.sourceRate = sourceRate;
    }

	public ParticleDomain getSourceDomain() {
    	return sourceDomain;
    }

	public void setSourceDomain(ParticleDomain sourceDomain) {
    	this.sourceDomain = sourceDomain;
    }

	public ParticleDomain getVelDomain() {
    	return velDomain;
    }

	public void setVelDomain(ParticleDomain velDomain) {
    	this.velDomain = velDomain;
    }

	public RemoveCondition getRemoveCondition() {
    	return removeCondition;
    }

	public void setRemoveCondition(RemoveCondition removeCondition) {
    	this.removeCondition = removeCondition;
    }

	public List<ParticleAction> getActions() {
    	return actions;
    }
}
