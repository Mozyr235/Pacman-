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


public class Board extends JPanel implements ActionListener {

    private Dimension d;
    private final Font smallfont = new Font("Helvetica", Font.BOLD, 14);

    private Image ii;
    private final Color dotcolor = new Color(192, 192, 0);
    private Color mazecolor;

    private boolean ingame = false;
    private boolean dying = false;

    private final int blocksize = 24;
    private final int nrofblocks = 15;
    private final int scrsize = nrofblocks * blocksize;
    private final int pacanimdelay = 2;
    private final int pacmananimcount = 4;
    private final int maxghosts = 20;
    private final int pacmanspeed = 6;

    private int pacanimcount = pacanimdelay;
    private int pacanimdir = 1;
    private int pacmananimpos = 0;
    private int nrofghosts = 12;
    private int pacsleft, score;
    private int[] dx, dy;
    private int[] ghostx, ghosty, ghostdx, ghostdy, ghostspeed;

    private Image ghost;
    private Image pacman1, pacman2up, pacman2left, pacman2right, pacman2down;
    private Image pacman3up, pacman3down, pacman3left, pacman3right;
    private Image pacman4up, pacman4down, pacman4left, pacman4right;

    private int pacmanx, pacmany, pacmandx, pacmandy;
    private int reqdx, reqdy, viewdx, viewdy;

    private final short leveldata[] = {
        19, 26, 26, 26, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 22,
        21, 0, 0, 0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
        21, 0, 0, 0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
        21, 0, 0, 0, 17, 16, 16, 24, 16, 16, 16, 16, 16, 16, 20,
        17, 18, 18, 18, 16, 16, 20, 0, 17, 16, 16, 16, 16, 16, 20,
        17, 16, 16, 16, 16, 16, 20, 0, 17, 16, 16, 16, 16, 24, 20,
        25, 16, 16, 16, 24, 24, 28, 0, 25, 24, 24, 16, 20, 0, 21,
        1, 17, 16, 20, 0, 0, 0, 0, 0, 0, 0, 17, 20, 0, 21,
        1, 17, 16, 16, 18, 18, 22, 0, 19, 18, 18, 16, 20, 0, 21,
        1, 17, 16, 16, 16, 16, 20, 0, 17, 16, 16, 16, 20, 0, 21,
        1, 17, 16, 16, 16, 16, 20, 0, 17, 16, 16, 16, 20, 0, 21,
        1, 17, 16, 16, 16, 16, 16, 18, 16, 16, 16, 16, 20, 0, 21,
        1, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20, 0, 21,
        1, 25, 24, 24, 24, 24, 24, 24, 24, 24, 16, 16, 16, 18, 20,
        9, 8, 8, 8, 8, 8, 8, 8, 8, 8, 25, 24, 24, 24, 28
    };

    private final int validspeeds[] = {1, 2, 3, 4, 6, 8};
    private final int maxspeed = 6;

    private int currentspeed = 3;
    private short[] screendata;
    private Timer timer;
    
    public static int change=0;
    public static int r=0,g=0,b=0;
    public static int pacmanauto = 0;
    public static int ghost_number=2;
    public static Color FonColor = new Color(0,0,0);
    
    public Board() {

        loadImages();
        initVariables();
        
        addKeyListener(new TAdapter());

        setFocusable(true);

        setBackground(Color.black);
        setDoubleBuffered(true);
    }

    private void initVariables() {

        screendata = new short[nrofblocks * nrofblocks];
        mazecolor = new Color(100, 100, 110);
        d = new Dimension(400, 400);
        ghostx = new int[maxghosts];
        ghostdx = new int[maxghosts];
        ghosty = new int[maxghosts];
        ghostdy = new int[maxghosts];
        ghostspeed = new int[maxghosts];
        dx = new int[4];
        dy = new int[4];
        
        timer = new Timer(40, this);
        timer.start();
    }

    @Override
    public void addNotify() {
        super.addNotify();

        initGame();
    }

