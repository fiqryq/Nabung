package org.d3ifcool.nabung;

public class Pengeluaran {
    private String namaPengeluaran;
    private String TotalPengeluaran;
    private String TanggalPengeluaran;
    private String dataID;


    public Pengeluaran(){}

    public Pengeluaran(String dataID, String namaPengeluaran, String totalPengeluaran, String tanggalPengeluaran) {
        this.dataID = dataID;
        this.namaPengeluaran = namaPengeluaran;
        TotalPengeluaran = totalPengeluaran;
        TanggalPengeluaran = tanggalPengeluaran;
    }

    public String getDataID() {
        return dataID;
    }

    public void setDataID(String dataID) {
        this.dataID = dataID;
    }

    public String getNamaPengeluaran() {
        return namaPengeluaran;
    }

    public void setNamaPengeluaran(String namaPengeluaran) {
        this.namaPengeluaran = namaPengeluaran;
    }

    public String getTotalPengeluaran() {
        return TotalPengeluaran;
    }

    public void setTotalPengeluaran(String totalPengeluaran) {
        TotalPengeluaran = totalPengeluaran;
    }

    public String getTanggalPengeluaran() {
        return TanggalPengeluaran;
    }

    public void setTanggalPengeluaran(String tanggalPengeluaran) {
        TanggalPengeluaran = tanggalPengeluaran;
    }
}
