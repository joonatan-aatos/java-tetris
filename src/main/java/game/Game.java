package game;

import engine.EngineInterface;
import engine.GameInterface;
import logic.World;
import logic.WorldToGameInterface;
import userInput.KeyListener;
import userInput.KeyListenerInterface;
import visualizer.Visualizer;
import visualizer.VisualizerToGameInterface;

// This class is the core of the game and it handles most of the basic game logic
public class Game implements GameInterface, VisualizerToGameInterface, WorldToGameInterface {

    // An interface for using the engine
    private EngineInterface engineInterface;

    private final Visualizer visualizer;
    private final World world;
    private final KeyListener keyListener;

    /**
     * Create a new game
     */
    public Game(KeyListener keyListener) {
        visualizer = new Visualizer(this);
        this.keyListener = keyListener;
        world = new World(this);
        visualizer.getWindow().setKeyCallback(keyListener);
    }

    @Override
    public void init(EngineInterface engine) {
        engineInterface = engine;
    }

    @Override
    public void update() {
        world.tick();
    }

    @Override
    public void render() {
        visualizer.update(world);
    }

    @Override
    public void windowClosed() {
        engineInterface.stop();
    }

    @Override
    public void initFailed() {
        engineInterface.stop();
    }

    @Override
    public void addKeyListener(KeyListenerInterface keyListenerInterface) {
        keyListener.addKeyListener(keyListenerInterface);
    }
}
