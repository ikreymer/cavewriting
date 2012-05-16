package edu.brown.ccv.cweditor.story;

public class StoryObject implements Named {
	public static abstract class Content {
		Content() {}
		
		public static final class Text extends Content {
			// WARNING: RENAMING THESE ENUM CONSTANTS WILL BREAK XML
			// TODO: store xml name vs internal name
			public static enum VerticalAlign {
				top, center, bottom
			}
			public static enum HorizontalAlign {
				left, center, right
			}
			HorizontalAlign horizAlign;
			VerticalAlign vertAlign;
			String font;
			double depth;
			String text;
			
			public Text(HorizontalAlign horizAlign, VerticalAlign vertAlign, String font, double depth, String text) {
	            this.horizAlign = horizAlign;
	            this.vertAlign = vertAlign;
	            this.font = font;
	            this.depth = depth;
	            this.text = text;
            }

			public HorizontalAlign getHorizAlign() {
            	return horizAlign;
            }

			public void setHorizAlign(HorizontalAlign horizAlign) {
            	this.horizAlign = horizAlign;
            }

			public VerticalAlign getVertAlign() {
            	return vertAlign;
            }

			public void setVertAlign(VerticalAlign vertAlign) {
            	this.vertAlign = vertAlign;
            }

			public String getFont() {
            	return font;
            }

			public void setFont(String font) {
            	this.font = font;
            }

			public double getDepth() {
            	return depth;
            }

			public void setDepth(double depth) {
            	this.depth = depth;
            }

			public String getText() {
            	return text;
            }

			public void setText(String text) {
            	this.text = text;
            }
		}
		
		public static final class Image extends Content {
			String filename;

			public Image(String filename) {
	            this.filename = filename;
            }

			public String getFilename() {
            	return filename;
            }

			public void setFilename(String filename) {
            	this.filename = filename;
            }
		}
		
		public static final class ParticleSystem extends Content {
			int maxParticles;
			ParticleAction actions;
			Group particleGroup;
			boolean lookAtCamera;
			boolean sequential;
			double speed;
			
			public ParticleSystem(int maxParticles, ParticleAction actions, Group particleGroup, boolean lookAtCamera, boolean sequential, double speed) {
	            this.maxParticles = maxParticles;
	            this.actions = actions;
	            this.particleGroup = particleGroup;
	            this.lookAtCamera = lookAtCamera;
	            this.sequential = sequential;
	            this.speed = speed;
            }

			public int getMaxParticles() {
            	return maxParticles;
            }

			public void setMaxParticles(int maxParticles) {
            	this.maxParticles = maxParticles;
            }

			public ParticleAction getActions() {
            	return actions;
            }

			public void setActions(ParticleAction actions) {
            	this.actions = actions;
            }

			public Group getParticleGroup() {
            	return particleGroup;
            }

			public void setParticleGroup(Group particleGroup) {
            	this.particleGroup = particleGroup;
            }

			public boolean isLookAtCamera() {
            	return lookAtCamera;
            }

			public void setLookAtCamera(boolean lookAtCamera) {
            	this.lookAtCamera = lookAtCamera;
            }

			public boolean isSequential() {
            	return sequential;
            }

			public void setSequential(boolean sequential) {
            	this.sequential = sequential;
            }

			public double getSpeed() {
            	return speed;
            }

			public void setSpeed(double speed) {
            	this.speed = speed;
            }
		}
		
		public static final class Light extends Content {
			public static class Type {
				public static final class Point extends Type { Point() {} }
				public static final Point POINT = new Point();
				
				public static final class Directional extends Type { Directional() {} }
				public static final Directional DIRECTIONAL = new Directional();
				
				public static final class Spot extends Type {
					double angle;

					public Spot(double angle) {
	                    this.angle = angle;
                    }

					public double getAngle() {
                    	return angle;
                    }

					public void setAngle(double angle) {
                    	this.angle = angle;
                    }
				}
			}
			
