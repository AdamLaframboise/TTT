//TicTacToe class is a class for main
class TicTacToe
{
    public static void main(final String[] array) {
        new game().start();
    }
}

abstract class player
{
    //Attributes
    public int playerSymbol;
    public board gameBoard;
    public String playerName;
    
    public abstract void play(final board p0);
    
    public player(final board gameBoard, final int playerSymbol, final String playerName) {
        //Sets up game board player symbol and player name
        this.gameBoard = gameBoard;
        this.playerSymbol = playerSymbol;
        this.playerName = playerName;
    }
    
    @Override
    //to string for player name
    public String toString() {
        return this.playerName;
    }
}
 
import java.util.Scanner;

//Human player class extends abstract class player
class HumanPlayer extends player
{
    public HumanPlayer(final board board, final int n, final String s) {
        super(board, n, s);
    }
    
    @Override
    public void play(final board gameBoard) {
        final Scanner scanner = new Scanner(System.in);
        super.gameBoard = gameBoard;
        boolean move;
        do {
            //Gets the move selection from the user
            System.out.println("Narrator: Select an available number from the list to move there: ");
            gameBoard.displayPlayerSelectionBoard();
            move = gameBoard.makeMove(scanner.nextInt(), this.playerSymbol);
            if (!move) {
                //Prints out invalid move if the move is not valid
                System.out.println("Narrator: Invalid move attempted, please try again");
            }
        }
        while (!move);
    }
}
 
interface global
    //Global interface that maintains some shared constants
{
    public static final int EMPTY = 0;
    public static final int X = 1;
    public static final int O = 2;
    public static final int DRAW = 3;
    public static final boolean DEBUG = false;
}
 
import java.util.Random;
import java.util.Scanner;


class game implements global
{
    //Attributes
    board gameBoard;
    player playerX;
    player playerO;
    int turn;
    boolean done;
    
    public game()
    {
      //Game function contains a game board and two players, playerX and playerO. It manages the iteration with the players.
      //First by getting the player names, does a coin toss to decide the turn and allows each player to take a turn and make a move.
      //It finally announces the winner/draw when the game ends.
        this.done = false;
        final Scanner scanner = new Scanner(System.in);
        this.gameBoard = new board();
        String p1 = "player1";
        String p2 = "player2";
        int nextInt = 0;

        while (!this.done) {
            System.out.println("Narrator: Welcome to TicTacToe");
            //Asks the user to select a game option then stores the option chosen
            System.out.println("Narrator: Select a game option that you would like to play: [1] Human vs Human [2] Human vs AI");
            nextInt = scanner.nextInt();
            switch (nextInt)
            {
                case 1: {
                    //For game option one Human vs Human
                    //Asks and stores the human player 1's name
                    System.out.print("Narrator: Please enter human player X name: ");
                    p1 = scanner.next().trim();
                    //Asks and stores the human player 2's name
                    System.out.print("Narrator: Please enter human player O name: ");
                    p2 = scanner.next().trim();
                    this.done = true;
                    continue;
                }
                case 2: {
                    //For game option two Human vs AI
                    //Asks and stores the human players name
                    System.out.print("Narrator: Please enter human player X name: ");
                    p1 = scanner.next().trim();
                    p2 = "AI BOT:"; //Sets the AI name called AI Bot
                    this.done = true;
                    continue;
                }

                default: {
                    System.out.println("Narrator: Please enter a valid move");
                    continue;
                }
            }
        }
        if (nextInt == 1)
        {
            //Human vs Human
            this.playerX = new HumanPlayer(this.gameBoard, 1, p1);
            this.playerO = new HumanPlayer(this.gameBoard, 2, p2);
        }
        else if (nextInt == 2)
        {
            //Human vs AI
            this.playerX = new HumanPlayer(this.gameBoard, 1, p1);
            this.playerO = new AIPlayer(this.gameBoard, 2, p2);
        }

        System.out.println("Narrator: Game initiated: " + p1 + " VS. " + p2 + "\n");
    }
    
