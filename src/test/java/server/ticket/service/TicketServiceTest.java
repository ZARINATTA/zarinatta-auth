package server.ticket.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import server.entity.Ticket;
import server.enums.StationCode;
import server.ticket.controller.dto.request.TicketSearchRequest;
import server.ticket.controller.dto.response.PageTicketResponse;
import server.ticket.repository.TicketRepository;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assert.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Profile("test")
class TicketServiceTest {

    @Autowired
    private TicketService ticketService;
    @Autowired
    private TicketRepository ticketRepository;

    @Test
    @DisplayName("출발/도착역, 시간, 기차 종류을 통해 열차 티켓을 검색한다.")
    void getTicket() {
        //given
        createDummyTicket();
        TicketSearchRequest ticketSearchRequest = TicketSearchRequest.builder()
                .departStation(StationCode.부산)
                .arriveStation(StationCode.수원)
                .departDate("20240807")
                .departTime("070000")
                .trainType("새마을호")
                .build();
        Pageable pageable = PageRequest.of(0, 2);
        //when
        PageTicketResponse result = ticketService.getTicket(ticketSearchRequest, pageable);

        //then
        assertThat(result.getPage()).isEqualTo(1);
        assertThat(result.getTotalDataCount()).isEqualTo(4); // 페이지당 2개의 항목이 존재해야 합니다.
        assertThat(result.getTotalPageCount()).isEqualTo(2);
        assertThat(result.getResponseList().get(0).getTicketType()).isEqualTo("새마을호");
        assertThat(result.getResponseList().get(0).getDepartStation()).isEqualTo(StationCode.부산);
        assertThat(result.getResponseList().get(0).getArriveStation()).isEqualTo(StationCode.수원);
    }

    @Transactional
    void createDummyTicket() {
        List<Ticket> tickets = Arrays.asList(
                new Ticket("새마을호 1001", "20240807", "060000", StationCode.부산, "100000", StationCode.수원, "25000원"),
                new Ticket("KTX 101", "20240807", "070000", StationCode.부산, "100000", StationCode.수원, "48000원"),
                new Ticket("ITX-새마을 2010", "20240807", "080000", StationCode.부산, "110000", StationCode.수원, "30000원"),
                new Ticket("새마을호 1002", "20240807", "090000", StationCode.부산, "130000", StationCode.수원, "25000원"),
                new Ticket("KTX 102", "20240807", "100000", StationCode.부산, "130000", StationCode.수원, "48000원"),
                new Ticket("ITX-새마을 2011", "20240807", "110000", StationCode.부산, "140000", StationCode.수원, "30000원"),
                new Ticket("새마을호 1003", "20240807", "120000", StationCode.부산, "160000", StationCode.수원, "25000원"),
                new Ticket("KTX 103", "20240807", "130000", StationCode.부산, "160000", StationCode.수원, "48000원"),
                new Ticket("ITX-새마을 2012", "20240807", "140000", StationCode.부산, "170000", StationCode.수원, "30000원"),
                new Ticket("새마을호 1004", "20240807", "150000", StationCode.부산, "190000", StationCode.수원, "25000원")
        );
        ticketRepository.saveAll(tickets);
    }
}