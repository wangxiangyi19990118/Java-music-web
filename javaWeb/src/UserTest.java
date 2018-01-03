import Bean.MusicBean;
import Bean.UserBean;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class UserTest {
    public static void main(String[] args) {
        // TODO Auto-generated method stub


        @SuppressWarnings("resource")
        MongoClient mongoClient=new MongoClient();
        MongoDatabase mongoDatabase=mongoClient.getDatabase("musicweb");
        MongoCollection<Document> collection_users=mongoDatabase.getCollection("users");
        MongoCollection<Document> collection_musics=mongoDatabase.getCollection("musics");
        MongoCollection<Document> collection_musicsheets=mongoDatabase.getCollection("musicsheets");
        Document document=new Document();

        UserBean user=new UserBean();
        user.setNikename("111");
        user.setNumberid("11111");
        user.setPassword("123456");


        MusicBean music=new MusicBean();
        music.setName("111");
        music.setSonger("222");
        music.setMd5Value("452514818178");

//			User user2=MongoUtil.findOne(collection_users, eq("_id", "ea73fafc678d42e18eeca5ae1e5dc481"),new  TypeReference<User>() {});
//			user2.getMusicsheets().remove("33a9bc169bcc43c9b4fe613f0775ab6b");
//			MongoUtil.update(collection_users, user2);

        //collection_users.find(eq("musicsheets", "dsfdsfsdfdsfdsfdsfs")).forEach(printBlock);;


//	MusicSheet mSheet=		MongoUtil.findOne(collection_musicsheets, eq("_id","438530e833524a69b601cf430520c454"), new TypeReference<MusicSheet>() {});
//
//			System.out.println(mSheet);
//添加对象
//			document.putAll(MongoUtil.Obj2Map(user));
//			collection.insertOne(document);
//			MongoUtil.insert(collection, user);
//			MongoUtil.insert(collection_musics, music);



//更新对象
//			List<String>  muStirngs=new ArrayList<String>();
//			muStirngs.add("dsfdsfsdfdsfdsfdsfs");
//			muStirngs.add("dsfdsfsdf4646446");
//			user.setMusicsheets(muStirngs);
//			user.set_id("4f43448ba48e4e5c9823cb5988fe5abb");
////			System.out.println(user);
//			document.append("$set",MongoUtil.Obj2Map(user));

//			Music m=MongoUtil.findOne(collection_musics, eq("_id","2f6068c592414589a63975577990c6e7"),new TypeReference<Music>() {} );
//			m.setSonger("ttttt");
//			m.setName("ssssss");
//			MongoUtil.update(collection_musics, m);

//			collection.updateOne(eq("_id","4f43448ba48e4e5c9823cb5988fe5abb"), document);
//查找对象

//			Document document2=collection_users.find(eq("_id","4f43448ba48e4e5c9823cb5988fe5abb")).first();
//			User user2=JSON.parseObject(document2.toJson(),new TypeReference<User>() {});
//			System.out.println(user2);
//			System.out.println(MongoUtil.findOne(collection, eq("_id", "37663933768e47f1b8228ca8391400d7")).toString());
//			Object object=MongoUtil.findOne(collection_musics, eq("_id","2f6068c592414589a63975577990c6e7"),new TypeReference<Music>() {} );
//			System.out.println(object==null?"yesy":object.toString());


//删除对象
//			collection.deleteOne(eq("_id","c948ab8dbc5649baaa1811f88c2d1da2"));
//			collection_users.find().forEach(printBlock);
//			collection_musics.find().forEach(printBlock);
//			User uuuu=MongoUtil.findOne(collection_users, eq("_id", "37663933768e47f1b8228ca8391400d7"), new TypeReference<User>() {});

//			MongoUtil.delete(collection_users, uuuu);
//			System.out.println(document.toJson());


    }
    static Block<Document> printBlock = new Block<Document>() {
        public void apply(final Document document) {
            System.out.println(document.toJson());
        }
    };
}
