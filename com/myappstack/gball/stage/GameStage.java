package com.myappstack.gball.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.myappstack.gball.MyGballGame;
import com.myappstack.gball.PowerupsManager;
import com.myappstack.gball.actors.Ball;
import com.myappstack.gball.actors.BallT;
import com.myappstack.gball.actors.BgGameStage;
import com.myappstack.gball.actors.Food;
import com.myappstack.gball.actors.Food.FoodType;
import com.myappstack.gball.actors.Line;
import com.myappstack.gball.screens.GameOverScreen;
import com.myappstack.gball.screens.GameScreen;
import com.myappstack.gball.screens.StartScreen;
import com.myappstack.gball.utils.Constants;
import com.myappstack.gball.utils.WorldUtils;
import com.sun.org.apache.bcel.internal.generic.FADD;

public class GameStage extends Stage {

	private static final int VIEWPORT_WIDTH = Constants.VIEWPORT_WIDTH;
	private static final int VIEWPORT_HEIGHT = Constants.VIEWPORT_HEIGHT;
	
	private PowerupsManager pm;
	private MyGballGame gballGame;

	private World world;
	//private Body wallLeft, wallRight, wallBottom, wallTop;
	private Vector2 touchStart;
	private Vector2 touchEnd;

	private Line line;
	private Food food;
	private BallT blueBall;
	private BallT redBall;
	private BgGameStage bgWood;
	
	private Vector2 margins,screenDims;
	
	private Integer score;
	private Table table;
	private Label scoreLabel;
	private Label modeLabel;
	
	private Image pScoreBg;
	private Button pReplay;
	private Button pMenu;
	private Label pScore;

	private final float TIME_STEP = 1 / 300f;
	private float accumulator = 0f;

	private OrthographicCamera camera;
	private Box2DDebugRenderer renderer;
	
	private boolean gOver;

	public GameStage( MyGballGame game) {
		this.gballGame = game;
		this.gOver = false;
		pm = new PowerupsManager();
		setupCamera();
    	setupWorld();
    	
    	setupBall();
    	setupFood(Constants.VIEWPORT_WIDTH/2,Constants.VIEWPORT_HEIGHT/2);
    	setupScore();
    	Gdx.input.setInputProcessor(this);
        renderer = new Box2DDebugRenderer();
        
        touchStart = touchEnd = null;
    }
	
	
	@Override
	public void act(float delta) {
		super.act(delta);
		// Fixed timestep
		accumulator += delta;
		while (accumulator >= delta) {
			world.step(TIME_STEP, 6, 2);
			accumulator -= TIME_STEP;
		}
		
		if(this.gOver){
			return;
		}
		
		
		// TODO: Implement interpolation
		
		handleLineCollision();
		//blueBall.getBounds().overlaps(redBall.getBounds())
		if(blueBall.collidedWith(redBall.getPos())){
			if(blueBall.getState() == BallT.State.GOTHROUGH || redBall.getState() == BallT.State.GOTHROUGH){
				System.out.println("not out : go through");
			}
			else{
				System.out.println("game over");
				this.gOver = true;
				showGameOverPoupUp();
				//this.gballGame.setScreen(new GameOverScreen(gballGame,score));
			}
			
		}

		else if (blueBall.getBounds().overlaps(food.getBounds())) {
			System.out.println("Collided");
			int newXpos = MathUtils.random(Constants.MARGIN+1, Constants.VIEWPORT_WIDTH
					- Constants.MARGIN- Constants.FOOD_WIDTH - 1);
			int newYpos = MathUtils.random(Constants.MARGIN +1, Constants.VIEWPORT_HEIGHT
					- Constants.FOOD_HEIGHT- Constants.TOP_MARGIN - 1);
			if(food.getType() == Food.FoodType.SPEEDER){
				blueBall.setStateProperties(BallT.State.SPEED);
			}
			else if(food.getType() == Food.FoodType.GOTHROUGH){
				blueBall.setStateProperties(BallT.State.GOTHROUGH);
			}
			
			/*if(pm.powerup()){
				food.change(newXpos, newYpos,FoodType.SPEEDER);
			}
			else{
				food.change(newXpos, newYpos,FoodType.NORMAL);
			}*/
			food.change(newXpos, newYpos,FoodType.NORMAL);
			score++;
			scoreLabel.setText("Score : "+score.toString());

		}
		else if (redBall.getBounds().overlaps(food.getBounds())) {
			System.out.println("Collided");
			int newXpos = MathUtils.random(Constants.MARGIN + 1, Constants.VIEWPORT_WIDTH
					- Constants.FOOD_WIDTH - Constants.MARGIN - 1);
			int newYpos = MathUtils.random(Constants.MARGIN +1, Constants.VIEWPORT_HEIGHT
					- Constants.FOOD_HEIGHT - Constants.TOP_MARGIN - 1);
			
			if(food.getType() == Food.FoodType.SPEEDER){
				redBall.setStateProperties(BallT.State.SPEED);
			}
			else if(food.getType() == Food.FoodType.GOTHROUGH){
				redBall.setStateProperties(BallT.State.GOTHROUGH);
			}
			
			/*if(pm.powerup()){
				food.change(newXpos, newYpos,FoodType.GOTHROUGH);
			}
			else{
				food.change(newXpos, newYpos,FoodType.NORMAL);
			}*/
			food.change(newXpos, newYpos,FoodType.NORMAL);
			score++;
			scoreLabel.setText("Score : "+score.toString());

		}

	}

