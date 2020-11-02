package logic;

import userInput.KeyListenerInterface;

import java.util.ArrayList;

// This class handles all the basic game logic and stores most of the relevant game objects
public class World implements KeyListenerInterface {

    public static final int WORLD_WIDTH = 10;
    public static final int WORLD_HEIGHT = 20;
    // Coordinates between two adjacent squares
    public static final int GRID_SIZE = 10;

    // List that stores all sprites
    private ArrayList<Sprite> sprites;
    private int[][] placedSquares;
    private Piece currentPiece;
    private WorldToGameInterface game;

    public World(WorldToGameInterface game) {

        this.game = game;
        init();
        game.addKeyListener(this);
    }

    // Init
    private void init() {

        sprites = new ArrayList<Sprite>();
        placedSquares = new int[20][10];

        // Set all the arrays values to 0
        for(int i = 0; i < placedSquares.length; i++) {
            for(int j = 0; j < placedSquares[0].length; j++) {
                placedSquares[i][j] = 0;
            }
        }

        currentPiece = null;
    }

    // Called every time the world should update
    public void tick() {
        if(currentPiece == null)
            createNewPiece();
        // Invoke tick() for every sprite
        for(Sprite sprite : sprites) {
            sprite.tick();
        }
    }

    private void createNewPiece() {
        currentPiece = new Piece(4*GRID_SIZE, 18*GRID_SIZE, Piece.PieceType.T);
        sprites.add(currentPiece);
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

    @Override
    public void onKeyPressed(int key, int action) {
        if(action != 0) {
            if(key == 265)
                currentPiece.rotate();
        }
    }
}
