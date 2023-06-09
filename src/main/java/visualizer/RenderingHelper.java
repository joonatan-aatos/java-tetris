package visualizer;

import game.Game;
import game.Game.State;
import logic.Piece;
import logic.Piece.PieceType;
import logic.Sprite;
import logic.World;

import java.util.ArrayList;

// This class has more high level interface for drawing the game
// It mostly uses the low level functions provided by the Renderer class
public class RenderingHelper {

    private final Renderer renderer;
    private final float stageWidth = 0.9f;
    private final float stageHeight = 1.8f;
    private final float sideBoxWidth = 0.4f;
    private final float sideBoxHeight = 0.5f;

    // Draw logic
    private int clearEffectTimer = 0;
    private int dropEffectTimer = 0;
    private int gameOverEffectTimer = 0;
    private float effectYChange = 0;
    private State previousState;

    public RenderingHelper(Renderer renderer) {
        this.renderer = renderer;
    }

    public void drawWorld(World world, State state) {

        updateLogic(world, state);

        // Reset
        renderer.reset();

        // Draw
        if(state == State.Running) {
            drawStage();
            drawPlacedPieces(world.getPlacedSquares());
            drawNextBlockBox(world.getNextPieceType());
            drawStoredBlockBox(world.getStoredPieceType());
            drawCurrentPiece(world.getCurrentPiece());
            drawScore(world.getScore());
        }
        else if(state == State.GameOver) {
            if(gameOverEffectTimer > 0) {
                drawStage();
                drawPlacedPieces(world.getPlacedSquares());
                drawNextBlockBox(world.getNextPieceType());
                drawStoredBlockBox(world.getStoredPieceType());
                drawCurrentPiece(world.getCurrentPiece());
                drawScore(world.getScore());
            }
            drawGameOverScreen(world.getScore());
        }
        else if(state == State.MainMenu) {
            drawMainMenu();
        }

        // Swap color buffers
        renderer.draw();
    }

