package visualizer;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.opengles.GLES32.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import org.lwjgl.opengles.GLES;
import org.lwjgl.system.MemoryUtil;

// This class is responsible for rendering stuff on the screen
// This is also where most of the openGL code is located
public class Renderer {

	// How many coordinates there are per vertex
	private final int COORDS_PER_VERTEX = 3;
	// 4 bytes per vertex
	private final int VERTEX_STRIDE = COORDS_PER_VERTEX * 4;

	private final Window window;
	private ShaderProgram triangleShaderProgram;
	private ShaderProgram spriteShaderProgram;
	private int triangleVaoID;
	private int triangleVboID;
	private int spriteVaoID;
	private int spriteVboID;
	private int tetrisPieceTextureHandle;

	private final float[] triangleVertices = new float[] {
			-0.5f, -0.5f, 0f,
			0f, 0.5f, 0f,
			0.5f, -0.5f, 0f
	};

	private final float[] spriteVertices = new float[] {
			-1f, -1f, 0f,
			-1f, 1f, 0f,
			1f, -1f, 0f,
			-1f, 1f, 0f,
			1f, -1f, 0f,
			1f, 1f, 0f
	};

	private FloatBuffer spriteTextureCoordinates;

	private final float[] spriteTextureCoordinateData = new float[] {
			// First triangle
			0.0f, 1.0f,
			0.0f, 0.0f,
			1.0f, 1.0f,
			// Second triangle
			0.0f, 0.0f,
			1.0f, 1.0f,
			1.0f, 0.0f,
	};

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

		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		// Set the clear color
		glClearColor(0.0f, 0.0f, 0.5f, 0.0f);

		// Create triangle shader programs
		triangleShaderProgram = new ShaderProgram();
		triangleShaderProgram.createVertexShader("src/main/glsl/triangle_vertex.glsl");
		triangleShaderProgram.createFragmentShader("src/main/glsl/triangle_fragment.glsl");
		triangleShaderProgram.link();

		// Create sprite shader programs
		spriteShaderProgram = new ShaderProgram();
		spriteShaderProgram.createVertexShader("src/main/glsl/sprite_vertex.glsl");
		spriteShaderProgram.createFragmentShader("src/main/glsl/sprite_fragment.glsl");
		spriteShaderProgram.link();

		// Triangle VBO

		// Create float buffer
		FloatBuffer triangleVerticesBuffer = MemoryUtil.memAllocFloat(triangleVertices.length);
		triangleVerticesBuffer.put(triangleVertices).flip();

		// Create VAO
		triangleVaoID = glGenVertexArrays();
		glBindVertexArray(triangleVaoID);

