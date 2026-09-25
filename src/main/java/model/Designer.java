package model;

public class Designer {
    private int id;
    private String fullName;
    private String specialization;
    private String email;

    public Designer() {
    }

    public Designer(String fullName, String specialization, String email) {
        this.fullName = fullName;
        this.specialization = specialization;
        this.email = email;
    }

    public Designer(int id, String fullName, String specialization, String email) {
        this.id = id;
        this.fullName = fullName;
        this.specialization = specialization;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Дизайнер id:" + id + ", ФИО:'" + fullName + "', специализация:'" + specialization + "', email:'" + email;
    }
}