	private void setupScore() {
		score = 0;
		Vector2 dims = WorldUtils.viewportToScreen(new Vector2(Constants.VIEWPORT_WIDTH-2*Constants.MARGIN,Constants.GP_BOARD), camera);
		Vector2 pos = WorldUtils.viewportToScreen(new Vector2(Constants.MARGIN,Constants.VIEWPORT_HEIGHT -(Constants.TOP_MARGIN+Constants.GP_BOARD) ), camera);
		
		BitmapFont font = new BitmapFont(Gdx.files.internal("fonts/hobostd.fnt"),
                Gdx.files.internal("fonts/hobostd.png"), false);
		LabelStyle style = new LabelStyle();
		style.font = font;
		scoreLabel = new Label("Score : "+score.toString(), style);
		scoreLabel.setText("Score : "+score.toString());
		
		modeLabel = new Label("Mode : Normal",style);
		modeLabel.setText("Mode : Normal");
		
		table = new Table();
		//table.setFillParent(true);
		table.setDebug(true);
		table.setSize(dims.x, dims.y);
		table.setPosition(pos.x, pos.y);
		//table.center();
		table.add(scoreLabel).expandX().height(dims.y);
		table.add(modeLabel).expandX().height(dims.y);
		
		addActor(table);
	}



	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		// return super.touchDown(screenX, screenY, pointer, button);
		resetLine();
		touchStart = new Vector2(screenX, screenY);
		return super.touchDown(screenX, screenY, pointer, button);
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		// return super.touchUp(screenX, screenY, pointer, button);
		touchEnd = new Vector2(screenX, screenY);
		if(!this.gOver){
			drawLineActor();
		}
		
