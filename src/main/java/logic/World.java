package logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

// This class handles all the basic game logic and stores most of the relevant game objects
public class World {

    public static final int WORLD_WIDTH = 10;
    public static final int WORLD_HEIGHT = 20;
    public static final int PLAYABLE_WORLD_HEIGHT = 25;
    // Coordinates between two adjacent squares
    public static final int GRID_SIZE = 10;

    private final Piece.PieceType[] pieceTypes = new Piece.PieceType[]{
            Piece.PieceType.I,
            Piece.PieceType.J,
            Piece.PieceType.L,
            Piece.PieceType.O,
            Piece.PieceType.S,
            Piece.PieceType.T,
            Piece.PieceType.Z,
    };

    // Other
    private final double fallSpeedMultiplier = 60d/Math.log(201);

    private int[][] placedSquares;
    private Piece currentPiece;
    private Piece.PieceType storedPieceType;
    private boolean canStorePiece;
    private TetrisPlayer tetrisPlayer;
    private WorldToGameInterface game;
    private ArrayList<Piece.PieceType> currentPieceList;
    private ArrayList<Piece.PieceType> nextPieceList;
    private int rowsCleared;
    private int currentFallTime;
    private boolean rowCleared;
    private boolean pieceHardened;

    public World(WorldToGameInterface game) {

        this.game = game;
        init();
        game.addKeyListener(tetrisPlayer);
    }

    // Init
    private void init() {

        placedSquares = new int[PLAYABLE_WORLD_HEIGHT][WORLD_WIDTH];

        // Set all the arrays values to 0
        for(int i = 0; i < placedSquares.length; i++) {
            for(int j = 0; j < placedSquares[0].length; j++) {
                placedSquares[i][j] = 0;
            }
        }

        currentPiece = null;
        storedPieceType = null;
        canStorePiece = false;
        rowCleared = false;
        pieceHardened = false;

        tetrisPlayer = new TetrisPlayer(this);

        currentPieceList = new ArrayList<Piece.PieceType>(Arrays.asList(pieceTypes));
        nextPieceList = new ArrayList<Piece.PieceType>(Arrays.asList(pieceTypes));
        Collections.shuffle(currentPieceList);
        Collections.shuffle(nextPieceList);

        rowsCleared = 0;
        currentFallTime = -1;
        updateFallTime();
    }

    // Called every time the world should update
    public void tick() {

        if(rowCleared)
            rowCleared = false;
        if(pieceHardened)
            pieceHardened = false;

        if(currentPiece == null)
            createNewPiece();
        // Invoke tick() for current piece
        currentPiece.tick();

        checkForCompleteRows();
    }

    private void checkForCompleteRows() {
        for(int i = placedSquares.length - 1; i >= 0; i--) {
            for(int j = 0; j < placedSquares[0].length; j++) {
                if(placedSquares[i][j] == 0)
                    break;

                if(j == placedSquares[0].length-1) {
                    // Row is complete
                    removeRow(i);
                    rowsCleared++;
                    updateFallTime();
                    rowCleared = true;
                    // Check the same row again
                    i++;
                    break;
                }
            }
        }
        if(rowCleared)
            System.out.println("Rows cleared: "+rowsCleared);
    }

    // Update fall time
    private void updateFallTime() {

        int calculatedFallTime = 60 - (int) Math.round(fallSpeedMultiplier*Math.log(rowsCleared+1));

        if(calculatedFallTime < 0)
            calculatedFallTime = 0;

        if(currentFallTime != calculatedFallTime)
            currentFallTime = calculatedFallTime;
    }

    // Remove a row
    private void removeRow(int height) {

        int[][] newPlacedSquares = new int[placedSquares.length][placedSquares[0].length];

        for(int i = 0; i < placedSquares.length-1; i++) {
            if(i >= height) {
                newPlacedSquares[i] = placedSquares[i+1];
            }
            else {
                newPlacedSquares[i] = placedSquares[i];
            }
        }

        // Empty the top row
        for(int i = 0; i < newPlacedSquares[0].length; i++) {
            newPlacedSquares[newPlacedSquares.length-1][i] = 0;
        }

        placedSquares = newPlacedSquares;
    }

    private void createNewPiece() {
        currentPiece = new Piece(4*GRID_SIZE, WORLD_HEIGHT*GRID_SIZE, currentPieceList.remove(0), this, currentFallTime);
        tetrisPlayer.updateCurrentPiece(currentPiece);

        if(currentPieceList.size() == 0) {
            currentPieceList = nextPieceList;
            nextPieceList = new ArrayList<Piece.PieceType>(Arrays.asList(pieceTypes));
            Collections.shuffle(nextPieceList);
        }

        if(!canStorePiece)
            canStorePiece = true;
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
                            y >= 0 && y < PLAYABLE_WORLD_HEIGHT*GRID_SIZE &&
                            placedSquares[y/GRID_SIZE][x/GRID_SIZE] == 0) {

                        placedSquares[y/GRID_SIZE][x/GRID_SIZE] = piece.getPieceType().getTypeIndex()+1;
                    }
                }
            }
        }

        currentPiece = null;
        pieceHardened = true;
    }

    public void storeCurrentPiece() {

        if(!canStorePiece)
            return;

        Piece.PieceType newPieceType = storedPieceType;
        storedPieceType = currentPiece.getPieceType();

        // Create compleately new piece if there is no stored piece
        if(newPieceType == null) {
            createNewPiece();
        }
        else {
            currentPiece = new Piece(4*GRID_SIZE, 19*GRID_SIZE, newPieceType, this, currentFallTime);
            tetrisPlayer.updateCurrentPiece(currentPiece);
        }

        canStorePiece = false;
    }

    /**
     * @return Return a copy of the current piece
     */
    public Piece getCurrentPiece() {
        return currentPiece;
    }

    /**
     * @return Return a copy of the current placedSquares object
     */
    public int[][] getPlacedSquares() {
        return placedSquares.clone();
    }

    public Piece.PieceType getNextPieceType() {
        return currentPieceList.size() == 0 ? nextPieceList.get(0) : currentPieceList.get(0);
    }

    public Piece.PieceType getStoredPieceType() {
        return storedPieceType;
    }

    public boolean wasRowCleared() {
        return rowCleared;
    }

    public boolean wasPieceHardened() {
        return pieceHardened;
    }
}
