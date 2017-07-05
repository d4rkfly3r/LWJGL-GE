package net.d4rkfly3r.engines.GameEngine.graphics;

import org.lwjgl.BufferUtils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.imageio.ImageIO;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;

public class TextureManager {

    private static final TextureManager INSTANCE = new TextureManager();
    private final Path defaultTextureLocation = Paths.get("assets", "textures");
    private final HashMap<String, TextureLink> textureLinks;
    private int stitchedTextureID;
    private BufferedImage stitchedTexture;
    private HashMap<String, Texture> textures;

    private TextureManager() {
        textureLinks = new HashMap<>();
        textures = new HashMap<>();
    }

    public static TextureManager i() {
        return INSTANCE;
    }

    public void scanAndStitchTextures() {
        scanTextures(defaultTextureLocation.toFile(), "");
        stitchTextures();
    }

    public void genGLTexture() {
        int[] pixels = new int[stitchedTexture.getWidth() * stitchedTexture.getHeight()];
        stitchedTexture.getRGB(0, 0, stitchedTexture.getWidth(), stitchedTexture.getHeight(), pixels, 0, stitchedTexture.getWidth());

        final ByteBuffer buffer = BufferUtils.createByteBuffer(stitchedTexture.getWidth() * stitchedTexture.getHeight() * 4); //4 for RGBA, 3 for RGB

        for (int y = 0; y < stitchedTexture.getHeight(); y++) {
            for (int x = 0; x < stitchedTexture.getWidth(); x++) {
                int pixel = pixels[y * stitchedTexture.getWidth() + x];
                buffer.put((byte) ((pixel >> 16) & 0xFF));
                buffer.put((byte) ((pixel >> 8) & 0xFF));
                buffer.put((byte) (pixel & 0xFF));
                buffer.put((byte) ((pixel >> 24) & 0xFF));
            }
        }

        buffer.flip();

        stitchedTextureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, stitchedTextureID);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, stitchedTexture.getWidth(), stitchedTexture.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

    }

    public int getStitchedTextureID() {
        return stitchedTextureID;
    }

    public Texture getTexture(final String name) {
        return textures.get(name);
    }

    private void stitchTextures() {
        final int indexSize = (int) Math.pow(2, Math.ceil(Math.log(Math.sqrt(textureLinks.size())) / Math.log(2)));
        final int size = indexSize * 64;
        System.out.println(indexSize + " | " + size);
        stitchedTexture = new BufferedImage(size, size, BufferedImage.TYPE_4BYTE_ABGR);

        int row = 0, column = 0, stitchX, stitchY;
        final Iterator<Map.Entry<String, TextureLink>> iterator = textureLinks.entrySet().iterator();
        for (int i = 0; i < textureLinks.size(); i++) {
            stitchX = column * 64;
            stitchY = row * 64;

            final Map.Entry<String, TextureLink> next = iterator.next();
            final TextureLink textureLink = next.getValue();
            textureLink.u = ((double) stitchX) / ((double) stitchedTexture.getWidth());
            textureLink.v = ((double) stitchY) / ((double) stitchedTexture.getHeight());
            textureLink.u2 = ((double) (stitchX + 64)) / ((double) stitchedTexture.getWidth());
            textureLink.v2 = ((double) (stitchY + 64)) / ((double) stitchedTexture.getHeight());
            final BufferedImage bufferedImage = textureLink.bufferedImage;
            for (int x = 0; x < 64; x++) {
                for (int y = 0; y < 64; y++) {
                    stitchedTexture.setRGB(stitchX + x, stitchY + y, bufferedImage.getRGB(x, y));
                }
            }

//            try {
//                ImageIO.write(stitchedTexture, "PNG", new File("debug/stitched.png"));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

            textures.put(next.getKey(), new Texture(textureLink.u, textureLink.v, textureLink.u2, textureLink.v2));
            next.getValue().bufferedImage = null;
            column++;
            if (column >= indexSize) {
                row++;
                column = 0;
            }
        }
    }

    private void scanTextures(final File folder, final String start) {
        for (final File file : folder.listFiles()) {
            if (file.isDirectory()) {
                scanTextures(file, start + file.getName() + "/");
            } else {
                // Image found... TODO: Verify is a supported image.
                try {
                    final String key = start + file.getName().substring(0, file.getName().lastIndexOf('.'));
                    final BufferedImage read = ImageIO.read(file);
                    if (read.getWidth() != 64 || read.getHeight() != 64) {
                        continue;
                    }
                    final TextureLink textureLink = new TextureLink(read);
                    System.out.println(key + " | " + textureLink);
                    textureLinks.put(key, textureLink);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class TextureLink {
        private BufferedImage bufferedImage;
        private double u, u2, v, v2;

        TextureLink(final BufferedImage bufferedImage) {
            this.bufferedImage = bufferedImage;
        }
    }
}
