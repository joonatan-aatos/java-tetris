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
public class Game implements GameInterface, VisualizerToGameInterface, WorldToGameInterface, KeyListenerInterface {

    // An interface for using the engine
    private EngineInterface engineInterface;

    private final Visualizer visualizer;
    private final KeyListener keyListener;
    private final World world;

    private State state;

    /**
     * Create a new game
     */
    public Game(KeyListener keyListener) {
        visualizer = new Visualizer(this);
        this.keyListener = keyListener;
        world = new World(this);
        visualizer.getWindow().setKeyCallback(keyListener);
        addKeyListener(this);
    }

    // ENGINE

    @Override
    public void init(EngineInterface engine) {
        engineInterface = engine;
        state = State.MainMenu;
    }

    @Override
    public void update() {
        if(state == State.Running)
            world.tick();
    }

    @Override
    public void render() {
        visualizer.update(world, state);
    }

    // VISUALIZER

    @Override
    public void windowClosed() {
        engineInterface.stop();
    }

    @Override
    public void initFailed() {
        engineInterface.stop();
    }

    // WORLD

    @Override
    public void addKeyListener(KeyListenerInterface keyListenerInterface) {
        keyListener.addKeyListener(keyListenerInterface);
    }

    @Override
    public void gameEnded() {
        state = State.GameOver;
    }

    // KEY LISTENER

    @Override
    public void onKeyPressed(int key, int action) {
        // Space
        if(key == 32) {
            if(action == 1) {
                if(state == State.GameOver)
                    state = State.MainMenu;
                else if(state == State.MainMenu)
                    startNewGame();
            }
        }
    }

    private void startNewGame() {
        world.reset();
        state = State.Running;
    }

    public enum State {
        Running,
        MainMenu,
        GameOver;
    }
}
