package visualizer;

import logic.World;

// This class has more high level interface for drawing the game
// It mostly uses the low level functions provided by the Renderer class
public class RenderingHelper {

    private final Renderer renderer;

    public RenderingHelper(Renderer renderer) {
        this.renderer = renderer;
    }

    public void drawWorld(World world) {
        // Reset
        renderer.reset();
        // Draw
        drawStage(world);
        // Swap color buffers
        renderer.draw();
    }

    private void drawStage(World world) {

        renderer.drawTriangle(new float[]{-0.5f, -0.5f, 0f, -0.5f, 0.5f, 0f, 0.5f, -0.5f, 0f}, 0);
    }
}
