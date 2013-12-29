package fr.umlv.masterPilot.ship;


import fr.umlv.masterPilot.ship.enemy.*;
import fr.umlv.masterPilot.ship.hero.Hero;
import fr.umlv.masterPilot.world.MasterPilotWorld;

/**
 * this will create a Star and register it to the StarManager
 * Created by emmanuel on 28/12/13.

 */
public class SpaceshipFactory {

    private final MasterPilotWorld masterPilotWorld;

    public SpaceshipFactory(MasterPilotWorld masterPilotWorld) {

        this.masterPilotWorld = masterPilotWorld;
    }


    public Hero createHero(int x, int y, MasterPilotWorld.MODE mode) {
        Hero h = new Hero(masterPilotWorld.getWorld(), 0, 0, mode);


        h.create();
        masterPilotWorld.setHero(h);

        return h;
    }

    /**
     * <<<<<<< HEAD
     * create an enemy according to his type
     * create his body in the world
     * and register it in enemy Manager
     * <p>
     * =======
     * create an enemy according to his type
     * create his body in the world
     * and register it in enemy Manager
     * >>>>>>> d713d4794548f2f9100ba0683bc2286c74de1644
     *
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
                space = new TIE(this.masterPilotWorld.getWorld(), x, y, hero);
                break;
                
            case "CRUISER":
                space = new Cruiser(this.masterPilotWorld.getWorld(), x, y, hero);
                break;
                
            case "SQUADRON":
                space = new Squadron(this.masterPilotWorld.getWorld(), x, y, hero);
                break;
                
            case "INVADER":
                space = new Invader(this.masterPilotWorld.getWorld(), x, y, hero);
                break;
                
            case "SPACEBALL":
                space = new SpaceBall(this.masterPilotWorld.getWorld(), x, y, hero);
                break;
                
            default:
                throw new UnsupportedOperationException();
        }
        space.create();
        this.masterPilotWorld.addToSpaceshipManager(space.getBody(), space);
        return space;
    }
}
