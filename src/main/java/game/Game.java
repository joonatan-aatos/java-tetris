package game;

import engine.EngineInterface;
import engine.GameInterface;

// This class is the core of the game and it handles most of the basic game logic
public class Game implements GameInterface {

    // An interface for using the engine
    private EngineInterface engineInterface;

    /**
     * Create a new game
     */
    public Game() {

    }

    @Override
    public void init(EngineInterface engine) {
        engineInterface = engine;
    }

    @Override
    public void update() {

    }

    @Override
    public void render() {

    }
}