    private void doAnim() {

        pacanimcount--;

        if (pacanimcount <= 0) {
            pacanimcount = pacanimdelay;
            pacmananimpos = pacmananimpos + pacanimdir;

            if (pacmananimpos == (pacmananimcount - 1) || pacmananimpos == 0) {
                pacanimdir = -pacanimdir;
            }
        }
    }
    
    private void playGame(Graphics2D g2d) {

        if (dying) {

            death();

        } else {
        	if(pacmanauto==1)
        	{
        		movePacmanAuto(g2d);
        	}
        	else
        	{
        		movePacman();
            }
            drawPacman(g2d);
            moveGhosts(g2d);
            checkMaze();
        }
    }

    public void showIntroScreen(Graphics2D g2d) {

       // g2d.setColor(new Color(0, 0, 0));
    	FonColor = new Color(r,g,b);
    	g2d.setColor(FonColor);
        g2d.fillRect(0, scrsize / 2 - 180, scrsize - 0, 380);
        g2d.setColor(Color.red);
        g2d.drawRect(0, scrsize / 2 - 30, scrsize - 0, 100);

        String s = "�������� ������� ���������:";
        Font small = new Font("Helvetica", Font.BOLD, 12);
        FontMetrics metr = this.getFontMetrics(small);
       
        String s1 = "e-�����,n-�������,h-�������.";
        Font small1 = new Font("Helvetica", Font.BOLD, 12);
        FontMetrics metr1 = this.getFontMetrics(small);
        
        String s2 = "� ����� ��� ������ ���� ������� s.";
        Font small2 = new Font("Helvetica", Font.BOLD, 12);
        FontMetrics metr2 = this.getFontMetrics(small);
        
        String s3 = "� - ����� ����������.";
        Font small3 = new Font("Helvetica", Font.BOLD, 12);
        FontMetrics metr3 = this.getFontMetrics(small);
        
        g2d.setColor(Color.red);
        g2d.setFont(small);
        g2d.drawString(s, (scrsize - metr.stringWidth(s)) / 2, scrsize / 2);
        
        g2d.setColor(Color.red);
        g2d.setFont(small1);
        g2d.drawString(s1, (scrsize - metr1.stringWidth(s1)) / 2, scrsize / 2+20);
        
        g2d.setColor(Color.red);
        g2d.setFont(small2);
        g2d.drawString(s2, (scrsize - metr2.stringWidth(s2)) / 2, scrsize / 2+40);
        
        g2d.setColor(Color.red);
        g2d.setFont(small3);
        g2d.drawString(s3, (scrsize - metr3.stringWidth(s3)) / 2, scrsize / 2+160);
    }

    private void drawScore(Graphics2D g) {

        int i;
        String s;

        g.setFont(smallfont);
        g.setColor(new Color(255, 255, 0));
        s = "����: " + score;
        g.drawString(s, scrsize / 2 + 96, scrsize + 16);

        for (i = 0; i < pacsleft; i++) {
            g.drawImage(pacman3left, i * 28 + 8, scrsize + 1, this);
        }
    }

    private void checkMaze() {

        short i = 0;
        boolean finished = true;

        while (i < nrofblocks * nrofblocks && finished) {

            if ((screendata[i] & 48) != 0) {
                finished = false;
            }

            i++;
        }

        if (finished) {

            score = score + 50 + 50*pacsleft;
            
            if (nrofghosts < maxghosts) {
                nrofghosts++;
            }

            if (currentspeed < maxspeed) {
                currentspeed++;
            }

            initLevel();
        }
    }

    private void death() {

        pacsleft--;

        if (pacsleft == 0) {
        	
            ingame = false;
        }

        continueLevel();
    }

