package Controller;

import Model.*;
import View.GameWindow;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * h klash contriller diaxeirizetai th roh tou paixnidioy , tin epikoinonia
 * metaksi moddel kai view
 * ylopoiei toys kanones gia 4 paiktes kai solo mode
 */
public class Controller {

    private Board board;
    private Bag bag;
    private ArrayList<Player> players;
    private int currentPlayerIndex;
    private GameWindow view;
    
    // gia to timer tis seiras tou kathe paikth 
    private Timer turnTimer;
    private int timeLeft;

    private boolean isSoloMode; // flag gia to an einai solo to paixnidi
    private Player thief;       // Ο εικονικός παίκτης "Κλέφτης"


    /**
     * constructor tou Controller.
     * arxikopoiei to paixnidi, to tablo, ti sakoula kai tous paiktes.
     * zitaei apo ton xristi na epileksei Mode (Solo i 4 Players).
     * @post Exei dimiourgithei to parathyro, exoun moirastei oi kartes kai exei oristei o prwtos paiktis.
     */

    public Controller() {
        this.board = new Board();
        this.bag = new Bag(); 
        this.players = new ArrayList<>();
        
        // baze se kathe paiktj kai ena xrwma 
        // gia tin epiulogi mode neo parathiro eite solo eite 4 players
        String[] options = {"1 Player (Solo)", "4 Players"};
        int response = JOptionPane.showOptionDialog(null, "Select Game Mode:", "Amphipolis",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, options[0]);

        isSoloMode = (response == 0); // an einai to 0 tote einai solo mode

        if (isSoloMode) {
            // dimiourgia paikth kai klefti 
            players.add(new Player("Player 1", "Yellow"));
            thief = new Player("The Thief", "Black"); //o kleftis
            this.currentPlayerIndex = 0; // paisei mono o paikths 1

            // topothetisi 8 plakidion katolisthisis stin eisodo (Kanonas Solo)
            for (int i = 0; i < 8; i++) {
                LandslideTile lt = new LandslideTile(-1 - i, "resources/images/landslide.png");
                board.addTileToArea(lt);
            }
            JOptionPane.showMessageDialog(null, "Solo Mode Started! 8 Landslide tiles added. Watch out for the Thief!");
        } else {
            // klasiko paixnidi 4 paikton
            players.add(new Player("Player 1", "Yellow"));
            players.add(new Player("Player 2", "Red"));
            players.add(new Player("Player 3", "Blue"));
            players.add(new Player("Player 4", "Black"));
            this.currentPlayerIndex = (int) (Math.random() * 4);
        }

        this.view = new GameWindow();

        if (isSoloMode) {
            // twra pou yparxei to view, emfanizoume ta landslides pou valame sto board prin
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

    /**
     * arxikopoiei tous akroathes gia ta koumpia tou parathyrou paixnidiou
     */
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

    /**
     * diaxeirizetai tin epilogi plakidiwn apo tis perioxes tou tablo (Mosaika, Agalmata klp)
     * @param areaIndex o deiktis tis perioxis (0: Mosaics, 1: Statues, 2: Amphorae, 3: Skeletons)
     * @pre o paiktis prepei na exei traviksei prwta apo ti sakoula (hasPlayed = true)
     * @pre o paiktis den prepei na exei ksanaparei apo to tablo ston idio gyro (ektos an exei eidiki ikanotita)
     * @post afairountai mexri 2 plakidia apo tin perioxi kai prostithentai sto xeri tou paikti
     * @post enimerwnetai to skor kai ta grafika
     */

    private void selectFromArea(int areaIndex) {
        Player p = players.get(currentPlayerIndex);
    
        //elgxos 1: prepei na exei traviksei apo ti sakoula
        if (!p.hasPlayed()) {
            JOptionPane.showMessageDialog(view, "You must draw tiles from the bag first (Step 1)!");
            return;
        }

        // elgxos 2: prepei na MIN exei ksanaparei apo to tablo se afton ton gyro
        if (p.hasPickedFromBoard()) {
            JOptionPane.showMessageDialog(view, "You have already picked tiles from a sorting area in this turn!");
            return;
        }
        
        ArrayList<Tile> areaTiles = getAreaTiles(areaIndex);
        
        if (areaTiles.isEmpty()) {
            JOptionPane.showMessageDialog(view, "This area is empty!");
            return;
        }
        
        // pairnei mexri 2 tiles
        int count = Math.min(2, areaTiles.size());
        ArrayList<Tile> taken = new ArrayList<>();
        
        for (int i = 0; i < count; i++) {
            int lastIndex = areaTiles.size() - 1;
            
            // afairoume apo to telos tis listas
            Tile t = areaTiles.remove(lastIndex); 
            
            taken.add(t);
            view.addTileToHand(t.getImagePath());
            board.removeTileFromArea(t);
            
            // enimeronoume to View na svisei to teleutaio graphic element
            view.removeTileFromBoard(t, lastIndex);
        }
        
        p.addTiles(taken);
        p.setLastSelectedArea(areaIndex);
        p.setHasSelectedFromArea(true); // Markarei oti exei parei
        view.updatePlayerInfo(p.getName(), p.getColor(), p.calculateScore());
        p.setHasPickedFromBoard(true); // "kleidonei" oti pire apo to tablo
        p.setLastSelectedArea(areaIndex); // krataei tin perioxi gia tous xaraktires
        view.updatePlayerInfo(p.getName(), p.getColor(), p.calculateScore());
        
        JOptionPane.showMessageDialog(view, "Took " + count + " tiles. You cannot select from areas again this turn!");
    }


    /**
     * proetoimazei ton gyro gia ton trexonta paikti
     * @post epanaferei tis simaies (flags) tou paikti (hasPlayed, hasPickedFromBoard)
     * @post ksekinaei ton xronodiakopti (Timer)
     * @post energopoiei/Apenergopoiei ta koumpia twn xaraktirwn analoga me ti diathesimotita tous
     */
    private void startTurn() {
        Player p = players.get(currentPlayerIndex);
        p.setHasPlayed(false);

        p.setHasPickedFromBoard(false); // reset oti den exei parei akoma apo to tablo
        p.setLastSelectedArea(-1); // reset tin perioxi pou dialekse, oste na doulepsoun swsta oi xaraktires
        p.setHasSelectedFromArea(false); // reset gia neo gyro
        p.setLastSelectedArea(-1); // reset

        // elegxos an eixe paiksei TheCoder proigoumena
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
            p.setCoderSelectedArea(-1); // reset
            JOptionPane.showMessageDialog(view, "TheCoder effect: received " + count + " tiles!");
        }
        
        // reset timer
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

    /**
     * enimerwnei ton xronodiakopti kathe deuterolepto
     * an o xronos teleiwsei, termatizei ton gyro tou paikti
     */
    private void updateTimer() {
        timeLeft--;
        view.updateTimer(timeLeft);
        
        if (timeLeft <= 0) {
            turnTimer.stop();
            JOptionPane.showMessageDialog(view, "Time ended. Next player");
            endTurn(true); 
        }
    }


    /**
     * travaei 4 plakidia apo ti sakoula gia ton trexonta paikti
     * @pre h sakoula na min einai adeia
     * @pre o paiktis na min exei traviksei idi se ayton ton gyro
     * @post ta FindingTiles prostithentai sto tablo kai sto xeri tou paikti (proswrina)
     * @post ta LandslideTiles topothetountai stin eisodo
     * @post an einai Solo Mode kai travixthei LandslideTile, energopoieitai o Kleftis
     */
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

                // elegxos gia to Solo Mode kai energopoiisi tou Klefti
                if (isSoloMode) {
                    JOptionPane.showMessageDialog(view, "The Thief strikes! He takes all visible findings!");
                    handleThiefTurn(); // kaloume ti methodo tou klefti
                    
                    // elegxos an gemise i eisodos META tin kinisi tou klefti
                    if (board.isEntranceFull()) {
                        handleGameOver(); // ksexoristi methodos gia katharo kwdika
                    } else {
                        // an den teleiwse to paixnidi, i seira tou paikti teleiwnei AMESWS
                        p.setHasPlayed(true); // yhewroume oti epaixe gia na min kollisei
                        endTurn(true); // force end turn
                    }
                    return; // Stamatame to loop, den travaei alla plakidia
                }
            

                if (board.isEntranceFull()) {
                     handleGameOver(); // tha ftiaksoume afti ti voithitiki kato
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




    /**
     * diaxeirizetai ton gyro tou Klefti sto Solo Mode
     * o Kleftis paragei ena skore analogo me ta plakidia pou exoun ston pinaka oi paiktes
     * @post To skor tou Klefti enimerwnetai
     */
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
            // yparxousa logiki gia 4 paiktes
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

    

    /**
     * ektelei tin eidiki ikanotita enos xaraktira
     * @param index o deiktis tou xaraktira sti lista tou paikti
     * @pre o xaraktiras na min exei xrisimopoiithei ksana sto paixnidi (isUsed == false)
     * @pre o paiktis na exei traviksei prwta plakidia apo ti sakoula
     * @post h ikanotita ekteleitai kai o xaraktiras markaretai ws xrisimopoiimenos
     */
    private void useCharacter(int index) {
        Player p = players.get(currentPlayerIndex);
        Model.Character c = p.getMyCharacters().get(index);

        if (!p.hasPlayed()) { // elegxos an travikse apo sakoula
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



    /**
     * enimerwnei ta koumpia twn xaraktirwn analoga me ti diathesimotita tous
     * @param p O trexontas paikths
     */
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


    /**
     * termatizei ton gyro tou trexontos paikti kai pernaei ston epomeno
     * @param force an true, termatizei ton gyro anagkastika (px logo xronou i Klefti)
     * @pre o paiktis prepei na exei oloklirwsei tis ypoxrewtikes kiniseis (ektos an force=true)
     * @post o deiktis currentPlayerIndex enimerwnetai gia ton epomeno paikti
     */

    public void endTurn() {
        endTurn(false);
    }



    /**
     * termatizei ton gyro tou trexontos paikti kai pernaei ston epomeno
     * @param force an true, termatizei ton gyro anagkastika (px logo xronou i Klefti)
     * @pre o paiktis prepei na exei oloklirwsei tis ypoxrewtikes kiniseis (ektos an force=true)
     * @post o deiktis currentPlayerIndex enimerwnetai gia ton epomeno paikti
     */
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





    /**
     * ulopoiiei tin ikanotita tou Professor
     * o paikths paragei 3 plakidia apo mia apo tis 4 perioxes
     * @pre o paiktis prepei na exei traviksei prwta apo ti sakoula
     * @post o paikths parei 3 plakidia apo mia epiloghmeni perioxi
     */
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





    /**
     * ulopoiiei tin ikanotita tou Assistant
     * o paikths paragei ena plakidio apo mia apo tis 4 perioxes
     * @pre o paiktis prepei na exei traviksei prwta apo ti sakoula
     * @post o paikths parei 1 plakidio apo mia epiloghmeni perioxi
     */
    
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
                
                // afairoume to teleutaio graphic element apo to view
                view.removeTileFromBoard(taken, lastIndex);
                
                return true;
            } else {
                JOptionPane.showMessageDialog(view, "Area is empty!");
                return false; //adeia perioxh ara apetyxe
            }
        }
        return false; // akurwthhke
    }




    /**
     * ylopoiiei tin ikanotita tou Archaeologist
     * o paikths paragei 2 plakidia apo mia diaforetiki apo tis 4 perioxes
     * @pre o paiktis prepei na exei traviksei prwta apo ti sakoula
     * @post o paikths parei 2 plakidia apo mia diaforetiki perioxi
     */

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
                return true; // petyxhke
            } else {
                JOptionPane.showMessageDialog(view, "You must choose a DIFFERENT area!");
                return false; // apetyxe (dialeje thn idia)
            }
        }
        return false; // akurwthhke
    }





    /**
     * ylopoiiei tin ikanotita tou Archaeologist
     * o paikths paragei 2 plakidia apo mia diaforetiki apo tis 4 perioxes
     * @pre o paiktis prepei na exei traviksei prwta apo ti sakoula
     * @post o paikths parei 2 plakidia apo mia diaforetiki perioxi
     */
    private boolean handleDigger(Player p) {
        if (p.getLastSelectedArea() == -1) {
            JOptionPane.showMessageDialog(view, "You must select an area first in step 2!");
            return false;
        }
        
        ArrayList<Tile> tiles = getAreaTiles(p.getLastSelectedArea());
        int count = Math.min(2, tiles.size());
        
        if (count == 0) {
            JOptionPane.showMessageDialog(view, "No more tiles in that area!");
            return false; // apetyxe
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
        return true; // petyxhke
    }




    /**
     * ylopoiiei tin ikanotita tou Professor
     * o paikths paragei 3 plakidia apo mia apo tis 4 perioxes
     * @pre o paiktis prepei na exei traviksei prwta apo ti sakoula
     * @post o paikths parei 3 plakidia apo mia epiloghmeni perioxi
     */
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
        return true; // petyxhke (akoma kai an pire 0, h energeia thewreitai oti egine)
    }




    /**
     * ylopoiiei tin ikanotita tou TheCoder
     * o paikths epilegei mia apo tis 4 perioxes gia na parei 2 plakidia ston epomeno gyro
     * @pre o paiktis prepei na exei traviksei prwta apo ti sakoula
     * @post o paikths epilegei mia perioxi gia na parei 2 plakidia ston epomeno gyro
     */
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





    /**
     * epistrefei ta plakidia mias apo tis 4 perioxes tou tablo
     * @param areaIndex o deiktis tis perioxis (0: Mosaics, 1: Statues, 2: Amphorae, 3: Skeletons)
     * @return h lista me ta plakidia tis epilegmenis perioxis
     */
    private ArrayList<Tile> getAreaTiles(int areaIndex) {
        switch(areaIndex) {
            case 0: return board.getMosaicArea();
            case 1: return board.getStatueArea();
            case 2: return board.getAmphoraArea();
            case 3: return board.getSkeletonArea();
            default: return new ArrayList<>();
        }
    }



    /**
     * apothikevei tin trexousa katastasi tou paixnidiou se arxeio
     * @post ta antikeimena Board, Bag, Players kai currentPlayerIndex ginontai serialize
     */
    private void saveGame() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(view) == JFileChooser.APPROVE_OPTION) {
            try {
                java.io.File file = fileChooser.getSelectedFile();
                java.io.ObjectOutputStream out = new java.io.ObjectOutputStream(new java.io.FileOutputStream(file));
                
                // apothikevoume ta vasika antikeimena
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


    /**
     * fortwnei mia apothikevmeni katastasi paixnidiou
     * @post to paixnidi epanerxetai akrivws stin katastasi pou eixe apothikeftei
     * @post to GUI ananewnetai plirws vasi twn fortwmenwn dedomenwn
     */

    private void loadGame() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(view) == JFileChooser.APPROVE_OPTION) {
            try {
                java.io.File file = fileChooser.getSelectedFile();
                java.io.ObjectInputStream in = new java.io.ObjectInputStream(new java.io.FileInputStream(file));
                
                // fortwnoume me tin IDIA seira pou ta swsame
                this.board = (Board) in.readObject();
                this.bag = (Bag) in.readObject();
                this.players = (ArrayList<Player>) in.readObject();
                this.currentPlayerIndex = (Integer) in.readObject();
                in.close();
                
                // Enimerwsi tou GUI 
                refreshLoadedGame();
                JOptionPane.showMessageDialog(view, "Game Loaded!");
                
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(view, "Error loading game: " + e.getMessage());
            }
        }
    }



    /**
     * ananewnei to GUI me ta dedomena pou fortothikan apo arxeio
     * @pre ta dedomena tou Board kai twn Players exoun fortothei swsta
     * @post to GUI anakatevthike plirws gia na antapokrinetai sta fortwmena dedomena
     */
    private void refreshLoadedGame() {
        // stamatame ton palio timer
        if (turnTimer != null) turnTimer.stop();
        
        // katharizoume ola ta grafika tou tablo kai tou xeri tou paikti
        view.resetBoardVisuals();
        view.clearHandPanel();
        
        //  ksanazografizoume to Board apo ta dedomena pou fortwsame
        for (Tile t : board.getMosaicArea()) view.addTileToBoard(t);
        for (Tile t : board.getStatueArea()) view.addTileToBoard(t);
        for (Tile t : board.getAmphoraArea()) view.addTileToBoard(t);
        for (Tile t : board.getSkeletonArea()) view.addTileToBoard(t);
        for (Tile t : board.getLandslideArea()) view.addTileToBoard(t);
        
        // enimerwnoume ton trexonta paikti
        Player p = players.get(currentPlayerIndex);
        view.updatePlayerInfo(p.getName(), p.getColor(), p.calculateScore());
        
        for (Tile t : p.getMyTiles()) {
            view.addTileToHand(t.getImagePath());
        }
        
        // restart ton round
        timeLeft = 30;
        view.updateTimer(timeLeft);
        turnTimer.start();
        view.playMusicForPlayer(currentPlayerIndex + 1);
        updateCharacterButtons(p);
    }


    /**
     * diaxeirizetai ti seira tou "Klefti" sto Solo Mode
     * kaleitai otan o paiktis traviksei Landslide Tile
     * @pre na einai energo to Solo Mode
     * @post ola ta plakidia evrimatwn afairountai apo to tablo
     * @post ta plakidia prostithentai sto skor tou Klefti
     * @post ta grafika tou tablo katharizoun
     */
    // gia solo mode
    private void handleThiefTurn() {
        // pairenoume ta plakidia apo ola ta areas
        ArrayList<Tile> stolenTiles = new ArrayList<>();
        
        // mosaics
        stolenTiles.addAll(board.getMosaicArea());
        for(Tile t : board.getMosaicArea()) view.removeTileFromBoard(t, 0); // Update View (θα χρειαστεί μια μικρή αλλαγή στο View, δες παρακάτω, ή χρήση της resetBoardVisuals αν θες πιο απλά)
        board.getMosaicArea().clear(); // Clear Model

        // statues
        stolenTiles.addAll(board.getStatueArea());
        for(Tile t : board.getStatueArea()) view.removeTileFromBoard(t, 1);
        board.getStatueArea().clear();

        // amphora
        stolenTiles.addAll(board.getAmphoraArea());
        for(Tile t : board.getAmphoraArea()) view.removeTileFromBoard(t, 2);
        board.getAmphoraArea().clear();

        // skeletons
        stolenTiles.addAll(board.getSkeletonArea());
        for(Tile t : board.getSkeletonArea()) view.removeTileFromBoard(t, 3);
        board.getSkeletonArea().clear();

        // o kleftis ta apothikevei
        thief.addTiles(stolenTiles);
        
        // epeidh i removeTileFromBoard sto View pou eheis einai me index, 
        //katharizoume ta grafika choris errors me
        view.resetBoardVisuals(); 
        // kai na ksanazografizoume mono ta landslides pou emeinan (afou ta alla ta pire o kleftis)
        for (Tile t : board.getLandslideArea()) view.addTileToBoard(t);
        
        System.out.println("Thief stole " + stolenTiles.size() + " tiles. Thief Score: " + thief.calculateScore());
    }

        

    public static void main(String[] args) {
        new Controller();
    }
}