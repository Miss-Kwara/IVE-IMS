package com.rbnk.invenmanagement.repository;

import com.rbnk.invenmanagement.entity.Project;
import com.rbnk.invenmanagement.entity.ProjectMember;
import com.rbnk.invenmanagement.entity.ProjectMemberId;
import com.rbnk.invenmanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectMemberRepository extends JpaRepository<ProjectMember, ProjectMemberId> {

    List<ProjectMember> findByProject(Project project);

    List<ProjectMember> findByUser(User user);

    Optional<ProjectMember> findByProjectAndUser(Project project, User user);
}

