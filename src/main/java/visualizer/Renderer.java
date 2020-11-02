package visualizer;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.opengles.GLES32.*;

import java.nio.FloatBuffer;

import org.lwjgl.opengles.GLES;
import org.lwjgl.system.MemoryUtil;

// This class is responsible for rendering stuff on the screen
// This is also where most of the openGL code is located
public class Renderer {

	private final Window window;
	private ShaderProgram triangleShaderProgram;
	private int triangleVaoID;
	private int triangleVboID;

	//TODO: Fix vertex shader code
	private final String TRIANGLE_VERTEX_SHADER_CODE = "" +
			"\n" +
			"#version 300 es\n" +
			"\n" +
			"uniform float angle;" +
			"\n" +
			"layout (location=0) in vec3 position;\n" +
			"\n" +
			"void main()\n" +
			"{\n" +
			"mat2 rotationMatrix;\n" +
			"rotationMatrix[0][0] = cos(angle);\n" +
			"rotationMatrix[0][1] = sin(angle);\n" +
			"rotationMatrix[1][0] = -sin(angle);\n" +
			"rotationMatrix[1][1] = cos(angle);\n" +
			"\n" +
			"vec2 newPosition = rotationMatrix * vec2(position.x, position.y);\n" +
			"gl_Position = vec4(newPosition, position.z, 1.0);\n" +
			"}\n" +
			"\n";

	private final String TRIANGLE_FRAGMENT_SHADER_CODE = ""
			+ "\n"
			+ "#version 300 es\n"
			+ "\n"
			+ "precision mediump float;\n"
			+ "\n"
			+ "out vec4 fragColor;\n"
			+ "\n"
			+ "void main()\n"
			+ "{\n"
			+ "fragColor = vec4(1.0, 0.0, 0.0, 1.0);\n"
			+ "}\n"
			+ "\n";


	public Renderer(Window window) throws Exception {
		this.window = window;
		init();
	}

	private void init() throws Exception {
		/*
		This line is critical for LWJGL's interoperation with GLFW's
		OpenGL context, or any context that is managed externally.
		LWJGL detects the context that is current in the current thread,
		creates the GLCapabilities instance and makes the OpenGL
		bindings available for use.
		 */
		GLES.createCapabilities();

		// Set the clear color
		glClearColor(0.0f, 0.0f, 1.0f, 0.0f);

		// Create shader programs
		triangleShaderProgram = new ShaderProgram();
		triangleShaderProgram.createVertexShader(TRIANGLE_VERTEX_SHADER_CODE);
		triangleShaderProgram.createFragmentShader(TRIANGLE_FRAGMENT_SHADER_CODE);
		triangleShaderProgram.link();

		// Define vertices for triangle vbo
		float[] triangleVertices = new float[] {
				-0.5f, -0.5f, 0f,
				0f, 0.5f, 0f,
				0.5f, -0.5f, 0f
		};

		// Create float buffer
		FloatBuffer verticesBuffer = MemoryUtil.memAllocFloat(triangleVertices.length);
		verticesBuffer.put(triangleVertices).flip();

		// Create VAO
		triangleVaoID = glGenVertexArrays();
		glBindVertexArray(triangleVaoID);

		// Create VBO
		triangleVboID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, triangleVboID);
		glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
		MemoryUtil.memFree(verticesBuffer);

		// Define the structure of the data and store it in one of the attribute lists of the VAO
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

		// Unbind the VBO
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		// Unbind the VAO
		glBindVertexArray(0);
	}

	/**
	 * Clear the frame buffer and update the viewport
	 */
	public void reset() {
		// Clear the frame buffer
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		// Resize the viewport if necessary
		if(window.isResized()) {
			glViewport(0, 0, window.getWidth(), window.getHeight());
			window.setResized(true);
		}
	}

	/**
	 * Swap the color buffers and poll for window events.
	 */
	public void draw() {
		// Swap the color buffers
		window.swapBuffers();

		// Poll for window events.
		// The key callback above will only be invoked during this call.
		glfwPollEvents();
	}

	/**
	 * Draw a triangle at the given coordinates with the given color
	 */
	public void drawTriangle(float[] coords, float rotationAngle) {

		// Bind to a shader program
		triangleShaderProgram.bind();

		// Bind to the VBO to change VBO data
		glBindBuffer(GL_ARRAY_BUFFER, triangleVboID);

		// Change VBO data to given coordinates
		glBufferSubData(GL_ARRAY_BUFFER, 0, coords);

		// Unbind VBO
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		// Bind to the VAO
		glBindVertexArray(triangleVaoID);
		glEnableVertexAttribArray(0);

		// Set uniforms
		int rotationUniformLocation = glGetUniformLocation(triangleShaderProgram.getProgramId(), "angle");
		glUniform1f(rotationUniformLocation, rotationAngle);

		// Draw the vertices
		glDrawArrays(GL_TRIANGLES, 0, 3);

		// Restore state
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);

		// Unbind from the shader program
		triangleShaderProgram.unbind();
	}

	/**
	 * Draw a rectangle at the given coordinates with the given color
	 */
	public void drawRectangle() {

	}

	public void cleanup() {

		// Clean up shader programs
		if (triangleShaderProgram != null) {
			triangleShaderProgram.cleanup();
		}

		glDisableVertexAttribArray(0);

		// Delete the VBO
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glDeleteBuffers(triangleVboID);

		// Delete the VAO
		glBindVertexArray(0);
		glDeleteVertexArrays(triangleVaoID);
	}

	/*
	// This was for testing
	public void render() {

		// Clear the frame buffer
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		// Resize the viewport if necessary
		if(window.isResized()) {
			glViewport(0, 0, window.getWidth(), window.getHeight());
			window.setResized(true);
		}

		// Bind to a shader program
		shaderProgram.bind();

		// Bind to the VAO
		glBindVertexArray(triangleVaoID);
		glEnableVertexAttribArray(0);

		// Set a uniform
		int uniformLocation = glGetUniformLocation(shaderProgram.getProgramId(), "rotationAngle");
		glUniform1f(uniformLocation, 0f);

		// Draw the vertices
		glDrawArrays(GL_TRIANGLES, 0, 3);

		// Restore state
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);

		// Unbind from the shader program
		shaderProgram.unbind();

		// Swap the color buffers
		window.swapBuffers();
		// Poll for window events.
		// The key callback above will only be invoked during this call.
		glfwPollEvents();
	}
	 */
}
