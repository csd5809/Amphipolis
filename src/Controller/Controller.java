package Controller;

import Model.*;
import View.GameWindow;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Controller {

    private Board board;
    private Bag bag;
    private ArrayList<Player> players;
    private int currentPlayerIndex;
    private GameWindow view;
    
    // gia to timer tis seiras tou kathe paikth 
    private Timer turnTimer;
    private int timeLeft;

    public Controller() {
        this.board = new Board();
        this.bag = new Bag(); 
        this.players = new ArrayList<>();
        
        // baze se kathe paiktj kai ena xrwma 
        players.add(new Player("Player 1", "Yellow"));
        players.add(new Player("Player 2", "Red"));
        players.add(new Player("Player 3", "Blue"));
        players.add(new Player("Player 4", "Black"));

        this.currentPlayerIndex = (int) (Math.random() * 4);

        this.view = new GameWindow();

        initListeners();
        
        // timwe update kathe 1 asec
        turnTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTimer();
            }
        });

        startTurn();
    }

    private void initListeners() {
        view.getDrawButton().addActionListener(e -> drawTilesForCurrentPlayer());
        view.getEndTurnButton().addActionListener(e -> endTurn());
        view.getMosaicAreaButton().addActionListener(e -> selectFromArea(0));
        view.getStatueAreaButton().addActionListener(e -> selectFromArea(1));
        view.getAmphoraAreaButton().addActionListener(e -> selectFromArea(2));
        view.getSkeletonAreaButton().addActionListener(e -> selectFromArea(3));
        
        ArrayList<JButton> charBtns = view.getCharacterButtons();
        for (int i = 0; i < charBtns.size(); i++) {
            final int charIndex = i;
            charBtns.get(i).addActionListener(e -> useCharacter(charIndex));
        }
    }

    private void selectFromArea(int areaIndex) {
        Player p = players.get(currentPlayerIndex);
    
        // Έλεγχος 1: Πρέπει να έχει τραβήξει από σακούλα πρώτα
        if (!p.hasPlayed()) {
            JOptionPane.showMessageDialog(view, "You must draw tiles from the bag first (Step 1)!");
            return;
        }

        // Έλεγχος 2: Πρέπει να ΜΗΝ έχει ξαναπάρει από το ταμπλό σε αυτόν τον γύρο
        if (p.hasPickedFromBoard()) {
            JOptionPane.showMessageDialog(view, "You have already picked tiles from a sorting area in this turn!");
            return;
        }
        
        ArrayList<Tile> areaTiles = getAreaTiles(areaIndex);
        
        if (areaTiles.isEmpty()) {
            JOptionPane.showMessageDialog(view, "This area is empty!");
            return;
        }
        
        // Παίρνει μέχρι 2 tiles
        int count = Math.min(2, areaTiles.size());
        ArrayList<Tile> taken = new ArrayList<>();
        
        for (int i = 0; i < count; i++) {
            Tile t = areaTiles.remove(0);
            taken.add(t);
            view.addTileToHand(t.getImagePath());
            board.removeTileFromArea(t);
        }
        
        p.addTiles(taken);
        p.setLastSelectedArea(areaIndex);
        p.setHasSelectedFromArea(true); // ΝΕΟ - Μαρκάρει ότι έχει πάρει
        view.updatePlayerInfo(p.getName(), p.getColor(), p.calculateScore());
        p.setHasPickedFromBoard(true); // Κλείδωσε ότι πήρε από το ταμπλό
        p.setLastSelectedArea(areaIndex); // Κράτα την περιοχή για τους χαρακτήρες
        view.updatePlayerInfo(p.getName(), p.getColor(), p.calculateScore());
        
        JOptionPane.showMessageDialog(view, "Took " + count + " tiles. You cannot select from areas again this turn!");
    }

    private void startTurn() {
        Player p = players.get(currentPlayerIndex);
        p.setHasPlayed(false);

        p.setHasPickedFromBoard(false); // Reset ότι δεν έχει πάρει ακόμα από το ταμπλό
        p.setLastSelectedArea(-1); // Reset την περιοχή που διάλεξε, ώστε να δουλέψουν σωστά οι χαρακτήρες
        p.setHasSelectedFromArea(false); // Reset για νέο γύρο
        p.setLastSelectedArea(-1); // Reset

        // Έλεγχος αν είχε παίξει TheCoder προηγούμενα
        if (p.getCoderSelectedArea() != -1) {
            ArrayList<Tile> tiles = getAreaTiles(p.getCoderSelectedArea());
            int count = Math.min(2, tiles.size());
            for (int i = 0; i < count; i++) {
                if (!tiles.isEmpty()) {
                    Tile t = tiles.remove(0);
                    p.addTiles(new ArrayList<>(java.util.Arrays.asList(t)));
                    board.removeTileFromArea(t);
                }
            }
            p.setCoderSelectedArea(-1); // Reset
            JOptionPane.showMessageDialog(view, "TheCoder effect: received " + count + " tiles!");
        }
        
        // Reset Timer
        timeLeft = 30; 
        view.updateTimer(timeLeft);
        turnTimer.start();

        // update to player info me ta katallhla conditions
        view.updatePlayerInfo(p.getName(), p.getColor(), p.calculateScore());
        view.playMusicForPlayer(currentPlayerIndex + 1);
        
        updateCharacterButtons(p);
        
        // clear th seira tou kai fortonoume ta epomena
        view.clearHandPanel();
        for (Tile t : p.getMyTiles()) {
            view.addTileToHand(t.getImagePath());
        }

        System.out.println("Turn started: " + p.getName());
    }
    
    private void updateTimer() {
        timeLeft--;
        view.updateTimer(timeLeft);
        
        if (timeLeft <= 0) {
            turnTimer.stop();
            JOptionPane.showMessageDialog(view, "Time ended. Next player");
            endTurn(true); 
        }
    }

    private void drawTilesForCurrentPlayer() {
        Player p = players.get(currentPlayerIndex);

        if (p.hasPlayed()) {
            JOptionPane.showMessageDialog(view, "You have already played!");
            return;
        }
        if (bag.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Bag is empty");
            return;
        }

        ArrayList<Tile> drawn = bag.drawTiles(4);
        ArrayList<Tile> findings = new ArrayList<>();

        for (Tile t : drawn) {
            if (t instanceof LandslideTile) {
                board.addTileToArea(t);
                view.addTileToBoard(t);  // ta emganizoume sto board sthn katallhlh thesi
                JOptionPane.showMessageDialog(view, "You drew a landslide tile!");// gia kathe tetoio tile emfanizei auto to mhnyma
                if (board.isEntranceFull()) {
                    turnTimer.stop();
                    calculateStatuePoints();
                    Player winner = players.get(0);
                    for (Player p1 : players) {
                        if (p1.calculateScore() > winner.calculateScore()) {
                            winner = p1;
                        }
                    }
                    StringBuilder message = new StringBuilder("Game Over!\n\n");
                    message.append("Final Scores:\n");

                    for (Player p2 : players) {
                        message.append(p2.getName()).append(": ").append(p2.calculateScore()).append(" points\n");
                    }

                    message.append("\nWinner: ").append(winner.getName()).append("!");

                    JOptionPane.showMessageDialog(view, message.toString());

                    view.stopMusic();
                    System.exit(0);
                }
            } else {
                findings.add(t);
                board.addTileToArea(t);  // ta emfanizei kai sth sosth thesi 
                view.addTileToBoard(t);  // kai sto board
                view.addTileToHand(t.getImagePath());
            }
        }
        
        p.addTiles(findings);
        p.setHasPlayed(true);
        
        // to idio me prin
        view.updatePlayerInfo(p.getName(), p.getColor(), p.calculateScore());
    }
    
    private void useCharacter(int index) {
        Player p = players.get(currentPlayerIndex);
        Model.Character c = p.getMyCharacters().get(index);

        if (!p.hasPlayed()) { // Έλεγχος αν τράβηξε από σακούλα
            JOptionPane.showMessageDialog(view, "You must draw tiles from the bag first!");
            return;
        }
        
        if (c.isUsed()) {
            JOptionPane.showMessageDialog(view, "This character has been used already!");
            return;
        }
        
        if (c instanceof Assistant) {
            handleAssistant(p);
        } else if (c instanceof Archaeologist) {
            handleArchaeologist(p);
        } else if (c instanceof Digger) {
            handleDigger(p);
        } else if (c instanceof Professor) {
            handleProfessor(p);
        } else if (c instanceof TheCoder) {
            handleTheCoder(p);
        }

        c.setUsed(true);
        JOptionPane.showMessageDialog(view, "You used: " + c.getClass().getSimpleName() + "!");
        
        view.getCharacterButtons().get(index).setEnabled(false);
    }


    
    private void updateCharacterButtons(Player p) {
        ArrayList<JButton> btns = view.getCharacterButtons();
        ArrayList<Model.Character> chars = p.getMyCharacters();
        
        for(int i=0; i<5; i++) {
            JButton btn = btns.get(i);
            Model.Character c = chars.get(i);
            
            // mporei na to kanei use mono an den exei xrisimopoihthei hdh 
            btn.setEnabled(!c.isUsed());
        }
    }

    public void endTurn() {
        endTurn(false);
    }

    public void endTurn(boolean force) {
        Player p = players.get(currentPlayerIndex);
        
        if (!force && !p.hasPlayed()) {
            JOptionPane.showMessageDialog(view, "You have to draw tiles first!");
            return;
        }

        turnTimer.stop(); 

        // to parakatw den to xreiazomai giati ebala gia kathe landslite tile drawn na elegxei an foulare j eisdodos
        /*
        if (board.isEntranceFull()) {
            JOptionPane.showMessageDialog(view, "End of the game");
            System.exit(0);
        }
        */

        currentPlayerIndex = (currentPlayerIndex + 1) % 4;
        startTurn();
    }
    private void calculateStatuePoints() { //  xreiazetai allh synarthsh gia ta score twm statue giati prepei na ginei sto telos kathe paixnidiou
        // oxi opws ta calculate score poy ginontai "realtime"
        
        // gia sfiges
        int maxSphinx = 0, minSphinx = Integer.MAX_VALUE;
        for (Player p : players) {
            int count = p.getSphinxCount();
            if (count > maxSphinx) maxSphinx = count;
            if (count < minSphinx) minSphinx = count;
        }
        
        for (Player p : players) {
            int count = p.getSphinxCount();
            if (count == maxSphinx && maxSphinx > 0) {
                p.addScore(6);
            } else if (count > minSphinx && count < maxSphinx) {
                p.addScore(3);
            }
            
        }
        
        // gia karyatides
        int maxCaryatid = 0, minCaryatid = Integer.MAX_VALUE;
        for (Player p : players) {
            int count = p.getCaryatidCount();
            if (count > maxCaryatid) maxCaryatid = count;
            if (count < minCaryatid) minCaryatid = count;
        }
        
        for (Player p : players) {
            int count = p.getCaryatidCount();
            if (count == maxCaryatid && maxCaryatid > 0) {
                p.addScore(6);
            } else if (count > minCaryatid && count < maxCaryatid) {
                p.addScore(3);
            }
            
        }
    }
    
    private void handleAssistant(Player p) {
    // Παίρνει 1 πλακίδιο από οποιαδήποτε περιοχή
        String[] options = {"Mosaics", "Statues", "Amphorae", "Skeletons"};
        int choice = JOptionPane.showOptionDialog(view, "Choose area:", "Assistant",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        
        if (choice >= 0) {
            ArrayList<Tile> tiles = getAreaTiles(choice);
            if (!tiles.isEmpty()) {
                Tile taken = tiles.remove(0);
                ArrayList<Tile> temp = new ArrayList<>();
                temp.add(taken);
                p.addTiles(temp);
                view.addTileToHand(taken.getImagePath());
                board.removeTileFromArea(taken);
            }
        }
    }

    private void handleArchaeologist(Player p) {
        if (p.getLastSelectedArea() == -1) {
            JOptionPane.showMessageDialog(view, "You must select an area first in step 2!");
            return;
        }
        
        String[] options = {"Mosaics", "Statues", "Amphorae", "Skeletons"};
        int choice = JOptionPane.showOptionDialog(view, "Choose DIFFERENT area (not " + options[p.getLastSelectedArea()] + "):", 
                "Archaeologist", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        
        if (choice >= 0 && choice != p.getLastSelectedArea()) {
            ArrayList<Tile> tiles = getAreaTiles(choice);
            int count = Math.min(2, tiles.size());
            
            for (int i = 0; i < count; i++) {
                if (!tiles.isEmpty()) {
                    Tile taken = tiles.remove(0);
                    p.addTiles(new ArrayList<>(java.util.Arrays.asList(taken)));
                    view.addTileToHand(taken.getImagePath());
                    board.removeTileFromArea(taken);
                }
            }
            view.updatePlayerInfo(p.getName(), p.getColor(), p.calculateScore());
        } else if (choice == p.getLastSelectedArea()) {
            JOptionPane.showMessageDialog(view, "You must choose a DIFFERENT area!");
        }
    }

    private void handleDigger(Player p) {
        if (p.getLastSelectedArea() == -1) {
            JOptionPane.showMessageDialog(view, "You must select an area first in step 2!");
            return;
        }
        
        ArrayList<Tile> tiles = getAreaTiles(p.getLastSelectedArea());
        int count = Math.min(2, tiles.size());
        
        if (count == 0) {
            JOptionPane.showMessageDialog(view, "No more tiles in that area!");
            return;
        }
        
        for (int i = 0; i < count; i++) {
            Tile taken = tiles.remove(0);
            p.addTiles(new ArrayList<>(java.util.Arrays.asList(taken)));
            view.addTileToHand(taken.getImagePath());
            board.removeTileFromArea(taken);
        }
        view.updatePlayerInfo(p.getName(), p.getColor(), p.calculateScore());
        JOptionPane.showMessageDialog(view, "Took " + count + " more tiles from same area!");
    }

    private void handleProfessor(Player p) {
        if (p.getLastSelectedArea() == -1) {
            JOptionPane.showMessageDialog(view, "You must select an area first in step 2!");
            return;
        }
        
        int taken = 0;
        for (int i = 0; i < 4; i++) {
            if (i != p.getLastSelectedArea()) {
                ArrayList<Tile> tiles = getAreaTiles(i);
                if (!tiles.isEmpty()) {
                    Tile t = tiles.remove(0);
                    p.addTiles(new ArrayList<>(java.util.Arrays.asList(t)));
                    view.addTileToHand(t.getImagePath());
                    board.removeTileFromArea(t);
                    taken++;
                }
            }
        }
        view.updatePlayerInfo(p.getName(), p.getColor(), p.calculateScore());
        JOptionPane.showMessageDialog(view, "Took 1 tile from " + taken + " other areas!");
    }

    private void handleTheCoder(Player p) {
        String[] options = {"Mosaics", "Statues", "Amphorae", "Skeletons"};
        int choice = JOptionPane.showOptionDialog(view, "Choose area for next turn:", "The Coder",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        
        if (choice >= 0) {
            p.setCoderSelectedArea(choice);
            JOptionPane.showMessageDialog(view, "Next turn you'll get 2 tiles from " + options[choice] + "!");
        }
    }

    private ArrayList<Tile> getAreaTiles(int areaIndex) {
        switch(areaIndex) {
            case 0: return board.getMosaicArea();
            case 1: return board.getStatueArea();
            case 2: return board.getAmphoraArea();
            case 3: return board.getSkeletonArea();
            default: return new ArrayList<>();
        }
    }

        

    public static void main(String[] args) {
        new Controller();
    }
}