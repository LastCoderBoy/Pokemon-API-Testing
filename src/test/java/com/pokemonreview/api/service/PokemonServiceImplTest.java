package com.pokemonreview.api.service;

import com.pokemonreview.api.Utils.PokemonUtils;
import com.pokemonreview.api.dto.PokemonDto;
import com.pokemonreview.api.dto.PokemonResponse;
import com.pokemonreview.api.exceptions.PokemonNotFoundException;
import com.pokemonreview.api.models.Pokemon;
import com.pokemonreview.api.repository.PokemonRepository;
import com.pokemonreview.api.service.impl.PokemonServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PokemonServiceImplTest {
    private Pokemon pokemonA;
    private Pokemon pokemonB;
    private Pokemon pokemonC;

    @Mock
    private PokemonRepository pokemonRepository;

    @InjectMocks
    private PokemonServiceImpl serviceUnderTest;

    @BeforeEach
    public void setup() {
        pokemonA = PokemonUtils.createPokemonA();
        pokemonB = PokemonUtils.createPokemonB();
        pokemonC = PokemonUtils.createPokemonC();
    }

    @Test
    public void testCreatePokemon_ReturnPokemonDto() {
        // Arrange
        PokemonDto dto = PokemonDto.builder()
                .name("Pikachu")
                .type("Electric")
                .build();
        Pokemon savedPokemon = pokemonA;

        // Stubbing
        when(pokemonRepository.save(any(Pokemon.class))).thenReturn(savedPokemon);

        // Act
        PokemonDto actualResult = serviceUnderTest.createPokemon(dto);

        // Assert
        Assertions.assertThat(actualResult).isNotNull();
        Assertions.assertThat(actualResult.getId()).isEqualTo(savedPokemon.getId());
        Assertions.assertThat(actualResult.getName()).isEqualTo(savedPokemon.getName());
        Assertions.assertThat(actualResult.getType()).isEqualTo(savedPokemon.getType());

        // Argument Captor
        ArgumentCaptor<Pokemon> pokemonCaptor = ArgumentCaptor.forClass(Pokemon.class);
        verify(pokemonRepository).save(pokemonCaptor.capture());

        Pokemon passedEntity = pokemonCaptor.getValue();
        Assertions.assertThat(passedEntity.getName()).isEqualTo("Pikachu");
        Assertions.assertThat(passedEntity.getType()).isEqualTo("Electric");

        // Verification
        verify(pokemonRepository, times(1)).save(Mockito.any(Pokemon.class));
    }

    @Test
    public void testGetAllPokemon_ReturnPokemonResponse() {
        // Arrange
        int page = 0;
        int size = 10;
        List<Pokemon> pokemonList = Arrays.asList(pokemonA, pokemonB, pokemonC);
        Page<Pokemon> savedPokemonPage = new PageImpl<>(pokemonList, PageRequest.of(page, size), pokemonList.size());


        // Stubbing
        when(pokemonRepository.findAll(any(Pageable.class))).thenReturn(savedPokemonPage);


        // Act
        PokemonResponse actualResult = serviceUnderTest.getAllPokemon(page, size);

        // Assert
        Assertions.assertThat(actualResult).isNotNull();
        Assertions.assertThat(actualResult.getContent().size()).isEqualTo(pokemonList.size());
        Assertions.assertThat(actualResult.getContent().get(1).getName()).isEqualTo(pokemonB.getName());
        Assertions.assertThat(actualResult.getContent().get(2).getType()).isEqualTo(pokemonC.getType());
        Assertions.assertThat(actualResult.getTotalElements()).isEqualTo(savedPokemonPage.getTotalElements());
        Assertions.assertThat(actualResult.getPageNo()).isEqualTo(page);
        Assertions.assertThat(actualResult.getPageSize()).isEqualTo(size);
        Assertions.assertThat(actualResult.getTotalPages()).isEqualTo(1);
        Assertions.assertThat(actualResult.isLast()).isTrue();

        // Argument Captor
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(pokemonRepository).findAll(pageableCaptor.capture());

        Pageable captorValue = pageableCaptor.getValue();
        Assertions.assertThat(captorValue.getPageNumber()).isEqualTo(page);
        Assertions.assertThat(captorValue.getPageSize()).isEqualTo(size);

        // Verification
        verify(pokemonRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    public void testGetPokemonById_ReturnPokemonDto() {
        int id = 3;

        // Stubbing
        when(pokemonRepository.findById(id)).thenReturn(Optional.ofNullable(pokemonB));

        // Act
        PokemonDto actualPokemonDto = serviceUnderTest.getPokemonById(id);

        // Assert
        Assertions.assertThat(actualPokemonDto).isNotNull();
        Assertions.assertThat(actualPokemonDto.getId()).isEqualTo(pokemonB.getId());
        Assertions.assertThat(actualPokemonDto.getName()).isEqualTo(pokemonB.getName());
        Assertions.assertThat(actualPokemonDto.getType()).isEqualTo(pokemonB.getType());

        verify(pokemonRepository, times(1)).findById(id);
    }

    @Test
    public void testGetPokemonById_ThrowPokemonNotFoundException(){
        int id = 99;

        // Stubbing
        when(pokemonRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        Assertions.assertThatThrownBy(() -> serviceUnderTest.getPokemonById(id)).isInstanceOf(PokemonNotFoundException.class);
        verify(pokemonRepository, times(1)).findById(id);
        verify(pokemonRepository, never()).save(any());
    }

    @Test
    public void testUpdatePokemon_ReturnPokemonDto() {
        int idToUpdate = 1;
        PokemonDto newPokemonDto = PokemonDto.builder()
                .name("Charmander")
                .type("Fire")
                .build();

        // Stubbing
        when(pokemonRepository.findById(idToUpdate)).thenReturn(Optional.ofNullable(pokemonA));
        when(pokemonRepository.save(any(Pokemon.class))).thenReturn(pokemonA);

        // Act
        PokemonDto actualPokemonDto = serviceUnderTest.updatePokemon(newPokemonDto, idToUpdate);

        // Assert
        Assertions.assertThat(actualPokemonDto).isNotNull();
        Assertions.assertThat(actualPokemonDto.getId()).isEqualTo(pokemonA.getId());
        Assertions.assertThat(actualPokemonDto.getName()).isEqualTo(newPokemonDto.getName());
        Assertions.assertThat(actualPokemonDto.getType()).isEqualTo(newPokemonDto.getType());

        // Argument Captor
        ArgumentCaptor<Pokemon> pokemonCaptor = ArgumentCaptor.forClass(Pokemon.class);
        verify(pokemonRepository).save(pokemonCaptor.capture());

        Pokemon passedEntity = pokemonCaptor.getValue();
        Assertions.assertThat(passedEntity.getName()).isEqualTo("Charmander");
        Assertions.assertThat(passedEntity.getType()).isEqualTo("Fire");
        Assertions.assertThat(passedEntity.getId()).isEqualTo(idToUpdate);
    }

    @Test
    public void testDeletePokemon_ReturnVoid() {
        int idToDelete = 1;

        // Stubbing
        when(pokemonRepository.findById(idToDelete)).thenReturn(Optional.ofNullable(pokemonA));

        // Act
        serviceUnderTest.deletePokemonId(idToDelete);

        // Assert
        verify(pokemonRepository, times(1)).delete(pokemonA);
        verify(pokemonRepository, times(1)).findById(idToDelete);

        // Argument Captor
        ArgumentCaptor<Pokemon> captor = ArgumentCaptor.forClass(Pokemon.class);
        verify(pokemonRepository).delete(captor.capture());

        Pokemon deleted = captor.getValue();
        Assertions.assertThat(deleted.getId()).isEqualTo(idToDelete);
        Assertions.assertThat(deleted.getName()).isEqualTo("Pikachu");
    }

    @Test
    public void testDeletePokemon_ThrowPokemonNotFoundException(){
        when(pokemonRepository.findById(99)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> serviceUnderTest.deletePokemonId(99))
                .isInstanceOf(PokemonNotFoundException.class)
                .hasMessage("Pokemon could not be delete");

        verify(pokemonRepository, never()).delete(any());

    }
}
