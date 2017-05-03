package oldFiles;
import java.util.Scanner;

/* Author: Ethan Hanlon
 * Project Desc: IST 411 Group Project ~ Designing a simple battleship game.
 * Start Date: Feb 13, 2017
 */

/* ===== UPDATES =====
 * Desc:
 * Date:
 */

public class Battleship 
{

    private static int turnNum = 0;
    private static String turn = "Player 1";
    private static int playerShotX, playerShotY;
    private static boolean gameEnd = false;
     
    public static void main(String[] args) 
    {
        
        int[][] Board = new int[10][10];
        int[] Carrier = new int[5];
        int[] Battleship = new int[4];
        int[] Cruiser = new int[3];
        int[] Submarine = new int[3];
        int[] Destroyer = new int[2];
        
        Battleship player1 = new Battleship();
        Battleship player2 = new Battleship();
        
        player1.setBoard(Board);
        player1.setShips(Carrier, Battleship, Cruiser, Submarine, Destroyer);
        player1.placeShips(Carrier, Battleship, Cruiser, Submarine, Destroyer, Board);
        
        player2.setBoard(Board);
        player2.setShips(Carrier, Battleship, Cruiser, Submarine, Destroyer);
        player2.placeShips(Carrier, Battleship, Cruiser, Submarine, Destroyer, Board);
        
        while (gameEnd == false)
        {
            if(turn == "Player 1")
            {
                System.out.println(" ===Player 1=== ");
                player1.turnTimer();
                player1.shoot();
                player1.hitCheck(Board);
                
            }
                
            else
            {
                System.out.println(" ===Player 2=== ");
                player2.turnTimer();
                player2.shoot();
                player2.hitCheck(Board);
                
            }
               
        }
        
                
    }
    
    public static void setBoard(int[][] Board)
    {
        for(int row = 0; row < 10 ; row++ )
            for(int column=0 ; column < 10 ; column++ )
                Board[row][column]=-1;
    }
    
    public static void setShips(int[] Carrier, int[] Battleship, int[] Cruiser, 
            int[] Submarine, int[] Destroyer)
    {
        
        for(int i = 0 ; i < 5; i++)
            Carrier[i] = 0;
        for(int i = 0 ; i < 4; i++)
            Battleship[i] = 0;
        for(int i = 0 ; i < 3; i++)
            Cruiser[i] = 0;
        for(int i = 0 ; i < 3; i++)
            Submarine[i] = 0;
        for(int i = 0 ; i < 2; i++)
            Destroyer[i] = 0;
        
    }
    
