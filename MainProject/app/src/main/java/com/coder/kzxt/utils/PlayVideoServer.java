package com.coder.kzxt.utils;

import android.content.Context;
import android.os.Environment;

import com.coder.kzxt.utils.NanoHTTPD.Response.Status;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.Map;
import java.util.Random;

/**
 * 播放本地视频的服务器
 */
public class PlayVideoServer extends NanoHTTPD{

	private Context context;
	private SharedPreferencesUtil spu;
	public PlayVideoServer(Context context) {
		super(37542);
		this.context = context;
		spu = new SharedPreferencesUtil(context);
	}

	@Override
	public Response serve(String uri, Method method, Map<String, String> headers, Map<String, String> parms, Map<String, String> files){
		InputStream is = null;
		String path = Environment.getExternalStorageDirectory().getAbsolutePath();
		String path_two = spu.getSecondSdcard();
		try {
			if(uri.contains(path)) {
				System.out.println("request for media on sdcard"+uri);
				is = new FileInputStream(new File(uri));
				FileNameMap fileNameMap = URLConnection.getFileNameMap();
				String mimeType = fileNameMap.getContentTypeFor(uri);
				Response streamResponse = new Response(Status.OK,mimeType,is);
				Random rnd = new Random();
				String etag = Integer.toHexString(rnd.nextInt());
				streamResponse.addHeader("ETag", etag);
				streamResponse.addHeader("Connection", "Keep-alive");
				return streamResponse;
				
			}else if (uri.contains(path_two)) {
				
				System.out.println("request for media on sdcard"+uri);
				is = new FileInputStream(new File(uri));
				FileNameMap fileNameMap = URLConnection.getFileNameMap();
				String mimeType = fileNameMap.getContentTypeFor(uri);
				Response streamResponse = new Response(Status.OK,mimeType,is);
				Random rnd = new Random();
				String etag = Integer.toHexString(rnd.nextInt());
				streamResponse.addHeader("ETag", etag);
				streamResponse.addHeader("Connection", "Keep-alive");
				return streamResponse;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
