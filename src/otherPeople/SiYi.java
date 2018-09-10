package otherPeople;

import java.util.List;

import boardgame.Move;
import boardgame.Player;
import coordinates.Coord;
import coordinates.Coordinates;
import tablut.GreedyTablutPlayer;
import tablut.RandomTablutPlayer;
import tablut.TablutBoardState;
import tablut.TablutMove;
import tablut.TablutPlayer;

/** A player file submitted by a student. */
public class SiYi extends TablutPlayer {
	int opponent_id;
    /**
     * You must modify this constructor to return your student number. This is
     * important, because this is what the code that runs the competition uses to
     * associate you with your agent. The constructor should do nothing else.
     */
    public SiYi() {
        super("MinimaxWithAlg1");
    }
    
    /**
     * This method calculate the score of a particular board state regarding to the player id
     */
    public float stateScore(TablutBoardState bs){
    	if(bs.gameOver()){
    		if(this.player_id == bs.getWinner()){
    			return 1000.0f;
    		}
    		else{
    			return -1000.0f;
    		}
    	}
    	
    	float score = 0;
    	int myPiecesNumber = bs.getNumberPlayerPieces(this.player_id);
    	int opponentPiecesNumber = bs.getNumberPlayerPieces(this.opponent_id);
    	if(this.player_id != bs.getTurnPlayer()){
    		score += (myPiecesNumber - opponentPiecesNumber)*1.0f;
    	}
    	else{
    		score += (opponentPiecesNumber - myPiecesNumber)*1.0f;
    	}
    	
    	Coord kingPosition = bs.getKingPosition();    	   	
    	int kingDistance = Coordinates.distanceToClosestCorner(kingPosition);
    	
    	if(this.player_id == 1){
    		score -= 0.5f * kingDistance;
    	}
    	else{
    		score += 0.5f * kingDistance;
    	}
    	
    	return score;
    }
    
    public float min(TablutBoardState bs, int ply, float alpha, float beta){
    	if(bs.gameOver() || ply == 0){
    		return stateScore(bs);
    	}
    	
    	float worstScore = Integer.MAX_VALUE;
    	List<TablutMove> minOptions = bs.getAllLegalMoves();
    	for(TablutMove option : minOptions){
    		TablutBoardState cloneBS = (TablutBoardState) bs.clone();
    		cloneBS.processMove(option);
    		
    		float currentValue = max(cloneBS, ply-1, alpha, beta);
    		if(currentValue < worstScore){
    			worstScore = currentValue;
    		}
    		
    		beta = Math.min(beta, currentValue);
    		if(beta <= alpha){
    			break;
    		}
    	}
    	return worstScore;
    }
    
    public float max(TablutBoardState bs, int ply, float alpha, float beta){
    	if(bs.gameOver() || ply == 0){
    		return stateScore(bs);
    	}
    	
    	float bestScore = Integer.MIN_VALUE;
    	List<TablutMove> maxOptions = bs.getAllLegalMoves();
    	for(TablutMove option : maxOptions){
    		TablutBoardState cloneBS = (TablutBoardState) bs.clone();
    		cloneBS.processMove(option);
    		float currentValue = min(cloneBS, ply-1, alpha, beta);
    		
    		if(currentValue > bestScore){
    			bestScore = currentValue;
    		}
    		
    		alpha = Math.max(alpha, currentValue);
    		if(beta <= alpha){
    			break;
    		}
    	}
    	return bestScore;
    }
    
    public TablutMove minimaxDecision(final TablutBoardState bs){
    	List<TablutMove> myOptions = bs.getAllLegalMoves();
    	TablutMove bestMove = null;
    	float bestScore = Float.NEGATIVE_INFINITY;
    	
    	for(TablutMove myMove : myOptions){
    		TablutBoardState cloneBS = (TablutBoardState) bs.clone();
    		cloneBS.processMove(myMove);
    		float scoreOfMyMove = min(cloneBS, 2, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY);
    		if(scoreOfMyMove > bestScore){
    			bestScore = scoreOfMyMove;
    			bestMove = myMove;
    		}
    	}
    	
    	return bestMove;
    }

    /**
     * This is the primary method that you need to implement. The ``boardState``
     * object contains the current state of the game, which your agent must use to
     * make decisions.
     */
    public Move chooseMove(TablutBoardState boardState) {
        // You probably will make separate functions in MyTools.
        // For example, maybe you'll need to load some pre-processed best opening
        // strategies...
        //MyTools.getSomething();

        //initiate
        this.opponent_id = boardState.getOpponent();
        
        // Is random the best you can do?
        //Move myMove = boardState.getRandomMove();

        // Return your move to be processed by the server.
        return minimaxDecision(boardState);
    }
    
    
}