package org.lwjglb.engine.scene;

import org.lwjglb.engine.graph.*;

public class SkyBox {

    private Material material;
    private Mesh mesh;
    private Entity skyBoxEntity;
    private Model skyBoxModel;
    private int skyBoxNum;


    public SkyBox(String skyBoxModelPath, TextureCache textureCache, MaterialCache materialCache, int skyBoxNum) {
        skyBoxModel = ModelLoader.loadModel("skybox-model", skyBoxModelPath, textureCache, materialCache, false);
        MeshData meshData = skyBoxModel.getMeshDataList().get(0);
        material = materialCache.getMaterial(meshData.getMaterialIdx());
        mesh = new Mesh(meshData);
        skyBoxModel.getMeshDataList().clear();
        skyBoxEntity = new Entity("skyBoxEntity-entity", skyBoxModel.getId());
        skyBoxNum = 1;
    }

    public int getSkyBoxNum() {
        return skyBoxNum;
    }

    public void setSkyBoxNum(int skyBoxNum) {
        this.skyBoxNum = skyBoxNum;
    }

    public void cleanuo() {
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

    public SkyBox changeTexture() {
        return this;
    }

}