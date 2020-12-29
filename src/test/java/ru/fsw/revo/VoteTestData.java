package ru.fsw.revo;

import ru.fsw.revo.domain.model.Vote;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static java.time.LocalDateTime.of;
import static ru.fsw.revo.RestaurantsTestData.*;
import static ru.fsw.revo.domain.model.AbstractBaseEntity.START_SEQ;

public class VoteTestData {
    public static final TestMatcher<Vote> VOTE_MATCHER = TestMatcher.usingIgnoringFieldsComparator(Vote.class, "user", "restaurant.menu");
    public static final TestMatcher<Vote> VOTE_MATCHER_NO = TestMatcher.usingIgnoringFieldsComparator(Vote.class, "user", "restaurant");

    public static final long VOTE_NOT_FOUND_ID = 99999;
    public static final long VOTE_NOT_OWN_ID = 10028;
    public static final long VOTE1_ID = START_SEQ + 24;


    public static final Vote vote1 = new Vote(VOTE1_ID, 3, rest1, of(2020, Month.NOVEMBER, 12, 10, 0));
    public static final Vote vote2 = new Vote(VOTE1_ID+1, 6, rest2, of(2020, Month.NOVEMBER, 12, 10, 2));
    public static final Vote vote3 = new Vote(VOTE1_ID+2, 4, rest3, of(2020, Month.NOVEMBER, 12, 10, 3));
    public static final Vote vote4 = new Vote(VOTE1_ID+3, 10, rest4, of(2020, Month.NOVEMBER, 12, 10, 4));
     public static final List<Vote> all_votes = List.of(vote1, vote2, vote3, vote4);

    public static Vote getNew() {
        return new Vote(null, 4, rest1, of(2020, Month.SEPTEMBER, 20, 10, 0)) ;
    }

    public static Vote getUpdated() {
        return new Vote(VOTE1_ID, 10, rest1, of(2020, Month.SEPTEMBER, 20, 10, 0)) ;
    }

//    public static final Meal meal2 = new Meal(MEAL1_ID + 1, of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000);
//    public static final Meal meal3 = new Meal(MEAL1_ID + 2, of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500);
//    public static final Meal meal4 = new Meal(MEAL1_ID + 3, of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100);
//    public static final Meal meal5 = new Meal(MEAL1_ID + 4, of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 500);
//    public static final Meal meal6 = new Meal(MEAL1_ID + 5, of(2020, Month.JANUARY, 31, 13, 0), "Обед", 1000);
//    public static final Meal meal7 = new Meal(MEAL1_ID + 6, of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 510);
//    public static final Meal adminMeal1 = new Meal(ADMIN_MEAL_ID, of(2020, Month.JANUARY, 31, 14, 0), "Админ ланч", 510);
//    public static final Meal adminMeal2 = new Meal(ADMIN_MEAL_ID + 1, of(2020, Month.JANUARY, 31, 21, 0), "Админ ужин", 1500);

//    public static final List<Meal> meals = List.of(meal7, meal6, meal5, meal4, meal3, meal2, meal1);
//
//    public static Meal getUpdated() {
//        return new Meal(MEAL1_ID, meal1.getDateTime().plus(2, ChronoUnit.MINUTES), "Обновленный завтрак", 200);
}
