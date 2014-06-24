package com.game.Handler;

import com.game.Model.Creature;
import com.game.Model.Platform;
import com.game.Model.Player;
import com.game.Util.CollisionCenter;
import com.game.View.*;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.*;

/**
 * The main GUI generator and handler. Generates all
 * services, menus and other widgets, playing the role
 * of the JFrame main window. Also controls player's
 * movement on itself via listening MouseEvent objects.
 * Declare a timer for checking the game status and if
 * the game is not active show the result menu.
 *
 * This module can be loaded externally if it is
 * necessary, e.g. for a multi-games client.
 *
 * @see java.awt.event.MouseEvent
 * @see java.awt.event.MouseListener
 * @see java.awt.event.MouseMotionListener
 *
 * Created by E. Mozharovsky on 19.06.14.
 */
public class GameHandler extends JFrame implements ActionListener {
    // constants

    /**
     * Window's width.
     */
    private static final int WIDTH  = 700;

    /**
     * Window's height.
     */
    private static final int HEIGHT = 730;

    // creatures

    /**
     * The only playable platform in game.
     */
    private Player        player;

    /**
     * An array of non-playable platforms.
     */
    private Platform[] platforms;

    // game resources

    /**
     * A game over control timer.
     */
    private Timer timer;

    /**
     * The game's status flag.
     */
    private boolean isGameStarted = false;

    /**
     * Create a new object of GameHandler, load all dependencies
     * and initialize main menu waiting for player's actions.
     *
     * Parameters used to create a new window on the previous location.
     * @param x The previous X-coordinate.
     * @param y The previous Y-coordinate.
     */
    public GameHandler(int x, int y) {
        // installation of the window
        setLayout(null);
        setBounds(x, y, WIDTH, HEIGHT);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.lightGray);

        // main menu initialization
        initMainMenu();

        // bar initialization
        initMenuBar();

