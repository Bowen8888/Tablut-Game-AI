package otherPeople;

import java.util.List;

import boardgame.Move;
import coordinates.Coord;
import tablut.TablutBoardState;
import tablut.TablutMove;
import tablut.TablutPlayer;

/** A player file submitted by a student. */
public class Yuan extends TablutPlayer {

    /**
     * You must modify this constructor to return your student number. This is
     * important, because this is what the code that runs the competition uses to
     * associate you with your agent. The constructor should do nothing else.
     */
    public Yuan() {
        super("260735120");
    }

    /**
     * This is the primary method that you need to implement. The ``boardState``
     * object contains the current state of the game, which your agent must use to
     * make decisions.
     */
    
    public int evlfcn(){
    	int numberOfBlackPieces =0;
    	int numberOfWhitePieces =0;
    	int kingPositionToNearestCorner=0;
    	
    	
    	return numberOfWhitePieces - numberOfBlackPieces + kingPositionToNearestCorner;
    	
    	
    }
    
    public Move chooseMove(TablutBoardState boardState) {
        // You probably will make separate functions in MyTools.
        // For example, maybe you'll need to load some pre-processed best opening
        // strategies...
        //MyTools.getSomething();

        // Is random the best you can do?
        //Move myMove = boardState.getRandomMove();
    	 
    	// minimax algorithm
    	
        List<TablutMove> legalOp = boardState.getAllLegalMoves();
        
        // setup an array to record all values of operators
        //double[] values = new double[legalOp.size()];
        
        
        // record the move with the highest utility value
        //double highestValue = 0;
        TablutMove highestOp = legalOp.get(0);
        //System.out.printf("there are %d moves in choose", legalOp.size());
        
        // minimax decision
        //System.out.print("in choose move --");
        double alpha = -100;
        double beta = 100;
        int cutoff = 85;
        
        for (TablutMove op: legalOp){
        	
        	if (cutoff == 0){
    			break;
    		}   
        	
            // clone the boardState so that we can modify it
            TablutBoardState cloneBS = (TablutBoardState) boardState.clone();

            // Process that move, as if we actually made it happen.
            cloneBS.processMove(op);
            cutoff --;
            
            //System.out.print("in choose move for --");
            /*
             * If we also want to check if the move would cause us to win, this works for
             * both! This will check if black can capture the king, and will also check if
             * white can move to a corner, since if either of these things happen then a
             * winner will be set.
             */
    	
            if (cloneBS.getWinner() == player_id) {
            	//System.out.print("in choose move for, check winner --");
                return op;
            }            	
            
            // determine the move's value and compare it with the highest value
            double opValue = YuanTools.minValue(cloneBS,alpha,beta, 3);
            //System.out.print("in choose move for, after minValue call --");
            
            if (alpha < opValue){
            	alpha = opValue;
            	highestOp = op;
            }
            
            // prune
            if (alpha >= beta){
            	//System.out.print("in choose move, prune --");
            	return highestOp;
            }
            
            /*
            if (opValue > highestValue){
            	//System.out.print("in choose move for, compare values --");
            	highestValue = opValue;
            	highestOp = op;
            }
            */
        }   	
       
        // return the move with the highest value        
        // Return your move to be processed by the server.
        return highestOp;
    }
    
    
}