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
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class RecordServiceImpl implements RecordService {
	private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	
	@Autowired
	private RecordDao recordDao;
	
	@Override
	public String getRecords(String sessionId,String user_id,String tenant_id,String call_type,String token,String domain,String ani,String dnis,int pageNo,int pageSize) throws NotAcceptableException {
		Map<String,Object> responseMap = new HashMap<String,Object>();
		JSONObject user = AuthUtil.instance.getUser(sessionId, user_id);
		if (null==user || user.isNullObject()) {
			throw new NotAcceptableException("没有权限");
		}
		if (!token.equals("manage")) {//后台管理员不做角色判断
			JSONArray roles = user.getJSONArray("roles");
			JSONObject role = roles.getJSONObject(0);
			String role_name=role.getString("role_name");
			if (!"agent".equals(role_name) && !"tm".equals(role_name)) {//允许坐席和租户管理员查看通话记录
				throw new NotAcceptableException("角色异常");
			}
		}
		List<Record> records =null;
		int count=0;
		if (token.equals("crm")) {//使用crm系统的用户，只能查询属于自己数据
			records = recordDao.getRecords(user_id,"",call_type,domain,ani,dnis,(pageNo-1)*pageSize,pageSize);
			count = recordDao.getCount(user_id,"",call_type,domain,ani,dnis);
		}else if(token.equals("tm")){//使用tm系统的用户，可以查询当前租户下所有数据
			records = recordDao.getRecords("",tenant_id,call_type,domain,ani,dnis,(pageNo-1)*pageSize,pageSize);
			count = recordDao.getCount("",tenant_id,call_type,domain,ani,dnis);
		}else if (token.equals("manage")) {//使用manage系统的用户，查询数据库中所有数据
			records = recordDao.getRecords("","",call_type,domain,ani,dnis,(pageNo-1)*pageSize,pageSize);
			count = recordDao.getCount("","",call_type,domain,ani,dnis);
		}else{//其他token直接返回失败
			throw new NotAcceptableException("凭证异常");
		}
		responseMap.put("data", records);
		responseMap.put("count", count);
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
