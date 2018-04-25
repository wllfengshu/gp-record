package app.wllfengshu.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import app.wllfengshu.entity.Record;

@Repository
public interface RecordDao {
	public List<Record> getRecords(@Param("user_id")String user_id,@Param("call_type")String call_type);

	public void addRecord(@Param("record")Record record);

	public Record getRecord(@Param("id")String id);

	public void deleteRecord(@Param("id")String id);
	
}
