package com.sunflower.nabung;

public class User {
    private String namaUser;
    private String passUser;
    private String emailUser;
    private String saldoPemasukan;
    private String saldoPengeluaran;
    private String saldoWishlist;

    public User() {
    }

    public User(String namaUser, String passUser, String emailUser, String saldoPemasukan, String saldoPengeluaran, String saldoWishlist) {
        this.namaUser = namaUser;
        this.passUser = passUser;
        this.emailUser = emailUser;
        this.saldoPemasukan = saldoPemasukan;
        this.saldoPengeluaran = saldoPengeluaran;
        this.saldoWishlist = saldoWishlist;
    }

    public String getNamaUser() {
        return namaUser;
    }

    public void setNamaUser(String namaUser) {
        this.namaUser = namaUser;
    }

    public String getPassUser() {
        return passUser;
    }

    public void setPassUser(String passUser) {
        this.passUser = passUser;
    }

    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }

    public String getSaldoPemasukan() {
        return saldoPemasukan;
    }

    public void setSaldoPemasukan(String saldoPemasukan) {
        this.saldoPemasukan = saldoPemasukan;
    }

    public String getSaldoPengeluaran() {
        return saldoPengeluaran;
    }

    public void setSaldoPengeluaran(String saldoPengeluaran) {
        this.saldoPengeluaran = saldoPengeluaran;
    }

    public String getSaldoWishlist() {
        return saldoWishlist;
    }

    public void setSaldoWishlist(String saldoWishlist) {
        this.saldoWishlist = saldoWishlist;
    }
}
