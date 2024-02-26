

package csc133;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static csc133.spot.WIN_HEIGHT;
import static csc133.spot.WIN_WIDTH;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.system.MemoryUtil.*;


public class Main {

    //static spot spot = new spot();
    static GLFWKeyCallback keyCallback;
    static GLFWFramebufferSizeCallback fbCallback;

    static long window;

    private static final int OGL_MATRIX_SIZE = 16;
    // call glCreateProgram() here - we have no gl-context here
    int shader_program;
    Matrix4f viewProjMatrix = new Matrix4f();
    FloatBuffer myFloatBuffer = BufferUtils.createFloatBuffer(OGL_MATRIX_SIZE);
    int vpMatLocation = 0, renderColorLocation = 0;
    public static void main(String[] args) {
        new csc133.slWindow().slWindow(WIN_WIDTH, WIN_HEIGHT);
        window = slWindow.get(WIN_WIDTH, WIN_HEIGHT);
        new Main().render();
    } // public static void main(String[] args)
    void render() {
        try {
            renderLoop();
            slWindow.destroy();
            keyCallback.free();
            fbCallback.free();
        } finally {
            glfwTerminate();
            glfwSetErrorCallback(null).free();
        }
    } // void render()


    void renderLoop() {
        glfwPollEvents();
        initOpenGL();
        renderObjects();
        /* Process window messages in the main thread */
        while (!glfwWindowShouldClose(window)) {
            glfwWaitEvents();
        }
    } // void renderLoop()
    void initOpenGL() {
        GL.createCapabilities();
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        glViewport(0, 0, WIN_WIDTH, WIN_HEIGHT);
        glClearColor(0.0f, 0.0f, 1.0f, 1.0f);
        this.shader_program = glCreateProgram();
        int vs = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vs,
                "uniform mat4 viewProjMatrix;" +
                        "void main(void) {" +
                        " gl_Position = viewProjMatrix * gl_Vertex;" +
                        "}");
        glCompileShader(vs);
        glAttachShader(shader_program, vs);
        int fs = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fs,
                "uniform vec3 color;" +
                        "void main(void) {" +
                        " gl_FragColor = vec4(0.2f, 0.6f, 0.8f, 1.0f);" +
                        "}");
        glCompileShader(fs);
        glAttachShader(shader_program, fs);
        glLinkProgram(shader_program);
        glUseProgram(shader_program);
        vpMatLocation = glGetUniformLocation(shader_program, "viewProjMatrix");
        return;
    } // void initOpenGL()
    void renderObjects() {
        float glUni_v0 = 1.0f, glUni_v1 = 0.498f, glUni_v2 = 0.153f;
        final int MAX_ROWS = 7, MAX_COLS = 5;
        final int vps = 4, fpv = 2, ips = 6;
        int orthoBottom = -100, zFar = 10;
        int glVertexSize = 2;


        float[] vertices = new float[MAX_COLS * MAX_ROWS * vps * fpv];
        int[] indices = new int[MAX_COLS * MAX_ROWS * ips];

        final float length = 10.0f, offset = 10.0f, padding = 5.0f;
        float xmin = offset;
        float xmax = xmin + length;
        float ymax = WIN_HEIGHT - offset;
        float ymin = ymax - length;

        int index = 0;

        for (int row = 0; row < MAX_ROWS; row++){
            for (int col = 0; col < MAX_COLS; col++){
                vertices[index++] = xmin;
                vertices[index++] = ymin;
                vertices[index++] = xmax;
                vertices[index++] = ymin;
                vertices[index++] = xmax;
                vertices[index++] = ymax;
                vertices[index++] = xmin;
                vertices[index++] = ymax;
                xmin = xmax + padding;
                xmax = xmin + length;
            }
            xmin = offset;
            xmax = offset + length;
            ymax = ymin - padding;
            ymin = ymax - length;
        }

        int v_index = 0, my_index = 0;
        while (my_index < indices.length){
            indices[my_index++] = v_index;
            indices[my_index++] = v_index+1;
            indices[my_index++] = v_index+2;
            indices[my_index++] = v_index;
            indices[my_index++] = v_index+2;
            indices[my_index++] = v_index+3;
            v_index += vps;
        }



        while (!glfwWindowShouldClose(window)) {
            glfwPollEvents();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            int vbo = glGenBuffers();
            int ibo = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, vbo);
            glBufferData(GL_ARRAY_BUFFER, (FloatBuffer) BufferUtils.
                    createFloatBuffer(vertices.length).
                    put(vertices).flip(), GL_STATIC_DRAW);
            glEnableClientState(GL_VERTEX_ARRAY);
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, (IntBuffer) BufferUtils.
                    createIntBuffer(indices.length).
                    put(indices).flip(), GL_STATIC_DRAW);
            glVertexPointer(glVertexSize, GL_FLOAT, 0, 0L);
            viewProjMatrix.setOrtho(0, WIN_WIDTH, orthoBottom, WIN_HEIGHT, 0, zFar);
            glUniformMatrix4fv(vpMatLocation, false,
                    viewProjMatrix.get(myFloatBuffer));
            glUniform3f(renderColorLocation, glUni_v0, glUni_v1, glUni_v2);
            glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
            int VTD = vertices.length; // need to process 6 Vertices To Draw 2 triangles
            glDrawElements(GL_TRIANGLES, VTD, GL_UNSIGNED_INT, 0L);
            glfwSwapBuffers(window);
        }
    } // renderObjects
}
