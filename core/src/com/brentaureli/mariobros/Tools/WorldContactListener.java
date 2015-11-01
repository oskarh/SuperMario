package com.brentaureli.mariobros.Tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.brentaureli.mariobros.MarioBros;
import com.brentaureli.mariobros.Sprites.Enemies.Enemy;
import com.brentaureli.mariobros.Sprites.Items.Item;
import com.brentaureli.mariobros.Sprites.Mario;
import com.brentaureli.mariobros.Sprites.Other.FireBall;
import com.brentaureli.mariobros.Sprites.TileObjects.InteractiveTileObject;

/**
 * Created by brentaureli on 9/4/15.
 */
public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDef){
            case MarioBros.MARIO_HEAD_BIT | MarioBros.BRICK_BIT:
            case MarioBros.MARIO_HEAD_BIT | MarioBros.COIN_BIT:
                if(fixA.getFilterData().categoryBits == MarioBros.MARIO_HEAD_BIT)
                    ((InteractiveTileObject) fixB.getUserData()).onHeadHit((Mario) fixA.getUserData());
                else
                    ((InteractiveTileObject) fixA.getUserData()).onHeadHit((Mario) fixB.getUserData());
                break;
            case MarioBros.ENEMY_HEAD_BIT | MarioBros.MARIO_BIT:
                Mario mario = (Mario) (fixA.getUserData() instanceof Mario ? fixA.getUserData() : fixB.getUserData());
                // Only kill enemies if we're landing on them, ie not if we're jumping and hit an enemy on our way up
                System.out.println("Linear velocity " + mario.getBody().getLinearVelocity().y);
                if (mario.getBody().getLinearVelocity().y < 0 && mario.isJumping()) {
                    if(fixA.getFilterData().categoryBits == MarioBros.ENEMY_HEAD_BIT)
                        ((Enemy)fixA.getUserData()).hitOnHead((Mario) fixB.getUserData());
                    else
                        ((Enemy)fixB.getUserData()).hitOnHead((Mario) fixA.getUserData());
                }
                break;
            case MarioBros.ENEMY_BIT | MarioBros.OBJECT_BIT:
            case MarioBros.ENEMY_BIT | MarioBros.PIPE_BIT:
                if(fixA.getFilterData().categoryBits == MarioBros.ENEMY_BIT)
                    ((Enemy)fixA.getUserData()).reverseVelocity();
                else
                    ((Enemy)fixB.getUserData()).reverseVelocity();
                break;
            case MarioBros.MARIO_BIT | MarioBros.ENEMY_BIT:
                if(fixA.getFilterData().categoryBits == MarioBros.MARIO_BIT)
                    ((Mario) fixA.getUserData()).hit((Enemy)fixB.getUserData());
                else
                    ((Mario) fixB.getUserData()).hit((Enemy)fixA.getUserData());
                break;
            case MarioBros.ENEMY_BIT:
                ((Enemy)fixA.getUserData()).hitByEnemy((Enemy)fixB.getUserData());
                ((Enemy)fixB.getUserData()).hitByEnemy((Enemy)fixA.getUserData());
                break;
            case MarioBros.ITEM_BIT | MarioBros.OBJECT_BIT:
            case MarioBros.ITEM_BIT | MarioBros.PIPE_BIT:
                if(fixA.getFilterData().categoryBits == MarioBros.ITEM_BIT)
                    ((Item)fixA.getUserData()).reverseVelocity();
                else
                    ((Item)fixB.getUserData()).reverseVelocity();
                break;
            case MarioBros.ITEM_BIT | MarioBros.MARIO_BIT:
                if(fixA.getFilterData().categoryBits == MarioBros.ITEM_BIT)
                    ((Item)fixA.getUserData()).use((Mario) fixB.getUserData());
                else
                    ((Item)fixB.getUserData()).use((Mario) fixA.getUserData());
                break;
            case MarioBros.FIREBALL_BIT | MarioBros.OBJECT_BIT:
                if(fixA.getFilterData().categoryBits == MarioBros.FIREBALL_BIT)
                    ((FireBall)fixA.getUserData()).setToDestroy();
                else
                    ((FireBall)fixB.getUserData()).setToDestroy();
                break;
            case MarioBros.PIPE_BIT | MarioBros.MARIO_BIT:
            case MarioBros.GROUND_BIT | MarioBros.MARIO_BIT:
            case MarioBros.COIN_BIT | MarioBros.MARIO_BIT:
            case MarioBros.BRICK_BIT | MarioBros.MARIO_BIT:
                if(fixA.getFilterData().categoryBits == MarioBros.MARIO_BIT)
                    ((Mario) fixA.getUserData()).touchedGround();
                else
                    ((Mario) fixB.getUserData()).touchedGround();
                break;
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDef) {
            case MarioBros.GROUND_BIT | MarioBros.MARIO_BIT:
            case MarioBros.COIN_BIT | MarioBros.MARIO_BIT:
            case MarioBros.BRICK_BIT | MarioBros.MARIO_BIT:
            case MarioBros.PIPE_BIT | MarioBros.MARIO_BIT:
                if(fixA.getFilterData().categoryBits == MarioBros.MARIO_BIT)
                    ((Mario) fixA.getUserData()).leftGround();
                else
                    ((Mario) fixB.getUserData()).leftGround();
                break;
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
