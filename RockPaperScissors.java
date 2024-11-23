import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class RockPaperScissors {
    enum Move {
        ROCK("✊"), PAPER("✋"), SCISSORS("✌️");
        
        private final String symbol;
        
        Move(String symbol) {
            this.symbol = symbol;
        }
        
        public String getSymbol() {
            return symbol;
        }
        
        public static Move getRandomMove() {
            Random random = new Random();
            return values()[random.nextInt(values().length)];
        }
    }
    
    static class GameHistory {
        String playerMove;
        String computerMove;
        String result;
        
        GameHistory(String playerMove, String computerMove, String result) {
            this.playerMove = playerMove;
            this.computerMove = computerMove;
            this.result = result;
        }
    }
    
    private static ArrayList<GameHistory> history = new ArrayList<>();
    private static String playerName;
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // Welcome screen
        displayWelcomeScreen();
        
        // Get player name
        System.out.print("Enter your name: ");
        playerName = scanner.nextLine();
        
        while (true) {
            // Game mode selection
            System.out.println("\nGame Modes:");
            System.out.println("1. Best of N rounds");
            System.out.println("2. Practice Mode (Unlimited)");
            System.out.println("3. View Game History");
            System.out.println("4. Quit");
            System.out.print("Select mode (1-4): ");
            
            int mode = scanner.nextInt();
            scanner.nextLine(); // Clear buffer
            
            switch (mode) {
                case 1:
                    playBestOfN(scanner);
                    break;
                case 2:
                    playUnlimited(scanner);
                    break;
                case 3:
                    displayGameHistory();
                    break;
                case 4:
                    System.out.println("Thanks for playing, " + playerName + "!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid mode selection!");
            }
        }
    }
    
    private static void displayWelcomeScreen() {
        System.out.println("********************************");
        System.out.println("*    ROCK PAPER SCISSORS       *");
        System.out.println("*        DELUXE EDITION        *");
        System.out.println("********************************");
        System.out.println("✊  ✋  ✌️");
        System.out.println();
    }
    
    private static void playBestOfN(Scanner scanner) {
        System.out.print("Enter number of rounds (odd number): ");
        int rounds = scanner.nextInt();
        scanner.nextLine(); // Clear buffer
        
        if (rounds % 2 == 0) {
            rounds++; // Make it odd
            System.out.println("Adjusted to " + rounds + " rounds for a clear winner!");
        }
        
        int playerScore = 0;
        int computerScore = 0;
        int roundsPlayed = 0;
        int roundsToWin = (rounds / 2) + 1;
        
        while (playerScore < roundsToWin && computerScore < roundsToWin) {
            roundsPlayed++;
            System.out.println("\nRound " + roundsPlayed + " of " + rounds);
            boolean playerWon = playRound(scanner);
            
            if (playerWon) {
                playerScore++;
            } else {
                computerScore++;
            }
            
            displayScore(playerScore, computerScore);
        }
        
        announceWinner(playerScore, computerScore);
    }
    
    private static void playUnlimited(Scanner scanner) {
        int playerScore = 0;
        int computerScore = 0;
        
        while (true) {
            System.out.println("\nCurrent Score - " + playerName + ": " + playerScore + " Computer: " + computerScore);
            System.out.print("Play round? (yes/no): ");
            String answer = scanner.nextLine().toLowerCase();
            
            if (answer.startsWith("n")) {
                break;
            }
            
            boolean playerWon = playRound(scanner);
            if (playerWon) {
                playerScore++;
            } else {
                computerScore++;
            }
            
            displayScore(playerScore, computerScore);
        }
    }
    
    private static boolean playRound(Scanner scanner) {
        displayMoveOptions();
        Move playerMove = getPlayerMove(scanner);
        Move computerMove = Move.getRandomMove();
        
        System.out.println("\n" + playerName + "'s move: " + playerMove.getSymbol() + " (" + playerMove + ")");
        System.out.println("Computer's move: " + computerMove.getSymbol() + " (" + computerMove + ")");
        
        boolean playerWon = false;
        String result;
        
        if (playerMove == computerMove) {
            System.out.println("It's a tie! Replaying round...");
            return playRound(scanner);
        } else if ((playerMove == Move.ROCK && computerMove == Move.SCISSORS) ||
                   (playerMove == Move.PAPER && computerMove == Move.ROCK) ||
                   (playerMove == Move.SCISSORS && computerMove == Move.PAPER)) {
            System.out.println(playerName + " wins this round!");
            playerWon = true;
            result = "Win";
        } else {
            System.out.println("Computer wins this round!");
            result = "Loss";
        }
        
        // Record in history
        history.add(new GameHistory(playerMove.toString(), computerMove.toString(), result));
        
        return playerWon;
    }
    
    private static void displayMoveOptions() {
        System.out.println("\nAvailable moves:");
        for (Move move : Move.values()) {
            System.out.println(move.ordinal() + 1 + ": " + move + " " + move.getSymbol());
        }
    }
    
    private static Move getPlayerMove(Scanner scanner) {
        while (true) {
            System.out.print("Enter your move (1-3): ");
            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Clear buffer
                if (choice >= 1 && choice <= 3) {
                    return Move.values()[choice - 1];
                }
            } catch (Exception e) {
                scanner.nextLine(); // Clear buffer
            }
            System.out.println("Invalid move! Please enter 1, 2, or 3.");
        }
    }
    
    private static void displayScore(int playerScore, int computerScore) {
        System.out.println("\nScore:");
        System.out.println(playerName + ": " + playerScore);
        System.out.println("Computer: " + computerScore);
    }
    
    private static void announceWinner(int playerScore, int computerScore) {
        System.out.println("\n********************************");
        if (playerScore > computerScore) {
            System.out.println("🎉 Congratulations, " + playerName + "! You won the game! 🎉");
        } else {
            System.out.println("Better luck next time, " + playerName + "! Computer wins!");
        }
        System.out.println("Final Score - " + playerName + ": " + playerScore + " Computer: " + computerScore);
        System.out.println("********************************");
    }
    
    private static void displayGameHistory() {
        if (history.isEmpty()) {
            System.out.println("\nNo games played yet!");
            return;
        }
        
        System.out.println("\nGame History:");
        System.out.println("----------------------------------------");
        System.out.printf("%-5s %-10s %-10s %-10s%n", "Game", "Player", "Computer", "Result");
        System.out.println("----------------------------------------");
        
        for (int i = 0; i < history.size(); i++) {
            GameHistory game = history.get(i);
            System.out.printf("%-5d %-10s %-10s %-10s%n", 
                (i + 1), game.playerMove, game.computerMove, game.result);
        }
        System.out.println("----------------------------------------");
    }
}