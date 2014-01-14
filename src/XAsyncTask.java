import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


public class XAsyncTask {
	private static final String LOG_TAG = XAsyncTask.class.getSimpleName();

    private static final int CORE_POOL_SIZE = 5;
    private static final int MAXIMUM_POOL_SIZE = 128;
    private static final int KEEP_ALIVE = 1;
	
    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            return new Thread(r, "XAsyncTask #" + mCount.getAndIncrement());
        }
    };
	
    /**
     * work Queue.
     */
	private final BlockingQueue<? extends Runnable> mPoolWorkQueue = new PriorityBlockingQueue<IXTask<?>>();
	
	/**
	 * Thread pool.
	 */
	private Executor mWorkExecutor = null;
	
	public XAsyncTask(int capability){
		mWorkExecutor = new  ThreadPoolExecutor(CORE_POOL_SIZE, 
				MAXIMUM_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS, 
				(BlockingQueue<Runnable>) mPoolWorkQueue, sThreadFactory);
		
	}
	
	
	public void clearAllTask(){
		
	}
	
	public void post(IXTask<?> task){
		
	}
	
	public void cancel(IXTask<?> task){
		
	}
	
	
	
	
	
	
	

}
