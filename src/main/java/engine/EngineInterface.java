package engine;

public interface EngineInterface {
	public void start();
	public void stop();
	public void setDesiredTPS(int tps);
	public void setDesiredFPS(int fps);
	public void printFps(boolean print);
}