    private void moveGhosts(Graphics2D g2d) {

        short i;
        int pos;
        int count;

        for (i = 0; i < nrofghosts; i++) {
            if (ghostx[i] % blocksize == 0 && ghosty[i] % blocksize == 0) {
                pos = ghostx[i] / blocksize + nrofblocks * (int) (ghosty[i] / blocksize);

                count = 0;

                if ((screendata[pos] & 1) == 0 && ghostdx[i] != 1) {
                    dx[count] = -1;
                    dy[count] = 0;
                    count++;
                }

                if ((screendata[pos] & 2) == 0 && ghostdy[i] != 1) {
                    dx[count] = 0;
                    dy[count] = -1;
                    count++;
                }

                if ((screendata[pos] & 4) == 0 && ghostdx[i] != -1) {
                    dx[count] = 1;
                    dy[count] = 0;
                    count++;
                }

                if ((screendata[pos] & 8) == 0 && ghostdy[i] != -1) {
                    dx[count] = 0;
                    dy[count] = 1;
                    count++;
                }

                if (count == 0) {

                    if ((screendata[pos] & 15) == 15) {
                        ghostdx[i] = 0;
                        ghostdy[i] = 0;
                    } else {
                        ghostdx[i] = -ghostdx[i];
                        ghostdy[i] = -ghostdy[i];
                    }

                } else {

                    count = (int) (Math.random() * count);

                    if (count > 3) {
                        count = 3;
                    }

                    ghostdx[i] = dx[count];
                    ghostdy[i] = dy[count];
                }

            }

            ghostx[i] = ghostx[i] + (ghostdx[i] * ghostspeed[i]);
            ghosty[i] = ghosty[i] + (ghostdy[i] * ghostspeed[i]);
            drawGhost(g2d, ghostx[i] + 1, ghosty[i] + 1);

            if (pacmanx > (ghostx[i] - 12) && pacmanx < (ghostx[i] + 12)
                    && pacmany > (ghosty[i] - 12) && pacmany < (ghosty[i] + 12)
                    && ingame) {
                dying = true;
            }
        }
    }
///////////////////////////////////////////////
    private void movePacmanAuto(Graphics2D g2d) {

        short i;
        int pos;
        int count;
        short ch;
        
            if (pacmanx % blocksize == 0 && pacmany % blocksize == 0) {
                pos = pacmanx / blocksize + nrofblocks * (int) (pacmany / blocksize);

                count = 0;

                if ((screendata[pos] & 1) == 0 && pacmandx != 1) {
                    dx[count] = -1;
                    dy[count] = 0;
                    count++;
                   viewdy = 0;
                   viewdx = -1;
                   reqdx = -1;
                   reqdy = 0;
                }

                if ((screendata[pos] & 2) == 0 && pacmandy != 1) {
                    dx[count] = 0;
                    dy[count] = -1;
                    count++;
                    reqdx = 0;
                    reqdy = -1;
                    viewdx = 0;
                    viewdy = -1;
                }

                if ((screendata[pos] & 4) == 0 && pacmandx != -1) {
                    dx[count] = 1;
                    dy[count] = 0;
                    count++;
                    reqdx = 1;
                    reqdy = 0;
                    viewdy = 0;
                    viewdx = 1;
                }

                if ((screendata[pos] & 8) == 0 && pacmandy != -1) {
                    dx[count] = 0;
                    dy[count] = 1;
                    count++;
                    reqdx = 0;
                    reqdy = 1;
                    viewdx = 0;
                    viewdy = 1;
                }

                if (count == 0) {

                    if ((screendata[pos] & 15) == 15) {
                    	pacmandx = 0;
                    	pacmandy = 0;
                    } else {
                    	pacmanx = -pacmandx;
                    	pacmandy = -pacmandy;
                    }

                } else {

                    count = (int) (Math.random() * count);

                    if (count > 3) {
                        count = 3;
                    }

                    pacmandx = dx[count];
                    pacmandy = dy[count];
                }

            }
            

            
            if (pacmanx % blocksize == 0 && pacmany % blocksize == 0) {
                pos = pacmanx / blocksize + nrofblocks * (int) (pacmany / blocksize);
                ch = screendata[pos];

                if ((ch & 16) != 0) {
                    screendata[pos] = (short) (ch & 15);
                    score++;
                }

                if (reqdx != 0 || reqdy != 0) {
                    if (!((reqdx == -1 && reqdy == 0 && (ch & 1) != 0)
                            || (reqdx == 1 && reqdy == 0 && (ch & 4) != 0)
                            || (reqdx == 0 && reqdy == -1 && (ch & 2) != 0)
                            || (reqdx == 0 && reqdy == 1 && (ch & 8) != 0))) {
                        viewdx = pacmandx;
                        viewdy = pacmandy;
                    }
                }

                if ((pacmandx == -1 && pacmandy == 0 && (ch & 1) != 0)
                        || (pacmandx == 1 && pacmandy == 0 && (ch & 4) != 0)
                        || (pacmandx == 0 && pacmandy == -1 && (ch & 2) != 0)
                        || (pacmandx == 0 && pacmandy == 1 && (ch & 8) != 0)) {
                    pacmandx = 0;
                    pacmandy = 0;
                }
            }
            pacmanx = pacmanx + (pacmandx * pacmanspeed);
            pacmany = pacmany + (pacmandy * pacmanspeed);
            
    }
///////////////////////////////////////////////
    private void drawGhost(Graphics2D g2d, int x, int y) {

        g2d.drawImage(ghost, x, y, this);
    }

