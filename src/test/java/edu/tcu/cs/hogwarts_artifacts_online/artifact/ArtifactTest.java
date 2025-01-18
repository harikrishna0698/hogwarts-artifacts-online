package edu.tcu.cs.hogwarts_artifacts_online.artifact;

import edu.tcu.cs.hogwarts_artifacts_online.wizard.Wizard;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ArtifactTest {

    @Test
    void testGetterSetter() {
        // Create an instance of Artifact
        Artifact artifact = new Artifact();

        // Set values using setters
        artifact.setId("1");
        artifact.setName("Magic Wand");
        artifact.setDescription("A powerful wand used by wizards.");
        artifact.setImageUrl("http://example.com/wand.png");

        // Create a mock Wizard object
        Wizard wizard = new Wizard();
        artifact.setOwner(wizard);

        // Assert values using getters
        assertThat(artifact.getId()).isEqualTo("1");
        assertThat(artifact.getName()).isEqualTo("Magic Wand");
        assertThat(artifact.getDescription()).isEqualTo("A powerful wand used by wizards.");
        assertThat(artifact.getImageUrl()).isEqualTo("http://example.com/wand.png");
        assertThat(artifact.getOwner()).isEqualTo(wizard);
    }
}
