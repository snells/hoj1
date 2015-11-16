package hoj;


// Timer runs in it's own threads so you need to call quit to finish it.
public class Timer extends Thread {
    private volatile long startTime;
    private volatile long time;
    private boolean running = true;
    private boolean quitFlag = false;
    public Timer() {
	}
    @Override
    public void start() {
	startTime();
	super.start();
    }
    public void startTime() {
	startTime = System.currentTimeMillis();
	time = startTime;
	running = true;
    }
    public void stopTime() {
	running = false;
    }
    public void update() {
	if(running) {
	    time = System.currentTimeMillis();
	}
    }
    public void reset() {
	startTime = System.currentTimeMillis();
	time = startTime;
    }
    
    // returns time in seconds 
    public float getTime() {
    	return ((float)(time - startTime)) / 1000f;
    }
    public void quit() {
    	quitFlag = true;
    }
    public boolean isActive() {
    	return !quitFlag;
    }
    @Override
    public void run() {
	while(!quitFlag) {
	    update();
	}
    }	
}

