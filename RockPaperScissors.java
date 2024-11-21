import java.util.Random;
import java.util.Scanner;

public class RockPaperScissors {
    enum Move {
        ROCK, PAPER, SCISSORS;
        
        // Get a random move
        public static Move getRandomMove() {
            Random random = new Random();
            return values()[random.nextInt(values().length)];
        }
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int playerScore = 0;
        int computerScore = 0;
        
        System.out.println("Welcome to Rock, Paper, Scissors!");
        System.out.println("Enter 'quit' to end the game");
        
        // Main game loop
        while (true) {
            System.out.println("\nCurrent Score - You: " + playerScore + " Computer: " + computerScore);
            System.out.print("Enter your move (rock/paper/scissors): ");
            
            // Get player's move
            String playerInput = scanner.nextLine().toUpperCase();
            
            // Check if player wants to quit
            if (playerInput.equals("QUIT")) {
                break;
            }
            
            // Validate input
            Move playerMove;
            try {
                playerMove = Move.valueOf(playerInput);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid move! Please enter rock, paper, or scissors.");
                continue;
            }
            
            // Get computer's move
            Move computerMove = Move.getRandomMove();
            
            // Show moves
            System.out.println("Your move: " + playerMove);
            System.out.println("Computer's move: " + computerMove);
            
            // Determine winner
            if (playerMove == computerMove) {
                System.out.println("It's a tie!");
            } else if ((playerMove == Move.ROCK && computerMove == Move.SCISSORS) ||
                      (playerMove == Move.PAPER && computerMove == Move.ROCK) ||
                      (playerMove == Move.SCISSORS && computerMove == Move.PAPER)) {
                System.out.println("You win this round!");
                playerScore++;
            } else {
                System.out.println("Computer wins this round!");
                computerScore++;
            }
        }
        
        // Show final score
        System.out.println("\nGame Over!");
        System.out.println("Final Score - You: " + playerScore + " Computer: " + computerScore);
        if (playerScore > computerScore) {
            System.out.println("Congratulations! You won the game!");
        } else if (computerScore > playerScore) {
            System.out.println("Better luck next time! Computer wins!");
        } else {
            System.out.println("It's a tie game!");
        }
        
        scanner.close();
    }
}