package visualizer;

import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwTerminate;

import game.Game;
import game.GameInfo;
import logic.World;
import org.lwjgl.glfw.GLFWErrorCallback;

// This class is responsible for handling most of the basic rendering logic and creating and destroying the window
public class Visualizer {

    private Window window;
    private Renderer renderer;
    private RenderingHelper renderingHelper;
    private VisualizerToGameInterface gameInterface;

    public Visualizer(VisualizerToGameInterface gameInterface) {

        this.gameInterface = gameInterface;
        init();
    }

    private void init() {

        // Setup an error callback.
        // The default implementation will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        window = new Window(GameInfo.TITLE, 800, 800);

        try {
            renderer = new Renderer(window);
        } catch (Exception e) {
            e.printStackTrace();
            gameInterface.initFailed();
        }

        renderingHelper = new RenderingHelper(renderer);
    }

    public void update(World world, Game.State state) {

        renderer.pollForWindowEvents();

        if(!window.shouldClose()) {
            renderingHelper.drawWorld(world, state);
        }
        else {
            stop();
        }
    }

    public Window getWindow() {
        return window;
    }

    private void stop() {

        gameInterface.windowClosed();

        // Destroy the window
        window.destroy();

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();

    }
}
