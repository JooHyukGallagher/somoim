package me.weekbelt.runningflex.modules.enrollment.service;

import lombok.RequiredArgsConstructor;
import me.weekbelt.runningflex.modules.enrollment.Enrollment;
import me.weekbelt.runningflex.modules.enrollment.repository.EnrollmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;

    public Enrollment getEnrollmentById(Long enrollmentId) {
        return enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new IllegalArgumentException("찾는 참가자가 없습니다."));
    }
}