    public void start()
    {
        final Random random = new Random();
        int i = 0;
        this.turn = random.nextInt(2) + 1;  //Random number for coin toss
        if (this.turn == 1)
        {
            //If 1, player X wins the coin toss and will start
            System.out.println("Narrator: " + this.playerX.playerName + " wins the coin toss " + this.playerX.playerName + " will start\n");
        }
        else
        {
            //else player O wins coin toss and will start
            System.out.println("Narrator: " + this.playerO.playerName + " wins the coin toss "+ this.playerO.playerName +" will start\n");
        }
        while (i == 0)
        {
            //Switches turns between player X and player O
            if (this.turn == 1)
            {
                System.out.println("Narrator: Player X's Turn, go ahead " + this.playerX.playerName + ":");
                this.playerX.play(this.gameBoard);
            }
            else
            {
                System.out.println("Narrator: Player O's Turn, go ahead " + this.playerO.playerName + ":");
                this.playerO.play(this.gameBoard);
            }
            if (this.gameBoard.getState() != 0)
            {
                i = 1;
            }
            if (i == 0)
            {
                if (this.turn == 1)
                {
                    this.turn = 2;
                }
                else
                {
                    this.turn = 1;
                }
            }
        }
        this.gameBoard.displayBoard();
        if (this.gameBoard.getState() == 3)
        {
            //If the game ends in a draw
            System.out.println("Narrator: It's a DRAW!");
        }
        else if (this.turn == 1)
        {
            //If the winner is player X
            System.out.println("Narrator: The winner is " + this.playerX);
        }
        else
        {
            //If the winner is player O
            System.out.println("Narrator: The winner is " + this.playerO);
        }
    }
}
 
class board implements global
{
    //board contains 9 blocks in a 2D array (3x3) that shapes the game space of tic-tac-toe.
    // It also maintains an internal state which can be one of the following: EMPTY (the initial state of the board), X (X claims a win), Y (Y claims a win), or DRAW (where neither X nor Y can claim a win and no further moves are possible).
    // The board has the key methods makeMove( ) that is called by a player making a move and getState() or updateState() that updates the state of the board after every move.
    // It checks for a win or a draw when they occur.
    public block[][] blocks;
    private int state;
    
