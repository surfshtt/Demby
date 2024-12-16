package Model;

public class ArchiveMutual {
    private int id;
    private String ownerName;
    private String mutualNames;

    public ArchiveMutual() {
    }

    public ArchiveMutual(String ownerName, String mutualNames) {
        this.ownerName = ownerName;
        this.mutualNames = mutualNames;
    }

    public ArchiveMutual(int id, String ownerName, String mutualNames) {
        this.id = id;
        this.ownerName = ownerName;
        this.mutualNames = mutualNames;
    }

    public int getId() {
        return id;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getMutualNames() {
        return mutualNames;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public void setMutualNames(String mutualNames) {
        this.mutualNames = mutualNames;
    }
}
