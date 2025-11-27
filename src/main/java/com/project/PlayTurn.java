package com.project;
import java.util.ArrayList;
import java.util.List; 
import java.util.Scanner; 

public class PlayTurn {
    private final Gameboard board; 
   // private GameState state; 
    private final List<Player> players;
    private int currentIndex; 
    private GameState state; 
    private boolean quitGame = false; 


    public PlayTurn(Gameboard board){
        this.board = board; 
    
        this.players = new ArrayList<>(); 
        this.currentIndex = 0; 
        this.state = new ChooseCategory(this); 
        addPlayers();
    }
    private void addPlayers(){
        Scanner scanner = new Scanner(System.in); 
        System.out.println("Enter the number of players, the max is 4"); 
        int numPlayers = 0; 
        while(true){
            try{
                numPlayers = Integer.parseInt(scanner.nextLine().trim()); 
                if(numPlayers < 1 || numPlayers > 4){
                    System.out.print("Please enter a number between 1 and 4");
                }else {
                    break;
                }
            }catch(NumberFormatException e ){
                System.out.println("Invalid input Enter a number:");
            }
        }
        for(int i = 1; i <= numPlayers; i++){
            System.out.println("Enter name for player " + i + ":"); 
            String name = scanner.nextLine().trim();
            Player player = new Player(name);
            players.add(player); 
            // Register player with Game singleton
            Game.getInstance().addPlayer(player);
        }
        // Don't close scanner - it closes System.in and breaks subsequent reads
    }
    // public PlayTurn(Gameboard board, Player singlePlayer){
    //     this(board, List.of(singlePlayer)); 
    // }

    public void setState(GameState newState){
        this.state=  newState; 

    }
    public boolean isQuitGame(){
        return quitGame; 
    }
    public void quitGame(){
        quitGame = true; 
        state = null; 
    }
    
    
public void runOneTurn(){
    if(state == null)
       state = new ChooseCategory(this); 


        Player current = getCurrentPlayer();
            while(state != null){
            // Player current = getCurrentPlayer(); 
             System.out.println("---------------" + current.getName() + "'s turn ----------"); 
             System.out.println("Current Score: $" + current.getScore());
             state.handleGameLogic();


            
        }
    
  }

    

    public void runOneRound(){
        int playersCount = players.size(); 
        for(int i = 0; i < playersCount; i++){
            runOneTurn();
            if(quitGame){
                System.out.println("Game quit!"); 
                break;
            }
            switchPlayer(); 
        }
    }

    public void runRounds(int rounds){
        if(rounds <=0)
            return; 
        for(int r = 0; r <rounds; r++){
            runOneRound();
            if(quitGame)
                break; 
        }
    }

    public void switchPlayer(){
        currentIndex = (currentIndex + 1) % players.size();
     //   this.state = new ChooseCategory(this); 
        state = null; 
    }


    public Gameboard getBoard(){
        return board;
    }

    public Player getCurrentPlayer(){
        return players.get(currentIndex); 
    }

    public List<Player> getPlayers(){
        return List.copyOf(players);
    }

    public boolean hasMultiplePlayers(){
        return players.size()> 1; 
    }

    public void resetToFirstPlayer(){
        currentIndex = 0; 
        state = null;
    }
    
}

