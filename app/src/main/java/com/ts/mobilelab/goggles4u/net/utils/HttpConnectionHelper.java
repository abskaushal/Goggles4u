package com.ts.mobilelab.goggles4u.net.utils;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ts.mobilelab.goggles4u.data.AppConstants;
import com.ts.mobilelab.goggles4u.logs.Logger;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

@SuppressWarnings("deprecation")
public class HttpConnectionHelper {

	/**
	 *
	 */
	public HttpConnectionHelper(Context context) {
		mContext = context;
	}

	/**
	 * isNetworkAvailable is used to Check n/w connection available in Device.
	 *
	 * @return Boolean
	 **/
	public boolean isNetworkAvailable() {
		boolean available = false;
		/** Getting the system's connectivity service */
		ConnectivityManager connMgr = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		/** Getting active network interface to get the network's status */
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

		if (networkInfo != null && networkInfo.isAvailable())
			available = true;

		/** Returning the status of the network */
		return available;
	}

	public void connectionDisconnect() {
		if (mHttpURLConnection != null) {
			mHttpURLConnection.disconnect();
			mHttpURLConnection = null;
			sInputStream = null;
		}
	}

	//post

	public JSONObject getConnection(String url, JSONObject jsonData) {
		try {

			Log.v("url===", "" + url);
			Log.v("jsonData=", "" + jsonData);
			Logger.addRecordToLog("url : " + url);
			Logger.addRecordToLog("jsonData : " +jsonData);
			URL urlToConnect = new URL(url);
			mHttpURLConnection = (HttpURLConnection) urlToConnect
					.openConnection();
			mHttpURLConnection.setReadTimeout(20000); /* milliseconds */
			mHttpURLConnection.setConnectTimeout(30000); /* milliseconds */

			mHttpURLConnection.setRequestProperty("Cache-Control", "no-cache");
			mHttpURLConnection.setRequestProperty("Content-Type",
					"application/json");
			mHttpURLConnection.setRequestProperty("Accept", "application/json");
			mHttpURLConnection.setRequestProperty("Connection", "close");
			if(jsonData== null){
				mHttpURLConnection.setRequestMethod("GET");
			}else{
				mHttpURLConnection.setRequestMethod("POST");
			}


			mHttpURLConnection.setDoInput(true);
			mHttpURLConnection.setDoOutput(true);
			mHttpURLConnection.connect();
			//Log.v("ResponseCode===", "" + mHttpURLConnection.getResponseCode());
			if (jsonData != null) {
				OutputStreamWriter os = new OutputStreamWriter(
						mHttpURLConnection.getOutputStream(), "UTF-8");
				os.write(jsonData.toString());
				os.flush();
				os.close();
			}
			sInputStream = mHttpURLConnection.getInputStream();

			Log.v("sInputStream=", "" + sInputStream);
			if (sInputStream == null) {
				throw new IOException();
			}
			Log.v("jsonData===", "" + mHttpURLConnection.getResponseCode());
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					sInputStream, "UTF-8"), 32);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");

			}
			//Log.v("sb===", "" + sb);
			sInputStream.close();
			sJson = sb.toString();
			Logger.addRecordToLog("sJson : " +sJson);
			Log.v("sJson===", "" + sJson);
			if (sJson != null) {
				if (sJson.length() != 0) {

					try {

						sJsonObj = new JSONObject(sJson.substring(
								sJson.indexOf("{"), sJson.lastIndexOf("}") + 1));
						//ststusMessage = KenConstants.SUCCESSFUL;
					} catch (StringIndexOutOfBoundsException e) {
						// mExceptionCode =
						// InspectConstants.EXCEPTION_CODE_FOR_UNSUPPORTED_ENCODING;
						connectionDisconnect();
						//ststusMessage = KenConstants.UNABLE_TO_PROCESS;
						return null;
					}
				} else {
					throw new UnsupportedEncodingException();

				}

			} else {
				throw new UnsupportedEncodingException();
			}

		} catch (UnsupportedEncodingException e) {
			Log.i("Goggles4u",
					"UnsupportedEncodingException:" + e.getMessage());
			Logger.addRecordToLog("UnsupportedEncodingException : " + e.getMessage());
			e.printStackTrace();
			ststusMessage = AppConstants.UNABLE_TO_PROCESS;
			connectionDisconnect();
			return null;

		} catch (SocketTimeoutException e) {
			Log.i("Goggles4u", "SocketTimeoutException:" + e.getMessage());
			Logger.addRecordToLog("SocketTimeoutException : " + e.getMessage());
			e.printStackTrace();
			connectionDisconnect();
			ststusMessage = AppConstants.TIME_OUT;
			return null;
		} catch (SocketException e) {
			Log.i("Goggles4u", "SocketException:" + e.getMessage());
			Logger.addRecordToLog("SocketException : " + e.getMessage());
			e.printStackTrace();
			connectionDisconnect();
			ststusMessage = AppConstants.UNABLE_TO_PROCESS;
			return null;
		} catch (FileNotFoundException e) {
			Log.i("Goggles4u", "FileNotFoundException:" + e.getMessage());
			Logger.addRecordToLog("FileNotFoundException : " + e.getMessage());
			try {
				Log.v("jsonData===", "filenot" + mHttpURLConnection.getResponseCode());

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
			connectionDisconnect();
			ststusMessage = AppConstants.UNABLE_TO_PROCESS;
			return null;
		} catch (IOException e) {

			Log.i("Goggles4u", "IOException:" + e.getMessage());
			Logger.addRecordToLog("IOException : " + e.getMessage());
			e.printStackTrace();
			connectionDisconnect();

			ststusMessage = AppConstants.UNABLE_TO_PROCESS;
			return null;
		} catch (JSONException e) {
			Log.i("Goggles4u", "JSONException:" + e.getMessage());
			Logger.addRecordToLog("JSONException : " + e.getMessage());
			e.printStackTrace();
			connectionDisconnect();
			ststusMessage = AppConstants.UNABLE_TO_PROCESS;
			return null;
		}

		return sJsonObj;
	}

	public JSONObject getConnectionGet(String url) {

		try {

			Log.v("url===", "" + url);

			URL urlToConnect = new URL(url);
			mHttpURLConnection = (HttpURLConnection) urlToConnect
					.openConnection();
			mHttpURLConnection.setReadTimeout(20000); /* milliseconds */
			mHttpURLConnection.setConnectTimeout(30000); /* milliseconds */

			mHttpURLConnection.setRequestProperty("Cache-Control", "no-cache");
			mHttpURLConnection.setRequestProperty("Content-Type",
					"application/json");
			mHttpURLConnection.setRequestProperty("Accept", "application/json");
			mHttpURLConnection.setRequestProperty("Connection", "close");
			mHttpURLConnection.setRequestMethod("POST");

			mHttpURLConnection.setDoInput(true);
			mHttpURLConnection.setDoOutput(true);
			mHttpURLConnection.connect();
			//Log.v("ResponseCode===", "" + mHttpURLConnection.getResponseCode());
			/*if (jsonData != null) {
				OutputStreamWriter os = new OutputStreamWriter(
						mHttpURLConnection.getOutputStream(), "UTF-8");
				os.write(jsonData.toString());
				os.flush();
				os.close();
			}*/
			sInputStream = mHttpURLConnection.getInputStream();

			Log.v("sInputStream=", "" + sInputStream);
			if (sInputStream == null) {
				throw new IOException();
			}
			Log.v("jsonData===", "" + mHttpURLConnection.getResponseCode());
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					sInputStream, "UTF-8"), 32);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");

			}
			Log.v("sb===", "" + sb);
			sInputStream.close();
			sJson = sb.toString();

			Log.v("sJson===", "" + sJson);
			if (sJson != null) {
				if (sJson.length() != 0) {

					try {

						sJsonObj = new JSONObject(sJson.substring(
								sJson.indexOf("{"), sJson.lastIndexOf("}") + 1));
						//ststusMessage = KenConstants.SUCCESSFUL;
					} catch (StringIndexOutOfBoundsException e) {
						// mExceptionCode =
						// InspectConstants.EXCEPTION_CODE_FOR_UNSUPPORTED_ENCODING;
						connectionDisconnect();
						//ststusMessage = KenConstants.UNABLE_TO_PROCESS;
						return null;
					}
				} else {
					throw new UnsupportedEncodingException();

				}

			} else {
				throw new UnsupportedEncodingException();
			}

		} catch (UnsupportedEncodingException e) {
			Log.i("Goggles4u",
					"UnsupportedEncodingException:" + e.getMessage());
			e.printStackTrace();
			ststusMessage = AppConstants.UNABLE_TO_PROCESS;
			connectionDisconnect();
			return null;

		} catch (SocketTimeoutException e) {
			Log.i("Goggles4u", "SocketTimeoutException:" + e.getMessage());
			e.printStackTrace();
			connectionDisconnect();
			ststusMessage = AppConstants.TIME_OUT;
			return null;
		} catch (SocketException e) {
			Log.i("Goggles4u", "SocketException:" + e.getMessage());
			e.printStackTrace();
			connectionDisconnect();
			ststusMessage = AppConstants.UNABLE_TO_PROCESS;
			return null;
		} catch (FileNotFoundException e) {
			Log.i("Goggles4u", "IOException:" + e.getMessage());
			try {
				Log.v("jsonData===", "filenot" + mHttpURLConnection.getResponseCode());

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
			connectionDisconnect();
			ststusMessage = AppConstants.UNABLE_TO_PROCESS;
			return null;
		} catch (IOException e) {

			Log.i("Goggles4u", "IOException:" + e.getMessage());

			e.printStackTrace();
			connectionDisconnect();

			ststusMessage = AppConstants.UNABLE_TO_PROCESS;
			return null;
		} catch (JSONException e) {
			Log.i("Goggles4u", "JSONException:" + e.getMessage());
			e.printStackTrace();
			connectionDisconnect();
			ststusMessage = AppConstants.UNABLE_TO_PROCESS;
			return null;
		}
		return sJsonObj;
	}

	private String ststusMessage = "";

	public String getMessage() {
		return ststusMessage;
	}

	private Context mContext;
	private InputStream sInputStream;
	private String sJson;
	private JSONObject sJsonObj;
	private HttpURLConnection mHttpURLConnection;
	static String mJson;


	public JSONObject getConnectionData(String url, JSONArray jsonArray) {
		// TODO Auto-generated method stub
		try {

			Log.v("url===", "" + url);
			Log.v("jsonArray==", "" + jsonArray);
			URL urlToConnect = new URL(url);
			mHttpURLConnection = (HttpURLConnection) urlToConnect
					.openConnection();
			mHttpURLConnection.setReadTimeout(20000); /* milliseconds */
			mHttpURLConnection.setConnectTimeout(30000); /* milliseconds */

			mHttpURLConnection.setRequestProperty("Cache-Control", "no-cache");
			mHttpURLConnection.setRequestProperty("Content-Type",
					"application/json");
			mHttpURLConnection.setRequestProperty("Accept", "application/json");
			mHttpURLConnection.setRequestProperty("Connection", "close");
			mHttpURLConnection.setRequestMethod("POST");

			mHttpURLConnection.setDoInput(true);
			mHttpURLConnection.setDoOutput(true);
			mHttpURLConnection.connect();
			//Log.v("ResponseCode===", "" + mHttpURLConnection.getResponseCode());
			if (jsonArray != null) {
				OutputStreamWriter os = new OutputStreamWriter(
						mHttpURLConnection.getOutputStream(), "UTF-8");
				os.write(jsonArray.toString());
				os.flush();
				os.close();
			}
			sInputStream = mHttpURLConnection.getInputStream();

			Log.v("sInputStream=", "" + sInputStream);
			if (sInputStream == null) {
				throw new IOException();
			}
			Log.v("jsonData===", "" + mHttpURLConnection.getResponseCode());
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					sInputStream, "UTF-8"), 32);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");

			}
			Log.v("sb===", "" + sb);
			sInputStream.close();
			sJson = sb.toString();

			Log.v("sJson===", "" + sJson);
			if (sJson != null) {
				if (sJson.length() != 0) {

					try {

						sJsonObj = new JSONObject(sJson.substring(
								sJson.indexOf("{"), sJson.lastIndexOf("}") + 1));
						//ststusMessage = KenConstants.SUCCESSFUL;
					} catch (StringIndexOutOfBoundsException e) {
						// mExceptionCode =
						// InspectConstants.EXCEPTION_CODE_FOR_UNSUPPORTED_ENCODING;
						connectionDisconnect();
						//ststusMessage = KenConstants.UNABLE_TO_PROCESS;
						return null;
					}
				} else {
					throw new UnsupportedEncodingException();

				}

			} else {
				throw new UnsupportedEncodingException();
			}

		} catch (UnsupportedEncodingException e) {
			Log.i("Goggles4u",
					"UnsupportedEncodingException:" + e.getMessage());
			e.printStackTrace();
			ststusMessage = AppConstants.UNABLE_TO_PROCESS;
			connectionDisconnect();
			return null;

		} catch (SocketTimeoutException e) {
			Log.i("Goggles4u", "SocketTimeoutException:" + e.getMessage());
			e.printStackTrace();
			connectionDisconnect();
			ststusMessage = AppConstants.TIME_OUT;
			return null;
		} catch (SocketException e) {
			Log.i("Goggles4u", "SocketException:" + e.getMessage());
			e.printStackTrace();
			connectionDisconnect();
			ststusMessage = AppConstants.UNABLE_TO_PROCESS;
			return null;
		} catch (FileNotFoundException e) {
			Log.i("Goggles4u", "IOException:" + e.getMessage());
			try {
				Log.v("jsonData===", "filenot" + mHttpURLConnection.getResponseCode());

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
			connectionDisconnect();
			ststusMessage = AppConstants.UNABLE_TO_PROCESS;
			return null;
		} catch (IOException e) {

			Log.i("Goggles4u", "IOException:" + e.getMessage());

			e.printStackTrace();
			connectionDisconnect();

			ststusMessage = AppConstants.UNABLE_TO_PROCESS;
			return null;
		} catch (JSONException e) {
			Log.i("Goggles4u", "JSONException:" + e.getMessage());
			e.printStackTrace();
			connectionDisconnect();
			ststusMessage = AppConstants.UNABLE_TO_PROCESS;
			return null;
		}

		return sJsonObj;
	}


	//returning array
