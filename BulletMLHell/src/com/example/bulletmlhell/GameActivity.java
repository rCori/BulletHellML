package com.example.bulletmlhell;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.TouchEvent;
import org.andengine.ui.activity.BaseGameActivity;

import com.example.bulletmlhell.scenemanagement.GameScene;
import com.example.bulletmlhell.scenemanagement.SceneManager;
import com.example.bulletmlhell.scenemanagement.SceneManager.AllScenes;

public class GameActivity extends BaseGameActivity {

	public static int CAMERA_WIDTH = 480;
	public static int CAMERA_HEIGHT = 800;

	
	//For the new SceneManager stuff
	private SceneManager sceneManager;
	private Camera mCamera;
	
    
	@Override
	public EngineOptions onCreateEngineOptions() {
		mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		EngineOptions options = new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED, 
			    new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mCamera);
		return options;
	}
    

	@Override
	public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) 
			throws Exception {
		sceneManager = new SceneManager(this, mEngine, mCamera);
		sceneManager.loadSplashResources();
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}
	

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws Exception {
	   pOnCreateSceneCallback.onCreateSceneFinished(sceneManager.createSplashScene());
	}


	@Override
	public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {
		// TODO Auto-generated method stub
		mEngine.registerUpdateHandler(new TimerHandler(3f, new ITimerCallback(){
			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				// TODO Auto-generated method stub
				mEngine.unregisterUpdateHandler(pTimerHandler);
				sceneManager.loadGameResources();
				sceneManager.createGameScene();
				sceneManager.setCurrentScene(AllScenes.GAME);
			}
		}));
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}
	
}
