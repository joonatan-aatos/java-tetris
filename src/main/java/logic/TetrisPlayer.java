package logic;

import userInput.KeyListenerInterface;

public class TetrisPlayer implements KeyListenerInterface {

    Piece currentPiece;
    World world;

    protected TetrisPlayer(World world) {
        this.world = world;
    }

    protected void updateCurrentPiece(Piece p) {
        currentPiece = p;
    }

    @Override
    public void onKeyPressed(int key, int action) {

        if(currentPiece == null)
            return;

        // Arrow down
        if(key == 264) {
            if(action == 1) {
                currentPiece.fallFast(true);
            }
            else if(action == 0) {
                currentPiece.fallFast(false);
            }
        }
        // Arrow up
        else if(key == 265) {
            if(action == 1) {
                currentPiece.rotate();
            }
        }
        // Arrow left and right
        else if(key == 263 || key == 262) {
            if(action != 0) {
                currentPiece.move(key == 262);
            }
        }
        // Space
        else if(key == 32) {
            if(action == 1) {
                currentPiece.fallDown();
            }
        }
        // C
        else if(key == 67) {
            if(action == 1) {
                world.storeCurrentPiece();
            }
        }
    }
}
