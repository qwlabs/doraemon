package com.qwlabs.panache;


import io.quarkus.panache.common.Sort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class WhereTest {

    @Test
    public void test_sort() {
        Assertions.assertEquals(true, Where.create().getSort().isEmpty());
        Assertions.assertEquals(" ORDER BY abc DESC", Where.create().sort(Sort.descending("abc")).getSort().get());
    }

    @Test
    public void test_get() {
        Assertions.assertEquals("", Where.create().get());

        Assertions.assertEquals("name=:name ORDER BY abc DESC",
                Where.create().and("name=:name", "name", "zhangsan")
                        .sort(Sort.descending("abc")).get());

    }

    @Test
    public void test_getAll() {
        Assertions.assertEquals("", Where.create().getAll());

        Assertions.assertEquals(" where name=:name",
                Where.create().and("name=:name", "name", "zhangsan")
                        .getAll());

        Assertions.assertEquals(" where name=:name ORDER BY abc DESC",
                Where.create().and("name=:name", "name", "zhangsan")
                        .sort(Sort.descending("abc")).getAll());

    }

}