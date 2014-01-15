import java.util.concurrent.CancellationException;


/**
 * 
 * @author Yananh
 *
 */
public interface IXTask<Result, ErrorInfo> extends Comparable<IXTask<?,?>>{
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
    
    public Status getStatus();
    
	public Result runInBackground() throws InterruptedException, CancellationException;
	
	public void setStatus(Status status);
	
	public boolean isCanceled();
	
	public int getPriority();
	
	public void setPriority(int p);
	
	/**
	 * Check if the current task has canceled.
	 */
	public void cancel();
	
	public void checkIfCanceled() throws InterruptedException, CancellationException;
	
	/**
	 * Check if the current task has finished.
	 * @return 
	 */
	public boolean isFinished();
	
	public void onProgressUpdate(Object ... data);
	
//	/**
//	 * Invoke this function before running in background.
//	 */
//	public  void onPreExecute();
	
	/**
	 * When some exception occurred, call this function to notify the observer.
	 * <b>It's must work in the background thread.</b>
	 * @see #{@link #onCanceled(String)}
	 * @see #{@link #onResult(Result)}
	 * @param e
	 */
	public void onException(ErrorInfo errorInfo);
	
	/**
	 * Invoke this function when user canceled this task.
	 * <b>It's must work in the background thread.</b>
	 * @see #{@link #onException(Exception)}
	 * @see #{@link #onResult(Result)}
	 * @param info
	 */
	public void onCanceled(ErrorInfo errorInfo);
	
	/**
	 * Invoke this function when this task running complete.
	 * <b>It's must work in the background thread.</b>
	 * @see #{@link #onException(Exception)}
	 * @see #{@link #onPostResult(Result)}
	 * @param result the result after calculate.
	 */
	public void onResult(Result result);
}
