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
    private static final float POSITION_OFFSET = 1000.0f;

    private AnimationData animationData1;
    private Entity villageHouseEntity;
    private Entity snowLakeEntity;
    private Entity isLandEntity;
    private Entity mountainEntity;
    private Entity sunEntity;
    private Entity moonEntity;
    private Entity mercuryEntity;
    private Entity venusEntity;
    private Entity marsEntity;
    private Entity jupiterEntity;
    private Entity saturnEntity;
    private Entity uranusEntity;
    private Entity neptuneEntity;
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

        //村庄场景
        createScene(scene,"village",villageHouseEntity, 0f,-0.3f,0f,0.01f);

        //雪屋场景
        createScene(scene,"snow",snowLakeEntity, 0,-0.3f,0 + POSITION_OFFSET,0.02f);

        //小岛场景
        createScene(scene,"island",isLandEntity, 0 + POSITION_OFFSET,-0.3f,0,0.03f);

        //山脉场景
        createScene(scene,"mountain",mountainEntity, 0,0.3f,0 - POSITION_OFFSET ,0.0005f);



        String bobModelId = "bobModel";
        Model bobModel = ModelLoader.loadModel(bobModelId, "resources/models/bob/boblamp.md5mesh",
                scene.getTextureCache(), scene.getMaterialCache(), true);
        scene.addModel(bobModel);
        Entity bobEntity = new Entity("bobEntity-1", bobModelId);
        bobEntity.setScale(0.05f);
        bobEntity.setPosition(0, 0, 0);
        bobEntity.updateModelMatrix();
        animationData1 = new AnimationData(bobModel.getAnimationList().get(0));
        bobEntity.setAnimationData(animationData1);
        scene.addEntity(bobEntity);

        Sun sun = new Sun("resources/models/sun/sun.obj", scene.getTextureCache(),
                scene.getMaterialCache());
        scene.setSun(sun);
        sunEntity = sun.getSunEntity();
        sunEntity.setPosition(0, 100, -50);
        sunEntity.setScale(1.0f);
        sunEntity.updateModelMatrix();

        Model moonModel = ModelLoader.loadModel("moon-model", "resources/models/moon/Moon_2K.obj",
                scene.getTextureCache(), scene.getMaterialCache(), false);
        scene.addModel(moonModel);
        moonEntity = new Entity("moon-entity", moonModel.getId());
        moonEntity.setPosition(0, 40, -1);
        moonEntity.setScale(0.5f);
        moonEntity.updateModelMatrix();
        scene.addEntity(moonEntity);

        mercuryEntity = setPlanets("mercury","resources/models/planet/Mercury.obj",scene,-10,10,3,0.5f);
        scene.addEntity(mercuryEntity);
        venusEntity = setPlanets("venus","resources/models/planet/Venus.obj",scene,-10,20,30,0.8f);
        scene.addEntity(venusEntity);
        marsEntity = setPlanets("mars","resources/models/planet/Mars.obj",scene,10,10,3,0.6f);
        scene.addEntity(marsEntity);
        jupiterEntity = setPlanets("jupiter","resources/models/planet/Jupiter.obj",scene,18,50,50,1.2f);
        scene.addEntity(jupiterEntity);
        saturnEntity = setPlanets("saturn","resources/models/planet/Saturn.obj",scene,38,60,20,1.0f);
        scene.addEntity(saturnEntity);
        uranusEntity = setPlanets("uranus","resources/models/planet/Uranus.obj",scene,50,70,25,0.9f);
        scene.addEntity(uranusEntity);
        neptuneEntity = setPlanets("neptune","resources/models/planet/Neptune.obj",scene,58,80,50,0.9f);
        scene.addEntity(neptuneEntity);

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
        skyBox.getSkyBoxEntity().setScale(1000);
        skyBox.getSkyBoxEntity().updateModelMatrix();
        scene.setSkyBox(skyBox);
        skyBoxEntity = skyBox.getSkyBoxEntity();


        scene.setFog(new Fog(true, new Vector3f(0.5f, 0.5f, 0.5f), 0.003f));

        Camera camera = scene.getCamera();
        camera.setPosition(-1.5f, 3.0f, 4.5f);
        camera.addRotation((float) Math.toRadians(15.0f), (float) Math.toRadians(390.f));

        lightAngle = 45.001f;
        timeSpeed = TIME_DEFAULT_SPEED;
    }

    private void createScene(Scene scene, String name,Entity entity, float x, float y, float z, float s) {
        String terrainModelId = name + "-terrain";
        Model terrainModel = ModelLoader.loadModel(terrainModelId, "resources/models/terrain/" +name + "_terrain.obj",
                scene.getTextureCache(), scene.getMaterialCache(), false);
        scene.addModel(terrainModel);
        Entity terrainEntity = new Entity(name + "TerrainEntity", terrainModelId);
        terrainEntity.setScale(500.0f);
        terrainEntity.setPosition(x,y,z);
        terrainEntity.updateModelMatrix();
        scene.addEntity(terrainEntity);
        Model isLandModel = ModelLoader.loadModel(name, "resources/models/"+name+"/"+name+".obj",
                scene.getTextureCache(), scene.getMaterialCache(), false);
        scene.addModel(isLandModel);
        entity = new Entity(name + "Entity", name);
        entity.setPosition(x,y,z);
        entity.setScale(s);
        entity.updateModelMatrix();
        scene.addEntity(entity);
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
            scene.getSkyBox().changeNightSky();
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


        if (window.isKeyClick(GLFW_KEY_F1)){
            //村庄场景
            camera.setPosition(-1.5f,3.0f, 4.5f);
        }
        if (window.isKeyClick(GLFW_KEY_F2)){
            //雪地场景
            camera.setPosition(-1.5f, 3.0f, 4.5f + POSITION_OFFSET);
        }
        if (window.isKeyClick(GLFW_KEY_F3)){
            //小岛场景
            camera.setPosition(80.f + POSITION_OFFSET, 3.0f, -20.0f);
        }
        if (window.isKeyClick(GLFW_KEY_F4)){
            //山脉场景
            camera.setPosition(-1.5f, 4.0f, 4.5f - POSITION_OFFSET);
        }
        if (window.isKeyClick(GLFW_KEY_P)){
            System.out.println("( " + camera.getPosition().x + ", " + camera.getPosition().y + ", " + camera.getPosition().z +")");
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
        if (rotation > 36000) {
            rotation = 0;
        }

        // 太阳运动（每天从东方升起，西方落下）
        float sunX = (float) Math.sin(Math.toRadians(rotation)) * 1000; // 水平运动
        float sunY = (float) Math.cos(Math.toRadians(rotation)) * 500 + 50; // 垂直运动（模拟高度角）
        sunEntity.setPosition(sunX, sunY, -150); // 更新太阳位置
        sunEntity.updateModelMatrix();

        // 月亮运动（每天从东方升起，西方落下，相位变化）
        float moonX = (float) Math.sin(Math.toRadians(rotation + 180)) * 800; // 水平运动（与太阳相反）
        float moonY = (float) Math.cos(Math.toRadians(rotation + 180)) * 400 + 40; // 垂直运动
        moonEntity.setPosition(moonX, moonY, -1); // 更新月亮位置
        moonEntity.updateModelMatrix();

        // 水星运动（缓慢移动）
        float mercuryX = (float) Math.sin(Math.toRadians(rotation * 0.5f)) * 100;
        float mercuryY = (float) Math.cos(Math.toRadians(rotation * 0.5f)) * 100 + 10;
        mercuryEntity.setPosition(mercuryX, mercuryY, 3);
        mercuryEntity.updateModelMatrix();

        // 金星运动（缓慢移动）
        float venusX = (float) Math.sin(Math.toRadians(rotation * 0.4f)) * 200;
        float venusY = (float) Math.cos(Math.toRadians(rotation * 0.4f)) * 200 + 20;
        venusEntity.setPosition(venusX, venusY, 30);
        venusEntity.updateModelMatrix();

        // 火星运动（缓慢移动）
        float marsX = (float) Math.sin(Math.toRadians(rotation * 0.3f)) * 100;
        float marsY = (float) Math.cos(Math.toRadians(rotation * 0.3f)) * 100 + 10;
        marsEntity.setPosition(marsX, marsY, 3);
        marsEntity.updateModelMatrix();

        // 木星运动（缓慢移动）
        float jupiterX = (float) Math.sin(Math.toRadians(rotation * 0.2f)) * 500;
        float jupiterY = (float) Math.cos(Math.toRadians(rotation * 0.2f)) * 500 + 50;
        jupiterEntity.setPosition(jupiterX, jupiterY, 50);
        jupiterEntity.updateModelMatrix();

        // 土星运动（缓慢移动）
        float saturnX = (float) Math.sin(Math.toRadians(rotation * 0.15f)) * 600;
        float saturnY = (float) Math.cos(Math.toRadians(rotation * 0.15f)) * 600 + 60;
        saturnEntity.setPosition(saturnX, saturnY, 20);
        saturnEntity.updateModelMatrix();

        // 天王星运动（缓慢移动）
        float uranusX = (float) Math.sin(Math.toRadians(rotation * 0.1f)) * 700;
        float uranusY = (float) Math.cos(Math.toRadians(rotation * 0.1f)) * 700 + 70;
        uranusEntity.setPosition(uranusX, uranusY, 25);
        uranusEntity.updateModelMatrix();

        // 海王星运动（缓慢移动）
        float neptuneX = (float) Math.sin(Math.toRadians(rotation * 0.08f)) * 800;
        float neptuneY = (float) Math.cos(Math.toRadians(rotation * 0.08f)) * 800 + 80;
        neptuneEntity.setPosition(neptuneX, neptuneY, 50);
        neptuneEntity.updateModelMatrix();

        if (lightAngle > 100 && lightAngle < 280) { // 假设光照角度大于120且小于300度为夜晚
            if (scene.getSkyBox().isDay()) {
                scene.getSkyBox().setDay(false);
                scene.getSkyBox().switchDayNight(); // 切换到夜晚天空盒
            }
        } else {
            if (!scene.getSkyBox().isDay()) {
                scene.getSkyBox().setDay(true);
                scene.getSkyBox().switchDayNight(); // 切换到白天天空盒
            }
        }
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