import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.time.LocalDateTime;

public class RockPaperScissors {
    private static final int MIN_ROUNDS = 1;
    private static final int MAX_ROUNDS = 21;
    private static final Random RANDOM = new Random();
    private static final Scanner SCANNER = new Scanner(System.in);

    private final List<GameResult> gameHistory = new ArrayList<>();
    private String playerName;
    private int playerTotalScore = 0;
    private int computerTotalScore = 0;

    private enum Move {
        ROCK("✊", "A solid rock-solid choice!"), 
        PAPER("✋", "Smooth as a sheet of paper."), 
        SCISSORS("✌️", "Sharp and precise cutting edge.");
        
        private final String symbol;
        private final String description;
        
        Move(String symbol, String description) {
            this.symbol = symbol;
            this.description = description;
        }
        
        public String getSymbol() {
            return symbol;
        }
        
        public String getDescription() {
            return description;
        }
        
        public static Move getRandomMove() {
            return values()[RANDOM.nextInt(values().length)];
        }
        
        public boolean beats(Move other) {
            return switch (this) {
                case ROCK -> other == SCISSORS;
                case PAPER -> other == ROCK;
                case SCISSORS -> other == PAPER;
            };
        }
    }

    private static class GameResult {
        final LocalDateTime timestamp;
        final String playerMove;
        final String computerMove;
        final GameOutcome outcome;
        
        enum GameOutcome {
            WIN("Victory! 🏆"), 
            LOSS("Defeat! 😔"), 
            TIE("Draw! 🤝");
            
            private final String message;
            
            GameOutcome(String message) {
                this.message = message;
            }
            
            public String getMessage() {
                return message;
            }
        }
        
        GameResult(String playerMove, String computerMove, GameOutcome outcome) {
            this.timestamp = LocalDateTime.now();
            this.playerMove = playerMove;
            this.computerMove = computerMove;
            this.outcome = outcome;
        }
    }

    public void start() {
        displayWelcomeScreen();
        getPlayerName();
        playGame();
    }

    private void displayWelcomeScreen() {
        System.out.println("*".repeat(40));
        System.out.println("*    ROCK PAPER SCISSORS ULTIMATE    *");
        System.out.println("*         CHAMPIONSHIP EDITION       *");
        System.out.println("*".repeat(40));
        System.out.println("✊  ✋  ✌️  - Choose Your Destiny! 🎲");
        System.out.println();
    }

    private void getPlayerName() {
        while (true) {
            System.out.print("Enter your name (3-15 characters): ");
            String input = SCANNER.nextLine().trim();
            if (input.length() >= 3 && input.length() <= 15) {
                playerName = input;
                return;
            }
            System.out.println("Invalid name. Please try again.");
        }
    }

    private void playGame() {
        while (true) {
            displayGameMenu();
            int choice = getValidatedMenuChoice();
            
            switch (choice) {
                case 1 -> playBestOfNRounds();
                case 2 -> playPracticeMode();
                case 3 -> displayDetailedGameHistory();
                case 4 -> {
                    displayFinalStats();
                    return;
                }
            }
        }
    }

    private void displayGameMenu() {
        System.out.println("\n--- Game Modes ---");
        System.out.println("1. Best of N Rounds");
        System.out.println("2. Unlimited Practice Mode");
        System.out.println("3. Detailed Game History");
        System.out.println("4. Quit and Show Total Stats");
        System.out.print("Select mode (1-4): ");
    }

    private int getValidatedMenuChoice() {
        while (true) {
            try {
                int choice = Integer.parseInt(SCANNER.nextLine());
                if (choice >= 1 && choice <= 4) {
                    return choice;
                }
                throw new NumberFormatException();
            } catch (NumberFormatException e) {
                System.out.println("Invalid selection. Please enter a number between 1 and 4.");
            }
        }
    }

    private void playBestOfNRounds() {
        int rounds = getNumberOfRounds();
        int playerScore = 0;
        int computerScore = 0;
        int roundsToWin = (rounds / 2) + 1;
        
        for (int currentRound = 1; playerScore < roundsToWin && computerScore < roundsToWin; currentRound++) {
            System.out.println("\n--- Round " + currentRound + " of " + rounds + " ---");
            GameResult.GameOutcome roundOutcome = playRound();
            
            switch (roundOutcome) {
                case WIN -> playerScore++;
                case LOSS -> computerScore++;
                case TIE -> currentRound--; // replay tie rounds
            }
            
            displayCurrentScore(playerScore, computerScore);
        }
        
        announceMatchWinner(playerScore, computerScore);
    }

