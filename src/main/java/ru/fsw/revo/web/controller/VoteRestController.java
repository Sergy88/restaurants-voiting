package ru.fsw.revo.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.fsw.revo.domain.model.Restaurant;
import ru.fsw.revo.domain.model.User;
import ru.fsw.revo.domain.model.Vote;
import ru.fsw.revo.service.VoteService;
import ru.fsw.revo.utils.SecurityUtil;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = VoteRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class VoteRestController {
    public final static String REST_URL = "/rest/votes";

    @Autowired
    private VoteService service;

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody Vote vote, @PathVariable long id) {
        long userId = SecurityUtil.authUserId();
        service.update(vote, id , userId);
    }

    @PostMapping("/{restaurantId}/{rank}")
    public Vote create(@PathVariable long restaurantId, @PathVariable int rank) {
        long userId = SecurityUtil.authUserId();
        Vote vote = new Vote(rank, new Date());
        vote.setRestaurant(new Restaurant(restaurantId));
        Vote returned = service.create(vote, userId);
        //cleanup response
        returned.setUser(null);
        returned.getRestaurant().setMenu(null);
        return returned;
    }

    @GetMapping("/{id}")
    public Vote get(@PathVariable long id) {
        long userId = SecurityUtil.authUserId();
        return service.get(id, userId);
    }


    @GetMapping
    public List<Vote> getAll() {
        long userId = SecurityUtil.authUserId();
        return service.getAll(userId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        long userId = SecurityUtil.authUserId();
        service.delete(id, userId);
    }
}
