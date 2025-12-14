package View;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import Model.AmphoraTile;
import Model.LandslideTile;
import Model.MosaicTile;
import Model.SkeletonTile;
import Model.StatueTile;
import Model.Tile;

import java.awt.*;
import java.net.URL;
import java.util.ArrayList;

public class GameWindow extends JFrame {

    private JLayeredPane layeredPane;
    private JLabel backgroundLabel;

    // ola poy xreiazontai gia ti deksia meria tou panel
    //siga siga briskw olo kai perissotera poy xreiazontai
    private JPanel rightPanel;
    private JLabel playerNameLabel;
    private JPanel playerColorPanel;
    private JLabel scoreLabel;
    private JLabel timerLabel;
    private JPanel charactersPanel;
    private ArrayList<JButton> characterButtons;
    private JButton drawButton;
    private JButton endTurnButton;
    private JPanel playerHandPanel;
    private JButton mosaicAreaButton;
    private JButton statueAreaButton;
    private JButton amphoraAreaButton;
    private JButton skeletonAreaButton;
    private JPanel landslideGridPanel;
    private ArrayList<JLabel> mosaicLabels;
    private ArrayList<JLabel> statueLabels;
    private ArrayList<JLabel> amphoraLabels;
    private ArrayList<JLabel> skeletonLabels;
    private ArrayList<JLabel> landslideLabels;
    private javax.sound.sampled.Clip musicClip;
    

    public GameWindow() {
        // scediash parathyrou
        setTitle("Amphipolis");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1225, 858); 
        setLayout(null); 
        setResizable(false);

        // to ebala gia error check na dw gia ta images sxetika me to directoru
        System.out.println("Working Directory: " + System.getProperty("user.dir"));

