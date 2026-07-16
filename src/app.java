import javax.swing.JFrame;//for window
public class app
{
	public static void main(String args[]) throws Exception
	{
		int rowcount=21,coloumncount=19;//number of rows and coloumns
		int tilesize=32;//size of tiles
		int boardwidth=coloumncount*tilesize,boardheight=rowcount*tilesize;
		
		JFrame frame = new JFrame("Pacman");//window title
		//frame.setVisible(true);//make frame visible
		frame.setSize(boardwidth, boardheight);//sets board size
		frame.setLocationRelativeTo(null);//puts frame in the centre of the screen
		frame.setResizable(false);//user cannot resize the window
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//closes windows on clicking close button	

		PacMan pacmanGame = new PacMan();
		frame.add(pacmanGame);//add JPanel to JFrame
		frame.pack();//get full board on screen
		pacmanGame.requestFocus();
		frame.setVisible(true);
	}
}
