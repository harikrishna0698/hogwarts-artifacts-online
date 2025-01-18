package edu.tcu.cs.hogwarts_artifacts_online.artifact.converter;

import edu.tcu.cs.hogwarts_artifacts_online.artifact.Artifact;
import edu.tcu.cs.hogwarts_artifacts_online.artifact.dto.ArtifactDto;
import edu.tcu.cs.hogwarts_artifacts_online.wizard.converter.WizardToWizardDtoConverter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ArtfiactToArtifactDtoConverter {

    private final WizardToWizardDtoConverter wizardToWizardDtoConverter;

    public ArtfiactToArtifactDtoConverter(WizardToWizardDtoConverter wizardToWizardDtoConverter) {
        this.wizardToWizardDtoConverter = wizardToWizardDtoConverter;
    }


    public ArtifactDto convert(Artifact source) {
        ArtifactDto artifactDto =   new ArtifactDto(source.getId(),
                source.getName(), source.getDescription(), source.getImageUrl(),
                source.getOwner()!=null
                        ? this.wizardToWizardDtoConverter.convert(source.getOwner()) : null);
        return artifactDto;
    }
}
