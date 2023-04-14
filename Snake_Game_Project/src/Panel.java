import javax.swing.*;
import java.awt.*;
import java.awt.Graphics;
import java.awt.event.*;
import java.util.Random;
import java.util.Arrays;

public class Panel extends JPanel implements ActionListener {
    static int width = 1200;
    static int height = 800;
    static int unit = 50;
    boolean flag = false;
    int score = 0;
    Timer timer;
    Random random;
    int fx, fy;
    int length = 3;

    char dir = 'R';
    int[] xsnake = new int[288];
    int[] ysnake = new int[288];


    Panel() {
        this.setPreferredSize(new Dimension(width,height));
        this.setBackground(Color.black);
        this.addKeyListener(new key()); // this takes input from keyboard
        this.setFocusable(true);
        random = new Random();
        gamestart();
    }
    public void gamestart() { //at start of the game
        spawnfood();
        flag = true;
        timer = new Timer(160,this);
        timer.start();
    }

    public void spawnfood() { // food for snake
        fx = random.nextInt(width/unit)*unit;
        fy = random.nextInt(height/unit)*unit;
    }
    public void paintComponent(Graphics graphic) {
        super.paintComponent(graphic);
        draw(graphic);
    }
    public void draw(Graphics graphic) {
        if (flag) {
            graphic.setColor(Color.red);
            graphic.fillOval(fx, fy, unit, unit);

            for (int i=0;i<length;i++) {
                graphic.setColor(Color.green);
                graphic.fillRect(xsnake[i], ysnake[i], unit, unit);
            }

        }
        else {
            gameover(graphic);
        }
    }
    public void gameover(Graphics graphic) {
        //final score
        graphic.setColor(Color.white);
        graphic.setFont(new Font("Comic sans", Font.BOLD, 30));
        FontMetrics fme = getFontMetrics(graphic.getFont());
        graphic.drawString("Score:" + score, (width - fme.stringWidth("Score" + score)) / 2, graphic.getFont().getSize());

        //gameover text
        graphic.setColor(Color.red);
        graphic.setFont(new Font("Comic sans", Font.BOLD, 60));
        FontMetrics fme1 = getFontMetrics(graphic.getFont());
        graphic.drawString("GAME OVER", (width - fme1.stringWidth("GAME OVER")) / 2, height/2);

        // to display the replay button
        graphic.setColor(Color.green);
        graphic.setFont(new Font("Comic sans", Font.BOLD, 30));
        FontMetrics fme2 = getFontMetrics(graphic.getFont());
        graphic.drawString("Press R to replay", (width - fme2.stringWidth("Press R to replay")) / 2,3*height/4);

    }
    public void eat() { // when snake eats it's length increase by 1 unit
        if ((fx==xsnake[0] && fy==ysnake[0])) {
            length++;
            score++;
            spawnfood();
        }
    }
    public void hit() {
        for (int i=length-1;i>0;i--) { // to check hit with its own body
            if (xsnake[0]==xsnake[i] && (ysnake[0]==ysnake[i])) {
                flag = false;
            }
        }
        if (xsnake[0]<0 || xsnake[0]>width || ysnake[0]<0 || ysnake[0]>height) {
            flag = false;
        }
        if (flag==false) {
            timer.stop();
        }
    }
    public void move() {
        // updating the coordinates of the body except the head
        for (int i=length-1;i>0;i--) {
            xsnake[i] = xsnake[i-1];
            ysnake[i] = ysnake[i-1];
        }

        switch(dir) { // to update head
            case 'U':
                ysnake[0] = ysnake[0] - unit;
                break;
            case 'D':
                ysnake[0] = ysnake[0] + unit;
                break;
            case 'R':
                xsnake[0] = xsnake[0] + unit;
                break;
            case 'L':
                xsnake[0] = xsnake[0] - unit;
                break;
        }
    }

    public class key extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {
            switch(e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    if (dir!='D') {
                        dir = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (dir!='U') {
                        dir = 'D';
                    }
                    break;
                case KeyEvent.VK_LEFT:
                    if (dir!='R') {
                        dir = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (dir!='L') {
                        dir = 'R';
                    }
                    break;
                case KeyEvent.VK_R:
                    if (flag==false) {
                        score = 0;
                        length = 3;
                        dir = 'R';
                        Arrays.fill(xsnake, 0);
                        Arrays.fill(ysnake, 0);
                        gamestart();
                    }
                    break;
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (flag) {
            move();
            hit();
            eat();
        }
        repaint();
    }
}
