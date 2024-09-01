//package server.ticket.repository;
//
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.annotation.Profile;
//import org.springframework.transaction.annotation.Transactional;
//import server.entity.Ticket;
//import server.enums.StationCode;
//import server.ticket.controller.dto.request.TicketSearchRequest;
//
//import java.util.Arrays;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@Profile("test")
//class TicketRepositoryCustomImplTest {
//
//    @Autowired
//    private TicketRepository ticketRepository;
//
//    @Test
//    @DisplayName("카운트 쿼리 테스트")
//    public void 테스트이름(){
//        //given
//        createDummyTicket();
//        TicketSearchRequest ticketSearchRequest = TicketSearchRequest.builder()
//                .departStation(StationCode.부산)
//                .arriveStation(StationCode.수원)
//                .departDate("20240807")
//                .departTime("070000")
//                .trainType("새마을호")
//                .build();
//
//        //when
//        Long ticketCount = ticketRepository.countAll(ticketSearchRequest);
//
//        //then
//        assertThat(ticketCount).isEqualTo(4);
//    }
//
//    @Transactional
//    void createDummyTicket() {
//        List<Ticket> tickets = Arrays.asList(
//                new Ticket("새마을호 1001", "20240807", "060000", StationCode.부산, "100000", StationCode.수원, "25000원"),
//                new Ticket("KTX 101", "20240807", "070000", StationCode.부산, "100000", StationCode.수원, "48000원"),
//                new Ticket("ITX-새마을 2010", "20240807", "080000", StationCode.부산, "110000", StationCode.수원, "30000원"),
//                new Ticket("새마을호 1002", "20240807", "090000", StationCode.부산, "130000", StationCode.수원, "25000원"),
//                new Ticket("KTX 102", "20240807", "100000", StationCode.부산, "130000", StationCode.수원, "48000원"),
//                new Ticket("ITX-새마을 2011", "20240807", "110000", StationCode.부산, "140000", StationCode.수원, "30000원"),
//                new Ticket("새마을호 1003", "20240807", "120000", StationCode.부산, "160000", StationCode.수원, "25000원"),
//                new Ticket("KTX 103", "20240807", "130000", StationCode.부산, "160000", StationCode.수원, "48000원"),
//                new Ticket("ITX-새마을 2012", "20240807", "140000", StationCode.부산, "170000", StationCode.수원, "30000원"),
//                new Ticket("새마을호 1004", "20240807", "150000", StationCode.부산, "190000", StationCode.수원, "25000원")
//        );
//        ticketRepository.saveAll(tickets);
//    }
//}