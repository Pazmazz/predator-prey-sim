package classes;

import java.util.Arrays;
import java.util.Random;

public class main{
    public static void main(String[] args) {
        Object[][] playField = new Object[3][3];

        bugSpawn(playField, new Ant());
        bugSpawn(playField, new Doodlebug());
        
        System.out.println(Arrays.deepToString(playField));
    }

    static void bugSpawn(Object[][] playField, Bug bug){
        Random random = new Random();

        int spawnPosX = random.nextInt(2);
        int spawnPosY = random.nextInt(2);

        if (playField[spawnPosX][spawnPosY] == null){
            playField[spawnPosX][spawnPosY] = bug;
        }
    }
}