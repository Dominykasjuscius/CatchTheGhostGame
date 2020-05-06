package com.mygdx.game;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Main extends ApplicationAdapter{
	public static int ENEMY_COUNT = 50;
	public static int PLAYER_SPEED = 5;
	public static boolean USE_MOUSE = false;
	public static float PlayerSize = 100;
	public static float playerSmallestSize = 100;
	public static float PlayerBiggestSize = 400;
	SpriteBatch batch;

	Texture pacman, bg, circle;
	public static Vector2 pos;
	String message;
	BitmapFont font, font2;
	public static int WIDTH, HEIGHT;
	public static int score;
	float num1, num2, time;
	public static boolean GAMEOVER;
	public static float number, healthpoints;
	OrthographicCamera cam;
	TextureRegion textureRegion;
	Array<Enemy> henchmen;
	NativePlatform nativePlatform;


	public Main(NativePlatform nativePlatform) {
		this.nativePlatform = nativePlatform;
	}

	@Override
	public void create () {
		WIDTH = Gdx.graphics.getWidth();
		HEIGHT = Gdx.graphics.getHeight();
		batch = new SpriteBatch();
		bg = new Texture("square.png");
		pacman = new Texture("pacman.png");
		circle = new Texture("circle.png");
		pos = new Vector2(WIDTH, HEIGHT);
		cam = new OrthographicCamera();
		font = new BitmapFont();
		font2 = new BitmapFont();
		henchmen = new Array<Enemy>();
		number = 0;
		healthpoints = 2;
		GAMEOVER = false;
		textureRegion = new TextureRegion(pacman);
		generateText();
		for(int i = 0; i < ENEMY_COUNT; i++) {
			//henchmen.add(new Enemy(MathUtils.random(-2*HEIGHT, 2*HEIGHT), MathUtils.random(-2*HEIGHT, 2*HEIGHT)));
		}
	}

	@Override
	public void render () {


		if(number >=0) {
			number += 0.002f;
		}
		else number += 0.003f;

		PlayerSize -= number;
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		float Pitch = MathUtils.round((Gdx.input.getPitch()+40));
		float Roll = MathUtils.round(Gdx.input.getRoll());

		if(!GAMEOVER) {
			num1 += Pitch / 200;
			num2 += Roll / 400;
			pos.x += num2;
			pos.y += num1;
		}
		else nativePlatform.StartActivity();

		if(PlayerSize < playerSmallestSize) {
			PlayerSize = playerSmallestSize;
			healthpoints -= 1;
		}
		if(PlayerSize > PlayerBiggestSize) PlayerSize = PlayerBiggestSize;

		message = "Pitch: " + num1 +  "\nRoll: " + num2 + " " + number;
		batch.setProjectionMatrix(cam.combined);
		batch.begin();

		background();
		generateEnemies();
		generateLimits();
		camera();

		batch.draw(circle, pos.x - PlayerSize/2, pos.y - PlayerSize/2, PlayerSize, PlayerSize);
		font2.draw(batch, "score: " + number + "\n" + "HP: " + Math.round(healthpoints) + "\n" + "Mass: " + Math.round(PlayerSize), pos.x - 25, pos.y -50);
		if(GAMEOVER && Gdx.input.justTouched()) {

			healthpoints = 1000;
			GAMEOVER = false;
			PlayerSize = 150;
			score = 0;
		}
		batch.end();
	}

	public void background() {
		for(int i = -4*WIDTH; i <= 4*WIDTH; i += 120) {
			for (int g = -4*HEIGHT; g <= 4*HEIGHT; g += 120) {
				if(pos.dst(i,g) < 300+PlayerSize*4)
					batch.draw(bg, i, g, 120, 120);
			}
		}
	}

	public void generateLimits() {
		if(num1 >= PLAYER_SPEED) num1 = PLAYER_SPEED;
		if(num1 <= -PLAYER_SPEED) num1 = -PLAYER_SPEED;
		if(num2 >= PLAYER_SPEED) num2 = PLAYER_SPEED;
		if(num2 <= -PLAYER_SPEED) num2 = -PLAYER_SPEED;

		if(pos.x >= 2*WIDTH) pos.x = 2*WIDTH;
		if(pos.x <= -2*WIDTH) pos.x = -2*WIDTH;
		if(pos.y >= 2*HEIGHT) pos.y = 2*HEIGHT;
		if(pos.y <= -2*HEIGHT) pos.y = -2*HEIGHT;

		if(number >= 0.4f) number = 0.4f;
		if(number <= -0.9f) number = -0.9f;

		if(healthpoints <=0) {
			GAMEOVER = true;
			healthpoints = 0;
			}
		}

	public void generateEnemies() {
		for(Enemy e : henchmen) {
			e.update(Gdx.graphics.getDeltaTime());
			e.render(batch);
		}
	}

	public void camera() {
		cam.setToOrtho(false, WIDTH/4 + PlayerSize*2, HEIGHT/4 + PlayerSize*4);
		cam.translate(pos.x - (WIDTH/4 + PlayerSize*2)/2, pos.y - (HEIGHT/4 + PlayerSize*4)/2);
		cam.update();
	}

	public void generateDialogue() {

	}
	public void generateText() {

		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		FreeTypeFontGenerator.FreeTypeFontParameter parameter2 = new FreeTypeFontGenerator.FreeTypeFontParameter();

		parameter.size = 20;
		parameter.characters = "-AGAMEOVERPitchRollScor-e:0123456789.!'()>?";

		parameter2.size = 30;
		parameter2.characters = "MassPHi-ghsScore0123456789:.!'()>?";

		font = generator.generateFont(parameter);
		font.setColor(Color.BLACK);

		font2 = generator.generateFont(parameter2);
		font2.setColor(Color.BLACK);

		generator.dispose();
	}

	@Override
	public void dispose () {
		batch.dispose();
		font.dispose();
		bg.dispose();
		pacman.dispose();
	}
	
}