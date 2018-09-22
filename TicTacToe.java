/*
This is a tic tac toe game. Moves are entered by players, alternating X and O. X always goes first.
Created by Ben Anker
(email banker@cnm.edu)
On Wednesday September 19 2018
For CSCI-2251
*/
import java.util.Scanner;
import java.util.Random;

public class TicTacToe
{
	private enum player	
	{
		X, O, EMPTY;
	}
	private enum gameState
	{
		WIN, DRAW, CONTINUE;
	}
	private player[][] board=new player[3][3];
	private gameState state;
	private int numchars=0;
	private boolean isAIX=false;
	public TicTacToe()
	{
		for(int i=0;i<3;i++)
			for(int j=0; j<3;j++)
				board[i][j]=player.EMPTY;
		state=gameState.CONTINUE;
	}
	public void setBoard(char value, int i, int j)
	{
		switch(value)
		{
			case 'X':
				board[i][j]=player.X;
				break;
			case 'O':
				board[i][j]=player.O;
				break;
			case 'E':
				board[i][j]=player.EMPTY;
				break;
		}
	}
	private gameState gameStatus(player[][] board)
	{
		player winner=getWinner(board);
		if(winner==player.X||winner==player.O)
			return gameState.WIN;
		boolean isDraw=true;
		for(int i=0;i<3;i++)
			for(int j=0; j<3;j++)
				if(board[i][j]==player.EMPTY)
					isDraw=false;
		if(isDraw)
			return gameState.DRAW;
		return gameState.CONTINUE;
	}
	private player getWinner(player[][] board)
	{
		for(int i=0;i<3;i++)
			if(board[i][0]==board[i][1] && board[i][0]==board[i][2] && board[i][0]!=player.EMPTY)
				return board[i][0];
		for(int i=0;i<3;i++)
			if(board[0][i]==board[1][i] && board[0][i]==board[2][i] && board[0][i]!=player.EMPTY)
				return board[0][i];
		if(board[0][0]==board[1][1] && board[0][0]==board[2][2] && board[0][0]!=player.EMPTY)
			return board[0][0];
		if(board[0][2]==board[1][1] && board[0][2]==board[2][0] && board[0][2]!=player.EMPTY)
			return board[2][0];
		return player.EMPTY;			
	}
	private void printStatus(Object p)
	{
		if(state==gameState.CONTINUE)
			System.out.printf("Player %s's turn.\n", p);
		else if(state==gameState.DRAW)
			System.out.println("It was a draw.");
		else if(state==gameState.WIN)
			System.out.printf("Player %s wins.\n", p);
	}
	private void printBoard(player[][] board)
	{
		for(int i=0; i<13;i++)
		{
			for(int j=0; j<13; j++)
			{
				if(i%4==0 && j%4!=0)
					System.out.printf("-");
				else if(j%4==0 && i%4!=0)
					System.out.printf("|");
				else if((j-2)%4==0 && (i-2)%4==0)
				{
					switch(board[i/4][j/4])
					{
						case X:
							System.out.printf("X");
							break;
						case O:
							System.out.printf("O");
							break;
						case EMPTY:
							System.out.printf(" ");
					}
				}
				else
					System.out.printf(" ");
			}
			System.out.println();		
		}
	}
	private boolean validMove(int row, int col)
	{
		if(row<0||row>2||col<0||col>2||board[row][col]!=player.EMPTY)
			return false;
		return true;
	}
	public void play()
	{
		Scanner s=new Scanner(System.in);
		for(int turn=0; state==gameState.CONTINUE; turn++, state=gameStatus(board))
		{
			printBoard(board);
			player p=player.values()[turn%2];
			printStatus(p);
			int row=-1;
			int col=-1;
			while(!validMove(row, col))
			{
				System.out.printf("Player %s: Enter row <0, 1 or 2>: ", p);
				row=s.nextInt();
				System.out.printf("Player %s: Enter col <0, 1 or 2>: ", p);
				col=s.nextInt();
			}
			board[row][col]=p;
		}
		printBoard(board);
		printStatus(getWinner(board));				
	}
	public void playAI(boolean isPlayerX)
	{
		isAIX=!isPlayerX;
		Scanner s=new Scanner(System.in);
		for(int turn=0; state==gameState.CONTINUE; turn++, state=gameStatus(board))
		{
			printBoard(board);
			player p=player.values()[turn%2];
			printStatus(p);
			int row=-1;
			int col=-1;
			while(!validMove(row, col))
			{
				if(isPlayerX^(turn%2==1))
				{
					System.out.printf("Player %s: Enter row <0, 1 or 2>: ", p);
					row=s.nextInt();
					System.out.printf("Player %s: Enter col <0, 1 or 2>: ", p);
					col=s.nextInt();
				}
				else
				{
					int[] bestspot=bestMove(board, 1);
					row=bestspot[0];
					col=bestspot[1];
				}
			}
			board[row][col]=p;
		}
		printBoard(board);
		printStatus(getWinner(board));
	}
	private player getSymbol(int p)
	{
		if((p==1&&isAIX)||(p==-1&&!isAIX))
			return player.X;
		return player.O;
	}
	private int[] bestMove(player[][] board, int playerNum)
	{
		//empty best
		int[] best=new int[3];
		if(playerNum==1)//AI
			//looking to maximize value
			best=new int[] {0,0,Integer.MIN_VALUE};	
		else
			//looking to minimize value
			best=new int[] {0,0,Integer.MAX_VALUE};
		if(gameStatus(board)!=gameState.CONTINUE)
		{
			//return a score
			return new int[] {0,0,score(board)};
		}
		for(int i=0;i<3;i++)
		{
			for(int j=0;j<3;j++)
			{
				//for each empty space
				if(board[i][j]==player.EMPTY)
				{
					//set it to the current symbol
					board[i][j]=getSymbol(playerNum);
					//get the score for that configuration
					int[] s=bestMove(board, -1*playerNum);
					//reset the board state
					board[i][j]=player.EMPTY;
					//if we're looking to maximize
					if(playerNum==1)
					{
						if(s[2]>best[2])
						{
							best[0]=i;
							best[1]=j;
							best[2]=s[2];
						}
					}
					//if we're looking to minimize
					if(playerNum==-1)
					{
						if(s[2]<best[2])
						{
							best[0]=i;	
							best[1]=j;
							best[2]=s[2];
						}
					}
				}
			}
		}
		return best;			
	}
	private int score(player[][] board)
	{
		//Random r=new Random();
		//return r.nextInt(100)-50;
		player v=getWinner(board);
		if(v==player.X&&isAIX)
			return 10;
		if(v==player.O&&!isAIX)
			return 10;
		if(v==player.X&&!isAIX)
			return -10;
		if(v==player.O&&isAIX)
			return -10;
		return 0;
	}
}
