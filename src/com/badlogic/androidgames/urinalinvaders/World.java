package com.badlogic.androidgames.urinalinvaders;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.androidgames.framework.math.OverlapTester;

public class World {
	public interface WorldListener {
		public void hit();

		public void shot();
		
		public void hurt();
	}

	final static float WORLD_MIN_X = -14;
	final static float WORLD_MAX_X = 14;
	final static float WORLD_MIN_Z = -15;

	WorldListener listener;
	int waves = 1;
	int score = 0;
	float speedMultiplier = 1;
	final List<Shot> shots = new ArrayList<Shot>();
	final List<Invader> invaders = new ArrayList<Invader>();
	final List<Shield> shields = new ArrayList<Shield>();
	final Ship ship;
	long lastShotTime;
	Random random;
	int hitCount = 0;

	public World() {
		ship = new Ship(0, 0, 0);
		generateInvaders();
		//generateShields();
		lastShotTime = System.nanoTime();
		random = new Random();
	}

	private void generateInvaders() {
		int row = 0;
		int column = 4;
		//for (int row = 0; row < 4; row++) {
		//	for (int column = 0; column < 8; column++) {
				Invader invader = new Invader(
					-WORLD_MAX_X / 2 + column * 2f,
					-9, 
					WORLD_MIN_Z + row * 2f
				);
				invader.bounds.radius *= 3.5;
				invaders.add(invader);
			//}
		//}
	}

	/*private void generateShields() {
		for (int shield = 0; shield < 3; shield++) {
			shields.add(new Shield(-10 + shield * 10 - 1, 0, -3));
			shields.add(new Shield(-10 + shield * 10 + 0, 0, -3));
			shields.add(new Shield(-10 + shield * 10 + 1, 0, -3));
			shields.add(new Shield(-10 + shield * 10 - 1, 0, -2));
			shields.add(new Shield(-10 + shield * 10 + 1, 0, -2));
		}
	}*/

	public void setWorldListener(WorldListener worldListener) {
		this.listener = worldListener;
	}

	public void update(float deltaTime, float accelX) {
		ship.update(deltaTime, accelX);
		updateInvaders(deltaTime);
		updateShots(deltaTime);

		checkShotCollisions();
		//checkInvaderCollisions();

		if (invaders.size() == 0) {
			generateInvaders();
			waves++;
			speedMultiplier += 0.5f;
		}
	}

	private void updateInvaders(float deltaTime) {
		int len = invaders.size();
		for (int i = 0; i < len; i++) {
			Invader invader = invaders.get(i);
			invader.update(deltaTime, speedMultiplier);

			/*if (invader.state == Invader.INVADER_ALIVE) {
				if (random.nextFloat() < 0.001f) {
					Shot shot = new Shot(invader.position.x,
							invader.position.y, invader.position.z,
							Shot.SHOT_VELOCITY);
					shots.add(shot);
					listener.shot();
				}
			}*/

			if (invader.state == Invader.INVADER_DEAD
					&& invader.stateTime > Invader.INVADER_EXPLOSION_TIME) {
				invaders.remove(i);
				i--;
				len--;
			}
		}
	}

	private void updateShots(float deltaTime) {
		int len = shots.size();
		for (int i = 0; i < len; i++) {
			Shot shot = shots.get(i);
			shot.update(deltaTime);
			if (shot.position.z < WORLD_MIN_Z  - 5 ||  shot.position.z > 0) {
				if (shot.position.z < WORLD_MIN_Z - 5) {
					ship.lives--;
					Assets.playSound(Assets.missSound);
				}
				shots.remove(i);
				i--;
				len--;
			}
		}
	}

	/*private void checkInvaderCollisions() {
		if (ship.state == Ship.SHIP_EXPLODING)
			return;

		int len = invaders.size();
		for (int i = 0; i < len; i++) {
			Invader invader = invaders.get(i);
			if (OverlapTester.overlapSpheres(ship.bounds, invader.bounds)) {
				ship.lives = 1;
				ship.kill();
				return;
			}
		}
	}*/

	private void checkShotCollisions() {
		int len = shots.size();
		for (int i = 0; i < len; i++) {
			Shot shot = shots.get(i);
			//boolean shotRemoved = false;

			int len2 = shields.size();
			/*for (int j = 0; j < len2; j++) {
				Shield shield = shields.get(j);
				if (OverlapTester.overlapSpheres(shield.bounds, shot.bounds)) {
					shields.remove(j);
					shots.remove(i);
					i--;
					len--;
					shotRemoved = true;
					break;
				}
			}
			if (shotRemoved)
				continue;*/

			if (shot.velocity.z < 0) {
				len2 = invaders.size();
				for (int j = 0; j < len2; j++) {
					Invader invader = invaders.get(j);
					if (OverlapTester.overlapSpheres(invader.bounds, shot.bounds) && invader.state == Invader.INVADER_ALIVE) {
						//invader.kill();
						listener.hit();
						score++;
						hitCount++;
						if (hitCount % 40 == 0) {
							invader.bounds.radius *= 0.75f;
						}
						shots.remove(i);
						i--;
						len--;
						
						Shot shotBack = new Shot(shot.position.x, shot.position.y, shot.position.z, Shot.SHOT_VELOCITY);
						float xVel = (float) ((random.nextFloat() - 0.5f) * 2) * 10f;
						shotBack.velocity.x = xVel;
						shotBack.velocity.y = -shot.velocity.y;
						shotBack.velocity.z = -shot.velocity.z;
						//shotBack.bounds.radius *= 2;
						shots.add(shotBack);
						
						//listener.shot();
						//break;
					}
				}
			} else {
				if (OverlapTester.overlapSpheres(shot.bounds, ship.bounds)
						&& ship.state == Ship.SHIP_ALIVE) {
					//ship.kill();
					ship.lives -= (ship.lives < 5 ? ship.lives:5);
					score -= 10;
					//listener.explosion();
					listener.hurt();
					shots.remove(i);
					i--;
					len--;
				}
			}
		}
	}

	public boolean isGameOver() {
		return ship.lives <= 0;
	}

	public void shot() {
		if (ship.state == Ship.SHIP_EXPLODING)
			return;

		int friendlyShots = 0;
		int len = shots.size();
		for (int i = 0; i < len; i++) {
			if (shots.get(i).velocity.z < 0)
				friendlyShots++;
		}

		if (System.nanoTime() - lastShotTime > 100000000 || friendlyShots == 0) {
			Shot newShot = new Shot(ship.position.x, ship.position.y, ship.position.z, -Shot.SHOT_VELOCITY);
			newShot.bounds.radius *= 2;
			shots.add(newShot);
			lastShotTime = System.nanoTime();
			//listener.shot();
		}
	}
}
