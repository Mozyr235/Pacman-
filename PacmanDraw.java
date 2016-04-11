import java.awt.Graphics2D;
import java.util.Scanner;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;
import java.lang.*;
import java.io.*;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.Timer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.swing.ImageIcon;

public class PacmanDraw {

    private Board board;
    
    PacmanDraw(Board board)
    {
    	this.board = board;
    }
    
    //функция отрисовки меню
    public void showIntroScreen(Graphics2D g2d) {

    	this.board.FonColor = new Color(this.board.r,this.board.g,this.board.b);
    	g2d.setColor(this.board.FonColor);
        g2d.fillRect(0, this.board.scrsize / 2 - 180, this.board.scrsize - 0, 380);
        g2d.setColor(Color.red);
        g2d.drawRect(0, this.board.scrsize / 2 - 30, this.board.scrsize - 0, 100);

        String s = "Выберите уровень сложности:";
        Font small = new Font("Helvetica", Font.BOLD, 12);
        FontMetrics metr = this.board.getFontMetrics(small);
       
        String s1 = "e-лёгкий,n-средний,h-сложный.";
        Font small1 = new Font("Helvetica", Font.BOLD, 12);
        FontMetrics metr1 = this.board.getFontMetrics(small);
        
        String s2 = "А после для начала игры нажмите s.";
        Font small2 = new Font("Helvetica", Font.BOLD, 12);
        FontMetrics metr2 = this.board.getFontMetrics(small);
        
        String s3 = "а - режим автопилота.";
        Font small3 = new Font("Helvetica", Font.BOLD, 12);
        FontMetrics metr3 = this.board.getFontMetrics(small);
        
        g2d.setColor(Color.red);
        g2d.setFont(small);
        g2d.drawString(s, (this.board.scrsize - metr.stringWidth(s)) / 2, this.board.scrsize / 2);
        
        g2d.setColor(Color.red);
        g2d.setFont(small1);
        g2d.drawString(s1, (this.board.scrsize - metr1.stringWidth(s1)) / 2, this.board.scrsize / 2+20);
        
        g2d.setColor(Color.red);
        g2d.setFont(small2);
        g2d.drawString(s2, (this.board.scrsize - metr2.stringWidth(s2)) / 2, this.board.scrsize / 2+40);
        
        g2d.setColor(Color.red);
        g2d.setFont(small3);
        g2d.drawString(s3, (this.board.scrsize - metr3.stringWidth(s3)) / 2, this.board.scrsize / 2+160);
    }
    //функция обработки счёта
    private void drawScore(Graphics2D g) {

        int i;
        String s;

        g.setFont(this.board.smallfont);
        g.setColor(new Color(255, 255, 0));
        s = "Счёт: " + this.board.score;
        g.drawString(s, this.board.scrsize / 2 + 96, this.board.scrsize + 16);

        for (i = 0; i < this.board.pacsleft; i++) {
            g.drawImage(this.board.pacman3left, i * 28 + 8, this.board.scrsize + 1, this.board);
        }
    }
    
    //функция отрисовки пакмана
    public void drawPacman(Graphics2D g2d) {
    	Scanner sc = new Scanner(System.in); 
    	try {
	        if (Board.viewdx == -1) {
	            drawPacmanLeft(g2d);
	        } else if (Board.viewdx == 1) {
	            drawPacmanRight(g2d);
	        } else if (Board.viewdy == -1) {
	            drawPacmanUp(g2d);
	        } else {
	            drawPacmanDown(g2d);
	        }
    	} catch (Error e) { 
            System.out.println("Произошло исключение"); 
        } 
    }
    //функция отрисовки пакмана при движении вверх
    public void drawPacmanUp(Graphics2D g2d) {

        switch (Board.pacmananimpos) {
            case 1:
            	g2d.drawImage(this.board.pacman2up, Board.pacmanx + 1, Board.pacmany + 1, this.board);
                break;
            case 2:
                g2d.drawImage(Board.pacman3up, Board.pacmanx + 1, Board.pacmany + 1, this.board);
                break;
            case 3:
                g2d.drawImage(Board.pacman4up, Board.pacmanx + 1, Board.pacmany + 1, this.board);
                break;
            default:
                g2d.drawImage(Board.pacman1, Board.pacmanx + 1, Board.pacmany + 1, this.board);
                break;
        }
    }
    //функция отрисовки пакмана при движении вниз
    public void drawPacmanDown(Graphics2D g2d) {

        switch (this.board.pacmananimpos) {
            case 1:
                g2d.drawImage(this.board.pacman2down, this.board.pacmanx + 1, this.board.pacmany + 1, this.board);
                break;
            case 2:
                g2d.drawImage(this.board.pacman3down, this.board.pacmanx + 1, this.board.pacmany + 1, this.board);
                break;
            case 3:
                g2d.drawImage(this.board.pacman4down, this.board.pacmanx + 1, this.board.pacmany + 1, this.board);
                break;
            default:
                g2d.drawImage(this.board.pacman1, this.board.pacmanx + 1, this.board.pacmany + 1, this.board);
                break;
        }
    }
    //функция отрисовки пакмана при движении влево
    public void drawPacmanLeft(Graphics2D g2d) {

        switch (this.board.pacmananimpos) {
            case 1:
                g2d.drawImage(this.board.pacman2left, this.board.pacmanx + 1, this.board.pacmany + 1, this.board);
                break;
            case 2:
                g2d.drawImage(this.board.pacman3left, this.board.pacmanx + 1, this.board.pacmany + 1, this.board);
                break;
            case 3:
                g2d.drawImage(this.board.pacman4left, this.board.pacmanx + 1, this.board.pacmany + 1, this.board);
                break;
            default:
                g2d.drawImage(this.board.pacman1, this.board.pacmanx + 1, this.board.pacmany + 1, this.board);
                break;
        }
    }
    //функция отрисовки пакмана при движении вправо
    public void drawPacmanRight(Graphics2D g2d) {

        switch (this.board.pacmananimpos) {
            case 1:
                g2d.drawImage(this.board.pacman2right, this.board.pacmanx + 1, this.board.pacmany + 1, this.board);
                break;
            case 2:
                g2d.drawImage(this.board.pacman3right, this.board.pacmanx + 1, this.board.pacmany + 1, this.board);
                break;
            case 3:
                g2d.drawImage(this.board.pacman4right, this.board.pacmanx + 1, this.board.pacmany + 1, this.board);
                break;
            default:
                g2d.drawImage(this.board.pacman1, this.board.pacmanx + 1, this.board.pacmany + 1, this.board);
                break;
        }
    }
    
