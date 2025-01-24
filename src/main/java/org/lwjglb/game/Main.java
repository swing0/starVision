package org.lwjglb.game;

import org.joml.*;
import org.lwjglb.engine.*;
import org.lwjglb.engine.graph.*;
import org.lwjglb.engine.scene.*;
import org.lwjglb.engine.scene.lights.*;

import java.lang.Math;

import static org.lwjgl.glfw.GLFW.*;

public class Main implements IAppLogic {

    private static final float MOUSE_SENSITIVITY = 0.1f;
    private static final float MOVEMENT_SPEED = 0.005f;
    private static final float TIME_DEFAULT_SPEED = 1 * 0.01f;

    private AnimationData animationData1;
    private Entity cubeEntity1;
    private Entity cubeEntity2;
    private Entity moonEntity;
    private Entity sunEntity;
    private Entity marsEntity;
    private Entity skyBoxEntity;
    private float lightAngle;
    private float rotation;
    private float timeSpeed;
    private float scopeNum = 60.0f;

    public static void main(String[] args) {
        Main main = new Main();
        Window.WindowOptions opts = new Window.WindowOptions();
        opts.antiAliasing = true;
        Engine gameEng = new Engine("starVision", opts, main);
        gameEng.start();
    }

    @Override
    public void cleanup() {
        // Nothing to be done yet
    }

    @Override
    public void init(Window window, Scene scene, Render render) {
        String terrainModelId = "terrain";
        Model terrainModel = ModelLoader.loadModel(terrainModelId, "resources/models/terrain/terrain.obj",
                scene.getTextureCache(), scene.getMaterialCache(), false);
        scene.addModel(terrainModel);
        Entity terrainEntity = new Entity("terrainEntity", terrainModelId);
        terrainEntity.setScale(500.0f);
        terrainEntity.updateModelMatrix();
        scene.addEntity(terrainEntity);

        String villageHouseId = "villageHouse";
        Model villageHouseModel = ModelLoader.loadModel(villageHouseId, "resources/models/terrain/villageHouse.obj",
                scene.getTextureCache(), scene.getMaterialCache(), false);
        scene.addModel(villageHouseModel);
        Entity villageHouseEntity = new Entity("villageHouseEntity", villageHouseId);
        villageHouseEntity.setScale(0.01f);
        villageHouseEntity.updateModelMatrix();
        scene.addEntity(villageHouseEntity);

        String bobModelId = "bobModel";
        Model bobModel = ModelLoader.loadModel(bobModelId, "resources/models/bob/boblamp.md5mesh",
                scene.getTextureCache(), scene.getMaterialCache(), true);
        scene.addModel(bobModel);
        Entity bobEntity = new Entity("bobEntity-1", bobModelId);
        bobEntity.setScale(0.05f);
        bobEntity.setPosition(0, 0.3f, 0);
        bobEntity.updateModelMatrix();
        animationData1 = new AnimationData(bobModel.getAnimationList().get(0));
        bobEntity.setAnimationData(animationData1);
        scene.addEntity(bobEntity);

//        Model cubeModel = ModelLoader.loadModel("cube-model", "resources/models/cube/cube.obj",
//                scene.getTextureCache(), scene.getMaterialCache(), false);
//        scene.addModel(cubeModel);
//        cubeEntity1 = new Entity("cube-entity-1", cubeModel.getId());
//        cubeEntity1.setPosition(0, 2, -1);
//        cubeEntity1.updateModelMatrix();
//        scene.addEntity(cubeEntity1);
//
//        cubeEntity2 = new Entity("cube-entity-2", cubeModel.getId());
//        cubeEntity2.setPosition(-2, 2, -1);
//        cubeEntity2.updateModelMatrix();
//        scene.addEntity(cubeEntity2);


        Model moonModel = ModelLoader.loadModel("moon-model", "resources/models/moon/Moon_2K.obj",
                scene.getTextureCache(), scene.getMaterialCache(), false);
        scene.addModel(moonModel);
        moonEntity = new Entity("moon-entity", moonModel.getId());
        moonEntity.setPosition(0, 40, -1);
        moonEntity.setScale(1);
        moonEntity.updateModelMatrix();
        scene.addEntity(moonEntity);

        Model sunModel = ModelLoader.loadModel("sun-model", "resources/models/sun/sun.obj",
                scene.getTextureCache(), scene.getMaterialCache(), false);
        scene.addModel(sunModel);
        sunEntity = new Entity("sun-entity", sunModel.getId());
        sunEntity.setPosition(0, 50, -50);
        sunEntity.setScale(1);
        sunEntity.setHaveShadow(false);
        sunEntity.updateModelMatrix();
        scene.addEntity(sunEntity);

        marsEntity = setPlanets("mars","resources/models/planet/Mars.obj",scene,10,10,3,1);
        scene.addEntity(marsEntity);

        render.setupData(scene);

        SceneLights sceneLights = new SceneLights();
        AmbientLight ambientLight = sceneLights.getAmbientLight();
        ambientLight.setIntensity(0.5f);
        ambientLight.setColor(0.3f, 0.3f, 0.3f);

        DirLight dirLight = sceneLights.getDirLight();
        dirLight.setPosition(0, 1, 0);
        dirLight.setIntensity(1.0f);
        scene.setSceneLights(sceneLights);

        SkyBox skyBox = new SkyBox("resources/models/skybox/skybox.obj", scene.getTextureCache(),
                scene.getMaterialCache());
        skyBox.getSkyBoxEntity().setScale(100);
        skyBox.getSkyBoxEntity().updateModelMatrix();
        scene.setSkyBox(skyBox);
        skyBoxEntity = skyBox.getSkyBoxEntity();

        scene.setFog(new Fog(true, new Vector3f(0.5f, 0.5f, 0.5f), 0.01f));

        Camera camera = scene.getCamera();
        camera.setPosition(-1.5f, 3.0f, 4.5f);
        camera.addRotation((float) Math.toRadians(15.0f), (float) Math.toRadians(390.f));

        lightAngle = 45.001f;
        timeSpeed = TIME_DEFAULT_SPEED;
    }