    private void updateLogic(World world, State newState) {

        // Logic
        if(previousState == State.Running && newState == State.GameOver) {
            gameOverEffectTimer = 60;
        }
        if(newState != State.GameOver && gameOverEffectTimer != 0) {
            gameOverEffectTimer = 0;
        }
        if(world.wasRowCleared())
            clearEffectTimer = 8;
        else if(world.wasPieceHardened())
            dropEffectTimer = 6;

        if(gameOverEffectTimer > 0) {
            gameOverEffectTimer--;
            effectYChange = (float) Math.pow(gameOverEffectTimer - 60f, 2f) / 1200f;
            if(dropEffectTimer != 0)
                dropEffectTimer = 0;
            if(clearEffectTimer != 0)
                clearEffectTimer = 0;
        }
        else if(clearEffectTimer > 0) {
            clearEffectTimer--;
            effectYChange = clearEffectTimer * (clearEffectTimer - 8f) / 400f;
            if(dropEffectTimer != 0)
                dropEffectTimer = 0;
        }
        else if(dropEffectTimer > 0) {
            dropEffectTimer--;
            effectYChange = dropEffectTimer * (dropEffectTimer - 6f) / 800f;
        }

        previousState = newState;

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
                    y-stageHeight/World.WORLD_HEIGHT/2 + effectYChange,
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
                y-stageHeight/World.WORLD_HEIGHT/2 + effectYChange,
                stageWidth/World.WORLD_WIDTH/2,
                stageHeight/World.WORLD_HEIGHT/2,
                0,
                colors,
                1f
        );
    }

    private void drawScore(int score) {

        float textSize = 0.06f;
        String scoreString = Integer.toString(score);
        for(int i = 0; i < scoreString.length(); i++) {
            drawNumber(Integer.parseInt(Character.toString(scoreString.charAt(i))), stageWidth*3f/5f + i*0.12f, 1-stageHeight+textSize + effectYChange, textSize, new float[]{0.7f, 0.7f, 0.7f, 1f});
        }
    }

    private void drawGameOverScreen(int score) {

        float widthMultiplier = (30-gameOverEffectTimer)/30f;
        if(widthMultiplier < 0)
            widthMultiplier = 0;

        renderer.drawImage(
                0,
                0.1f,
                0.8f*widthMultiplier,
                0.8f*widthMultiplier*renderer.gameOverTexture.height/renderer.gameOverTexture.width,
                gameOverEffectTimer/4f,
                renderer.gameOverTexture.textureHandle
        );

        renderer.drawImage(
                0,
                -0.15f,
                0.6f*widthMultiplier,
                0.6f*widthMultiplier*renderer.pressContinueTexture.height/renderer.pressContinueTexture.width,
                gameOverEffectTimer/4f,
                renderer.pressContinueTexture.textureHandle
        );

        float textSize = 0.06f * widthMultiplier;
        String scoreString = Integer.toString(score);
        for(int i = 0; i < scoreString.length(); i++) {
            drawNumber(Integer.parseInt(Character.toString(scoreString.charAt(i))), -0.8f + i*0.12f, 0.8f, textSize, new float[]{0.7f, 0.7f, 0.7f, 1f});
        }
    }

    private void drawMainMenu() {

        renderer.drawImage(
                0,
                -0.15f,
                0.5f,
                0.5f*renderer.pressStartTexture.height/renderer.pressStartTexture.width,
                0,
                renderer.pressStartTexture.textureHandle
        );
    }

    private void drawStage() {

        // Black rectangle
        renderer.drawRectangle(
                -stageWidth/2,
                1f,
                stageWidth,
                stageHeight + 1f-stageHeight/2 - effectYChange,
                new float[]{0f, 0f, 0f, 1f}
        );
    }

    private void drawPlacedPieces(int[][] placedSquares) {

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
                0.8f + effectYChange,
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
                0.8f + effectYChange,
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

    private void drawCurrentPiece(Piece piece) {

        if(piece == null)
            return;

        int[][] pieceShape = piece.getPieceShape();
        // Center distance is 1 except in the O piece where it is 0
        int centerDistance = 1;
        if (pieceShape.length == 2) {
            centerDistance = 0;
        }

        // Draw ghost block first
        for (int i = 0; i < pieceShape.length; i++) {
            for (int j = 0; j < pieceShape[0].length; j++) {
                if (pieceShape[i][j] != 0) {
                    int x = piece.getxPos() + (j - centerDistance) * World.GRID_SIZE;
                    int y = piece.getGhostBlockHeight() + ((pieceShape.length - i) - centerDistance) * World.GRID_SIZE;
                    drawSquare(x, y, piece.getPieceType().getTypeIndex(), true);
                }
            }
        }

        // Draw the actual piece
        for (int i = 0; i < pieceShape.length; i++) {
            for (int j = 0; j < pieceShape[0].length; j++) {
                if (pieceShape[i][j] != 0) {
                    int x = piece.getxPos() + (j - centerDistance) * World.GRID_SIZE;
                    int y = piece.getyPos() + ((pieceShape.length - i) - centerDistance) * World.GRID_SIZE;
                    drawSquare(x, y, piece.getPieceType().getTypeIndex(), false);
                }
            }
        }
    }

    private void drawNumber(int number, float xPos, float yPos, float width, float[] color) {

        if(number < 0 || number > 9)
            throw new IllegalArgumentException("Only numbers that are one digit long can be drawn. Number given: "+number);

        float widthMultiplier = 0.2f;

        // Draw horizontal lines
        if(number != 1 && number != 4)
            renderer.drawRectangle(xPos, yPos, width + width*widthMultiplier, width*widthMultiplier, color);
        if(number != 0 && number != 1 && number != 7)
            renderer.drawRectangle(xPos, yPos + -1*width, width + width*widthMultiplier, width*widthMultiplier, color);
        if(number != 1 && number != 4 && number != 7)
            renderer.drawRectangle(xPos, yPos + -2*width, width + width*widthMultiplier, width*widthMultiplier, color);

        // Draw vertical lines
        if(number != 1 && number != 2 && number != 3 && number != 7)
            renderer.drawRectangle(xPos, yPos, width*widthMultiplier, width + width*widthMultiplier, color);
        if(number != 5 && number != 6)
            renderer.drawRectangle(xPos + width, yPos, width*widthMultiplier, width + width*widthMultiplier, color);
        if(number != 1 && number != 3 && number != 4 && number != 5 && number != 7 && number != 9)
            renderer.drawRectangle(xPos, yPos - width, width*widthMultiplier, width + width*widthMultiplier, color);
        if(number != 2)
            renderer.drawRectangle(xPos + width, yPos -width, width*widthMultiplier, width + width*widthMultiplier, color);

    }

    private float[] convertSpriteCoords(int x, int y) {
        return new float[] {
                -stageWidth / 2f + (stageWidth / World.WORLD_WIDTH * ((float) x / World.GRID_SIZE)),
                -stageHeight / 2f + (stageHeight / World.WORLD_HEIGHT * ((float) y / World.GRID_SIZE))
        };
    }
}
