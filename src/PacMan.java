import java.awt.*;//import java Abstract Window Toolkit
import java.awt.event.*;//import events
import java.util.HashSet;//import hashset
import java.util.Random;
import javax.swing.*;

public class PacMan extends JPanel implements ActionListener, KeyListener
{
    class Block
    {
        int x;
        int y;
        int width;
        int height;
        Image image;

        int startX;
        int startY;
        char direction='U';
        int velocityX=0;
        int velocityY=0;

        Block(Image image, int x, int y, int width, int height)
        {
            this.image=image;
            this.x=x;
            this.y=y;
            this.width=width;
            this.height=height;
            this.startX=x;
            this.startY=y;
        }

        void updateDirection(char direction)
        {
            char prevDirection = this.direction;
            this.direction = direction;
            updateVelocity();
            this.x += this.velocityX;
            this.y += this.velocityY;
            for (Block wall : walls)
            {
                if (collision(this, wall))
                {
                    this.x -= this.velocityX;
                    this.y -= this.velocityY;
                    this.direction = prevDirection;
                    updateVelocity();
                }
            }
        }

        void updateVelocity()
        {
            if (this.direction == 'U')//if direction is upwards
            {
                this.velocityX = 0;
                this.velocityY = -tilesize/4;
            }

            else if (this.direction == 'D')//if direction is downwards
            {
                this.velocityX = 0;
                this.velocityY = tilesize/4;
            }

            else if (this.direction == 'L')//if direction is left
            {
                this.velocityX = -tilesize/4;
                this.velocityY = 0;
            }

            else if (this.direction == 'R')//if direction is right
            {
                this.velocityX = tilesize/4;
                this.velocityY = 0;
            }
        }
        void reset()//to reset sprites to initial position
        {
            this.x = this.startX;
            this.y = this.startY;
        }
    }
    private int rowcount=21;//number of rows
    private int coloumncount=19;//number of coloumns
	private int tilesize=32;//size of tiles
	private int boardwidth=coloumncount*tilesize;
    private int boardheight=rowcount*tilesize;

    private Image wallImage;
    private Image blueGhost;
    private Image orangeGhost;
    private Image pinkGhost;
    private Image redGhost;

    private Image pacmanUp;
    private Image pacmanDown;
    private Image pacmanLeft;
    private Image pacmanRight;

    //X = wall, O = skip, P = pac man, ' ' = food
    //Ghosts: b = blue, o = orange, p = pink, r = red
    private String[] tileMap = {
        "XXXXXXXXXXXXXXXXXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X                 X",
        "X XX X XXXXX X XX X",
        "X    X       X    X",
        "XXXX XXXX XXXX XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXrXX X XXXX",
        "O       bpo       O",
        "XXXX X XXXXX X XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXXXX X XXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X  X     P     X  X",
        "XX X X XXXXX X X XX",
        "X    X   X   X    X",
        "X XXXXXX X XXXXXX X",
        "X                 X",
        "XXXXXXXXXXXXXXXXXXX" 
    };

    HashSet<Block> walls;
    HashSet<Block> foods;
    HashSet<Block> ghosts;
    Block pacman;

    Timer gameLoop;
    char[] directions = {'U','D','L','R'};//array of directions of ghosts' movement
    Random random = new Random();//instantiating Random class
    int score = 0,lives = 3;
    boolean gameOver=false;

