/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chriswatnee.martinis.webservice;

import com.chriswatnee.martinis.commandmodel.block.createblock.CreateBlockCommandModel;
import com.chriswatnee.martinis.commandmodel.block.createblockbelow.CreateBlockBelowCommandModel;
import com.chriswatnee.martinis.commandmodel.block.editblock.EditBlockCommandModel;
import com.chriswatnee.martinis.dto.Block;
import com.chriswatnee.martinis.dto.Person;
import com.chriswatnee.martinis.dto.Project;
import com.chriswatnee.martinis.dto.Scene;
import com.chriswatnee.martinis.service.BlockService;
import com.chriswatnee.martinis.service.PersonService;
import com.chriswatnee.martinis.service.ProjectService;
import com.chriswatnee.martinis.service.SceneService;
import com.chriswatnee.martinis.viewmodel.block.createblock.CreateBlockViewModel;
import com.chriswatnee.martinis.viewmodel.block.createblock.CreatePersonViewModel;
import com.chriswatnee.martinis.viewmodel.block.createblockbelow.CreateBlockBelowViewModel;
import com.chriswatnee.martinis.viewmodel.block.editblock.EditBlockViewModel;
import com.chriswatnee.martinis.viewmodel.block.editblock.EditPersonViewModel;
import com.chriswatnee.martinis.viewmodel.scene.sceneprofile.BlockViewModel;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Inject;

/**
 *
 * @author chris
 */
public class BlockWebServiceImpl implements BlockWebService {

    BlockService blockService;
    PersonService personService;
    SceneService sceneService;
    ProjectService projectService;

    @Inject
    public BlockWebServiceImpl(BlockService blockService, PersonService personService, SceneService sceneService, ProjectService projectService) {
        this.blockService = blockService;
        this.personService = personService;
        this.sceneService = sceneService;
        this.projectService = projectService;
    }
    
    @Override
    public CreateBlockViewModel getCreateBlockViewModel(Integer sceneId) {

        // Instantiate
        CreateBlockViewModel createBlockViewModel = new CreateBlockViewModel();
        
        // Look up stuff
        Scene scene = sceneService.read(sceneId);
        Project project = projectService.getProjectByScene(scene);

        // Populate commmand model
        CreateBlockCommandModel commandModel = new CreateBlockCommandModel();
        commandModel.setSceneId(scene.getId());
        
        createBlockViewModel.setCreateBlockCommandModel(commandModel);
        
        List<Person> persons = personService.getPersonsByProject(project);

        // Populate
        createBlockViewModel.setSceneId(scene.getId());
        createBlockViewModel.setPersons(translateCreatePersonViewModel(persons));

        return createBlockViewModel;
    }
    
    @Override
    public CreateBlockBelowViewModel getCreateBlockBelowViewModel(Integer id) {

        // Instantiate
        CreateBlockBelowViewModel createBlockBelowViewModel = new CreateBlockBelowViewModel();
        
        // Look up stuff
        Block existingBlock = blockService.read(id);
        Scene scene = sceneService.read(existingBlock.getScene().getId());
        Project project = projectService.getProjectByScene(scene);

        // Populate commmand model
        CreateBlockBelowCommandModel commandModel = new CreateBlockBelowCommandModel();
        commandModel.setId(existingBlock.getId());
        commandModel.setSceneId(scene.getId());
        
        createBlockBelowViewModel.setCreateBlockBelowCommandModel(commandModel);
        
        List<Person> persons = personService.getPersonsByProject(project);

        // Populate
        createBlockBelowViewModel.setSceneId(scene.getId());
        createBlockBelowViewModel.setPersons(translateCreatePersonViewModel(persons));

        return createBlockBelowViewModel;
    }

    @Override
    public EditBlockViewModel getEditBlockViewModel(Integer id) {

        // Instantiate
        EditBlockViewModel editBlockViewModel = new EditBlockViewModel();

        // Look up stuff
        Block existingBlock = blockService.read(id);
        
        List<Person> allPersons = personService.list();
        Person selectedPerson = null;
        if (existingBlock.getPerson() != null) {
            selectedPerson = personService.read(existingBlock.getPerson().getId());
        }
        
        Scene selectedScene = sceneService.read(existingBlock.getScene().getId());
        
        // Populate
        editBlockViewModel.setSceneId(selectedScene.getId());
        editBlockViewModel.setPersons(translateEditPersonViewModel(allPersons));
        
        // Populate commmand model
        EditBlockCommandModel commandModel = new EditBlockCommandModel();
        commandModel.setId(existingBlock.getId());
        commandModel.setContent(existingBlock.getContent());
        
        if (selectedPerson != null) {
            commandModel.setPersonId(selectedPerson.getId());
        }
        
        commandModel.setSceneId(selectedScene.getId());

        editBlockViewModel.setEditBlockCommandModel(commandModel);

        return editBlockViewModel;
    }

    @Override
    public Block saveCreateBlockCommandModel(CreateBlockCommandModel createBlockCommandModel) {

        // Instantiate
        Block block = new Block();

        // Look up stuff
        Person person = null;
        if (createBlockCommandModel.getPersonId() != null) {
            person = personService.read(createBlockCommandModel.getPersonId());
        }

        Scene scene = sceneService.read(createBlockCommandModel.getSceneId());

        String content = createBlockCommandModel.getContent();

        if (person == null) {
            String characterName = extractCharacterName(content);
            if (characterName != null) {
                Project project = projectService.getProjectByScene(scene);
                person = findOrCreatePerson(characterName, project);
                content = stripCharacterName(content);
            }
        }

        // Put stuff
        block.setContent(content);

        if (person != null) {
            block.setPerson(person);
        }

        block.setScene(scene);

        // Save stuff
        block = blockService.create(block);

        return block;
    }