        // further installation work
        setVisible(true);
        setResizable(false);
        repaint();
    }

    /**
     * In fact, start a game. Initialize creatures (player and
     * platforms) and adding them to a game panel which will be
     * set as the window's content pane. Add the mouse adapter for
     * listening mouse events.
     */
    private void initCreatures() {
        isGameStarted = true;
        timer = new Timer(20, this);
        timer.start();

        // change the window title
        setTitle("SNAKE [Status: RUNNING]");

        // make a game panel which is 23 pixels below of the frame's start point
        JPanel gamePanel = new JPanel();
        gamePanel.setBounds(0, 23, WIDTH, HEIGHT);
        gamePanel.setLayout(null);
        gamePanel.setBackground(Color.lightGray);

        // create and add creatures
        player = new Player();

        platforms = new Platform[4];
        platforms[0] = new Platform(new ImageIcon("resources/2.png").getImage(), 150, 150);
        platforms[1] = new Platform(new ImageIcon("resources/3.png").getImage(), 300, 300);
        platforms[2] = new Platform(new ImageIcon("resources/3.png").getImage(), 500, 300);
        platforms[3] = new Platform(new ImageIcon("resources/2.png").getImage(), 300, 550);

        for(Creature creature : platforms)
            gamePanel.add(creature);
        gamePanel.add(player);
        setContentPane(gamePanel);

        // add an adapter to control player's actions
        addMouseAdapter(gamePanel);
    }

    /**
     * Initialize main menu. Add buttons of start and close
     * the game with action listeners aimed at obvious actions.
     */
    private void initMainMenu() {
        isGameStarted = false;

        setTitle("Main Menu");
        Icon icon = new ImageIcon("resources/Logo.png");

        JLabel logo = new JLabel();
        logo.setBounds(200, 150, icon.getIconWidth(), icon.getIconHeight());
        logo.setIcon(icon);

        JButton start = new JButton("Start");
        start.setBounds(285, 430, 130, 30);
        start.setFocusable(false);
        start.addActionListener(e -> {
            getContentPane().removeAll();
            repaint();
            initCreatures();
        });

        JButton close = new JButton("Close");
        close.setBounds(285, 460, 130, 30);
        close.setFocusable(false);
        close.addActionListener(e -> System.exit(0));

        // TODO: Make a statistics service
        JButton statistics = new JButton("Statistics");
        statistics.setBounds(285, 490, 130, 30);
        statistics.setFocusPainted(true);
        statistics.setEnabled(false);

        getContentPane().add(statistics);
        getContentPane().add(start);
        getContentPane().add(close);
        getContentPane().add(logo);
    }

    /**
     * Pause the game process depending on
     * the state given as an argument.
     *
     * @param state
     *             true - start game,
     *             false - pause.
     */
    private void pause(boolean state) {
        if(state) {
            setTitle("SNAKE [Status: RUNNING]");
            isGameStarted = true;
        }

        else {
            setTitle("SNAKE [Status: PAUSED]");
            isGameStarted = false;
        }

        for(CollisionListener creature : CustomStorage.getCollisionListeners())
            ((Creature) creature).setMovementState(state);
    }

    /**
     * Initialize the menu bar with its items.
     * When it's open while the game process,
     * the game pauses itself and continue after
     * player deselect the menu either do something
     * inside of this menu.
     */
    private void initMenuBar() {
        JMenuBar bar = new JMenuBar();
        setJMenuBar(bar);

        JMenu file = new JMenu("File");
        file.addMenuListener(new MenuListener() {
            // pause the game
            @Override
            public void menuSelected(MenuEvent e) {
                pause(false);
            }

            // start the game from the pause point
            @Override
            public void menuDeselected(MenuEvent e) {
                pause(true);
            }

            // unused
            @Override
            public void menuCanceled(MenuEvent e) { }
        });

        JMenuItem about = new JMenuItem("About me");
        about.addActionListener(e -> {
            // pause the game
            pause(false);

            JOptionPane.showMessageDialog(getContentPane(), "Copyright (c) 2014 E. Mozharovsky. All rights reserved. \nThe game is written in Java 8 (jdk_1.8.X).\n\nContact us: mozharovsky@live.com", "About me", JOptionPane.INFORMATION_MESSAGE);

            // resume the game process
            pause(true);
        });

        // other widgets shouldn't be added since they do not provide any
        // back-game processes

        JMenuItem menu = new JMenuItem("Main menu");
        menu.addActionListener(e -> {
            dispose();
            CustomStorage.clear();

            new GameHandler(getX(), getY()).initMainMenu();
        });

        JMenuItem close = new JMenuItem("Close game");
        close.addActionListener(e -> System.exit(0));

        file.add(about);
        file.add(menu);
        file.addSeparator();
        file.add(close);
        bar.add(file);
    }

    /**
     * Create a modal JDialog widget with buttons of restarting, closing
     * and returning to the main menu. Create a new session if the player
     * wants to restart the game or return to its main menu. Also display
     * player's score points.
     */
    private void showResultMenu() {
        setTitle("SNAKE [Status: PAUSED]");

        JDialog result = new JDialog(this);
        result.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE); // so user should use buttons
        result.setBounds(getX() + 200, getY() + 200, 300, 300);
        result.setResizable(false);
        result.setTitle("Game Over");
        result.setLayout(null);
        result.setModal(true); // the widget should be a modal of the main window

        JLabel score = new JLabel("Your score: " + CollisionCenter.getScore() / 5); // a stupid way ...
        score.setBounds(100, 60, 200, 30);
        score.setFont(new Font("sansserif", Font.BOLD, 14));
        CollisionCenter.cleanScore();

        JButton restart = new JButton("Restart");
        restart.setBounds(90, 120, 130, 30);
        restart.setFocusPainted(false);
        restart.addActionListener(e -> {
            CustomStorage.clear();
            getContentPane().removeAll();
            repaint();

            dispose();

            // start a new session
            new GameHandler(getX(), getY()).initCreatures();
        });

        JButton menu = new JButton("Menu");
        menu.setBounds(90, 150, 130, 30);
        menu.setFocusPainted(false);
        menu.addActionListener(e -> {
            CustomStorage.clear();
            getContentPane().removeAll();
            repaint();

            dispose();

            // start a new session
            new GameHandler(getX(), getY()).initMainMenu();
        });

        JButton close = new JButton("Close");
        close.setBounds(90, 180, 130, 30);
        close.setFocusPainted(false);
        close.addActionListener(e -> System.exit(0));

        result.add(restart);
        result.add(score);
        result.add(close);
        result.add(menu);

        result.setVisible(true);
    }

    /**
     * A way to control playable widget on the window by using
     * a mouse. Create and register a MouseAdapter to listen
     * mouse events.
     *
     * @param comp Any JComponent which registers player's control adapter.
     */
    private void addMouseAdapter(JComponent comp) {
        MouseAdapter adapter = new MouseAdapter() {
            boolean canMove = false;

            @Override
            public void mouseDragged(MouseEvent e) {
                if(canMove) {
                    // visualize interaction with the player's object
                    setCursor(new Cursor(Cursor.HAND_CURSOR));

                    // a way to detect movement's vector
                    if(e.getX() - 25 > player.getX()) {
                        player.setVector(Creature.Vector.RIGHT);
                    }
                    if(e.getX() - 25 < player.getX()) {
                        player.setVector(Creature.Vector.LEFT);
                    }
                    if(e.getY() - 25 < player.getY()) {
                        player.setVector(Creature.Vector.UP);
                    }
                    if(e.getY() - 25 > player.getY()) {
                        player.setVector(Creature.Vector.DOWN);
                    }

                    // set cursor on the middle of the player's model
                    player.updateCoord(e.getX() - 25, e.getY() - 25);

                    // TODO: Print bonus panel + add more score points
                    if(player.getX() + player.getWidth() >= 700) {
                        player.updateCoord(player.getX() - 650, player.getY());
                    } else if(player.getX() <= 0) {
                        player.updateCoord(player.getX() + 650, player.getY());
                    } else if(player.getY() <= 0) {
                        player.updateCoord(player.getX(), player.getY() + 630);
                    } else if(player.getY() + player.getHeight() >= 680) {
                        player.updateCoord(player.getX(), player.getY() - 630);
                    }
                }
            }

            /**
             * Check if the clicked area belongs to the player's figure.
             * If so, then allow coordinates updating.
             *
             * @param e A mouse event object.
             */
            @Override
            public void mousePressed(MouseEvent e) {
                if(e.getX() > player.getX() && e.getX() < player.getX() + player.getWidth() &&
                        e.getY() > player.getY() && e.getY() < player.getY() + player.getHeight()) {
                    canMove = true;
                }
            }

            /**
             * If the player released its figure the game return
             * the default cursor type, stops any further movement
             * detecting and sets player's vector of movement as NONE.
             *
             * @param e A mouse event object.
             */
            @Override
            public void mouseReleased(MouseEvent e) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                player.setVector(Creature.Vector.NONE);
                canMove = false;
            }
        };

        // add installed adapter as a listener for mouse
        comp.addMouseListener(adapter);
        comp.addMouseMotionListener(adapter);
    }

    /**
     * Check for the game activity. If it wasn't detected, stop the timer
     * and display the result menu.
     *
     * @param ev An action event.
     */
    @Override
    public void actionPerformed(ActionEvent ev) {
        if(!player.getMovementState() && isGameStarted) {
            showResultMenu();
            timer.stop();
        }
    }
}
