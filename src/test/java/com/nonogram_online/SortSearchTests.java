/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nonogram_online;

import com.nonogram_online.server.SearchResponse;
import com.nonogram_online.server.SortResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 *
 * @author marton552
 */
public class SortSearchTests {

    SortResponse sortR;
    SearchResponse searchR;

    @BeforeEach
    void init() {
        sortR = new SortResponse();
        searchR = new SearchResponse("test");
    }

    @Test
    @DisplayName("Test: Default Sort response")
    public void testDefaultSortResponse() {
        assertEquals(sortR.toString(), " levels.id ASC, levels.level_name , levels.created_date , rate ");
    }

    @Test
    @DisplayName("Test: Sort response after clicked on Date")
    public void testDateSortResponse() {
        sortR.setDate();
        assertEquals(sortR.toString(), " levels.created_date ASC, levels.id ASC, levels.level_name , rate ");
    }

    @Test
    @DisplayName("Test: Sort response after clicked on Name")
    public void testNameSortResponse() {
        sortR.setName();
        assertEquals(sortR.toString(), " levels.level_name ASC, levels.id ASC, levels.created_date , rate ");
    }

    @Test
    @DisplayName("Test: Sort response after clicked on Rate")
    public void testRateSortResponse() {
        sortR.setRate();
        assertEquals(sortR.toString(), " rate ASC, levels.id ASC, levels.level_name , levels.created_date ");
    }

    @Test
    @DisplayName("Test: Sort response after double clicked on name")
    public void testNameNameSortResponse() {
        sortR.setName();
        sortR.setName();
        assertEquals(sortR.toString(), " levels.level_name DESC, levels.id ASC, levels.created_date , rate ");
    }

    @Test
    @DisplayName("Test: Default Search response")
    public void testDefaultSearchResponse() {
        searchR.setLevelName();
        assertEquals(searchR.toString(), "where levels.level_name LIKE '%test%'");
    }

    @Test
    @DisplayName("Test: Another Search response")
    public void testAnotherSearchResponse() {
        searchR.setSearch("another");
        searchR.setLevelName();
        assertEquals(searchR.toString(), "where levels.level_name LIKE '%another%'");
    }

    @Test
    @DisplayName("Test: Search response by user name")
    public void testUsernameSearchResponse() {
        searchR.setUserName();
        assertEquals(searchR.toString(), "where levels.creator_name LIKE '%test%'");
    }

    @Test
    @DisplayName("Test: Search response by level name")
    public void testLevelnameSearchResponse() {
        searchR.setLevelName();
        assertEquals(searchR.toString(), "where levels.level_name LIKE '%test%'");
    }

    @Test
    @DisplayName("Test: Search response by user and level name")
    public void testUserLevelnameSearchResponse() {
        searchR.setUserName();
        searchR.setLevelName();
        assertEquals(searchR.toString(), "where levels.level_name LIKE '%test%' and levels.creator_name LIKE '%test%'");
    }
}
