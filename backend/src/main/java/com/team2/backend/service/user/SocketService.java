package com.team2.backend.service.user;

import com.team2.backend.domain.reservation.*;
import com.team2.backend.domain.resource.CategoryRepository;
import com.team2.backend.domain.resource.ResourceRepository;
import com.team2.backend.web.dto.SocketMessage;
import com.team2.backend.web.dto.user.UserReservationDto;
import lombok.RequiredArgsConstructor;
import org.apache.http.client.utils.DateUtils;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RequiredArgsConstructor
@Service
public class SocketService {
    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat fullFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ReservationRepository reservationRepository;
    private final ReservationCheckRepository reservationCheckRepository;
    private final TimelistRepository timelistRepository;
    private final ResourceRepository resourceRepository;

    @Transactional
    public SocketMessage getTimelist(SocketMessage message) throws ParseException {
        HashMap<String, String> data = (HashMap<String, String>) message.getData();
        Long resourceNo = Long.parseLong(data.get("resourceNo"));
        Date startDate = formatter.parse(data.get("startTime"));
        Date endDate = formatter.parse(data.get("endTime"));

        List<Date> dateList = new ArrayList<>();
        for (Date i = startDate; i.before(endDate) || i.equals(endDate); i = new Date(i.getTime() + (1000 * 60 * 60 * 24))) {
            dateList.add(i);
        }

        HashMap<String, Long[]> timelists = new HashMap<>();
        for (int i = 0; i < dateList.size(); i++) {
            List<ReservationCheck> check = reservationCheckRepository.findByResourceNoAndCheckDate(resourceNo, formatter.format(dateList.get(i)));
            if (check.isEmpty()) {
                continue;
            }
            else {
                for (int j = 0; j < check.size(); j++) {
                    Long[] timelist =  timelistRepository.findAllByCheckNo(check.get(j).getCheckNo());
                    timelists.put(formatter.format(dateList.get(i)), timelist);
                }
            }
        }

        if (timelists.isEmpty()) {
            message.setResCode(4000);
            message.setMessage("[SUCCESS] 빈 예약 리스트");
        }
        else {
            message.setData(timelists); // 예약된 시간 리스트 보냄 - 여기 있는 시간들만 disable 시키면 됨
            message.setResCode(4000);
            message.setMessage("[SUCCESS] 예약 리스트 전송");
        }

        simpMessagingTemplate.convertAndSendToUser(message.getSenderName(),"/do", message);

        return message;
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public SocketMessage checkReservation(SocketMessage message) throws ParseException {
        HashMap<String, String> data = (HashMap<String, String>) message.getData();
        Long userNo = Long.parseLong(data.get("userNo"));
        Long resourceNo = Long.parseLong(data.get("resourceNo"));
        String startDate = data.get("startTime").split(" ")[0];
        String endDate = data.get("endTime").split(" ")[0];
        Date from = formatter.parse(startDate);
        Date to = formatter.parse(endDate);
        Integer startTime = timeParser(data.get("startTime").split(" ")[1]);
        Integer endTime = timeParser(data.get("endTime").split(" ")[1]);
        Long cateNo = resourceRepository.findCateNoByResourceNo(resourceNo);

        List<Date> dateList = new ArrayList<>();
        for (Date i = from; i.before(to) || i.equals(to); i = new Date(i.getTime() + (1000 * 60 * 60 * 24))) {
            dateList.add(i);
        }

        switch (isAvailable(resourceNo, startDate, startTime, endDate, endTime)) {
            case "OK":
                System.out.println("OK");
                Reservation newReserv = reservationRepository.save(
                        Reservation.builder()
                                .resourceNo(resourceNo)
                                .userNo(userNo)
                                .startTime(fullFormatter.parse(data.get("startTime")))
                                .endTime(fullFormatter.parse(data.get("endTime")))
                                .build()
                );
                for (int i = 0; i < dateList.size(); i++) {
                    ReservationCheck newCheck = reservationCheckRepository.save(
                            ReservationCheck.builder()
                                    .resourceNo(resourceNo)
                                    .checkDate(formatter.format(dateList.get(i)))
                                    .cateNo(cateNo)
                                    .reservNo(newReserv.getReservNo())
                                    .build()
                    );
                    if (dateList.size() > 1) {
                        if (i == 0) {
                            saveTimelist(newCheck.getCheckNo(), startTime, 48);
                        }
                        else if (i == dateList.size() - 1) {
                            saveTimelist(newCheck.getCheckNo(), 0, endTime);
                        }
                        else {
                            saveTimelist(newCheck.getCheckNo(), 0, 48);
                        }
                    }
                    else  {
                        saveTimelist(newCheck.getCheckNo(), startTime, endTime);
                    }
                }
                message.setResCode(4000);
                message.setMessage("[SUCCESS] 시간 저장 완료");
                break;
//            case "OK":
//                System.out.println("OK");
//                Reservation okReserv = reservationRepository.save(
//                        Reservation.builder()
//                                .resourceNo(resourceNo)
//                                .userNo(userNo)
//                                .startTime(fullFormatter.parse(data.get("startTime")))
//                                .endTime(fullFormatter.parse(data.get("endTime")))
//                                .build()
//                );
//
//                for (int i = 0; i < dateList.size(); i++) {
//                    ReservationCheck findCheck = reservationCheckRepository
//                            .findByReservNoAndCheckDate(okReserv.getReservNo(), formatter.format(dateList.get(i)));
//                    if (dateList.size() > 1) {
//                        if (i == 0) {
//                            saveTimelist(findCheck.getCheckNo(), startTime, 48);
//                        }
//                        else if (i == dateList.size() - 1) {
//                            saveTimelist(findCheck.getCheckNo(), 0, endTime);
//                        }
//                        else {
//                            saveTimelist(findCheck.getCheckNo(), 0, 48);
//                        }
//                    }
//                    else  {
//                        saveTimelist(findCheck.getCheckNo(), startTime, endTime);
//                    }
//                }
//                message.setResCode(4000);
//                message.setMessage("[SUCCESS] 시간 저장 완료");
//                break;
            case "FAIL":
                // 시간 리스트 다시 보내줄까?
                System.out.println("FAIL");
                message.setResCode(4001);
                message.setMessage("[FAIL] 시간 중복");
                break;
            default:
                System.out.println("ERROR");
                message.setResCode(4004);
                message.setMessage("[ERROR] 알 수 없는 오류");
                throw new RuntimeException();
        }
        simpMessagingTemplate.convertAndSendToUser(message.getSenderName(),"/do", message);
        return message;
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public String isAvailable(Long resourceNo, String startDate , Integer startTime, String endDate, Integer endTime) throws ParseException {

        List<ReservationCheck> checklist = null;

        // 당일 예약이라면
        if (startDate.equals(endDate)) {
            checklist = reservationCheckRepository
                    .findAllByResourceNoAndCheckDate(resourceNo, startDate);
        }
        else { // 여러 날짜 예약이라면
            checklist = reservationCheckRepository
                    .findAllByResourceNoAndCheckDateBetween(resourceNo, startDate, endDate);
        }

        if (checklist.isEmpty()) {
            return "OK";
        }
        else {
            for (int i = 0; i < checklist.size(); i++) {
                ReservationCheck check = checklist.get(i);
                List<Timelist> timelist = check.getTimelist();
                for (int j = 0; j < timelist.size(); j++) {
                    Long timeNo = timelist.get(j).getTimeNo();
                    if (timeNo >= startTime && timeNo < endTime) {
                        return "FAIL";
                    }
                }
            }
            return "OK";
        }

    }


    //        3. 예약 완료 -> 예약 완료 테이블에 넣고 현재 예약중에서 state 예약 완료로 변경
//        4. 소켓 끊음
    @Transactional
    public SocketMessage fixReservation(SocketMessage message) throws ParseException {
        HashMap<String, String> data = (HashMap<String, String>) message.getData();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Long resourceNo = Long.parseLong(data.get("resourceNo"));
        Date startTime = dateFormatter.parse(data.get("startTime"));
        Date endTime = dateFormatter.parse(data.get("endTime"));
        String reservName = data.get("reservName");
//        Long userNo = Long.parseLong((String) req.getAttribute("userNo"));

        System.out.println(resourceNo);
        System.out.println(startTime);
        System.out.println(endTime);
        System.out.println(reservName);
//        String peopleCnt = data.get("peopleCnt"); // 보류

//        Reservation reservation = reservationRepository.save(
//            Reservation.builder()
//                .resourceNo(resourceNo)
//                .userNo(userNo)
//                .reservName(reservName)
//                .startTime(startTime)
//                .endTime(endTime)
//                .build()
//        );
        // 현재 예약중 테이블 state 예약 완료로 변경

        // 예약 테이블에 save
        // 예약 성공 메세지 전송 - 클라이언트는 예약 완료 페이지로 이동
        // 예약 실패 메세지 전송(db에러?) - 클라이언트는 알수없는 오류 모달? - 초기화면으로 이동
        return null;
    }

    public Integer timeParser(String time) {
        Integer hours = Integer.parseInt(time.split(":")[0]) * 2;
        Integer minutes = Integer.parseInt(time.split(":")[1]) == 0 ? 0 : 1;
        return hours + minutes;
    }

    @Transactional
    public void saveTimelist(Long checkNo, Integer startTime, Integer endTime) {
        for (Integer j = startTime; j < endTime; j++) {
            timelistRepository.save(
                    Timelist.builder()
                            .checkNo(checkNo)
                            .timeNo(new Long(j))
                            .build()
            );
        }
    }

//    @Transactional
//    public Reservation saveReservation(Long ) {
//        return reservationRepository.save(
//                Reservation.builder()
//                        .resourceNo(resourceNo)
//                        .userNo(userNo)
//                        .startTime(fullFormatter.parse(data.get("startTime")))
//                        .endTime(fullFormatter.parse(data.get("endTime")))
//                        .build()
//        );
//    }

}
