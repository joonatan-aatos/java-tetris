package logic;

public abstract class Sprite {

    protected int xPos;
    protected int yPos;

    protected Sprite(int x, int y) {
        xPos = x;
        yPos = y;
    }

    protected abstract void tick();

    public int getxPos() {
        return xPos;
    }

    public int getyPos() {
        return yPos;
    }
}
