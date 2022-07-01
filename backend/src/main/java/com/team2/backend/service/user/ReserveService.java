package com.team2.backend.service.user;

import com.team2.backend.config.security.auth.EmployeeDetails;
import com.team2.backend.domain.reservation.Reservation;
import com.team2.backend.domain.reservation.ReservationRepository;
import com.team2.backend.domain.resource.PeopleCnt;
import com.team2.backend.domain.resource.PeopleCntRepository;
import com.team2.backend.domain.resource.Resource;
import com.team2.backend.domain.resource.ResourceRepository;
import com.team2.backend.web.dto.JsonResponse;
import com.team2.backend.web.dto.Message;
import com.team2.backend.web.dto.user.UserReservationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ReserveService {

    private final ReservationRepository reservationRepository;
    private final PeopleCntRepository peopleCntRepository;
    private final ResourceRepository resourceRepository;

    @Transactional
    public ResponseEntity<Message> getResourceList() {
        List<Resource> resourceList = resourceRepository.findAll();
        if (resourceList.isEmpty()) {
            Message message = Message.builder()
                    .resCode(4001)
                    .message("[WARN] Empty Resource List")
                    .build();
            return new JsonResponse().send(400, message);
        }

        Message message = Message.builder()
                .resCode(4000)
                .message("[SUCCESS] Get Resource List")
                .data(resourceList)
                .build();
        return new JsonResponse().send(200, message);
    }

    @Transactional
    public ResponseEntity<Message> myReservationList(EmployeeDetails user) {
        List<Reservation> reservationList = reservationRepository.findAllByUserNo(user.getEmployee().getNo());
        Message message = Message.builder()
                .resCode(4000)
                .message("[SUCCESS] Get my reservations")
                .data(reservationList)
                .build();
        return new JsonResponse().send(200, message);
    }

    @Transactional
    public ResponseEntity<Message> cancelReservation(UserReservationDto body) {

        reservationRepository.delete(reservationRepository.findByReservNo(body.getReservNo()));

        Message message = Message.builder()
                .resCode(4000)
                .message("[SUCCESS] Cancel reservation")
                .build();
        return new JsonResponse().send(200, message);
    }
}
