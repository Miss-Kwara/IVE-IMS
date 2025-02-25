package com.rbnk.invenmanagement.repository;

import com.rbnk.invenmanagement.entity.Project;
import com.rbnk.invenmanagement.entity.ProjectDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectDocumentRepository extends JpaRepository<ProjectDocument, Long> {

    List<ProjectDocument> findByProject(Project project);

    Optional<ProjectDocument> findByFilepath(String filepath);
}

