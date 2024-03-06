package com.Musicritica.VictorArthurGabriel.entity.usuario;

import org.springframework.web.multipart.MultipartFile;

public record UsuarioUpdateDTO(String nome, MultipartFile imagem_perfil) {
}
