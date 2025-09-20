package com.pokemonreview.api.service;


import com.pokemonreview.api.Utils.PokemonUtils;
import com.pokemonreview.api.Utils.ReviewUtils;
import com.pokemonreview.api.dto.PokemonDto;
import com.pokemonreview.api.dto.ReviewDto;
import com.pokemonreview.api.exceptions.PokemonNotFoundException;
import com.pokemonreview.api.models.Pokemon;
import com.pokemonreview.api.models.Review;
import com.pokemonreview.api.repository.PokemonRepository;
import com.pokemonreview.api.repository.ReviewRepository;
import com.pokemonreview.api.service.impl.ReviewServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceImplTest {
    private Pokemon pokemon;
    private Pokemon pokemonC;
    private PokemonDto pokemonDto;
    private Review reviewA;
    private Review reviewB;
    private Review reviewC;
    private ReviewDto reviewDto;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private PokemonRepository pokemonRepository;

    @InjectMocks
    private ReviewServiceImpl serviceUnderTest;

    @BeforeEach
    public void setup(){
        pokemon = PokemonUtils.createPokemonA();
        pokemonC = PokemonUtils.createPokemonC();
        pokemonDto = PokemonDto.builder()
                .name("Pikachu")
                .type("Electric")
                .build();

        reviewA = ReviewUtils.createReviewA();
        reviewB = ReviewUtils.createReviewB();
        reviewC = ReviewUtils.createReviewC();
        reviewDto = ReviewDto.builder()
                .title("Good Pokemon")
                .content("Little bit good in battles")
                .stars(3)
                .pokemonId(3)
                .build();
    }

    @Test
    public void testCreateReview_ReturnReviewDto(){
        int pokemonId = 1;

        // Stubbing
        when(pokemonRepository.findById(pokemonId)).thenReturn(Optional.of(pokemon));
        when(reviewRepository.save(any(Review.class))).thenReturn(reviewA);

        // Act
        ReviewDto actualResponseDto = serviceUnderTest.createReview(pokemonId, reviewDto);

        // Assert the Response
        Assertions.assertThat(actualResponseDto).isNotNull();
        Assertions.assertThat(actualResponseDto.getId()).isEqualTo(reviewA.getId());
        Assertions.assertThat(actualResponseDto.getTitle()).isEqualTo(reviewA.getTitle());
        Assertions.assertThat(actualResponseDto.getContent()).isEqualTo(reviewA.getContent());
        Assertions.assertThat(actualResponseDto.getStars()).isEqualTo(reviewA.getStars());

        // Assert the Pass Argument
        ArgumentCaptor<Review> reviewCaptor = ArgumentCaptor.forClass(Review.class);
        verify(reviewRepository).save(reviewCaptor.capture());

        Review captorValue = reviewCaptor.getValue();
        Assertions.assertThat(captorValue.getPokemon()).isEqualTo(pokemon);
        Assertions.assertThat(captorValue.getTitle()).isEqualTo(reviewDto.getTitle());
        Assertions.assertThat(captorValue.getContent()).isEqualTo(reviewDto.getContent());
        Assertions.assertThat(captorValue.getStars()).isEqualTo(reviewDto.getStars());
    }

    @Test
    public void testCreateReview_ThrowPokemonNotFoundException(){
        int pokemonId = 99;
        // Stubbing
        when(pokemonRepository.findById(pokemonId)).thenReturn(Optional.empty());

        // Act & Assert using JUnit
        Assertions.assertThatThrownBy(() -> serviceUnderTest.createReview(pokemonId, reviewDto))
                .isInstanceOf(PokemonNotFoundException.class)
                .hasMessage("Pokemon with associated review not found");

        // Verify the repository interactions
        verify(pokemonRepository, times(1)).findById(pokemonId);
        verify(reviewRepository, never()).save(any());
    }

    @Test
    public void testGetReviewsByPokemonId_ReturnReviewDtoList(){
        int pokemonId = 2;
        List<Review> reviews = Arrays.asList(reviewB, reviewC);
        // Stubbing
        when(reviewRepository.findByPokemonId(pokemonId)).thenReturn(reviews);

        // Act
        List<ReviewDto> actualResponseDtoList = serviceUnderTest.getReviewsByPokemonId(2);

        // Assert
        assertEquals(2, actualResponseDtoList.size());
        Assertions.assertThat(actualResponseDtoList.get(0).getId()).isEqualTo(reviewB.getId());
        Assertions.assertThat(actualResponseDtoList.get(0).getPokemonId()).isEqualTo(2);
        Assertions.assertThat(actualResponseDtoList.get(1).getPokemonId()).isEqualTo(2);

        // Assert the Pass Argument
        ArgumentCaptor<Integer> pokemonCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(reviewRepository).findByPokemonId(pokemonCaptor.capture());
        assertEquals(pokemonId, pokemonCaptor.getValue());
    }

    @Test
    public void testGetAllReviews_ReturnReviewDtoList(){
        List<Review> reviews = Arrays.asList(reviewA, reviewB, reviewC);
        // Stubbing
        when(reviewRepository.findAll()).thenReturn(reviews);

        // Act
        List<ReviewDto> actualResponseDtoList = serviceUnderTest.getAllReviews();

        // Assert
        assertEquals(3, actualResponseDtoList.size());
        Assertions.assertThat(actualResponseDtoList.get(0).getId()).isEqualTo(reviewA.getId());
        Assertions.assertThat(actualResponseDtoList.get(0).getPokemonId()).isEqualTo(pokemon.getId());

    }

    @Test
    public void testUpdateReview_ReturnReviewDto(){
        int reviewId = 1;
        int pokemonId = 1;

        // Stubbing
        when(pokemonRepository.findById(pokemonId)).thenReturn(Optional.of(pokemon));
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(reviewA));
        when(pokemonRepository.findById(reviewDto.getPokemonId())).thenReturn(Optional.of(pokemonC));
        when(reviewRepository.save(any(Review.class))).thenReturn(reviewA);

        // Act: reviewA will be updated with reviewDto
        ReviewDto updatedReviewDto = serviceUnderTest.updateReview(pokemonId, reviewId, reviewDto);

        // Assert
        Assertions.assertThat(updatedReviewDto).isNotNull();
        Assertions.assertThat(updatedReviewDto.getTitle()).isEqualTo(reviewDto.getTitle());
        Assertions.assertThat(updatedReviewDto.getContent()).isEqualTo(reviewDto.getContent());
        Assertions.assertThat(updatedReviewDto.getStars()).isEqualTo(reviewDto.getStars());

        // Assert the Pass Argument
        ArgumentCaptor<Review> reviewCaptor = ArgumentCaptor.forClass(Review.class);
        verify(reviewRepository).save(reviewCaptor.capture());

        Review captorValue = reviewCaptor.getValue();
        Assertions.assertThat(captorValue.getPokemon()).isEqualTo(pokemonC);
        Assertions.assertThat(captorValue.getTitle()).isEqualTo(reviewDto.getTitle());
        Assertions.assertThat(captorValue.getContent()).isEqualTo(reviewDto.getContent());
    }

    @Test
    public void testDeleteReview_ReturnVoid() {
        int pokemonId = 1;
        int reviewId = 1;

        // Stubbing
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(reviewA));

        // Act
        serviceUnderTest.deleteReview(pokemonId, reviewId);

        // Assert
        ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(reviewRepository, times(1)).findById(idCaptor.capture());
        assertEquals(reviewId, idCaptor.getValue());

        ArgumentCaptor<Review> reviewCaptor = ArgumentCaptor.forClass(Review.class);
        verify(reviewRepository, times(1)).delete(reviewCaptor.capture());
        assertEquals(reviewA, reviewCaptor.getValue());
    }
}
