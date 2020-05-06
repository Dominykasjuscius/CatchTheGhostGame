package com.mygdx.game;

/**
 * Created by ddomi on 18-Oct-17.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import static com.mygdx.game.Main.GAMEOVER;
import static com.mygdx.game.Main.HEIGHT;
import static com.mygdx.game.Main.PlayerSize;
import static com.mygdx.game.Main.WIDTH;
import static com.mygdx.game.Main.number;
import static com.mygdx.game.Main.score;


public class Enemy {
    Vector2 pos;
    Texture ghost;
    float time, random1, random2, gSpeed;
    Enemy(int x, int y) {
        this.pos = new Vector2(x, y);
        ghost = new Texture("ghost.png");
        time = MathUtils.random(5,8);
        random1 = MathUtils.random(-2*WIDTH, 2*WIDTH);
        random2 = MathUtils.random(-2*HEIGHT,2*HEIGHT);
    }

    public void render(SpriteBatch batch) {
        if (Main.pos.dst(this.pos.x, this.pos.y) < 200 + PlayerSize * 4 && !GAMEOVER) {
            batch.draw(ghost, this.pos.x - 15, this.pos.y - 15, 30, 30);
        }
    }

    public Vector2 getPos() {
        return pos;
    }

    public void setPos(Vector2 pos) {
        this.pos = pos;
    }

    public void update(float dt) {

        if(this.pos.x > 2*WIDTH) this.pos.x = 2*WIDTH;
        if(this.pos.x < -2*WIDTH) this.pos.x = -2*WIDTH;
        if(this.pos.y > 2*HEIGHT) this.pos.y = 2*HEIGHT;
        if(this.pos.y < -2*HEIGHT) this.pos.y = -2*HEIGHT;

        if (Vector2.dst(this.pos.x, this.pos.y, Main.pos.x, Main.pos.y) > PlayerSize / 2 && Vector2.dst(this.pos.x, this.pos.y, Main.pos.x, Main.pos.y) < PlayerSize && PlayerSize >= 50) {
            move(this.pos, Main.pos.x, Main.pos.y, dt, false, gSpeed + PlayerSize/2);
        }

        gSpeed = score*2;
        if(gSpeed >= 100) gSpeed = 100;

        else if (Vector2.dst(this.pos.x, this.pos.y, Main.pos.x, Main.pos.y) < PlayerSize/2) {
            score++;

            if(number <=0) {
                number -= 0.4;
            }
            else number -= 0.6;

            this.pos.set(MathUtils.random(-3*WIDTH, 3*WIDTH), MathUtils.random(-3*HEIGHT, 3*HEIGHT));
        }
        else if(Vector2.dst(this.pos.x, this.pos.y, Main.pos.x, Main.pos.y) >  PlayerSize) {
            time -= Gdx.graphics.getDeltaTime();

            if (time >= 0) {
                move(this.pos, random1, random2, dt, true, 30 + PlayerSize / 2);
            } else {
                random1 = MathUtils.random(-2*WIDTH, 2*WIDTH);
                random2 = MathUtils.random(-2*HEIGHT, 2*HEIGHT);
                time = MathUtils.random(5, 8);
            }
        }

        if(this.pos.dst(random1,random2) < 10) {
            random1 = MathUtils.random(-2*WIDTH, 2*WIDTH);
            random2 = MathUtils.random(-2*HEIGHT, 2*HEIGHT);
        }
    }

    public void move(Vector2 start, float endX, float endY, float dt, boolean towards, float speed) {
        Vector2 path = new Vector2(endX - start.x, endY - start.y);
        float distance = (float) Math.sqrt(path.x * path.x + path.y * path.y);
        float directionX = path.x / distance;
        float directionY = path.y / distance;
        if (towards) {
            start.x += directionX * speed * dt;
            start.y += directionY * speed * dt;
        } else {
            start.x -= directionX * speed * dt;
            start.y -= directionY * speed * dt;
        }
    }
}