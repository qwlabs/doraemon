package com.qwlabs.graphql.builder;

import org.junit.jupiter.api.Test;

import static com.qwlabs.graphql.builder.Gql.compressedFormatter;
import static com.qwlabs.graphql.builder.Gql.field;
import static com.qwlabs.graphql.builder.Gql.prettifyFormatter;
import static com.qwlabs.graphql.builder.Gql.ref;
import static com.qwlabs.graphql.builder.Gql.relayNode;
import static com.qwlabs.graphql.builder.Gql.var;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class GqlTest {

    @Test
    public void should_build_query() {
        Gql gql = Gql.query("DEPARTMENTS")
                .vars(
                        var("after", "String").required(),
                        var("first", "Int").required().defaultValue(10),
                        var("condition", "MemberSearchCondition").required()
                )
                .fields(
                        field("departments")
                                .alias("query1")
                                .args(ref("after"), ref("firs"))
                                .fields(relayNode()
                                        .fields("id", "name",
                                                field("members")
                                                        .args(ref("condition"))
                                                        .fields("id", "name")
                                        )),
                        field("pageInfo").fields("hasPreviousPage", "hasNextPage"),
                        "totalCount"
                );
        String reault = gql.build(prettifyFormatter());
        assertThat(reault, is("query DEPARTMENTS($after: String!, $first: Int! = 10, $condition: MemberSearchCondition!) {\n\tquery1 : departments(after: $after, firs: $firs) {\n\t\tedges {\n\t\t\tid \n\t\t\tname \n\t\t\tmembers(condition: $condition) {\n\t\t\t\tid \n\t\t\t\tname\n\t\t\t}\n\t\t}\n\t} \n\tpageInfo {\n\t\thasPreviousPage \n\t\thasNextPage\n\t} \n\ttotalCount\n}"));
        reault = gql.build(compressedFormatter());
        assertThat(reault, is("query DEPARTMENTS($after:String!,$first:Int!=10,$condition:MemberSearchCondition!){query1:departments(after:$after,firs:$firs){edges{id name members(condition:$condition){id name}}} pageInfo{hasPreviousPage hasNextPage} totalCount}"));
    }
}