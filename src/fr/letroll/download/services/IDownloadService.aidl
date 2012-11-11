package fr.letroll.download.services;

interface IDownloadService {
	
	void startManage();
	
	void addTask(String url);
	
	void pauseTask(String url);
	
	void pauseTasks();
	
	void deleteTask(String url);

	void deleteTasks();	
	
	void continueTask(String url);
}
