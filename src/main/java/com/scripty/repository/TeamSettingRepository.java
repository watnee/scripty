package com.scripty.repository;

import com.scripty.dto.TeamSetting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamSettingRepository extends JpaRepository<TeamSetting, Integer> {
}
