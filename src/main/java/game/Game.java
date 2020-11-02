package game;

import engine.EngineInterface;
import engine.GameInterface;
import logic.World;
import visualizer.Visualizer;
import visualizer.VisualizerToGameInterface;

// This class is the core of the game and it handles most of the basic game logic
public class Game implements GameInterface, VisualizerToGameInterface {

    // An interface for using the engine
    private EngineInterface engineInterface;

    private final Visualizer visualizer;
    private final World world;

    /**
     * Create a new game
     */
    public Game() {
        visualizer = new Visualizer(this);
        world = new World();
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
}
