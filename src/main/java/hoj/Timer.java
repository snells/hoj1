package hoj;

public class Timer {
	private float time;
	private float oldTime;
	private boolean running = false;
	
	public Timer() {
	}
	
	public void start() {
		oldTime = System.currentTimeMillis();
		running = true;
	}
	public void stop() {
		running = false;
	}
	public void update() {
		if(running) {
			float tmp = System.currentTimeMillis();
			time +=  tmp - oldTime;
			oldTime = tmp;
		}
	}
	public void reset() {
		time = 0f;
		oldTime = System.currentTimeMillis();
	}
	
	// returns time in seconds 
	public float getTime() {
		return time / (float)(Math.pow(10.0, -9.0));
	}
}
