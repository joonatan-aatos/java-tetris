package engine;

import game.GameInfo;

// This class manages the main thread that the game is running on
public class Engine implements Runnable, EngineInterface {

    // A constant that can be used to enable vsync
    public static final int VSYNC = -1;

    // Determines whether or not the engine is running
    private boolean running;
    // Determines whether or not the tps and fps is printed to the console output
    private boolean printFps;
    // Desired ticks per second
    private int desiredTPS;
    // Desired frames per second
    private int desiredFPS;

    // An interface for invoking call back functions
    private final GameInterface gameInterface;

    /**
     * Create an engine
     * @param game An implemented instance of the game interface
     */
    public Engine(GameInterface game) {
        running = false;
        printFps = false;
        gameInterface = game;
        desiredFPS = -1;
        desiredTPS = -1;
    }

    // Called when a new thread is created. This should not be called manually
    @Override
    public void run() {

        gameInterface.init(this);

        // Declare variables that keep track of how often the game renders and updates
        int fps = 0;
        int tps = 0;

        // Calculate how much time is between each render/update
        double nsPerTick = desiredTPS == 0 ? 0 : 1000000000d / desiredTPS;
        double nsPerFrame = desiredFPS == 0 ? 0 : 1000000000d / desiredFPS;
        // Store the current time in a variable
        double then = System.nanoTime();
        double now;
        // Variables that keep track of unprocessed ticks and frames
        double unprocessedTicks = 0;
        double unprocessedFrames = 0;

        double fpsTimer = System.currentTimeMillis();

        while(running) {

            // Math
            now = System.nanoTime();
            unprocessedTicks += (now - then) / nsPerTick;
            unprocessedFrames += (now - then) / nsPerFrame;
            then = now;

            while(unprocessedTicks >= 1) {
                // Update
                gameInterface.update();
                tps++;
                unprocessedTicks--;
            }

            // Check if vsync is enabled
            if(desiredFPS == VSYNC) {
                // Render
                gameInterface.render();
                fps++;
                unprocessedFrames -= Math.floor(unprocessedFrames);
            }
            else {
                if(unprocessedFrames >= 1) {
                    // Render
                    gameInterface.render();
                    fps++;
                    unprocessedFrames -= Math.floor(unprocessedFrames);
                }
            }

            if(System.currentTimeMillis() - fpsTimer >= 1000) {
                // Update fps and tps to game info
                GameInfo.FPS = fps;
                GameInfo.TPS = tps;

                if(printFps) {
                    System.out.println("FPS: " + fps + " TPS: " + tps);
                }
                // Update fps timer
                fpsTimer = System.currentTimeMillis();
                fps = 0;
                tps = 0;
            }

            // Wait a couple milliseconds to reduce CPU-usage
            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Start the engine
     */
    @Override
    public void start() {
        running = true;
        // Create a new thread and start it
        new Thread(this).run();
    }

    /**
     * Stop the engine
     */
    @Override
    public void stop() {
        running = false;
    }

    /**
     * @param tps Ticks per second
     */
    @Override
    public void setDesiredTPS(int tps) {
        desiredTPS = tps;
    }

    /**
     * @param fps Frames per second
     */
    @Override
    public void setDesiredFPS(int fps) {
        desiredFPS = fps;
    }

    /**
     * @param print True: Print fps and tps. False: Don't print fps and tps.
     */
    @Override
    public void printFps(boolean print) {
        printFps = print;
    }
}
