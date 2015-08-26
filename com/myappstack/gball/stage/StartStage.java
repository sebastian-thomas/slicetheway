package com.myappstack.gball.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.myappstack.gball.MyGballGame;
import com.myappstack.gball.actors.startscreen.PlayButton;
import com.myappstack.gball.actors.startscreen.StartScreenBackground;
import com.myappstack.gball.actors.startscreen.YellowBg;
import com.myappstack.gball.screens.GameScreen;
import com.myappstack.gball.screens.StartScreen;
import com.myappstack.gball.utils.Constants;
import com.myappstack.gball.utils.WorldUtils;

public class StartStage extends Stage {
	
	private OrthographicCamera camera;
	private Vector2 screenDims;
	
	
	private PlayButton playButton;
	private StartScreenBackground sbg;
	private Skin buttonSkin;
	private TextButton playBtn;
	
	
	private MyGballGame gGame;
	
	public StartStage(MyGballGame gGame){
		setupCamera();
		
		this.gGame = gGame;
		Gdx.input.setInputProcessor(this);

		
		screenDims = WorldUtils.viewportToScreen(new Vector2(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT),camera);
		
		playButton = new PlayButton(screenDims,camera);
		sbg = new StartScreenBackground(new Vector2(0,0), screenDims);
		
		Texture btnUp = new Texture(Gdx.files.internal("button-normal.png"));
		Texture btndown = new Texture(Gdx.files.internal("button-press.png"));
		BitmapFont font = new BitmapFont(Gdx.files.internal("tek-hed/nbs.fnt"),
                Gdx.files.internal("tek-hed/nbs.png"), false);
		TextButtonStyle tbs = new TextButtonStyle();
		tbs.up = new Image(btnUp).getDrawable();
		tbs.down = new Image(btndown).getDrawable();
		tbs.font = font;
		
		float w = screenDims.x/3;
		float posY,posX;
		posX = screenDims.x/2 - w/2;
		posY = screenDims.y/4;
		playBtn = new TextButton("", tbs);
		playBtn.setPosition(posX, posY);
		playBtn.setWidth(w);
		playBtn.setHeight(WorldUtils.getProportionalHeight((int)w,new Vector2(btnUp.getWidth(), btnUp.getHeight()) ));
		
		playBtn.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				Gdx.app.log("my app", "Pressed"); // ** Usually used to start
													// Game, etc. **//
				return true;
			}

			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				Gdx.app.log("my app", "Released");
				changeScreen();
			}
		});
		
		addActor(sbg);		
		//addActor(playButton);
		addActor(playBtn);
		
		
		playButton.addListener(new InputListener() {
		    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
		        System.out.println("down");
		        changeScreen();
		        return true;
		    }

		});
		
		if (!gGame.actionResolver.getSignedInGPGS()) {
			System.out.println("Signing in");
			gGame.actionResolver.loginGPGS();
		}
		else{
			System.out.println("Already Signedin");
		}
		 
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
