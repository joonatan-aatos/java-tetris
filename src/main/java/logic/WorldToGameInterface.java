package logic;

import userInput.KeyListenerInterface;

public interface WorldToGameInterface {
    public void addKeyListener(KeyListenerInterface keyListenerInterface);
    public void gameEnded();
}
