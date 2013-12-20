package fr.umlv.masterPilot.world;

import fr.umlv.masterPilot.Interface.SpaceShip;
import fr.umlv.masterPilot.enemy.Cruiser;
import fr.umlv.masterPilot.enemy.TIE;
import fr.umlv.masterPilot.hero.Hero;

/**
 * Created by emmanuel on 19/12/13.
 */
public class SpaceshipFactory {

    private final MasterPilot masterPilot;

    public SpaceshipFactory(MasterPilot masterPilot) {

        this.masterPilot = masterPilot;
    }
    public  Hero createHero(int x, int y){
        Hero h = new Hero(masterPilot.getWorld(), 0, 0);

        h.create();
        masterPilot.setHero(h);

        return h;
    }

    /**
     *  create an enemy according to his type
     *  create his body in the world
     *  and register it in enemy Manager
     * @param enemyType
     * @param x
     * @param y
     * @param hero
     * @return
     */
    public SpaceShip createEnemy(String enemyType, int x, int y, Hero hero) {
        SpaceShip space;
        switch (enemyType) {
            case "TIE":
                space = new TIE(this.masterPilot.getWorld(), x, y, hero);


                break;

            case "CRUISER":
                space = new Cruiser(this.masterPilot.getWorld(), x, y);

                break;

            case "SQUADRON":
//                space = new Squadron(this.world,x,y,hero);
                throw new UnsupportedOperationException();
//                break;
            default:
                throw new UnsupportedOperationException();
        }
        space.create();
        this.masterPilot.addToSpaceshipManager(space.getBody(), space);
        return space;
    }
}
