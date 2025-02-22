package com.rbnk.invenmanagement.repository;

import com.rbnk.invenmanagement.entity.Project;
import com.rbnk.invenmanagement.entity.ProjectMember;
import com.rbnk.invenmanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
    boolean existsByUserAndProject(User user, Project project);
    void deleteByUserAndProject(User user, Project project);
    void deleteByUser(User user);
}
