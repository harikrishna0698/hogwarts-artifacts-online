package edu.tcu.cs.hogwarts_artifacts_online.wizard;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WizardService {
    private final WizardRepository wizardRepository;

    public WizardService(WizardRepository wizardRepository) {
        this.wizardRepository = wizardRepository;
    }

    public List<Wizard> findAll(){
        List<Wizard> wizards = this.wizardRepository.findAll();
        return wizards;
    }

    public Wizard findById(Integer wizardId){
        return this.wizardRepository.findById(wizardId)
                .orElseThrow(() -> new WizardNotFoundException(wizardId));
    }

    public Wizard save(Wizard newWizard){
        return this.wizardRepository.save(newWizard);
    }

    public Wizard update(Integer wizardId, Wizard update){
        Wizard oldWizard = this.wizardRepository.findById(wizardId)
                .orElseThrow(() -> new WizardNotFoundException(wizardId));
        oldWizard.setName(update.getName());
        Wizard updatedWizard = this.wizardRepository.save(oldWizard);
        return updatedWizard;
    }

    public void delete(Integer wizardId){
        this.wizardRepository.findById(wizardId)
                .orElseThrow(() ->new WizardNotFoundException(wizardId));
        this.wizardRepository.deleteById(wizardId);
    }
}
