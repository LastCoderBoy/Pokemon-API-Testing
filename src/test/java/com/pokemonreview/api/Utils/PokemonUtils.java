package com.pokemonreview.api.Utils;

import com.pokemonreview.api.models.Pokemon;

public class PokemonUtils {

    public static Pokemon createPokemonA() {
        return Pokemon.builder()
                .name("Pikachu")
                .type("Electric")
                .build();
    }
    public static Pokemon createPokemonB() {
        return Pokemon.builder()
                .name("Bulbasaur")
                .type("Grass")
                .build();
    }
    public static Pokemon createPokemonC() {
        return Pokemon.builder()
                .name("Charmander")
                .type("Fire")
                .build();
    }
    public static Pokemon createPokemonD() {
        return Pokemon.builder()
                .name("Squirtle")
                .type("Water")
                .build();
    }
}
