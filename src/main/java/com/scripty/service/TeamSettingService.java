package com.scripty.service;

import com.scripty.dto.TeamSetting;

public interface TeamSettingService {

    TeamSetting getTeamSetting();

    String getTeamName();

    TeamSetting saveTeamName(String teamName);
}
