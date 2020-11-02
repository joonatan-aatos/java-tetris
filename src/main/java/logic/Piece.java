package logic;

public class Piece extends Sprite {

    private int fallTime;
    private int fallTimer;
    private PieceType type;
    private int[][] shape;

    protected Piece(int x, int y, PieceType type) {
        super(x, y);
        this.type = type;
        shape = type.getShape();
        fallTime = 60;
        fallTimer = fallTime;
    }

    @Override
    protected void tick() {

        fallTimer--;
        if(fallTimer == 0) {
            fallTimer = fallTime;
//            fall();
            shape = rotatePiece(shape);
        }
    }

    private void fall() {
        yPos -= 10;
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
