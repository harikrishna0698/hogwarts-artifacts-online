package edu.tcu.cs.hogwarts_artifacts_online.artifact.converter;

import edu.tcu.cs.hogwarts_artifacts_online.artifact.Artifact;
import edu.tcu.cs.hogwarts_artifacts_online.artifact.dto.ArtifactDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ArtifactDtoToArtifactConverter{

    private static final Logger LOGGER = LoggerFactory.getLogger(ArtifactDtoToArtifactConverter.class);

    public Artifact convert(ArtifactDto source) {
        LOGGER.info("source : {}",source);
        Artifact artifact = new Artifact();
        LOGGER.info("Artifact obj created : {}",artifact);
        artifact.setId(source.id());
        artifact.setName(source.name());
        artifact.setDescription(source.description());
        artifact.setImageUrl(source.imageUrl());
        LOGGER.info("articat : {}",artifact);
        return artifact;
    }
}
