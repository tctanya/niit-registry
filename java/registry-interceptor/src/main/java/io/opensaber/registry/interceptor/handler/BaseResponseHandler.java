package io.opensaber.registry.interceptor.handler;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.google.gson.Gson;

import io.opensaber.pojos.Response;
import io.opensaber.pojos.ResponseParams;

/**
 * 
 * @author jyotsna
 *
 */
public class BaseResponseHandler {
	
	protected HttpServletResponse response;
	protected ResponseWrapper responseWrapper;
	
	public void setResponse(HttpServletResponse response) throws IOException{
		this.response = response;
		setResponseWrapper();
	}
	
	public void setResponseWrapper() throws IOException{
		responseWrapper = new ResponseWrapper(response);
	}
	
	public void writeResponseBody(String content) throws IOException{
		//setResponseWrapper();
		responseWrapper.writeResponseBody(content);
		response = (HttpServletResponse)responseWrapper.getResponse();
	}

	public HttpServletResponse getResponse(){
		return response;
	}
	
	public String getResponseContent() throws IOException{
		//setResponseWrapper();
		return responseWrapper.getResponseContent();
	}
	
	public Map<String,Object> getResponseHeaderMap() throws IOException{
		//setResponseWrapper();
		Map<String,Object> responseHeaderMap = new HashMap<String,Object>();
		Collection<String> headerNames = responseWrapper.getHeaderNames();
		if(headerNames!=null){
			for(String header: headerNames){
				responseHeaderMap.put(header, responseWrapper.getHeader(header));
			}
		}
		return responseHeaderMap;
	}
	
	public void writeResponseObj(Gson gson, String errMessage) throws IOException{
		responseWrapper.setStatus(HttpStatus.OK.value());
		responseWrapper.setContentType(MediaType.APPLICATION_JSON_VALUE);
		writeResponseBody(gson.toJson(setErrorResponse(errMessage)));
	}
	
	public Response setErrorResponse(String message){
		ResponseParams responseParams = new ResponseParams();
		Response response = new Response(Response.API_ID.NONE, "OK", responseParams);
		Map<String, Object> result = new HashMap<>();
		response.setResult(result);
		responseParams.setStatus(Response.Status.UNSUCCESSFUL);
		responseParams.setErrmsg(message);
		return response;
	}
}
