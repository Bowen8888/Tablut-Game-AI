package otherPeople;

import tablut.*;
import tablut.TablutBoardState.Piece;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import boardgame.Move;
import coordinates.*;
import coordinates.Coordinates.CoordinateDoesNotExistException;

public class DanTools {
	
    public static double getSomething() {
        return Math.random();
    }
    
    public static boolean kingIsAtOneSide(TablutBoardState bs){
    	Coord kingPosition = bs.getKingPosition();
    	if(kingPosition.x==0||kingPosition.x == 8||kingPosition.y == 0||kingPosition.y == 8){
    		return true;
    	}
    	return false;
    }
    
    /**
     * 
     * @param bs
     * @param start
     * @return true if a Piece can move from start position to any corner in one move.(This is an assumption that assume if there is a piece on start position, the start Position may not have any piece in bs) 
     */
    public static boolean canMoveToCorner(TablutBoardState bs, Coord start){
    	for(Coord corner : Coordinates.getCorners()){
			if(canMoveTo(bs, start, corner)){
				return true;
			}
		}
    	return false;
    }
    
    /**
     * 
     * @param bs
     * @param start
     * @param end
     * @return true if a piece can move from start to end, startPosition may have no piece in bs, but the move must be legal if there is a piece at start position. 
     */
    public static boolean canMoveTo(TablutBoardState bs, Coord start, Coord end){
        // Next, make sure move doesn't end on a piece.
        if (!bs.coordIsEmpty(end))
            return false;

        // Next, make sure the move is actually a move.
        int coordDiff = start.maxDifference(end);
        if (coordDiff == 0)
            return false;

        // Now for the actual game logic. First we make sure it is moving like a rook.
        if (!(start.x == end.x || start.y == end.y))
            return false;

        // Now we make sure it isn't moving through any other pieces.
        for (Coord throughCoordinate : start.getCoordsBetween(end)) {
            if (!bs.coordIsEmpty(throughCoordinate))
                return false;
        }

        // All of the conditions have been satisfied, we have a legal move!
        return true;
    }
    
    
    	/**
    	 * 
    	 * @param bs
    	 * @return If king need 1 step to go to corner, it returns 4, if king need 2 steps to go to corner, it returns 2, otherwise 0. 
    	 */
    public static int numberOfStepsKingNeededToCorner(TablutBoardState bs){
    	Coord kingPosition = bs.getKingPosition();
    	if(canMoveToCorner(bs, kingPosition)){
    		return 4;
    	}
    	else if((canMoveTo(bs, kingPosition, Coordinates.get(0, kingPosition.y))&&canMoveToCorner(bs, Coordinates.get(0, kingPosition.y))&&!canBeSandwiched(bs, Coordinates.get(0, kingPosition.y)))|| 
			(canMoveTo(bs, kingPosition, Coordinates.get(8, kingPosition.y))&&canMoveToCorner(bs, Coordinates.get(8, kingPosition.y))&&!canBeSandwiched(bs, Coordinates.get(8, kingPosition.y)))||
			(canMoveTo(bs, kingPosition, Coordinates.get(kingPosition.x, 0))&&canMoveToCorner(bs, Coordinates.get(kingPosition.x, 0))&&!canBeSandwiched(bs, Coordinates.get(kingPosition.x, 0)))||
			(canMoveTo(bs, kingPosition, Coordinates.get(kingPosition.x, 8))&&canMoveToCorner(bs, Coordinates.get(kingPosition.x, 8))&&!canBeSandwiched(bs, Coordinates.get(kingPosition.x, 8)))){
			return 2;
    	}
    	else{
        	return 0;
    	}

    }
    
    	/**
    	 * 
    	 * @param bs
    	 * @param coord
    	 * @return if there is a piece in there, If this piece can be eaten in next step, it return true. 
    	 */
    public static boolean canBeSandwiched(TablutBoardState bs, Coord coord){
    	Coord killPosition = null;

    	List<Coord> neighbors = Coordinates.getNeighbors(coord);
    	for(Coord position: neighbors) {
    		if((bs.getPieceAt(position) == TablutBoardState.Piece.BLACK)||Coordinates.isCorner(position)) {
				try {
					killPosition = Coordinates.getSandwichCoord(position, coord);
					if(killPosition != null) {
	    				break;
	    			}
				} catch (CoordinateDoesNotExistException e) {
					//do nothing
				}
    		}
    	}
    	
    	if(killPosition == null) {
    		return false;
    	}
    	HashSet<Coord> pieces;
    	if(bs.getTurnPlayer()==0){
    		pieces = bs.getPlayerPieceCoordinates();
    	}
    	else{
    		pieces = bs.getOpponentPieceCoordinates();
    	}
    	for(Coord c: pieces) {
    		if(canMoveTo(bs, c, killPosition)){
    			return true;
    		}
    	}
    	return false;
    }
    
}
