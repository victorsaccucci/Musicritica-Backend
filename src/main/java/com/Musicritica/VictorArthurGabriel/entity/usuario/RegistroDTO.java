package com.Musicritica.VictorArthurGabriel.entity.usuario;

import org.springframework.web.multipart.MultipartFile;

public record RegistroDTO(String nome, String email, String senha, String dt_cadastro, CargoUsuario cargo, MultipartFile imagem_perfil) {
}
