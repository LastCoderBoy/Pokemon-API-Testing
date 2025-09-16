package com.pokemonreview.api.repository;

import com.pokemonreview.api.Utils.PokemonUtils;
import com.pokemonreview.api.Utils.ReviewUtils;
import com.pokemonreview.api.models.Pokemon;
import com.pokemonreview.api.models.Review;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ReviewRepositoryTest {

    private Review reviewA;
    private Review reviewB;
    private Review reviewC;

    @Autowired
    private PokemonRepository pokemonRepository;

    @Autowired
    private ReviewRepository repositoryUnderTest;

    @BeforeEach
    public void setup(){
        reviewA = ReviewUtils.createReviewA();
        reviewB = ReviewUtils.createReviewB();
        reviewC = ReviewUtils.createReviewC();
    }

    // Save
    @Test
    public void testSaveReviewEntity_ReturnReviewEntity(){
        // Act
        Review savedReviewEntity = repositoryUnderTest.save(reviewA);

        // Assert
        Assertions.assertThat(savedReviewEntity).isNotNull();
        Assertions.assertThat(savedReviewEntity.getId()).isGreaterThan(0);
    }

    // Find By Pokemon Id & Return List
    @Test
    public void testFindByPokemonIdReviewEntity_ReturnReviewEntityList(){
        // Arrange
        Pokemon pokemonA = PokemonUtils.createPokemonA();
        Pokemon pokemonB = PokemonUtils.createPokemonB();

        pokemonRepository.save(pokemonA);
        pokemonRepository.save(pokemonB);

        reviewA.setPokemon(pokemonA);
        reviewC.setPokemon(pokemonA);
        reviewB.setPokemon(pokemonB);

        repositoryUnderTest.save(reviewA);
        repositoryUnderTest.save(reviewB);
        repositoryUnderTest.save(reviewC);

        // Act
        List<Review> actualPokemonList = repositoryUnderTest.findByPokemonId(pokemonA.getId());
        System.out.println(actualPokemonList);

        // Assert
        Assertions.assertThat(actualPokemonList).isNotNull();
        Assertions.assertThat(actualPokemonList.size()).isEqualTo(2);
        Assertions.assertThat(actualPokemonList.get(0).getContent()).isEqualTo(reviewA.getContent());
        Assertions.assertThat(actualPokemonList.get(1).getStars()).isEqualTo(reviewC.getStars());
    }

    // Find All
    @Test
    public void testFindAllReviewEntity_ReturnReviewEntityList(){
        repositoryUnderTest.save(reviewA);
        repositoryUnderTest.save(reviewB);
        repositoryUnderTest.save(reviewC);

        // Act
        List<Review> actualResultList = repositoryUnderTest.findAll();

        // Assert
        Assertions.assertThat(actualResultList).isNotNull();
        Assertions.assertThat(actualResultList.size()).isEqualTo(3);
    }

    // Update
    @Test
    public void testUpdateReviewEntity_ReturnReviewEntity(){
        // Arrange
        repositoryUnderTest.save(reviewA);
        repositoryUnderTest.save(reviewB);

        Review entityToBeUpdated = repositoryUnderTest.findById(reviewA.getId()).get();

        String newContent = "Updated Content";
        entityToBeUpdated.setContent(newContent);

        // Act
        Review updatedReviewEntity = repositoryUnderTest.save(entityToBeUpdated);

        // Assert
        Assertions.assertThat(updatedReviewEntity).isNotNull();
        Assertions.assertThat(updatedReviewEntity.getContent()).isEqualTo(newContent);
        Assertions.assertThat(updatedReviewEntity.getId()).isEqualTo(reviewA.getId());
        Assertions.assertThat(updatedReviewEntity.getStars()).isEqualTo(reviewA.getStars());
    }

    // Delete
    @Test
    public void testDeleteReviewEntity_ReturnReviewEntityIsEmpty(){
        repositoryUnderTest.save(reviewA);
        repositoryUnderTest.save(reviewB);
        repositoryUnderTest.save(reviewC);

        List<Review> entityListBeforeDelete = repositoryUnderTest.findAll();

        // Act
        repositoryUnderTest.deleteById(reviewC.getId());
        List<Review> entityListAfterDelete = repositoryUnderTest.findAll();

        // Assert
        Assertions.assertThat(repositoryUnderTest.findById(reviewC.getId())).isEmpty();
        Assertions.assertThat(entityListAfterDelete.size()).isEqualTo(entityListBeforeDelete.size() - 1);
        Assertions.assertThat(entityListAfterDelete).doesNotContain(reviewC);
    }
}

