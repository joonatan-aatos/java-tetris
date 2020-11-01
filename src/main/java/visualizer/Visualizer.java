package visualizer;

import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwTerminate;

import game.GameInfo;
import logic.World;
import org.lwjgl.glfw.GLFWErrorCallback;

// This class is responsible for handling most of the basic rendering logic and creating and destroying the window
public class Visualizer {

    private Window window;
    private Renderer renderer;
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

        window = new Window(GameInfo.TITLE, 500, 500);

        try {
            renderer = new Renderer(window);
        } catch (Exception e) {
            e.printStackTrace();
            gameInterface.initFailed();
        }
    }

    public void update(World world) {

        if(!window.shouldClose()) {
            renderer.render(world);
        }
        else {
            stop();
        }
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
