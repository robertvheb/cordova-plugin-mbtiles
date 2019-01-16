package com.makina.offline.mbtiles;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;

/**
 * <code>MBTilesPlugin</code> Cordova plugin.
 * <p>
 * Manage MBTiles format as SQLite database or as filesystem.
 * 
 * @author <a href="mailto:sebastien.grimault@makina-corpus.com">S. Grimault</a>
 */
public class MBTilesPlugin extends CordovaPlugin
{
	// declaration of static variable 
	public static final String ACTION_INIT = "init";
	public static final String ACTION_OPEN = "open";
	public static final String ACTION_CLOSE = "close";
	public static final String ACTION_OPEN_TYPE_DB = "db";
	public static final String ACTION_OPEN_TYPE_FILE = "file";
	public static final String OPEN_TYPE_PATH_CDV = "cdvfile";
	public static final String OPEN_TYPE_PATH_FULL = "fullpath";
	public static final String ACTION_GET_METADATA = "get_metadata";
	public static final String ACTION_GET_MIN_ZOOM = "get_min_zoom";
	public static final String ACTION_GET_MAX_ZOOM = "get_max_zoom";
	public static final String ACTION_GET_TILE = "get_tile";
	public static final String ACTION_EXECUTE_STATEMENT = "execute_statement";
	public static final String ACTION_DIRECTORY_WORKING = "get_directory_working";
	public static final String ACTION_IS_SDCARD = "is_sdcard";
	
	// interface to treat action of plugin 
	private IMBTilesActions mbTilesActions = null;
	
	/*
	 * (non-Javadoc)
	 * @see org.apache.cordova.api.Plugin#execute(java.lang.String, org.json.JSONArray, java.lang.String)
	 */
	@Override
	 public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {
		
		final JSONArray dataFinal = data;
		final String actionFinal = action;
		final CallbackContext callbackContextFinal = callbackContext;
		
		cordova.getThreadPool().execute(new Runnable() {
            public void run() {
		
	            PluginResult result = null;
				try
				{
					if (actionFinal.equals(ACTION_INIT))
					{
						result = actionInit(dataFinal);
					}
					
					else if (actionFinal.equals(ACTION_OPEN))
					{
						result = actionOpen(dataFinal);
					}
					
					else if (actionFinal.equals(ACTION_CLOSE))
					{
						result = actionClose(dataFinal);
					}
					
					else if (actionFinal.equals(ACTION_GET_METADATA))
					{
						result = actionGetMetadata(dataFinal);
					}
					
					else if (actionFinal.equals(ACTION_GET_MIN_ZOOM))
					{
						result = actionGetMinZoom(dataFinal);
					}
					
					else if (actionFinal.equals(ACTION_GET_MAX_ZOOM))
					{
						result = actionGetMaxZoom(dataFinal);
					}
					
					else if (actionFinal.equals(ACTION_GET_TILE))
					{
						result = actionGetTile(dataFinal);
					}
					
					else if (actionFinal.equals(ACTION_EXECUTE_STATEMENT))
					{
						result = actionExecuteStatement(dataFinal);
					}

					else if (actionFinal.equals(ACTION_DIRECTORY_WORKING))
					{
						result = actionGetDirectoryWorking(dataFinal);
					}
					
					else if (actionFinal.equals(ACTION_IS_SDCARD))
					{
						result = actionIsSDCard(dataFinal);
					}
					
					if (result == null)
					{
						result = new PluginResult(PluginResult.Status.INVALID_ACTION);
					}
					
					callbackContextFinal.sendPluginResult(result);
				}
				catch (JSONException je)
				{
					callbackContextFinal.sendPluginResult(new PluginResult(PluginResult.Status.JSON_EXCEPTION));
				}
			}
        });
		return true;
	}
	
	/**
	 * Override of onDestroy method
	 */
	@Override
	public void onDestroy()
	{
		// close db if is not case
		if ((mbTilesActions != null) && mbTilesActions.isOpen())
		{
			mbTilesActions.close();
		}
		
		super.onDestroy();
	}
	
	
	/**
	 * open database or file with given name
	 * @param data : the parameters (type:'type',optional typepath:('fullpath', 'cdvfle'),  url:'cdvfile://url...')
	 * @return the pluginResult
	 */
	private PluginResult actionInit(JSONArray data) throws JSONException
	{
		String type = data.getJSONObject(0).getString("type");
		String url = null;
		String typepath = null;
		if (data.getJSONObject(0).has("typepath") && data.getJSONObject(0).has("url")) {
			typepath = data.getJSONObject(0).getString("typepath");
			url = data.getJSONObject(0).getString("url");
		}
		return new PluginResult(initAction(type, typepath, url) ? PluginResult.Status.OK : PluginResult.Status.IO_EXCEPTION);
	}
	
	/**
	 * open database or file with given name
	 *  * @param data : the parameters (name:'name')
	 * @return the pluginResult
	 */
	private PluginResult actionOpen(JSONArray data) throws JSONException
	{
		PluginResult result = null;
		
		// database
		if (mbTilesActions != null)
		{
			String name = data.getJSONObject(0).getString("name");
			mbTilesActions.open(name);
			// test if file or database is opened
			if (mbTilesActions.isOpen())
			{
				result = new PluginResult(PluginResult.Status.OK);
			}
			else
			{
				result = new PluginResult(PluginResult.Status.IO_EXCEPTION);
			}
		} else {
			result = new PluginResult(PluginResult.Status.ERROR);
		}
		return result;
	}
	
