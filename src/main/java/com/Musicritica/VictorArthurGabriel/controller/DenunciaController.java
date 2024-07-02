package com.Musicritica.VictorArthurGabriel.controller;

import com.Musicritica.VictorArthurGabriel.entity.Comentario;
import com.Musicritica.VictorArthurGabriel.entity.Denuncia;
import com.Musicritica.VictorArthurGabriel.entity.Musica;
import com.Musicritica.VictorArthurGabriel.entity.usuario.Usuario;
import com.Musicritica.VictorArthurGabriel.service.DenunciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/denuncia")
@CrossOrigin(origins = {"http://localhost:4200","http://localhost:5500"}, maxAge = 3600)
public class DenunciaController {

    @Autowired
    private DenunciaService service;

    @Autowired
    private ComentarioController controller;

    @Autowired
    private UsuarioController usuarioController;

    private final SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private final SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @PostMapping(value = "/{usuarioId}/{comentarioId}")
    public ResponseEntity<?> salvar(@PathVariable Long usuarioId,
                                    @PathVariable Long comentarioId) {
        try {
            Denuncia denuncia = new Denuncia();

            Comentario comentario = controller.buscarComentarioPorId(comentarioId);
            if (comentario == null) {

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comentário não encontrado.");
            }

            Usuario usuarioReportado = comentario.getUsuario();
            if (usuarioReportado == null) {

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário reportado não encontrado.");
            }

            Usuario usuario = usuarioController.buscarPeloId(usuarioId);
            if (usuario == null) {

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
            }


            denuncia.setComentario(comentario);
            denuncia.setUsuarioReportado(usuarioReportado);
            denuncia.setUsuario(usuario);

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String dataFormatada = formatter.format(new Date());
            denuncia.setDt_denuncia(dataFormatada);
            denuncia.setStatus(true);

            Denuncia denunciaSalva = service.salvar(denuncia);

            return ResponseEntity.ok(denunciaSalva);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao salvar denúncia: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao salvar denúncia: " + e.getMessage());
        }
    }


    @GetMapping(value = "/listarTodos")
    public List<Denuncia> listarTodos() {
        List <Denuncia> denuncias = service.listarTodos();
        return denuncias;
    }

    @GetMapping(value = "/buscarDenunciasFechadas")
    public List<Denuncia> buscarDenunciasFechadas() {
        List <Denuncia> denuncias = service.buscarDenunciasFechadas();
        return denuncias;
    }

    @GetMapping(value = "/buscar/{nome}")
    public List<Denuncia> buscarPorNome(@PathVariable String nome) {

        List <Denuncia> denuncias = service.buscarPorNome(nome);
        return denuncias;
    }

    @GetMapping("/buscarPorData/{dataInicio}/{dataFim}")
    public List<Denuncia> buscarPorData(
            @PathVariable String dataInicio,
            @PathVariable String dataFim) {
        // Print the dates in dd/MM/yyyy format
        String formattedDataInicio = formatToDDMMYYYY(dataInicio);
        String formattedDataFim = formatToDDMMYYYY(dataFim);

        System.out.println("Data Inicio (dd/MM/yyyy): " + formattedDataInicio);
        System.out.println("Data Fim (dd/MM/yyyy): " + formattedDataFim);

        List<Denuncia> denuncias = service.buscarPorData(formattedDataInicio, formattedDataFim);

        return denuncias;
    }

    private String formatToDDMMYYYY(String date) {
        if (date == null || date.isEmpty()) {
            return "";
        }
        String[] parts = date.split("-");
        if (parts.length != 3) {
            return date;
        }
        return parts[2] + "/" + parts[1] + "/" + parts[0];
    }

    @PutMapping("/fechar/{id}")
    public ResponseEntity<?> fecharDenuncia(@PathVariable Long id) {

            boolean isClosed = service.fecharDenuncia(id);
            if (isClosed) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Denúncia não encontrada");
            }
    }



}
