package edu.tcu.cs.hogwarts_artifacts_online.wizard.converter;

import edu.tcu.cs.hogwarts_artifacts_online.wizard.Wizard;
import edu.tcu.cs.hogwarts_artifacts_online.wizard.dto.WizardDto;
import org.springframework.stereotype.Component;

@Component
public class WizardDtoToWizardConverter {
    public Wizard convert(WizardDto source) {
        Wizard wizard = new Wizard();
        wizard.setId(source.id());
        wizard.setName(source.name());
        return wizard;
    }
}
