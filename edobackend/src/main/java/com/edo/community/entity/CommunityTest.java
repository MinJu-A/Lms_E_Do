package com.edo.community.entity;

import com.edo.community.dto.CommunityTestDto;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "community_Test")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CommunityTest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "community_id")
    private Long communityId;

    //    제목
    @Column
    private String communityTitle;

    //    내용
    @Column
    private String communityContent;


//    id, 제목, 내용을 받아서 게시글을 등록하는 메소드
    public void updateContents(Long communityId, String communityTitle, String communityContent){
        this.communityId = communityId;
        this.communityTitle = communityTitle;
        this.communityContent = communityContent;
    }


//    communityTest 객체를 communityDto로 변환해주는 메소드(기본)
    public CommunityTestDto toDto(){
        CommunityTestDto communityTestDto = new CommunityTestDto();
        communityTestDto.setCommunityId(this.communityId);
        communityTestDto.setCommunityTitle( this.communityTitle);
        communityTestDto.setCommunityContents(this.communityContent);
        return communityTestDto;
    }

    
//    communitymain에서 list로 받아오기 위해 communityDto로 변환해주는 메소드
    public CommunityTestDto toDtoNoContents(){
        CommunityTestDto communityTestDto = new CommunityTestDto();
        communityTestDto.setCommunityId(this.communityId);
        communityTestDto.setCommunityTitle(this.communityTitle);
        return communityTestDto;
    }


}
