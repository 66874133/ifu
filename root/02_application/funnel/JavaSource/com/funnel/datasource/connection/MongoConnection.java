package com.funnel.datasource.connection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.funnel.svc.util.StringUtils;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

public class MongoConnection {

	private MongoClient client = null;
	private MongoDatabase db = null;
	private ObjectValueMapper objectValueMapper = new ObjectValueMapper();

	private String ip;

	private int port;

	private String user;
	private String pwd;
	private String source;

	public MongoConnection(String ip, int port, String user, String pwd,
			String source, String auth) {

		this.ip = ip;
		this.port = port;
		this.user = user;
		this.pwd = pwd;
		this.source = source;
		client = newMongoClient(ip, port, user, pwd, auth);

	}

	private MongoClient newMongoClient(String ip, int port, String user,
			String pwd, String auth) {
		MongoClient client = null;
		if ("true".equals(auth) && StringUtils.hasText(user)
				&& StringUtils.hasText(pwd)) {
			MongoCredential mongoCredential = MongoCredential
					.createScramSha1Credential(user, "admin", pwd.toCharArray());
			client = new MongoClient(new ServerAddress(ip, port),
					Arrays.asList(mongoCredential));
		} else {
			client = new MongoClient(Arrays.asList(new ServerAddress(ip, port)));
		}

		return client;

	}

	@SuppressWarnings("rawtypes")
	public Editor editor(String dbName) {
		if (null != dbName) {
			db = client.getDatabase(dbName);
		}

		return new Editor();
	}

	@SuppressWarnings("rawtypes")
	public Editor editor() {
		return editor(source);
	}

	private Document toDocument(Object o) throws IllegalArgumentException,
			IllegalAccessException {
		Document document = new Document();
		Map<String, String> map = objectValueMapper.toFieldValuesMap(o);
		document.putAll(map);
		return document;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public class Editor<T> {

		public void createTable(String table) {
			db.createCollection(table);
		}

		public void dropTable(String table) {
			db.getCollection(table).drop();
		}

		public MongoIterable<String> listTables() {
			return db.listCollectionNames();
		}

		public void clearTable(String table) {
			dropTable(table);
			createTable(table);
		}

		public long countTable(String table) {
			return db.getCollection(table).count();
		}

		public void insert(String table, Object o)
				throws IllegalArgumentException, IllegalAccessException {
			db.getCollection(table).insertOne(toDocument(o));
		}

		public void insert(String table, String json)
				throws IllegalArgumentException, IllegalAccessException {
			db.getCollection(table).insertOne(Document.parse(json));
		}

		public void insertBatch(String table, List<Object> os)
				throws IllegalArgumentException, IllegalAccessException {
			List<Document> list = new ArrayList<Document>();
			for (Object o : os) {
				list.add(toDocument(o));
			}
			db.getCollection(table).insertMany(list);
		}

		public void insertBatchJson(String table, List<String> os)
				throws IllegalArgumentException, IllegalAccessException {
			List<Document> list = new ArrayList<Document>();
			for (String json : os) {
				list.add(Document.parse(json));
			}
			db.getCollection(table).insertMany(list);
		}

		public void update(String table, Map<String, String> where,
				String fieldName, String value) {
			List<Bson> filters = toFilterConditions(where);
			db.getCollection(table).updateMany(Filters.and(filters),
					Updates.set(fieldName, value));
		}

		public void delete(String table, Map<String, String> where)
				throws IllegalArgumentException, IllegalAccessException {
			List<Bson> filters = toFilterConditions(where);
			db.getCollection(table).deleteMany(Filters.and(filters));
		}

		public FindIterable<Document> select(String table,
				Map<String, String> where) throws IllegalArgumentException,
				IllegalAccessException {
			List<Bson> filterConditions = toFilterConditions(where);
			return db.getCollection(table).find(Filters.and(filterConditions));

		}

		public List<?> select(String table, Map<String, String> where,
				Class<?> class1) throws IllegalArgumentException,
				IllegalAccessException, InstantiationException {
			List<Object> list = new ArrayList<Object>();
			List<Bson> filterConditions = null;
			MongoCursor<Document> findIterable = null;
			if (null != where) {
				filterConditions = toFilterConditions(where);
				findIterable = db.getCollection(table)
						.find(Filters.and(filterConditions)).iterator();
			} else {
				findIterable = db.getCollection(table).find().iterator();

			}

			while (findIterable.hasNext()) {

				Document doc = findIterable.next();
				Object t = objectValueMapper.toObject(doc, class1);
				list.add(t);

			}

			return (List<?>) list;
		}

		private List<Bson> toFilterConditions(Map<String, String> where) {
			List<Bson> filters = new ArrayList<Bson>();
			Iterator<String> iterator = where.keySet().iterator();
			while (iterator.hasNext()) {
				String key = iterator.next();
				Bson bson = Filters.eq(key, where.get(key));
				filters.add(bson);
			}
			return filters;
		}

	}

}
