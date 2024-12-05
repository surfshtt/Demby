package Model;

public class Profile {
    private int id;
    private String ownerName;
    private String realName;
    private int age;
    private String city;
    private String description;
    private String seenBy;
    private String likedBy;
    private byte[] image;
    private String instagram;
    private String telegram;


    public Profile(){}

    public Profile(String ownerId, String realName, int age, String city, String description, String seenBy, String likedBy, byte[] image, String telegram, String instagram) {
        this.ownerName = ownerId;
        this.realName = realName;
        this.age = age;
        this.city = city;
        this.description = description;
        this.seenBy = seenBy;
        this.likedBy = likedBy;
        this.image = image;
        this.instagram = instagram;
        this.telegram = telegram;
    }

    public Profile(int id, String ownerId, String realName, int age, String city, String description, String seenBy, String likedBy, byte[] image, String telegram, String instagram) {
        this.id = id;
        this.ownerName = ownerId;
        this.realName = realName;
        this.age = age;
        this.city = city;
        this.description = description;
        this.seenBy = seenBy;
        this.likedBy = likedBy;
        this.image = image;
        this.instagram = instagram;
        this.telegram = telegram;
    }

    public String getTelegram() {
        return telegram;
    }

    public String getInstagram() {
        return instagram;
    }
    public byte[] getImage() {
        return image;
    }

    public int getId() {
        return id;
    }

    public String getOwnerId() {
        return ownerName;
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

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public void setTelegram(String telegram) {
        this.telegram = telegram;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setOwnerId(String ownerId) {
        this.ownerName = ownerId;
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