			boolean diffuse, specular;
			double constAtten, linAtten, quadAtten;
			Type type;
			
			public Light(boolean diffuse, boolean specular, double constAtten, double linAtten, double quadAtten, Type type) {
	            this.diffuse = diffuse;
	            this.specular = specular;
	            this.constAtten = constAtten;
	            this.linAtten = linAtten;
	            this.quadAtten = quadAtten;
	            this.type = type;
            }

			public boolean isDiffuse() {
            	return diffuse;
            }

			public void setDiffuse(boolean diffuse) {
            	this.diffuse = diffuse;
            }

			public boolean isSpecular() {
            	return specular;
            }

			public void setSpecular(boolean specular) {
            	this.specular = specular;
            }

			public double getConstAtten() {
            	return constAtten;
            }

			public void setConstAtten(double constAtten) {
            	this.constAtten = constAtten;
            }

			public double getLinAtten() {
            	return linAtten;
            }

			public void setLinAtten(double linAtten) {
            	this.linAtten = linAtten;
            }

			public double getQuadAtten() {
            	return quadAtten;
            }

			public void setQuadAtten(double quadAtten) {
            	this.quadAtten = quadAtten;
            }

			public Type getType() {
            	return type;
            }

			public void setType(Type type) {
            	this.type = type;
            }
		}
		
		public static final class StereoImage extends Content {
			String leftImage;
			String rightImage;
			
			public StereoImage(String leftImage, String rightImage) {
	            this.leftImage = leftImage;
	            this.rightImage = rightImage;
            }

			public String getLeftImage() {
            	return leftImage;
            }

			public void setLeftImage(String leftImage) {
            	this.leftImage = leftImage;
            }

			public String getRightImage() {
            	return rightImage;
            }

			public void setRightImage(String rightImage) {
            	this.rightImage = rightImage;
            }
		}
		
		public static final class Model extends Content {
			String filename;
			boolean checkCollisions;
			
			public Model(String filename, boolean checkCollisions) {
	            this.filename = filename;
	            this.checkCollisions = checkCollisions;
            }

			public String getFilename() {
            	return filename;
            }

			public void setFilename(String filename) {
            	this.filename = filename;
            }

			public boolean isCheckCollisions() {
            	return checkCollisions;
            }

			public void setCheckCollisions(boolean checkCollisions) {
            	this.checkCollisions = checkCollisions;
            }
		}
		
		public static final class None extends Content { None() {} }
		public static final None NONE = new None();
	}
	
	String name;
	boolean visible;
	Color color;
	boolean lighting;
	boolean clickThrough;
	boolean aroundSelfAxis;
	double scale;
	Placement placement;
	Content content;

	public StoryObject(String name) {
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

	public boolean isVisible() {
    	return visible;
    }

	public void setVisible(boolean visible) {
    	this.visible = visible;
    }

	public Color getColor() {
    	return color;
    }

	public void setColor(Color color) {
    	this.color = color;
    }

	public boolean isLighting() {
    	return lighting;
    }

	public void setLighting(boolean lighting) {
    	this.lighting = lighting;
    }

	public boolean isClickThrough() {
    	return clickThrough;
    }

	public void setClickThrough(boolean clickThrough) {
    	this.clickThrough = clickThrough;
    }

	public boolean isAroundSelfAxis() {
    	return aroundSelfAxis;
    }

	public void setAroundSelfAxis(boolean aroundSelfAxis) {
    	this.aroundSelfAxis = aroundSelfAxis;
    }

	public double getScale() {
    	return scale;
    }

	public void setScale(double scale) {
    	this.scale = scale;
    }

	public Placement getPlacement() {
    	return placement;
    }

	public void setPlacement(Placement placement) {
    	this.placement = placement;
    }

	public Content getContent() {
    	return content;
    }

	public void setContent(Content content) {
    	this.content = content;
    }
	
}
