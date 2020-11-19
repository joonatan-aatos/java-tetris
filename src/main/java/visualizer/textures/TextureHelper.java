package visualizer.textures;


import de.matthiasmann.twl.utils.PNGDecoder;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static org.lwjgl.opengles.GLES20.*;
import static org.lwjgl.opengles.GLES32.glGenTextures;

public class TextureHelper {

    public static Texture loadTexture(String path) throws IOException {

        // Load and decode texture
        InputStream imageStream = TextureHelper.class.getResourceAsStream(path);
        PNGDecoder decoder = new PNGDecoder(imageStream);
        ByteBuffer buffer = ByteBuffer.allocateDirect(4*decoder.getWidth()*decoder.getHeight());;
        decoder.decode(buffer, decoder.getWidth()*4, PNGDecoder.Format.RGBA);
        buffer.flip();

        // OpenGL stuff
        int textureHandle = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureHandle);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        imageStream.close();

        return new Texture(path, textureHandle, decoder.getWidth(), decoder.getHeight());
    }
}