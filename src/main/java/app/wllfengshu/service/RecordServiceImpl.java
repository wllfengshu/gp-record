package app.wllfengshu.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import app.wllfengshu.dao.RecordDao;
import app.wllfengshu.entity.Record;
import app.wllfengshu.exception.NotAcceptableException;
import app.wllfengshu.util.AuthUtil;
import net.sf.json.JSONObject;

@Service
public class RecordServiceImpl implements RecordService {
	private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	
	@Autowired
	private RecordDao recordDao;
	
	@Override
	public String getRecords(String sessionId,String user_id) throws NotAcceptableException {
		Map<String,Object> responseMap = new HashMap<String,Object>();
		AuthUtil.instance.checkUserInfo(sessionId, user_id);
		List<Record> records = recordDao.getRecords(user_id);
		responseMap.put("data", records);
		responseMap.put("count", records.size());
		responseMap.put("timestamp", String.valueOf(System.currentTimeMillis()));
		return gson.toJson(responseMap);
	}
	
	@Override
	public String addRecord(String record,String sessionId,String user_id) throws NotAcceptableException {
		Map<String,Object> responseMap = new HashMap<String,Object>();
		String record_id = UUID.randomUUID().toString();
		AuthUtil.instance.checkUserInfo(sessionId, user_id);
		JSONObject recordJson = null;
		Record recordTemp=null;
		try{
			recordJson=JSONObject.fromObject(record);
			recordTemp=(Record) JSONObject.toBean(recordJson,Record.class);
			recordTemp.setId(record_id);
			recordTemp.setUser_id(user_id);
		}catch(Exception e){
			throw new NotAcceptableException("数据格式错误");
		}
		recordDao.addRecord(recordTemp);
		responseMap.put("timestamp", String.valueOf(System.currentTimeMillis()));
		return gson.toJson(responseMap);
	}
	
	@Override
	public String getRecord(String record_id,String sessionId,String user_id) throws NotAcceptableException {
		Map<String,Object> responseMap = new HashMap<String,Object>();
		AuthUtil.instance.checkUserInfo(sessionId, user_id);
		Record record = recordDao.getRecord(record_id);
		responseMap.put("data", record);
		responseMap.put("timestamp", String.valueOf(System.currentTimeMillis()));
		return gson.toJson(responseMap);
	}
	
	@Override
	public String deleteRecord(String record_id,String sessionId,String user_id) throws NotAcceptableException {
		Map<String,Object> responseMap = new HashMap<String,Object>();
		AuthUtil.instance.checkUserInfo(sessionId, user_id);
		recordDao.deleteRecord(record_id);
		responseMap.put("timestamp", String.valueOf(System.currentTimeMillis()));
		return gson.toJson(responseMap);
	}

}
