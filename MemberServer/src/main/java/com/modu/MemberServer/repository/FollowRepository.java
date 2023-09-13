package com.modu.MemberServer.repository;

import com.modu.MemberServer.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {

}