    @Override
    public void input(Window window, Scene scene, long diffTimeMillis, boolean inputConsumed) {
        if (inputConsumed) {
            return;
        }
        float move = diffTimeMillis * MOVEMENT_SPEED;
        Camera camera = scene.getCamera();
        if (window.isKeyPressed(GLFW_KEY_W)) {
            camera.moveForward(move);
        } else if (window.isKeyPressed(GLFW_KEY_S)) {
            camera.moveBackwards(move);
        }
        if (window.isKeyPressed(GLFW_KEY_A)) {
            camera.moveLeft(move);
        } else if (window.isKeyPressed(GLFW_KEY_D)) {
            camera.moveRight(move);
        }
        if (window.isKeyPressed(GLFW_KEY_LEFT)) {
            timeSpeed = timeSpeed - 0.1f;
        } else if (window.isKeyPressed(GLFW_KEY_RIGHT)) {
            timeSpeed = timeSpeed + 0.1f;
        }
        if (window.isKeyClick(GLFW_KEY_0)){
            timeSpeed = TIME_DEFAULT_SPEED;
        }
        if (window.isKeyClick(GLFW_KEY_TAB)) {
            scene.getSkyBox().changeTexture();
        }
        //按PAGE_UP与PAGE_DOWN实现望远镜效果
        if (window.isKeyClick(GLFW_KEY_PAGE_UP) && scopeNum > 10.0f){
            scopeNum -= 10.0f;
            float newFov = (float) Math.toRadians(scopeNum);
            scene.getCamera().setFov(newFov);
            scene.getProjection().setFov(newFov);
            scene.getProjection().updateProjMatrix(window.getWidth(), window.getHeight());
        }
        if (window.isKeyClick(GLFW_KEY_PAGE_DOWN) && scopeNum < 90.0f){
            scopeNum += 10.0f;
            float newFov = (float) Math.toRadians(scopeNum);
            scene.getCamera().setFov(newFov);
            scene.getProjection().setFov(newFov);
            scene.getProjection().updateProjMatrix(window.getWidth(), window.getHeight());
        }

        MouseInput mouseInput = window.getMouseInput();
        if (mouseInput.isRightButtonPressed()) {
            Vector2f displVec = mouseInput.getDisplVec();
            camera.addRotation((float) Math.toRadians(displVec.x * MOUSE_SENSITIVITY), (float) Math.toRadians(displVec.y * MOUSE_SENSITIVITY));
        }

    }

    @Override
    public void update(Window window, Scene scene, long diffTimeMillis) {
        animationData1.nextFrame();

        lightAngle = (lightAngle + timeSpeed) % 360;
        SceneLights sceneLights = scene.getSceneLights();
        DirLight dirLight = sceneLights.getDirLight();
        double angRad = Math.toRadians(lightAngle);
        dirLight.getDirection().x = (float) Math.sin(angRad);
        dirLight.getDirection().y = (float) Math.cos(angRad);
        dirLight.getDirection().z = (float) Math.cos(angRad);


        rotation += timeSpeed;
        if (rotation > 360) {
            rotation = 0;
        }
//        cubeEntity1.setRotation(1, 1, 1, (float) Math.toRadians(rotation));
//        cubeEntity1.updateModelMatrix();
//
//        cubeEntity2.setRotation(1, 1, 1, (float) Math.toRadians(360 - rotation));
//        cubeEntity2.updateModelMatrix();
        skyBoxEntity.setRotation(1, 1, 1, (float) Math.toRadians(360 - rotation));
        skyBoxEntity.updateModelMatrix();
    }


    private Entity setPlanets(String name,String modelPath,Scene scene,float x, float y, float z, float scale){
        String id = name + "-model";
        String entityId = name + "-entity";
        Model model = ModelLoader.loadModel(id,modelPath,scene.getTextureCache(),scene.getMaterialCache(),false);
        scene.addModel(model);
        Entity entity = new Entity(entityId,model.getId());
        entity.setPosition(x,y,z);
        entity.setScale(scale);
        entity.updateModelMatrix();
        return entity;
    }
}