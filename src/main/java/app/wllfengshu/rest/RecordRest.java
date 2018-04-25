package app.wllfengshu.rest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import app.wllfengshu.exception.NotAcceptableException;
import app.wllfengshu.service.RecordService;
import app.wllfengshu.util.LogUtils;

/**
 * @title 通话记录管理（不提供修改通话记录的接口）
 */
@Controller
@Path("/record")
public class RecordRest {
	
	@Autowired
	private RecordService recordService ;
    
	/**
	 * @title 查询所有通话记录信息
	 * @param sessionId
	 * @param request
	 * @param response
	 * @return
	 */
    @GET
    public Response getRecords(@HeaderParam(value="sessionId") String sessionId,
    		@HeaderParam(value="user_id") String user_id,
    		@HeaderParam(value="tenant_id") String tenant_id,
    		@HeaderParam(value="call_type") String call_type,
    		@QueryParam("token") String token,
    		@QueryParam("pageNo") int pageNo,@QueryParam("pageSize") int pageSize,
    		@Context HttpServletRequest request,@Context HttpServletResponse response) {
		String responseStr = null;
		try{
			responseStr=recordService.getRecords(sessionId,user_id,tenant_id,call_type,token,pageNo,pageSize);
		}catch (NotAcceptableException e) {
			System.out.println(e);
			return Response.serverError().entity("{\"message\":\""+e.getMessage()+"\",\"timestamp\":\""+System.currentTimeMillis()+"\"}").status(406).build();
		}catch (Exception e) {
			System.out.println(e);
			LogUtils.error(this, e, "# RecordRest-getRecords,has exception #");
			return Response.serverError().entity("{\"message\":\"has exception\",\"timestamp\":\""+System.currentTimeMillis()+"\"}").status(500).build();
		}
		return Response.ok(responseStr, MediaType.APPLICATION_JSON)
        		.status(200).build();
    }
    
    /**
     * @title 添加通话记录信息
     * @param record 通话记录信息数据
     * @param sessionId
     * @param request
     * @param response
     * @return
     */
    @POST
    public Response addRecord(String record,
    		@HeaderParam(value="sessionId") String sessionId,
    		@HeaderParam(value="user_id") String user_id,
    		@HeaderParam(value="call_type") String call_type,
    		@Context HttpServletRequest request,@Context HttpServletResponse response) {
		String responseStr = null;
		try{
			responseStr=recordService.addRecord(record,sessionId,user_id);
		}catch (NotAcceptableException e) {
			System.out.println(e);
			return Response.serverError().entity("{\"message\":\""+e.getMessage()+"\",\"timestamp\":\""+System.currentTimeMillis()+"\"}").status(406).build();
		}catch (Exception e) {
			System.out.println(e);
			LogUtils.error(this, e, "# RecordRest-addRecord,has exception #");
			return Response.serverError().entity("{\"message\":\"has exception\",\"timestamp\":\""+System.currentTimeMillis()+"\"}").status(500).build();
		}
		return Response.ok(responseStr, MediaType.APPLICATION_JSON)
        		.status(200).build();
    }
    
    /**
     * @title 查询通话记录详情
     * @param record_id 通话记录ID
     * @param sessionId
     * @param request
     * @param response
     * @return
     */
    @GET
    @Path("/{record_id}/")
    public Response getRecord(@PathParam("record_id")String record_id,
    		@HeaderParam(value="sessionId") String sessionId,
    		@HeaderParam(value="user_id") String user_id,
    		@HeaderParam(value="call_type") String call_type,
    		@Context HttpServletRequest request,@Context HttpServletResponse response) {
		String responseStr = null;
		try{
			responseStr=recordService.getRecord(record_id,sessionId,user_id);
		}catch (NotAcceptableException e) {
			System.out.println(e);
			return Response.serverError().entity("{\"message\":\""+e.getMessage()+"\",\"timestamp\":\""+System.currentTimeMillis()+"\"}").status(406).build();
		}catch (Exception e) {
			System.out.println(e);
			LogUtils.error(this, e, "# RecordRest-getRecord,has exception #");
			return Response.serverError().entity("{\"message\":\"has exception\",\"timestamp\":\""+System.currentTimeMillis()+"\"}").status(500).build();
		}
		return Response.ok(responseStr, MediaType.APPLICATION_JSON)
        		.status(200).build();
    }
    
    /**
     * @title 删除通话记录信息
     * @param record_id 通话记录ID
     * @param sessionId
     * @param request
     * @param response
     * @return
     */
    @DELETE
    @Path("/{record_id}/")
    public Response deleteRecord(@PathParam("record_id")String record_id,
    		@HeaderParam(value="sessionid") String sessionId,
    		@HeaderParam(value="user_id") String user_id,
    		@HeaderParam(value="call_type") String call_type,
    		@Context HttpServletRequest request,@Context HttpServletResponse response) {
		String responseStr = null;
		try{
			responseStr=recordService.deleteRecord(record_id,sessionId,user_id);
		}catch (NotAcceptableException e) {
			System.out.println(e);
			return Response.serverError().entity("{\"message\":\""+e.getMessage()+"\",\"timestamp\":\""+System.currentTimeMillis()+"\"}").status(406).build();
		}catch (Exception e) {
			System.out.println(e);
			LogUtils.error(this, e, "# RecordRest-deleteRecord,has exception #");
			return Response.serverError().entity("{\"message\":\"has exception\",\"timestamp\":\""+System.currentTimeMillis()+"\"}").status(500).build();
		}
		return Response.ok(responseStr, MediaType.APPLICATION_JSON)
        		.status(200).build();
    }
    
}
