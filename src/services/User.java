package services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import dao.search.ObjetRSV;
import dao.users.UserDao;
import dao.users.UsersImagesDao;
import enumerations.Status;
import exceptions.StringNotFoundException;
import exceptions.UserNotFoundException;
import exceptions.UserNotUniqueException;
import json.Error;
import json.Success;
import json.Warning;
import lingua.Lingua;
import services.config.Config;
import utils.SendEmail;
import utils.Tools;

/**
 * * @author Anagbla Jean, Daniel RADEAU
 * */

public class User {

	/**
	 * Permet d'ajouter un utilisateur dans la bas des utilisateurs
	 * @param email
	 * @param mdp
	 * @param nom
	 * @param prenom
	 * @param numero
	 * @return
	 * @throws 
	 * @throws JSONException 
	 * @throws UserNotUniqueException 
	 * @throws UserNotFoundException 
	 */
	public static JSONObject createUser(String value, String email,String mdp,String nom,String prenom, String numero) 
			throws JSONException, UserNotFoundException, UserNotUniqueException{		
		if (UserDao.userExistsByEmail(email))
			return Tools.serviceMessage("User's email already exists.");

		UserDao.addUser(email,mdp,nom,prenom,numero);
		
		try {

			SendEmail.sendMail(email,
					Lingua.get("welcomeMailSubject",value),
					Lingua.get("welcomeMailMessage",value)
					+Config.getDomain()+"/KasuKasu/confirm?id="+UserDao.getUserByEmail(email).getId()
					);
		} catch (StringNotFoundException e) { 
			System.out.println("Dictionary Error : Mail not send");
			e.printStackTrace(); 
		}
		return Tools.serviceMessage(1);
	}



	/**
	 * check if user account is checked
	 * @rebasetested 
	 * @param id */
	public static boolean isConfirmed(String id){
		return UserDao.isConfirmed(id);
	}


	/**
	 * Met � jour les informations de l'utilisateur correspondant � l'email et au mot de passe par celle contenues
	 * dans l'instance d'entities.User
	 * @param email
	 * @param password
	 * @param newUser
	 * @return
	 * @throws 
	 * @throws Exception
	 */

	public static boolean updateUser(String email, String password, entities.User newUser) throws Exception {
		entities.User oldUser = UserDao.getUserByEmail(email);
		if (!oldUser.getPassword().equals(password))
			return false;
		UserDao.updateUser(oldUser, newUser);
		return true;
	}

	/**
	 * Retourne un objet json representant une liste d'utilisateurs trouves pour le champ 'field' et sa valeur 'value' <br/>
	 * 
	 * Exemple : 	&nbspgetUsersJSONProfileWhere("nom", "PIERRE") returns <br/>
	 * 				{ 	user0 : { <br/>
	 * 						id : "42", <br/>
	 * 						email : "truc@gmail.com", <br/>
	 * 						password : "123456", <br/>
	 * 						name : "PIERRE", <br/>
	 * 						firstname : "Jean", <br/>
	 * 						phone : "0611223344" }, <br/>
	 *  				user1 : { <br/>
	 * 						id : "30", <br/>
	 * 						email : "bidulle@gmail.com", <br/>
	 * 						password : "123456", <br/>
	 * 						name : "PIERRE", <br/>
	 * 						firstname : "Victor", <br/>
	 * 						phone : "0611223344" } <br/>
	 * 				} <br/>
	 * @param field
	 * @param value
	 * @return
	 */
	public static JSONObject getUsersJSONProfileWhere(String field, String value) {

		JSONObject usersJSON = new JSONObject();

		if (value == null) {
			return new Error(field + " value is null");
		}

		if (value.equals("")) {
			return new Warning(field + " value is empty");
		}

		List<entities.User> users = null;

		try {
			users = UserDao.getUsersWhere(field, value);
			int index = 0;

			for (entities.User user : users) {
				JSONObject userJSON = new JSONObject();

				userJSON.put("id", user.getId());
				userJSON.put("email", user.getEmail());
				//userJSON.put("password", user.getPassword());
				userJSON.put("name", user.getName());
				userJSON.put("firstname", user.getFirstname());
				userJSON.put("phone", user.getPhone());

				usersJSON.put("user" +(index++), userJSON);
			}

		} catch (JSONException e) {
			return new Error("JSON exception");
		}

		if (users.isEmpty())
			return new Warning("Not users found for '" + field + "' = '" + value + "'");

		return usersJSON;
	}

