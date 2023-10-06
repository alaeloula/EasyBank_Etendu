package ma.alae.eloula.classes;

import java.time.LocalDate;

public class Client  extends Personel{
    private String address;

    public Client(Integer id, String nom, String prenom, LocalDate dateNaissance, String tel, String address) {
        super(id, nom, prenom, dateNaissance, tel);
        this.address = address;
    }

    public Client() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
