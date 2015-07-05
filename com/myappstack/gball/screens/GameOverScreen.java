package com.myappstack.gball.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.myappstack.gball.MyGballGame;
import com.myappstack.gball.stage.GameOverStage;

public class GameOverScreen implements Screen {
	
	MyGballGame gGame;
	private GameOverStage goStage;
	
	 public GameOverScreen(MyGballGame gGame) {
		 goStage = new GameOverStage();
		 this.gGame = gGame;
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		goStage.draw();
		goStage.act(delta);
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
