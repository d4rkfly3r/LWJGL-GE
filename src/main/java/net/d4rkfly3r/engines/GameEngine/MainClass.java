package net.d4rkfly3r.engines.GameEngine;

import net.d4rkfly3r.engines.GameEngine.graphics.TextureManager;
import net.d4rkfly3r.engines.GameEngine.world.GrassTile;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class MainClass implements Runnable {

    private final InputManager inputManager;
    private long windowHandle;
    private int windowWidth, windowHeight;
    private GrassTile grassTile;

    public MainClass() {
        TextureManager.i().scanAndStitchTextures();
        this.inputManager = new InputManager();
        grassTile = new GrassTile(100, 100);
    }

    public static void main(String[] args) {
        new MainClass().run();
    }

    public void run() {
        System.out.println("LWJGL Version: " + Version.getVersion());

        init();
        postInit();
        loop();

        glfwFreeCallbacks(windowHandle);
        glfwDestroyWindow(windowHandle);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() {

        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);
        glfwWindowHint(GLFW_DECORATED, GLFW_FALSE);

        windowHandle = glfwCreateWindow(800, 600, "D4 Game Engine!", NULL, NULL);
        if (windowHandle == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        inputManager.activateCallbacks(windowHandle);

        try (final MemoryStack stack = stackPush()) {
            final IntBuffer pWidth = stack.mallocInt(1); // int*
            final IntBuffer pHeight = stack.mallocInt(1); // int*

            glfwGetWindowSize(windowHandle, pWidth, pHeight);

            final GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            windowWidth = vidMode.width();
            windowHeight = vidMode.height();

            glfwSetWindowPos(
                    windowHandle,
                    (vidMode.width() - pWidth.get(0)) / 2,
                    (vidMode.height() - pHeight.get(0)) / 2
            );
        }
        glfwMakeContextCurrent(windowHandle);
        glfwSwapInterval(1);
        glfwShowWindow(windowHandle);
    }

    private void postInit() {
        createCapabilities();

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, windowWidth, windowHeight, 0, 10, -10);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        glShadeModel(GL_SMOOTH);
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClearDepth(1.0);
        glEnable(GL_TEXTURE_2D);

//        glEnable(GL_BLEND);
//        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        TextureManager.i().genGLTexture();
//        framebuffer = new Framebuffer(width, height);

    }

    private void loop() {

        while (!glfwWindowShouldClose(windowHandle)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            glBindTexture(GL_TEXTURE_2D, 0);
            glPushMatrix();

            grassTile.render();

            glPopMatrix();

            glfwSwapBuffers(windowHandle);
            glfwPollEvents();
        }
    }
}
