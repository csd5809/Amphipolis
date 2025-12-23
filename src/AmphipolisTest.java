import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import java.util.ArrayList;
import Model.*;

public class AmphipolisTest {

    private Player player;
    private Board board;

    @Before
    public void setUp() {
        // arxikopoiisi prin apo kathe test
        player = new Player("TestPlayer", "Blue");
        board = new Board();
    }

    // test 1 elegxos vathmologias mosaikon
    @Test
    public void testMosaicScore() {
        ArrayList<Tile> tiles = new ArrayList<>();
        
        // senario 4 prasina mosaika dinoun 4 pontous
        tiles.add(new MosaicTile(1, "path/img.png", "Green"));
        tiles.add(new MosaicTile(2, "path/img.png", "Green"));
        tiles.add(new MosaicTile(3, "path/img.png", "Green"));
        tiles.add(new MosaicTile(4, "path/img.png", "Green"));
        
        player.addTiles(tiles);
        
        int score = player.calculateScore();
        assertEquals("4 Mosaics of same color should give 4 points", 4, score);
    }

    // test 2 elegxos vathmologias amphorewn
    @Test
    public void testAmphoraScore() {
        ArrayList<Tile> tiles = new ArrayList<>();
        
        // senario 4 amphoreis diaforetikwn xromatwn dinoun 2 pontous
        tiles.add(new AmphoraTile(10, "path/img.png", "Blue"));
        tiles.add(new AmphoraTile(11, "path/img.png", "Red"));
        tiles.add(new AmphoraTile(12, "path/img.png", "Green"));
        tiles.add(new AmphoraTile(13, "path/img.png", "Yellow"));
        
        player.addTiles(tiles);
        
        int score = player.calculateScore();
        assertEquals("4 different colored amphorae should give 2 points", 2, score);
    }

    // test 3 elegxos vathmologias skeletwn
    @Test
    public void testSkeletonScore() {
        ArrayList<Tile> tiles = new ArrayList<>();
        
        // senario mia oikogeneia 2 megaloi kai 1 mikros dinoun 6 pontous
        
        // megalos skeletos 1 panw kai katw
        tiles.add(new SkeletonTile(20, "path/img.png", true, true));   
        tiles.add(new SkeletonTile(21, "path/img.png", true, false));  
        
        // megalos skeletos 2 panw kai katw
        tiles.add(new SkeletonTile(22, "path/img.png", true, true));   
        tiles.add(new SkeletonTile(23, "path/img.png", true, false));  
        
        // mikros skeletos panw kai katw
        tiles.add(new SkeletonTile(24, "path/img.png", false, true));  
        tiles.add(new SkeletonTile(25, "path/img.png", false, false)); 
        
        player.addTiles(tiles);
        
        int score = player.calculateScore();
        
        assertEquals("Skeleton family calculation is incorrect", 6, score);
    }

    // test 4 elegxos katolisthisis an gemise i eisodos
    @Test
    public void testEntranceFull() {
        // gemizoume tin eisodo me 15 plakidia prepei na einai false
        for (int i = 0; i < 15; i++) {
            board.addTileToArea(new LandslideTile(i, "path/img.png"));
        }
        assertFalse("Entrance should not be full with 15 tiles", board.isEntranceFull());
        
        // prosthetoume to 16o plakidio prepei na einai true
        board.addTileToArea(new LandslideTile(16, "path/img.png"));
        assertTrue("Entrance should be full with 16 tiles", board.isEntranceFull());
    }

    // test 5 elegxos xrisis xaraktira
    @Test
    public void testCharacterUse() {
        // pairnoume ton proto xaraktira apo ti lista tou paikti
        Model.Character c = player.getMyCharacters().get(0);
        
        // prepei na einai unused stin arxi
        assertFalse("Character should start unused", c.isUsed());
        
        // ton xrisimopoioume
        player.useCharacter(c);
        
        // prepei na einai used meta ti xrisi
        assertTrue("Character should be marked as used after use", c.isUsed());
    }
}