    private int getNumberOfRounds() {
        while (true) {
            System.out.print("Enter number of rounds (odd, 1-21): ");
            try {
                int rounds = Integer.parseInt(SCANNER.nextLine());
                if (rounds >= MIN_ROUNDS && rounds <= MAX_ROUNDS) {
                    return rounds % 2 == 0 ? rounds + 1 : rounds;
                }
                System.out.println("Please enter an odd number between 1 and 21.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private void playPracticeMode() {
        while (true) {
            System.out.println("\n--- Practice Mode ---");
            System.out.printf("Current Total Score - %s: %d | Computer: %d%n", 
                playerName, playerTotalScore, computerTotalScore);
            
            if (!confirmContinuePlaying()) {
                break;
            }
            
            GameResult.GameOutcome roundOutcome = playRound();
            updateTotalScores(roundOutcome);
        }
    }

    private boolean confirmContinuePlaying() {
        System.out.print("Ready to play another round? (yes/no): ");
        return !SCANNER.nextLine().toLowerCase().startsWith("n");
    }

    private void updateTotalScores(GameResult.GameOutcome outcome) {
        switch (outcome) {
            case WIN -> playerTotalScore++;
            case LOSS -> computerTotalScore++;
        }
    }

    private GameResult.GameOutcome playRound() {
        Move playerMove = getPlayerMove();
        Move computerMove = Move.getRandomMove();
        
        displayMoves(playerMove, computerMove);
        
        GameResult.GameOutcome outcome;
        if (playerMove == computerMove) {
            outcome = GameResult.GameOutcome.TIE;
            System.out.println("It's a tie! " + outcome.getMessage());
        } else {
            boolean playerWon = playerMove.beats(computerMove);
            outcome = playerWon ? 
                GameResult.GameOutcome.WIN : 
                GameResult.GameOutcome.LOSS;
            System.out.println(playerWon ? 
                playerName + " wins this round! " + outcome.getMessage() : 
                "Computer wins this round! " + outcome.getMessage());
        }

        gameHistory.add(new GameResult(
            playerMove.toString(), 
            computerMove.toString(), 
            outcome
        ));
        
        return outcome;
    }

    private Move getPlayerMove() {
        while (true) {
            displayMoveOptions();
            System.out.print("Enter your move (1-3): ");
            try {
                int choice = Integer.parseInt(SCANNER.nextLine());
                if (choice >= 1 && choice <= 3) {
                    Move selectedMove = Move.values()[choice - 1];
                    System.out.println(selectedMove.getDescription());
                    return selectedMove;
                }
            } catch (NumberFormatException e) {

            }
            System.out.println("Invalid move! Please enter 1, 2, or 3.");
        }
    }

    private void displayMoveOptions() {
        System.out.println("\nAvailable Moves:");
        for (Move move : Move.values()) {
            System.out.printf("%d: %s %s%n", 
                move.ordinal() + 1, move, move.getSymbol());
        }
    }

    private void displayMoves(Move playerMove, Move computerMove) {
        System.out.printf("%n%s's move: %s (%s)%n", 
            playerName, playerMove.getSymbol(), playerMove);
        System.out.printf("Computer's move: %s (%s)%n", 
            computerMove.getSymbol(), computerMove);
    }

    private void displayCurrentScore(int playerScore, int computerScore) {
        System.out.printf("%nCurrent Score:%n%s: %d | Computer: %d%n", 
            playerName, playerScore, computerScore);
    }

    private void announceMatchWinner(int playerScore, int computerScore) {
        System.out.println("\n" + "=".repeat(40));
        if (playerScore > computerScore) {
            System.out.printf("🎉 Congratulations, %s! You won the match! 🎉%n", playerName);
        } else {
            System.out.printf("Better luck next time, %s! Computer wins the match!%n", playerName);
        }
        System.out.printf("Final Score - %s: %d | Computer: %d%n", 
            playerName, playerScore, computerScore);
        System.out.println("=".repeat(40));
    }

    private void displayDetailedGameHistory() {
        if (gameHistory.isEmpty()) {
            System.out.println("\nNo games have been played yet!");
            return;
        }
        
        System.out.println("\n--- Detailed Game History ---");
        System.out.println("-".repeat(70));
        System.out.printf("%-25s %-10s %-10s %-15s %s%n", 
            "Timestamp", "Player", "Computer", "Result", "Outcome");
        System.out.println("-".repeat(70));
        
        gameHistory.forEach(result -> 
            System.out.printf("%-25s %-10s %-10s %-15s %s%n", 
                result.timestamp, 
                result.playerMove, 
                result.computerMove, 
                result.outcome, 
                result.outcome.getMessage())
        );
        
        System.out.println("-".repeat(70));
    }

    private void displayFinalStats() {
        System.out.println("\n" + "=".repeat(40));
        System.out.printf("Final Tournament Stats for %s:%n", playerName);
        System.out.printf("Total Wins: %d%n", playerTotalScore);
        System.out.printf("Total Losses: %d%n", computerTotalScore);
        System.out.println("Thanks for playing Rock Paper Scissors Ultimate!");
        System.out.println("=".repeat(40));
    }

    public static void main(String[] args) {
        new RockPaperScissors().start();
    }
}
