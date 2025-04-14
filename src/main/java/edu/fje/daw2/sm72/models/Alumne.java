package edu.fje.daw2.sm72.models;

import java.util.Objects;

public class Alumne {
    private int id;
    private String nom;
    private int nota;

    public Alumne(int id){
        this.id = id;
    }
    public Alumne(int id, String nom, int nota) {
        this.id = id;
        this.nom = nom;
        this.nota = nota;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getNota() {
        if(nota>10) nota=10;
        return nota;
    }

    public void setNota(int nota){
        this.nota = nota;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Alumne alumne = (Alumne) o;
        return id == alumne.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Alumne{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", nota=" + nota +
                '}';
    }
}
