package net.d4rkfly3r.engines.GameEngine.graphics;

import org.lwjgl.BufferUtils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.imageio.ImageIO;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;

public class TextureManager {

    private final Path defaultTextureLocation = Paths.get("assets", "textures");
    private final ArrayList<TextureLink> textureLinks;
    private int stitchedTextureID;

    public TextureManager() {
        textureLinks = new ArrayList<>();
    }

    public int getStitchedTextureID() {
        return stitchedTextureID;
    }

    public void scanAndInjectTextures() {
        Class<?> classLoaderClass = this.getClass().getClassLoader().getClass();
        while (classLoaderClass != ClassLoader.class) {
            classLoaderClass = classLoaderClass.getSuperclass();
        }

        try {
            final File initial = new File(this.getClass().getClassLoader().getResources("").nextElement().toURI());
            for (File file1 : initial.listFiles()) {
                System.out.println(file1);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        try {
            final Field classesField = classLoaderClass.getDeclaredField("classes");
            classesField.setAccessible(true);
            final List<Class<?>> classList = new ArrayList<>((Collection<? extends Class<?>>) classesField.get(this.getClass().getClassLoader()));
            for (final Class<?> clazz : classList) {
                for (final Field field : clazz.getDeclaredFields()) {
                    if (field.isAnnotationPresent(TextureMeta.class) && field.getType().equals(Texture.class)) {
                        loadTextureFromFieldAnnotation(field);
                    }
                }
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        final int indexSize = (int) Math.pow(2, Math.ceil(Math.log(Math.sqrt(textureLinks.size())) / Math.log(2)));
        final int size = indexSize * 64;
        System.out.println(indexSize + " | " + size);
        final BufferedImage stitchedTexture = new BufferedImage(size, size, BufferedImage.TYPE_4BYTE_ABGR);

        int row = 0, column = 0, stitchX, stitchY;

        for (int i = 0; i < textureLinks.size(); i++) {
            stitchX = column * 64;
            stitchY = row * 64;

            final TextureLink textureLink = textureLinks.get(i);
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

            column++;
            if (column > indexSize) {
                row++;
                column = 0;
            }
        }


        int[] pixels = new int[stitchedTexture.getWidth() * stitchedTexture.getHeight()];
        stitchedTexture.getRGB(0, 0, stitchedTexture.getWidth(), stitchedTexture.getHeight(), pixels, 0, stitchedTexture.getWidth());

        ByteBuffer buffer = BufferUtils.createByteBuffer(stitchedTexture.getWidth() * stitchedTexture.getHeight() * 4); //4 for RGBA, 3 for RGB

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


        for (final TextureLink textureLink : textureLinks) {
            textureLink.field.setAccessible(true);
            try {
                textureLink.field.set(null, new Texture(textureLink.u, textureLink.v, textureLink.u2, textureLink.v2));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadTextureFromFieldAnnotation(final Field field) {
        final String path = field.getAnnotation(TextureMeta.class).value();
        final Path texturePath = defaultTextureLocation.resolve(path + (path.endsWith(".png") ? "" : ".png"));
        if (Files.notExists(texturePath)) {
            System.err.println("Necessary texture file NOT found: " + texturePath);
            // TODO Missing File Handling... Maybe add a generic PINK texture or something?
            return;
        }

        try {
            final BufferedImage read = ImageIO.read(texturePath.toFile());
            textureLinks.add(new TextureLink(field, read));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class TextureLink {
        private final Field field;
        private final BufferedImage bufferedImage;
        private double u, u2, v, v2;

        public TextureLink(final Field field, final BufferedImage bufferedImage) {
            this.field = field;
            this.bufferedImage = bufferedImage;
        }
    }
}
