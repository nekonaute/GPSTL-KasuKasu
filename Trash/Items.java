package services;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.BasicDBList;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import dao.LoaningDB;
import dao.items.ItemsDB;
import dao.items.mapreduce.ItemsMR;
import dao.items.mapreduce.ObjetRSV;
import exceptions.DatabaseException;
import utils.Tools;

/**
 *@author ANAGBLA Joan */
public class Items {
	/**
	 * Update an Item
	 * @param id
	 * @param title
	 * @param description
	 * @return
	 * @throws JSONException
	 * @throws DatabaseException */
	public static JSONObject updateItem(String userId,String id, String title, String description) 
			throws JSONException, DatabaseException{
		if(ItemsDB.checkAthorization(userId,id))
			ItemsDB.updateItem(id,title,description);
		else return Tools.serviceRefused(
				"Vous n'avez pas le droit de modifier cet objet", -1);
		return Tools.serviceMessage(1);
	}


	/**
	 * return an item (all fields)
	 * @param id
	 * @param title
	 * @param description 
	 * @return 
	 * @throws JSONException */
	public static JSONObject getItem(String id) throws JSONException {
		DBObject dbo = ItemsDB.getItem(id);
		return new JSONObject().put(
				"items",
				new JSONArray().put(
						new JSONObject()
						.put("id",dbo.get("_id"))
						.put("type","item")
						.put("owner",dbo.get("owner"))
						.put("title",dbo.get("title"))
						.put("group",dbo.get("group"))//TODO MOD groups
						.put("date",dbo.get("date"))
						.put("description",dbo.get("description"))));
	}


	/**
	 * Remove an item
	 * @param id
	 * @param title
	 * @param description 
	 * @throws JSONException */
	public static JSONObject removeItem(String userId,String id) throws JSONException {
		if(!ItemsDB.checkAthorization(userId,id))
			return Tools.serviceRefused(
					"Vous n'avez pas le droit de supprimer cet objet", -1);
		ItemsDB.removeItem(id);
		return Tools.serviceMessage(1);
	}	

	/**
	 * return a list of items owned by an user 
	 * @param userID
	 * @return
	 * @throws JSONException
	 * @throws DatabaseException */
	public static JSONObject userItems(String query, String userID) throws JSONException, DatabaseException{
		JSONArray jar =new JSONArray();
		List<ObjetRSV> results=new ArrayList<ObjetRSV>();

		DBCursor cursor = ItemsDB.userItems(userID);

		if(!query.equals("")){//with QueryFilter
			results=ItemsMR.pertinence(query,cursor);
			if(results.isEmpty()){
				System.out.println(
						"userItems [with queryfilter]  : No pertinent results switching to SQLMODO");
				cursor=ItemsDB.userItemsSQLMODO(query, userID);}}

		while(cursor.hasNext()){
			DBObject doc = cursor.next();
			results.add(new ObjetRSV(doc,1));}

		for(ObjetRSV orsv : results )
			jar.put(new JSONObject()
					.put("id",orsv.getDbo().get("_id"))
					.put("type","item")
					.put("owner",orsv.getDbo().get("owner"))
					.put("title",orsv.getDbo().get("title"))
					.put("group",orsv.getDbo().get("group"))//TODO MOD groups
					.put("longitude",orsv.getDbo().get("longitude"))//TODO REM
					.put("latitude",orsv.getDbo().get("latitude"))//TODO REM
					.put("date",orsv.getDbo().get("date"))
					.put("description",orsv.getDbo().get("description")));
		return new JSONObject().put("items",jar);}

	/**
	 * Search Items by query using ItemsMR's pertinence
	 * @param query
	 * @return
	 * @throws JSONException
	 * @throws DatabaseException */
	public static JSONObject searchItems(String query,String userID) throws JSONException, DatabaseException{		
		JSONArray jar =new JSONArray();
		List<ObjetRSV> results=new ArrayList<ObjetRSV>();

		DBCursor cursor = ItemsDB.utherItems(userID);

		if(!query.equals("")){//with QueryFilter
			System.out.println("QUERY : "+query);
			results=ItemsMR.pertinence(query,cursor);
			if(results.isEmpty()){
				System.out.println(
						"searchItems  : No pertinent results switching to SQLMODO");
				cursor=ItemsDB.utherItemsSQLMODO(query, userID);}}

		while(cursor.hasNext()){
			DBObject doc = cursor.next();
			results.add(new ObjetRSV(doc,1));}

		for(ObjetRSV orsv : results )
			if(LoaningDB.requestExists(userID,orsv.getDbo().get("_id").toString()))
				continue;
			else
				jar.put(new JSONObject()
						.put("id",orsv.getDbo().get("_id"))
						.put("type","item")
						.put("owner",orsv.getDbo().get("owner"))
						.put("title",orsv.getDbo().get("title"))
						.put("group",orsv.getDbo().get("group"))
						.put("longitude",orsv.getDbo().get("longitude"))
						.put("latitude",orsv.getDbo().get("latitude"))
						.put("date",orsv.getDbo().get("date"))
						.put("description", orsv.getDbo().get("description")));
		return new JSONObject().put("items",jar);}

	public static void addGroupToItem(String itemID, String groupID,String userID){
		if(!ItemsDB.checkAthorization(userID,itemID))
			return ;
		ItemsDB.addGroupToItem(itemID, groupID);
	}

	public static void addExPointToItem(String itemID,String exPointID,String userID){
		if(!ItemsDB.checkAthorization(userID,itemID))
			return ;
		ItemsDB.addExPointToItem(itemID, exPointID);
	}

	public static void removeGroupFromItem(String itemID, String groupID,String userID){
		if(!ItemsDB.checkAthorization(userID,itemID))
			return ;
		ItemsDB.removeGroupFromItem(itemID, groupID);
	}

	public static void removeExPointFromItem(String itemID,String exPointID,String userID){
		if(!ItemsDB.checkAthorization(userID,itemID))
			return ;
		ItemsDB.removeExPointFromItem(itemID, exPointID);
	}

	public static JSONArray getGroupsFromItem(String itemID,String userID) throws JSONException{
		/* => BasicDBList */
		if(!ItemsDB.checkAthorization(userID,itemID))
			return null;
		BasicDBList o= (BasicDBList)ItemsDB.getGroupsFromItem(itemID);
		JSONArray js;
		if(o==null)
			js=new JSONArray();
		else
			js=new JSONArray(o.toString());
		return js;
	}

	public static JSONArray getExchangePointsFromItem(String itemID,String userID) throws JSONException{
		/* o => BasicDBList */
		if(!ItemsDB.checkAthorization(userID,itemID))
			return null;
		BasicDBList o= (BasicDBList)ItemsDB.getExchangePointsFromItem(itemID);
		JSONArray js;
		if(o==null)
			js=new JSONArray();
		else
			js=new JSONArray(o.toString());
		return js;
	}
}