    public board() {
        this.state = 0;
        this.blocks = new block[3][3];
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                this.blocks[i][j] = new block();
            }
        }
    }
    
    public void displayBoard() {
        //Function for the display board
        System.out.print("" + this.blocks[0][0] + "|" + this.blocks[0][1] + "|" + this.blocks[0][2] + "\n");
        System.out.print("" + this.blocks[1][0] + "|" + this.blocks[1][1] + "|" + this.blocks[1][2] + "\n");
        System.out.print("" + this.blocks[2][0] + "|" + this.blocks[2][1] + "|" + this.blocks[2][2] + "\n");
    }
    
    public void displayPlayerSelectionBoard() {
        int i = 1;
        for (int j = 0; j < 3; ++j) {
            for (int k = 0; k < 3; ++k) {
                System.out.print((this.blocks[j][k].getState() == 0) ? Integer.valueOf(i) : this.blocks[j][k]);
                ++i;
                if (k != 2) {
                    System.out.print("|");
                }
            }
            System.out.print("\n");
        }
    }
    
    public int getState()
     // Get method for state
    {
        return this.updateState();
    }
    
    public int updateState()
    {
        //Checks rows columns and diagonals for state (win/lose)
        final int state = this.checkRow(0) + this.checkRow(1) + this.checkRow(2) + this.checkCol(0) + this.checkCol(1) + this.checkCol(2) + this.checkDiagonals();
        if (state != 0) {
            this.state = state;
        }
        else {
            //Checks the state for a draw
            this.state = this.checkDraw();
        }
        return this.state;
    }
    
    private int checkDraw()
    {
        //Checks the state if there is a draw
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                if (this.blocks[i][j].getState() == 0) {
                    return 0;
                }
            }
        }
        return 3;
    }
    
    private int checkRow(final int n)
    {
        //Checks rows
        if (this.blocks[n][0].getState() != 0 && this.blocks[n][0].getState() == this.blocks[n][1].getState() && this.blocks[n][1].getState() == this.blocks[n][2].getState()) {
            return this.blocks[n][0].getState();
        }
        return 0;
    }
    
    private int checkCol(final int n)
    {
        //Checks columns
        if (this.blocks[0][n].getState() != 0 && this.blocks[0][n].getState() == this.blocks[1][n].getState() && this.blocks[1][n].getState() == this.blocks[2][n].getState()) {
            return this.blocks[0][n].getState();
        }
        return 0;
    }
    
    private int checkDiagonals()
    {
        //Checks diagonals
        if (this.blocks[0][0].getState() != 0 && this.blocks[0][0].getState() == this.blocks[1][1].getState() && this.blocks[1][1].getState() == this.blocks[2][2].getState()) {
            return this.blocks[0][0].getState();
        }
        if (this.blocks[0][2].getState() != 0 && this.blocks[0][2].getState() == this.blocks[1][1].getState() && this.blocks[1][1].getState() == this.blocks[2][0].getState()) {
            return this.blocks[0][2].getState();
        }
        return 0;
    }
    
    public boolean makeMove(final int n, final int state)
    {
        //Initializes the game board with corresponding values
        //If those values are entered the user will make a move to that spot
        int n2 = 0;
        int n3 = 0;
        if (n < 1 || n > 9) {
            return false;
        }
        if (n == 1) //Location for space 1
        {
            n2 = 0;
            n3 = 0;
        }
        if (n == 2) //Location for space 2
        {
            n2 = 0;
            n3 = 1;
        }
        if (n == 3) //Location for space 3
        {
            n2 = 0;
            n3 = 2;
        }
        if (n == 4) //Location for space 4
        {
            n2 = 1;
            n3 = 0;
        }
        if (n == 5) //Location for space 5
        {
            n2 = 1;
            n3 = 1;
        }
        if (n == 6) //Location for space 6
        {
            n2 = 1;
            n3 = 2;
        }
        if (n == 7) //Location for space 7
        {
            n2 = 2;
            n3 = 0;
        }
        if (n == 8) //Location for space 8
        {
            n2 = 2;
            n3 = 1;
        }
        if (n == 9) //Location for space 9
        {
            n2 = 2;
            n3 = 2;
        }
        if (this.blocks[n2][n3].getState() == 0) {
            this.blocks[n2][n3].setState(state);
            return true;
        }
        return false;
    }
}

class block implements global
{
    // A block represents a playable cell and maintains a state attribute that is either EMPTY, occupied by X, or occupied by O)
    // By default it is EMPTY, would require setState, getState, and a toString where EMPTY is just a blank space
    private int state;
    
    public block()
    {

        this.state = 0;
    }
    
    public boolean setState(final int state)
    {
        //Set method for state
        if (this.isValidState(state))
        {
            this.state = state;
            return true;
        }
        return false;
    }
    
    public int getState()
            //get method for state
    {
        return this.state;
    }
    
    public boolean isValidState(final int n)
            //Checks if the state is valid, when n is equal to either 1 or 2
    {
        return n == 2 || n == 1;
    }
    
    @Override
    public String toString()
    {
        //toString returns either nothing X or O
        if (this.state == 0)
        {
            return " ";
        }
        if (this.state == 1)
        {
            return "x";
        }
        return "o";
    }
}
import java.util.Random;

public class AIPlayer extends player implements global
{
    //AIPlayer extends player, It works by playing a valid random move
    public AIPlayer(final board board, final int n, final String s)
    {
        super(board, n, s);
    }

    private int randomLegal()
    {
        //Random moves that are within the playable game board area
        int bound = 0;
        final int[] array = new int[9];
        final Random random = new Random();
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                if (this.gameBoard.blocks[i][j].getState() == 0) {
                    array[bound++] = 3 * i + 1 + j;
                }
            }
        }
        return array[random.nextInt(bound)];
    }

    @Override
    public void play(final board board)
    {
        //Makes the AI Bot play a valid random move
        final int randomLegal = this.randomLegal();
        board.makeMove(randomLegal, this.playerSymbol);
        System.out.println(this.playerName + " Playing move at " + randomLegal);
    }
    

}
