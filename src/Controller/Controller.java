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

    private boolean isSoloMode; // Flag για το αν είναι solo παιχνίδι
    private Player thief;       // Ο εικονικός παίκτης "Κλέφτης"

    public Controller() {
        this.board = new Board();
        this.bag = new Bag(); 
        this.players = new ArrayList<>();
        
        // baze se kathe paiktj kai ena xrwma 
        // --- ΝΕΟΣ ΚΩΔΙΚΑΣ ΓΙΑ ΕΠΙΛΟΓΗ MODE ---
        String[] options = {"1 Player (Solo)", "4 Players"};
        int response = JOptionPane.showOptionDialog(null, "Select Game Mode:", "Amphipolis",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, options[0]);

        isSoloMode = (response == 0); // Αν διάλεξε το 0, είναι Solo

        if (isSoloMode) {
            // Δημιουργία παίκτη και κλέφτη
            players.add(new Player("Player 1", "Yellow"));
            thief = new Player("The Thief", "Black"); // Ο κλέφτης
            this.currentPlayerIndex = 0; // Παίζει πάντα ο παίκτης 1

            // Τοποθέτηση 8 πλακιδίων κατολίσθησης στην είσοδο (Κανόνας Solo)
            for (int i = 0; i < 8; i++) {
                LandslideTile lt = new LandslideTile(-1 - i, "resources/images/landslide.png");
                board.addTileToArea(lt);
            }
            JOptionPane.showMessageDialog(null, "Solo Mode Started! 8 Landslide tiles added. Watch out for the Thief!");
        } else {
            // Κλασικό παιχνίδι 4 παικτών
            players.add(new Player("Player 1", "Yellow"));
            players.add(new Player("Player 2", "Red"));
            players.add(new Player("Player 3", "Blue"));
            players.add(new Player("Player 4", "Black"));
            this.currentPlayerIndex = (int) (Math.random() * 4);
        }
        // -------------------------------------

        this.view = new GameWindow();

        if (isSoloMode) {
            // Τώρα που υπάρχει το view, εμφανίζουμε τα landslides που βάλαμε στο board πριν
            for (Tile t : board.getLandslideArea()) {
                view.addTileToBoard(t);
            }
        }

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

        //gia save/load
        view.getSaveItem().addActionListener(e -> saveGame());
        view.getLoadItem().addActionListener(e -> loadGame());
        
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
            int lastIndex = areaTiles.size() - 1;
            
            // Αφαιρούμε από το τέλος της λίστας
            Tile t = areaTiles.remove(lastIndex); 
            
            taken.add(t);
            view.addTileToHand(t.getImagePath());
            board.removeTileFromArea(t);
            
            // Ενημερώνουμε το View να σβήσει το τελευταίο graphic element
            view.removeTileFromBoard(t, lastIndex);
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
                    int lastIndex = tiles.size() - 1;
                    Tile t = tiles.remove(lastIndex);
                    
                    p.addTiles(new ArrayList<>(java.util.Arrays.asList(t)));
                    board.removeTileFromArea(t);
                    
                    view.addTileToHand(t.getImagePath());
                    view.removeTileFromBoard(t, lastIndex);
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
                view.addTileToBoard(t);
                JOptionPane.showMessageDialog(view, "You drew a landslide tile!");

                // --- ΕΛΕΓΧΟΣ SOLO MODE & ΚΛΕΦΤΗ ---
                if (isSoloMode) {
                    JOptionPane.showMessageDialog(view, "The Thief strikes! He takes all visible findings!");
                    handleThiefTurn(); // Καλούμε τη μέθοδο του κλέφτη
                    
                    // Έλεγχος αν γέμισε η είσοδος ΜΕΤΑ την κίνηση του κλέφτη
                    if (board.isEntranceFull()) {
                        handleGameOver(); // Ξεχωριστή μέθοδος για καθαρό κώδικα
                    } else {
                        // Αν δεν τελείωσε το παιχνίδι, η σειρά του παίκτη τελειώνει ΑΜΕΣΩΣ
                        p.setHasPlayed(true); // Θεωρούμε ότι έπαιξε για να μην κολλήσει
                        endTurn(true); // Force end turn
                    }
                    return; // Σταματάμε το loop, δεν τραβάει άλλα πλακίδια
                }
                // ----------------------------------

                if (board.isEntranceFull()) {
                     handleGameOver(); // (Θα φτιάξουμε αυτή τη βοηθητική κάτω)
                     return;
                }
            } else {
                findings.add(t);
                board.addTileToArea(t);
                view.addTileToBoard(t);
                view.addTileToHand(t.getImagePath());
            }
        }
        
        p.addTiles(findings);
        p.setHasPlayed(true);
        
        // to idio me prin
        view.updatePlayerInfo(p.getName(), p.getColor(), p.calculateScore());
    }
    private void handleGameOver() {
        turnTimer.stop();
        calculateStatuePoints();
        
        StringBuilder message = new StringBuilder("Game Over!\n\n");
        
        if (isSoloMode) {
            int playerScore = players.get(0).calculateScore();
            int thiefScore = thief.calculateScore();
            
            message.append("Player 1 Score: ").append(playerScore).append("\n");
            message.append("Thief Score: ").append(thiefScore).append("\n\n");
            
            if (playerScore > thiefScore) {
                message.append("VICTORY! You defeated the Thief!");
            } else {
                message.append("DEFEAT! The Thief has more points.");
            }
        } else {
            // Υπάρχουσα λογική για 4 παίκτες
            Player winner = players.get(0);
            for (Player p1 : players) {
                if (p1.calculateScore() > winner.calculateScore()) {
                    winner = p1;
                }
            }
            message.append("Final Scores:\n");
            for (Player p2 : players) {
                message.append(p2.getName()).append(": ").append(p2.calculateScore()).append(" points\n");
            }
            message.append("\nWinner: ").append(winner.getName()).append("!");
        }

        JOptionPane.showMessageDialog(view, message.toString());
        view.stopMusic();
        System.exit(0);
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
        boolean success = false;
        
        if (c instanceof Assistant) {
            success = handleAssistant(p);
        } else if (c instanceof Archaeologist) {
            success = handleArchaeologist(p);
        } else if (c instanceof Digger) {
            success = handleDigger(p);
        } else if (c instanceof Professor) {
            success = handleProfessor(p);
        } else if (c instanceof TheCoder) {
            success = handleTheCoder(p);
        }

        if (success) {
            c.setUsed(true);
            JOptionPane.showMessageDialog(view, "You used: " + c.getClass().getSimpleName() + "!");
            view.getCharacterButtons().get(index).setEnabled(false);
        }
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

        if (isSoloMode) {
            currentPlayerIndex = 0; // Μένει πάντα στον Player 1
        } else {
            currentPlayerIndex = (currentPlayerIndex + 1) % 4;
        }
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
    
    private boolean handleAssistant(Player p) {
        String[] options = {"Mosaics", "Statues", "Amphorae", "Skeletons"};
        int choice = JOptionPane.showOptionDialog(view, "Choose area:", "Assistant",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        
        if (choice >= 0) {
            ArrayList<Tile> tiles = getAreaTiles(choice);
            if (!tiles.isEmpty()) {
                int lastIndex = tiles.size() - 1;
                Tile taken = tiles.remove(lastIndex);
                
                ArrayList<Tile> temp = new ArrayList<>();
                temp.add(taken);
                p.addTiles(temp);
                view.addTileToHand(taken.getImagePath());
                board.removeTileFromArea(taken);
                
                // Αφαιρούμε από τα γραφικά το τελευταίο
                view.removeTileFromBoard(taken, lastIndex);
                
                return true;
            } else {
                 JOptionPane.showMessageDialog(view, "Area is empty!");
                 return false; // Άδεια περιοχή, δεν έγινε ενέργεια
            }
        }
        return false; // Ακυρώθηκε
    }

    private boolean handleArchaeologist(Player p) {
        if (p.getLastSelectedArea() == -1) {
            JOptionPane.showMessageDialog(view, "You must select an area first in step 2!");
            return false;
        }
        
        String[] options = {"Mosaics", "Statues", "Amphorae", "Skeletons"};
        int choice = JOptionPane.showOptionDialog(view, "Choose DIFFERENT area (not " + options[p.getLastSelectedArea()] + "):", 
                "Archaeologist", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        
        if (choice >= 0) {
            if (choice != p.getLastSelectedArea()) {
                ArrayList<Tile> tiles = getAreaTiles(choice);
                int count = Math.min(2, tiles.size());
                
                if (count == 0) {
                     JOptionPane.showMessageDialog(view, "Area is empty!");
                     return false;
                }

                for (int i = 0; i < count; i++) {
                    if (!tiles.isEmpty()) {
                        int lastIndex = tiles.size() - 1;
                        Tile taken = tiles.remove(lastIndex);
                        
                        p.addTiles(new ArrayList<>(java.util.Arrays.asList(taken)));
                        view.addTileToHand(taken.getImagePath());
                        board.removeTileFromArea(taken);
                        
                        view.removeTileFromBoard(taken, lastIndex);
                    }
                }
                view.updatePlayerInfo(p.getName(), p.getColor(), p.calculateScore());
                return true; // Πέτυχε
            } else {
                JOptionPane.showMessageDialog(view, "You must choose a DIFFERENT area!");
                return false; // Απέτυχε (διάλεξε την ίδια)
            }
        }
        return false; // Ακυρώθηκε
    }

    private boolean handleDigger(Player p) {
        if (p.getLastSelectedArea() == -1) {
            JOptionPane.showMessageDialog(view, "You must select an area first in step 2!");
            return false;
        }
        
        ArrayList<Tile> tiles = getAreaTiles(p.getLastSelectedArea());
        int count = Math.min(2, tiles.size());
        
        if (count == 0) {
            JOptionPane.showMessageDialog(view, "No more tiles in that area!");
            return false; // Απέτυχε
        }
        
        for (int i = 0; i < count; i++) {
            int lastIndex = tiles.size() - 1;
            Tile taken = tiles.remove(lastIndex);
            
            p.addTiles(new ArrayList<>(java.util.Arrays.asList(taken)));
            view.addTileToHand(taken.getImagePath());
            board.removeTileFromArea(taken);
            
            view.removeTileFromBoard(taken, lastIndex);
        }
        view.updatePlayerInfo(p.getName(), p.getColor(), p.calculateScore());
        JOptionPane.showMessageDialog(view, "Took " + count + " more tiles from same area!");
        return true; // Πέτυχε
    }

    private boolean handleProfessor(Player p) {
        if (p.getLastSelectedArea() == -1) {
            JOptionPane.showMessageDialog(view, "You must select an area first in step 2!");
            return false;
        }
        
        int taken = 0;
        for (int i = 0; i < 4; i++) {
            if (i != p.getLastSelectedArea()) {
                ArrayList<Tile> tiles = getAreaTiles(i);
                if (!tiles.isEmpty()) {
                    int lastIndex = tiles.size() - 1;
                    Tile t = tiles.remove(lastIndex);
                    
                    p.addTiles(new ArrayList<>(java.util.Arrays.asList(t)));
                    view.addTileToHand(t.getImagePath());
                    board.removeTileFromArea(t);
                    
                    view.removeTileFromBoard(t, lastIndex);
                    
                    taken++;
                }
            }
        }
        view.updatePlayerInfo(p.getName(), p.getColor(), p.calculateScore());
        JOptionPane.showMessageDialog(view, "Took 1 tile from " + taken + " other areas!");
        return true; // Πέτυχε (ακόμα και αν πήρε 0, η ενέργεια θεωρείται ότι έγινε)
    }

    private boolean handleTheCoder(Player p) {
        String[] options = {"Mosaics", "Statues", "Amphorae", "Skeletons"};
        int choice = JOptionPane.showOptionDialog(view, "Choose area for next turn:", "The Coder",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        
        if (choice >= 0) {
            p.setCoderSelectedArea(choice);
            JOptionPane.showMessageDialog(view, "Next turn you'll get 2 tiles from " + options[choice] + "!");
            return true;
        }
        return false;
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

    private void saveGame() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(view) == JFileChooser.APPROVE_OPTION) {
            try {
                java.io.File file = fileChooser.getSelectedFile();
                java.io.ObjectOutputStream out = new java.io.ObjectOutputStream(new java.io.FileOutputStream(file));
                
                // Αποθηκεύουμε τα βασικά αντικείμενα
                out.writeObject(board);
                out.writeObject(bag);
                out.writeObject(players);
                out.writeObject(currentPlayerIndex);
                
                out.close();
                JOptionPane.showMessageDialog(view, "Game Saved!");
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(view, "Error saving game: " + e.getMessage());
            }
        }
    }

    private void loadGame() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(view) == JFileChooser.APPROVE_OPTION) {
            try {
                java.io.File file = fileChooser.getSelectedFile();
                java.io.ObjectInputStream in = new java.io.ObjectInputStream(new java.io.FileInputStream(file));
                
                // Φορτώνουμε με την ΙΔΙΑ σειρά που τα σώσαμε
                this.board = (Board) in.readObject();
                this.bag = (Bag) in.readObject();
                this.players = (ArrayList<Player>) in.readObject();
                this.currentPlayerIndex = (Integer) in.readObject();
                in.close();
                
                // Ενημέρωση του GUI (το δύσκολο κομμάτι)
                refreshLoadedGame();
                JOptionPane.showMessageDialog(view, "Game Loaded!");
                
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(view, "Error loading game: " + e.getMessage());
            }
        }
    }

    private void refreshLoadedGame() {
        // 1. Σταματάμε τον παλιό timer
        if (turnTimer != null) turnTimer.stop();
        
        // 2. Καθαρίζουμε το View
        view.resetBoardVisuals();
        view.clearHandPanel();
        
        // 3. Ξανα-ζωγραφίζουμε το Board από τα δεδομένα που φορτώσαμε
        for (Tile t : board.getMosaicArea()) view.addTileToBoard(t);
        for (Tile t : board.getStatueArea()) view.addTileToBoard(t);
        for (Tile t : board.getAmphoraArea()) view.addTileToBoard(t);
        for (Tile t : board.getSkeletonArea()) view.addTileToBoard(t);
        for (Tile t : board.getLandslideArea()) view.addTileToBoard(t);
        
        // 4. Ενημερώνουμε τον τρέχοντα παίκτη
        Player p = players.get(currentPlayerIndex);
        view.updatePlayerInfo(p.getName(), p.getColor(), p.calculateScore());
        
        for (Tile t : p.getMyTiles()) {
            view.addTileToHand(t.getImagePath());
        }
        
        // 5. Επανεκκίνηση γύρου
        timeLeft = 30;
        view.updateTimer(timeLeft);
        turnTimer.start();
        view.playMusicForPlayer(currentPlayerIndex + 1);
        updateCharacterButtons(p);
    }


    // Νέα μέθοδος για τον Κλέφτη (Solo Mode)
    private void handleThiefTurn() {
        // Παίρνουμε όλα τα πλακίδια από τις 4 περιοχές
        ArrayList<Tile> stolenTiles = new ArrayList<>();
        
        // 1. Mosaics
        stolenTiles.addAll(board.getMosaicArea());
        for(Tile t : board.getMosaicArea()) view.removeTileFromBoard(t, 0); // Update View (θα χρειαστεί μια μικρή αλλαγή στο View, δες παρακάτω, ή χρήση της resetBoardVisuals αν θες πιο απλά)
        board.getMosaicArea().clear(); // Clear Model

        // 2. Statues
        stolenTiles.addAll(board.getStatueArea());
        for(Tile t : board.getStatueArea()) view.removeTileFromBoard(t, 1);
        board.getStatueArea().clear();

        // 3. Amphorae
        stolenTiles.addAll(board.getAmphoraArea());
        for(Tile t : board.getAmphoraArea()) view.removeTileFromBoard(t, 2);
        board.getAmphoraArea().clear();

        // 4. Skeletons
        stolenTiles.addAll(board.getSkeletonArea());
        for(Tile t : board.getSkeletonArea()) view.removeTileFromBoard(t, 3);
        board.getSkeletonArea().clear();

        // Ο κλέφτης τα αποθηκεύει
        thief.addTiles(stolenTiles);
        
        // Επειδή η removeTileFromBoard στο View που έχεις είναι με index, 
        // ένας πιο εύκολος τρόπος να καθαρίσεις τα γραφικά χωρίς errors είναι:
        view.resetBoardVisuals(); 
        // Και να ξαναζωγραφίσεις μόνο τα landslides που έμειναν (αφού τα άλλα τα πήρε ο κλέφτης)
        for (Tile t : board.getLandslideArea()) view.addTileToBoard(t);
        
        System.out.println("Thief stole " + stolenTiles.size() + " tiles. Thief Score: " + thief.calculateScore());
    }

        

    public static void main(String[] args) {
        new Controller();
    }
}