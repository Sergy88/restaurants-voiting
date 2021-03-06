package ru.fsw.revo;

import ru.fsw.revo.domain.model.MenuItem;
import ru.fsw.revo.domain.model.Restaurant;
import ru.fsw.revo.domain.to.RestaurantTo;

import java.util.*;

import static ru.fsw.revo.domain.model.AbstractBaseEntity.START_SEQ;

public class RestaurantsTestData {
    public static final TestMatcher<RestaurantTo> RESTAURANT_TO_MATCHER = TestMatcher.usingIgnoringFieldsComparator(RestaurantTo.class);
    public static final TestMatcher<Restaurant> RESTAURANT_MATCHER = TestMatcher.usingIgnoringFieldsComparator(Restaurant.class, "menu");

    public static final long REST1_ID = START_SEQ + 20;
    public static final double rating_rest1 = (3 + 8 + 8) / 3d;
    public static final int votes_rest1 = 3;
    public static final List<MenuItem> rest1_menu = new ArrayList<>() {{
        add(new MenuItem(10000L, "Беляш", 10000, new GregorianCalendar(2019,10,12).getTime()));
        add(new MenuItem(10001L, "Бигос", 12000, new GregorianCalendar(2019,10,12).getTime()));
        add(new MenuItem(10002L, "Бисквит", 5000, new GregorianCalendar(2019,10,12).getTime()));
        add(new MenuItem(10003L, "Винегрет", 7000, new GregorianCalendar(2019,10,12).getTime()));
    }};

    public static final List<MenuItem> rest1_updated_menu = new ArrayList<>() {{
        add (new MenuItem("Латте", 12000));
        add (new MenuItem("Эспрессо", 12000));
    }};

    public static final Restaurant rest1 = new Restaurant(REST1_ID, "Гавана");
    public static final Restaurant rest1_updated = new Restaurant(REST1_ID, "Гавана - кофе", rest1_updated_menu);
    public static final Restaurant rest1_with_updated_menu = new Restaurant(REST1_ID, "Гавана", rest1_updated_menu);
    public static final Restaurant rest2 = new Restaurant(REST1_ID + 1, "Триполи");
    public static final Restaurant rest3 = new Restaurant(REST1_ID + 2, "Гараж");
    public static final Restaurant rest4 = new Restaurant(REST1_ID + 3, "Пляж");

    public static final RestaurantTo rest1To = new RestaurantTo(new Restaurant(REST1_ID, "Гавана"), rating_rest1, 3, rest1_menu);
    public static final Restaurant created = new Restaurant(REST1_ID + 16, "МакКинг", rest1_menu);
    public static final RestaurantTo createdTo = new RestaurantTo(new Restaurant(REST1_ID + 16, "МакКинг"), 0d, 0, rest1_menu);
    public static final RestaurantTo rest1_updatedTo = new RestaurantTo(rest1_updated, rating_rest1, votes_rest1);
    public static final RestaurantTo rest1To_with_updated_menu = new RestaurantTo(rest1_with_updated_menu, rating_rest1, votes_rest1);

    public static Restaurant getNew() {
        return created;
    }

    public static List<Restaurant> getRestaurants() {
        return restaurants_sorted_by_name;
    }

    public static final List<Restaurant> restaurants_sorted_by_name = new ArrayList<>() {{
        add(rest1);
        add(rest3);
        add(rest4);
        add(rest2);
    }};
}
