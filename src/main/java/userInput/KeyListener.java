package userInput;

import org.lwjgl.glfw.GLFWKeyCallback;

import java.util.ArrayList;

public class KeyListener extends GLFWKeyCallback {

    ArrayList<KeyListenerInterface> keyListenerInterfaces;

    public KeyListener() {
        keyListenerInterfaces = new ArrayList<KeyListenerInterface>();
    }

    public void addKeyListener(KeyListenerInterface keyListenerInterface) {
        keyListenerInterfaces.add(keyListenerInterface);
    }

    @Override
    public void invoke(long window, int key, int scancode, int action, int mods) {
        for(KeyListenerInterface keyListenerInterface : keyListenerInterfaces) {
            keyListenerInterface.onKeyPressed(key, action);
        }
    }
}
