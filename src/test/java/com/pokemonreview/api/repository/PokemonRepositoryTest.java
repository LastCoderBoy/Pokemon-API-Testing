package com.pokemonreview.api.repository;

import com.pokemonreview.api.Utils.PokemonUtils;
import com.pokemonreview.api.models.Pokemon;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class PokemonRepositoryTest {

    private Pokemon pokemonA;
    private Pokemon pokemonB;
    private Pokemon pokemonC;
    private Pokemon pokemonD;


    @Autowired
    private PokemonRepository pokemonRepository;

    @BeforeEach
    public void setup() {
        pokemonA = PokemonUtils.createPokemonA();
        pokemonB = PokemonUtils.createPokemonB();
        pokemonC = PokemonUtils.createPokemonC();
        pokemonD = PokemonUtils.createPokemonD();
    }

    @Test
    public void testSavePokemonEntity_ReturnPokemonEntity() {
        // Act
        Pokemon savedPokemon = pokemonRepository.save(pokemonA);

        // Assert
        Assertions.assertThat(savedPokemon).isNotNull();
        Assertions.assertThat(savedPokemon.getId()).isGreaterThan(0);
    }

    @Test
    public void testFindAllPokemon_ReturnPokemonList() {
        pokemonRepository.save(pokemonA);
        pokemonRepository.save(pokemonB);
        pokemonRepository.save(pokemonC);
        pokemonRepository.save(pokemonD);

        // Act
        List<Pokemon> actualResultList = pokemonRepository.findAll();

        // Assert
        Assertions.assertThat(actualResultList).isNotNull();
        Assertions.assertThat(actualResultList.size()).isEqualTo(4);
        Assertions.assertThat(actualResultList.get(0).getName()).isEqualTo("Pikachu");
        Assertions.assertThat(actualResultList.get(2).getType()).isEqualTo("Fire");
    }

    @Test
    public void testFindByIdPokemon_ReturnPokemonEntity() {
        pokemonRepository.save(pokemonA);
        pokemonRepository.save(pokemonB);
        pokemonRepository.save(pokemonC);

        // Act
        System.out.println("Saved PokemonA ID: " + pokemonA.getId());
        Optional<Pokemon> actualPokemonEntity = pokemonRepository.findById(pokemonA.getId());

        // Assert
        if(actualPokemonEntity.isPresent()) {
            Assertions.assertThat(actualPokemonEntity.get().getName()).isEqualTo("Pikachu");
            Assertions.assertThat(actualPokemonEntity.get().getType()).isEqualTo("Electric");
        }
        else {
            Assertions.fail("Pokemon not found");
        }
    }


    @Test
    public void testFindByTypePokemon_ReturnPokemonEntity() {
        pokemonRepository.save(pokemonA);
        pokemonRepository.save(pokemonD);

        // Act
        Optional<Pokemon> actualPokemonEntity = pokemonRepository.findByType(pokemonD.getType());
        if(actualPokemonEntity.isPresent()) {
            Assertions.assertThat(actualPokemonEntity.get().getName()).isEqualTo("Squirtle");
            Assertions.assertThat(actualPokemonEntity.get().getType()).isEqualTo("Water");
        }else{
            Assertions.fail("Pokemon not found");
        }
    }

    @Test
    public void testUpdatePokemonEntity_ReturnPokemonEntity() {
        // Arrange
        pokemonRepository.save(pokemonA);
        pokemonRepository.save(pokemonB);

        Optional<Pokemon> pokemonEntityOpt = pokemonRepository.findById(pokemonA.getId());
        pokemonEntityOpt.ifPresent(
                pokemonEntity -> pokemonEntity.setName("Charmander")
        );

        // Act
        Pokemon updatedPokemon = pokemonRepository.save(pokemonEntityOpt.get());

        // Assert
        Assertions.assertThat(updatedPokemon).isNotNull();
        Assertions.assertThat(updatedPokemon.getName()).isEqualTo("Charmander");
        Assertions.assertThat(updatedPokemon.getId()).isEqualTo(pokemonA.getId());
        Assertions.assertThat(updatedPokemon.getType()).isEqualTo(pokemonA.getType());
    }

    @Test
    public void testDeletePokemonEntity_ReturnPokemonIsEmpty() {
        pokemonRepository.save(pokemonA);
        pokemonRepository.save(pokemonB);
        pokemonRepository.save(pokemonC);

        List<Pokemon> entityListBeforeDelete = pokemonRepository.findAll();

        // Act
        pokemonRepository.deleteById(pokemonC.getId());
        List<Pokemon> entityListAfterDelete = pokemonRepository.findAll();
        Optional<Pokemon> deletedPokemon = pokemonRepository.findById(pokemonC.getId());

        // Assert
        Assertions.assertThat(deletedPokemon).isEmpty();
        Assertions.assertThat(entityListAfterDelete.size()).isEqualTo(entityListBeforeDelete.size() - 1);
        Assertions.assertThat(entityListAfterDelete).doesNotContain(pokemonC);
    }
}
