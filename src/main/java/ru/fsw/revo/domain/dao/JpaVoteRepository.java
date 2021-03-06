package ru.fsw.revo.domain.dao;


import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.fsw.revo.domain.model.User;
import ru.fsw.revo.domain.model.Vote;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;

import static ru.fsw.revo.utils.DateTimeUtil.*;

@Repository
@Transactional(readOnly = true)
public class JpaVoteRepository implements VoteRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public Vote save(Vote vote, long userId) {
        vote.setUser(em.find(User.class, userId));
        if (vote.isNew()) {
            vote.setDate(new Date());
            em.persist(vote);
            return vote;
        } else if (vote.getUser().getId() == null) {
            return null;
        }
        restrictionTimeCheck();
        return em.merge(vote);
    }

    @Override
    @Transactional
            (
                    propagation = Propagation.REQUIRED,
                    readOnly = false,
                    rollbackFor = Throwable.class
            )
    public boolean delete(long id, long userId) {
        restrictionTimeCheck();
        return em.createNamedQuery(Vote.DELETE)
                .setParameter("id", id)
                .setParameter("userId", userId)
                .executeUpdate() != 0;
    }

    @Override
    public Vote get(long id, long userId) {
        Query query = em.createNamedQuery(Vote.GET)
                .setParameter("id", id)
                .setParameter("userId", userId);
        Vote vote = (Vote) DataAccessUtils.singleResult(query.getResultList());
        return vote;
    }

    @Override
    public List<Vote> getAll(long userId) {
        return em.createNamedQuery(Vote.ALL_SORTED, Vote.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public List<Vote> getAllForRestaurant(long rId) {
        return em.createNamedQuery(Vote.ALL_FOR_REST, Vote.class)
                .setParameter("rId", rId)
                .getResultList();
    }
}
