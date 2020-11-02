import engine.Engine;
import game.Game;
import userInput.KeyListener;

public class Ankkatetris {

    private final Engine engine;
    private final Game game;
    private final KeyListener keyListener;

    public Ankkatetris() {
        // Initialize a game and an engine
        keyListener = new KeyListener();
        game = new Game(keyListener);
        engine = new Engine(game);

        engine.setDesiredFPS(Engine.VSYNC);
        engine.setDesiredTPS(60);
        engine.printFps(false);
    }

    public void start() {

        System.out.println("Starting Ankkatetris...");

        engine.start();
    }

    public static void main(String[] args) {

        Ankkatetris ankkatetris = new Ankkatetris();
        ankkatetris.start();
    }
}
