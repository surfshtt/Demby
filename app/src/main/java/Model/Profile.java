package Model;

public class Profile {
    private int id;
    private int ownerId;
    private String realName;
    private int age;
    private String city;
    private String description;
    private String seenBy;
    private String likedBy;

    public Profile(){}

    public Profile(int ownerId, String realName, int age, String city, String description, String seenBy, String likedBy) {
        this.ownerId = ownerId;
        this.realName = realName;
        this.age = age;
        this.city = city;
        this.description = description;
        this.seenBy = seenBy;
        this.likedBy = likedBy;
    }

    public Profile(int id, int ownerId, String realName, int age, String city, String description, String seenBy, String likedBy) {
        this.id = id;
        this.ownerId = ownerId;
        this.realName = realName;
        this.age = age;
        this.city = city;
        this.description = description;
        this.seenBy = seenBy;
        this.likedBy = likedBy;
    }

    public int getId() {
        return id;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public String getRealName() {
        return realName;
    }

    public int getAge() {
        return age;
    }

    public String getCity() {
        return city;
    }

    public String getDescription() {
        return description;
    }

    public String getSeenBy() {
        return seenBy;
    }

    public String getLikedBy() {
        return likedBy;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public void setRealName(String name) {
        this.realName = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSeenBy(String seenBy) {
        this.seenBy = seenBy;
    }

    public void setLikedBy(String likedBy) {
        this.likedBy = likedBy;
    }
}
