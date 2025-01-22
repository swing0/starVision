package org.lwjglb.engine.scene;

import org.joml.Matrix4f;

public class Projection {

    private float fov;
    private static final float Z_FAR = 1000.f;
    private static final float Z_NEAR = 0.01f;

    private Matrix4f invProjMatrix;
    private Matrix4f projMatrix;

    public Projection(int width, int height) {
        projMatrix = new Matrix4f();
        invProjMatrix = new Matrix4f();
        fov = (float) Math.toRadians(60.0f);
        updateProjMatrix(width, height);
    }

    public Matrix4f getInvProjMatrix() {
        return invProjMatrix;
    }

    public Matrix4f getProjMatrix() {
        return projMatrix;
    }

    public void updateProjMatrix(int width, int height) {
        projMatrix.setPerspective(fov, (float) width / height, Z_NEAR, Z_FAR);
        invProjMatrix.set(projMatrix).invert();
    }

    public float getFov() {
        return fov;
    }

    public void setFov(float fov) {
        this.fov = fov;
    }
}
