package com.myappstack.gball.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.myappstack.gball.MyGballGame;
import com.myappstack.gball.actors.startscreen.BallOne;
import com.myappstack.gball.actors.startscreen.BallTwo;
import com.myappstack.gball.actors.startscreen.GameName;
import com.myappstack.gball.actors.startscreen.PlayButton;
import com.myappstack.gball.screens.GameScreen;
import com.myappstack.gball.utils.Constants;
import com.myappstack.gball.utils.WorldUtils;

public class StartStage extends Stage {
	
	private OrthographicCamera camera;
	private Vector2 screenDims;
	
	private Sprite background;
	private GameName gName;
	private BallOne ball1;
	private BallTwo ball2;
	private PlayButton playButton;
	private MyGballGame gGame;
	
	public StartStage(MyGballGame gGame){
		setupCamera();
		
		this.gGame = gGame;
		Gdx.input.setInputProcessor(this);

		
		screenDims = WorldUtils.viewportToScreen(new Vector2(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT),camera);
		gName = new GameName(screenDims);
		ball1 = new BallOne(screenDims);
		ball2 = new BallTwo(screenDims);
		playButton = new PlayButton(screenDims,camera);
		
		addActor(gName);
		addActor(ball1);
		addActor(ball2);
		addActor(playButton);
		
		playButton.addListener(new ChangeListener(){

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				System.out.println("Changing screen");
				changeScreen();
			}
			
		});
	}
	
	private void changeScreen(){
		gGame.setScreen(new GameScreen(gGame));
	}
	
	private void setupCamera() {
		camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
		camera.position.set(camera.viewportWidth / 2,
				camera.viewportHeight / 2, 0f);
		camera.update();
	}

}
