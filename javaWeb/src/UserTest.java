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


    }
}
