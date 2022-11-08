package com.edo.community.service;


import com.edo.community.dto.CommunityDto;
import com.edo.community.entity.Community;
import com.edo.community.entity.CommunityTest;
import com.edo.community.repository.CommunityRepository;
import com.edo.community.repository.CommunityTestRepository;
import com.edo.util.pagination.Paged;
import com.edo.util.pagination.Paging;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CommunityService {

    private final CommunityTestRepository communityTestRepository;

    private final CommunityRepository communityRepository;

    public Long saveContents(CommunityDto communityDto) throws Exception {

//        게시글 등록
        CommunityTest communityTest = communityDto.createContents();
        communityTestRepository.save(communityTest);

        return communityTest.getCommunityId();
    }

    public List<Community> getMainList() {
        List<Community> communityList = communityRepository.findAll();
        return communityList;
    }


    //    페이징 처리(main) -> 내가 이게 필요한가?
    public Paged<Community> getPage(int pageNumber, int size) {
        PageRequest request = PageRequest.of(pageNumber - 1, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Community> postPage = communityRepository.findAll(request);
        return new Paged<>(postPage, Paging.of(postPage.getTotalPages(), pageNumber, size));
    }

    //    파트 페이징 처리
    public Paged<Community> getPageByPart(int pageNumber, int size, String part) {
        PageRequest request = PageRequest.of(pageNumber - 1, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Community> postPage = communityRepository.findAll(request);
        return new Paged<>(postPage, Paging.of(postPage.getTotalPages(), pageNumber, size));
    }


//    게시글 세부 조회

    //	Mapper로 넘기기
//	Dto->community니까 생성자로 Dto 넘겨준다
    public Community createRealContents(CommunityDto communityDto) {
        ModelMapper modelMapper = new ModelMapper();
        Community community = modelMapper.map(communityDto, Community.class);
        return community;
    }


    //    게시글 조회수 올리기
    public Community updateHit(Long id) {
//        일단 커뮤니티에서 아이디로 가져옵니다
        Community community = communityRepository.findById(id).get();
        community.hitCount(community.getHit());
        return community;
    }


}
