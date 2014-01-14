
/**
 * 
 * @author Yananh
 *
 */
public interface IXTask<Result> extends Comparable<IXTask<?>>, Runnable{
    /**
     * Indicates the current status of the task. Each status will be set only once
     * during the lifetime of a task.
     */
    public enum Status {
        /**
         * Indicates that the task has not been executed yet.
         */
        PENDING,
        /**
         * Indicates that the task is running.
         */
        RUNNING,
        /**
         * Indicates that task has finished.
         */
        FINISHED,
    }
    
	public Result runInBackground() throws InterruptedException, CanceledExcpetion;
	
	public void isCanceled();
	
	public void cancel();
	
	public void onProgressUpdate(Object ... data);
	
	/**
	 * Invoke this function before running in background.
	 */
	public  void onPreExecute();
	
	/**
	 * When some exception occurred, call this function to notify the observer.
	 * @see #{@link #onCanceled(String)}
	 * @see #{@link #onPostResult(Result)}
	 * @param e
	 */
	public void onException(Exception e);
	
	/**
	 * Invoke this function when user canceled this task.
	 * @see #{@link #onException(Exception)}
	 * @see #{@link #onPostResult(Result)}
	 * @param info
	 */
	public void onCanceled(String info);
	
	/**
	 * Invoke this function when this task running complete.
	 * 
	 * @see #{@link #onException(Exception)}
	 * @see #{@link #onPostResult(Result)}
	 * @param result
	 */
	public void onPostResult(Result result);
}