		return super.touchUp(screenX, screenY, pointer, button);
	}

	public void drawLineActor() {
		if (touchStart != null && touchEnd != null) {
			Vector2 start_temp = screenToViewport(touchStart);
			Vector2 end_temp = screenToViewport(touchEnd);
						
			line = new Line(camera, start_temp,end_temp);
			addActor(line);
		}
	}

	public void resetLine() {
		if (line != null) {
			line.removeLine(world);
			line.remove();
			line = null;
			touchStart = touchEnd = null;
		}
	}

	
	
	public void handleLineCollision(){
		if(line != null){
			if(line.getDistanceFromLine(blueBall.getPos())<= Constants.BALL_RADIUS){
				if(line.inSegmentIntrRegion(blueBall.getPos())){
					System.out.println("Line-Blue Ball collision");
					blueBall.setDirection(WorldUtils.reflect(blueBall.getDirection(), line.getNormalVector()));
					resetLine();
				}
			}
		}
		
		if(line != null){
			if(line.getDistanceFromLine(redBall.getPos())<= Constants.BALL_RADIUS){
				if(line.inSegmentIntrRegion(redBall.getPos())){
					System.out.println("Line-Red Ball collision");
					redBall.setDirection(WorldUtils.reflect(redBall.getDirection(), line.getNormalVector()));
					resetLine();
				}
			}
		}
	}
	
	private void showGameOverPoupUp(){
		Vector2 screenDims = WorldUtils.viewportToScreen(new Vector2(Constants.VIEWPORT_WIDTH, 
				Constants.VIEWPORT_HEIGHT),camera);
		
		Texture tPScoreBg = new Texture(Gdx.files.internal("scorebg.png"));
		Texture tReplay = new Texture(Gdx.files.internal("score_replay.png"));
		Texture tMenu = new Texture(Gdx.files.internal("score_menu.png"));
		BitmapFont font = new BitmapFont(Gdx.files.internal("fonts/hobostd.fnt"),
                Gdx.files.internal("fonts/hobostd.png"), false);
		
		redBall.remove();
		blueBall.remove();
		food.remove();
		table.remove();
		
		pScoreBg = new Image(tPScoreBg);
		//pScoreBg.setSize(screenDims.x*.75f, screenDims.y*.75f);
		//pScoreBg.setPosition(screenDims.x/8, screenDims.y/8);
		//addActor(pScoreBg);
		float sq = screenDims.x*.75f;
		
		LabelStyle style = new LabelStyle();
		style.font = font;
		pScore = new Label(score.toString(), style);
		float fontScale = sq > 64 ? sq/(4*64) : 1;
		pScore.setFontScale(fontScale, fontScale);
		
		ButtonStyle bPReplayStyle = new ButtonStyle();
		Image tmp1 = new Image(tReplay);
		pReplay = new Button(tmp1.getDrawable(),tmp1.getDrawable(),tmp1.getDrawable());
		Image tmp2 = new Image(tMenu);
		pMenu = new Button(tmp2.getDrawable(),tmp2.getDrawable(),tmp2.getDrawable());
		
		Table pOptionsTable = new Table();
		pOptionsTable.add(pReplay).expandX();
		pOptionsTable.add(pMenu).expandX();
		
		Table pTable = new Table();
		
		pTable.setSize(sq, sq);
		//pTable.setPosition(screenDims.x/2 - sq/2, screenDims.y/2 - sq/2);
		pTable.setPosition(screenDims.x/2 - sq/2, screenDims.y);
		pTable.setBackground(pScoreBg.getDrawable());
		
		pTable.add(pScore).height(sq/4).align(Align.center);
		pTable.row();
		pTable.add(pOptionsTable);
		pTable.setDebug(true);
		addActor(pTable);
		
		//addActor(pScoreBg);
		
		
		//pTable.addAction(Actions.sequence(Actions.alpha(0f),Actions.fadeIn(4f)));
		pTable.addAction(
				Actions.sequence(
						Actions.moveTo(screenDims.x/2 - sq/2, screenDims.y/2 - sq/2 , 0.8f)
						)
		);
		
		pReplay.addListener(new InputListener(){
			 public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
			        System.out.println("down");
			        replay();
			        return true;
			    }
		});
		
		pMenu.addListener(new InputListener(){
			 public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
			        System.out.println("down");
			        goToMainMenu();
			        return true;
			    }
		});
		
	}
	
	private void goToMainMenu(){
		this.gballGame.setScreen(new StartScreen(this.gballGame));
	}
	
	private void replay(){
		this.gballGame.setScreen(new GameScreen(this.gballGame));
	}
	
	
	private void setupFood(int x, int y) {
		food = new Food(world, camera, x, y,Food.FoodType.NORMAL);
		addActor(food);	
	}

	private void setupBall() {
		blueBall = new BallT(camera,margins, new Vector2(1,1), Constants.FOOD_WIDTH+1, Constants.FOOD_HEIGHT+1,"b2.png");
		redBall = new BallT(camera,margins, new Vector2(1,-1), Constants.FOOD_WIDTH+1, Constants.VIEWPORT_HEIGHT-Constants.FOOD_HEIGHT-1,"b1.png");
		addActor(blueBall);
		addActor(redBall);
	}


	private void setupWorld() {
		world = WorldUtils.createWorld();
		margins = WorldUtils.viewportToScreen(new Vector2(Constants.TOP_MARGIN,Constants.MARGIN), camera);
		screenDims = WorldUtils.viewportToScreen(new Vector2(Constants.VIEWPORT_WIDTH,Constants.VIEWPORT_HEIGHT), camera);
		bgWood = new BgGameStage(screenDims, margins,camera);
		addActor(bgWood);
	}

	private void setupCamera() {
		camera = new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
		camera.position.set(camera.viewportWidth / 2,
				camera.viewportHeight / 2, 0f);
		camera.update();
	}
	

	@Override
	public void draw() {
		super.draw();
		//renderer.render(world, camera.combined);

	}

	public Vector2 screenToViewport(Vector2 v) {
		Vector3 vtemp = camera.unproject(new Vector3(v.x, v.y, 0));
		return new Vector2(vtemp.x, vtemp.y);
	}

}
