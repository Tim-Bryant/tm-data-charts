package com.app.timer.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * 利用HttpClient对象实现发送POST和GET请求
 * 
 * @author liuxf
 * @date 2017-04-01
 * 需要的JAR包列表：
 */
public class HttpClientUtils {
	
	/**
	 * 通过GET方式发起http请求
	 */
	public static String requestByGetMethod(String url)throws Exception {
		StringBuffer result=new StringBuffer();
		// 创建默认的httpClient实例
		CloseableHttpClient httpClient = getHttpClient();
		try {
			String encode = url;
			// 用get方法发送http请求
			HttpGet get = new HttpGet(encode);
			//System.out.println("执行get请求:" + get.getURI());
			CloseableHttpResponse httpResponse = null;
			// 发送get请求
			httpResponse = httpClient.execute(get);
			try {
				// response实体
				HttpEntity entity = httpResponse.getEntity();
				if (null != entity) {
					result.append("【响应状态码:】" + httpResponse.getStatusLine());
					System.out.println("【响应状态码:】" + httpResponse.getStatusLine());
					//System.out.println("-------------------------------------------------");
					//System.out.println("响应内容:" + EntityUtils.toString(entity));
					result.append("【响应内容:】" + EntityUtils.toString(entity));
					//result.append(","+httpResponse.getProtocolVersion());
					//System.out.println(httpResponse.getProtocolVersion()+"-------------------------------------------------");
				}
				return result.toString();
			}catch (Exception e) {
				e.printStackTrace();
			} finally {
				httpResponse.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			try {
				closeHttpClient(httpClient);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "";
	}
	
	 /**
     * POST方式发起http请求
     */
    public static void requestByPostMethod(String url){
        CloseableHttpClient httpClient = getHttpClient();
        try {
            HttpPost post = new HttpPost(url); 
            //这里用上本机的某个工程做测试
            //创建参数列表    
            List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
            list.add(new BasicNameValuePair("j_username", "admin"));
            list.add(new BasicNameValuePair("j_password", "passw0rd"));
            //url格式编码
            UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(list,"UTF-8");
            post.setEntity(uefEntity);
            System.out.println("POST 请求...." + post.getURI());
            //执行请求
            CloseableHttpResponse httpResponse = httpClient.execute(post);
            try{
                HttpEntity entity = httpResponse.getEntity();
                if (null != entity){
                    System.out.println("-------------------------------------------------------");
                    System.out.println(EntityUtils.toString(uefEntity));
                    System.out.println("响应内容:" + EntityUtils.toString(entity));
                    System.out.println("-------------------------------------------------------");
                }
            } finally{
                httpResponse.close();
            }
             
        } catch( UnsupportedEncodingException e){
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally{
            try{
                closeHttpClient(httpClient);               
            } catch(Exception e){
                e.printStackTrace();
            }
        }
         
    }
     
    private static CloseableHttpClient getHttpClient(){
        return HttpClients.createDefault();
    }
     
    private static void closeHttpClient(CloseableHttpClient client) throws IOException{
        if (client != null){
            client.close();
        }
    }
     
    public static void main(String[] args) throws InterruptedException {
    	
	}
	
}
