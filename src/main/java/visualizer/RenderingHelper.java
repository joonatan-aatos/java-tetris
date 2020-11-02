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

    float rotation = 0f;

    private void drawStage(World world) {

        rotation += 0.02f;
        if(rotation >= 2*Math.PI)
            rotation = 0;

        renderer.drawTriangle(
                new float[]{-0.5f, -0.5f, 0f, -0.5f, 0.5f, 0f, 0.5f, -0.5f, 0f},
                rotation,
                new float[]{1f, 0f, 1f, 1f}
        );
    }
}
