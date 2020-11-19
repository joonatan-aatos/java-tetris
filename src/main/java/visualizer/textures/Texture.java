package visualizer.textures;

public class Texture {

    public final String path;
    public final int textureHandle;
    public final int width;
    public final int height;

    protected Texture(String path, int textureHandle, int width, int height) {

        this.path = path;
        this.textureHandle = textureHandle;
        this.width = width;
        this.height = height;
    }
}
