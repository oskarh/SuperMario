package com.brentaureli.mariobros.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.brentaureli.mariobros.MarioBros;
import com.brentaureli.mariobros.Screens.PlayScreen;
import com.brentaureli.mariobros.Sprites.Enemies.Enemy;
import com.brentaureli.mariobros.Sprites.Enemies.Goomba;
import com.brentaureli.mariobros.Sprites.Enemies.Turtle;
import com.brentaureli.mariobros.Sprites.Mario;
import com.brentaureli.mariobros.Sprites.TileObjects.Brick;
import com.brentaureli.mariobros.Sprites.TileObjects.Coin;

/**
 * Created by brentaureli on 8/28/15.
 */
public class B2WorldCreator {

    public static final String GROUND_LAYER = "Ground";
    public static final String PIPES_LAYER = "Pipes";
    public static final String BRICKS_LAYER = "Bricks";
    public static final String COINS_LAYER = "Coins";
    public static final String GOOMBAS_LAYER = "Goombas";
    public static final String TURTLES_LAYER = "Turtles";
    private static final String PLAYER_LAYER = "Player";

    private Mario mario;
    Array<Enemy> enemies = new Array<Enemy>();

    public B2WorldCreator(PlayScreen screen){
        World world = screen.getWorld();
        TiledMap map = screen.getMap();
        //create body and fixture variables
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;
        enemies = new Array<Enemy>();

        //create ground bodies/fixtures
        createGround(world, map, bdef, shape, fdef);

        //create pipe bodies/fixtures
        createPipes(world, map, bdef, shape, fdef);

        //create brick bodies/fixtures
        createBricks(screen, map);

        //create coin bodies/fixtures
        createCoins(screen, map);

        //create all goombas
        createGoombas(screen, map);
        createTurtles(screen, map);

        mario = createPlayer(screen, map);
    }

    private void createGround(World world, TiledMap map, BodyDef bdef, PolygonShape shape, FixtureDef fdef) {
        Body body;
        for(MapObject object : map.getLayers().get(GROUND_LAYER).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / MarioBros.PPM, (rect.getY() + rect.getHeight() / 2) / MarioBros.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / MarioBros.PPM, rect.getHeight() / 2 / MarioBros.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        }
    }

    private void createPipes(World world, TiledMap map, BodyDef bdef, PolygonShape shape, FixtureDef fdef) {
        Body body;
        for(MapObject object : map.getLayers().get(PIPES_LAYER).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / MarioBros.PPM, (rect.getY() + rect.getHeight() / 2) / MarioBros.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / MarioBros.PPM, rect.getHeight() / 2 / MarioBros.PPM);
            fdef.shape = shape;
            fdef.filter.categoryBits = MarioBros.PIPE_BIT;
            fdef.filter.maskBits = MarioBros.MARIO_BIT
                    | MarioBros.OBJECT_BIT
                    | MarioBros.ENEMY_BIT
                    | MarioBros.ITEM_BIT
                    | MarioBros.FIREBALL_BIT;
            body.createFixture(fdef);
        }
    }

    private void createBricks(PlayScreen screen, TiledMap map) {
        for(MapObject object : map.getLayers().get(BRICKS_LAYER).getObjects().getByType(RectangleMapObject.class)){
            new Brick(screen, object);
        }
    }

    private void createCoins(PlayScreen screen, TiledMap map) {
        for(MapObject object : map.getLayers().get(COINS_LAYER).getObjects().getByType(RectangleMapObject.class)){
            new Coin(screen, object);
        }
    }

    private void createGoombas(PlayScreen screen, TiledMap map) {
        Array<Goomba> goombas = new Array<Goomba>();
        for(MapObject object : map.getLayers().get(GOOMBAS_LAYER).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            goombas.add(new Goomba(screen, rect.getX() / MarioBros.PPM, rect.getY() / MarioBros.PPM));
        }
        enemies.addAll(goombas);
    }

    private void createTurtles(PlayScreen screen, TiledMap map) {
        Array<Turtle> turtles = new Array<Turtle>();
        for(MapObject object : map.getLayers().get(TURTLES_LAYER).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            turtles.add(new Turtle(screen, rect.getX() / MarioBros.PPM, rect.getY() / MarioBros.PPM));
        }
        enemies.addAll(turtles);
    }

    private Mario createPlayer(PlayScreen screen, TiledMap map) {
        Rectangle playerPosition = map.getLayers().get(PLAYER_LAYER).getObjects().getByType(RectangleMapObject.class).first().getRectangle();
        Mario player = new Mario(screen, playerPosition.getX(), playerPosition.getY());
        return player;
    }

    public Mario getMario() {
        return mario;
    }

    public Array<Enemy> getEnemies(){
        return enemies;
    }
}
