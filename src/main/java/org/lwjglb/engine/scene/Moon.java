package org.lwjglb.engine.scene;

import org.lwjglb.engine.graph.*;

public class Moon {
    private Material material;
    private Mesh mesh;
    private Entity moonEntity;
    private Model moonModel;

    public Moon(String MoonModelPath, TextureCache textureCache, MaterialCache materialCache) {
        moonModel = ModelLoader.loadModel("moon-model", MoonModelPath, textureCache, materialCache, false);
        MeshData meshData = moonModel.getMeshDataList().get(0);
        material = materialCache.getMaterial(meshData.getMaterialIdx());
        mesh = new Mesh(meshData);
        moonModel.getMeshDataList().clear();
        moonEntity = new Entity("skyBoxEntity-entity", moonModel.getId());
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Mesh getMesh() {
        return mesh;
    }

    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }

    public Entity getMoonEntity() {
        return moonEntity;
    }

    public void setMoonEntity(Entity moonEntity) {
        this.moonEntity = moonEntity;
    }

    public Model getMoonModel() {
        return moonModel;
    }

    public void setMoonModel(Model moonModel) {
        this.moonModel = moonModel;
    }
}
