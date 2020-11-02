package logic;

import java.util.ArrayList;

// This class handles all the basic game logic and stores most of the relevant game objects
public class World {

    public static final int WORLD_WIDTH = 10;
    public static final int WORLD_HEIGHT = 20;
    // Coordinates between two adjacent squares
    public static final int GRID_SIZE = 10;

    // List that stores all sprites
    private ArrayList<Sprite> sprites;
    private int[][] placedSquares;

    public World() {

        init();

        // Temporary
        placedSquares[1][5] = 1;
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
    }

    // Called every time the world should update
    public void tick() {
        // Invoke tick() for every sprite
        for(Sprite sprite : sprites) {
            sprite.tick();
        }
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
