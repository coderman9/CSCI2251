import java.util.Random;
public class TicTacToeTest
{
	public static void main(String[] args)
	{
		TicTacToe b=new TicTacToe();
		if(args.length>0 && args[0].equals("AI"))
		{
			Random r = new Random();
			b.playAI(r.nextBoolean());
		}
		else
			b.play();
	}
}
