package com.Musicritica.VictorArthurGabriel.entity.usuario.DTO;

import com.Musicritica.VictorArthurGabriel.entity.usuario.CargoUsuario;
import org.springframework.web.multipart.MultipartFile;

public record RegistroDTO(String nome, String email, String senha, String dt_cadastro, CargoUsuario cargo, byte[] imagem_perfil) {
}