    private void movePacman() {

        int pos;
        short ch;

        if (reqdx == -pacmandx && reqdy == -pacmandy) {
            pacmandx = reqdx;
            pacmandy = reqdy;
            viewdx = pacmandx;
            viewdy = pacmandy;
        }

        if (pacmanx % blocksize == 0 && pacmany % blocksize == 0) {
            pos = pacmanx / blocksize + nrofblocks * (int) (pacmany / blocksize);
            ch = screendata[pos];

            if ((ch & 16) != 0) {
                screendata[pos] = (short) (ch & 15);
                score++;
            }

            if (reqdx != 0 || reqdy != 0) {
                if (!((reqdx == -1 && reqdy == 0 && (ch & 1) != 0)
                        || (reqdx == 1 && reqdy == 0 && (ch & 4) != 0)
                        || (reqdx == 0 && reqdy == -1 && (ch & 2) != 0)
                        || (reqdx == 0 && reqdy == 1 && (ch & 8) != 0))) {
                    pacmandx = reqdx;
                    pacmandy = reqdy;
                    viewdx = pacmandx;
                    viewdy = pacmandy;
                }
            }

            if ((pacmandx == -1 && pacmandy == 0 && (ch & 1) != 0)
                    || (pacmandx == 1 && pacmandy == 0 && (ch & 4) != 0)
                    || (pacmandx == 0 && pacmandy == -1 && (ch & 2) != 0)
                    || (pacmandx == 0 && pacmandy == 1 && (ch & 8) != 0)) {
                pacmandx = 0;
                pacmandy = 0;
            }
        }
        pacmanx = pacmanx + pacmanspeed * pacmandx;
        pacmany = pacmany + pacmanspeed * pacmandy;
    }

    private void drawPacman(Graphics2D g2d) {

        if (viewdx == -1) {
            drawPacmanLeft(g2d);
        } else if (viewdx == 1) {
            drawPacmanRight(g2d);
        } else if (viewdy == -1) {
            drawPacmanUp(g2d);
        } else {
            drawPacmanDown(g2d);
        }
    }

    private void drawPacmanUp(Graphics2D g2d) {

        switch (pacmananimpos) {
            case 1:
                g2d.drawImage(pacman2up, pacmanx + 1, pacmany + 1, this);
                break;
            case 2:
                g2d.drawImage(pacman3up, pacmanx + 1, pacmany + 1, this);
                break;
            case 3:
                g2d.drawImage(pacman4up, pacmanx + 1, pacmany + 1, this);
                break;
            default:
                g2d.drawImage(pacman1, pacmanx + 1, pacmany + 1, this);
                break;
        }
    }

    private void drawPacmanDown(Graphics2D g2d) {

        switch (pacmananimpos) {
            case 1:
                g2d.drawImage(pacman2down, pacmanx + 1, pacmany + 1, this);
                break;
            case 2:
                g2d.drawImage(pacman3down, pacmanx + 1, pacmany + 1, this);
                break;
            case 3:
                g2d.drawImage(pacman4down, pacmanx + 1, pacmany + 1, this);
                break;
            default:
                g2d.drawImage(pacman1, pacmanx + 1, pacmany + 1, this);
                break;
        }
    }