JSONArray recivearray;

	public JSONArray getConnectiongetArray(String url, JSONObject jsonData) {
		try {

			Log.v("url===", "" + url);
			Log.v("jsonData===", "" + jsonData);
			URL urlToConnect = new URL(url);
			mHttpURLConnection = (HttpURLConnection) urlToConnect
					.openConnection();
			mHttpURLConnection.setReadTimeout(20000); /* milliseconds */
			mHttpURLConnection.setConnectTimeout(30000); /* milliseconds */

			mHttpURLConnection.setRequestProperty("Cache-Control", "no-cache");
			mHttpURLConnection.setRequestProperty("Content-Type",
					"application/json");
			mHttpURLConnection.setRequestProperty("Accept", "application/json");
			mHttpURLConnection.setRequestProperty("Connection", "close");
			mHttpURLConnection.setRequestMethod("POST");

			mHttpURLConnection.setDoInput(true);
			mHttpURLConnection.setDoOutput(true);
			mHttpURLConnection.connect();
			//Log.v("ResponseCode===", "" + mHttpURLConnection.getResponseCode());
			if (jsonData != null) {
				OutputStreamWriter os = new OutputStreamWriter(
						mHttpURLConnection.getOutputStream(), "UTF-8");
				os.write(jsonData.toString());
				os.flush();
				os.close();
			}
			sInputStream = mHttpURLConnection.getInputStream();

			Log.v("sInputStream=", "" + sInputStream);
			if (sInputStream == null) {
				throw new IOException();
			}
			Log.v("jsonData===", "" + mHttpURLConnection.getResponseCode());
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					sInputStream, "UTF-8"), 32);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");

			}
			Log.v("sb===", "" + sb);
			Log.v("sb length===", "" + sb.length());
			sInputStream.close();
			sJson = sb.toString();

			Log.v("sJson===", "" + sJson);
			if (sJson != null) {
				if (sJson.length() != 0) {

					try {

						recivearray = new JSONArray(sJson);
						/*sJsonObj = new JSONObject(sJson.substring(
								sJson.indexOf("{"), sJson.lastIndexOf("}") + 1));*/

						//ststusMessage = KenConstants.SUCCESSFUL;
					} catch (StringIndexOutOfBoundsException e) {
						// mExceptionCode =
						// InspectConstants.EXCEPTION_CODE_FOR_UNSUPPORTED_ENCODING;
						connectionDisconnect();
						//ststusMessage = KenConstants.UNABLE_TO_PROCESS;
						return null;
					}
				} else {
					throw new UnsupportedEncodingException();

				}

			} else {
				throw new UnsupportedEncodingException();
			}

		} catch (UnsupportedEncodingException e) {
			Log.i("Goggles4u",
					"UnsupportedEncodingException:" + e.getMessage());
			e.printStackTrace();
			ststusMessage = AppConstants.UNABLE_TO_PROCESS;
			connectionDisconnect();
			return null;

		} catch (SocketTimeoutException e) {
			Log.i("Goggles4u", "SocketTimeoutException:" + e.getMessage());
			e.printStackTrace();
			connectionDisconnect();
			ststusMessage = AppConstants.TIME_OUT;
			return null;
		} catch (SocketException e) {
			Log.i("Goggles4u", "SocketException:" + e.getMessage());
			e.printStackTrace();
			connectionDisconnect();
			ststusMessage = AppConstants.UNABLE_TO_PROCESS;
			return null;
		} catch (FileNotFoundException e) {
			Log.i("Goggles4u", "IOException:" + e.getMessage());
			try {
				Log.v("jsonData===", "filenot" + mHttpURLConnection.getResponseCode());

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
			connectionDisconnect();
			ststusMessage = AppConstants.UNABLE_TO_PROCESS;
			return null;
		} catch (IOException e) {

			Log.i("Goggles4u", "IOException:" + e.getMessage());

			e.printStackTrace();
			connectionDisconnect();

			ststusMessage = AppConstants.UNABLE_TO_PROCESS;
			return null;
		} catch (JSONException e) {
			Log.i("Goggles4u", "JSONException:" + e.getMessage());
			e.printStackTrace();
			connectionDisconnect();
			ststusMessage = AppConstants.UNABLE_TO_PROCESS;
			return null;
		}

		return recivearray;
	}
}




