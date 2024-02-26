
package csc133;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.system.MemoryUtil.NULL;

public class slWindow {

    private static long my_oglWindow = 0;
    private static int WIN_WIDTH = 1000, WIN_HEIGHT = 1000;

    static GLFWErrorCallback errorCallback;
    static GLFWKeyCallback keyCallback;
    static GLFWFramebufferSizeCallback fbCallback;
    static int WIN_POS_X = 0, WIN_POX_Y = 0;

    void slWindow(int win_width, int win_height) {
        System.out.println("Call to slWindow:: (width, height) == ("
                        + win_width + ", " + win_height +") received!");
    }

    public static long get(){
        int windowHint = 8;

        if (my_oglWindow == 0){
            //Main.initGLFWindow();
            glfwSetErrorCallback(errorCallback =
                    GLFWErrorCallback.createPrint(System.err));
            if (!glfwInit())
                throw new IllegalStateException("Unable to initialize GLFW");
            glfwDefaultWindowHints();
            glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
            glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
            glfwWindowHint(GLFW_SAMPLES, windowHint);
            my_oglWindow = glfwCreateWindow(WIN_WIDTH, WIN_HEIGHT, "CSC 133", NULL, NULL);
            if (my_oglWindow == NULL)
                throw new RuntimeException("Failed to create the GLFW window");
            glfwSetKeyCallback(my_oglWindow, keyCallback = new GLFWKeyCallback() {
                @Override
                public void invoke(long my_oglWindow, int key, int scancode, int action, int
                        mods) {
                    if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                        glfwSetWindowShouldClose(my_oglWindow, true);
                }
            });
            glfwSetFramebufferSizeCallback(my_oglWindow, fbCallback = new
                    GLFWFramebufferSizeCallback() {
                        @Override
                        public void invoke(long my_oglWindow, int w, int h) {
                            if (w > 0 && h > 0) {
                                WIN_WIDTH = w;
                                WIN_HEIGHT = h;
                            }
                        }
                    });
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            glfwSetWindowPos(my_oglWindow, WIN_POS_X, WIN_POX_Y);
            glfwMakeContextCurrent(my_oglWindow);
            int VSYNC_INTERVAL = 1;
            glfwSwapInterval(VSYNC_INTERVAL);
            glfwShowWindow(my_oglWindow);
        }
        return my_oglWindow;
    }
    public static long get(int wid, int ht){
        WIN_WIDTH = wid;
        WIN_HEIGHT = ht;
        return get();
    }

    public static void destroy(){
        glfwDestroyWindow(my_oglWindow);
    }
}

