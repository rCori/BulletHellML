package com.example.bulletmlhell.scenemanagement;

import java.io.IOException;
import java.io.InputStream;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.adt.io.in.IInputStreamOpener;

public class SplashScene extends BaseScene {

	private TextureRegion mSplashScreenTextureRegion;
	
	private BaseGameActivity activity;
	private Engine engine;
	private Camera camera;
	private SceneManager sceneManager;
	
	public SplashScene(BaseGameActivity activity, Engine engine, Camera camera, SceneManager sceneManager) {
		this.activity = activity;
		this.engine = engine;
		this.camera = camera;
		this.sceneManager = sceneManager;
	}
	

	@Override
	public void SceneUpdate(float pSecondsElapsed) {
		int temp = 0;
		temp++;
	}

	@Override
	public Scene CreateScene() {
		scene = new Scene();
		scene.setBackground(new Background(1,1,1));
		Sprite icon = new Sprite(0,0,this.mSplashScreenTextureRegion, activity.getVertexBufferObjectManager());
		scene.attachChild(icon);
		return scene;
	}

	@Override
	public void LoadSceneResources() {
		try {
			ITexture splashScreenTexture = new BitmapTexture(engine.getTextureManager(), new IInputStreamOpener() {
				@Override
				public InputStream open() throws IOException {
					return activity.getAssets().open("splashscreen.png");
				}
			});
			splashScreenTexture.load();
			this.mSplashScreenTextureRegion = TextureRegionFactory.extractFromTexture(splashScreenTexture);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
