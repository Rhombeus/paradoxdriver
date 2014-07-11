package org.paradox.parser.nodes;

import org.paradox.metadata.ParadoxTable;

public class TableNode extends SQLNode {

    private ParadoxTable table;

    public TableNode(final SQLNode node, final String name) {
        super(node, name);
    }

    public ParadoxTable getTable() {
        return table;
    }

    public void setTable(ParadoxTable table) {
        this.table = table;
    }
}