package net.d4rkfly3r.engines.GameEngine;

import org.lwjgl.glfw.GLFWCursorPosCallbackI;
import org.lwjgl.glfw.GLFWKeyCallbackI;
import org.lwjgl.glfw.GLFWMouseButtonCallbackI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.lwjgl.glfw.GLFW.*;

public class InputManager {

    private final HashMap<Integer, List<Function<Long, Function<Integer, Function<Integer, Consumer<Integer>>>>>> keyBindings;
    private final GLFWKeyCallbackI glfwKeyCallbackI;
    private final GLFWCursorPosCallbackI glfwCursorPosCallbackI;
    private final GLFWMouseButtonCallbackI glfwMouseButtonCallbackI;
    private double lastMouseX;
    private double lastMouseY;

    public InputManager() {
        keyBindings = new HashMap<>();
        glfwKeyCallbackI = this::getGLFWKeyCallback;
        glfwCursorPosCallbackI = this::getGLFWCursorPosCallback;
        glfwMouseButtonCallbackI = this::getGLFWMouseButtonCallback;

        this.generateDefaultKeyBindings();
    }

    private void getGLFWMouseButtonCallback(long windowHandle, int button, int action, int mods) {

    }


    private void getGLFWCursorPosCallback(long windowHandle, double xPos, double yPos) {
        this.lastMouseX = xPos;
        this.lastMouseY = yPos;
    }

    private void getGLFWKeyCallback(long windowHandle, int key, int scanCode, int action, int mods) {
        if (this.keyBindings.containsKey(key)) {
            this.keyBindings.get(key).forEach(con -> con.apply(windowHandle).apply(key).apply(action).accept(mods));//.accept(key, action));
        }
    }

    private void generateDefaultKeyBindings() {
        this.addKeyBinding(GLFW_KEY_ESCAPE, w -> k -> a -> m -> {
            if (a == GLFW_RELEASE) {
                glfwSetWindowShouldClose(w, true);
            }
        });
    }

    public void addKeyBinding(int key, final Function<Long, Function<Integer, Function<Integer, Consumer<Integer>>>> consumer) {
        if (!this.keyBindings.containsKey(key)) {
            this.keyBindings.put(key, new ArrayList<>());
        }
        this.keyBindings.get(key).add(consumer);
    }

    public void activateCallbacks(long windowHandle) {
        glfwSetKeyCallback(windowHandle, glfwKeyCallbackI);
        glfwSetMouseButtonCallback(windowHandle, glfwMouseButtonCallbackI);
        glfwSetCursorPosCallback(windowHandle, glfwCursorPosCallbackI);
    }

}
