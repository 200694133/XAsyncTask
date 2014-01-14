import java.util.concurrent.atomic.AtomicBoolean;


public class XTask implements IXTask{
	private final AtomicBoolean mCancelled = new AtomicBoolean();
	private final AtomicBoolean mTaskInvoked = new AtomicBoolean();
	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object runInBackground() throws InterruptedException,
			CanceledExcpetion {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void isCanceled() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cancel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProgressUpdate(Object... data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPreExecute() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onException(Exception e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCanceled(String info) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPostResult(Object result) {
		// TODO Auto-generated method stub
		
	}

}
