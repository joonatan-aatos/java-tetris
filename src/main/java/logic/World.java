package logic;

import userInput.KeyListenerInterface;

import java.util.ArrayList;

// This class handles all the basic game logic and stores most of the relevant game objects
public class World {

    public static final int WORLD_WIDTH = 10;
    public static final int WORLD_HEIGHT = 20;
    // Coordinates between two adjacent squares
    public static final int GRID_SIZE = 10;

    // List that stores all sprites
    private ArrayList<Sprite> sprites;
    private ArrayList<Sprite> spritesToRemove;
    private int[][] placedSquares;
    private Piece currentPiece;
    private TetrisPlayer tetrisPlayer;
    private WorldToGameInterface game;

    public World(WorldToGameInterface game) {

        this.game = game;
        init();
        game.addKeyListener(tetrisPlayer);
    }

    // Init
    private void init() {

        sprites = new ArrayList<Sprite>();
        spritesToRemove = new ArrayList<Sprite>();
        placedSquares = new int[20][10];

        // Set all the arrays values to 0
        for(int i = 0; i < placedSquares.length; i++) {
            for(int j = 0; j < placedSquares[0].length; j++) {
                placedSquares[i][j] = 0;
            }
        }

        currentPiece = null;

        tetrisPlayer = new TetrisPlayer();
    }

    // Called every time the world should update
    public void tick() {

        if(currentPiece == null)
            createNewPiece();
        // Invoke tick() for every sprite
        for(Sprite sprite : sprites) {
            sprite.tick();
        }
        // Remove sprites
        for(Sprite sprite : spritesToRemove) {
            sprites.remove(sprite);
        }
        spritesToRemove.clear();
    }

    private void createNewPiece() {
        currentPiece = new Piece(4*GRID_SIZE, 18*GRID_SIZE, Piece.PieceType.T, this);
        sprites.add(currentPiece);
        tetrisPlayer.updateCurrentPiece(currentPiece);
    }

    protected void hardenAPiece(Piece piece) {

        int[][] shape = piece.getPieceShape();

        // Center distance is 1 except in the O-piece
        int centerDistance = 1;
        if(shape.length == 2) {
            centerDistance = 0;
        }

        for(int i = 0; i < shape.length; i++) {
            for(int j = 0; j < shape[0].length; j++) {
                if(shape[i][j] != 0) {
                    int x = piece.getxPos() + (j - centerDistance) * GRID_SIZE;
                    int y = piece.getyPos() + ((shape.length - i - 1) - centerDistance) * GRID_SIZE;
                    if(x >= 0 && x < WORLD_WIDTH*GRID_SIZE &&
                            y >= 0 && y < WORLD_HEIGHT*GRID_SIZE &&
                            placedSquares[y/GRID_SIZE][x/GRID_SIZE] == 0) {

                        placedSquares[y/GRID_SIZE][x/GRID_SIZE] = 1;
                    }
                }
            }
        }

        spritesToRemove.add(piece);
        currentPiece = null;
    }

    /**
     * @return Return a copy of the current sprites object
     */
    public ArrayList<Sprite> getSprites() {
        return new ArrayList<Sprite>(sprites);
    }

    /**
     * @return Return a copy of the current placedSquares object
     */
    public int[][] getPlacedSquares() {
        return placedSquares.clone();
    }
}
