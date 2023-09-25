package com.modu.GatewayServer;

import com.modu.GatewayServer.dto.MemberInfoViewDto;
import com.modu.GatewayServer.dto.MessageDto;
import com.modu.GatewayServer.dto.PostDto;
import com.modu.GatewayServer.dto.UpdateMemberDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.message.Message;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class HomeController {

    private final RestTemplate restTemplate;

    @GetMapping("/authentication")
    public ResponseEntity<MessageDto> authentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken) {
            // JWT 토큰으로 인증된 사용자
            return new ResponseEntity<>(new MessageDto("1"), HttpStatus.OK);
        } else {
            // 익명 사용자
            return new ResponseEntity<>(new MessageDto("0"), HttpStatus.OK);
        }
    }

    @GetMapping("/free")
    public ResponseEntity<MessageDto> getFree() {
        log.info("GET /free 컨트롤러 진입");
        return new ResponseEntity<>(new MessageDto("익명 사용자도 얻을 수 있는 자원"), HttpStatus.OK);
    }

    @GetMapping("/posts")
    public ResponseEntity<List<PostDto>> getAllPosts() {
        log.info("GET /posts 컨트롤러 진입");
        String uriString = UriComponentsBuilder
                .newInstance()
                .scheme("http")
                .host("127.0.0.1")
                .port(8085)
                .path("/posts")
                .build().toUriString();

        HttpEntity<String> entity = new HttpEntity<>(new HttpHeaders());
        ResponseEntity<List<PostDto>> response = restTemplate.exchange(uriString, HttpMethod.GET, entity, new ParameterizedTypeReference<>() {});

        List<PostDto> postDtoList = response.getBody();
        postDtoList.sort((post1, post2) ->
                post2.getUpdatedAt().compareTo(post1.getUpdatedAt()));
        return new ResponseEntity<>(postDtoList, HttpStatus.OK);
    }

    @GetMapping("/members/info")
    public ResponseEntity<MemberInfoViewDto> memberInfo(@AuthenticationPrincipal Jwt jwt) {

        int memberId = Integer.parseInt(jwt.getClaim("sub"));

        String uriString = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("127.0.0.1")
                .port(8083)
                .path("/members/info/" + memberId)
                .build().toUriString();

        HttpEntity<String> entity = new HttpEntity<>(new HttpHeaders());

        return restTemplate.exchange(uriString, HttpMethod.GET, entity, new ParameterizedTypeReference<>() {});
    }

    @PutMapping("/members/info")
    public ResponseEntity<MessageDto> updateMemberInfo(@AuthenticationPrincipal Jwt jwt,
                                                       HttpServletRequest request,
                                                       @RequestParam String email,
                                                       @RequestParam String nickname,
                                                       @RequestParam String address,
                                                       @RequestParam String introduceMyself,
                                                       @RequestParam String socialType) {
        int memberId = Integer.parseInt(jwt.getClaim("sub"));

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("email", List.of(email));
        if (request.getParameterMap().containsKey("password") && request.getParameterMap().containsKey("repeatPassword")) {
            map.put("password", List.of(request.getParameter("password")));
            map.put("repeatPassword", List.of(request.getParameter("repeatPassword")));
        }
        map.put("nickname", List.of(nickname));
        map.put("address", List.of(address));
        map.put("introduceMyself", List.of(introduceMyself));
        map.put("socialType", List.of(socialType));

        String uriString = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("127.0.0.1")
                .port(8083)
                .path("/members/info/" + memberId)
                .queryParams(map)
                .build().toUriString();

        HttpEntity<String> entity = new HttpEntity<>(new HttpHeaders());

        ResponseEntity<MessageDto> messageResponse = restTemplate.exchange(uriString, HttpMethod.PUT, entity, new ParameterizedTypeReference<>() {});

        return new ResponseEntity<>(new MessageDto(messageResponse.getBody().getMessage()), HttpStatus.OK);
    }
}
