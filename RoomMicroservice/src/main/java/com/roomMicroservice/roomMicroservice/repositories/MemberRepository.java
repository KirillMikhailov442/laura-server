package com.roomMicroservice.roomMicroservice.repositories;

import com.roomMicroservice.roomMicroservice.modes.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
