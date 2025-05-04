package environment;

import main.GamePanel;

import java.awt.Graphics2D;
import java.sql.ClientInfoStatus;

public class EnvironmentManager {

    GamePanel gp;
    public Lighting lighting;

    public EnvironmentManager(GamePanel gp) {
        this.gp = gp;
    }

    // Passing the circle size will increase/decrease the area, but it should not cross the max screen size, i,e 576 in this case
    public void setUp() {
        lighting = new Lighting(gp);
    }

    public void update() {
        lighting.update();
    }

    public void draw(Graphics2D g2) {
        lighting.draw(g2);
    }

}
