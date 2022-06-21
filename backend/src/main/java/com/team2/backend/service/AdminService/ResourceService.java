package com.team2.backend.service.AdminService;

import com.team2.backend.domain.resource.Category;
import com.team2.backend.domain.resource.Resource;
import com.team2.backend.domain.resource.ResourceQuerydslRepository;
import com.team2.backend.domain.resource.ResourceRepository;
import com.team2.backend.web.dto.JsonResponse;
import com.team2.backend.web.dto.Message;
import com.team2.backend.web.dto.admin.ResourceAdminDto;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ResourceService {
    private final ResourceRepository resourceRepository;

    private final ResourceQuerydslRepository resourceQuerydslRepository;

    @Transactional
    public ResponseEntity<Message> getResourceList(){
//        List<Resource> resourceList = resourceRepository.findAllResource();
        List<ResourceAdminDto> reosourceList = resourceQuerydslRepository.getAllResourceList();
        System.out.println("resourceservice시작");
        if (reosourceList.isEmpty()) {
            Message message = Message.builder()
                    .resCode(3001)
                    .message("실패: 전체조회 실패")
                    .build();
            return new JsonResponse().send(400, message);
        }

        Message message = Message.builder()
                .resCode(3000)
                .message("성공: 전체조회 잘됨")
                .data(reosourceList)
                .build();
        return new JsonResponse().send(200, message);
    }
    @Transactional
    public ResponseEntity<Message> getEachList(Category category){
        List<Resource> Aeachlist = resourceRepository.findByCateNo(category.getCateNo());
        if (Aeachlist.isEmpty()) {
            Message message = Message.builder()
                    .resCode(3001)
                    .message("실패: 전체조회 실패")
                    .build();
            return new JsonResponse().send(400, message);
        }

        Message message = Message.builder()
                .resCode(3000)
                .message("성공: 전체조회 성공")
                .data(Aeachlist)
                .build();
        return new JsonResponse().send(200, message);
    }

    @Transactional
    public ResponseEntity<Message> resourceRegister(HttpServletRequest req, ResourceAdminDto resourceDto){
                if(resourceDto.getCateNo() == 1){
                    Resource office = resourceRepository.save(
                            Resource.builder()
                                    .cateNo(resourceDto.getCateNo())
                                    .location(resourceDto.getLocation())
                                    .availableTime(resourceDto.getAvailableTime())
                                    .resourceName(resourceDto.getResourceName())
                                    .able(resourceDto.getAble())
                                    .people(resourceDto.getPeople())
                                    .adminNo(resourceDto.getAdminNo())
                                    .option(resourceDto.getOption())
                                    .build()
                    );
                    Message message = Message.builder()
                            .resCode(3000)
                            .message("성공: 회의실 등록")
                            .data(office)
                            .build();
                    return new JsonResponse().send(200, message);

                }else if (resourceDto.getCateNo() == 2){
                    Resource car = resourceRepository.save(
                            Resource.builder()
                                    .cateNo(resourceDto.getCateNo())
                                    .location(resourceDto.getLocation())
                                    .availableTime(resourceDto.getAvailableTime())
                                    .resourceName(resourceDto.getResourceName())
                                    .able(resourceDto.getAble())
                                    .people(resourceDto.getPeople())
                                    .adminNo(resourceDto.getAdminNo())
                                    .option(resourceDto.getOption())
                                    .fuel(resourceDto.getFuel())
                                    .build()

                    );
                    Message message = Message.builder()
                            .resCode(3000)
                            .message("성공: 차량 등록")
                            .data(car)
                            .build();
                    return new JsonResponse().send(200, message);

                } else if (resourceDto.getCateNo() == 3) {
                    Resource laptop = resourceRepository.save(
                            Resource.builder()
                                    .resourceNo(resourceDto.getResourceNo())
                                    .cateNo(resourceDto.getCateNo())
                                    .location(resourceDto.getLocation())
                                    .resourceName(resourceDto.getResourceName())
                                    .availableTime(resourceDto.getAvailableTime())
                                    .able(resourceDto.getAble())
                                    .people(resourceDto.getPeople())
                                    .adminNo(resourceDto.getAdminNo())
                                    .option(resourceDto.getOption())
                                    .build()

                    );
                    Message message = Message.builder()
                            .resCode(3000)
                            .message("성공: 노트북 등록")
                            .data(laptop)
                            .build();
                    return new JsonResponse().send(200, message);

            }
            Message message = Message.builder()
                        .resCode(3001)
                        .message("실패: 자원 등록 실패")
                        .build();
            return new JsonResponse().send(400, message);
    }

//    @Transactional
//    public ResponseEntity<Message> resourceUpdate(HttpServletRequest req, Long resourceNo, Resource resource){
//        Resource updateresourse = resourceRepository.findByResourceNo(resourceNo);
//
//        // 존재한다면
//        if(updateresourse != null){
//            System.out.println("updateresource값 존재");
//            if(updateresourse.getCateNo() != null && updateresourse.getAdminNo() != null){
//                updateresourse.update(resource.getCateNo(), resource.getAble(), resource.getResourceName(),
//                        resource.getLocation(), resource.getPeople(), resource.getAvailableTime(),
//                        resource.getAdminNo(), resource.getOption(), resource.getFuel());
//
//                        Message message = Message.builder()
//                                .resCode(3000)
//                                .message("성공: 자원 수정 성공")
//                                .data(updateresourse)
//                                .build();
//                        return new JsonResponse().send(200, message);
//            }
//        }
//        System.out.println("updateresource값 존재안함");
//        Message message = Message.builder()
//                .resCode(3001)
//                .message("실패: 자원 수정 실패")
//                .build();
//        return new JsonResponse().send(400, message);
//    }

    @Transactional
    public ResponseEntity<Message> delresourceList(Long resourceNo){
        Resource delesourse = resourceRepository.findByResourceNo(resourceNo);

        if(delesourse != null){
            resourceRepository.deleteById(resourceNo);
            Message message = Message.builder()
                    .resCode(3000)
                    .message("성공: 해당 자원 삭제")
                    .build();
            return new JsonResponse().send(200, message);
        }
        Message message = Message.builder()
                .resCode(3001)
                .message("실패: 해당 자원 없음")
                .build();
        return new JsonResponse().send(400, message);
    }
}
