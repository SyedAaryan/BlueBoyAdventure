package main;

import entity.NPC_OldMan;
import monster.MON_GreenSlime;
import object.*;
import tile_interactive.IT_DryTree;

public class AssetSetter {

    GamePanel gp;

    public AssetSetter(GamePanel gp) {

        this.gp = gp;

    }

    // Check the EOF for MapNum
    public void setObject() {

        int mapNum = 0;
        int i = 0;
        gp.obj[mapNum][i] = new OBJ_Coin_Bronze(gp);
        gp.obj[mapNum][i].worldX = gp.tileSize * 25;
        gp.obj[mapNum][i].worldY = gp.tileSize * 23;
        i++;

        gp.obj[mapNum][i] = new OBJ_Heart(gp);
        gp.obj[mapNum][i].worldX = gp.tileSize * 21;
        gp.obj[mapNum][i].worldY = gp.tileSize * 19;
        i++;

        gp.obj[mapNum][i] = new OBJ_ManaCrystal(gp);
        gp.obj[mapNum][i].worldX = gp.tileSize * 26;
        gp.obj[mapNum][i].worldY = gp.tileSize * 21;
        i++;

        gp.obj[mapNum][i] = new OBJ_Axe(gp);
        gp.obj[mapNum][i].worldX = gp.tileSize * 33;
        gp.obj[mapNum][i].worldY = gp.tileSize * 21;
        i++;

        gp.obj[mapNum][i] = new OBJ_Shield_Blue(gp);
        gp.obj[mapNum][i].worldX = gp.tileSize * 35;
        gp.obj[mapNum][i].worldY = gp.tileSize * 21;
        i++;

        gp.obj[mapNum][i] = new OBJ_Potion_Red(gp);
        gp.obj[mapNum][i].worldX = gp.tileSize * 22;
        gp.obj[mapNum][i].worldY = gp.tileSize * 27;
    }

    // Check the EOF for MapNum
    public void setNPC() {

//        gp.npc[0] = new NPC_OldMan(gp);
//        gp.npc[0].worldX = gp.tileSize * 9;
//        gp.npc[0].worldY = gp.tileSize * 10;

        int mapNum = 0;
        gp.npc[mapNum][0] = new NPC_OldMan(gp);
        gp.npc[mapNum][0].worldX = gp.tileSize * 21;
        gp.npc[mapNum][0].worldY = gp.tileSize * 21;

        mapNum = 1;
        gp.npc[mapNum][0] = new NPC_OldMan(gp);
        gp.npc[mapNum][0].worldX = gp.tileSize * 12;
        gp.npc[mapNum][0].worldY = gp.tileSize * 7;


    }

    // Check the EOF for MapNum
    public void setMonster() {

        int mapNum = 0;
        int i = 0;
        gp.monster[mapNum][i] = new MON_GreenSlime(gp);
        gp.monster[mapNum][i].worldX = gp.tileSize * 23;
        gp.monster[mapNum][i].worldY = gp.tileSize * 36;
        i++;

        gp.monster[mapNum][i] = new MON_GreenSlime(gp);
        gp.monster[mapNum][i].worldX = gp.tileSize * 23;
        gp.monster[mapNum][i].worldY = gp.tileSize * 37;
        i++;

        gp.monster[mapNum][i] = new MON_GreenSlime(gp);
        gp.monster[mapNum][i].worldX = gp.tileSize * 24;
        gp.monster[mapNum][i].worldY = gp.tileSize * 37;
        i++;

        gp.monster[mapNum][i] = new MON_GreenSlime(gp);
        gp.monster[mapNum][i].worldX = gp.tileSize * 34;
        gp.monster[mapNum][i].worldY = gp.tileSize * 42;
        i++;

        gp.monster[mapNum][i] = new MON_GreenSlime(gp);
        gp.monster[mapNum][i].worldX = gp.tileSize * 38;
        gp.monster[mapNum][i].worldY = gp.tileSize * 42;

//        mapNum = 1;
//        gp.monster[mapNum][i] = new MON_GreenSlime(gp);
//        gp.monster[mapNum][i].worldX = gp.tileSize * 23;
//        gp.monster[mapNum][i].worldY = gp.tileSize * 36;
//        i++;

    }

    // Check the EOF for MapNum
    public void setInteractiveTile() {

        int mapNum = 0;
        int i = 0;
        gp.iTile[mapNum][i] = new IT_DryTree(gp, 27, 12);
        i++;

        gp.iTile[mapNum][i] = new IT_DryTree(gp, 28, 12);
        i++;

        gp.iTile[mapNum][i] = new IT_DryTree(gp, 29, 12);
        i++;

        gp.iTile[mapNum][i] = new IT_DryTree(gp, 30, 12);
        i++;

        gp.iTile[mapNum][i] = new IT_DryTree(gp, 31, 12);
        i++;

        gp.iTile[mapNum][i] = new IT_DryTree(gp, 32, 12);
        i++;

        gp.iTile[mapNum][i] = new IT_DryTree(gp, 33, 12);
        i++;

        gp.iTile[mapNum][i] = new IT_DryTree(gp, 10, 40);
        i++;

        gp.iTile[mapNum][i] = new IT_DryTree(gp, 10, 41);
        i++;

        gp.iTile[mapNum][i] = new IT_DryTree(gp, 11, 41);
        i++;

    }

    /*So MapNum works as the pointer, i,e MapNum = 0 will place the monsters or NPC in the given location
     * of map0, when mapNum is changed to 1; it will spawn the monsters in that map, an example is provided in
     * setMonster class*/

}
