package com.hyn.xtask;
import java.util.WeakHashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;


public class XAsyncTask<Result, ErrorInfo> {
	private static final String LOG_TAG = XAsyncTask.class.getSimpleName();
	private static final AtomicInteger sPoolSize = new AtomicInteger(0);
    private static final int CORE_POOL_SIZE = 5;
    private static final int MAXIMUM_POOL_SIZE = 128;
    private static final int KEEP_ALIVE = 1;
    
    private static final int QUEUE_THRESHOLD = 5;
	
    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
        	sPoolSize.incrementAndGet();
            return new Thread(r, "XAsyncTask #" + mCount.getAndIncrement());
        }
    };
	
    /**
     * work Queue.
     */
	private final BlockingQueue<IXTask<Result, ErrorInfo>> mPoolWaitingQueue = new PriorityBlockingQueue<IXTask<Result, ErrorInfo>>();
	private final BlockingQueue<Runnable> mWorkQueue = new LinkedBlockingQueue<Runnable>(QUEUE_THRESHOLD<<2);
	ReentrantLock mWorkQueueLock = new ReentrantLock();
	private final WeakHashMap<IXTask<Result, ErrorInfo>, WorkFutureTask<Result, ErrorInfo>> mTaskFutureMap = 
			new WeakHashMap<IXTask<Result, ErrorInfo>, WorkFutureTask<Result, ErrorInfo>>();
	/**
	 * Thread pool.
	 */
	private Executor mWorkExecutor = null;
	
	public XAsyncTask(int capability){
		mWorkExecutor = new  ThreadPoolExecutor(CORE_POOL_SIZE,  MAXIMUM_POOL_SIZE, KEEP_ALIVE, 
				TimeUnit.SECONDS,  mWorkQueue, sThreadFactory);
		
		
	}
	
	private void sheduleNext(){
		mWorkQueueLock.lock();
		if(mWorkQueue.size() <= QUEUE_THRESHOLD){
			IXTask<Result, ErrorInfo> task = mPoolWaitingQueue.poll();
			if(null != task){
				WorkFutureTask<Result, ErrorInfo> futuretask = parseSubmitTask(task);
				mTaskFutureMap.put(task, futuretask);
				mWorkExecutor.execute(futuretask);
			}
		}
		mWorkQueueLock.unlock();
	}
	
	private WorkFutureTask<Result, ErrorInfo> parseSubmitTask(IXTask<Result, ErrorInfo> task){
		WorkFutureTask<Result, ErrorInfo> futureTask = new WorkFutureTask<Result, ErrorInfo>(task){
			@Override
			protected void done() {
				try {
					Result result = get();
					if(mTask.isCanceled()){
						notifyCancelComplete(mTask);
					}else{
						notifyResult(mTask, result);
					}
				} catch (CancellationException e) {
					notifyCancelComplete(mTask);
				} catch(Throwable e){
					if(mTask.isCanceled()){
						notifyCancelComplete(mTask);
					}else{
						notifyExcption(mTask, null);
					}
				}finally{
					mTask.setStatus(IXTask.Status.FINISHED);
					sheduleNext();
				}
			}
		};
		return futureTask;
	}
	
	
	public void post(IXTask<Result, ErrorInfo> task){
		mPoolWaitingQueue.offer(task);
		sheduleNext();
	}
	
	public void cancel(IXTask<Result, ErrorInfo> task){
		mWorkQueueLock.lock();
		if(mPoolWaitingQueue.contains(task)){
			mPoolWaitingQueue.remove(task);
			XLog.i(LOG_TAG, "the task is not running, so just delete it from queue, and no need to do any thing else.");
			mWorkQueueLock.unlock();
			return;
		}
		WorkFutureTask<Result, ErrorInfo> futuretask = mTaskFutureMap.get(task);
		if (null == futuretask) return;
		IXTask.Status status = task.getStatus();
		if (status == IXTask.Status.FINISHED) {
			XLog.d(LOG_TAG, "the task  is finished.");
		} else if (status == IXTask.Status.PENDING) {
			XLog.d(LOG_TAG, "the task  is not running.");
			futuretask.cancel(true);
		} else {
			XLog.d(LOG_TAG, "the task  is running, cancel it.");
			futuretask.cancel(true);
		}
	}
	
	public void clearAllTask(){
		
	}
	
	private void notifyExcption(IXTask<Result, ErrorInfo> task, ErrorInfo info){
		task.onException(info);
	}
	
	private void notifyResult(IXTask<Result, ErrorInfo> task, Result result){
		task.onResult(result);
	}
	
	public void notifyCancelComplete(IXTask<Result, ErrorInfo> task){
		task.onCanceled(null);
	}
	
	private static abstract class WorkFutureTask<Result, ErrorInfo> extends FutureTask<Result>{
		protected IXTask<Result, ErrorInfo> mTask = null;
		public WorkFutureTask(IXTask<Result, ErrorInfo> task) {
			super(new WorkCallable<Result, ErrorInfo>(task));
		}
		IXTask<Result, ErrorInfo> getTask(){
			return mTask;
		}
		
        protected  abstract void done();
	};
	
	private static class WorkCallable<Result, ErrInfo> implements Callable<Result>{
		private IXTask<Result, ErrInfo> mTask = null;
		WorkCallable(IXTask<Result, ErrInfo> task){
			mTask = task;
		}
		@Override
		public Result call() throws Exception {
			mTask.setStatus(IXTask.Status.RUNNING);
			Result r =  mTask.runInBackground();
			mTask.setStatus(IXTask.Status.FINISHED);
			return r;
		}
	};
}
