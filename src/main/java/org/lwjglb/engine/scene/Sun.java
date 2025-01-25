package org.lwjglb.engine.scene;

import org.lwjglb.engine.graph.*;

public class Sun{
    private Material material;
    private Mesh mesh;
    private Entity sunEntity;
    private Model sunModel;

    public Sun(String sunModelPath, TextureCache textureCache, MaterialCache materialCache) {
        sunModel = ModelLoader.loadModel("sun-model", sunModelPath, textureCache, materialCache, false);
        MeshData meshData = sunModel.getMeshDataList().get(0);
        material = materialCache.getMaterial(meshData.getMaterialIdx());
        mesh = new Mesh(meshData);
        sunModel.getMeshDataList().clear();
        sunEntity = new Entity("sunEntity-entity", sunModel.getId());
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

    public Entity getSunEntity() {
        return sunEntity;
    }

    public void setSunEntity(Entity sunEntity) {
        this.sunEntity = sunEntity;
    }

    public Model getSunModel() {
        return sunModel;
    }

    public void setSunModel(Model sunModel) {
        this.sunModel = sunModel;
    }
}
