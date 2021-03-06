package dao.items;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;


import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import dao.ExchangePointsDB;
import dao.GroupsDB;
import dao.search.FuzzyFinder;
import dao.search.PatternsHolder;
import exceptions.DatabaseException;
import kasudb.KasuDB;


/**
 * @author ANAGBLA Joan, Giuseppe FEDERICO, Cedric Ribeiro, Lina YAHI*/
public class ItemsDB {

	public static DBCollection collection = KasuDB.getMongoCollection("items");
	//According to the customer demand fuzzyness is 2(omission|substitution|addition)
	private static int fuzzyness = 2; 

	/**
	 * Ajoute un objet à la base mongo.
	 * @param object
	 */
	public static void addItem(JSONObject object, JSONArray exPoints) {
		// Parsing de l'objet
		DBObject dbObj = (DBObject) com.mongodb.util.JSON.parse(object.toString());

		// Ajout de la date
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date today = new Date();
		dbObj .put("date", dateFormat.format(today));

		// Ajout dans la base de donnees des objets
		collection.insert( dbObj ).toString();	

		// Récupère l'id et l'utilisateur de l'objet qui vient d'être ajouté
		String itemID = ((ObjectId)dbObj.get( "_id" )).toString();
		String userID = (String) dbObj.get( "owner" );

		// Conversion JSONArray to ArrayList
		ArrayList<String> exPointsList = new ArrayList<String>();     
		JSONArray jsonArray = exPoints; 
		for (int i=0; i<jsonArray.length(); i++)
			try {
				exPointsList.add(jsonArray.get(i).toString());
			} catch (JSONException e) {
				e.printStackTrace();
			}

		// Ajout l'objet dans les points d'échange de l'utilisateur
		ExchangePointsDB.addItemToUserExPoints(itemID, userID, exPointsList);
	}


	/**
	 * Check if user's right on the object   
	 * @param userId
	 * @param id
	 * @return */
	public static boolean checkAthorization(String userId,String id) {
		return collection.find(
				new BasicDBObject()
				.append("_id",new ObjectId(id))
				.append("owner",userId)).hasNext();
	}


	/**
	 * Update an item
	 * @param id
	 * @param title
	 * @param description */
	public static void updateItem(String id, String title, String description) {
		collection.update(
				new BasicDBObject()
				.append("_id",new ObjectId(id))
				,new BasicDBObject()
				.append("$set",
						new BasicDBObject()
						.append("title",title)
						.append("description",description)));
	}	

	/**
	 * Remove an item
	 * @param id
	 * @param title
	 * @param description */
	public static void removeItem(String id) {
		collection.remove(
				new BasicDBObject()
				.append("_id",new ObjectId(id)));
	}	


	/**
	 * return an item (all fields)
	 * @param id
	 * @param title
	 * @param description 
	 * @return */
	public static DBObject getItem(String id) {
		return collection.findOne(
				new BasicDBObject()
				.append("_id",new ObjectId(id)));
	}	


	/**
	 * return item's status 
	 * status are borrowed|available|busy
	 * 
	 * @param id
	 * @param title
	 * @param description 
	 * @return */
	public static String itemStatus(String id) {
		return (String)collection.findOne(
				new BasicDBObject()
				.append("_id",new ObjectId(id))).get("status");
	}	
	
	
	
	/**
	 * Set an item's status 
	 * status are borrowed|available|busy
	 * @param id
	 * @param title
	 * @param description */
	public static void setItemStatus(String id,String status) {
		collection.update(
				new BasicDBObject()
				.append("_id",new ObjectId(id))
				,new BasicDBObject()
				.append("$set",
						new BasicDBObject()						
						.append("status",status)));
	}




	/**
	 * Find all items owned by the user
	 * @param userID
	 * @return
	 * @throws DatabaseException */
	public static Iterable<DBObject> userItems(String userID,String query)  {  
		BasicDBObject constraints = new BasicDBObject("owner",userID);
		if(query.trim().length()==0) 
			return collection.aggregate(Arrays.asList(
					new BasicDBObject("$match", constraints)
					,new BasicDBObject("$sort",new BasicDBObject("date", -1))
					//,new BasicDBObject("$project",new BasicDBObject("nom",1).append("prenom",1).append("_id", 0))//debug
					)).results();
		return FuzzyFinder.find(collection,	
				PatternsHolder.wordSet(query,PatternsHolder.blank),
				fuzzyness,
				Arrays.asList("title","description"),constraints,"");

	}