        // layered pane gia to window gia na exei eikones mia panw sthn allh opou xreiazetai 
        layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, 800, 825); 
        add(layeredPane);
        mosaicLabels = new ArrayList<>();
        statueLabels = new ArrayList<>();
        amphoraLabels = new ArrayList<>();
        skeletonLabels = new ArrayList<>();
        landslideLabels = new ArrayList<>();
        createBoardAreas();

        // kanw load to background apo ta dosmena images
        ImageIcon boardIcon = loadImage("resources/images/background.png");
        if (boardIcon != null) {
            Image image = boardIcon.getImage().getScaledInstance(800, 820, Image.SCALE_SMOOTH);
            backgroundLabel = new JLabel(new ImageIcon(image));
            backgroundLabel.setBounds(0, 0, 800, 820);
            layeredPane.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER);
        } else {
            JPanel fallback = new JPanel();
            fallback.setBackground(new Color(240, 240, 240)); 
            fallback.setBounds(0, 0, 800, 850);
            layeredPane.add(fallback, JLayeredPane.DEFAULT_LAYER);
        }

        // h deksia meria tou parathiroy poy tha exo character player name ktl ktl
        rightPanel = new JPanel();
        rightPanel.setBounds(800, 0, 400,821);
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS)); 
        rightPanel.setBorder(new EmptyBorder(10, 15, 10, 15)); 

        initRightPanelContent();

        add(rightPanel); 

        setLocationRelativeTo(null); 
        setVisible(true);
    }

    private void initRightPanelContent() {


        // paikths
        JPanel playerInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        playerInfoPanel.setBackground(Color.WHITE);
        playerInfoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        playerNameLabel = new JLabel("Player 1");
        playerNameLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        playerNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        playerColorPanel = new JPanel();
        playerColorPanel.setPreferredSize(new Dimension(20, 20));
        playerColorPanel.setBackground(Color.YELLOW); 
        playerColorPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        playerInfoPanel.add(playerNameLabel);
        playerInfoPanel.add(playerColorPanel);
        rightPanel.add(playerInfoPanel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // xronos kai score
        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setFont(new Font("SansSerif", Font.BOLD, 16));  
        scoreLabel.setForeground(Color.BLACK);  // mayro xroma
        rightPanel.add(scoreLabel);

        timerLabel = new JLabel("Time left: 30s");
        timerLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        timerLabel.setForeground(Color.BLACK);  // san dynamic timer otan ginei <5sec to kanei kokkino
        rightPanel.add(timerLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // zarakthres - h perioxh tous - pali dokimaza poia coordinates to kanoun na fainetai oreo
        addHeader("Use character");
        charactersPanel = new JPanel(new GridLayout(2, 3, 10, 5));
        charactersPanel.setBackground(Color.WHITE);
        charactersPanel.setMaximumSize(new Dimension(470, 250)); 
        charactersPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        characterButtons = new ArrayList<>();

        String[] charNames = {"assistant", "archaeologist", "digger", "professor", "coder"};
        for (String name : charNames) {
            JButton btn = new JButton();
            btn.setPreferredSize(new Dimension(130, 176));
            
            ImageIcon icon = loadImage("resources/images/" + name + ".png");
            
            if (icon != null) {
                Image img = icon.getImage().getScaledInstance(140, 180, Image.SCALE_SMOOTH);
                btn.setIcon(new ImageIcon(img));
            } else {
                btn.setText(name.substring(0, 3).toUpperCase());
                btn.setBackground(Color.LIGHT_GRAY);
            }
            btn.setFocusPainted(false);
            characterButtons.add(btn);
            charactersPanel.add(btn);
        }
        rightPanel.add(charactersPanel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 20)));


        
        JPanel actionsPanel = new JPanel();
        actionsPanel.setLayout(new BoxLayout(actionsPanel, BoxLayout.Y_AXIS));
        actionsPanel.setBackground(Color.WHITE);
        actionsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        drawButton = new JButton("Draw tiles");
        drawButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        drawButton.setBackground(new Color(173, 216, 230)); 
        drawButton.setMaximumSize(new Dimension(370, 40));
        drawButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        endTurnButton = new JButton("End turn");
        endTurnButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        endTurnButton.setBackground(new Color(173, 216, 230)); 
        endTurnButton.setMaximumSize(new Dimension(370, 40));
        endTurnButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        actionsPanel.add(drawButton);
        actionsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        actionsPanel.add(endTurnButton);
        
        rightPanel.add(actionsPanel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // ekei poy deixnw ta plakidia pou exei kathe paikths otan einai h seira tou 
        addHeader("My tiles");
        playerHandPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        playerHandPanel.setBackground(new Color(250, 250, 250));
        playerHandPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        // pali ekana dokimes na dw ti tairiasei sthn eikona/window poy ebala
        int tileWidth = 50;
        int gap = 5;
        int tilesPerRow = 6;
        int panelWidth = (tileWidth + gap) * tilesPerRow + 10;

        playerHandPanel.setPreferredSize(new Dimension(panelWidth, 400));
        playerHandPanel.setMaximumSize(new Dimension(370, 180));
        playerHandPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JScrollPane scrollPane = new JScrollPane(playerHandPanel);
        scrollPane.setMaximumSize(new Dimension(370, 180));
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        rightPanel.add(scrollPane);
    }

    private void createBoardAreas() {
        // moasic area
        mosaicAreaButton = new JButton();
        mosaicAreaButton.setBounds(112, 229, 57, 57);
        mosaicAreaButton.setBackground(new Color(144, 238, 144, 120)); 
        mosaicAreaButton.setFocusPainted(false);
        mosaicAreaButton.setFont(new Font("SansSerif", Font.BOLD, 10));
        layeredPane.add(mosaicAreaButton, Integer.valueOf(2));  
        mosaicAreaButton.setOpaque(false);

        //srtatue area
        statueAreaButton = new JButton();
        statueAreaButton.setBounds(594, 229, 132, 60);
        statueAreaButton.setBackground(new Color(173, 216, 230, 120)); 
        statueAreaButton.setFocusPainted(false);
        statueAreaButton.setFont(new Font("SansSerif", Font.BOLD, 10));
        layeredPane.add(statueAreaButton, Integer.valueOf(2));  
        statueAreaButton.setOpaque(false);

        // area gia tous amforeis 
        amphoraAreaButton = new JButton();
        amphoraAreaButton.setBounds(113, 530, 57, 60);
        amphoraAreaButton.setBackground(new Color(255, 182, 193, 120)); 
        amphoraAreaButton.setFocusPainted(false);
        amphoraAreaButton.setFont(new Font("SansSerif", Font.BOLD, 10));
        layeredPane.add(amphoraAreaButton, Integer.valueOf(2)); 
        amphoraAreaButton.setOpaque(false);

        // skeleton area
        skeletonAreaButton = new JButton();
        skeletonAreaButton.setBounds(632, 528, 60, 62);
        skeletonAreaButton.setBackground(new Color(255, 228, 181, 120)); 
        skeletonAreaButton.setFocusPainted(false);
        skeletonAreaButton.setFont(new Font("SansSerif", Font.BOLD, 10));
        layeredPane.add(skeletonAreaButton, Integer.valueOf(2));  
        skeletonAreaButton.setOpaque(false);

        //landslide area panel 
        landslideGridPanel = new JPanel(new GridLayout(4, 4, 0, 0));
        landslideGridPanel.setBounds(290, 366, 220, 220);
        landslideGridPanel.setOpaque(false);
        landslideGridPanel.setBorder(null);
        
        // 16 theseis gia ta landslide tiles
        for (int i = 0; i < 16; i++) {
            JLabel slot = new JLabel();
            slot.setBorder(null); 
            slot.setOpaque(false);
            slot.setHorizontalAlignment(SwingConstants.CENTER);
            landslideLabels.add(slot);
            landslideGridPanel.add(slot);
        }
        
        layeredPane.add(landslideGridPanel, Integer.valueOf(2));  // ΕΔΩ ΑΛΛΑΞΕ
        
        // apenergopoihsh tooltip. mou ebgaze paraksena ta koumpia gia auto to ekana etsi 
        mosaicAreaButton.setToolTipText(null);
        statueAreaButton.setToolTipText(null);
        amphoraAreaButton.setToolTipText(null);
        skeletonAreaButton.setToolTipText(null);
    }

    private void addHeader(String text) {
        JLabel header = new JLabel(text);
        header.setFont(new Font("SansSerif", Font.BOLD, 14));
        header.setForeground(Color.BLACK);
        header.setAlignmentX(Component.LEFT_ALIGNMENT);
        rightPanel.add(header);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 5))); 
    }

    // upadters gia kathe pragma (timer - player info)

    public void updatePlayerInfo(String name, String colorHex, int score) {
        playerNameLabel.setText(name);
        try {
            Color c = Color.GRAY;
            if (colorHex.equalsIgnoreCase("Yellow")) c = Color.YELLOW;
            else if (colorHex.equalsIgnoreCase("Red")) c = Color.RED;
            else if (colorHex.equalsIgnoreCase("Blue")) c = Color.BLUE;
            else if (colorHex.equalsIgnoreCase("Black")) c = Color.BLACK;
            playerColorPanel.setBackground(c);
        } catch (Exception e) {
            playerColorPanel.setBackground(Color.GRAY);
        }
        scoreLabel.setText("Score: " + score);
    }

    public void updateTimer(int seconds) {
        timerLabel.setText("Time left: " + seconds + "s");
        if (seconds <= 5) timerLabel.setForeground(Color.RED);
        else timerLabel.setForeground(new Color(0, 100, 0)); 
    }

    public void addTileToHand(String imagePath) {
        // kanw thn synarthsh poy einai ligo pio katw(eksigo ekei giati)
        ImageIcon icon = loadImage(imagePath);
        
        if (icon != null) {
            Image img = icon.getImage().getScaledInstance(43, 43, Image.SCALE_SMOOTH);
            JLabel tileLabel = new JLabel(new ImageIcon(img));
            tileLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            playerHandPanel.add(tileLabel);
            playerHandPanel.revalidate();
            playerHandPanel.repaint();
        } else {
            /// error catch an de brei tin eikona
            JLabel errorLabel = new JLabel("IMG?");
            errorLabel.setBorder(BorderFactory.createLineBorder(Color.RED));
            errorLabel.setPreferredSize(new Dimension(43, 43));
            playerHandPanel.add(errorLabel);
        }
    }

    public void clearHandPanel() {
        playerHandPanel.removeAll();
        playerHandPanel.revalidate();
        playerHandPanel.repaint();
    }

    public void addTileToBoard(Tile tile) {
        ImageIcon icon = loadImage(tile.getImagePath());
        if (icon == null) return;
        
        Image img = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        JLabel tileLabel = new JLabel(new ImageIcon(img));
        tileLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        
        if (tile instanceof MosaicTile) {
            mosaicLabels.add(tileLabel);
            addTileToAreaVisual(mosaicAreaButton, tileLabel, mosaicLabels.size() - 1);
        } else if (tile instanceof StatueTile) {
            statueLabels.add(tileLabel);
            addTileToAreaVisual(statueAreaButton, tileLabel, statueLabels.size() - 1);
        } else if (tile instanceof AmphoraTile) {
            amphoraLabels.add(tileLabel);
            addTileToAreaVisual(amphoraAreaButton, tileLabel, amphoraLabels.size() - 1);
        } else if (tile instanceof SkeletonTile) {
            skeletonLabels.add(tileLabel);
            addTileToAreaVisual(skeletonAreaButton, tileLabel, skeletonLabels.size() - 1);
        } else if (tile instanceof LandslideTile) {
            Image imgL = icon.getImage().getScaledInstance(55, 55, Image.SCALE_SMOOTH);
            int index = landslideLabels.size() - 16; // to proto keno(synhthos apla to epomeno)
            for (int i = 0; i < landslideLabels.size(); i++) {
                if (landslideLabels.get(i).getIcon() == null) {
                    landslideLabels.get(i).setIcon(new ImageIcon(imgL));
                    break;
                }
            }
        }
    }

    private void addTileToAreaVisual(JButton areaButton, JLabel tileLabel, int index) {
        // meta apo polla tweaks bash to poy einai ta antistoixa koumpia
        int x, y;
    
        if (areaButton == mosaicAreaButton) {
            x = areaButton.getX() - 80 + (index % 6) * 35;
            y = areaButton.getY() - 200  + (index / 6) * 35;
        } 
        else if (areaButton == statueAreaButton) {
            x = areaButton.getX() - 41 + (index % 6) * 35;
            y = areaButton.getY() - 200  + (index / 6) * 35;
        } 
        else if (areaButton == amphoraAreaButton) {
            x = areaButton.getX() - 80  + (index % 6) * 35;
            y = areaButton.getY() + 61 + (index / 6) * 35;
        } 
        else { // skeletonAreaButton
            x = areaButton.getX() - 80 + (index % 6) * 35;
            y = areaButton.getY() + 63 + (index / 6) * 35;
        }
        tileLabel.setBounds(x, y, 40, 40);
        layeredPane.add(tileLabel, Integer.valueOf(2));
        layeredPane.revalidate();
        layeredPane.repaint();
    }

    public void removeTileFromBoard(Tile tile, int areaIndex) {
        ArrayList<JLabel> targetList = null;
        
        if (tile instanceof MosaicTile) targetList = mosaicLabels;
        else if (tile instanceof StatueTile) targetList = statueLabels;
        else if (tile instanceof AmphoraTile) targetList = amphoraLabels;
        else if (tile instanceof SkeletonTile) targetList = skeletonLabels;
        
        if (targetList != null && areaIndex < targetList.size()) {
            JLabel removed = targetList.remove(areaIndex);
            layeredPane.remove(removed);
            layeredPane.revalidate();
            layeredPane.repaint();
        }
    }

    // ebala aytj tj sunarthsh gia to image giati mou ebgaze errors 
    // wste na pairno sosta tin eikona( bohthhse kai to ai edw)
    private ImageIcon loadImage(String path) {
        URL imgURL = getClass().getResource("/" + path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        }
        
        if (new java.io.File(path).exists()) {
            return new ImageIcon(path);
        }
    
        imgURL = getClass().getResource("/" + path.replace("resources/", ""));
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        }

        System.err.println(" Image not found: " + path);
        return null;
    }




    public void playMusicForPlayer(int playerNum) { 
        try { 
            if (musicClip != null && musicClip.isOpen()) { 
                musicClip.stop(); musicClip.close(); 
            } 
            java.net.URL url = getClass().getResource("/resources/music/Player" + playerNum + ".wav"); 
            if (url == null) return; 
            javax.sound.sampled.AudioInputStream ais = javax.sound.sampled.AudioSystem.getAudioInputStream(url); 
            musicClip = javax.sound.sampled.AudioSystem.getClip(); 
            musicClip.open(ais); 
            musicClip.loop(javax.sound.sampled.Clip.LOOP_CONTINUOUSLY); 
            musicClip.start(); 
        } catch (Exception ex) { 
            ex.printStackTrace(); 
        } 
    }

    public void stopMusic() { 
        if (musicClip != null && musicClip.isOpen()) { 
            musicClip.stop(); 
            musicClip.close(); 
        } 
    }

    // getters
    public JButton getDrawButton() { return drawButton; }
    public JButton getEndTurnButton() { return endTurnButton; }
    public ArrayList<JButton> getCharacterButtons() { return characterButtons; }
    public JLayeredPane getLayeredPane() { return layeredPane; }


    public JButton getMosaicAreaButton() { return mosaicAreaButton; }
    public JButton getStatueAreaButton() { return statueAreaButton; }
    public JButton getAmphoraAreaButton() { return amphoraAreaButton; }
    public JButton getSkeletonAreaButton() { return skeletonAreaButton; }
}