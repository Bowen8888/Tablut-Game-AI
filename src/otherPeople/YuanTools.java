package otherPeople;

import java.util.HashMap;
import java.util.List;

import tablut.TablutMove;
import tablut.TablutBoardState;

//import boardgame.Move;
//import tablut.TablutPlayer;
//import java.util.ArrayList;
import coordinates.Coord;
//import coordinates.Coordinates;
//import coordinates.Coordinates.CoordinateDoesNotExistException;

public class YuanTools {
	
	// keep a copy of determine state values
	// HashMap<TablutBoardState, Double> opValueMap;
	//public static List<HashMap<TablutBoardState, Double>> opValueList;
	//public static List<TablutBoardState> bsList;
	//public static List<Double> valueList;
	
	// minimax cutoff
	// public static int CUTOFF = 4;

    public static double getSomething() {
        return Math.random();
    }
    
    /*
    // minimax algorithm
    public static double minimaxValue(TablutBoardState bs, int level) {
    	
    	int opponent = bs.getOpponent();
    	int winner = bs.getWinner();
        int minNumberOfOpponentPieces = bs.getNumberPlayerPieces(opponent);
    	
    	// check if there is a winner
    	if (winner == opponent){
    		return -1000.00;
    	}

    	if(winner == 260735120){
    		return 1000.00;
    	}
    	
		// array to keep values
		List<Double> values = new ArrayList<Double>();

    	// check value of successor states
        List<TablutMove> legalOp = bs.getAllLegalMoves();
        
    	for (TablutMove op : legalOp){    		
    		// clone the boardState so that we can modify it
            TablutBoardState cloneBS = (TablutBoardState) bs.clone();
            // Process that move, as if we actually made it happen.
            cloneBS.processMove(op);
            // note the value
            values.add(minimaxValue(cloneBS, level - 1));
        	// terminating condition + utility function (black's perspective)
        	if (level == 0){
        		Coord endPosition = op.getEndPosition();
        		Coord k = bs.getKingPosition();
        		List<Coord> neigh = Coordinates.getNeighbors(endPosition);
        		List<Coord> neighK = Coordinates.getNeighbors(k);

        		// int pID = op.getPlayerID();
        		
        		double blackValue = 0;
        		
        		

        		// to avoid being eaten by whites/black
        		for (Coord c : neigh){
        			if(bs.isOpponentPieceAt(c) && c != k){
        				blackValue -= 10;
        				break;
        			}
        		}	        	
        			
        		
        		// surround k
        		
        		for (Coord c : neighK){
        			if(!(bs.isOpponentPieceAt(c)) && !bs.coordIsEmpty(c)){
        				blackValue += 25;
        				try {
        					Coordinates.getSandwichCoord(c,k);
        					blackValue += 80;
        				}catch(CoordinateDoesNotExistException e){
        				}
        			}
        		}
        		
        		// to eat
                // Check how many opponent pieces there are now, maybe we captured some!
                int newNumberOfOpponentPieces = cloneBS.getNumberPlayerPieces(opponent);
                blackValue += (minNumberOfOpponentPieces - newNumberOfOpponentPieces) * 2;
        		
        		// follow king
        		for (Coord c : bs.getPlayerPieceCoordinates()){
        			blackValue += (double) (100 - c.distance(k));
        		}
        		
        		// king protected
        		if(Coordinates.isCenterOrNeighborCenter(k)){
        			blackValue -= 15;
        		}
        		
        		// less nb of opponent pieces
        		blackValue -= bs.getNumberPlayerPieces(opponent);
        		
        		// king to corners' unguarded (nb of pieces to the side)
        		
        		Coord c1 = Coordinates.get(k.x, 0);
        		Coord c2 = Coordinates.get(k.x, 8);
        		Coord c3 = Coordinates.get(0, k.y);
        		Coord c4 = Coordinates.get(8, k.y);
        		blackValue += ((k.getCoordsBetween(c1)).size())*4;
        		blackValue += ((k.getCoordsBetween(c2)).size())*4;
        		blackValue += ((k.getCoordsBetween(c3)).size())*4;
        		blackValue += ((k.getCoordsBetween(c4)).size())*4;			
        		
        		// king on the side
        		if (k.x == 0 || k.x == 8 || k.y == 8 || k.y == 0){
        			blackValue -= 25;
        		}
        		
        		return blackValue;
        	}	        	      		
    	}
            
	    // if player is black and it is player's turn -> max player
	    if(opponent == TablutBoardState.SWEDE && 260735120 == bs.getTurnPlayer()){
	    	double max = 0.00;
	    	for(double v : values){
	    		if (v > max){
	    			max = v;
	    		}
	    	}
	    	return max;
	    }else{	// min player's turn
	    	double min = 100.00;
	    	for (double v: values){
	    		if (v < min){
	    			min = v;
	    		}
	    	}
	    	return min;
	    }
               		
    }
    */
    
