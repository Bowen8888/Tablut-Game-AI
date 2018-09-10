package otherPeople;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TimerTask;

import coordinates.Coord;
import coordinates.Coordinates;
import tablut.TablutBoardState;
import tablut.TablutBoardState.Piece;
import tablut.TablutMove;

public class SearchingThread implements Runnable{
	private static TablutBoardState boardState;
	private static int player_id;
	private static int opponent_id;
	//public static TablutMove bestMove;
	public static int moveTimes=0;
	public static int moveChecked=0;
	public static double alpha = -10000;
	public static double beta = 10000;
	public static Dan.Result result;
	public SearchingThread(TablutBoardState bs, int myId, int opponentId, Dan.Result aResult){
		boardState = bs;
		player_id = myId;
		opponent_id = opponentId;
		//bestMove = (TablutMove) bs.getRandomMove();
		result = aResult;
	}
	@Override
	public void run() {
    	int depth = 3;
    	if(boardState.getTurnNumber() == 0){
    		depth = 5;
    	}
    	//while(!Thread.currentThread().isInterrupted()){
    	alpha = -10000;
    	beta = 10000;
    	//System.out.println(alpha);
		List<TablutMove> allMoves = boardState.getAllLegalMoves();
		Collections.shuffle(allMoves);
		//System.out.println(allMoves.size());
    	for(TablutMove move : allMoves){ //This is actually the highest depth, so if I say depth = 3 actually runs 4 levels
    		//System.out.println("good");
    		TablutBoardState cloneBS = (TablutBoardState) boardState.clone();
    		cloneBS.processMove(move);
    		double moveValue = getStateValue(cloneBS, player_id, opponent_id, alpha, beta, depth);
    		if(moveValue>alpha){ //alpha beta pruning
    			//System.out.println(moveValue);
				alpha = moveValue;
				result.setMove(move);
				if(alpha>=beta) break;
			}
		}	
	}
	
	/**
	 * 
	 * @param bs
	 * @return kings manhattan distance to the closest corner
	 */
	public static int getKingdistanceToCorner(TablutBoardState bs){
    	Coord kingPosition = bs.getKingPosition();
    	int distance = Coordinates.distanceToClosestCorner(kingPosition);
    	return distance;
    }
	//This may not used
	
    public static double getStateValue(TablutBoardState bs, int myId, int opponentId, double alpha, double beta, int depth){
    	//if current State already have a value, give the min or max according to how is the winner
    	if(bs.getWinner() == opponentId){
        	return -10000;
    	}
    	else if(bs.getWinner() == myId){
    		return 10000;
    	}
    	else if(depth>0){
    		List<TablutMove> options = bs.getAllLegalMoves();
    		Collections.shuffle(options);
    		if(bs.getTurnPlayer() == myId){
    			double v = -10000;
    			for(TablutMove move : options){
    				TablutBoardState cloneBS = (TablutBoardState) bs.clone();
    				cloneBS.processMove(move);
    				v= Math.max(v, getStateValue(cloneBS, myId, opponentId, alpha, beta, depth-1));
    				alpha = Math.max(alpha, v);
					if(alpha>=beta) break;
				}
				return v;
    		}
			else{
				double v = 1000;
				for(TablutMove move : options){
    				TablutBoardState cloneBS = (TablutBoardState) bs.clone();
    				cloneBS.processMove(move);
    				v = Math.min(v, getStateValue(cloneBS, myId, opponentId, alpha, beta, depth-1));
					beta = Math.min(beta, v);
					if(alpha>=beta) break;
				}
				return v;
    		}
    	}
    	else{
    		//depth = 0 do evaluation function 
    		double stateToReturn = -10000;

    		if(myId == TablutBoardState.MUSCOVITE){
    				stateToReturn = bs.getNumberPlayerPieces(myId) - bs.getNumberPlayerPieces(opponentId) - DanTools.numberOfStepsKingNeededToCorner(bs);
    		}
    		else{
    			if(DanTools.canBeSandwiched(bs, bs.getKingPosition())){
    				return stateToReturn;
    			}
       			stateToReturn = DanTools.numberOfStepsKingNeededToCorner(bs) + bs.getNumberPlayerPieces(myId) - bs.getNumberPlayerPieces(opponentId);
    		}
    		return stateToReturn;
    	}
    }
	
//	public TablutMove getBestMove(){
//		return bestMove;
//	}
	
}
