package com.example.bulletmlhell.scenemanagement;

import org.andengine.entity.scene.Scene;

public abstract class BaseScene extends Scene {

	public abstract void SceneUpdate(float pSecondsElapsed);
	public abstract Scene CreateScene();
	public abstract void LoadSceneResources();
	
	protected Scene scene;
	
}
