package logic;

public class Piece {

    int xPos;
    int yPos;

    protected Piece() {

    }

    protected enum PieceType {
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
    }
}
