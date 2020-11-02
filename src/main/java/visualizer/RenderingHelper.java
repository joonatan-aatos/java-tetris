package visualizer;

import logic.World;

// This class has more high level interface for drawing the game
// It mostly uses the low level functions provided by the Renderer class
public class RenderingHelper {

    private final Renderer renderer;
    private final float stageWidth = 0.9f;
    private final float stageHeight = 1.8f;

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

        // Black rectangle
        renderer.drawRectangle(
                -stageWidth/2,
                stageHeight/2,
                stageWidth,
                stageHeight,
                new float[]{0f, 0f, 0f, 1f}
        );

        // Draw placed squares
        int[][] placedSquares = world.getPlacedSquares();
        for(int i = 0; i < placedSquares.length; i++) {
            for(int j = 0; j < placedSquares[0].length; j++) {
                if(placedSquares[i][j] == 1) {
                    renderer.drawRectangle(
                            -stageWidth/2+stageWidth/World.WORLD_WIDTH*j,
                            -stageHeight/2+stageHeight/World.WORLD_HEIGHT*(i+1),
                            stageWidth/World.WORLD_WIDTH,
                            stageHeight/World.WORLD_HEIGHT,
                            new float[]{1f, 1f, 1f, 1f}
                    );
                }
            }
        }
    }
}