    public static void placeShips(int[] Carrier, int[] Battleship, int[] Cruiser, 
            int[] Submarine, int[] Destroyer, int[][] Board)
    {
        int playerInputX, playerInputY;
        String playerInputDirection;
        Scanner playerIn = new Scanner(System.in);
        
        System.out.print(" ===Carrier Origen=== \n X = ");
        playerInputX = playerIn.nextInt();
        System.out.print(" Y = ");
        playerInputY = playerIn.nextInt();
        Board[playerInputX][playerInputY] = Carrier[0];
        
        System.out.println("Specify wether you want the Carrier to go to the top,"
                + " bottom, right, or left of the origen.");
        playerInputDirection = playerIn.next();
        switch (playerInputDirection) 
        {
            case "top": System.arraycopy(Carrier, 1, Board [playerInputX], playerInputY + 1, 4);
            case "bottom": for(int j = 1; j < 5; j++)
                           Board [playerInputX][playerInputY - j] = Carrier[j];
            case "left": for(int j = 1; j < 5; j++)
                           Board [playerInputX - j][playerInputY] = Carrier[j];
            case "right": for(int j = 1; j < 5; j++)
                           Board [playerInputX + j][playerInputY] = Carrier[j];
        }
        
        System.out.print(" ===Battleship Origen=== \n X = ");
        playerInputX = playerIn.nextInt();
        System.out.print(" Y = ");
        playerInputY = playerIn.nextInt();
        Battleship[0] = Board[playerInputX][playerInputY];
        
        System.out.println("Specify wether you want the Battleship to go to the top,"
                + " bottom, right, or left of the origen.");
        playerInputDirection = playerIn.next();
        switch (playerInputDirection) 
        {
            case "top": System.arraycopy(Battleship, 1, Board [playerInputX], playerInputY + 1, 3);
            case "bottom": for(int j = 1; j < 4; j++)
                           Board [playerInputX][playerInputY - j] = Battleship[j];
            case "left": for(int j = 1; j < 4; j++)
                           Board [playerInputX - j][playerInputY] = Battleship[j];
            case "right": for(int j = 1; j < 4; j++)
                           Board [playerInputX + j][playerInputY] = Battleship[j];
        }
        
        System.out.print(" ===Cruiser Origen=== \n X = ");
        playerInputX = playerIn.nextInt();
        System.out.print(" Y = ");
        playerInputY = playerIn.nextInt();
        Cruiser[0] = Board[playerInputX][playerInputY];
        
        System.out.println("Specify wether you want the Cruiser to go to the top,"
                + " bottom, right, or left of the origen.");
        playerInputDirection = playerIn.next();
        switch (playerInputDirection) 
        {
            case "top": System.arraycopy(Carrier, 1, Board [playerInputX], playerInputY + 1, 2);
            case "bottom": for(int j = 1; j < 3; j++)
                           Board [playerInputX][playerInputY - j] = Carrier[j];
            case "left": for(int j = 1; j < 3; j++)
                           Board [playerInputX - j][playerInputY] = Carrier[j];
            case "right": for(int j = 1; j < 3; j++)
                           Board [playerInputX + j][playerInputY] = Carrier[j];
        }
        
        System.out.print(" ===Submarine Origen=== \n X = ");
        playerInputX = playerIn.nextInt();
        System.out.print(" Y = ");
        playerInputY = playerIn.nextInt();
        Submarine[0] = Board[playerInputX][playerInputY];
        
        System.out.println("Specify wether you want the Submarine to go to the top,"
                + " bottom, right, or left of the origen.");
        playerInputDirection = playerIn.next();
        switch (playerInputDirection) 
        {
            case "top": System.arraycopy(Submarine, 1, Board [playerInputX], playerInputY + 1, 2);
            case "bottom": for(int j = 1; j < 3; j++)
                           Board [playerInputX][playerInputY - j] = Submarine[j];
            case "left": for(int j = 1; j < 3; j++)
                           Board [playerInputX - j][playerInputY] = Submarine[j];
            case "right": for(int j = 1; j < 3; j++)
                           Board [playerInputX + j][playerInputY] = Submarine[j];
        }
        
        System.out.print(" ===Destroyer Origen=== \n X = ");
        playerInputX = playerIn.nextInt();
        System.out.print(" Y = ");
        playerInputY = playerIn.nextInt();
        Destroyer[0] = Board[playerInputX][playerInputY];
        
        System.out.println("Specify wether you want the Destroyer to go to the top,"
                + " bottom, right, or left of the origen.");
        playerInputDirection = playerIn.next();
        switch (playerInputDirection) 
        {
            case "top": System.arraycopy(Destroyer, 1, Board [playerInputX], playerInputY + 1, 1);
            case "bottom": for(int j = 1; j < 2; j++)
                           Board [playerInputX][playerInputY - j] = Destroyer[j];
            case "left": for(int j = 1; j < 2; j++)
                           Board [playerInputX - j][playerInputY] = Destroyer[j];
            case "right": for(int j = 1; j < 2; j++)
                           Board [playerInputX + j][playerInputY] = Destroyer[j];
        }
    }
    
    public static void turnTimer()
    {
        
        turnNum++;
        if(turnNum % 2 == 0)
            turn = "Player 1";
        else
            turn = "Player 2";
        
    }
    
    public static void shoot()
    {
        
        Scanner playerShot = new Scanner(System.in);
        
        System.out.print(" ===Shot=== \n X = ");
        playerShotX = playerShot.nextInt();
        System.out.print(" Y = ");
        playerShotY = playerShot.nextInt();
        
    }
    
    public static void hitCheck(int[][] Board)
    {
        
        if(Board[playerShotX][playerShotY] == 0)
        {
            Board[playerShotX][playerShotY] = 1;
        }
        else if(Board[playerShotX][playerShotY] == 1)
        {
            System.out.println("You already struck there!\n");
        }
        else
            System.out.println("You missed!\n");
    }
    
    public static void gameEnder()
    {
        gameEnd = true;
    }
    
    public static void resetGame()
    {
    
    }
  
}
