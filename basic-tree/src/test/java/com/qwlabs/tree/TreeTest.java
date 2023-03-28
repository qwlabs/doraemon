package com.qwlabs.tree;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class TreeTest {
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
    void should_tree() {
        Tree<TreeSource> tree = Tree.toTree(List.of(source1, source2, source3, source4, source5, source6, source7, source8, source9, source10, source11, source12, source13),
                TreeSource::getId, TreeSource::getParentId);

        List<TreeSource> path = tree.path(new TreeSource("13", "12"));
        assertThat(path.toString(), is("[3, 8, 9, 11, 12, 13]"));

        TreeSource treeSource = tree.findNode(s -> s.id.equals("6")).get().getSource();
        assertThat(treeSource.toString(), is("6"));

        List<String> ids = tree.mapSource(TreeSource::getId);
        assertThat(ids.toString(), is("[1, 4, 5, 7, 10, 6, 2, 3, 8, 9, 11, 12, 13]"));

        TreeNode<TreeSource> foundNode = tree.findNode(s -> s.id.equals("4")).get();
        ids = foundNode.mapSource(TreeSource::getId, true);
        assertThat(ids.toString(), is("[4, 5, 7, 10]"));
        ids = foundNode.mapSource(TreeSource::getId, false);
        assertThat(ids.toString(), is("[5, 7, 10]"));
        ids = foundNode.mapSource(TreeSource::getId);
        assertThat(ids.toString(), is("[5, 7, 10]"));
    }

    static class TreeSource {
        private final String id;
        private final String parentId;


        TreeSource(String id, String parentId) {
            this.id = id;
            this.parentId = parentId;
        }

        String getId() {
            return id;
        }

        String getParentId() {
            return parentId;
        }


        @Override
        public String toString() {
            return this.id;
        }
    }
}
