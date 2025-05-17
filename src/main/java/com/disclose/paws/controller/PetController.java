package com.disclose.paws.controller;

import com.disclose.paws.model.dto.PetDTO;
import com.disclose.paws.service.PetService;
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
@RequestMapping("/pets")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Pets", description = "Endpoint para gerenciamento de pets para adoção")
public class PetController {

    private final PetService petService;

    @Operation(summary = "Criar novo pet", description = "Cria um novo pet no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pet criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de pet inválidos")
    })
    @PostMapping("/create")
    public ResponseEntity<PetDTO> createPet(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados do pet a ser criado")
            @RequestBody PetDTO petDTO) {
        return new ResponseEntity<>(petService.createPet(petDTO), HttpStatus.CREATED);
    }

    @Operation(summary = "Listar todos os pets", description = "Retorna todos os pets cadastrados no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pets listados com sucesso"),
            @ApiResponse(responseCode = "204", description = "Nenhum pet encontrado")
    })
    @GetMapping
    public ResponseEntity<List<PetDTO>> getAllPets() {
        List<PetDTO> pets = petService.getAllPets();
        if (pets.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(pets, HttpStatus.OK);
    }

    @Operation(summary = "Buscar pet por ID", description = "Retorna um pet específico pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pet encontrado"),
            @ApiResponse(responseCode = "404", description = "Pet não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PetDTO> getPetById(
            @Parameter(description = "ID do pet a ser buscado") @PathVariable Long id) {
        return new ResponseEntity<>(petService.getPetById(id), HttpStatus.OK);
    }

    @Operation(summary = "Excluir pet", description = "Remove um pet do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pet excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pet não encontrado")
    })
    @DeleteMapping
    public ResponseEntity<Void> deletePet(
            @Parameter(description = "ID do pet a ser excluído") @RequestHeader Long id) {
        petService.deletePet(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Atualizar pet", description = "Atualiza dados de um pet existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pet atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pet não encontrado")
    })
    @PutMapping("/update")
    public ResponseEntity<PetDTO> updatePet(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados atualizados do pet")
            @RequestBody PetDTO petDTO) {
        return new ResponseEntity<>(petService.updatePet(petDTO), HttpStatus.OK);
    }

    @Operation(summary = "Pesquisar pets", description = "Busca pets com filtros personalizados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso")
    })
    @GetMapping("/search")
    public ResponseEntity<Page<PetDTO>> searchPets(
            @Parameter(description = "Nome do pet") @RequestParam(required = false) String name,
            @Parameter(description = "Raça do pet") @RequestParam(required = false) String breed,
            @Parameter(description = "Idade do pet") @RequestParam(required = false) Long age,
            @Parameter(description = "Gênero do pet") @RequestParam(required = false) String gender,
            @Parameter(description = "Porte do pet") @RequestParam(required = false) String size,
            @Parameter(description = "Se o pet é castrado") @RequestParam(required = false) Boolean castrated,
            @Parameter(description = "Se o pet é vermifugado") @RequestParam(required = false) Boolean dewormed,
            @Parameter(description = "Se o pet é vacinado") @RequestParam(required = false) Boolean vaccinated,
            @Parameter(description = "Descrição do pet") @RequestParam(required = false) String description,
            @Parameter(description = "Opções de paginação") Pageable pageable) {

        Page<PetDTO> pets = petService.findPets(name, breed, age, gender, size,
                castrated, dewormed, vaccinated,
                description, pageable);
        return ResponseEntity.ok(pets);
    }
}