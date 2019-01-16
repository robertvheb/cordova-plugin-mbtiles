
var MBTilesPlugin = function()
{
};

/*
function init
init the given database sqlite
params : {type : 'file' or 'db'or 'cdvfile', optionnal url:path}
return : success or error callback
*/
MBTilesPlugin.prototype.init = function(params, onSuccess, onError)
{
	return cordova.exec(onSuccess, onError, "MBTilesPlugin", "init", [params]);
};

/*
function open
open the given database sqlite
params : {name : 'name of database'}
return : success or error callback
*/
MBTilesPlugin.prototype.open = function(params, onSuccess, onError)
{
	return cordova.exec(onSuccess, onError, "MBTilesPlugin", "open", [params]);
};

/*
function close
closes the  database sqlite
params : {}
return : success or error callback
*/
MBTilesPlugin.prototype.close = function(params, onSuccess, onError)
{
	return cordova.exec(onSuccess, onError, "MBTilesPlugin", "close", [params]);
};

/*
function getMetadata
get the metadata of the database opened
return : success or error callback
*/
MBTilesPlugin.prototype.getMetadata = function(onSuccess, onError)
{
	return cordova.exec(onSuccess, onError, "MBTilesPlugin", "get_metadata", []);
};

/*
function getMinZoom
get the min zoom of the database opened
return : success or error callback
*/
MBTilesPlugin.prototype.getMinZoom = function(onSuccess, onError)
{
	return cordova.exec(onSuccess, onError, "MBTilesPlugin", "get_min_zoom", []);
};

/*
function getMaxZoom
get the max zoom of the database opened
return : success or error callback
*/
MBTilesPlugin.prototype.getMaxZoom = function(onSuccess, onError)
{
	return cordova.exec(onSuccess, onError, "MBTilesPlugin", "get_max_zoom", []);
};

/*
function getTile
get tile in base 64 of the database opened with params
params : {z:'z', x:'x', y:'y'} // coord
return : success or error callback
*/
MBTilesPlugin.prototype.getTile = function(params, onSuccess, onError)
{
	return cordova.exec(onSuccess, onError, "MBTilesPlugin", "get_tile", [params]);
};

/*
function exectuteStatment
execute query in the database opened
params : {query:'query', params:{'param', 'param', 'param'}} 
return : success or error callback
*/
MBTilesPlugin.prototype.executeStatement = function(params, onSuccess, onError)
{
	return cordova.exec(onSuccess, onError, "MBTilesPlugin", "execute_statement", [params]);
};

/*
function getDirectoryWorking
get the directory path of working
return : success or error callback
*/
MBTilesPlugin.prototype.getDirectoryWorking = function(onSuccess, onError)
{
	return cordova.exec(onSuccess, onError, "MBTilesPlugin", "get_directory_working", []);
};

/*
function isSDCard
check if the sdcard is present, (always true on wp8 (readonly), and ios (not exist))
return : success or error callback
*/
MBTilesPlugin.prototype.isSDCard = function(onSuccess, onError)
{
	return cordova.exec(onSuccess, onError, "MBTilesPlugin", "is_sdcard", []);
};
	
module.exports = MBTilesPlugin;

