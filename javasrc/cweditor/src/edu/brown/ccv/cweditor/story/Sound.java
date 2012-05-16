package edu.brown.ccv.cweditor.story;

public class Sound implements Named {
	public static enum Mode {
		// WARNING: RENAMING WILL BREAK XML
		Positional, Fixed;
	}
	
	public static class Repeat {
		Repeat() {}
		
		// "singletons" scoped to outer class
		public static class None extends Repeat { None() {} }
		public static final None NONE = new None();
		
		public static class Forever extends Repeat { Forever() {} }
		public static final Forever FOREVER = new Forever();
		
		// must be instantiatiable, so Repeat can't be an enum :(
		public static class Num extends Repeat {
			int num;

			public Num(int num) {
	            this.num = num;
            }

			public int getNum() {
            	return num;
            }

			public void setNum(int num) {
            	this.num = num;
            }
		}
	}
	
	String name;
	String filename;
	boolean autostart;
	Mode mode;
	Repeat repeat;
	double freq;
	double volume;
	double pan;

	public Sound(String name, String filename, boolean autostart, Mode mode, Repeat repeat, double freq, double volume, double pan) {
	    this.name = name;
	    this.filename = filename;
	    this.autostart = autostart;
	    this.mode = mode;
	    this.repeat = repeat;
	    this.freq = freq;
	    this.volume = volume;
	    this.pan = pan;
    }

	@Override
    public String getName() {
    	return name;
    }

	@Override
    public void setName(String name) {
    	this.name = name;
    }

	public String getFilename() {
    	return filename;
    }

	public void setFilename(String filename) {
    	this.filename = filename;
    }

	public boolean isAutostart() {
    	return autostart;
    }

	public void setAutostart(boolean autostart) {
    	this.autostart = autostart;
    }

	public Mode getMode() {
    	return mode;
    }

	public void setMode(Mode mode) {
    	this.mode = mode;
    }

	public Repeat getRepeat() {
    	return repeat;
    }

	public void setRepeat(Repeat repeat) {
    	this.repeat = repeat;
    }

	public double getFreq() {
    	return freq;
    }

	public void setFreq(double freq) {
    	this.freq = freq;
    }

	public double getVolume() {
    	return volume;
    }

	public void setVolume(double volume) {
    	this.volume = volume;
    }

	public double getPan() {
    	return pan;
    }

	public void setPan(double pan) {
    	this.pan = pan;
    }
	
}
