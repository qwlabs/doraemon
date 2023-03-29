package com.qwlabs.tree;

import com.qwlabs.jackson.Jackson;
import lombok.Getter;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class TreeTest {
    TreeSource source1 = new TreeSource("1", null);
    TreeSource source2 = new TreeSource("2", null);
    TreeSource source3 = new TreeSource("3", null);
    TreeSource source4 = new TreeSource("4", "1");
    TreeSource source5 = new TreeSource("5", "4");
    TreeSource source6 = new TreeSource("6", "1");
    TreeSource source7 = new TreeSource("7", "5");
    TreeSource source8 = new TreeSource("8", "3");
    TreeSource source9 = new TreeSource("9", "8");
    TreeSource source10 = new TreeSource("10", "4");
    TreeSource source11 = new TreeSource("11", "9");
    TreeSource source12 = new TreeSource("12", "11");
    TreeSource source13 = new TreeSource("13", "12");

    @Test
    void should_of() {
        TreeNodes<TreeSource> nodes = Tree.of(List.of(source1, source2, source3, source4, source5, source6, source7, source8, source9, source10, source11, source12, source13),
                TreeSource::getId, TreeSource::getParentId);
        assertThat(Jackson.write(nodes).get(), is("[{\"id\":\"1\",\"parentId\":null,\"children\":[{\"id\":\"4\",\"parentId\":\"1\",\"children\":[{\"id\":\"5\",\"parentId\":\"4\",\"children\":[{\"id\":\"7\",\"parentId\":\"5\",\"children\":[]}]},{\"id\":\"10\",\"parentId\":\"4\",\"children\":[]}]},{\"id\":\"6\",\"parentId\":\"1\",\"children\":[]}]},{\"id\":\"2\",\"parentId\":null,\"children\":[]},{\"id\":\"3\",\"parentId\":null,\"children\":[{\"id\":\"8\",\"parentId\":\"3\",\"children\":[{\"id\":\"9\",\"parentId\":\"8\",\"children\":[{\"id\":\"11\",\"parentId\":\"9\",\"children\":[{\"id\":\"12\",\"parentId\":\"11\",\"children\":[{\"id\":\"13\",\"parentId\":\"12\",\"children\":[]}]}]}]}]}]}]"));
    }

    @Getter
    static class TreeSource {
        private final String id;
        private final String parentId;

        TreeSource(String id, String parentId) {
            this.id = id;
            this.parentId = parentId;
        }

        @Override
        public String toString() {
            return this.id;
        }
    }
}
