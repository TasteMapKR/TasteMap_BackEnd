package tasteMap.backend.CourseService;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tasteMap.backend.domain.course.dto.request.CourseDTO;
import tasteMap.backend.domain.course.entity.Course;
import tasteMap.backend.domain.course.entity.Enum.Category;
import tasteMap.backend.domain.course.repository.CourseRepository;
import tasteMap.backend.domain.course.service.CourseService;
import tasteMap.backend.domain.member.entity.Member;
import tasteMap.backend.domain.member.repository.MemberRepository;
import tasteMap.backend.global.exception.AppException;
import tasteMap.backend.global.exception.errorCode.CourseErrorCode;
import tasteMap.backend.global.exception.errorCode.MemberErrorCode;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.*;

@Transactional
public class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private CourseService courseService;

    private Member member;
    private Course course;
    private CourseDTO courseDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        member = Member.builder()
            .id(1L)
            .username("user1")
            .name("User One")
            .profile_image("image.png")
            .build();

        course = Course.builder()
            .id(1L)
            .title("Sample Course")
            .content("Course Content")
            .category(Category.MIXED)
            .member(member)
            .build();

        courseDTO = new CourseDTO("Updated Course", "Updated Content", "MIXED");
    }

    @Test
    void testSaveCourse() {
        Course savedCourse = Course.builder()
            .id(1L)
            .title("Updated Course")
            .content("Updated Content")
            .category(Category.MIXED)
            .member(member)
            .build();

        when(memberRepository.findByUsername("user1")).thenReturn(Optional.ofNullable(member));
        when(courseRepository.save(any(Course.class))).thenReturn(savedCourse);

        Course result = courseService.save(courseDTO, "user1");

        assertNotNull(result);
        assertEquals("Updated Course", result.getTitle());
        assertEquals("Updated Content", result.getContent());
        assertEquals(Category.MIXED, result.getCategory());
        verify(memberRepository).findByUsername("user1");
        verify(courseRepository).save(any(Course.class));
    }

    @Test
    void testDeleteCourse() {
        when(courseRepository.findById(1L)).thenReturn(java.util.Optional.of(course));
        when(memberRepository.findByUsername("user1")).thenReturn(Optional.ofNullable(member));

        courseService.delete(1L, "user1");

        verify(courseRepository).deleteById(1L);
    }

    @Test
    void testDeleteCourseWithInvalidMember() {
        when(courseRepository.findById(1L)).thenReturn(java.util.Optional.of(course));
        when(memberRepository.findByUsername("user1")).thenReturn(null);

        AppException thrown = assertThrows(AppException.class, () -> {
            courseService.delete(1L, "user1");
        });

        assertEquals(MemberErrorCode.MEMBER_NOT_FOUND, thrown.getErrorCode());
    }

    @Test
    void testDeleteCourseUnauthorized() {
        Member otherMember = Member.builder().id(2L).username("user2").build();
        when(courseRepository.findById(1L)).thenReturn(java.util.Optional.of(course));
        when(memberRepository.findByUsername("user2")).thenReturn(Optional.ofNullable(otherMember));

        AppException thrown = assertThrows(AppException.class, () -> {
            courseService.delete(1L, "user2");
        });

        assertEquals(CourseErrorCode.UNAUTHORIZED_ACCESS, thrown.getErrorCode());
    }

    @Test
    void testUpdateCourse() {
        Course updatedCourse = Course.builder()
            .id(1L)
            .title("Updated Course")
            .content("Updated Content")
            .category(Category.MIXED)
            .member(member)
            .build();

        when(courseRepository.findById(1L)).thenReturn(java.util.Optional.of(course));
        when(memberRepository.findByUsername("user1")).thenReturn(Optional.ofNullable(member));
        when(courseRepository.save(any(Course.class))).thenReturn(updatedCourse);

        Course result = courseService.update(1L, courseDTO, "user1");

        assertNotNull(result);
        assertEquals("Updated Course", result.getTitle());
        assertEquals("Updated Content", result.getContent());
        assertEquals(Category.MIXED, result.getCategory());
        verify(courseRepository).save(any(Course.class));
    }

    @Test
    void testUpdateCourseWithInvalidMember() {
        when(courseRepository.findById(1L)).thenReturn(java.util.Optional.of(course));
        when(memberRepository.findByUsername("user1")).thenReturn(null);

        AppException thrown = assertThrows(AppException.class, () -> {
            courseService.update(1L, courseDTO, "user1");
        });

        assertEquals(MemberErrorCode.MEMBER_NOT_FOUND, thrown.getErrorCode());
    }

    @Test
    void testUpdateCourseUnauthorized() {
        Member otherMember = Member.builder().id(2L).username("user2").build();
        when(courseRepository.findById(1L)).thenReturn(java.util.Optional.of(course));
        when(memberRepository.findByUsername("user2")).thenReturn(Optional.ofNullable(otherMember));

        AppException thrown = assertThrows(AppException.class, () -> {
            courseService.update(1L, courseDTO, "user2");
        });

        assertEquals(CourseErrorCode.UNAUTHORIZED_ACCESS, thrown.getErrorCode());
    }
}