	private PluginResult actionClose(JSONArray data) throws JSONException
	{
		PluginResult result = null;
		
		if (mbTilesActions != null)
		{
			if (mbTilesActions.isOpen()) {
				mbTilesActions.close();
			}
			
			result = new PluginResult(PluginResult.Status.OK);
		} else {
			result = new PluginResult(PluginResult.Status.OK);
		}
		
		return result;
	}
	
	/**
	 * get metadata of the database opened
	  * @param data : the parameters ()
	 * @return the pluginResult
	 */
	private PluginResult actionGetMetadata(JSONArray data) throws JSONException
	{
		PluginResult result = null;
		
		if ((mbTilesActions != null) && mbTilesActions.isOpen())
		{
			result = new PluginResult(PluginResult.Status.OK, mbTilesActions.getMetadata());
		}
		else
		{
			result = new PluginResult(PluginResult.Status.IO_EXCEPTION);
		}
		
		return result;
	}
	
	/**
	 * get min zoom of the database opened
	 * @param data : the parameters ()
	 * @return the pluginResult
	 */
	private PluginResult actionGetMinZoom(JSONArray data) throws JSONException
	{
		PluginResult result = null;
		
		if ((mbTilesActions != null) && mbTilesActions.isOpen())
		{
			result = new PluginResult(PluginResult.Status.OK, mbTilesActions.getMinZoom());
		}
		else
		{
			result = new PluginResult(PluginResult.Status.IO_EXCEPTION);
		}
		
		return result;
	}
	
	/**
	 * get max zoom of the database opened
	 * @param data : the parameters () 
	 * @return the pluginResult
	 */
	private PluginResult actionGetMaxZoom(JSONArray data) throws JSONException
	{
		PluginResult result = null;
		
		if ((mbTilesActions != null) && mbTilesActions.isOpen())
		{
			result = new PluginResult(PluginResult.Status.OK, mbTilesActions.getMaxZoom());
		}
		else
		{
			result = new PluginResult(PluginResult.Status.IO_EXCEPTION);
		}
		
		return result;
	}
	
	/**
	 * get tile of the database opened with given parameters
	 * @param data : the parameters (z:'z', x:'x', y:'y') 
	 * @return the pluginResult
	 */
	private PluginResult actionGetTile(JSONArray data) throws JSONException
	{
		PluginResult result = null;
		
		Log.i(getClass().getName(), "actionGetTile Tiles");
		if ((mbTilesActions != null) && mbTilesActions.isOpen())
		{
			Log.i(getClass().getName(), "isOpen Tiles");
			result = new PluginResult(PluginResult.Status.OK, mbTilesActions.getTile(data.getJSONObject(0).getInt("z"), data.getJSONObject(0).getInt("x"), data.getJSONObject(0).getInt("y")));
		}
		else
		{
			Log.i(getClass().getName(), "isOpen Error Tiles");
			result = new PluginResult(PluginResult.Status.IO_EXCEPTION);
		}
		
		return result;
	}
	
	/**
	 * execute given query in on the database opened
	 * @param data : the parameters (query:'query', params:{'param', 'param'}) 
	 * @return the pluginResult
	 */
	private PluginResult actionExecuteStatement(JSONArray data) throws JSONException
	{
		PluginResult result = null;
		
		Log.i(getClass().getName(), "actionExecuteStatement");
		
		if ((mbTilesActions != null) && mbTilesActions.isOpen())
		{
			Log.i(getClass().getName(), "isOpen Execute Statement");
				
			String query= data.getJSONObject(0).getString("query");
			
			JSONArray jparams =  data.getJSONObject(0).getJSONArray("params");

			String[] params = null;

			// get parameters
			if (jparams != null) {
				params = new String[jparams.length()];

				for (int j = 0; j < jparams.length(); j++) {
					if (jparams.isNull(j))
						params[j] = "";
					else
						params[j] = jparams.getString(j);
				}
			}
			
			result = new PluginResult(PluginResult.Status.OK,  mbTilesActions.executeStatement(query, params));
		}
		else
		{
			Log.i(getClass().getName(), "isOpen Error Execute Statement");
			result = new PluginResult(PluginResult.Status.IO_EXCEPTION);
		}
		
		return result;
	}

	/**
	 * get directory of working
	 * @return the pluginResult
	 */
	private PluginResult actionGetDirectoryWorking(JSONArray data) throws JSONException
	{
		PluginResult result = null;
		
		if (mbTilesActions != null) {
			result = new PluginResult(PluginResult.Status.OK, mbTilesActions.getDirectoryWorking());
		} else {
			result = new PluginResult(PluginResult.Status.ERROR);
		}
		return result;
	}
	
	/**
	 * is sdcard
	 * @return the pluginResult
	 */
	private PluginResult actionIsSDCard(JSONArray data) throws JSONException
	{
		PluginResult result = null;
		
		if (FileUtils.checkExternalStorageState()) {
			result = new PluginResult(PluginResult.Status.OK);
		} else {
			result = new PluginResult(PluginResult.Status.ERROR);
		}
		return result;
	}
	
	/**
	 * Init Action
	 * @param type
	 * @param typePath
	 * @param url
	 * return if type is correct
	 */
	boolean initAction(String type, String typePath, String url) {
		if (mbTilesActions != null) {
			mbTilesActions.close();
			mbTilesActions = null;
		}
		
		// database
		if (type.equals(ACTION_OPEN_TYPE_DB))
		{
			mbTilesActions = new MBTilesActionsDatabaseImpl(this.cordova.getActivity(), webView.getResourceApi(), typePath,  url);
		}
		// file
		else if (type.equals(ACTION_OPEN_TYPE_FILE))
		{
			mbTilesActions = new MBTilesActionsFileImpl(this.cordova.getActivity(), webView.getResourceApi(), typePath,  url);
		}
		return mbTilesActions != null;
	}
}