    @Override
    public Block saveCreateBlockBelowCommandModel(CreateBlockBelowCommandModel createBlockBelowCommandModel) {

        // Instantiate
        Block block = new Block();

        // Look up stuff
        Block existingBlock = blockService.read(createBlockBelowCommandModel.getId());

        Person person = null;
        if (createBlockBelowCommandModel.getPersonId() != null) {
            person = personService.read(createBlockBelowCommandModel.getPersonId());
        }

        Scene scene = sceneService.read(existingBlock.getScene().getId());

        String content = createBlockBelowCommandModel.getContent();

        if (person == null) {
            String characterName = extractCharacterName(content);
            if (characterName != null) {
                Project project = projectService.getProjectByScene(scene);
                person = findOrCreatePerson(characterName, project);
                content = stripCharacterName(content);
            }
        }

        // Put stuff
        block.setOrder(existingBlock.getOrder());
        block.setContent(content);

        if (person != null) {
            block.setPerson(person);
        }

        block.setScene(scene);

        // Save stuff
        block = blockService.createBelow(block);

        return block;
    }

    @Override
    public Block saveEditBlockCommandModel(EditBlockCommandModel editBlockCommandModel) {

        // Instantiate
        Block block = blockService.read(editBlockCommandModel.getId());

        // Look up stuff
        Person person = null;
        if (editBlockCommandModel.getPersonId() != null) {
            person = personService.read(editBlockCommandModel.getPersonId());
        }
        Scene scene = sceneService.read(editBlockCommandModel.getSceneId());

        String content = editBlockCommandModel.getContent();

        if (person == null) {
            String characterName = extractCharacterName(content);
            if (characterName != null) {
                Project project = projectService.getProjectByScene(scene);
                person = findOrCreatePerson(characterName, project);
                content = stripCharacterName(content);
            }
        }

        // Put stuff
        block.setContent(content);
        block.setPerson(person);
        block.setScene(scene);

        // Save stuff
        blockService.update(block);

        return block;
    }
    
    @Override
    public BlockViewModel getBlockViewModel(Integer id) {
        Block block = blockService.read(id);
        BlockViewModel vm = new BlockViewModel();
        vm.setId(block.getId());
        vm.setOrder(block.getOrder());
        vm.setContent(block.getContent());
        if (block.getPerson() != null) {
            Person person = personService.read(block.getPerson().getId());
            vm.setPersonId(person.getId());
            vm.setPersonName(person.getName());
        }
        return vm;
    }

    @Override
    public Block deleteBlock(Integer id) {

        // Instantiate
        Block block = blockService.read(id);

        // Delete
        blockService.delete(block);

        return block;
    }
    
    @Override
    public Block moveBlockUp(Integer id) {

        // Instantiate
        Block block = blockService.read(id);

        // Delete
        blockService.moveUp(block);

        return block;
    }
    
    @Override
    public Block moveBlockDown(Integer id) {

        // Instantiate
        Block block = blockService.read(id);

        // Delete
        blockService.moveDown(block);

        return block;
    }

    @Override
    public Block moveBlockTo(Integer id, int newOrder) {
        Block block = blockService.read(id);
        blockService.moveTo(block, newOrder);
        return block;
    }
    
    // Translate create person/scene
    private List<CreatePersonViewModel> translateCreatePersonViewModel(List<Person> persons) {

        List<CreatePersonViewModel> createPersonViewModels = new ArrayList<>();

        for (Person person : persons) {
            CreatePersonViewModel createPersonViewModel = new CreatePersonViewModel();
            createPersonViewModel.setId(person.getId());
            createPersonViewModel.setName(person.getName());
            createPersonViewModels.add(createPersonViewModel);
        }

        return createPersonViewModels;
    }

    private static final Pattern CHARACTER_NAME_PATTERN = Pattern.compile("^([A-Z]{2,}(?:\\s+[A-Z]{2,})*)(?:\\s*\\n|\\s+|$)");

    private String extractCharacterName(String content) {
        if (content == null) return null;
        Matcher matcher = CHARACTER_NAME_PATTERN.matcher(content.trim());
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private String stripCharacterName(String content) {
        if (content == null) return content;
        Matcher matcher = CHARACTER_NAME_PATTERN.matcher(content.trim());
        if (matcher.find()) {
            return content.trim().substring(matcher.end()).trim();
        }
        return content;
    }

    private Person findOrCreatePerson(String characterName, Project project) {
        List<Person> persons = personService.getPersonsByProject(project);
        for (Person person : persons) {
            if (person.getName().equalsIgnoreCase(characterName)) {
                return person;
            }
        }
        Person newPerson = new Person();
        newPerson.setName(characterName);
        newPerson.setFullName(characterName);
        newPerson.setProject(project);
        return personService.create(newPerson);
    }

    // Translate edit person/scene
    private List<EditPersonViewModel> translateEditPersonViewModel(List<Person> persons) {

        List<EditPersonViewModel> editPersonViewModels = new ArrayList<>();

        for (Person person : persons) {
            EditPersonViewModel editPersonViewModel = new EditPersonViewModel();
            editPersonViewModel.setId(person.getId());
            editPersonViewModel.setName(person.getName());
            editPersonViewModels.add(editPersonViewModel);
        }

        return editPersonViewModels;
    }
    
}
