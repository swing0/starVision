package org.lwjglb.engine.graph;

import java.util.*;

public class TextureCache {

    public static final String DEFAULT_TEXTURE = "resources/models/default/default_texture.png";

    private Map<String, Texture> textureMap;

    public TextureCache() {
        textureMap = new LinkedHashMap<>(); // 使用 LinkedHashMap 来保持插入顺序
        textureMap.put(DEFAULT_TEXTURE, new Texture(DEFAULT_TEXTURE));
    }

    public void cleanup() {
        textureMap.values().forEach(Texture::cleanup);
    }

    public Texture createTexture(String texturePath) {
        return textureMap.computeIfAbsent(texturePath, Texture::new);
    }

    public void removeTexture(String texturePath) {
        Texture texture = textureMap.remove(texturePath);
        if (texture != null) {
            texture.cleanup(); // 清理纹理资源
        }
    }

    public Collection<Texture> getAll() {
        return textureMap.values();
    }

    public Texture getTexture(String texturePath) {
        Texture texture = null;
        if (texturePath != null) {
            texture = textureMap.get(texturePath);
        }
        if (texture == null) {
            texture = textureMap.get(DEFAULT_TEXTURE);
        }
        return texture;
    }
}
