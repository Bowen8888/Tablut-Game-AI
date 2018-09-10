package otherPeople;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import boardgame.Move;
import tablut.TablutBoardState;
import tablut.TablutMove;
import tablut.TablutPlayer;

/** A player file submitted by a student. */
public class Dan extends TablutPlayer {

    /**
     * You must modify this constructor to return your student number. This is
     * important, because this is what the code that runs the competition uses to
     * associate you with your agent. The constructor should do nothing else.
     */
    public Dan() {
        super("260743330");
    }

    /**
     * This is the primary method that you need to implement. The ``boardState``
     * object contains the current state of the game, which your agent must use to
     * make decisions.
     */
    
    public static class Result{
    	private Move move = null;
    	
    	public void setMove(Move aMove){
    		move = aMove;
    	}
    	
    	public Move getMove(){
    		return move;
    	}
    }
    public Move chooseMove(TablutBoardState boardState) {
    	Result result = new Result();
    	result.setMove(boardState.getRandomMove());
    	ExecutorService threadPool = Executors.newSingleThreadExecutor();
    	//Timer timer = new Timer();
    	//SearchingThread searchingThread = new SearchingThread(boardState, player_id, 1-player_id, result);
    	//timer.schedule(searchingThread, LocalDateTime.now(), 1950);
    	try {
    		Future<?> search = threadPool.submit(new SearchingThread(boardState, player_id, 1-player_id, result));
//    		if(boardState.getTurnNumber()==0){
//    			search.get(29950, TimeUnit.MILLISECONDS);
//    		}
//    		else{
    			search.get(1950, TimeUnit.MILLISECONDS);
//    		}
        }
        catch (TimeoutException e){
        	threadPool.shutdownNow();
        	//return result.getMove();
        	System.out.println("TimeOut");
        }
        catch (Exception e){
        	threadPool.shutdownNow();
        	System.out.println("OtherMoves");
        	//return result.getMove();
//        	e.printStackTrace();
        }
        finally{
        	//shut down the executor service now
        	threadPool.shutdownNow();
        	//return result.getMove();
        }
    	return result.getMove();
    }
}