package ma.alae.eloula.classes;

import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;

public class MissionEmploye
{
    private Integer id;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private Mission m;
    private Employee e;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public Mission getM() {
        return m;
    }

    public void setM(Mission m) {
        this.m = m;
    }

    public Employee getE() {
        return e;
    }

    public void setE(Employee e) {
        this.e = e;
    }
}
