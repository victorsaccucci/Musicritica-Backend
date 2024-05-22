package com.Musicritica.VictorArthurGabriel.entity.usuario.DTO;

import org.springframework.web.multipart.MultipartFile;

public record UsuarioUpdateDTO(String nome, MultipartFile imagem_perfil, MultipartFile imagem_background) {
}
