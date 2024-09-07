package tasteMap.backend.CourseService;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import tasteMap.backend.domain.course.dto.response.CourseMainPageDTO;
import tasteMap.backend.domain.course.dto.response.CourseMyDTO;
import tasteMap.backend.domain.course.entity.Course;
import tasteMap.backend.domain.course.entity.Enum.Category;
import tasteMap.backend.domain.course.service.CourseApiService;
import tasteMap.backend.domain.member.entity.Enum.Role;
import tasteMap.backend.domain.member.entity.Member;
import tasteMap.backend.domain.member.repository.MemberRepository;
import tasteMap.backend.domain.course.repository.CourseRepository;


import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class CourseApiServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private CourseApiService courseApiService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCoursesByMemberId() {
        Member member = new Member(1L, "testuser", "John Doe", "john@example.com", Role.ROLE_USER, "profile.jpg");
        Course course = new Course(1L, "Sample Course", "Course Content", Category.DESSERT, member);
        CourseMyDTO courseMyDTO = new CourseMyDTO(course.getId(), course.getTitle());
        Page<CourseMyDTO> page = new PageImpl<>(Collections.singletonList(courseMyDTO));
        Pageable pageable = PageRequest.of(0, 10);

        when(memberRepository.findByUsername(anyString())).thenReturn(member);
        when(courseRepository.findCoursesByMemberId(anyLong(), any(Pageable.class))).thenReturn(page);

        Page<CourseMyDTO> result = courseApiService.getCoursesByMemberId("testuser", pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("Sample Course", result.getContent().get(0).getTitle());
    }

    @Test
    void testGetCoursesByCategory() {
        Member member = new Member(1L, "testuser", "John Doe", "john@example.com", Role.ROLE_USER, "profile.jpg");
        Course course = new Course(1L, "Sample Course", "Course Content", Category.DESSERT, member);
        CourseMainPageDTO courseMainPageDTO = new CourseMainPageDTO(course.getId(), course.getTitle(), "DESERT", member.getName(), member.getProfile_image());
        Page<CourseMainPageDTO> page = new PageImpl<>(Collections.singletonList(courseMainPageDTO));
        Pageable pageable = PageRequest.of(0, 10);

        when(courseRepository.findCourseMainPageByCategory(anyString(), any(Pageable.class))).thenReturn(page);

        Page<CourseMainPageDTO> result = courseApiService.getCoursesByCategory("DESERT", pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("Sample Course", result.getContent().get(0).getTitle());
        assertEquals("DESERT", result.getContent().get(0).getCategory());
    }
}