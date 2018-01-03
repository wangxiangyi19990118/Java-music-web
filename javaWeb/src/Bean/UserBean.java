package Bean;



import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserBean implements ObjectType {
    private String user_id;
    private String nikename;
    private String number;
    private String password;
    private List<String> musicsheets;

    public UserBean() {
        user_id= UUID.randomUUID().toString().replaceAll("-", "");
        musicsheets=new ArrayList<String>();
    }

    @Override
    public String get_id() {
        return user_id;
    }

    public void set_id(String _id) {
        this.user_id = _id;
    }

    public String getNikename() {
        return nikename;
    }
    public void setNikename(String nikename) {
        this.nikename = nikename;
    }
    public String getNumberid() {
        return number;
    }
    public void setNumberid(String numberid) {
        this.number = numberid;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public List<String> getMusicsheets() {
        return musicsheets;
    }
    public void setMusicsheets(List<String> musicsheets) {
        this.musicsheets = musicsheets;
    }

    @Override
    public String toString() {
        return "User [_id=" + user_id + ", nikename=" + nikename + ", numberid=" + number + ", password=" + password
                + ", musicsheets=" + musicsheets + "]";
    }
}