    private void drawPacmanLeft(Graphics2D g2d) {

        switch (pacmananimpos) {
            case 1:
                g2d.drawImage(pacman2left, pacmanx + 1, pacmany + 1, this);
                break;
            case 2:
                g2d.drawImage(pacman3left, pacmanx + 1, pacmany + 1, this);
                break;
            case 3:
                g2d.drawImage(pacman4left, pacmanx + 1, pacmany + 1, this);
                break;
            default:
                g2d.drawImage(pacman1, pacmanx + 1, pacmany + 1, this);
                break;
        }
    }

    private void drawPacmanRight(Graphics2D g2d) {

        switch (pacmananimpos) {
            case 1:
                g2d.drawImage(pacman2right, pacmanx + 1, pacmany + 1, this);
                break;
            case 2:
                g2d.drawImage(pacman3right, pacmanx + 1, pacmany + 1, this);
                break;
            case 3:
                g2d.drawImage(pacman4right, pacmanx + 1, pacmany + 1, this);
                break;
            default:
                g2d.drawImage(pacman1, pacmanx + 1, pacmany + 1, this);
                break;
        }
    }

    private void drawMaze(Graphics2D g2d) {

        short i = 0;
        int x, y;

        for (y = 0; y < scrsize; y += blocksize) {
            for (x = 0; x < scrsize; x += blocksize) {

                g2d.setColor(mazecolor);
                g2d.setStroke(new BasicStroke(2));

                if ((screendata[i] & 1) != 0) { 
                    g2d.drawLine(x, y, x, y + blocksize - 1);
                }

                if ((screendata[i] & 2) != 0) { 
                    g2d.drawLine(x, y, x + blocksize - 1, y);
                }

                if ((screendata[i] & 4) != 0) { 
                    g2d.drawLine(x + blocksize - 1, y, x + blocksize - 1,
                            y + blocksize - 1);
                }

                if ((screendata[i] & 8) != 0) { 
                    g2d.drawLine(x, y + blocksize - 1, x + blocksize - 1,
                            y + blocksize - 1);
                }

                if ((screendata[i] & 16) != 0) { 
                    g2d.setColor(dotcolor);
                    g2d.fillRect(x + 11, y + 11, 2, 2);
                }

                i++;
            }
        }
    }

    private void initGame() {

        pacsleft = 3;
        score = 0;
        initLevel();
        nrofghosts = ghost_number;
        currentspeed = 4;
    }

    private void initLevel() {

        int i;
        for (i = 0; i < nrofblocks * nrofblocks; i++) {
            screendata[i] = leveldata[i];
        }

        continueLevel();
    }

    private void continueLevel() {

        short i;
        int dx = 1;
        int random;
        int q2,q3;
        if(ingame==false)
        	pacmanauto=0;
        Random r = new Random(System.currentTimeMillis());
        int q=r.nextInt(12)+1;
        r = new Random(System.currentTimeMillis());
        int q1=r.nextInt(14);
        while(q>=1&&q<=3&&q1>=1&&q1<=3||
        		q==0&&q1>=7&&q1<=15||
        		q1==15&&q>=1&&q<=9||
        		q==14&&q1>=6&&q1<=13||
        		q==7&&q1>=4&&q1<=11||
        		q1==7&&q>=4&&q<=11)
        {
        	r = new Random(System.currentTimeMillis());
            q=r.nextInt(12)+1;
            r = new Random(System.currentTimeMillis());
            q1=r.nextInt(14);
        }
        for (i = 0; i < nrofghosts; i++) {
        	
        	q2=(int) (Math.random()*(12)+1);
        	q3=(int) (Math.random()*14);
        	
            while((q2>=1&&q2<=3&&q3>=1&&q3<=3)||
            		((q2==0&&q3>=7&&q3<=15)&&
            		(q3==15&&q2>=1&&q2<=9))||
            		(q2==14&&q3>=6&&q3<=13)||
            		(q2==7&&q3>=4&&q3<=11)||
            		(q3==7&&q2>=4&&q2<=11)||
            		(q2>=q-2&&q2<=q+2&&q3>=q1-2&&q3<=q1+2)
            		||(q2<0||q2>15||q3<0||q3>15))
            {
            	
            	q2=(int) (Math.random()*(12)+1);
            	q3=(int) (Math.random()*14);
            	
            }
        	
            ghosty[i] = q3 * blocksize;
            ghostx[i] = q2 * blocksize;
            ghostdy[i] = 0;
            ghostdx[i] = dx;
            dx = -dx;
            random = (int) (Math.random() * (currentspeed + 1));

            while(random>currentspeed)
            {
                random = (int) (Math.random() * (currentspeed + 1));
            }

            ghostspeed[i] = validspeeds[random];
        }
        pacmanx = q * blocksize;
        pacmany = q1 * blocksize;
        pacmandx = 0;
        pacmandy = 0;
        reqdx = 0;
        reqdy = 0;
        viewdx = -1;
        viewdy = 0;
        dying = false;
    }

