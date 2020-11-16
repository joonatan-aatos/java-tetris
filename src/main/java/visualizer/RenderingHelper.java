package visualizer;

import logic.Piece;
import logic.Piece.PieceType;
import logic.Sprite;
import logic.World;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

// This class has more high level interface for drawing the game
// It mostly uses the low level functions provided by the Renderer class
public class RenderingHelper {

    private final Renderer renderer;
    private final float stageWidth = 0.9f;
    private final float stageHeight = 1.8f;
    private final float sideBoxWidth = 0.4f;
    private final float sideBoxHeight = 0.5f;

    public RenderingHelper(Renderer renderer) {
        this.renderer = renderer;
    }

    public void drawWorld(World world) {
        // Reset
        renderer.reset();
        // Draw
        drawStage(world.getPlacedSquares());
        drawNextBlockBox(world.getNextPieceType());
        drawStoredBlockBox(world.getStoredPieceType());
        drawSprites(world.getSprites());

        // Swap color buffers
        renderer.draw();
    }

    private void drawSquare(int x, int y, int colorIndex, boolean ghostBlock) {

        float[] coords = convertSpriteCoords(x, y);
        drawSquare(coords[0], coords[1], colorIndex, ghostBlock);
    }

    private void drawSquare(float x, float y, int colorIndex, boolean ghostBlock) {

        if(ghostBlock) {

            float[][] colors = new float[][]{
                    new float[]{0f, 0f, 0f, 1f},
                    new float[]{0f, 0f, 0f, 1f},
                    new float[]{0f, 0f, 0f, 1f},
                    new float[]{0f, 0f, 0f, 1f},
                    new float[]{0f, 0f, 0f, 1f},
                    Colors.brightenedPieceColors[colorIndex]
            };

            renderer.drawTetrisPiece(
                    x+stageWidth/World.WORLD_WIDTH/2,
                    y-stageHeight/World.WORLD_HEIGHT/2,
                    stageWidth/World.WORLD_WIDTH/2,
                    stageHeight/World.WORLD_HEIGHT/2,
                    0,
                    colors,
                    0.20f
            );
            return;
        }

        float[][] colors = new float[][]{
                Colors.pieceColors[colorIndex],
                Colors.pieceColors2[colorIndex],
                Colors.pieceColors3[colorIndex],
                Colors.pieceColors4[colorIndex],
                Colors.pieceColors5[colorIndex],
                Colors.brightenedPieceColors[colorIndex]
        };

        renderer.drawTetrisPiece(
                x+stageWidth/World.WORLD_WIDTH/2,
                y-stageHeight/World.WORLD_HEIGHT/2,
                stageWidth/World.WORLD_WIDTH/2,
                stageHeight/World.WORLD_HEIGHT/2,
                0,
                colors,
                1f
        );
    }

    private void drawStage(int[][] placedSquares) {

        // Black rectangle
        renderer.drawRectangle(
                -stageWidth/2,
                1,
                stageWidth,
                stageHeight + 1f-stageHeight/2,
                new float[]{0f, 0f, 0f, 1f}
        );

        // Draw placed squares
        for(int i = 0; i < placedSquares.length; i++) {
            for(int j = 0; j < placedSquares[0].length; j++) {
                if(placedSquares[i][j] != 0) {
                    drawSquare(
                            j*World.GRID_SIZE,
                            (i+1)*World.GRID_SIZE,
                            placedSquares[i][j]-1,
                            false
                    );
                }
            }
        }
    }

    private void drawNextBlockBox(PieceType nextPiece) {

        // Draw another black rectangle for the next square box
        renderer.drawRectangle(
                stageWidth/2 + (1-(stageWidth/2))/2-sideBoxWidth/2,
                0.8f,
                sideBoxWidth,
                sideBoxHeight,
                new float[]{0f, 0f, 0f, 1f}
        );

        int[][] pieceShape = nextPiece.getShape();
        // Center distance is 1 except in the O piece where it is 0
        int centerDistance = 1;
        if(pieceShape.length == 2) {
            centerDistance = 0;
        }

        float positionFixX = 0f;
        float positionFixY = 0f;

        switch (nextPiece.getTypeIndex()) {
            case 0: {
                positionFixX = -stageWidth / World.WORLD_WIDTH / 2;
                positionFixY = -stageWidth / World.WORLD_WIDTH / 2;
                break;
            }
            case 1: {
                positionFixX = -stageWidth / World.WORLD_WIDTH / 2;
                positionFixY = 0;
                break;
            }
        }

        for(int i = 0; i < pieceShape.length; i++) {
            for(int j = 0; j < pieceShape[0].length; j++) {
                if(pieceShape[i][j] != 0) {
                    drawSquare(stageWidth/2 + (1-(stageWidth/2))/2 - stageWidth/World.WORLD_WIDTH/2 + (j-centerDistance)*stageWidth/World.WORLD_WIDTH + positionFixX,
                            0.8f-sideBoxHeight/2 + (pieceShape.length-1-i-centerDistance)*stageWidth/World.WORLD_WIDTH + positionFixY,
                            nextPiece.getTypeIndex(), false);
                }
            }
        }

    }

