package com.Musicritica.VictorArthurGabriel.entity.usuario;

public enum CargoUsuario {
    USER("usuario"),
    ADMIN("admin");

    private String cargo;

    CargoUsuario(String cargo){
        this.cargo = cargo;
    }

    public String getCargo(){
        return cargo;
    }
}