		// Create VBO
		triangleVboID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, triangleVboID);
		glBufferData(GL_ARRAY_BUFFER, triangleVerticesBuffer, GL_STATIC_DRAW);
		MemoryUtil.memFree(triangleVerticesBuffer);

		// Define the structure of the data and store it in one of the attribute lists of the VAO
		glVertexAttribPointer(0, COORDS_PER_VERTEX, GL_FLOAT, false, VERTEX_STRIDE, 0);

		// Unbind the VBO
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		// Unbind the VAO
		glBindVertexArray(0);
		
		// Sprite VBO

		// Create float buffer
		FloatBuffer spriteVerticesBuffer = MemoryUtil.memAllocFloat(spriteVertices.length);
		spriteVerticesBuffer.put(spriteVertices).flip();

		// Create VAO
		spriteVaoID = glGenVertexArrays();
		glBindVertexArray(spriteVaoID);

		// Create VBO
		spriteVboID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, spriteVboID);
		glBufferData(GL_ARRAY_BUFFER, spriteVerticesBuffer, GL_STATIC_DRAW);
		MemoryUtil.memFree(spriteVerticesBuffer);

		// Define the structure of the data and store it in one of the attribute lists of the VAO
		glVertexAttribPointer(0, COORDS_PER_VERTEX, GL_FLOAT, false, VERTEX_STRIDE, 0);

		// Unbind the VBO
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		// Unbind the VAO
		glBindVertexArray(0);

		// Textures

		// Load tetris piece texture
		tetrisPieceTextureHandle = TextureHelper.loadTexture("/tetrispiece.png");

		// Create sprite texture coordinates
		spriteTextureCoordinates = ByteBuffer.allocateDirect(spriteTextureCoordinateData.length * Float.BYTES)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
		spriteTextureCoordinates.put(spriteTextureCoordinateData).position(0);
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
	public void drawTriangle(float[] coords, float rotationAngle, float[] color) {

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

		// Set rotation uniform
		int rotationUniformLocation = glGetUniformLocation(triangleShaderProgram.getProgramId(), "angle");
		glUniform1f(rotationUniformLocation, rotationAngle);

		// Set color uniform
		int colorUniformLocation = glGetUniformLocation(triangleShaderProgram.getProgramId(), "color");
		glUniform4fv(colorUniformLocation, color);

		// Draw the vertices
		glDrawArrays(GL_TRIANGLES, 0, 3);

		// Restore state
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);

		// Unbind from the shader program
		triangleShaderProgram.unbind();
	}

	/**
	 * Draw rectangle at given position
	 */
	public void drawRectangle(float xPos, float yPos, float width, float height, float[] color) {
		drawTriangle(
				new float[]{
						xPos, yPos, 0f,
						xPos, yPos-height, 0f,
						xPos+width, yPos-height, 0f
				},
				0,
				color
		);
		drawTriangle(
				new float[] {
						xPos, yPos, 0f,
						xPos+width, yPos, 0f,
						xPos+width, yPos-height, 0f
				},
				0,
				color
		);
	}

	/**
	 * Draw an image at the given coordinates.
	 */
	public void drawImage(int textureHandle, float xPos, float yPos, float width, float height, float rotationAngle) {

		// Bind to a shader program
		spriteShaderProgram.bind();

		// Bind to the VAO
		glBindVertexArray(spriteVaoID);
		glEnableVertexAttribArray(0);

		// Set rotation uniform
		int rotationUniformLocation = glGetUniformLocation(spriteShaderProgram.getProgramId(), "angle");
		glUniform1f(rotationUniformLocation, 0);

		// Set scale uniform
		int scaleUniformLocation = glGetUniformLocation(spriteShaderProgram.getProgramId(), "scale");
		glUniform1f(scaleUniformLocation, 0.5f);

		// Set color uniform
		int colorUniformLocation = glGetUniformLocation(spriteShaderProgram.getProgramId(), "color");
		glUniform4fv(colorUniformLocation, new float[]{1f, 1f, 1f, 1});

		// Set texture uniform
		int textureUniformLocation = glGetUniformLocation(spriteShaderProgram.getProgramId(), "texture");
		// Tell the texture uniform sampler to user this texture in the shader by binding to texture unit 0
		glUniform1i(textureUniformLocation, 0);

		// Bind texture
		glBindTexture(GL_TEXTURE_2D, tetrisPieceTextureHandle);

		// Get and enable texture coordinates attribute
		int textureCoordinateAttribLocation = glGetAttribLocation(spriteShaderProgram.getProgramId(), "textureCoordinate");
		glEnableVertexAttribArray(textureCoordinateAttribLocation);

		// Set texture coordinates
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		spriteTextureCoordinates.position(0);
		glVertexAttribPointer(textureCoordinateAttribLocation, 2, GL_FLOAT, false, 0, spriteTextureCoordinates);

		// Draw the vertices
		glDrawArrays(GL_TRIANGLES, 0, 6);

		// Restore state
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
		glBindTexture(GL_TEXTURE_2D, 0);

		// Unbind from the shader program
		spriteShaderProgram.unbind();
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
}