    // evaluation function
    public static double eval(TablutBoardState bs) {
    	////System.out.print("in eval -- \n");
    	int opponent = bs.getOpponent();
    	
    	// if there is already a winner
    	if (bs.gameOver()){
    		int winner = bs.getWinner();    		
			// to check winner
    		if (winner == opponent){	// white wins
    			////System.out.print("black get eaten --");
    			return -100.0 ;
    		}else{	// black wins        			
				////System.out.print("white get eaten --");
				return 100.0;    			
    		}  
    	} 	    	
    	Coord k = bs.getKingPosition();
    	////System.out.printf("k.x = %d, k.y = %d",k.x,k.x); 
    	double whiteValue = 0.0;
    	//List<Coord> neighK = Coordinates.getNeighbors(k); 
    	
		// king protected    	
    	/*
		if (Coordinates.isCenterOrNeighborCenter(k)){
			whiteValue += 2.9;
		}
		*/
		
		// king on the side    	
		if (k.x == 0 || k.x == 8 || k.y == 8 || k.y == 0){
			whiteValue += 5.5;
		}
		
    	// if current player is black
    	if (TablutBoardState.SWEDE == opponent){
    		//System.out.print("in eval, black player --");
    		
    		// black concerns about nb of pieces
    		return (double) 60.0 - (1.8 * bs.getNumberPlayerPieces(opponent)) + bs.getNumberPlayerPieces(TablutBoardState.MUSCOVITE); // - whiteValue;
    				//25 -  1.7*(bs.getNumberPlayerPieces(opponent)) + bs.getNumberPlayerPieces(TablutBoardState.MUSCOVITE) + Coordinates.distanceToClosestCorner(k);
       	
    	}else{ // current player is white   		
    		//System.out.print("in eval, white player --"); 		
       		       		
       		// king could be eaten by opponent(black)
    		/*
			for (Coord c : neighK){
				if (bs.isOpponentPieceAt(c)){
					whiteValue -= 10;
					break;
				}			
			}			
			*/   		
	
       		    		   		
    		// white concerns about nb of black pieces and king's location
       		return (double) 60.0 - (1.5 * bs.getNumberPlayerPieces(opponent)) + whiteValue + 0.98 * bs.getNumberPlayerPieces(TablutBoardState.SWEDE);
       				 //25 -  1.3*(bs.getNumberPlayerPieces(opponent)) + bs.getNumberPlayerPieces(TablutBoardState.MUSCOVITE) + Coordinates.distanceToClosestCorner(k);
       	}
    }   
    
    // alpha beta alg
    public static double maxValue (TablutBoardState bs, double alpha, double beta, int depth){	
    	//System.out.print("in maxValue --  ");
   	
    	// check if there is a winner and depth 
    	if (depth == 0 || bs.gameOver()){
    		////System.out.print("in maxValue, go eval --");
    		return eval(bs);
    	}
    	
    	int cutoff; 
    	
    	if (depth <= 1){
    		cutoff = 40;
    	}else{
    		cutoff = 65;
    	}
    	
    	List<TablutMove> legalOp;
    	Coord k = bs.getKingPosition();
    	if (bs.getOpponent() == TablutBoardState.MUSCOVITE && (k.x == 0 || k.x == 8 || k.y == 8 || k.y == 0)){
    		legalOp = bs.getLegalMovesForPosition(k);
    	}else{    	
    		legalOp = bs.getAllLegalMoves();
    	}
        //System.out.printf("there are %d moves in max", legalOp.size());

    	for (TablutMove op: legalOp){
    		////System.out.print("in maxValue, for --");
    		
    		if (cutoff == 0){
    			break;
    		}    		
    		// clone the boardState so that we can modify it
            TablutBoardState cloneBS = (TablutBoardState) bs.clone();

            // Process that move, as if we actually made it happen.
            cloneBS.processMove(op);
            cutoff --;
            
            // call min alg
            double minSucc = minValue (cloneBS, alpha, beta, depth-1);
            ////System.out.printf("in maxValue, after call min -- minSucc is %f", minSucc);
            
            // choose max
            if (alpha < minSucc){
            	alpha = minSucc;
            }
            ////System.out.printf("in maxValue, after call min -- alpha is %f, beta is %f", alpha, beta);
            // prune            
            if (alpha >= beta){
            	////System.out.print("in maxValue, prune --");
            	return beta;
            }            
    	}    	
    	return alpha;
    }
    
    public static double minValue (TablutBoardState bs, double alpha, double beta, int depth){
    	//System.out.print("in minValue --  ");
    	
    	// check if there is a winner and depth 
    	if (depth == 0 || bs.gameOver()){
    		////System.out.print("in minValue, go eval --");
    		return eval(bs);
    	}
    	
    	int cutoff; 
    	
    	if (depth <= 1){
    		cutoff = 40;
    	}else{
    		cutoff = 65;
    	}
    	
    	List<TablutMove> legalOp;
    	Coord k = bs.getKingPosition();
    	if (bs.getOpponent() == TablutBoardState.MUSCOVITE && (k.x == 0 || k.x == 8 || k.y == 8 || k.y == 0)){
    		legalOp = bs.getLegalMovesForPosition(k);
    	}else{
    		legalOp = bs.getAllLegalMoves();
    	}
        //System.out.printf("there are %d moves in min", legalOp.size());

    	for (TablutMove op: legalOp){
    		////System.out.print("in minValue, for --");
    		
    		if (cutoff == 0){
    			break;
    		}
    		// clone the boardState so that we can modify it
            TablutBoardState cloneBS = (TablutBoardState) bs.clone();

            // Process that move, as if we actually made it happen.
            cloneBS.processMove(op);
            
            cutoff --;
            
            // call max alg
            double maxSucc = maxValue (cloneBS, alpha, beta, depth-1);
            ////System.out.printf("in minValue, call max -- maxSucc is %f", maxSucc);
            
            // choose min
            if (beta > maxSucc){
            	beta = maxSucc;
            }
            ////System.out.printf("in minValue, after call max -- alpha is %f, beta is %f", alpha, beta);
            // prune            
            if (alpha >= beta){
            	////System.out.print("in minValue, prune --");
            	return alpha;
            }            
    	}    	
    	return beta;    	
    }

    

}
