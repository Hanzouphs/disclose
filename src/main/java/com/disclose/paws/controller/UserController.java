package com.disclose.paws.controller;

import com.disclose.paws.model.dto.UserDTO;
import com.disclose.paws.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Usuários", description = "Endpoint para gerenciamento de usuários do sistema")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Criar novo usuário", description = "Cadastra um novo usuário no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de usuário inválidos")
    })
    @PostMapping("/create")
    public ResponseEntity<UserDTO> createUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados do usuário a ser criado")
            @RequestBody UserDTO userDTO) {
        return new ResponseEntity<>(userService.createUser(userDTO), HttpStatus.CREATED);
    }

    @Operation(summary = "Listar todos os usuários", description = "Retorna todos os usuários cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuários listados com sucesso"),
            @ApiResponse(responseCode = "204", description = "Nenhum usuário encontrado")
    })
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        if (users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @Operation(summary = "Buscar usuário por ID", description = "Retorna um usuário específico pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(
            @Parameter(description = "ID do usuário a ser buscado") @PathVariable Long id) {
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }

    @Operation(summary = "Excluir usuário", description = "Remove um usuário do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuário excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @DeleteMapping
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID do usuário a ser excluído") @RequestHeader Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Atualizar usuário", description = "Atualiza dados de um usuário existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @PutMapping("/update")
    public ResponseEntity<UserDTO> updateUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados atualizados do usuário")
            @RequestBody UserDTO userDTO) {
        return new ResponseEntity<>(userService.updateUser(userDTO), HttpStatus.OK);
    }

    @Operation(summary = "Pesquisar usuários", description = "Busca usuários com filtros personalizados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso")
    })
    @GetMapping("/search")
    public ResponseEntity<Page<UserDTO>> searchUsers(
            @Parameter(description = "Nome do usuário") @RequestParam(required = false) String name,
            @Parameter(description = "Email do usuário") @RequestParam(required = false) String email,
            @Parameter(description = "Status de atividade") @RequestParam(required = false) Boolean active,
            @Parameter(description = "Papel/função do usuário") @RequestParam(required = false) String role,
            @Parameter(description = "Nome de usuário") @RequestParam(required = false) String username,
            @Parameter(description = "Telefone de contato") @RequestParam(required = false) String phoneNumber,
            @Parameter(description = "Cidade") @RequestParam(required = false) String city,
            @Parameter(description = "Estado/UF") @RequestParam(required = false) String state,
            @Parameter(description = "País") @RequestParam(required = false) String country,
            @Parameter(description = "CEP") @RequestParam(required = false) String postalCode,
            @Parameter(description = "Opções de paginação") Pageable pageable) {

        Page<UserDTO> users = userService.findUsers(name, email, active, role,
                username, phoneNumber, city,
                state, country, postalCode,
                pageable);
        return ResponseEntity.ok(users);
    }
}