  //функция отрисовки призрака
    public void drawGhost(Graphics2D g2d, int x, int y) {

        g2d.drawImage(this.board.ghost, x, y, this.board);
    }
    
    //фукция загрузки изображений
    public void loadImages() {

    	Board.ghost = new ImageIcon("images/ghost.png").getImage();
    	Board.pacman1 = new ImageIcon("images/pacman.png").getImage();
    	Board.pacman2up = new ImageIcon("images/up1.png").getImage();
    	Board.pacman3up = new ImageIcon("images/up2.png").getImage();
    	Board.pacman4up = new ImageIcon("images/up3.png").getImage();
    	Board.pacman2down = new ImageIcon("images/down1.png").getImage();
    	Board.pacman3down = new ImageIcon("images/down2.png").getImage();
    	Board.pacman4down = new ImageIcon("images/down3.png").getImage();
    	Board.pacman2left = new ImageIcon("images/left1.png").getImage();
    	Board.pacman3left = new ImageIcon("images/left2.png").getImage();
    	Board.pacman4left = new ImageIcon("images/left3.png").getImage();
    	Board.pacman2right = new ImageIcon("images/right1.png").getImage();
    	Board.pacman3right = new ImageIcon("images/right2.png").getImage();
    	Board.pacman4right = new ImageIcon("images/right3.png").getImage();

    }
    //функция отрисовки поля
    private void drawMaze(Graphics2D g2d) {

        short i = 0;
        int x, y;

        for (y = 0; y < this.board.scrsize; y += this.board.blocksize) {
            for (x = 0; x < this.board.scrsize; x += this.board.blocksize) {

                g2d.setColor(this.board.mazecolor);
                g2d.setStroke(new BasicStroke(2));

                if ((this.board.screendata[i] & 1) != 0) { 
                    g2d.drawLine(x, y, x, y + this.board.blocksize - 1);
                }

                if ((this.board.screendata[i] & 2) != 0) { 
                    g2d.drawLine(x, y, x + this.board.blocksize - 1, y);
                }

                if ((this.board.screendata[i] & 4) != 0) { 
                    g2d.drawLine(x + this.board.blocksize - 1, y, x + this.board.blocksize - 1,
                            y + this.board.blocksize - 1);
                }

                if ((this.board.screendata[i] & 8) != 0) { 
                    g2d.drawLine(x, y + this.board.blocksize - 1, x + this.board.blocksize - 1,
                            y + this.board.blocksize - 1);
                }

                if ((this.board.screendata[i] & 16) != 0) { 
                    g2d.setColor(this.board.dotcolor);
                    g2d.fillRect(x + 11, y + 11, 2, 2);
                }

                i++;
            }
        }
    }
    
    //функция отрисовки поля
    public void doDrawing(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.black);
        g2d.fillRect(0, 0, this.board.d.width, this.board.d.height);

        drawMaze(g2d);
        drawScore(g2d);
        this.board.doAnim();

        if (this.board.ingame) {
        	this.board.playGame(g2d);
        } else {
            showIntroScreen(g2d);
        }

        g2d.drawImage(this.board.ii, 5, 5, this.board);
        Toolkit.getDefaultToolkit().sync();
        g2d.dispose();
    }
}
