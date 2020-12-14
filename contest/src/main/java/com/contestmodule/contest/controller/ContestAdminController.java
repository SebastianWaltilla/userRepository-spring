package com.contestmodule.contest.controller;

import com.contestmodule.contest.entity.Contest;
import com.contestmodule.contest.service.ContestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/admin/contest")
@Validated
@RolesAllowed("ADMIN")
public class ContestAdminController {

    Logger logger = LoggerFactory.getLogger(ContestService.class);

    private ContestService contestService;

    public ContestAdminController(ContestService contestService) {
        this.contestService = contestService;
    }

    @GetMapping("/find-all")
    public Iterable<Contest> findAllContests() {
        return contestService.findAllContests();
    }

    @PostMapping("/create")
    public Contest createContest(@RequestBody @Valid Contest contest) {
        logger.info("createContest() was called with contestname: " + contest.getName());
        return contestService.createContest(contest);
    }

    @PatchMapping("/update-contest/{id}")
    public ResponseEntity<Contest> updateContest(@Valid @PathVariable("id") Long id, @RequestBody Contest updateContest) {

        Optional<Contest> contestOptional = contestService.findContestByID(id);
        if (!contestOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Contest contest = contestOptional.get();
        if (updateContest.getName() != null) {
            contest.setName(updateContest.getName());
        }
        if (updateContest.getDescription() != null) {
            contest.setDescription(updateContest.getDescription());
        }
        if (updateContest.getMaxParticipants() >= 0) {
            contest.setMaxParticipants(updateContest.getMaxParticipants());
        }
        if (updateContest.getStartDate() != null) {
            contest.setStartDate(updateContest.getStartDate());
        }
        if (updateContest.getEndDate() != null) {
            contest.setEndDate(updateContest.getEndDate());
        }
        if (updateContest.getEntryFee() != null) {
            contest.setEntryFee(updateContest.getEntryFee());
        }
        if (updateContest.getContestLevel() != null) {
            contest.setContestLevel(updateContest.getContestLevel());
        }
        if (updateContest.getWinningAward() != null) {
            contest.setWinningAward(updateContest.getWinningAward());
        }
        if (updateContest.getAdminComment() != null) {
            contest.setAdminComment(updateContest.getAdminComment());
        }

        contestService.createContest(contest);
        logger.info("createContest() was called through update-contest with contestId: " + contest.getId());
        logger.info("Contest" + contest.getName() + "was updated");
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteContest(@PathVariable Long id) {
        if (contestService.findContestByID(id).isPresent()) {
            contestService.deleteContest(id);
            return new ResponseEntity(id + " was deleted", HttpStatus.OK);
        } return ResponseEntity.notFound().build();
    }
}

