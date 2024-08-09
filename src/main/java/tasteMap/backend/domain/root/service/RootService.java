package tasteMap.backend.domain.root.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tasteMap.backend.domain.course.entity.Course;
import tasteMap.backend.domain.root.dto.RootDTO;
import tasteMap.backend.domain.root.entity.Root;
import tasteMap.backend.domain.root.repository.RootRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RootService {
    private final RootRepository rootRepository;

    @Transactional
    public void save(List<RootDTO> roots, Course course){
        for(RootDTO rootDto : roots){
            Root root = Root.builder()
                .title(rootDto.getTitle())
                .content(rootDto.getContent())
                .address(rootDto.getAddress())
                .course(course)
                .build();
            rootRepository.save(root);
        }
    }

}
