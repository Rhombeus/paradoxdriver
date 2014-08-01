package com.googlecode.paradox.planner;

import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;

import com.googlecode.paradox.ParadoxConnection;
import com.googlecode.paradox.data.TableData;
import com.googlecode.paradox.metadata.ParadoxTable;
import com.googlecode.paradox.parser.nodes.SelectNode;
import com.googlecode.paradox.parser.nodes.StatementNode;
import com.googlecode.paradox.parser.nodes.TableNode;
import com.googlecode.paradox.planner.nodes.PlanTableNode;
import com.googlecode.paradox.planner.plan.Plan;
import com.googlecode.paradox.planner.plan.SelectPlan;
import com.googlecode.paradox.utils.SQLStates;

public class Planner {

	private final ParadoxConnection conn;

	public Planner(final ParadoxConnection conn) {
		this.conn = conn;
	}

	public Plan create(final StatementNode statement) throws SQLException {
		if (statement instanceof SelectNode) {
			return createSelect((SelectNode) statement);
		} else {
			throw new SQLFeatureNotSupportedException();
		}
	}

	private Plan createSelect(final SelectNode statement) throws SQLException {
		final SelectPlan plan = new SelectPlan();
		for (final TableNode table : statement.getTables()) {
			final ArrayList<ParadoxTable> paradoxTables = TableData.listTables(conn, table.getName());
			if (paradoxTables.size() != 1) {
				throw new SQLException("Table " + table.getName() + " not found.", SQLStates.INVALID_SQL);
			}
			final PlanTableNode node = new PlanTableNode();
			node.setTable(paradoxTables.get(0));
			if (!table.getName().equals(table.getAlias())) {
				node.setAlias(table.getAlias());
			}
			plan.addTable(node);
		}
		return plan;
	}
}
