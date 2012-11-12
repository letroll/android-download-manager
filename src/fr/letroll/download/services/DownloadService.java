package fr.letroll.download.services;

import com.magic.debug.Logger;

import fr.letroll.download.services.IDownloadService;

import fr.letroll.download.utils.MyIntents;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;

public class DownloadService extends Service {

	private DownloadManager mDownloadManager;

	@Override
	public IBinder onBind(Intent intent) {
		return new DownloadServiceImpl();
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mDownloadManager = new DownloadManager(this);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Logger.loge(this, "onStartCommand");
		if (intent.getAction().equals(MyIntents.DownloadService)) {
			int type = intent.getIntExtra(MyIntents.TYPE, -1);
			String url;

			switch (type) {
			case MyIntents.Types.START:
				if (!mDownloadManager.isRunning()) {
					mDownloadManager.startManage();
				} else {
					mDownloadManager.reBroadcastAddAllTask();
				}
				break;
			case MyIntents.Types.ADD:
				url = intent.getStringExtra(MyIntents.URL);
				if (!TextUtils.isEmpty(url) && !mDownloadManager.hasTask(url)) {
					mDownloadManager.addTask(url);
				}
				break;
			case MyIntents.Types.CONTINUE:
				url = intent.getStringExtra(MyIntents.URL);
				if (!TextUtils.isEmpty(url)) {
					mDownloadManager.continueTask(url);
				}
				break;
			case MyIntents.Types.DELETE:
				url = intent.getStringExtra(MyIntents.URL);
				if (!TextUtils.isEmpty(url)) {
					mDownloadManager.deleteTask(url);
				}
				break;
			case MyIntents.Types.PAUSE:
				url = intent.getStringExtra(MyIntents.URL);
				if (!TextUtils.isEmpty(url)) {
					mDownloadManager.pauseTask(url);
				}
				break;
			case MyIntents.Types.STOP:
				mDownloadManager.close();
				break;
			case MyIntents.Types.EMPTY:
				mDownloadManager.empty();
				break;
			default:
				break;
			}
		}
		return super.onStartCommand(intent, flags, startId);
	}

	private class DownloadServiceImpl extends IDownloadService.Stub {
		@Override
		public void startManage() throws RemoteException {
			mDownloadManager.startManage();
		}

		@Override
		public void addTask(String url) throws RemoteException {
			mDownloadManager.addTask(url);
		}

		@Override
		public void pauseTask(String url) throws RemoteException {
			mDownloadManager.pauseTask(url);
		}

		@Override
		public void pauseTasks() throws RemoteException {
			mDownloadManager.close();
		}

		@Override
		public void deleteTask(String url) throws RemoteException {
			mDownloadManager.deleteTask(url);
		}

		@Override
		public void continueTask(String url) throws RemoteException {
			mDownloadManager.continueTask(url);
		}

		@Override
		public void deleteTasks() throws RemoteException {
			mDownloadManager.empty();
		}

	}

}
