package tablut;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import boardgame.Move;
import coordinates.Coord;
import coordinates.Coordinates;
import otherPeople.YuanTools;

public class RandomTablutPlayer extends TablutPlayer {
    public RandomTablutPlayer() {
        super("RandomPlayer");
    }

    public RandomTablutPlayer(String name) {
        super(name);
    }

//    @Override
//    public Move chooseMove(TablutBoardState boardState) {
//        return boardState.getRandomMove();
//    }
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
    
    
    
//    int opponent_id;
//    public float stateScore(TablutBoardState bs){
//    	if(bs.gameOver()){
//    		if(this.player_id == bs.getWinner()){
//    			return 1000.0f;
//    		}
//    		else{
//    			return -1000.0f;
//    		}
//    	}
//    	
//    	float score = 0;
//    	int myPiecesNumber = bs.getNumberPlayerPieces(this.player_id);
//    	int opponentPiecesNumber = bs.getNumberPlayerPieces(this.opponent_id);
//    	if(this.player_id != bs.getTurnPlayer()){
//    		score += (myPiecesNumber - opponentPiecesNumber)*1.0f;
//    	}
//    	else{
//    		score += (opponentPiecesNumber - myPiecesNumber)*1.0f;
//    	}
//    	
//    	Coord kingPosition = bs.getKingPosition();    	   	
//    	int kingDistance = Coordinates.distanceToClosestCorner(kingPosition);
//    	
//    	if(this.player_id == 1){
//    		score -= 0.5f * kingDistance;
//    	}
//    	else{
//    		score += 0.5f * kingDistance;
//    	}
//    	
//    	return score;
//    }
//    
//    public float min(TablutBoardState bs, int ply, float alpha, float beta){
//    	if(bs.gameOver() || ply == 0){
//    		return stateScore(bs);
//    	}
//    	
//    	float worstScore = Integer.MAX_VALUE;
//    	List<TablutMove> minOptions = bs.getAllLegalMoves();
//    	for(TablutMove option : minOptions){
//    		TablutBoardState cloneBS = (TablutBoardState) bs.clone();
//    		cloneBS.processMove(option);
//    		
//    		float currentValue = max(cloneBS, ply-1, alpha, beta);
//    		if(currentValue < worstScore){
//    			worstScore = currentValue;
//    		}
//    		
//    		beta = Math.min(beta, currentValue);
//    		if(beta <= alpha){
//    			break;
//    		}
//    	}
//    	return worstScore;
//    }
//    
//    public float max(TablutBoardState bs, int ply, float alpha, float beta){
//    	if(bs.gameOver() || ply == 0){
//    		return stateScore(bs);
//    	}
//    	
//    	float bestScore = Integer.MIN_VALUE;
//    	List<TablutMove> maxOptions = bs.getAllLegalMoves();
//    	for(TablutMove option : maxOptions){
//    		TablutBoardState cloneBS = (TablutBoardState) bs.clone();
//    		cloneBS.processMove(option);
//    		float currentValue = min(cloneBS, ply-1, alpha, beta);
//    		
//    		if(currentValue > bestScore){
//    			bestScore = currentValue;
//    		}
//    		
//    		alpha = Math.max(alpha, currentValue);
//    		if(beta <= alpha){
//    			break;
//    		}
//    	}
//    	return bestScore;
//    }
//    
//    public TablutMove minimaxDecision(final TablutBoardState bs){
//    	List<TablutMove> myOptions = bs.getAllLegalMoves();
//    	TablutMove bestMove = null;
//    	float bestScore = Float.NEGATIVE_INFINITY;
//    	
//    	for(TablutMove myMove : myOptions){
//    		TablutBoardState cloneBS = (TablutBoardState) bs.clone();
//    		cloneBS.processMove(myMove);
//    		float scoreOfMyMove = min(cloneBS, 2, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY);
//    		if(scoreOfMyMove > bestScore){
//    			bestScore = scoreOfMyMove;
//    			bestMove = myMove;
//    		}
//    	}
//    	
//    	return bestMove;
//    }
//
//    /**
//     * This is the primary method that you need to implement. The ``boardState``
//     * object contains the current state of the game, which your agent must use to
//     * make decisions.
//     */
//    public Move chooseMove(TablutBoardState boardState) {
//        // You probably will make separate functions in MyTools.
//        // For example, maybe you'll need to load some pre-processed best opening
//        // strategies...
//        //MyTools.getSomething();
//
//        //initiate
//        this.opponent_id = boardState.getOpponent();
//        
//        // Is random the best you can do?
//        //Move myMove = boardState.getRandomMove();
//
//        // Return your move to be processed by the server.
//        return minimaxDecision(boardState);
//    }

}