	public static JSONObject getUsersJSONProfileFromIds(ArrayList<String> ids) throws UserNotFoundException, UserNotUniqueException{

		JSONObject usersJSON = new JSONObject();
		JSONArray usersArray = new JSONArray();
		if (ids == null)
			return new Error("Ids value is null");

		List<entities.User> users = new ArrayList<entities.User>();

		try {
			for(String i : ids)
				users.add(UserDao.getUserById(i));

			for (entities.User user : users) {
				JSONObject userJSON = new JSONObject();

				userJSON.put("id", user.getId());
				userJSON.put("email", user.getEmail());
				//userJSON.put("password", user.getPassword());
				userJSON.put("name", user.getName());
				userJSON.put("firstname", user.getFirstname());
				userJSON.put("phone", user.getPhone());

				usersArray.put(userJSON);
			}
			usersJSON.put("users", usersArray);

		} catch (JSONException e) {
			return new Error("JSON exception");
		}

		if (users.isEmpty())
			return new Warning("No users found");

		return usersJSON;
	}

	/**
	 * Get user image path
	 * @param user
	 * @return
	 */

	public static JSONObject getUserImage(entities.User user) {
		JSONObject response = null;

		try {
			String url = UsersImagesDao.getUserImage(user);
			response = new Success(url);
		} catch (Exception e) {
			response = new Warning("No picture for this user");

		}

		return response;
	}

	/**
	 * Put new url for user image
	 * 
	 * @param user
	 * @param url
	 * @return
	 */

	public static JSONObject putUserImage(entities.User user, String url) {
		JSONObject response = null;


		UsersImagesDao.updateUserImage(user, url);
		response = new Success("Picture successfully updated");


		return response;
	}

	/**
	 * Remove user image
	 * @param user
	 * @return
	 */

	public static JSONObject removeUserPIcture (entities.User user) {
		JSONObject response = null;

		UsersImagesDao.removeUserImage(user);
		response = new Success("Picture successfully removed");

		return response;
	}

	public static JSONObject filterUserPassword(JSONObject userJSON) {
		JSONObject userWithoutPassword = new JSONObject();
		@SuppressWarnings("unchecked")
		Iterator<String> users = (Iterator<String>)userJSON.keys();

		try {

			while (users.hasNext()) {
				String user = users.next();
				@SuppressWarnings("unchecked")
				Iterator<String> profile = ((JSONObject) userJSON.get(user)).keys();
				System.out.println(user);
				while (profile.hasNext()) {
					String key = profile.next();
					System.out.println(key);
					if (key.equals("password"))
						continue;
					userWithoutPassword.put(key, ((JSONObject) userJSON.get(user)).get(key));
				}
			}

		} catch (JSONException e) {
			return new Error("Filtering JSON password failure !");
		}
		return userWithoutPassword;
	}

	/**
	 * Recupere l'utilisateur correspondant a l'email passe en parametre
	 * @param email
	 * @return
	 * @throws 
	 * @throws Exception
	 */

	public static entities.User getUser(String email) throws Exception {
		entities.User user = UserDao.getUserByEmail(email);
		if (user != null)
			return user;
		return null;
	}

	public static entities.User getUserById(String id) throws Exception {
		List<entities.User> users = UserDao.getUsersWhere("_id", id);
		if (users.size() == 1)
			return users.get(0);
		if (users.size() > 1)
			throw new UserNotUniqueException();
		else return null;
	}

