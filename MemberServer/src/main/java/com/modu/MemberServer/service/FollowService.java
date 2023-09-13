package com.modu.MemberServer.service;

import com.modu.MemberServer.entity.Follow;
import com.modu.MemberServer.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FollowService {

    private final FollowRepository followRepository;

    public Long save(Follow follow) {
        Follow savedFollow = followRepository.save(follow);
        return savedFollow.getFollowId();
    }
}
