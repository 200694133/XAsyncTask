package com.hyn.xtask;
import java.util.concurrent.CancellationException;
import java.util.concurrent.atomic.AtomicBoolean;


public abstract class XTask<Result, ErrorInfo> implements IXTask<Result, ErrorInfo>{
	private static final String LOG_TAG = XTask.class.getSimpleName();
	private static final int HIGH_PRIORITY = 0;
	private static final int MIDLLE_PRIORITY = 10;
	private static final int LOW_PRIORITY = 20;
	private int mPriority = MIDLLE_PRIORITY;
	private Status mStatus = Status.PENDING;
	private final AtomicBoolean mCancelled = new AtomicBoolean();
	
	public XTask(){
		
	}
	
	/**
	 * Compare two task by priority, in order to enqueue the list by seqence.
	 */
	@Override
	public int compareTo(IXTask<Result, ErrorInfo> o) {
		if(null == o) throw new NullPointerException("");
		int p1 = this.getPriority();
		int p2 = o.getPriority();
		if(p1 <= p2){
			return -1;
		}
		return 1;
	}


	@Override
	public IXTask.Status getStatus() {
		return mStatus;
	}

	@Override
	public void setStatus(IXTask.Status status) {
		mStatus = status;
	}


	@Override
	public boolean isCanceled() {
		return mCancelled.get();
	}


	@Override
	public int getPriority() {
		return mPriority;
	}


	@Override
	public void setPriority(int p) {
		mPriority = p;
	}


	@Override
	public void cancel() {
		mCancelled.set(true);
	}


	@Override
	public void checkIfCanceled() throws InterruptedException,
			CancellationException {
		if(mCancelled.get()){
			throw new CancellationException("Task has been canceled.");
		}
	}


	@Override
	public boolean isFinished() {
		return mStatus == Status.FINISHED;
	}


	@Override
	public void onProgressUpdate(Object... data) {
		
	}

	@Override
	public void onException(ErrorInfo errorInfo) {
		
	}
	
	@Override
	public void onCanceled(ErrorInfo errorInfo) {
		
	}

	@Override
	public void onResult(Result result) {
		
	}
}
