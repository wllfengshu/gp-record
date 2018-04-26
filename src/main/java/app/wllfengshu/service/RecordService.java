package app.wllfengshu.service;

import app.wllfengshu.exception.NotAcceptableException;

public interface RecordService {
	
	public String getRecords(String sessionId,String user_id,String tenant_id, String call_type,String token,String domain,String ani,String dnis,int pageNo,int pageSize) throws NotAcceptableException;

	public String addRecord(String record,String sessionId,String user_id) throws NotAcceptableException;

	public String getRecord(String record_id,String sessionId,String user_id) throws NotAcceptableException;

	public String deleteRecord(String record_id,String sessionId,String user_id) throws NotAcceptableException;

}
