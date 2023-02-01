package com.qwlabs.graphql.builder;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static com.qwlabs.graphql.builder.Gql.compressedFormatter;
import static com.qwlabs.graphql.builder.Gql.field;
import static com.qwlabs.graphql.builder.Gql.prettifyFormatter;
import static com.qwlabs.graphql.builder.Gql.ref;
import static com.qwlabs.graphql.builder.Gql.var;

public class GqlTest {

    @Test
    public void should_build_department_query() {
        Gql gql = Gql.query("DEPARTMENTS").vars(var("after", "String").required(), var("first", "Int").required().defaultValue(10), var("condition", "MemberSearchCondition").required()).fields(field("departments").alias("query1").args(ref("after"), ref("first")).fields(field("edges").fields(field("node").fields("id", "name", field("members").args(ref("condition")).fields("id", "name")))), field("pageInfo").fields("hasPreviousPage", "hasNextPage"), "totalCount");
        String reault = gql.build(prettifyFormatter(), Map.of("after", "1", "first", 2));
//        assertThat(reault, is("{\"operationName\":\"DEPARTMENTS\",\"query\":\"query DEPARTMENTS($after: String!, $first: Int! = 10, $condition: MemberSearchCondition!) {\n\tquery1 : departments(after: $after, first: $first) {\n\t\tedges {\n\t\t\tnode {\n\t\t\t\tid \n\t\t\t\tname \n\t\t\t\tmembers(condition: $condition) {\n\t\t\t\t\tid \n\t\t\t\t\tname\n\t\t\t\t}\n\t\t\t}\n\t\t}\n\t} \n\tpageInfo {\n\t\thasPreviousPage \n\t\thasNextPage\n\t} \n\ttotalCount\n}\",\"variables\":{\"after\":\"1\",\"first\":2}}"));
        reault = gql.build(compressedFormatter(), Map.of("after", "1", "first", 2));
//        assertThat(reault, is("{\"operationName\":\"DEPARTMENTS\",\"query\":\"query DEPARTMENTS($after: String!, $first: Int! = 10, $condition: MemberSearchCondition!) {\n\tquery1 : departments(after: $after, first: $first) {\n\t\tedges {\n\t\t\tnode {\n\t\t\t\tid \n\t\t\t\tname \n\t\t\t\tmembers(condition: $condition) {\n\t\t\t\t\tid \n\t\t\t\t\tname\n\t\t\t\t}\n\t\t\t}\n\t\t}\n\t} \n\tpageInfo {\n\t\thasPreviousPage \n\t\thasNextPage\n\t} \n\ttotalCount\n}\",\"variables\":{\"after\":\"1\",\"first\":2}}"));
    }
}