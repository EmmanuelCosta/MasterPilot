package fr.umlv.masterPilot.world;

import fr.umlv.masterPilot.ship.SpaceShip;
import fr.umlv.masterPilot.ship.enemy.Cruiser;
import fr.umlv.masterPilot.ship.enemy.Squadron;
import fr.umlv.masterPilot.ship.enemy.TIE;
import fr.umlv.masterPilot.ship.enemy.TIE2;
import fr.umlv.masterPilot.ship.hero.Hero;

/**
 * Created by emmanuel on 19/12/13.
 */
public class SpaceshipFactory {

    private final MasterPilotWorld masterPilotWorld;

    public SpaceshipFactory(MasterPilotWorld masterPilotWorld) {

        this.masterPilotWorld = masterPilotWorld;
    }
    public  Hero createHero(int x, int y){
        Hero h = new Hero(masterPilotWorld.getWorld(), 0, 0);

        h.create();
        masterPilotWorld.setHero(h);

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
                space = new TIE2(this.masterPilotWorld.getWorld(), x, y, hero);
                break;

            case "CRUISER":
                space = new Cruiser(this.masterPilotWorld.getWorld(), x, y, hero);
                break;

            case "SQUADRON":
                space = new Squadron(this.masterPilotWorld.getWorld(), x, y,hero);
                break;
            default:
                throw new UnsupportedOperationException();
        }
        space.create();
        this.masterPilotWorld.addToSpaceshipManager(space.getBody(), space);
        return space;
    }
}