    public void CreateSave() 
    {
	    String filePath = getFilePath("save");
	    System.out.println("Test file here: "+filePath);
	    File file = new File( filePath );
	    try
		{
		    if(change==1)
		    {
		    	Saving(file);
		    	change=0;
		    }
		    else if(change==2)
		    {
		    	Loading(file);
		    	change=0;
		    }
		}catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public String getFilePath(String fileName)
    {
        String baseDir = System.getProperty("user.dir");
      //  String baseDir = System.getProperty("user.home");
        String filePath = baseDir + "\\"+fileName;
        return filePath;
    }
    
    public void Saving(File file) throws FileNotFoundException
    {
 
        PrintWriter writer = new PrintWriter(file);
        writer.write(pacmanx);
        writer.write('_');
        writer.write(pacmany);
        writer.write('_');
        for(int i = 0;i < nrofghosts; i++)
        {
        	writer.write(ghostx[i]);
        	writer.write('_');
        	writer.write(ghosty[i]);
        	writer.write('_');
        }
        writer.close();
    }
    
    public void Loading(File file) throws FileNotFoundException
    {
        Scanner reader = new Scanner(file);
        reader.useDelimiter("\\n");
        StringBuilder data = new StringBuilder();
        while( reader.hasNext() ) data.append(reader.next()+"\n");
        reader.close();
        data.toString();
        pacmanx=0;
        pacmany=0;
        for(int k=0;k<nrofghosts;k++)
        {
        	ghostx[k]=0;
        	ghosty[k]=0;
        }
        int i=0,j=0,t=0;
        char c;
        while(data.charAt(i)!='_')
        {
        	while(data.charAt(j)!='_')
        	{
        		j++;
        	}
        	t=j;
        	c=data.charAt(i);
        	pacmanx+=Character.getNumericValue(c)*10^t;
        	t--;
        	i++;
        }
        i++;
        j++;
        while(data.charAt(i)!='_')
        {
        	int t1=j;
        	while(data.charAt(j)!='_')
        	{
        		j++;
        	}
        	t=j-t1;
        	c=data.charAt(i);
        	pacmany+=Character.getNumericValue(c)*10^t;
        	t--;
        }
        i++;
        j++;
        for(int k=0;k<nrofghosts;k++)
        {
        	int t1=j;
        	while(data.charAt(j)!='_')
        	{
        		j++;
        	}
        	t=j-t1;
        	c=data.charAt(i);
        	ghostx[k]+=Character.getNumericValue(c)*10^t;
        	t--;
        	j++;
        	t1=j;
        	while(data.charAt(j)!='_')
        	{
        		j++;
        	}
        	t=j-t1;
        	c=data.charAt(i);
        	ghosty[k]+=Character.getNumericValue(c)*10^t;
        	t--;
        	j++;
        }
        continueLevel();
    }
    
    private void loadImages() {

        ghost = new ImageIcon("images/ghost.png").getImage();
        pacman1 = new ImageIcon("images/pacman.png").getImage();
        pacman2up = new ImageIcon("images/up1.png").getImage();
        pacman3up = new ImageIcon("images/up2.png").getImage();
        pacman4up = new ImageIcon("images/up3.png").getImage();
        pacman2down = new ImageIcon("images/down1.png").getImage();
        pacman3down = new ImageIcon("images/down2.png").getImage();
        pacman4down = new ImageIcon("images/down3.png").getImage();
        pacman2left = new ImageIcon("images/left1.png").getImage();
        pacman3left = new ImageIcon("images/left2.png").getImage();
        pacman4left = new ImageIcon("images/left3.png").getImage();
        pacman2right = new ImageIcon("images/right1.png").getImage();
        pacman3right = new ImageIcon("images/right2.png").getImage();
        pacman4right = new ImageIcon("images/right3.png").getImage();

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }

    private void doDrawing(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.black);
        g2d.fillRect(0, 0, d.width, d.height);

        drawMaze(g2d);
        drawScore(g2d);
        doAnim();

        if (ingame) {
            playGame(g2d);
        } else {
            showIntroScreen(g2d);
        }

        g2d.drawImage(ii, 5, 5, this);
        Toolkit.getDefaultToolkit().sync();
        g2d.dispose();
    }

