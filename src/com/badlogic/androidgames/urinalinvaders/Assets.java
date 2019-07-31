package com.badlogic.androidgames.urinalinvaders;

import com.badlogic.androidgames.framework.Music;
import com.badlogic.androidgames.framework.Sound;
import com.badlogic.androidgames.framework.gl.Animation;
import com.badlogic.androidgames.framework.gl.Font;
import com.badlogic.androidgames.framework.gl.ObjLoader;
import com.badlogic.androidgames.framework.gl.Texture;
import com.badlogic.androidgames.framework.gl.TextureRegion;
import com.badlogic.androidgames.framework.gl.Vertices3;
import com.badlogic.androidgames.framework.impl.GLGame;

public class Assets {
	public static Texture background;
	public static Texture background2;
	public static Texture background3;
	public static Texture items;
	public static TextureRegion puddle;
	public static TextureRegion backgroundRegion;
	public static TextureRegion backgroundRegion2;
	public static TextureRegion backgroundRegion3;
	public static TextureRegion logoRegion;
	public static TextureRegion menuRegion;
	public static TextureRegion gameOverRegion;
	public static TextureRegion pauseRegion;
	public static TextureRegion settingsRegion;
	public static TextureRegion touchRegion;
	public static TextureRegion accelRegion;
	public static TextureRegion touchEnabledRegion;
	public static TextureRegion accelEnabledRegion;
	public static TextureRegion soundRegion;
	public static TextureRegion soundEnabledRegion;
	public static TextureRegion leftRegion;
	public static TextureRegion rightRegion;
	public static TextureRegion fireRegion;
	public static TextureRegion pauseButtonRegion;
	public static TextureRegion quote;
	public static Font font;

	public static Texture explosionTexture;
	public static Animation explosionAnim;
	
	public static Vertices3 shipModel;
	public static Texture shipTexture;
	
	public static Vertices3 invaderModel;
	public static Texture invaderTexture;
	
	public static Vertices3 urinalModel;
	
	
	public static Vertices3 shotModel;
	public static Vertices3 shieldModel;

	
	
	
	public static Music music;
	public static Music water;
	public static Sound clickSound;
	public static Sound hitSound;
	public static Sound hurtSound;
	public static Sound shotSound;
	public static Sound missSound;

	public static void load(GLGame game) {
		background = new Texture(game, "bathroom.png", true);
		background2 = new Texture(game, "bathroom_outside.jpg", true);
		background3 = new Texture(game, "bathroom_gg.jpg", true);
		backgroundRegion = new TextureRegion(background, 0, 0, 512, 314);
		backgroundRegion2 = new TextureRegion(background2, 0, 0, 512, 314);
		backgroundRegion3 = new TextureRegion(background3, 0, 0, 512, 314);

		puddle = new TextureRegion(background, 0, 316, 195, 110);
		
		
		
		items = new Texture(game, "items2.png", true);
		logoRegion = new TextureRegion(items, 0, 256, 252, 126);
		menuRegion = new TextureRegion(items, 0, 128, 224, 64);
		gameOverRegion = new TextureRegion(items, 224, 128, 128, 64);
		pauseRegion = new TextureRegion(items, 0, 192, 160, 64);
		settingsRegion = new TextureRegion(items, 0, 160, 224, 32);
		
		touchRegion = new TextureRegion(items, 0, 384, 64, 64);
		touchEnabledRegion = new TextureRegion(items, 0, 448, 64, 64);
		
		accelRegion = new TextureRegion(items, 64, 384, 64, 64);
		accelEnabledRegion = new TextureRegion(items, 64, 448, 64, 64);
		
		soundRegion = new TextureRegion(items, 128, 384, 64, 64);
		soundEnabledRegion = new TextureRegion(items, 192, 384, 64, 64);
		
		leftRegion = new TextureRegion(items, 0, 0, 64, 64);
		rightRegion = new TextureRegion(items, 64, 0, 64, 64);
		
		fireRegion = new TextureRegion(items, 128, 0, 64, 64);
		pauseButtonRegion = new TextureRegion(items, 0, 64, 64, 64);
		quote = new TextureRegion(items, 318, 448, 194, 64);
		
		font = new Font(items, 224, 0, 16, 16, 20);
		explosionTexture = new Texture(game, "explode.png", true);
		TextureRegion[] keyFrames = new TextureRegion[16];
		int frame = 0;
		for (int y = 0; y < 256; y += 64) {
			for (int x = 0; x < 256; x += 64) {
				keyFrames[frame++] = new TextureRegion(explosionTexture, x, y,
						64, 64);
			}
		}
		explosionAnim = new Animation(0.1f, keyFrames);

		shipTexture = new Texture(game, "guy.png", true);
		shipModel = ObjLoader.load(game, "guy.obj");
		
		invaderTexture = new Texture(game, "urinal.png", true);
		invaderModel = ObjLoader.load(game, "invader.obj");
		
		urinalModel = ObjLoader.load(game, "urinal.obj");
		
		shieldModel = ObjLoader.load(game, "shield.obj");
		shotModel = ObjLoader.load(game, "shot.obj");

		music = game.getAudio().newMusic("porno.mp3");
		music.setLooping(true);
		music.setVolume(0.5f);
		if (Settings.soundEnabled)
			music.play();
		
		water = game.getAudio().newMusic("water-continuous.wav");
		water.setLooping(true);
		water.setVolume(1.0f);
		
		clickSound = game.getAudio().newSound("click.ogg");
		hitSound = game.getAudio().newSound("coin.wav");
		hurtSound = game.getAudio().newSound("ah.wav");
		shotSound = game.getAudio().newSound("shot.ogg");
		missSound = game.getAudio().newSound("buzzer.wav");
	}

	public static void reload() {
		background.reload();
		background2.reload();
		background3.reload();
		items.reload();
		explosionTexture.reload();
		shipTexture.reload();
		invaderTexture.reload();
		if (Settings.soundEnabled)
			music.play();
	}

	public static void playSound(Sound sound) {
		if (Settings.soundEnabled)
			sound.play(1);
	}
}
