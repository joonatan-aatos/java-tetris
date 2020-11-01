import engine.Engine;
import game.Game;

public class Ankkatetris {

    private Engine engine;
    private Game game;

    public Ankkatetris() {
        // Initialize a game and an engine
        game = new Game();
        engine = new Engine(game);

        engine.setDesiredFPS(Engine.VSYNC);
        engine.setDesiredTPS(60);
        engine.printFps(true);
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
