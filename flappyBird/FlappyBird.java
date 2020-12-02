package flappyBird;

import javax.swing.*;
import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.util.ArrayList;
import java.util.Random;

public class FlappyBird implements ActionListener, MouseListener, KeyListener {

    public static FlappyBird flappyBird;

    public final int WINDOW_WIDTH = 1200, WINDOW_HEIGHT = 800;
    public  Renderer renderer;

    public Rectangle bird;
    public int ticks, yMotion, score;
    public boolean gameOver, started;

    public ArrayList<Rectangle> columns;
    public Random rand;
    public final int GROUND_WIDTH = 120, SPACE_BETWEEN_COLUMN_PAIRS = 300;

    public final String FONT_NAME = "Arial";
    public final int FONT_SIZE = 100;

    public FlappyBird() {
        JFrame jFrame = new JFrame();
        Timer timer = new Timer(20, this);
        renderer = new Renderer();
        rand = new Random();

        jFrame.add(renderer);
        jFrame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);

        jFrame.addMouseListener(this);
        jFrame.addKeyListener(this);

        jFrame.setResizable(true);
        jFrame.setTitle("Flappy Bird");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        jFrame.setVisible(true);

        bird = new Rectangle(WINDOW_WIDTH / 2 - 10, WINDOW_HEIGHT / 2 - 10, 20, 20);
        columns = new ArrayList<Rectangle>();

        addColumn(true);
        addColumn(true);
        addColumn(true);
        addColumn(true);

        timer.start();
    }

    public void addColumn(boolean start) {

        int width = 100;
        int height = 200 + rand.nextInt(300);


        if(start) {
            columns.add(new Rectangle(WINDOW_WIDTH + width + columns.size() * SPACE_BETWEEN_COLUMN_PAIRS, WINDOW_HEIGHT - height - GROUND_WIDTH, width, height));
            columns.add(new Rectangle(WINDOW_WIDTH + width + (columns.size() - 1) * SPACE_BETWEEN_COLUMN_PAIRS, 0, width, WINDOW_HEIGHT - height - SPACE_BETWEEN_COLUMN_PAIRS));
        } else {
            columns.add(new Rectangle(columns.get(columns.size() - 1).x + SPACE_BETWEEN_COLUMN_PAIRS * 2, WINDOW_HEIGHT - height - GROUND_WIDTH, width, height));
            columns.add(new Rectangle(columns.get(columns.size() - 1).x, 0, width, WINDOW_HEIGHT - height - SPACE_BETWEEN_COLUMN_PAIRS));
        }
    }

    public void paintColumn(Graphics g, Rectangle column) {
        g.setColor(Color.green.brighter());
        g.fillRect(column.x, column.y, column.width, column.height);
    }

    public void jump() {
        if (gameOver) {
            bird = new Rectangle(WINDOW_WIDTH / 2 - 10, WINDOW_HEIGHT / 2 - 10, 20, 20);
            columns.clear();
            yMotion = 0;
            score = 0;

            addColumn(true);
            addColumn(true);
            addColumn(true);
            addColumn(true);

            gameOver = false;
        }

        if(!started) {
            started = true;
        } else {
            if(yMotion > 0)
                yMotion = 0;

            yMotion -= 10;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ticks++;
        int speed = 10;

        if(started) {

            for (Rectangle column : columns) {
                column.x -= speed;
            }

            if (ticks % 2 == 0 && yMotion < 15)
                yMotion += 2;

            for (int i = 0; i < columns.size(); i++) {
                Rectangle column = columns.get(i);
                if (column.x + column.width < 0) {
                    columns.remove(column);

                    if (column.y == 0)
                        addColumn(false);
                }
            }

            bird.y += yMotion;

            for (Rectangle column : columns) {
                if( column.y == 0 && bird.x + bird.width / 2 > column.x + column.width / 2 - 10 && bird.x + bird.width / 2 < column.x + column.width / 2 + 10) {
                    score++;
                }
                if (column.intersects(bird)) {
                    gameOver = true;
                    bird.x = column.x - bird.width;
                }
            }

            if (bird.y > WINDOW_HEIGHT - GROUND_WIDTH || bird.y <= 0)
                gameOver = true;

            if (bird.y + yMotion >= WINDOW_HEIGHT - GROUND_WIDTH) {
                bird.y = WINDOW_HEIGHT - GROUND_WIDTH - bird.width;
            }
        }
        renderer.repaint();
    }

    public void repaint(Graphics g) {
        //Draw sky
        g.setColor(Color.cyan.darker().darker());
        g.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

        //Draw ground
        g.setColor(Color.orange);
        g.fillRect(0, WINDOW_HEIGHT - GROUND_WIDTH, WINDOW_WIDTH, 150);

        //Draw grass
        g.setColor(new Color(111, 175, 0));
        g.fillRect(0, WINDOW_HEIGHT - GROUND_WIDTH, WINDOW_WIDTH, 20);

        //Draw bird
        g.setColor(Color.red);
        g.fillRect(bird.x, bird.y, bird.width, bird.height);

        for(Rectangle column : columns) {
            paintColumn(g, column);
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font(FONT_NAME, Font.BOLD, FONT_SIZE));

        if(gameOver)
            g.drawString("Game Over!", WINDOW_WIDTH / 2 - 250, WINDOW_HEIGHT / 2 - 50);
        else if (!started)
            g.drawString("Click to start!", WINDOW_WIDTH / 2 - 300, WINDOW_HEIGHT / 2 - 50 );

        if(!gameOver && started)
            g.drawString(String.valueOf(score), WINDOW_WIDTH / 2 - 25, 100);
    }

    public static void main(String[] args) {
        flappyBird = new FlappyBird();
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        jump();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            jump();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