    PacMan()
    {
        setPreferredSize(new Dimension(boardwidth,boardheight));
        setBackground(Color.BLACK);//set background colour to black
        addKeyListener(this);//listens for key presses
        setFocusable(true);//only JPanel can listen to key presses

        //load images
        wallImage = new ImageIcon(getClass().getResource("./wall.png")).getImage();

        blueGhost = new ImageIcon(getClass().getResource("./blueGhost.png")).getImage();
        redGhost = new ImageIcon(getClass().getResource("./redGhost.png")).getImage();
        pinkGhost = new ImageIcon(getClass().getResource("./pinkGhost.png")).getImage();
        orangeGhost = new ImageIcon(getClass().getResource("./orangeGhost.png")).getImage();

        pacmanDown = new ImageIcon(getClass().getResource("./pacmanDown.png")).getImage();
        pacmanLeft = new ImageIcon(getClass().getResource("./pacmanLeft.png")).getImage();
        pacmanRight = new ImageIcon(getClass().getResource("./pacmanRight.png")).getImage();
        pacmanUp = new ImageIcon(getClass().getResource("./pacmanUp.png")).getImage();

        loadMap();
        for (Block ghost : ghosts)
        {
            char newDirection = directions[random.nextInt(4)];//choose random direction of movement for ghosts
            ghost.updateDirection(newDirection);//update direction for ghosts
        }
        gameLoop = new Timer(50, this);//renders PacMan object at 50ms delay (game runs at 20fps => 1000ms/50ms delay=20fps)
        gameLoop.start();//start timer
    }

