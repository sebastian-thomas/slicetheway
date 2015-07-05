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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.myappstack.gball.actors.Ball;
import com.myappstack.gball.actors.BallT;
import com.myappstack.gball.actors.BgWhiteRep;
import com.myappstack.gball.actors.Food;
import com.myappstack.gball.actors.Line;
import com.myappstack.gball.utils.Constants;
import com.myappstack.gball.utils.WorldUtils;

public class GameStage extends Stage {

	private static final int VIEWPORT_WIDTH = Constants.VIEWPORT_WIDTH;
	private static final int VIEWPORT_HEIGHT = Constants.VIEWPORT_HEIGHT;

	private World world;
	//private Body wallLeft, wallRight, wallBottom, wallTop;
	private Vector2 touchStart;
	private Vector2 touchEnd;

	private Line line;
	private Food food;
	private BallT blueBall;
	private BallT redBall;
	private BgWhiteRep bgWhite;
	
	private Vector2 margins,screenDims;
	
	private Integer score;
	private Label scoreLabel;

	private final float TIME_STEP = 1 / 300f;
	private float accumulator = 0f;

	private OrthographicCamera camera;
	private Box2DDebugRenderer renderer;

	public GameStage() {
		setupCamera();
    	setupWorld();
    	
    	setupBall();
    	setupFood(Constants.VIEWPORT_WIDTH/2,Constants.VIEWPORT_HEIGHT/2);
    	setupScore();
    	Gdx.input.setInputProcessor(this);
        renderer = new Box2DDebugRenderer();
        
        touchStart = touchEnd = null;
    }

	private void setupScore() {
		score = 0;
		Vector2 dims = WorldUtils.viewportToScreen(new Vector2(6,6), camera);
		Vector2 pos = WorldUtils.viewportToScreen(new Vector2(0,Constants.VIEWPORT_HEIGHT-6), camera);
		BitmapFont font = new BitmapFont();
		LabelStyle style = new LabelStyle();
		Label text;
		style.font = font;
		scoreLabel = new Label("SCORE : "+score.toString(), style);
		scoreLabel.setText(score.toString());
		scoreLabel.setBounds(pos.x, pos.y, dims.x, dims.y);

		addActor(scoreLabel);
	}

	private void setupFood(int x, int y) {
		food = new Food(world, camera, x, y);
		addActor(food);	
	}

	private void setupBall() {
		//ball = new Ball(world);
		//addActor(ball);
		//bt = new BallT(camera, new Vector2(1,1), Constants.FOOD_WIDTH+1, Constants.FOOD_HEIGHT+1);
		blueBall = new BallT(camera,margins, new Vector2(1,1), Constants.FOOD_WIDTH+1, Constants.FOOD_HEIGHT+1,"b1.png");
		redBall = new BallT(camera,margins, new Vector2(1,-1), Constants.FOOD_WIDTH+1, Constants.VIEWPORT_HEIGHT-Constants.FOOD_HEIGHT-1,"b2.png");
		addActor(blueBall);
		addActor(redBall);
	}


	private void setupWorld() {
		world = WorldUtils.createWorld();
		margins = WorldUtils.viewportToScreen(new Vector2(Constants.TOP_MARGIN,Constants.MARGIN), camera);
		screenDims = WorldUtils.viewportToScreen(new Vector2(Constants.VIEWPORT_WIDTH,Constants.VIEWPORT_HEIGHT), camera);
		bgWhite = new BgWhiteRep(screenDims, margins,camera);
		addActor(bgWhite);
	}

	private void setupCamera() {
		camera = new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
		camera.position.set(camera.viewportWidth / 2,
				camera.viewportHeight / 2, 0f);
		camera.update();
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		// return super.touchDown(screenX, screenY, pointer, button);
		resetLine();
		touchStart = new Vector2(screenX, screenY);
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		// return super.touchUp(screenX, screenY, pointer, button);
		touchEnd = new Vector2(screenX, screenY);
		drawLineActor();
		return true;
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

	@Override
	public void act(float delta) {
		super.act(delta);
		// Fixed timestep
		accumulator += delta;
		while (accumulator >= delta) {
			world.step(TIME_STEP, 6, 2);
			accumulator -= TIME_STEP;
		}
		
		
		// TODO: Implement interpolation
		
		handleLineCollision();
		
		if(blueBall.getBounds().overlaps(redBall.getBounds())){
			System.out.println("game over");
		}

		if (blueBall.getBounds().overlaps(food.getBounds())) {
			System.out.println("Collided");
			int newXpos = MathUtils.random(1, Constants.VIEWPORT_WIDTH
					- Constants.FOOD_WIDTH - 1);
			int newYpos = MathUtils.random(1, Constants.VIEWPORT_HEIGHT
					- Constants.FOOD_HEIGHT - 1);
			food.changePos(newXpos, newYpos);
			score++;
			scoreLabel.setText("SCORE : "+score.toString());

		}
		
		if (redBall.getBounds().overlaps(food.getBounds())) {
			System.out.println("Collided");
			int newXpos = MathUtils.random(Constants.MARGIN + 1, Constants.VIEWPORT_WIDTH
					- Constants.FOOD_WIDTH - Constants.MARGIN - 1);
			int newYpos = MathUtils.random(Constants.MARGIN +1, Constants.VIEWPORT_HEIGHT
					- Constants.FOOD_HEIGHT - Constants.TOP_MARGIN - 1);
			food.changePos(newXpos, newYpos);
			score++;
			scoreLabel.setText(score.toString());

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

	@Override
	public void draw() {
		super.draw();
		renderer.render(world, camera.combined);

	}

	public Vector2 screenToViewport(Vector2 v) {
		Vector3 vtemp = camera.unproject(new Vector3(v.x, v.y, 0));
		return new Vector2(vtemp.x, vtemp.y);
	}

}
