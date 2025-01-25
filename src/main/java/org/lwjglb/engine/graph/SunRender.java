package org.lwjglb.engine.graph;

import org.joml.Matrix4f;
import org.lwjglb.engine.scene.*;

import java.util.*;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class SunRender {

    private ShaderProgram shaderProgram;
    private UniformsMap uniformsMap;
    private Matrix4f viewMatrix;

    public SunRender() {
        List<ShaderProgram.ShaderModuleData> shaderModuleDataList = new ArrayList<>();
        shaderModuleDataList.add(new ShaderProgram.ShaderModuleData("resources/shaders/sun.vert", GL_VERTEX_SHADER));
        shaderModuleDataList.add(new ShaderProgram.ShaderModuleData("resources/shaders/sun.frag", GL_FRAGMENT_SHADER));
        shaderProgram = new ShaderProgram(shaderModuleDataList);
        viewMatrix = new Matrix4f();
        createUniforms();
    }

    public void cleanup() {
        shaderProgram.cleanup();
    }

    private void createUniforms() {
        uniformsMap = new UniformsMap(shaderProgram.getProgramId());
        uniformsMap.createUniform("projectionMatrix");
        uniformsMap.createUniform("viewMatrix");
        uniformsMap.createUniform("modelMatrix");
        uniformsMap.createUniform("diffuse");
        uniformsMap.createUniform("txtSampler");
        uniformsMap.createUniform("hasTexture");
        uniformsMap.createUniform("emissiveColor"); // 添加自发光颜色的统一变量
    }

    public void render(Scene scene) {
        Sun sun = scene.getSun();
        if (sun == null) {
            return;
        }
        shaderProgram.bind();

        // 设置投影矩阵和视图矩阵
        uniformsMap.setUniform("projectionMatrix", scene.getProjection().getProjMatrix());
        viewMatrix.set(scene.getCamera().getViewMatrix());
        uniformsMap.setUniform("viewMatrix", viewMatrix);
        uniformsMap.setUniform("txtSampler", 0);

        Entity sunEntity = sun.getSunEntity();

        // 设置太阳的位置（固定在天空中的某个位置）
        Matrix4f modelMatrix = sunEntity.getModelMatrix();
//        modelMatrix.identity()
//                .translate(0, 100, -50) // 放置在天空中的某个位置
//                .scale(1); // 缩放大小

        uniformsMap.setUniform("modelMatrix", modelMatrix);

        // 获取材质和纹理
        TextureCache textureCache = scene.getTextureCache();
        Material material = sun.getMaterial();
        Mesh mesh = sun.getMesh();
        Texture texture = textureCache.getTexture(material.getTexturePath());
        glActiveTexture(GL_TEXTURE0);
        texture.bind();

        // 设置材质属性
        uniformsMap.setUniform("diffuse", material.getDiffuseColor());
        uniformsMap.setUniform("hasTexture", texture.getTexturePath().equals(TextureCache.DEFAULT_TEXTURE) ? 0 : 1);
        uniformsMap.setUniform("emissiveColor", material.getEmissiveColor());

        glDisable(GL_BLEND);
        // 绑定 VAO 并绘制
        glBindVertexArray(mesh.getVaoId());
        glDrawElements(GL_TRIANGLES, mesh.getNumVertices(), GL_UNSIGNED_INT, 0);

        // 解绑 VAO 和着色器程序
        glBindVertexArray(0);
        shaderProgram.unbind();
    }
}