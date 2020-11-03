package logic;

public class Piece extends Sprite {

    private final int fallTime;
    private int fallTimer;
    private final int hardenTime;
    private int hardenTimer;
    private PieceType type;
    private int[][] shape;
    private boolean fallFast;
    private World world;

    protected Piece(int x, int y, PieceType type, World world) {
        super(x, y);
        this.type = type;
        this.world = world;
        shape = type.getShape();
        fallFast = false;
        fallTime = 60;
        fallTimer = fallTime;
        hardenTime = 60;
        hardenTimer = hardenTime;
    }

    @Override
    protected void tick() {

        if(canBeInPosition(xPos, yPos - 10, shape)) {
            fallTimer--;
        }
        else {
            hardenTimer--;
            if(hardenTimer <= 0) {
                harden();
            }
        }

        if(fallTimer <= 0) {
            fallTimer = fallFast ? fallTime / 15 : fallTime;
            fall();
        }
    }

    public void rotate() {
        int[][] rotatedShape = rotatePiece(shape);
        if(canBeInPosition(xPos, yPos, rotatedShape)) {
            shape = rotatedShape;
        }
        else if(canBeInPosition(xPos-10, yPos, rotatedShape)) {
            shape = rotatedShape;
            xPos -= 10;
        } else if(canBeInPosition(xPos+10, yPos, rotatedShape)) {
            shape = rotatedShape;
            xPos += 10;
        } else if(canBeInPosition(xPos, yPos+10, rotatedShape)) {
            shape = rotatedShape;
            yPos += 10;
        }
    }

    public void move(boolean right) {
        int dx = -10;
        if(right)
            dx = 10;

        if(canBeInPosition(xPos + dx, yPos, shape)) {
            xPos += dx;
        }
    }

    public void fallFast(boolean fallFast) {
        this.fallFast = fallFast;
        fallTimer = 0;
    }

    private void fall() {
        if(canBeInPosition(xPos, yPos - 10, shape))
            yPos -= 10;
    }

    private void harden() {
        world.hardenAPiece(this);
    }

    private boolean canBeInPosition(int newX, int newY, int[][] newShape) {

        // Center distance is 1 except in the O-piece
        int centerDistance = 1;
        if(newShape.length == 2) {
            centerDistance = 0;
        }
        // Check if every block of the piece can be in the next position
        for(int i = 0; i < newShape.length; i++) {
            for(int j = 0; j < newShape[0].length; j++) {
                if(newShape[i][j] != 0) {
                    int x = newX + (j - centerDistance) * World.GRID_SIZE;
                    int y = newY + ((newShape.length - i - 1) - centerDistance) * World.GRID_SIZE;
                    if(x < 0 || x >= World.WORLD_WIDTH*World.GRID_SIZE ||
                            y < 0 || y >= World.WORLD_HEIGHT*World.GRID_SIZE ||
                            world.getPlacedSquares()[y/World.GRID_SIZE][x/World.GRID_SIZE] != 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private int[][] rotatePiece(int[][] piece) {

        int[][] newPiece = new int[piece.length][piece[0].length];

        if(piece.length == 2) {
            return piece;
        }
        else if(piece.length == 3) {
            newPiece[0][0] = piece[2][0];
            newPiece[0][1] = piece[1][0];
            newPiece[0][2] = piece[0][0];
            newPiece[1][2] = piece[0][1];
            newPiece[2][2] = piece[0][2];
            newPiece[2][1] = piece[1][2];
            newPiece[2][0] = piece[2][2];
            newPiece[1][0] = piece[2][1];
            newPiece[1][1] = piece[1][1];
        }
        else {
            newPiece[0][0] = piece[3][0];
            newPiece[0][1] = piece[2][0];
            newPiece[0][2] = piece[1][0];
            newPiece[0][3] = piece[0][0];
            newPiece[1][3] = piece[0][1];
            newPiece[2][3] = piece[0][2];
            newPiece[3][3] = piece[0][3];
            newPiece[3][2] = piece[1][3];
            newPiece[3][1] = piece[2][3];
            newPiece[3][0] = piece[3][3];
            newPiece[2][0] = piece[3][2];
            newPiece[1][0] = piece[3][1];
            newPiece[1][1] = piece[2][1];
            newPiece[1][2] = piece[1][1];
            newPiece[2][2] = piece[1][2];
            newPiece[2][1] = piece[2][2];

        }

        return newPiece;
    }

    public PieceType getPieceType() {
        return type;
    }

    public int[][] getPieceShape() {
        return shape;
    }

    public enum PieceType {
        I(new int[][]{
                {0, 0, 0, 0},
                {1, 1, 1, 1},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        }, 0),
        O(new int[][]{
                {1, 1},
                {1, 1}
        }, 1),
        T(new int[][]{
                {0, 1, 0},
                {1, 1, 1},
                {0, 0, 0}
        }, 2),
        S(new int[][]{
                {0, 1, 1},
                {1, 1, 0},
                {0, 0, 0}
        }, 3),
        Z(new int[][]{
                {1, 1, 0},
                {0, 1, 1},
                {0, 0, 0}
        }, 4),
        J(new int[][]{
                {1, 0, 0},
                {1, 1, 1},
                {0, 0, 0}
        }, 5),
        L(new int[][]{
                {0, 0, 1},
                {1, 1, 1},
                {0, 0, 0}
        }, 6);

        private int[][] pieceShape;
        private int typeIndex;

        private PieceType(int[][] pieceShape, int typeIndex) {
            this.pieceShape = pieceShape;
            this.typeIndex = typeIndex;
        }

        public int[][] getShape() {
            return pieceShape;
        }

        public int getTypeIndex() {
            return typeIndex;
        }
    }
}
