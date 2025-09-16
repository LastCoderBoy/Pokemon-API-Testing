package com.pokemonreview.api.repository;

import com.pokemonreview.api.Utils.UserUtils;
import com.pokemonreview.api.models.UserEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserRepositoryTest {
    private UserEntity userA;
    private UserEntity userB;
    private UserEntity userC;
    private UserEntity userD;

    @Autowired
    private UserRepository repositoryUnderTest;

    @BeforeEach
    public void setup(){
        userA = UserUtils.createUserA();
        userB = UserUtils.createUserB();
        userC = UserUtils.createUserC();
        userD = UserUtils.createUserD();
    }

    @Test
    public void testFindByUsername_ReturnUserEntity(){
        repositoryUnderTest.save(userA);
        repositoryUnderTest.save(userB);
        repositoryUnderTest.save(userC);

        // Act
        Optional<UserEntity> actualResponseOpt = repositoryUnderTest.findByUsername(userB.getUsername());

        // Assert
        if(actualResponseOpt.isPresent()){
            UserEntity actualResponse = actualResponseOpt.get();
            Assertions.assertThat(actualResponse.getUsername()).isEqualTo(userB.getUsername());
            Assertions.assertThat(actualResponse.getPassword()).isEqualTo(userB.getPassword());
        }
    }

    @Test
    public void testFindByUsername_ReturnEmpty(){
        repositoryUnderTest.save(userA);
        repositoryUnderTest.save(userB);
        repositoryUnderTest.save(userC);

        // Act
        Optional<UserEntity> actualResponseOpt = repositoryUnderTest.findByUsername(userD.getUsername());

        // Assert
        Assertions.assertThat(actualResponseOpt).isEmpty();
    }

    @Test
    public void testExistsByUsername_ReturnTrue(){
        repositoryUnderTest.save(userA);
        repositoryUnderTest.save(userB);
        repositoryUnderTest.save(userC);

        // Act
        Boolean actualCondition = repositoryUnderTest.existsByUsername(userC.getUsername());

        // Assert
        Assertions.assertThat(actualCondition).isTrue();
    }

    @Test
    public void testExistsByUsername_ReturnFalse(){
        repositoryUnderTest.save(userA);
        repositoryUnderTest.save(userB);
        repositoryUnderTest.save(userC);

        // Act
        Boolean actualCondition = repositoryUnderTest.existsByUsername(userD.getUsername());

        // Assert
        Assertions.assertThat(actualCondition).isFalse();
    }
}
