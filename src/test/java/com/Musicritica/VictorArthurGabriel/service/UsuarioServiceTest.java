package com.Musicritica.VictorArthurGabriel.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.Musicritica.VictorArthurGabriel.controller.UsuarioController;
import com.Musicritica.VictorArthurGabriel.entity.usuario.CargoUsuario;
import com.Musicritica.VictorArthurGabriel.entity.usuario.DTO.AuthenticationDTO;
import com.Musicritica.VictorArthurGabriel.entity.usuario.DTO.LoginResponseDTO;
import com.Musicritica.VictorArthurGabriel.entity.usuario.DTO.RegistroDTO;
import com.Musicritica.VictorArthurGabriel.entity.usuario.Usuario;
import com.Musicritica.VictorArthurGabriel.exception.MusicriticaException;
import com.Musicritica.VictorArthurGabriel.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository repository;

    @Mock
    private ResourceLoader resourceLoader;

    @InjectMocks
    private UsuarioService usuarioService;


    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private UsuarioController authenticationService;

    private AuthenticationDTO dadosInvalidos;
    private AuthenticationDTO dadosValidos;

    private RegistroDTO registroDTO;

    private Usuario usuario1;
    private Usuario usuario2;

    @BeforeEach
    void setUp() {
        registroDTO = new RegistroDTO(
                "Nome Teste",
                "email@teste.com",
                "senha123",
                "26-06-2024",
                CargoUsuario.USER);

        usuario1 = new Usuario("Nome1", "email1@teste.com", "senha123", CargoUsuario.USER, "26-06-2024", new byte[]{}, new byte[]{});
        usuario2 = new Usuario("Nome2", "email2@teste.com", "senha456", CargoUsuario.ADMIN, "01-01-2024", new byte[]{}, new byte[]{});

        dadosValidos = new AuthenticationDTO("email@teste.com", "senha123");
        dadosInvalidos = new AuthenticationDTO("email@teste.com", "senhaErrada");
    }

    @Test
    void testeRegistrar() throws MusicriticaException {
        Resource avatarPlaceholder = mock(Resource.class);
        Resource backgroundPlaceholder = mock(Resource.class);
        when(resourceLoader.getResource("classpath:img/avatar_placeholder.png")).thenReturn(avatarPlaceholder);
        when(resourceLoader.getResource("classpath:img/background_placeholder.png")).thenReturn(backgroundPlaceholder);

        try {
            when(avatarPlaceholder.getInputStream()).thenReturn(mock(InputStream.class));
            when(backgroundPlaceholder.getInputStream()).thenReturn(mock(InputStream.class));
        } catch (IOException e) {
            System.out.println("Erro na imagem: " + e);
        }
        when(repository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        usuarioService.registrar(registroDTO);

        ArgumentCaptor<Usuario> usuarioCaptor = ArgumentCaptor.forClass(Usuario.class);
        verify(repository, times(1)).save(usuarioCaptor.capture());
        Usuario savedUsuario = usuarioCaptor.getValue();

        assertNotNull(savedUsuario);
        assertEquals(registroDTO.nome(), savedUsuario.getNome());
        assertEquals(registroDTO.email(), savedUsuario.getEmail());
        assertNotNull(savedUsuario.getSenha());
        assertEquals(CargoUsuario.USER, savedUsuario.getRole());
    }

    @Test
    void testeListarTodos() {
        List<Usuario> usuarios = Arrays.asList(usuario1, usuario2);

        when(repository.findAll()).thenReturn(usuarios);

        List<Usuario> result = usuarioService.listarTodos();

        verify(repository, times(1)).findAll();
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("email1@teste.com", result.get(0).getEmail());
        assertEquals("email2@teste.com", result.get(1).getEmail());
    }

    @Test
    void testeLoginValido() {
        Usuario usuarioCadastrado = new Usuario("Nome Teste", "email@teste.com", "senha123", null, null, null, null);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(usuarioCadastrado, null));

        String mockToken = "mocked-jwt-token";
        when(tokenService.generateToken(usuarioCadastrado)).thenReturn(mockToken);

        ResponseEntity<?> responseEntity = authenticationService.login(dadosValidos);

        assertNotNull(responseEntity);
        assertEquals(dadosValidos.email(), usuarioCadastrado.getEmail());
        assertEquals(dadosValidos.senha(), usuarioCadastrado.getSenha());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        LoginResponseDTO loginResponseDTO = (LoginResponseDTO) responseEntity.getBody();
        assertNotNull(loginResponseDTO);
        assertEquals(mockToken, loginResponseDTO.token());
    }

    @Test
    void testeLoginInvalido() {
        Usuario usuarioCadastrado = new Usuario("Nome Teste", "email@teste.com", "senha123", null, null, null, null);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(usuarioCadastrado, null));

        String mockToken = "mocked-jwt-token";
        when(tokenService.generateToken(usuarioCadastrado)).thenReturn(mockToken);

        ResponseEntity<?> responseEntity = authenticationService.login(dadosInvalidos);

        assertNotNull(responseEntity);
        assertEquals(dadosInvalidos.email(), usuarioCadastrado.getEmail());
        assertEquals(dadosInvalidos.senha(), usuarioCadastrado.getSenha());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        LoginResponseDTO loginResponseDTO = (LoginResponseDTO) responseEntity.getBody();
        assertNotNull(loginResponseDTO);
        assertEquals(mockToken, loginResponseDTO.token());
    }
}