    public void loadMap()
    {
        walls = new HashSet<Block>();
        foods = new HashSet<Block>();
        ghosts = new HashSet<Block>();

        for (int r=0; r<rowcount; r++)
        {
            for (int c=0; c<coloumncount; c++)
            {
                String row=tileMap[r];
                char tileMapChar = row.charAt(c);

                int x = c*tilesize;
                int y = r*tilesize;

                if (tileMapChar=='X')//creating walls
                {
                    Block wall = new Block(wallImage, x, y, tilesize, tilesize);
                    walls.add(wall);
                }

                else if (tileMapChar=='b')//creating blue ghost
                {
                    Block ghost = new Block(blueGhost, x, y, tilesize, tilesize);
                    ghosts.add(ghost);
                }
                
                else if (tileMapChar=='o')//creating orange ghost
                {
                    Block ghost = new Block(orangeGhost, x, y, tilesize, tilesize);
                    ghosts.add(ghost);
                }

                else if (tileMapChar=='p')//creating pink ghost
                {
                    Block ghost = new Block(pinkGhost, x, y, tilesize, tilesize);
                    ghosts.add(ghost);
                }

                else if (tileMapChar=='r')//creating red ghost
                {
                    Block ghost = new Block(redGhost, x, y, tilesize, tilesize);
                    ghosts.add(ghost);
                }

                else if (tileMapChar=='P')//creating pacman
                {
                    pacman = new Block(pacmanRight, x, y, tilesize, tilesize);//by default pacman faces right
                }

                else if (tileMapChar==' ')//creating food
                {
                    Block food = new Block(null, x+14, y+14, 4, 4);//since no food sprite exists
                    foods.add(food);
                }
            }
        }
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);//invokes paintComponent from JPanel
        draw(g);
    }

    public void draw(Graphics g)
    {
        g.drawImage(pacman.image, pacman.x, pacman.y, pacman.width, pacman.height, null);//draws pacman sprite

        for (Block ghost : ghosts)
        {
            g.drawImage(ghost.image, ghost.x, ghost.y, ghost.width, ghost.height, null);//draws ghost sprites
        }

        for (Block wall : walls)
        {
            g.drawImage(wall.image, wall.x, wall.y, wall.width, wall.height, null);//draws wall sprite
        }

        g.setColor(Color.WHITE);
        for (Block food : foods)//fills food rectangles
        {
            g.fillRect(food.x, food.y, food.width, food.height);
        }
        g.setFont(new Font("Arial", Font.PLAIN, 18));//font Arial, plain size 18
        if (gameOver)
        {
            g.drawString("GAME OVER: "+String.valueOf(score), tilesize/2, tilesize/2);//display game over message with score of size tilesize/2 * tilesize/2
        }
        else
        {
            g.drawString("x"+String.valueOf(lives)+"\nScore: "+String.valueOf(score), tilesize/2, tilesize/2);//display lives and score if not game over
        }
    }

    public void move()//movement
    {
        pacman.x += pacman.velocityX;
        pacman.y += pacman.velocityY;

        //checking for collisions
        for (Block wall : walls)
        {
            if (collision (pacman, wall))//if collision occurs
            {
                pacman.x -= pacman.velocityX;
                pacman.y -= pacman.velocityY;
                break;
            }
        }
        for (Block ghost : ghosts)//ghost movement
        {
            if (collision(ghost, pacman))//if pacman and ghost collide
            {
                lives -= 1;
                if (lives == 0)
                {
                    gameOver = true;
                    return;
                }
                resetPositions();
            }
            if (ghost.y == tilesize*9 && ghost.direction != 'U' && ghost.direction != 'D')
                {
                    ghost.updateDirection('U');
                }
            ghost.x += ghost.velocityX;
            ghost.y += ghost.velocityY;
            for (Block wall : walls)
            {
                if (collision (ghost, wall) || ghost.x <= 0 || ghost.x + ghost.width >= boardwidth)//checks for collision of ghost with walls
                {
                    ghost.x -= ghost.velocityX;
                    ghost.y -= ghost.velocityY;
                    char newDirection = directions[random.nextInt(4)];//adds new direction post collision
                    ghost.updateDirection(newDirection);//moves in that direction
                }//add methods that ensure that when it reaches the board extremeties, they teleport to the other side
            }
        }
        Block foodEaten = null;
        for (Block food : foods)
        {
            if (collision(pacman, food))//if pacman sprite collides with food sprite
            {
                foodEaten = food;
                score += 10;//increase score by 10
            }
        }
        foods.remove(foodEaten);//remove eaten food
        if (foods.isEmpty())//if all food is eaten
        {
            loadMap();
            resetPositions();
        }
    }

     public static void main(String[] args)
     {
        PacMan ob = new PacMan();
        ob.loadMap();
     }

     @Override
     public void actionPerformed(ActionEvent e)//called every 50ms and repaints PacMan (game runs at 20fps => 1000/50=20)
     {
        move();
        repaint();
        if (gameOver)
        {
            gameLoop.stop();
        }
     }

     public boolean collision(Block a, Block b)
     {
        return a.x < b.x + b.width && a.x + a.width > b.x && a.y < b.y + b.height && a.y + a.height > b.y;
     }

     public void resetPositions()
     {
        pacman.reset();//reset pacman position to initial
        pacman.velocityX=0;
        pacman.velocityY=0;
        for (Block ghost : ghosts)
        {
            ghost.reset();
            char newDirection = directions[random.nextInt(4)];
            ghost.updateDirection(newDirection);
        }
     }

     @Override
     public void keyPressed(KeyEvent e) {}//not using as we need not hold keys down

     @Override
     public void keyReleased(KeyEvent e) 
     {
        if (gameOver)//if any key is released
        {
            loadMap();//reload map
            resetPositions();//reset positions
            lives = 3;//reset lives
            score = 0;//reset score
            gameOver = false;
            gameLoop.start();//restart game
        }
        //System.out.println("KeyEvent: "+e.getKeyCode());
        if (e.getKeyCode() == KeyEvent.VK_UP)
            pacman.updateDirection('U');//logs 'U' for up arrow key press
        else if (e.getKeyCode() == KeyEvent.VK_DOWN)
            pacman.updateDirection('D');//logs 'D' for down arrow key press
        else if (e.getKeyCode() == KeyEvent.VK_LEFT)
            pacman.updateDirection('L');//logs 'L' for left arrow key press
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
            pacman.updateDirection('R');//logs 'R' for right arrow key press

        if (pacman.direction == 'U')//changing pacman face direction
            pacman.image = pacmanUp;
        else if (pacman.direction == 'D')
            pacman.image = pacmanDown;
        else if (pacman.direction == 'L')
            pacman.image = pacmanLeft;
        else if (pacman.direction == 'R')
            pacman.image = pacmanRight;
     }

     @Override
     public void keyTyped(KeyEvent e) {}//not using as arrow kays have no letter associated with them
}
//add highscore, add pause, add it so that reaching sides teleports you to the opposite side, power pellets to eat ghosts, cherry with extra points, (advanced) make ghosts chase player