    class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if (ingame) {
                if (key == KeyEvent.VK_LEFT) {
                    reqdx = -1;
                    reqdy = 0;
                } else if (key == KeyEvent.VK_RIGHT) {
                    reqdx = 1;
                    reqdy = 0;
                } else if (key == KeyEvent.VK_UP) {
                    reqdx = 0;
                    reqdy = -1;
                } else if (key == KeyEvent.VK_DOWN) {
                    reqdx = 0;
                    reqdy = 1;
                } else if(key =='h' ||key=='H'){
                	pacsleft++;
                }
                //////////////////////////
                else if(key == KeyEvent.VK_F5){
                	//Saving(file);
                	change=1;
                	CreateSave();
                } else if(key == KeyEvent.VK_F9){
                	//Loading(file);
                	change=2;
                	CreateSave();
                }
                
                //////////////////////////
                else if (key == KeyEvent.VK_ESCAPE && timer.isRunning()) {
                    ingame = false;
                    pacmanauto=0;
                } else if (key == KeyEvent.VK_PAUSE) {
                    if (timer.isRunning()) {
                        timer.stop();
                    } else {
                        timer.start();
                    }
                }
            } else {
            	if (key == 'r' || key == 'R' ){
                	if(r<=155)
                		Board.r=Board.r+100;
                	else
                		Board.r=255;
            	}
            	if (key == 'z' || key == 'Z' ){
                	if(r>=100)
                	{
                		Board.r=Board.r-100;
                	}
                	else
                		Board.r=0;
            	}            	
            	if (key == 'g' || key == 'G'){
            		if(g<=155)
            			Board.g=Board.g+100;	
            		else
            			Board.g=255;
            	}
            	if (key == 'x' || key == 'X'){
                	if(g>=100)
                	{
                		Board.g=Board.g-100;
                	}
                	else
                		Board.g=0;
            	}     
            	if (key == 'b' || key == 'B'){
            		if(b<=155)
            			Board.b=Board.b+100;
            		else
            			Board.b=255;
            	}
            	if (key == 'c' || key == 'C'){
                	if(b>=100)
                	{
                		Board.b=Board.b-100;
                	}
                	else
                		Board.b=0;
            	}     
            	if (key == 'a' || key == 'A'){
            		Board.pacmanauto=1;
            	}
            	if (key == 'e' || key == 'E'){
            		Board.ghost_number=4;
            	}
            	if (key == 'n' || key == 'N'){
            		Board.ghost_number=6;
            	}
            	if (key == 'h' || key == 'H'){
            		Board.ghost_number=8;
            	}
                if (key == 's' || key == 'S') {
                    ingame = true;
                    initGame();
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {

            int key = e.getKeyCode();

            if (key == Event.LEFT || key == Event.RIGHT
                    || key == Event.UP || key == Event.DOWN) {
                reqdx = 0;
                reqdy = 0;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        repaint();
    }
}
