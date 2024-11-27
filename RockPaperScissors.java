import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class RockPaperScissors {
    private static final ArrayList<GameHistory> history = new ArrayList<>();
    private static String playerName;
    private static final Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        displayWelcomeScreen();
        getPlayerName();
        playGame();
        scanner.close();
    }

    private enum Move {
        ROCK("✊"), 
        PAPER("✋"), 
        SCISSORS("✌️");
        
        private final String symbol;
        
        Move(String symbol) {
            this.symbol = symbol;
        }
        
        public String getSymbol() {
            return symbol;
        }
        
        public static Move getRandomMove() {
            return values()[new Random().nextInt(values().length)];
        }
        
        public boolean beats(Move other) {
            return (this == ROCK && other == SCISSORS) ||
                (this == PAPER && other == ROCK) ||
                (this == SCISSORS && other == PAPER);
        }
    }
    
    private static class GameHistory {
        final String playerMove;
        final String computerMove;
        final String result;
        
        GameHistory(String playerMove, String computerMove, String result) {
            this.playerMove = playerMove;
            this.computerMove = computerMove;
            this.result = result;
        }
    }
    
    private static void getPlayerName() {
        System.out.print("Enter your name: ");
        playerName = scanner.nextLine();
    }
    
    private static void playGame() {
        while (true) {
            displayGameMenu();
            int mode = getMenuChoice();
            
            switch (mode) {
                case 1:
                    playBestOfN();
                    break;
                case 2:
                    playUnlimited();
                    break;
                case 3:
                    displayGameHistory();
                    break;
                case 4:
                    System.out.println("Thanks for playing, " + playerName + "!");
                    return;
                default:
                    System.out.println("Invalid mode selection!");
            }
        }
    }
    private static void displayGameMenu() {
        System.out.println("\nGame Modes:");
        System.out.println("1. Best of N rounds");
        System.out.println("2. Practice Mode (Unlimited)");
        System.out.println("3. View Game History");
        System.out.println("4. Quit");
        System.out.print("Select mode (1-4): ");
    }
    
    private static int getMenuChoice() {
        int choice = scanner.nextInt();
        scanner.nextLine();
        return choice;
    }
    
    private static void displayWelcomeScreen() {
        System.out.println("********************************");
        System.out.println("*    ROCK PAPER SCISSORS       *");
        System.out.println("*        DELUXE EDITION        *");
        System.out.println("********************************");
        System.out.println("✊  ✋  ✌️");
        System.out.println();
    }
    
    private static void playBestOfN() {
        int rounds = getNumberOfRounds();
        int playerScore = 0;
        int computerScore = 0;
        int roundsToWin = (rounds / 2) + 1;
        
        for (int roundsPlayed = 1; playerScore < roundsToWin && computerScore < roundsToWin; roundsPlayed++) {
            System.out.println("\nRound " + roundsPlayed + " of " + rounds);
            if (playRound()) {
                playerScore++;
            } else {
                computerScore++;
            }
            displayScore(playerScore, computerScore);
        }
        
        announceWinner(playerScore, computerScore);
    }
    
    private static int getNumberOfRounds() {
        System.out.print("Enter number of rounds (odd number): ");
        int rounds = scanner.nextInt();
        scanner.nextLine();
        
        if (rounds % 2 == 0) {
            rounds++;
            System.out.println("Adjusted to " + rounds + " rounds for a clear winner!");
        }
        return rounds;
    }
    
    private static void playUnlimited() {
        int playerScore = 0;
        int computerScore = 0;
        
        while (true) {
            System.out.println("\nCurrent Score - " + playerName + ": " + playerScore + " Computer: " + computerScore);
            if (!shouldContinuePlaying()) {
                break;
            }
            
            if (playRound()) {
                playerScore++;
            } else {
                computerScore++;
            }
            displayScore(playerScore, computerScore);
        }
    }
    
    private static boolean shouldContinuePlaying() {
        System.out.print("Play round? (yes/no): ");
        return !scanner.nextLine().toLowerCase().startsWith("n");
    }
    
    private static boolean playRound() {
        displayMoveOptions();
        Move playerMove = getPlayerMove();
        Move computerMove = Move.getRandomMove();
        
        displayMoves(playerMove, computerMove);
        
        if (playerMove == computerMove) {
            System.out.println("It's a tie! Replaying round...");
            return playRound();
        }
        
        boolean playerWon = playerMove.beats(computerMove);
        String result = playerWon ? "Win" : "Loss";
        
        System.out.println(playerWon ? playerName + " wins this round!" : "Computer wins this round!");
        history.add(new GameHistory(playerMove.toString(), computerMove.toString(), result));
        
        return playerWon;
    }
    
    private static void displayMoves(Move playerMove, Move computerMove) {
        System.out.println("\n" + playerName + "'s move: " + playerMove.getSymbol() + " (" + playerMove + ")");
        System.out.println("Computer's move: " + computerMove.getSymbol() + " (" + computerMove + ")");
    }
    
    private static void displayMoveOptions() {
        System.out.println("\nAvailable moves:");
        for (Move move : Move.values()) {
            System.out.println((move.ordinal() + 1) + ": " + move + " " + move.getSymbol());
        }
    }
    
    private static Move getPlayerMove() {
        while (true) {
            System.out.print("Enter your move (1-3): ");
            try {
                int choice = scanner.nextInt();
                scanner.nextLine();
                if (choice >= 1 && choice <= 3) {
                    return Move.values()[choice - 1];
                }
            } catch (Exception e) {
                scanner.nextLine();
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