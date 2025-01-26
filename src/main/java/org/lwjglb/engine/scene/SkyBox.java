package org.lwjglb.engine.scene;

import org.lwjglb.engine.graph.*;

public class SkyBox {

    private Material material;
    private Mesh mesh;
    private Entity skyBoxEntity;
    private Model skyBoxModel;
    private int skyBoxNum;
    private TextureCache textureCache;

    public SkyBox(String skyBoxModelPath, TextureCache textureCache, MaterialCache materialCache) {
        this.textureCache = textureCache; // 保存纹理缓存引用
        skyBoxModel = ModelLoader.loadModel("skybox-model", skyBoxModelPath, textureCache, materialCache, false);
        MeshData meshData = skyBoxModel.getMeshDataList().get(0);
        material = materialCache.getMaterial(meshData.getMaterialIdx());
        mesh = new Mesh(meshData);
        skyBoxModel.getMeshDataList().clear();
        skyBoxEntity = new Entity("skyBoxEntity-entity", skyBoxModel.getId());
        skyBoxNum = 0;
    }

    public int getSkyBoxNum() {
        return skyBoxNum;
    }

    public void setSkyBoxNum(int skyBoxNum) {
        this.skyBoxNum = skyBoxNum;
    }

    public void cleanup() {
        mesh.cleanup();
    }

    public Material getMaterial() {
        return material;
    }

    public Mesh getMesh() {
        return mesh;
    }

    public Entity getSkyBoxEntity() {
        return skyBoxEntity;
    }

    public Model getSkyBoxModel() {
        return skyBoxModel;
    }

    public void changeTexture() {
        String path = "resources/models/skybox/skybox0.png";
        switch (skyBoxNum) {
            case 0 ->{
                path = "resources/models/skybox/skybox1.png";
                skyBoxNum = 1;
            }
            case 1 -> {
                path = "resources/models/skybox/skybox2.png";
                skyBoxNum = 2;
            }
            case 2 -> {
                path = "resources/models/skybox/skybox3.png";
                skyBoxNum = 3;
            }
            case 3 -> {
                path = "resources/models/skybox/skybox0.png";
                skyBoxNum = 0;
            }
        }


        // 设置新的纹理路径并加载新纹理
        material.setTexturePath(path);
        textureCache.createTexture(path);
    }
}