	public static void confirmUser(String id) throws UserNotFoundException{ 
		if(!UserDao.userExistsById(id))
			throw new UserNotFoundException();
		UserDao.confirmUser(id);
	}

	/**
	 * Return a json object containing users found according to query 
	 * @param userId
	 * @param query
	 * @return 
	 * @throws JSONException */
	public static JSONObject findUser(String userId, String query) throws JSONException {
		JSONArray jar = new JSONArray();		
		int sizeres = 10,i=0;
		for(ObjetRSV user : UserDao.findUser(userId, query)){
			if(i++>=sizeres) break;
			JSONObject o =new JSONObject()
					.put("id", user.getDbo().get("_id"))
					.put("email", user.getDbo().get("email"))
					.put("name", user.getDbo().get("nom"))
					.put("firstname", user.getDbo().get("prenom"))
					.put("phone", user.getDbo().get("numero"));
			String otherId=o.get("id").toString();
			if(Friends.isPending(userId,otherId))
				o.put("friend", "waiting");
			else if(Friends.areFriends(userId,otherId))
				o.put("friend", "friend");
			else
				o.put("friend", "stranger");
			jar.put(o);
		}
		return new JSONObject().put("users",jar);
	}


	/**
	 * Return a json object containing users found according to query among user's friends
	 * @param userId
	 * @param query
	 * @return 
	 * @throws JSONException */
	public static JSONObject findFriends(String userId, String query) throws JSONException {
		JSONArray jar = new JSONArray();		
		int sizeres = 10,i=0;
		for(ObjetRSV user : UserDao.findFriends(userId, query)){
			if(i++>=sizeres) break;			
			jar.put(new JSONObject()
					.put("id", user.getDbo().get("_id"))
					.put("email", user.getDbo().get("email"))
					.put("name", user.getDbo().get("nom"))
					.put("firstname", user.getDbo().get("prenom"))
					.put("phone", user.getDbo().get("numero")));
		}
		return new JSONObject().put("users",jar);
	}

	public static void freeze(String userId) throws UserNotFoundException, UserNotUniqueException {
		UserDao.freeze(userId);
	}

	public static void unfreeze(String userId) throws UserNotFoundException, UserNotUniqueException {
		UserDao.unfreeze(userId);
	}

	public static void ban(String userId) {
		UserDao.ban(userId);
	}

	public static boolean isFrozen(String userId) throws UserNotFoundException, UserNotUniqueException{
		return UserDao.isFrozen(userId);
	}

	public static boolean isBanned(String userId) throws UserNotFoundException, UserNotUniqueException{
		return UserDao.isBanned(userId);
	}

	public static boolean isNormal(String userId) throws UserNotFoundException, UserNotUniqueException{
		return UserDao.isNormal(userId);
	}

	public static void setStatus(String userId,Status status){
		UserDao.setStatus(userId, status);
	}
	public static Status getStatus(String userId) throws UserNotFoundException, UserNotUniqueException{
		return UserDao.getStatus(userId);
	}

	public static boolean isAdmin(String userId) throws UserNotFoundException, UserNotUniqueException{
		return UserDao.isAdmin(userId);
	}


	/**
	 * Set user to vacation mode
	 * All user's items will be disabled to loan (
	 * 	they wont appear in search 's results 
	 * )
	 * @param id */
	public static JSONObject setVacationStatus(String id, boolean vacationStatus)
			throws JSONException{
		if (vacationStatus)
			UserDao.goOnVacation(id);
		else
			UserDao.exitVacation(id);
		return Tools.serviceMessage(1);
	}
	
	public static JSONObject getVacationStatus(String id) throws JSONException{
		return new JSONObject().put("vacationstatus", UserDao.getVacationStatus(id));
	}

	/**
	 * Local tests
	 * @param args
	 * @throws JSONException */
	public static void main(String[] args) throws JSONException {
		System.out.println("main : findUser : "+findUser("5856f940a7705c0d0f55e35f",""));
		System.out.println("main : findFriends : "+findFriends("5851476fd4fa474871be3d76","zoro tutanck"));
	}

}