	/**
	 * Find all items not owned by user (uther = other user)
	 * @param userID
	 * @return
	 * @throws DatabaseException */
	public static Iterable<DBObject> utherItems(String userID,String query) { 
		DBCursor dbc = GroupsDB.userGroupsMembership(userID);
		BasicDBList userMembershipGroupsIDList = new BasicDBList();
		while(dbc.hasNext())
			userMembershipGroupsIDList.add(dbc.next().get("_id").toString());
		System.out.println("ItemDB/utherItems::userGroupsMembership="+userMembershipGroupsIDList);//debug

		//is item acessible bu this user
		BasicDBList orList = new BasicDBList();
		orList.add(new BasicDBObject(
				"groups", new BasicDBObject("$elemMatch",
				new BasicDBObject(
						"id",new BasicDBObject(
								"$in",userMembershipGroupsIDList))))
				);
		orList.add(new BasicDBObject("groups",new BasicDBObject("$size",0)));

		BasicDBObject constraints = new BasicDBObject("status","available")
				.append("$or", orList)
				.append("owner",new BasicDBObject("$ne",userID));

		if(query.trim().length()==0) 
			return collection.aggregate(Arrays.asList(
					new BasicDBObject("$match", constraints)
					,new BasicDBObject("$sort",new BasicDBObject("date", -1))
					//,new BasicDBObject("$project",new BasicDBObject("nom",1).append("prenom",1).append("_id", 0))//debug
					)).results();
		return FuzzyFinder.find(collection,
				PatternsHolder.wordSet(query,PatternsHolder.blank),
				fuzzyness,
				Arrays.asList("title","description"),constraints,"");
	}	




	/***************** ITEMS GROUPS (VISIBILITY ) MANAGEMENT *****************/


	/**  
	 * Add to an item one more groupId 
	 * @param itemID
	 * @param groupID */
	public static void addGroupToItem(String itemID, String groupID){
		collection.update(
				new BasicDBObject("_id", new ObjectId(itemID)),
				new BasicDBObject("$addToSet", 
						new BasicDBObject("groups",
								new BasicDBObject("id",groupID)
								.append("nom", GroupsDB.getGroup(groupID).get("name"))))
				);
	}


	/**
	 * Remove from an item the specified groupId
	 * @param itemID
	 * @param groupID */
	public static void removeGroupFromItem(String itemID, String groupID){	
		collection.update(new BasicDBObject("_id", new ObjectId(itemID)),
				new BasicDBObject("$pull",
						new BasicDBObject("groups",
								new BasicDBObject("id",groupID))
						)
				);
	}

	
	
	
	public static void main(String[] args) {
		System.out.println("Results...\n%");

		//addGroupToItem("58886d2eed0a14c5a9f5bc2e","58809F2D6D26AA03D268B50B");

		System.out.println(getItem("58886d2eed0a14c5a9f5bc2e"));
		removeGroupFromItem("58886d2eed0a14c5a9f5bc2e","58886d0ced0a14c5a9f5bc2d");
		
		//System.out.println(userItemsLoaned("586e8cd92736d4e126b99c07"));

		//		Iterable<DBObject> res =userItems("586f67636c7ec4b61187a196","");
		//		Iterable<DBObject> res =userItems("586f67636c7ec4b61187a196","V");
		//		Iterable<DBObject> res =utherItems("1","");
		//		Iterable<DBObject> res =utherItems("6","    V�lo   noir  ");
		//		for(DBObject o : res)System.out.println(o);
		//		System.out.println("%\n");
		//		System.out.print("Permission : ");
		//		if(checkAthorization("6","581c70b04c1471dd003afb61")){
		//			System.out.println("Granted");
		//			updateItem("581c70b04c1471dd003afb61","galaxy S5 neuf",
		//					"galaxy S5 noir neuf, tres peu servi");
		//		}else System.out.println("Denied");
	}


	/**
	 * return the list of groupIDs of an item 
	 * @param itemID
	 * @return */
	public static BasicDBList getGroupsFromItem(String itemID){
		DBObject item = getItem(itemID);
		BasicDBList groups = (BasicDBList) item.get("groups");
		return groups;
	}

	/**
	 * Get the list of items visible by the user
	 * @param itemID
	 * @param userID
	 * @return
	 */
	public static DBCursor accessibleItems(String userID) {

		BasicDBList usergroupsmembership = new BasicDBList();
		DBCursor dbc = GroupsDB.userGroupsMembership(userID);

		while(dbc.hasNext())
			usergroupsmembership.add(dbc.next().get("_id").toString());

		BasicDBList exprs = new BasicDBList();
		exprs.add(
				new BasicDBObject()
				.append("groups", 
						new BasicDBObject("$elemMatch",new BasicDBObject("id",new BasicDBObject("$in",usergroupsmembership)))
						));

		exprs.add(new BasicDBObject()
				.append("groups",new BasicDBObject("$size", 0)));

		return collection.find(new BasicDBObject().append("$or", exprs));
	}


	/**
	 * List of items loaned by a user
	 * @param userID
	 * @return
	 */
	public static DBCursor userItemsLoaned(String userID){
		return collection.find(
				new BasicDBObject()
				.append("owner", userID)
				.append("status", "borrowed"));
	}
	
	public static DBCursor userItems2(String userID){
		return collection.find(
				new BasicDBObject()
				.append("owner", userID));
	}
	
}