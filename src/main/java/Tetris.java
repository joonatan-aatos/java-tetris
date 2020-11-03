import engine.Engine;
import game.Game;
import userInput.KeyListener;

public class Tetris {

    private final Engine engine;
    private final Game game;
    private final KeyListener keyListener;

    public Tetris() {
        // Initialize a game and an engine
        keyListener = new KeyListener();
        game = new Game(keyListener);
        engine = new Engine(game);

        engine.setDesiredFPS(Engine.VSYNC);
        engine.setDesiredTPS(60);
        engine.printFps(false);
    }

    public void start() {

        System.out.println("Starting Tetris...");

        engine.start();
    }

    public static void main(String[] args) {

        Tetris tetris = new Tetris();
        tetris.start();
    }
}
