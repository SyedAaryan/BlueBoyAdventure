package debugger;

import main.GamePanel;

public class Debugger {

    GamePanel gp;

    public Debugger(GamePanel gp) {

        this.gp = gp;

    }

    // This method is to show the solid area of the player for collision
    //Set solidAreaDebugSwitch as true
    public void showSolidAreaOfPlayer() {

        gp.player.solidAreaDebugSwitch = false;

    }

}
