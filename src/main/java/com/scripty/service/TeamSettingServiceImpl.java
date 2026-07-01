package com.scripty.service;

import com.scripty.dto.TeamSetting;
import com.scripty.repository.TeamSettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeamSettingServiceImpl implements TeamSettingService {

    private final TeamSettingRepository teamSettingRepository;

    @Autowired
    public TeamSettingServiceImpl(TeamSettingRepository teamSettingRepository) {
        this.teamSettingRepository = teamSettingRepository;
    }

    @Override
    public TeamSetting getTeamSetting() {
        return teamSettingRepository.findAll().stream().findFirst().orElse(null);
    }

    @Override
    public String getTeamName() {
        TeamSetting setting = getTeamSetting();
        return setting != null ? setting.getTeamName() : "My Team";
    }

    @Override
    public TeamSetting saveTeamName(String teamName) {
        TeamSetting setting = getTeamSetting();
        if (setting == null) {
            setting = new TeamSetting();
        }
        setting.setTeamName(teamName);
        return teamSettingRepository.save(setting);
    }
}