    private void drawStoredBlockBox(PieceType storedPiece) {

        // Draw another black rectangle for the next square box
        renderer.drawRectangle(
                - stageWidth/2 - (1-(stageWidth/2))/2 - sideBoxWidth/2,
                0.8f,
                sideBoxWidth,
                sideBoxHeight,
                new float[]{0f, 0f, 0f, 1f}
        );

        // Return if there is no stored piece
        if(storedPiece == null)
            return;

        int[][] pieceShape = storedPiece.getShape();
        // Center distance is 1 except in the O piece where it is 0
        int centerDistance = 1;
        if(pieceShape.length == 2) {
            centerDistance = 0;
        }

        float positionFixX = 0f;
        float positionFixY = 0f;

        switch (storedPiece.getTypeIndex()) {
            case 0: {
                positionFixX = -stageWidth / World.WORLD_WIDTH / 2;
                positionFixY = -stageWidth / World.WORLD_WIDTH / 2;
                break;
            }
            case 1: {
                positionFixX = -stageWidth / World.WORLD_WIDTH / 2;
                positionFixY = 0;
                break;
            }
        }

        for(int i = 0; i < pieceShape.length; i++) {
            for(int j = 0; j < pieceShape[0].length; j++) {
                if(pieceShape[i][j] != 0) {
                    drawSquare(- stageWidth/2 - (1-(stageWidth/2))/2 - stageWidth/World.WORLD_WIDTH/2 + (j-centerDistance)*stageWidth/World.WORLD_WIDTH + positionFixX,
                            0.8f-sideBoxHeight/2 + (pieceShape.length-1-i-centerDistance)*stageWidth/World.WORLD_WIDTH + positionFixY,
                            storedPiece.getTypeIndex(), false);
                }
            }
        }

    }

    private void drawSprites(ArrayList<Sprite> sprites) {

        for(Sprite sprite : sprites) {
            if(sprite instanceof Piece) {
                Piece piece = (Piece) sprite;
                PieceType pieceType = piece.getPieceType();
                int[][] pieceShape = piece.getPieceShape();
                // Center distance is 1 except in the O piece where it is 0
                int centerDistance = 1;
                if(pieceShape.length == 2) {
                    centerDistance = 0;
                }
                for(int i = 0; i < pieceShape.length; i++) {
                    for(int j = 0; j < pieceShape[0].length; j++) {
                        if(pieceShape[i][j] != 0) {
                            int x = piece.getxPos() + (j - centerDistance) * World.GRID_SIZE;
                            int y = piece.getyPos() + ((pieceShape.length - i) - centerDistance) * World.GRID_SIZE;
                            drawSquare(x, y, piece.getPieceType().getTypeIndex(), false);
                        }
                    }
                }

                // Draw ghost block
                for(int i = 0; i < pieceShape.length; i++) {
                    for(int j = 0; j < pieceShape[0].length; j++) {
                        if(pieceShape[i][j] != 0) {
                            int x = piece.getxPos() + (j - centerDistance) * World.GRID_SIZE;
                            int y = piece.getGhostBlockHeight() + ((pieceShape.length - i) - centerDistance) * World.GRID_SIZE;
                            drawSquare(x, y, piece.getPieceType().getTypeIndex(), true);
                        }
                    }
                }
            }
        }
    }

    private float[] convertSpriteCoords(int x, int y) {
        return new float[] {
                -stageWidth / 2f + (stageWidth / World.WORLD_WIDTH * ((float) x / World.GRID_SIZE)),
                -stageHeight / 2f + (stageHeight / World.WORLD_HEIGHT * ((float) y / World.GRID_SIZE))
        };
    }
}
