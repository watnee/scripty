package com.scripty.repository;

import com.scripty.dto.Project;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProjectRepository extends JpaRepository<Project, Integer> {

    List<Project> findAllByOrderByTitleAsc();

    @Query("SELECT s.project FROM Scene s WHERE s.id = :sceneId")
    Project findBySceneId(Integer sceneId);

    @Query("SELECT DISTINCT p FROM Project p JOIN p.teams t JOIN t.members m WHERE m.username = :username ORDER BY p.title ASC")
    List<Project> findByTeamMemberUsername(String username);
}
